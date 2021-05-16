package com.example.happybirthday.viewModel

import android.view.View.INVISIBLE
import android.view.View.VISIBLE
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.SavedStateHandle
import androidx.lifecycle.ViewModel
import com.example.happybirthday.utils.Event


class HappyBirthdayViewModel(private val state: SavedStateHandle) : ViewModel() {

    companion object {
        private const val NAME = "name"
        private const val DATE_OF_BIRTHDAY = "dateOfBirthday"

    }

    private val _name: MutableLiveData<String> = state.getLiveData(NAME)
    private val _dateOfBirthday: MutableLiveData<String> = state.getLiveData(DATE_OF_BIRTHDAY)
    private val _birthdayScreenBtnVisibility = MutableLiveData(if (_name.value.isNullOrEmpty() || _dateOfBirthday.value.isNullOrEmpty()) INVISIBLE else VISIBLE)
    private val _imageFilePath = MutableLiveData("")
    private val _newDestination: MutableLiveData<Event<Int>> = MutableLiveData()

    val name: LiveData<String> = _name
    val dateOfBirthday: LiveData<String> = _dateOfBirthday
    val birthdayScreenBtnVisibility: LiveData<Int> = _birthdayScreenBtnVisibility
    val imageFilePath: LiveData<String> = _imageFilePath

    fun setName(name: String) {
        _name.value = name
        state.set(NAME, name)
        _birthdayScreenBtnVisibility.value =
            if (_name.value.isNullOrEmpty() || _dateOfBirthday.value.isNullOrEmpty()) INVISIBLE else VISIBLE
    }

    fun setDateOfBirthday(dateOfBirthday: String) {
        _dateOfBirthday.value = dateOfBirthday
        state.set(DATE_OF_BIRTHDAY, dateOfBirthday)
        _birthdayScreenBtnVisibility.value =
            if (_name.value.isNullOrEmpty() || _dateOfBirthday.value.isNullOrEmpty()) INVISIBLE else VISIBLE
    }

    fun setImageFilePath(filePath: String?) {
        _imageFilePath.value = filePath
    }

    fun getNewDestination(): LiveData<Event<Int>> {
        return _newDestination
    }

    fun setNewDestination(destinationId: Int) {
        _newDestination.value = Event(destinationId)
    }

}