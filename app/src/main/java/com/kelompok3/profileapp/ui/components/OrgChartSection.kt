package com.kelompok3.profileapp.ui.components

import androidx.compose.foundation.Canvas
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.widthIn
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.geometry.Offset
import androidx.compose.ui.graphics.Path
import androidx.compose.ui.graphics.StrokeCap
import androidx.compose.ui.graphics.drawscope.Stroke
import androidx.compose.ui.unit.dp
import com.kelompok3.profileapp.model.MemberProfile
import com.kelompok3.profileapp.ui.theme.ChartLineSlate

@Composable
fun OrgChartSection(
    members: List<MemberProfile>,
    activeRoleFilter: String,
    onMemberClick: (MemberProfile) -> Unit,
    modifier: Modifier = Modifier
) {
    val leader = members.firstOrNull { it.isLeader } ?: members.firstOrNull()
    val teamMembers = members.filter { it.id != leader?.id }

    // Reflows across 3 layout states based on content width: 1-column (<340dp), 2-column (340-699dp), 4-column (>=700dp) (R-03).
    BoxWithConstraints(modifier = modifier.fillMaxWidth()) {
        val columns = when {
            maxWidth < 340.dp -> 1
            maxWidth < 700.dp -> 2
            else -> 4
        }

        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            modifier = Modifier.fillMaxWidth()
        ) {
            leader?.let { leaderProfile ->
                val isHighlighted = activeRoleFilter != "Semua" &&
                    leaderProfile.roleCategory.equals(activeRoleFilter, ignoreCase = true)

                MemberNodeCard(
                    member = leaderProfile,
                    isRoleHighlighted = isHighlighted,
                    onClick = { onMemberClick(leaderProfile) },
                    modifier = Modifier
                        .widthIn(max = 380.dp)
                        .fillMaxWidth()
                )
            }

            if (teamMembers.isNotEmpty()) {
                when (columns) {
                    1 -> SingleColumnOrgBranches(
                        teamMembers = teamMembers,
                        activeRoleFilter = activeRoleFilter,
                        onMemberClick = onMemberClick
                    )
                    2 -> TwoColumnOrgBranches(
                        teamMembers = teamMembers,
                        activeRoleFilter = activeRoleFilter,
                        onMemberClick = onMemberClick
                    )
                    else -> FourColumnOrgBranches(
                        teamMembers = teamMembers,
                        activeRoleFilter = activeRoleFilter,
                        onMemberClick = onMemberClick
                    )
                }
            }
        }
    }
}

@Composable
private fun SingleColumnOrgBranches(
    teamMembers: List<MemberProfile>,
    activeRoleFilter: String,
    onMemberClick: (MemberProfile) -> Unit
) {
    teamMembers.forEach { member ->
        VerticalSpineConnector(heightDp = 24)
        val isHighlighted = activeRoleFilter != "Semua" &&
            member.roleCategory.equals(activeRoleFilter, ignoreCase = true)
        MemberNodeCard(
            member = member,
            isRoleHighlighted = isHighlighted,
            onClick = { onMemberClick(member) },
            modifier = Modifier.fillMaxWidth()
        )
    }
}

@Composable
private fun TwoColumnOrgBranches(
    teamMembers: List<MemberProfile>,
    activeRoleFilter: String,
    onMemberClick: (MemberProfile) -> Unit
) {
    // Bezier connector splits from Leader apex into left and right branch columns (FR-03).
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(44.dp)
    ) {
        val strokePx = 3.dp.toPx()
        val dotRadius = 4.5.dp.toPx()
        val centerX = size.width / 2f
        val leftX = size.width * 0.25f
        val rightX = size.width * 0.75f
        val midY = size.height * 0.5f

        val path = Path().apply {
            moveTo(centerX, 0f)
            cubicTo(centerX, midY, leftX, midY * 0.6f, leftX, size.height)
            moveTo(centerX, 0f)
            cubicTo(centerX, midY, rightX, midY * 0.6f, rightX, size.height)
        }

        drawPath(
            path = path,
            color = ChartLineSlate,
            style = Stroke(width = strokePx, cap = StrokeCap.Round)
        )
        drawCircle(color = ChartLineSlate, radius = dotRadius, center = Offset(centerX, 2.dp.toPx()))
        drawCircle(color = ChartLineSlate, radius = dotRadius, center = Offset(leftX, size.height - 2.dp.toPx()))
        drawCircle(color = ChartLineSlate, radius = dotRadius, center = Offset(rightX, size.height - 2.dp.toPx()))
    }

    val rows = teamMembers.chunked(2)
    rows.forEachIndexed { rowIndex, rowItems ->
        Row(
            modifier = Modifier.fillMaxWidth(),
            horizontalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            rowItems.forEach { member ->
                val isHighlighted = activeRoleFilter != "Semua" &&
                    member.roleCategory.equals(activeRoleFilter, ignoreCase = true)
                MemberNodeCard(
                    member = member,
                    isRoleHighlighted = isHighlighted,
                    onClick = { onMemberClick(member) },
                    modifier = Modifier.weight(1f)
                )
            }
            if (rowItems.size == 1) {
                Spacer(modifier = Modifier.weight(1f))
            }
        }

        if (rowIndex < rows.lastIndex) {
            Canvas(
                modifier = Modifier
                    .fillMaxWidth()
                    .height(26.dp)
            ) {
                val strokePx = 3.dp.toPx()
                val dotRadius = 4.dp.toPx()
                val leftX = size.width * 0.25f
                val rightX = size.width * 0.75f

                drawLine(
                    color = ChartLineSlate,
                    start = Offset(leftX, 0f),
                    end = Offset(leftX, size.height),
                    strokeWidth = strokePx,
                    cap = StrokeCap.Round
                )
                drawCircle(color = ChartLineSlate, radius = dotRadius, center = Offset(leftX, size.height - 2.dp.toPx()))

                if (rows[rowIndex + 1].size > 1) {
                    drawLine(
                        color = ChartLineSlate,
                        start = Offset(rightX, 0f),
                        end = Offset(rightX, size.height),
                        strokeWidth = strokePx,
                        cap = StrokeCap.Round
                    )
                    drawCircle(color = ChartLineSlate, radius = dotRadius, center = Offset(rightX, size.height - 2.dp.toPx()))
                }
            }
        }
    }
}

@Composable
private fun FourColumnOrgBranches(
    teamMembers: List<MemberProfile>,
    activeRoleFilter: String,
    onMemberClick: (MemberProfile) -> Unit
) {
    val count = teamMembers.size.coerceAtLeast(1)

    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(48.dp)
    ) {
        val strokePx = 3.dp.toPx()
        val dotRadius = 4.5.dp.toPx()
        val centerX = size.width / 2f
        val midY = size.height * 0.5f

        val path = Path()
        for (i in 0 until count) {
            val targetX = size.width * ((i + 0.5f) / count.toFloat())
            path.moveTo(centerX, 0f)
            path.cubicTo(centerX, midY, targetX, midY, targetX, size.height)
            drawCircle(
                color = ChartLineSlate,
                radius = dotRadius,
                center = Offset(targetX, size.height - 2.dp.toPx())
            )
        }
        drawPath(
            path = path,
            color = ChartLineSlate,
            style = Stroke(width = strokePx, cap = StrokeCap.Round)
        )
        drawCircle(color = ChartLineSlate, radius = dotRadius, center = Offset(centerX, 2.dp.toPx()))
    }

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        teamMembers.forEach { member ->
            val isHighlighted = activeRoleFilter != "Semua" &&
                member.roleCategory.equals(activeRoleFilter, ignoreCase = true)
            MemberNodeCard(
                member = member,
                isRoleHighlighted = isHighlighted,
                onClick = { onMemberClick(member) },
                modifier = Modifier.weight(1f)
            )
        }
    }
}

@Composable
private fun VerticalSpineConnector(heightDp: Int) {
    Canvas(
        modifier = Modifier
            .fillMaxWidth()
            .height(heightDp.dp)
    ) {
        val centerX = size.width / 2f
        drawLine(
            color = ChartLineSlate,
            start = Offset(centerX, 0f),
            end = Offset(centerX, size.height),
            strokeWidth = 3.dp.toPx(),
            cap = StrokeCap.Round
        )
    }
}
