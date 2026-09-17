package com.example.ui

import androidx.activity.compose.BackHandler
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.WindowInsets
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.navigationBars
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.safeDrawing
import androidx.compose.foundation.layout.windowInsetsPadding
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import com.example.ui.components.AdminDashboardDialog
import com.example.ui.components.AdminLoginDialog
import com.example.ui.components.AuthModalDialog
import com.example.ui.components.SalonBottomNav
import com.example.ui.components.SalonTopBar
import com.example.ui.components.ServiceSummaryDialog
import com.example.ui.screens.MesReservationsScreen
import com.example.ui.screens.SalonsOverviewScreen
import com.example.ui.screens.Step0SalonScreen
import com.example.ui.screens.Step1PrestationScreen
import com.example.ui.screens.Step2OptionsScreen
import com.example.ui.screens.Step3DevisScreen
import com.example.ui.screens.Step4CreneauScreen
import com.example.ui.screens.Step5PaymentScreen
import com.example.ui.screens.Step6ConfirmationScreen
import com.example.ui.theme.SalonCream
import com.example.ui.viewmodel.AppTab
import com.example.ui.viewmodel.BookingViewModel

@Composable
fun MainSalonApp(
    viewModel: BookingViewModel = viewModel()
) {
    val uiState by viewModel.uiState.collectAsStateWithLifecycle()
    val userReservations by viewModel.userReservations.collectAsStateWithLifecycle()

    // Handle system back button during multi-step booking
    BackHandler(enabled = uiState.currentTab == AppTab.RESERVER && uiState.currentStep > 0) {
        if (uiState.currentStep == 6) {
            viewModel.resetBookingFlow()
        } else {
            viewModel.goToStep(uiState.currentStep - 1)
        }
    }

    Scaffold(
        modifier = Modifier.fillMaxSize(),
        contentWindowInsets = WindowInsets.safeDrawing,
        topBar = {
            SalonTopBar(
                currentStep = uiState.currentStep,
                currentTab = uiState.currentTab,
                isUserRegistered = uiState.isUserRegistered,
                clientNom = uiState.clientNom,
                loggedAdmin = uiState.loggedAdmin,
                onBackClick = {
                    if (uiState.currentStep == 6) {
                        viewModel.resetBookingFlow()
                    } else {
                        viewModel.goToStep(uiState.currentStep - 1)
                    }
                },
                onAccountClick = {
                    viewModel.openAuthModal()
                },
                onAdminSecretTrigger = {
                    if (uiState.loggedAdmin != null) {
                        viewModel.openAdminDashboard()
                    } else {
                        viewModel.openAdminLogin()
                    }
                }
            )
        },
        bottomBar = {
            SalonBottomNav(
                currentTab = uiState.currentTab,
                reservationsCount = userReservations.count { it.statut == "CONFIRMEE" },
                onTabSelected = { tab ->
                    viewModel.setTab(tab)
                }
            )
        }
    ) { innerPadding ->
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SalonCream)
                .padding(innerPadding)
        ) {
            when (uiState.currentTab) {
                AppTab.RESERVER -> {
                    when (uiState.currentStep) {
                        0 -> {
                            Step0SalonScreen(
                                salons = viewModel.salons,
                                filteredSalons = viewModel.getFilteredSalons(),
                                filteredServices = viewModel.getFilteredServices(),
                                filteredHairstyles = viewModel.getFilteredHairstyles(),
                                searchQuery = uiState.searchQuery,
                                selectedCategory = uiState.selectedCategoryFilter,
                                onSearchQueryChange = { viewModel.setSearchQuery(it) },
                                onCategorySelected = { viewModel.setSearchCategory(it) },
                                onClearSearch = { viewModel.clearSearch() },
                                onSalonSelected = { salon ->
                                    viewModel.selectSalon(salon)
                                },
                                onServiceSelected = { service ->
                                    if (uiState.selectedSalon == null) {
                                        viewModel.selectSalon(viewModel.salons.first())
                                    }
                                    viewModel.selectService(service)
                                },
                                onHairstyleSelected = { hairstyle ->
                                    val matching = viewModel.services.find {
                                        it.nom.contains(hairstyle.categorie, ignoreCase = true) ||
                                        it.tag.contains(hairstyle.categorie, ignoreCase = true)
                                    } ?: viewModel.services.first()
                                    if (uiState.selectedSalon == null) {
                                        viewModel.selectSalon(viewModel.salons.first())
                                    }
                                    viewModel.selectService(matching)
                                }
                            )
                        }

                        1 -> {
                            val salon = uiState.selectedSalon ?: viewModel.salons.first()
                            Step1PrestationScreen(
                                salon = salon,
                                services = viewModel.services,
                                selectedService = uiState.selectedService,
                                onChangeSalonClick = { viewModel.goToStep(0) },
                                onServiceSelected = { service ->
                                    viewModel.selectService(service)
                                }
                            )
                        }

                        2 -> {
                            val service = uiState.selectedService ?: viewModel.services.first()
                            Step2OptionsScreen(
                                service = service,
                                options = viewModel.options,
                                selectedOptionIds = uiState.selectedOptionIds,
                                totalPrix = viewModel.calculateTotalPrix(),
                                onToggleOption = { optId -> viewModel.toggleOption(optId) },
                                onContinueClick = { viewModel.openSummaryDialog() }
                            )
                        }

                        3 -> {
                            val salon = uiState.selectedSalon ?: viewModel.salons.first()
                            val service = uiState.selectedService ?: viewModel.services.first()
                            Step3DevisScreen(
                                salon = salon,
                                service = service,
                                selectedOptions = viewModel.getSelectedOptionsList(),
                                totalPrix = viewModel.calculateTotalPrix(),
                                totalDureeMin = viewModel.calculateTotalDuree(),
                                onModifyOptionsClick = { viewModel.goToStep(2) },
                                onValidateDevisClick = { viewModel.goToStep(4) }
                            )
                        }

                        4 -> {
                            val salon = uiState.selectedSalon ?: viewModel.salons.first()
                            Step4CreneauScreen(
                                salon = salon,
                                availableDays = viewModel.availableDays,
                                selectedDateIndex = uiState.selectedDateIndex,
                                selectedDateDisplay = viewModel.getCurrentDateDisplay(),
                                creneaux = viewModel.getCurrentCreneaux(),
                                selectedCreneau = uiState.selectedCreneau,
                                crossSiteSuggestion = uiState.crossSiteSuggestion,
                                onSelectDateIndex = { index -> viewModel.selectDateIndex(index) },
                                onSelectCustomDate = { display, key -> viewModel.selectCustomDate(display, key) },
                                onFilterTimeSlot = { time -> viewModel.filterTimeSlot(time) },
                                onSelectCreneau = { creneau -> viewModel.selectCreneau(creneau) },
                                onAcceptCrossSiteAlternative = { viewModel.acceptCrossSiteAlternative() },
                                onContinueClick = { viewModel.goToStep(5) }
                            )
                        }

                        5 -> {
                            val salon = uiState.selectedSalon ?: viewModel.salons.first()
                            val service = uiState.selectedService ?: viewModel.services.first()
                            val creneau = uiState.selectedCreneau
                            val dayPair = viewModel.availableDays.getOrNull(uiState.selectedDateIndex)

                            if (creneau != null && dayPair != null) {
                                Step5PaymentScreen(
                                    salon = salon,
                                    service = service,
                                    options = viewModel.getSelectedOptionsList(),
                                    creneau = creneau,
                                    dateStr = viewModel.getCurrentDateDisplay(),
                                    totalPrix = viewModel.calculateTotalPrix(),
                                    remainingSeconds = uiState.holdRemainingSeconds,
                                    clientNom = uiState.clientNom,
                                    clientTelephone = uiState.clientTelephone,
                                    isUserRegistered = uiState.isUserRegistered,
                                    isProcessing = uiState.isProcessingPayment,
                                    onClientDetailsChange = { nom, tel ->
                                        viewModel.updateClientDetails(nom, tel)
                                    },
                                    onRegisterClick = { viewModel.openAuthModal() },
                                    onConfirmReservationClick = { viewModel.processReservationPaiementCaisse() }
                                )
                            }
                        }

                        6 -> {
                            val confirmed = uiState.confirmedReservation
                            if (confirmed != null) {
                                Step6ConfirmationScreen(
                                    reservation = confirmed,
                                    onViewMyBookingsClick = {
                                        viewModel.setTab(AppTab.MES_RDV)
                                    },
                                    onNewBookingClick = {
                                        viewModel.resetBookingFlow()
                                    }
                                )
                            }
                        }
                    }
                }

                AppTab.MES_RDV -> {
                    MesReservationsScreen(
                        reservations = userReservations,
                        onCancelReservation = { id -> viewModel.cancelUserReservation(id) },
                        onBookNewClick = {
                            viewModel.resetBookingFlow()
                            viewModel.setTab(AppTab.RESERVER)
                        }
                    )
                }

                AppTab.SALONS_INFO -> {
                    SalonsOverviewScreen(
                        salons = viewModel.salons,
                        onSelectSalonForBooking = { salon ->
                            viewModel.resetBookingFlow()
                            viewModel.selectSalon(salon)
                            viewModel.setTab(AppTab.RESERVER)
                        }
                    )
                }
            }

            // Summary Dialog after service & option selection
            if (uiState.showSummaryDialog) {
                val salon = uiState.selectedSalon ?: viewModel.salons.first()
                val service = uiState.selectedService ?: viewModel.services.first()
                ServiceSummaryDialog(
                    salon = salon,
                    service = service,
                    selectedOptions = viewModel.getSelectedOptionsList(),
                    totalPrix = viewModel.calculateTotalPrix(),
                    totalDureeMin = viewModel.calculateTotalDuree(),
                    onDismiss = { viewModel.dismissSummaryDialog() },
                    onConfirm = { viewModel.confirmSummaryDialogAndProceed() }
                )
            }

            // Auth Modal Dialog: mandatory registration / login for visitors
            if (uiState.showAuthModal) {
                AuthModalDialog(
                    onDismiss = { viewModel.dismissAuthModal() },
                    onAuthSuccess = { nom, telephone, email ->
                        viewModel.onAuthSuccess(nom, telephone, email)
                    }
                )
            }

            // Secret Admin Login Dialog (triggered by double-tap on logo)
            if (uiState.showAdminLogin) {
                AdminLoginDialog(
                    isSuperAdminConfigured = uiState.isSuperAdminConfigured,
                    superAdminUser = uiState.superAdminUser,
                    errorMessage = uiState.adminErrorMessage,
                    onDismiss = { viewModel.dismissAdminLogin() },
                    onLoginSubmit = { email, nom ->
                        viewModel.loginAdmin(email, nom)
                    }
                )
            }

            // Admin Dashboard Dialog
            if (uiState.showAdminDashboard && uiState.loggedAdmin != null) {
                AdminDashboardDialog(
                    loggedAdmin = uiState.loggedAdmin!!,
                    superAdminUser = uiState.superAdminUser,
                    secondaryAdmins = uiState.secondaryAdmins,
                    services = viewModel.services,
                    options = viewModel.options,
                    hairstyles = uiState.hairstylesList,
                    salons = viewModel.salons,
                    successMessage = uiState.adminSuccessMessage,
                    errorMessage = uiState.adminErrorMessage,
                    onDismiss = { viewModel.dismissAdminDashboard() },
                    onLogout = { viewModel.logoutAdmin() },
                    onAddSecondaryAdmin = { nom, email ->
                        viewModel.addSecondaryAdmin(nom, email)
                    },
                    onRemoveSecondaryAdmin = { email ->
                        viewModel.removeSecondaryAdmin(email)
                    },
                    onUpdateServicePrice = { id, price ->
                        viewModel.updateServicePrice(id, price)
                    },
                    onUpdateOptionPrice = { id, price ->
                        viewModel.updateOptionPrice(id, price)
                    },
                    onResetPrices = {
                        viewModel.resetPricesToDefault()
                    },
                    onAddHairstyle = { titre, cat, prix, desc, img, salon ->
                        viewModel.addHairstyle(titre, cat, prix, desc, img, salon)
                    },
                    onDeleteHairstyle = { id ->
                        viewModel.deleteHairstyle(id)
                    },
                    onClearMessages = {
                        viewModel.clearAdminMessages()
                    }
                )
            }
        }
    }
}
