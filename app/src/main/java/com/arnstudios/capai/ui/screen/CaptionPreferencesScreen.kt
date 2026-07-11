package com.arnstudios.capai.ui.screen

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import coil.compose.AsyncImage
import com.arnstudios.capai.domain.model.Length
import com.arnstudios.capai.ui.CapAiViewModel
import com.arnstudios.capai.ui.screen.components.ErrorDialog
import com.arnstudios.capai.ui.theme.ScreenBackgroundGradient
import com.arnstudios.capai.utils.NetworkUtils
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.saveable.rememberSaveable

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaptionPreferencesScreen(
    viewModel: CapAiViewModel,
    onBackArrowClick: () -> Unit,
    onGenerateCaptionClick : (selectedLength : Length) -> Unit
) {
    val context = LocalContext.current
    var showNoInternetError by remember { mutableStateOf(false) }

    BackHandler {
        onBackArrowClick()
    }

    if (showNoInternetError) {
        ErrorDialog(
            errorMessage = "Please check your internet connection and try again.",
            onDismiss = { showNoInternetError = false }
        )
    }

    var selectedIndex by rememberSaveable{ mutableIntStateOf(0) }
    var selectedLength by rememberSaveable{ mutableStateOf(Length.SHORT) }
    val options = listOf("Short", "Long")

    val imageHeight = 420.dp

    Scaffold(
        modifier = Modifier,
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = "Preferences",
                        modifier = Modifier.fillMaxWidth(),
                        textAlign = TextAlign.Center,
                        fontWeight = FontWeight.Bold,
                        fontSize = 20.sp
                    )
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
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(ScreenBackgroundGradient)
                .padding(16.dp),
            verticalArrangement = Arrangement.Top,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {


            Card(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(imageHeight),
                shape = RoundedCornerShape(20.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                colors = CardDefaults.cardColors(containerColor = Color.White)
            ) {
                if (viewModel.imageUri.collectAsState().value != null) {
                    AsyncImage(
                        model = viewModel.imageUri.collectAsState().value,
                        contentDescription = "Selected image",
                        modifier = Modifier.fillMaxSize(),
                        contentScale = ContentScale.Crop,
                        alignment = Alignment.Center
                    )
                } else {
                    Box(modifier = Modifier.fillMaxSize(), contentAlignment = Alignment.Center) {
                         Image(
                            painter = painterResource(id = com.arnstudios.capai.R.drawable.upload),
                            contentDescription = "No image",
                            modifier = Modifier.size(120.dp),
                            contentScale = ContentScale.Fit
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(32.dp))
            
            Text(
                text = "Caption Length",
                fontSize = 18.sp,
                fontWeight = FontWeight.Bold,
                color = Color(0xFF6366F1)
            )
            
            Spacer(modifier = Modifier.height(12.dp))

            SingleChoiceSegmentedButtonRow(
                modifier = Modifier.fillMaxWidth().padding(horizontal = 16.dp)
            ) {
                options.forEachIndexed { index, text ->
                    SegmentedButton(
                        selected = selectedIndex == index,
                        onClick = {
                            selectedIndex = index
                            selectedLength = if (index == 0) Length.SHORT else Length.LONG },
                        shape = SegmentedButtonDefaults.itemShape(
                            index = index,
                            count = options.size
                        ),
                        colors = SegmentedButtonDefaults.colors(
                            activeContainerColor = Color(0xFF6366F1),
                            activeContentColor = Color.White,
                            inactiveContainerColor = Color.White,
                            inactiveContentColor = Color(0xFF6366F1)
                        )
                    ) {
                        Text(text = text, fontWeight = FontWeight.ExtraBold)
                    }
                }
            }

            Spacer(modifier = Modifier.weight(1f))

            Button(
                onClick = {
                    if (NetworkUtils.isInternetAvailable(context)) {
                        viewModel.prepareForCaptionGeneration()
                        onGenerateCaptionClick(selectedLength)
                    } else {
                        showNoInternetError = true
                    }
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .height(56.dp),
                shape = RoundedCornerShape(16.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFF6366F1),
                    contentColor = Color.White
                )
            ) {
                Text(text = "Generate Captions ✨", fontSize = 18.sp, fontWeight = FontWeight.Bold)
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = "AI will analyze your image and create engaging captions tailored for all platforms.",
                fontSize = 12.sp,
                color = Color.Gray,
                textAlign = TextAlign.Center,
                modifier = Modifier.padding(horizontal = 24.dp)
            )

        }
    }

}
