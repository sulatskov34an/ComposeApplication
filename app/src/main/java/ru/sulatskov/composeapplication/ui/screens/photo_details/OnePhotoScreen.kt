package ru.sulatskov.composeapplication.ui.screens.photo_details

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.ConstraintLayout
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material.*
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import androidx.navigation.NavHostController
import androidx.navigation.compose.navigate
import dev.chrisbanes.accompanist.glide.GlideImage
import ru.sulatskov.composeapplication.ui.main.NavScreen
import ru.sulatskov.composeapplication.ui.theme.JetTheme

@Composable
fun PhotoDetailScreen(navHostController: NavHostController, id: String) {

    Scaffold(
        topBar = {
            TopAppBar(backgroundColor = JetTheme.colors.toolbar) {
                ConstraintLayout(modifier = Modifier.fillMaxSize()) {
                 }
            }
        }
    ) {
        Surface(color = JetTheme.colors.background) {
            ConstraintLayout {
                val (button, image) = createRefs()

                Button(onClick = { navHostController.navigate(NavScreen.PhotosList.route) },
                    modifier = Modifier.constrainAs(button) {
                        top.linkTo(parent.top, margin = 16.dp)
                    }) {
                    Text(text = "Back")
                }

                GlideImage(
                    data = id,
                    fadeIn = true,
                    contentScale = ContentScale.Crop,
                    loading = {
                        Box(Modifier.fillMaxSize()) {
                            CircularProgressIndicator(
                                color = Color.Green,
                                modifier = Modifier.align(Alignment.Center)
                            )
                        }
                    },
                    modifier = Modifier.constrainAs(image) {
                        top.linkTo(button.bottom, margin = 16.dp)
                    }
                )
            }
        }
    }



}