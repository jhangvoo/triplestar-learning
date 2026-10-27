package com.triplestar.learn.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.VolumeUp
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
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.triplestar.learn.ui.theme.EmeraldSuccess
import com.triplestar.learn.ui.theme.StarGoldPrimary
import com.triplestar.learn.util.LocaleManager
import com.triplestar.learn.util.TtsManager

data class AlphabetLetter(
    val letter: Char,
    val uppercase: String,
    val lowercase: String,
    val wordEn: String,
    val wordUr: String,
    val emoji: String,
    val phonicsSound: String,
    val exampleSentence: String,
    val colorHex: Long
)

val ALPHABET_LIST = listOf(
    AlphabetLetter('A', "A", "a", "Apple", "سیب", "🍎", "/æ/ as in Apple", "A is for Apple, sweet and red!", 0xFFEF4444),
    AlphabetLetter('B', "B", "b", "Ball", "گیند", "⚽", "/b/ as in Ball", "B is for Ball, bounce it high!", 0xFF3B82F6),
    AlphabetLetter('C', "C", "c", "Cat", "بلی", "🐱", "/k/ as in Cat", "C is for Cat, soft and furry!", 0xFFF59E0B),
    AlphabetLetter('D', "D", "d", "Dog", "کتا", "🐶", "/d/ as in Dog", "D is for Dog, a loyal friend!", 0xFF10B981),
    AlphabetLetter('E', "E", "e", "Elephant", "ہاتھی", "🐘", "/e/ as in Elephant", "E is for Elephant, strong and big!", 0xFF8B5CF6),
    AlphabetLetter('F', "F", "f", "Fish", "مچھلی", "🐟", "/f/ as in Fish", "F is for Fish, swimming in the sea!", 0xFF06B6D4),
    AlphabetLetter('G', "G", "g", "Grapes", "انگور", "🍇", "/g/ as in Grapes", "G is for Grapes, juicy and sweet!", 0xFF84CC16),
    AlphabetLetter('H', "H", "h", "Hat", "ٹوپی", "🎩", "/h/ as in Hat", "H is for Hat, wear it on your head!", 0xFFEC4899),
    AlphabetLetter('I', "I", "i", "Ice Cream", "آئس کریم", "🍦", "/aɪ/ as in Ice", "I is for Ice Cream, cold and delicious!", 0xFFF97316),
    AlphabetLetter('J', "J", "j", "Juice", "جوس", "🧃", "/dʒ/ as in Juice", "J is for Juice, fresh and fruity!", 0xFF14B8A6),
    AlphabetLetter('K', "K", "k", "Kite", "پتنگ", "🪁", "/k/ as in Kite", "K is for Kite, flying high in the sky!", 0xFF6366F1),
    AlphabetLetter('L', "L", "l", "Lion", "شیر", "🦁", "/l/ as in Lion", "L is for Lion, king of the jungle!", 0xFFEAB308),
    AlphabetLetter('M', "M", "m", "Monkey", "بندر", "🐵", "/m/ as in Monkey", "M is for Monkey, swinging in the trees!", 0xFFD97706),
    AlphabetLetter('N', "N", "n", "Nest", "گھونسلا", "🪺", "/n/ as in Nest", "N is for Nest, cozy for birds!", 0xFF10B981),
    AlphabetLetter('O', "O", "o", "Orange", "مالٹا", "🍊", "/ɒ/ as in Orange", "O is for Orange, round and juicy!", 0xFFF97316),
    AlphabetLetter('P', "P", "p", "Penguin", "پینگوئن", "🐧", "/p/ as in Penguin", "P is for Penguin, waddling on ice!", 0xFF0284C7),
    AlphabetLetter('Q', "Q", "q", "Queen", "ملکہ", "👑", "/kw/ as in Queen", "Q is for Queen, wearing a golden crown!", 0xFF9333EA),
    AlphabetLetter('R', "R", "r", "Rainbow", "قوس قزح", "🌈", "/r/ as in Rainbow", "R is for Rainbow, colorful in the sky!", 0xFFE11D48),
    AlphabetLetter('S', "S", "s", "Sun", "سورج", "☀️", "/s/ as in Sun", "S is for Sun, bright and warm!", 0xFFF59E0B),
    AlphabetLetter('T', "T", "t", "Tree", "درخت", "🌳", "/t/ as in Tree", "T is for Tree, tall with green leaves!", 0xFF16A34A),
    AlphabetLetter('U', "U", "u", "Umbrella", "چھتری", "☂️", "/ʌ/ as in Umbrella", "U is for Umbrella, keeping us dry!", 0xFF7C3AED),
    AlphabetLetter('V', "V", "v", "Violin", "وائلن", "🎻", "/v/ as in Violin", "V is for Violin, making sweet music!", 0xFFB45309),
    AlphabetLetter('W', "W", "w", "Watch", "گھڑی", "⌚", "/w/ as in Watch", "W is for Watch, ticking on your wrist!", 0xFF0D9488),
    AlphabetLetter('X', "X", "x", "Xylophone", "زائلو فون", "🎵", "/z/ as in Xylophone", "X is for Xylophone, tapping happy tunes!", 0xFFDB2777),
    AlphabetLetter('Y', "Y", "y", "Yak", "یاک", "🐂", "/j/ as in Yak", "Y is for Yak, climbing high mountains!", 0xFFEA580C),
    AlphabetLetter('Z', "Z", "z", "Zebra", "زیبرا", "🦓", "/z/ as in Zebra", "Z is for Zebra, with black and white stripes!", 0xFF475569)
)

@Composable
fun EnglishAlphabetWritingStudio(
    modifier: Modifier = Modifier
) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val currentLang by LocaleManager.currentLanguage.collectAsState()
    val isUrdu = currentLang == LocaleManager.LANG_URDU
    val currentItem = ALPHABET_LIST[selectedIndex]

    val lines = remember(selectedIndex) { mutableStateListOf<DrawingLine>() }
    var currentPath by remember(selectedIndex) { mutableStateOf<Path?>(null) }
    var currentColor by remember { mutableStateOf(Color(0xFF2563EB)) }
    var currentStrokeWidth by remember { mutableStateOf(20f) }
    var hasPracticedWriting by remember(selectedIndex) { mutableStateOf(false) }

    val colors = listOf(
        Color(0xFFEF4444), // Red
        Color(0xFF2563EB), // Blue
        Color(0xFF10B981), // Green
        Color(0xFFF59E0B), // Yellow
        Color(0xFF8B5CF6), // Purple
        Color(0xFF0F172A), // Black
        Color(0xFFFFFFFF)  // Eraser
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("english_alphabet_studio"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(currentItem.colorHex).copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = Color(currentItem.colorHex),
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (isUrdu) "انگریزی حروف کی پہچان اور لکھائی" else "Alphabet & Writing Studio",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = if (isUrdu) "تمام ۲۶ حروف کی پہچان، آوازیں اور مشق" else "Letter Recognition, Phonics & Writing Practice (A–Z)",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                IconButton(
                    onClick = {
                        val speakText = if (isUrdu) {
                            "حرف ${currentItem.uppercase}۔ ${currentItem.wordUr}۔ ${currentItem.exampleSentence}"
                        } else {
                            "Letter ${currentItem.uppercase}, lowercase ${currentItem.lowercase}. ${currentItem.wordEn}. ${currentItem.exampleSentence}"
                        }
                        TtsManager.speak(speakText, isUrdu)
                    },
                    modifier = Modifier.testTag("btn_speak_alphabet_overview")
                ) {
                    Icon(
                        imageVector = Icons.Default.VolumeUp,
                        contentDescription = "Pronounce",
                        tint = MaterialTheme.colorScheme.primary
                    )
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // A–Z Letter Chips Horizontal Scroll
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                ALPHABET_LIST.forEachIndexed { index, item ->
                    val isSelected = selectedIndex == index
                    FilterChip(
                        selected = isSelected,
                        onClick = {
                            selectedIndex = index
                            TtsManager.speak("Letter ${item.uppercase}", false)
                        },
                        label = {
                            Text(
                                text = "${item.uppercase} ${item.lowercase}",
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            selectedContainerColor = Color(item.colorHex).copy(alpha = 0.2f),
                            selectedLabelColor = Color(item.colorHex)
                        ),
                        modifier = Modifier.testTag("letter_chip_${item.letter}")
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Letter Card with Illustration & Phonics
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = Color(currentItem.colorHex).copy(alpha = 0.08f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.Bottom) {
                            Text(
                                text = "${currentItem.uppercase}${currentItem.lowercase}",
                                style = MaterialTheme.typography.displayMedium.copy(
                                    fontWeight = FontWeight.Black,
                                    color = Color(currentItem.colorHex)
                                )
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = currentItem.phonicsSound,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                ),
                                modifier = Modifier.padding(bottom = 8.dp)
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = "${currentItem.uppercase} is for ${currentItem.wordEn} (${currentItem.wordUr})",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(4.dp))

                        Text(
                            text = currentItem.exampleSentence,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = currentItem.emoji,
                            fontSize = 48.sp,
                            modifier = Modifier.clickable {
                                TtsManager.speak(currentItem.wordEn, false)
                            }
                        )
                        IconButton(
                            onClick = {
                                TtsManager.speak("${currentItem.uppercase}. ${currentItem.wordEn}", false)
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.VolumeUp,
                                contentDescription = "Hear word",
                                tint = Color(currentItem.colorHex)
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Interactive Tracing & Writing Practice Canvas Section
            Text(
                text = "✍️ Write & Trace Letter ${currentItem.uppercase}",
                style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Text(
                text = "Trace over the letter below using your finger to practice writing!",
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Color & Clear Toolbar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    colors.forEach { color ->
                        val isEraser = color == Color(0xFFFFFFFF)
                        val isSelected = currentColor == color
                        Box(
                            modifier = Modifier
                                .size(28.dp)
                                .clip(CircleShape)
                                .background(color)
                                .border(
                                    width = if (isSelected) 3.dp else 1.dp,
                                    color = if (isSelected) MaterialTheme.colorScheme.primary else Color.LightGray,
                                    shape = CircleShape
                                )
                                .clickable {
                                    currentColor = color
                                    val colorName = when (color) {
                                        Color(0xFFEF4444) -> "Red"
                                        Color(0xFF2563EB) -> "Blue"
                                        Color(0xFF10B981) -> "Green"
                                        Color(0xFFF59E0B) -> "Yellow"
                                        Color(0xFF8B5CF6) -> "Purple"
                                        Color(0xFF0F172A) -> "Black"
                                        else -> "Eraser"
                                    }
                                    TtsManager.speak(colorName, false)
                                }
                        )
                    }
                }

                IconButton(
                    onClick = {
                        lines.clear()
                        currentPath = null
                    },
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Delete,
                        contentDescription = "Clear",
                        tint = MaterialTheme.colorScheme.error
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Writing Surface Canvas with Tracing Background Watermark
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(200.dp)
                    .clip(RoundedCornerShape(14.dp))
                    .background(Color.White)
                    .border(1.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
            ) {
                // Tracing watermark
                Text(
                    text = "${currentItem.uppercase}${currentItem.lowercase}",
                    fontSize = 120.sp,
                    fontWeight = FontWeight.Black,
                    color = Color.LightGray.copy(alpha = 0.25f),
                    modifier = Modifier.align(Alignment.Center)
                )

                Canvas(
                    modifier = Modifier
                        .fillMaxSize()
                        .pointerInput(currentColor, currentStrokeWidth) {
                            detectDragGestures(
                                onDragStart = { offset ->
                                    val path = Path().apply { moveTo(offset.x, offset.y) }
                                    currentPath = path
                                    hasPracticedWriting = true
                                },
                                onDrag = { change, _ ->
                                    change.consume()
                                    currentPath?.let { path ->
                                        path.lineTo(change.position.x, change.position.y)
                                    }
                                },
                                onDragEnd = {
                                    currentPath?.let { path ->
                                        lines.add(DrawingLine(path, currentColor, currentStrokeWidth))
                                        currentPath = null
                                    }
                                }
                            )
                        }
                ) {
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
                    currentPath?.let { path ->
                        drawPath(
                            path = path,
                            color = currentColor,
                            style = Stroke(
                                width = currentStrokeWidth,
                                cap = StrokeCap.Round,
                                join = StrokeJoin.Round
                            )
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Encouragement & Completion Row
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Button(
                    onClick = {
                        hasPracticedWriting = true
                        val message = if (isUrdu) {
                            "شاباش! آپ نے حرف ${currentItem.uppercase} لکھنے کی مشق مکمل کرلی!"
                        } else {
                            "Awesome job practicing letter ${currentItem.uppercase}!"
                        }
                        TtsManager.speak(message, isUrdu)
                    },
                    modifier = Modifier
                        .weight(1f)
                        .testTag("btn_practice_done"),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.CheckCircle,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text("I Practiced Writing ${currentItem.uppercase}! ✨", fontWeight = FontWeight.Bold)
                }

                Spacer(modifier = Modifier.width(8.dp))

                // Previous Letter Button
                IconButton(
                    onClick = {
                        if (selectedIndex > 0) {
                            selectedIndex--
                            TtsManager.speak("Letter ${ALPHABET_LIST[selectedIndex].uppercase}", false)
                        }
                    },
                    enabled = selectedIndex > 0
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous")
                }

                // Next Letter Button
                IconButton(
                    onClick = {
                        if (selectedIndex < ALPHABET_LIST.size - 1) {
                            selectedIndex++
                            TtsManager.speak("Letter ${ALPHABET_LIST[selectedIndex].uppercase}", false)
                        }
                    },
                    enabled = selectedIndex < ALPHABET_LIST.size - 1
                ) {
                    Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next")
                }
            }
        }
    }
}
