package ui

import androidx.compose.foundation.layout.size
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.MainComponent
import org.example.whiskr.theme.WhiskrTheme

@Composable
fun MainScreen(
    mainComponent: MainComponent
) {
    val model by mainComponent.model.subscribeAsState()

    Text(
        text = model.userProfile?.displayName ?: "Hueta",
        style = WhiskrTheme.typography.h1,
        color = WhiskrTheme.colors.onBackground
    )

    if (model.isLoading) {
        CircularProgressIndicator(modifier = Modifier.size(24.dp))
    }
}