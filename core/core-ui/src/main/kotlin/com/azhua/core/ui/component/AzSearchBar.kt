package com.azhua.core.ui.component

import androidx.compose.animation.*
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.outlined.History
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.azhua.core.ui.theme.*

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AzSearchBar(
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    expanded: Boolean,
    onExpandedChange: (Boolean) -> Unit,
    modifier: Modifier = Modifier,
    placeholder: String = "Cari donghua...",
    suggestions: List<String> = emptyList(),
    recentSearches: List<String> = emptyList(),
) {
    SearchBar(
        inputField = {
            SearchBarDefaults.InputField(
                query = query,
                onQueryChange = onQueryChange,
                onSearch = {
                    onSearch(query)
                    onExpandedChange(false)
                },
                expanded = expanded,
                onExpandedChange = onExpandedChange,
                placeholder = {
                    Text(
                        text = placeholder,
                        style = MaterialTheme.typography.bodyLarge,
                        color = ColorTextTertiary,
                    )
                },
                leadingIcon = {
                    Icon(
                        Icons.Filled.Search,
                        contentDescription = "Cari",
                        tint = ColorIconDefault,
                    )
                },
                trailingIcon = {
                    if (expanded) {
                        IconButton(onClick = {
                            if (query.isNotEmpty()) {
                                onQueryChange("")
                            } else {
                                onExpandedChange(false)
                            }
                        }) {
                            Icon(
                                Icons.Filled.Close,
                                contentDescription = "Tutup",
                                tint = ColorIconDefault,
                            )
                        }
                    }
                },
                colors = SearchBarDefaults.inputFieldColors(
                    focusedTextColor = ColorTextPrimary,
                    unfocusedTextColor = ColorTextPrimary,
                    cursorColor = ColorPrimary,
                ),
            )
        },
        expanded = expanded,
        onExpandedChange = onExpandedChange,
        modifier = modifier,
        shape = SearchBarDefaults.fullScreenShape,
        colors = SearchBarDefaults.colors(
            containerColor = ColorSurfaceContainerHigh,
        ),
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxWidth(),
            contentPadding = PaddingValues(16.dp),
        ) {
            if (recentSearches.isNotEmpty() && query.isEmpty()) {
                item {
                    Text(
                        text = "Pencarian terakhir",
                        style = MaterialTheme.typography.labelLarge,
                        color = ColorTextSecondary,
                        modifier = Modifier.padding(bottom = 8.dp),
                    )
                }
                items(recentSearches) { recent ->
                    ListItem(
                        headlineContent = {
                            Text(
                                text = recent,
                                style = MaterialTheme.typography.bodyLarge,
                                color = ColorTextPrimary,
                            )
                        },
                        leadingContent = {
                            Icon(
                                Icons.Outlined.History,
                                contentDescription = null,
                                tint = ColorIconDefault,
                            )
                        },
                        modifier = Modifier.clickable {
                            onQueryChange(recent)
                            onSearch(recent)
                            onExpandedChange(false)
                        },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                    )
                }
            }

            if (suggestions.isNotEmpty()) {
                item {
                    Text(
                        text = "Saran",
                        style = MaterialTheme.typography.labelLarge,
                        color = ColorTextSecondary,
                        modifier = Modifier.padding(
                            top = if (recentSearches.isNotEmpty()) 16.dp else 0.dp,
                            bottom = 8.dp,
                        ),
                    )
                }
                items(suggestions) { suggestion ->
                    ListItem(
                        headlineContent = {
                            Text(
                                text = suggestion,
                                style = MaterialTheme.typography.bodyLarge,
                                color = ColorTextPrimary,
                            )
                        },
                        leadingContent = {
                            Icon(
                                Icons.Filled.Search,
                                contentDescription = null,
                                tint = ColorIconDefault,
                            )
                        },
                        modifier = Modifier.clickable {
                            onQueryChange(suggestion)
                            onSearch(suggestion)
                            onExpandedChange(false)
                        },
                        colors = ListItemDefaults.colors(containerColor = Color.Transparent),
                    )
                }
            }
        }
    }
}
