package com.kelompok3.profileapp

import com.kelompok3.profileapp.ui.screens.DataViewStatus
import com.kelompok3.profileapp.ui.screens.HomeViewModel
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class HomeViewModelTest {

    @Test
    fun initialState_loadsFiveMembersAndLeaderAtApex() {
        val viewModel = HomeViewModel()
        val state = viewModel.uiState.value

        assertEquals(DataViewStatus.READY, state.status)
        assertEquals(5, state.members.size)
        assertTrue(state.members.first().isLeader)
        assertEquals("Semua", state.activeRoleFilter)
        assertNull(state.selectedMember)
    }

    @Test
    fun rotateAndResetControls_update3DOrientationWithinBounds() {
        val viewModel = HomeViewModel()

        viewModel.rotateStep()
        assertEquals(24f, viewModel.uiState.value.rotationXDeg, 0.01f)
        assertEquals(7f, viewModel.uiState.value.rotationYDeg, 0.01f)

        viewModel.updateRotation(rotX = 90f, rotY = -120f)
        assertEquals(45f, viewModel.uiState.value.rotationXDeg, 0.01f)
        assertEquals(-65f, viewModel.uiState.value.rotationYDeg, 0.01f)

        viewModel.resetRotation()
        assertEquals(14f, viewModel.uiState.value.rotationXDeg, 0.01f)
        assertEquals(-18f, viewModel.uiState.value.rotationYDeg, 0.01f)
    }

    @Test
    fun roleFilterAndMemberSelection_openAndCloseDetailSheet() {
        val viewModel = HomeViewModel()
        val firstMember = viewModel.uiState.value.members.first()

        viewModel.selectRoleFilter("Ketua")
        assertEquals("Ketua", viewModel.uiState.value.activeRoleFilter)

        viewModel.selectRoleFilter("Anggota")
        assertEquals("Anggota", viewModel.uiState.value.activeRoleFilter)
        assertEquals(3, viewModel.uiState.value.roleCategories.size)

        viewModel.selectMember(firstMember)
        assertNotNull(viewModel.uiState.value.selectedMember)
        assertEquals("member-1", viewModel.uiState.value.selectedMember?.id)

        viewModel.selectMember(null)
        assertNull(viewModel.uiState.value.selectedMember)
    }

    @Test
    fun resilienceStates_transitionThroughLoadingEmptyErrorAndReady() {
        val viewModel = HomeViewModel()

        viewModel.setDataViewStatus(DataViewStatus.LOADING)
        assertEquals(DataViewStatus.LOADING, viewModel.uiState.value.status)

        viewModel.setDataViewStatus(DataViewStatus.EMPTY)
        assertEquals(DataViewStatus.EMPTY, viewModel.uiState.value.status)
        assertTrue(viewModel.uiState.value.members.isEmpty())

        viewModel.setDataViewStatus(DataViewStatus.ERROR)
        assertEquals(DataViewStatus.ERROR, viewModel.uiState.value.status)
        assertNotNull(viewModel.uiState.value.errorMessage)

        viewModel.setDataViewStatus(DataViewStatus.READY)
        assertEquals(DataViewStatus.READY, viewModel.uiState.value.status)
        assertEquals(5, viewModel.uiState.value.members.size)
    }
}
