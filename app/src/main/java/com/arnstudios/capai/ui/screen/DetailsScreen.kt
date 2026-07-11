package com.arnstudios.capai.ui.screen

import android.annotation.SuppressLint
import android.widget.Toast
import androidx.activity.compose.BackHandler
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
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Fullscreen
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalClipboardManager
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.AnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import coil.compose.AsyncImage
import com.arnstudios.capai.R
import com.arnstudios.capai.domain.model.Length
import com.arnstudios.capai.ui.CapAiViewModel
import com.arnstudios.capai.ui.loading.CaptionGenerationLoadingScreen
import com.arnstudios.capai.ui.screen.components.ErrorDialog
import com.arnstudios.capai.ui.screen.components.ShareImageAndCaption
import com.arnstudios.capai.ui.theme.ScreenBackgroundGradient
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
    var selectedCaption by remember { mutableStateOf(AnnotatedString("")) }
    var isFullScreen by remember { mutableStateOf(false) }
    
    if (isFullScreen) {
        Dialog(
            onDismissRequest = { isFullScreen = false },
            properties = DialogProperties(usePlatformDefaultWidth = false)
        ) {
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(Color.Black),
                contentAlignment = Alignment.Center
            ) {
                AsyncImage(
                    model = viewModel.imageUri.collectAsState().value,
                    contentDescription = "Full screen image",
                    modifier = Modifier.fillMaxSize(),
                    contentScale = ContentScale.Fit
                )

                IconButton(
                    onClick = { isFullScreen = false },
                    modifier = Modifier
                        .align(Alignment.TopEnd)
                        .padding(16.dp)
                        .background(Color.Black.copy(alpha = 0.5f), RoundedCornerShape(50))
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                        contentDescription = "Close",
                        tint = Color.White
                    )
                }
            }
        }
    }

    BackHandler {
        if (result.isGenerating) {
            Toast.makeText(context, "Please wait until AI is working...", Toast.LENGTH_SHORT).show()
        } else {
            onBackArrowClick()
        }
    }

    LaunchedEffect(Unit) {
        viewModel.getImageCaption(context, selectedLength)
    }
    Scaffold(
        containerColor = Color.Transparent,
        snackbarHost = {
            SnackbarHost(snackBarHostState) { data ->
                Card(
                    modifier = Modifier
                        .padding(horizontal = 16.dp, vertical = 32.dp)
                        .fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(0xFF323232)),
                    elevation = CardDefaults.cardElevation(defaultElevation = 6.dp)
                ) {
                    Row(
                        modifier = Modifier.padding(16.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = Color(0xFF4CAF50),
                            modifier = Modifier.size(24.dp)
                        )
                        Spacer(modifier = Modifier.width(12.dp))
                        Text(
                            text = data.visuals.message,
                            color = Color.White,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }
        },
        topBar = {
            if (!result.isGenerating) {
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
                        containerColor = Color(0xFF6366F1),
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
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(ScreenBackgroundGradient)
        ) {
            if(result.isGenerating){
                CaptionGenerationLoadingScreen(
                    modifier = Modifier.padding(innerPadding)
                )
            } else if(result.capAI?.isSuccess == false){
                ErrorDialog(
                    errorMessage = result.capAI?.errorMessage ?: "Failed to generate captions",
                    onDismiss = onIsSuccessFalse
                )
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
                        Box(modifier = Modifier.fillMaxSize()) {
                            AsyncImage(
                                model = viewModel.imageUri.collectAsState().value,
                                contentDescription = "Selected image",
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(imageHeight),
                                contentScale = ContentScale.Fit,
                                alignment = Alignment.Center
                            )

                            IconButton(
                                onClick = { isFullScreen = true },
                                modifier = Modifier
                                    .align(Alignment.BottomEnd)
                                    .padding(12.dp)
                                    .background(Color.Black.copy(alpha = 0.4f), RoundedCornerShape(12.dp))
                                    .size(40.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Fullscreen,
                                    contentDescription = "Full Screen",
                                    tint = Color.White,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                        }
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
                        
                        LaunchedEffect(pagerState.currentPage) {
                            selectedCaption = AnnotatedString(captions[pagerState.currentPage].second)
                        }

                        HorizontalPager(
                            pagerState,
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(200.dp),
                        ) { page ->
                            val (platform, captionText) = captions[page]
                            val pageScrollState = rememberScrollState()
                            Card(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 4.dp, vertical = 8.dp),
                                shape = RoundedCornerShape(16.dp),
                                elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
                                colors = CardDefaults.cardColors(containerColor = Color.White)
                            ) {
                                Column(
                                    modifier = Modifier.fillMaxSize()
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
                                                else -> Color(0xFF6366F1)
                                            })
                                            .padding(horizontal = 16.dp, vertical = 6.dp),
                                        verticalAlignment = Alignment.CenterVertically
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
                                            modifier = Modifier.size(28.dp),
                                            tint = Color.Unspecified
                                        )
                                        Spacer(modifier = Modifier.width(12.dp))
                                        Text(text = platform, fontWeight = FontWeight.Bold, fontSize = 16.sp, color = Color.White)
                                    }
                                    Box(
                                        modifier = Modifier
                                            .fillMaxSize()
                                            .padding(12.dp)
                                    ){
                                        Text(text = captionText, fontSize = 14.sp,
                                            textAlign = TextAlign.Justify,
                                            modifier = Modifier
                                                .fillMaxSize()
                                                .verticalScroll(pageScrollState),
                                            color = Color.DarkGray)
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
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(50))
                                            .width(if (selected) 20.dp else 8.dp)
                                            .height(6.dp)
                                            .background(
                                                if (selected) Color(0xFF6366F1) else Color(0xFFE0E0E0)
                                            )
                                    )
                                    if (index != captions.lastIndex) {
                                        Spacer(modifier = Modifier.size(6.dp))
                                    }
                                }
                            }

                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(horizontal = 8.dp),
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
                                        .weight(1f)
                                        .height(56.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1))
                                ) {
                                    Text(
                                        text = "Copy",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
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
                                        .weight(1f)
                                        .height(56.dp),
                                    shape = RoundedCornerShape(16.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = Color(0xFF6366F1))
                                ) {
                                    Text(
                                        text = "Share",
                                        fontSize = 16.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = Color.White
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
