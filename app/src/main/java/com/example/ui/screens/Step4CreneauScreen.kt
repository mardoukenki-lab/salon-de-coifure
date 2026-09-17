package com.example.ui.screens

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.animation.fadeIn
import androidx.compose.animation.fadeOut
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
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
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.ElectricBolt
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.SwapHoriz
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextDecoration
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Creneau
import com.example.data.model.CreneauStatut
import com.example.data.model.Salon
import com.example.data.repository.CrossSiteAlternative
import com.example.ui.components.DateTimePickerSection
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

@Composable
fun Step4CreneauScreen(
    salon: Salon,
    availableDays: List<Pair<String, String>>,
    selectedDateIndex: Int,
    selectedDateDisplay: String,
    creneaux: List<Creneau>,
    selectedCreneau: Creneau?,
    crossSiteSuggestion: CrossSiteAlternative?,
    onSelectDateIndex: (Int) -> Unit,
    onSelectCustomDate: (dateDisplay: String, dateKey: String) -> Unit,
    onFilterTimeSlot: (String) -> Unit,
    onSelectCreneau: (Creneau) -> Unit,
    onAcceptCrossSiteAlternative: () -> Unit,
    onContinueClick: () -> Unit
) {
    val selectedDayPair = availableDays.getOrNull(selectedDateIndex)

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SalonCream)
            .testTag("screen_step_4"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Étape 4 · Le créneau",
                    fontSize = 12.sp,
                    color = SalonTerracotta,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "$selectedDateDisplay — ${salon.nom}",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = SalonNavy,
                    fontFamily = FontFamily.Serif
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Disponibilité en temps réel. Si votre créneau est complet, notre réseau vous propose immédiatement une alternative dans un salon voisin.",
                    fontSize = 13.sp,
                    color = SalonInkSoft,
                    lineHeight = 18.sp
                )
            }
        }

        // Dedicated Date and Time Picker Component
        item {
            DateTimePickerSection(
                selectedDateDisplay = selectedDateDisplay,
                selectedTimeStr = selectedCreneau?.heureDebut,
                onCustomDateSelected = onSelectCustomDate,
                onTimePicked = onFilterTimeSlot
            )
        }

        // Quick Day Selector Tabs
        item {
            Column {
                Text(
                    text = "Jours rapides :",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SalonInkSoft,
                    modifier = Modifier.padding(bottom = 6.dp)
                )
                LazyRow(
                    horizontalArrangement = Arrangement.spacedBy(8.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    itemsIndexed(availableDays) { index, dayPair ->
                        val isDaySelected = index == selectedDateIndex

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isDaySelected) SalonTerracotta else SalonPaper)
                                .border(
                                    1.dp,
                                    if (isDaySelected) SalonTerracotta else SalonLine,
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable { onSelectDateIndex(index) }
                                .padding(horizontal = 14.dp, vertical = 10.dp)
                                .testTag("day_tab_$index"),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = dayPair.first,
                                fontSize = 13.sp,
                                fontWeight = if (isDaySelected) FontWeight.Bold else FontWeight.Medium,
                                color = if (isDaySelected) SalonPaper else SalonNavy
                            )
                        }
                    }
                }
            }
        }

        // Cross-Site Alternative Banner (Featured in Specification!)
        item {
            AnimatedVisibility(
                visible = crossSiteSuggestion != null,
                enter = fadeIn(),
                exit = fadeOut()
            ) {
                if (crossSiteSuggestion != null) {
                    Card(
                        shape = RoundedCornerShape(12.dp),
                        colors = CardDefaults.cardColors(containerColor = SalonTerracottaSoft.copy(alpha = 0.45f)),
                        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.5.dp, SalonTerracotta, RoundedCornerShape(12.dp))
                            .testTag("card_cross_site_suggestion")
                    ) {
                        Column(modifier = Modifier.padding(14.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(28.dp)
                                        .clip(RoundedCornerShape(6.dp))
                                        .background(SalonTerracotta),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.ElectricBolt,
                                        contentDescription = null,
                                        tint = SalonPaper,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Disponibilité cross-site détectée !",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = SalonNavy
                                )
                            }

                            Spacer(modifier = Modifier.height(8.dp))

                            Text(
                                text = "Ce créneau est complet à ${salon.quartier}, mais disponible exactement à ${crossSiteSuggestion.heureDebut} au ${crossSiteSuggestion.salonAlternatif.nom} (${crossSiteSuggestion.distanceKm} km).",
                                fontSize = 12.sp,
                                color = SalonNavy,
                                lineHeight = 17.sp
                            )

                            Spacer(modifier = Modifier.height(10.dp))

                            Button(
                                onClick = onAcceptCrossSiteAlternative,
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SalonTerracotta),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("btn_accept_cross_site")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.SwapHoriz,
                                    contentDescription = null,
                                    modifier = Modifier.size(16.dp)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = "Basculer sur ${crossSiteSuggestion.salonAlternatif.nom} (${crossSiteSuggestion.heureDebut})",
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }
                    }
                }
            }
        }

        // Slot Grid (2 columns layout matching mockup)
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SalonPaper),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, SalonLine, RoundedCornerShape(14.dp))
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Créneaux horaires",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = SalonNavy
                        )
                        Row(horizontalArrangement = Arrangement.spacedBy(10.dp)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(SalonOkFg)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "Libre", fontSize = 11.sp, color = SalonInkSoft)
                            }
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(8.dp)
                                        .clip(RoundedCornerShape(2.dp))
                                        .background(SalonDangerFg)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(text = "Complet", fontSize = 11.sp, color = SalonInkSoft)
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Group slots in pairs of 2
                    val chunkedSlots = creneaux.chunked(2)
                    chunkedSlots.forEach { pair ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 4.dp),
                            horizontalArrangement = Arrangement.spacedBy(10.dp)
                        ) {
                            pair.forEach { creneau ->
                                val isAvailable = creneau.statut == CreneauStatut.LIBRE
                                val isChosen = selectedCreneau?.id == creneau.id

                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(
                                            when {
                                                isChosen -> SalonTerracotta
                                                isAvailable -> SalonOkBg
                                                else -> SalonDangerBg
                                            }
                                        )
                                        .border(
                                            width = if (isChosen) 2.dp else 1.dp,
                                            color = when {
                                                isChosen -> SalonTerracotta
                                                isAvailable -> SalonOkFg.copy(alpha = 0.3f)
                                                else -> SalonDangerFg.copy(alpha = 0.2f)
                                            },
                                            shape = RoundedCornerShape(8.dp)
                                        )
                                        .clickable { onSelectCreneau(creneau) }
                                        .padding(vertical = 12.dp)
                                        .testTag("slot_${creneau.id}"),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Column(horizontalAlignment = Alignment.CenterHorizontally) {
                                        Text(
                                            text = "${creneau.heureDebut} – ${creneau.heureFin}",
                                            fontSize = 13.sp,
                                            fontWeight = if (isChosen) FontWeight.Bold else FontWeight.SemiBold,
                                            color = when {
                                                isChosen -> SalonPaper
                                                isAvailable -> SalonOkFg
                                                else -> SalonDangerFg
                                            },
                                            textDecoration = if (!isAvailable && !isChosen) TextDecoration.LineThrough else TextDecoration.None
                                        )
                                        Text(
                                            text = when {
                                                isChosen -> "Sélectionné ✓"
                                                isAvailable -> "Disponible"
                                                else -> "Complet (voir alt.)"
                                            },
                                            fontSize = 10.sp,
                                            color = when {
                                                isChosen -> SalonPaper.copy(alpha = 0.9f)
                                                isAvailable -> SalonOkFg.copy(alpha = 0.8f)
                                                else -> SalonDangerFg.copy(alpha = 0.8f)
                                            }
                                        )
                                    }
                                }
                            }
                            // If odd number, pad with empty spacer
                            if (pair.size == 1) {
                                Spacer(modifier = Modifier.weight(1f))
                            }
                        }
                    }
                }
            }
        }

        // Continue Button
        item {
            Button(
                onClick = onContinueClick,
                enabled = selectedCreneau != null,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = SalonTerracotta,
                    disabledContainerColor = SalonLine
                ),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(48.dp)
                    .testTag("btn_continue_to_payment")
            ) {
                Text(
                    text = if (selectedCreneau != null) {
                        "Continuer vers la confirmation (${selectedCreneau.heureDebut})"
                    } else {
                        "Veuillez choisir un créneau libre"
                    },
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = if (selectedCreneau != null) SalonPaper else SalonInkSoft
                )
                if (selectedCreneau != null) {
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
