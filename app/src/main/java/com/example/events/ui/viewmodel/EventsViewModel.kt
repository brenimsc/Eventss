package com.example.events.ui.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.events.data.model.Event
import com.example.events.data.repository.EventsRepository
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class EventsViewModel(val repository: EventsRepository) : ViewModel() {

    private val _currentStep = MutableLiveData<Int>().apply {
        value = 0
    }
    val currentStep: LiveData<Int> = _currentStep

    val listCheckins = repository.getListCheckins()

    private val _listEvents = MutableLiveData<List<Event>>()
    val listEvents: LiveData<List<Event>> get() = _listEvents

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> get() = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    val listFavorites = repository.getFavorites()

    fun goFragmentEvents() {
        _currentStep.value = 0
    }

    fun goFragmentFavorites() {
        _currentStep.value = 1
    }

    fun goFragmentsCheckins() {
        _currentStep.value = 2
    }

    fun alterToFavorites(event: Event) {
        viewModelScope.launch {
            repository.alterToFavorites(event)
        }
    }

    fun getListEvents(error: Boolean) {
        viewModelScope.launch {
            repository.getListEvents()
                .onStart {
                    if (error) {
                        _loading.postValue(true) //para mostrar o dialog apenas quando for recarregar a pagina e nao quando mudar de fragment
                        delay(500)
                    }
                }
                .catch {
                    _error.postValue(true)
                    hideDialog()
                }
                .collect {
                    hideDialog()
                    when (it) {
                        is Exception -> {
                            _error.postValue(true)
                            return@collect
                        }
                    }
                    _error.postValue(false)
                    _listEvents.postValue(it as List<Event>?)
                }
        }
    }

    private fun hideDialog() {
        _loading.postValue(false)
    }


}