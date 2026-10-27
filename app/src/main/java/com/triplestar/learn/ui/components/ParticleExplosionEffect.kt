package com.triplestar.learn.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutLinearInEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.rotate
import kotlin.math.cos
import kotlin.math.sin
import kotlin.random.Random

data class Particle(
    val angle: Double,
    val speed: Float,
    val color: Color,
    val size: Float,
    val isRect: Boolean,
    val rotationSpeed: Float
)

@Composable
fun ParticleExplosionEffect(
    trigger: Boolean,
    modifier: Modifier = Modifier,
    particleCount: Int = 80,
    onAnimationEnd: () -> Unit = {}
) {
    if (!trigger) return

    val progress = remember(trigger) { Animatable(0f) }

    val colors = listOf(
        Color(0xFFFFD700), // Gold
        Color(0xFFFF4081), // Pink / Rose
        Color(0xFF00E676), // Bright Emerald
        Color(0xFF2979FF), // Vibrant Blue
        Color(0xFFFF9100), // Orange
        Color(0xFFAA00FF), // Purple
        Color(0xFF00E5FF)  // Cyan
    )

    val particles = remember(trigger) {
        List(particleCount) {
            val angle = Random.nextDouble(0.0, 2.0 * Math.PI)
            val speed = Random.nextFloat() * 700f + 250f
            val color = colors[Random.nextInt(colors.size)]
            val size = Random.nextFloat() * 14f + 8f
            val isRect = Random.nextBoolean()
            val rotationSpeed = Random.nextFloat() * 720f - 360f
            Particle(angle, speed, color, size, isRect, rotationSpeed)
        }
    }

    LaunchedEffect(trigger) {
        progress.snapTo(0f)
        progress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1500, easing = FastOutLinearInEasing)
        )
        onAnimationEnd()
    }

    Canvas(modifier = modifier.fillMaxSize()) {
        val centerX = size.width / 2f
        val centerY = size.height / 2f
        val t = progress.value
        val alpha = (1f - t).coerceIn(0f, 1f)

        particles.forEach { particle ->
            val distance = particle.speed * t
            // Add gravity effect pulling down over time
            val gravity = 450f * t * t
            val x = centerX + (cos(particle.angle) * distance).toFloat()
            val y = centerY + (sin(particle.angle) * distance).toFloat() + gravity

            val currentRotation = particle.rotationSpeed * t

            rotate(degrees = currentRotation, pivot = Offset(x, y)) {
                if (particle.isRect) {
                    drawRect(
                        color = particle.color.copy(alpha = alpha),
                        topLeft = Offset(x - particle.size / 2, y - particle.size / 3),
                        size = Size(particle.size, particle.size / 2)
                    )
                } else {
                    drawCircle(
                        color = particle.color.copy(alpha = alpha),
                        radius = particle.size / 2,
                        center = Offset(x, y)
                    )
                }
            }
        }
    }
}
