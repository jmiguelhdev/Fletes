package com.example.fletes.ui.screenActiveDispatch.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import com.example.fletes.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveDispatchTopAppBar(
    modifier: Modifier = Modifier,
    count: Int = 0,
    onClick: () -> Unit = {}
) {
    CenterAlignedTopAppBar(
        title = {
            Text("Active Dispatch: $count")
        },
        modifier = modifier,
        navigationIcon = {
            Icon(
                painter = painterResource(R.drawable.ic_arrow_back_24),
                contentDescription = "Back Icon"
            )
        },
        actions = {
            IconButton(
                onClick = onClick
            ) {
                Icon(
                    painter = painterResource(R.drawable.ic_more_vert_24),
                    contentDescription = "More Icon"
                )
            }

        },
    )
}


@Preview(showBackground = true)
@Composable
private fun ActiveDispatchTopAppBarPreview(
) {
    ActiveDispatchTopAppBar(
        count = 2,
        onClick = {}
    )
}