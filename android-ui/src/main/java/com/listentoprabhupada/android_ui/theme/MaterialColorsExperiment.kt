package com.listentoprabhupada.android_ui.theme

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp

@Composable
fun MaterialColorsExperiment() {
    LazyColumn(
        modifier = Modifier
            .padding(horizontal = 8.dp)
            .padding(top = 20.dp)
            .fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(16.dp),
    ) {
        item {
            Text(
                "primary",
                color = MaterialTheme.colorScheme.onPrimary,
                modifier = Modifier.fillMaxWidth().height(45.dp).background(MaterialTheme.colorScheme.primary)
            )
        }

        item {
            Text(
                "onPrimary",
                color = MaterialTheme.colorScheme.primary,
                modifier = Modifier.fillMaxWidth().height(45.dp).background(MaterialTheme.colorScheme.onPrimary)
            )
        }

        item {
            Text(
                "primaryContainer",
                color = MaterialTheme.colorScheme.onPrimaryContainer,
                modifier = Modifier.fillMaxWidth().height(45.dp).background(MaterialTheme.colorScheme.primaryContainer)
            )
        }
        item {
            Text(
                "onPrimaryContainer",
                color = MaterialTheme.colorScheme.primaryContainer,
                modifier = Modifier.fillMaxWidth().height(45.dp)
                    .background(MaterialTheme.colorScheme.onPrimaryContainer)
            )
        }

        item {
            Text(
                "secondary",
                color = MaterialTheme.colorScheme.onSecondary,
                modifier = Modifier.fillMaxWidth().height(45.dp).background(MaterialTheme.colorScheme.secondary)
            )
        }
        item {
            Text(
                "onSecondary",
                color = MaterialTheme.colorScheme.secondary,
                modifier = Modifier.fillMaxWidth().height(45.dp).background(MaterialTheme.colorScheme.onSecondary)
            )
        }
        item {
            Text(
                "secondaryContainer",
                color = MaterialTheme.colorScheme.onSecondaryContainer,
                modifier = Modifier.fillMaxWidth().height(45.dp)
                    .background(MaterialTheme.colorScheme.secondaryContainer)
            )
        }
        item {
            Text(
                "onSecondaryContainer",
                color = MaterialTheme.colorScheme.secondaryContainer,
                modifier = Modifier.fillMaxWidth().height(45.dp)
                    .background(MaterialTheme.colorScheme.onSecondaryContainer)
            )
        }
        item {
            Text(
                "tertiary",
                color = MaterialTheme.colorScheme.onTertiary,
                modifier = Modifier.fillMaxWidth().height(45.dp).background(MaterialTheme.colorScheme.tertiary)
            )
        }
        item {
            Text(
                "onTertiary",
                color = MaterialTheme.colorScheme.tertiary,
                modifier = Modifier.fillMaxWidth().height(45.dp).background(MaterialTheme.colorScheme.onTertiary)
            )
        }
        item {
            Text(
                "tertiaryContainer",
                color = MaterialTheme.colorScheme.onTertiaryContainer,
                modifier = Modifier.fillMaxWidth().height(45.dp).background(MaterialTheme.colorScheme.tertiaryContainer)
            )
        }
        item {
            Text(
                "onTertiaryContainer",
                color = MaterialTheme.colorScheme.tertiaryContainer,
                modifier = Modifier.fillMaxWidth().height(45.dp)
                    .background(MaterialTheme.colorScheme.onTertiaryContainer)
            )
        }
        item {
            Text(
                "error",
                color = MaterialTheme.colorScheme.onError,
                modifier = Modifier.fillMaxWidth().height(45.dp).background(MaterialTheme.colorScheme.error)
            )
        }
        item {
            Text(
                "onError",
                color = MaterialTheme.colorScheme.error,
                modifier = Modifier.fillMaxWidth().height(45.dp).background(MaterialTheme.colorScheme.onError)
            )
        }
        item {
            Text(
                "errorContainer",
                color = MaterialTheme.colorScheme.onErrorContainer,
                modifier = Modifier.fillMaxWidth().height(45.dp).background(MaterialTheme.colorScheme.errorContainer)
            )
        }
        item {
            Text(
                "onErrorContainer",
                color = MaterialTheme.colorScheme.errorContainer,
                modifier = Modifier.fillMaxWidth().height(45.dp).background(MaterialTheme.colorScheme.onErrorContainer)
            )
        }
        item {
            Text(
                "background",
                color = MaterialTheme.colorScheme.onBackground,
                modifier = Modifier.fillMaxWidth().height(45.dp).background(MaterialTheme.colorScheme.background)
            )
        }
        item {
            Text(
                "onBackground",
                color = MaterialTheme.colorScheme.background,
                modifier = Modifier.fillMaxWidth().height(45.dp).background(MaterialTheme.colorScheme.onBackground)
            )
        }
        item {
            Text(
                "surface",
                color = MaterialTheme.colorScheme.onSurface,
                modifier = Modifier.fillMaxWidth().height(45.dp).background(MaterialTheme.colorScheme.surface)
            )
        }
        item {
            Text(
                "onSurface",
                color = MaterialTheme.colorScheme.surface,
                modifier = Modifier.fillMaxWidth().height(45.dp).background(MaterialTheme.colorScheme.onSurface)
            )
        }
        item {
            Text(
                "surfaceVariant",
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.fillMaxWidth().height(45.dp).background(MaterialTheme.colorScheme.surfaceVariant)
            )
        }
        item {
            Text(
                "onSurfaceVariant",
                color = MaterialTheme.colorScheme.surfaceVariant,
                modifier = Modifier.fillMaxWidth().height(45.dp).background(MaterialTheme.colorScheme.onSurfaceVariant)
            )
        }
        item {
            Text(
                "outline",
                color = MaterialTheme.colorScheme.inverseSurface,
                modifier = Modifier.fillMaxWidth().height(45.dp).background(MaterialTheme.colorScheme.outline)
            )
        }
        item {
            Text(
                "inverseSurface",
                color = MaterialTheme.colorScheme.outline,
                modifier = Modifier.fillMaxWidth().height(45.dp).background(MaterialTheme.colorScheme.inverseSurface)
            )
        }
        item {
            Text(
                "inverseOnSurface",
                modifier = Modifier.fillMaxWidth().height(45.dp).background(MaterialTheme.colorScheme.inverseOnSurface)
            )
        }
        item {
            Text(
                "inversePrimary",
                modifier = Modifier.fillMaxWidth().height(45.dp).background(MaterialTheme.colorScheme.inversePrimary)
            )
        }
        item {
            Text(
                "surfaceTint",
                modifier = Modifier.fillMaxWidth().height(45.dp).background(MaterialTheme.colorScheme.surfaceTint)
            )
        }
    }
}
