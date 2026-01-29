package ui

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.graphics.Color
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import component.ProfileComponent
import org.example.whiskr.theme.WhiskrTheme

@Composable
fun ProfileScreen(
    component: ProfileComponent
) {
    val model by component.model.subscribeAsState()

    Text(
        text = model.profile?.handle ?: "",
        style = WhiskrTheme.typography.h1,
        color = Color.Black
    )
}