package ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import component.ProfileComponent
import org.example.whiskr.components.AvatarPlaceholder
import org.example.whiskr.components.WhiskrButton
import org.example.whiskr.extensions.animatedGradientBackground
import org.example.whiskr.extensions.customClickable
import org.example.whiskr.theme.WhiskrTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import whiskr.features.profile.generated.resources.Res
import whiskr.features.profile.generated.resources.add
import whiskr.features.profile.generated.resources.edit_profile
import whiskr.features.profile.generated.resources.follow
import whiskr.features.profile.generated.resources.followers
import whiskr.features.profile.generated.resources.following
import whiskr.features.profile.generated.resources.ic_add
import whiskr.features.profile.generated.resources.ic_arrow_back
import whiskr.features.profile.generated.resources.message
import whiskr.features.profile.generated.resources.my_pets
import whiskr.features.profile.generated.resources.unfollow

@Composable
fun ProfileHeaderContent(
    model: ProfileComponent.Model,
    onBackClick: () -> Unit,
    onEditClick: () -> Unit,
    onFollowClick: () -> Unit,
    onMessageClick: () -> Unit,
    onPetClick: (Long) -> Unit,
    onAddPetClick: () -> Unit
) {
    val isMe = model.profile?.isMe == true

    Column(modifier = Modifier.fillMaxWidth()) {
        Box(
            modifier = Modifier
                .fillMaxWidth()
                .height(200.dp)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(150.dp)
                    .animatedGradientBackground(
                        colors = WhiskrTheme.colors.skyGradient,
                        animationTimeMillis = 5000
                    )
            )

            IconButton(
                onClick = onBackClick,
                modifier = Modifier
                    .padding(8.dp)
                    .background(WhiskrTheme.colors.background.copy(alpha = 0.5f), CircleShape)
            ) {
                Icon(
                    painter = painterResource(Res.drawable.ic_arrow_back),
                    contentDescription = null,
                    tint = WhiskrTheme.colors.onBackground
                )
            }

            AvatarPlaceholder(
                avatarUrl = model.profile?.avatarUrl,
                modifier = Modifier
                    .align(Alignment.BottomStart)
                    .padding(start = 16.dp)
                    .size(100.dp)
                    .border(2.dp, WhiskrTheme.colors.background, CircleShape)
            )

            Row(
                modifier = Modifier
                    .align(Alignment.BottomEnd)
                    .padding(end = 16.dp, bottom = 12.dp),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                if (isMe) {
                    WhiskrButton(
                        text = stringResource(Res.string.edit_profile),
                        onClick = onEditClick,
                        containerColor = WhiskrTheme.colors.background,
                        contentColor = WhiskrTheme.colors.onBackground,
                        borderColor = WhiskrTheme.colors.outline,
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(20.dp)
                    )
                } else {
                    WhiskrButton(
                        text = stringResource(Res.string.message),
                        onClick = onMessageClick,
                        containerColor = WhiskrTheme.colors.background,
                        contentColor = WhiskrTheme.colors.onBackground,
                        borderColor = WhiskrTheme.colors.outline,
                        contentPadding = PaddingValues(horizontal = 16.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(20.dp)
                    )

                    val isFollowing = model.profile?.isFollowing == true
                    WhiskrButton(
                        text = stringResource(if (isFollowing) Res.string.unfollow else Res.string.follow),
                        onClick = onFollowClick,
                        containerColor = if (isFollowing) WhiskrTheme.colors.background else WhiskrTheme.colors.onBackground,
                        contentColor = if (isFollowing) WhiskrTheme.colors.onBackground else WhiskrTheme.colors.background,
                        borderColor = if (isFollowing) WhiskrTheme.colors.outline else null,
                        contentPadding = PaddingValues(horizontal = 20.dp, vertical = 8.dp),
                        shape = RoundedCornerShape(20.dp)
                    )
                }
            }
        }

        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            Text(
                text = model.profile?.displayName ?: "",
                style = WhiskrTheme.typography.h1,
                color = WhiskrTheme.colors.onBackground
            )

            Text(
                text = "${model.profile?.handle}",
                style = WhiskrTheme.typography.body,
                color = WhiskrTheme.colors.secondary
            )

            if (!model.profile?.bio.isNullOrBlank()) {
                Text(
                    text = model.profile.bio,
                    style = WhiskrTheme.typography.body,
                    color = WhiskrTheme.colors.onBackground
                )
            }

            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                StatItem(
                    count = model.profile?.followingCount ?: 0,
                    label = stringResource(Res.string.following)
                )

                StatItem(
                    count = model.profile?.followersCount ?: 0,
                    label = stringResource(Res.string.followers)
                )
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        if (model.pets.isNotEmpty() || isMe) {
            Text(
                text = stringResource(Res.string.my_pets),
                style = WhiskrTheme.typography.h3,
                color = WhiskrTheme.colors.onBackground,
                modifier = Modifier.padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(12.dp))

            LazyRow(
                contentPadding = PaddingValues(horizontal = 16.dp),
                horizontalArrangement = Arrangement.spacedBy(16.dp)
            ) {
                items(model.pets) { pet ->
                    PetItem(
                        name = pet.name,
                        imageUrl = pet.avatarUrl,
                        onClick = { onPetClick(pet.id) }
                    )
                }
                if (isMe) {
                    item {
                        AddPetButton(onClick = onAddPetClick)
                    }
                }
            }
            Spacer(modifier = Modifier.height(24.dp))
        }

        HorizontalDivider(color = WhiskrTheme.colors.outline.copy(alpha = 0.5f))
    }
}

@Composable
private fun StatItem(count: Int, label: String) {
    Text(
        text = buildAnnotatedString {
            withStyle(
                SpanStyle(
                    fontWeight = FontWeight.Bold,
                    color = WhiskrTheme.colors.onBackground
                )
            ) {
                append("$count ")
            }
            withStyle(
                SpanStyle(
                    fontWeight = FontWeight.Normal,
                    color = WhiskrTheme.colors.secondary
                )
            ) {
                append(label)
            }
        },
        style = WhiskrTheme.typography.body
    )
}

@Composable
private fun PetItem(
    name: String,
    imageUrl: String?,
    onClick: () -> Unit
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        AvatarPlaceholder(
            avatarUrl = imageUrl,
            modifier = Modifier
                .size(64.dp)
                .clip(CircleShape)
                .border(1.dp, WhiskrTheme.colors.outline.copy(alpha = 0.5f), CircleShape)
                .customClickable(onClick = onClick)
        )

        Text(
            text = name,
            style = WhiskrTheme.typography.button,
            color = WhiskrTheme.colors.onBackground
        )
    }
}

@Composable
private fun AddPetButton(onClick: () -> Unit) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(4.dp)
    ) {
        Icon(
            painter = painterResource(Res.drawable.ic_add),
            contentDescription = stringResource(Res.string.add),
            tint = WhiskrTheme.colors.outline,
            modifier = Modifier
                .size(64.dp)
                .customClickable(onClick = onClick)
        )

        Text(
            text = stringResource(Res.string.add),
            style = WhiskrTheme.typography.button,
            color = WhiskrTheme.colors.onBackground
        )
    }
}