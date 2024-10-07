package com.example.testapp.main

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.domain.entity.ListElement
import com.example.testapp.R
import com.example.testapp.main.vm.MainState
import com.example.testapp.main.vm.MainViewModel
import org.koin.androidx.compose.koinViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun MainScreen(viewModel: MainViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    Column {
        TopAppBar(
            title = {
                Text(
                    text = "Title",
                    style = MaterialTheme.typography.titleLarge,
                    modifier = Modifier.padding(start = 32.dp)
                )
            },
            actions = {
                Icon(
                    painter = painterResource(id = R.drawable.icon),
                    contentDescription = null,
                    modifier = Modifier.padding(end = 18.dp)
                )
            })
        Box(modifier = Modifier.fillMaxWidth()) {
            when (val st = state) {
                is MainState.Content -> {
                    ContentState(list = st.list)
                }

                is MainState.Error -> {
                    Text(text = "Error")
                }

                MainState.Loading -> {
                    LoadingState()
                }
            }
        }
    }
}

@Composable
private fun LoadingState() {
    Box(modifier = Modifier.fillMaxSize()) {
        CircularProgressIndicator(
            modifier = Modifier
                .align(Alignment.Center)
        )
    }
}

@Composable
private fun ContentState(list: List<ListElement>) {
    LazyColumn {
        item {
            list.forEach { element ->
                ElementRow(element)
            }
        }
    }
}

@Composable
private fun ElementRow(element: ListElement) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(vertical = 8.dp, horizontal = 16.dp)
    ) {
        Box(
            modifier = Modifier
                .size(136.dp)
                .clip(shape = RoundedCornerShape(28.dp))
                .shadow(8.dp, shape = RoundedCornerShape(28.dp))
                .background(
                    brush = Brush.linearGradient(
                        colors = listOf(
                            MaterialTheme.colorScheme.primary,
                            MaterialTheme.colorScheme.secondary
                        )
                    )
                )
        ) {
            AsyncImage(
                model = element.image,
                contentDescription = null,
                modifier = Modifier
                    .fillMaxSize()
                    .clip(shape = RoundedCornerShape(28.dp)),
                contentScale = ContentScale.Crop
            )
        }
        Column(modifier = Modifier.padding(vertical = 8.dp, horizontal = 16.dp)) {
            Text(text = element.title, style = MaterialTheme.typography.headlineSmall)
            if (element.subtitle != null) {
                Text(
                    text = element.subtitle.toString(),
                    style = MaterialTheme.typography.titleMedium
                )
            }
            Button(
                onClick = { /*TODO*/ },
                modifier = Modifier.padding(top = 16.dp)
            ) {
                element.button?.title?.let {
                    Text(
                        text = it,
                        style = MaterialTheme.typography.labelLarge
                    )
                }
            }
        }
    }
}

@Preview
@Composable
fun MainScreenPreview() {
    MainScreen()
}