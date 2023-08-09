@file:OptIn(ExperimentalFoundationApi::class)

package com.susumunoda.kansha.ui.component

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.PagerScope
import androidx.compose.foundation.pager.PagerState
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.dimensionResource
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import com.susumunoda.kansha.R
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.launch

class PagerController(val pagerState: PagerState, private val scope: CoroutineScope) {
    fun goToPrevious() {
        if (pagerState.currentPage > 0) {
            scope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage - 1)
            }
        }
    }

    fun goToNext() {
        if (pagerState.currentPage < pagerState.pageCount - 1) {
            scope.launch {
                pagerState.animateScrollToPage(pagerState.currentPage + 1)
            }
        }
    }
}

@Composable
fun rememberPagerController(pageCount: Int): PagerController {
    val pagerState = rememberPagerState { pageCount }
    val scope = rememberCoroutineScope()
    return remember { PagerController(pagerState, scope) }
}

@Composable
fun HorizontalPagerWithIndicator(
    pagerState: PagerState,
    modifier: Modifier = Modifier,
    userScrollEnabled: Boolean = true,
    showPageIndicator: Boolean = true,
    pageIndicatorPadding: Dp = dimensionResource(R.dimen.padding_medium),
    pageContent: @Composable PagerScope.(page: Int) -> Unit
) {
    Column(modifier = modifier.fillMaxHeight()) {
        HorizontalPager(
            state = pagerState,
            // weight here needed so that parent column will allocate space for page indicator
            modifier = Modifier.weight(1f),
            userScrollEnabled = userScrollEnabled,
            pageContent = pageContent
        )
        if (showPageIndicator) {
            HorizontalPageIndicator(
                totalPages = pagerState.pageCount,
                currentPage = pagerState.currentPage,
                modifier = Modifier.padding(pageIndicatorPadding)
            )
        }
    }
}

@Composable
private fun HorizontalPageIndicator(
    totalPages: Int,
    currentPage: Int,
    modifier: Modifier = Modifier,
    indicatorPadding: Dp = 5.dp,
    indicatorSize: Dp = 10.dp,
    currentPageIndicatorColor: Color = MaterialTheme.colorScheme.onBackground,
    nonCurrentPageIndicatorColor: Color = currentPageIndicatorColor.copy(alpha = 0.2f)
) {
    Row(
        modifier = modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.Center,
        verticalAlignment = Alignment.CenterVertically
    ) {
        repeat(totalPages) { iteration ->
            val color =
                if (currentPage == iteration) currentPageIndicatorColor else nonCurrentPageIndicatorColor
            Box(
                modifier = Modifier
                    .padding(indicatorPadding)
                    .clip(CircleShape)
                    .background(color)
                    .size(indicatorSize)
            )
        }
    }
}