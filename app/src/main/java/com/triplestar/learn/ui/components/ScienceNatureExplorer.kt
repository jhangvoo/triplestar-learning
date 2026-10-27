package com.triplestar.learn.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.VolumeUp
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.triplestar.learn.ui.theme.EmeraldSuccess
import com.triplestar.learn.ui.theme.StarGoldPrimary
import com.triplestar.learn.util.LocaleManager
import com.triplestar.learn.util.TtsManager

data class ScienceFact(
    val titleEn: String,
    val titleUr: String,
    val detailEn: String,
    val detailUr: String,
    val emoji: String
)

data class ScienceQuizQuestion(
    val questionEn: String,
    val questionUr: String,
    val optionsEn: List<String>,
    val optionsUr: List<String>,
    val correctIndex: Int,
    val explanationEn: String,
    val explanationUr: String
)

data class ScienceTopic(
    val id: String,
    val nameEn: String,
    val nameUr: String,
    val emoji: String,
    val colorHex: Long,
    val overviewEn: String,
    val overviewUr: String,
    val facts: List<ScienceFact>,
    val quiz: ScienceQuizQuestion
)

val SCIENCE_TOPICS = listOf(
    ScienceTopic(
        id = "animals",
        nameEn = "Animals & Habitats",
        nameUr = "جانور اور مسکن",
        emoji = "🐾",
        colorHex = 0xFF10B981, // Emerald Green
        overviewEn = "Animals live across our world in natural homes called habitats, including lush jungles, deep oceans, hot deserts, and frosty polar ice.",
        overviewUr = "جانور دنیا بھر میں اپنے قدرتی مسکنوں میں رہتے ہیں جیسے گھنے جنگلات، گہرے سمندر، گرم صحرا اور برفیلا قطبی علاقہ۔",
        facts = listOf(
            ScienceFact(
                titleEn = "Dolphins Sleep with One Eye Open",
                titleUr = "ڈولفن ایک آنکھ کھول کر سوتی ہے",
                detailEn = "Dolphins keep half of their brain awake while resting so they can surface to breathe air and watch for predators!",
                detailUr = "ڈولفن آرام کرتے ہوئے اپنے دماغ کا آدھا حصہ بیدار رکھتی ہے تاکہ سانس لینے کے لیے اوپر آ سکے اور خطرات سے ہوشیار رہے!",
                emoji = "🐬"
            ),
            ScienceFact(
                titleEn = "Camels Are Desert Kings",
                titleUr = "اونٹ صحرا کا جہاز ہے",
                detailEn = "Camels have wide cushioned feet that never sink in the sand and can go weeks without drinking water!",
                detailUr = "اونٹ کے چوڑے پاؤں ریت میں نہیں دھنستے اور وہ پانی پیئے بغیر کئی ہفتے صحرا میں سفر کر سکتا ہے!",
                emoji = "🐪"
            ),
            ScienceFact(
                titleEn = "Polar Bear's Secret Heat Coat",
                titleUr = "قطبی ریچھ کی خاص جلد",
                detailEn = "Underneath their thick white fur, polar bears have black skin that absorbs warmth from the arctic sun!",
                detailUr = "اپنی سفید کھال کے نیچے قطبی ریچھ کی جلد سیاہ ہوتی ہے جو سورج کی گرمی جذب کر کے اسے گرم رکھتی ہے!",
                emoji = "🐻‍❄️"
            ),
            ScienceFact(
                titleEn = "Lions and Their Pride",
                titleUr = "شیر اور اس کا خاندان",
                detailEn = "Lions live in big supportive family groups called prides, and their roar can be heard 5 miles away!",
                detailUr = "شیر اپنے خاندان کے ساتھ رہتے ہیں اور ان کی دھاڑ پانچ میل دور تک سنی جا سکتی ہے!",
                emoji = "🦁"
            )
        ),
        quiz = ScienceQuizQuestion(
            questionEn = "Which habitat is specially suited for camels with dry sand and hot sun?",
            questionUr = "خشک ریت اور گرم موسم میں اونٹ کا قدرتی مسکن کون سا ہے؟",
            optionsEn = listOf("Deep Ocean", "Hot Desert", "Frozen Arctic", "Rainforest"),
            optionsUr = listOf("گہرا سمندر", "گرم صحرا", "برفیلا قطب", "بارانی جنگل"),
            correctIndex = 1,
            explanationEn = "Camels are perfectly adapted to live in hot deserts with sandy dunes!",
            explanationUr = "اونٹ صحرا کے گرم اور ریتلے ماحول کے لیے بالکل موزوں بنایا گیا ہے!"
        )
    ),
    ScienceTopic(
        id = "plants",
        nameEn = "Plants & Trees",
        nameUr = "پودے اور درخت",
        emoji = "🌱",
        colorHex = 0xFF059669, // Forest Green
        overviewEn = "Plants are nature's green oxygen factories! They use sunlight, water, and soil minerals to grow and feed our planet.",
        overviewUr = "پودے قدرتی آکسیجن بنانے والے جاندار ہیں! وہ سورج کی روشنی، پانی اور مٹی کی مدد سے خوراک بناتے اور بڑھتے ہیں۔",
        facts = listOf(
            ScienceFact(
                titleEn = "Sunflowers Dance with Sunlight",
                titleUr = "سورج مکھی سورج کے ساتھ مڑتی ہے",
                detailEn = "Young sunflowers follow the bright sun across the blue sky from east to west every day!",
                detailUr = "نوجوان سورج مکھی کا پھول روزانہ مشرق سے مغرب تک سورج کے ساتھ اپنا رخ موڑتا ہے!",
                emoji = "🌻"
            ),
            ScienceFact(
                titleEn = "Plant Roots are Water Straws",
                titleUr = "جڑیں زمین سے پانی چوستی ہیں",
                detailEn = "Roots anchor the plant firmly into the soil and drink up water and vital minerals like living straws.",
                detailUr = "جڑیں پودے کو زمین میں مضبوطی سے تھامتی ہیں اور پانی و نمکیات کو چوس کر تنوں تک پہنچاتی ہیں۔",
                emoji = "🥕"
            ),
            ScienceFact(
                titleEn = "Leaves Make Clean Oxygen",
                titleUr = "پتے تازہ آکسیجن بناتے ہیں",
                detailEn = "Using green chlorophyll, leaves capture sunlight in a process called photosynthesis to make sweet food and release fresh oxygen!",
                detailUr = "پتے دھوپ اور سبز مادے کی مدد سے ضیائی تالیف (Photosynthesis) کے ذریعے کھانا بناتے اور آکسیجن چھوڑتے ہیں!",
                emoji = "🍃"
            ),
            ScienceFact(
                titleEn = "Tree Rings Reveal Tree Age",
                titleUr = "درخت کے دائرے اس کی عمر بتاتے ہیں",
                detailEn = "Every year a tree grows, it creates a new ring inside its trunk. Counting the rings tells how many years old the tree is!",
                detailUr = "ہر سال درخت کے تنے کے اندر ایک نیا گول دائرہ بنتا ہے۔ ان دائروں کو گن کر درخت کی عمر معلوم کی جا سکتی ہے!",
                emoji = "🌲"
            )
        ),
        quiz = ScienceQuizQuestion(
            questionEn = "What wonderful gas do green leaves produce that helps humans and animals breathe?",
            questionUr = "سبز پتے کون سی گیس بناتے ہیں جو انسانوں اور جانوروں کو سانس لینے میں مدد دیتی ہے؟",
            optionsEn = listOf("Helium", "Oxygen", "Smoke", "Dust"),
            optionsUr = listOf("ہیلیم", "آکسیجن", "دھواں", "گرد و غبار"),
            correctIndex = 1,
            explanationEn = "Leaves release pure Oxygen gas during photosynthesis for all living beings to breathe!",
            explanationUr = "پتے ضیائی تالیف کے دوران صاف آکسیجن خارج کرتے ہیں جو تمام جانداروں کے لیے ضروری ہے!"
        )
    ),
    ScienceTopic(
        id = "weather",
        nameEn = "Weather & Seasons",
        nameUr = "موسم اور موسمی چکر",
        emoji = "🌦️",
        colorHex = 0xFF0284C7, // Vibrant Sky Blue
        overviewEn = "The water cycle moves water around our planet in an endless loop: evaporating from oceans, forming clouds, and raining back down.",
        overviewUr = "پانی کا قدرتی چکر زمین پر مسلسل چلتا رہتا ہے: سمندروں سے بخارات بننا، بادل بننا اور پھر بارش بن کر برسنا۔",
        facts = listOf(
            ScienceFact(
                titleEn = "The Sun Powers Evaporation",
                titleUr = "سورج پانی کو بخارات بناتا ہے",
                detailEn = "Warm sunshine heats water in rivers, lakes, and oceans, turning liquid into invisible rising vapor!",
                detailUr = "سورج کی گرمی پانی کو گرم کر کے ہلکے بخارات میں بدل دیتی ہے جو اوپر آسمان کی طرف اٹھتے ہیں!",
                emoji = "☀️"
            ),
            ScienceFact(
                titleEn = "Clouds are Floating Water Droplets",
                titleUr = "بادل پانی کے ننھے قطروں کا مجموعہ ہیں",
                detailEn = "High up in the cool sky, rising water vapor cools down and condenses into billowy clouds.",
                detailUr = "بلندی پر ٹھنڈی ہوا میں آبی بخارات اکٹھے ہو کر بادل کی شکل اختیار کر لیتے ہیں۔",
                emoji = "☁️"
            ),
            ScienceFact(
                titleEn = "Rain Nourishes the Earth",
                titleUr = "بارش زمین کو شاداب کرتی ہے",
                detailEn = "When cloud droplets become heavy, gravity pulls them down as refreshing raindrops to fill streams and water gardens.",
                detailUr = "جب بادل میں پانی کے قطرے بھاری ہو جاتے ہیں تو وہ بارش بن کر زمین پر گرتے ہیں اور ندی نالوں کو بھرتے ہیں۔",
                emoji = "🌧️"
            ),
            ScienceFact(
                titleEn = "Rainbows Have Seven Colors",
                titleUr = "قوسِ قزح کے سات رنگ ہوتے ہیں",
                detailEn = "When bright sunlight shines through falling rain, raindrops bend the light into red, orange, yellow, green, blue, indigo, and violet!",
                detailUr = "جب بارش کے قطروں میں سے دھوپ گزرتی ہے تو روشنی سات خوبصورت رنگوں میں بٹ جاتی ہے!",
                emoji = "🌈"
            )
        ),
        quiz = ScienceQuizQuestion(
            questionEn = "What causes ocean and puddle water to evaporate upward into the atmosphere?",
            questionUr = "زمین اور سمندر کا پانی بخارات بن کر فضا میں کس وجہ سے اڑتا ہے؟",
            optionsEn = listOf("Cold Moonlight", "Heat from the Sun", "Falling Snow", "Night Darkness"),
            optionsUr = listOf("چاند کی ٹھنڈک", "سورج کی گرمی", "گرتی ہوئی برف", "رات کا اندھیرا"),
            correctIndex = 1,
            explanationEn = "Heat energy from the warm Sun turns liquid water into rising water vapor!",
            explanationUr = "سورج کی گرمی پانی کو مائع سے بخارات میں تبدیل کرتی ہے!"
        )
    ),
    ScienceTopic(
        id = "space",
        nameEn = "Earth & Space",
        nameUr = "زمین اور کائنات",
        emoji = "🌍",
        colorHex = 0xFF8B5CF6, // Deep Cosmic Violet
        overviewEn = "Earth is our home planet orbiting the Sun. It rotates every 24 hours to give us daylight and starry nights!",
        overviewUr = "زمین ہمارا گھر ہے جو سورج کے گرد گھومتی ہے۔ یہ اپنے محور پر ۲۴ گھنٹے میں ایک چکر لگاتی ہے جس سے دن اور رات بنتے ہیں۔",
        facts = listOf(
            ScienceFact(
                titleEn = "Day and Night Happen as Earth Spins",
                titleUr = "زمین کے گھومنے سے دن اور رات بنتے ہیں",
                detailEn = "As Earth rotates, the half facing the Sun experiences daylight, while the other half has cozy bedtime!",
                detailUr = "جب زمین اپنے محور پر گھومتی ہے تو سورج کے سامنے والا حصہ دن اور دوسرا حصہ رات کہلاتا ہے!",
                emoji = "🌎"
            ),
            ScienceFact(
                titleEn = "The Moon is Earth's Natural Satellite",
                titleUr = "چاند زمین کا قدرتی سیارچہ ہے",
                detailEn = "The Moon doesn't produce its own light; it reflects bright sunshine like a cosmic night-light!",
                detailUr = "چاند کی اپنی روشنی نہیں ہوتی بلکہ وہ سورج کی روشنی کو آئینے کی طرح منعکس کرتا ہے!",
                emoji = "🌙"
            ),
            ScienceFact(
                titleEn = "Shooting Stars are Tiny Meteors",
                titleUr = "شہابِ ثاقب چمکتے ہوئے پتھر ہیں",
                detailEn = "Shooting stars aren't real stars—they are small space pebbles glowing brightly as they enter Earth's air!",
                detailUr = "ٹوٹتے ہوئے تارے دراصل چھوٹے آسمانی پتھر ہوتے ہیں جو فضا میں داخل ہو کر چمک اٹھتے ہیں!",
                emoji = "✨"
            ),
            ScienceFact(
                titleEn = "Our Solar System Has 8 Planets",
                titleUr = "ہمارے نظامِ شمسی میں آٹھ سیارے ہیں",
                detailEn = "Mercury, Venus, Earth, Mars, Jupiter, Saturn, Uranus, and Neptune all circle around our giant star, the Sun!",
                detailUr = "عطارد، زہرہ، زمین، مریخ، مشتری، زحل، یورینس اور نیپچون سب مل کر سورج کے گرد گھومتے ہیں!",
                emoji = "🪐"
            )
        ),
        quiz = ScienceQuizQuestion(
            questionEn = "How many hours does it take for Planet Earth to complete one full rotation on its axis?",
            questionUr = "زمین کو اپنے محور پر ایک مکمل چکر لگانے میں کتنے گھنٹے لگتے ہیں؟",
            optionsEn = listOf("6 hours", "12 hours", "24 hours (1 Day)", "48 hours"),
            optionsUr = listOf("۶ گھنٹے", "۱۲ گھنٹے", "۲۴ گھنٹے (۱ دن)", "۴۸ گھنٹے"),
            correctIndex = 2,
            explanationEn = "It takes exactly 24 hours for Earth to spin once, giving us one day and one night!",
            explanationUr = "زمین کو ایک چکر لگانے میں پورے ۲۴ گھنٹے لگتے ہیں جس سے ایک دن اور ایک رات بنتی ہے!"
        )
    )
)

@Composable
fun ScienceNatureExplorer(
    modifier: Modifier = Modifier
) {
    var selectedTopicIndex by remember { mutableIntStateOf(0) }
    val currentLang by LocaleManager.currentLanguage.collectAsState()
    val isUrdu = currentLang == LocaleManager.LANG_URDU
    val currentTopic = SCIENCE_TOPICS[selectedTopicIndex]

    // Mini-quiz state
    var selectedOptionIndex by remember(selectedTopicIndex) { mutableStateOf<Int?>(null) }
    var hasSubmittedQuiz by remember(selectedTopicIndex) { mutableStateOf(false) }
    var isQuizCorrect by remember(selectedTopicIndex) { mutableStateOf<Boolean?>(null) }
    var showParticleExplosion by remember(selectedTopicIndex) { mutableStateOf(false) }

    Card(
        modifier = modifier
            .fillMaxWidth()
            .testTag("science_nature_explorer"),
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
                                .background(Color(currentTopic.colorHex).copy(alpha = 0.2f)),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = currentTopic.emoji,
                                fontSize = 22.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = if (isUrdu) "سائنس اور فطرت کی دنیا" else "Science & Nature Explorer",
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 18.sp
                                )
                            )
                            Text(
                                text = if (isUrdu) "جانور، پودے، موسم اور کائنات کی حیرت انگیز معلومات" else "Animals, Plants, Weather Cycles & Space Wonders",
                                style = MaterialTheme.typography.labelSmall,
                                color = MaterialTheme.colorScheme.onSurfaceVariant
                            )
                        }
                    }

                    IconButton(
                        onClick = {
                            val speakText = if (isUrdu) {
                                "${currentTopic.nameUr}۔ ${currentTopic.overviewUr}"
                            } else {
                                "${currentTopic.nameEn}. ${currentTopic.overviewEn}"
                            }
                            TtsManager.speak(speakText, isUrdu)
                        },
                        modifier = Modifier.testTag("btn_speak_science_overview")
                    ) {
                        Icon(
                            imageVector = Icons.Default.VolumeUp,
                            contentDescription = "Speak overview",
                            tint = Color(currentTopic.colorHex)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Topic Filter Chips
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .horizontalScroll(rememberScrollState()),
                    horizontalArrangement = Arrangement.spacedBy(8.dp)
                ) {
                    SCIENCE_TOPICS.forEachIndexed { index, topic ->
                        val isSelected = selectedTopicIndex == index
                        FilterChip(
                            selected = isSelected,
                            onClick = {
                                selectedTopicIndex = index
                                val speakTitle = if (isUrdu) topic.nameUr else topic.nameEn
                                TtsManager.speak(speakTitle, isUrdu)
                            },
                            label = {
                                Text(
                                    text = "${topic.emoji} ${if (isUrdu) topic.nameUr else topic.nameEn}",
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium
                                )
                            },
                            colors = FilterChipDefaults.filterChipColors(
                                selectedContainerColor = Color(topic.colorHex).copy(alpha = 0.22f),
                                selectedLabelColor = Color(topic.colorHex)
                            ),
                            modifier = Modifier.testTag("science_topic_chip_${topic.id}")
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Topic Overview Card
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = Color(currentTopic.colorHex).copy(alpha = 0.08f)),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(14.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            Text(text = currentTopic.emoji, fontSize = 24.sp)
                            Text(
                                text = if (isUrdu) currentTopic.nameUr else currentTopic.nameEn,
                                style = MaterialTheme.typography.titleMedium.copy(
                                    fontWeight = FontWeight.Bold,
                                    color = Color(currentTopic.colorHex)
                                )
                            )
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = if (isUrdu) currentTopic.overviewUr else currentTopic.overviewEn,
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }

                Spacer(modifier = Modifier.height(14.dp))

                // Fun Facts Section
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = if (isUrdu) "🌟 دلچسپ اور سچی معلومات" else "🌟 Fascinating Facts",
                        style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                        color = MaterialTheme.colorScheme.onSurface
                    )
                    Text(
                        text = "${currentTopic.facts.size} Facts",
                        style = MaterialTheme.typography.labelSmall,
                        color = Color(currentTopic.colorHex)
                    )
                }

                Spacer(modifier = Modifier.height(8.dp))

                // Facts List
                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    currentTopic.facts.forEach { fact ->
                        val factTitle = if (isUrdu) fact.titleUr else fact.titleEn
                        val factDetail = if (isUrdu) fact.detailUr else fact.detailEn

                        Card(
                            shape = RoundedCornerShape(12.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.35f)),
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(12.dp),
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = fact.emoji,
                                    fontSize = 32.sp,
                                    modifier = Modifier.clickable {
                                        TtsManager.speak("$factTitle. $factDetail", isUrdu)
                                    }
                                )
                                Spacer(modifier = Modifier.width(12.dp))
                                Column(modifier = Modifier.weight(1f)) {
                                    Text(
                                        text = factTitle,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                        color = MaterialTheme.colorScheme.onSurface
                                    )
                                    Spacer(modifier = Modifier.height(2.dp))
                                    Text(
                                        text = factDetail,
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                                IconButton(
                                    onClick = {
                                        TtsManager.speak("$factTitle. $factDetail", isUrdu)
                                    },
                                    modifier = Modifier.size(32.dp)
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.VolumeUp,
                                        contentDescription = "Read fact",
                                        tint = Color(currentTopic.colorHex),
                                        modifier = Modifier.size(18.dp)
                                    )
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Interactive Mini-Quiz Section
                val quiz = currentTopic.quiz
                val questionText = if (isUrdu) quiz.questionUr else quiz.questionEn
                val options = if (isUrdu) quiz.optionsUr else quiz.optionsEn
                val explanation = if (isUrdu) quiz.explanationUr else quiz.explanationEn

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.25f)),
                    border = CardDefaults.outlinedCardBorder(),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("science_quiz_card_${currentTopic.id}")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            horizontalArrangement = Arrangement.SpaceBetween,
                            modifier = Modifier.fillMaxWidth()
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = StarGoldPrimary,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isUrdu) "کوئز: اپنی معلومات آزمائیں" else "Mini-Quiz: Test Your Knowledge",
                                    style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.primary
                                )
                            }
                            IconButton(
                                onClick = {
                                    TtsManager.speak(questionText, isUrdu)
                                },
                                modifier = Modifier.size(28.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.VolumeUp,
                                    contentDescription = "Read question",
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(8.dp))

                        Text(
                            text = questionText,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.onSurface
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Options
                        options.forEachIndexed { index, option ->
                            val isSelected = selectedOptionIndex == index
                            val borderColor = when {
                                hasSubmittedQuiz && index == quiz.correctIndex -> EmeraldSuccess
                                hasSubmittedQuiz && isSelected && index != quiz.correctIndex -> MaterialTheme.colorScheme.error
                                isSelected -> Color(currentTopic.colorHex)
                                else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.35f)
                            }
                            val bgTint = when {
                                hasSubmittedQuiz && index == quiz.correctIndex -> EmeraldSuccess.copy(alpha = 0.12f)
                                hasSubmittedQuiz && isSelected && index != quiz.correctIndex -> MaterialTheme.colorScheme.error.copy(alpha = 0.12f)
                                isSelected -> Color(currentTopic.colorHex).copy(alpha = 0.12f)
                                else -> MaterialTheme.colorScheme.surface
                            }

                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = bgTint),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .border(1.5.dp, borderColor, RoundedCornerShape(12.dp))
                                    .clickable(enabled = !hasSubmittedQuiz) {
                                        selectedOptionIndex = index
                                    }
                                    .testTag("science_quiz_opt_$index")
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(12.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { if (!hasSubmittedQuiz) selectedOptionIndex = index },
                                        colors = RadioButtonDefaults.colors(selectedColor = Color(currentTopic.colorHex))
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = option,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                                    )
                                }
                            }
                        }

                        // Immediate Feedback Banner
                        if (hasSubmittedQuiz) {
                            Spacer(modifier = Modifier.height(10.dp))
                            Surface(
                                shape = RoundedCornerShape(10.dp),
                                color = if (isQuizCorrect == true) EmeraldSuccess.copy(alpha = 0.15f) else MaterialTheme.colorScheme.error.copy(alpha = 0.15f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (isQuizCorrect == true) Icons.Default.CheckCircle else Icons.Default.ErrorOutline,
                                        contentDescription = null,
                                        tint = if (isQuizCorrect == true) EmeraldSuccess else MaterialTheme.colorScheme.error
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = if (isQuizCorrect == true) {
                                                if (isUrdu) "شاباش! بالکل درست جواب! 🎉" else "Awesome! That is correct! 🎉"
                                            } else {
                                                if (isUrdu) "کوئی بات نہیں! دوبارہ کوشش کریں 💪" else "Almost there! Keep trying, you can do it! 💪"
                                            },
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                            color = if (isQuizCorrect == true) EmeraldSuccess else MaterialTheme.colorScheme.error
                                        )
                                        Spacer(modifier = Modifier.height(3.dp))
                                        Text(
                                            text = "💡 $explanation",
                                            style = MaterialTheme.typography.bodySmall,
                                            color = MaterialTheme.colorScheme.onSurface
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        // Submit or Retry Action
                        if (!hasSubmittedQuiz) {
                            Button(
                                onClick = {
                                    if (selectedOptionIndex != null) {
                                        hasSubmittedQuiz = true
                                        val correct = selectedOptionIndex == quiz.correctIndex
                                        isQuizCorrect = correct
                                        if (correct) {
                                            showParticleExplosion = true
                                            val praise = if (isUrdu) "شاباش! بالکل درست جواب!" else "Awesome! That is the correct answer!"
                                            TtsManager.speak(praise, isUrdu)
                                        } else {
                                            val retryPrompt = if (isUrdu) "تقریباً درست، دوبارہ کوشش کریں۔" else "Almost there! Give it another try!"
                                            TtsManager.speak(retryPrompt, isUrdu)
                                        }
                                    }
                                },
                                enabled = selectedOptionIndex != null,
                                colors = ButtonDefaults.buttonColors(containerColor = Color(currentTopic.colorHex)),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("btn_submit_science_quiz"),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text(
                                    text = if (isUrdu) "جواب جمع کروائیں" else "Submit Answer",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        } else if (isQuizCorrect == false) {
                            OutlinedButton(
                                onClick = {
                                    hasSubmittedQuiz = false
                                    selectedOptionIndex = null
                                    isQuizCorrect = null
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("btn_retry_science_quiz"),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Refresh,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = if (isUrdu) "دوبارہ کوشش کریں" else "Try Again",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
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
