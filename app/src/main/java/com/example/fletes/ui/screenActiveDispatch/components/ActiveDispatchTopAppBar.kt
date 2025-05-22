package com.example.fletes.ui.screenActiveDispatch.components

import androidx.compose.material3.CenterAlignedTopAppBar
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.DrawerState
import androidx.compose.material3.DrawerValue
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.rememberDrawerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch
import androidx.compose.ui.tooling.preview.Preview
import com.example.fletes.R


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActiveDispatchTopAppBar(
    modifier: Modifier = Modifier,
    count: Int = 0,
    onClick: () -> Unit = {},
    drawerState: DrawerState,
    scope: CoroutineScope
) {
    CenterAlignedTopAppBar(
        title = {
            Text("Active Dispatch: $count")
        },
        modifier = modifier,
        navigationIcon = {
            IconButton(onClick = {
                scope.launch {
                    drawerState.open()
                }
            }) {
                Icon(
                    imageVector = Icons.Filled.Menu,
                    contentDescription = "Open Navigation Drawer"
                )
            }
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
    val previewScope = rememberCoroutineScope()
    val previewDrawerState = rememberDrawerState(initialValue = DrawerValue.Closed)
    ActiveDispatchTopAppBar(
        count = 2,
        onClick = {},
        drawerState = previewDrawerState,
        scope = previewScope
    )
}