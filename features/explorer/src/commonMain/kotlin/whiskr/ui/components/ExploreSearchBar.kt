package org.example.whiskr.ui.components

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import org.example.whiskr.components.WhiskrTextField
import org.example.whiskr.extensions.customClickable
import org.jetbrains.compose.resources.painterResource
import org.jetbrains.compose.resources.stringResource
import whiskr.features.explorer.generated.resources.Res
import whiskr.features.explorer.generated.resources.ic_arrow_back
import whiskr.features.explorer.generated.resources.searchbar_hint

@Composable
fun ExploreSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: () -> Unit,
    isSearching: Boolean,
    onBackClick: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 8.dp),
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        if (isSearching) {
            Icon(
                painter = painterResource(Res.drawable.ic_arrow_back),
                contentDescription = null,
                modifier = Modifier.customClickable(onClick = onBackClick)
            )
        }

        WhiskrTextField(
            value = query,
            onValueChange = onQueryChange,
            hint = stringResource(Res.string.searchbar_hint),
            imeAction = ImeAction.Search,
            onImeAction = onSearch,
            modifier = Modifier.weight(1f)
        )
    }
}