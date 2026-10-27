package com.triplestar.learn.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.VolumeUp
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.scale
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.triplestar.learn.ui.theme.AstroBluePrimary
import com.triplestar.learn.ui.theme.EmeraldSuccess
import com.triplestar.learn.ui.theme.StarGoldPrimary
import com.triplestar.learn.util.ComputerProgressManager
import com.triplestar.learn.util.LocaleManager
import com.triplestar.learn.util.TtsManager
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

// -------------------------------------------------------------
// Data Models for Hardware & Software
// -------------------------------------------------------------
enum class TechCategory {
    HARDWARE, SOFTWARE, INPUT_OUTPUT
}

data class ComputerPart(
    val id: String,
    val category: TechCategory,
    val nameEn: String,
    val nameUr: String,
    val roleEn: String,
    val roleUr: String,
    val detailEn: String,
    val detailUr: String,
    val icon: ImageVector,
    val emoji: String,
    val accentColor: Color,
    val kidAnalogyEn: String,
    val kidAnalogyUr: String
)

val COMPUTER_PARTS = listOf(
    ComputerPart(
        id = "cpu",
        category = TechCategory.HARDWARE,
        nameEn = "CPU (Processor)",
        nameUr = "سی پی یو (پراسیسر)",
        roleEn = "The Brain of the Computer",
        roleUr = "کمپیوٹر کا دماغ",
        detailEn = "The Central Processing Unit calculates math, follows code instructions, and coordinates all tasks billions of times per second!",
        detailUr = "سی پی یو ہر سیکنڈ میں اربوں ہدایات اور حسابی کام سرانجام دیتا ہے اور پورے کمپیوٹر کو کنٹرول کرتا ہے۔",
        icon = Icons.Default.Memory,
        emoji = "🧠",
        accentColor = Color(0xFF0284C7),
        kidAnalogyEn = "Like your brain deciding what move to make in a game!",
        kidAnalogyUr = "جیسے آپ کا دماغ سوچ کر فیصلہ کرتا ہے!"
    ),
    ComputerPart(
        id = "ram",
        category = TechCategory.HARDWARE,
        nameEn = "RAM (Short-term Memory)",
        nameUr = "ریم (عارضی میموری)",
        roleEn = "Super-Fast Study Desk",
        roleUr = "تیز رفتار عارضی ڈیسک",
        detailEn = "Random Access Memory holds games and apps you are currently playing. When you shut off the computer, RAM empties completely.",
        detailUr = "ریم ان ایپس کو عارضی طور پر محفوظ رکھتی ہے جو ابھی چل رہی ہیں۔ بجلی بند ہونے پر یہ خالی ہو جاتی ہے۔",
        icon = Icons.Default.DeveloperBoard,
        emoji = "⚡",
        accentColor = Color(0xFF10B981),
        kidAnalogyEn = "Like keeping open books on your study desk while doing homework!",
        kidAnalogyUr = "جیسے ہوم ورک کرتے ہوئے کھلی کتابیں میز پر رکھنا!"
    ),
    ComputerPart(
        id = "storage",
        category = TechCategory.HARDWARE,
        nameEn = "Storage (SSD / Hard Drive)",
        nameUr = "اسٹوریج (ایس ایس ڈی / ہارڈ ڈرائیو)",
        roleEn = "Permanent Giant Library",
        roleUr = "مستقل بڑی لائبریری",
        detailEn = "Stores all your photos, videos, school files, and saved games permanently, even when the power is turned off.",
        detailUr = "یہ آپ کی تمام تصاویر، ویڈیوز اور گیمز کو ہمیشہ محفوظ رکھتا ہے چاہے کمپیوٹر بند ہی کیوں نہ ہو۔",
        icon = Icons.Default.Storage,
        emoji = "📚",
        accentColor = Color(0xFFF59E0B),
        kidAnalogyEn = "Like a huge backpack that holds all your books forever!",
        kidAnalogyUr = "جیسے ایک بڑا بستہ جس میں کتابیں ہمیشہ محفوظ رہتی ہیں!"
    ),
    ComputerPart(
        id = "gpu",
        category = TechCategory.HARDWARE,
        nameEn = "GPU (Graphics Card)",
        nameUr = "گرافکس کارڈ (GPU)",
        roleEn = "The Master Visual Artist",
        roleUr = "شاندار تصویری آرٹسٹ",
        detailEn = "Specialized chip dedicated to drawing 3D game worlds, smooth animations, colors, and high-definition videos.",
        detailUr = "یہ خاص چپ ویڈیو گیمز کے تھری ڈی مناظر، خوبصورت اینیمیشنز اور ویڈیوز تیار کرتی ہے۔",
        icon = Icons.Default.SportsEsports,
        emoji = "🎨",
        accentColor = Color(0xFF8B5CF6),
        kidAnalogyEn = "Like having an ultra-fast artist drawing 60 frames every single second!",
        kidAnalogyUr = "جیسے کوئی ماہر مصور ہر سیکنڈ میں 60 خوبصورت تصویریں بنا رہا ہو!"
    ),
    ComputerPart(
        id = "motherboard",
        category = TechCategory.HARDWARE,
        nameEn = "Motherboard",
        nameUr = "مدر بورڈ",
        roleEn = "The Grand Super-Highway",
        roleUr = "اہم برقی شاہراہ",
        detailEn = "The large circuit board that connects CPU, RAM, Storage, and Power together so they can speak to each other.",
        detailUr = "یہ بڑا سرکٹ بورڈ ہے جو سی پی یو، ریم اور تمام پرزوں کو ایک دوسرے سے جوڑتا ہے۔",
        icon = Icons.Default.Hub,
        emoji = "🛣️",
        accentColor = Color(0xFFEC4899),
        kidAnalogyEn = "Like city roads connecting all houses, schools, and hospitals!",
        kidAnalogyUr = "جیسے شہر کی سڑکیں تمام گھروں اور اسکولوں کو آپس میں جوڑتی ہیں!"
    ),
    ComputerPart(
        id = "os",
        category = TechCategory.SOFTWARE,
        nameEn = "Operating System (OS)",
        nameUr = "آپریٹنگ سسٹم",
        roleEn = "The Master Manager",
        roleUr = "کمپیوٹر کا بڑا مینیجر",
        detailEn = "Android, Windows, or Linux. The master program that manages hardware and lets you run your favorite apps.",
        detailUr = "اینڈرائیڈ یا ونڈوز؛ یہ بنیادی سافٹ ویئر ہے جو کمپیوٹر کو چلاتا ہے اور ایپس کو کنٹرول کرتا ہے۔",
        icon = Icons.Default.SettingsApplications,
        emoji = "📋",
        accentColor = Color(0xFF06B6D4),
        kidAnalogyEn = "Like the principal of a school managing all classrooms and teachers!",
        kidAnalogyUr = "جیسے اسکول کا پرنسپل تمام کلاسوں کا نظام چلاتا ہے!"
    ),
    ComputerPart(
        id = "input_devices",
        category = TechCategory.INPUT_OUTPUT,
        nameEn = "Input Devices",
        nameUr = "ان پٹ آلات (کی بورڈ، ماؤس)",
        roleEn = "Sending Commands In",
        roleUr = "کمپیوٹر کو حکم دینا",
        detailEn = "Keyboard, Mouse, Microphone, and Touchscreen help YOU send words, clicks, and sounds into the computer.",
        detailUr = "کی بورڈ، ماؤس اور مائیکروفون جن کے ذریعے ہم کمپیوٹر کو ہدایات اور آوازیں بھیجتے ہیں۔",
        icon = Icons.Default.Keyboard,
        emoji = "⌨️",
        accentColor = Color(0xFF14B8A6),
        kidAnalogyEn = "Like your ears and eyes gathering information for your brain!",
        kidAnalogyUr = "جیسے آپ کی آنکھیں اور کان معلومات اکٹھی کرتے ہیں!"
    ),
    ComputerPart(
        id = "output_devices",
        category = TechCategory.INPUT_OUTPUT,
        nameEn = "Output Devices",
        nameUr = "آؤٹ پٹ آلات (اسکرین، اسپیکر)",
        roleEn = "Showing Results Out",
        roleUr = "نتائج اور آوازیں دکھانا",
        detailEn = "Monitor screens, headphones, speakers, and printers show the pictures, audio, and documents created.",
        detailUr = "اسکرین، اسپیکر اور پرنٹر جو نتائج، ویڈیوز اور آوازیں ہمیں سناتے اور دکھاتے ہیں۔",
        icon = Icons.Default.Monitor,
        emoji = "🖥️",
        accentColor = Color(0xFFF43F5E),
        kidAnalogyEn = "Like your voice and smile showing what you are thinking!",
        kidAnalogyUr = "جیسے آپ کی مسکراہٹ اور آواز آپ کی بات دوسروں تک پہنچاتی ہے!"
    )
)

// -------------------------------------------------------------
// Data Models for Coding Rover Simulation
// -------------------------------------------------------------
enum class RoverAction(val titleEn: String, val titleUr: String, val emoji: String, val color: Color) {
    MOVE_FORWARD("Move Forward", "آگے بڑھیں", "⬆️", Color(0xFF0284C7)),
    TURN_LEFT("Turn Left", "بائیں مڑیں", "↩️", Color(0xFF8B5CF6)),
    TURN_RIGHT("Turn Right", "دائیں مڑیں", "↪️", Color(0xFF10B981)),
    JUMP_OBSTACLE("Jump Asteroid", "رکاوٹ کودیں", "🦘", Color(0xFFF59E0B)),
    REPEAT_TWO("Repeat 2x", "دو بار دہرائیں", "🔁", Color(0xFFEC4899))
}

enum class Direction { NORTH, EAST, SOUTH, WEST }

data class RoverGridLevel(
    val levelNumber: Int,
    val titleEn: String,
    val titleUr: String,
    val conceptEn: String,
    val conceptUr: String,
    val startX: Int,
    val startY: Int,
    val startDirection: Direction,
    val targetX: Int,
    val targetY: Int,
    val obstacles: List<Pair<Int, Int>>,
    val gems: List<Pair<Int, Int>> = emptyList(),
    val targetGoalEn: String,
    val targetGoalUr: String,
    val availableActions: List<RoverAction>
)

val ROVER_LEVELS = listOf(
    RoverGridLevel(
        levelNumber = 1,
        titleEn = "Level 1: First Steps (Sequencing)",
        titleUr = "لیول ۱: پہلا قدم (ترتیب وار کوڈنگ)",
        conceptEn = "Computers follow step-by-step instructions in exact sequence from top to bottom.",
        conceptUr = "کمپیوٹر ہدایات پر اوپر سے نیچے بالکل ترتیب کے ساتھ عمل کرتا ہے۔",
        startX = 0,
        startY = 2,
        startDirection = Direction.EAST,
        targetX = 3,
        targetY = 2,
        obstacles = emptyList(),
        gems = listOf(Pair(1, 2), Pair(2, 2)),
        targetGoalEn = "Program the Rover to move 3 steps forward to collect gems and reach the Star!",
        targetGoalUr = "روور کو ۳ قدم آگے چلنے کا کوڈ دیں تاکہ وہ ستارے تک پہنچ سکے!",
        availableActions = listOf(RoverAction.MOVE_FORWARD)
    ),
    RoverGridLevel(
        levelNumber = 2,
        titleEn = "Level 2: Direction Turns (Angles)",
        titleUr = "لیول ۲: رخ موڑنا (دائیں بائیں)",
        conceptEn = "Navigating around requires changing orientation with turn commands before moving.",
        conceptUr = "منزل تک پہنچنے کے لیے پہلے رخ موڑنا ہوتا ہے اور پھر آگے بڑھنا ہوتا ہے۔",
        startX = 0,
        startY = 3,
        startDirection = Direction.EAST,
        targetX = 2,
        targetY = 1,
        obstacles = listOf(Pair(1, 2)),
        gems = listOf(Pair(2, 3)),
        targetGoalEn = "Move forward, turn left, and walk forward to reach the Star!",
        targetGoalUr = "آگے بڑھیں، بائیں مڑیں اور ستارے تک پہنچیں!",
        availableActions = listOf(RoverAction.MOVE_FORWARD, RoverAction.TURN_LEFT, RoverAction.TURN_RIGHT)
    ),
    RoverGridLevel(
        levelNumber = 3,
        titleEn = "Level 3: Power Loops (Repeat)",
        titleUr = "لیول ۳: لوپ کا جادو (دہرانا)",
        conceptEn = "Instead of writing the same code again and again, programmers use LOOPS!",
        conceptUr = "ایک ہی کام بار بار لکھنے کے بجائے پروگرامرز 'لوپ' استعمال کرتے ہیں۔",
        startX = 0,
        startY = 0,
        startDirection = Direction.EAST,
        targetX = 3,
        targetY = 2,
        obstacles = listOf(Pair(1, 1)),
        gems = listOf(Pair(2, 0)),
        targetGoalEn = "Use the Repeat loop to quickly travel and reach the Star!",
        targetGoalUr = "روور کو تیز چلانے کے لیے لوپ (Repeat) کا استعمال کریں!",
        availableActions = listOf(RoverAction.MOVE_FORWARD, RoverAction.TURN_RIGHT, RoverAction.REPEAT_TWO)
    ),
    RoverGridLevel(
        levelNumber = 4,
        titleEn = "Level 4: Asteroid Jump (Conditionals)",
        titleUr = "لیول ۴: رکاوٹ عبور کرنا (کنڈیشنل لاجک)",
        conceptEn = "When facing an obstacle, smart code evaluates conditions: If Rock -> Jump!",
        conceptUr = "جب سامنے رکاوٹ آئے تو اسمارٹ لاجک کا استعمال کریں: اگر پتھر ہے تو کود جائیں!",
        startX = 0,
        startY = 1,
        startDirection = Direction.EAST,
        targetX = 3,
        targetY = 1,
        obstacles = listOf(Pair(1, 1)),
        gems = listOf(Pair(2, 1)),
        targetGoalEn = "Jump over the asteroid obstacle to reach the Star safely!",
        targetGoalUr = "پتھر پر سے چھلانگ لگا کر ستارے تک پہنچیں!",
        availableActions = listOf(RoverAction.MOVE_FORWARD, RoverAction.JUMP_OBSTACLE)
    )
)

// -------------------------------------------------------------
// Data Models for Cyber Safety
// -------------------------------------------------------------
data class CyberScenario(
    val id: String,
    val badgeId: String,
    val badgeTitleEn: String,
    val badgeTitleUr: String,
    val badgeEmoji: String,
    val questionEn: String,
    val questionUr: String,
    val optionSafeEn: String,
    val optionSafeUr: String,
    val optionUnsafeEn: String,
    val optionUnsafeUr: String,
    val explanationEn: String,
    val explanationUr: String,
    val tipEn: String,
    val tipUr: String
)

val CYBER_SCENARIOS = listOf(
    CyberScenario(
        id = "passwords",
        badgeId = "badge_password_hero",
        badgeTitleEn = "Password Guardian",
        badgeTitleUr = "پاس ورڈ کا محافظ",
        badgeEmoji = "🔑",
        questionEn = "You are creating a password for your favorite learning game. Which one is best?",
        questionUr = "آپ اپنے تعلیمی کھیل کے لیے پاس ورڈ بنا رہے ہیں۔ کون سا پاس ورڈ بہترین ہے؟",
        optionSafeEn = "A strong phrase with numbers and symbols (e.g., Star#Rocket99)",
        optionSafeUr = "نمبرز اور علامات والا مضبوط پاس ورڈ (جیسے Star#Rocket99)",
        optionUnsafeEn = "Your simple nickname or '123456'",
        optionUnsafeUr = "اپنا سادہ نام یا 123456",
        explanationEn = "Strong passwords with letters, numbers, and symbols stop bad actors from guessing your account!",
        explanationUr = "مضبوط پاس ورڈز میں حروف، نمبرز اور نشانات ہوتے ہیں جن کا اندازہ لگانا ناممکن ہوتا ہے۔",
        tipEn = "Never share your password with anyone except your parents!",
        tipUr = "اپنے والدین کے علاوہ کبھی کسی کو اپنا پاس ورڈ نہ بتائیں!"
    ),
    CyberScenario(
        id = "privacy",
        badgeId = "badge_privacy_shield",
        badgeTitleEn = "Privacy Shield",
        badgeTitleUr = "ذاتی معلومات کی ڈھال",
        badgeEmoji = "🛡️",
        questionEn = "Someone you met in an online game asks for your real home address and school name. What do you do?",
        questionUr = "آن لائن گیم میں ایک اجنبی آپ کا گھر کا پتہ اور اسکول کا نام پوچھتا ہے۔ آپ کیا کریں گے؟",
        optionSafeEn = "Politely say NO and immediately tell an adult/parent",
        optionSafeUr = "صاف انکار کریں اور فوراً اپنے والدین کو بتائیں",
        optionUnsafeEn = "Share it because they seem friendly in the game",
        optionUnsafeUr = "انہیں بتا دیں کیونکہ وہ گیم میں اچھے لگ رہے ہیں",
        explanationEn = "Personal Information (PII) like your address, phone, and school must always stay strictly private!",
        explanationUr = "گھر کا پتہ، فون نمبر اور اسکول کا نام ذاتی معلومات ہیں، انہیں اجنبیوں سے ہمیشہ چھپا کر رکھنا چاہیے!",
        tipEn = "Keep your real identity safe online at all times.",
        tipUr = "آن لائن دنیا میں اپنی اصل شناخت ہمیشہ محفوظ رکھیں۔"
    ),
    CyberScenario(
        id = "phishing",
        badgeId = "badge_phishing_detective",
        badgeTitleEn = "Phishing Detective",
        badgeTitleUr = "آن لائن دھوکے کی شناخت",
        badgeEmoji = "🕵️",
        questionEn = "A flashy popup appears on screen: '🎉 Congratulations! You won a FREE iPhone! Click here to claim!'",
        questionUr = "اسکرین پر اشتہار آتا ہے: 'مبارک ہو! آپ نے مفت آئی فون جیت لیا ہے! ابھی کلک کریں!'",
        optionSafeEn = "Close the tab immediately — it is a fake scam/trick link!",
        optionSafeUr = "صفحہ فوراً بند کر دیں — یہ ایک جعلی اشتہار اور دھوکہ ہے!",
        optionUnsafeEn = "Click the link quickly to enter your email and claim it",
        optionUnsafeUr = "جلدی سے کلک کر کے اپنی ای میل لکھیں",
        explanationEn = "Real companies never give away free expensive gifts through random popup links. It is phishing!",
        explanationUr = "کوئی بھی کمپنی ایسے مفت تحفے نہیں بانٹتی، یہ دھوکہ ہوتا ہے تاکہ وائرس بھیجا جا سکے۔",
        tipEn = "If something sounds too good to be true online, it is usually a trick.",
        tipUr = "اگر آن لائن کوئی چیز مفت نظر آئے تو وہ عام طور پر دھوکہ ہوتی ہے۔"
    ),
    CyberScenario(
        id = "etiquette",
        badgeId = "badge_digital_kindness",
        badgeTitleEn = "Digital Kindness Star",
        badgeTitleUr = "آن لائن اخلاق کا ستارہ",
        badgeEmoji = "💖",
        questionEn = "In a multiplayer game chat, a player is feeling sad because they lost a match.",
        questionUr = "آن لائن گیم میں ایک بچہ میچ ہارنے پر اداس ہو گیا ہے۔ آپ کیا کہیں گے؟",
        optionSafeEn = "Type 'Good game! You played well, keep trying!'",
        optionSafeUr = "'شاباش! آپ نے اچھا کھیلا، کوشش جاری رکھیں!' لکھیں",
        optionUnsafeEn = "Make fun of them or send angry emojis",
        optionUnsafeUr = "اس کا مذاق اڑائیں یا غصے والے ایموجی بھیجیں",
        explanationEn = "Being kind, respectful, and encouraging online makes games and communities fun and safe for everyone!",
        explanationUr = "آن لائن دوسروں کے ساتھ ہمیشہ خوش اخلاقی اور احترام سے پیش آنا چاہیے۔",
        tipEn = "Treat people online the same kind way you treat friends in real life.",
        tipUr = "انٹرنیٹ پر لوگوں کے ساتھ ویسا ہی اچھا سلوک کریں جیسا اسکول میں دوستوں کے ساتھ کرتے ہیں۔"
    )
)

// -------------------------------------------------------------
// Data Models for Logic Mini-Puzzles & Quizzes
// -------------------------------------------------------------
data class LogicQuiz(
    val id: String,
    val titleEn: String,
    val titleUr: String,
    val questionEn: String,
    val questionUr: String,
    val codeSnippet: String = "",
    val optionsEn: List<String>,
    val optionsUr: List<String>,
    val correctIndex: Int,
    val explanationEn: String,
    val explanationUr: String
)

val LOGIC_QUIZZES = listOf(
    LogicQuiz(
        id = "binary_counting",
        titleEn = "Binary Bits (0 and 1)",
        titleUr = "بائنری گنتی (۰ اور ۱)",
        questionEn = "Computers use binary numbers made of only 0 and 1. How does binary 101 translate into normal decimal numbers?",
        questionUr = "کمپیوٹر بائنری گنتی (۰ اور ۱) استعمال کرتا ہے۔ بائنری ۱۰۱ کا عام حساب میں کیا مطلب ہے؟",
        codeSnippet = "Bit values: 4 + 2 + 1\nBinary 1 0 1 = (4 * 1) + (2 * 0) + (1 * 1)",
        optionsEn = listOf("3", "5", "6", "101"),
        optionsUr = listOf("۳", "۵", "۶", "۱۰۱"),
        correctIndex = 1,
        explanationEn = "In binary: The first 1 is worth 4, 0 is worth 0, and the last 1 is worth 1. So 4 + 0 + 1 = 5!",
        explanationUr = "بائنری میں پہلا ۱ چار کے برابر ہے اور آخری ۱ ایک کے برابر ہے۔ ۴ + ۱ = ۵ بنتا ہے!"
    ),
    LogicQuiz(
        id = "loop_output",
        titleEn = "Loop Output Mystery",
        titleUr = "لوپ کا نتیجہ بوجھیں",
        questionEn = "Look at this computer loop. What will the final number of StarCoins be?",
        questionUr = "اس لوپ کو دیکھیں۔ آخر میں کل کتنے اسٹار کوائنز بنیں گے؟",
        codeSnippet = "starCoins = 0\nRepeat 3 times {\n    starCoins = starCoins + 5\n}",
        optionsEn = listOf("5", "10", "15", "20"),
        optionsUr = listOf("۵", "۱۰", "۱۵", "۲۰"),
        correctIndex = 2,
        explanationEn = "The loop adds 5 three times: 5 + 5 + 5 = 15 coins!",
        explanationUr = "لوپ ۳ بار ۵ جمع کرے گا: ۵ + ۵ + ۵ = ۱۵ کوائنز!"
    ),
    LogicQuiz(
        id = "if_else_decision",
        titleEn = "If / Else Decision Logic",
        titleUr = "اگر / ورنہ کا فیصلہ",
        questionEn = "If the temperature is 35 degrees, what will the robot wear?",
        questionUr = "اگر درجہ حرارت ۳۵ ڈگری ہو تو روبوٹ کیا پہنے گا؟",
        codeSnippet = "if (temperature > 30) {\n    wear = \"Sunglasses 🕶️\"\n} else {\n    wear = \"Warm Coat 🧥\"\n}",
        optionsEn = listOf("Sunglasses 🕶️", "Warm Coat 🧥", "Rain Boots 🥾", "Scarf 🧣"),
        optionsUr = listOf("دھوپ کا چشمہ 🕶️", "گرم کوٹ 🧥", "بارش کے بوٹ 🥾", "مفلر 🧣"),
        correctIndex = 0,
        explanationEn = "Since 35 is greater than 30, the 'If' block triggers and the robot picks Sunglasses!",
        explanationUr = "کیونکہ ۳۵ تیس سے بڑا ہے، اس لیے روبوٹ دھوپ کا چشمہ منتخب کرے گا!"
    ),
    LogicQuiz(
        id = "hardware_recall",
        titleEn = "Hardware Champion",
        titleUr = "ہارڈویئر چیلنج",
        questionEn = "Which computer component is known as the fast working desk that forgets everything when the power is turned off?",
        questionUr = "کمپیوٹر کا کون سا حصہ بجلی بند ہونے پر اپنا تمام عارضی ڈیٹا بھول جاتا ہے؟",
        optionsEn = listOf("SSD Storage", "RAM (Memory)", "Mouse", "Monitor"),
        optionsUr = listOf("ایس ایس ڈی اسٹوریج", "ریم (RAM)", "ماؤس", "مانیٹر"),
        correctIndex = 1,
        explanationEn = "RAM is volatile memory; it holds active tasks super-fast, but resets when power is off.",
        explanationUr = "ریم عارضی تیز رفتار میموری ہے جو بجلی جانے پر خالی ہو جاتی ہے۔"
    )
)

// -------------------------------------------------------------
// Main Composable: ComputerLogicStudio
// -------------------------------------------------------------
@Composable
fun ComputerLogicStudio(modifier: Modifier = Modifier) {
    val context = LocalContext.current
    val currentLang by LocaleManager.currentLanguage.collectAsState()
    val isUrdu = currentLang == LocaleManager.LANG_URDU
    val progressState by ComputerProgressManager.state.collectAsState()

    var selectedTab by remember { mutableIntStateOf(0) }
    var triggerConfetti by remember { mutableStateOf(false) }

    Box(modifier = modifier.fillMaxWidth().testTag("computer_logic_studio")) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .clip(RoundedCornerShape(24.dp))
                .background(MaterialTheme.colorScheme.surface)
                .border(1.dp, AstroBluePrimary.copy(alpha = 0.2f), RoundedCornerShape(24.dp))
                .padding(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            // Header with Module Banner & Total Stats
            ComputerStudioHeader(
                isUrdu = isUrdu,
                totalStars = progressState.totalStars,
                totalScore = progressState.totalScore
            )

            // Navigation Tabs
            ScrollableTabRow(
                selectedTabIndex = selectedTab,
                edgePadding = 0.dp,
                divider = {},
                containerColor = Color.Transparent,
                modifier = Modifier.fillMaxWidth().testTag("computer_studio_tabs")
            ) {
                Tab(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    text = {
                        Text(
                            text = if (isUrdu) "🖥️ ہارڈویئر و سافٹ ویئر" else "🖥️ Hardware & OS",
                            fontWeight = if (selectedTab == 0) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    modifier = Modifier.testTag("tab_computer_hardware")
                )
                Tab(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    text = {
                        Text(
                            text = if (isUrdu) "🧱 روور بلاک کوڈنگ" else "🧱 Rover Coding",
                            fontWeight = if (selectedTab == 1) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    modifier = Modifier.testTag("tab_computer_coding")
                )
                Tab(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    text = {
                        Text(
                            text = if (isUrdu) "🛡️ سائبر سیفٹی" else "🛡️ Cyber Safety",
                            fontWeight = if (selectedTab == 2) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    modifier = Modifier.testTag("tab_computer_safety")
                )
                Tab(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    text = {
                        Text(
                            text = if (isUrdu) "🎯 لاجک کوئز" else "🎯 Logic Puzzles",
                            fontWeight = if (selectedTab == 3) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    modifier = Modifier.testTag("tab_computer_quizzes")
                )
            }

            // Tab Content
            when (selectedTab) {
                0 -> HardwareSoftwareSection(isUrdu = isUrdu)
                1 -> RoverBlockCodingSection(
                    isUrdu = isUrdu,
                    onLevelSolved = {
                        triggerConfetti = true
                    }
                )
                2 -> CyberSafetySection(
                    isUrdu = isUrdu,
                    onBadgeEarned = {
                        triggerConfetti = true
                    }
                )
                3 -> LogicQuizSection(
                    isUrdu = isUrdu,
                    onQuizSolved = {
                        triggerConfetti = true
                    }
                )
            }
        }

        // Particle explosion for positive reinforcement
        ParticleExplosionEffect(
            trigger = triggerConfetti,
            onAnimationEnd = { triggerConfetti = false }
        )
    }
}

// -------------------------------------------------------------
// Component 1: Studio Header & Stats
// -------------------------------------------------------------
@Composable
private fun ComputerStudioHeader(
    isUrdu: Boolean,
    totalStars: Int,
    totalScore: Int
) {
    Card(
        colors = CardDefaults.cardColors(
            containerColor = AstroBluePrimary.copy(alpha = 0.08f)
        ),
        shape = RoundedCornerShape(20.dp),
        border = CardDefaults.outlinedCardBorder().copy(brush = Brush.horizontalGradient(listOf(AstroBluePrimary, Color(0xFF06B6D4)))),
        modifier = Modifier.fillMaxWidth().testTag("card_computer_header")
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Column(modifier = Modifier.weight(1f)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(40.dp)
                            .clip(CircleShape)
                            .background(AstroBluePrimary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(text = "💻", fontSize = 22.sp)
                    }
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = if (isUrdu) "کمپیوٹر، لاجک اور کوڈنگ" else "Computer & Logic Studio",
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = if (isUrdu) "عمر ۵ تا ۱۵ سال • سیکھیں اور بنائیں" else "Ages 5–15 • Hardware, Code & Cyber Safety",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            // Gamified Star and Score Badges
            Row(horizontalArrangement = Arrangement.spacedBy(8.dp)) {
                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = StarGoldPrimary.copy(alpha = 0.2f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = StarGoldPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "$totalStars",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Surface(
                    shape = RoundedCornerShape(12.dp),
                    color = AstroBluePrimary.copy(alpha = 0.15f)
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.EmojiEvents,
                            contentDescription = null,
                            tint = AstroBluePrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Text(
                            text = "${totalScore} XP",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = AstroBluePrimary
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Component 2: Hardware & Software Explorer
// -------------------------------------------------------------
@Composable
private fun HardwareSoftwareSection(isUrdu: Boolean) {
    val context = LocalContext.current
    val progressState by ComputerProgressManager.state.collectAsState()
    var selectedPartId by remember { mutableStateOf(COMPUTER_PARTS.first().id) }

    val activePart = remember(selectedPartId) {
        COMPUTER_PARTS.firstOrNull { it.id == selectedPartId } ?: COMPUTER_PARTS.first()
    }

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = if (isUrdu) "انٹرایکٹو کمپیوٹر ڈایاگرام: پرزوں کو چھو کر دریافت کریں" else "Interactive System Diagram: Tap any component to explore!",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Visual Component Selector Carousel
        LazyRow(
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.fillMaxWidth().testTag("list_computer_parts")
        ) {
            items(COMPUTER_PARTS) { part ->
                val isSelected = part.id == selectedPartId
                val isExplored = progressState.exploredComponents.contains(part.id)

                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) part.accentColor.copy(alpha = 0.18f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    border = if (isSelected) BorderStroke(2.dp, part.accentColor) else null,
                    modifier = Modifier
                        .clickable {
                            selectedPartId = part.id
                            ComputerProgressManager.recordComponentExplored(context, part.id)
                        }
                        .testTag("part_chip_${part.id}")
                ) {
                    Row(
                        modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        Text(text = part.emoji, fontSize = 20.sp)
                        Column {
                            Text(
                                text = if (isUrdu) part.nameUr else part.nameEn,
                                style = MaterialTheme.typography.labelMedium.copy(
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                ),
                                color = if (isSelected) part.accentColor else MaterialTheme.colorScheme.onSurface
                            )
                            if (isExplored) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = EmeraldSuccess,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(3.dp))
                                    Text(
                                        text = if (isUrdu) "سیکھ لیا" else "Explored",
                                        style = MaterialTheme.typography.labelSmall.copy(fontSize = 10.sp),
                                        color = EmeraldSuccess
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Active Component Detailed Inspection Card
        Card(
            colors = CardDefaults.cardColors(
                containerColor = activePart.accentColor.copy(alpha = 0.08f)
            ),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.5.dp, activePart.accentColor.copy(alpha = 0.35f)),
            modifier = Modifier.fillMaxWidth().testTag("card_active_part_detail")
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(50.dp)
                                .clip(RoundedCornerShape(14.dp))
                                .background(activePart.accentColor.copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = activePart.icon,
                                contentDescription = null,
                                tint = activePart.accentColor,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(12.dp))
                        Column {
                            Text(
                                text = if (isUrdu) activePart.nameUr else activePart.nameEn,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (isUrdu) activePart.roleUr else activePart.roleEn,
                                style = MaterialTheme.typography.labelMedium.copy(color = activePart.accentColor, fontWeight = FontWeight.SemiBold)
                            )
                        }
                    }

                    // Audio Pronunciation Button
                    IconButton(
                        onClick = {
                            val textToSpeak = if (isUrdu) {
                                "${activePart.nameUr}۔ ${activePart.roleUr}۔ ${activePart.detailUr}"
                            } else {
                                "${activePart.nameEn}. ${activePart.roleEn}. ${activePart.detailEn}"
                            }
                            TtsManager.speak(textToSpeak, isUrdu)
                        },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(activePart.accentColor.copy(alpha = 0.15f))
                            .testTag("btn_speak_active_part")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Pronounce",
                            tint = activePart.accentColor
                        )
                    }
                }

                // Detailed Technical Explanation
                Text(
                    text = if (isUrdu) activePart.detailUr else activePart.detailEn,
                    style = MaterialTheme.typography.bodyMedium.copy(fontSize = 14.sp),
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Kid Analogy Highlight Box
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(12.dp))
                        .background(MaterialTheme.colorScheme.surface)
                        .border(1.dp, activePart.accentColor.copy(alpha = 0.25f), RoundedCornerShape(12.dp))
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(text = "💡", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (isUrdu) activePart.kidAnalogyUr else activePart.kidAnalogyEn,
                            style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Component 3: Rover Visual Block Coding & Logic Runner
// -------------------------------------------------------------
@Composable
private fun RoverBlockCodingSection(
    isUrdu: Boolean,
    onLevelSolved: () -> Unit
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val progressState by ComputerProgressManager.state.collectAsState()

    var currentLevelIndex by remember { mutableIntStateOf(0) }
    val level = remember(currentLevelIndex) { ROVER_LEVELS[currentLevelIndex] }

    var roverX by remember(level) { mutableIntStateOf(level.startX) }
    var roverY by remember(level) { mutableIntStateOf(level.startY) }
    var roverDir by remember(level) { mutableStateOf(level.startDirection) }

    val programBlocks = remember(level) { mutableStateListOf<RoverAction>() }
    var isRunning by remember { mutableStateOf(false) }
    var activeBlockIndex by remember { mutableIntStateOf(-1) }
    var runFeedbackMessage by remember { mutableStateOf<String?>(null) }
    var isSuccess by remember { mutableStateOf(false) }

    val isLevelDone = progressState.completedCodingLevels.contains(level.levelNumber)

    Column(verticalArrangement = Arrangement.spacedBy(14.dp)) {
        // Level Navigation Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = if (isUrdu) level.titleUr else level.titleEn,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = if (isUrdu) level.conceptUr else level.conceptEn,
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Level Selector Chips
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                ROVER_LEVELS.forEachIndexed { idx, lvl ->
                    val isCur = idx == currentLevelIndex
                    val isDone = progressState.completedCodingLevels.contains(lvl.levelNumber)
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(
                                if (isCur) AstroBluePrimary else if (isDone) EmeraldSuccess.copy(alpha = 0.3f) else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .clickable {
                                currentLevelIndex = idx
                                isSuccess = false
                                runFeedbackMessage = null
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${lvl.levelNumber}",
                            color = if (isCur) Color.White else MaterialTheme.colorScheme.onSurface,
                            fontWeight = FontWeight.Bold,
                            fontSize = 12.sp
                        )
                    }
                }
            }
        }

        // 4x4 Grid Simulation Canvas
        Card(
            colors = CardDefaults.cardColors(containerColor = Color(0xFF0F172A)), // Midnight Navy
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier.fillMaxWidth().testTag("rover_grid_canvas")
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(14.dp),
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                // 4 Rows of 4 Cells
                for (row in 0 until 4) {
                    Row(
                        horizontalArrangement = Arrangement.spacedBy(6.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        for (col in 0 until 4) {
                            val isRoverHere = roverX == col && roverY == row
                            val isTargetHere = level.targetX == col && level.targetY == row
                            val isObstacleHere = level.obstacles.contains(Pair(col, row))
                            val isGemHere = level.gems.contains(Pair(col, row))

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .aspectRatio(1f)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(
                                        when {
                                            isRoverHere -> AstroBluePrimary.copy(alpha = 0.4f)
                                            isTargetHere -> StarGoldPrimary.copy(alpha = 0.25f)
                                            isObstacleHere -> Color(0xFFDC2626).copy(alpha = 0.35f)
                                            else -> Color(0xFF1E293B)
                                        }
                                    )
                                    .border(
                                        1.dp,
                                        if (isRoverHere) AstroBluePrimary else Color(0xFF334155),
                                        RoundedCornerShape(10.dp)
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                when {
                                    isRoverHere -> {
                                        val roverEmoji = when (roverDir) {
                                            Direction.EAST -> "🤖➡️"
                                            Direction.WEST -> "⬅️🤖"
                                            Direction.NORTH -> "🤖⬆️"
                                            Direction.SOUTH -> "🤖⬇️"
                                        }
                                        Text(text = roverEmoji, fontSize = 18.sp)
                                    }
                                    isTargetHere -> Text(text = "⭐", fontSize = 22.sp)
                                    isObstacleHere -> Text(text = "🪨", fontSize = 20.sp)
                                    isGemHere -> Text(text = "💎", fontSize = 18.sp)
                                }
                            }
                        }
                    }
                }
            }
        }

        // Mission Goal Banner
        Surface(
            shape = RoundedCornerShape(12.dp),
            color = AstroBluePrimary.copy(alpha = 0.08f),
            modifier = Modifier.fillMaxWidth()
        ) {
            Row(
                modifier = Modifier.padding(horizontal = 12.dp, vertical = 8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(text = "🎯", fontSize = 18.sp)
                Spacer(modifier = Modifier.width(8.dp))
                Text(
                    text = if (isUrdu) level.targetGoalUr else level.targetGoalEn,
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    color = MaterialTheme.colorScheme.onSurface
                )
            }
        }

        // Program Sequence Tray (Workspace)
        Text(
            text = if (isUrdu) "آپ کا کوڈ (پروگرام)" else "Your Program Workspace:",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier.fillMaxWidth().testTag("rover_program_workspace")
        ) {
            if (programBlocks.isEmpty()) {
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(16.dp),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isUrdu) "نیچے دیے گئے بلاکس پر کلک کر کے کوڈ ترتیب دیں!" else "Tap action blocks below to add to your program!",
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                }
            } else {
                LazyRow(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    items(programBlocks.size) { idx ->
                        val block = programBlocks[idx]
                        val isExecuting = activeBlockIndex == idx

                        Surface(
                            shape = RoundedCornerShape(10.dp),
                            color = if (isExecuting) StarGoldPrimary else block.color,
                            modifier = Modifier
                                .clickable(enabled = !isRunning) {
                                    programBlocks.removeAt(idx)
                                }
                                .testTag("block_item_$idx")
                        ) {
                            Row(
                                modifier = Modifier.padding(horizontal = 10.dp, vertical = 6.dp),
                                verticalAlignment = Alignment.CenterVertically,
                                horizontalArrangement = Arrangement.spacedBy(4.dp)
                            ) {
                                Text(text = "${idx + 1}. ${block.emoji}", fontSize = 14.sp)
                                Text(
                                    text = if (isUrdu) block.titleUr else block.titleEn,
                                    style = MaterialTheme.typography.labelSmall.copy(
                                        color = if (isExecuting) Color.Black else Color.White,
                                        fontWeight = FontWeight.Bold
                                    )
                                )
                                if (!isRunning) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Remove",
                                        tint = Color.White.copy(alpha = 0.8f),
                                        modifier = Modifier.size(12.dp)
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        // Action Block Palettes (Blocks to Tap)
        Text(
            text = if (isUrdu) "کوڈنگ بلاکس (ٹول باکس):" else "Available Coding Blocks:",
            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            level.availableActions.forEach { action ->
                Button(
                    onClick = {
                        if (!isRunning && programBlocks.size < 8) {
                            programBlocks.add(action)
                        }
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = action.color),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier.testTag("btn_block_${action.name}")
                ) {
                    Text(text = action.emoji, fontSize = 16.sp)
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(text = if (isUrdu) action.titleUr else action.titleEn, fontSize = 12.sp)
                }
            }
        }

        // Runner Controls: Run, Reset, Clear
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            Button(
                onClick = {
                    if (isRunning || programBlocks.isEmpty()) return@Button
                    isRunning = true
                    runFeedbackMessage = null
                    isSuccess = false

                    coroutineScope.launch {
                        // Reset rover to initial state
                        roverX = level.startX
                        roverY = level.startY
                        roverDir = level.startDirection
                        delay(250)

                        var collided = false

                        for (i in programBlocks.indices) {
                            activeBlockIndex = i
                            val action = programBlocks[i]

                            when (action) {
                                RoverAction.MOVE_FORWARD -> {
                                    val (nextX, nextY) = getNextPosition(roverX, roverY, roverDir)
                                    if (level.obstacles.contains(Pair(nextX, nextY))) {
                                        collided = true
                                        runFeedbackMessage = if (isUrdu) "اوہ! روور پتھر سے ٹکرا گیا۔ رکاوٹ کودنے کے لیے 'Jump' استعمال کریں!" else "Ouch! The rover hit an asteroid! Try jumping over it."
                                        break
                                    } else {
                                        roverX = nextX.coerceIn(0, 3)
                                        roverY = nextY.coerceIn(0, 3)
                                    }
                                }
                                RoverAction.TURN_LEFT -> {
                                    roverDir = turnLeft(roverDir)
                                }
                                RoverAction.TURN_RIGHT -> {
                                    roverDir = turnRight(roverDir)
                                }
                                RoverAction.JUMP_OBSTACLE -> {
                                    // Jumps 2 steps ahead over obstacle
                                    val (jumpX, jumpY) = getNextPosition(roverX, roverY, roverDir, steps = 2)
                                    roverX = jumpX.coerceIn(0, 3)
                                    roverY = jumpY.coerceIn(0, 3)
                                }
                                RoverAction.REPEAT_TWO -> {
                                    // Move forward twice
                                    val (n1X, n1Y) = getNextPosition(roverX, roverY, roverDir, steps = 2)
                                    roverX = n1X.coerceIn(0, 3)
                                    roverY = n1Y.coerceIn(0, 3)
                                }
                            }
                            delay(450)
                        }

                        activeBlockIndex = -1
                        isRunning = false

                        if (!collided) {
                            if (roverX == level.targetX && roverY == level.targetY) {
                                isSuccess = true
                                runFeedbackMessage = if (isUrdu) "🎉 زبردست! آپ کا الگورتھم کامیاب رہا اور روور نے ستارہ حاصل کر لیا!" else "🎉 Excellent Code! Your algorithm successfully guided the Rover to the Star!"
                                ComputerProgressManager.recordCodingLevelCompletion(context, level.levelNumber)
                                onLevelSolved()
                            } else {
                                runFeedbackMessage = if (isUrdu) "روور ابھی ستارے تک نہیں پہنچ سکا۔ کوڈ میں مزید قدم شامل کریں!" else "Almost there! The rover stopped before the Star. Add more steps to your program."
                            }
                        }
                    }
                },
                enabled = !isRunning && programBlocks.isNotEmpty(),
                colors = ButtonDefaults.buttonColors(containerColor = EmeraldSuccess),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.weight(1f).testTag("btn_run_program")
            ) {
                Icon(imageVector = Icons.Default.PlayArrow, contentDescription = null)
                Spacer(modifier = Modifier.width(6.dp))
                Text(text = if (isUrdu) "چلائیں (Run)" else "Run Code", fontWeight = FontWeight.Bold)
            }

            OutlinedButton(
                onClick = {
                    roverX = level.startX
                    roverY = level.startY
                    roverDir = level.startDirection
                    runFeedbackMessage = null
                    isSuccess = false
                },
                enabled = !isRunning,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("btn_reset_rover")
            ) {
                Icon(imageVector = Icons.Default.Refresh, contentDescription = null)
            }

            OutlinedButton(
                onClick = {
                    programBlocks.clear()
                    roverX = level.startX
                    roverY = level.startY
                    roverDir = level.startDirection
                    runFeedbackMessage = null
                    isSuccess = false
                },
                enabled = !isRunning,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.testTag("btn_clear_blocks")
            ) {
                Icon(imageVector = Icons.Default.DeleteOutline, contentDescription = null)
            }
        }

        // Live Execution Result / Feedback Toast
        runFeedbackMessage?.let { msg ->
            Card(
                colors = CardDefaults.cardColors(
                    containerColor = if (isSuccess) EmeraldSuccess.copy(alpha = 0.15f) else Color(0xFFF59E0B).copy(alpha = 0.15f)
                ),
                shape = RoundedCornerShape(12.dp),
                border = BorderStroke(1.dp, if (isSuccess) EmeraldSuccess else Color(0xFFF59E0B)),
                modifier = Modifier.fillMaxWidth().testTag("card_rover_feedback")
            ) {
                Row(
                    modifier = Modifier.padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(text = if (isSuccess) "🏆" else "💡", fontSize = 22.sp)
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = msg,
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                }
            }
        }
    }
}

private fun getNextPosition(x: Int, y: Int, dir: Direction, steps: Int = 1): Pair<Int, Int> {
    return when (dir) {
        Direction.NORTH -> Pair(x, y - steps)
        Direction.SOUTH -> Pair(x, y + steps)
        Direction.EAST -> Pair(x + steps, y)
        Direction.WEST -> Pair(x - steps, y)
    }
}

private fun turnLeft(dir: Direction): Direction {
    return when (dir) {
        Direction.NORTH -> Direction.WEST
        Direction.WEST -> Direction.SOUTH
        Direction.SOUTH -> Direction.EAST
        Direction.EAST -> Direction.NORTH
    }
}

private fun turnRight(dir: Direction): Direction {
    return when (dir) {
        Direction.NORTH -> Direction.EAST
        Direction.EAST -> Direction.SOUTH
        Direction.SOUTH -> Direction.WEST
        Direction.WEST -> Direction.NORTH
    }
}

// -------------------------------------------------------------
// Component 4: Cyber Safety & Internet Etiquette
// -------------------------------------------------------------
@Composable
private fun CyberSafetySection(
    isUrdu: Boolean,
    onBadgeEarned: () -> Unit
) {
    val context = LocalContext.current
    val progressState by ComputerProgressManager.state.collectAsState()
    var selectedScenarioIndex by remember { mutableIntStateOf(0) }
    val scenario = remember(selectedScenarioIndex) { CYBER_SCENARIOS[selectedScenarioIndex] }

    var selectedChoice by remember(scenario) { mutableStateOf<Boolean?>(null) } // true = Safe, false = Unsafe

    val isBadgeUnlocked = progressState.earnedSafetyBadges.contains(scenario.badgeId)

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        Text(
            text = if (isUrdu) "انٹرنیٹ حفاظت، پاس ورڈ اور آداب کے اسباق" else "Digital Citizenship: Cyber Safety, Privacy & Internet Etiquette",
            style = MaterialTheme.typography.labelLarge.copy(fontWeight = FontWeight.Bold),
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        // Scenario Selector Badges
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .horizontalScroll(rememberScrollState()),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            CYBER_SCENARIOS.forEachIndexed { index, sc ->
                val isSelected = index == selectedScenarioIndex
                val hasBadge = progressState.earnedSafetyBadges.contains(sc.badgeId)

                FilterChip(
                    selected = isSelected,
                    onClick = { selectedScenarioIndex = index },
                    label = {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Text(text = sc.badgeEmoji)
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = if (isUrdu) sc.badgeTitleUr else sc.badgeTitleEn,
                                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                            )
                            if (hasBadge) {
                                Spacer(modifier = Modifier.width(4.dp))
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = null,
                                    tint = EmeraldSuccess,
                                    modifier = Modifier.size(14.dp)
                                )
                            }
                        }
                    },
                    modifier = Modifier.testTag("chip_safety_scenario_$index")
                )
            }
        }

        // Active Scenario Interactive Decision Card
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.5.dp, AstroBluePrimary.copy(alpha = 0.3f)),
            modifier = Modifier.fillMaxWidth().testTag("card_safety_scenario")
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(44.dp)
                                .clip(CircleShape)
                                .background(AstroBluePrimary.copy(alpha = 0.15f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(text = scenario.badgeEmoji, fontSize = 22.sp)
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isUrdu) scenario.badgeTitleUr else scenario.badgeTitleEn,
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = MaterialTheme.colorScheme.onSurface
                            )
                            Text(
                                text = if (isUrdu) "حفاظتی مشق" else "Interactive Safety Challenge",
                                style = MaterialTheme.typography.labelSmall,
                                color = AstroBluePrimary
                            )
                        }
                    }

                    // TTS button
                    IconButton(
                        onClick = {
                            val speech = if (isUrdu) {
                                "${scenario.badgeTitleUr}۔ ${scenario.questionUr}"
                            } else {
                                "${scenario.badgeTitleEn}. ${scenario.questionEn}"
                            }
                            TtsManager.speak(speech, isUrdu)
                        },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(AstroBluePrimary.copy(alpha = 0.1f))
                            .testTag("btn_speak_safety")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Pronounce",
                            tint = AstroBluePrimary
                        )
                    }
                }

                // Scenario Question Text
                Text(
                    text = if (isUrdu) scenario.questionUr else scenario.questionEn,
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium, fontSize = 15.sp),
                    color = MaterialTheme.colorScheme.onSurface
                )

                // Safe Choice Option Button
                val isSafeChosen = selectedChoice == true
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSafeChosen) EmeraldSuccess.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    ),
                    shape = RoundedCornerShape(14.dp),
                    border = if (isSafeChosen) BorderStroke(1.5.dp, EmeraldSuccess) else null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedChoice = true
                            ComputerProgressManager.recordSafetyBadgeEarned(context, scenario.badgeId)
                            onBadgeEarned()
                        }
                        .testTag("btn_choice_safe")
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "🛡️", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (isUrdu) scenario.optionSafeUr else scenario.optionSafeEn,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (isSafeChosen) FontWeight.Bold else FontWeight.Normal
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                        if (isSafeChosen) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = EmeraldSuccess
                            )
                        }
                    }
                }

                // Unsafe Choice Option Button
                val isUnsafeChosen = selectedChoice == false
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = if (isUnsafeChosen) Color(0xFFEF4444).copy(alpha = 0.12f) else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    ),
                    shape = RoundedCornerShape(14.dp),
                    border = if (isUnsafeChosen) BorderStroke(1.5.dp, Color(0xFFEF4444)) else null,
                    modifier = Modifier
                        .fillMaxWidth()
                        .clickable {
                            selectedChoice = false
                        }
                        .testTag("btn_choice_unsafe")
                ) {
                    Row(
                        modifier = Modifier.padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(text = "⚠️", fontSize = 20.sp)
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (isUrdu) scenario.optionUnsafeUr else scenario.optionUnsafeEn,
                            style = MaterialTheme.typography.bodyMedium.copy(
                                fontWeight = if (isUnsafeChosen) FontWeight.Bold else FontWeight.Normal
                            ),
                            color = MaterialTheme.colorScheme.onSurface,
                            modifier = Modifier.weight(1f)
                        )
                        if (isUnsafeChosen) {
                            Icon(
                                imageVector = Icons.Default.ErrorOutline,
                                contentDescription = null,
                                tint = Color(0xFFEF4444)
                            )
                        }
                    }
                }

                // Immediate Explanation Box
                AnimatedVisibility(visible = selectedChoice != null) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (selectedChoice == true) EmeraldSuccess.copy(alpha = 0.1f) else Color(0xFFF59E0B).copy(alpha = 0.12f)
                            )
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        Text(
                            text = if (selectedChoice == true) {
                                if (isUrdu) "🌟 بہترین فیصلہ!" else "🌟 Smart Digital Choice!"
                            } else {
                                if (isUrdu) "⚠️ ہوشیار رہیں! یہ غیر محفوظ ہے" else "⚠️ Caution! This action is unsafe."
                            },
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (selectedChoice == true) EmeraldSuccess else Color(0xFFF59E0B)
                            )
                        )
                        Text(
                            text = if (isUrdu) scenario.explanationUr else scenario.explanationEn,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                        Text(
                            text = "💡 " + (if (isUrdu) scenario.tipUr else scenario.tipEn),
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }
    }
}

// -------------------------------------------------------------
// Component 5: Interactive Coding Mini-Puzzles & Quizzes
// -------------------------------------------------------------
@Composable
private fun LogicQuizSection(
    isUrdu: Boolean,
    onQuizSolved: () -> Unit
) {
    val context = LocalContext.current
    val progressState by ComputerProgressManager.state.collectAsState()
    var quizIndex by remember { mutableIntStateOf(0) }
    val quiz = remember(quizIndex) { LOGIC_QUIZZES[quizIndex] }

    var selectedOptionIndex by remember(quiz) { mutableStateOf<Int?>(null) }
    val isSolved = progressState.solvedQuizIds.contains(quiz.id)

    Column(verticalArrangement = Arrangement.spacedBy(16.dp)) {
        // Quiz Carousel / Stepper Header
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Column {
                Text(
                    text = if (isUrdu) quiz.titleUr else quiz.titleEn,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = if (isUrdu) "سوال ${quizIndex + 1} از ${LOGIC_QUIZZES.size}" else "Puzzle ${quizIndex + 1} of ${LOGIC_QUIZZES.size}",
                    style = MaterialTheme.typography.labelSmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }

            // Step Indicator Dots
            Row(horizontalArrangement = Arrangement.spacedBy(4.dp)) {
                LOGIC_QUIZZES.forEachIndexed { i, q ->
                    val isCur = i == quizIndex
                    val isDone = progressState.solvedQuizIds.contains(q.id)
                    Box(
                        modifier = Modifier
                            .size(28.dp)
                            .clip(CircleShape)
                            .background(
                                if (isCur) AstroBluePrimary else if (isDone) EmeraldSuccess.copy(alpha = 0.3f) else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .clickable {
                                quizIndex = i
                                selectedOptionIndex = null
                            },
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${i + 1}",
                            color = if (isCur) Color.White else MaterialTheme.colorScheme.onSurface,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }

        // Active Quiz Card
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(20.dp),
            border = BorderStroke(1.5.dp, AstroBluePrimary.copy(alpha = 0.25f)),
            modifier = Modifier.fillMaxWidth().testTag("card_logic_quiz")
        ) {
            Column(
                modifier = Modifier.padding(18.dp),
                verticalArrangement = Arrangement.spacedBy(14.dp)
            ) {
                // Audio question reader
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isUrdu) quiz.questionUr else quiz.questionEn,
                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold, fontSize = 15.sp),
                        color = MaterialTheme.colorScheme.onSurface,
                        modifier = Modifier.weight(1f)
                    )
                    IconButton(
                        onClick = {
                            val speech = if (isUrdu) quiz.questionUr else quiz.questionEn
                            TtsManager.speak(speech, isUrdu)
                        },
                        modifier = Modifier
                            .clip(CircleShape)
                            .background(AstroBluePrimary.copy(alpha = 0.1f))
                            .testTag("btn_speak_quiz_question")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.VolumeUp,
                            contentDescription = "Read aloud",
                            tint = AstroBluePrimary
                        )
                    }
                }

                // Monospace Code Snippet Display if available
                if (quiz.codeSnippet.isNotBlank()) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(Color(0xFF0F172A))
                            .border(1.dp, Color(0xFF334155), RoundedCornerShape(12.dp))
                            .padding(12.dp)
                    ) {
                        Text(
                            text = quiz.codeSnippet,
                            color = Color(0xFF38BDF8),
                            fontFamily = FontFamily.Monospace,
                            fontSize = 13.sp,
                            lineHeight = 18.sp
                        )
                    }
                }

                // Multiple Choice Option Buttons
                val options = if (isUrdu) quiz.optionsUr else quiz.optionsEn
                options.forEachIndexed { idx, optText ->
                    val isSelected = selectedOptionIndex == idx
                    val isCorrect = idx == quiz.correctIndex
                    val showAnswer = selectedOptionIndex != null

                    val backgroundColor = when {
                        showAnswer && isCorrect -> EmeraldSuccess.copy(alpha = 0.18f)
                        showAnswer && isSelected && !isCorrect -> Color(0xFFEF4444).copy(alpha = 0.14f)
                        isSelected -> AstroBluePrimary.copy(alpha = 0.12f)
                        else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                    }

                    val borderColor = when {
                        showAnswer && isCorrect -> EmeraldSuccess
                        showAnswer && isSelected && !isCorrect -> Color(0xFFEF4444)
                        isSelected -> AstroBluePrimary
                        else -> Color.Transparent
                    }

                    Card(
                        colors = CardDefaults.cardColors(containerColor = backgroundColor),
                        shape = RoundedCornerShape(12.dp),
                        border = BorderStroke(1.dp, borderColor),
                        modifier = Modifier
                            .fillMaxWidth()
                            .clickable(enabled = selectedOptionIndex == null) {
                                selectedOptionIndex = idx
                                if (idx == quiz.correctIndex) {
                                    ComputerProgressManager.recordQuizCompletion(context, quiz.id)
                                    onQuizSolved()
                                }
                            }
                            .testTag("quiz_option_$idx")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Text(
                                text = "${('A' + idx)}.",
                                fontWeight = FontWeight.Bold,
                                color = AstroBluePrimary
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Text(
                                text = optText,
                                style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium),
                                color = MaterialTheme.colorScheme.onSurface,
                                modifier = Modifier.weight(1f)
                            )
                            if (showAnswer && isCorrect) {
                                Icon(
                                    imageVector = Icons.Default.CheckCircle,
                                    contentDescription = "Correct",
                                    tint = EmeraldSuccess
                                )
                            } else if (showAnswer && isSelected && !isCorrect) {
                                Icon(
                                    imageVector = Icons.Default.ErrorOutline,
                                    contentDescription = "Incorrect",
                                    tint = Color(0xFFEF4444)
                                )
                            }
                        }
                    }
                }

                // Explanation Banner
                AnimatedVisibility(visible = selectedOptionIndex != null) {
                    val isUserCorrect = selectedOptionIndex == quiz.correctIndex
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isUserCorrect) EmeraldSuccess.copy(alpha = 0.1f) else Color(0xFFF59E0B).copy(alpha = 0.1f)
                            )
                            .padding(12.dp),
                        verticalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Text(
                            text = if (isUserCorrect) {
                                if (isUrdu) "🎉 شاندار! بالکل درست جواب" else "🎉 Spot on! Correct Answer"
                            } else {
                                if (isUrdu) "کوئی بات نہیں، دوبارہ سمجھیں:" else "Nice try! Here is how it works:"
                            },
                            style = MaterialTheme.typography.labelMedium.copy(
                                fontWeight = FontWeight.Bold,
                                color = if (isUserCorrect) EmeraldSuccess else Color(0xFFF59E0B)
                            )
                        )
                        Text(
                            text = if (isUrdu) quiz.explanationUr else quiz.explanationEn,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
            }
        }
    }
}
