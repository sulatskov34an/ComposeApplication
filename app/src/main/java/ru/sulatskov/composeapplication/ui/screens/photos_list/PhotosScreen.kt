package ru.sulatskov.composeapplication.ui.screens.photos_list

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyItemScope
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import dev.chrisbanes.accompanist.glide.GlideImage
import ru.sulatskov.composeapplication.R
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
    PhotosListContent(photos, navHostController)
}

@Composable
fun PhotosListContent(
    items: List<Photo>,
    navHostController: NavHostController
) {
    Scaffold(
        topBar = {
            TopAppBar(backgroundColor = JetTheme.colors.toolbar) {
                ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                    val (screenTitle) = createRefs()
                    Text(
                        text = stringResource(R.string.home_text),
                        style = JetTheme.typography.toolbarTitle,
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
    LazyGrid(
        items = items,
        rows = 2,
        hPadding = 8
    ) { photo ->
        PhotoListItem(navHostController = navHostController, photo = photo)
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
                navHostController.navigate(route = "${NavScreen.PhotosDetails.route}/${photo.id}")
            })
            .fillMaxWidth()
    ) {
        photo.urls?.regular?.let {
            GlideImage(
                data = it,
                fadeIn = true,
                contentScale = ContentScale.None,
                loading = {
                    Box(Modifier.fillMaxSize()) {
                        CircularProgressIndicator(
                            color = JetTheme.colors.text,
                            modifier = Modifier.align(Alignment.Center)
                        )
                    }
                },
                modifier = Modifier
                    .height(120.dp)
                    .fillMaxSize()
            )
        }
    }
}

@Composable
fun <T> LazyGrid(
    items: List<T> = listOf(),
    rows: Int = 3,
    hPadding: Int = 0,
    itemContent: @Composable LazyItemScope.(T) -> Unit
) {
    val chunkedList = items.chunked(rows)
    LazyColumn(modifier = Modifier.padding(horizontal = hPadding.dp)) {
        itemsIndexed(items = chunkedList) { index, item ->
            if (index == 0) {
                Spacer(modifier = Modifier.preferredHeight(8.dp))
            }

            Row {
                item.forEachIndexed { rowIndex, item ->
                    Box(
                        modifier = Modifier
                            .weight(1F)
                            .align(Alignment.Top),
                        contentAlignment = Alignment.Center
                    ) {
                        itemContent(item)
                    }
                }
                repeat(rows - item.size) {
                    Box(
                        modifier = Modifier
                            .weight(1F)
                            .padding(2.dp)
                    ) {}
                }
            }
        }
    }
}