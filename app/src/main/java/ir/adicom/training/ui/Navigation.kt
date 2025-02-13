/*
 * Copyright (C) 2022 The Android Open Source Project
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package ir.adicom.training.ui

import ir.adicom.training.ui.add_cateogry.AddCategoryScreen
import androidx.compose.foundation.layout.padding
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import ir.adicom.training.ui.cateogry_list.CategoryListScreen
import ir.adicom.training.ui.dataitemtypetest.DataItemTypeTestScreen
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
    }
}

sealed class Screen(val route: String) {
    data object Home: Screen("home_screen")
    data object AddCategory: Screen("add_category_screen")
    data object CategoryList: Screen("category_list_screen")
}