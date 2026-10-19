package com.triplestar.learn.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Face
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Button
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Slider
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableFloatStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.triplestar.learn.R
import com.triplestar.learn.data.model.ChildProfile
import com.triplestar.learn.data.model.LearningLevel
import com.triplestar.learn.data.repository.ChildProfileRepository
import com.triplestar.learn.ui.theme.AstroBluePrimary
import com.triplestar.learn.ui.theme.StarGoldPrimary
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CreateChildProfileScreen(
    parentId: String,
    repository: ChildProfileRepository,
    onProfileCreated: () -> Unit,
    onBack: () -> Unit,
    modifier: Modifier = Modifier
) {
    var displayName by remember { mutableStateOf("") }
    var ageSlider by remember { mutableFloatStateOf(8f) } // 5 to 14
    var selectedLevel by remember { mutableStateOf(LearningLevel.BEGINNER) }
    var dailyTargetMinutes by remember { mutableStateOf(20) }
    var selectedAvatar by remember { mutableStateOf("avatar_star") }
    var isSaving by remember { mutableStateOf(false) }

    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(stringResource(R.string.child_add_new)) },
                navigationIcon = {
                    IconButton(onClick = onBack, modifier = Modifier.testTag("btn_back")) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = stringResource(R.string.action_back)
                        )
                    }
                }
            )
        },
        snackbarHost = { SnackbarHost(snackbarHostState) },
        modifier = modifier
    ) { innerPadding ->
        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
                .padding(horizontal = 20.dp)
                .verticalScroll(rememberScrollState())
                .testTag("create_child_profile_form")
        ) {
            Spacer(modifier = Modifier.height(8.dp))

            // Avatar Selector
            Text(
                text = stringResource(R.string.child_avatar_label),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(12.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(16.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                val avatars = listOf("avatar_star", "avatar_explorer", "avatar_hero")
                avatars.forEach { avatar ->
                    val isSelected = selectedAvatar == avatar
                    Box(
                        modifier = Modifier
                            .size(60.dp)
                            .clip(CircleShape)
                            .background(
                                if (isSelected) StarGoldPrimary.copy(alpha = 0.3f)
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .border(
                                width = if (isSelected) 2.dp else 1.dp,
                                color = if (isSelected) StarGoldPrimary else MaterialTheme.colorScheme.outlineVariant,
                                shape = CircleShape
                            )
                            .clickable { selectedAvatar = avatar }
                            .testTag("avatar_$avatar"),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = if (avatar == "avatar_star") Icons.Default.Star else Icons.Default.Face,
                            contentDescription = null,
                            tint = if (isSelected) AstroBluePrimary else MaterialTheme.colorScheme.onSurfaceVariant,
                            modifier = Modifier.size(32.dp)
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Name Field
            OutlinedTextField(
                value = displayName,
                onValueChange = { displayName = it },
                label = { Text(stringResource(R.string.child_name_label)) },
                placeholder = { Text(stringResource(R.string.child_name_hint)) },
                singleLine = true,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("input_child_name")
            )

            Spacer(modifier = Modifier.height(24.dp))

            // Age Slider (5 - 14)
            val currentAge = ageSlider.roundToInt()
            Text(
                text = "${stringResource(R.string.child_age_label)}: $currentAge",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Slider(
                value = ageSlider,
                onValueChange = {
                    ageSlider = it
                    val a = it.roundToInt()
                    selectedLevel = when {
                        a <= 7 -> LearningLevel.BEGINNER
                        a <= 10 -> LearningLevel.INTERMEDIATE
                        else -> LearningLevel.ADVANCED
                    }
                },
                valueRange = 5f..14f,
                steps = 8,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("slider_child_age")
            )

            Spacer(modifier = Modifier.height(16.dp))

            // Learning Level Selector
            Text(
                text = stringResource(R.string.child_level_label),
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                LearningLevel.entries.forEach { level ->
                    val isSelected = selectedLevel == level
                    val label = when (level) {
                        LearningLevel.BEGINNER -> stringResource(R.string.child_level_beginner)
                        LearningLevel.INTERMEDIATE -> stringResource(R.string.child_level_intermediate)
                        LearningLevel.ADVANCED -> stringResource(R.string.child_level_advanced)
                    }
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.primaryContainer
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.primary else MaterialTheme.colorScheme.outlineVariant,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { selectedLevel = level }
                            .padding(vertical = 10.dp, horizontal = 6.dp)
                            .testTag("level_${level.name}"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = label,
                            style = MaterialTheme.typography.labelSmall.copy(fontSize = 11.sp),
                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal,
                            color = if (isSelected) MaterialTheme.colorScheme.onPrimaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Daily Target (Minutes)
            Text(
                text = "${stringResource(R.string.child_daily_target_label)}: ${dailyTargetMinutes}m",
                style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
            )
            Spacer(modifier = Modifier.height(8.dp))
            Row(
                horizontalArrangement = Arrangement.spacedBy(12.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                listOf(15, 20, 30).forEach { mins ->
                    val isSelected = dailyTargetMinutes == mins
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(
                                if (isSelected) MaterialTheme.colorScheme.secondaryContainer
                                else MaterialTheme.colorScheme.surfaceVariant
                            )
                            .border(
                                width = if (isSelected) 1.5.dp else 1.dp,
                                color = if (isSelected) MaterialTheme.colorScheme.secondary else MaterialTheme.colorScheme.outlineVariant,
                                shape = RoundedCornerShape(12.dp)
                            )
                            .clickable { dailyTargetMinutes = mins }
                            .padding(vertical = 12.dp),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = "${mins} min",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                            color = if (isSelected) MaterialTheme.colorScheme.onSecondaryContainer else MaterialTheme.colorScheme.onSurfaceVariant
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(36.dp))

            // Save Button
            Button(
                onClick = {
                    val resolvedParentId = parentId.ifBlank {
                        try {
                            com.google.firebase.auth.FirebaseAuth.getInstance().currentUser?.uid ?: ""
                        } catch (e: Exception) {
                            ""
                        }
                    }
                    if (resolvedParentId.isBlank()) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Parent login required. Please sign in to add a child profile.")
                        }
                        return@Button
                    }
                    if (displayName.trim().isEmpty()) {
                        coroutineScope.launch {
                            snackbarHostState.showSnackbar("Please enter a child name or nickname")
                        }
                        return@Button
                    }
                    isSaving = true
                    coroutineScope.launch {
                        val newProfile = ChildProfile(
                            id = "",
                            parentId = resolvedParentId,
                            displayName = displayName.trim(),
                            avatarId = selectedAvatar,
                            age = currentAge,
                            learningLevel = selectedLevel.name,
                            dailyTargetMinutes = dailyTargetMinutes
                        )
                        val result = repository.createChildProfile(newProfile)
                        isSaving = false
                        if (result.isSuccess) {
                            onProfileCreated()
                        } else {
                            snackbarHostState.showSnackbar(
                                result.exceptionOrNull()?.localizedMessage ?: "Failed to save profile"
                            )
                        }
                    }
                },
                enabled = !isSaving,
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btn_submit_child_profile")
            ) {
                if (isSaving) {
                    CircularProgressIndicator(
                        modifier = Modifier.size(20.dp),
                        color = MaterialTheme.colorScheme.onPrimary
                    )
                } else {
                    Text(stringResource(R.string.child_create_button))
                }
            }

            Spacer(modifier = Modifier.height(24.dp))
        }
    }
}
