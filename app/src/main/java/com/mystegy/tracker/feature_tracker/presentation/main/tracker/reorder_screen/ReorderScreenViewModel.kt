package com.mystegy.tracker.feature_tracker.presentation.main.tracker.reorder_screen

import android.app.Application
import android.widget.Toast
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.mystegy.tracker.feature_tracker.domain.models.Tracker
import com.mystegy.tracker.feature_tracker.domain.repository.TrackerRepository
import com.mystegy.tracker.feature_tracker.presentation.main.navArgs
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.launchIn
import kotlinx.coroutines.flow.onEach
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
import javax.inject.Inject


@HiltViewModel
class ReorderScreenViewModel @Inject constructor(
    private val application: Application,
    private val savedStateHandle: SavedStateHandle,
    private val repository: TrackerRepository
) : ViewModel() {

    private val _uiState = MutableStateFlow(ReorderUIState(args = savedStateHandle.navArgs()))
    val uiState = _uiState.asStateFlow()

    init {
        getTrackers()
    }

    fun onEvent(event: ReorderUIEvent) {
        when (event) {
            is ReorderUIEvent.Drag -> {
                _uiState.value = _uiState.value.copy(
                    trackers = _uiState.value.trackers.toMutableList().apply {
                        add(event.to, removeAt(event.from))
                    }
                )
            }

            ReorderUIEvent.Save -> {
                saveOrder()
            }
        }
    }

    private fun saveOrder() {
        viewModelScope.launch(Dispatchers.IO) {
            val trackers: MutableList<Tracker> = mutableListOf()
            var counter: Long = 0
            if (_uiState.value.args.group.isEmpty()) {
                _uiState.value.trackers.forEach { tracker ->
                    if (tracker.group.isEmpty()) {
                        trackers.add(tracker.copy(sort = counter))
                        counter++
                    } else {
                        for (T in _uiState.value.originalTrackers.filter { it.group.getOrNull(_uiState.value.args.index) == tracker.group[_uiState.value.args.index] }) {
                            trackers.add(T.copy(sort = counter))
                            counter++
                        }
                    }
                }
            } else {
                var flag = false
                _uiState.value.originalTrackers.forEach { tracker ->
                    if (tracker.group.getOrNull(_uiState.value.args.index) != _uiState.value.args.group) {
                        trackers.add(tracker.copy(sort = counter))
                        counter++
                    } else if (tracker.group.getOrNull(_uiState.value.args.index) == _uiState.value.args.group && !flag) {
                        _uiState.value.trackers.forEach {
                            trackers.add(it.copy(sort = counter))
                            counter++
                        }
                        flag = true
                    }
                }
            }
            repository.reorderTrackers(trackers)
            withContext(Dispatchers.Main) {
                Toast.makeText(application, "Trackers successfully reordered", Toast.LENGTH_SHORT)
                    .show()
            }
        }
    }

    private fun getTrackers() =
        repository.getTrackers().onEach { trackers ->
            _uiState.value = _uiState.value.copy(
                originalTrackers = trackers
            )
            _uiState.value = _uiState.value.copy(
                trackers = listOf()
            )
            if (_uiState.value.args.group.isBlank()) {
                trackers.sortedBy { it.sort }.groupBy {
                    if (it.group.isEmpty()) {
                        it.id
                    } else {
                        it.group[_uiState.value.args.index]
                    }
                }.forEach { (key, u) ->
                    if (key is Long) {
                        _uiState.value = _uiState.value.copy(
                            trackers = _uiState.value.trackers.toMutableList().apply {
                                add(u.first())
                            }
                        )
                    } else {
                        _uiState.value = _uiState.value.copy(
                            trackers = _uiState.value.trackers.toMutableList().apply {
                                add(u.first())
                            }
                        )
                    }
                }
            } else {
                trackers
                    .filter {
                        it.group.getOrNull(_uiState.value.args.index) == _uiState.value.args.group
                    }
                    .sortedBy {
                        it.sort
                    }
                    .groupBy {
                        if (it.group.getOrNull(_uiState.value.args.index + 1) == null) {
                            it.id
                        } else {
                            it.group[_uiState.value.args.index + 1]
                        }
                    }.forEach { (key, u) ->
                        if (key is Long) {
                            _uiState.value = _uiState.value.copy(
                                trackers = _uiState.value.trackers.toMutableList().apply {
                                    add(u.first())
                                }
                            )
                        } else {
                            _uiState.value = _uiState.value.copy(
                                trackers = _uiState.value.trackers.toMutableList().apply {
                                    add(u.first())
                                }
                            )
                        }
                    }
            }
        }.launchIn(viewModelScope)


}