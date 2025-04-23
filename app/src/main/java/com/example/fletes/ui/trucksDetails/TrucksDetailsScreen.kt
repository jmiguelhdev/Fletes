package com.example.fletes.ui.trucksDetails

import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.imePadding
import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.fletes.R
import com.example.fletes.ui.destino.DispatchViewModel
import com.example.fletes.ui.theme.FletesTheme
import org.koin.androidx.compose.koinViewModel

@Composable
fun TrucksDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: DispatchViewModel = koinViewModel(),
    content: @Composable (paddingValues: PaddingValues) -> Unit = {},
    onClickFab: () -> Unit = {}
) {

    val activeDispatchCount by viewModel.activeDispatchCount.collectAsState(0)
    val activeDispatch by viewModel.activeDispatch.collectAsState(emptyList())
    Scaffold(
        modifier = modifier.imePadding(),
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
fun TrucksTopAppBar(
    modifier: Modifier = Modifier,
    onClick: () -> Unit = {}
) {
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
        actions = {
            IconButton(
                onClick = onClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_more_vert_24),
                    contentDescription = "More Icon"
                )
            }

        },
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