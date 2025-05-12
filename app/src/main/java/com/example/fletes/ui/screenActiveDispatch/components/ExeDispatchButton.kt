package com.example.fletes.ui.screenActiveDispatch.components


import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.example.fletes.data.room.Camion
import com.example.fletes.data.room.Destino

@Composable
fun ExeDispatchButton(
    activeTruck: Boolean,
    activeDispatch: Boolean,
    isvalidForm: Boolean,
    onClickSave: (camion: Camion, destino: Destino) -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = { onClickSave },
        modifier = modifier,
        enabled = activeTruck && activeDispatch && isvalidForm,
        colors = if (activeTruck && activeDispatch && isvalidForm) ButtonDefaults.outlinedButtonColors() else ButtonDefaults.outlinedButtonColors(
            containerColor = MaterialTheme.colorScheme.errorContainer
        )
    ) {
        Text(
            text = "Save Dispatch",
        )
    }

}

@Preview(showBackground = true)
@Composable
fun ExeDispatchButtonPreview() {
    ExeDispatchButton(
        activeTruck = true,
        activeDispatch = true,
        isvalidForm = true,
        onClickSave = { _, _ -> }
    )
}

@Preview(showBackground = true)
@Composable
fun ExeDispatchButtonDisabledPreview() {
    ExeDispatchButton(
        activeTruck = false,
        activeDispatch = true,
        isvalidForm = true,
        onClickSave = { _, _ -> }
    )
}



