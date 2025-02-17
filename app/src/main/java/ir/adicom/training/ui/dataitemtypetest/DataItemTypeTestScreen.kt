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

package ir.adicom.training.ui.dataitemtypetest

import android.util.Log
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.Button
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import ir.adicom.training.TAG
import ir.adicom.training.data.local.database.DataItemType


@Composable
fun DataItemTypeTestScreen(modifier: Modifier = Modifier, viewModel: DataItemTypeTestViewModel = hiltViewModel()) {
    Log.e(TAG, "DataItemTypeTestScreen: ")

    val items by viewModel.uiState.collectAsStateWithLifecycle()
    if (items is DataItemTypeTestUiState.Success) {
        DataItemTypeTestScreen(
            items = (items as DataItemTypeTestUiState.Success).data,
            onSave = viewModel::addDataItemTypeTest,
            modifier = modifier,
            onDelete = viewModel::deleteItem
        )
    }
    if (items is DataItemTypeTestUiState.Error) {
        Log.e(TAG, "DataItemTypeTestScreen: ${(items as DataItemTypeTestUiState.Error).throwable.message}")
        Text(text = "Error")
    }
    if (items is DataItemTypeTestUiState.Loading) {
        Text(text = "Loading")
    }
}

@Composable
internal fun DataItemTypeTestScreen(
    items: List<DataItemType>,
    onSave: (name: String) -> Unit,
    modifier: Modifier = Modifier,
    onDelete: (item: DataItemType) -> Unit
) {
    Log.e(TAG, "DataItemTypeTestScreen: ")
    Column(modifier = modifier.fillMaxSize()) {
        var nameDataItemTypeTest by remember { mutableStateOf("Compose") }
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 24.dp),
            horizontalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            TextField(
                value = nameDataItemTypeTest,
                onValueChange = { nameDataItemTypeTest = it }
            )

            Button(modifier = Modifier.width(96.dp), onClick = { onSave(nameDataItemTypeTest) }) {
                Text("Save")
            }
        }
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
        ) {
            items(items) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text("Saved item: ${it.uid}.${it.name}")
                    IconButton(onClick = {
                        onDelete(it)
                    }) {
                        Icon(imageVector = Icons.Default.Delete, contentDescription = "delete_icons", tint = Color.Red)
                    }
                }
            }
        }
    }
}

// Previews

//@Preview(showBackground = true)
//@Composable
//private fun DefaultPreview() {
//    MyApplicationTheme {
//        DataItemTypeTestScreen(listOf("Compose", "Room", "Kotlin"), onSave = {})
//    }
//}
//
//@Preview(showBackground = true, widthDp = 480)
//@Composable
//private fun PortraitPreview() {
//    MyApplicationTheme {
//        DataItemTypeTestScreen(listOf("Compose", "Room", "Kotlin"), onSave = {})
//    }
//}
