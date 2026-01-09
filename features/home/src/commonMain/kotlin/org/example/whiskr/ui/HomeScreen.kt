package org.example.whiskr.ui

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.TextFieldDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import coil3.compose.AsyncImage
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.mohamedrejeb.calf.core.LocalPlatformContext
import com.mohamedrejeb.calf.io.KmpFile
import com.mohamedrejeb.calf.picker.FilePickerFileType
import com.mohamedrejeb.calf.picker.FilePickerSelectionMode
import com.mohamedrejeb.calf.picker.rememberFilePickerLauncher
import org.example.whiskr.component.HomeComponent
import org.example.whiskr.component.create.CreatePostComponent
import org.example.whiskr.components.AvatarPlaceholder
import org.example.whiskr.components.WhiskrButton
import org.example.whiskr.extensions.customClickable
import org.example.whiskr.theme.WhiskrTheme
import org.example.whiskr.ui.components.CreatePostLayout
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import util.LocalUser
import whiskr.features.home.generated.resources.Res
import whiskr.features.home.generated.resources.add_image
import whiskr.features.home.generated.resources.add_post
import whiskr.features.home.generated.resources.ic_add
import whiskr.features.home.generated.resources.ic_close
import whiskr.features.home.generated.resources.ic_gallery
import whiskr.features.home.generated.resources.post
import whiskr.features.home.generated.resources.whats_happening


@Composable
fun HomeScreen(
    component: HomeComponent
) {
    val user = LocalUser.current

    BoxWithConstraints(modifier = Modifier.fillMaxSize()) {
        val isTablet = maxWidth > 600.dp

        Scaffold(
            modifier = Modifier.fillMaxSize(),
            containerColor = WhiskrTheme.colors.background,
            floatingActionButton = {
                if (!isTablet) {
                    FloatingActionButton(
                        onClick = component::onNavigateToCreatePostScreen,
                        containerColor = WhiskrTheme.colors.primary,
                        shape = CircleShape,
                        contentColor = Color.White
                    ) {
                        Icon(
                            painter = painterResource(Res.drawable.ic_add),
                            contentDescription = stringResource(Res.string.add_post)
                        )
                    }
                }
            }
        ) { innerPadding ->
            LazyColumn(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                if (isTablet) {
                    item {
                        CreatePostLayout(
                            component = component.createPostComponent,
                            userAvatar = user?.profile?.avatarUrl
                        )
                        HorizontalDivider(thickness = 0.5.dp, color = Color.LightGray)
                    }


                }
            }
        }
    }
}

@Composable
fun CreatePostInput(
    text: String,
    userAvatar: String?,
    onTextChange: (String) -> Unit,
    selectedFiles: List<KmpFile>,
    onRemoveFile: (KmpFile) -> Unit,
    onPickImagesClick: () -> Unit,
    onSendClick: () -> Unit,
    isSending: Boolean,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .fillMaxWidth()
            .padding(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalAlignment = Alignment.Top
    ) {
        AvatarPlaceholder(
            avatarUrl = userAvatar,
            modifier = Modifier.padding(top = 4.dp)
        )

        Column(
            modifier = Modifier.weight(1f)
        ) {
            TextField(
                value = text,
                onValueChange = onTextChange,
                placeholder = {
                    Text(
                        text = stringResource(Res.string.whats_happening),
                        color = WhiskrTheme.colors.secondary,
                        style = WhiskrTheme.typography.h3.copy(fontWeight = FontWeight.Normal)
                    )
                },
                modifier = Modifier.fillMaxWidth(),
                colors = TextFieldDefaults.colors(
                    focusedContainerColor = Color.Transparent,
                    unfocusedContainerColor = Color.Transparent,
                    focusedIndicatorColor = Color.Transparent,
                    unfocusedIndicatorColor = Color.Transparent,
                    focusedTextColor = WhiskrTheme.colors.onBackground,
                )
            )

            if (selectedFiles.isNotEmpty()) {
                LazyRow(
                    modifier = Modifier.padding(top = 8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(selectedFiles) { file ->
                        Box {
                            AsyncImage(
                                model = file,
                                contentDescription = null,
                                modifier = Modifier
                                    .size(80.dp)
                                    .clip(RoundedCornerShape(12.dp)),
                                contentScale = ContentScale.Crop
                            )

                            Box(
                                modifier = Modifier
                                    .align(Alignment.TopEnd)
                                    .padding(4.dp)
                                    .size(20.dp)
                                    .background(Color.Black.copy(alpha = 0.6f), CircleShape)
                                    .customClickable { onRemoveFile(file) },
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    painter = painterResource(Res.drawable.ic_close),
                                    contentDescription = null,
                                    tint = Color.White,
                                    modifier = Modifier.size(12.dp)
                                )
                            }
                        }
                    }
                }
            }

            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(horizontalArrangement = Arrangement.spacedBy(16.dp)) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_gallery),
                        contentDescription = stringResource(Res.string.add_image),
                        tint = WhiskrTheme.colors.primary,
                        modifier = Modifier
                            .size(24.dp)
                            .customClickable(onClick = onPickImagesClick)
                    )

                    // Сюда потом добавим иконку видео ;)
                }

                WhiskrButton(
                    onClick = onSendClick,
                    enabled = !isSending && (text.isNotBlank() || selectedFiles.isNotEmpty()),
                    text = if (isSending) "..." else stringResource(Res.string.post),
                    isLoading = isSending,
                    contentColor = Color.White,
                    modifier = Modifier.widthIn(min = 100.dp)
                )
            }
        }
    }
}