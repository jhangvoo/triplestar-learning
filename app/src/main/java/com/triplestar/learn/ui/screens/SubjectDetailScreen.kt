package com.triplestar.learn.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.PlayArrow
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.triplestar.learn.R
import com.triplestar.learn.data.model.AdPlacementLocation
import com.triplestar.learn.data.model.ChildProgress
import com.triplestar.learn.data.model.Lesson
import com.triplestar.learn.data.model.Step
import com.triplestar.learn.data.model.StepStatus
import com.triplestar.learn.data.repository.AdRepository
import com.triplestar.learn.data.repository.CurriculumRepository
import com.triplestar.learn.data.repository.ProgressRepository
import com.triplestar.learn.ui.components.FixedAdBanner
import com.triplestar.learn.ui.theme.AstroBluePrimary
import com.triplestar.learn.ui.theme.EmeraldSuccess
import com.triplestar.learn.ui.theme.StarGoldPrimary
import com.triplestar.learn.util.LocaleManager
import com.triplestar.learn.util.StepLockEngine

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun SubjectDetailScreen(
    childId: String,
    subjectId: String,
    curriculumRepository: CurriculumRepository,
    progressRepository: ProgressRepository,
    adRepository: AdRepository,
    onStepClick: (lessonId: String, stepId: String) -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    val currentLang by LocaleManager.currentLanguage.collectAsState()
    val progress by progressRepository.observeProgress(childId, subjectId).collectAsState(initial = null)
    val lessons = remember(subjectId) { curriculumRepository.getLessonsForSubject(subjectId) }

    Scaffold(
        modifier = modifier.fillMaxSize().testTag("subject_detail_screen"),
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = subjectId.replaceFirstChar { it.uppercase() },
                        fontWeight = FontWeight.Bold
                    )
                },
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
        LazyColumn(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding),
            contentPadding = PaddingValues(16.dp),
            verticalArrangement = Arrangement.spacedBy(16.dp)
        ) {
            items(lessons, key = { it.id }) { lesson ->
                val lessonSteps = remember(lesson.id) { curriculumRepository.getStepsForLesson(lesson.id) }
                val title = if (currentLang == LocaleManager.LANG_URDU) lesson.titleUr else lesson.titleEn

                LessonCard(
                    lesson = lesson,
                    displayTitle = title,
                    steps = lessonSteps,
                    progress = progress,
                    onStepClick = { stepId -> onStepClick(lesson.id, stepId) },
                    currentLang = currentLang
                )
            }

            item {
                FixedAdBanner(
                    config = adRepository.getFixedPlacement(AdPlacementLocation.DASHBOARD_BOTTOM)
                )
            }
        }
    }
}

@Composable
private fun LessonCard(
    lesson: Lesson,
    displayTitle: String,
    steps: List<Step>,
    progress: ChildProgress?,
    onStepClick: (stepId: String) -> Unit,
    currentLang: String,
    modifier: Modifier = Modifier
) {
    Card(
        modifier = modifier.fillMaxWidth(),
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp)
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Text(
                text = displayTitle,
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onSurface
            )
            Spacer(modifier = Modifier.height(12.dp))

            steps.forEach { step ->
                val status = StepLockEngine.resolveStepStatus(step, steps, progress)
                val stepTitle = if (currentLang == LocaleManager.LANG_URDU) step.titleUr else step.titleEn
                val isClickable = status != StepStatus.LOCKED

                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 6.dp)
                        .clip(RoundedCornerShape(12.dp))
                        .background(
                            when (status) {
                                StepStatus.COMPLETED, StepStatus.MASTERED -> EmeraldSuccess.copy(alpha = 0.1f)
                                StepStatus.AVAILABLE, StepStatus.IN_PROGRESS -> AstroBluePrimary.copy(alpha = 0.1f)
                                else -> MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
                            }
                        )
                        .clickable(enabled = isClickable) { onStepClick(step.id) }
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(
                                when (status) {
                                    StepStatus.COMPLETED, StepStatus.MASTERED -> EmeraldSuccess
                                    StepStatus.AVAILABLE, StepStatus.IN_PROGRESS -> AstroBluePrimary
                                    else -> MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f)
                                }
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (status) {
                                StepStatus.COMPLETED, StepStatus.MASTERED -> Icons.Default.CheckCircle
                                StepStatus.AVAILABLE, StepStatus.IN_PROGRESS -> Icons.Default.PlayArrow
                                else -> Icons.Default.Lock
                            },
                            contentDescription = null,
                            tint = MaterialTheme.colorScheme.surface,
                            modifier = Modifier.size(20.dp)
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = stepTitle,
                            style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold)
                        )
                        Text(
                            text = when (status) {
                                StepStatus.COMPLETED -> stringResource(R.string.step_completed)
                                StepStatus.MASTERED -> stringResource(R.string.step_mastered)
                                StepStatus.AVAILABLE -> stringResource(R.string.step_available)
                                StepStatus.IN_PROGRESS -> stringResource(R.string.step_in_progress)
                                else -> stringResource(R.string.step_locked)
                            },
                            style = MaterialTheme.typography.labelSmall,
                            color = when (status) {
                                StepStatus.COMPLETED, StepStatus.MASTERED -> EmeraldSuccess
                                StepStatus.AVAILABLE, StepStatus.IN_PROGRESS -> AstroBluePrimary
                                else -> MaterialTheme.colorScheme.onSurfaceVariant
                            }
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = StarGoldPrimary,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "+${step.xpReward} XP",
                            style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.Bold),
                            color = StarGoldPrimary
                        )
                    }
                }
            }
        }
    }
}
