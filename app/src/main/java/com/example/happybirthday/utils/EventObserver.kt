package com.example.happybirthday.utils

import androidx.lifecycle.Observer

internal class EventObserver<T>(private val eventListener: EventListener<T>) :
    Observer<Event<T>> {
    override fun onChanged(event: Event<T>) {
        val consumedValue = event.consume()
        consumedValue?.let { eventListener.onEvent(it) }
    }

}