package ir.adicom.training.ui.expense_list

import DeleteExpenseDialog
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.MutableState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ir.adicom.training.data.local.database.Expense
import ir.adicom.training.ui.Screen
import java.text.SimpleDateFormat

@Composable
fun ExpenseListScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: ExpenseListViewModel = hiltViewModel()
) {
    val deleteDialog: MutableState<Int> = remember { mutableIntStateOf(-1) }
    val state = viewModel.uiState.collectAsState().value

    if (deleteDialog.value != -1) {
        DeleteExpenseDialog(
            onDismissRequest = { deleteDialog.value = -1 },
            onConfirmation = {
                viewModel.delete(deleteDialog.value)
                deleteDialog.value = -1
            },
            dialogTitle = "Delete Expense",
            dialogText = "Do you want delete this expense?",
        )
    }

    Scaffold(
        topBar = {
            AppBar(
                title = "List Category",
                onAddClick = {
                    navController.navigate(Screen.AddExpense.route + "/-1")
                },
                onBackClick = {
                    navController.popBackStack()
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
            if (state is ExpenseListUiState.Loading) {
                CircularProgressIndicator()
            }

            if (state is ExpenseListUiState.Error) {
                Text(state.throwable.message.toString())
            }

            if (state is ExpenseListUiState.Success) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    items(state.data) { item ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clickable {
                                    navController.navigate(Screen.AddExpense.route + "/${item.uid}")
                                },
                        ) {
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                val sdf = SimpleDateFormat("dd-MM-yyyy")
                                val currentDateAndTime = sdf.format(item.dateTime)
                                Column {
                                    Text(item.title)
                                    Text(item.price.toString())
                                    Text(item.categoryName)
                                    Text(currentDateAndTime)
                                }
                                IconButton(
                                    onClick = {
                                        deleteDialog.value = item.uid
                                    }
                                ) {
                                    Icon(Icons.Default.Delete, "Delete", tint = Color.Red)
                                }
                            }
                        }
                    }
                }
            }

        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AppBar(
    title: String,
    onAddClick: () -> Unit,
    onBackClick: () -> Unit
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
            IconButton(onClick = onAddClick) {
                Icon(
                    imageVector = Icons.Default.Add,
                    contentDescription = "Add"
                )
            }
        },
    )
}