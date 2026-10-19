package com.triplestar.learn.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Bolt
import androidx.compose.material.icons.filled.LocalFireDepartment
import androidx.compose.material.icons.filled.Star
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.triplestar.learn.R
import com.triplestar.learn.ui.theme.CoralFun
import com.triplestar.learn.ui.theme.PurpleMagic
import com.triplestar.learn.ui.theme.StarGoldPrimary

@Composable
fun MetricPill(
    icon: ImageVector,
    iconTint: Color,
    bgColor: Color,
    text: String,
    testTag: String,
    modifier: Modifier = Modifier
) {
    Row(
        modifier = modifier
            .testTag(testTag)
            .clip(RoundedCornerShape(16.dp))
            .background(bgColor)
            .padding(horizontal = 10.dp, vertical = 6.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            modifier = Modifier.size(18.dp),
            tint = iconTint
        )
        Spacer(modifier = Modifier.width(4.dp))
        Text(
            text = text,
            style = MaterialTheme.typography.labelMedium.copy(
                fontWeight = FontWeight.Bold,
                fontSize = 13.sp
            ),
            color = MaterialTheme.colorScheme.onSurface
        )
    }
}

@Composable
fun StreakPill(streakDays: Int, modifier: Modifier = Modifier) {
    MetricPill(
        icon = Icons.Default.LocalFireDepartment,
        iconTint = CoralFun,
        bgColor = CoralFun.copy(alpha = 0.15f),
        text = stringResource(R.string.dashboard_streak_label, streakDays),
        testTag = "pill_streak",
        modifier = modifier
    )
}

@Composable
fun StarsPill(stars: Int, modifier: Modifier = Modifier) {
    MetricPill(
        icon = Icons.Default.Star,
        iconTint = StarGoldPrimary,
        bgColor = StarGoldPrimary.copy(alpha = 0.15f),
        text = stringResource(R.string.dashboard_stars_label, stars),
        testTag = "pill_stars",
        modifier = modifier
    )
}

@Composable
fun XpPill(xp: Int, modifier: Modifier = Modifier) {
    MetricPill(
        icon = Icons.Default.Bolt,
        iconTint = PurpleMagic,
        bgColor = PurpleMagic.copy(alpha = 0.15f),
        text = stringResource(R.string.dashboard_xp_label, xp),
        testTag = "pill_xp",
        modifier = modifier
    )
}
