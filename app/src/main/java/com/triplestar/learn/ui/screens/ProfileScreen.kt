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
import androidx.compose.material.icons.filled.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import com.triplestar.learn.R
import com.triplestar.learn.data.model.ChildProfile
import com.triplestar.learn.data.model.ChildProgress
import com.triplestar.learn.data.model.BadgeUnlockStatus
import com.triplestar.learn.data.repository.ChildProfileRepository
import com.triplestar.learn.ui.components.BadgeGridItem
import com.triplestar.learn.ui.components.BadgeDetailDialog
import com.triplestar.learn.util.BadgeManager
import com.triplestar.learn.ui.theme.*
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ProfileScreen(
    child: ChildProfile,
    progress: ChildProgress?,
    childRepository: ChildProfileRepository,
    onSwitchLearner: () -> Unit,
    onBack: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var displayName by remember(child.displayName) { mutableStateOf(child.displayName) }
    var selectedAvatar by remember(child.avatarId) { mutableStateOf(child.avatarId) }
    var isSaving by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    val avatars = listOf("avatar_star", "avatar_explorer", "avatar_hero", "avatar_rocket", "avatar_crown")

    Scaffold(
        snackbarHost = { SnackbarHost(snackbarHostState) },
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.nav_profile), fontWeight = FontWeight.Bold) },
                navigationIcon = {
                    if (onBack != null) {
                        IconButton(onClick = onBack, modifier = Modifier.testTag("btn_back")) {
                            Icon(imageVector = Icons.Default.ArrowBack, contentDescription = stringResource(R.string.action_back))
                        }
                    }
                },
                actions = {
                    TextButton(onClick = onSwitchLearner, modifier = Modifier.testTag("btn_switch_learner")) {
                        Icon(imageVector = Icons.Default.SwapHoriz, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(stringResource(R.string.child_switch_profile))
                    }
                }
            )
        },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(20.dp)
                .verticalScroll(rememberScrollState())
                .testTag("profile_screen_content"),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Avatar Selector Section
            Text(
                text = stringResource(R.string.child_avatar_label),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                modifier = Modifier.align(Alignment.Start)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth(),
                verticalAlignment = Alignment.CenterVertically
            ) {
                avatars.forEach { avatar ->
                    val isSelected = selectedAvatar == avatar
                    val iconVector = when (avatar) {
                        "avatar_star" -> Icons.Default.Star
                        "avatar_explorer" -> Icons.Default.Explore
                        "avatar_hero" -> Icons.Default.EmojiEvents
                        "avatar_rocket" -> Icons.Default.RocketLaunch
                        else -> Icons.Default.WorkspacePremium
                    }
                    Box(
                        modifier = Modifier
                            .size(56.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) StarGoldPrimary.copy(alpha = 0.3f)
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .border(
                                width = if (isSelected) 2.5.dp else 1.dp,
                                color = if (isSelected) StarGoldPrimary else MaterialTheme.colorScheme.outlineVariant,
                                shape = CircleShape
                            )
                            .clickable { selectedAvatar = avatar }
                            .testTag("avatar_option_$avatar"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = iconVector,
                            contentDescription = null,
                            tint = if (isSelected) AstroBluePrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(28.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Display Name Edit Section
            OutlinedTextField(
                value = displayName,
                onValueChange = { displayName = it },
                label = { Text(stringResource(R.string.child_name_label)) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_edit_display_name")
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    if (displayName.isBlank()) {
                        coroutineScope.launch { snackbarHostState.showSnackbar("Display name cannot be empty") }
                        return@Button
                    }
                    isSaving = true
                    coroutineScope.launch {
                        val updatedProfile = child.copy(
                            displayName = displayName.trim(),
                            avatarId = selectedAvatar,
                            updatedAt = com.google.firebase.Timestamp.now()
                        )
                        val result = childRepository.createChildProfile(updatedProfile)
                        isSaving = false
                        if (result.isSuccess) {
                            snackbarHostState.showSnackbar("Profile updated successfully! ⭐")
                        } else {
                            snackbarHostState.showSnackbar(result.exceptionOrNull()?.localizedMessage ?: "Failed to update profile")
                        }
                    }
                },
                enabled = !isSaving,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btn_save_profile")
            ) {
                if (isSaving) {
                    CircularProgressIndicator(modifier = Modifier.size(20.dp), color = MaterialTheme.colorScheme.onPrimary)
                } else {
                    Text(stringResource(R.string.action_save))
                }
            }

            Spacer(modifier = Modifier.height(28.dp))

            // Learning Streaks & Stats Card
            Card(
                colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                shape = RoundedCornerShape(16.dp),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("card_learning_streaks")
            ) {
                Column(modifier = Modifier.padding(20.dp)) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(imageVector = Icons.Default.LocalFireDepartment, contentDescription = null, tint = CoralFun, modifier = Modifier.size(32.dp))
                            Spacer(modifier = Modifier.width(8.dp))
                            Column {
                                Text(
                                    text = "${progress?.streakDays ?: 0} Day Streak",
                                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                                    color = MaterialTheme.colorScheme.onSurface
                                )
                                Text(
                                    text = "Keep learning daily!",
                                    style = MaterialTheme.typography.bodySmall,
                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                )
                            }
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(12.dp))
                                .background(StarGoldLight.copy(alpha = 0.4f))
                                .padding(horizontal = 12.dp, vertical = 6.dp)
                        ) {
                            Text(
                                text = "${progress?.totalStars ?: 0} ⭐",
                                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold),
                                color = StarGoldDark
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))
                    HorizontalDivider(color = MaterialTheme.colorScheme.outlineVariant)
                    Spacer(modifier = Modifier.height(16.dp))

                    Row(
                        horizontalArrangement = Arrangement.SpaceAround,
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "${progress?.totalXp ?: 0}", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold), color = AstroBluePrimary)
                            Text(text = "Total XP", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "${progress?.completedStepIds?.size ?: 0}", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold), color = EmeraldSuccess)
                            Text(text = "Milestones", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Text(text = "${child.dailyTargetMinutes}m", style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold), color = PurpleMagic)
                            Text(text = "Daily Goal", style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Badges & Virtual Stickers Section
            var selectedFilter by remember { mutableStateOf("all") }
            var selectedBadgeStatus by remember { mutableStateOf<BadgeUnlockStatus?>(null) }

            val badgeStatuses = remember(progress) {
                BadgeManager.evaluateBadges(progress, progress)
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

            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = stringResource(R.string.badges_subtitle),
                style = MaterialTheme.typography.bodySmall,
                color = MaterialTheme.colorScheme.onSurfaceVariant,
                modifier = Modifier.align(Alignment.Start)
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
                modifier = Modifier.fillMaxWidth().testTag("badge_grid")
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
}

