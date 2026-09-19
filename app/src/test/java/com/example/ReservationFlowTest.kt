package com.example

import android.app.Application
import androidx.test.core.app.ApplicationProvider
import com.example.data.db.AppDatabase
import com.example.data.model.CreneauStatut
import com.example.data.repository.AdminManager
import com.example.data.repository.SalonRepository
import com.example.ui.viewmodel.BookingViewModel
import com.example.ui.viewmodel.ReservationMode
import kotlinx.coroutines.ExperimentalCoroutinesApi
import kotlinx.coroutines.test.runTest
import org.junit.Assert.assertEquals
import org.junit.Assert.assertNotNull
import org.junit.Assert.assertTrue
import org.junit.Before
import org.junit.Test
import org.junit.runner.RunWith
import org.robolectric.RobolectricTestRunner
import org.robolectric.annotation.Config

@OptIn(ExperimentalCoroutinesApi::class)
@RunWith(RobolectricTestRunner::class)
@Config(sdk = [36])
class ReservationFlowTest {

    private lateinit var app: Application
    private lateinit var repository: SalonRepository

    @Before
    fun setUp() {
        app = ApplicationProvider.getApplicationContext()
        val db = AppDatabase.getDatabase(app)
        val adminManager = AdminManager(app)
        repository = SalonRepository(db.reservationDao(), adminManager)
    }

    @Test
    fun testRepositorySalonsAndServices() {
        val salons = repository.getSalons()
        assertEquals(3, salons.size)
        assertTrue(salons.any { it.id == "yopougon" })
        assertTrue(salons.any { it.id == "treichville" })
        assertTrue(salons.any { it.id == "angre" })

        val services = repository.getServices()
        assertTrue(services.isNotEmpty())
        assertTrue(services.any { it.nom.contains("Tresses", ignoreCase = true) })
    }

    @Test
    fun testCalendarDateSlotGeneration() {
        // Test dynamic slot generation for any calendar date
        val slots = repository.getCreneaux("angre", "2026-09-25")
        assertTrue(slots.isNotEmpty())
        assertTrue(slots.any { it.heureDebut == "09h00" })
    }

    @Test
    fun testReservationViewModelFlow() = runTest {
        val vm = BookingViewModel(app)
        val initialUiState = vm.uiState.value

        assertEquals(ReservationMode.UNIFIED, initialUiState.reservationMode)
        assertNotNull(initialUiState.selectedSalon)
        assertNotNull(initialUiState.selectedService)

        // 1. Select specific salon (e.g. Treichville)
        val treichville = vm.salons.first { it.id == "treichville" }
        vm.selectSalonOnly(treichville)
        assertEquals("treichville", vm.uiState.value.selectedSalon?.id)

        // 2. Select specific service (e.g. Tresses africaines)
        val service = vm.services.first { it.nom.contains("Tresses", ignoreCase = true) }
        vm.selectServiceOnly(service)
        assertEquals(service.id, vm.uiState.value.selectedService?.id)

        // 3. Pick date & time slot from calendar view
        vm.selectCustomDate("Vendredi 25 Septembre 2026", "2026-09-25")
        val availableSlots = vm.getCurrentCreneaux()
        assertTrue(availableSlots.isNotEmpty())

        val freeSlot = availableSlots.first { it.statut == CreneauStatut.LIBRE }
        vm.selectCreneau(freeSlot)
        assertEquals(freeSlot.id, vm.uiState.value.selectedCreneau?.id)

        // 4. Try to proceed before registration: auth modal must open
        vm.proceedFromReservationScreen()
        assertTrue(vm.uiState.value.showAuthModal)

        // 5. Register the client
        vm.onAuthSuccess("Aïcha Diallo", "0701020304", "aicha@test.ci")
        assertTrue(vm.uiState.value.isUserRegistered)
        assertEquals(5, vm.uiState.value.currentStep)
    }
}
