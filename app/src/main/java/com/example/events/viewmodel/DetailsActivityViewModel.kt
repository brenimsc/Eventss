package com.example.events.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.events.model.Checkin
import com.example.events.model.Event
import com.example.events.model.User
import com.example.events.repository.DetailsRepository
import com.google.gson.Gson
import kotlinx.coroutines.launch

class DetailsActivityViewModel(private val repository: DetailsRepository) : ViewModel() {

    private val _eventModel = MutableLiveData<Event>()
    val event: LiveData<Event> get() = _eventModel

    private val _error = MutableLiveData<Boolean>()
    val error: LiveData<Boolean> get() = _error

    private val _loading = MutableLiveData<Boolean>()
    val loading: LiveData<Boolean> get() = _loading

    private val _user = MutableLiveData<User?>()
    val user: LiveData<User?> get() = _user

    private val _sucessCheckin = MutableLiveData<Boolean>()
    val sucessCheckin: LiveData<Boolean> get() = _sucessCheckin

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
            _loading.value = true
            try {
                _eventModel.postValue(repository.getEventId(id))
                _error.value = false
                _loading.value = false
            } catch (e: Exception) {
                _error.value = true
                _loading.value = false
            }
        }
    }

    fun doCheckin(checkin: Checkin) {
        viewModelScope.launch {
            try {
                repository.doCheckin(checkin)
                _sucessCheckin.postValue(true)
                doCheckinIfSuccess()
            } catch (e: Exception) {
                _sucessCheckin.postValue(false)
            }
        }
    }

    private suspend fun doCheckinIfSuccess() {
        val event = _eventModel.value
        event?.checkin = !event?.checkin!!
        repository.alterCheckin(event)
    }


}