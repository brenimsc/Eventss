package com.example.events.ui.viewmodel

import android.os.RemoteException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.events.data.model.Event
import com.example.events.data.model.State
import com.example.events.data.repository.EventsRepositoryInterface
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.collect
import kotlinx.coroutines.flow.onStart
import kotlinx.coroutines.launch

class EventsViewModel(private val repository: EventsRepositoryInterface) : ViewModel() {

    private val _currentStep = MutableLiveData<Int>().apply {
        value = 0
    }
    val currentStep: LiveData<Int> = _currentStep

    //esse currenStep eu uso pra controlar para troca de fragments.

    val listCheckins = repository.getListCheckins()

    private val _listNews = MutableLiveData<State<List<Event>>>()
    val listNews: LiveData<State<List<Event>>> get() = _listNews

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    val listFavorites = repository.getFavorites()

    private val _snackbar = MutableLiveData<String?>(null)
    val snackbar: LiveData<String?> get() = _snackbar

    fun onSnackBarShow() {
        _snackbar.value = null
    }


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
                        //para mostrar o dialog apenas quando for recarregar a pagina e nao quando mudar de fragment
                        _listNews.postValue(State.Loading)
                        delay(300)
                    }
                }
                .catch {
                    val exception = RemoteException(it.message)
                    _snackbar.value = it.message
                    _listNews.postValue(State.Error(exception))
                }
                .collect {
                    _listNews.postValue(State.Success(it))
                }
        }
    }

    fun hideDialog() {
        _loading.postValue(false)
    }

    fun showDialog() {
        _loading.postValue(true)
    }


}