package com.kelompok3.profileapp.ui.theme

import androidx.compose.ui.graphics.Color

// Neutral daylight surfaces and ink hierarchy verified for WCAG AA contrast (R-25, R-29).
val WarmCreamBg = Color(0xFFFFFDF9)
val PureWhiteSurface = Color(0xFFFFFFFF)
val SoftCloudSurface = Color(0xFFF3F6FF)
val SlateCardSurface = Color(0xFFF8FAFC)

// DeepInkNavy (#1E293B) gives 14.40:1 on WarmCreamBg; MutedSlate (#475569) gives 7.46:1 (R-25).
val DeepInkNavy = Color(0xFF1E293B)
val MutedSlate = Color(0xFF475569)
// ChartLineSlate (#64748B) ensures 4.68:1 non-text contrast for org-chart lines on WarmCreamBg.
val ChartLineSlate = Color(0xFF64748B)

// Core 1: Primary brand identity for Kelompok 3 interactive controls (5.17:1 to 6.70:1 with white).
val ElectricSkyBlue = Color(0xFF2563EB)
val ElectricSkyBlueDark = Color(0xFF1D4ED8)
val SkyPastelSurface = Color(0xFFE0F2FE)
val SkyPastelBadge = Color(0xFFDBEAFE)

// Core 2: Secondary hierarchy highlight distinguishing the team leader node in the org chart.
val SunnyMangoBorder = Color(0xFFD97706)
val SunnyMangoInk = Color(0xFF92400E)
val ButterYellowSurface = Color(0xFFFFF3C4)

// Single deliberate accent (Part 3) reserved for active filter selection and 3D stage indicator.
val BubblegumCoralAccent = Color(0xFFE11D48)
val BubblegumCoralInk = Color(0xFFBE123C)
val BubblegumCoralTint = Color(0xFFFFE4E6)
