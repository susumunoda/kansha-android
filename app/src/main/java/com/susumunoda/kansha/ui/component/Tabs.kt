package com.susumunoda.kansha.ui.component

import androidx.annotation.StringRes
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Tab
import androidx.compose.material3.TabPosition
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.res.stringResource
import com.susumunoda.kansha.R

class TabOption(@StringRes val titleId: Int)

enum class TabType { PRIMARY, SECONDARY }

@Composable
fun Tabs(
    type: TabType,
    tabOptions: List<TabOption>,
    selectedTab: TabOption,
    onSelectTab: (TabOption) -> Unit
) {
    val selectedTabIndex = tabOptions.indexOf(selectedTab)
    val indicator = @Composable { tabPositions: List<TabPosition> ->
        if (selectedTabIndex < tabPositions.size) {
            when (type) {
                TabType.PRIMARY -> {
                    val width by animateDpAsState(
                        label = "tab animation",
                        targetValue = tabPositions[selectedTabIndex].contentWidth
                    )
                    TabRowDefaults.PrimaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                        width = width
                    )
                }

                TabType.SECONDARY -> {
                    TabRowDefaults.SecondaryIndicator(
                        Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex])
                    )
                }
            }
        }
    }
    TabRow(
        selectedTabIndex = selectedTabIndex,
        indicator = indicator
    ) {
        tabOptions.forEach {
            Tab(
                selected = selectedTab == it,
                onClick = { onSelectTab(it) },
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
            ) {
                Text(
                    stringResource(it.titleId),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}