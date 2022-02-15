package com.example.events.ui.viewmodel

import android.os.RemoteException
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.events.data.model.Checkin
import com.example.events.data.model.Event
import com.example.events.data.model.State
import com.example.events.data.model.User
import com.example.events.data.repository.DetailsRepositoryInterface
import com.google.gson.Gson
import kotlinx.coroutines.launch

class DetailsActivityViewModel(private val repository: DetailsRepositoryInterface) : ViewModel() {

    private val _eventModel = MutableLiveData<Event>()

    private val _event = MutableLiveData<State<Event>>()
    val event: LiveData<State<Event>> get() = _event


    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    private val _sucessCheckin = MutableLiveData<Boolean>()
    val sucessCheckin: LiveData<Boolean> get() = _sucessCheckin

    private val _snackbar = MutableLiveData<String?>(null)
    val snackbar: LiveData<String?> get() = _snackbar

    fun onSnackBarShow() {
        _snackbar.value = null
    }

    fun saveUser(user: User) {
        repository.saveUser(user)
    }

    fun getUser() {
        _user.postValue(Gson().fromJson(repository.getUser(), User::class.java))
    }

    fun alterToFavorite(event: Event) {
        viewModelScope.launch {
            repository.alterToFavorites(event)
        }
    }

    fun getEvent(id: Int) {
        viewModelScope.launch {
            _event.postValue(State.Loading)
            try {
                val event = repository.getEventId(id)
                _event.postValue(State.Success(event))
                _eventModel.postValue(event)
            } catch (e: Exception) {
                val exception = RemoteException("Não foi possível conectar")
                _snackbar.value = exception.message
                _event.postValue(State.Error(exception))
            }
        }
    }

    fun doCheckin(checkin: Checkin) {
        viewModelScope.launch {
            _loading.value = true
            try {
                repository.doCheckin(checkin)
                _sucessCheckin.postValue(true)
                doCheckinIfSuccess()
            } catch (e: Exception) {
                _sucessCheckin.postValue(false)
            } finally {
                _loading.value = false
            }
        }
    }

    private suspend fun doCheckinIfSuccess() {
        val event = _eventModel.value
        event?.checkin = !event?.checkin!!
        repository.alterCheckin(event)
    }

    fun showDialog() {
        _loading.value = true
    }

    fun hideDialog() {
        _loading.value = false
    }


}