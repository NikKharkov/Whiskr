package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.preat.peekaboo.image.picker.ResizeOptions
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher
import org.example.whiskr.extensions.customClickable
import org.example.whiskr.theme.WhiskrTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import whiskr.features.registration.generated.resources.Res
import whiskr.features.registration.generated.resources.add_photo
import whiskr.features.registration.generated.resources.ic_add
import whiskr.features.registration.generated.resources.ic_camera

@Composable
fun ProfilePhotoSelector(
    modifier: Modifier = Modifier,
    avatarBytes: ByteArray?,
    onAvatarSelected: (ByteArray) -> Unit
) {
    val scope = rememberCoroutineScope()
    val imagePicker = rememberImagePickerLauncher(
        selectionMode = SelectionMode.Single,
        scope = scope,
        resizeOptions = ResizeOptions(),
        onResult = { byteArrays ->
            byteArrays.firstOrNull()?.let { onAvatarSelected(it) }
        }
    )

    Column(
        modifier = modifier.fillMaxWidth(),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp, Alignment.CenterVertically)
    ) {
        Box(
            modifier = Modifier.customClickable(onClick = { imagePicker.launch() }),
            contentAlignment = Alignment.BottomEnd
        ) {
            Box(
                modifier = Modifier
                    .size(80.dp)
                    .clip(CircleShape)
                    .background(WhiskrTheme.colors.surface)
                    .border(1.dp, WhiskrTheme.colors.outline, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (avatarBytes != null) {
                    AsyncImage(
                        model = avatarBytes,
                        contentDescription = stringResource(Res.string.add_photo),
                        contentScale = ContentScale.Crop,
                        modifier = Modifier.fillMaxSize()
                    )
                } else {
                    Icon(
                        painter = painterResource(Res.drawable.ic_camera),
                        contentDescription = null,
                        tint = WhiskrTheme.colors.onBackground.copy(alpha = 0.6f),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }

            Icon(
                painter = painterResource(Res.drawable.ic_add),
                contentDescription = null,
                tint = Color.Unspecified,
                modifier = Modifier.size(24.dp)
            )
        }

        Text(
            text = stringResource(Res.string.add_photo),
            style = WhiskrTheme.typography.caption,
            color = WhiskrTheme.colors.primary
        )
    }
}