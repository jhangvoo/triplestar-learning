package com.triplestar.learn.ui.components

import android.content.Intent
import android.speech.RecognitionListener
import android.speech.RecognizerIntent
import android.speech.SpeechRecognizer
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.triplestar.learn.ui.theme.AstroBluePrimary
import com.triplestar.learn.ui.theme.CoralFun
import com.triplestar.learn.ui.theme.EmeraldSuccess
import com.triplestar.learn.ui.theme.StarGoldPrimary
import com.triplestar.learn.util.LocaleManager
import com.triplestar.learn.util.MathHelper
import com.triplestar.learn.util.TtsManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun AudioLearningStudio(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val currentLang by LocaleManager.currentLanguage.collectAsState()
    val isUrdu = currentLang == LocaleManager.LANG_URDU

    var selectedMode by remember { mutableIntStateOf(0) } // 0: Listen, 1: Repeat, 2: Sing, 3: Practice, 4: Count 1-1000
    var isSlowMode by remember { mutableStateOf(TtsManager.isSlowMode) }
    var highlightedIndex by remember { mutableIntStateOf(-1) }
    var spokenFeedback by remember { mutableStateOf("") }
    var numberInput by remember { mutableIntStateOf(1) }

    val englishAlphabet = ('A'..'Z').map { it.toString() }
    val urduAlphabet = listOf("ا", "ب", "پ", "ت", "ٹ", "ث", "ج", "چ", "ح", "خ", "د", "ڈ", "ذ", "ر", "ڑ", "ز", "ژ", "س", "ش", "ص", "ض", "ط", "ظ", "ع", "غ", "ف", "ق", "ک", "گ", "ل", "م", "ن", "و", "ہ", "ء", "ی", "ے")

    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(24.dp),
        border = BorderStroke(1.5.dp, AstroBluePrimary.copy(alpha = 0.25f)),
        modifier = modifier.fillMaxWidth().testTag("audio_learning_studio")
    ) {
        Column(
            modifier = Modifier.padding(18.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header & Slow-Learning Mode Toggle
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(44.dp)
                            .clip(CircleShape)
                            .background(AstroBluePrimary.copy(alpha = 0.15f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = null,
                            tint = AstroBluePrimary
                        )
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (isUrdu) "آڈیو لرننگ اسٹوڈیو" else "Audio Learning Studio",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = if (isUrdu) "تلفظ، گنتی اور آواز کی مشق" else "Pronunciation, Phonics & Counting",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                // Slow Speed Toggle Chip
                FilterChip(
                    selected = isSlowMode,
                    onClick = {
                        isSlowMode = !isSlowMode
                        TtsManager.isSlowMode = isSlowMode
                    },
                    label = { Text(if (isUrdu) "سست رفتار 🐢" else "Slow Mode 🐢", fontSize = 11.sp) },
                    modifier = Modifier.testTag("chip_slow_mode")
                )
            }

            // Mode Selection Tabs
            ScrollableTabRow(
                selectedTabIndex = selectedMode,
                edgePadding = 0.dp,
                divider = {},
                containerColor = Color.Transparent
            ) {
                Tab(
                    selected = selectedMode == 0,
                    onClick = { selectedMode = 0 },
                    text = { Text(if (isUrdu) "🎧 سنیں (Listen)" else "🎧 Listen With Me") }
                )
                Tab(
                    selected = selectedMode == 1,
                    onClick = { selectedMode = 1 },
                    text = { Text(if (isUrdu) "🗣️ دہرائیں (Repeat)" else "🗣️ Repeat With Me") }
                )
                Tab(
                    selected = selectedMode == 2,
                    onClick = { selectedMode = 2 },
                    text = { Text(if (isUrdu) "🎵 گیت (Sing)" else "🎵 Sing With Me") }
                )
                Tab(
                    selected = selectedMode == 3,
                    onClick = { selectedMode = 3 },
                    text = { Text(if (isUrdu) "🎤 مشق (Practice)" else "🎤 Practice With Me") }
                )
                Tab(
                    selected = selectedMode == 4,
                    onClick = { selectedMode = 4 },
                    text = { Text(if (isUrdu) "🔢 گنتی ۱-۱۰۰۰" else "🔢 Count 1-1000") }
                )
            }

            // Mode Content
            when (selectedMode) {
                0 -> {
                    // Listen With Me: Alphabet & Phonics
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        Text(
                            text = if (isUrdu) "الف بے پے اور اے بی سی سنیں:" else "Tap any letter to hear crystal-clear offline pronunciation:",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold)
                        )

                        val alphabetList = if (isUrdu) urduAlphabet else englishAlphabet
                        LazyRow(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.fillMaxWidth().testTag("audio_alphabet_row")
                        ) {
                            itemsIndexed(alphabetList) { index, letter ->
                                val isHighlighted = highlightedIndex == index
                                Surface(
                                    shape = RoundedCornerShape(12.dp),
                                    color = if (isHighlighted) StarGoldPrimary else AstroBluePrimary.copy(alpha = 0.15f),
                                    modifier = Modifier
                                        .size(48.dp)
                                        .clickable {
                                            highlightedIndex = index
                                            TtsManager.speakAlphabet(letter, isUrdu)
                                            spokenFeedback = if (isUrdu) "حرف: $letter" else "Letter: $letter"
                                        }
                                        .testTag("audio_letter_$letter")
                                ) {
                                    Box(contentAlignment = Alignment.Center) {
                                        Text(
                                            text = letter,
                                            style = MaterialTheme.typography.titleMedium.copy(
                                                fontWeight = FontWeight.Bold,
                                                color = if (isHighlighted) Color.Black else MaterialTheme.colorScheme.onSurface
                                            )
                                        )
                                    }
                                }
                            }
                        }
                    }
                }
                1 -> {
                    // Repeat With Me
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (isUrdu) "پہلے سنیں، پھر بلند آواز میں دہرائیں!" else "Listen to the word, then repeat it out loud!",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )

                        val repeatWords = if (isUrdu) listOf("بچہ", "کتاب", "قلم", "آسمان", "ستارہ", "پھول") else listOf("Apple", "Ball", "Cat", "Dog", "School", "Star")

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(8.dp),
                            modifier = Modifier.horizontalScroll(rememberScrollState())
                        ) {
                            repeatWords.forEachIndexed { idx, word ->
                                Button(
                                    onClick = {
                                        highlightedIndex = idx
                                        TtsManager.speak(word, isUrdu)
                                        spokenFeedback = if (isUrdu) "دہرائیں: $word" else "Repeat: $word"
                                    },
                                    colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                                    shape = RoundedCornerShape(14.dp),
                                    modifier = Modifier.testTag("btn_repeat_$word")
                                ) {
                                    Text(text = word, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
                2 -> {
                    // Sing With Me (Rhythmic Alphabet / Counting Chant)
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (isUrdu) "موسیقی اور تال کے ساتھ گنگنائیں!" else "Sing along to the rhythmic alphabet & numbers chant!",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )

                        Button(
                            onClick = {
                                val chant = if (isUrdu) "الف ب پ ت ٹ! علم کی روشنی ہے!" else "A B C D E! Learning is fun and free!"
                                TtsManager.speak(chant, isUrdu)
                                spokenFeedback = chant
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = CoralFun),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.testTag("btn_sing_chant")
                        ) {
                            Icon(imageVector = Icons.Default.MusicNote, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = if (isUrdu) "گیت سنیں (Sing Chant)" else "Play Educational Chant", fontWeight = FontWeight.Bold)
                        }
                    }
                }
                3 -> {
                    // Practice With Me (Interactive Voice Prompt & TTS)
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (isUrdu) "بولنے کی مشق: بٹن دبائیں اور لفظ سنیں" else "Pronunciation Practice: Tap to hear correct native pronunciation",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )

                        Button(
                            onClick = {
                                val testPhrase = if (isUrdu) "میں بہت اچھا سیکھ رہا ہوں!" else "I love learning everyday!"
                                TtsManager.speak(testPhrase, isUrdu)
                                spokenFeedback = testPhrase
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = StarGoldPrimary),
                            shape = RoundedCornerShape(16.dp),
                            modifier = Modifier.testTag("btn_practice_voice")
                        ) {
                            Icon(imageVector = Icons.Default.Mic, contentDescription = null, tint = Color.Black)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = if (isUrdu) "تلفظ سنیں" else "Pronounce Sentence", color = Color.Black, fontWeight = FontWeight.Bold)
                        }
                    }
                }
                4 -> {
                    // Counting from 1 to 1000 with correct number words
                    Column(
                        verticalArrangement = Arrangement.spacedBy(12.dp),
                        horizontalAlignment = Alignment.CenterHorizontally,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Text(
                            text = if (isUrdu) "۱ سے ۱۰۰۰ تک گنتی درست الفاظ میں سنیں:" else "Count from 1 to 1000 with correct native number words:",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center
                        )

                        Row(
                            horizontalArrangement = Arrangement.spacedBy(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            OutlinedButton(
                                onClick = { if (numberInput > 1) numberInput-- },
                                shape = CircleShape
                            ) {
                                Text("-", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            }

                            Card(
                                colors = CardDefaults.cardColors(containerColor = AstroBluePrimary.copy(alpha = 0.15f)),
                                shape = RoundedCornerShape(16.dp)
                            ) {
                                Column(
                                    modifier = Modifier.padding(horizontal = 24.dp, vertical = 12.dp),
                                    horizontalAlignment = Alignment.CenterHorizontally
                                ) {
                                    Text(
                                        text = if (isUrdu) MathHelper.toUrduDigits(numberInput) else "$numberInput",
                                        style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                                        color = AstroBluePrimary
                                    )
                                    Text(
                                        text = if (isUrdu) MathHelper.toUrduWords(numberInput) else MathHelper.toEnglishWords(numberInput),
                                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.SemiBold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                }
                            }

                            OutlinedButton(
                                onClick = { if (numberInput < 1000) numberInput++ },
                                shape = CircleShape
                            ) {
                                Text("+", fontSize = 20.sp, fontWeight = FontWeight.Bold)
                            }
                        }

                        Button(
                            onClick = {
                                TtsManager.speakNumber(numberInput, isUrdu)
                                spokenFeedback = if (isUrdu) MathHelper.toUrduWords(numberInput) else MathHelper.toEnglishWords(numberInput)
                            },
                            colors = ButtonDefaults.buttonColors(containerColor = AstroBluePrimary),
                            shape = RoundedCornerShape(14.dp),
                            modifier = Modifier.testTag("btn_speak_number")
                        ) {
                            Icon(imageVector = Icons.Default.VolumeUp, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = if (isUrdu) "نمبر سنیں (Speak Number)" else "Speak Number", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }

            // Spoken Feedback Banner
            if (spokenFeedback.isNotBlank()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(EmeraldSuccess.copy(alpha = 0.15f))
                        .padding(12.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = "🔊 $spokenFeedback",
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface,
                        textAlign = TextAlign.Center
                    )
                }
            }
        }
    }
}
