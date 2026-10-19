package com.triplestar.learn.ui.screens

import android.app.Activity
import android.content.Context
import android.content.ContextWrapper
import android.util.Log
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ArrowForward
import androidx.compose.material.icons.filled.Book
import androidx.compose.material.icons.filled.Calculate
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChildCare
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.MenuBook
import androidx.compose.material.icons.filled.RecordVoiceOver
import androidx.compose.material.icons.filled.School
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.outlined.Lock
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.stringResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.triplestar.learn.R
import com.triplestar.learn.data.repository.AuthRepository
import com.triplestar.learn.data.state.AuthState
import com.triplestar.learn.ui.components.LanguageToggle
import com.triplestar.learn.ui.components.TripleStarLogoLockup
import com.triplestar.learn.ui.theme.StarGoldPrimary
import kotlinx.coroutines.launch

private tailrec fun Context.findActivity(): Activity? = when (this) {
    is Activity -> this
    is ContextWrapper -> baseContext.findActivity()
    else -> null
}

@Composable
fun AuthScreen(
    authRepository: AuthRepository,
    authState: AuthState,
    onNavigateToChildSelect: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val coroutineScope = rememberCoroutineScope()
    val snackbarHostState = remember { SnackbarHostState() }
    val scrollState = rememberScrollState()

    LaunchedEffect(authState) {
        if (authState is AuthState.Authenticated) {
            onNavigateToChildSelect()
        }
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .testTag("auth_screen")
            .background(MaterialTheme.colorScheme.background)
    ) {
        Column(
            modifier = Modifier
                .fillMaxSize()
                .verticalScroll(scrollState)
                .padding(horizontal = 20.dp, vertical = 24.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            // Container constraint for large screens
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .widthIn(max = 680.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Header with Brand & Language Toggle
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    TripleStarLogoLockup(iconSize = 36.dp)
                    LanguageToggle()
                }

                Spacer(modifier = Modifier.height(40.dp))

                // 1. HERO SECTION
                Column(
                    horizontalAlignment = Alignment.CenterHorizontally,
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(50.dp))
                            .background(MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.5f))
                            .padding(horizontal = 16.dp, vertical = 6.dp)
                    ) {
                        Text(
                            text = "⭐ Trusted Learning Platform • Ages 5–14",
                            style = MaterialTheme.typography.labelMedium.copy(fontWeight = FontWeight.SemiBold),
                            color = MaterialTheme.colorScheme.primary
                        )
                    }
                    Spacer(modifier = Modifier.height(16.dp))
                    Text(
                        text = "Empowering Young Minds to Learn, Practice & Excel",
                        style = MaterialTheme.typography.headlineLarge.copy(
                            fontWeight = FontWeight.Bold,
                            fontSize = 32.sp
                        ),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onBackground
                    )
                    Spacer(modifier = Modifier.height(12.dp))
                    Text(
                        text = stringResource(R.string.auth_subtitle),
                        style = MaterialTheme.typography.bodyLarge,
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant
                    )
                    Spacer(modifier = Modifier.height(28.dp))

                    // Parent Login Anchor Card (Primary CTA)
                    Card(
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("parent_login_anchor_card"),
                        shape = RoundedCornerShape(20.dp),
                        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface),
                        elevation = CardDefaults.cardElevation(defaultElevation = 4.dp)
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.fillMaxWidth()
                            ) {
                                Icon(
                                    imageVector = Icons.Outlined.Lock,
                                    contentDescription = null,
                                    tint = MaterialTheme.colorScheme.primary,
                                    modifier = Modifier.size(26.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = stringResource(R.string.auth_parent_section),
                                        style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold)
                                    )
                                    Text(
                                        text = stringResource(R.string.auth_parent_desc),
                                        style = MaterialTheme.typography.bodySmall,
                                        color = MaterialTheme.colorScheme.onSurfaceVariant
                                    )
                                }
                            }

                            Spacer(modifier = Modifier.height(20.dp))

                            when (authState) {
                                is AuthState.Loading -> {
                                    CircularProgressIndicator(
                                        modifier = Modifier
                                            .size(36.dp)
                                            .testTag("auth_loading_indicator")
                                    )
                                }

                                is AuthState.Authenticated -> {
                                    Text(
                                        text = stringResource(R.string.auth_signed_in_as, authState.displayName),
                                        style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold),
                                        color = MaterialTheme.colorScheme.primary
                                    )
                                    Spacer(modifier = Modifier.height(14.dp))
                                    Button(
                                        onClick = onNavigateToChildSelect,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(50.dp)
                                            .testTag("auth_continue_button"),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text(stringResource(R.string.action_continue), fontSize = 16.sp)
                                        Spacer(modifier = Modifier.width(8.dp))
                                        Icon(Icons.Default.ArrowForward, contentDescription = null)
                                    }
                                }

                                is AuthState.BackendConfigPending -> {
                                    Column(
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .clip(RoundedCornerShape(12.dp))
                                            .background(MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.6f))
                                            .padding(14.dp)
                                    ) {
                                        Row(verticalAlignment = Alignment.Top) {
                                            Icon(
                                                imageVector = Icons.Default.Info,
                                                contentDescription = null,
                                                tint = MaterialTheme.colorScheme.primary,
                                                modifier = Modifier.size(20.dp)
                                            )
                                            Spacer(modifier = Modifier.width(8.dp))
                                            Column {
                                                Text(
                                                    text = stringResource(R.string.auth_firebase_pending_title),
                                                    style = MaterialTheme.typography.labelLarge,
                                                    color = MaterialTheme.colorScheme.primary
                                                )
                                                Spacer(modifier = Modifier.height(4.dp))
                                                Text(
                                                    text = authState.message,
                                                    style = MaterialTheme.typography.bodySmall.copy(fontSize = 12.sp),
                                                    color = MaterialTheme.colorScheme.onSurfaceVariant
                                                )
                                            }
                                        }
                                    }

                                    Spacer(modifier = Modifier.height(16.dp))

                                    OutlinedButton(
                                        onClick = onNavigateToChildSelect,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(50.dp)
                                            .testTag("btn_explore_profiles"),
                                        shape = RoundedCornerShape(12.dp)
                                    ) {
                                        Text(stringResource(R.string.child_select_title), fontSize = 16.sp)
                                    }
                                }

                                is AuthState.Unauthenticated, is AuthState.Error -> {
                                    Button(
                                        onClick = {
                                            Log.d("AuthScreen", "Sign-in button clicked. LocalContext=$context")
                                            val activity = context.findActivity()
                                            if (activity != null) {
                                                Log.d("AuthScreen", "Resolved activity: ${activity.javaClass.name}. Invoking signInWithGoogle.")
                                                authRepository.signInWithGoogle(
                                                    activity = activity,
                                                    scope = coroutineScope,
                                                    onSuccess = onNavigateToChildSelect,
                                                    onError = { error ->
                                                        Log.e("AuthScreen", "Sign-in error callback: $error")
                                                        coroutineScope.launch {
                                                            snackbarHostState.showSnackbar(error)
                                                        }
                                                    }
                                                )
                                            } else {
                                                val errorMsg = "Cannot initiate Google Sign-In: activity not found in context hierarchy"
                                                Log.e("AuthScreen", errorMsg)
                                                coroutineScope.launch {
                                                    snackbarHostState.showSnackbar(errorMsg)
                                                }
                                            }
                                        },
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .height(52.dp)
                                            .testTag("sign_in_google_button"),
                                        shape = RoundedCornerShape(12.dp),
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = MaterialTheme.colorScheme.primary
                                        )
                                    ) {
                                        Text(stringResource(R.string.auth_sign_in_google), fontSize = 16.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))

                // 2. LEARNING PROGRAMS SECTION
                SectionHeader(title = "Comprehensive Learning Programs", subtitle = "Core subjects curated for young learners")
                Spacer(modifier = Modifier.height(16.dp))

                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        ProgramFeatureCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.Book,
                            title = "English",
                            desc = "Reading, phonics, and vocabulary building."
                        )
                        ProgramFeatureCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.RecordVoiceOver,
                            title = "English Speaking",
                            desc = "Listening, pronunciation, & conversation."
                        )
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        ProgramFeatureCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.Calculate,
                            title = "Mathematics",
                            desc = "Counting, arithmetic, and problem solving."
                        )
                        ProgramFeatureCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.MenuBook,
                            title = "Reading & Writing",
                            desc = "Story comprehension and clear expression."
                        )
                    }
                    Row(modifier = Modifier.fillMaxWidth(), horizontalArrangement = Arrangement.spacedBy(12.dp)) {
                        ProgramFeatureCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.Star,
                            title = "Vocabulary",
                            desc = "Rich word bank and contextual usage."
                        )
                        ProgramFeatureCard(
                            modifier = Modifier.weight(1f),
                            icon = Icons.Default.School,
                            title = "Grammar",
                            desc = "Sentence structure and polite communication."
                        )
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))

                // 3. ENGLISH SPEAKING JOURNEY SECTION
                SectionHeader(
                    title = "English Speaking Journey",
                    subtitle = "Step-by-step path to confident and polite communication"
                )
                Spacer(modifier = Modifier.height(16.dp))

                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surfaceVariant.copy(alpha = 0.5f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(20.dp),
                        verticalArrangement = Arrangement.spacedBy(12.dp)
                    ) {
                        JourneyStepRow(step = "1", title = "Listening", desc = "Train the ear to natural rhythms and sounds.")
                        JourneyStepRow(step = "2", title = "Pronunciation", desc = "Master clear sounds and phonetic confidence.")
                        JourneyStepRow(step = "3", title = "Words", desc = "Build vocabulary for everyday topics.")
                        JourneyStepRow(step = "4", title = "Sentences", desc = "Form grammatically correct expressions.")
                        JourneyStepRow(step = "5", title = "Conversations", desc = "Practice polite dialogues and greetings.")
                        JourneyStepRow(step = "6", title = "Real-Life Situations", desc = "Speak with confidence in social settings.")
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))

                // 4. FOR CHILDREN & 5. FOR PARENTS
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(16.dp)
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Icon(Icons.Default.ChildCare, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "For Children", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                                Spacer(modifier = Modifier.height(8.dp))
                                BulletPoint(text = "Age-appropriate lessons")
                                BulletPoint(text = "Fun gamified progress")
                                BulletPoint(text = "Speaking challenges")
                                BulletPoint(text = "Positive reinforcement")
                            }
                        }
                    }
                    Column(modifier = Modifier.weight(1f)) {
                        Card(
                            modifier = Modifier.fillMaxWidth(),
                            shape = RoundedCornerShape(16.dp),
                            colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
                        ) {
                            Column(modifier = Modifier.padding(18.dp)) {
                                Icon(Icons.Default.Security, contentDescription = null, tint = MaterialTheme.colorScheme.primary)
                                Spacer(modifier = Modifier.height(8.dp))
                                Text(text = "For Parents", style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold))
                                Spacer(modifier = Modifier.height(8.dp))
                                BulletPoint(text = "Parent-controlled access")
                                BulletPoint(text = "Activity visibility")
                                BulletPoint(text = "Structured curriculum")
                                BulletPoint(text = "COPPA & family safe")
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))

                // 6. HOW IT WORKS
                SectionHeader(title = "How It Works", subtitle = "Four simple steps to start learning")
                Spacer(modifier = Modifier.height(16.dp))

                Column(modifier = Modifier.fillMaxWidth(), verticalArrangement = Arrangement.spacedBy(12.dp)) {
                    HowItWorksStep(number = "1", title = "Parent Sign-In", desc = "Securely sign in with your parent account.")
                    HowItWorksStep(number = "2", title = "Create Child Profile", desc = "Set up your child's age group and daily goals.")
                    HowItWorksStep(number = "3", title = "Interactive Learning", desc = "Child engages with bite-sized lessons & speaking tasks.")
                    HowItWorksStep(number = "4", title = "Monitor Progress", desc = "Review stars, XP, and daily achievements.")
                }

                Spacer(modifier = Modifier.height(48.dp))

                // 7. AGE GROUPS
                SectionHeader(title = "Tailored for Every Age Group", subtitle = "Customized depth for ages 5 to 14")
                Spacer(modifier = Modifier.height(16.dp))

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(12.dp)
                ) {
                    AgeGroupCard(modifier = Modifier.weight(1f), ageRange = "5–7", level = "Explorer", focus = "Foundations & Phonics")
                    AgeGroupCard(modifier = Modifier.weight(1f), ageRange = "8–10", level = "Adventurer", focus = "Fluency & Arithmetic")
                    AgeGroupCard(modifier = Modifier.weight(1f), ageRange = "11–14", level = "Master", focus = "Advanced Literacy & Logic")
                }

                Spacer(modifier = Modifier.height(48.dp))

                // 8. FINAL CTA
                Card(
                    modifier = Modifier.fillMaxWidth(),
                    shape = RoundedCornerShape(20.dp),
                    colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.primaryContainer.copy(alpha = 0.3f))
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Text(
                            text = "Start Your Child's Learning Journey",
                            style = MaterialTheme.typography.headlineMedium.copy(fontWeight = FontWeight.Bold),
                            textAlign = TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Join parents who trust TripleStarTask Learning for safe and effective skill building.",
                            style = MaterialTheme.typography.bodyMedium,
                            textAlign = TextAlign.Center,
                            color = MaterialTheme.colorScheme.onSurfaceVariant
                        )
                        Spacer(modifier = Modifier.height(20.dp))
                        Button(
                            onClick = {
                                Log.d("AuthScreen", "Bottom CTA button clicked. AuthState=$authState")
                                val activity = context.findActivity()
                                if (activity != null && authState is AuthState.Unauthenticated) {
                                    Log.d("AuthScreen", "Resolved activity: ${activity.javaClass.name}. Launching signInWithGoogle from bottom CTA.")
                                    authRepository.signInWithGoogle(
                                        activity = activity,
                                        scope = coroutineScope,
                                        onSuccess = onNavigateToChildSelect,
                                        onError = { error ->
                                            Log.e("AuthScreen", "Bottom CTA sign-in error: $error")
                                            coroutineScope.launch { snackbarHostState.showSnackbar(error) }
                                        }
                                    )
                                } else {
                                    Log.d("AuthScreen", "Navigating to child select from bottom CTA")
                                    onNavigateToChildSelect()
                                }
                            },
                            modifier = Modifier.height(50.dp),
                            shape = RoundedCornerShape(12.dp)
                        ) {
                            Text("Parent Login / Get Started", fontSize = 16.sp, fontWeight = FontWeight.Bold)
                        }
                    }
                }

                Spacer(modifier = Modifier.height(48.dp))

                Divider(color = MaterialTheme.colorScheme.outlineVariant)
                Spacer(modifier = Modifier.height(24.dp))

                // 9. FOOTER
                Column(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalAlignment = Alignment.CenterHorizontally
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        FooterLink("Home")
                        Spacer(modifier = Modifier.width(16.dp))
                        FooterLink("Learning")
                        Spacer(modifier = Modifier.width(16.dp))
                        FooterLink("Parent Login")
                    }
                    Spacer(modifier = Modifier.height(12.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.Center
                    ) {
                        FooterLink("Privacy Policy")
                        Spacer(modifier = Modifier.width(16.dp))
                        FooterLink("Terms")
                        Spacer(modifier = Modifier.width(16.dp))
                        FooterLink("Contact")
                    }
                    Spacer(modifier = Modifier.height(20.dp))
                    Text(
                        text = "© TripleStarTask Learning. All rights reserved. COPPA & Google Play Families Compliant.",
                        style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp),
                        textAlign = TextAlign.Center,
                        color = MaterialTheme.colorScheme.onSurfaceVariant.copy(alpha = 0.7f)
                    )
                }

                Spacer(modifier = Modifier.height(32.dp))
            }
        }

        SnackbarHost(
            hostState = snackbarHostState,
            modifier = Modifier.align(Alignment.BottomCenter)
        )
    }
}

@Composable
private fun SectionHeader(title: String, subtitle: String) {
    Column(modifier = Modifier.fillMaxWidth(), horizontalAlignment = Alignment.CenterHorizontally) {
        Text(
            text = title,
            style = MaterialTheme.typography.titleLarge.copy(fontWeight = FontWeight.Bold),
            textAlign = TextAlign.Center
        )
        Spacer(modifier = Modifier.height(4.dp))
        Text(
            text = subtitle,
            style = MaterialTheme.typography.bodyMedium,
            textAlign = TextAlign.Center,
            color = MaterialTheme.colorScheme.onSurfaceVariant
        )
    }
}

@Composable
private fun ProgramFeatureCard(
    modifier: Modifier = Modifier,
    icon: androidx.compose.ui.graphics.vector.ImageVector,
    title: String,
    desc: String
) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp)
        ) {
            Icon(imageVector = icon, contentDescription = null, tint = MaterialTheme.colorScheme.primary, modifier = Modifier.size(24.dp))
            Spacer(modifier = Modifier.height(8.dp))
            Text(text = title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
            Spacer(modifier = Modifier.height(4.dp))
            Text(text = desc, style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp), color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun JourneyStepRow(step: String, title: String, desc: String) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Box(
            modifier = Modifier
                .size(28.dp)
                .clip(RoundedCornerShape(14.dp))
                .background(MaterialTheme.colorScheme.primary),
            contentAlignment = Alignment.Center
        ) {
            Text(text = step, color = MaterialTheme.colorScheme.onPrimary, fontWeight = FontWeight.Bold, fontSize = 12.sp)
        }
        Spacer(modifier = Modifier.width(12.dp))
        Column(modifier = Modifier.weight(1f)) {
            Text(text = title, style = MaterialTheme.typography.bodyMedium.copy(fontWeight = FontWeight.SemiBold))
            Text(text = desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun BulletPoint(text: String) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        modifier = Modifier.padding(vertical = 3.dp)
    ) {
        Icon(
            imageVector = Icons.Default.CheckCircle,
            contentDescription = null,
            tint = MaterialTheme.colorScheme.primary,
            modifier = Modifier.size(16.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(text = text, style = MaterialTheme.typography.bodySmall)
    }
}

@Composable
private fun HowItWorksStep(number: String, title: String, desc: String) {
    Card(
        modifier = Modifier.fillMaxWidth(),
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(16.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            Box(
                modifier = Modifier
                    .size(36.dp)
                    .clip(RoundedCornerShape(18.dp))
                    .background(MaterialTheme.colorScheme.primaryContainer),
                contentAlignment = Alignment.Center
            ) {
                Text(text = number, color = MaterialTheme.colorScheme.primary, fontWeight = FontWeight.Bold)
            }
            Spacer(modifier = Modifier.width(16.dp))
            Column(modifier = Modifier.weight(1f)) {
                Text(text = title, style = MaterialTheme.typography.titleSmall.copy(fontWeight = FontWeight.Bold))
                Spacer(modifier = Modifier.height(2.dp))
                Text(text = desc, style = MaterialTheme.typography.bodySmall, color = MaterialTheme.colorScheme.onSurfaceVariant)
            }
        }
    }
}

@Composable
private fun AgeGroupCard(modifier: Modifier = Modifier, ageRange: String, level: String, focus: String) {
    Card(
        modifier = modifier,
        shape = RoundedCornerShape(16.dp),
        colors = CardDefaults.cardColors(containerColor = MaterialTheme.colorScheme.surface)
    ) {
        Column(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Text(text = ageRange, style = MaterialTheme.typography.titleMedium.copy(fontWeight = FontWeight.Bold), color = MaterialTheme.colorScheme.primary)
            Text(text = level, style = MaterialTheme.typography.labelMedium, fontWeight = FontWeight.SemiBold)
            Spacer(modifier = Modifier.height(6.dp))
            Text(text = focus, style = MaterialTheme.typography.bodySmall.copy(fontSize = 11.sp), textAlign = TextAlign.Center, color = MaterialTheme.colorScheme.onSurfaceVariant)
        }
    }
}

@Composable
private fun FooterLink(text: String) {
    Text(
        text = text,
        style = MaterialTheme.typography.bodySmall.copy(fontWeight = FontWeight.Medium),
        color = MaterialTheme.colorScheme.primary
    )
}
