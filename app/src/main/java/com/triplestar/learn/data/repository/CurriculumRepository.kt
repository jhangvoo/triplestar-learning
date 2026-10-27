package com.triplestar.learn.data.repository

import com.triplestar.learn.data.model.Lesson
import com.triplestar.learn.data.model.Question
import com.triplestar.learn.data.model.Step
import com.triplestar.learn.data.model.StepType
import com.triplestar.learn.data.model.Subject
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flowOf

class CurriculumRepository {

    private val subjects = listOf(
        Subject(
            id = "english",
            titleEn = "English",
            titleUr = "انگریزی",
            subtitleEn = "Alphabet, Phonics & Reading",
            subtitleUr = "حروف، صوتیات اور الفاظ",
            iconName = "menu_book",
            displayOrder = 1,
            colorHex = 0xFFF43F5E
        ),
        Subject(
            id = "math",
            titleEn = "Math",
            titleUr = "ریاضی",
            subtitleEn = "Numbers, Counting & Shapes",
            subtitleUr = "گنتی، جمع تفریق اور شکلیں",
            iconName = "calculate",
            displayOrder = 2,
            colorHex = 0xFF2563EB
        ),
        Subject(
            id = "science",
            titleEn = "Science",
            titleUr = "سائنس",
            subtitleEn = "Living Things, Plants & Space",
            subtitleUr = "جاندار، پودے اور کائنات",
            iconName = "science",
            displayOrder = 3,
            colorHex = 0xFF10B981
        ),
        Subject(
            id = "art",
            titleEn = "Art",
            titleUr = "آرٹ اور ڈرائنگ",
            subtitleEn = "Colors, Drawing & Patterns",
            subtitleUr = "رنگ، ڈیزائن اور تخلیقی صلاحیت",
            iconName = "palette",
            displayOrder = 4,
            colorHex = 0xFF8B5CF6
        ),
        Subject(
            id = "computer",
            titleEn = "Computer",
            titleUr = "کمپیوٹر اور کوڈنگ",
            subtitleEn = "Coding, Logic & Hardware",
            subtitleUr = "ہارڈویئر، ٹائپنگ اور لاجک",
            iconName = "computer",
            displayOrder = 5,
            colorHex = 0xFF0284C7
        ),
        Subject(
            id = "urdu",
            titleEn = "Urdu Literacy",
            titleUr = "اردو زبان و ادب",
            subtitleEn = "Haroof-e-Tahaji & Adab",
            subtitleUr = "حروفِ تہجی اور عبارت فہمی",
            iconName = "book_open",
            displayOrder = 6,
            colorHex = 0xFFF59E0B
        ),
        Subject(
            id = "general_knowledge",
            titleEn = "General Knowledge",
            titleUr = "عام معلومات",
            subtitleEn = "World Geography & Inventions",
            subtitleUr = "دنیا کا جغرافیہ اور ایجادات",
            iconName = "globe",
            displayOrder = 7,
            colorHex = 0xFF0D9488
        )
    )

    fun getSubjects(): Flow<List<Subject>> = flowOf(subjects)

    fun getLessonsForSubject(subjectId: String): List<Lesson> {
        return when (subjectId) {
            "english" -> listOf(
                Lesson(id = "eng_l1", subjectId = "english", titleEn = "Level 1: Alphabet & Phonics", titleUr = "لیول ۱: حروف تہجی اور صوتیات", order = 1),
                Lesson(id = "eng_l2", subjectId = "english", titleEn = "Level 2: Sight Words & Vocabulary", titleUr = "لیول ۲: الفاظ اور ذخیرہ الفاظ", order = 2),
                Lesson(id = "eng_l3", subjectId = "english", titleEn = "Level 3: Reading & Comprehension", titleUr = "لیول ۳: پڑھنا اور سمجھنا", order = 3)
            )
            "math" -> listOf(
                Lesson(id = "math_l1", subjectId = "math", titleEn = "Level 1: Counting & Numbers (1-20)", titleUr = "لیول ۱: گنتی اور نمبر (۱ تا ۲۰)", order = 1),
                Lesson(id = "math_l2", subjectId = "math", titleEn = "Level 2: Addition & Subtraction", titleUr = "لیول ۲: جمع اور تفریق", order = 2),
                Lesson(id = "math_l3", subjectId = "math", titleEn = "Level 3: Shapes & Word Problems", titleUr = "لیول ۳: شکلیں اور حسابی مسائل", order = 3)
            )
            "science" -> listOf(
                Lesson(id = "sci_l1", subjectId = "science", titleEn = "Level 1: Animals, Plants & Habitats", titleUr = "لیول ۱: جانور، پودے اور ماحول", order = 1),
                Lesson(id = "sci_l2", subjectId = "science", titleEn = "Level 2: Five Senses & Human Body", titleUr = "لیول ۲: پانچ حسیں اور انسانی جسم", order = 2),
                Lesson(id = "sci_l3", subjectId = "science", titleEn = "Level 3: Weather, Earth & Space", titleUr = "لیول ۳: موسم، زمین اور کائنات", order = 3)
            )
            "art" -> listOf(
                Lesson(id = "art_l1", subjectId = "art", titleEn = "Level 1: Colors & Basic Shapes", titleUr = "لیول ۱: بنیادی رنگ اور شکلیں", order = 1),
                Lesson(id = "art_l2", subjectId = "art", titleEn = "Level 2: Drawing & Patterns", titleUr = "لیول ۲: ڈرائنگ اور ڈیزائن", order = 2),
                Lesson(id = "art_l3", subjectId = "art", titleEn = "Level 3: World Art & Creativity", titleUr = "لیول ۳: تصویری فن اور تخلیق", order = 3)
            )
            "computer" -> listOf(
                Lesson(id = "comp_l1", subjectId = "computer", titleEn = "Level 1: Computer Basics & Hardware", titleUr = "لیول ۱: کمپیوٹر کی بنیادی معلومات", order = 1),
                Lesson(id = "comp_l2", subjectId = "computer", titleEn = "Level 2: Coding Concepts & Logic", titleUr = "لیول ۲: کوڈنگ کے تصورات اور لاجک", order = 2),
                Lesson(id = "comp_l3", subjectId = "computer", titleEn = "Level 3: Digital Skills & Safety", titleUr = "لیول ۳: ڈیجیٹل مہارتیں اور حفاظت", order = 3)
            )
            "urdu" -> listOf(
                Lesson(id = "urdu_l1", subjectId = "urdu", titleEn = "Level 1: Haroof-e-Tahaji (حروف تہجی)", titleUr = "لیول ۱: حروفِ تہجی کی پہچان", order = 1),
                Lesson(id = "urdu_l2", subjectId = "urdu", titleEn = "Level 2: Word Building (الفاظ بنانا)", titleUr = "لیول ۲: الفاظ اور جوڑ توڑ", order = 2),
                Lesson(id = "urdu_l3", subjectId = "urdu", titleEn = "Level 3: Reading & Grammar (عبارت فہمی)", titleUr = "لیول ۳: جملے اور عبارت فہمی", order = 3)
            )
            "general_knowledge" -> listOf(
                Lesson(id = "gk_l1", subjectId = "general_knowledge", titleEn = "Level 1: Pakistan Studies", titleUr = "لیول ۱: مطالعہ پاکستان", order = 1),
                Lesson(id = "gk_l2", subjectId = "general_knowledge", titleEn = "Level 2: World Geography & Capitals", titleUr = "لیول ۲: دنیا کا جغرافیہ اور دارالحکومت", order = 2),
                Lesson(id = "gk_l3", subjectId = "general_knowledge", titleEn = "Level 3: Inventions & Everyday Safety", titleUr = "لیول ۳: ایجادات اور روزمرہ حفاظت", order = 3)
            )
            else -> listOf(
                Lesson(id = "${subjectId}_l1", subjectId = subjectId, titleEn = "Foundations 1", titleUr = "بنیادی مرحلہ ۱", order = 1)
            )
        }
    }

    fun getStepsForLesson(lessonId: String): List<Step> {
        return when {
            lessonId.startsWith("eng") -> listOf(
                Step(
                    id = "${lessonId}_s1",
                    lessonId = lessonId,
                    stepNumber = 1,
                    stepType = StepType.LEARN.name,
                    titleEn = "Letter Sounds & Phonics",
                    titleUr = "حروف کی آوازیں اور صوتیات",
                    contentEn = "Phonics helps connect letters with sounds to read words effortlessly. For example, letter 'A' makes the /æ/ sound as in Apple.",
                    contentUr = "صوتیات حروف کو آوازوں سے جوڑنے میں مدد دیتی ہے۔ مثال کے طور پر 'A' کی آواز 'ایپل' جیسی ہوتی ہے۔",
                    xpReward = 15,
                    starsReward = 1
                ),
                Step(
                    id = "${lessonId}_s2",
                    lessonId = lessonId,
                    stepNumber = 2,
                    stepType = StepType.PRACTICE.name,
                    titleEn = "Guided Word Practice",
                    titleUr = "رہنمائی شدہ الفاظ کی مشق",
                    contentEn = "Practice identifying starting sounds and spelling simple words.",
                    contentUr = "شروع کی آوازوں اور آسان الفاظ کے ہجے کی مشق کریں۔",
                    questions = listOf(
                        Question(
                            id = "${lessonId}_q1",
                            questionEn = "What letter does 'Apple' start with?",
                            questionUr = "لفظ 'Apple' کس حرف سے شروع ہوتا ہے؟",
                            optionsEn = listOf("A", "B", "C", "D"),
                            optionsUr = listOf("A", "B", "C", "D"),
                            correctIndex = 0,
                            explanationEn = "Apple starts with the letter A.",
                            explanationUr = "ایپل حرف A سے شروع ہوتا ہے۔"
                        )
                    ),
                    xpReward = 20,
                    starsReward = 1
                ),
                Step(
                    id = "${lessonId}_s3",
                    lessonId = lessonId,
                    stepNumber = 3,
                    stepType = StepType.QUIZ.name,
                    titleEn = "Mastery Quiz",
                    titleUr = "مہارت کا امتحان",
                    contentEn = "Test your reading and vocabulary knowledge.",
                    contentUr = "اپنے الفاظ اور پڑھنے کی صلاحیت کا امتحان لیں۔",
                    questions = listOf(
                        Question(
                            id = "${lessonId}_q2",
                            questionEn = "Which word is a noun (naming word)?",
                            questionUr = "ان میں سے کون سا لفظ اسم (نام) ہے؟",
                            optionsEn = listOf("Run", "Cat", "Happy", "Fast"),
                            optionsUr = listOf("دوڑنا", "بلی", "خوش", "تیز"),
                            correctIndex = 1,
                            explanationEn = "'Cat' is an animal name (noun).",
                            explanationUr = "'بلی' ایک جانور کا نام (اسم) ہے۔"
                        )
                    ),
                    xpReward = 30,
                    starsReward = 1
                )
            )
            lessonId.startsWith("urdu") -> listOf(
                Step(
                    id = "${lessonId}_s1",
                    lessonId = lessonId,
                    stepNumber = 1,
                    stepType = StepType.LEARN.name,
                    titleEn = "Haroof-e-Tahaji (حروف تہجی)",
                    titleUr = "حروفِ تہجی کی بنیاد",
                    contentEn = "Urdu has beautiful letters. 'الف' (Alif) is the first letter of Urdu alphabet.",
                    contentUr = "اردو کے حروف بہت خوبصورت ہیں۔ 'الف' اردو حروف تہجی کا پہلا حرف ہے۔",
                    xpReward = 15,
                    starsReward = 1
                ),
                Step(
                    id = "${lessonId}_s2",
                    lessonId = lessonId,
                    stepNumber = 2,
                    stepType = StepType.PRACTICE.name,
                    titleEn = "Word Building Practice",
                    titleUr = "الفاظ بنانے کی مشق",
                    contentEn = "Learn how letters combine in Urdu words.",
                    contentUr = "سیکھیں کہ اردو میں حروف کو کیسے جوڑا جاتا ہے۔",
                    questions = listOf(
                        Question(
                            id = "${lessonId}_q1",
                            questionEn = "What sound does 'ب' make?",
                            questionUr = "'ب' کی آواز کیا ہے؟",
                            optionsEn = listOf("Bay / B", "Alif / A", "Seen / S", "Alif Bay"),
                            optionsUr = listOf("بے / B", "الف / A", "سین / S", "الف بے"),
                            correctIndex = 0,
                            explanationEn = "'ب' sounds like B (Bay).",
                            explanationUr = "'ب' کی آواز 'بے' (B) ہے۔"
                        )
                    ),
                    xpReward = 20,
                    starsReward = 1
                ),
                Step(
                    id = "${lessonId}_s3",
                    lessonId = lessonId,
                    stepNumber = 3,
                    stepType = StepType.QUIZ.name,
                    titleEn = "Urdu Quiz",
                    titleUr = "اردو کوئز",
                    contentEn = "Test your Urdu reading skills.",
                    contentUr = "اپنی اردو پڑھنے کی مہارت کا امتحان لیں۔",
                    questions = listOf(
                        Question(
                            id = "${lessonId}_q2",
                            questionEn = "Which word means Apple in Urdu?",
                            questionUr = "اردو میں سیب کو کیا کہتے ہیں؟",
                            optionsEn = listOf("Kela", "Seb", "Anar", "Aam"),
                            optionsUr = listOf("کیلا", "سیب", "انار", "آم"),
                            correctIndex = 1,
                            explanationEn = "Apple translates to Seb (سیب) in Urdu.",
                            explanationUr = "ایپل کا اردو ترجمہ 'سیب' ہے۔"
                        )
                    ),
                    xpReward = 30,
                    starsReward = 1
                )
            )
            lessonId.startsWith("math") -> listOf(
                Step(
                    id = "${lessonId}_s1",
                    lessonId = lessonId,
                    stepNumber = 1,
                    stepType = StepType.LEARN.name,
                    titleEn = "Counting & Number Concepts",
                    titleUr = "گنتی اور نمبر کے تصورات",
                    contentEn = "Numbers help us count objects. 1, 2, 3, 4, 5...",
                    contentUr = "نمبر ہمیں چیزیں گننے میں مدد دیتے ہیں۔ ۱، ۲، ۳، ۴، ۵...",
                    xpReward = 15,
                    starsReward = 1
                ),
                Step(
                    id = "${lessonId}_s2",
                    lessonId = lessonId,
                    stepNumber = 2,
                    stepType = StepType.PRACTICE.name,
                    titleEn = "Addition Practice",
                    titleUr = "جمع کی مشق",
                    contentEn = "Addition means putting things together. 2 + 3 = 5.",
                    contentUr = "جمع کرنے کا مطلب چیزوں کو اکٹھا کرنا ہے۔ ۲ + ۳ = ۵۔",
                    questions = listOf(
                        Question(
                            id = "${lessonId}_q1",
                            questionEn = "What is 3 + 2?",
                            questionUr = "۳ + ۲ کیا ہوتا ہے؟",
                            optionsEn = listOf("4", "5", "6", "7"),
                            optionsUr = listOf("۴", "۵", "۶", "۷"),
                            correctIndex = 1,
                            explanationEn = "3 plus 2 equals 5.",
                            explanationUr = "۳ اور ۲ مل کر ۵ بنتے ہیں۔"
                        )
                    ),
                    xpReward = 20,
                    starsReward = 1
                ),
                Step(
                    id = "${lessonId}_s3",
                    lessonId = lessonId,
                    stepNumber = 3,
                    stepType = StepType.QUIZ.name,
                    titleEn = "Math Mastery Quiz",
                    titleUr = "ریاضی کا امتحان",
                    contentEn = "Solve math problems to earn your star.",
                    contentUr = "اپنا ستارہ حاصل کرنے کے لیے ریاضی کے سوالات حل کریں۔",
                    questions = listOf(
                        Question(
                            id = "${lessonId}_q2",
                            questionEn = "What is 10 - 4?",
                            questionUr = "۱۰ میں سے ۴ نکالیں تو کیا بچتا ہے؟",
                            optionsEn = listOf("5", "6", "7", "8"),
                            optionsUr = listOf("۵", "۶", "۷", "۸"),
                            correctIndex = 1,
                            explanationEn = "10 minus 4 equals 6.",
                            explanationUr = "۱۰ میں سے ۴ تفریق کریں تو ۶ بچتے ہیں۔"
                        )
                    ),
                    xpReward = 30,
                    starsReward = 1
                )
            )
            lessonId.startsWith("sci") -> listOf(
                Step(
                    id = "${lessonId}_s1",
                    lessonId = lessonId,
                    stepNumber = 1,
                    stepType = StepType.LEARN.name,
                    titleEn = "Animals & Habitats",
                    titleUr = "جانور اور ان کے مسکن",
                    contentEn = "Animals live in natural places called habitats. Forests, oceans, and deserts are habitats.",
                    contentUr = "جانور قدرتی جگہوں پر رہتے ہیں جنہیں مسکن کہتے ہیں۔ جنگلات، سمندر اور صحرا مسکن ہیں۔",
                    xpReward = 15,
                    starsReward = 1
                ),
                Step(
                    id = "${lessonId}_s2",
                    lessonId = lessonId,
                    stepNumber = 2,
                    stepType = StepType.PRACTICE.name,
                    titleEn = "Five Senses Practice",
                    titleUr = "پانچ حسوں کی مشق",
                    contentEn = "We use our eyes to see, ears to hear, nose to smell, tongue to taste, and skin to feel.",
                    contentUr = "ہم دیکھنے کے لیے آنکھیں، سننے کے لیے کان، سونگھنے کے لیے ناک، چکھنے کے لیے زبان اور چھونے کے لیے جلد استعمال کرتے ہیں۔",
                    questions = listOf(
                        Question(
                            id = "${lessonId}_q1",
                            questionEn = "Which body part do we use to see?",
                            questionUr = "ہم دیکھنے کے لیے جسم کا کون سا حصہ استعمال کرتے ہیں؟",
                            optionsEn = listOf("Ears", "Eyes", "Nose", "Hands"),
                            optionsUr = listOf("کان", "آنکھیں", "ناک", "ہاتھ"),
                            correctIndex = 1,
                            explanationEn = "We use our eyes to see the world around us.",
                            explanationUr = "ہم اپنے اردگرد کی دنیا دیکھنے کے لیے آنکھیں استعمال کرتے ہیں۔"
                        )
                    ),
                    xpReward = 20,
                    starsReward = 1
                ),
                Step(
                    id = "${lessonId}_s3",
                    lessonId = lessonId,
                    stepNumber = 3,
                    stepType = StepType.QUIZ.name,
                    titleEn = "Science Mastery Quiz",
                    titleUr = "سائنس کوئز",
                    contentEn = "Test your science knowledge.",
                    contentUr = "اپنی سائنس کی معلومات کا امتحان لیں۔",
                    questions = listOf(
                        Question(
                            id = "${lessonId}_q2",
                            questionEn = "What planet do we live on?",
                            questionUr = "ہم کس سیارے پر رہتے ہیں؟",
                            optionsEn = listOf("Mars", "Earth", "Venus", "Jupiter"),
                            optionsUr = listOf("مریخ", "زمین", "زہرہ", "مشتری"),
                            correctIndex = 1,
                            explanationEn = "We live on Planet Earth.",
                            explanationUr = "ہم سیارہ زمین پر رہتے ہیں۔"
                        )
                    ),
                    xpReward = 30,
                    starsReward = 1
                )
            )
            lessonId.startsWith("gk") -> listOf(
                Step(
                    id = "${lessonId}_s1",
                    lessonId = lessonId,
                    stepNumber = 1,
                    stepType = StepType.LEARN.name,
                    titleEn = "Pakistan Studies",
                    titleUr = "مطالعہ پاکستان",
                    contentEn = "Pakistan has four main provinces: Punjab, Sindh, Khyber Pakhtunkhwa, and Balochistan. Islamabad is the capital.",
                    contentUr = "پاکستان کے چار بڑے صوبے ہیں: پنجاب، سندھ، خیبر پختونخوا اور بلوچستان۔ اسلام آباد دارالحکومت ہے۔",
                    xpReward = 15,
                    starsReward = 1
                ),
                Step(
                    id = "${lessonId}_s2",
                    lessonId = lessonId,
                    stepNumber = 2,
                    stepType = StepType.PRACTICE.name,
                    titleEn = "World Geography Practice",
                    titleUr = "دنیا کا جغرافیہ",
                    contentEn = "There are 7 continents on Earth. Asia is the largest continent.",
                    contentUr = "زمین پر ۷ براعظم ہیں۔ ایشیا سب سے بڑا براعظم ہے۔",
                    questions = listOf(
                        Question(
                            id = "${lessonId}_q1",
                            questionEn = "What is the capital of Pakistan?",
                            questionUr = "پاکستان کا دارالحکومت کیا ہے؟",
                            optionsEn = listOf("Lahore", "Karachi", "Islamabad", "Peshawar"),
                            optionsUr = listOf("لاہور", "کراچی", "اسلام آباد", "پشاور"),
                            correctIndex = 2,
                            explanationEn = "Islamabad is the capital of Pakistan.",
                            explanationUr = "اسلام آباد پاکستان کا دارالحکومت ہے۔"
                        )
                    ),
                    xpReward = 20,
                    starsReward = 1
                ),
                Step(
                    id = "${lessonId}_s3",
                    lessonId = lessonId,
                    stepNumber = 3,
                    stepType = StepType.QUIZ.name,
                    titleEn = "General Knowledge Quiz",
                    titleUr = "عام معلومات کا امتحان",
                    contentEn = "Test your general knowledge.",
                    contentUr = "اپنی عام معلومات کی جانچ کریں۔",
                    questions = listOf(
                        Question(
                            id = "${lessonId}_q2",
                            questionEn = "How many continents are there in the world?",
                            questionUr = "دنیا میں کل کتنے براعظم ہیں؟",
                            optionsEn = listOf("5", "6", "7", "8"),
                            optionsUr = listOf("۵", "۶", "۷", "۸"),
                            correctIndex = 2,
                            explanationEn = "There are 7 continents in total.",
                            explanationUr = "دنیا میں کل ۷ براعظم ہیں۔"
                        )
                    ),
                    xpReward = 30,
                    starsReward = 1
                )
            )
            lessonId.startsWith("art") -> listOf(
                Step(
                    id = "${lessonId}_s1",
                    lessonId = lessonId,
                    stepNumber = 1,
                    stepType = StepType.LEARN.name,
                    titleEn = "Primary Colors & Mixing",
                    titleUr = "بنیادی رنگ اور ان کا ملاپ",
                    contentEn = "Red, Blue, and Yellow are the three primary colors! When you mix Red and Yellow, you create beautiful Orange.",
                    contentUr = "سرخ، نیلا اور پیلا تین بنیادی رنگ ہیں۔ سرخ اور پیلے کو ملانے سے نارنجی رنگ بنتا ہے۔",
                    xpReward = 15,
                    starsReward = 1
                ),
                Step(
                    id = "${lessonId}_s2",
                    lessonId = lessonId,
                    stepNumber = 2,
                    stepType = StepType.DRAWING.name,
                    titleEn = "Interactive Drawing Canvas",
                    titleUr = "تخلیقی ڈرائنگ کینوس",
                    contentEn = "Use the drawing canvas below to draw and color shapes, animals, or patterns with different brush sizes and colors!",
                    contentUr = "مختلف رنگوں اور برش کے ساتھ شکلیں، جانور یا پیٹرن بنانے کے لیے نیچے دیے گئے ڈرائنگ کینوس کا استعمال کریں!",
                    xpReward = 20,
                    starsReward = 1
                ),
                Step(
                    id = "${lessonId}_s3",
                    lessonId = lessonId,
                    stepNumber = 3,
                    stepType = StepType.QUIZ.name,
                    titleEn = "Master Artist Challenge",
                    titleUr = "فنکاری کا چیلنج",
                    contentEn = "Test your artistic knowledge and color awareness.",
                    contentUr = "رنگوں اور فنکاری سے متعلق اپنی صلاحیتوں کا امتحان لیں۔",
                    questions = listOf(
                        Question(
                            id = "${lessonId}_q2",
                            questionEn = "How many primary colors are there in basic art?",
                            questionUr = "بنیادی فن میں کتنے بنیادی رنگ ہوتے ہیں؟",
                            optionsEn = listOf("2", "3", "5", "10"),
                            optionsUr = listOf("۲", "۳", "۵", "۱۰"),
                            correctIndex = 1,
                            explanationEn = "There are 3 primary colors: Red, Blue, and Yellow.",
                            explanationUr = "تین بنیادی رنگ ہیں: سرخ، نیلا اور پیلا۔"
                        )
                    ),
                    xpReward = 25,
                    starsReward = 1
                )
            )
            lessonId.startsWith("comp") -> listOf(
                Step(
                    id = "${lessonId}_s1",
                    lessonId = lessonId,
                    stepNumber = 1,
                    stepType = StepType.LEARN.name,
                    titleEn = "Parts of a Computer",
                    titleUr = "کمپیوٹر کے اہم حصے",
                    contentEn = "A computer has hardware parts: the Monitor shows pictures, the CPU is the brain, and the Keyboard helps us type words.",
                    contentUr = "کمپیوٹر کے اہم حصے: مانیٹر تصاویر دکھاتا ہے، سی پی یو دماغ کی طرح کام کرتا ہے، اور کی بورڈ سے ہم لکھتے ہیں۔",
                    xpReward = 15,
                    starsReward = 1
                ),
                Step(
                    id = "${lessonId}_s2",
                    lessonId = lessonId,
                    stepNumber = 2,
                    stepType = StepType.PRACTICE.name,
                    titleEn = "Input vs Output Devices",
                    titleUr = "ان پٹ اور آؤٹ پٹ آلات",
                    contentEn = "A mouse and keyboard send information into the computer (Input). A screen and speakers display or play sounds (Output).",
                    contentUr = "ماؤس اور کی بورڈ معلومات اندر بھیجتے ہیں (ان پٹ)۔ اسکرین اور اسپیکر معلومات دکھاتے یا سناتے ہیں (آؤٹ پٹ)۔",
                    questions = listOf(
                        Question(
                            id = "${lessonId}_q1",
                            questionEn = "Which device is considered the 'brain' of the computer?",
                            questionUr = "کمپیوٹر کا دماغ کس حصے کو کہا جاتا ہے؟",
                            optionsEn = listOf("Monitor", "CPU", "Mouse", "Printer"),
                            optionsUr = listOf("مانیٹر", "سی پی یو (CPU)", "ماؤس", "پرنٹر"),
                            correctIndex = 1,
                            explanationEn = "The Central Processing Unit (CPU) executes instructions like a brain.",
                            explanationUr = "سی پی یو (CPU) تمام حساب کتاب اور ہدایات پر عمل کرتا ہے۔"
                        )
                    ),
                    xpReward = 20,
                    starsReward = 1
                ),
                Step(
                    id = "${lessonId}_s3",
                    lessonId = lessonId,
                    stepNumber = 3,
                    stepType = StepType.QUIZ.name,
                    titleEn = "Junior Coder Challenge",
                    titleUr = "ننھے کوڈر کا امتحان",
                    contentEn = "Demonstrate your understanding of technology and algorithms.",
                    contentUr = "ٹیکنالوجی اور کمپیوٹر سے متعلق اپنی معلومات کا امتحان دیں۔",
                    questions = listOf(
                        Question(
                            id = "${lessonId}_q2",
                            questionEn = "What is a step-by-step set of instructions for a computer called?",
                            questionUr = "کمپیوٹر کے لیے مرحلہ وار ہدایات کے سیٹ کو کیا کہتے ہیں؟",
                            optionsEn = listOf("Algorithm", "Hardware", "Screen", "Cable"),
                            optionsUr = listOf("الگورتھم (Algorithm)", "ہارڈویئر", "اسکرین", "کیبل"),
                            correctIndex = 0,
                            explanationEn = "An algorithm is a step-by-step procedure to solve problems.",
                            explanationUr = "الگورتھم مسائل حل کرنے کے لیے مرحلہ وار ہدایات ہیں۔"
                        )
                    ),
                    xpReward = 25,
                    starsReward = 1
                )
            )
            else -> listOf(
                Step(
                    id = "${lessonId}_s1",
                    lessonId = lessonId,
                    stepNumber = 1,
                    stepType = StepType.LEARN.name,
                    titleEn = "Core Concepts",
                    titleUr = "بنیادی تصورات",
                    contentEn = "Introduction to core learning concepts.",
                    contentUr = "بنیادی تعلیمی تصورات کا تعارف۔",
                    xpReward = 15,
                    starsReward = 1
                ),
                Step(
                    id = "${lessonId}_s2",
                    lessonId = lessonId,
                    stepNumber = 2,
                    stepType = StepType.PRACTICE.name,
                    titleEn = "Guided Practice",
                    titleUr = "رہنمائی شدہ مشق",
                    contentEn = "Practice what you learned.",
                    contentUr = "سیکھی ہوئی چیز کی مشق کریں۔",
                    questions = listOf(
                        Question(
                            id = "${lessonId}_q1",
                            questionEn = "Is learning fun?",
                            questionUr = "کیا سیکھنا مزے کا ہے؟",
                            optionsEn = listOf("No", "Yes", "Maybe", "Never"),
                            optionsUr = listOf("نہیں", "ہاں", "شاید", "کبھی نہیں"),
                            correctIndex = 1,
                            explanationEn = "Learning is always fun and exciting!",
                            explanationUr = "سیکھنا ہمیشہ پرلطف اور شاندار ہوتا ہے!"
                        )
                    ),
                    xpReward = 20,
                    starsReward = 1
                ),
                Step(
                    id = "${lessonId}_s3",
                    lessonId = lessonId,
                    stepNumber = 3,
                    stepType = StepType.QUIZ.name,
                    titleEn = "Mastery Challenge",
                    titleUr = "مہارت کا امتحان",
                    contentEn = "Complete the final challenge.",
                    contentUr = "آخری چیلنج مکمل کریں۔",
                    questions = listOf(
                        Question(
                            id = "${lessonId}_q2",
                            questionEn = "Are you ready to grow?",
                            questionUr = "کیا آپ آگے بڑھنے کے لیے تیار ہیں؟",
                            optionsEn = listOf("No", "Yes", "Later", "Stop"),
                            optionsUr = listOf("نہیں", "ہاں", "بعد میں", "روکیں"),
                            correctIndex = 1,
                            explanationEn = "Great job! Keep exploring.",
                            explanationUr = "بہت خوب! سیکھتے رہیے۔"
                        )
                    ),
                    xpReward = 30,
                    starsReward = 1
                )
            )
        }
    }
}
