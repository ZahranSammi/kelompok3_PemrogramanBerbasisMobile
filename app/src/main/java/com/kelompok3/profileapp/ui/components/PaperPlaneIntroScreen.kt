package com.kelompok3.profileapp.ui.components

import androidx.compose.animation.core.Animatable
import androidx.compose.animation.core.FastOutSlowInEasing
import androidx.compose.animation.core.LinearEasing
import androidx.compose.animation.core.tween
import androidx.compose.foundation.Canvas
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.offset
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.ArrowForward
import androidx.compose.material.icons.outlined.Air
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.draw.shadow
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.geometry.Size
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.PathEffect
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.StrokeJoin
import androidx.compose.ui.graphics.drawscope.DrawScope
import androidx.compose.ui.graphics.drawscope.Fill
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.IntOffset
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompok3.profileapp.ui.theme.ButterYellowSurface
import com.kelompok3.profileapp.ui.theme.ChartLineSlate
import com.kelompok3.profileapp.ui.theme.DeepInkNavy
import com.kelompok3.profileapp.ui.theme.ElectricSkyBlue
import com.kelompok3.profileapp.ui.theme.ElectricSkyBlueDark
import com.kelompok3.profileapp.ui.theme.MutedSlate
import com.kelompok3.profileapp.ui.theme.PureWhiteSurface
import com.kelompok3.profileapp.ui.theme.SkyPastelBadge
import com.kelompok3.profileapp.ui.theme.SunnyMangoBorder
import com.kelompok3.profileapp.ui.theme.SunnyMangoInk
import com.kelompok3.profileapp.ui.theme.WarmCreamBg
import kotlinx.coroutines.delay
import kotlin.math.roundToInt

/**
 * Animasi pembuka: Kertas bertuliskan "Kelompok 3" melipat menjadi pesawat kertas,
 * lalu meluncur terbang ke angkasa sebelum membuka halaman utama.
 */
@Composable
fun PaperPlaneIntroScreen(
    onFinish: () -> Unit,
    modifier: Modifier = Modifier
) {
    // 0f = kertas datar, 1f = terlipat sempurna menjadi pesawat kertas
    val foldProgress = remember { Animatable(0f) }
    // 0f = siap di landasan, 1f = meluncur terbang menjauh ke luar layar
    val flightProgress = remember { Animatable(0f) }
    // Transisi fade keluar seluruh intro saat pesawat sudah terbang jauh
    val introAlpha = remember { Animatable(1f) }

    var stageText by remember { mutableStateOf("Membuka lembar catatan Kelompok 3...") }

    LaunchedEffect(Unit) {
        // Tahap 1: Tampilkan lembar kertas dengan tulisan Kelompok 3
        delay(650)
        stageText = "Kertas mulai melipat menjadi pesawat..."

        // Tahap 2: Animasi origami melipat kertas menjadi pesawat kertas
        foldProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1400, easing = FastOutSlowInEasing)
        )

        stageText = "Pesawat kertas siap meluncur!"
        delay(250)

        // Tahap 3: Pesawat kertas meluncur dan terbang menjauh
        stageText = "Meluncur terbang menuju beranda..."
        flightProgress.animateTo(
            targetValue = 1f,
            animationSpec = tween(durationMillis = 1100, easing = FastOutSlowInEasing)
        )

        // Tahap 4: Fade out intro dan buka homepage
        introAlpha.animateTo(
            targetValue = 0f,
            animationSpec = tween(durationMillis = 280, easing = LinearEasing)
        )
        onFinish()
    }

    Box(
        modifier = modifier
            .fillMaxSize()
            .background(WarmCreamBg)
            .alpha(introAlpha.value)
            .clickable(
                interactionSource = remember { MutableInteractionSource() },
                indication = null,
                onClick = onFinish
            )
            .semantics {
                contentDescription = "Animasi pengantar Kelompok 3: Kertas melipat menjadi pesawat lalu terbang. Ketuk untuk melewati."
            }
    ) {
        // Tombol Lewati di sudut kanan atas agar pengguna selalu memiliki kendali (C-2, R-32)
        Surface(
            color = PureWhiteSurface.copy(alpha = 0.92f),
            shape = RoundedCornerShape(14.dp),
            modifier = Modifier
                .align(Alignment.TopEnd)
                .padding(top = 48.dp, end = 20.dp)
                .border(1.dp, ChartLineSlate.copy(alpha = 0.5f), RoundedCornerShape(14.dp))
        ) {
            FilledTonalButton(
                onClick = onFinish,
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = SkyPastelBadge,
                    contentColor = ElectricSkyBlueDark
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(44.dp)
            ) {
                Text(
                    text = "Lewati",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.Bold
                )
                Spacer(modifier = Modifier.size(6.dp))
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.ArrowForward,
                    contentDescription = null,
                    modifier = Modifier.size(16.dp)
                )
            }
        }

        // Panggung utama animasi kertas dan pesawat kertas
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.Center,
            modifier = Modifier
                .fillMaxSize()
                .padding(horizontal = 24.dp)
        ) {
            Box(
                contentAlignment = Alignment.Center,
                modifier = Modifier
                    .size(340.dp)
            ) {
                // Jejak garis angin / flight trail saat pesawat meluncur
                if (flightProgress.value > 0.05f) {
                    WindTrailCanvas(
                        flightProgress = flightProgress.value,
                        modifier = Modifier.fillMaxSize()
                    )
                }

                // Kanvas Origami Kertas & Pesawat Kertas
                OrigamiPaperPlaneCanvas(
                    foldProgress = foldProgress.value,
                    flightProgress = flightProgress.value,
                    modifier = Modifier.fillMaxSize()
                )
            }

            Spacer(modifier = Modifier.height(32.dp))

            // Status narasi animasi di bawah objek
            Surface(
                color = PureWhiteSurface,
                shape = RoundedCornerShape(16.dp),
                shadowElevation = 2.dp,
                modifier = Modifier
                    .border(1.dp, ChartLineSlate.copy(alpha = 0.35f), RoundedCornerShape(16.dp))
            ) {
                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.padding(horizontal = 16.dp, vertical = 10.dp)
                ) {
                    Icon(
                        imageVector = Icons.Outlined.Air,
                        contentDescription = null,
                        tint = ElectricSkyBlueDark,
                        modifier = Modifier.size(18.dp)
                    )
                    Text(
                        text = stageText,
                        style = MaterialTheme.typography.bodyMedium,
                        color = DeepInkNavy,
                        fontWeight = FontWeight.SemiBold,
                        textAlign = TextAlign.Center
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = "Ketuk di mana saja untuk langsung masuk",
                style = MaterialTheme.typography.labelMedium,
                color = MutedSlate
            )
        }
    }
}

/**
 * Menggambar proses metamorfosis dari selembar kertas bertuliskan Kelompok 3
 * menjadi pesawat kertas 3D lipat dengan pencahayaan taktil.
 */
@Composable
private fun OrigamiPaperPlaneCanvas(
    foldProgress: Float,
    flightProgress: Float,
    modifier: Modifier = Modifier
) {
    // Parameter pergerakan saat meluncur terbang:
    // Pesawat condong ke kanan atas, bergerak cepat melesat ke luar layar
    val flightShiftX = flightProgress * 620f
    val flightShiftY = -flightProgress * 780f
    val flightScale = if (flightProgress < 0.2f) {
        1f - flightProgress * 0.4f // Antisipasi sedikit mengecil saat bersiap
    } else {
        0.92f + (flightProgress - 0.2f) * 0.65f // Membesar lalu menembus layar
    }
    val flightRotationZ = if (foldProgress < 0.9f) {
        0f
    } else {
        // Miring mengikuti sudut terbang ke arah kanan atas
        32f + flightProgress * 18f
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = modifier
            .graphicsLayer {
                translationX = flightShiftX
                translationY = flightShiftY
                rotationZ = flightRotationZ
                scaleX = flightScale
                scaleY = flightScale
                cameraDistance = 12f * density
            }
    ) {
        Canvas(modifier = Modifier.fillMaxSize()) {
            val cX = size.width / 2f
            val cY = size.height / 2f

            if (foldProgress < 0.45f) {
                // TAHAP A: Kertas Datar dengan bayangan dan tulisan Kelompok 3
                drawFlatPaperPhase(
                    centerX = cX,
                    centerY = cY,
                    foldFactor = foldProgress / 0.45f
                )
            } else if (foldProgress < 0.85f) {
                // TAHAP B: Lipatan diagonal dan pembentukan sayap origami
                val interFold = (foldProgress - 0.45f) / 0.4f
                drawIntermediateFoldingPhase(
                    centerX = cX,
                    centerY = cY,
                    progress = interFold
                )
            } else {
                // TAHAP C: Pesawat Kertas 3D Utuh siap meluncur
                val readyFactor = (foldProgress - 0.85f) / 0.15f
                drawCompletePaperPlane(
                    centerX = cX,
                    centerY = cY,
                    readyFactor = readyFactor,
                    flightProgress = flightProgress
                )
            }
        }

        // Teks "Kelompok 3" pada lembar kertas di awal, memudar saat mulai terlipat
        if (foldProgress < 0.45f) {
            val textAlpha = (1f - (foldProgress / 0.35f)).coerceIn(0f, 1f)
            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                verticalArrangement = Arrangement.Center,
                modifier = Modifier
                    .alpha(textAlpha)
                    .padding(bottom = 12.dp)
            ) {
                Surface(
                    color = SkyPastelBadge,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.border(1.dp, ElectricSkyBlueDark.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                ) {
                    Text(
                        text = "LEMBAR TUGAS",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.Bold,
                        color = ElectricSkyBlueDark,
                        modifier = Modifier.padding(horizontal = 10.dp, vertical = 3.dp)
                    )
                }
                Spacer(modifier = Modifier.height(8.dp))
                Text(
                    text = "Kelompok 3",
                    fontSize = 28.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = DeepInkNavy,
                    textAlign = TextAlign.Center
                )
                Text(
                    text = "Aplikasi Profil Interaktif",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.Medium,
                    color = MutedSlate,
                    textAlign = TextAlign.Center
                )
            }
        }
    }
}

/**
 * Menggambar lembar kertas datar persegi panjang dengan lipatan sudut atas yang mulai melipat.
 */
private fun DrawScope.drawFlatPaperPhase(
    centerX: Float,
    centerY: Float,
    foldFactor: Float
) {
    val paperWidth = 210.dp.toPx()
    val paperHeight = 260.dp.toPx()
    val left = centerX - paperWidth / 2f
    val right = centerX + paperWidth / 2f
    val top = centerY - paperHeight / 2f
    val bottom = centerY + paperHeight / 2f

    // Bayangan lembut di bawah kertas
    drawRoundRect(
        color = DeepInkNavy.copy(alpha = 0.08f),
        topLeft = Offset(left + 6.dp.toPx(), top + 10.dp.toPx()),
        size = Size(paperWidth, paperHeight),
        cornerRadius = androidx.compose.ui.geometry.CornerRadius(16.dp.toPx())
    )

    // Badan kertas utama berwarna putih bersih
    val paperPath = Path().apply {
        val foldOffset = foldFactor * 52.dp.toPx()
        moveTo(left + foldOffset, top)
        lineTo(right - foldOffset, top)
        lineTo(right, top + foldOffset)
        lineTo(right, bottom)
        lineTo(left, bottom)
        lineTo(left, top + foldOffset)
        close()
    }

    drawPath(
        path = paperPath,
        color = PureWhiteSurface,
        style = Fill
    )
    drawPath(
        path = paperPath,
        color = ChartLineSlate.copy(alpha = 0.6f),
        style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round)
    )

    // Garis lipatan tengah vertikal putus-putus
    drawLine(
        color = ChartLineSlate.copy(alpha = 0.4f),
        start = Offset(centerX, top),
        end = Offset(centerX, bottom),
        strokeWidth = 1.5.dp.toPx(),
        pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f))
    )

    // Segitiga lipatan di sudut kiri atas dan kanan atas saat mulai melipat
    if (foldFactor > 0.05f) {
        val foldSize = foldFactor * 52.dp.toPx()

        // Lipatan kiri atas
        val leftFold = Path().apply {
            moveTo(left, top + foldSize)
            lineTo(left + foldSize, top)
            lineTo(centerX * (foldFactor * 0.4f) + left * (1f - foldFactor * 0.4f), centerY - 40f)
            close()
        }
        drawPath(
            path = leftFold,
            color = Color(0xFFF1F5F9),
            style = Fill
        )
        drawPath(
            path = leftFold,
            color = ChartLineSlate.copy(alpha = 0.5f),
            style = Stroke(width = 1.5.dp.toPx())
        )

        // Lipatan kanan atas
        val rightFold = Path().apply {
            moveTo(right, top + foldSize)
            lineTo(right - foldSize, top)
            lineTo(right - (centerX - left) * (foldFactor * 0.4f), centerY - 40f)
            close()
        }
        drawPath(
            path = rightFold,
            color = Color(0xFFE2E8F0),
            style = Fill
        )
        drawPath(
            path = rightFold,
            color = ChartLineSlate.copy(alpha = 0.5f),
            style = Stroke(width = 1.5.dp.toPx())
        )
    }
}

/**
 * Tahap transisi lipatan origami dari lembar terlipat sebagian menuju bentuk taji pesawat kertas.
 */
private fun DrawScope.drawIntermediateFoldingPhase(
    centerX: Float,
    centerY: Float,
    progress: Float
) {
    val noseY = centerY - 110.dp.toPx()
    val tailY = centerY + 100.dp.toPx()
    val wingSpan = (180.dp.toPx()) * (1f - progress * 0.45f)
    val leftWingX = centerX - wingSpan / 2f
    val rightWingX = centerX + wingSpan / 2f

    // Bayangan dinamis di bawah lipatan
    val shadowPath = Path().apply {
        moveTo(centerX, noseY + 12f)
        lineTo(leftWingX + 8f, tailY + 8f)
        lineTo(centerX, tailY - 10f)
        lineTo(rightWingX + 8f, tailY + 8f)
        close()
    }
    drawPath(path = shadowPath, color = DeepInkNavy.copy(alpha = 0.10f), style = Fill)

    // Sayap Kiri
    val leftWing = Path().apply {
        moveTo(centerX, noseY)
        lineTo(leftWingX, tailY)
        lineTo(centerX, tailY - 30.dp.toPx())
        close()
    }
    drawPath(path = leftWing, color = PureWhiteSurface, style = Fill)
    drawPath(path = leftWing, color = ChartLineSlate, style = Stroke(width = 2.dp.toPx()))

    // Sayap Kanan (diberi sedikit shading bayangan lipatan)
    val rightWing = Path().apply {
        moveTo(centerX, noseY)
        lineTo(rightWingX, tailY)
        lineTo(centerX, tailY - 30.dp.toPx())
        close()
    }
    drawPath(path = rightWing, color = Color(0xFFF1F5F9), style = Fill)
    drawPath(path = rightWing, color = ChartLineSlate, style = Stroke(width = 2.dp.toPx()))

    // Tulang punggung tengah lipatan
    drawLine(
        color = DeepInkNavy.copy(alpha = 0.7f),
        start = Offset(centerX, noseY),
        end = Offset(centerX, tailY - 30.dp.toPx()),
        strokeWidth = 2.5.dp.toPx(),
        cap = StrokeCap.Round
    )
}

/**
 * Menggambar Pesawat Kertas 3D sempurna dengan sayap bersudut, lunas penyeimbang,
 * dan aksen brand Kelompok 3 di sayapnya.
 */
private fun DrawScope.drawCompletePaperPlane(
    centerX: Float,
    centerY: Float,
    readyFactor: Float,
    flightProgress: Float
) {
    val noseY = centerY - 115.dp.toPx()
    val spineEndY = centerY + 65.dp.toPx()
    val keelBottomY = centerY + 85.dp.toPx()

    val wingWidth = 105.dp.toPx()
    val leftWingTip = Offset(centerX - wingWidth, centerY + 55.dp.toPx())
    val rightWingTip = Offset(centerX + wingWidth, centerY + 55.dp.toPx())
    val nose = Offset(centerX, noseY)
    val spineEnd = Offset(centerX, spineEndY)
    val keelBottom = Offset(centerX, keelBottomY)

    // Bayangan di tanah yang menjauh saat pesawat mulai terbang meluncur
    val shadowAlpha = (0.16f * (1f - flightProgress * 0.8f)).coerceAtLeast(0f)
    val shadowOffset = 18.dp.toPx() * (1f + flightProgress * 1.5f)
    val planeShadow = Path().apply {
        moveTo(nose.x + shadowOffset, nose.y + shadowOffset)
        lineTo(leftWingTip.x + shadowOffset, leftWingTip.y + shadowOffset)
        lineTo(spineEnd.x + shadowOffset, spineEnd.y + shadowOffset)
        lineTo(rightWingTip.x + shadowOffset, rightWingTip.y + shadowOffset)
        close()
    }
    drawPath(path = planeShadow, color = DeepInkNavy.copy(alpha = shadowAlpha), style = Fill)

    // 1. Lunas Bawah Pesawat (Keel / badan lipatan tengah bawah yang memberikan efek 3D)
    val keelLeft = Path().apply {
        moveTo(nose.x, nose.y + 20f)
        lineTo(centerX, spineEndY)
        lineTo(centerX - 8f, keelBottomY)
        close()
    }
    drawPath(path = keelLeft, color = Color(0xFFCBD5E1), style = Fill)

    val keelRight = Path().apply {
        moveTo(nose.x, nose.y + 20f)
        lineTo(centerX, spineEndY)
        lineTo(centerX + 8f, keelBottomY)
        close()
    }
    drawPath(path = keelRight, color = Color(0xFF94A3B8), style = Fill)

    // 2. Sayap Kiri Atas (sisi terang)
    val leftWingPath = Path().apply {
        moveTo(nose.x, nose.y)
        lineTo(leftWingTip.x, leftWingTip.y)
        lineTo(spineEnd.x, spineEnd.y)
        close()
    }
    drawPath(path = leftWingPath, color = PureWhiteSurface, style = Fill)
    drawPath(
        path = leftWingPath,
        color = DeepInkNavy.copy(alpha = 0.55f),
        style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
    )

    // 3. Sayap Kanan Atas (sisi bayangan lembut untuk kedalaman 3D)
    val rightWingPath = Path().apply {
        moveTo(nose.x, nose.y)
        lineTo(rightWingTip.x, rightWingTip.y)
        lineTo(spineEnd.x, spineEnd.y)
        close()
    }
    drawPath(path = rightWingPath, color = Color(0xFFE2E8F0), style = Fill)
    drawPath(
        path = rightWingPath,
        color = DeepInkNavy.copy(alpha = 0.55f),
        style = Stroke(width = 2.dp.toPx(), cap = StrokeCap.Round, join = StrokeJoin.Round)
    )

    // 4. Garis Tulang Belakang Utama (Center Crease)
    drawLine(
        color = DeepInkNavy,
        start = nose,
        end = spineEnd,
        strokeWidth = 3.dp.toPx(),
        cap = StrokeCap.Round
    )

    // 5. Garis Aksen Biru Brand Kelompok 3 di Sayap Kiri
    val accentStripeLeft = Path().apply {
        moveTo(nose.x * 0.7f + leftWingTip.x * 0.3f, nose.y * 0.7f + leftWingTip.y * 0.3f)
        lineTo(nose.x * 0.55f + leftWingTip.x * 0.45f, nose.y * 0.55f + leftWingTip.y * 0.45f)
        lineTo(spineEnd.x * 0.4f + leftWingTip.x * 0.6f, spineEnd.y * 0.4f + leftWingTip.y * 0.6f)
        lineTo(spineEnd.x * 0.6f + leftWingTip.x * 0.4f, spineEnd.y * 0.6f + leftWingTip.y * 0.4f)
        close()
    }
    drawPath(path = accentStripeLeft, color = ElectricSkyBlue.copy(alpha = 0.85f), style = Fill)
}

/**
 * Garis-garis lintasan udara (wind swoosh trails) saat pesawat meluncur cepat.
 */
@Composable
private fun WindTrailCanvas(
    flightProgress: Float,
    modifier: Modifier = Modifier
) {
    Canvas(modifier = modifier) {
        val cX = size.width / 2f
        val cY = size.height / 2f

        val trailAlpha = (1f - flightProgress).coerceIn(0f, 1f) * 0.75f
        val trailLen = 80.dp.toPx() * flightProgress

        // Lintasan angin 1 (tengah bawah melengkung ke arah luncuran)
        val trail1 = Path().apply {
            moveTo(cX - 70.dp.toPx(), cY + 50.dp.toPx())
            quadraticTo(
                cX - 20.dp.toPx(),
                cY + 20.dp.toPx(),
                cX + trailLen * 0.8f,
                cY - trailLen * 0.9f
            )
        }
        drawPath(
            path = trail1,
            color = ElectricSkyBlueDark.copy(alpha = trailAlpha),
            style = Stroke(
                width = 2.5.dp.toPx(),
                cap = StrokeCap.Round,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(16f, 10f))
            )
        )

        // Lintasan angin 2 (sayap kiri)
        val trail2 = Path().apply {
            moveTo(cX - 120.dp.toPx(), cY + 80.dp.toPx())
            quadraticTo(
                cX - 60.dp.toPx(),
                cY + 40.dp.toPx(),
                cX + (trailLen - 30f) * 0.7f,
                cY - (trailLen - 30f) * 0.8f
            )
        }
        drawPath(
            path = trail2,
            color = ChartLineSlate.copy(alpha = trailAlpha * 0.8f),
            style = Stroke(
                width = 2.dp.toPx(),
                cap = StrokeCap.Round,
                pathEffect = PathEffect.dashPathEffect(floatArrayOf(12f, 8f))
            )
        )
    }
}
