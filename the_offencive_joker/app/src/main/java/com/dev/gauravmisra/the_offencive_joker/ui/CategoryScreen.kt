package com.dev.gauravmisra.the_offencive_joker.ui

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp


@Composable
fun CategoryScreen(
    categories: List<String>,
    onSelect: (String) -> Unit
) {
    Column(
        modifier = Modifier.fillMaxSize().padding(24.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        categories.forEach { category ->
            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .clickable { onSelect(category) },
                colors = CardDefaults.cardColors(containerColor = Color(0xFF1F1F1F))
            ) {
                Text(
                    text = category,
                    modifier = Modifier.padding(24.dp),
                    fontSize = 22.sp,
                    color = Color.Yellow
                )
            }
        }
    }
}
