package com.example.fletes.ui.screenActiveDispatch.components


import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun ExeDispatchButton(
    activeTruck: Boolean,
    activeDispatch: Boolean,
    onClickSave: () -> Unit,
    modifier: Modifier = Modifier
) {
    OutlinedButton(
        onClick = onClickSave,
        modifier = modifier.fillMaxWidth(),
        enabled = activeTruck && activeDispatch,
        colors = if (activeTruck && activeDispatch) ButtonDefaults.outlinedButtonColors() else ButtonDefaults.outlinedButtonColors(
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
        onClickSave = { }
    )
}

@Preview(showBackground = true)
@Composable
fun ExeDispatchButtonDisabledPreview() {
    ExeDispatchButton(
        activeTruck = false,
        activeDispatch = true,
        onClickSave = { }
    )
}



