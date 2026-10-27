package com.triplestar.learn.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.foundation.gestures.detectDragGestures
import com.triplestar.learn.util.LocaleManager
import com.triplestar.learn.util.TtsManager

data class DrawingLine(
    val path: Path,
    val color: Color,
    val strokeWidth: Float
)

@Composable
fun DrawingCanvasComponent(
    onDrawingCompleted: () -> Unit,
    modifier: Modifier = Modifier
) {
    val lines = remember { mutableStateListOf<DrawingLine>() }
    var currentPath by remember { mutableStateOf<Path?>(null) }
    var currentColor by remember { mutableStateOf(Color(0xFF2563EB)) } // Default Astro Blue
    var currentStrokeWidth by remember { mutableStateOf(16f) }
    var isEraser by remember { mutableStateOf(false) }

    val colors = listOf(
        Color(0xFFF43F5E), // Red/Rose
        Color(0xFF2563EB), // Blue
        Color(0xFF10B981), // Green
        Color(0xFFF59E0B), // Yellow/Amber
        Color(0xFF8B5CF6), // Purple
        Color(0xFF0F172A), // Black
        Color(0xFFFFFFFF)  // Eraser (White)
    )

    val strokeWidths = listOf(8f, 16f, 28f)

    Column(
        modifier = modifier
            .fillMaxWidth()
            .testTag("drawing_canvas_component")
    ) {
        // Toolbar: Colors & Eraser
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(vertical = 8.dp),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                colors.forEach { color ->
                    val isSelected = currentColor == color && !isEraser
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(color)
                            .border(
                                width = if (isSelected) 3.dp else 1.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray,
                                shape = CircleShape
                            )
                            .clickable {
                                isEraser = (color == Color(0xFFFFFFFF))
                                currentColor = color
                                val currentLang = LocaleManager.currentLanguage.value
                                val isUrdu = currentLang == LocaleManager.LANG_URDU
                                val colorName = when(color) {
                                    Color(0xFFF43F5E) -> if (isUrdu) "سرخ" else "Red"
                                    Color(0xFF2563EB) -> if (isUrdu) "نیلا" else "Blue"
                                    Color(0xFF10B981) -> if (isUrdu) "سبز" else "Green"
                                    Color(0xFFF59E0B) -> if (isUrdu) "پیلا" else "Yellow"
                                    Color(0xFF8B5CF6) -> if (isUrdu) "جامنی" else "Purple"
                                    Color(0xFF0F172A) -> if (isUrdu) "سیاہ" else "Black"
                                    else -> if (isUrdu) "ربر" else "Eraser"
                                }
                                TtsManager.speak(colorName, isUrdu)
                            }
                            .testTag("color_btn_${color.value}")
                    )
                }
            }

            IconButton(
                onClick = { lines.clear(); currentPath = null },
                modifier = Modifier.testTag("btn_clear_canvas")
            ) {
                Icon(
                    imageVector = Icons.Default.Delete,
                    contentDescription = "Clear Canvas",
                    tint = MaterialTheme.colorScheme.error
                )
            }
        }

        // Brush Size Selector
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(bottom = 8.dp),
            horizontalArrangement = Arrangement.spacedBy(12.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text("Size:", style = MaterialTheme.typography.labelMedium)
            strokeWidths.forEach { width ->
                val isSelected = currentStrokeWidth == width
                FilterChip(
                    selected = isSelected,
                    onClick = { currentStrokeWidth = width },
                    label = { Text(when(width) { 8f -> "Small" ; 16f -> "Medium" ; else -> "Large" }) },
                    modifier = Modifier.testTag("size_chip_${width.toInt()}")
                )
            }
        }

        // Drawing Canvas Surface
        Card(
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = Color.White),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(320.dp)
                .border(2.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(16.dp))
        ) {
            Canvas(
                modifier = Modifier
                    .fillMaxSize()
                    .pointerInput(currentColor, currentStrokeWidth, isEraser) {
                        detectDragGestures(
                            onDragStart = { offset ->
                                val path = Path().apply {
                                    moveTo(offset.x, offset.y)
                                }
                                currentPath = path
                            },
                            onDrag = { change, dragAmount ->
                                change.consume()
                                currentPath?.let { path ->
                                    val position = change.position
                                    path.lineTo(position.x, position.y)
                                }
                            },
                            onDragEnd = {
                                currentPath?.let { path ->
                                    val activeColor = if (isEraser) Color.White else currentColor
                                    lines.add(DrawingLine(path, activeColor, currentStrokeWidth))
                                    currentPath = null
                                }
                            }
                        )
                    }
            ) {
                // Draw existing saved lines
                lines.forEach { line ->
                    drawPath(
                        path = line.path,
                        color = line.color,
                        style = Stroke(
                            width = line.strokeWidth,
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )
                }
                // Draw current active line
                currentPath?.let { path ->
                    val activeColor = if (isEraser) Color.White else currentColor
                    drawPath(
                        path = path,
                        color = activeColor,
                        style = Stroke(
                            width = currentStrokeWidth,
                            cap = StrokeCap.Round,
                            join = StrokeJoin.Round
                        )
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        Button(
            onClick = {
                val currentLang = LocaleManager.currentLanguage.value
                val isUrdu = currentLang == LocaleManager.LANG_URDU
                TtsManager.speak(if (isUrdu) "بہت خوبصورت ڈرائنگ! شاباش!" else "Wonderful drawing! Great job!", isUrdu)
                onDrawingCompleted()
            },
            modifier = Modifier
                .fillMaxWidth()
                .height(50.dp)
                .testTag("btn_complete_drawing"),
            shape = RoundedCornerShape(12.dp)
        ) {
            Text("I'm Done! Finish Drawing ✨", fontWeight = FontWeight.Bold)
        }
    }
}
