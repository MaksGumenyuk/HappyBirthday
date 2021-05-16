package com.example.happybirthday.utils

interface EventListener<T> {
    fun onEvent(t: T)
}