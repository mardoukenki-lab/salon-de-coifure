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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Message
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.QrCode
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.ReservationEntity
import com.example.ui.theme.SalonCream
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
fun Step6ConfirmationScreen(
    reservation: ReservationEntity,
    onViewMyBookingsClick: () -> Unit,
    onNewBookingClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SalonCream)
            .testTag("screen_step_6"),
        contentPadding = PaddingValues(16.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Spacer(modifier = Modifier.height(10.dp))
            // Big Checkmark in OkBg circle (matching mockup)
            Box(
                modifier = Modifier
                    .size(68.dp)
                    .clip(CircleShape)
                    .background(SalonOkBg)
                    .border(2.dp, SalonOkFg, CircleShape),
                contentAlignment = Alignment.Center
            ) {
                Icon(
                    imageVector = Icons.Default.Check,
                    contentDescription = null,
                    tint = SalonOkFg,
                    modifier = Modifier.size(36.dp)
                )
            }

            Spacer(modifier = Modifier.height(12.dp))

            Text(
                text = "Rendez-vous calé !",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold,
                color = SalonNavy,
                fontFamily = FontFamily.Serif
            )
            Spacer(modifier = Modifier.height(4.dp))
            Text(
                text = "Votre réservation est confirmée et synchronisée avec le salon.",
                fontSize = 13.sp,
                color = SalonInkSoft,
                textAlign = TextAlign.Center
            )
        }

        // Ticket / Receipt Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SalonPaper),
                elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, SalonLine, RoundedCornerShape(16.dp))
                    .testTag("card_confirmed_receipt")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Code de réservation",
                                fontSize = 11.sp,
                                color = SalonInkSoft
                            )
                            Text(
                                text = reservation.codeReservation,
                                fontSize = 16.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = SalonTerracotta
                            )
                        }
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(SalonOkBg)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Confirmé ✓",
                                fontSize = 11.sp,
                                color = SalonOkFg,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = SalonLine, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(14.dp))

                    // Date & Time highlighted
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(SalonCream),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.CalendarMonth,
                                contentDescription = null,
                                tint = SalonTerracotta,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "${reservation.dateStr} à ${reservation.heure}",
                                fontSize = 15.sp,
                                fontWeight = FontWeight.Bold,
                                color = SalonNavy
                            )
                            Text(
                                text = "Durée estimée : ~${reservation.dureeTotaleMin} minutes",
                                fontSize = 11.sp,
                                color = SalonInkSoft
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))

                    // Salon Location
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(SalonCream),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = SalonTerracotta,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = reservation.salonNom,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SalonNavy
                            )
                            Text(
                                text = reservation.salonAdresse,
                                fontSize = 11.sp,
                                color = SalonInkSoft
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = SalonLine, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Service & Options
                    Text(
                        text = "Prestation : ${reservation.serviceNom}",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SalonNavy
                    )
                    if (reservation.optionsNoms.isNotBlank() && reservation.optionsNoms != "Aucune option") {
                        Text(
                            text = "Options : ${reservation.optionsNoms}",
                            fontSize = 12.sp,
                            color = SalonInkSoft
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    // Payment details
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Règlement : ${reservation.modePaiement}",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SalonNavy
                            )
                            Text(
                                text = "Réf : ${reservation.referencePaiement}",
                                fontSize = 11.sp,
                                color = SalonInkSoft
                            )
                        }
                        Column(horizontalAlignment = Alignment.End) {
                            Text(
                                text = "À régler au salon",
                                fontSize = 10.sp,
                                color = SalonInkSoft
                            )
                            Text(
                                text = "${String.format("%,d", reservation.prixTotal).replace(',', ' ')} FCFA",
                                fontSize = 18.sp,
                                fontWeight = FontWeight.ExtraBold,
                                color = SalonTerracotta
                            )
                        }
                    }
                }
            }
        }

        // SMS notification confirmation callout (from mockup: "SMS de rappel envoyé")
        item {
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(SalonCream)
                    .border(1.dp, SalonLine, RoundedCornerShape(10.dp))
                    .padding(12.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Message,
                        contentDescription = null,
                        tint = SalonTerracotta,
                        modifier = Modifier.size(20.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Column {
                        Text(
                            text = "SMS et WhatsApp de rappel envoyés",
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 12.sp,
                            color = SalonNavy
                        )
                        Text(
                            text = "Un rappel avec localisation GPS vous sera renvoyé 2h avant le rendez-vous.",
                            fontSize = 11.sp,
                            color = SalonInkSoft
                        )
                    }
                }
            }
        }

        // Buttons
        item {
            Button(
                onClick = onViewMyBookingsClick,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SalonNavy),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .testTag("btn_view_my_bookings")
            ) {
                Text(
                    text = "Voir mes réservations",
                    fontSize = 13.sp,
                    fontWeight = FontWeight.SemiBold
                )
            }

            Spacer(modifier = Modifier.height(8.dp))

            OutlinedButton(
                onClick = onNewBookingClick,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .height(46.dp)
                    .testTag("btn_new_booking")
            ) {
                Text(
                    text = "Faire une autre réservation",
                    fontSize = 13.sp,
                    color = SalonTerracotta,
                    fontWeight = FontWeight.SemiBold
                )
            }
        }

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
