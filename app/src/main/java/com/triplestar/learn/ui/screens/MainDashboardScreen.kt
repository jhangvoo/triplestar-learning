package com.triplestar.learn.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.horizontalScroll
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.automirrored.filled.MenuBook
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.AutoStories
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.Computer
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Palette
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Public
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Science
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timeline
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilterChip
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import com.triplestar.learn.data.model.BadgeUnlockStatus
import com.triplestar.learn.ui.components.BadgeDetailDialog
import com.triplestar.learn.ui.components.BadgeGridItem
import com.triplestar.learn.util.BadgeManager
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.triplestar.learn.R
import com.triplestar.learn.data.model.AdPlacementLocation
import com.triplestar.learn.data.model.ChildProfile
import com.triplestar.learn.data.model.ChildProgress
import com.triplestar.learn.data.model.Subject
import com.triplestar.learn.data.repository.AdRepository
import com.triplestar.learn.ui.components.FixedAdBanner
import com.triplestar.learn.ui.components.LanguageToggle
import com.triplestar.learn.ui.components.EnglishAlphabetWritingStudio
import com.triplestar.learn.ui.components.UrduAlphabetWritingStudio
import com.triplestar.learn.ui.components.ScienceNatureExplorer
import com.triplestar.learn.ui.components.MathExplorerStudio
import com.triplestar.learn.ui.components.ComputerLogicStudio
import com.triplestar.learn.ui.components.StarsPill
import com.triplestar.learn.ui.components.StreakPill
import com.triplestar.learn.ui.components.XpPill
import com.triplestar.learn.ui.theme.AstroBluePrimary
import com.triplestar.learn.ui.theme.EmeraldSuccess
import com.triplestar.learn.ui.theme.StarGoldPrimary
import com.triplestar.learn.util.LocaleManager
import com.triplestar.learn.util.StreakManager
import androidx.compose.ui.platform.LocalContext

@Composable
fun MainDashboardScreen(
    child: ChildProfile,
    progress: ChildProgress?,
    mathProgress: ChildProgress? = null,
    englishProgress: ChildProgress? = progress,
    subjects: List<Subject>,
    adRepository: AdRepository,
    onSwitchLearner: () -> Unit,
    onNavigateToParentSettings: () -> Unit,
    onSubjectClick: (String) -> Unit = {},
    modifier: Modifier = Modifier
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    var showParentGate by remember { mutableStateOf(false) }

    if (showParentGate) {
        ParentGateDialog(
            onDismiss = { showParentGate = false },
            onVerified = {
                showParentGate = false
                onNavigateToParentSettings()
            }
        )
    }

    Scaffold(
        bottomBar = {
            NavigationBar(
                modifier = Modifier.testTag("main_bottom_nav"),
                containerColor = MaterialTheme.colorScheme.surface
            ) {
                NavigationBarItem(
                    selected = selectedTab == 0,
                    onClick = { selectedTab = 0 },
                    icon = { Icon(Icons.Default.School, contentDescription = null) },
                    label = { Text(stringResource(R.string.nav_learn)) },
                    modifier = Modifier.testTag("nav_tab_learn")
                )
                NavigationBarItem(
                    selected = selectedTab == 1,
                    onClick = { selectedTab = 1 },
                    icon = { Icon(Icons.Default.AutoAwesome, contentDescription = null) },
                    label = { Text(stringResource(R.string.nav_practice)) },
                    modifier = Modifier.testTag("nav_tab_practice")
                )
                NavigationBarItem(
                    selected = selectedTab == 2,
                    onClick = { selectedTab = 2 },
                    icon = { Icon(Icons.Default.Timeline, contentDescription = null) },
                    label = { Text(stringResource(R.string.nav_progress)) },
                    modifier = Modifier.testTag("nav_tab_progress")
                )
                NavigationBarItem(
                    selected = selectedTab == 3,
                    onClick = { selectedTab = 3 },
                    icon = { Icon(Icons.Default.Person, contentDescription = null) },
                    label = { Text(stringResource(R.string.nav_profile)) },
                    modifier = Modifier.testTag("nav_tab_profile")
                )
            }
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .background(MaterialTheme.colorScheme.background)
        ) {
            // Dashboard Top App Header
            DashboardTopBar(
                child = child,
                onSwitchLearner = onSwitchLearner,
                onOpenParentSettings = { showParentGate = true }
            )

            when (selectedTab) {
                0 -> LearnTabContent(
                    child = child,
                    progress = progress,
                    mathProgress = mathProgress,
                    englishProgress = englishProgress,
                    subjects = subjects,
                    adRepository = adRepository,
                    onSubjectClick = onSubjectClick
                )
                1 -> PracticeTabContent(adRepository = adRepository)
                2 -> ProgressTabContent(
                    child = child,
                    mathProgress = mathProgress,
                    englishProgress = englishProgress,
                    progress = progress
                )
                3 -> ProfileTabContent(
                    child = child,
                    progress = progress,
                    mathProgress = mathProgress,
                    englishProgress = englishProgress,
                    onSwitchLearner = onSwitchLearner
                )
            }
        }
    }
}

@Composable
private fun DashboardTopBar(
    child: ChildProfile,
    onSwitchLearner: () -> Unit,
    onOpenParentSettings: () -> Unit
) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .background(MaterialTheme.colorScheme.surface)
            .padding(horizontal = 16.dp, vertical = 10.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(StarGoldPrimary.copy(alpha = 0.2f))
                    .clickable(onClick = onSwitchLearner),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Face,
                    contentDescription = null,
                    tint = AstroBluePrimary,
                    modifier = Modifier.size(24.dp)
                )
            }
            Spacer(modifier = Modifier.width(10.dp))
            Column {
                Text(
                    text = stringResource(R.string.dashboard_greeting, child.displayName),
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                    color = MaterialTheme.colorScheme.onSurface
                )
                Text(
                    text = stringResource(R.string.child_switch_profile),
                    style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                    color = MaterialTheme.colorScheme.primary,
                    modifier = Modifier.clickable(onClick = onSwitchLearner)
                )
            }
        }

        Row(verticalAlignment = Alignment.CenterVertically) {
            LanguageToggle()
            Spacer(modifier = Modifier.width(6.dp))
            IconButton(
                onClick = onOpenParentSettings,
                modifier = Modifier.testTag("btn_parent_gate_top")
            ) {
                Icon(
                    imageVector = Icons.Default.Settings,
                    contentDescription = stringResource(R.string.parent_settings_title),
                    tint = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }
    }
}

@Composable
private fun LearnTabContent(
    child: ChildProfile,
    progress: ChildProgress?,
    mathProgress: ChildProgress? = null,
    englishProgress: ChildProgress? = progress,
    subjects: List<Subject>,
    adRepository: AdRepository,
    onSubjectClick: (String) -> Unit
) {
    val currentLang by LocaleManager.currentLanguage.collectAsState()
    val combinedStars = (mathProgress?.totalStars ?: 0) + (englishProgress?.totalStars ?: (progress?.totalStars ?: 0))
    val combinedXp = (mathProgress?.totalXp ?: 0) + (englishProgress?.totalXp ?: (progress?.totalXp ?: 0))

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .testTag("dashboard_learn_content"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        // Real-Time Verified Gamification Stats
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween
            ) {
                
                val context = LocalContext.current
                val currentStreak = remember { StreakManager.getStreak(context) }
                StreakPill(streakDays = currentStreak, modifier = Modifier.weight(1f))

                Spacer(modifier = Modifier.width(8.dp))
                StarsPill(stars = combinedStars, modifier = Modifier.weight(1f))
                Spacer(modifier = Modifier.width(8.dp))
                XpPill(xp = combinedXp, modifier = Modifier.weight(1f))
            }
        }

        // Today's Learning Quest
        item {
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("card_todays_quest")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = stringResource(R.string.dashboard_today_title),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "${child.dailyTargetMinutes}m goal",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = stringResource(R.string.dashboard_today_desc),
                        style = MaterialTheme.typography.bodySmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    LinearProgressIndicator(
                        progress = { 0.1f },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(8.dp)
                            .clip(RoundedCornerShape(4.dp)),
                        color = StarGoldPrimary,
                        trackColor = MaterialTheme.colorScheme.surfaceVariant
                    )
                }
            }
        }

        // Educational Categories Header
        item {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(top = 4.dp, bottom = 2.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = stringResource(R.string.dashboard_categories_header),
                        style = MaterialTheme.typography.titleLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 20.sp
                        ),
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.spacedBy(4.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Default.AutoAwesome,
                            contentDescription = null,
                            tint = StarGoldPrimary,
                            modifier = Modifier.size(18.dp)
                        )
                        Text(
                            text = "${subjects.size} Subjects",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                }
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = stringResource(R.string.dashboard_categories_sub),
                    style = MaterialTheme.typography.bodySmall,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )
            }
        }

        // Educational Category Cards List
        items(subjects, key = { it.id }) { subject ->
            val title = if (currentLang == LocaleManager.LANG_URDU) subject.titleUr else subject.titleEn
            val subtitle = if (currentLang == LocaleManager.LANG_URDU) subject.subtitleUr else subject.subtitleEn
            SubjectCard(
                subject = subject,
                displayTitle = title,
                displaySubtitle = subtitle,
                onClick = { onSubjectClick(subject.id) }
            )
        }

        // Empty state when progress is null or completely fresh
        if (progress == null || (progress.completedStepIds.isEmpty() && progress.totalStars == 0)) {
            item {
                Card(
                    colors = CardDefaults.cardColors(
                        containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(16.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("empty_progress_card")
                ) {
                    Column(
                        modifier = Modifier.padding(16.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = stringResource(R.string.dashboard_empty_progress_title),
                            style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                            color = MaterialTheme.colorScheme.primary
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = stringResource(R.string.dashboard_empty_progress_desc),
                            style = MaterialTheme.typography.bodySmall,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        // Fixed Ad Placement Container (Families Policy compliant)
        item {
            FixedAdBanner(
                config = adRepository.getFixedPlacement(AdPlacementLocation.DASHBOARD_BOTTOM)
            )
        }
    }
}

@Composable
private fun SubjectCard(
    subject: Subject,
    displayTitle: String,
    displaySubtitle: String = "",
    onClick: () -> Unit
) {
    val categoryColor = remember(subject.id, subject.colorHex) {
        getSubjectColor(subject.id, subject.colorHex)
    }
    val categoryIcon = remember(subject.id, subject.iconName) {
        getSubjectIcon(subject.id, subject.iconName)
    }

    // Color-coded background tint for each educational category card with rich vibrancy
    val cardBackground = categoryColor.copy(alpha = 0.14f)
    val cardBorder = categoryColor.copy(alpha = 0.35f)

    Card(
        colors = CardDefaults.cardColors(containerColor = cardBackground),
        border = BorderStroke(2.dp, cardBorder),
        shape = RoundedCornerShape(24.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("subject_card_${subject.id}")
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(18.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Category Icon Badge with rich color-coded container
            Box(
                modifier = Modifier
                    .size(60.dp)
                    .clip(RoundedCornerShape(20.dp))
                    .background(categoryColor.copy(alpha = 0.22f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = categoryIcon,
                    contentDescription = displayTitle,
                    tint = categoryColor,
                    modifier = Modifier.size(32.dp)
                )
            }

            Spacer(modifier = Modifier.width(16.dp))

            // Category Details
            Column(modifier = Modifier.weight(1f)) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // Category Badge Pill
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(categoryColor.copy(alpha = 0.2f))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Text(
                            text = displayTitle.uppercase(),
                            style = MaterialTheme.typography.labelSmall.copy(
                                fontWeight = FontWeight.Bold,
                                fontSize = 11.sp
                            ),
                            color = categoryColor
                        )
                    }

                    Text(
                        text = "•",
                        style = MaterialTheme.typography.labelSmall,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f)
                    )

                    Text(
                        text = stringResource(R.string.step_available),
                        style = MaterialTheme.typography.labelSmall.copy(fontWeight = FontWeight.SemiBold),
                        color = EmeraldSuccess
                    )
                }

                Spacer(modifier = Modifier.height(6.dp))

                Text(
                    text = displayTitle,
                    style = MaterialTheme.typography.titleMedium.copy(
                        fontWeight = FontWeight.Bold,
                        fontSize = 18.sp
                    ),
                    color = MaterialTheme.colorScheme.onSurface
                )

                if (displaySubtitle.isNotBlank()) {
                    Spacer(modifier = Modifier.height(2.dp))
                    Text(
                        text = displaySubtitle,
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 13.sp),
                        color = MaterialTheme.colorScheme.onSurfaceVariant,
                        maxLines = 1
                    )
                }
            }

            Spacer(modifier = Modifier.width(8.dp))

            // Action Chevron Pill with category color tint
            Box(
                modifier = Modifier
                    .size(42.dp)
                    .clip(CircleShape)
                    .background(categoryColor.copy(alpha = 0.2f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                    contentDescription = null,
                    tint = categoryColor,
                    modifier = Modifier.size(20.dp)
                )
            }
        }
    }
}

private fun getSubjectColor(subjectId: String, fallbackHex: Long): Color {
    return when (subjectId.lowercase()) {
        "english" -> Color(0xFFF43F5E) // Vibrant Coral Rose
        "math" -> Color(0xFF2563EB)    // Vibrant Royal Astro Blue
        "science" -> Color(0xFF10B981) // Vibrant Fresh Emerald Green
        "art" -> Color(0xFF8B5CF6)     // Vibrant Creative Violet
        "computer" -> Color(0xFF0284C7)// Vibrant Tech Sky Blue
        "urdu" -> Color(0xFFF59E0B)    // Warm Golden Amber
        "general_knowledge" -> Color(0xFF0D9488) // Global Teal
        else -> Color(fallbackHex)
    }
}

private fun getSubjectIcon(subjectId: String, iconName: String): ImageVector {
    return when {
        subjectId.equals("english", ignoreCase = true) || iconName == "menu_book" -> Icons.AutoMirrored.Filled.MenuBook
        subjectId.equals("math", ignoreCase = true) || iconName == "calculate" -> Icons.Default.Calculate
        subjectId.equals("science", ignoreCase = true) || iconName == "science" -> Icons.Default.Science
        subjectId.equals("art", ignoreCase = true) || iconName == "palette" -> Icons.Default.Palette
        subjectId.equals("computer", ignoreCase = true) || iconName == "computer" -> Icons.Default.Computer
        subjectId.equals("urdu", ignoreCase = true) || iconName == "book_open" -> Icons.Default.AutoStories
        subjectId.equals("general_knowledge", ignoreCase = true) || iconName == "globe" -> Icons.Default.Public
        else -> Icons.AutoMirrored.Filled.MenuBook
    }
}

@Composable
private fun PracticeTabContent(adRepository: AdRepository) {
    var selectedStudio by remember { mutableIntStateOf(0) } // 0 = Urdu, 1 = English

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(horizontal = 16.dp)
            .testTag("tab_practice_content"),
        contentPadding = PaddingValues(vertical = 16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .horizontalScroll(rememberScrollState()),
                horizontalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                FilterChip(
                    selected = selectedStudio == 0,
                    onClick = { selectedStudio = 0 },
                    label = {
                        Text(
                            text = "اردو حروف (ا تا ے)",
                            fontWeight = if (selectedStudio == 0) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    modifier = Modifier.testTag("chip_practice_urdu")
                )
                FilterChip(
                    selected = selectedStudio == 1,
                    onClick = { selectedStudio = 1 },
                    label = {
                        Text(
                            text = "English (A–Z)",
                            fontWeight = if (selectedStudio == 1) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    modifier = Modifier.testTag("chip_practice_english")
                )
                FilterChip(
                    selected = selectedStudio == 2,
                    onClick = { selectedStudio = 2 },
                    label = {
                        Text(
                            text = "🌿 Science & Nature",
                            fontWeight = if (selectedStudio == 2) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    modifier = Modifier.testTag("chip_practice_science")
                )
                FilterChip(
                    selected = selectedStudio == 3,
                    onClick = { selectedStudio = 3 },
                    label = {
                        Text(
                            text = "🔢 Math (1–20 & Puzzles)",
                            fontWeight = if (selectedStudio == 3) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    modifier = Modifier.testTag("chip_practice_math")
                )
                FilterChip(
                    selected = selectedStudio == 4,
                    onClick = { selectedStudio = 4 },
                    label = {
                        Text(
                            text = "💻 Computer & Logic",
                            fontWeight = if (selectedStudio == 4) FontWeight.Bold else FontWeight.Medium
                        )
                    },
                    modifier = Modifier.testTag("chip_practice_computer")
                )
            }
        }

        item {
            when (selectedStudio) {
                0 -> UrduAlphabetWritingStudio()
                1 -> EnglishAlphabetWritingStudio()
                2 -> ScienceNatureExplorer()
                3 -> MathExplorerStudio()
                4 -> ComputerLogicStudio()
            }
        }

        item {
            FixedAdBanner(
                config = adRepository.getFixedPlacement(AdPlacementLocation.PRACTICE_SUMMARY_BOTTOM)
            )
        }
    }
}

@Composable
private fun ProgressTabContent(
    child: ChildProfile,
    mathProgress: ChildProgress?,
    englishProgress: ChildProgress?,
    progress: ChildProgress?
) {
    val totalStars = (mathProgress?.totalStars ?: 0) + (englishProgress?.totalStars ?: (progress?.totalStars ?: 0))
    val totalXp = (mathProgress?.totalXp ?: 0) + (englishProgress?.totalXp ?: (progress?.totalXp ?: 0))
    val totalCompletedSteps = (mathProgress?.completedStepIds?.size ?: 0) + (englishProgress?.completedStepIds?.size ?: (progress?.completedStepIds?.size ?: 0))

    var selectedFilter by remember { mutableStateOf("all") }
    var selectedBadgeStatus by remember { mutableStateOf<BadgeUnlockStatus?>(null) }

    val badgeStatuses = remember(mathProgress, englishProgress, progress) {
        BadgeManager.evaluateBadges(
            mathProgress = mathProgress,
            englishProgress = englishProgress ?: progress
        )
    }

    val filteredBadges = remember(badgeStatuses, selectedFilter) {
        when (selectedFilter) {
            "math" -> badgeStatuses.filter { it.badge.subjectId == "math" }
            "english" -> badgeStatuses.filter { it.badge.subjectId == "english" }
            else -> badgeStatuses
        }
    }

    val unlockedCount = badgeStatuses.count { it.isUnlocked }
    val totalCount = badgeStatuses.size

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp)
            .verticalScroll(rememberScrollState())
            .testTag("tab_progress_content")
    ) {
        Text(
            text = stringResource(R.string.nav_progress),
            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold)
        )
        Spacer(modifier = Modifier.height(16.dp))

        // Learner Overview Card
        Card(
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            shape = RoundedCornerShape(20.dp),
            elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("card_learner_progress_overview")
        ) {
            Column(modifier = Modifier.padding(20.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(54.dp)
                            .clip(CircleShape)
                            .background(StarGoldPrimary.copy(alpha = 0.2f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Face,
                            contentDescription = null,
                            tint = AstroBluePrimary,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(14.dp))
                    Column {
                        Text(
                            text = child.displayName,
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold)
                        )
                        Text(
                            text = "Age ${child.age} • ${child.learningLevel} • ${child.dailyTargetMinutes}m Daily Goal",
                            style = MaterialTheme.typography.bodySmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }

                Spacer(modifier = Modifier.height(18.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween
                ) {
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                        Text(
                            text = "$totalStars ⭐",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = StarGoldPrimary
                        )
                        Text(
                            text = "Total Stars",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                        Text(
                            text = "$totalXp",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = AstroBluePrimary
                        )
                        Text(
                            text = "Total XP",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                    Column(horizontalAlignment = Alignment.CenterHorizontally, modifier = Modifier.weight(1f)) {
                        Text(
                            text = "$totalCompletedSteps",
                            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                            color = EmeraldSuccess
                        )
                        Text(
                            text = "Milestones",
                            style = MaterialTheme.typography.labelSmall,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(24.dp))

        // Badges & Virtual Stickers Section
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.SpaceBetween,
            verticalAlignment = Alignment.CenterVertically
        ) {
            Text(
                text = stringResource(R.string.badges_title),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Text(
                text = stringResource(R.string.badges_collected_count, unlockedCount, totalCount),
                style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                color = MaterialTheme.colorScheme.primary
            )
        }

        Spacer(modifier = Modifier.height(6.dp))
        Text(
            text = stringResource(R.string.badges_subtitle),
            style = MaterialTheme.typography.bodySmall,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )

        Spacer(modifier = Modifier.height(12.dp))

        // Filter Chips
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            FilterChip(
                selected = selectedFilter == "all",
                onClick = { selectedFilter = "all" },
                label = { Text(stringResource(R.string.badges_filter_all)) },
                modifier = Modifier.testTag("filter_chip_all")
            )
            FilterChip(
                selected = selectedFilter == "math",
                onClick = { selectedFilter = "math" },
                label = { Text(stringResource(R.string.badges_filter_math)) },
                modifier = Modifier.testTag("filter_chip_math")
            )
            FilterChip(
                selected = selectedFilter == "english",
                onClick = { selectedFilter = "english" },
                label = { Text(stringResource(R.string.badges_filter_english)) },
                modifier = Modifier.testTag("filter_chip_english")
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Badge Grid
        Column(
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .testTag("progress_badge_grid")
        ) {
            val chunks = filteredBadges.chunked(2)
            chunks.forEach { rowItems ->
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    rowItems.forEach { status ->
                        Box(modifier = Modifier.weight(1f)) {
                            BadgeGridItem(
                                status = status,
                                onClick = { selectedBadgeStatus = status }
                            )
                        }
                    }
                    if (rowItems.size == 1) {
                        Spacer(modifier = Modifier.weight(1f))
                    }
                }
            }
        }

        selectedBadgeStatus?.let { status ->
            BadgeDetailDialog(
                status = status,
                onDismiss = { selectedBadgeStatus = null }
            )
        }

        Spacer(modifier = Modifier.height(32.dp))
    }
}

@Composable
private fun ProfileTabContent(
    child: ChildProfile,
    progress: ChildProgress?,
    mathProgress: ChildProgress?,
    englishProgress: ChildProgress?,
    onSwitchLearner: () -> Unit
) {
    val context = androidx.compose.ui.platform.LocalContext.current
    val childRepository = remember { com.triplestar.learn.data.repository.ChildProfileRepository(context) }
    ProfileScreen(
        child = child,
        progress = progress,
        mathProgress = mathProgress,
        englishProgress = englishProgress ?: progress,
        childRepository = childRepository,
        onSwitchLearner = onSwitchLearner,
        modifier = Modifier.testTag("tab_profile_content")
    )
}
