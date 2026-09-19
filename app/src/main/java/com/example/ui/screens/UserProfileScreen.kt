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
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.ErrorOutline
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Logout
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Save
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.VerifiedUser
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Salon
import com.example.data.model.UserProfile
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

/**
 * Écran de profil utilisateur :
 * Permet de visualiser et de modifier les informations essentielles du compte :
 * - Nom complet
 * - Numéro de téléphone
 * - Adresse email
 * - Salon préféré / habituel
 */
@Composable
fun UserProfileScreen(
    userProfile: UserProfile,
    salons: List<Salon>,
    reservationsCount: Int,
    successMessage: String?,
    errorMessage: String?,
    onSaveProfile: (nom: String, telephone: String, email: String, salonPrefereId: String) -> Unit,
    onClearMessages: () -> Unit,
    onGoToReservations: () -> Unit,
    onGoToNewBooking: () -> Unit,
    onLogout: () -> Unit
) {
    // État local des champs du formulaire
    var editNom by remember(userProfile.nom) { mutableStateOf(userProfile.nom) }
    var editTelephone by remember(userProfile.telephone) { mutableStateOf(userProfile.telephone) }
    var editEmail by remember(userProfile.email) { mutableStateOf(userProfile.email) }
    var selectedSalonId by remember(userProfile.salonPrefereId) { mutableStateOf(userProfile.salonPrefereId) }

    var isEditMode by remember { mutableStateOf(false) }
    var showLogoutDialog by remember { mutableStateOf(false) }

    // Détection de modifications par rapport au profil enregistré
    val hasChanges = editNom.trim() != userProfile.nom.trim() ||
            editTelephone.trim() != userProfile.telephone.trim() ||
            editEmail.trim() != userProfile.email.trim() ||
            selectedSalonId != userProfile.salonPrefereId

    // Nom de l'avatar (initiales)
    val initials = remember(userProfile.nom) {
        val parts = userProfile.nom.trim().split(" ").filter { it.isNotBlank() }
        when {
            parts.size >= 2 -> "${parts[0].first().uppercase()}${parts[1].first().uppercase()}"
            parts.isNotEmpty() && parts[0].length >= 2 -> parts[0].take(2).uppercase()
            parts.isNotEmpty() -> parts[0].take(1).uppercase()
            else -> "CL"
        }
    }

    val preferredSalon = remember(selectedSalonId, salons) {
        salons.find { it.id == selectedSalonId } ?: salons.firstOrNull()
    }

    val scrollState = rememberScrollState()

    Column(
        modifier = Modifier
            .fillMaxSize()
            .background(SalonCream)
            .verticalScroll(scrollState)
            .padding(16.dp)
            .testTag("screen_user_profile")
    ) {
        // En-tête profil stylisé
        Card(
            colors = CardDefaults.cardColors(containerColor = SalonPaper),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, SalonLine, RoundedCornerShape(20.dp))
        ) {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(20.dp),
                horizontalAlignment = Alignment.CenterHorizontally
            ) {
                // Avatar avec gradient et badge vérifié
                Box(contentAlignment = Alignment.BottomEnd) {
                    Box(
                        modifier = Modifier
                            .size(80.dp)
                            .clip(CircleShape)
                            .background(
                                Brush.linearGradient(
                                    listOf(SalonTerracotta, SalonNavy)
                                )
                            ),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = initials,
                            color = SalonPaper,
                            fontSize = 28.sp,
                            fontWeight = FontWeight.Bold,
                            fontFamily = FontFamily.Serif
                        )
                    }

                    Box(
                        modifier = Modifier
                            .size(26.dp)
                            .clip(CircleShape)
                            .background(SalonOkFg)
                            .border(2.dp, SalonPaper, CircleShape),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = "Vérifié",
                            tint = SalonPaper,
                            modifier = Modifier.size(15.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(12.dp))

                Text(
                    text = if (userProfile.nom.isNotBlank()) userProfile.nom else "Client(e) Invité(e)",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = SalonNavy,
                    fontFamily = FontFamily.Serif
                )

                Spacer(modifier = Modifier.height(4.dp))

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    horizontalArrangement = Arrangement.Center
                ) {
                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(SalonOkBg)
                            .border(1.dp, SalonOkFg.copy(alpha = 0.4f), RoundedCornerShape(8.dp))
                            .padding(horizontal = 8.dp, vertical = 3.dp)
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.VerifiedUser,
                                contentDescription = null,
                                tint = SalonOkFg,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = "Client(e) membre du réseau",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SalonOkFg
                            )
                        }
                    }
                }

                if (userProfile.dateInscription.isNotBlank()) {
                    Spacer(modifier = Modifier.height(4.dp))
                    Text(
                        text = "Membre depuis le ${userProfile.dateInscription}",
                        fontSize = 11.sp,
                        color = SalonInkSoft
                    )
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Métriques rapides du compte
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Carte RDV
                    Box(
                        modifier = Modifier
                            .weight(1f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(SalonCream)
                            .border(1.dp, SalonLine, RoundedCornerShape(12.dp))
                            .clickable { onGoToReservations() }
                            .padding(10.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.CalendarMonth,
                                    contentDescription = null,
                                    tint = SalonTerracotta,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = "$reservationsCount RDV",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SalonNavy
                                )
                            }
                            Text(
                                text = "Gérer mes RDV",
                                fontSize = 10.sp,
                                color = SalonTerracotta,
                                fontWeight = FontWeight.Medium
                            )
                        }
                    }

                    // Carte Salon Préféré
                    Box(
                        modifier = Modifier
                            .weight(1.3f)
                            .clip(RoundedCornerShape(12.dp))
                            .background(SalonCream)
                            .border(1.dp, SalonLine, RoundedCornerShape(12.dp))
                            .padding(10.dp)
                    ) {
                        Column(horizontalAlignment = Alignment.CenterHorizontally) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Storefront,
                                    contentDescription = null,
                                    tint = SalonNavy,
                                    modifier = Modifier.size(15.dp)
                                )
                                Spacer(modifier = Modifier.width(4.dp))
                                Text(
                                    text = preferredSalon?.quartier ?: "Angré",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SalonNavy
                                )
                            }
                            Text(
                                text = "Salon habituel",
                                fontSize = 10.sp,
                                color = SalonInkSoft
                            )
                        }
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Messages de statut (Succès ou Erreur)
        AnimatedVisibility(
            visible = successMessage != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            if (successMessage != null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = SalonOkBg),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .border(1.dp, SalonOkFg.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .testTag("profile_success_banner")
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.CheckCircle,
                            contentDescription = null,
                            tint = SalonOkFg,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = successMessage,
                            fontSize = 13.sp,
                            color = SalonOkFg,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = onClearMessages,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Fermer",
                                tint = SalonOkFg,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        AnimatedVisibility(
            visible = errorMessage != null,
            enter = fadeIn(),
            exit = fadeOut()
        ) {
            if (errorMessage != null) {
                Card(
                    colors = CardDefaults.cardColors(containerColor = SalonDangerBg),
                    shape = RoundedCornerShape(12.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(bottom = 12.dp)
                        .border(1.dp, SalonDangerFg.copy(alpha = 0.5f), RoundedCornerShape(12.dp))
                        .testTag("profile_error_banner")
                ) {
                    Row(
                        modifier = Modifier.padding(12.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.ErrorOutline,
                            contentDescription = null,
                            tint = SalonDangerFg,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = errorMessage,
                            fontSize = 13.sp,
                            color = SalonDangerFg,
                            fontWeight = FontWeight.Medium,
                            modifier = Modifier.weight(1f)
                        )
                        IconButton(
                            onClick = onClearMessages,
                            modifier = Modifier.size(24.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Fermer",
                                tint = SalonDangerFg,
                                modifier = Modifier.size(16.dp)
                            )
                        }
                    }
                }
            }
        }

        // Section Coordonnées du compte (Visualisation et Édition)
        Card(
            colors = CardDefaults.cardColors(containerColor = SalonPaper),
            shape = RoundedCornerShape(20.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, SalonLine, RoundedCornerShape(20.dp))
        ) {
            Column(modifier = Modifier.padding(18.dp)) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column {
                        Text(
                            text = "Informations du compte",
                            fontSize = 16.sp,
                            fontWeight = FontWeight.Bold,
                            color = SalonNavy,
                            fontFamily = FontFamily.Serif
                        )
                        Text(
                            text = "Coordonnées de réservation et de contact",
                            fontSize = 12.sp,
                            color = SalonInkSoft
                        )
                    }

                    // Bouton de bascule Mode Édition
                    IconButton(
                        onClick = { isEditMode = !isEditMode },
                        modifier = Modifier
                            .size(36.dp)
                            .clip(CircleShape)
                            .background(if (isEditMode) SalonTerracottaSoft.copy(alpha = 0.4f) else SalonCream)
                            .testTag("btn_toggle_edit_mode")
                    ) {
                        Icon(
                            imageVector = if (isEditMode) Icons.Default.Close else Icons.Default.Edit,
                            contentDescription = if (isEditMode) "Quitter l'édition" else "Modifier le profil",
                            tint = if (isEditMode) SalonTerracotta else SalonNavy,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                }

                Spacer(modifier = Modifier.height(16.dp))

                // Champ 1 : Nom complet
                Text(
                    text = "Nom complet",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SalonNavy
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = editNom,
                    onValueChange = {
                        editNom = it
                        onClearMessages()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("profile_input_name"),
                    placeholder = { Text("Ex: Aminata Koné", color = SalonInkSoft.copy(alpha = 0.6f)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Person,
                            contentDescription = null,
                            tint = SalonTerracotta,
                            modifier = Modifier.size(19.dp)
                        )
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SalonTerracotta,
                        unfocusedBorderColor = SalonLine,
                        focusedContainerColor = SalonPaper,
                        unfocusedContainerColor = if (isEditMode) SalonPaper else SalonCream.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Champ 2 : Numéro de téléphone
                Text(
                    text = "Numéro de téléphone",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SalonNavy
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = editTelephone,
                    onValueChange = {
                        editTelephone = it
                        onClearMessages()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("profile_input_phone"),
                    placeholder = { Text("Ex: +225 07 08 45 67 89", color = SalonInkSoft.copy(alpha = 0.6f)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Phone,
                            contentDescription = null,
                            tint = SalonTerracotta,
                            modifier = Modifier.size(19.dp)
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SalonTerracotta,
                        unfocusedBorderColor = SalonLine,
                        focusedContainerColor = SalonPaper,
                        unfocusedContainerColor = if (isEditMode) SalonPaper else SalonCream.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )
                Text(
                    text = "Ce numéro servira à recevoir les SMS et rappels de confirmation de vos créneaux.",
                    fontSize = 11.sp,
                    color = SalonInkSoft,
                    modifier = Modifier.padding(start = 4.dp, top = 2.dp)
                )

                Spacer(modifier = Modifier.height(12.dp))

                // Champ 3 : Adresse email
                Text(
                    text = "Adresse email",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SalonNavy
                )
                Spacer(modifier = Modifier.height(4.dp))
                OutlinedTextField(
                    value = editEmail,
                    onValueChange = {
                        editEmail = it
                        onClearMessages()
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("profile_input_email"),
                    placeholder = { Text("Ex: aminata.kone@gmail.com", color = SalonInkSoft.copy(alpha = 0.6f)) },
                    leadingIcon = {
                        Icon(
                            imageVector = Icons.Default.Email,
                            contentDescription = null,
                            tint = SalonTerracotta,
                            modifier = Modifier.size(19.dp)
                        )
                    },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SalonTerracotta,
                        unfocusedBorderColor = SalonLine,
                        focusedContainerColor = SalonPaper,
                        unfocusedContainerColor = if (isEditMode) SalonPaper else SalonCream.copy(alpha = 0.5f)
                    ),
                    shape = RoundedCornerShape(12.dp)
                )

                Spacer(modifier = Modifier.height(16.dp))

                // Champ 4 : Salon favori dans le réseau
                Text(
                    text = "Salon habituel dans le réseau",
                    fontSize = 12.sp,
                    fontWeight = FontWeight.SemiBold,
                    color = SalonNavy
                )
                Spacer(modifier = Modifier.height(6.dp))

                Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                    salons.forEach { salon ->
                        val isSelected = salon.id == selectedSalonId
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(if (isSelected) SalonTerracottaSoft.copy(alpha = 0.25f) else SalonCream)
                                .border(
                                    1.dp,
                                    if (isSelected) SalonTerracotta else SalonLine,
                                    RoundedCornerShape(10.dp)
                                )
                                .clickable {
                                    selectedSalonId = salon.id
                                    onClearMessages()
                                }
                                .padding(horizontal = 12.dp, vertical = 9.dp)
                                .testTag("profile_salon_choice_${salon.id}"),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(18.dp)
                                    .clip(CircleShape)
                                    .background(if (isSelected) SalonTerracotta else SalonPaper)
                                    .border(2.dp, if (isSelected) SalonTerracotta else SalonLine, CircleShape),
                                contentAlignment = Alignment.Center
                            ) {
                                if (isSelected) {
                                    Box(
                                        modifier = Modifier
                                            .size(7.dp)
                                            .clip(CircleShape)
                                            .background(SalonPaper)
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column(modifier = Modifier.weight(1f)) {
                                Text(
                                    text = salon.nom,
                                    fontSize = 13.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) SalonNavy else SalonInk
                                )
                                Text(
                                    text = "${salon.quartier} · ${salon.adresse}",
                                    fontSize = 11.sp,
                                    color = SalonInkSoft
                                )
                            }
                        }
                    }
                }

                Spacer(modifier = Modifier.height(20.dp))

                // Actions Enregistrer / Annuler
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.spacedBy(10.dp)
                ) {
                    // Bouton Annuler / Restaurer
                    OutlinedButton(
                        onClick = {
                            editNom = userProfile.nom
                            editTelephone = userProfile.telephone
                            editEmail = userProfile.email
                            selectedSalonId = userProfile.salonPrefereId
                            isEditMode = false
                            onClearMessages()
                        },
                        enabled = hasChanges,
                        modifier = Modifier
                            .weight(1f)
                            .height(48.dp)
                            .testTag("profile_btn_cancel"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.outlinedButtonColors(
                            contentColor = SalonNavy
                        )
                    ) {
                        Text("Rétablir", fontSize = 13.sp)
                    }

                    // Bouton Enregistrer
                    Button(
                        onClick = {
                            onSaveProfile(editNom, editTelephone, editEmail, selectedSalonId)
                            isEditMode = false
                        },
                        enabled = hasChanges || isEditMode,
                        modifier = Modifier
                            .weight(1.6f)
                            .height(48.dp)
                            .testTag("profile_btn_save"),
                        shape = RoundedCornerShape(12.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = SalonTerracotta,
                            contentColor = SalonPaper
                        )
                    ) {
                        Icon(
                            imageVector = Icons.Default.Save,
                            contentDescription = null,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Enregistrer",
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Avantages Réseau & Sécurité
        Card(
            colors = CardDefaults.cardColors(containerColor = SalonPaper),
            shape = RoundedCornerShape(16.dp),
            modifier = Modifier
                .fillMaxWidth()
                .border(1.dp, SalonLine, RoundedCornerShape(16.dp))
        ) {
            Column(modifier = Modifier.padding(16.dp)) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = SalonTerracotta,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(8.dp))
                    Text(
                        text = "Vos privilèges Réseau Abidjan",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = SalonNavy
                    )
                }

                Spacer(modifier = Modifier.height(10.dp))

                PrivilegeRow("Pré-remplissage instantané lors de chaque réservation")
                PrivilegeRow("Notification SMS / WhatsApp automatique 2h avant le créneau")
                PrivilegeRow("Transfert de créneau possible entre Yopougon, Treichville et Angré")
                PrivilegeRow("Tarifs transparents garantis en caisse sans surprise")

                Spacer(modifier = Modifier.height(14.dp))

                Button(
                    onClick = onGoToNewBooking,
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("profile_btn_new_booking"),
                    shape = RoundedCornerShape(12.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SalonNavy,
                        contentColor = SalonPaper
                    )
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCut,
                        contentDescription = null,
                        modifier = Modifier.size(15.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(
                        text = "Réserver une coiffure",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        }

        Spacer(modifier = Modifier.height(16.dp))

        // Bouton de réinitialisation / Déconnexion
        OutlinedButton(
            onClick = { showLogoutDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .height(46.dp)
                .testTag("profile_btn_logout"),
            shape = RoundedCornerShape(12.dp),
            colors = ButtonDefaults.outlinedButtonColors(
                contentColor = SalonDangerFg
            )
        ) {
            Icon(
                imageVector = Icons.Default.Logout,
                contentDescription = null,
                tint = SalonDangerFg,
                modifier = Modifier.size(16.dp)
            )
            Spacer(modifier = Modifier.width(6.dp))
            Text(
                text = "Déconnexion / Réinitialiser ce compte",
                fontSize = 13.sp,
                fontWeight = FontWeight.SemiBold,
                color = SalonDangerFg
            )
        }

        Spacer(modifier = Modifier.height(24.dp))
    }

    // Boîte de dialogue de confirmation de déconnexion
    if (showLogoutDialog) {
        AlertDialog(
            onDismissRequest = { showLogoutDialog = false },
            title = {
                Text(
                    text = "Confirmer la déconnexion",
                    fontFamily = FontFamily.Serif,
                    fontWeight = FontWeight.Bold,
                    color = SalonNavy
                )
            },
            text = {
                Text(
                    text = "Voulez-vous vraiment vous déconnecter ? Les coordonnées enregistrées sur cet appareil seront effacées.",
                    color = SalonInk
                )
            },
            confirmButton = {
                Button(
                    onClick = {
                        showLogoutDialog = false
                        onLogout()
                    },
                    colors = ButtonDefaults.buttonColors(
                        containerColor = SalonDangerFg,
                        contentColor = SalonPaper
                    )
                ) {
                    Text("Oui, me déconnecter")
                }
            },
            dismissButton = {
                TextButton(onClick = { showLogoutDialog = false }) {
                    Text("Annuler", color = SalonInkSoft)
                }
            },
            containerColor = SalonPaper,
            shape = RoundedCornerShape(16.dp)
        )
    }
}

@Composable
private fun PrivilegeRow(text: String) {
    Row(
        modifier = Modifier.padding(vertical = 3.dp),
        verticalAlignment = Alignment.CenterVertically
    ) {
        Icon(
            imageVector = Icons.Default.Check,
            contentDescription = null,
            tint = SalonOkFg,
            modifier = Modifier.size(14.dp)
        )
        Spacer(modifier = Modifier.width(8.dp))
        Text(
            text = text,
            fontSize = 12.sp,
            color = SalonInk
        )
    }
}
