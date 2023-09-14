package com.susumunoda.kansha.ui.screen.reminders

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.tooling.preview.Preview
import com.susumunoda.compose.material3.ScaffoldWithStatusBarInsets
import com.susumunoda.compose.material3.TabOption
import com.susumunoda.compose.material3.TabType
import com.susumunoda.compose.material3.Tabs
import com.susumunoda.kansha.R

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