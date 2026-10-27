package com.triplestar.learn.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Remove
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Stop
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.triplestar.learn.ui.theme.EmeraldSuccess
import com.triplestar.learn.ui.theme.StarGoldPrimary
import com.triplestar.learn.util.LocaleManager
import com.triplestar.learn.util.MathHelper
import com.triplestar.learn.util.MathProgressManager
import com.triplestar.learn.util.TtsManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

data class MathNumber(
    val value: Int,
    val digitEn: String,
    val digitUr: String,
    val nameEn: String,
    val nameUr: String,
    val romanUr: String,
    val emoji: String,
    val itemNameEn: String,
    val itemNameUr: String,
    val colorHex: Long
)

data class MathPuzzle(
    val id: String,
    val isAddition: Boolean,
    val operand1: Int,
    val operand2: Int,
    val emoji: String,
    val itemNameEn: String,
    val itemNameUr: String,
    val options: List<Int>,
    val correctIndex: Int,
    val explanationEn: String,
    val explanationUr: String
)

data class MathQuizQuestion(
    val id: String,
    val questionEn: String,
    val questionUr: String,
    val emoji: String,
    val optionsEn: List<String>,
    val optionsUr: List<String>,
    val correctIndex: Int,
    val explanationEn: String,
    val explanationUr: String
)

val MATH_NUMBERS = listOf(
    MathNumber(1, "1", "۱", "One", "ایک", "Aik", "🍎", "Apple", "سیب", 0xFFEF4444),
    MathNumber(2, "2", "۲", "Two", "دو", "Do", "🌟", "Star", "ستارہ", 0xFFF59E0B),
    MathNumber(3, "3", "۳", "Three", "تین", "Teen", "🐱", "Cat", "بلی", 0xFF10B981),
    MathNumber(4, "4", "۴", "Four", "چار", "Chaar", "🚗", "Car", "گاڑی", 0xFF3B82F6),
    MathNumber(5, "5", "۵", "Five", "پانچ", "Paanch", "🎈", "Balloon", "غبارہ", 0xFFEC4899),
    MathNumber(6, "6", "۶", "Six", "چھ", "Chhay", "🌸", "Flower", "پھول", 0xFF8B5CF6),
    MathNumber(7, "7", "۷", "Seven", "سات", "Saat", "🦋", "Butterfly", "تتلی", 0xFF06B6D4),
    MathNumber(8, "8", "۸", "Eight", "آٹھ", "Aath", "🍓", "Strawberry", "سٹرابیری", 0xFFE11D48),
    MathNumber(9, "9", "۹", "Nine", "نو", "Nau", "⚽", "Football", "فٹ بال", 0xFF10B981),
    MathNumber(10, "10", "۱۰", "Ten", "دس", "Das", "🐠", "Fish", "مچھلی", 0xFFF97316),
    MathNumber(11, "11", "۱۱", "Eleven", "گیارہ", "Gyarah", "🚀", "Rocket", "راکٹ", 0xFF6366F1),
    MathNumber(12, "12", "۱۲", "Twelve", "بارہ", "Barah", "🎨", "Palette", "رنگ", 0xFFA855F7),
    MathNumber(13, "13", "۱۳", "Thirteen", "تیرہ", "Terah", "🐤", "Chick", "چوزہ", 0xFFEAB308),
    MathNumber(14, "14", "۱۴", "Fourteen", "چودہ", "Chaudah", "✈️", "Airplane", "جہاز", 0xFF0EA5E9),
    MathNumber(15, "15", "۱۵", "Fifteen", "پندرہ", "Pandrah", "🍪", "Cookie", "بسکٹ", 0xFFD97706),
    MathNumber(16, "16", "۱۶", "Sixteen", "سولہ", "Solah", "🎁", "Gift", "تحفہ", 0xFFEC4899),
    MathNumber(17, "17", "۱۷", "Seventeen", "سترہ", "Satrah", "🍦", "Ice Cream", "آئس کریم", 0xFF14B8A6),
    MathNumber(18, "18", "۱۸", "Eighteen", "اٹھارہ", "Atharah", "🔔", "Bell", "گھنٹی", 0xFFF59E0B),
    MathNumber(19, "19", "۱۹", "Nineteen", "انیس", "Unnees", "💎", "Gem", "ہیرا", 0xFF3B82F6),
    MathNumber(20, "20", "۲۰", "Twenty", "بیس", "Bees", "🏆", "Trophy", "ٹرافی", 0xFFEAB308)
)

val ADDITION_PUZZLES = listOf(
    MathPuzzle(
        id = "add_1",
        isAddition = true,
        operand1 = 2,
        operand2 = 3,
        emoji = "🍎",
        itemNameEn = "Apples",
        itemNameUr = "سیب",
        options = listOf(4, 5, 6),
        correctIndex = 1,
        explanationEn = "2 apples plus 3 apples equals 5 juicy apples!",
        explanationUr = "۲ سیب اور ۳ سیب مل کر ۵ سیب بنتے ہیں!"
    ),
    MathPuzzle(
        id = "add_2",
        isAddition = true,
        operand1 = 4,
        operand2 = 2,
        emoji = "🌟",
        itemNameEn = "Stars",
        itemNameUr = "ستارے",
        options = listOf(6, 7, 5),
        correctIndex = 0,
        explanationEn = "4 bright stars plus 2 more gives 6 glowing stars!",
        explanationUr = "۴ چمکتے ستارے اور ۲ مزید مل کر ۶ ستارے بنتے ہیں!"
    ),
    MathPuzzle(
        id = "add_3",
        isAddition = true,
        operand1 = 5,
        operand2 = 4,
        emoji = "🚗",
        itemNameEn = "Cars",
        itemNameUr = "گاڑیاں",
        options = listOf(8, 9, 10),
        correctIndex = 1,
        explanationEn = "5 zooming cars plus 4 cars equals 9 cars on the road!",
        explanationUr = "۵ گاڑیاں اور ۴ مزید مل کر کل ۹ گاڑیاں بنتی ہیں!"
    ),
    MathPuzzle(
        id = "add_4",
        isAddition = true,
        operand1 = 7,
        operand2 = 3,
        emoji = "🎈",
        itemNameEn = "Balloons",
        itemNameUr = "غبارے",
        options = listOf(9, 10, 11),
        correctIndex = 1,
        explanationEn = "7 colorful balloons plus 3 more equals a big bunch of 10!",
        explanationUr = "۷ رنگ برنگے غبارے اور ۳ مزید مل کر پورے ۱۰ بنتے ہیں!"
    ),
    MathPuzzle(
        id = "add_5",
        isAddition = true,
        operand1 = 6,
        operand2 = 6,
        emoji = "🌸",
        itemNameEn = "Flowers",
        itemNameUr = "پھول",
        options = listOf(11, 12, 13),
        correctIndex = 1,
        explanationEn = "6 blooming flowers plus 6 more makes a lovely bouquet of 12!",
        explanationUr = "۶ خوبصورت پھول اور ۶ مزید مل کر ۱۲ پھولوں کا گلدستہ بناتے ہیں!"
    )
)

val SUBTRACTION_PUZZLES = listOf(
    MathPuzzle(
        id = "sub_1",
        isAddition = false,
        operand1 = 5,
        operand2 = 2,
        emoji = "🍪",
        itemNameEn = "Cookies",
        itemNameUr = "بسکٹ",
        options = listOf(2, 3, 4),
        correctIndex = 1,
        explanationEn = "5 yummy cookies minus 2 eaten leaves 3 left!",
        explanationUr = "۵ مزیدار بسکٹوں میں سے ۲ کھا لیے تو ۳ باقی بچے!"
    ),
    MathPuzzle(
        id = "sub_2",
        isAddition = false,
        operand1 = 7,
        operand2 = 3,
        emoji = "🐠",
        itemNameEn = "Fish",
        itemNameUr = "مچھلیاں",
        options = listOf(4, 5, 3),
        correctIndex = 0,
        explanationEn = "7 swimming fish minus 3 that swam away leaves 4 in the pond!",
        explanationUr = "۷ مچھلیوں میں سے ۳ تیر کر چلی گئیں تو ۴ تالاب میں رہ گئیں!"
    ),
    MathPuzzle(
        id = "sub_3",
        isAddition = false,
        operand1 = 9,
        operand2 = 4,
        emoji = "🍓",
        itemNameEn = "Strawberries",
        itemNameUr = "سٹرابیریز",
        options = listOf(6, 5, 4),
        correctIndex = 1,
        explanationEn = "9 sweet strawberries take away 4 leaves 5 in the basket!",
        explanationUr = "۹ سٹرابیریز میں سے ۴ نکال لیں تو ٹوکری میں ۵ بچیں!"
    ),
    MathPuzzle(
        id = "sub_4",
        isAddition = false,
        operand1 = 10,
        operand2 = 6,
        emoji = "⚽",
        itemNameEn = "Footballs",
        itemNameUr = "فٹ بال",
        options = listOf(3, 4, 5),
        correctIndex = 1,
        explanationEn = "10 footballs minus 6 passed to friends leaves 4 footballs!",
        explanationUr = "۱۰ فٹ بال میں سے ۶ دوستوں کو دے دیے تو ۴ باقی رہے!"
    ),
    MathPuzzle(
        id = "sub_5",
        isAddition = false,
        operand1 = 8,
        operand2 = 5,
        emoji = "🦋",
        itemNameEn = "Butterflies",
        itemNameUr = "تتلیاں",
        options = listOf(3, 2, 4),
        correctIndex = 0,
        explanationEn = "8 fluttering butterflies minus 5 flying high leaves 3 in the garden!",
        explanationUr = "۸ تتلیوں میں سے ۵ اڑ گئیں تو باغ میں ۳ باقی رہیں!"
    )
)

val MATH_QUIZ_QUESTIONS = listOf(
    MathQuizQuestion(
        id = "quiz_1",
        questionEn = "Count the stars: ⭐ ⭐ ⭐ ⭐. How many are there in total?",
        questionUr = "ستارے گنیں: ⭐ ⭐ ⭐ ⭐۔ کل کتنے ستارے ہیں؟",
        emoji = "⭐",
        optionsEn = listOf("3 Three", "4 Four", "5 Five"),
        optionsUr = listOf("۳ تین", "۴ چار", "۵ پانچ"),
        correctIndex = 1,
        explanationEn = "1, 2, 3, 4! There are 4 twinkling stars!",
        explanationUr = "۱، ۲، ۳، ۴! بالکل چار چمکتے ستارے ہیں!"
    ),
    MathQuizQuestion(
        id = "quiz_2",
        questionEn = "What number comes right after 7?",
        questionUr = "عدد ۷ کے فوراً بعد کون سا عدد آتا ہے؟",
        emoji = "🔢",
        optionsEn = listOf("6 Six", "8 Eight", "9 Nine"),
        optionsUr = listOf("۶ چھ", "۸ آٹھ", "۹ نو"),
        correctIndex = 1,
        explanationEn = "Counting forward: 6, 7, 8! Eight comes after seven.",
        explanationUr = "۷ کے بعد عدد ۸ (آٹھ) آتا ہے!"
    ),
    MathQuizQuestion(
        id = "quiz_3",
        questionEn = "In multiplication tables, what is 3 × 4?",
        questionUr = "تین کے پہاڑے میں، ۳ × ۴ کا کیا جواب ہے؟",
        emoji = "✖️",
        optionsEn = listOf("10 Ten", "12 Twelve", "14 Fourteen"),
        optionsUr = listOf("۱۰ دس", "۱۲ بارہ", "۱۴ چودہ"),
        correctIndex = 1,
        explanationEn = "3 times 4 equals 12! (Three fours are twelve)",
        explanationUr = "تین چوکے بارہ! ۳ × ۴ = ۱۲!"
    ),
    MathQuizQuestion(
        id = "quiz_4",
        questionEn = "What is 8 - 3?",
        questionUr = "۸ - ۳ کا کیا جواب ہے؟",
        emoji = "➖",
        optionsEn = listOf("4 Four", "5 Five", "6 Six"),
        optionsUr = listOf("۴ چار", "۵ پانچ", "۶ چھ"),
        correctIndex = 1,
        explanationEn = "8 minus 3 leaves 5!",
        explanationUr = "۸ میں سے ۳ نکالیں تو ۵ بچتا ہے!"
    ),
    MathQuizQuestion(
        id = "quiz_5",
        questionEn = "How is 100 written in Eastern Arabic Urdu digits?",
        questionUr = "عدد ۱۰۰ کو اردو اعداد میں کیسے لکھا جاتا ہے؟",
        emoji = "💯",
        optionsEn = listOf("۱۰ (10)", "۱۰۰ (100)", "۱۰۰۰ (1000)"),
        optionsUr = listOf("۱۰ (دس)", "۱۰۰ (ایک سو)", "۱۰۰۰ (ایک ہزار)"),
        correctIndex = 1,
        explanationEn = "100 is written as ۱۰۰ in Urdu digits and pronounced 'Aik Sau'!",
        explanationUr = "۱۰۰ کو اردو میں 'ایک سو' کہتے ہیں اور ۱۰۰ لکھا جاتا ہے!"
    ),
    MathQuizQuestion(
        id = "quiz_6",
        questionEn = "If you have 5 apples 🍎 and receive 5 more 🍎, how many do you have?",
        questionUr = "اگر آپ کے پاس ۵ سیب 🍎 ہوں اور ۵ مزید مل جائیں تو کل کتنے ہوں گے؟",
        emoji = "🍎",
        optionsEn = listOf("8 Eight", "9 Nine", "10 Ten"),
        optionsUr = listOf("۸ آٹھ", "۹ نو", "۱۰ دس"),
        correctIndex = 2,
        explanationEn = "5 + 5 = 10 wonderful apples!",
        explanationUr = "۵ + ۵ = ۱۰ بہترین سیب!"
    )
)

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MathExplorerStudio(
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    LaunchedEffect(Unit) {
        MathProgressManager.init(context)
    }

    val currentLang by LocaleManager.currentLanguage.collectAsState()
    val isUrdu = currentLang == LocaleManager.LANG_URDU
    val progressState by MathProgressManager.state.collectAsState()

    // 0 = Visual Counting (1-20), 1 = Numbers 1-1000 (Hundreds & Tens), 2 = Tables (1-12), 3 = Arithmetic, 4 = Mini-Quiz
    var selectedMode by remember { mutableIntStateOf(0) }

    // Counting 1-20 State
    var selectedNumberIndex by remember { mutableIntStateOf(0) }
    var tappedCount by remember(selectedNumberIndex) { mutableIntStateOf(0) }

    // Numbers 1-1000 State
    var selected1000Number by remember { mutableIntStateOf(1) }

    // Tables State
    var selectedTable by remember { mutableIntStateOf(2) }

    // Addition Puzzle State
    var selectedAddPuzzleIndex by remember { mutableIntStateOf(0) }
    var selectedAddOption by remember(selectedAddPuzzleIndex) { mutableStateOf<Int?>(null) }
    var isAddCorrect by remember(selectedAddPuzzleIndex) { mutableStateOf<Boolean?>(null) }

    // Subtraction Puzzle State
    var selectedSubPuzzleIndex by remember { mutableIntStateOf(0) }
    var selectedSubOption by remember(selectedSubPuzzleIndex) { mutableStateOf<Int?>(null) }
    var isSubCorrect by remember(selectedSubPuzzleIndex) { mutableStateOf<Boolean?>(null) }

    // Quiz State
    var selectedQuizIndex by remember { mutableIntStateOf(0) }
    var selectedQuizOption by remember(selectedQuizIndex) { mutableStateOf<Int?>(null) }
    var isQuizCorrect by remember(selectedQuizIndex) { mutableStateOf<Boolean?>(null) }

    // Particle explosion trigger
    var showParticleExplosion by remember { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("math_explorer_studio"),
        shape = RoundedCornerShape(20.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 3.dp)
    ) {
        Box(modifier = Modifier.fillMaxWidth()) {
            Column(modifier = Modifier.padding(16.dp)) {
                // Header with Title & Audio Pronunciation & Stars Progress Badge
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
                                .background(Color(0xFF2563EB).copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Calculate,
                                contentDescription = null,
                                tint = Color(0xFF2563EB),
                                modifier = Modifier.size(24.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isUrdu) "ریاضی کی دنیا (۱ تا ۱۰۰۰)" else "Mathematics Studio (1–1000)",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 17.sp
                                )
                            )
                            Text(
                                text = if (isUrdu) "گنتی، سینکڑے، دہائیاں، پہاڑے اور پہیلیاں" else "Hundreds, Tens, Paharay & Practice",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        // Persistent Math Stars Badge
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(StarGoldPrimary.copy(alpha = 0.18f))
                                .padding(horizontal = 8.dp, vertical = 4.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Star,
                                    contentDescription = null,
                                    tint = StarGoldPrimary,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "${progressState.totalStars} ⭐",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                            }
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        IconButton(
                            onClick = {
                                val textToSpeak = when (selectedMode) {
                                    0 -> {
                                        val currentNumber = MATH_NUMBERS[selectedNumberIndex]
                                        if (isUrdu) "${currentNumber.digitUr}، ${currentNumber.nameUr}، ${currentNumber.value} ${currentNumber.itemNameUr}"
                                        else "${currentNumber.value}, ${currentNumber.nameEn}, ${currentNumber.value} ${currentNumber.itemNameEn}"
                                    }
                                    1 -> {
                                        if (isUrdu) "${MathHelper.toUrduDigits(selected1000Number)}، ${MathHelper.toUrduWords(selected1000Number)}"
                                        else "$selected1000Number, ${MathHelper.toEnglishWords(selected1000Number)}"
                                    }
                                    2 -> {
                                        if (isUrdu) "$selectedTable کا پہاڑا" else "Table of $selectedTable"
                                    }
                                    3 -> {
                                        val p = ADDITION_PUZZLES[selectedAddPuzzleIndex]
                                        if (isUrdu) "${p.operand1} جمع ${p.operand2} برابر ہے کیا؟" else "${p.operand1} plus ${p.operand2} equals what?"
                                    }
                                    else -> {
                                        val q = MATH_QUIZ_QUESTIONS[selectedQuizIndex]
                                        if (isUrdu) q.questionUr else q.questionEn
                                    }
                                }
                                TtsManager.speak(textToSpeak, isUrdu)
                            },
                            modifier = Modifier.testTag("btn_speak_math_current")
                        ) {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = "Speak current content",
                                tint = Color(0xFF2563EB)
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Mode Selector Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    FilterChip(
                        selected = selectedMode == 0,
                        onClick = { selectedMode = 0 },
                        label = {
                            Text(
                                text = if (isUrdu) "🍎 گنتی (۱ تا ۲۰)" else "🍎 Counting 1–20",
                                fontWeight = if (selectedMode == 0) FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        modifier = Modifier.testTag("chip_math_mode_counting")
                    )
                    FilterChip(
                        selected = selectedMode == 1,
                        onClick = { selectedMode = 1 },
                        label = {
                            Text(
                                text = if (isUrdu) "📜 گنتی (۱ تا ۱۰۰۰)" else "📜 Explorer (1–1000)",
                                fontWeight = if (selectedMode == 1) FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        modifier = Modifier.testTag("chip_math_mode_1000")
                    )
                    FilterChip(
                        selected = selectedMode == 2,
                        onClick = { selectedMode = 2 },
                        label = {
                            Text(
                                text = if (isUrdu) "✖️ پہاڑے (۱ تا ۱۲)" else "✖️ Tables (1–12)",
                                fontWeight = if (selectedMode == 2) FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        modifier = Modifier.testTag("chip_math_mode_tables")
                    )
                    FilterChip(
                        selected = selectedMode == 3,
                        onClick = { selectedMode = 3 },
                        label = {
                            Text(
                                text = if (isUrdu) "➕➖ حسابی پہیلیاں" else "➕➖ Visual Arithmetic",
                                fontWeight = if (selectedMode == 3) FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        modifier = Modifier.testTag("chip_math_mode_arithmetic")
                    )
                    FilterChip(
                        selected = selectedMode == 4,
                        onClick = { selectedMode = 4 },
                        label = {
                            Text(
                                text = if (isUrdu) "🎯 ریاضی کوئز" else "🎯 Math Quiz",
                                fontWeight = if (selectedMode == 4) FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        modifier = Modifier.testTag("chip_math_mode_quiz")
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                when (selectedMode) {
                    0 -> CountingSection(
                        isUrdu = isUrdu,
                        selectedNumberIndex = selectedNumberIndex,
                        tappedCount = tappedCount,
                        onNumberSelected = { index ->
                            selectedNumberIndex = index
                            tappedCount = 0
                            val num = MATH_NUMBERS[index]
                            val phrase = if (isUrdu) "${num.digitUr}، ${num.nameUr}" else "${num.value}, ${num.nameEn}"
                            TtsManager.speak(phrase, isUrdu)
                        },
                        onItemTapped = { itemIndex ->
                            val nextCount = itemIndex + 1
                            tappedCount = maxOf(tappedCount, nextCount)
                            val num = MATH_NUMBERS[selectedNumberIndex]
                            val countWord = if (isUrdu) {
                                if (itemIndex < MATH_NUMBERS.size) MATH_NUMBERS[itemIndex].nameUr else "$nextCount"
                            } else {
                                if (itemIndex < MATH_NUMBERS.size) MATH_NUMBERS[itemIndex].nameEn else "$nextCount"
                            }
                            TtsManager.speak(countWord, isUrdu)
                            if (nextCount == num.value) {
                                showParticleExplosion = true
                                MathProgressManager.recordNumberCounted(context, num.value)
                            }
                        }
                    )
                    1 -> Numbers1000Section(
                        isUrdu = isUrdu,
                        currentNumber = selected1000Number,
                        onNumberChange = { num ->
                            selected1000Number = num.coerceIn(1, 1000)
                            MathProgressManager.recordNumberCounted(context, selected1000Number)
                            val speakText = if (isUrdu) {
                                "${MathHelper.toUrduDigits(selected1000Number)}، ${MathHelper.toUrduWords(selected1000Number)}"
                            } else {
                                "$selected1000Number, ${MathHelper.toEnglishWords(selected1000Number)}"
                            }
                            TtsManager.speak(speakText, isUrdu)
                        }
                    )
                    2 -> MultiplicationTablesSection(
                        isUrdu = isUrdu,
                        currentTable = selectedTable,
                        onTableChange = { t ->
                            selectedTable = t
                            val speakText = if (isUrdu) "$t کا پہاڑا" else "Table of $t"
                            TtsManager.speak(speakText, isUrdu)
                        },
                        onCorrectQuiz = {
                            showParticleExplosion = true
                            MathProgressManager.recordTableMastered(context, selectedTable)
                        }
                    )
                    3 -> ArithmeticPuzzlesSection(
                        isUrdu = isUrdu,
                        selectedAddPuzzleIndex = selectedAddPuzzleIndex,
                        selectedAddOption = selectedAddOption,
                        isAddCorrect = isAddCorrect,
                        onAddPuzzleChange = { selectedAddPuzzleIndex = it },
                        onAddOptionSelected = { optionIndex ->
                            selectedAddOption = optionIndex
                            val puzzle = ADDITION_PUZZLES[selectedAddPuzzleIndex]
                            val correct = optionIndex == puzzle.correctIndex
                            isAddCorrect = correct
                            if (correct) {
                                showParticleExplosion = true
                                MathProgressManager.recordPuzzleCompletion(context, puzzle.id)
                                val praise = if (isUrdu) "شاباش! بالکل درست! ${puzzle.explanationUr}" else "Awesome! Correct! ${puzzle.explanationEn}"
                                TtsManager.speak(praise, isUrdu)
                            } else {
                                val retry = if (isUrdu) "دوبارہ گن کر کوشش کریں!" else "Count the items and try again!"
                                TtsManager.speak(retry, isUrdu)
                            }
                        },
                        selectedSubPuzzleIndex = selectedSubPuzzleIndex,
                        selectedSubOption = selectedSubOption,
                        isSubCorrect = isSubCorrect,
                        onSubPuzzleChange = { selectedSubPuzzleIndex = it },
                        onSubOptionSelected = { optionIndex ->
                            selectedSubOption = optionIndex
                            val puzzle = SUBTRACTION_PUZZLES[selectedSubPuzzleIndex]
                            val correct = optionIndex == puzzle.correctIndex
                            isSubCorrect = correct
                            if (correct) {
                                showParticleExplosion = true
                                MathProgressManager.recordPuzzleCompletion(context, puzzle.id)
                                val praise = if (isUrdu) "شاباش! بالکل درست! ${puzzle.explanationUr}" else "Awesome! Correct! ${puzzle.explanationEn}"
                                TtsManager.speak(praise, isUrdu)
                            } else {
                                val retry = if (isUrdu) "دوبارہ سوچیں اور کوشش کریں!" else "Look at the remaining items and try again!"
                                TtsManager.speak(retry, isUrdu)
                            }
                        }
                    )
                    4 -> QuizSection(
                        isUrdu = isUrdu,
                        currentIndex = selectedQuizIndex,
                        selectedOption = selectedQuizOption,
                        isCorrect = isQuizCorrect,
                        onQuestionChange = { newIdx ->
                            selectedQuizIndex = newIdx
                        },
                        onOptionSelected = { optionIndex ->
                            selectedQuizOption = optionIndex
                            val question = MATH_QUIZ_QUESTIONS[selectedQuizIndex]
                            val correct = optionIndex == question.correctIndex
                            isQuizCorrect = correct
                            if (correct) {
                                showParticleExplosion = true
                                MathProgressManager.recordQuizCompletion(context, question.id)
                                val praise = if (isUrdu) "شاباش! درست جواب! 🎉" else "Awesome! You nailed it! 🎉"
                                TtsManager.speak(praise, isUrdu)
                            } else {
                                val retry = if (isUrdu) "کوئی بات نہیں، دوبارہ کوشش کریں!" else "Almost! Give it another try!"
                                TtsManager.speak(retry, isUrdu)
                            }
                        }
                    )
                }
            }

            // Confetti Particle Explosion for positive reinforcement
            ParticleExplosionEffect(
                trigger = showParticleExplosion,
                onAnimationEnd = { showParticleExplosion = false }
            )
        }
    }
}

/**
 * 1. Visual Counting Section for 1 to 20 with interactive real-world emojis
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun CountingSection(
    isUrdu: Boolean,
    selectedNumberIndex: Int,
    tappedCount: Int,
    onNumberSelected: (Int) -> Unit,
    onItemTapped: (Int) -> Unit
) {
    val currentNumber = MATH_NUMBERS[selectedNumberIndex]

    Column {
        // Horizontal Number Selector (1 to 20)
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MATH_NUMBERS.forEachIndexed { index, item ->
                val isSelected = index == selectedNumberIndex
                val itemColor = Color(item.colorHex)
                Box(
                    modifier = Modifier
                        .size(44.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            if (isSelected) itemColor else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                        )
                        .clickable { onNumberSelected(index) }
                        .testTag("num_chip_${item.value}"),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isUrdu) item.digitUr else "${item.value}",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Hero Number Display Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("card_number_hero"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(
                containerColor = Color(currentNumber.colorHex).copy(alpha = 0.12f)
            )
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    // English Digit
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(currentNumber.colorHex)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentNumber.digitEn,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }

                    // Word Pronunciation (Bilingual)
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = if (isUrdu) currentNumber.nameUr else currentNumber.nameEn,
                            fontSize = 24.sp,
                            fontWeight = FontWeight.Bold,
                            color = Color(currentNumber.colorHex)
                        )
                        Text(
                            text = if (isUrdu) "${currentNumber.nameEn} • ${currentNumber.romanUr}" else "${currentNumber.nameUr} (${currentNumber.digitUr})",
                            fontSize = 14.sp,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = if (isUrdu) "${currentNumber.value} ${currentNumber.itemNameUr}" else "${currentNumber.value} ${currentNumber.itemNameEn}",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Medium,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }

                    // Urdu Eastern Arabic Numeral
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(RoundedCornerShape(14.dp))
                            .background(Color(currentNumber.colorHex)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = currentNumber.digitUr,
                            fontSize = 32.sp,
                            fontWeight = FontWeight.Black,
                            color = Color.White
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Interactive tap-to-count prompt
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.AutoAwesome,
                        contentDescription = null,
                        tint = StarGoldPrimary,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isUrdu) {
                            "اشیاء کو چھوئیں اور گنیں: $tappedCount / ${currentNumber.value}"
                        } else {
                            "Tap each object to count: $tappedCount / ${currentNumber.value}"
                        },
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = Color(currentNumber.colorHex)
                    )
                }

                Spacer(modifier = Modifier.height(12.dp))

                // Interactive Objects Flow Grid
                FlowRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("grid_counting_objects"),
                    horizontalArrangement = Arrangement.Center,
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    maxItemsInEachRow = 5
                ) {
                    for (i in 0 until currentNumber.value) {
                        val isCounted = i < tappedCount
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .size(52.dp)
                                .clip(RoundedCornerShape(12.dp))
                                .background(
                                    if (isCounted) Color(currentNumber.colorHex).copy(alpha = 0.25f)
                                    else MaterialTheme.colorScheme.surface
                                )
                                .border(
                                    width = if (isCounted) 2.dp else 1.dp,
                                    color = if (isCounted) Color(currentNumber.colorHex) else MaterialTheme.colorScheme.outlineVariant,
                                    shape = RoundedCornerShape(12.dp)
                                )
                                .clickable { onItemTapped(i) }
                                .testTag("item_count_$i"),
                            contentAlignment = Alignment.Center
                        ) {
                            Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                Text(
                                    text = currentNumber.emoji,
                                    fontSize = 24.sp
                                )
                                Text(
                                    text = "${i + 1}",
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCounted) Color(currentNumber.colorHex) else MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                    }
                }
            }
        }
    }
}

/**
 * 2. Numbers 1 to 1000 Section strictly grouped by HUNDREDS (1-100, 101-200, ..., 901-1000)
 * and TENS (1-10, 11-20, 21-30, ...), with search, steppers, and bilingual pronunciations
 */
@Composable
private fun Numbers1000Section(
    isUrdu: Boolean,
    currentNumber: Int,
    onNumberChange: (Int) -> Unit
) {
    var searchInput by remember { mutableStateOf("") }
    val focusManager = LocalFocusManager.current

    // Grouping by Hundreds: 0..9 (1..100, 101..200, ..., 901..1000)
    var selectedHundredIndex by remember { mutableIntStateOf(((currentNumber - 1).coerceIn(0, 999)) / 100) }

    // Grouping by Tens within the selected hundred: 0..9 (1..10, 11..20, ..., 91..100)
    var selectedTenIndex by remember { mutableIntStateOf((((currentNumber - 1).coerceIn(0, 999)) % 100) / 10) }

    // Synchronize when currentNumber changes via stepper or search
    LaunchedEffect(currentNumber) {
        val h = ((currentNumber - 1).coerceIn(0, 999)) / 100
        val t = (((currentNumber - 1).coerceIn(0, 999)) % 100) / 10
        selectedHundredIndex = h
        selectedTenIndex = t
    }

    val startOfHundred = selectedHundredIndex * 100 + 1
    val endOfHundred = (selectedHundredIndex + 1) * 100

    val startOfTen = selectedHundredIndex * 100 + selectedTenIndex * 10 + 1
    val endOfTen = startOfTen + 9
    val activeDecade = startOfTen..endOfTen

    Column {
        // Quick Search / Jump to Number
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically
        ) {
            OutlinedTextField(
                value = searchInput,
                onValueChange = { input ->
                    if (input.all { it.isDigit() } && input.length <= 4) {
                        searchInput = input
                        val num = input.toIntOrNull()
                        if (num != null && num in 1..1000) {
                            onNumberChange(num)
                        }
                    }
                },
                modifier = Modifier
                    .weight(1f)
                    .testTag("input_search_number"),
                placeholder = {
                    Text(
                        text = if (isUrdu) "کوئی بھی عدد تلاش کریں (۱ تا ۱۰۰۰)" else "Search any number (1–1000)",
                        fontSize = 13.sp
                    )
                },
                leadingIcon = {
                    Icon(imageVector = Icons.Default.Search, contentDescription = null, tint = Color(0xFF2563EB))
                },
                trailingIcon = {
                    if (searchInput.isNotEmpty()) {
                        IconButton(onClick = { searchInput = "" }) {
                            Icon(imageVector = Icons.Default.Clear, contentDescription = "Clear")
                        }
                    }
                },
                singleLine = true,
                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number, imeAction = ImeAction.Done),
                keyboardActions = KeyboardActions(onDone = { focusManager.clearFocus() }),
                shape = RoundedCornerShape(12.dp)
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Hero Number Spotlight Card
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("card_spotlight_number_1000"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = Color(0xFF2563EB).copy(alpha = 0.08f)),
            border = androidx.compose.foundation.BorderStroke(1.5.dp, Color(0xFF2563EB).copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Stepper Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    IconButton(
                        onClick = { if (currentNumber > 1) onNumberChange(currentNumber - 1) },
                        enabled = currentNumber > 1,
                        modifier = Modifier.testTag("btn_prev_number")
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowBack, contentDescription = "Previous")
                    }

                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(16.dp)
                    ) {
                        // English Numeral
                        Text(
                            text = "$currentNumber",
                            fontSize = 38.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF2563EB)
                        )
                        Text(
                            text = "•",
                            fontSize = 24.sp,
                            color = MaterialTheme.colorScheme.outlineVariant
                        )
                        // Urdu Eastern Arabic Numeral
                        Text(
                            text = MathHelper.toUrduDigits(currentNumber),
                            fontSize = 38.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF10B981)
                        )
                    }

                    IconButton(
                        onClick = { if (currentNumber < 1000) onNumberChange(currentNumber + 1) },
                        enabled = currentNumber < 1000,
                        modifier = Modifier.testTag("btn_next_number")
                    ) {
                        Icon(imageVector = Icons.AutoMirrored.Filled.ArrowForward, contentDescription = "Next")
                    }
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Bilingual Words
                Text(
                    text = MathHelper.toUrduWords(currentNumber),
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = MaterialTheme.colorScheme.onSurface,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = MathHelper.toEnglishWords(currentNumber),
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = MaterialTheme.colorScheme.onSurfaceVariant,
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(10.dp))

                // Tap to speak button
                FilledTonalButton(
                    onClick = {
                        val speakText = if (isUrdu) {
                            "${MathHelper.toUrduDigits(currentNumber)}، ${MathHelper.toUrduWords(currentNumber)}"
                        } else {
                            "$currentNumber, ${MathHelper.toEnglishWords(currentNumber)}"
                        }
                        TtsManager.speak(speakText, isUrdu)
                    },
                    modifier = Modifier.testTag("btn_speak_number_1000"),
                    shape = RoundedCornerShape(10.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = if (isUrdu) "تلفظ سنیں (Pronounce)" else "Listen Audio",
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Level 1: Hundreds Selector (1–100, 101–200, ..., 901–1000)
        Text(
            text = if (isUrdu) "۱. سینکڑے منتخب کریں (Hundreds):" else "1. Select Hundred (1–1000):",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (h in 0..9) {
                val start = h * 100 + 1
                val end = (h + 1) * 100
                val isSelected = h == selectedHundredIndex
                val label = if (isUrdu) {
                    "${MathHelper.toUrduDigits(start)}–${MathHelper.toUrduDigits(end)}"
                } else {
                    "$start–$end"
                }

                FilterChip(
                    selected = isSelected,
                    onClick = {
                        selectedHundredIndex = h
                        onNumberChange(start + selectedTenIndex * 10)
                    },
                    label = { Text(text = label, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium) },
                    modifier = Modifier.testTag("chip_hundred_$h")
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        // Level 2: Tens Selector (1–10, 11–20, ..., 91–100 within active hundred)
        Text(
            text = if (isUrdu) "۲. دہائیاں منتخب کریں (Tens):" else "2. Select Decade (Tens):",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurface
        )
        Spacer(modifier = Modifier.height(6.dp))
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (t in 0..9) {
                val start = selectedHundredIndex * 100 + t * 10 + 1
                val end = start + 9
                val isSelected = t == selectedTenIndex
                val label = if (isUrdu) {
                    "${MathHelper.toUrduDigits(start)}–${MathHelper.toUrduDigits(end)}"
                } else {
                    "$start–$end"
                }

                FilterChip(
                    selected = isSelected,
                    onClick = {
                        selectedTenIndex = t
                        onNumberChange(start)
                    },
                    label = { Text(text = label, fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium) },
                    modifier = Modifier.testTag("chip_ten_$t")
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // The 10 Numbers in the Active Decade
        LazyVerticalGrid(
            columns = GridCells.Fixed(5),
            modifier = Modifier
                .fillMaxWidth()
                .heightIn(max = 160.dp)
                .testTag("grid_numbers_1000"),
            verticalArrangement = Arrangement.spacedBy(6.dp),
            horizontalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            items(activeDecade.toList()) { num ->
                val isSelected = num == currentNumber
                Box(
                    modifier = Modifier
                        .height(52.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            if (isSelected) Color(0xFF2563EB)
                            else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.45f)
                        )
                        .clickable { onNumberChange(num) }
                        .testTag("num_cell_$num"),
                    contentAlignment = Alignment.Center
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        Text(
                            text = "$num",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isSelected) Color.White else MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = MathHelper.toUrduDigits(num),
                            fontSize = 11.sp,
                            color = if (isSelected) Color.White.copy(alpha = 0.9f) else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

/**
 * 3. Multiplication Tables (Paharay 1 to 12) Section with rhythmic audio recitation
 */
@Composable
private fun MultiplicationTablesSection(
    isUrdu: Boolean,
    currentTable: Int,
    onTableChange: (Int) -> Unit,
    onCorrectQuiz: () -> Unit
) {
    val coroutineScope = rememberCoroutineScope()
    var isRecitingAll by remember(currentTable) { mutableStateOf(false) }

    // Interactive quiz for the current table
    var quizMultiplier by remember(currentTable) { mutableIntStateOf((1..10).random()) }
    var quizOptions by remember(currentTable, quizMultiplier) {
        val target = currentTable * quizMultiplier
        val wrong1 = target + currentTable
        val wrong2 = maxOf(1, target - currentTable)
        mutableStateOf(listOf(target, wrong1, wrong2).shuffled())
    }
    var selectedQuizAnswer by remember(currentTable, quizMultiplier) { mutableStateOf<Int?>(null) }
    var isQuizAnswerCorrect by remember(currentTable, quizMultiplier) { mutableStateOf<Boolean?>(null) }

    Column {
        // Table Selector 1 to 12
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            for (t in 1..12) {
                val isSelected = t == currentTable
                FilterChip(
                    selected = isSelected,
                    onClick = {
                        isRecitingAll = false
                        TtsManager.stop()
                        onTableChange(t)
                    },
                    label = {
                        Text(
                            text = if (isUrdu) "$t کا پہاڑا" else "Table $t",
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    modifier = Modifier.testTag("chip_table_$t")
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        // Table Header & Recite Entire Table Action
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = if (isUrdu) "$currentTable کا پہاڑا (${MathHelper.toUrduDigits(currentTable)})" else "Multiplication Table: $currentTable",
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = if (isUrdu) "سطر سنیں یا پورا پہاڑا چلائیں" else "Tap row or play full table",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            Button(
                onClick = {
                    if (isRecitingAll) {
                        isRecitingAll = false
                        TtsManager.stop()
                    } else {
                        isRecitingAll = true
                        coroutineScope.launch {
                            for (m in 1..10) {
                                if (!isRecitingAll) break
                                val product = currentTable * m
                                val phrase = if (isUrdu) {
                                    MathHelper.getTablePhraseUrdu(currentTable, m, product)
                                } else {
                                    MathHelper.getTablePhraseEnglish(currentTable, m, product)
                                }
                                TtsManager.speak(phrase, isUrdu)
                                delay(2200)
                            }
                            isRecitingAll = false
                        }
                    }
                },
                colors = ButtonDefaults.buttonColors(
                    containerColor = if (isRecitingAll) MaterialTheme.colorScheme.error else Color(0xFF2563EB)
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("btn_recite_entire_table")
            ) {
                Icon(
                    imageVector = if (isRecitingAll) Icons.Default.Stop else Icons.Default.PlayArrow,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
                Spacer(modifier = Modifier.width(4.dp))
                Text(
                    text = if (isRecitingAll) {
                        if (isUrdu) "روکیں" else "Stop"
                    } else {
                        if (isUrdu) "پورا سنیں" else "Play All"
                    },
                    fontWeight = FontWeight.Bold,
                    fontSize = 13.sp
                )
            }
        }

        Spacer(modifier = Modifier.height(12.dp))

        // Table Rows List
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("list_table_rows"),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            for (m in 1..10) {
                val product = currentTable * m
                val urduRow = "${MathHelper.toUrduDigits(currentTable)} × ${MathHelper.toUrduDigits(m)} = ${MathHelper.toUrduDigits(product)}"
                val enRow = "$currentTable × $m = $product"
                val urduPhrase = MathHelper.getTablePhraseUrdu(currentTable, m, product)
                val enPhrase = MathHelper.getTablePhraseEnglish(currentTable, m, product)

                Card(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            val phrase = if (isUrdu) urduPhrase else enPhrase
                            TtsManager.speak(phrase, isUrdu)
                        }
                        .testTag("row_table_${currentTable}_$m"),
                    shape = RoundedCornerShape(10.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 14.dp, vertical = 10.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = enRow,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF2563EB)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = "•",
                                color = MaterialTheme.colorScheme.outlineVariant
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = urduRow,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.Bold,
                                color = Color(0xFF10B981)
                            )
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(
                                text = if (isUrdu) urduPhrase else enPhrase,
                                fontSize = 12.sp,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                                contentDescription = "Speak row",
                                tint = Color(0xFF2563EB),
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Table Practice Challenge
        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("card_table_practice"),
            shape = RoundedCornerShape(16.dp),
            colors = CardDefaults.cardColors(containerColor = StarGoldPrimary.copy(alpha = 0.12f)),
            border = androidx.compose.foundation.BorderStroke(1.dp, StarGoldPrimary.copy(alpha = 0.3f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isUrdu) "⚡ پہاڑے کی فوری مشق:" else "⚡ Rapid Table Practice:",
                    style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Spacer(modifier = Modifier.height(6.dp))
                Text(
                    text = "$currentTable × $quizMultiplier = ?",
                    fontSize = 24.sp,
                    fontWeight = FontWeight.Black,
                    color = Color(0xFF2563EB)
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    val target = currentTable * quizMultiplier
                    quizOptions.forEach { opt ->
                        val isSelected = selectedQuizAnswer == opt
                        val isCorrect = opt == target
                        val btnColor = when {
                            selectedQuizAnswer == null -> MaterialTheme.colorScheme.surface
                            isSelected && isCorrect -> EmeraldSuccess.copy(alpha = 0.2f)
                            isSelected && !isCorrect -> MaterialTheme.colorScheme.error.copy(alpha = 0.2f)
                            !isCorrect && isQuizAnswerCorrect == false && isCorrect -> EmeraldSuccess.copy(alpha = 0.1f)
                            else -> MaterialTheme.colorScheme.surface
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(44.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(btnColor)
                                .border(1.5.dp, if (isSelected && isCorrect) EmeraldSuccess else MaterialTheme.colorScheme.outlineVariant, RoundedCornerShape(10.dp))
                                .clickable {
                                    selectedQuizAnswer = opt
                                    val correct = opt == target
                                    isQuizAnswerCorrect = correct
                                    if (correct) {
                                        onCorrectQuiz()
                                        val praise = if (isUrdu) "شاباش! درست جواب!" else "Awesome! Correct!"
                                        TtsManager.speak(praise, isUrdu)
                                    } else {
                                        val retry = if (isUrdu) "دوبارہ سوچیں!" else "Think again!"
                                        TtsManager.speak(retry, isUrdu)
                                    }
                                }
                                .testTag("btn_table_opt_$opt"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$opt",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                if (isQuizAnswerCorrect != null) {
                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = if (isQuizAnswerCorrect == true) {
                            if (isUrdu) "🎉 شاباش! بالکل درست جواب!" else "🎉 Excellent! Spot on!"
                        } else {
                            if (isUrdu) "💡 دوبارہ کوشش کریں یا اوپر دیے گئے پہاڑے کو دیکھیں!" else "💡 Check the table above and try again!"
                        },
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = if (isQuizAnswerCorrect == true) EmeraldSuccess else MaterialTheme.colorScheme.error
                    )
                }
            }
        }
    }
}

/**
 * 4. Visual Arithmetic Section (Addition and Subtraction Puzzles)
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun ArithmeticPuzzlesSection(
    isUrdu: Boolean,
    selectedAddPuzzleIndex: Int,
    selectedAddOption: Int?,
    isAddCorrect: Boolean?,
    onAddPuzzleChange: (Int) -> Unit,
    onAddOptionSelected: (Int) -> Unit,
    selectedSubPuzzleIndex: Int,
    selectedSubOption: Int?,
    isSubCorrect: Boolean?,
    onSubPuzzleChange: (Int) -> Unit,
    onSubOptionSelected: (Int) -> Unit
) {
    var subTab by remember { mutableIntStateOf(0) } // 0 = Addition, 1 = Subtraction

    Column {
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = subTab == 0,
                onClick = { subTab = 0 },
                label = { Text("➕ Visual Addition", fontWeight = FontWeight.Bold) },
                modifier = Modifier.weight(1f)
            )
            FilterChip(
                selected = subTab == 1,
                onClick = { subTab = 1 },
                label = { Text("➖ Visual Subtraction", fontWeight = FontWeight.Bold) },
                modifier = Modifier.weight(1f)
            )
        }

        Spacer(modifier = Modifier.height(14.dp))

        if (subTab == 0) {
            AdditionPuzzleSection(
                isUrdu = isUrdu,
                currentIndex = selectedAddPuzzleIndex,
                selectedOption = selectedAddOption,
                isCorrect = isAddCorrect,
                onPuzzleChange = onAddPuzzleChange,
                onOptionSelected = onAddOptionSelected
            )
        } else {
            SubtractionPuzzleSection(
                isUrdu = isUrdu,
                currentIndex = selectedSubPuzzleIndex,
                selectedOption = selectedSubOption,
                isCorrect = isSubCorrect,
                onPuzzleChange = onSubPuzzleChange,
                onOptionSelected = onSubOptionSelected
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun AdditionPuzzleSection(
    isUrdu: Boolean,
    currentIndex: Int,
    selectedOption: Int?,
    isCorrect: Boolean?,
    onPuzzleChange: (Int) -> Unit,
    onOptionSelected: (Int) -> Unit
) {
    val puzzle = ADDITION_PUZZLES[currentIndex]

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            ADDITION_PUZZLES.forEachIndexed { index, item ->
                FilterChip(
                    selected = index == currentIndex,
                    onClick = { onPuzzleChange(index) },
                    label = {
                        Text(
                            text = "${item.operand1} + ${item.operand2} = ?",
                            fontWeight = if (index == currentIndex) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    modifier = Modifier.testTag("chip_add_puzzle_$index")
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("card_addition_puzzle"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceEvenly,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        FlowRow(
                            horizontalArrangement = Arrangement.Center,
                            maxItemsInEachRow = 3
                        ) {
                            repeat(puzzle.operand1) {
                                Text(text = puzzle.emoji, fontSize = 24.sp, modifier = Modifier.padding(2.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${puzzle.operand1}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF2563EB)
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF2563EB)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Add, contentDescription = "Plus", tint = Color.White)
                    }

                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                        FlowRow(
                            horizontalArrangement = Arrangement.Center,
                            maxItemsInEachRow = 3
                        ) {
                            repeat(puzzle.operand2) {
                                Text(text = puzzle.emoji, fontSize = 24.sp, modifier = Modifier.padding(2.dp))
                            }
                        }
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = "${puzzle.operand2}",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Black,
                            color = Color(0xFF2563EB)
                        )
                    }

                    Text(text = "=", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)

                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isCorrect == true) EmeraldSuccess.copy(alpha = 0.2f)
                                else Color(0xFF2563EB).copy(alpha = 0.1f)
                            )
                            .border(
                                width = 2.dp,
                                color = if (isCorrect == true) EmeraldSuccess else Color(0xFF2563EB),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isCorrect == true) "${puzzle.options[puzzle.correctIndex]}" else "?",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isCorrect == true) EmeraldSuccess else Color(0xFF2563EB)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (isUrdu) "درست جواب کا انتخاب کریں:" else "Choose the correct answer:",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    puzzle.options.forEachIndexed { optIndex, value ->
                        val isSelected = selectedOption == optIndex
                        val btnColor = when {
                            selectedOption == null -> MaterialTheme.colorScheme.surface
                            isSelected && isCorrect == true -> EmeraldSuccess.copy(alpha = 0.2f)
                            isSelected && isCorrect == false -> MaterialTheme.colorScheme.error.copy(alpha = 0.2f)
                            else -> MaterialTheme.colorScheme.surface
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(btnColor)
                                .border(
                                    2.dp,
                                    if (isSelected && isCorrect == true) EmeraldSuccess else MaterialTheme.colorScheme.outlineVariant,
                                    RoundedCornerShape(14.dp)
                                )
                                .clickable { onOptionSelected(optIndex) }
                                .testTag("btn_add_option_$optIndex"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$value",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected && isCorrect == true) EmeraldSuccess else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                if (isCorrect != null) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isCorrect) EmeraldSuccess.copy(alpha = 0.12f)
                            else MaterialTheme.colorScheme.error.copy(alpha = 0.12f)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.ErrorOutline,
                                contentDescription = null,
                                tint = if (isCorrect) EmeraldSuccess else MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isCorrect) {
                                    if (isUrdu) "شاباش! ${puzzle.explanationUr}" else "Awesome! ${puzzle.explanationEn}"
                                } else {
                                    if (isUrdu) "کوئی بات نہیں، اشیاء کو دوبارہ گنیں اور کوشش کریں!" else "Almost there! Count the objects and try again!"
                                },
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                color = if (isCorrect) EmeraldSuccess else MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun SubtractionPuzzleSection(
    isUrdu: Boolean,
    currentIndex: Int,
    selectedOption: Int?,
    isCorrect: Boolean?,
    onPuzzleChange: (Int) -> Unit,
    onOptionSelected: (Int) -> Unit
) {
    val puzzle = SUBTRACTION_PUZZLES[currentIndex]

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            SUBTRACTION_PUZZLES.forEachIndexed { index, item ->
                FilterChip(
                    selected = index == currentIndex,
                    onClick = { onPuzzleChange(index) },
                    label = {
                        Text(
                            text = "${item.operand1} - ${item.operand2} = ?",
                            fontWeight = if (index == currentIndex) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    modifier = Modifier.testTag("chip_sub_puzzle_$index")
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("card_subtraction_puzzle"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Text(
                    text = if (isUrdu) "کل اشیاء میں سے تفریق شدہ اشیاء کو الگ کریں:" else "Start with total and take away crossed-out items:",
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(10.dp))

                FlowRow(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    maxItemsInEachRow = 5
                ) {
                    for (i in 0 until puzzle.operand1) {
                        val isRemoved = i >= (puzzle.operand1 - puzzle.operand2)
                        Box(
                            modifier = Modifier
                                .padding(4.dp)
                                .size(44.dp)
                                .clip(RoundedCornerShape(10.dp))
                                .background(
                                    if (isRemoved) MaterialTheme.colorScheme.error.copy(alpha = 0.15f)
                                    else EmeraldSuccess.copy(alpha = 0.15f)
                                )
                                .border(
                                    1.dp,
                                    if (isRemoved) MaterialTheme.colorScheme.error.copy(alpha = 0.4f)
                                    else EmeraldSuccess.copy(alpha = 0.4f),
                                    RoundedCornerShape(10.dp)
                                ),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = puzzle.emoji,
                                fontSize = 22.sp,
                                textDecoration = if (isRemoved) TextDecoration.LineThrough else TextDecoration.None
                            )
                        }
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.Center,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = "${puzzle.operand1}", fontSize = 26.sp, fontWeight = FontWeight.Black, color = Color(0xFFEF4444))
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(
                        modifier = Modifier.size(34.dp).clip(CircleShape).background(Color(0xFFEF4444)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(imageVector = Icons.Default.Remove, contentDescription = "Minus", tint = Color.White)
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "${puzzle.operand2}", fontSize = 26.sp, fontWeight = FontWeight.Black, color = Color(0xFFEF4444))
                    Spacer(modifier = Modifier.width(12.dp))
                    Text(text = "=", fontSize = 26.sp, fontWeight = FontWeight.Bold, color = MaterialTheme.colorScheme.onSurface)
                    Spacer(modifier = Modifier.width(12.dp))
                    Box(
                        modifier = Modifier
                            .size(48.dp)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isCorrect == true) EmeraldSuccess.copy(alpha = 0.2f)
                                else Color(0xFFEF4444).copy(alpha = 0.1f)
                            )
                            .border(
                                width = 2.dp,
                                color = if (isCorrect == true) EmeraldSuccess else Color(0xFFEF4444),
                                shape = RoundedCornerShape(12.dp)
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = if (isCorrect == true) "${puzzle.options[puzzle.correctIndex]}" else "?",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.Black,
                            color = if (isCorrect == true) EmeraldSuccess else Color(0xFFEF4444)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = if (isUrdu) "کتنی اشیاء باقی رہ گئیں؟" else "How many are left?",
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(10.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    puzzle.options.forEachIndexed { optIndex, value ->
                        val isSelected = selectedOption == optIndex
                        val btnColor = when {
                            selectedOption == null -> MaterialTheme.colorScheme.surface
                            isSelected && isCorrect == true -> EmeraldSuccess.copy(alpha = 0.2f)
                            isSelected && isCorrect == false -> MaterialTheme.colorScheme.error.copy(alpha = 0.2f)
                            else -> MaterialTheme.colorScheme.surface
                        }

                        Box(
                            modifier = Modifier
                                .weight(1f)
                                .height(52.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(btnColor)
                                .border(
                                    2.dp,
                                    if (isSelected && isCorrect == true) EmeraldSuccess else MaterialTheme.colorScheme.outlineVariant,
                                    RoundedCornerShape(14.dp)
                                )
                                .clickable { onOptionSelected(optIndex) }
                                .testTag("btn_sub_option_$optIndex"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "$value",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = if (isSelected && isCorrect == true) EmeraldSuccess else MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }

                if (isCorrect != null) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isCorrect) EmeraldSuccess.copy(alpha = 0.12f)
                            else MaterialTheme.colorScheme.error.copy(alpha = 0.12f)
                        )
                    ) {
                        Row(
                            modifier = Modifier.padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = if (isCorrect) Icons.Default.CheckCircle else Icons.Default.ErrorOutline,
                                contentDescription = null,
                                tint = if (isCorrect) EmeraldSuccess else MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = if (isCorrect) {
                                    if (isUrdu) "شاباش! ${puzzle.explanationUr}" else "Awesome! ${puzzle.explanationEn}"
                                } else {
                                    if (isUrdu) "کوئی بات نہیں، باقی بچ جانے والی اشیاء دوبارہ گنیں!" else "Almost there! Count the remaining items and try again!"
                                },
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                color = if (isCorrect) EmeraldSuccess else MaterialTheme.colorScheme.error
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * 5. Math Quiz Section with immediate feedback, explanation, and data persistence
 */
@Composable
private fun QuizSection(
    isUrdu: Boolean,
    currentIndex: Int,
    selectedOption: Int?,
    isCorrect: Boolean?,
    onQuestionChange: (Int) -> Unit,
    onOptionSelected: (Int) -> Unit
) {
    val question = MATH_QUIZ_QUESTIONS[currentIndex]

    Column {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            MATH_QUIZ_QUESTIONS.forEachIndexed { index, q ->
                FilterChip(
                    selected = index == currentIndex,
                    onClick = { onQuestionChange(index) },
                    label = {
                        Text(
                            text = "${q.emoji} Q${index + 1}",
                            fontWeight = if (index == currentIndex) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    modifier = Modifier.testTag("chip_quiz_q_$index")
                )
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Card(
            modifier = Modifier
                .fillMaxWidth()
                .testTag("card_math_quiz"),
            shape = RoundedCornerShape(18.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(Color(0xFF2563EB).copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = question.emoji, fontSize = 20.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = if (isUrdu) question.questionUr else question.questionEn,
                        style = MaterialTheme.typography.titleMedium.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 17.sp
                        ),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                val options = if (isUrdu) question.optionsUr else question.optionsEn
                options.forEachIndexed { optIndex, optionText ->
                    val isSelected = selectedOption == optIndex
                    val cardBg = when {
                        selectedOption == null -> MaterialTheme.colorScheme.surface
                        isSelected && isCorrect == true -> EmeraldSuccess.copy(alpha = 0.2f)
                        isSelected && isCorrect == false -> MaterialTheme.colorScheme.error.copy(alpha = 0.2f)
                        else -> MaterialTheme.colorScheme.surface
                    }

                    val borderColor = when {
                        selectedOption == null -> MaterialTheme.colorScheme.outlineVariant
                        isSelected && isCorrect == true -> EmeraldSuccess
                        isSelected && isCorrect == false -> MaterialTheme.colorScheme.error
                        else -> MaterialTheme.colorScheme.outlineVariant
                    }

                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(vertical = 4.dp)
                            .clickable { onOptionSelected(optIndex) }
                            .testTag("quiz_option_$optIndex"),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = cardBg),
                        border = androidx.compose.foundation.BorderStroke(1.5.dp, borderColor)
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween
                        ) {
                            Text(
                                text = optionText,
                                style = MaterialTheme.typography.bodyMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                ),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            if (isSelected) {
                                Icon(
                                    imageVector = if (isCorrect == true) Icons.Default.CheckCircle else Icons.Default.ErrorOutline,
                                    contentDescription = null,
                                    tint = if (isCorrect == true) EmeraldSuccess else MaterialTheme.colorScheme.error
                                )
                            }
                        }
                    }
                }

                if (isCorrect != null) {
                    Spacer(modifier = Modifier.height(14.dp))
                    Card(
                        modifier = Modifier.fillMaxWidth(),
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(
                            containerColor = if (isCorrect) EmeraldSuccess.copy(alpha = 0.12f)
                            else MaterialTheme.colorScheme.error.copy(alpha = 0.12f)
                        )
                    ) {
                        Column(modifier = Modifier.padding(12.dp)) {
                            Text(
                                text = if (isCorrect) {
                                    if (isUrdu) "شاباش! بالکل درست! 🎉" else "Awesome! That is correct! 🎉"
                                } else {
                                    if (isUrdu) "کوئی بات نہیں، دوبارہ کوشش کریں!" else "Almost! Give it another try!"
                                },
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                color = if (isCorrect) EmeraldSuccess else MaterialTheme.colorScheme.error
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "💡 ${if (isUrdu) question.explanationUr else question.explanationEn}",
                                style = MaterialTheme.typography.bodySmall,
                                color = MaterialTheme.colorScheme.onSurface
                            )
                        }
                    }
                }
            }
        }
    }
}
