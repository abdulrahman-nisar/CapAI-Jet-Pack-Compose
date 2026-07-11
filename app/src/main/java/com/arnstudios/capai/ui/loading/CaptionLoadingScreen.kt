package com.arnstudios.capai.ui.loading

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.tween
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.blur
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arnstudios.capai.ui.theme.ScreenBackgroundGradient
import kotlinx.coroutines.delay
import kotlin.math.sin

private val AccentPurple = Color(0xFF6F5CE0)

private const val TYPE_DELAY_MS = 45L
private const val HOLD_DELAY_MS = 1100L
private const val FADE_DELAY_MS = 300L
private const val GAP_DELAY_MS = 200L

@Composable
fun TypewriterCaptionLoader(
    modifier: Modifier = Modifier,
    phrases: List<String> = listOf(
        "Analyzing your photo",
        "Finding the right words",
        "Writing captions..."
    )
) {
    var phraseIndex by remember { mutableStateOf(0) }
    var displayedText by remember { mutableStateOf("") }
    var visible by remember { mutableStateOf(true) }

    // Drives the typing -> hold -> fade -> next-phrase loop.
    LaunchedEffect(Unit) {
        while (true) {
            val current = phrases[phraseIndex]

            // Type out current phrase.
            for (i in 1..current.length) {
                displayedText = current.take(i)
                delay(TYPE_DELAY_MS)
            }

            // Hold
            delay(HOLD_DELAY_MS)

            // Fade out, swap phrase, fade in.
            visible = false
            delay(FADE_DELAY_MS)
            phraseIndex = (phraseIndex + 1) % phrases.size
            displayedText = ""
            visible = true
            delay(GAP_DELAY_MS)
        }
    }

    // Continuous glow pulse for the cursor — sine wave like the web preview.
    val transition = rememberInfiniteTransition(label = "cursor_glow")
    val glowPhase by transition.animateFloat(
        initialValue = 0f,
        targetValue = (2 * Math.PI).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1400, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "glow_phase"
    )
    val glowIntensity = (sin(glowPhase) + 1f) / 2f // 0..1
    val cursorAlpha = 0.4f + glowIntensity * 0.6f
    val glowRadius = (4 + glowIntensity * 6).dp

    Column(
        modifier = modifier,
        horizontalAlignment = Alignment.CenterHorizontally
    ) {
        // Typewriter text + glowing cursor.
        Row(
            verticalAlignment = Alignment.CenterVertically,
            modifier = Modifier.padding(16.dp)
        ) {
            AnimatedContent(
                targetState = visible,
                label = "phrase_fade"
            ) { isVisible ->
                Text(
                    text = if (isVisible) displayedText else "",
                    fontSize = 22.sp, // Slightly larger for center focus
                    fontWeight = FontWeight.Bold,
                    color = AccentPurple,
                    textAlign = TextAlign.Center
                )
            }

            Box(
                modifier = Modifier
                    .padding(start = 6.dp)
                    .width(3.dp)
                    .height(28.dp)
                    .graphicsLayer { alpha = cursorAlpha }
            ) {
                // Soft glow halo behind the cursor bar.
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .blur(glowRadius)
                        .background(AccentPurple.copy(alpha = 0.6f), RoundedCornerShape(1.dp))
                )
                // Solid cursor bar on top.
                Box(
                    modifier = Modifier
                        .matchParentSize()
                        .background(AccentPurple, RoundedCornerShape(1.dp))
                )
            }
        }
    }
}

@Composable
fun CaptionGenerationLoadingScreen(
    modifier: Modifier = Modifier
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBackgroundGradient),
        contentAlignment = Alignment.Center
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            // Minimalist centered loader focus
            TypewriterCaptionLoader()
        }
    }
}

