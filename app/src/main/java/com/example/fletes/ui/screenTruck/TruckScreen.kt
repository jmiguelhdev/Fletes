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
import androidx.compose.ui.tooling.preview.Preview
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fletes.data.room.Camion
import com.example.fletes.ui.screenTruck.components.ListOfTrucks
import com.example.fletes.ui.screenTruck.components.TruckUpdateDialog
import com.example.fletes.ui.screenTruck.components.TruckInsertDialog
import com.example.fletes.ui.screenTruck.components.TruckTopAppBar
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@Composable
fun TruckScreen(
    truckViewModel: TruckViewModel = koinViewModel(),
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
            if (uiState.value.showInsertDialog) {
                TruckInsertDialog(
                    camionUiState = uiState.value,
                    camionViewModel = truckViewModel,
                    onDismiss = { truckViewModel.hideDialog() },
                    onConfirm = { truckViewModel.insertCamion() }
                )
            }
            if (uiState.value.showEditDialog) {
                TruckUpdateDialog(
                    camion = listOfTrucks.value.first(),
                    camionUiState = uiState.value,
                    camionViewModel = truckViewModel,
                    onDismiss = { truckViewModel.hideDialog() },
                    onConfirm = {
                        truckViewModel.updateCamion(id = it)
                    }
                )
            }
        },
        bottomBar = {

        },

    )
}

@Preview(showSystemUi = true)
@Composable
fun PreviewTruckScreen() {
    val snackBarHostState = remember { SnackbarHostState() }
    val mockListOfTrucks = listOf(
        Camion(
            choferName = "John Doe",
            choferDni = 11111111,
            patenteTractor = "ab123cd",
            patenteJaula = "ab123cd",
            isActive = true
        ),
        Camion(choferName = "Janne Doe",
            choferDni = 11111111,
            patenteTractor = "ab123cd",
            patenteJaula = "ab123cd",
            isActive = true
        )
    )
    Scaffold(
        modifier = Modifier.imePadding(),
        snackbarHost = { SnackbarHost(snackBarHostState) },
        topBar = {
            TruckTopAppBar(
                onInsertTruck = {  },
                onDeleteAllTrucks = {  },
                onBack = { }
            )
        },
    ) {paddingValues ->
        ListOfTrucks(
            listOfTrucks = mockListOfTrucks,
            onDeleteTruck = {},
            onEditTruck = {},
            modifier = Modifier.padding(paddingValues))
    }
}

