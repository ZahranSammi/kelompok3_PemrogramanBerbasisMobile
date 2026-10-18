package com.kelompok3.profileapp.ui.components

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.spring
import androidx.compose.animation.core.tween
import androidx.compose.animation.fadeIn
import androidx.compose.animation.slideInVertically
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.focusable
import androidx.compose.foundation.gestures.detectDragGestures
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.widthIn
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.RotateRight
import androidx.compose.material.icons.outlined.Flight
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material.icons.outlined.TouchApp
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.focus.onFocusChanged
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.input.pointer.pointerInput
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompok3.profileapp.ui.theme.BubblegumCoralAccent
import com.kelompok3.profileapp.ui.theme.BubblegumCoralInk
import com.kelompok3.profileapp.ui.theme.BubblegumCoralTint
import com.kelompok3.profileapp.ui.theme.ButterYellowSurface
import com.kelompok3.profileapp.ui.theme.ChartLineSlate
import com.kelompok3.profileapp.ui.theme.DeepInkNavy
import com.kelompok3.profileapp.ui.theme.ElectricSkyBlue
import com.kelompok3.profileapp.ui.theme.ElectricSkyBlueDark
import com.kelompok3.profileapp.ui.theme.MutedSlate
import com.kelompok3.profileapp.ui.theme.PureWhiteSurface
import com.kelompok3.profileapp.ui.theme.SkyPastelBadge
import com.kelompok3.profileapp.ui.theme.SoftCloudSurface
import com.kelompok3.profileapp.ui.theme.SunnyMangoBorder
import com.kelompok3.profileapp.ui.theme.SunnyMangoInk
import kotlinx.coroutines.launch
import kotlin.math.roundToInt

@Composable
fun Kelompok3Header(
    memberCount: Int,
    onReplayIntro: (() -> Unit)? = null,
    modifier: Modifier = Modifier
) {
    var visible by remember { mutableStateOf(false) }
    LaunchedEffect(Unit) {
        visible = true
    }

    AnimatedVisibility(
        visible = visible,
        enter = fadeIn(tween(350)) + slideInVertically(
            initialOffsetY = { -24 },
            animationSpec = spring(dampingRatio = Spring.DampingRatioNoBouncy)
        ),
        modifier = modifier.fillMaxWidth()
    ) {
        Column(
            modifier = Modifier.fillMaxWidth(),
            verticalArrangement = Arrangement.spacedBy(6.dp)
        ) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Text(
                    text = "Kelompok 3",
                    style = MaterialTheme.typography.displayMedium,
                    color = DeepInkNavy
                )

                if (onReplayIntro != null) {
                    FilledTonalButton(
                        onClick = onReplayIntro,
                        colors = ButtonDefaults.filledTonalButtonColors(
                            containerColor = SkyPastelBadge,
                            contentColor = ElectricSkyBlueDark
                        ),
                        shape = RoundedCornerShape(12.dp),
                        modifier = Modifier.height(38.dp)
                    ) {
                        Icon(
                            imageVector = Icons.Outlined.Flight,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.size(6.dp))
                        Text(
                            text = "Intro Pesawat",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }

            Text(
                text = "Struktur tim beranggotakan $memberCount mahasiswa. Putar prisma 3D atau pilih kartu anggota untuk melihat biodata lengkap.",
                style = MaterialTheme.typography.bodyMedium,
                color = MutedSlate
            )
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun Hero3DSection(
    rotationXDeg: Float,
    rotationYDeg: Float,
    onDragRotate: (deltaX: Float, deltaY: Float) -> Unit,
    onRotateStep: () -> Unit,
    onResetRotation: () -> Unit,
    modifier: Modifier = Modifier
) {
    val scope = rememberCoroutineScope()
    val animatedRotX = remember { Animatable(rotationXDeg) }
    val animatedRotY = remember { Animatable(rotationYDeg) }
    var isStageFocused by remember { mutableStateOf(false) }

    LaunchedEffect(rotationXDeg, rotationYDeg) {
        launch {
            animatedRotX.animateTo(
                targetValue = rotationXDeg,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            )
        }
        launch {
            animatedRotY.animateTo(
                targetValue = rotationYDeg,
                animationSpec = spring(
                    dampingRatio = Spring.DampingRatioMediumBouncy,
                    stiffness = Spring.StiffnessMediumLow
                )
            )
        }
    }

    Surface(
        color = SoftCloudSurface,
        shape = RoundedCornerShape(24.dp),
        modifier = modifier
            .fillMaxWidth()
            .border(
                width = if (isStageFocused) 3.dp else 1.5.dp,
                color = if (isStageFocused) ElectricSkyBlueDark else ChartLineSlate,
                shape = RoundedCornerShape(24.dp)
            )
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier
                .fillMaxWidth()
                .padding(horizontal = 16.dp, vertical = 18.dp)
        ) {
            // Top bar inside 3D stage shows live orientation angle and touch guidance.
            FlowRow(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalArrangement = Arrangement.spacedBy(8.dp)
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(6.dp)
                ) {
                    // TouchApp icon communicates direct drag interaction on the 3D stage (R-04).
                    Icon(
                        imageVector = Icons.Outlined.TouchApp,
                        contentDescription = null,
                        tint = ElectricSkyBlueDark,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = "Panggung Prisma 3D Interaktif",
                        style = MaterialTheme.typography.labelMedium,
                        color = DeepInkNavy
                    )
                }

                // Single deliberate accent badge displays live X/Y tilt coordinates (Part 3).
                Surface(
                    color = BubblegumCoralTint,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.border(1.dp, BubblegumCoralAccent, RoundedCornerShape(8.dp))
                ) {
                    Text(
                        text = "X: ${animatedRotX.value.roundToInt()}° | Y: ${animatedRotY.value.roundToInt()}°",
                        style = MaterialTheme.typography.labelMedium,
                        color = BubblegumCoralInk,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Interactive 3D touch area using pure Compose graphicsLayer perspective and spring physics (R-19).
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .fillMaxWidth()
                    .height(176.dp)
                    .onFocusChanged { isStageFocused = it.isFocused }
                    .focusable()
                    .semantics {
                        contentDescription =
                            "Objek Prisma 3D Kelompok 3, sudut X ${animatedRotX.value.roundToInt()} derajat, sudut Y ${animatedRotY.value.roundToInt()} derajat. Geser untuk memutar."
                    }
                    .pointerInput(Unit) {
                        detectDragGestures(
                            onDragEnd = {
                                onResetRotation()
                            }
                        ) { change, dragAmount ->
                            change.consume()
                            scope.launch {
                                val nextY = (animatedRotY.value + dragAmount.x * 0.45f).coerceIn(-65f, 65f)
                                val nextX = (animatedRotX.value - dragAmount.y * 0.45f).coerceIn(-45f, 45f)
                                animatedRotY.snapTo(nextY)
                                animatedRotX.snapTo(nextX)
                                onDragRotate(nextX, nextY)
                            }
                        }
                    }
            ) {
                // Subtle perspective floor shadow anchors the 3D object so it does not float aimlessly (R-12).
                Canvas(
                    modifier = Modifier
                        .size(width = 148.dp, height = 160.dp)
                        .align(Alignment.Center)
                ) {
                    val shiftX = (animatedRotY.value / 65f) * 18.dp.toPx()
                    val shiftY = (animatedRotX.value / 45f) * 10.dp.toPx()
                    drawOval(
                        color = DeepInkNavy.copy(alpha = 0.12f),
                        topLeft = Offset(
                            x = size.width * 0.15f - shiftX,
                            y = size.height * 0.82f + shiftY
                        ),
                        size = androidx.compose.ui.geometry.Size(
                            width = size.width * 0.7f,
                            height = 20.dp.toPx()
                        )
                    )
                }

                // Back depth extrusion layer creates tactile clay thickness when rotated in 3D space.
                Box(
                    modifier = Modifier
                        .size(134.dp)
                        .graphicsLayer {
                            rotationX = animatedRotX.value
                            rotationY = animatedRotY.value
                            translationX = (-animatedRotY.value * 0.28f)
                            translationY = (animatedRotX.value * 0.28f) + 6f
                            cameraDistance = 11f * density
                        }
                        .clip(RoundedCornerShape(28.dp))
                        .background(SunnyMangoBorder)
                        .border(2.dp, DeepInkNavy, RoundedCornerShape(28.dp))
                )

                // Front 3D prism face uses brand ElectricSkyBlue gradient to separate foreground from depth layer (R-01).
                Box(
                    contentAlignment = Alignment.Center,
                    modifier = Modifier
                        .size(134.dp)
                        .graphicsLayer {
                            rotationX = animatedRotX.value
                            rotationY = animatedRotY.value
                            cameraDistance = 11f * density
                        }
                        .shadow(
                            elevation = 12.dp,
                            shape = RoundedCornerShape(28.dp),
                            spotColor = DeepInkNavy
                        )
                        .clip(RoundedCornerShape(28.dp))
                        .background(
                            Brush.linearGradient(
                                colors = listOf(ElectricSkyBlue, ElectricSkyBlueDark)
                            )
                        )
                        .border(3.dp, PureWhiteSurface, RoundedCornerShape(28.dp))
                ) {
                    Column(
                        horizontalAlignment = Alignment.CenterHorizontally,
                        verticalArrangement = Arrangement.Center,
                        modifier = Modifier
                            .fillMaxSize()
                            .padding(12.dp)
                    ) {
                        Text(
                            text = "K-3",
                            fontSize = 42.sp,
                            fontWeight = FontWeight.Black,
                            color = PureWhiteSurface
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "5 Anggota Tim",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SkyPastelBadge
                        )
                    }
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Explicit tap and keyboard controls ensure 3D interaction works without drag gestures (R-03, R-32).
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(10.dp, Alignment.CenterHorizontally),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                FilledTonalButton(
                    onClick = onRotateStep,
                    colors = ButtonDefaults.filledTonalButtonColors(
                        containerColor = SkyPastelBadge,
                        contentColor = ElectricSkyBlueDark
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .height(48.dp)
                        .widthIn(min = 140.dp)
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Outlined.RotateRight,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(
                        text = "Putar Sudut 3D",
                        fontWeight = FontWeight.Bold
                    )
                }

                OutlinedButton(
                    onClick = onResetRotation,
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = PureWhiteSurface,
                        contentColor = DeepInkNavy
                    ),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .height(48.dp)
                        .widthIn(min = 140.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.size(6.dp))
                    Text(
                        text = "Reset Posisi",
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}
