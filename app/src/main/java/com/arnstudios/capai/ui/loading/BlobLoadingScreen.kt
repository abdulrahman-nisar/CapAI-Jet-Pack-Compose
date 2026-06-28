package com.arnstudios.capai.ui.loading

import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathFillType
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.arnstudios.capai.ui.theme.ScreenBackgroundGradient
import kotlin.math.cos
import kotlin.math.sin

/**
 * AI-thinking pulsing gradient blob loader.
 *
 * - Organic blob shape with a noise-displaced edge (8 control points,
 *   quadratic-curve smoothed) instead of a plain circle.
 * - Radial gradient core (light purple -> deep purple).
 * - Two breathing halo rings behind the blob for ambient glow.
 * - Speed matches the "sped up" preview: full morph/breathe cycle
 *   roughly every ~1.1s for an energetic "actively thinking" feel.
 *
 * Pure Canvas drawing — no images/gifs, fully theme-able via the
 * colors passed in.
 */

private const val POINTS = 8
private const val BASE_RADIUS_DP = 30f

@Composable
fun PulsingBlobLoader(
    modifier: Modifier = Modifier,
    size: androidx.compose.ui.unit.Dp = 140.dp,
    coreColors: List<Color> = listOf(
        Color(0xFF9B8CF2),
        Color(0xFF6F5CE0),
        Color(0xFF4A3BB8)
    ),
    haloColor: Color = Color(0xFF6F5CE0)
) {
    val transition = rememberInfiniteTransition(label = "blob_loader")

    // Drives both the morph noise and the halo breathing — single time
    // driver keeps everything in sync, matching the web preview's `t`.
    val time by transition.animateFloat(
        initialValue = 0f,
        targetValue = (Math.PI * 2).toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(durationMillis = 1100, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "time"
    )

    Canvas(modifier = modifier.size(size)) {
        val center = Offset(this.size.width / 2f, this.size.height / 2f)
        val baseRadiusPx = BASE_RADIUS_DP.dp.toPx() * (this.size.minDimension / 280f)

        drawBreathingHalos(center, baseRadiusPx, time, haloColor)
        drawMorphingBlob(center, baseRadiusPx, time, coreColors)
    }
}

private fun DrawScope.drawBreathingHalos(
    center: Offset,
    baseRadiusPx: Float,
    time: Float,
    haloColor: Color
) {
    val outerR = baseRadiusPx * 1.93f + sin(time * 1.6f) * (baseRadiusPx * 0.13f)
    val midR = baseRadiusPx * 1.47f + sin(time * 1.6f + 0.5f) * (baseRadiusPx * 0.13f)

    drawCircle(color = haloColor.copy(alpha = 0.12f), radius = outerR, center = center)
    drawCircle(color = haloColor.copy(alpha = 0.18f), radius = midR, center = center)
}

private fun DrawScope.drawMorphingBlob(
    center: Offset,
    baseRadiusPx: Float,
    time: Float,
    coreColors: List<Color>
) {
    val breathe = sin(time * 2f) * (baseRadiusPx * 0.1f)
    val path = buildBlobPath(center, baseRadiusPx, time, breathe)

    val brush = Brush.radialGradient(
        colors = coreColors,
        center = Offset(
            center.x - baseRadiusPx * 0.3f,
            center.y - baseRadiusPx * 0.35f
        ),
        radius = baseRadiusPx * 2.1f
    )

    drawPath(path = path, brush = brush)
}

/**
 * Builds an organic blob outline: POINTS points placed around a circle,
 * each radius perturbed by layered sine/cosine noise (mirrors the JS
 * preview's blobPath()), then smoothed with quadratic Bezier segments
 * through midpoints so the edge has no hard corners.
 */
private fun buildBlobPath(
    center: Offset,
    baseRadius: Float,
    time: Float,
    radiusOffset: Float
): Path {
    val coords = ArrayList<Offset>(POINTS)
    for (i in 0 until POINTS) {
        val angle = (i.toFloat() / POINTS) * (2 * Math.PI).toFloat()
        val noise = sin(time + i * 1.3f) * (baseRadius * 0.13f) +
            cos(time * 0.7f + i * 2.1f) * (baseRadius * 0.1f)
        val r = baseRadius + noise + radiusOffset
        val x = center.x + cos(angle) * r
        val y = center.y + sin(angle) * r
        coords.add(Offset(x, y))
    }

    return Path().apply {
        fillType = PathFillType.NonZero
        moveTo(coords[0].x, coords[0].y)
        for (i in 0 until POINTS) {
            val curr = coords[i]
            val next = coords[(i + 1) % POINTS]
            val midX = (curr.x + next.x) / 2f
            val midY = (curr.y + next.y) / 2f
            quadraticBezierTo(curr.x, curr.y, midX, midY)
        }
        close()
    }
}

/**
 * Full loading screen wrapper — drop this in as your generation/loading
 * screen content. Pass an optional label that updates as work progresses.
 */
@Composable
fun BlobLoadingScreen(
    modifier: Modifier = Modifier,
    label: String = "Loading ads..."
) {
    Box(
        modifier = modifier
            .fillMaxSize()
            .background(ScreenBackgroundGradient),
        contentAlignment = Alignment.Center
    ) {
        Column(horizontalAlignment = Alignment.CenterHorizontally) {
            PulsingBlobLoader(size = 140.dp)
            Text(
                text = label,
                fontSize = 16.sp,
                fontWeight = FontWeight.Medium,
                color = Color(0xFF6F5CE0),
                modifier = Modifier.padding(top = 24.dp)
            )
        }
    }
}

@Preview(showBackground = true)
@Composable
private fun BlobLoadingScreenPreview() {
    BlobLoadingScreen()
}
