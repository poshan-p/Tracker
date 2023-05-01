@file:Suppress("DEPRECATION")

package com.mystegy.tracker.feature_tracker.presentation.main.tracker.tracker_screen.components

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.wrapContentHeight
import androidx.compose.material3.Button
import androidx.compose.material3.ElevatedCard
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import com.google.accompanist.pager.ExperimentalPagerApi
import com.google.accompanist.pager.HorizontalPager
import com.google.accompanist.pager.rememberPagerState
import com.mystegy.tracker.feature_tracker.domain.models.Tracker

@OptIn(ExperimentalPagerApi::class, ExperimentalMaterial3Api::class)
@Composable
fun GroupCard(
    modifier: Modifier = Modifier,
    trackers: List<Tracker>,
    group: String,
    track: (Int) -> Unit,
    onClick:(String) -> Unit
) {
    val pagerState = rememberPagerState(0)
    ElevatedCard(onClick = { onClick.invoke(group) }) {
        Column(modifier = modifier) {
            Text(
                text = group,
                style = MaterialTheme.typography.titleSmall,
                modifier = Modifier
                    .padding(8.dp)
                    .fillMaxWidth(),
                maxLines = 1,
                overflow = TextOverflow.Ellipsis,
                textAlign = TextAlign.Center
            )
            HorizontalPager(
                count = trackers.count(),
                state = pagerState,
                modifier = Modifier.wrapContentHeight()
            ) { page ->
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Text(
                        text = trackers[page].title,
                        style = MaterialTheme.typography.titleLarge,
                        modifier = Modifier
                            .align(Alignment.CenterHorizontally)
                            .padding(16.dp)
                    )
                }
            }
            Button(
                onClick = { track.invoke(pagerState.currentPage) }, modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp)
            ) {
                Text(text = "Track")
            }
            PagerIndicator(
                pagerState = pagerState,
                modifier = Modifier
                    .align(Alignment.CenterHorizontally)
                    .padding(horizontal =  16.dp, vertical = 8.dp),
                space = 4.dp,
                indicatorSize = 8.dp,
                indicatorCount = if (trackers.size < 5) trackers.size else 5,
                activeColor = MaterialTheme.colorScheme.primary
            ) {

            }
        }
    }
}