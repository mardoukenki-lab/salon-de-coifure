package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.animateColorAsState
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.ExperimentalLayoutApi
import androidx.compose.foundation.layout.FlowRow
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FilterChip
import androidx.compose.material3.FilterChipDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Creneau
import com.example.data.model.CreneauStatut
import com.example.data.model.HairService
import com.example.data.model.Salon
import com.example.data.repository.CrossSiteAlternative
import com.example.ui.theme.SalonCream
import com.example.ui.theme.SalonDangerBg
import com.example.ui.theme.SalonDangerFg
import com.example.ui.theme.SalonInk
import com.example.ui.theme.SalonInkSoft
import com.example.ui.theme.SalonLine
import com.example.ui.theme.SalonNavy
import com.example.ui.theme.SalonNavySoft
import com.example.ui.theme.SalonOkBg
import com.example.ui.theme.SalonOkFg
import com.example.ui.theme.SalonPaper
import com.example.ui.theme.SalonTerracotta
import com.example.ui.theme.SalonTerracottaSoft
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Écran complet de Réservation :
 * Permet à l'utilisateur de :
 * 1. Sélectionner un salon dans le réseau d'Abidjan (Angré, Treichville, Yopougon)
 * 2. Choisir une prestation / service spécifique (tresses, tissage, coupe, soins)
 * 3. Choisir une date et un créneau horaire précis via une vue Calendrier interactive
 * 4. Valider et finaliser la réservation avec un récapitulatif instantané
 */
@OptIn(ExperimentalLayoutApi::class, ExperimentalMaterial3Api::class)
@Composable
fun ReservationScreen(
    salons: List<Salon>,
    services: List<HairService>,
    selectedSalon: Salon?,
    selectedService: HairService?,
    selectedDateDisplay: String,
    selectedDateKey: String?,
    creneaux: List<Creneau>,
    selectedCreneau: Creneau?,
    crossSiteSuggestion: CrossSiteAlternative?,
    onSelectSalon: (Salon) -> Unit,
    onSelectService: (HairService) -> Unit,
    onSelectCustomDate: (dateDisplay: String, dateKey: String) -> Unit,
    onSelectCreneau: (Creneau) -> Unit,
    onAcceptCrossSiteAlternative: () -> Unit,
    isUserRegistered: Boolean = false,
    clientNom: String = "",
    onOpenAuthModal: () -> Unit = {},
    onConfirmReservation: () -> Unit
) {
    var selectedCategoryFilter by remember { mutableStateOf("Tous") }
    val categories = listOf("Tous", "Tresses", "Tissage", "Coupes", "Soins", "Locks")

    // Filtered services based on category pill
    val filteredServices = remember(selectedCategoryFilter, services) {
        if (selectedCategoryFilter == "Tous") {
            services
        } else {
            services.filter { service ->
                when (selectedCategoryFilter) {
                    "Tresses" -> service.tag.contains("Tresse", ignoreCase = true) || service.nom.contains("Tresse", ignoreCase = true)
                    "Tissage" -> service.nom.contains("Tissage", ignoreCase = true) || service.nom.contains("Perruque", ignoreCase = true)
                    "Coupes" -> service.nom.contains("Coupe", ignoreCase = true) || service.nom.contains("Barbe", ignoreCase = true)
                    "Soins" -> service.nom.contains("Soin", ignoreCase = true) || service.tag.contains("Soin", ignoreCase = true)
                    "Locks" -> service.nom.contains("Lock", ignoreCase = true)
                    else -> true
                }
            }
        }
    }

    val isFormComplete = selectedSalon != null && selectedService != null && selectedCreneau != null

    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SalonCream)
            .testTag("screen_reservation")
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 120.dp),
            verticalArrangement = Arrangement.spacedBy(20.dp)
        ) {
            // Header
            item {
                Column {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "RÉSERVATION DIRECTE",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SalonTerracotta,
                                letterSpacing = 1.sp
                            )
                            Text(
                                text = "Réserver une prestation",
                                fontSize = 22.sp,
                                fontWeight = FontWeight.Bold,
                                color = SalonNavy,
                                fontFamily = FontFamily.Serif
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Choisissez votre salon, sélectionnez votre prestation et réservez votre créneau sur le calendrier.",
                        fontSize = 12.sp,
                        color = SalonInkSoft,
                        lineHeight = 17.sp
                    )
                }
            }

            // État d'inscription obligatoire du client
            item {
                if (!isUserRegistered) {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SalonTerracottaSoft.copy(alpha = 0.35f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, SalonTerracotta.copy(alpha = 0.4f), RoundedCornerShape(14.dp))
                            .clickable { onOpenAuthModal() }
                            .testTag("banner_auth_required")
                    ) {
                        Row(
                            modifier = Modifier.padding(14.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(CircleShape)
                                    .background(SalonTerracotta),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PersonAdd,
                                    contentDescription = null,
                                    tint = SalonPaper,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Inscription requise pour réserver",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = SalonNavy
                                )
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Créez votre compte en 30 secondes ou connectez-vous avec Google avant de réserver.",
                                    fontSize = 11.sp,
                                    color = SalonInkSoft,
                                    lineHeight = 15.sp
                                )
                            }
                            Spacer(modifier = Modifier.width(8.dp))
                            Button(
                                onClick = onOpenAuthModal,
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SalonTerracotta),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text("S'inscrire", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                } else {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SalonOkBg.copy(alpha = 0.45f)),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, SalonOkFg.copy(alpha = 0.35f), RoundedCornerShape(14.dp))
                            .testTag("banner_auth_connected")
                    ) {
                        Row(
                            modifier = Modifier.padding(horizontal = 14.dp, vertical = 10.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Icon(
                                imageVector = Icons.Default.CheckCircle,
                                contentDescription = null,
                                tint = SalonOkFg,
                                modifier = Modifier.size(20.dp)
                            )
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = "Client(e) membre : ${clientNom.ifBlank { "Compte vérifié" }}",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 12.sp,
                                    color = SalonNavy
                                )
                                Text(
                                    text = "Votre compte est prêt pour confirmer vos réservations.",
                                    fontSize = 11.sp,
                                    color = SalonInkSoft
                                )
                            }
                        }
                    }
                }
            }

            // ============================================================
            // SECTION 1: SÉLECTION DU SALON (Select a salon)
            // ============================================================
            item {
                Column(modifier = Modifier.testTag("section_salon_selection")) {
                    SectionTitle(
                        title = "Sélectionnez votre salon",
                        subtitle = "3 adresses d'exception à Abidjan"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        salons.forEach { salon ->
                            val isSelected = salon.id == selectedSalon?.id
                            SalonSelectCard(
                                salon = salon,
                                isSelected = isSelected,
                                onSelect = { onSelectSalon(salon) }
                            )
                        }
                    }
                }
            }

            // ============================================================
            // SECTION 2: CHOIX DE LA PRESTATION (Choose a specific service)
            // ============================================================
            item {
                Column(modifier = Modifier.testTag("section_service_selection")) {
                    SectionTitle(
                        title = "Choisissez votre prestation",
                        subtitle = "Tarifs transparents & durée garantie"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Catégories de filtres
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        contentPadding = PaddingValues(horizontal = 2.dp)
                    ) {
                        items(categories) { cat ->
                            val isCatSelected = cat == selectedCategoryFilter
                            FilterChip(
                                selected = isCatSelected,
                                onClick = { selectedCategoryFilter = cat },
                                label = { Text(cat, fontSize = 12.sp, fontWeight = if (isCatSelected) FontWeight.Bold else FontWeight.Normal) },
                                colors = FilterChipDefaults.filterChipColors(
                                    selectedContainerColor = SalonTerracotta,
                                    selectedLabelColor = SalonPaper,
                                    containerColor = SalonPaper,
                                    labelColor = SalonInk
                                ),
                                border = FilterChipDefaults.filterChipBorder(
                                    enabled = true,
                                    selected = isCatSelected,
                                    borderColor = if (isCatSelected) SalonTerracotta else SalonLine
                                ),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier.testTag("service_category_chip_$cat")
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Liste des prestations filtrées
                    Column(verticalArrangement = Arrangement.spacedBy(10.dp)) {
                        filteredServices.forEach { service ->
                            val isSelected = service.id == selectedService?.id
                            ServiceSelectCard(
                                service = service,
                                isSelected = isSelected,
                                onSelect = { onSelectService(service) }
                            )
                        }
                    }
                }
            }

            // ============================================================
            // SECTION 3: VUE CALENDRIER & CRÉNEAU (Date/time slot from calendar view)
            // ============================================================
            item {
                Column(modifier = Modifier.testTag("section_calendar_view")) {
                    SectionTitle(
                        title = "Choisissez la date et l'heure",
                        subtitle = "Calendrier interactif et créneaux en direct"
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Calendrier interactif
                    CalendarMonthPicker(
                        selectedDateKey = selectedDateKey,
                        onDateSelected = onSelectCustomDate
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    // Affichage des créneaux horaires
                    Text(
                        text = "Créneaux pour le $selectedDateDisplay",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold,
                        color = SalonNavy
                    )
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Horaires d'ouverture : 08h30 - 19h30 · Réservation sans attente",
                        fontSize = 11.sp,
                        color = SalonInkSoft
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Suggestion alternative inter-salons si créneau complet sélectionné
                    AnimatedVisibility(
                        visible = crossSiteSuggestion != null,
                        enter = fadeIn(),
                        exit = fadeOut()
                    ) {
                        crossSiteSuggestion?.let { alt ->
                            Card(
                                colors = CardDefaults.cardColors(containerColor = SalonTerracottaSoft.copy(alpha = 0.35f)),
                                shape = RoundedCornerShape(12.dp),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(bottom = 10.dp)
                                    .border(1.dp, SalonTerracotta, RoundedCornerShape(12.dp))
                                    .testTag("cross_site_suggestion_banner")
                            ) {
                                Row(
                                    modifier = Modifier.padding(12.dp),
                                    verticalAlignment = Alignment.CenterVertically
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.SwapHoriz,
                                        contentDescription = null,
                                        tint = SalonTerracotta,
                                        modifier = Modifier.size(20.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Column(modifier = Modifier.weight(1f)) {
                                        Text(
                                            text = "Créneau complet à ${selectedSalon?.nom}",
                                            fontSize = 12.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = SalonNavy
                                        )
                                        Text(
                                            text = "Disponible à ${alt.salonAlternatif.nom} (${alt.distanceKm} km) à ${alt.heureDebut}",
                                            fontSize = 11.sp,
                                            color = SalonInk
                                        )
                                    }
                                    Button(
                                        onClick = onAcceptCrossSiteAlternative,
                                        colors = ButtonDefaults.buttonColors(
                                            containerColor = SalonTerracotta,
                                            contentColor = SalonPaper
                                        ),
                                        contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                                        shape = RoundedCornerShape(8.dp)
                                    ) {
                                        Text("Changer", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                    }
                                }
                            }
                        }
                    }

                    // Grille des créneaux horaires
                    TimeSlotsGrid(
                        creneaux = creneaux,
                        selectedCreneau = selectedCreneau,
                        onSelectCreneau = onSelectCreneau
                    )
                }
            }
        }

        // ============================================================
        // RÉCAPITULATIF ET VALIDATION FLOTTANT EN BAS
        // ============================================================
        Surface(
            modifier = Modifier
                .align(Alignment.BottomCenter)
                .fillMaxWidth()
                .testTag("reservation_summary_card"),
            color = SalonPaper,
            shadowElevation = 8.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, SalonLine)
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(16.dp)
            ) {
                // Live recap row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        val salonText = selectedSalon?.quartier ?: "Salon non choisi"
                        val serviceText = selectedService?.nom ?: "Prestation non choisie"
                        val timeText = if (selectedCreneau != null) " · ${selectedCreneau.heureDebut}" else ""

                        Text(
                            text = "$salonText · $serviceText",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SalonNavy,
                            maxLines = 1,
                            overflow = TextOverflow.Ellipsis
                        )
                        Text(
                            text = if (selectedCreneau != null) "$selectedDateDisplay$timeText" else "Veuillez choisir un créneau horaire",
                            fontSize = 11.sp,
                            color = if (selectedCreneau != null) SalonTerracotta else SalonInkSoft
                        )
                    }

                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = if (selectedService != null) "${selectedService.prixReference} FCFA" else "0 FCFA",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = SalonNavy,
                        fontFamily = FontFamily.Serif
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                // Bouton d'action principal
                Button(
                    onClick = onConfirmReservation,
                    enabled = isFormComplete,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(48.dp)
                        .testTag("btn_confirm_reservation"),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SalonTerracotta,
                        contentColor = SalonPaper,
                        disabledContainerColor = SalonLine,
                        disabledContentColor = SalonInkSoft
                    ),
                    shape = RoundedCornerShape(12.dp)
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        Icon(
                            imageVector = when {
                                !isFormComplete -> Icons.AutoMirrored.Filled.ArrowForward
                                !isUserRegistered -> Icons.Default.PersonAdd
                                else -> Icons.Default.CheckCircle
                            },
                            contentDescription = null,
                            modifier = Modifier.size(17.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = when {
                                !isFormComplete -> "Complétez votre sélection ci-dessus"
                                !isUserRegistered -> "S'inscrire et Continuer vers le paiement"
                                else -> "Confirmer & Continuer vers le paiement"
                            },
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }
                }
            }
        }
    }
}

/**
 * En-tête pour chaque section
 */
@Composable
private fun SectionTitle(
    title: String,
    subtitle: String
) {
    Column {
        Text(
            text = title,
            fontSize = 16.sp,
            fontWeight = FontWeight.Bold,
            color = SalonNavy,
            fontFamily = FontFamily.Serif
        )
        Text(
            text = subtitle,
            fontSize = 11.sp,
            color = SalonInkSoft
        )
    }
}

/**
 * Carte de sélection de Salon
 */
@Composable
private fun SalonSelectCard(
    salon: Salon,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) SalonTerracotta else SalonLine,
        label = "salon_border"
    )
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) SalonTerracottaSoft.copy(alpha = 0.25f) else SalonPaper,
        label = "salon_bg"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(if (isSelected) 2.dp else 1.dp, borderColor, RoundedCornerShape(14.dp))
            .clickable { onSelect() }
            .testTag("salon_card_${salon.id}"),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Radio circle indicator
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) SalonTerracotta else SalonPaper)
                    .border(2.dp, if (isSelected) SalonTerracotta else SalonLine, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Sélectionné",
                        tint = SalonPaper,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = salon.nom,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = SalonNavy
                    )
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Star,
                            contentDescription = null,
                            tint = Color(0xFFD97706),
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(2.dp))
                        Text(
                            text = "${salon.note}",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.Bold,
                            color = SalonNavy
                        )
                    }
                }

                Spacer(modifier = Modifier.height(2.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = SalonTerracotta,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(3.dp))
                    Text(
                        text = "${salon.adresse} · ${salon.distanceKm} km",
                        fontSize = 11.sp,
                        color = SalonInkSoft
                    )
                }

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    horizontalArrangement = Arrangement.spacedBy(6.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(6.dp))
                            .background(SalonOkBg)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = "${salon.creneauxDispoAujourdhui} créneaux dispo",
                            fontSize = 10.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SalonOkFg
                        )
                    }

                    Text(
                        text = salon.horaires.split("·").firstOrNull()?.trim() ?: "",
                        fontSize = 10.sp,
                        color = SalonInkSoft
                    )
                }
            }
        }
    }
}

/**
 * Carte de sélection de prestation spécifique
 */
@Composable
private fun ServiceSelectCard(
    service: HairService,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val borderColor by animateColorAsState(
        targetValue = if (isSelected) SalonTerracotta else SalonLine,
        label = "service_border"
    )
    val bgColor by animateColorAsState(
        targetValue = if (isSelected) SalonTerracottaSoft.copy(alpha = 0.25f) else SalonPaper,
        label = "service_bg"
    )

    Card(
        modifier = Modifier
            .fillMaxWidth()
            .clip(RoundedCornerShape(14.dp))
            .border(if (isSelected) 2.dp else 1.dp, borderColor, RoundedCornerShape(14.dp))
            .clickable { onSelect() }
            .testTag("service_card_${service.id}"),
        colors = CardDefaults.cardColors(containerColor = bgColor),
        shape = RoundedCornerShape(14.dp)
    ) {
        Row(
            modifier = Modifier
                .fillMaxWidth()
                .padding(14.dp),
            verticalAlignment = Alignment.CenterVertically
        ) {
            // Radio circle indicator
            Box(
                modifier = Modifier
                    .size(22.dp)
                    .clip(CircleShape)
                    .background(if (isSelected) SalonTerracotta else SalonPaper)
                    .border(2.dp, if (isSelected) SalonTerracotta else SalonLine, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                if (isSelected) {
                    Icon(
                        imageVector = Icons.Default.Check,
                        contentDescription = "Sélectionné",
                        tint = SalonPaper,
                        modifier = Modifier.size(13.dp)
                    )
                }
            }

            Spacer(modifier = Modifier.width(12.dp))

            Column(modifier = Modifier.weight(1f)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = service.nom,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = SalonNavy
                    )
                    Text(
                        text = "${service.prixReference} FCFA",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = SalonTerracotta
                    )
                }

                Spacer(modifier = Modifier.height(3.dp))

                Text(
                    text = service.description,
                    fontSize = 11.sp,
                    color = SalonInkSoft,
                    maxLines = 2,
                    overflow = TextOverflow.Ellipsis
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.AccessTime,
                        contentDescription = null,
                        tint = SalonInkSoft,
                        modifier = Modifier.size(12.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Durée estimée : ${service.dureeMin} min",
                        fontSize = 11.sp,
                        color = SalonInkSoft,
                        fontWeight = FontWeight.Medium
                    )

                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "·",
                        fontSize = 11.sp,
                        color = SalonInkSoft
                    )
                    Spacer(modifier = Modifier.width(8.dp))

                    Text(
                        text = service.tag,
                        fontSize = 10.sp,
                        color = SalonTerracotta,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }
    }
}

/**
 * Composant de vue calendrier du mois avec navigation entre les mois
 * et sélection interactive de n'importe quel jour.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
private fun CalendarMonthPicker(
    selectedDateKey: String?,
    onDateSelected: (dateDisplay: String, dateKey: String) -> Unit
) {
    val todayCal = remember { Calendar.getInstance(Locale.FRENCH) }
    var displayedYear by remember { mutableIntStateOf(todayCal.get(Calendar.YEAR)) }
    var displayedMonth by remember { mutableIntStateOf(todayCal.get(Calendar.MONTH)) } // 0-based

    var showNativeDatePicker by remember { mutableStateOf(false) }

    val monthCal = remember(displayedYear, displayedMonth) {
        Calendar.getInstance(Locale.FRENCH).apply {
            set(Calendar.YEAR, displayedYear)
            set(Calendar.MONTH, displayedMonth)
            set(Calendar.DAY_OF_MONTH, 1)
        }
    }

    val monthName = remember(displayedYear, displayedMonth) {
        val sdf = SimpleDateFormat("MMMM yyyy", Locale.FRENCH)
        sdf.format(monthCal.time).replaceFirstChar { it.uppercase() }
    }

    val daysInMonth = monthCal.getActualMaximum(Calendar.DAY_OF_MONTH)
    // In Java Calendar: SUNDAY=1, MONDAY=2, ..., SATURDAY=7
    // We want Monday=0, Tuesday=1, ..., Sunday=6
    val firstDayOfWeek = (monthCal.get(Calendar.DAY_OF_WEEK) + 5) % 7

    val dayNames = listOf("Lun", "Mar", "Mer", "Jeu", "Ven", "Sam", "Dim")

    Card(
        colors = CardDefaults.cardColors(containerColor = SalonPaper),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, SalonLine, RoundedCornerShape(16.dp))
            .testTag("interactive_calendar_card")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Month navigation bar
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                IconButton(
                    onClick = {
                        if (displayedMonth == 0) {
                            displayedMonth = 11
                            displayedYear -= 1
                        } else {
                            displayedMonth -= 1
                        }
                    },
                    modifier = Modifier.testTag("calendar_btn_prev_month")
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronLeft,
                        contentDescription = "Mois précédent",
                        tint = SalonNavy
                    )
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier.clickable { showNativeDatePicker = true }
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = SalonTerracotta,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = monthName,
                        fontSize = 15.sp,
                        fontWeight = FontWeight.Bold,
                        color = SalonNavy,
                        fontFamily = FontFamily.Serif
                    )
                }

                IconButton(
                    onClick = {
                        if (displayedMonth == 11) {
                            displayedMonth = 0
                            displayedYear += 1
                        } else {
                            displayedMonth += 1
                        }
                    },
                    modifier = Modifier.testTag("calendar_btn_next_month")
                ) {
                    Icon(
                        imageVector = Icons.Default.ChevronRight,
                        contentDescription = "Mois suivant",
                        tint = SalonNavy
                    )
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Day names row
            Row(modifier = Modifier.fillMaxWidth()) {
                dayNames.forEach { dayName ->
                    Text(
                        text = dayName,
                        modifier = Modifier.weight(1f),
                        textAlign = TextAlign.Center,
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SalonInkSoft
                    )
                }
            }

            Spacer(modifier = Modifier.height(6.dp))

            // Days Grid
            val totalCells = firstDayOfWeek + daysInMonth
            val totalRows = (totalCells + 6) / 7

            for (row in 0 until totalRows) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 3.dp),
                    horizontalArrangement = Arrangement.SpaceEvenly
                ) {
                    for (col in 0 until 7) {
                        val cellIndex = row * 7 + col
                        val dayNumber = cellIndex - firstDayOfWeek + 1

                        if (dayNumber in 1..daysInMonth) {
                            val cellCal = Calendar.getInstance(Locale.FRENCH).apply {
                                set(Calendar.YEAR, displayedYear)
                                set(Calendar.MONTH, displayedMonth)
                                set(Calendar.DAY_OF_MONTH, dayNumber)
                                set(Calendar.HOUR_OF_DAY, 0)
                                set(Calendar.MINUTE, 0)
                                set(Calendar.SECOND, 0)
                                set(Calendar.MILLISECOND, 0)
                            }

                            val isPast = cellCal.before(Calendar.getInstance(Locale.FRENCH).apply {
                                set(Calendar.HOUR_OF_DAY, 0)
                                set(Calendar.MINUTE, 0)
                                set(Calendar.SECOND, 0)
                                set(Calendar.MILLISECOND, 0)
                            })

                            val formatKey = SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH)
                            val key = formatKey.format(cellCal.time)
                            val isSelected = key == selectedDateKey

                            val isToday = cellCal.get(Calendar.YEAR) == todayCal.get(Calendar.YEAR) &&
                                    cellCal.get(Calendar.MONTH) == todayCal.get(Calendar.MONTH) &&
                                    cellCal.get(Calendar.DAY_OF_MONTH) == todayCal.get(Calendar.DAY_OF_MONTH)

                            Box(
                                modifier = Modifier
                                    .weight(1f)
                                    .height(38.dp)
                                    .padding(2.dp)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(
                                        when {
                                            isSelected -> SalonTerracotta
                                            isToday -> SalonTerracottaSoft.copy(alpha = 0.3f)
                                            else -> Color.Transparent
                                        }
                                    )
                                    .border(
                                        width = if (isToday && !isSelected) 1.dp else 0.dp,
                                        color = if (isToday && !isSelected) SalonTerracotta else Color.Transparent,
                                        shape = RoundedCornerShape(8.dp)
                                    )
                                    .clickable(enabled = !isPast) {
                                        val formatDisplay = SimpleDateFormat("EEEE d MMMM yyyy", Locale.FRENCH)
                                        val display = formatDisplay.format(cellCal.time).replaceFirstChar { it.uppercase() }
                                        onDateSelected(display, key)
                                    }
                                    .testTag("calendar_day_$dayNumber"),
                                contentAlignment = Alignment.Center
                            ) {
                                Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                    Text(
                                        text = "$dayNumber",
                                        fontSize = 12.sp,
                                        fontWeight = if (isSelected || isToday) FontWeight.Bold else FontWeight.Normal,
                                        color = when {
                                            isSelected -> SalonPaper
                                            isPast -> SalonInkSoft.copy(alpha = 0.4f)
                                            else -> SalonNavy
                                        }
                                    )
                                    if (!isPast && !isSelected) {
                                        Box(
                                            modifier = Modifier
                                                .size(3.dp)
                                                .clip(CircleShape)
                                                .background(SalonOkFg)
                                        )
                                    }
                                }
                            }
                        } else {
                            // Empty cell before first day of month or after end of month
                            Spacer(modifier = Modifier.weight(1f))
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(8.dp))

            // Quick native DatePicker dialog button
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.Center
            ) {
                OutlinedButton(
                    onClick = { showNativeDatePicker = true },
                    contentPadding = PaddingValues(horizontal = 12.dp, vertical = 4.dp),
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier.testTag("btn_open_calendar_picker")
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp),
                        tint = SalonTerracotta
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Ouvrir le sélecteur de date complet",
                        fontSize = 11.sp,
                        color = SalonNavy
                    )
                }
            }
        }
    }

    // Dialogue DatePicker natif
    if (showNativeDatePicker) {
        val datePickerState = rememberDatePickerState(
            initialSelectedDateMillis = System.currentTimeMillis(),
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    val cal = Calendar.getInstance().apply {
                        timeInMillis = utcTimeMillis
                        set(Calendar.HOUR_OF_DAY, 23)
                        set(Calendar.MINUTE, 59)
                    }
                    val now = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                    }
                    return cal.after(now)
                }
            }
        )

        DatePickerDialog(
            onDismissRequest = { showNativeDatePicker = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val millis = datePickerState.selectedDateMillis
                        if (millis != null) {
                            val picked = Date(millis)
                            val formatDisplay = SimpleDateFormat("EEEE d MMMM yyyy", Locale.FRENCH)
                            val formatKey = SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH)
                            val display = formatDisplay.format(picked).replaceFirstChar { it.uppercase() }
                            val key = formatKey.format(picked)
                            onDateSelected(display, key)

                            val pickedCal = Calendar.getInstance().apply { time = picked }
                            displayedYear = pickedCal.get(Calendar.YEAR)
                            displayedMonth = pickedCal.get(Calendar.MONTH)
                        }
                        showNativeDatePicker = false
                    }
                ) {
                    Text("Valider", color = SalonTerracotta, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showNativeDatePicker = false }) {
                    Text("Annuler", color = SalonInkSoft)
                }
            },
            colors = DatePickerDefaults.colors(containerColor = SalonPaper)
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    containerColor = SalonPaper,
                    titleContentColor = SalonNavy,
                    headlineContentColor = SalonNavy,
                    selectedDayContainerColor = SalonTerracotta,
                    selectedDayContentColor = SalonPaper,
                    todayDateBorderColor = SalonTerracotta
                )
            )
        }
    }
}

/**
 * Grille de créneaux horaires (Time Slot Grid)
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
private fun TimeSlotsGrid(
    creneaux: List<Creneau>,
    selectedCreneau: Creneau?,
    onSelectCreneau: (Creneau) -> Unit
) {
    Card(
        colors = CardDefaults.cardColors(containerColor = SalonPaper),
        shape = RoundedCornerShape(16.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, SalonLine, RoundedCornerShape(16.dp))
            .testTag("time_slots_grid")
    ) {
        Column(modifier = Modifier.padding(14.dp)) {
            // Group slots by period
            val morningSlots = creneaux.filter { it.heureDebut < "12h00" }
            val afternoonSlots = creneaux.filter { it.heureDebut in "12h00".."16h59" }
            val eveningSlots = creneaux.filter { it.heureDebut >= "17h00" }

            if (morningSlots.isNotEmpty()) {
                Text(
                    text = "Matinée (08h30 - 12h00)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SalonInkSoft,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    morningSlots.forEach { slot ->
                        TimeSlotChip(
                            creneau = slot,
                            isSelected = selectedCreneau?.id == slot.id,
                            onSelect = { onSelectCreneau(slot) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (afternoonSlots.isNotEmpty()) {
                Text(
                    text = "Après-midi (13h00 - 16h59)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SalonInkSoft,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    afternoonSlots.forEach { slot ->
                        TimeSlotChip(
                            creneau = slot,
                            isSelected = selectedCreneau?.id == slot.id,
                            onSelect = { onSelectCreneau(slot) }
                        )
                    }
                }
                Spacer(modifier = Modifier.height(12.dp))
            }

            if (eveningSlots.isNotEmpty()) {
                Text(
                    text = "Fin de journée (17h00 - 19h30)",
                    fontSize = 11.sp,
                    fontWeight = FontWeight.Bold,
                    color = SalonInkSoft,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(6.dp))
                FlowRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    verticalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    eveningSlots.forEach { slot ->
                        TimeSlotChip(
                            creneau = slot,
                            isSelected = selectedCreneau?.id == slot.id,
                            onSelect = { onSelectCreneau(slot) }
                        )
                    }
                }
            }
        }
    }
}

/**
 * Chip individuel pour chaque créneau horaire
 */
@Composable
private fun TimeSlotChip(
    creneau: Creneau,
    isSelected: Boolean,
    onSelect: () -> Unit
) {
    val isReserved = creneau.statut == CreneauStatut.RESERVE

    val bgColor = when {
        isSelected -> SalonTerracotta
        isReserved -> SalonLine.copy(alpha = 0.4f)
        else -> SalonCream
    }

    val textColor = when {
        isSelected -> SalonPaper
        isReserved -> SalonInkSoft.copy(alpha = 0.5f)
        else -> SalonNavy
    }

    val borderColor = when {
        isSelected -> SalonTerracotta
        isReserved -> Color.Transparent
        else -> SalonLine
    }

    Box(
        modifier = Modifier
            .clip(RoundedCornerShape(10.dp))
            .background(bgColor)
            .border(1.dp, borderColor, RoundedCornerShape(10.dp))
            .clickable { onSelect() }
            .padding(horizontal = 12.dp, vertical = 9.dp)
            .testTag("time_slot_${creneau.heureDebut}"),
        contentAlignment = Alignment.Center
    ) {
        Row(verticalAlignment = Alignment.CenterVertically) {
            Icon(
                imageVector = if (isSelected) Icons.Default.Check else Icons.Default.Schedule,
                contentDescription = null,
                tint = textColor,
                modifier = Modifier.size(13.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "${creneau.heureDebut} - ${creneau.heureFin}",
                fontSize = 12.sp,
                fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                color = textColor
            )

            if (isReserved) {
                Spacer(modifier = Modifier.width(6.dp))
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(4.dp))
                        .background(SalonDangerBg)
                        .padding(horizontal = 4.dp, vertical = 1.dp)
                ) {
                    Text(
                        text = "Complet",
                        fontSize = 9.sp,
                        fontWeight = FontWeight.Bold,
                        color = SalonDangerFg
                    )
                }
            }
        }
    }
}
