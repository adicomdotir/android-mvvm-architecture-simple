package ir.adicom.training.ui.home

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalDrawerSheet
import androidx.compose.material3.ModalNavigationDrawer
import androidx.compose.material3.NavigationDrawerItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ir.adicom.training.ui.Screen
import kotlinx.coroutines.launch

@Composable
fun HomeScreen(navController: NavHostController, viewModel: HomeViewModel = hiltViewModel()) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val state = viewModel.uiState.collectAsState()

    ModalNavigationDrawer(
        drawerContent = {
            ModalDrawerSheet {
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .verticalScroll(rememberScrollState())
                ) {
                    Spacer(Modifier.height(12.dp))
                    Text(
                        "Drawer Title",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleLarge
                    )
                    HorizontalDivider()

                    Text(
                        "Main Section",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                    NavigationDrawerItem(
                        label = { Text("Categories") },
                        selected = false,
                        icon = { Icon(Icons.Outlined.Info, contentDescription = null) },
                        onClick = {
                            navController.navigate(Screen.CategoryList.route)
                        }
                    )
                    NavigationDrawerItem(
                        label = { Text("Expenses") },
                        selected = false,
                        icon = { Icon(Icons.Outlined.Info, contentDescription = null) },
                        onClick = {
                            navController.navigate(Screen.ExpenseList.route)
                        }
                    )

                    HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

                    Text(
                        "Section 2",
                        modifier = Modifier.padding(16.dp),
                        style = MaterialTheme.typography.titleMedium
                    )
                    NavigationDrawerItem(
                        label = { Text("Settings") },
                        selected = false,
                        icon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
                        badge = { Text("20") }, // Placeholder
                        onClick = { /* Handle click */ }
                    )
                    NavigationDrawerItem(
                        label = { Text("Help and feedback") },
                        selected = false,
                        icon = {
                            Icon(
                                Icons.AutoMirrored.Outlined.List,
                                contentDescription = null
                            )
                        },
                        onClick = { /* Handle click */ },
                    )
                    Spacer(Modifier.height(12.dp))
                }
            }
        },
        drawerState = drawerState
    ) {
        Scaffold(
            topBar = {
                AppBar(
                    title = "Home",
                    onClick = {
                        scope.launch {
                            if (drawerState.isClosed) {
                                drawerState.open()
                            } else {
                                drawerState.close()
                            }
                        }
                    }
                )
            },
        ) { paddingValues ->
            // Main content of the screen
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
            ) {
                if (state.value is HomeUiState.Loading) {
                    CircularProgressIndicator()
                }

                if (state.value is HomeUiState.Error) {
                    Text("Something Get Wrong")
                }

                if (state.value is HomeUiState.Success) {
                    Column {
                        Text("Expenses Size: ${(state.value as HomeUiState.Success).data.expenses.size}")
                        Text("Categories Size: ${(state.value as HomeUiState.Success).data.categories.size}")
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
    onClick: () -> Unit
) {
    TopAppBar(
        title = {
            Text(
                text = title,
                style = MaterialTheme.typography.titleMedium
            )
        },
        navigationIcon = {
            IconButton(onClick = onClick) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }
        }
    )
}
