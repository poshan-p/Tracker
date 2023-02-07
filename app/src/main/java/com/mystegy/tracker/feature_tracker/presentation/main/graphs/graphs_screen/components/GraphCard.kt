package com.mystegy.tracker.feature_tracker.presentation.main.graphs.graphs_screen.components

import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.toArgb
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.mystegy.tracker.core.utils.toLocalDateTime
import com.mystegy.tracker.feature_tracker.domain.models.Tracker
import com.patrykandpatrick.vico.compose.axis.horizontal.bottomAxis
import com.patrykandpatrick.vico.compose.axis.vertical.startAxis
import com.patrykandpatrick.vico.compose.chart.Chart
import com.patrykandpatrick.vico.compose.chart.line.lineChart
import com.patrykandpatrick.vico.compose.component.shapeComponent
import com.patrykandpatrick.vico.compose.dimensions.dimensionsOf
import com.patrykandpatrick.vico.compose.legend.verticalLegend
import com.patrykandpatrick.vico.compose.legend.verticalLegendItem
import com.patrykandpatrick.vico.compose.m3.style.m3ChartStyle
import com.patrykandpatrick.vico.compose.style.ProvideChartStyle
import com.patrykandpatrick.vico.core.chart.line.LineChart
import com.patrykandpatrick.vico.core.chart.scale.AutoScaleUp
import com.patrykandpatrick.vico.core.component.shape.Shapes
import com.patrykandpatrick.vico.core.entry.ChartEntry
import com.patrykandpatrick.vico.core.entry.ChartEntryModelProducer
import com.patrykandpatrick.vico.core.entry.composed.plus
import java.time.LocalDate

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun LazyItemScope.GraphCard(trackers: List<Tracker>, title: String, onEdit: () -> Unit, onDelete: () -> Unit) {
    if (trackers.isNotEmpty()) {
        val dates = getDates().mapIndexed { index, localDate -> localDate to index.toFloat() }
        val producer = ChartEntryModelProducer(
            dates.map { pair ->
                Entry(
                    localDate = pair.first,
                    x = pair.second,
                    y = 0f
                )
            }
        ) + ChartEntryModelProducer(
            trackers.map { tracker ->
                tracker.items.reversed().filter { trackerItem ->
                    dates.find {
                        it.first.isEqual(
                            trackerItem.localDateTime.toLocalDateTime().toLocalDate()
                        )
                    } != null
                }.map { trackerItem ->
                    Entry(
                        localDate = trackerItem.localDateTime.toLocalDateTime().toLocalDate(),
                        x = dates.find {
                            it.first.isEqual(
                                trackerItem.localDateTime.toLocalDateTime().toLocalDate()
                            )
                        }!!.second,
                        y = if (trackerItem.volume != 0.0) trackerItem.volume.toFloat() else trackerItem.reps.toFloat()
                    )
                }
            }
        )
        Column(modifier = Modifier.animateItemPlacement()) {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(
                    text = title,
                    maxLines = 1,
                    overflow = TextOverflow.Ellipsis,
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.weight(1f)
                )
                IconButton(onClick = { onDelete.invoke() }) {
                    Icon(imageVector = Icons.Default.Delete, contentDescription = null)
                }
                IconButton(onClick = { onEdit.invoke() }) {
                    Icon(imageVector = Icons.Default.Edit, contentDescription = null)
                }
            }

            ProvideChartStyle(chartStyle = m3ChartStyle()) {
                Chart(
                    chart = lineChart(
                        lines = arrayListOf(
                            LineChart.LineSpec(
                                Color.Transparent.toArgb()
                            )
                        ).apply {
                            addAll(
                                entityColors.take(trackers.size).map {
                                    LineChart.LineSpec(
                                        Color(it).toArgb()
                                    )
                                }
                            )
                        }
                    ),
                    chartModelProducer = producer,
                    startAxis = startAxis(),
                    bottomAxis = bottomAxis(
                        valueFormatter = { value, chartValues ->
                            (chartValues.chartEntryModel.entries[0].getOrNull(value.toInt()) as Entry?)
                                ?.localDate
                                ?.run { "$dayOfMonth/$monthValue" }
                                .orEmpty()
                        }
                    ),
                    autoScaleUp = AutoScaleUp.Full,
                    isZoomEnabled = false
                )
            }
            trackers.forEachIndexed { index, tracker ->
                Row(verticalAlignment = Alignment.CenterVertically, modifier = Modifier.padding(start = 32.dp, top = 2.dp, bottom = 2.dp)) {
                    Box(
                        Modifier
                            .size(8.dp)
                            .background(Color(entityColors[index]), RoundedCornerShape(8.dp))
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(text = tracker.title, style = MaterialTheme.typography.bodySmall)
                }
            }
        }

    }
}

fun getDates(): List<LocalDate> {
    val date = LocalDate.now()
    return listOf(
        date,
        date.minusDays(1),
        date.minusDays(2),
        date.minusDays(3),
        date.minusDays(4),
        date.minusDays(5),
        date.minusDays(6)
    ).reversed()
}


class Entry(
    val localDate: LocalDate,
    override val x: Float,
    override val y: Float,
) : ChartEntry {
    override fun withY(y: Float) = Entry(
        localDate = this.localDate,
        x = this.x,
        y = y,
    )
}

val entityColors = arrayListOf(
    0xFFC1D4E3,
    0xFFBEB4D6,
    0xFFFADAE2,
    0xFFF8B3CA,
    0xFFCC97C1
)

