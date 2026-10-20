package com.triplestar.learn.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Settings
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Timeline
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

        // Subjects Title
        item {
            Text(
                text = stringResource(R.string.dashboard_subjects_title),
                style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                color = MaterialTheme.colorScheme.onBackground
            )
        }

        // Subjects List
        items(subjects, key = { it.id }) { subject ->
            val title = if (currentLang == LocaleManager.LANG_URDU) subject.titleUr else subject.titleEn
            SubjectCard(
                subject = subject,
                displayTitle = title,
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
    onClick: () -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .testTag("subject_card_${subject.id}")
            .clickable(onClick = onClick)
    ) {
        Row(
            modifier = Modifier.padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(48.dp)
                    .clip(CircleShape)
                    .background(AstroBluePrimary.copy(alpha = 0.1f)),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.MenuBook,
                    contentDescription = null,
                    tint = AstroBluePrimary,
                    modifier = Modifier.size(26.dp)
                )
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(
                    text = displayTitle,
                    style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                )
                Text(
                    text = stringResource(R.string.step_available),
                    style = MaterialTheme.typography.labelSmall,
                    color = EmeraldSuccess
                )
            }
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = StarGoldPrimary,
                modifier = Modifier.size(20.dp)
            )
        }
    }
}

@Composable
private fun PracticeTabContent(adRepository: AdRepository) {
    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(20.dp)
            .testTag("tab_practice_content"),
        verticalArrangement = Arrangement.SpaceBetween
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 40.dp)
        ) {
            Icon(
                imageVector = Icons.Default.AutoAwesome,
                contentDescription = null,
                tint = StarGoldPrimary,
                modifier = Modifier.size(56.dp)
            )
            Spacer(modifier = Modifier.height(16.dp))
            Text(
                text = stringResource(R.string.nav_practice),
                style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.step_lock_desc),
                style = MaterialTheme.typography.bodyMedium,
                textAlign = TextAlign.Center,
                color = MaterialTheme.colorScheme.onSurfaceVariant
            )
        }

        FixedAdBanner(
            config = adRepository.getFixedPlacement(AdPlacementLocation.PRACTICE_SUMMARY_BOTTOM)
        )
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
