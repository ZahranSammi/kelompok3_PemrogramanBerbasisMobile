package com.kelompok3.profileapp.ui.screens

import androidx.lifecycle.ViewModel
import com.kelompok3.profileapp.data.Kelompok3Repository
import com.kelompok3.profileapp.model.MemberProfile
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update

enum class DataViewStatus {
    READY,
    LOADING,
    EMPTY,
    ERROR
}

data class HomeUiState(
    val status: DataViewStatus = DataViewStatus.READY,
    val members: List<MemberProfile> = Kelompok3Repository.getMembers(),
    val roleCategories: List<String> = Kelompok3Repository.roleCategories,
    val activeRoleFilter: String = "Semua",
    val selectedMember: MemberProfile? = null,
    val rotationXDeg: Float = 14f,
    val rotationYDeg: Float = -18f,
    val errorMessage: String? = null
)

class HomeViewModel : ViewModel() {

    private val _uiState = MutableStateFlow(HomeUiState())
    val uiState: StateFlow<HomeUiState> = _uiState.asStateFlow()

    fun selectMember(member: MemberProfile?) {
        _uiState.update { it.copy(selectedMember = member) }
    }

    fun selectRoleFilter(category: String) {
        _uiState.update { it.copy(activeRoleFilter = category) }
    }

    fun updateRotation(rotX: Float, rotY: Float) {
        _uiState.update {
            it.copy(
                rotationXDeg = rotX.coerceIn(-45f, 45f),
                rotationYDeg = rotY.coerceIn(-65f, 65f)
            )
        }
    }

    fun rotateStep() {
        _uiState.update { current ->
            val nextY = if (current.rotationYDeg >= 35f) -35f else current.rotationYDeg + 25f
            val nextX = if (current.rotationXDeg >= 20f) -12f else current.rotationXDeg + 10f
            current.copy(rotationXDeg = nextX, rotationYDeg = nextY)
        }
    }

    fun resetRotation() {
        _uiState.update {
            it.copy(rotationXDeg = 14f, rotationYDeg = -18f)
        }
    }

    // Allows verifying Empty, Loading, and Error resilience states directly in the UI (R-27).
    fun setDataViewStatus(status: DataViewStatus) {
        _uiState.update { current ->
            when (status) {
                DataViewStatus.READY -> current.copy(
                    status = DataViewStatus.READY,
                    members = Kelompok3Repository.getMembers(),
                    errorMessage = null
                )
                DataViewStatus.LOADING -> current.copy(
                    status = DataViewStatus.LOADING,
                    selectedMember = null
                )
                DataViewStatus.EMPTY -> current.copy(
                    status = DataViewStatus.EMPTY,
                    members = emptyList(),
                    selectedMember = null
                )
                DataViewStatus.ERROR -> current.copy(
                    status = DataViewStatus.ERROR,
                    selectedMember = null,
                    errorMessage = "Gagal membaca bundel profil lokal Kelompok 3."
                )
            }
        }
    }
}
