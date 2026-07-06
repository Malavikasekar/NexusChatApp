package com.malavikafaseed.nexuschat

import androidx.compose.animation.core.*
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.*
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.rounded.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.rotate
import androidx.compose.ui.draw.scale
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import kotlinx.coroutines.delay
import kotlin.math.sin

@Composable
fun AnimatedSplashScreen(onFinished: () -> Unit) {
    var startAnimation by remember { mutableStateOf(false) }
    
    // Duration of splash screen
    LaunchedEffect(key1 = true) {
        startAnimation = true
        delay(4000) // 4 seconds of gorgeous animations
        onFinished()
    }

    // Logo scale and rotation animations
    val scale by animateFloatAsState(
        targetValue = if (startAnimation) 1.2f else 0f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "logoScale"
    )

    val alpha by animateFloatAsState(
        targetValue = if (startAnimation) 1f else 0f,
        animationSpec = tween(durationMillis = 1500, easing = LinearOutSlowInEasing),
        label = "logoAlpha"
    )

    // Infinite rotation for glow ring
    val infiniteTransition = rememberInfiniteTransition(label = "glowTransition")
    val rotationAngle by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 360f,
        animationSpec = infiniteRepeatable(
            animation = tween(4000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "glowRotation"
    )

    // Pulsing size for glow rings
    val pulseScale by infiniteTransition.animateFloat(
        initialValue = 0.95f,
        targetValue = 1.05f,
        animationSpec = infiniteRepeatable(
            animation = tween(1500, easing = FastOutSlowInEasing),
            repeatMode = RepeatMode.Reverse
        ),
        label = "glowPulse"
    )

    // Premium dark/neon background gradient
    val backgroundBrush = Brush.verticalGradient(
        colors = listOf(
            Color(0xFF0F0C1B), // Midnight Dark Blue
            Color(0xFF201335), // Royal Deep Purple
            Color(0xFF0A0813)  // Abyss Black
        )
    )

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(backgroundBrush),
        contentAlignment = Alignment.Center
    ) {
        // Falling/Rising glowing particles
        ParticlesEffect(startAnimation)

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(240.dp)
                    .scale(scale)
                    .alpha(alpha)
            ) {
                // Outer glowing animated ring 1
                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .rotate(rotationAngle)
                        .scale(pulseScale)
                ) {
                    drawCircle(
                        brush = Brush.sweepGradient(
                            colors = listOf(
                                Color(0xFF00F2FE), // Neon Cyan
                                Color(0xFF4FACFE), // Soft Blue
                                Color(0xFF00F2FE)
                            )
                        ),
                        style = Stroke(width = 4.dp.toPx()),
                        alpha = 0.4f
                    )
                }

                // Outer glowing animated ring 2 (contrasting rotation and size)
                Canvas(
                    modifier = Modifier
                        .size(190.dp)
                        .rotate(-rotationAngle * 1.5f)
                        .scale(2f - pulseScale)
                ) {
                    drawCircle(
                        brush = Brush.sweepGradient(
                            colors = listOf(
                                Color(0xFFF355DA), // Electric Magenta
                                Color(0xFFFF007F), // Glowing Pink
                                Color(0xFFF355DA)
                            )
                        ),
                        style = Stroke(width = 2.dp.toPx()),
                        alpha = 0.5f
                    )
                }

                // Inner Solid Circle Background for the Icon
                Canvas(modifier = Modifier.size(130.dp)) {
                    drawCircle(
                        brush = Brush.radialGradient(
                            colors = listOf(
                                Color(0xFF341C52),
                                Color(0xFF160E27)
                            )
                        ),
                        radius = 65.dp.toPx()
                    )
                }

                // Premium Glowing Chat Icon in Center
                Icon(
                    imageVector = Icons.Rounded.Send,
                    contentDescription = "Nexus Logo",
                    tint = Color.White,
                    modifier = Modifier
                        .size(60.dp)
                        .rotate(-45f) // Styled arrow/send icon
                )
            }

            Spacer(modifier = Modifier.height(40.dp))

            // Animated App Name
            Text(
                text = "N E X U S  C H A T",
                color = Color.White,
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                fontFamily = FontFamily.Monospace,
                modifier = Modifier
                    .alpha(alpha)
                    .padding(horizontal = 16.dp)
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Animated Subtitle
            Text(
                text = "Connect Beyond Boundaries",
                color = Color(0xFF00F2FE),
                fontSize = 12.sp,
                fontWeight = FontWeight.Medium,
                fontFamily = FontFamily.SansSerif,
                modifier = Modifier
                    .alpha(alpha * 0.7f)
            )
        }
    }
}

@Composable
fun ParticlesEffect(startAnimation: Boolean) {
    val infiniteTransition = rememberInfiniteTransition(label = "particles")
    val time by infiniteTransition.animateFloat(
        initialValue = 0f,
        targetValue = 2 * Math.PI.toFloat(),
        animationSpec = infiniteRepeatable(
            animation = tween(6000, easing = LinearEasing),
            repeatMode = RepeatMode.Restart
        ),
        label = "particleTimer"
    )

    if (startAnimation) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val particleColors = listOf(
                Color(0x8000F2FE), // Semi-transparent Neon Cyan
                Color(0x80F355DA), // Semi-transparent Magenta
                Color(0x60FFFFFF)  // Semi-transparent White
            )

            // Draw a few floating particle stars/circles
            val numParticles = 12
            for (i in 0 until numParticles) {
                val seed = i * 45.23f
                val x = (size.width * 0.15f) + (size.width * 0.7f * ((seed % 10) / 10f))
                
                // Animate Y position rising upwards
                val speedFactor = 0.5f + (seed % 5) * 0.1f
                val yOffset = (size.height * ((1f - (time * speedFactor / (2 * Math.PI.toFloat()))) % 1f))
                
                // Add horizontal sinusoidal swaying
                val sway = sin(time + seed) * 30f
                
                drawCircle(
                    color = particleColors[i % particleColors.size],
                    radius = (4 + (i % 6)).dp.toPx(),
                    center = Offset(x + sway, yOffset)
                )
            }
        }
    }
}
