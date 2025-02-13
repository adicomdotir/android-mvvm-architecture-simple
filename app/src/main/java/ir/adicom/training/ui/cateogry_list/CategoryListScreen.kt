package ir.adicom.training.ui.cateogry_list

import DeleteDialog
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
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
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import ir.adicom.training.data.local.database.Category
import ir.adicom.training.ui.Screen

const val TAG = "tag"

@Composable
fun CategoryListScreen(
    navController: NavController,
    modifier: Modifier = Modifier,
    viewModel: CategoryListViewModel = hiltViewModel()
) {
    val deleteDialog: MutableState<Category?> = remember { mutableStateOf(null) }
    val state = viewModel.uiState.collectAsState().value

    if (deleteDialog.value != null) {
        DeleteDialog(
            onDismissRequest = { deleteDialog.value = null },
            onConfirmation = {
                viewModel.delete(deleteDialog.value!!)
                deleteDialog.value = null
            },
            dialogTitle = "Delete Category",
            dialogText = "Do you want delete this category?",
        )
    }

    Scaffold(
        topBar = {
            AppBar(
                title = "List Category",
                onAddClick = {
                    navController.navigate(Screen.AddCategory.route)
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
            if (state is CategoryListUiState.Loading) {
                CircularProgressIndicator()
            }

            if (state is CategoryListUiState.Error) {
                Text(state.throwable.message.toString())
            }

            if (state is CategoryListUiState.Success) {
                LazyColumn(
                    modifier = Modifier
                        .fillMaxSize()
                        .padding(16.dp)
                ) {
                    items(state.data) { item ->
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(24.dp)
                                    .background(Color(item.color))
                            )
                            Spacer(Modifier.width(24.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.SpaceBetween
                            ) {
                                Text(item.name)
                                IconButton(
                                    onClick = {
                                        deleteDialog.value = item
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