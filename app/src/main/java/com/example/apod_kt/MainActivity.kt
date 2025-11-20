package com.example.apod_kt

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.aspectRatio
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.statusBarsPadding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.apod_kt.ui.theme.ApodktTheme
import io.ktor.client.HttpClient
import io.ktor.client.engine.okhttp.OkHttp
import kotlinx.coroutines.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.util.fastCbrt
import coil.compose.AsyncImage
import io.ktor.client.call.body
import io.ktor.client.request.get
import io.ktor.client.statement.bodyAsText
import kotlinx.serialization.*
import kotlinx.serialization.json.*
import kotlinx.serialization.Serializable
import kotlinx.serialization.SerialName
import io.ktor.client.plugins.contentnegotiation.ContentNegotiation
import io.ktor.serialization.kotlinx.json.json
import io.ktor.client.call.body
import kotlinx.serialization.json.Json

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            ApodktTheme {
                Scaffold(modifier = Modifier.fillMaxSize()) { innerPadding ->
                    Column (
                        modifier = Modifier
                            .statusBarsPadding()
                    ) {
                        loadData()
//                        loadFakeData()
//                        manualCor()
                    }
//                    Greeting(
//                        name = "Android",
//                        modifier = Modifier.padding(innerPadding)
//                    )
                }
            }
        }
    }
}

@Serializable
data class Apod(
    val copyright: String?,
    val date: String,
    val explanation: String,
    val hdurl: String,
    @SerialName("media_type")
    val mediaType: String,
    @SerialName("service_version")
    val serviceType: String,
    val title: String,
    val url: String
)

@Composable
fun loadData() {
    val client = HttpClient(OkHttp) {
        install(ContentNegotiation) {
            json(Json {
                prettyPrint = true
                isLenient = true
                ignoreUnknownKeys = true
            })
        }
    }
    var loading by remember {mutableStateOf(true)}
    var responseText by remember { mutableStateOf<Apod?>(null)}
    var responseTextPre: String = ""
    LaunchedEffect(Unit) {
        try {
            val response: Apod = client.get("https://api.nasa.gov/planetary/apod?api_key=ZtFjqXtx27meihPnioblbXIi5jzZqNDEqkMWK8g8").body()
            responseText =  response
            loading = false

        } catch (e: Exception) {
            e.printStackTrace()
        }
    }


    Box(modifier = Modifier.fillMaxSize()) {

        if (loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator()
            }
        }
        else {
            Column (
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                responseText?.run {
                    url?.let {
                        AsyncImage(
                            model = it,
                            contentDescription = "test",
                            modifier = Modifier.fillMaxWidth().aspectRatio(16f / 9f),
                            contentScale = ContentScale.Crop
                        )
                    }

                    Box(
                        modifier = Modifier.padding(20.dp)
                    ){
                        Column {
                            title?.let { Text(it, style = MaterialTheme.typography.displayMedium) }
                            explanation?.let { Text(it, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top=20.dp))}
                        }
                    }
                }
            }

        }


    }
}

@Composable
fun loadFakeData() {
//    var data by remember { mutableStateOf("loading...") }
//
//    LaunchedEffect(Unit) {
//        data = fetchData()
//    }
//
//    Text(data)
}

@Composable
fun manualCor() {
    var data by remember { mutableStateOf("")}
    val scope = rememberCoroutineScope()

    Column {
        Button(onClick = {
            scope.launch {
                data = fetchData()
            }
        }) {
            Text("Click to load")
        }
        Text(data)
    }
}

suspend fun fetchData(): String {
    delay(1000)
    return "Data from Server"
}

//@Preview(showBackground = true)
//@Composable
//fun GreetingPreview() {
//    ApodktTheme {
//        Greeting("Android")
//    }
//}