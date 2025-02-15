package ir.adicom.training.ui

import ir.adicom.training.ui.add_cateogry.AddCategoryScreen
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ir.adicom.training.ui.add_expense.AddExpenseScreen
import ir.adicom.training.ui.cateogry_list.CategoryListScreen
import ir.adicom.training.ui.dataitemtypetest.DataItemTypeTestScreen
import ir.adicom.training.ui.expense_list.ExpenseListScreen
import ir.adicom.training.ui.home.HomeScreen

@Composable
fun MainNavigation() {
    val navController = rememberNavController()

    NavHost(navController = navController, startDestination = Screen.Home.route) {
        composable("main") { DataItemTypeTestScreen(modifier = Modifier.padding(16.dp)) }
        // TODO: Add more destinations
        composable(Screen.Home.route) { HomeScreen(navController = navController) }
        composable(Screen.AddCategory.route) { AddCategoryScreen(navController = navController) }
        composable(Screen.CategoryList.route) { CategoryListScreen(navController = navController) }
        composable(Screen.AddExpense.route) { AddExpenseScreen(navController = navController) }
        composable(Screen.ExpenseList.route) { ExpenseListScreen(navController = navController) }
    }
}

sealed class Screen(val route: String) {
    data object Home: Screen("home_screen")
    data object AddCategory: Screen("add_category_screen")
    data object CategoryList: Screen("category_list_screen")
    data object AddExpense: Screen("add_expense_screen")
    data object ExpenseList: Screen("expense_list_screen")

}