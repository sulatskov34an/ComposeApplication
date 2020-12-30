package ru.sulatskov.composeapplication.ui.screens.photos_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import dev.chrisbanes.accompanist.glide.GlideImage
import ru.sulatskov.composeapplication.model.network.dto.Photo
import ru.sulatskov.composeapplication.ui.main.MainViewModel
import ru.sulatskov.composeapplication.ui.main.NavScreen
import ru.sulatskov.composeapplication.ui.theme.JetTheme


@Composable
fun PhotosListScreen(
    navHostController: NavHostController,
    viewModel: MainViewModel
) {
    val photos: List<Photo> by viewModel.photosList.observeAsState(listOf())
    val isLoading: Boolean by viewModel.isLoading.observeAsState(false)
    PhotosListContent(photos, isLoading, navHostController)
}

@Composable
fun PhotosListContent(
    items: List<Photo>,
    isLoading: Boolean,
    navHostController: NavHostController
) {
    Scaffold(
        topBar = {
            TopAppBar(backgroundColor = JetTheme.colors.toolbar) {
                ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                    val (screenTitle) = createRefs()
                    Text(
                        text = "Photos list",
                        style = JetTheme.typography.textMedium,
                        color = JetTheme.colors.text,
                        modifier = Modifier.constrainAs(screenTitle) {
                            top.linkTo(parent.top)
                            bottom.linkTo(parent.bottom)
                            start.linkTo(parent.start, 12.dp)
                        }
                    )
                }
            }
        }
    ) {
        Surface(color = JetTheme.colors.background) {
            PhotosList(items = items, navHostController = navHostController)
        }
    }
}

@Composable
fun PhotosList(items: List<Photo>, navHostController: NavHostController) {
    LazyColumn(modifier = Modifier.padding(top = 8.dp)) {
        items(items) { photo ->
            PhotoListItem(navHostController = navHostController, photo = photo)
        }
    }
}

@Composable
fun PhotoListItem(
    navHostController: NavHostController,
    photo: Photo
) {
    Card(
        backgroundColor = JetTheme.colors.card,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier
            .padding(horizontal = 8.dp, vertical = 4.dp)
            .clickable(onClick = {
                navHostController.navigate(route = "${NavScreen.PhotosDetails.route}/${photo.urls?.regular}")
            })
            .fillMaxWidth()
    ) {
        ConstraintLayout(modifier = Modifier.fillMaxSize()) {
            val (image, createdAtTextDescription, createdAtText) = createRefs()
            photo.urls?.regular?.let {
                GlideImage(
                    data = it,
                    fadeIn = true,
                    contentScale = ContentScale.Crop,
                    loading = {
                        Box(Modifier.fillMaxSize()) {
                            CircularProgressIndicator(
                                color = JetTheme.colors.text,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    },
                    modifier = Modifier.height(120.dp).width(120.dp).constrainAs(image) {
                        top.linkTo(parent.top)
                        bottom.linkTo(parent.bottom)
                        start.linkTo(parent.start)
                    }
                )
            }

            Column(
                modifier = Modifier.fillMaxSize().constrainAs(createdAtTextDescription) {
                    top.linkTo(parent.top)
                    start.linkTo(image.end, margin = 16.dp)
                    bottom.linkTo(parent.bottom)
                }
            ) {
                Text(
                    text = "Created at",
                    style = JetTheme.typography.textSmall,
                    color = JetTheme.colors.text,
                    textAlign = TextAlign.Left,
                    modifier = Modifier.fillMaxSize()
                )
                photo.createdAt?.let {
                    Text(
                        text = it,
                        style = JetTheme.typography.textMedium,
                        color = JetTheme.colors.text,
                        textAlign = TextAlign.Left,
                        modifier = Modifier.fillMaxSize()
                    )
                }

            }
        }
    }
}