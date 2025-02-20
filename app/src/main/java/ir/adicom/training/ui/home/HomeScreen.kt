package ir.adicom.training.ui.home

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.List
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material.icons.outlined.Info
import androidx.compose.material.icons.outlined.Settings
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.DrawerState
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
import androidx.compose.material3.TextButton
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import ir.adicom.training.ui.Screen
import ir.adicom.training.utils.formatDateRelativelyLegacy
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat

@Composable
fun HomeScreen(navController: NavHostController, viewModel: HomeViewModel = hiltViewModel()) {
    val drawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    val scope = rememberCoroutineScope()
    val uiState = viewModel.uiState.collectAsState()

    ModalNavigationDrawer(
        drawerContent = { HomeDrawer(navController, drawerState, scope) },
        drawerState = drawerState
    ) {
        Scaffold(topBar = { HomeAppBar(drawerState, scope) }) { paddingValues ->
            HomeContent(uiState.value, navController, paddingValues)
        }
    }
}

@Composable
private fun HomeDrawer(
    navController: NavHostController,
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    ModalDrawerSheet {
        Column(modifier = Modifier.padding(horizontal = 16.dp)) {
            Spacer(Modifier.height(12.dp))
            Text("Drawer Title", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleLarge)
            HorizontalDivider()

            Text("Main Section", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
            NavigationDrawerItem(
                label = { Text("Categories") },
                selected = false,
                icon = { Icon(Icons.Outlined.Info, contentDescription = null) },
                onClick = {
                    navController.navigate(Screen.CategoryList.route)
                    scope.launch { drawerState.close() }
                }
            )
            NavigationDrawerItem(
                label = { Text("Expenses") },
                selected = false,
                icon = { Icon(Icons.Outlined.Info, contentDescription = null) },
                onClick = {
                    navController.navigate(Screen.ExpenseList.route)
                    scope.launch { drawerState.close() }
                }
            )

            HorizontalDivider(modifier = Modifier.padding(vertical = 8.dp))

            Text("Section 2", modifier = Modifier.padding(16.dp), style = MaterialTheme.typography.titleMedium)
            NavigationDrawerItem(
                label = { Text("Settings") },
                selected = false,
                icon = { Icon(Icons.Outlined.Settings, contentDescription = null) },
                badge = { Text("20") },
                onClick = { /* Handle click */ }
            )
            NavigationDrawerItem(
                label = { Text("Help and feedback") },
                selected = false,
                icon = { Icon(Icons.AutoMirrored.Outlined.List, contentDescription = null) },
                onClick = { /* Handle click */ },
            )
            Spacer(Modifier.height(12.dp))
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun HomeAppBar(drawerState: DrawerState, scope: CoroutineScope) {
    TopAppBar(
        title = { Text("Home", style = MaterialTheme.typography.titleMedium) },
        navigationIcon = {
            IconButton(onClick = { scope.launch { if (drawerState.isClosed) drawerState.open() else drawerState.close() } }) {
                Icon(Icons.Default.Menu, contentDescription = "Menu")
            }
        }
    )
}

@Composable
private fun HomeContent(uiState: HomeUiState, navController: NavHostController, paddingValues: androidx.compose.foundation.layout.PaddingValues) {
    Box(modifier = Modifier.fillMaxSize().padding(paddingValues)) {
        when (uiState) {
            is HomeUiState.Loading -> CircularProgressIndicator(modifier = Modifier.align(Alignment.Center))
            is HomeUiState.Error -> Text("Something Get Wrong", modifier = Modifier.align(Alignment.Center))
            is HomeUiState.Success -> HomeSuccessContent(uiState.data, navController)
        }
    }
}

@Composable
private fun HomeSuccessContent(model: HomeModel, navController: NavHostController) {
    Column(modifier = Modifier.padding(16.dp)) {
        BalanceCard(model, navController)
        Spacer(Modifier.height(16.dp))
        RecentTransactionsCard(model, navController)
    }
}

@Composable
private fun BalanceCard(model: HomeModel, navController: NavHostController) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.Start) {
            Row {
                Text("Current Balance:")
                Text(" $${model.balance}", color = Color.Red)
            }
            Spacer(Modifier.height(8.dp))
            Text("Spent this month: $${model.monthExpense} / $1000 (Budget)")
            Spacer(Modifier.height(8.dp))
            TextButton(onClick = { navController.navigate(Screen.AddExpense.route + "/-1") }) {
                Text("Add Expense")
            }
        }
    }
}

@Composable
private fun RecentTransactionsCard(model: HomeModel, navController: NavHostController) {
    Card(modifier = Modifier.fillMaxWidth()) {
        Column(modifier = Modifier.padding(16.dp), horizontalAlignment = Alignment.Start) {
            Text("Recent Transactions:")
            Spacer(Modifier.height(8.dp))

            LazyColumn(modifier = Modifier.fillMaxWidth()) {
                items(model.expenses.take(3)) { item ->
                    val sdf = SimpleDateFormat("yyyy-MM-dd")
                    val currentDateAndTime = sdf.format(item.dateTime)
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.SpaceBetween) {
                        Text(formatDateRelativelyLegacy(currentDateAndTime))
                        Text("-$${item.price}")
                    }
                    Text(item.title)
                    Spacer(Modifier.height(8.dp))
                }
            }
            TextButton(onClick = { navController.navigate(Screen.ExpenseList.route) }) {
                Text("More")
            }
        }
    }
}