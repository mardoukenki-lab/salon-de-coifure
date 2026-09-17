package com.example.ui.screens

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Cancel
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ReservationEntity
import com.example.ui.theme.SalonCream
import com.example.ui.theme.SalonDangerBg
import com.example.ui.theme.SalonDangerFg
import com.example.ui.theme.SalonInk
import com.example.ui.theme.SalonInkSoft
import com.example.ui.theme.SalonLine
import com.example.ui.theme.SalonNavy
import com.example.ui.theme.SalonOkBg
import com.example.ui.theme.SalonOkFg
import com.example.ui.theme.SalonPaper
import com.example.ui.theme.SalonTerracotta
import com.example.ui.theme.SalonTerracottaSoft

@Composable
fun MesReservationsScreen(
    reservations: List<ReservationEntity>,
    onCancelReservation: (Int) -> Unit,
    onBookNewClick: () -> Unit
) {
    var reservationToCancel by remember { mutableStateOf<ReservationEntity?>(null) }

    if (reservationToCancel != null) {
        AlertDialog(
            onDismissRequest = { reservationToCancel = null },
            title = {
                Text(
                    text = "Annuler la réservation ?",
                    fontWeight = FontWeight.Bold,
                    color = SalonNavy
                )
            },
            text = {
                Text(
                    text = "Êtes-vous sûre de vouloir annuler le rendez-vous ${reservationToCancel?.codeReservation} au ${reservationToCancel?.salonNom} ? Le créneau sera immédiatement libéré.",
                    fontSize = 13.sp,
                    color = SalonInkSoft
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        reservationToCancel?.let { onCancelReservation(it.id) }
                        reservationToCancel = null
                    },
                    colors = ButtonDefaults.buttonColors(containerColor = SalonDangerFg)
                ) {
                    Text("Oui, annuler")
                }
            },
            dismissButton = {
                TextButton(onClick = { reservationToCancel = null }) {
                    Text("Conserver", color = SalonInkSoft)
                }
            }
        )
    }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SalonCream)
            .testTag("screen_mes_reservations"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Mes Rendez-vous",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = SalonNavy,
                    fontFamily = FontFamily.Serif
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Consultez vos réservations en cours et vos historiques dans nos 3 salons à Abidjan.",
                    fontSize = 13.sp,
                    color = SalonInkSoft
                )
            }
        }

        if (reservations.isEmpty()) {
            item {
                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(containerColor = SalonPaper),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(vertical = 24.dp)
                ) {
                    Column(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(28.dp),
                        horizontalAlignment = Alignment.CenterHorizontally
                    ) {
                        Box(
                            modifier = Modifier
                                .size(56.dp)
                                .clip(CircleShape)
                                .background(SalonCream),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = null,
                                tint = SalonTerracotta,
                                modifier = Modifier.size(30.dp)
                            )
                        }
                        Spacer(modifier = Modifier.height(14.dp))
                        Text(
                            text = "Aucun rendez-vous prévu",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = SalonNavy
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Text(
                            text = "Vous n'avez pas encore de séance programmée. Choisissez un salon et réservez votre coiffure en quelques clics !",
                            fontSize = 12.sp,
                            color = SalonInkSoft,
                            textAlign = androidx.compose.ui.text.style.TextAlign.Center
                        )
                        Spacer(modifier = Modifier.height(18.dp))
                        Button(
                            onClick = onBookNewClick,
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SalonTerracotta),
                            modifier = Modifier.testTag("btn_empty_book_now")
                        ) {
                            Text("Prendre rendez-vous")
                            Spacer(modifier = Modifier.width(6.dp))
                            Icon(Icons.AutoMirrored.Filled.ArrowForward, contentDescription = null)
                        }
                    }
                }
            }
        } else {
            items(reservations) { rdv ->
                val isCancelled = rdv.statut == "ANNULEE"

                Card(
                    shape = RoundedCornerShape(14.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isCancelled) SalonCream else SalonPaper
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(1.dp, SalonLine, RoundedCornerShape(14.dp))
                        .testTag("card_rdv_${rdv.id}")
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = rdv.codeReservation,
                                    fontSize = 15.sp,
                                    fontWeight = FontWeight.ExtraBold,
                                    color = if (isCancelled) SalonInkSoft else SalonTerracotta
                                )
                                Text(
                                    text = "Client : ${rdv.clientNom}",
                                    fontSize = 11.sp,
                                    color = SalonInkSoft
                                )
                            }

                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (isCancelled) SalonDangerBg else SalonOkBg)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = if (isCancelled) "Annulée" else "Confirmée ✓",
                                    fontSize = 11.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = if (isCancelled) SalonDangerFg else SalonOkFg
                                )
                            }
                        }

                        Spacer(modifier = Modifier.height(12.dp))
                        HorizontalDivider(color = SalonLine, thickness = 1.dp)
                        Spacer(modifier = Modifier.height(12.dp))

                        // Date & Heure
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = null,
                                tint = SalonTerracotta,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${rdv.dateStr} à ${rdv.heure}",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = SalonNavy
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Salon
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = SalonInkSoft,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${rdv.salonNom} (${rdv.salonAdresse})",
                                fontSize = 12.sp,
                                color = SalonInkSoft
                            )
                        }

                        Spacer(modifier = Modifier.height(6.dp))

                        // Service
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.ContentCut,
                                contentDescription = null,
                                tint = SalonInkSoft,
                                modifier = Modifier.size(16.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${rdv.serviceNom} (${rdv.optionsNoms})",
                                fontSize = 12.sp,
                                color = SalonInk
                            )
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = "Payé via ${rdv.modePaiement}",
                                    fontSize = 11.sp,
                                    color = SalonInkSoft
                                )
                                Text(
                                    text = "${String.format("%,d", rdv.prixTotal).replace(',', ' ')} FCFA",
                                    fontSize = 16.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SalonNavy
                                )
                            }

                            if (!isCancelled) {
                                OutlinedButton(
                                    onClick = { reservationToCancel = rdv },
                                    shape = RoundedCornerShape(8.dp),
                                    modifier = Modifier.testTag("btn_cancel_rdv_${rdv.id}")
                                ) {
                                    Text(
                                        text = "Annuler",
                                        fontSize = 11.sp,
                                        color = SalonDangerFg
                                    )
                                }
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
