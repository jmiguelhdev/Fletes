package com.example.fletes.ui.screenTruck

import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fletes.ui.screenTruck.components.ListOfTrucks
import com.example.fletes.ui.screenTruck.components.TruckInsertDialog
import com.example.fletes.ui.screenTruck.components.TruckTopAppBar
import com.example.fletes.ui.screenTruck.components.TruckUpdateDialog
import kotlinx.coroutines.launch

@Composable
fun TruckScreen(
    truckViewModel: TruckViewModel,
    onNavBack: () -> Unit
) {
    val listOfTrucks = truckViewModel.camiones.collectAsStateWithLifecycle()
    val uiState = truckViewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.value.showSnackbar) {
        if (uiState.value.showSnackbar) {
            scope.launch {
                snackBarHostState.showSnackbar(
                    message = uiState.value.snackbarMessage,
                    withDismissAction = true
                )
                truckViewModel.snackbarShown()
            }
        }
    }
    Scaffold(
        modifier = Modifier.imePadding(),
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            TruckTopAppBar(
                onInsertTruck = { truckViewModel.showDialog() },
                onDeleteAllTrucks = { truckViewModel.deleteAllCamiones() },
                onBack = onNavBack
            )
        },
        content = { paddingValues ->
            ListOfTrucks(
                listOfTrucks = listOfTrucks.value,
                onDeleteTruck = { truckViewModel.deleteCamion(it) },
                onEditTruck = {
                    truckViewModel.onShowEditDialog(it)
                },
                modifier = Modifier
                    .padding(paddingValues)

            )
            if (uiState.value.currentDialog == DialogState.Insert) {
                TruckInsertDialog(
                    camionUiState = uiState.value,
                    camionViewModel = truckViewModel, // Requires a real/fake ViewModel
                    onDismiss = { truckViewModel.hideDialog() },
                    onConfirm = { truckViewModel.insertCamion() }
                )
            }
            if (uiState.value.currentDialog == DialogState.Edit) {
                TruckUpdateDialog(
                    camionUiState = uiState.value,
                    camionViewModel = truckViewModel, // Requires a real/fake ViewModel
                    onDismiss = { truckViewModel.hideDialog() },
                    onConfirm = {
                        truckViewModel.updateCamion()
                    }
                )
            }
        },
        bottomBar = {

        },

    )
}



