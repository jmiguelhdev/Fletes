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
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.painter.Painter
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.fletes.R
import com.example.fletes.ui.dispatch.DispatchViewModel
import com.example.fletes.ui.theme.FletesTheme
import kotlinx.coroutines.launch
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
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.showSnackbar) {
        if (uiState.showSnackbar) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = uiState.snackbarMessage,
                    withDismissAction = true
                )
                viewModel.snackbarShown()
            }
        }
    }
    Log.d("TrucksDetailsScreen", "activeDispatch: $activeDispatch")

    Scaffold(
        modifier = modifier.imePadding(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
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
           showUpdateDialog = uiState.showUpdateDialog,
           onEditClick = {
               viewModel.showUpdateDialog()
           },
           onDeleteClick = {
               viewModel.showDeleteDialog()
           },
           onDismissRequestDelete = viewModel::hideDeleteDialog,
           onConfirmDelete = viewModel::deleteDetino,
           onDismissRequestUpdate = viewModel::hideUpdateDialog,
           onConfirmUpdate = viewModel::updateDestination,
           value = uiState.despacho.toString(),
           onValueChange = viewModel::onValueChangeDespacho,
           errorMessage = uiState.despachoErrorMessage,

       )

    }
}

@Composable
fun TruckFab(
    modifier: Modifier = Modifier,
    icon: Painter,
    onClick: () -> Unit = {}
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