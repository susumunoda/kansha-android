package com.susumunoda.kansha.ui.screen.reminders

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.susumunoda.kansha.R
import com.susumunoda.kansha.ui.component.ScaffoldWithStatusBarInsets
import com.susumunoda.kansha.ui.component.TabOption
import com.susumunoda.kansha.ui.component.TabType
import com.susumunoda.kansha.ui.component.Tabs

private val upcomingTab = TabOption(R.string.reminders_tab_upcoming)
private val recurringTab = TabOption(R.string.reminders_tab_recurring)
private val remindersTabs = listOf(upcomingTab, recurringTab)

@Composable
fun RemindersScreen() {
    var selectedTabIndex by rememberSaveable { mutableIntStateOf(0) }

    ScaffoldWithStatusBarInsets {
        Tabs(
            tabType = TabType.PRIMARY,
            tabOptions = remindersTabs,
            selectedTabIndex = selectedTabIndex,
            onSelectTabIndex = { selectedTabIndex = it }
        )
        when (remindersTabs[selectedTabIndex]) {
            upcomingTab -> {}
            recurringTab -> {}
        }
    }
}

@Preview
@Composable
fun RemindersScreenPreview() {
    RemindersScreen()
}