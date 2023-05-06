package com.example.eats.pages.eat.pages.addinfo

import android.content.res.Configuration
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.runtime.snapshotFlow
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalConfiguration
import androidx.compose.ui.tooling.preview.Preview
import androidx.hilt.navigation.compose.hiltViewModel
import com.example.eats.pages.eat.FieldState
import com.example.eats.pages.utils.EATSTextField
import com.example.eats.pages.utils.TopBar
import com.example.eats.ui.theme.EATSTheme

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AddInfoPage(viewModel: AddInfoViewModel = hiltViewModel()) {
    val uiState by viewModel.uiState.collectAsState()
    val onEvent: (e: AddInfoEvent) -> Unit = { viewModel.createEvent(it) }
    var orientation by remember { mutableStateOf(Configuration.ORIENTATION_PORTRAIT) }
    val configuration = LocalConfiguration.current
    LaunchedEffect(key1 = configuration) {
        snapshotFlow { configuration.orientation }.collect { orientation = it }
    }
    Scaffold(topBar = { TopBar(label = "Добавление продукта") }) {
        Box(Modifier.padding(it)) {
            when (orientation) {
                Configuration.ORIENTATION_LANDSCAPE ->
                    LandScapeLayout(uiState = uiState, onEvent = onEvent)

                else ->
                    PortraitLayout(uiState = uiState, onEvent = onEvent)
            }
        }
    }
}

@Composable
private fun PortraitLayout(uiState: AddInfoState, onEvent: (e: AddInfoEvent) -> Unit) {
    Column() {

    }
}

@Composable
private fun LandScapeLayout(uiState: AddInfoState, onEvent: (e: AddInfoEvent) -> Unit) {

}

@Composable
private fun LabelTextField(state: FieldState, onEvent: (e: AddInfoEvent) -> Unit) {
    EATSTextField(
        value = state.value,
        error = state.error,
        hintText = "Введите название",
        onValueChange = { onEvent(AddInfoEvent.LabelChange(it)) })
}

@Composable
private fun CaloriesTextField(state: FieldState, onEvent: (e: AddInfoEvent) -> Unit) {

}

@Composable
@Preview(showBackground = true)
private fun PortraitPreview() {
    EATSTheme {
        PortraitLayout(uiState = AddInfoState(), onEvent = {})
    }
}

@Composable
@Preview(showBackground = true)
private fun LandScapePreview() {
    EATSTheme {
        LandScapeLayout(uiState = AddInfoState(), onEvent = {})
    }
}