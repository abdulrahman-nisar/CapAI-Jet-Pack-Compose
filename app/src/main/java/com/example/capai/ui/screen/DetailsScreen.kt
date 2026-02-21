package com.example.capai.ui.screen

import android.annotation.SuppressLint
import androidx.activity.compose.BackHandler
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.pager.HorizontalPager
import androidx.compose.foundation.pager.rememberPagerState
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Snackbar
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.example.capai.R
import com.example.capai.domain.model.Length
import com.example.capai.ui.CapAiViewModel
import com.example.capai.ui.screen.components.ShareImageAndCaption
import kotlinx.coroutines.launch

@SuppressLint("CoroutineCreationDuringComposition")
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailsScreen(
    viewModel: CapAiViewModel,
    selectedLength: Length,
    onBackArrowClick: () -> Unit,
    onIsSuccessFalse : () -> Unit
){
    val imageHeight = 400.dp
    val result by viewModel.result.collectAsState()
    val context = LocalContext.current
    val clipBoardManager = LocalClipboardManager.current
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var selectedCaption = remember{ AnnotatedString("") }
    val scrollState = rememberScrollState()
    BackHandler {
        onBackArrowClick()
    }

    LaunchedEffect(Unit) {
        viewModel.getImageCaption(context, selectedLength)
    }
    Scaffold(
        snackbarHost = {
            SnackbarHost(snackBarHostState){ snackbarData ->
                Snackbar(
                    snackbarData = snackbarData,
                    containerColor = MaterialTheme.colorScheme.surfaceVariant,
                    contentColor = MaterialTheme.colorScheme.onSurface,
                    actionColor = MaterialTheme.colorScheme.primary,
                    shape = RoundedCornerShape(16.dp)
                )
            }
        },
        topBar = {
            TopAppBar(
                title = {
                    Box(
                        modifier = Modifier.fillMaxWidth(),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "Details",
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 20.sp
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = Color(0xFF1948a6),
                    titleContentColor = Color.White
                ),
                navigationIcon = {
                    IconButton(onClick = onBackArrowClick) {
                        Icon(
                            Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Back",
                            tint = Color.White,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                },
                actions = {
                    Spacer(modifier = Modifier.size(48.dp))
                }
            )
        }
    ) {
        innerPadding->
        if(result.isGenerating){
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .padding(innerPadding),
                contentAlignment = Alignment.Center
            ) {
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    verticalArrangement = Arrangement.Center
                ) {

                    val infiniteTransition = rememberInfiniteTransition(label = "loading")
                    val scale by infiniteTransition.animateFloat(
                        initialValue = 0.8f,
                        targetValue = 1.0f,
                        animationSpec = infiniteRepeatable(
                            animation = tween(800, easing = FastOutSlowInEasing),
                            repeatMode = RepeatMode.Reverse
                        ),
                        label = "scale"
                    )

                    Box(
                        modifier = Modifier.scale(scale),
                        contentAlignment = Alignment.Center
                    ) {
                        CircularProgressIndicator(
                            modifier = Modifier.size(64.dp),
                            color = Color(0xFF1948a6),
                            strokeWidth = 6.dp,
                            strokeCap = StrokeCap.Round
                        )
                    }

                    Spacer(modifier = Modifier.height(24.dp))

                    Text(
                        text = "Generating Captions...",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Medium,
                        color = Color(0xFF1948a6)
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Please wait while AI creates your captions",
                        fontSize = 13.sp,
                        color = Color.Gray,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }else if(result.capAI?.isSuccess == false){
            scope.launch {
                snackBarHostState.showSnackbar(
                    result.capAI?.errorMessage ?: "Failed to generate captions",
                    withDismissAction = true)
                onIsSuccessFalse()
            }

        } else {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(innerPadding)
                .padding(12.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(imageHeight),
                    shape = RoundedCornerShape(12.dp),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    AsyncImage(
                        model = viewModel.imageUri.collectAsState().value,
                        contentDescription = "Selected image",
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(imageHeight),
                        contentScale = ContentScale.Fit,
                        alignment = Alignment.Center
                    )
                }

                Spacer(modifier = Modifier.height(25.dp))

                val capAI = result.capAI
                val captions = listOf(
                    "Instagram" to (capAI?.instagramCaption ?: ""),
                    "Facebook" to (capAI?.facebookCaption ?: ""),
                    "Twitter" to (capAI?.twitterCaption ?: ""),
                    "Pinterest" to (capAI?.pinterestCaption ?: ""),
                    "LinkedIn" to (capAI?.linkedinCaption ?: ""),
                    "Threads" to (capAI?.threadCaption ?: ""),
                    "Snapchat" to (capAI?.snapChatCaption ?: ""),
                    "TikTok" to (capAI?.tiktokCaption ?: "")
                ).filter { it.second.isNotBlank() }

                if (captions.isEmpty()) {
                    Text(text = "No captions generated", fontSize = 16.sp)
                } else {

                    val pagerState = rememberPagerState { captions.size }

                    HorizontalPager(
                        pagerState,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(200.dp),
                    ) { page ->
                        val (platform, captionText) = captions[page]
                        Card(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(8.dp),
                            shape = RoundedCornerShape(8.dp),
                            elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                        ) {
                            Column(
                                modifier = Modifier
                                    .fillMaxWidth(),
                                verticalArrangement = Arrangement.Top,
                                horizontalAlignment = Alignment.Start
                            ) {
                                Row(
                                    modifier = Modifier.fillMaxWidth()
                                        .background(color = when(platform) {
                                            "Instagram" -> Color(0xFFDA5D88)
                                            "Facebook" -> Color(0xFF1877F2)
                                            "Twitter" -> Color(0xFF1DA1F2)
                                            "Pinterest" -> Color(0xFFE60023)
                                            "LinkedIn" -> Color(0xFF0A66C2)
                                            "Threads" -> Color(0xFF000000)
                                            "Snapchat" -> Color(0xFFFFC107)
                                            "TikTok" -> Color(0xFF000000)
                                            else -> Color(0xFFE1306C)
                                        }),
                                    verticalAlignment = Alignment.CenterVertically,
                                    horizontalArrangement = Arrangement.Start
                                ) {
                                    Icon(
                                        painter = when (platform) {
                                            "Instagram" -> painterResource(id = R.mipmap.instagram_foreground)
                                            "Facebook" -> painterResource(id = R.mipmap.facebook_foreground)
                                            "Twitter" -> painterResource(id = R.mipmap.twitter_foreground)
                                            "Pinterest" -> painterResource(id = R.mipmap.pinterest_foreground)
                                            "LinkedIn" -> painterResource(id = R.mipmap.linkldin_foreground)
                                            "Threads" -> painterResource(id = R.mipmap.threads_foreground)
                                            "Snapchat" -> painterResource(id = R.mipmap.snapchat_foreground)
                                            "TikTok" -> painterResource(id = R.mipmap.tiktok_foreground)
                                            else -> painterResource(id = R.mipmap.instagram_foreground)
                                        },
                                        contentDescription = "$platform Icon",
                                        modifier = Modifier.size(40.dp),
                                        tint = Color.Unspecified
                                    )
                                    Spacer(modifier = Modifier.size(8.dp))
                                    Text(text = platform, fontWeight = FontWeight.Bold, fontSize = 16.sp)
                                }
                                Box(
                                    modifier = Modifier
                                        .background(
                                            color = when(platform) {
                                                "Instagram" -> Color(0xFFA86C80)
                                                "Facebook" -> Color(0xFF688BC8)
                                                "Twitter" -> Color(0xFF4D93B1)
                                                "Pinterest" -> Color(0xFFAA5E6E)
                                                "LinkedIn" -> Color(0xFF6186B3)
                                                "Threads" -> Color(0xFF635353)
                                                "Snapchat" -> Color(0xFFA1996D)
                                                "TikTok" -> Color(0xFF4C4141)
                                                else -> Color(0xFFFFE6F0)
                                            }
                                        )
                                ){
                                    Text(text = captionText, fontSize = 14.sp,
                                        textAlign = TextAlign.Justify,
                                        modifier = Modifier
                                            .padding(16.dp)
                                            .fillMaxSize()
                                            .verticalScroll(scrollState))
                                }
                            }
                        }
                    }

                    Column(
                        modifier = Modifier
                            .fillMaxWidth(),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        Row(
                            horizontalArrangement = Arrangement.Center,
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier.padding(vertical = 8.dp)
                        ) {
                            captions.indices.forEach { index ->
                                val selected = pagerState.currentPage == index
                                if(selected){
                                    selectedCaption = AnnotatedString(captions[index].second)
                                }
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(50))
                                        .width(if (selected) 24.dp else 12.dp)
                                        .height(4.dp)
                                        .background(
                                            if (selected) Color(0xFF1948a6) else Color(0xFFE0E0E0)
                                        )
                                )
                                if (index != captions.lastIndex) {
                                    Spacer(modifier = Modifier.size(8.dp))
                                }
                            }
                        }

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(horizontal = 16.dp),
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Button(
                                onClick = {
                                    clipBoardManager.setText(
                                        annotatedString = selectedCaption
                                    )
                                    scope.launch {
                                        snackBarHostState.showSnackbar("Caption copied to clipboard")
                                    }
                                },
                                modifier = Modifier
                                    .weight(1f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "Copy",
                                    fontWeight = FontWeight.Medium
                                )
                            }

                            Button(
                                onClick = {
                                    clipBoardManager.setText(
                                        annotatedString = selectedCaption
                                    )
                                    scope.launch {
                                        snackBarHostState.showSnackbar(
                                            "Caption copied to clipboard",
                                            withDismissAction = true)
                                    }
                                    ShareImageAndCaption(
                                        context,
                                        viewModel.imageUri.value!!,
                                        selectedCaption.text
                                    )
                                },
                                modifier = Modifier
                                    .weight(1f),
                                shape = RoundedCornerShape(8.dp)
                            ) {
                                Text(
                                    text = "Share",
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }

                    }
                }

        }
        }
    }

}