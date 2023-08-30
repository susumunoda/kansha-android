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
    tabType: TabType,
    tabOptions: List<TabOption>,
    selectedTabIndex: Int,
    onSelectTabIndex: (Int) -> Unit
) {
    val indicator = @Composable { tabPositions: List<TabPosition> ->
        if (selectedTabIndex < tabPositions.size) {
            when (tabType) {
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
        tabOptions.forEachIndexed { index, tabOption ->
            Tab(
                selected = selectedTabIndex == index,
                onClick = { onSelectTabIndex(index) },
                modifier = Modifier.padding(dimensionResource(R.dimen.padding_medium))
            ) {
                Text(
                    stringResource(tabOption.titleId),
                    style = MaterialTheme.typography.titleMedium
                )
            }
        }
    }
}