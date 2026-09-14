package com.example.listycity3

import android.R.attr.name
import android.R.attr.onClick
import android.app.ProgressDialog.show
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.listycity3.ui.theme.ListyCity3Theme
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Button
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.material3.FloatingActionButton
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.clickable
import androidx.compose.material3.MaterialTheme
import kotlinx.coroutines.selects.select
import androidx.compose.ui.text.font.FontWeight

// ima make it so that you have to click the city you wanna update and then itll prompt you to do it
@Composable
// onAddCity: (City) -> Unit means CityListScreen gets a func, then that func takes that City obj and does something
fun CityListScreen(
    cities: List<City>,
    onAddCity: (City) -> Unit,
    onUpdateCity: (City, City) -> Unit,
    modifier: Modifier = Modifier
) {
    // thesere the updated city and province names
    // remembers gonna keep the vals available while app is running
    // mutablestateof tells compose to update UI when val changes
    var newCityName by remember { mutableStateOf("") }
    var newProvinceName by remember { mutableStateOf("") }
    var showAddCityFields by remember { mutableStateOf(false) }
    var selectedCity by remember { mutableStateOf<City?>(null) }
    var showEditCityFields by remember { mutableStateOf(false) }
    var editCityName by remember { mutableStateOf("") }
    var editProvinceName by remember { mutableStateOf("") }
    Column(modifier = modifier.fillMaxSize()) {
        // got to press this button to show the add city fields
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.End
        ) {
            FloatingActionButton(
                modifier = Modifier.padding(16.dp),
                onClick = {
                    showAddCityFields = !showAddCityFields
                }
            ) {
                Text("+")
            }
        }
        // add city fields
        if (showAddCityFields) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = newCityName,
                    onValueChange = { newCityName = it },
                    label = { Text("City") },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedTextField(
                    value = newProvinceName,
                    onValueChange = { newProvinceName = it },
                    label = { Text("Province") },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))
                Button(
                    modifier = Modifier.padding(vertical = 12.dp),
                    onClick = {
                        if (newCityName.isNotBlank() && newProvinceName.isNotBlank()) {
                            onAddCity(
                                City(
                                    name = newCityName,
                                    province = newProvinceName
                                )
                            )
                            newCityName = ""
                            newProvinceName = ""
                            showAddCityFields = false
                        }
                    }
                ) {
                    Text("Add City")
                }
            }
        }
        // edit city fields, a majority of the code is ripped from above as its basically near the same thing
        if (showEditCityFields) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                OutlinedTextField(
                    value = editCityName,
                    onValueChange = { editCityName = it },
                    label = { Text("Updated City") },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                OutlinedTextField(
                    value = editProvinceName,
                    onValueChange = { editProvinceName = it },
                    label = { Text("Updated Province") },
                    modifier = Modifier.weight(1f)
                )

                Spacer(modifier = Modifier.width(8.dp))

                Button(
                    modifier = Modifier.padding(vertical = 12.dp),
                    onClick = {
                        val previousCity = selectedCity
                        if (previousCity != null && editCityName.isNotBlank() && editProvinceName.isNotBlank()
                        ) {
                            onUpdateCity(
                                previousCity,
                                City(
                                    name = editCityName,
                                    province = editProvinceName
                                )
                            )
                            selectedCity = null
                            showEditCityFields = false
                        }
                    }
                ) {
                    Text("Update City")
                }
            }
        }
        LazyColumn(modifier = Modifier.fillMaxSize()) {
            itemsIndexed(cities) { index, city ->
                CityRow(
                    city = city,
                    isSelected = city == selectedCity,
                    onClick = {
                        if (selectedCity == city) {
                            selectedCity = null
                            showEditCityFields = false
                        } else {
                            selectedCity = city
                            editCityName = city.name
                            editProvinceName = city.province
                            showEditCityFields = true
                            showAddCityFields = false // make sure we aint showing it twice
                        }
                    }
                )
                if (index < cities.lastIndex) {
                    HorizontalDivider()
                }
            }
        }
    }
}

@Composable
fun CityRow(city: City, isSelected: Boolean, onClick: () -> Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable(onClick = onClick)
            .padding(horizontal = 20.dp, vertical = 16.dp)
    ) {
        Text(
            text = city.name,
            fontSize = 30.sp,
            // bold and change colour
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f)
        )

        Text(
            text = city.province,
            fontSize = 30.sp,
            // bold and change colour
            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
            color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.onBackground,
            modifier = Modifier.weight(1f)
        )
    }
}

@Preview(showBackground = true)
@Composable
fun CityListScreenPreview() {
    ListyCity3Theme {
        CityListScreen(
            cities = listOf(
                City("Edmonton", "AB"),
                City("Vancouver", "BC"),
                City("Calgary", "AB")
            ),
            onAddCity = {},
            onUpdateCity = {_, _ ->}
        )
    }
}