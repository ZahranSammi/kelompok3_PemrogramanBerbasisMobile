package com.kelompok3.profileapp.ui.screens

import androidx.compose.foundation.border
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.BoxWithConstraints
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.heightIn
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.outlined.ErrorOutline
import androidx.compose.material.icons.outlined.FilterList
import androidx.compose.material.icons.outlined.FolderOff
import androidx.compose.material.icons.outlined.Refresh
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.kelompok3.profileapp.ui.components.Hero3DSection
import com.kelompok3.profileapp.ui.components.Kelompok3Header
import com.kelompok3.profileapp.ui.components.MemberDetailContent
import com.kelompok3.profileapp.ui.components.OrgChartSection
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
import com.kelompok3.profileapp.ui.theme.SunnyMangoBorder
import com.kelompok3.profileapp.ui.theme.WarmCreamBg

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    viewModel: HomeViewModel = viewModel(),
    onReplayIntro: (() -> Unit)? = null
) {
    val uiState by viewModel.uiState.collectAsState()
    val scrollState = rememberScrollState()
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Scaffold(
        containerColor = WarmCreamBg
    ) { innerPadding ->
        // Adapts layout across phone (<600dp), tablet/foldable (600-839dp), and wide desktop (>=840dp) (R-03).
        BoxWithConstraints(
            modifier = Modifier
                .fillMaxSize()
                .padding(innerPadding)
        ) {
            val isExpanded = maxWidth >= 840.dp
            val isMedium = maxWidth in 600.dp..839.dp
            val horizontalPad = when {
                isExpanded -> 32.dp
                isMedium -> 24.dp
                else -> 16.dp
            }
            val verticalPad = if (isExpanded || isMedium) 24.dp else 16.dp

            if (isExpanded) {
                Row(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(horizontal = horizontalPad, vertical = verticalPad),
                    horizontalArrangement = Arrangement.spacedBy(28.dp),
                    verticalAlignment = Alignment.Top
                ) {
                    Column(
                        modifier = Modifier.weight(0.42f),
                        verticalArrangement = Arrangement.spacedBy(20.dp)
                    ) {
                        Kelompok3Header(
                            memberCount = uiState.members.size,
                            onReplayIntro = onReplayIntro
                        )
                        Hero3DSection(
                            rotationXDeg = uiState.rotationXDeg,
                            rotationYDeg = uiState.rotationYDeg,
                            onDragRotate = viewModel::updateRotation,
                            onRotateStep = viewModel::rotateStep,
                            onResetRotation = viewModel::resetRotation
                        )
                        UiStateSwitcherSection(
                            currentStatus = uiState.status,
                            onStatusChange = viewModel::setDataViewStatus
                        )
                    }

                    Column(
                        modifier = Modifier.weight(0.58f),
                        verticalArrangement = Arrangement.spacedBy(18.dp)
                    ) {
                        OrgChartWorkspaceSection(
                            uiState = uiState,
                            onFilterSelect = viewModel::selectRoleFilter,
                            onMemberClick = viewModel::selectMember,
                            onReloadData = { viewModel.setDataViewStatus(DataViewStatus.READY) }
                        )
                    }
                }
            } else {
                Column(
                    modifier = Modifier
                        .fillMaxSize()
                        .verticalScroll(scrollState)
                        .padding(horizontal = horizontalPad, vertical = verticalPad),
                    verticalArrangement = Arrangement.spacedBy(if (isMedium) 24.dp else 18.dp)
                ) {
                    Kelompok3Header(
                        memberCount = uiState.members.size,
                        onReplayIntro = onReplayIntro
                    )

                    Hero3DSection(
                        rotationXDeg = uiState.rotationXDeg,
                        rotationYDeg = uiState.rotationYDeg,
                        onDragRotate = viewModel::updateRotation,
                        onRotateStep = viewModel::rotateStep,
                        onResetRotation = viewModel::resetRotation
                    )

                    OrgChartWorkspaceSection(
                        uiState = uiState,
                        onFilterSelect = viewModel::selectRoleFilter,
                        onMemberClick = viewModel::selectMember,
                        onReloadData = { viewModel.setDataViewStatus(DataViewStatus.READY) }
                    )

                    UiStateSwitcherSection(
                        currentStatus = uiState.status,
                        onStatusChange = viewModel::setDataViewStatus
                    )
                }
            }
        }

        uiState.selectedMember?.let { member ->
            ModalBottomSheet(
                onDismissRequest = { viewModel.selectMember(null) },
                sheetState = sheetState,
                containerColor = PureWhiteSurface,
                shape = RoundedCornerShape(topStart = 28.dp, topEnd = 28.dp)
            ) {
                MemberDetailContent(
                    member = member,
                    onClose = { viewModel.selectMember(null) }
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun OrgChartWorkspaceSection(
    uiState: HomeUiState,
    onFilterSelect: (String) -> Unit,
    onMemberClick: (com.kelompok3.profileapp.model.MemberProfile) -> Unit,
    onReloadData: () -> Unit
) {
    Column(
        modifier = Modifier.fillMaxWidth(),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        Row(
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.FilterList,
                contentDescription = null,
                tint = ElectricSkyBlueDark,
                modifier = Modifier.size(20.dp)
            )
            Text(
                text = "Bagan Struktur & Filter Peran",
                style = MaterialTheme.typography.titleMedium,
                color = DeepInkNavy
            )
        }

        Text(
            text = "Pilih filter peran untuk menyoroti anggota pada bagan, atau ketuk kartu anggota untuk membuka biodata lengkap.",
            style = MaterialTheme.typography.bodyMedium,
            color = MutedSlate
        )

        // Role filter chips with 48dp minimum touch targets for comfortable thumb use (FR-06, R-03).
        FlowRow(
            horizontalArrangement = Arrangement.spacedBy(8.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp),
            modifier = Modifier.fillMaxWidth()
        ) {
            uiState.roleCategories.forEach { category ->
                val selected = uiState.activeRoleFilter == category
                FilterChip(
                    selected = selected,
                    onClick = { onFilterSelect(category) },
                    label = {
                        Text(
                            text = category,
                            fontWeight = if (selected) FontWeight.Bold else FontWeight.SemiBold
                        )
                    },
                    colors = FilterChipDefaults.filterChipColors(
                        containerColor = PureWhiteSurface,
                        labelColor = DeepInkNavy,
                        selectedContainerColor = BubblegumCoralTint,
                        selectedLabelColor = BubblegumCoralInk
                    ),
                    border = FilterChipDefaults.filterChipBorder(
                        enabled = true,
                        selected = selected,
                        borderColor = ChartLineSlate,
                        selectedBorderColor = BubblegumCoralAccent,
                        borderWidth = 1.dp,
                        selectedBorderWidth = 2.dp
                    ),
                    modifier = Modifier.heightIn(min = 48.dp)
                )
            }
        }

        when (uiState.status) {
            DataViewStatus.READY -> {
                if (uiState.members.isEmpty()) {
                    EmptyChartStateCard(onReload = onReloadData)
                } else {
                    OrgChartSection(
                        members = uiState.members,
                        activeRoleFilter = uiState.activeRoleFilter,
                        onMemberClick = onMemberClick
                    )
                }
            }
            DataViewStatus.LOADING -> LoadingChartStateCard(onFinishLoading = onReloadData)
            DataViewStatus.EMPTY -> EmptyChartStateCard(onReload = onReloadData)
            DataViewStatus.ERROR -> ErrorChartStateCard(
                errorMessage = uiState.errorMessage ?: "Terjadi kendala saat memuat data.",
                onRetry = onReloadData
            )
        }
    }
}

@Composable
private fun LoadingChartStateCard(onFinishLoading: () -> Unit) {
    Surface(
        color = PureWhiteSurface,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, ChartLineSlate, RoundedCornerShape(20.dp))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(12.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            CircularProgressIndicator(color = ElectricSkyBlue)
            Text(
                text = "Memuat struktur bagan dan biodata Kelompok 3...",
                style = MaterialTheme.typography.titleMedium,
                color = DeepInkNavy,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Menyiapkan 5 node anggota beserta jalur konektor bagan.",
                style = MaterialTheme.typography.bodyMedium,
                color = MutedSlate,
                textAlign = TextAlign.Center
            )
            Button(
                onClick = onFinishLoading,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ElectricSkyBlue,
                    contentColor = PureWhiteSurface
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(48.dp)
            ) {
                Text(
                    text = "Tampilkan Bagan Sekarang",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun EmptyChartStateCard(onReload: () -> Unit) {
    Surface(
        color = ButterYellowSurface,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.5.dp, SunnyMangoBorder, RoundedCornerShape(20.dp))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.FolderOff,
                contentDescription = null,
                tint = DeepInkNavy,
                modifier = Modifier.size(32.dp)
            )
            Text(
                text = "Belum Ada Data Anggota yang Ditampilkan",
                style = MaterialTheme.typography.titleMedium,
                color = DeepInkNavy,
                textAlign = TextAlign.Center
            )
            Text(
                text = "Daftar anggota Kelompok 3 saat ini kosong. Muat ulang data repositori lokal untuk menampilkan bagan struktur tim.",
                style = MaterialTheme.typography.bodyMedium,
                color = MutedSlate,
                textAlign = TextAlign.Center
            )
            Button(
                onClick = onReload,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ElectricSkyBlue,
                    contentColor = PureWhiteSurface
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.size(6.dp))
                Text(
                    text = "Muat Ulang Data Kelompok 3",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@Composable
private fun ErrorChartStateCard(
    errorMessage: String,
    onRetry: () -> Unit
) {
    Surface(
        color = BubblegumCoralTint,
        shape = RoundedCornerShape(20.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(2.dp, BubblegumCoralAccent, RoundedCornerShape(20.dp))
    ) {
        Column(
            horizontalAlignment = Alignment.CenterHorizontally,
            verticalArrangement = Arrangement.spacedBy(10.dp),
            modifier = Modifier.padding(24.dp)
        ) {
            Icon(
                imageVector = Icons.Outlined.ErrorOutline,
                contentDescription = null,
                tint = BubblegumCoralInk,
                modifier = Modifier.size(32.dp)
            )
            Text(
                text = "Gagal Memuat Bagan Anggota",
                style = MaterialTheme.typography.titleMedium,
                color = DeepInkNavy,
                textAlign = TextAlign.Center
            )
            Text(
                text = "$errorMessage Tekan tombol di bawah untuk memulihkan data dari sumber lokal.",
                style = MaterialTheme.typography.bodyMedium,
                color = DeepInkNavy,
                textAlign = TextAlign.Center
            )
            Button(
                onClick = onRetry,
                colors = ButtonDefaults.buttonColors(
                    containerColor = ElectricSkyBlue,
                    contentColor = PureWhiteSurface
                ),
                shape = RoundedCornerShape(12.dp),
                modifier = Modifier.height(48.dp)
            ) {
                Icon(
                    imageVector = Icons.Outlined.Refresh,
                    contentDescription = null,
                    modifier = Modifier.size(18.dp)
                )
                Spacer(modifier = Modifier.size(6.dp))
                Text(
                    text = "Pulihkan Data Lokal",
                    fontWeight = FontWeight.Bold
                )
            }
        }
    }
}

@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun UiStateSwitcherSection(
    currentStatus: DataViewStatus,
    onStatusChange: (DataViewStatus) -> Unit
) {
    Surface(
        color = PureWhiteSurface,
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, ChartLineSlate.copy(alpha = 0.55f), RoundedCornerShape(16.dp))
    ) {
        Column(
            modifier = Modifier.padding(14.dp),
            verticalArrangement = Arrangement.spacedBy(8.dp)
        ) {
            Text(
                text = "Panel Pengujian Status UI (Resilience R-27)",
                style = MaterialTheme.typography.labelMedium,
                color = MutedSlate
            )
            FlowRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                val options = listOf(
                    DataViewStatus.READY to "Data Siap",
                    DataViewStatus.LOADING to "Status Memuat",
                    DataViewStatus.EMPTY to "Status Kosong",
                    DataViewStatus.ERROR to "Status Galat"
                )
                options.forEach { (status, label) ->
                    val selected = currentStatus == status
                    FilterChip(
                        selected = selected,
                        onClick = { onStatusChange(status) },
                        label = {
                            Text(
                                text = label,
                                fontWeight = if (selected) FontWeight.Bold else FontWeight.Medium
                            )
                        },
                        colors = FilterChipDefaults.filterChipColors(
                            containerColor = Color.Transparent,
                            labelColor = DeepInkNavy,
                            selectedContainerColor = SkyPastelBadge,
                            selectedLabelColor = ElectricSkyBlueDark
                        ),
                        modifier = Modifier.heightIn(min = 48.dp)
                    )
                }
            }
        }
    }
}
