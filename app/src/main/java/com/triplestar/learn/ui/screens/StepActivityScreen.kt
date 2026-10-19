package com.triplestar.learn.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.triplestar.learn.R
import com.triplestar.learn.data.repository.CurriculumRepository
import com.triplestar.learn.data.repository.ProgressRepository
import com.triplestar.learn.ui.theme.AstroBluePrimary
import com.triplestar.learn.ui.theme.EmeraldSuccess
import com.triplestar.learn.ui.theme.StarGoldPrimary
import com.triplestar.learn.util.LocaleManager
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StepActivityScreen(
    childId: String,
    parentId: String,
    subjectId: String,
    lessonId: String,
    stepId: String,
    curriculumRepository: CurriculumRepository,
    progressRepository: ProgressRepository,
    onStepCompleted: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val coroutineScope = rememberCoroutineScope()
    val currentLang by LocaleManager.currentLanguage.collectAsState()
    val allSteps = remember(lessonId) { curriculumRepository.getStepsForLesson(lessonId) }
    val currentStep = remember(allSteps, stepId) { allSteps.firstOrNull { it.id == stepId } ?: allSteps.firstOrNull() }

    var selectedOptionIndex by remember(stepId) { mutableStateOf<Int?>(null) }
    var isAnswerCorrect by remember(stepId) { mutableStateOf<Boolean?>(null) }
    var hasSubmittedAnswer by remember(stepId) { mutableStateOf(false) }

    var isSubmitting by remember { mutableStateOf(false) }
    var hasCompleted by remember { mutableStateOf(false) }

    val questions = currentStep?.questions ?: emptyList()
    val currentQuestion = questions.firstOrNull()

    val title = if (currentLang == LocaleManager.LANG_URDU) currentStep?.titleUr ?: "" else currentStep?.titleEn ?: ""
    val content = if (currentLang == LocaleManager.LANG_URDU) currentStep?.contentUr ?: "" else currentStep?.contentEn ?: ""

    val needsAnswering = currentQuestion != null
    val canComplete = if (needsAnswering) hasSubmittedAnswer && (isAnswerCorrect == true) else true

    Scaffold(
        modifier = modifier.fillMaxSize().testTag("step_activity_screen"),
        topBar = {
            TopAppBar(
                title = { Text(title, fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("btn_back")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.action_back)
                        )
                    }
                },
                colors = TopAppBarDefaults.topAppBarColors(
                    containerColor = MaterialTheme.colorScheme.background
                )
            )
        }
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState()),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(72.dp)
                    .clip(CircleShape)
                    .background(AstroBluePrimary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Star,
                    contentDescription = null,
                    tint = StarGoldPrimary,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            Text(
                text = title,
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                textAlign = TextAlign.Center
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Explanation / Content Card
            if (content.isNotBlank()) {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = content,
                            style = MaterialTheme.typography.bodyMedium,
                            lineHeight = 22.sp,
                            color = MaterialTheme.colorScheme.onSurface
                        )
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Interactive Question / Quiz Card if questions exist
            if (currentQuestion != null) {
                val qText = if (currentLang == LocaleManager.LANG_URDU) currentQuestion.questionUr else currentQuestion.questionEn
                val options = if (currentLang == LocaleManager.LANG_URDU) currentQuestion.optionsUr else currentQuestion.optionsEn
                val explanation = if (currentLang == LocaleManager.LANG_URDU) currentQuestion.explanationUr else currentQuestion.explanationEn

                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(20.dp)) {
                        Text(
                            text = "Question:",
                            style = MaterialTheme.typography.labelMedium,
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = qText,
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )

                        Spacer(modifier = Modifier.height(16.dp))

                        options.forEachIndexed { index, option ->
                            val isSelected = selectedOptionIndex == index
                            val borderColor = when {
                                hasSubmittedAnswer && index == currentQuestion.correctIndex -> EmeraldSuccess
                                hasSubmittedAnswer && isSelected && index != currentQuestion.correctIndex -> MaterialTheme.colorScheme.error
                                isSelected -> MaterialTheme.colorScheme.primary
                                else -> MaterialTheme.colorScheme.outline.copy(alpha = 0.4f)
                            }
                            val backgroundColor = when {
                                hasSubmittedAnswer && index == currentQuestion.correctIndex -> EmeraldSuccess.copy(alpha = 0.1f)
                                hasSubmittedAnswer && isSelected && index != currentQuestion.correctIndex -> MaterialTheme.colorScheme.error.copy(alpha = 0.1f)
                                isSelected -> MaterialTheme.colorScheme.primary.copy(alpha = 0.1f)
                                else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.3f)
                            }

                            Card(
                                shape = RoundedCornerShape(12.dp),
                                colors = CardDefaults.cardColors(containerColor = backgroundColor),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp)
                                    .border(2.dp, borderColor, RoundedCornerShape(12.dp))
                                    .clickable(enabled = !hasSubmittedAnswer) {
                                        selectedOptionIndex = index
                                    }
                                    .testTag("quiz_option_$index")
                            ) {
                                Row(
                                    modifier = Modifier
                                        .padding(16.dp)
                                        .fillMaxWidth(),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    RadioButton(
                                        selected = isSelected,
                                        onClick = { if (!hasSubmittedAnswer) selectedOptionIndex = index }
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = option,
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Medium)
                                    )
                                }
                            }
                        }

                        if (hasSubmittedAnswer) {
                            Spacer(modifier = Modifier.height(12.dp))
                            Surface(
                                shape = RoundedCornerShape(8.dp),
                                color = if (isAnswerCorrect == true) EmeraldSuccess.copy(alpha = 0.15f) else MaterialTheme.colorScheme.error.copy(alpha = 0.15f)
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = if (isAnswerCorrect == true) Icons.Default.CheckCircle else Icons.Default.ErrorOutline,
                                        contentDescription = null,
                                        tint = if (isAnswerCorrect == true) EmeraldSuccess else MaterialTheme.colorScheme.error
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column {
                                        Text(
                                            text = if (isAnswerCorrect == true) "Correct! Great job! 🎉" else "Not quite right. Try again!",
                                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.Bold),
                                            color = if (isAnswerCorrect == true) EmeraldSuccess else MaterialTheme.colorScheme.error
                                        )
                                        if (explanation.isNotBlank()) {
                                            Spacer(modifier = Modifier.height(2.dp))
                                            Text(
                                                text = explanation,
                                                style = MaterialTheme.typography.bodySmall,
                                                color = MaterialTheme.colorScheme.onSurface
                                            )
                                        }
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))

                        if (!hasSubmittedAnswer) {
                            Button(
                                onClick = {
                                    if (selectedOptionIndex != null) {
                                        hasSubmittedAnswer = true
                                        isAnswerCorrect = selectedOptionIndex == currentQuestion.correctIndex
                                    }
                                },
                                enabled = selectedOptionIndex != null,
                                modifier = Modifier.fillMaxWidth().testTag("btn_submit_answer"),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Submit Answer", fontWeight = FontWeight.Bold)
                            }
                        } else if (isAnswerCorrect == false) {
                            OutlinedButton(
                                onClick = {
                                    hasSubmittedAnswer = false
                                    selectedOptionIndex = null
                                    isAnswerCorrect = null
                                },
                                modifier = Modifier.fillMaxWidth().testTag("btn_retry_answer"),
                                shape = RoundedCornerShape(10.dp)
                            ) {
                                Text("Retry", fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
                Spacer(modifier = Modifier.height(16.dp))
            }

            // Reward info
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier.padding(16.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(imageVector = Icons.Default.Star, contentDescription = null, tint = StarGoldPrimary)
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Reward upon completion: +${currentStep?.xpReward ?: 15} XP & ${currentStep?.starsReward ?: 1} Star",
                        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Bold)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            Button(
                onClick = {
                    if (currentStep != null && !isSubmitting && !hasCompleted && canComplete) {
                        isSubmitting = true
                        coroutineScope.launch {
                            val result = progressRepository.recordStepCompletion(
                                childId = childId,
                                parentId = parentId,
                                subjectId = subjectId,
                                step = currentStep,
                                allStepsInLesson = allSteps
                            )
                            isSubmitting = false
                            if (result.isSuccess) {
                                hasCompleted = true
                                onStepCompleted()
                            }
                        }
                    }
                },
                enabled = !isSubmitting && currentStep != null && canComplete,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(54.dp)
                    .testTag("btn_complete_step"),
                shape = RoundedCornerShape(14.dp)
            ) {
                if (isSubmitting) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(24.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(
                        text = if (hasCompleted) "Completed! ⭐" else stringResource(R.string.step_completed),
                        fontSize = 16.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }
    }
}
