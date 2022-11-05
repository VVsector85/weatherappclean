package com.learning.weatherappclean.presentation.ui.components.dragdrop

import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.animateDpAsState
import androidx.compose.animation.core.tween
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.gestures.animateScrollBy
import androidx.compose.foundation.gestures.detectDragGesturesAfterLongPress
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.Card
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.State
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.unit.dp
import com.learning.weatherappclean.domain.model.WeatherCard
import kotlinx.coroutines.Job
import kotlinx.coroutines.launch

/**
 * not my code, took it from here:
 * https://stackoverflow.com/questions/64913067/reorder-lazycolumn-items-with-drag-drop
 * */

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun <T : Any> DragDropColumn(
    items: List<T>,
    contentPadding: PaddingValues,
    scrollToFirst: State<Pair<Boolean, Int>>,
    stopScrollToFirst: (() -> Unit)? = null,
    onSwap: ((Int, Int) -> Unit)? = null,
    itemContent: @Composable LazyItemScope.(index: Int, item: T) -> Unit
) {
    var overscrollJob by remember { mutableStateOf<Job?>(null) }
    val listState = rememberLazyListState()
    val scope = rememberCoroutineScope()
    val dragDropState = rememberDragDropState(listState) { fromIndex, toIndex ->
        onSwap?.invoke(fromIndex, toIndex)
    }

    LaunchedEffect(scrollToFirst.value.first) {
        if (scrollToFirst.value.first) {
            listState.scrollToItem(scrollToFirst.value.second)
            stopScrollToFirst?.invoke()
        }
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxHeight()
            .pointerInput(dragDropState) {
                detectDragGesturesAfterLongPress(
                    onDrag = { change, offset ->
                        change.consume()
                        dragDropState.onDrag(offset = offset)

                        if (overscrollJob?.isActive == true)
                            return@detectDragGesturesAfterLongPress

                        dragDropState
                            .checkForOverScroll()
                            .takeIf { it != 0f }
                            ?.let {
                                overscrollJob =
                                    scope.launch {
                                        dragDropState.state.animateScrollBy(
                                            it * 1.3f, tween(easing = FastOutLinearInEasing)
                                        )
                                    }
                            }
                            ?: run { overscrollJob?.cancel() }
                    },
                    onDragStart = { offset -> dragDropState.onDragStart(offset) },
                    onDragEnd = {
                        dragDropState.onDragInterrupted()
                        overscrollJob?.cancel()
                    },
                    onDragCancel = {
                        dragDropState.onDragInterrupted()
                        overscrollJob?.cancel()
                    },

                    )
            },
        state = listState,
        contentPadding = contentPadding,
        verticalArrangement = Arrangement.spacedBy(20.dp)
    ) {

        itemsIndexed(
            items = items,
            key = { _, item -> with(item as WeatherCard) { "$location$country$region" } }) { index, item ->
            DraggableItem(
                dragDropState = dragDropState,
                index = index, modifier = Modifier
            ) { isDragging ->
                val elevation by animateDpAsState(if (isDragging) 4.dp else 0.dp)

                Card(elevation = elevation) {
                    itemContent(index, item)
                }
            }
        }
    }
}

