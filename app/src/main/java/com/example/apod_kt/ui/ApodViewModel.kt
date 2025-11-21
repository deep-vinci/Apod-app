package com.example.apod_kt.ui

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.apod_kt.model.Apod
import io.ktor.client.HttpClient
import io.ktor.client.call.body
import io.ktor.client.engine.okhttp.OkHttp
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.client.request.get
import io.ktor.serialization.kotlinx.json.json
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch
import kotlinx.serialization.json.Json

class ApodViewModel : ViewModel() {
    private val _data = MutableStateFlow<Apod>(Apod())
    private val _loading = MutableStateFlow(true)
    val data: StateFlow<Apod> = _data.asStateFlow()
    val loading: StateFlow<Boolean> = _loading.asStateFlow()

    init {
        viewModelScope.launch {
            updateData()
        }
    }
    private suspend fun updateData() {
        val client = HttpClient(OkHttp) {
            install(ContentNegotiation) {
                json(Json {
                    prettyPrint = true
                    isLenient = true
                    ignoreUnknownKeys = true
                })
            }
        }
        try {
            val response: Apod = client.get("https://api.nasa.gov/planetary/apod?api_key=ZtFjqXtx27meihPnioblbXIi5jzZqNDEqkMWK8g8").body()
            _data.value =  response
            _loading.value = false

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}