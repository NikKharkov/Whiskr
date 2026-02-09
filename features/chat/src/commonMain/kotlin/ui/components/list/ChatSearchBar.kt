package ui.components.list

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import org.example.whiskr.components.WhiskrTextField
import org.example.whiskr.theme.WhiskrTheme
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import whiskr.features.chat.generated.resources.Res
import whiskr.features.chat.generated.resources.clear_search
import whiskr.features.chat.generated.resources.ic_close
import whiskr.features.chat.generated.resources.ic_search
import whiskr.features.chat.generated.resources.search_hint

@Composable
fun ChatSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    modifier: Modifier = Modifier
) {
    WhiskrTextField(
        value = query,
        onValueChange = onQueryChange,
        modifier = modifier.fillMaxWidth(),
        hint = stringResource(Res.string.search_hint),
        shape = RoundedCornerShape(32.dp),
        leadingIcon = {
            Icon(
                painter = painterResource(Res.drawable.ic_search),
                contentDescription = null,
                tint = WhiskrTheme.colors.onBackground.copy(alpha = 0.5f),
                modifier = Modifier.size(16.dp)
            )
        },
        trailingIcon = if (query.isNotEmpty()) {
            {
                IconButton(onClick = { onQueryChange("") }) {
                    Icon(
                        painter = painterResource(Res.drawable.ic_close),
                        contentDescription = stringResource(Res.string.clear_search),
                        tint = WhiskrTheme.colors.onBackground,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        } else null,
        imeAction = ImeAction.Search
    )
}