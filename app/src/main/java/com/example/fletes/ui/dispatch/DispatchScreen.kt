package com.example.fletes.ui.destino

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusRequester
import androidx.compose.ui.focus.focusRequester
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.unit.dp
import com.example.fletes.R
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DispatchScreen(
    viewModel: DispatchViewModel = koinViewModel(),
    onBackClick: () -> Unit
) {
    val uiState by viewModel.uiState.collectAsState()
//    val comisionistaSuggestions by viewModel.comisionistas.collectAsState()
//    val localidadSuggestions by viewModel.localidades.collectAsState()

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Time to Leave") },
                navigationIcon = {
                    IconButton(onClick = onBackClick) {
                        Icon(painter = painterResource(R.drawable.ic_time_to_leave_24), contentDescription = "Back")
                    }
                }
            )
        }
    ) { paddingValues ->
        Column(
            modifier = Modifier
                .padding(paddingValues)
                .padding(16.dp)
        ) {
            AutoCompleteTextField(
                value = uiState.comisionista,
                onValueChange = { viewModel.updateComisionista(it) },
                label = "Comisionista",
                suggestions = uiState.comisionistaSuggestions,
                onSuggestionSelected = {
                    viewModel.updateComisionista(it)
                },
                errorMessage = uiState.comisionistaErrorMessage,
                onQueryChange = {
                    viewModel.onComisionistaQueryChange(it)
                }
            )

            Spacer(modifier = Modifier.padding(8.dp))

            AutoCompleteTextField(
                value = uiState.localidad,
                onValueChange = { viewModel.updateLocalidad(it) },
                label = "Destino",
                suggestions = uiState.localidadSuggestions,
                onSuggestionSelected = { viewModel.updateLocalidad(it) },
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
                    Divider()
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