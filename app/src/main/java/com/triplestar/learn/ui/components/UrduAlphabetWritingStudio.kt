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

data class UrduAlphabetLetter(
    val letter: String,
    val nameUr: String,
    val nameEn: String,
    val wordUr: String,
    val wordEn: String,
    val emoji: String,
    val soundPhonics: String,
    val rhymeUr: String,
    val rhymeEn: String,
    val colorHex: Long
)

val URDU_ALPHABET_LIST = listOf(
    UrduAlphabetLetter("ا", "الف", "Alif", "انار", "Pomegranate", "🍎", "ا کی آواز: اَ (انار)", "الف سے انار، میٹھا اور لال!", "Alif for Anaar, sweet and red!", 0xFFEF4444),
    UrduAlphabetLetter("ب", "بے", "Bay", "بلی", "Cat", "🐱", "ب کی آواز: بَ (بلی)", "بے سے بلی، پیاری اور چنچل!", "Bay for Billi, lovely and playful!", 0xFF3B82F6),
    UrduAlphabetLetter("پ", "پے", "Pay", "پتنگ", "Kite", "🪁", "پ کی آواز: پَ (پتنگ)", "پے سے پتنگ، ہوا میں اڑے!", "Pay for Patang, flying in the sky!", 0xFFF59E0B),
    UrduAlphabetLetter("ت", "تے", "Tay", "تتلی", "Butterfly", "🦋", "ت کی آواز: تَ (تتلی)", "تے سے تتلی، رنگ برنگی تتلی!", "Tay for Titli, colorful butterfly!", 0xFF10B981),
    UrduAlphabetLetter("ٹ", "ٹے", "Ttay", "ٹماٹر", "Tomato", "🍅", "ٹ کی آواز: ٹَ (ٹماٹر)", "ٹے سے ٹماٹر، لال لال ٹماٹر!", "Ttay for Tamatar, fresh red tomato!", 0xFFE11D48),
    UrduAlphabetLetter("ث", "ثے", "Say", "ثمر", "Fruits", "🍇", "ث کی آواز: ثَ (ثمر)", "ثے سے ثمر، میٹھے میٹھے پھل!", "Say for Samar, sweet wholesome fruits!", 0xFF8B5CF6),
    UrduAlphabetLetter("ج", "جیم", "Jeem", "جہاز", "Airplane", "✈️", "ج کی آواز: جَ (جہاز)", "جیم سے جہاز، بادلوں کے پار!", "Jeem for Jahaz, flying above the clouds!", 0xFF06B6D4),
    UrduAlphabetLetter("چ", "چے", "Chay", "چاند", "Moon", "🌙", "چ کی آواز: چَ (چاند)", "چے سے چاند، چمکے رات بھر!", "Chay for Chand, shining through the night!", 0xFFFBBF24),
    UrduAlphabetLetter("ح", "حے", "Hay", "حوض", "Fountain Pool", "⛲", "ح کی آواز: حَ (حوض)", "حے سے حوض، ٹھنڈا میٹھا پانی!", "Hay for Hauz, fresh and cool water!", 0xFF14B8A6),
    UrduAlphabetLetter("خ", "خے", "Khay", "خربوزہ", "Melon", "🍈", "خ کی آواز: خَ (خربوزہ)", "خے سے خربوزہ، رس بھرا خربوزہ!", "Khay for Kharbooza, sweet juicy melon!", 0xFF84CC16),
    UrduAlphabetLetter("د", "دال", "Daal", "درخت", "Tree", "🌳", "د کی آواز: دَ (درخت)", "دال سے درخت، سایہ دار درخت!", "Daal for Darakht, tall shady tree!", 0xFF16A34A),
    UrduAlphabetLetter("ڈ", "ڈال", "Ddaal", "ڈولفن", "Dolphin", "🐬", "ڈ کی آواز: ڈَ (ڈولفن)", "ڈال سے ڈولفن، لہروں میں ناچے!", "Ddaal for Dolphin, dancing in the waves!", 0xFF0284C7),
    UrduAlphabetLetter("ذ", "ذال", "Zaal", "ذخیرہ", "Treasure", "📦", "ذ کی آواز: ذَ (ذخیرہ)", "ذال سے ذخیرہ، علم کا خزانہ!", "Zaal for Zakheera, treasure trove of knowledge!", 0xFFD97706),
    UrduAlphabetLetter("ر", "رے", "Ray", "ریل گاڑی", "Train", "🚂", "ر کی آواز: رَ (ریل گاڑی)", "رے سے ریل گاڑی، چھک چھک جائے!", "Ray for Rail Gadi, chugging on the tracks!", 0xFFEA580C),
    UrduAlphabetLetter("ڑ", "ڑے", "Rray", "پہاڑ", "Mountain", "🏔️", "ڑ کی آواز: ڑَ (پہاڑ)", "ڑے سے پہاڑ، اونچا فلک بوس پہاڑ!", "Rray for Pahar, mighty high mountain!", 0xFF64748B),
    UrduAlphabetLetter("ز", "زے", "Zay", "زرافہ", "Giraffe", "🦒", "ز کی آواز: زَ (زرافہ)", "زے سے زرافہ، لمبی گردن والا!", "Zay for Zarafa, tall friendly giraffe!", 0xFFEAB308),
    UrduAlphabetLetter("ژ", "ژے", "Zhay", "ژالہ باری", "Hailstorm", "❄️", "ژ کی آواز: ژَ (ژالہ باری)", "ژے سے ژالہ باری، سفید سفید اولے!", "Zhay for Zhala Bari, sparkling hail!", 0xFF38BDF8),
    UrduAlphabetLetter("س", "سین", "Seen", "سورج", "Sun", "☀️", "س کی آواز: سَ (سورج)", "سین سے سورج، چمکے صبح سویرے!", "Seen for Sooraj, warm golden sunshine!", 0xFFF59E0B),
    UrduAlphabetLetter("ش", "شین", "Sheen", "شیر", "Lion", "🦁", "ش کی آواز: شَ (شیر)", "شین سے شیر، جنگل کا بادشاہ!", "Sheen for Sher, brave king of the jungle!", 0xFFD97706),
    UrduAlphabetLetter("ص", "صواد", "Suad", "صابن", "Soap", "🧼", "ص کی آواز: صَ (صابن)", "صواد سے صابن، ہاتھ دھوئیں صاف ستھرے!", "Suad for Saban, washing hands clean!", 0xFF06B6D4),
    UrduAlphabetLetter("ض", "ضواد", "Zuad", "ضعیف", "Elderly Grandparent", "👴", "ض کی آواز: ضَ (ضعیف)", "ضواد سے ضعیف، بزرگوں کی کریں خدمت!", "Zuad for Zaeef, caring for elders!", 0xFF6B7280),
    UrduAlphabetLetter("ط", "طوئیں", "Toyen", "طوطا", "Parrot", "🦜", "ط کی آواز: طَ (طوطا)", "طوئیں سے طوطا، بولے میٹھی بولی!", "Toyen for Tota, sweet singing parrot!", 0xFF22C55E),
    UrduAlphabetLetter("ظ", "ظوئیں", "Zoyen", "ظروف", "Clay Vessels", "🏺", "ظ کی آواز: ظَ (ظروف)", "ظوئیں سے ظروف، مٹی کے خوبصورت برتن!", "Zoyen for Zuroof, handcrafted earthen pots!", 0xFFB45309),
    UrduAlphabetLetter("ع", "عین", "Ain", "عینک", "Spectacles", "👓", "ع کی آواز: عَ (عینک)", "عین سے عینک، نظر آئے سب کچھ صاف!", "Ain for Ainak, crystal clear glasses!", 0xFF6366F1),
    UrduAlphabetLetter("غ", "غین", "Ghain", "غبارہ", "Balloon", "🎈", "غ کی آواز: غَ (غبارہ)", "غین سے غبارہ، رنگ برنگا غبارہ!", "Ghain for Ghubara, bright cheerful balloon!", 0xFFEC4899),
    UrduAlphabetLetter("ف", "فے", "Fay", "فوارہ", "Fountain", "⛲", "ف کی آواز: فَ (فوارہ)", "فے سے فوارہ، پانی اچھالے اوپر!", "Fay for Fawara, splashing joyful fountain!", 0xFF0284C7),
    UrduAlphabetLetter("ق", "قاف", "Qaaf", "قلم", "Pen", "✒️", "ق کی آواز: قَ (قلم)", "قاف سے قلم، لکھیں سبق اپنا!", "Qaaf for Qalam, write wisdom with the pen!", 0xFF7C3AED),
    UrduAlphabetLetter("ک", "کاف", "Kaaf", "کتاب", "Book", "📖", "ک کی آواز: کَ (کتاب)", "کاف سے کتاب، علم کا سمندر!", "Kaaf for Kitab, treasure box of knowledge!", 0xFF2563EB),
    UrduAlphabetLetter("گ", "گاف", "Gaaf", "گائے", "Cow", "🐄", "گ کی آواز: گَ (گائے)", "گاف سے گائے، دے مزیدار دودھ!", "Gaaf for Gaaye, gentle loving cow!", 0xFF10B981),
    UrduAlphabetLetter("ل", "لام", "Laam", "لیموں", "Lemon", "🍋", "ل کی آواز: لَ (لیموں)", "لام سے لیموں، شربت بنائیں ٹھنڈا!", "Laam for Leemoo, fresh zesty lemon!", 0xFFEAB308),
    UrduAlphabetLetter("م", "میم", "Meem", "مور", "Peacock", "🦚", "م کی آواز: مَ (مور)", "میم سے مور، پر پھیلائے ناچے!", "Meem for Mor, gorgeous dancing peacock!", 0xFF0D9488),
    UrduAlphabetLetter("ن", "نون", "Noon", "نلکا", "Water Tap", "🚰", "ن کی آواز: نَ (نلکا)", "نون سے نلکا، پانی کا تحفہ!", "Noon for Nalka, refreshing cool water!", 0xFF0284C7),
    UrduAlphabetLetter("و", "واؤ", "Wao", "وردی", "School Uniform", "🥋", "و کی آواز: وَ (وردی)", "واؤ سے وردی، پہن کے جائیں اسکول!", "Wao for Wardi, neat and smart uniform!", 0xFF4338CA),
    UrduAlphabetLetter("ہ", "ہے", "Hay", "ہاتھی", "Elephant", "🐘", "ہ کی آواز: ہَ (ہاتھی)", "ہے سے ہاتھی، سونڈ ہلائے مستی میں!", "Hay for Hathi, grand playful elephant!", 0xFF8B5CF6),
    UrduAlphabetLetter("ء", "حمزہ", "Hamza", "چائے", "Tea", "☕", "ء کی آواز: حمزہ", "حمزہ سے چائے، گرم گرم پیالی!", "Hamza for Chai, warm comforting cup!", 0xFFB45309),
    UrduAlphabetLetter("ی", "چھوٹی یے", "Choti Yae", "یکہ", "Horse Cart", "🐎", "ی کی آواز: یَ (یکہ)", "چھوٹی یے سے یکہ، ٹپ ٹپ دوڑے سڑک پر!", "Choti Yae for Yakka, cheerful horse carriage!", 0xFFD97706),
    UrduAlphabetLetter("ے", "بڑی یے", "Badi Yae", "تارے", "Stars", "⭐", "ے کی آواز: بڑی یے (تارے)", "بڑی یے سے تارے، چمکیں نیلا آسمان!", "Badi Yae for Taare, glittering evening stars!", 0xFFF59E0B)
)

@Composable
fun UrduAlphabetWritingStudio(
    modifier: Modifier = Modifier
) {
    var selectedIndex by remember { mutableIntStateOf(0) }
    val currentLang by LocaleManager.currentLanguage.collectAsState()
    val isUrdu = currentLang == LocaleManager.LANG_URDU
    val currentItem = URDU_ALPHABET_LIST[selectedIndex]

    val lines = remember(selectedIndex) { mutableStateListOf<DrawingLine>() }
    var currentPath by remember(selectedIndex) { mutableStateOf<Path?>(null) }
    var currentColor by remember { mutableStateOf(Color(0xFFF59E0B)) }
    val currentStrokeWidth by remember { mutableFloatStateOf(22f) }
    var hasPracticedWriting by remember(selectedIndex) { mutableStateOf(false) }
    var showParticleExplosion by remember(selectedIndex) { mutableStateOf(false) }

    val colors = listOf(
        Color(0xFFEF4444), // Red (لال)
        Color(0xFF2563EB), // Blue (نیلا)
        Color(0xFF10B981), // Green (سبز)
        Color(0xFFF59E0B), // Yellow/Amber (پیلا)
        Color(0xFF8B5CF6), // Purple (جامنی)
        Color(0xFF0F172A), // Black (سیاہ)
        Color(0xFFFFFFFF)  // Eraser (مٹانے والا)
    )

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("urdu_alphabet_studio"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        modifier = Modifier.weight(1f)
                    ) {
                        Box(
                            modifier = Modifier
                                .size(40.dp)
                                .clip(CircleShape)
                                .background(Color(currentItem.colorHex).copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = Color(currentItem.colorHex),
                                modifier = Modifier.size(22.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "حروفِ تہجی اور خوش خطی",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            )
                            Text(
                                text = "اردو حروف کی پہچان، آوازیں، الفاظ اور مشق (ا تا ے)",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    IconButton(
                        onClick = {
                            val speakText = "حرف ${currentItem.nameUr}۔ ${currentItem.wordUr}۔ ${currentItem.rhymeUr}"
                            TtsManager.speak(speakText, true)
                        },
                        modifier = Modifier.testTag("btn_speak_urdu_alphabet_overview")
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Pronounce",
                            tint = Color(currentItem.colorHex)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Alif to Yae Chips Horizontal Scroll
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    URDU_ALPHABET_LIST.forEachIndexed { index, item ->
                        val isSelected = selectedIndex == index
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                selectedIndex = index
                                TtsManager.speak("حرف ${item.nameUr}", true)
                            },
                            label = {
                                Text(
                                    text = "${item.letter} (${item.nameUr})",
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 15.sp
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(item.colorHex).copy(alpha = 0.22f),
                                selectedLabelColor = Color(item.colorHex)
                            ),
                            modifier = Modifier.testTag("urdu_chip_${item.letter}")
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
                                    text = currentItem.letter,
                                    style = MaterialTheme.typography.displayLarge.copy(
                                        fontWeight = FontWeight.Black,
                                        color = Color(currentItem.colorHex),
                                        fontSize = 58.sp
                                    )
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column {
                                    Text(
                                        text = currentItem.nameUr,
                                        style = MaterialTheme.typography.titleLarge.copy(
                                            fontWeight = FontWeight.Bold,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    )
                                    Text(
                                        text = currentItem.nameEn,
                                        style = MaterialTheme.typography.labelSmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "${currentItem.nameUr} سے ${currentItem.wordUr} (${currentItem.wordEn})",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )

                            Spacer(modifier = Modifier.height(4.dp))

                            Text(
                                text = currentItem.rhymeUr,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = FontWeight.SemiBold,
                                    color = Color(currentItem.colorHex)
                                )
                            )

                            Text(
                                text = currentItem.soundPhonics,
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }

                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(
                                text = currentItem.emoji,
                                fontSize = 52.sp,
                                modifier = Modifier
                                    .clickable {
                                        TtsManager.speak("${currentItem.nameUr} سے ${currentItem.wordUr}", true)
                                    }
                                    .testTag("urdu_object_emoji_${currentItem.letter}")
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            IconButton(
                                onClick = {
                                    TtsManager.speak("${currentItem.nameUr}۔ ${currentItem.wordUr}۔ ${currentItem.rhymeUr}", true)
                                },
                                modifier = Modifier
                                    .size(40.dp)
                                    .testTag("btn_speak_urdu_letter_${currentItem.letter}")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = "Hear pronunciation",
                                    tint = Color(currentItem.colorHex)
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Tracing & Writing Practice Header
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = "✍️ حرف '${currentItem.letter}' لکھنے کی مشق",
                            style = MaterialTheme.typography.labelLarge.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 16.sp
                            ),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "اپنی انگلی کی مدد سے نیچے دیئے گئے حرف پر ہاتھ پھیریں اور مشق کریں!",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }

                    if (hasPracticedWriting) {
                        Surface(
                            shape = RoundedCornerShape(8.dp),
                            color = EmeraldSuccess.copy(alpha = 0.15f)
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 8.dp, vertical = 4.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = EmeraldSuccess,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "شاباش!",
                                    style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                                    color = EmeraldSuccess
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Color Toolbar
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
                                        val colorNameUrdu = when (color) {
                                            Color(0xFFEF4444) -> "سرخ لال"
                                            Color(0xFF2563EB) -> "نیلا رنگ"
                                            Color(0xFF10B981) -> "سبز رنگ"
                                            Color(0xFFF59E0B) -> "پیلا رنگ"
                                            Color(0xFF8B5CF6) -> "جامنی رنگ"
                                            Color(0xFF0F172A) -> "سیاہ کالا رنگ"
                                            else -> "مٹانے والا صافی"
                                        }
                                        TtsManager.speak(colorNameUrdu, true)
                                    }
                            )
                        }
                    }

                    IconButton(
                        onClick = {
                            lines.clear()
                            currentPath = null
                        },
                        modifier = Modifier
                            .size(32.dp)
                            .testTag("btn_clear_urdu_canvas")
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
                        .height(210.dp)
                        .clip(RoundedCornerShape(14.dp))
                        .background(Color.White)
                        .border(1.5.dp, MaterialTheme.colorScheme.outline.copy(alpha = 0.3f), RoundedCornerShape(14.dp))
                        .testTag("urdu_writing_canvas")
                ) {
                    // Tracing Watermark of the Urdu letter
                    Text(
                        text = currentItem.letter,
                        fontSize = 140.sp,
                        fontWeight = FontWeight.Black,
                        color = Color.LightGray.copy(alpha = 0.28f),
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
                                        currentPath?.lineTo(change.position.x, change.position.y)
                                    },
                                    onDragEnd = {
                                        currentPath?.let {
                                            lines.add(DrawingLine(it, currentColor, currentStrokeWidth))
                                        }
                                        currentPath = null
                                    },
                                    onDragCancel = {
                                        currentPath = null
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

                Spacer(modifier = Modifier.height(12.dp))

                // Actions: Previous, Finish, Next
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    OutlinedButton(
                        onClick = {
                            if (selectedIndex > 0) {
                                selectedIndex--
                                TtsManager.speak("حرف ${URDU_ALPHABET_LIST[selectedIndex].nameUr}", true)
                            }
                        },
                        enabled = selectedIndex > 0,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("btn_prev_urdu_letter")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Previous letter",
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text("پچھلا")
                    }

                    Button(
                        onClick = {
                            showParticleExplosion = true
                            hasPracticedWriting = true
                            TtsManager.speak("شاباش! آپ نے حرف ${currentItem.nameUr} بہت خوبصورت لکھا!", true)
                        },
                        colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("btn_complete_urdu_writing")
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text("مشق مکمل! ✨", fontWeight = FontWeight.Bold)
                    }

                    OutlinedButton(
                        onClick = {
                            if (selectedIndex < URDU_ALPHABET_LIST.size - 1) {
                                selectedIndex++
                                TtsManager.speak("حرف ${URDU_ALPHABET_LIST[selectedIndex].nameUr}", true)
                            }
                        },
                        enabled = selectedIndex < URDU_ALPHABET_LIST.size - 1,
                        shape = RoundedCornerShape(10.dp),
                        modifier = Modifier.testTag("btn_next_urdu_letter")
                    ) {
                        Text("اگلا")
                        Spacer(modifier = Modifier.width(4.dp))
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                            contentDescription = "Next letter",
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }
            }

            // Positive reinforcement particle explosion
            ParticleExplosionEffect(
                trigger = showParticleExplosion,
                onAnimationEnd = { showParticleExplosion = false }
            )
        }
    }
}
