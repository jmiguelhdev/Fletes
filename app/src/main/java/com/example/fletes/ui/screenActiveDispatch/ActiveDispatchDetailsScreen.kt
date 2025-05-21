package com.example.fletes.ui.screenActiveDispatch

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.HorizontalDivider
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
import com.example.fletes.R
import com.example.fletes.data.room.Camion
import com.example.fletes.data.room.Destino
import com.example.fletes.ui.screenActiveDispatch.components.ActiveDispatchTopAppBar
import com.example.fletes.ui.screenActiveDispatch.components.CardSelectedTruck
import com.example.fletes.ui.screenActiveDispatch.components.DeleteDestinationDialog
import com.example.fletes.ui.screenActiveDispatch.components.DestinationCard
import com.example.fletes.ui.screenActiveDispatch.components.DestinationDropdown
import com.example.fletes.ui.screenActiveDispatch.components.ExeDispatchButton
import com.example.fletes.ui.screenActiveDispatch.components.TruckFab
import com.example.fletes.ui.screenActiveDispatch.components.TrucksDropdown
import com.example.fletes.ui.screenActiveDispatch.components.UpdateDestinationAlertDialog
import com.example.fletes.ui.screenDispatch.NewDispatchViewModel
import com.example.fletes.ui.screenTruck.TruckViewModel
import kotlinx.coroutines.launch


@Composable
fun ActiveDispatchDetailsScreen(
    modifier: Modifier = Modifier,
    newDispatchViewModel: NewDispatchViewModel,
    truckViewModel: TruckViewModel,
    alltrucks: List<Camion>,
    activeDispatch: List<Destino>,
    unActiveDestinations: List<Destino>,
    onClickFab: () -> Unit = {},
    onClickAction: () -> Unit = {}
) {
    val uiState by newDispatchViewModel.uiState.collectAsState()
    val activeDispatchCount by newDispatchViewModel.activeDispatchCount.collectAsState(0)

    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.showSnackbar) {
        if (uiState.showSnackbar) {
            scope.launch {
                snackBarHostState.showSnackbar(
                    message = uiState.snackbarMessage,
                    withDismissAction = true
                )
                newDispatchViewModel.snackbarShown()
            }
        }
    }

    Scaffold(
        modifier = modifier.imePadding(),
        snackbarHost = { SnackbarHost(snackBarHostState) },
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
    ) { innerPadding ->
        Column(
            modifier = Modifier.padding(innerPadding)
        ) {
            TrucksDropdown(
                onClickTruck = {
                    truckViewModel.activateTruck(it)
                    newDispatchViewModel.selectTruck(it)
                },
                list = alltrucks,
            )
            HorizontalDivider()
            if (!uiState.selectedTruck.isActive) {
                CardSelectedTruck(
                    truck = uiState.selectedTruck,
                    unSelectTruck = {
                        truckViewModel.deactivateTruck(it)
                        newDispatchViewModel.unSelectTruck(it)
                    },
                    modifier = Modifier
                )
            }
            HorizontalDivider()
            // List of destination dropdown menu
            if (activeDispatch.isNotEmpty()) {
                DestinationDropdown(
                    onClickDestination = {
                        newDispatchViewModel.selectDestination(destination = it)
                    },
                    list = activeDispatch
                )
            }
            HorizontalDivider()
            if (!uiState.selectedDestination.isActive) {
                DestinationCard(
                    modifier = modifier,
                    destination = uiState.selectedDestination,
                    onEditDestination = {
                        newDispatchViewModel.showUpdateDialog()
                    },
                    onDeleteDestination = {
                        newDispatchViewModel.showDeleteDialog()
                    },
                    onClicable = newDispatchViewModel::unSelectDestination
                )
            }
            HorizontalDivider()
            ExeDispatchButton(
                activeTruck = !uiState.selectedTruck.isActive,
                activeDispatch = !uiState.selectedDestination.isActive,
                onClickSave = {
                    Log.d("ActiveDispatchDetailsScreen", "onClickSave: ${uiState.selectedTruck.isActive}")
                    Log.d("ActiveDispatchDetailsScreen", "onClickSave: ${uiState.selectedDestination.isActive}")
                        newDispatchViewModel.createJourney(uiState.selectedTruck, uiState.selectedDestination)
                              },
                modifier = modifier
            )

            if (uiState.showDeleteDialog) {
                DeleteDestinationDialog(
                    destino = uiState.selectedDestination,
                    onDismissRequestDelete = newDispatchViewModel::hideDeleteDialog,
                    onConfirmDelete = newDispatchViewModel::deleteDetino,
                )
            }
            if (uiState.showUpdateDialog) {
                UpdateDestinationAlertDialog(
                    destino = uiState.selectedDestination,
                    onDismissRequest = newDispatchViewModel::hideUpdateDialog,
                    onConfirm = newDispatchViewModel::updateDestination,
                    value = uiState.despacho.toString(),
                    onValueChange = newDispatchViewModel::onValueChangeDespacho,
                    errorMessage = uiState.despachoErrorMessage,
                )
            }
        }
    }
}




