package com.example.fletes.ui.screenTruck

import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.material3.DrawerState
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import kotlinx.coroutines.CoroutineScope
import androidx.lifecycle.ViewModel
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import com.example.fletes.data.models.ValidationResult
import com.example.fletes.data.repositories.interfaces.TruckRepositoryInterface
import com.example.fletes.data.room.Camion
import com.example.fletes.domain.validators.DniValidator
import com.example.fletes.domain.validators.PatenteValidator
import com.example.fletes.ui.screenTruck.components.ListOfTrucks
import com.example.fletes.ui.screenTruck.components.TruckUpdateDialog
import com.example.fletes.ui.screenTruck.components.TruckInsertDialog
import com.example.fletes.ui.screenTruck.components.TruckTopAppBar
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.flowOf
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch
import java.time.LocalDate

@Composable
fun TruckScreen(
    truckViewModel: TruckViewModel,
    drawerState: DrawerState, // Add this
    scope: CoroutineScope,   // Add this
    onNavBack: () -> Unit
) {
    val listOfTrucks = truckViewModel.camiones.collectAsStateWithLifecycle()
    val uiState = truckViewModel.uiState.collectAsStateWithLifecycle()
    val snackBarHostState = remember { SnackbarHostState() }
    // val scope = rememberCoroutineScope() // Use the passed-in scope for drawer

    LaunchedEffect(uiState.value.showSnackbar) {
        if (uiState.value.showSnackbar) {
            // Use a new scope for snackbar to avoid conflict if passed-in scope is used for drawer
            rememberCoroutineScope().launch {
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
                onBack = onNavBack, // This onBack might conflict with navigation icon or be conditional
                drawerState = drawerState, // Pass drawerState
                scope = scope              // Pass scope
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

// --- Fake Dependencies and ViewModel for Previews ---

private class FakeTruckRepository : TruckRepositoryInterface {
    override suspend fun getAllCamiones(): Flow<List<Camion>> = flowOf(emptyList())
    override suspend fun getCamionById(id: Int): Camion? = null
    override suspend fun insertCamion(camion: Camion) {}
    override suspend fun updateCamion(camion: Camion) {}
    override suspend fun deleteCamion(camion: Camion) {}
    override suspend fun deleteAlllCamiones() {}
    override suspend fun updateTruckIsActive(camion: Camion) {}
    override suspend fun getAllActiveCamiones(): Flow<List<Camion>> = flowOf(emptyList())
    override suspend fun getActiveCamion(): Flow<Camion> = flowOf(
        Camion(id = 0, choferName = "Fake Driver", patenteTractor = "FAKE123", patenteJaula = "FAKE456", choferDni = 12345678, createdAt = LocalDate.now(), kmService = "10000", isActive = true, latitude = 0.0, longitude = 0.0, orderConteiner = 0)
    )
}

private class FakeDniValidator : DniValidator {
    override fun validateDni(dni: String): ValidationResult = ValidationResult(isValid = true, errorMessage = null)
}

private class FakePatenteValidator : PatenteValidator {
    override fun validatePatente(patente: String): ValidationResult = ValidationResult(isValid = true, errorMessage = null)
}

private open class PreviewTruckViewModel(
    initialUiState: TruckUiState = TruckUiState(),
    initialCamiones: List<Camion> = emptyList()
) : TruckViewModel(FakeTruckRepository(), FakeDniValidator(), FakePatenteValidator()) {

    private val _previewUiState = MutableStateFlow(initialUiState)
    override val uiState: StateFlow<TruckUiState> = _previewUiState.asStateFlow()

    private val _previewCamiones = MutableStateFlow(initialCamiones)
    override val camiones: StateFlow<List<Camion>> = _previewCamiones.asStateFlow()

    fun setPreviewCamiones(trucks: List<Camion>) {
        _previewCamiones.value = trucks
    }

    fun setUiState(uiState: TruckUiState) {
        _previewUiState.value = uiState
    }

    override fun loadCamiones() {
        // No-op for preview
    }
    override fun showDialog() {
        _previewUiState.update { it.copy(currentDialog = DialogState.Insert) }
    }

    override fun hideDialog() {
        _previewUiState.update {
            it.copy(
                currentDialog = DialogState.None,
                editingTruckId = null,
                choferName = "",
                choferDni = "",
                patenteTractor = "",
                patenteJaula = ""
            )
        }
    }

    override fun onShowEditDialog(id: Int) {
        val truckToEdit = _previewCamiones.value.find { it.id == id }
        if (truckToEdit != null) {
            _previewUiState.update {
                it.copy(
                    currentDialog = DialogState.Edit,
                    editingTruckId = truckToEdit.id,
                    choferName = truckToEdit.choferName,
                    choferDni = truckToEdit.choferDni.toString(),
                    patenteTractor = truckToEdit.patenteTractor,
                    patenteJaula = truckToEdit.patenteJaula
                )
            }
        } else { // Fallback if truck not found, to still show dialog
            _previewUiState.update {
                it.copy(
                    currentDialog = DialogState.Edit,
                    editingTruckId = id,
                    choferName = "Edit Name Fallback",
                    choferDni = "00000000",
                    patenteTractor = "FALL123",
                    patenteJaula = "BACK456"
                )
            }
        }
    }
    override fun insertCamion() {
        // Simulate insertion for UI update if needed, then hide dialog
        _previewUiState.update { it.copy(showSnackbar = true, snackbarMessage = "Preview: Camión insertado") }
        hideDialog()
    }

    override fun updateCamion() {
        // Simulate update for UI update if needed, then hide dialog
        _previewUiState.update { it.copy(showSnackbar = true, snackbarMessage = "Preview: Camión actualizado") }
        hideDialog()
    }

    override fun deleteCamion(id: Int) {
        _previewCamiones.update { list -> list.filterNot { it.id == id } }
        _previewUiState.update { it.copy(showSnackbar = true, snackbarMessage = "Preview: Camión eliminado") }
    }


    override fun snackbarShown() {
        _previewUiState.update { it.copy(showSnackbar = false, snackbarMessage = "") }
    }

    override fun deleteAllCamiones() {
        _previewCamiones.value = emptyList()
        _previewUiState.update { it.copy(showSnackbar = true, snackbarMessage = "Preview: Todos los camiones eliminados") }
    }

    override fun onChoferNameValueChange(newValue: String) {
        _previewUiState.update { it.copy(choferName = newValue) }
    }
    override fun onChoferDniValueChange(newValue: String) {
        _previewUiState.update { it.copy(choferDni = newValue, isValidDni = true) } // Simplified validation
    }
    override fun onPatenteTractorValueChange(newValue: String) {
        _previewUiState.update { it.copy(patenteTractor = newValue, isValidPatenteTractor = true) }
    }
    override fun onPatenteJaulaValueChange(newValue: String) {
        _previewUiState.update { it.copy(patenteJaula = newValue, isValidPatenteJaula = true) }
    }
}


// --- Previews ---

@Preview(name = "Truck Screen - With Trucks", showSystemUi = true)
@Composable
fun PreviewTruckScreen_WithTrucks() {
    val previewTruckViewModel = remember {
        PreviewTruckViewModel(
            initialCamiones = listOf(
                Camion(id = 1, choferName = "John Doe", choferDni = 11111111, patenteTractor = "ab123cd", patenteJaula = "efg456h", isActive = true, createdAt = LocalDate.now(), kmService = "0", latitude = 0.0, longitude = 0.0, orderConteiner = 1),
                Camion(id = 2, choferName = "Jane Doe", choferDni = 22222222, patenteTractor = "xy987zw", patenteJaula = "vu654ts", isActive = true, createdAt = LocalDate.now(), kmService = "0", latitude = 0.0, longitude = 0.0, orderConteiner = 2)
            )
        )
    }
    val previewScope = rememberCoroutineScope()
    val previewDrawerState = remember { androidx.compose.material3.DrawerState(androidx.compose.material3.DrawerValue.Closed) }
    TruckScreen(truckViewModel = previewTruckViewModel, drawerState = previewDrawerState, scope = previewScope, onNavBack = {})
}

@Preview(name = "Truck Screen - Empty List", showSystemUi = true)
@Composable
fun PreviewTruckScreen_EmptyList() {
    val previewTruckViewModel = remember { PreviewTruckViewModel(initialCamiones = emptyList()) }
    val previewScope = rememberCoroutineScope()
    val previewDrawerState = remember { androidx.compose.material3.DrawerState(androidx.compose.material3.DrawerValue.Closed) }
    TruckScreen(truckViewModel = previewTruckViewModel, drawerState = previewDrawerState, scope = previewScope, onNavBack = {})
}

@Preview(name = "Truck Screen - Insert Dialog", showSystemUi = true)
@Composable
fun PreviewTruckScreen_InsertDialogVisible() {
    val previewTruckViewModel = remember {
        PreviewTruckViewModel(initialUiState = TruckUiState(currentDialog = DialogState.Insert))
    }
    val previewScope = rememberCoroutineScope()
    val previewDrawerState = remember { androidx.compose.material3.DrawerState(androidx.compose.material3.DrawerValue.Closed) }
    // To make the dialog fields interactive in preview, we need the ViewModel to handle changes.
    // The PreviewTruckViewModel's onValueChange methods will handle this.
    TruckScreen(truckViewModel = previewTruckViewModel, drawerState = previewDrawerState, scope = previewScope, onNavBack = {})
}

@Preview(name = "Truck Screen - Edit Dialog", showSystemUi = true)
@Composable
fun PreviewTruckScreen_EditDialogVisible() {
    val truckToEdit = Camion(id = 1, choferName = "John Doe", choferDni = 11111111, patenteTractor = "ab123cd", patenteJaula = "efg456h", isActive = true, createdAt = LocalDate.now(), kmService = "0", latitude = 0.0, longitude = 0.0, orderConteiner = 1)
    val previewTruckViewModel = remember {
        PreviewTruckViewModel(
            initialUiState = TruckUiState(
                currentDialog = DialogState.Edit,
                editingTruckId = truckToEdit.id,
                choferName = truckToEdit.choferName,
                choferDni = truckToEdit.choferDni.toString(),
                patenteTractor = truckToEdit.patenteTractor,
                patenteJaula = truckToEdit.patenteJaula
            ),
            initialCamiones = listOf(truckToEdit)
        )
    }
    // The onShowEditDialog in PreviewTruckViewModel might be more robust if called directly,
    // but setting initialUiState like this also works for previewing the dialog's appearance.
    // For full interactivity, onShowEditDialog logic within the fake VM is better.
    // Let's refine it:
    // previewTruckViewModel.setPreviewCamiones(listOf(truckToEdit))
    // previewTruckViewModel.onShowEditDialog(truckToEdit.id) // This will set the state
    val previewScope = rememberCoroutineScope()
    val previewDrawerState = remember { androidx.compose.material3.DrawerState(androidx.compose.material3.DrawerValue.Closed) }
    TruckScreen(truckViewModel = previewTruckViewModel, drawerState = previewDrawerState, scope = previewScope, onNavBack = {})
}
