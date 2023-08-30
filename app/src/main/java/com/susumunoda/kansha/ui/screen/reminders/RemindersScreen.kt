package com.susumunoda.kansha.ui.screen.reminders

import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
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
    var selectedTab by remember { mutableStateOf(upcomingTab) }

    ScaffoldWithStatusBarInsets {
        Tabs(
            type = TabType.PRIMARY,
            tabOptions = remindersTabs,
            selectedTab = selectedTab,
            onSelectTab = { selectedTab = it }
        )
        when (selectedTab) {
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