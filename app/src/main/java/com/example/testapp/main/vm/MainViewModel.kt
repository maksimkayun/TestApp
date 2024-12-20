package com.example.testapp.main.vm

import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.toRoute
import com.example.domain.data.repository.LocalStorageRepository
import com.example.domain.entity.ListElementEntity
import com.example.domain.usecases.ListUseCase
import com.example.testapp.main.MainScreenRoute
import kotlinx.coroutines.CoroutineExceptionHandler
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch
import timber.log.Timber

class MainViewModel(
    private val useCase: ListUseCase,
    private val localStorageRepository: LocalStorageRepository,
    private val handle: SavedStateHandle
) : ViewModel() {

    private val _state = MutableStateFlow<MainState>(MainState.Loading)
    private val exceptionHandler = CoroutineExceptionHandler { _, throwable ->
        viewModelScope.launch {
            _state.emit(MainState.Error(throwable.message ?: "Ошибочка"))
        }
    }

    val state: StateFlow<MainState>
        get() = _state

    init {
        loadContent()
    }

    private fun loadContent() {
        viewModelScope.launch(context = exceptionHandler) {
            val result = useCase.execute(Unit)
            _state.emit(MainState.Content(result))
        }
    }

    fun refresh() {
        loadContent()
    }

    fun like(elementEntity: ListElementEntity, like: Boolean) {
        localStorageRepository.like(elementEntity.id, like)
        loadContent() // Обновляем список после изменения лайка
    }
}