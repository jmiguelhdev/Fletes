package com.example.fletes.ui.destino

import android.util.Log
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import com.example.fletes.R
import kotlinx.coroutines.launch
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DispatchScreen(
    viewModel: DispatchViewModel = koinViewModel(),
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
    val activeDispatchCount by viewModel.activeDispatchCount.collectAsState(0)
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

    Scaffold(
        modifier = Modifier.imePadding(),
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Time to Leave Active Dispatchs: $activeDispatchCount")
                        //tal vez ponga un contador de destinos activos
                        //para lo cual creo que tengo que agregar un campo en la base de datos
                        // que tenga en cuenta cuando un ddespacho esta finalizado o no
                        },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(painter = painterResource(R.drawable.ic_time_to_leave_24), contentDescription = "Back")
                    }
                },
                actions = {
                    //agregar o cambiar el botton de insertar aqui
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            DecimalTextField(
                value = if (uiState.despacho == null)"" else uiState.despacho.toString(),
                onValueChange = viewModel::onValueChangeDespacho,
                label = "Despacho",
                errorMessage = uiState.despachoErrorMessage,
                modifier = Modifier.fillMaxWidth()
                )

            Spacer(modifier = Modifier.padding(8.dp))

            AutoCompleteTextField(
                value = uiState.comisionista,
                onValueChange = { viewModel.onValueChangeComisionista(it) },
                label = "Comisionista",
                suggestions = uiState.comisionistaSuggestions,
                onSuggestionSelected = {
                    viewModel.onValueChangeComisionista(it)
                },
                errorMessage = uiState.comisionistaErrorMessage,
                onQueryChange = {
                    viewModel.onComisionistaQueryChange(it)
                    Log.d("DispatchScreen", "Query changed: $it")
                }
            )

            Spacer(modifier = Modifier.padding(8.dp))

            AutoCompleteTextField(
                value = uiState.localidad,
                onValueChange = { viewModel.onValueChangeLocalidad(it) },
                label = "Destino",
                suggestions = uiState.localidadSuggestions,
                onSuggestionSelected = { viewModel.onValueChangeLocalidad(it) },
                errorMessage = uiState.localidadErrorMessage,
                onQueryChange = {
                    viewModel.onLocalidadQueryChange(it)
                }
            )

            Spacer(modifier = Modifier.padding(16.dp))

            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                Button(
                    onClick = {
                        viewModel.validateComisionista(uiState.comisionista)
                        viewModel.validateLocalidad(uiState.localidad)
                        viewModel.insertNewDestino()
                    },
                    enabled = uiState.isInsertButtonEnabled
                ) {
                    if (uiState.isLoading) {
                        CircularProgressIndicator(
                            modifier = Modifier.width(24.dp),
                            color = MaterialTheme.colorScheme.onPrimary
                        )
                    } else {
                        Text(text = "Insertar")
                    }
                }
            }
        }
    }
}

@Composable
fun AutoCompleteTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    suggestions: List<String>,
    onSuggestionSelected: (String) -> Unit,
    errorMessage: String?,
    onQueryChange: (String) -> Unit,
) {
    val focusRequester = FocusRequester()
    Column {
        OutlinedTextField(
            modifier = Modifier
                .fillMaxWidth()
                .focusRequester(focusRequester),
            value = value,
            onValueChange = {
                onValueChange(it)
                onQueryChange(it)
            },
            label = { Text(label) },
            isError = errorMessage != null
        )

        if (errorMessage != null) {
            Text(
                text = errorMessage,
                color = MaterialTheme.colorScheme.error
            )
        }
        if (suggestions.isNotEmpty()) {
            LazyColumn(
                modifier = Modifier
                    .fillMaxWidth(),
                contentPadding = PaddingValues(horizontal = 16.dp)
            ) {
                items(suggestions) { suggestion ->
                    SuggestionItem(
                        suggestion = suggestion,
                        onSuggestionSelected = {
                            onSuggestionSelected(it)
                        }
                    )
                    HorizontalDivider(modifier = Modifier, thickness = 4.dp, color = MaterialTheme.colorScheme.onSurface.copy(alpha = 0.12f))
                }
            }
        }
    }
    LaunchedEffect(Unit){
        focusRequester.requestFocus()
    }
}

@Composable
fun SuggestionItem(
    suggestion: String,
    onSuggestionSelected: (String) -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .clickable { onSuggestionSelected(suggestion) }
            .padding(16.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = suggestion)
    }
}

@Composable
fun DecimalTextField(
    value: String,
    onValueChange: (String) -> Unit,
    label: String,
    errorMessage: String?,
    modifier: Modifier = Modifier
) {
    OutlinedTextField(
        modifier = modifier,
        value = value,
        onValueChange = { newText ->
            if (newText.matches(Regex("[0-9.,]*"))) { // Allow digits, comma, and period
                val dotCount = newText.count { it == '.' }
                val commaCount = newText.count { it == ',' }

                if (dotCount <= 1 && commaCount <= 1) { // Allow only one decimal separator
                    val hasLeadingSeparator = newText.startsWith(".") || newText.startsWith(",")
                    val hasTrailingSeparator = newText.endsWith(".") || newText.endsWith(",")
                    if (!hasLeadingSeparator && !hasTrailingSeparator) { // prevent leading or trailing separator
                        onValueChange(newText)
                    }
                }
            }
        },
        label = { Text(label) },
        isError = errorMessage != null,
        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Decimal), // Show decimal keyboard
    )
    if (errorMessage != null) {
        Text(text = errorMessage, color = MaterialTheme.colorScheme.error)
    }
}