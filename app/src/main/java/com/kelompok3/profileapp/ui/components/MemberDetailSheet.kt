package com.kelompok3.profileapp.ui.components

import android.content.ClipData
import android.content.ClipboardManager
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.outlined.Chat
import androidx.compose.material.icons.outlined.Badge
import androidx.compose.material.icons.outlined.Close
import androidx.compose.material.icons.outlined.ContentCopy
import androidx.compose.material.icons.outlined.Email
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material.icons.outlined.Phone
import androidx.compose.material.icons.outlined.School
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.FilledTonalButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalUriHandler
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompok3.profileapp.R
import com.kelompok3.profileapp.model.MemberProfile
import com.kelompok3.profileapp.ui.theme.ButterYellowSurface
import com.kelompok3.profileapp.ui.theme.ChartLineSlate
import com.kelompok3.profileapp.ui.theme.DeepInkNavy
import com.kelompok3.profileapp.ui.theme.ElectricSkyBlue
import com.kelompok3.profileapp.ui.theme.ElectricSkyBlueDark
import com.kelompok3.profileapp.ui.theme.MutedSlate
import com.kelompok3.profileapp.ui.theme.PureWhiteSurface
import com.kelompok3.profileapp.ui.theme.SkyPastelBadge
import com.kelompok3.profileapp.ui.theme.SlateCardSurface
import com.kelompok3.profileapp.ui.theme.SunnyMangoBorder
import com.kelompok3.profileapp.ui.theme.SunnyMangoInk

@OptIn(ExperimentalLayoutApi::class)
@Composable
fun MemberDetailContent(
    member: MemberProfile,
    onClose: () -> Unit,
    modifier: Modifier = Modifier
) {
    val context = LocalContext.current
    val uriHandler = LocalUriHandler.current
    var actionFeedback by remember(member.id) { mutableStateOf<String?>(null) }
    val scrollState = rememberScrollState()

    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        modifier = modifier
            .fillMaxWidth()
            .verticalScroll(scrollState)
            .padding(horizontal = 24.dp, vertical = 12.dp)
    ) {
        // Foto profil asli anggota tim atau placeholder geometris bersih (R-23, R-38).
        Box(
            contentAlignment = Alignment.Center,
            modifier = Modifier
                .size(88.dp)
                .clip(CircleShape)
                .background(Color(member.accentColorHex))
                .border(3.dp, Color(member.borderColorHex), CircleShape)
        ) {
            if (member.avatarRes != R.drawable.avatar_placeholder) {
                Image(
                    painter = painterResource(id = member.avatarRes),
                    contentDescription = member.fullName,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .fillMaxSize()
                        .clip(CircleShape)
                )
            } else {
                val badgeCode = remember(member.id) {
                    val number = member.id.substringAfterLast("-", "0")
                    "K$number"
                }
                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                    Icon(
                        imageVector = Icons.Outlined.Person,
                        contentDescription = null,
                        tint = DeepInkNavy,
                        modifier = Modifier.size(32.dp)
                    )
                    Text(
                        text = badgeCode,
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = DeepInkNavy
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(14.dp))

        Text(
            text = member.fullName,
            style = MaterialTheme.typography.titleLarge,
            color = DeepInkNavy,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(6.dp))

        Surface(
            color = if (member.isLeader) ButterYellowSurface else SkyPastelBadge,
            shape = RoundedCornerShape(10.dp),
            modifier = Modifier.border(
                width = 1.dp,
                color = if (member.isLeader) SunnyMangoBorder else ElectricSkyBlue,
                shape = RoundedCornerShape(10.dp)
            )
        ) {
            Text(
                text = member.role,
                style = MaterialTheme.typography.labelMedium,
                color = if (member.isLeader) SunnyMangoInk else ElectricSkyBlueDark,
                modifier = Modifier.padding(horizontal = 14.dp, vertical = 6.dp)
            )
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Kartu rincian biodata anggota dengan ikon semantik dan data riil (FR-05, R-04).
        Card(
            colors = CardDefaults.cardColors(containerColor = SlateCardSurface),
            shape = RoundedCornerShape(18.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, ChartLineSlate.copy(alpha = 0.45f), RoundedCornerShape(18.dp))
        ) {
            Column(
                modifier = Modifier.padding(16.dp),
                verticalArrangement = Arrangement.spacedBy(12.dp)
            ) {
                BiodataAttributeRow(
                    icon = Icons.Outlined.Badge,
                    label = "NIM",
                    value = member.nim
                )
                HorizontalDivider(color = ChartLineSlate.copy(alpha = 0.25f))
                BiodataAttributeRow(
                    icon = Icons.Outlined.School,
                    label = "Program Studi",
                    value = member.major
                )
                HorizontalDivider(color = ChartLineSlate.copy(alpha = 0.25f))
                BiodataAttributeRow(
                    icon = Icons.Outlined.Phone,
                    label = "Nomor WhatsApp",
                    value = member.phoneNumber
                )
                HorizontalDivider(color = ChartLineSlate.copy(alpha = 0.25f))
                BiodataAttributeRow(
                    icon = Icons.Outlined.Email,
                    label = "Alamat Email",
                    value = member.email
                )
            }
        }

        actionFeedback?.let { message ->
            Spacer(modifier = Modifier.height(14.dp))
            Surface(
                color = ButterYellowSurface,
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, SunnyMangoBorder, RoundedCornerShape(12.dp))
            ) {
                Text(
                    text = message,
                    style = MaterialTheme.typography.labelMedium,
                    color = DeepInkNavy,
                    modifier = Modifier.padding(12.dp)
                )
            }
        }

        Spacer(modifier = Modifier.height(18.dp))

        // Kontrol interaktif fungsional: Salin NIM, Hubungi WhatsApp, dan Kirim Email (FR-05, R-26).
        FlowRow(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(10.dp),
            verticalArrangement = Arrangement.spacedBy(10.dp)
        ) {
            FilledTonalButton(
                onClick = {
                    val clipboard = context.getSystemService(ClipboardManager::class.java)
                    clipboard?.setPrimaryClip(ClipData.newPlainText("NIM Kelompok 3", member.nim))
                    actionFeedback = "NIM ${member.nickName} (${member.nim}) telah disalin ke papan klip."
                },
                colors = ButtonDefaults.filledTonalButtonColors(
                    containerColor = SkyPastelBadge,
                    contentColor = ElectricSkyBlueDark
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.ContentCopy,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.size(6.dp))
                Text(
                    text = "Salin NIM",
                    fontWeight = FontWeight.Bold
                )
            }

            OutlinedButton(
                onClick = {
                    val formattedNumber = if (member.phoneNumber.startsWith("0")) {
                        "62" + member.phoneNumber.substring(1)
                    } else {
                        member.phoneNumber
                    }
                    try {
                        uriHandler.openUri("https://wa.me/$formattedNumber")
                    } catch (e: Exception) {
                        val clipboard = context.getSystemService(ClipboardManager::class.java)
                        clipboard?.setPrimaryClip(ClipData.newPlainText("No WA", member.phoneNumber))
                        actionFeedback = "Nomor WhatsApp ${member.phoneNumber} disalin ke papan klip."
                    }
                },
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier
                    .weight(1f)
                    .height(48.dp)
            ) {
                Icon(
                    imageVector = Icons.AutoMirrored.Outlined.Chat,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.size(6.dp))
                Text(
                    text = "WhatsApp",
                    color = DeepInkNavy,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        Spacer(modifier = Modifier.height(10.dp))

        OutlinedButton(
            onClick = {
                try {
                    uriHandler.openUri("mailto:${member.email}")
                } catch (e: Exception) {
                    val clipboard = context.getSystemService(ClipboardManager::class.java)
                    clipboard?.setPrimaryClip(ClipData.newPlainText("Email", member.email))
                    actionFeedback = "Alamat email ${member.email} disalin ke papan klip."
                }
            },
            shape = RoundedCornerShape(12.dp),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Email,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.size(6.dp))
            Text(
                text = "Kirim Email ke ${member.nickName}",
                color = DeepInkNavy,
                fontWeight = FontWeight.SemiBold
            )
        }

        Spacer(modifier = Modifier.height(12.dp))

        Button(
            onClick = onClose,
            shape = RoundedCornerShape(14.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = ElectricSkyBlue,
                contentColor = PureWhiteSurface
            ),
            modifier = Modifier
                .fillMaxWidth()
                .height(48.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.Close,
                contentDescription = null,
                modifier = Modifier.size(18.dp)
            )
            Spacer(modifier = Modifier.size(8.dp))
            Text(
                text = "Tutup Biodata Anggota",
                fontWeight = FontWeight.Bold
            )
        }

        Spacer(modifier = Modifier.height(16.dp))
    }
}

@Composable
private fun BiodataAttributeRow(
    icon: ImageVector,
    label: String,
    value: String
) {
    Row(
        modifier = Modifier.fillMaxWidth(),
        verticalAlignment = Alignment.Top,
        horizontalArrangement = Arrangement.spacedBy(10.dp)
    ) {
        Icon(
            imageVector = icon,
            contentDescription = null,
            tint = ElectricSkyBlueDark,
            modifier = Modifier
                .padding(top = 2.dp)
                .size(18.dp)
        )
        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(2.dp)
        ) {
            Text(
                text = label,
                style = MaterialTheme.typography.labelMedium,
                color = MutedSlate
            )
            Text(
                text = value,
                style = MaterialTheme.typography.bodyMedium,
                fontWeight = FontWeight.SemiBold,
                color = DeepInkNavy
            )
        }
    }
}
