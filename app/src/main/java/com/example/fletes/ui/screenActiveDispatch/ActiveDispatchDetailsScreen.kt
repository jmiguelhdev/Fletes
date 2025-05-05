package com.example.fletes.ui.screenActiveDispatch

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.fletes.R
import com.example.fletes.ui.screenActiveDispatch.components.ActiveDispatchTopAppBar
import com.example.fletes.ui.screenActiveDispatch.components.DeleteDestinationDialog
import com.example.fletes.ui.screenActiveDispatch.components.DestinationCard
import com.example.fletes.ui.screenActiveDispatch.components.TruckFab
import com.example.fletes.ui.screenActiveDispatch.components.UpdateDestinationAlertDialog
import com.example.fletes.ui.screenDispatch.NewDispatchViewModel
import com.example.fletes.ui.theme.FletesTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun ActiveDispatchDetailsScreen(
    modifier: Modifier = Modifier,
    viewModel: NewDispatchViewModel = koinViewModel(),
    onClickFab: () -> Unit = {},
    onClickAction: () -> Unit = {}
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
            ActiveDispatchTopAppBar(
                count = activeDispatchCount,
                onClick = onClickAction
            )
        },
        floatingActionButton = {
            TruckFab(
                onClick = onClickFab,
                icon = painterResource(R.drawable.icl_shipping_24),
                modifier = Modifier
            )
        }
    ) {innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ){
            LazyColumn(
            ){ 
                items(activeDispatch) { destination ->
                    DestinationCard(
                        destination = destination,
                        onEdit = {

                        },
                        onDelete = {

                        }
                    )
                    if (uiState.showDeleteDialog) {
                        DeleteDestinationDialog(
                            destino = destination,
                            onDismissRequestDelete = viewModel::hideDeleteDialog,
                            onConfirmDelete = viewModel::deleteDetino,
                        )
                    }
                    if (uiState.showUpdateDialog){
                        UpdateDestinationAlertDialog(
                            destino = destination,
                            onDismissRequest = viewModel::hideUpdateDialog,
                            onConfirm = viewModel::updateDestination,
                            value = uiState.despacho.toString(),
                            onValueChange = viewModel::onValueChangeDespacho,
                            errorMessage = uiState.despachoErrorMessage,
                        )
                    }
                }
            }
        }
    }
}





@Preview(showBackground = true)
@Composable
fun TrucksDetailsScreenPrev(modifier: Modifier = Modifier) {
    FletesTheme {
        ActiveDispatchDetailsScreen(
            modifier = modifier,
        )
    }
}