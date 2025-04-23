package com.example.fletes.ui.trucksDetails

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.fletes.R
import com.example.fletes.ui.theme.FletesTheme

@Composable
fun TrucksDetailsScreen(
    modifier: Modifier = Modifier,
    content: @Composable (paddingValues: PaddingValues) -> Unit = {},
    onClickFab: () -> Unit = {}
) {
    Scaffold(
        modifier = modifier.padding(),
        topBar = {
            TrucksTopAppBar()
        },
        floatingActionButton = {
            TruckFab(
                onClick = onClickFab,
                icon = painterResource(R.drawable.icl_shipping_24),
                modifier = modifier
            )
        }
    ) {
        content(it)
    }
}

@Composable
fun TruckFab(
    icon: Painter,
    onClick: () -> Unit = {},
    modifier: Modifier = Modifier
) {
    FloatingActionButton(
        modifier = modifier,
        onClick = onClick,
    ) {
        Icon(
            painter = icon,
            contentDescription = "Truck Icon"
        )
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrucksTopAppBar(modifier: Modifier = Modifier) {
    CenterAlignedTopAppBar(
        title = {
            Text("Details Trucks Available")
        },
        modifier = modifier,
        navigationIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_back_24),
                contentDescription = "Back Icon"
            )
        },
        actions = { },
    )
}

@Preview(showBackground = true)
@Composable
fun TrucksDetailsScreenPrev(modifier: Modifier = Modifier) {
    FletesTheme {
        TrucksDetailsScreen(
            content = {},
            modifier = modifier
        )
    }
}