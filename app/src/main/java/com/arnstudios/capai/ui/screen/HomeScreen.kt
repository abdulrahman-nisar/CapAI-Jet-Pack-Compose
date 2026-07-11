package com.arnstudios.capai.ui.screen

import androidx.compose.animation.*
import androidx.compose.animation.core.tween
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Menu
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arnstudios.capai.ui.CapAiViewModel
import com.arnstudios.capai.ui.screen.components.DeleteConfirmationDialog
import com.arnstudios.capai.ui.screen.components.DrawerContent
import com.arnstudios.capai.ui.screen.components.HistoryListItem
import com.arnstudios.capai.ui.screen.components.ShimmerHistoryItem
import com.arnstudios.capai.ui.theme.ScreenBackgroundGradient
import com.arnstudios.capai.domain.model.CapAI
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: CapAiViewModel,
    onNewBtnClick : () -> Unit,
    onHistoryItemClick: (Int) -> Unit
) {
    val historyList by viewModel.historyList.collectAsState()
    val drawerSate = rememberDrawerState(initialValue = DrawerValue.Closed)
    val snackBarHostState = remember { SnackbarHostState() }
    val scope = rememberCoroutineScope()
    var isLoading by remember { mutableStateOf(true) }
    var itemToDelete by remember { mutableStateOf<CapAI?>(null) }

    LaunchedEffect(Unit) {
        delay(1200) // Brief delay to show off the beautiful shimmer effect
        isLoading = false
    }

    if (itemToDelete != null) {
        DeleteConfirmationDialog(
            onDismissRequest = { itemToDelete = null },
            onConfirm = {
                itemToDelete?.let { 
                    viewModel.deleteCaptionFromHistory(it)
                    scope.launch {
                        snackBarHostState.showSnackbar("Caption deleted successfully")
                    }
                }
                itemToDelete = null
            }
        )
    }

    ModalNavigationDrawer(
        drawerContent = {
            DrawerContent()
        },
        drawerState = drawerSate
    ) {
        Scaffold(
            modifier = Modifier,
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
                TopAppBar(
                    title = {
                        Text(
                            text = "Capshot AI",
                            modifier = Modifier
                                .fillMaxWidth(),
                            textAlign = TextAlign.Center,
                            fontWeight = FontWeight.Bold,
                            fontSize = 22.sp
                        )

                    },
                    colors = TopAppBarDefaults.topAppBarColors(
                        containerColor = Color(0xFF6366F1),
                        titleContentColor = Color.White
                    ),
                    navigationIcon = {
                        IconButton(
                            onClick = {
                                scope.launch {
                                    drawerSate.open()
                                }
                            }
                        ) {
                            Icon(
                                Icons.Default.Menu,"Menu",
                                tint = Color.White,
                                modifier = Modifier
                                    .padding(start = 8.dp)
                                    .size(30.dp)
                            )
                        }
                    },
                    actions = {
                        Spacer(modifier = Modifier.size(48.dp))
                    }
                )
            },
            floatingActionButton = {
                ExtendedFloatingActionButton(
                    onClick = onNewBtnClick,
                    icon = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add",
                            tint = Color.White
                        )},
                    text = { Text(text = "New Caption") },
                    containerColor = Color(0xFF6366F1),
                    contentColor = Color.White,
                    modifier = Modifier
                        .padding(16.dp)
                )
            }
        ) { innerpadding ->
            Box(
                modifier = Modifier
                    .fillMaxSize()
                    .background(ScreenBackgroundGradient)
            ) {
                AnimatedContent(
                    targetState = if (isLoading) "loading" else if (historyList.isEmpty()) "empty" else "content",
                    transitionSpec = {
                        fadeIn(animationSpec = tween(500)) togetherWith fadeOut(animationSpec = tween(500))
                    },
                    label = "historyTransition"
                ) { state ->
                    when (state) {
                        "loading" -> {
                            LazyColumn(
                                modifier = Modifier
                                    .padding(innerpadding)
                                    .padding(top = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                items(5) {
                                    ShimmerHistoryItem()
                                }
                            }
                        }
                        "empty" -> {
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .padding(innerpadding),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                     Icon(
                                        imageVector = Icons.Default.Add,
                                        contentDescription = null,
                                        modifier = Modifier.size(64.dp),
                                        tint = Color.LightGray
                                    )
                                    Spacer(modifier = Modifier.height(16.dp))
                                    Text(
                                        text = "No history yet\nTap 'New' to create your first caption",
                                        textAlign = TextAlign.Center,
                                        style = MaterialTheme.typography.bodyLarge,
                                        color = Color.Gray
                                    )
                                }
                            }
                        }
                        "content" -> {
                            LazyColumn(
                                modifier = Modifier
                                    .padding(innerpadding)
                                    .padding(top = 8.dp, bottom = 8.dp),
                                verticalArrangement = Arrangement.spacedBy(8.dp)
                            ) {
                                itemsIndexed(historyList) { index, item ->
                                    HistoryListItem(
                                        capAI = item,
                                        onClick = { onHistoryItemClick(index) },
                                        onDelete = {
                                            itemToDelete = item
                                        }
                                    )

                                    if(index == historyList.lastIndex){
                                        Spacer(modifier = Modifier.height(100.dp))
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
    }
}
