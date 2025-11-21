package com.example.apod_kt

import DatePickerBox
import android.os.Build
import android.os.Bundle
import android.util.Log
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
import androidx.compose.runtime.saveable.rememberSaveable
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
import com.example.apod_kt.ui.components.BottomBar
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
import android.os.Parcelable
import androidx.annotation.RequiresApi
import androidx.compose.foundation.gestures.scrollable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.CalendarToday
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.runtime.collectAsState
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.apod_kt.ui.ApodViewModel
import kotlinx.parcelize.Parcelize
import org.jetbrains.annotations.Async
import java.text.SimpleDateFormat
import java.time.ZoneId
import java.util.Date
import java.util.Locale
import java.util.TimeZone
import kotlin.time.ExperimentalTime
import kotlin.time.Instant


private const val TAG = "MainActivity"
class MainActivity : ComponentActivity() {

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        Log.d(TAG, "onCreate Called")
        enableEdgeToEdge()
        setContent {
            ApodktTheme {
                val viewModel: ApodViewModel = viewModel()
                var showDialog by remember { mutableStateOf(false) }
                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        BottomBar()
                    },
                    floatingActionButton = {
                        FloatingActionButton(onClick = { showDialog = true }) {
                            Icon(Icons.Filled.CalendarToday, "Add button")
                        }
                    }
                ) { innerPadding ->
                    Column (
                        modifier = Modifier
                            .statusBarsPadding().padding(innerPadding)
                    ) {
                        if (showDialog) {
                            DatePickerBox(
                                onDismiss = { showDialog = false },
                                onDateSelected = { date ->
                                    showDialog = false
                                    println(convertMillisToDate(date))
                                    viewModel.changeDate(convertMillisToDate(date))
//                                    millis to date yyyy-mm-dd conversion
//                                    viewModel.changeDate("2025-11-20")

                                }
                            )
                        }
                        apodScreen(viewModel = viewModel)
                    }
                }
            }
        }
    }

    override fun onResume() {
        super.onResume()
        Log.d(TAG, "onResume Called")
    }

    override fun onRestart() {
        super.onRestart()
        Log.d(TAG, "onRestart Called")
    }

    override fun onPause() {
        super.onPause()
        Log.d(TAG, "onPause Called")
    }

    override fun onStop() {
        super.onStop()
        Log.d(TAG, "onStop Called")
    }

    override fun onDestroy() {
        super.onDestroy()
        Log.d(TAG, "onDestroy Called")
    }

}

fun convertMillisToDate(millis: Long?): String {
    val formatter = SimpleDateFormat("yyyy-MM-dd", Locale.US)

    formatter.timeZone = TimeZone.getTimeZone("UTC")

    return formatter.format(Date(millis ?: 0L))
}

@Composable
fun apodScreen(viewModel: ApodViewModel = viewModel()) {
    val data by viewModel.data.collectAsState()
    val loading by viewModel.loading.collectAsState()

    Box (
        modifier = Modifier.fillMaxSize()
    ) {
        if (loading) {
            Box(
                modifier = Modifier.fillMaxSize(),
                contentAlignment = Alignment.Center
            ){
                CircularProgressIndicator()
            }
        } else {
            Column (
                modifier = Modifier.verticalScroll(rememberScrollState())
            ) {
                AsyncImage(
                    model = data.url,
                    contentDescription = "test",
                    modifier = Modifier.fillMaxWidth().aspectRatio(16f / 9f),
                    contentScale = ContentScale.Crop
                )
                Column (
                    modifier = Modifier.padding(20.dp)
                ) {
                    Text(data.title, style = MaterialTheme.typography.displayMedium, modifier = Modifier.padding(bottom = 20.dp))
                    Text( data.explanation, style = MaterialTheme.typography.bodyLarge)

                }
            }
        }
    }
}

//@Composable
//fun loadData() {
//    val client = HttpClient(OkHttp) {
//        install(ContentNegotiation) {
//            json(Json {
//                prettyPrint = true
//                isLenient = true
//                ignoreUnknownKeys = true
//            })
//        }
//    }
//    var loading by rememberSaveable {mutableStateOf(true)}
//    var responseText by rememberSaveable { mutableStateOf<Apod?>(null)}
//    var responseTextPre: String = ""
//    LaunchedEffect(Unit) {
//        try {
//            val response: Apod = client.get("https://api.nasa.gov/planetary/apod?api_key=ZtFjqXtx27meihPnioblbXIi5jzZqNDEqkMWK8g8").body()
//            responseText =  response
//            loading = false
//
//        } catch (e: Exception) {
//            e.printStackTrace()
//        }
//    }
//
//
//    Box(modifier = Modifier.fillMaxSize()) {
//
//        if (loading) {
//            Box(
//                modifier = Modifier.fillMaxSize(),
//                contentAlignment = Alignment.Center
//            ){
//                CircularProgressIndicator()
//            }
//        }
//        else {
//            Column (
//                modifier = Modifier.verticalScroll(rememberScrollState())
//            ) {
//                responseText?.run {
//                    url?.let {
//                        AsyncImage(
//                            model = it,
//                            contentDescription = "test",
//                            modifier = Modifier.fillMaxWidth().aspectRatio(16f / 9f),
//                            contentScale = ContentScale.Crop
//                        )
//                    }
//
//                    Box(
//                        modifier = Modifier.padding(20.dp)
//                    ){
//                        Column {
//                            title?.let { Text(it, style = MaterialTheme.typography.displayMedium) }
//                            explanation?.let { Text(it, style = MaterialTheme.typography.bodyLarge, modifier = Modifier.padding(top=20.dp))}
//                        }
//                    }
//                }
//            }
//
//        }
//
//
//    }
//}

//@Composable
//fun loadFakeData() {
//    var data by remember { mutableStateOf("loading...") }
//
//    LaunchedEffect(Unit) {
//        data = fetchData()
//    }
//
//    Text(data)
//}

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