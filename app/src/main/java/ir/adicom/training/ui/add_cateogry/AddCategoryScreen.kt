package ir.adicom.training.ui.add_cateogry

import android.widget.Toast
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowBack
import androidx.compose.material.icons.filled.Done
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun AddCategoryScreen(
    modifier: Modifier = Modifier,
    viewModel: AddCategoryTestViewModel = hiltViewModel(),
    navController: NavController
) {
    var title by remember { mutableStateOf("") }
    var selectedColor by remember { mutableStateOf(Color.White) }
    var showColorDialog by remember { mutableStateOf(false) }
    val state = viewModel.state.collectAsState()

    if (state.value.success) {
        navController.popBackStack()
    }

    if (state.value.validate) {
        Toast.makeText(LocalContext.current, "Plz enter title for category", Toast.LENGTH_SHORT).show()

    }

    val colors = listOf(
        Color(0xFF6C5B7B), // Deep Lavender
        Color(0xFF355C7D), // Ocean Blue
        Color(0xFFC06C84), // Soft Pink
        Color(0xFFF67280), // Coral
        Color(0xFFF8B195), // Peach
        Color(0xFFA8E6CE), // Mint Green
        Color(0xFFDCEDC2), // Pastel Green
        Color(0xFFFFD3B5), // Light Coral
        Color(0xFFFFAAA6), // Blush Pink
        Color(0xFFFF8C94), // Watermelon
        Color(0xFF6A4C93), // Rich Purple
        Color(0xFFFF6F61), // Tangerine
        Color(0xFF4A90E2), // Sky Blue
        Color(0xFF50E3C2), // Aqua
        Color(0xFFB8E986), // Lime Green
        Color(0xFFF5A623), // Golden Yellow
        Color(0xFFD0021B), // Bold Red
        Color(0xFF7ED321), // Fresh Green
        Color(0xFFBD10E0), // Electric Purple
        Color(0xFF4A4A4A)  // Charcoal Gray
    )

    Scaffold(
        topBar = {
            AppBar(
                title = "Add Category",
                onBackClick = {
                    navController.popBackStack()
                },
                onSaveClick = {
                    viewModel.addCategory(title = title, color = selectedColor.toArgb())
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
                    label = { Text("Category Title") },
                    modifier = Modifier.fillMaxWidth()
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Color Selection Button
                Button(
                    onClick = { showColorDialog = true },
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Text("Select Color")
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Display Selected Color
                Box(
                    modifier = Modifier
                        .size(100.dp)
                        .background(selectedColor)
                )

                // Color Selection Dialog
                if (showColorDialog) {
                    ColorSelectionDialog(
                        colors = colors,
                        onColorSelected = { color ->
                            selectedColor = color
                            showColorDialog = false
                        },
                        onDismiss = { showColorDialog = false }
                    )
                }
            }
        }
    }
}

@Composable
fun ColorSelectionDialog(
    colors: List<Color>,
    onColorSelected: (Color) -> Unit,
    onDismiss: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("Select a color") },
        text = {
            LazyColumn {
                items(colors) { color ->
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable { onColorSelected(color) }
                            .padding(16.dp)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .background(color)
                        )
                        Spacer(modifier = Modifier.width(16.dp))
                        Text(text = color.toString())
                    }
                }
            }
        },
        confirmButton = {
            Button(onClick = onDismiss) {
                Text("OK")
            }
        }
    )
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