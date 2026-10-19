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
            titleEn = "English Literacy",
            titleUr = "انگریزی زبان",
            iconName = "alphabet",
            displayOrder = 1
        ),
        Subject(
            id = "urdu",
            titleEn = "Urdu Literacy",
            titleUr = "اردو زبان و ادب",
            iconName = "book_open",
            displayOrder = 2
        ),
        Subject(
            id = "math",
            titleEn = "Mathematics",
            titleUr = "ریاضی (میتھس)",
            iconName = "calculator",
            displayOrder = 3
        ),
        Subject(
            id = "science",
            titleEn = "Science & Nature",
            titleUr = "سائنس اور فطرت",
            iconName = "microscope",
            displayOrder = 4
        ),
        Subject(
            id = "general_knowledge",
            titleEn = "General Knowledge",
            titleUr = "عام معلومات",
            iconName = "globe",
            displayOrder = 5
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
            "urdu" -> listOf(
                Lesson(id = "urdu_l1", subjectId = "urdu", titleEn = "Level 1: Haroof-e-Tahaji (حروف تہجی)", titleUr = "لیول ۱: حروفِ تہجی کی پہچان", order = 1),
                Lesson(id = "urdu_l2", subjectId = "urdu", titleEn = "Level 2: Word Building (الفاظ بنانا)", titleUr = "لیول ۲: الفاظ اور جوڑ توڑ", order = 2),
                Lesson(id = "urdu_l3", subjectId = "urdu", titleEn = "Level 3: Reading & Grammar (عبارت فہمی)", titleUr = "لیول ۳: جملے اور عبارت فہمی", order = 3)
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
