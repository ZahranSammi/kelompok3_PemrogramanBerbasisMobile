package com.kelompok3.profileapp.ui.components

import androidx.compose.animation.core.Spring
import androidx.compose.animation.core.animateFloatAsState
import androidx.compose.animation.core.spring
import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.interaction.MutableInteractionSource
import androidx.compose.foundation.interaction.collectIsFocusedAsState
import androidx.compose.foundation.interaction.collectIsPressedAsState
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.kelompok3.profileapp.R
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.Groups
import androidx.compose.material.icons.outlined.Person
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.remember
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.semantics.Role
import androidx.compose.ui.semantics.contentDescription
import androidx.compose.ui.semantics.semantics
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.kelompok3.profileapp.model.MemberProfile
import com.kelompok3.profileapp.ui.theme.BubblegumCoralAccent
import com.kelompok3.profileapp.ui.theme.BubblegumCoralInk
import com.kelompok3.profileapp.ui.theme.BubblegumCoralTint
import com.kelompok3.profileapp.ui.theme.DeepInkNavy
import com.kelompok3.profileapp.ui.theme.ElectricSkyBlueDark
import com.kelompok3.profileapp.ui.theme.MutedSlate
import com.kelompok3.profileapp.ui.theme.PureWhiteSurface
import com.kelompok3.profileapp.ui.theme.SkyPastelBadge
import com.kelompok3.profileapp.ui.theme.SunnyMangoInk

@Composable
fun MemberNodeCard(
    member: MemberProfile,
    isRoleHighlighted: Boolean,
    onClick: () -> Unit,
    modifier: Modifier = Modifier
) {
    val interactionSource = remember { MutableInteractionSource() }
    val isPressed by interactionSource.collectIsPressedAsState()
    val isFocused by interactionSource.collectIsFocusedAsState()

    // Tactile press feedback scales the card down to 0.95f and springs back on release (FR-04, R-19).
    val scale by animateFloatAsState(
        targetValue = if (isPressed) 0.95f else 1f,
        animationSpec = spring(
            dampingRatio = Spring.DampingRatioMediumBouncy,
            stiffness = Spring.StiffnessMedium
        ),
        label = "nodeScale"
    )

    val borderColor = when {
        isFocused -> ElectricSkyBlueDark
        isRoleHighlighted -> BubblegumCoralAccent
        else -> Color(member.borderColorHex)
    }
    val borderWidth = if (isFocused || isRoleHighlighted || member.isLeader) 2.5.dp else 1.5.dp
    val shape = RoundedCornerShape(if (member.isLeader) 22.dp else 18.dp)

    // Leader card uses a wider horizontal layout while member nodes use compact vertical cards to reflect org hierarchy (R-14).
    Surface(
        color = Color(member.accentColorHex),
        shape = shape,
        shadowElevation = if (member.isLeader) 4.dp else 1.dp,
        modifier = modifier
            .graphicsLayer {
                scaleX = scale
                scaleY = scale
            }
            .heightIn(min = 88.dp)
            .clip(shape)
            .border(borderWidth, borderColor, shape)
            .clickable(
                interactionSource = interactionSource,
                indication = androidx.compose.material3.ripple(),
                role = Role.Button,
                onClickLabel = "Buka biodata ${member.nickName}",
                onClick = onClick
            )
            .semantics {
                contentDescription = "${member.nickName}, peran ${member.role}. Ketuk untuk membuka detail biodata."
            }
    ) {
        if (member.isLeader) {
            LeaderCardContent(
                member = member,
                isRoleHighlighted = isRoleHighlighted
            )
        } else {
            TeamMemberCardContent(
                member = member,
                isRoleHighlighted = isRoleHighlighted
            )
        }
    }
}

@Composable
private fun LeaderCardContent(
    member: MemberProfile,
    isRoleHighlighted: Boolean
) {
    Row(
        verticalAlignment = Alignment.CenterVertically,
        horizontalArrangement = Arrangement.spacedBy(14.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 16.dp, vertical = 14.dp)
    ) {
        InitialAvatarBadge(
            member = member,
            sizeDp = 52
        )

        Column(
            modifier = Modifier.weight(1f),
            verticalArrangement = Arrangement.spacedBy(4.dp)
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(6.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Groups,
                    contentDescription = null,
                    tint = SunnyMangoInk,
                    modifier = Modifier.size(16.dp)
                )
                Text(
                    text = "PUNCAK BAGAN (KETUA)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SunnyMangoInk
                )
            }

            Text(
                text = member.nickName,
                style = MaterialTheme.typography.titleMedium,
                color = DeepInkNavy
            )

            Text(
                text = member.role,
                style = MaterialTheme.typography.labelMedium,
                color = MutedSlate
            )

            Spacer(modifier = Modifier.height(2.dp))

            Row(
                horizontalArrangement = Arrangement.spacedBy(6.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                ActionLabelChip(text = "Buka Biodata")
                if (isRoleHighlighted) {
                    HighlightStatusChip()
                }
            }
        }
    }
}

@Composable
private fun TeamMemberCardContent(
    member: MemberProfile,
    isRoleHighlighted: Boolean
) {
    Column(
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(6.dp),
        modifier = Modifier
            .fillMaxWidth()
            .padding(horizontal = 12.dp, vertical = 14.dp)
    ) {
        InitialAvatarBadge(
            member = member,
            sizeDp = 44
        )

        Text(
            text = member.nickName,
            style = MaterialTheme.typography.titleMedium,
            color = DeepInkNavy,
            textAlign = TextAlign.Center
        )

        Text(
            text = member.role,
            style = MaterialTheme.typography.labelMedium,
            color = MutedSlate,
            textAlign = TextAlign.Center
        )

        Spacer(modifier = Modifier.height(2.dp))

        ActionLabelChip(text = "Buka Biodata")

        if (isRoleHighlighted) {
            HighlightStatusChip()
        }
    }
}

@Composable
private fun InitialAvatarBadge(
    member: MemberProfile,
    sizeDp: Int
) {
    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier
            .size(sizeDp.dp)
            .clip(CircleShape)
            .background(PureWhiteSurface)
            .border(2.dp, Color(member.borderColorHex), CircleShape)
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
                    modifier = Modifier.size((sizeDp * 0.36f).dp)
                )
                Text(
                    text = badgeCode,
                    fontSize = 11.sp,
                    fontWeight = FontWeight.ExtraBold,
                    color = DeepInkNavy
                )
            }
        }
    }
}

@Composable
private fun ActionLabelChip(text: String) {
    Surface(
        color = SkyPastelBadge,
        shape = RoundedCornerShape(8.dp)
    ) {
        Text(
            text = text,
            fontSize = 11.sp,
            fontWeight = FontWeight.Bold,
            color = ElectricSkyBlueDark,
            modifier = Modifier.padding(horizontal = 10.dp, vertical = 4.dp)
        )
    }
}

@Composable
private fun HighlightStatusChip() {
    // Pairs color highlight with an explicit text label so status is never communicated by color alone (C-4).
    Surface(
        color = BubblegumCoralTint,
        shape = RoundedCornerShape(8.dp),
        modifier = Modifier.border(1.dp, BubblegumCoralAccent, RoundedCornerShape(8.dp))
    ) {
        Text(
            text = "Cocok Filter",
            fontSize = 10.sp,
            fontWeight = FontWeight.Bold,
            color = BubblegumCoralInk,
            modifier = Modifier.padding(horizontal = 8.dp, vertical = 2.dp)
        )
    }
}
