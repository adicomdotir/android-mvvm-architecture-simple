package ir.adicom.training.ui.add_expense

import android.widget.Toast
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ir.adicom.training.data.local.database.Category
import java.text.SimpleDateFormat
import java.util.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddExpenseScreen(
    modifier: Modifier = Modifier,
    viewModel: AddExpenseViewModel = hiltViewModel(),
    navController: NavController,
    id: Int
) {
    val context = LocalContext.current
    var title by remember { mutableStateOf("") }
    var price by remember { mutableStateOf("") }
    var category by remember { mutableIntStateOf(-1) }
    val state = viewModel.state.collectAsState()
    val categories = viewModel.categories.collectAsState()

    if (id != -1) {
        LaunchedEffect(Unit) {
            viewModel.getExpense(id!!)
        }
    }

    if (state.value.expense != null) {
        title = state.value.expense!!.title
        price = state.value.expense!!.price.toString()
        category = state.value.expense!!.categoryId
    }

    if (state.value.success) {
        navController.popBackStack()
    }

    LaunchedEffect(Unit) {
        viewModel.toastMessage.collect { message ->
            Toast.makeText(context, message, Toast.LENGTH_SHORT).show()
        }
    }

    Scaffold(
        topBar = {
            AppBar(
                title = if (id == -1) "Add Expense" else "Edit Expense",
                onBackClick = {
                    navController.popBackStack()
                },
                onSaveClick = {
                    if (title.isNotEmpty() && price.isNotEmpty() && category != -1) {
                        if (id == -1) {
                            viewModel.addExpense(title = title, price = price, id = category)
                        } else {
                            viewModel.updateExpense(
                                state.value.expense!!.copy(
                                    title = title,
                                    price = price.toInt(),
                                    categoryId = category
                                )
                            )
                        }
                    } else {
                        Toast.makeText(context, "message", Toast.LENGTH_SHORT).show()
                    }
                }
            )
        }
    ) { paddingValues ->
        // Main content of the screen
        Box(
            modifier = Modifier
                .fillMaxSize()
                .padding(paddingValues)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(16.dp)
            ) {
                // Title Text Field
                OutlinedTextField(
                    value = title,
                    onValueChange = { title = it },
                    label = { Text("Expense Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Price Text Field
                OutlinedTextField(
                    value = price,
                    onValueChange = { price = it },
                    label = { Text("Expense Price") },
                    modifier = Modifier.fillMaxWidth(),
                    keyboardOptions = KeyboardOptions(
                        keyboardType = KeyboardType.Number
                    )
                )

                Spacer(modifier = Modifier.height(16.dp))


                ExposedDropdownMenuSample(
                    categories,
                    onSelect = { id ->
                        category = id
                    },
                    defaultId = id
                )

                Spacer(modifier = Modifier.height(16.dp))

                val sdf = SimpleDateFormat("dd-MM-yyyy")
                val currentDateAndTime = sdf.format(Date())
                Text(
                    text = currentDateAndTime,
                    modifier = Modifier.fillMaxWidth(),
                    textAlign = TextAlign.Center,
                    color = Color.Black,
                    fontWeight = FontWeight.Bold,
                    fontSize = 25.sp,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ExposedDropdownMenuSample(
    categories: State<List<Category>>,
    onSelect: (Int) -> Unit,
    defaultId: Int
) {
    val options: List<Category> = categories.value
    var expanded by remember { mutableStateOf(false) }
    var selectedOptionText by remember { mutableStateOf("") }

    if (defaultId != -1) {
        options.forEach {
            if (it.uid == defaultId) {
                selectedOptionText = it.name
            }
        }
    }

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        OutlinedTextField(
            value = selectedOptionText,
            onValueChange = {},
            readOnly = true,
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            modifier = Modifier.menuAnchor(),
            colors = ExposedDropdownMenuDefaults.textFieldColors()
        )

        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option.name, style = MaterialTheme.typography.bodyLarge) },
                    onClick = {
                        selectedOptionText = option.name
                        expanded = false
                        onSelect(option.uid)
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String,
    onBackClick: () -> Unit,
    onSaveClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
        },
        navigationIcon = {
            IconButton(onClick = onBackClick) {
                Icon(
                    imageVector = Icons.Default.ArrowBack,
                    contentDescription = "Back"
                )
            }
        },
        actions = {
            IconButton(onClick = onSaveClick) {
                Icon(
                    imageVector = Icons.Default.Done,
                    contentDescription = "Save"
                )
            }
        },
    )
}
