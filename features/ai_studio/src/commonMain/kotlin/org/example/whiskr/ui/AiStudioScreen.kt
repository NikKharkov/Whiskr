package org.example.whiskr.ui

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.arkivanov.decompose.extensions.compose.subscribeAsState
import com.preat.peekaboo.image.picker.ResizeOptions
import com.preat.peekaboo.image.picker.SelectionMode
import com.preat.peekaboo.image.picker.rememberImagePickerLauncher
import org.example.whiskr.component.AiStudioComponent
import org.example.whiskr.theme.LocalIsTablet
import org.example.whiskr.theme.WhiskrTheme
import org.example.whiskr.ui.components.AiGalleryCard
import org.example.whiskr.ui.components.AiTopBar
import org.example.whiskr.ui.components.GalleryGrid
import org.example.whiskr.ui.components.PromptInputSection
import org.example.whiskr.ui.components.ResultSection
import org.example.whiskr.ui.components.StyleSelector
import org.example.whiskr.ui.components.paginatedGridItems
import org.jetbrains.compose.resources.stringResource
import whiskr.features.ai_studio.generated.resources.Res
import whiskr.features.ai_studio.generated.resources.your_gallery

@Composable
fun AiStudioScreen(
    component: AiStudioComponent
) {
    val model by component.model.subscribeAsState()
    val isTablet = LocalIsTablet.current

    val scope = rememberCoroutineScope()
    val imagePicker = rememberImagePickerLauncher(
        selectionMode = SelectionMode.Single,
        scope = scope,
        resizeOptions = ResizeOptions(),
        onResult = { byteArrays ->
            byteArrays.firstOrNull()?.let { component.onSourceImagePicked(it) }
        }
    )

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        containerColor = WhiskrTheme.colors.background,
        topBar = {
            AiTopBar(
                balance = model.balance,
                modifier = Modifier.statusBarsPadding()
            )
        },
        contentWindowInsets = WindowInsets(0, 0, 0, 0)
    ) { innerPadding ->
        if (isTablet) {
            Row(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize()
                    .padding(16.dp),
                horizontalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                Column(
                    modifier = Modifier
                        .weight(1f)
                        .fillMaxHeight(),
                    verticalArrangement = Arrangement.spacedBy(24.dp)
                ) {
                    StyleSelector(
                        selectedStyle = model.selectedStyle,
                        onStyleSelect = component::onStyleSelected
                    )

                    PromptInputSection(
                        prompt = model.prompt,
                        onPromptChange = component::onPromptChanged,
                        isGenerating = model.isGenerating,
                        sourceImageBytes = model.sourceImageBytes,
                        onPickImage = { imagePicker.launch() },
                        onRemoveImage = { component.onSourceImagePicked(null) },
                        onGenerate = component::onGenerateClicked
                    )

                    Text(
                        text = stringResource(Res.string.your_gallery),
                        style = WhiskrTheme.typography.h3,
                        color = WhiskrTheme.colors.onBackground
                    )

                    GalleryGrid(
                        items = model.galleryItems,
                        isLoadingMore = model.isGalleryLoading,
                        onItemClick = { component.onImageClicked(it.imageUrl) },
                        onLoadMore = component::onGalleryLoadMore
                    )
                }

                ResultSection(
                    isGenerating = model.isGenerating,
                    error = model.error,
                    latestItem = model.galleryItems.firstOrNull(),
                    onDownload = component::onDownloadClicked,
                    onShare = component::onShareClicked,
                    onPost = component::onPostClicked,
                    modifier = Modifier.weight(1f).fillMaxHeight(),
                )
            }
        } else {
            LazyColumn(
                modifier = Modifier
                    .padding(innerPadding)
                    .fillMaxSize(),
                contentPadding = PaddingValues(16.dp),
                verticalArrangement = Arrangement.spacedBy(24.dp)
            ) {
                item {
                    StyleSelector(
                        selectedStyle = model.selectedStyle,
                        onStyleSelect = component::onStyleSelected
                    )
                }

                if (model.isGenerating || model.galleryItems.isNotEmpty()) {
                    item {
                        ResultSection(
                            isGenerating = model.isGenerating,
                            error = model.error,
                            latestItem = model.galleryItems.firstOrNull(),
                            onDownload = component::onDownloadClicked,
                            onShare = component::onShareClicked,
                            onPost = component::onPostClicked
                        )
                    }
                }

                item {
                    PromptInputSection(
                        prompt = model.prompt,
                        onPromptChange = component::onPromptChanged,
                        isGenerating = model.isGenerating,
                        sourceImageBytes = model.sourceImageBytes,
                        onPickImage = { imagePicker.launch() },
                        onRemoveImage = { component.onSourceImagePicked(null) },
                        onGenerate = component::onGenerateClicked
                    )
                }

                item {
                    Text(
                        text = stringResource(Res.string.your_gallery),
                        style = WhiskrTheme.typography.h3,
                        color = WhiskrTheme.colors.onBackground,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                }

                paginatedGridItems(
                    items = model.galleryItems,
                    columns = 3,
                    isLoadingMore = model.isGalleryLoading,
                    onLoadMore = component::onGalleryLoadMore,
                    key = { it.id }
                ) { item ->
                    AiGalleryCard(
                        item = item,
                        onClick = { component.onImageClicked(item.imageUrl) }
                    )
                }
            }
        }
    }
}