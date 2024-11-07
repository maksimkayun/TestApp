package com.example.testapp.main

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.example.domain.entity.ListElementEntity
import com.example.testapp.details.DetailsScreenRoute
import com.example.testapp.main.vm.MainState
import com.example.testapp.main.vm.MainViewModel
import com.example.testapp.ui.view.Like
import kotlinx.coroutines.flow.collectLatest
import org.koin.androidx.compose.koinViewModel

@Composable
fun MainScreen(navController: NavController, viewModel: MainViewModel = koinViewModel()) {
    val state by viewModel.state.collectAsState()
    // Добавляем эффект для обновления при возврате на экран
    LaunchedEffect(Unit) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            // Когда возвращаемся на главный экран, обновляем данные
            viewModel.refresh()
        }
    }

    Box(modifier = Modifier.fillMaxSize()) {
        when (val st = state) {
            is MainState.Content -> {
                ContentState(
                    navController = navController,
                    list = st.list
                ) { element, like ->
                    viewModel.like(element, like)
                }
            }
            is MainState.Error -> {
                ErrorState(st.message)
            }
            MainState.Loading -> {
                Text(text = "Загрузка...")
            }
        }
    }
}

@Composable
fun ErrorState(message: String) {
    Text(modifier = Modifier.fillMaxWidth(), text = message)
}

@Composable
fun ContentState(
    navController: NavController,
    list: List<ListElementEntity>,
    onLike: (ListElementEntity, Boolean) -> Unit
) {
    Column(modifier = Modifier.fillMaxSize()) {
        list.forEach { element ->
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 16.dp)
                    .clickable { navController.navigate(DetailsScreenRoute(element.id)) }
            ) {
                AsyncImage(
                    model = element.image,
                    contentDescription = null,
                    modifier = Modifier
                        .size(96.dp)
                        .clip(shape = RoundedCornerShape(28.dp)),
                    contentScale = ContentScale.Crop
                )
                Column(
                    modifier = Modifier
                        .padding(horizontal = 16.dp)
                        .weight(1f)
                ) {
                    Text(
                        text = element.title,
                        style = MaterialTheme.typography.headlineSmall,
                        fontWeight = FontWeight.Bold,
                        modifier = Modifier.padding(top = 16.dp)
                    )
                    Text(
                        text = "${element.subtitle}",
                        style = MaterialTheme.typography.titleMedium,
                        fontWeight = FontWeight.Normal
                    )

                }
                val like = remember { mutableStateOf(element.like) }
                Like(like = like, modifier = Modifier.align(Alignment.CenterVertically))
                LaunchedEffect(like.value) {
                    onLike.invoke(element, like.value)
                }

            }
        }
    }
}
