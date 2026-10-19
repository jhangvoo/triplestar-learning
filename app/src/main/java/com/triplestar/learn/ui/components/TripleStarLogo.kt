package com.triplestar.learn.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.Dp
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.triplestar.learn.ui.theme.StarGoldPrimary

@Composable
fun TripleStarLogoIcon(
    modifier: Modifier = Modifier,
    iconSize: Dp = 44.dp
) {
    val primaryBlue = Color(0xFF1E3A8A) // Deep educational blue
    val goldColor = StarGoldPrimary

    Box(
        modifier = modifier
            .size(iconSize)
            .clip(RoundedCornerShape(12.dp))
            .background(
                Brush.linearGradient(
                    colors = listOf(primaryBlue, Color(0xFF3B82F6))
                )
            ),
        contentAlignment = Alignment.Center
    ) {
        Canvas(modifier = Modifier.size(iconSize * 0.75f)) {
            val w = size.width
            val h = size.height

            // Draw abstract open book / upward learning path base
            val path = Path().apply {
                moveTo(w * 0.15f, h * 0.7f)
                quadraticBezierTo(w * 0.5f, h * 0.5f, w * 0.85f, h * 0.7f)
                lineTo(w * 0.85f, h * 0.85f)
                quadraticBezierTo(w * 0.5f, h * 0.65f, w * 0.15f, h * 0.85f)
                close()
            }
            drawPath(path, color = Color.White.copy(alpha = 0.25f), style = Fill)

            // Draw 3 ascending stars representing growth and achievement
            // Small star (left)
            drawCircle(color = goldColor, radius = w * 0.08f, center = Offset(w * 0.25f, h * 0.45f))
            // Medium star (center)
            drawCircle(color = goldColor, radius = w * 0.12f, center = Offset(w * 0.5f, h * 0.3f))
            // Large star (right, top)
            drawCircle(color = goldColor, radius = w * 0.16f, center = Offset(w * 0.75f, h * 0.2f))
        }
    }
}

@Composable
fun TripleStarLogoLockup(
    modifier: Modifier = Modifier,
    iconSize: Dp = 40.dp,
    showSubtitle: Boolean = false
) {
    Row(
        modifier = modifier,
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        TripleStarLogoIcon(iconSize = iconSize)
        Column {
            Text(
                text = "TripleStar Learning",
                style = MaterialTheme.typography.titleLarge.copy(
                    fontWeight = FontWeight.Bold,
                    letterSpacing = (-0.5).sp
                ),
                color = MaterialTheme.colorScheme.onBackground
            )
            if (showSubtitle) {
                Text(
                    text = "Ages 5–14 • Learn & Grow",
                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                    color = MaterialTheme.colorScheme.primary
                )
            }
        }
    }
}
