package com.example.fletes.ui.screenActiveDispatch

import android.util.Log
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.input.TextFieldLineLimits
import androidx.compose.foundation.text.input.rememberTextFieldState
import androidx.compose.foundation.text.input.setTextAndPlaceCursorAtEnd
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ExposedDropdownMenuAnchorType
import androidx.compose.material3.ExposedDropdownMenuBox
import androidx.compose.material3.ExposedDropdownMenuDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.fletes.R
import com.example.fletes.data.room.Camion
import com.example.fletes.ui.screenActiveDispatch.components.ActiveDispatchTopAppBar
import com.example.fletes.ui.screenActiveDispatch.components.DeleteDestinationDialog
import com.example.fletes.ui.screenActiveDispatch.components.DestinationCard
import com.example.fletes.ui.screenActiveDispatch.components.TruckFab
import com.example.fletes.ui.screenActiveDispatch.components.UpdateDestinationAlertDialog
import com.example.fletes.ui.screenDispatch.NewDispatchViewModel
import com.example.fletes.ui.screenTruck.TruckViewModel
import com.example.fletes.ui.theme.FletesTheme
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel
import java.time.LocalDate


@Composable
fun ActiveDispatchDetailsScreen(
    modifier: Modifier = Modifier,
    newDispatchViewModel: NewDispatchViewModel,
    alltrucks:List<Camion>,
    onClickFab: () -> Unit = {},
    onClickAction: () -> Unit = {}
) {
    val uiState by newDispatchViewModel.uiState.collectAsState()
    val activeDispatchCount by newDispatchViewModel.activeDispatchCount.collectAsState(0)
    val activeDispatch by newDispatchViewModel.activeDispatch.collectAsState(emptyList())
//    val loadedTrucks by truckViewModel.camiones.collectAsState(emptyList())
//    Log.d("TrucksDetailsScreen", "activeTrucks: $loadedTrucks")
    val snackbarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()

    LaunchedEffect(uiState.showSnackbar) {
        if (uiState.showSnackbar) {
            scope.launch {
                snackbarHostState.showSnackbar(
                    message = uiState.snackbarMessage,
                    withDismissAction = true
                )
                newDispatchViewModel.snackbarShown()
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
            TrucksDropdown(
                list = alltrucks,
            )
            // List of trucks dropdown menu
            LazyColumn(
            ){ 
                items(activeDispatch, key = { it.id }) { destination ->
                    DestinationCard(
                        destination = destination,
                        onEditDestination = {
                            newDispatchViewModel.showUpdateDialog()
                        },
                        onDeleteDestination =  {
                            newDispatchViewModel.showDeleteDialog()
                        },
                    )
                    if (uiState.showDeleteDialog) {
                        DeleteDestinationDialog(
                            destino = destination,
                            onDismissRequestDelete = newDispatchViewModel::hideDeleteDialog,
                            onConfirmDelete = newDispatchViewModel::deleteDetino,
                        )
                    }
                    if (uiState.showUpdateDialog){
                        UpdateDestinationAlertDialog(
                            destino = destination,
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
    }
}


val listaDeCamiones = listOf(
    Camion(
        id = 1,
        createdAt = LocalDate.now(),
        choferName = "John Doe",
        choferDni = 231231,
        patenteTractor = "122313",
        patenteJaula = "2123132",
        isActive = true
    ),
    Camion(
        id = 1,
        createdAt = LocalDate.now(),
        choferName = "John Doe",
        choferDni = 231231,
        patenteTractor = "122313",
        patenteJaula = "2123132",
        isActive = true
    ),
    Camion(
        id = 1,
        createdAt = LocalDate.now(),
        choferName = "John Doe",
        choferDni = 231231,
        patenteTractor = "122313",
        patenteJaula = "2123132",
        isActive = true
    ),
)
@Preview(showBackground = true)
@Composable
fun TrucksDropdownPreview() {

    FletesTheme {
        Column() {
            TrucksDropdown(
                list = listaDeCamiones,
            )
        }
    }
}

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TrucksDropdown(
    list: List<Camion>,
) {
    val listOfChoferName = list.filter { it.choferName.isNotBlank() }.map { it.choferName }


    val options: List<String> = listOfChoferName
    var expanded by remember { mutableStateOf(false) }
    val textFieldState = rememberTextFieldState(options[0])

    ExposedDropdownMenuBox(
        expanded = expanded,
        onExpandedChange = { expanded = it },
    ) {
        TextField(
            // The `menuAnchor` modifier must be passed to the text field to handle
            // expanding/collapsing the menu on click. A read-only text field has
            // the anchor type `PrimaryNotEditable`.
            modifier = Modifier
                .fillMaxWidth()
                .menuAnchor(ExposedDropdownMenuAnchorType.PrimaryNotEditable),
            state = textFieldState,
            readOnly = true,
            lineLimits = TextFieldLineLimits.SingleLine,
            label = { Text("Label") },
            trailingIcon = { ExposedDropdownMenuDefaults.TrailingIcon(expanded = expanded) },
            colors = ExposedDropdownMenuDefaults.textFieldColors(),
        )
        ExposedDropdownMenu(
            expanded = expanded,
            onDismissRequest = { expanded = false },
        ) {
            options.forEach { option ->
                DropdownMenuItem(
                    text = { Text(option, style = MaterialTheme.typography.bodyLarge) },
                    onClick = {
                        textFieldState.setTextAndPlaceCursorAtEnd(option)
                        expanded = false
                    },
                    contentPadding = ExposedDropdownMenuDefaults.ItemContentPadding,
                )
            }
        }
    }
}


