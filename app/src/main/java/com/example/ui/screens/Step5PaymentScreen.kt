package com.example.ui.screens

import androidx.compose.animation.core.RepeatMode
import androidx.compose.animation.core.animateFloat
import androidx.compose.animation.core.infiniteRepeatable
import androidx.compose.animation.core.rememberInfiniteTransition
import androidx.compose.animation.core.tween
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.PointOfSale
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Creneau
import com.example.data.model.HairService
import com.example.data.model.Salon
import com.example.data.model.ServiceOption
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
fun Step5PaymentScreen(
    salon: Salon,
    service: HairService,
    options: List<ServiceOption>,
    creneau: Creneau,
    dateStr: String,
    totalPrix: Int,
    remainingSeconds: Int,
    clientNom: String,
    clientTelephone: String,
    isUserRegistered: Boolean,
    isProcessing: Boolean,
    onClientDetailsChange: (String, String) -> Unit,
    onRegisterClick: () -> Unit,
    onConfirmReservationClick: () -> Unit
) {
    val minutes = remainingSeconds / 60
    val seconds = remainingSeconds % 60
    val timerFormatted = String.format("%02d:%02d", minutes, seconds)

    val infiniteTransition = rememberInfiniteTransition(label = "pulse")
    val pulseAlpha by infiniteTransition.animateFloat(
        initialValue = 0.6f,
        targetValue = 1f,
        animationSpec = infiniteRepeatable(
            animation = tween(800),
            repeatMode = RepeatMode.Reverse
        ),
        label = "alpha"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SalonCream)
            .testTag("screen_step_5"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Étape 5 · Mode de règlement",
                    fontSize = 12.sp,
                    color = SalonTerracotta,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Paiement à la caisse",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = SalonNavy,
                    fontFamily = FontFamily.Serif
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Aucun prépaiement en ligne requis. Réglez directement à la caisse du salon le jour de votre prestation (espèces ou carte bancaire).",
                    fontSize = 13.sp,
                    color = SalonInkSoft,
                    lineHeight = 18.sp
                )
            }
        }

        // Live Hold Countdown Card
        item {
            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SalonTerracottaSoft.copy(alpha = 0.35f)),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, SalonTerracotta, RoundedCornerShape(12.dp))
                    .testTag("card_hold_timer")
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(14.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Box(
                            modifier = Modifier
                                .size(34.dp)
                                .clip(CircleShape)
                                .background(SalonTerracotta)
                                .alpha(pulseAlpha),
                            contentAlignment = Alignment.Center
                        ) {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = SalonPaper,
                                modifier = Modifier.size(18.dp)
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Column {
                            Text(
                                text = "Créneau réservé pour vous",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = SalonNavy
                            )
                            Text(
                                text = "${creneau.heureDebut} – ${creneau.heureFin} (${salon.nom})",
                                fontSize = 11.sp,
                                color = SalonInkSoft
                            )
                        }
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Expire dans",
                            fontSize = 10.sp,
                            color = SalonInkSoft
                        )
                        Text(
                            text = timerFormatted,
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SalonTerracotta
                        )
                    }
                }
            }
        }

        // Client Details & Registration Card
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SalonPaper),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, if (isUserRegistered) SalonOkFg else SalonTerracotta, RoundedCornerShape(14.dp))
                    .testTag("card_client_registration")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Compte & Coordonnées",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = SalonNavy
                        )
                        if (isUserRegistered) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SalonOkBg)
                                    .border(1.dp, SalonOkFg, RoundedCornerShape(8.dp))
                                    .padding(horizontal = 8.dp, vertical = 3.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = SalonOkFg,
                                        modifier = Modifier.size(12.dp)
                                    )
                                    Spacer(modifier = Modifier.width(4.dp))
                                    Text(
                                        text = "Inscrit / Connecté",
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SalonOkFg
                                    )
                                }
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    if (!isUserRegistered) {
                        // Obligation d'inscription callout
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(SalonTerracottaSoft.copy(alpha = 0.35f))
                                .border(1.dp, SalonTerracottaSoft, RoundedCornerShape(10.dp))
                                .padding(12.dp)
                        ) {
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.PersonAdd,
                                        contentDescription = null,
                                        tint = SalonTerracotta,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Inscription obligatoire avant de réserver",
                                        fontSize = 13.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SalonNavy
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = "Les visiteurs doivent s'inscrire afin de sécuriser le créneau et recevoir leur récapitulatif par SMS.",
                                    fontSize = 11.sp,
                                    color = SalonInkSoft,
                                    lineHeight = 15.sp
                                )
                                Spacer(modifier = Modifier.height(8.dp))
                                Button(
                                    onClick = onRegisterClick,
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = SalonTerracotta),
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .testTag("btn_register_gate")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.PersonAdd,
                                        contentDescription = null,
                                        modifier = Modifier.size(16.dp)
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "S'inscrire / Se connecter maintenant",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold
                                    )
                                }
                            }
                        }
                    } else {
                        OutlinedTextField(
                            value = clientNom,
                            onValueChange = { onClientDetailsChange(it, clientTelephone) },
                            label = { Text("Nom et Prénom") },
                            leadingIcon = {
                                Icon(Icons.Default.Person, contentDescription = null, tint = SalonTerracotta)
                            },
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SalonTerracotta,
                                unfocusedBorderColor = SalonLine
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_client_nom")
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = clientTelephone,
                            onValueChange = { onClientDetailsChange(clientNom, it) },
                            label = { Text("Numéro de téléphone (ex: 07 08 12 34 56)") },
                            leadingIcon = {
                                Icon(Icons.Default.Phone, contentDescription = null, tint = SalonTerracotta)
                            },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SalonTerracotta,
                                unfocusedBorderColor = SalonLine
                            ),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_client_tel")
                        )
                    }
                }
            }
        }

        // Mode de règlement : Paiement à la caisse Card (In place of Mobile Money operators)
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SalonPaper),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, SalonLine, RoundedCornerShape(14.dp))
                    .testTag("card_payment_at_counter")
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Text(
                        text = "Mode de règlement sélectionné",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = SalonNavy
                    )
                    Spacer(modifier = Modifier.height(12.dp))

                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(12.dp))
                            .background(SalonOkBg.copy(alpha = 0.5f))
                            .border(1.5.dp, SalonOkFg, RoundedCornerShape(12.dp))
                            .padding(14.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(CircleShape)
                                    .background(SalonOkFg),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.PointOfSale,
                                    contentDescription = null,
                                    tint = SalonPaper,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Règlement à la caisse du salon",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 14.sp,
                                        color = SalonNavy
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Icon(
                                        imageVector = Icons.Default.CheckCircle,
                                        contentDescription = null,
                                        tint = SalonOkFg,
                                        modifier = Modifier.size(16.dp)
                                    )
                                }
                                Spacer(modifier = Modifier.height(2.dp))
                                Text(
                                    text = "Vous réglerez votre prestation sur place le jour du rendez-vous auprès de l'accueil.",
                                    fontSize = 12.sp,
                                    color = SalonInkSoft,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(SalonCream)
                            .padding(10.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = SalonTerracotta,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = "Moyens acceptés sur place : Espèces, Carte bancaire (Visa/Mastercard) & TPE.",
                            fontSize = 11.sp,
                            color = SalonInk
                        )
                    }
                }
            }
        }

        // Summary Devis Row & Confirmation Button
        item {
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SalonPaper),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
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
                        Column {
                            Text(
                                text = "Montant total à régler sur place",
                                fontSize = 13.sp,
                                color = SalonInkSoft
                            )
                            Text(
                                text = "${service.nom} · ${dateStr}",
                                fontSize = 11.sp,
                                color = SalonInkSoft
                            )
                        }
                        Text(
                            text = "${String.format("%,d", totalPrix).replace(',', ' ')} FCFA",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SalonNavy
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    if (!isUserRegistered) {
                        Button(
                            onClick = onRegisterClick,
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SalonTerracotta),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("btn_confirm_payment")
                        ) {
                            Icon(
                                imageVector = Icons.Default.PersonAdd,
                                contentDescription = null,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(
                                text = "S'inscrire pour confirmer la réservation",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    } else {
                        Button(
                            onClick = onConfirmReservationClick,
                            enabled = !isProcessing && clientNom.isNotBlank() && clientTelephone.isNotBlank(),
                            shape = RoundedCornerShape(10.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SalonTerracotta),
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(50.dp)
                                .testTag("btn_confirm_payment")
                        ) {
                            if (isProcessing) {
                                CircularProgressIndicator(
                                    color = SalonPaper,
                                    modifier = Modifier.size(20.dp),
                                    strokeWidth = 2.dp
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Confirmation du créneau en cours...",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            } else {
                                Icon(
                                    imageVector = Icons.Default.PointOfSale,
                                    contentDescription = null,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Confirmer la réservation (Paiement en salon)",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Text(
                        text = "Réservation immédiate et garantie. Règlement à la caisse le jour J.",
                        fontSize = 11.sp,
                        color = SalonInkSoft,
                        modifier = Modifier.align(Alignment.CenterHorizontally)
                    )
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
