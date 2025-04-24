package com.example.fletes.ui.trucksDetails

import android.util.Log
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
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
    onClickFab: () -> Unit = {}
) {
    val uiState by viewModel.uiState.collectAsState()
    val activeDispatchCount by viewModel.activeDispatchCount.collectAsState(0)
    val activeDispatch by viewModel.activeDispatch.collectAsState(emptyList())

    Log.d("TrucksDetailsScreen", "activeDispatch: $activeDispatch")

    Scaffold(
        modifier = modifier.imePadding(),
        topBar = {
            TrucksTopAppBar(count = activeDispatchCount)
        },
        floatingActionButton = {
            TruckFab(
                onClick = onClickFab,
                icon = painterResource(R.drawable.icl_shipping_24),
                modifier = Modifier
            )
        }
    ) {innerPadding ->
       ContentTrucksDetailsScreen(
           modifier = Modifier
               .fillMaxSize()
               .padding(innerPadding),
           activeDispatch = activeDispatch,
           showDeleteDialog = uiState.showDeleteDialog,
           onEditClick = {
                viewModel.editDispatch(it)
               // TODO: completar logica en vm y domain 
           },
           onDeleteClick = {
               viewModel.showDeleteDialog()
           },
           onDismissRequest = viewModel::hideDeleteDialog,
           onConfirm = viewModel::deleteDetino
       )

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
    onClick: () -> Unit = {},
    count: Int = 0
) {
    CenterAlignedTopAppBar(
        title = {
            Text("Prepare Dispatch $count")
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
            modifier = modifier,
        )
    }
}