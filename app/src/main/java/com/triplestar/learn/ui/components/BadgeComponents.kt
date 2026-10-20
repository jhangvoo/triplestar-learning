package com.triplestar.learn.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.EmojiEvents
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import com.triplestar.learn.R
import com.triplestar.learn.data.model.Badge
import com.triplestar.learn.data.model.BadgeCategory
import com.triplestar.learn.data.model.BadgeUnlockStatus
import com.triplestar.learn.ui.theme.*
import com.triplestar.learn.util.LocaleManager

@Composable
fun BadgeGridItem(
    status: BadgeUnlockStatus,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val badge = status.badge
    val isUnlocked = status.isUnlocked
    val currentLang by LocaleManager.currentLanguage.collectAsState()
    val title = if (currentLang == LocaleManager.LANG_URDU) badge.titleUr.ifEmpty { badge.title } else badge.titleEn.ifEmpty { badge.title }

    // Delightful scale-up spring animation effect when unlocked
    val scaleAnim by animateFloatAsState(
        targetValue = if (isUnlocked) 1f else 0.9f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "badgeScaleAnim"
    )

    Card(
        colors = CardDefaults.cardColors(
            containerColor = if (isUnlocked) MaterialTheme.colorScheme.surface else MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.4f)
        ),
        shape = RoundedCornerShape(16.dp),
        elevation = CardDefaults.cardElevation(defaultElevation = if (isUnlocked) 4.dp else 1.dp),
        modifier = modifier
            .fillMaxWidth()
            .graphicsLayer(scaleX = scaleAnim, scaleY = scaleAnim)
            .clickable(onClick = onClick)
            .testTag("badge_item_${badge.id}")
    ) {
        Column(
            modifier = Modifier
                .padding(12.dp)
                .fillMaxWidth(),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Box(
                modifier = Modifier
                    .size(56.dp)
                    .clip(CircleShape)
                    .background(
                        if (isUnlocked) Color(badge.colorHex).copy(alpha = 0.25f)
                        else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.08f)
                    )
                    .border(
                        width = 2.dp,
                        color = if (isUnlocked) Color(badge.colorHex) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.2f),
                        shape = CircleShape
                    ),
                contentAlignment = Alignment.Center
            ) {
                if (isUnlocked) {
                    Text(
                        text = badge.stickerEmoji,
                        fontSize = 28.sp
                    )
                } else {
                    Icon(
                        imageVector = Icons.Default.Lock,
                        contentDescription = stringResource(R.string.badge_status_locked),
                        tint = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.6f),
                        modifier = Modifier.size(24.dp)
                    )
                }
            }
            Spacer(modifier = Modifier.height(8.dp))
            Text(
                text = title,
                style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold),
                maxLines = 1,
                textAlign = TextAlign.Center,
                color = if (isUnlocked) MaterialTheme.colorScheme.onSurface else MaterialTheme.colorScheme.onSurfaceVariant
            )
            Spacer(modifier = Modifier.height(4.dp))
            if (isUnlocked) {
                Text(
                    text = stringResource(R.string.badge_status_unlocked),
                    style = MaterialTheme.typography.labelSmall,
                    color = StarGoldDark,
                    maxLines = 1
                )
            } else {
                LinearProgressIndicator(
                    progress = { status.currentProgress.toFloat() / status.targetProgress.coerceAtLeast(1) },
                    modifier = Modifier
                        .fillMaxWidth(0.8f)
                        .height(6.dp)
                        .clip(RoundedCornerShape(3.dp)),
                    color = MaterialTheme.colorScheme.primary,
                    trackColor = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.15f),
                )
            }
        }
    }
}

@Composable
fun BadgeDetailDialog(
    status: BadgeUnlockStatus,
    onDismiss: () -> Unit
) {
    val badge = status.badge
    val isUnlocked = status.isUnlocked
    val currentLang by LocaleManager.currentLanguage.collectAsState()
    val title = if (currentLang == LocaleManager.LANG_URDU) badge.titleUr.ifEmpty { badge.title } else badge.titleEn.ifEmpty { badge.title }
    val desc = if (currentLang == LocaleManager.LANG_URDU) badge.descriptionUr.ifEmpty { badge.descriptionEn } else badge.descriptionEn
    val criteria = if (currentLang == LocaleManager.LANG_URDU) badge.milestoneCriteriaUr.ifEmpty { badge.milestoneCriteriaEn } else badge.milestoneCriteriaEn

    // Entrance scale animation for the dialog badge icon
    val dialogScale by animateFloatAsState(
        targetValue = 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "dialogBadgeScale"
    )

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(24.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("badge_detail_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                Box(
                    modifier = Modifier
                        .size(88.dp)
                        .graphicsLayer(scaleX = dialogScale, scaleY = dialogScale)
                        .clip(CircleShape)
                        .background(
                            if (isUnlocked) Color(badge.colorHex).copy(alpha = 0.3f)
                            else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.1f)
                        )
                        .border(
                            width = 3.dp,
                            color = if (isUnlocked) Color(badge.colorHex) else MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.3f),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = if (isUnlocked) badge.stickerEmoji else "🔒",
                        fontSize = 44.sp
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = title,
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                Surface(
                    shape = RoundedCornerShape(8.dp),
                    color = if (isUnlocked) EmeraldSuccess.copy(alpha = 0.15f) else MaterialTheme.colorScheme.surfaceVariant
                ) {
                    Text(
                        text = if (isUnlocked) stringResource(R.string.badge_status_unlocked) else stringResource(R.string.badge_status_locked),
                        style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.Bold),
                        color = if (isUnlocked) EmeraldSuccess else MaterialTheme.colorScheme.onSurfaceVariant,
                        modifier = Modifier.padding(horizontal = 12.dp, vertical = 6.dp)
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurface
                )

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = "🎯 $criteria",
                    style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(24.dp))

                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_close_badge_dialog"),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Text(stringResource(R.string.action_close), fontWeight = FontWeight.Bold)
                }
            }
        }
    }
}

@Composable
fun BadgeCelebrationDialog(
    badge: Badge,
    onDismiss: () -> Unit
) {
    val currentLang by LocaleManager.currentLanguage.collectAsState()
    val title = if (currentLang == LocaleManager.LANG_URDU) badge.titleUr.ifEmpty { badge.title } else badge.titleEn.ifEmpty { badge.title }
    val desc = if (currentLang == LocaleManager.LANG_URDU) badge.descriptionUr.ifEmpty { badge.descriptionEn } else badge.descriptionEn

    var animationStarted by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        animationStarted = true
    }

    val celebrationScale by animateFloatAsState(
        targetValue = if (animationStarted) 1f else 0.4f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessLow
        ),
        label = "celebrationScale"
    )

    val subjectName = when (badge.subjectId) {
        "math" -> stringResource(R.string.badges_filter_math)
        "english" -> stringResource(R.string.badges_filter_english)
        else -> stringResource(R.string.badges_filter_all)
    }

    Dialog(onDismissRequest = onDismiss) {
        Card(
            shape = RoundedCornerShape(28.dp),
            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
            elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp)
                .testTag("badge_celebration_dialog")
        ) {
            Column(
                modifier = Modifier
                    .padding(24.dp)
                    .fillMaxWidth(),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header Title
                Text(
                    text = stringResource(R.string.badge_unlocked_dialog_title),
                    style = MaterialTheme.typography.headlineSmall.copy(fontWeight = FontWeight.ExtraBold),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.primary
                )

                Spacer(modifier = Modifier.height(4.dp))

                Text(
                    text = stringResource(R.string.badge_unlocked_dialog_subtitle, subjectName),
                    style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(20.dp))

                // Giant bouncy celebratory sticker
                Box(
                    modifier = Modifier
                        .size(108.dp)
                        .graphicsLayer(scaleX = celebrationScale, scaleY = celebrationScale)
                        .clip(CircleShape)
                        .background(Color(badge.colorHex).copy(alpha = 0.25f))
                        .border(
                            width = 4.dp,
                            color = Color(badge.colorHex),
                            shape = CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    Text(
                        text = badge.stickerEmoji,
                        fontSize = 56.sp
                    )
                }

                Spacer(modifier = Modifier.height(18.dp))

                // Badge Name
                Text(
                    text = title,
                    style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
                    textAlign = TextAlign.Center
                )

                Spacer(modifier = Modifier.height(8.dp))

                // Badge Description
                Text(
                    text = desc,
                    style = MaterialTheme.typography.bodyMedium,
                    textAlign = TextAlign.Center,
                    color = MaterialTheme.colorScheme.onSurfaceVariant
                )

                Spacer(modifier = Modifier.height(24.dp))

                // Collect Button
                Button(
                    onClick = onDismiss,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(52.dp)
                        .testTag("btn_claim_sticker"),
                    shape = RoundedCornerShape(14.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = StarGoldPrimary,
                        contentColor = TextPrimaryLight
                    )
                ) {
                    Text(
                        text = stringResource(R.string.badge_claim_button),
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp
                    )
                }
            }
        }
    }
}

