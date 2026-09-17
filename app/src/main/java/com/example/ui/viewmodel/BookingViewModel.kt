package com.example.ui.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.viewModelScope
import com.example.data.db.AppDatabase
import com.example.data.model.AdminUser
import com.example.data.model.Creneau
import com.example.data.model.CreneauStatut
import com.example.data.model.HairService
import com.example.data.model.HairstyleItem
import com.example.data.model.MobileMoneyOperator
import com.example.data.model.ReservationEntity
import com.example.data.model.Salon
import com.example.data.model.ServiceOption
import com.example.data.repository.AdminManager
import com.example.data.repository.CrossSiteAlternative
import com.example.data.repository.SalonRepository
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.SharingStarted
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.stateIn
import kotlinx.coroutines.launch

enum class AppTab {
    RESERVER,
    MES_RDV,
    SALONS_INFO
}

data class BookingUiState(
    val currentStep: Int = 0, // 0 to 6
    val currentTab: AppTab = AppTab.RESERVER,
    val selectedSalon: Salon? = null,
    val selectedService: HairService? = null,
    val selectedOptionIds: Set<String> = emptySet(),
    val selectedDateIndex: Int = 0,
    val selectedCreneau: Creneau? = null,
    val crossSiteSuggestion: CrossSiteAlternative? = null,
    val modePaiement: String = "Paiement en caisse",
    val clientNom: String = "",
    val clientTelephone: String = "",
    val clientEmail: String = "",
    val isUserRegistered: Boolean = false,
    val showAuthModal: Boolean = false,
    val selectedCustomDateDisplay: String? = null,
    val selectedCustomDateKey: String? = null,
    val selectedTimeFilter: String? = null,
    val holdRemainingSeconds: Int = 300, // 5 min
    val isHoldActive: Boolean = false,
    val isProcessingPayment: Boolean = false,
    val paymentSuccessMessage: String? = null,
    val confirmedReservation: ReservationEntity? = null,
    val activeInfoSalon: Salon? = null,
    val showSummaryDialog: Boolean = false,
    // Search fields
    val searchQuery: String = "",
    val selectedCategoryFilter: String = "Tous",
    // Admin fields
    val showAdminLogin: Boolean = false,
    val showAdminDashboard: Boolean = false,
    val loggedAdmin: AdminUser? = null,
    val adminErrorMessage: String? = null,
    val adminSuccessMessage: String? = null,
    val isSuperAdminConfigured: Boolean = false,
    val superAdminUser: AdminUser? = null,
    val secondaryAdmins: List<AdminUser> = emptyList(),
    val hairstylesList: List<HairstyleItem> = emptyList()
) {
    val totalPrix: Int
        get() {
            val base = selectedService?.prixReference ?: 0
            return base + optionsPrix
        }

    val optionsPrix: Int
        get() = 0 // Calculated dynamically via ViewModel with option list

    val totalDureeMin: Int
        get() = (selectedService?.dureeMin ?: 0)
}

class BookingViewModel(application: Application) : AndroidViewModel(application) {
    private val adminManager: AdminManager = AdminManager(application)
    private val repository: SalonRepository

    val salons: List<Salon>
    var services: List<HairService>
        private set
    var options: List<ServiceOption>
        private set
    val availableDays: List<Pair<String, String>>

    private val _uiState = MutableStateFlow(BookingUiState())
    val uiState: StateFlow<BookingUiState> = _uiState.asStateFlow()

    val creneauxMap = MutableStateFlow<Map<String, List<Creneau>>>(emptyMap())

    val userReservations: StateFlow<List<ReservationEntity>>

    private var holdJob: Job? = null

    init {
        val db = AppDatabase.getDatabase(application)
        repository = SalonRepository(db.reservationDao(), adminManager)
        salons = repository.getSalons()
        services = repository.getServices()
        options = repository.getOptions()
        availableDays = repository.getAvailableDays()

        val isSuperConfigured = adminManager.isSuperAdminConfigured()
        val superAdmin = adminManager.getSuperAdmin()
        val secondaryAdmins = adminManager.getSecondaryAdmins()
        val hairstyles = repository.getHairstyles()

        // Preselect the first salon as default entry point
        _uiState.value = _uiState.value.copy(
            selectedSalon = salons.firstOrNull(),
            isSuperAdminConfigured = isSuperConfigured,
            superAdminUser = superAdmin,
            secondaryAdmins = secondaryAdmins,
            hairstylesList = hairstyles
        )

        userReservations = repository.getAllReservations().stateIn(
            scope = viewModelScope,
            started = SharingStarted.WhileSubscribed(5000),
            initialValue = emptyList()
        )

        viewModelScope.launch {
            repository.creneauxState.collect { map ->
                creneauxMap.value = map
            }
        }
    }

    fun setTab(tab: AppTab) {
        _uiState.value = _uiState.value.copy(currentTab = tab)
    }

    fun selectSalon(salon: Salon) {
        _uiState.value = _uiState.value.copy(
            selectedSalon = salon,
            selectedCreneau = null,
            crossSiteSuggestion = null,
            currentStep = 1 // Move to Prestation selection
        )
    }

    fun selectService(service: HairService) {
        _uiState.value = _uiState.value.copy(
            selectedService = service,
            currentStep = 2 // Move to Options
        )
    }

    fun toggleOption(optionId: String) {
        val current = _uiState.value.selectedOptionIds.toMutableSet()
        if (current.contains(optionId)) {
            current.remove(optionId)
        } else {
            current.add(optionId)
        }
        _uiState.value = _uiState.value.copy(selectedOptionIds = current)
    }

    fun goToStep(step: Int) {
        if (step in 0..6) {
            _uiState.value = _uiState.value.copy(currentStep = step)
            if (step == 5 && !_uiState.value.isHoldActive) {
                startHoldCountdown()
            }
        }
    }

    fun selectDateIndex(index: Int) {
        _uiState.value = _uiState.value.copy(
            selectedDateIndex = index,
            selectedCustomDateDisplay = null,
            selectedCustomDateKey = null,
            selectedCreneau = null,
            crossSiteSuggestion = null
        )
    }

    fun selectCustomDate(dateDisplay: String, dateKey: String) {
        _uiState.value = _uiState.value.copy(
            selectedCustomDateDisplay = dateDisplay,
            selectedCustomDateKey = dateKey,
            selectedCreneau = null,
            crossSiteSuggestion = null
        )
    }

    fun filterTimeSlot(hour: String) {
        _uiState.value = _uiState.value.copy(selectedTimeFilter = hour)
        // Check if there is an exact or matching slot
        val slots = getCurrentCreneaux()
        val matching = slots.find { it.heureDebut.startsWith(hour.substring(0, 2)) }
        if (matching != null) {
            selectCreneau(matching)
        }
    }

    fun getCurrentCreneaux(): List<Creneau> {
        val salon = _uiState.value.selectedSalon ?: return emptyList()
        val customKey = _uiState.value.selectedCustomDateKey
        val dayKey = customKey ?: (availableDays.getOrNull(_uiState.value.selectedDateIndex)?.second ?: return emptyList())
        return repository.getCreneaux(salon.id, dayKey)
    }

    fun getCurrentDateDisplay(): String {
        return _uiState.value.selectedCustomDateDisplay
            ?: (availableDays.getOrNull(_uiState.value.selectedDateIndex)?.first ?: "Date sélectionnée")
    }

    fun openAuthModal() {
        _uiState.value = _uiState.value.copy(showAuthModal = true)
    }

    fun dismissAuthModal() {
        _uiState.value = _uiState.value.copy(showAuthModal = false)
    }

    fun onAuthSuccess(nom: String, telephone: String, email: String) {
        _uiState.value = _uiState.value.copy(
            clientNom = nom,
            clientTelephone = telephone,
            clientEmail = email,
            isUserRegistered = true,
            showAuthModal = false
        )
    }

    fun logoutUser() {
        _uiState.value = _uiState.value.copy(
            isUserRegistered = false,
            clientNom = "",
            clientTelephone = "",
            clientEmail = ""
        )
    }

    fun selectCreneau(creneau: Creneau) {
        if (creneau.statut == CreneauStatut.RESERVE) {
            // Slot is unavailable! Trigger cross-site suggestion!
            val salon = _uiState.value.selectedSalon ?: return
            val dayKey = availableDays.getOrNull(_uiState.value.selectedDateIndex)?.second ?: return
            val alt = repository.findCrossSiteAlternative(salon.id, creneau.heureDebut, dayKey)
            _uiState.value = _uiState.value.copy(
                selectedCreneau = null,
                crossSiteSuggestion = alt
            )
        } else {
            _uiState.value = _uiState.value.copy(
                selectedCreneau = creneau,
                crossSiteSuggestion = null
            )
        }
    }

    /**
     * Switch salon directly when clicking the cross-site recommendation banner
     */
    fun acceptCrossSiteAlternative() {
        val alt = _uiState.value.crossSiteSuggestion ?: return
        val dayKey = availableDays.getOrNull(_uiState.value.selectedDateIndex)?.second ?: return
        val matchingSlot = repository.getCreneaux(alt.salonAlternatif.id, dayKey)
            .find { it.heureDebut == alt.heureDebut }

        _uiState.value = _uiState.value.copy(
            selectedSalon = alt.salonAlternatif,
            selectedCreneau = matchingSlot,
            crossSiteSuggestion = null
        )
    }

    fun openSummaryDialog() {
        _uiState.value = _uiState.value.copy(showSummaryDialog = true)
    }

    fun dismissSummaryDialog() {
        _uiState.value = _uiState.value.copy(showSummaryDialog = false)
    }

    fun confirmSummaryDialogAndProceed() {
        _uiState.value = _uiState.value.copy(
            showSummaryDialog = false,
            currentStep = 3 // Devis transparent or continue flow
        )
    }

    fun updateClientDetails(nom: String, telephone: String) {
        _uiState.value = _uiState.value.copy(
            clientNom = nom,
            clientTelephone = telephone
        )
    }

    fun calculateTotalPrix(): Int {
        val base = _uiState.value.selectedService?.prixReference ?: 0
        val optionsSum = options.filter { _uiState.value.selectedOptionIds.contains(it.id) }.sumOf { it.prix }
        return base + optionsSum
    }

    fun calculateTotalDuree(): Int {
        val base = _uiState.value.selectedService?.dureeMin ?: 0
        val extra = options.filter { _uiState.value.selectedOptionIds.contains(it.id) }.sumOf { it.dureeMinAjoutee }
        return base + extra
    }

    fun getSelectedOptionsList(): List<ServiceOption> {
        return options.filter { _uiState.value.selectedOptionIds.contains(it.id) }
    }

    private fun startHoldCountdown() {
        holdJob?.cancel()
        val creneau = _uiState.value.selectedCreneau ?: return
        repository.lockCreneau(creneau.id)

        _uiState.value = _uiState.value.copy(
            isHoldActive = true,
            holdRemainingSeconds = 300
        )

        holdJob = viewModelScope.launch {
            while (_uiState.value.holdRemainingSeconds > 0 && _uiState.value.isHoldActive) {
                delay(1000)
                val current = _uiState.value.holdRemainingSeconds - 1
                _uiState.value = _uiState.value.copy(holdRemainingSeconds = current)
            }
            if (_uiState.value.holdRemainingSeconds <= 0 && _uiState.value.isHoldActive) {
                // Hold expired
                repository.unlockCreneau(creneau.id)
                _uiState.value = _uiState.value.copy(
                    isHoldActive = false,
                    selectedCreneau = null,
                    currentStep = 4 // back to slot
                )
            }
        }
    }

    /**
     * Confirms booking with payment at the salon counter (paiement à la caisse).
     */
    fun processReservationPaiementCaisse() {
        val salon = _uiState.value.selectedSalon ?: return
        val service = _uiState.value.selectedService ?: return
        val creneau = _uiState.value.selectedCreneau ?: return
        val dateDisplay = getCurrentDateDisplay()

        _uiState.value = _uiState.value.copy(isProcessingPayment = true)

        viewModelScope.launch {
            // Short realistic delay to register confirmation
            delay(1000)

            val confirmed = repository.confirmReservation(
                salon = salon,
                service = service,
                options = getSelectedOptionsList(),
                dateStr = dateDisplay,
                heure = "${creneau.heureDebut} - ${creneau.heureFin}",
                totalPrix = calculateTotalPrix(),
                creneauId = creneau.id,
                clientNom = _uiState.value.clientNom.ifBlank { "Visiteur Enregistré" },
                clientTelephone = _uiState.value.clientTelephone.ifBlank { "07 08 45 67 89" },
                modePaiement = "Paiement à la caisse"
            )

            holdJob?.cancel()

            _uiState.value = _uiState.value.copy(
                isProcessingPayment = false,
                isHoldActive = false,
                confirmedReservation = confirmed,
                currentStep = 6 // Confirmation step
            )
        }
    }

    fun resetBookingFlow() {
        holdJob?.cancel()
        _uiState.value = _uiState.value.copy(
            currentStep = 0,
            selectedSalon = salons.firstOrNull(),
            selectedService = null,
            selectedOptionIds = emptySet(),
            selectedCreneau = null,
            crossSiteSuggestion = null,
            isHoldActive = false,
            confirmedReservation = null
        )
    }

    fun cancelUserReservation(id: Int) {
        viewModelScope.launch {
            repository.cancelReservation(id)
        }
    }

    fun showSalonInfoDetail(salon: Salon?) {
        _uiState.value = _uiState.value.copy(activeInfoSalon = salon)
    }

    // ==========================================
    // RECHERCHE & FILTRAGE MULTI-CRITÈRES
    // ==========================================

    fun setSearchQuery(query: String) {
        _uiState.value = _uiState.value.copy(searchQuery = query)
    }

    fun setSearchCategory(category: String) {
        _uiState.value = _uiState.value.copy(selectedCategoryFilter = category)
    }

    fun clearSearch() {
        _uiState.value = _uiState.value.copy(
            searchQuery = "",
            selectedCategoryFilter = "Tous"
        )
    }

    fun getFilteredSalons(): List<Salon> {
        val query = _uiState.value.searchQuery.trim().lowercase()
        val cat = _uiState.value.selectedCategoryFilter

        val categoryMatchesSalon = cat == "Tous" || cat == "Salons" ||
                salons.any { it.quartier.equals(cat, ignoreCase = true) }

        if (!categoryMatchesSalon && query.isBlank()) {
            return emptyList()
        }

        return salons.filter { salon ->
            val matchesCategory = when (cat) {
                "Tous", "Salons" -> true
                else -> salon.quartier.contains(cat, ignoreCase = true)
            }
            val matchesQuery = query.isBlank() ||
                    salon.nom.lowercase().contains(query) ||
                    salon.quartier.lowercase().contains(query) ||
                    salon.adresse.lowercase().contains(query) ||
                    salon.photoDesc.lowercase().contains(query)

            matchesCategory && matchesQuery
        }
    }

    fun getFilteredServices(): List<HairService> {
        val query = _uiState.value.searchQuery.trim().lowercase()
        val cat = _uiState.value.selectedCategoryFilter

        if (cat == "Salons" && query.isBlank()) {
            return emptyList()
        }

        return services.filter { service ->
            val matchesCategory = when (cat) {
                "Tous" -> true
                "Salons" -> false
                "Tresses" -> service.tag.contains("Tresse", ignoreCase = true) || service.nom.contains("Tresse", ignoreCase = true)
                "Twists & Vanilles", "Vanilles" -> service.nom.contains("Vanille", ignoreCase = true) || service.nom.contains("Twist", ignoreCase = true)
                "Soins & Spa", "Soins" -> service.nom.contains("Soin", ignoreCase = true) || service.tag.contains("Soin", ignoreCase = true)
                "Locks" -> service.nom.contains("Lock", ignoreCase = true)
                "Tissage", "Perruque" -> service.nom.contains("Tissage", ignoreCase = true) || service.nom.contains("Perruque", ignoreCase = true)
                "Lissage", "Défrisage" -> service.nom.contains("Défrisage", ignoreCase = true) || service.nom.contains("Brushing", ignoreCase = true)
                else -> true
            }

            val matchesQuery = query.isBlank() ||
                    service.nom.lowercase().contains(query) ||
                    service.description.lowercase().contains(query) ||
                    service.tag.lowercase().contains(query)

            matchesCategory && matchesQuery
        }
    }

    fun getFilteredHairstyles(): List<HairstyleItem> {
        val query = _uiState.value.searchQuery.trim().lowercase()
        val cat = _uiState.value.selectedCategoryFilter

        if (cat == "Salons" && query.isBlank()) {
            return emptyList()
        }

        return _uiState.value.hairstylesList.filter { item ->
            val matchesCat = when (cat) {
                "Tous" -> true
                "Salons" -> false
                "Tresses" -> item.categorie.contains("Tresse", ignoreCase = true)
                "Twists & Vanilles", "Vanilles" -> item.categorie.contains("Twist", ignoreCase = true)
                "Soins & Spa", "Soins" -> item.categorie.contains("Soin", ignoreCase = true)
                "Locks" -> item.categorie.contains("Lock", ignoreCase = true)
                "Tissage", "Perruque" -> item.categorie.contains("Tissage", ignoreCase = true)
                else -> true
            }

            val matchesQuery = query.isBlank() ||
                    item.titre.lowercase().contains(query) ||
                    item.description.lowercase().contains(query) ||
                    item.categorie.lowercase().contains(query)

            matchesCat && matchesQuery
        }
    }

    // ==========================================
    // ESPACE ADMINISTRATEUR SECRET
    // ==========================================

    fun openAdminLogin() {
        val isSuperConfigured = adminManager.isSuperAdminConfigured()
        val superAdmin = adminManager.getSuperAdmin()
        val secondaries = adminManager.getSecondaryAdmins()
        _uiState.value = _uiState.value.copy(
            showAdminLogin = true,
            adminErrorMessage = null,
            adminSuccessMessage = null,
            isSuperAdminConfigured = isSuperConfigured,
            superAdminUser = superAdmin,
            secondaryAdmins = secondaries
        )
    }

    fun dismissAdminLogin() {
        _uiState.value = _uiState.value.copy(showAdminLogin = false, adminErrorMessage = null)
    }

    fun openAdminDashboard() {
        _uiState.value = _uiState.value.copy(showAdminDashboard = true, showAdminLogin = false)
    }

    fun dismissAdminDashboard() {
        _uiState.value = _uiState.value.copy(showAdminDashboard = false)
    }

    fun loginAdmin(email: String, nom: String = "") {
        val result = adminManager.login(email, nom)
        result.onSuccess { admin ->
            _uiState.value = _uiState.value.copy(
                loggedAdmin = admin,
                isSuperAdminConfigured = true,
                superAdminUser = adminManager.getSuperAdmin(),
                secondaryAdmins = adminManager.getSecondaryAdmins(),
                showAdminLogin = false,
                showAdminDashboard = true,
                adminErrorMessage = null,
                adminSuccessMessage = "Connexion réussie en tant que ${if (admin.isSuperAdmin) "Super Administrateur" else "Administrateur"}"
            )
        }.onFailure { error ->
            _uiState.value = _uiState.value.copy(
                adminErrorMessage = error.message ?: "Identifiants non reconnus."
            )
        }
    }

    fun logoutAdmin() {
        _uiState.value = _uiState.value.copy(
            loggedAdmin = null,
            showAdminDashboard = false,
            adminErrorMessage = null,
            adminSuccessMessage = null
        )
    }

    fun addSecondaryAdmin(nom: String, email: String) {
        val result = adminManager.addSecondaryAdmin(nom, email)
        result.onSuccess {
            val updated = adminManager.getSecondaryAdmins()
            _uiState.value = _uiState.value.copy(
                secondaryAdmins = updated,
                adminSuccessMessage = "Administrateur « $nom » ajouté avec succès !",
                adminErrorMessage = null
            )
        }.onFailure { error ->
            _uiState.value = _uiState.value.copy(
                adminErrorMessage = error.message ?: "Impossible d'ajouter l'administrateur."
            )
        }
    }

    fun removeSecondaryAdmin(email: String) {
        adminManager.removeSecondaryAdmin(email)
        val updated = adminManager.getSecondaryAdmins()
        _uiState.value = _uiState.value.copy(
            secondaryAdmins = updated,
            adminSuccessMessage = "Administrateur supprimé.",
            adminErrorMessage = null
        )
    }

    fun updateServicePrice(serviceId: String, newPrice: Int) {
        repository.updateServicePrice(serviceId, newPrice)
        services = repository.getServices()
        val currentSelected = _uiState.value.selectedService
        val updatedSelected = if (currentSelected?.id == serviceId) {
            services.find { it.id == serviceId }
        } else currentSelected

        _uiState.value = _uiState.value.copy(
            selectedService = updatedSelected,
            adminSuccessMessage = "Tarif mis à jour avec succès (${newPrice} FCFA)."
        )
    }

    fun updateOptionPrice(optionId: String, newPrice: Int) {
        repository.updateOptionPrice(optionId, newPrice)
        options = repository.getOptions()
        _uiState.value = _uiState.value.copy(
            adminSuccessMessage = "Tarif de l'option mis à jour (${newPrice} FCFA)."
        )
    }

    fun resetPricesToDefault() {
        adminManager.resetAllPrices()
        services = repository.getServices()
        options = repository.getOptions()
        _uiState.value = _uiState.value.copy(
            selectedService = services.find { it.id == _uiState.value.selectedService?.id },
            adminSuccessMessage = "Tous les tarifs ont été réinitialisés aux valeurs par défaut."
        )
    }

    fun addHairstyle(
        titre: String,
        categorie: String,
        prix: Int,
        description: String,
        imageResName: String = "img_coiffure_showcase",
        salonNom: String = "Tous nos salons"
    ) {
        val newId = "hs_" + System.currentTimeMillis()
        val item = HairstyleItem(
            id = newId,
            titre = titre.trim(),
            categorie = categorie.trim(),
            prixEstime = prix,
            description = description.trim(),
            imageResName = imageResName,
            salonNom = salonNom
        )
        repository.addHairstyle(item)
        val updatedList = repository.getHairstyles()
        _uiState.value = _uiState.value.copy(
            hairstylesList = updatedList,
            adminSuccessMessage = "Modèle « $titre » ajouté au catalogue !"
        )
    }

    fun deleteHairstyle(id: String) {
        repository.deleteHairstyle(id)
        val updatedList = repository.getHairstyles()
        _uiState.value = _uiState.value.copy(
            hairstylesList = updatedList,
            adminSuccessMessage = "Coiffure retirée du catalogue."
        )
    }

    fun clearAdminMessages() {
        _uiState.value = _uiState.value.copy(adminErrorMessage = null, adminSuccessMessage = null)
    }

    override fun onCleared() {
        super.onCleared()
        holdJob?.cancel()
    }
}
