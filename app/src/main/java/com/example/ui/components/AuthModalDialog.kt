package com.example.ui.components

import android.content.Context
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
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
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRow
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Salon
import com.example.ui.theme.SalonCream
import com.example.ui.theme.SalonDangerBg
import com.example.ui.theme.SalonDangerFg
import com.example.ui.theme.SalonInk
import com.example.ui.theme.SalonInkSoft
import com.example.ui.theme.SalonLine
import com.example.ui.theme.SalonNavy
import com.example.ui.theme.SalonOkFg
import com.example.ui.theme.SalonPaper
import com.example.ui.theme.SalonTerracotta
import com.example.ui.theme.SalonTerracottaSoft

/**
 * Dialogue d'inscription / connexion obligatoire avant de pouvoir réserver.
 * Supporte :
 * 1. Création de compte avec Email, Mot de passe, Nom complet, Téléphone et Salon favori.
 * 2. Connexion avec Email et Mot de passe existants.
 * 3. Connexion instantanée avec Google (Credential Manager / Firebase Auth).
 */
@Composable
fun AuthModalDialog(
    salons: List<Salon> = emptyList(),
    isLoading: Boolean = false,
    errorMessage: String? = null,
    onDismiss: () -> Unit,
    onRegisterWithEmail: (nom: String, telephone: String, email: String, motDePasse: String, salonPrefereId: String) -> Unit,
    onLoginWithEmail: (email: String, motDePasse: String) -> Unit,
    onGoogleSignIn: (context: Context) -> Unit
) {
    val context = LocalContext.current
    var selectedTabIndex by remember { mutableIntStateOf(0) } // 0 = Inscription, 1 = Connexion

    // Form fields
    var nom by remember { mutableStateOf("") }
    var telephone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var motDePasse by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var selectedSalonId by remember {
        mutableStateOf(salons.firstOrNull()?.id ?: "angre")
    }

    var localValidationError by remember { mutableStateOf<String?>(null) }
    val displayError = errorMessage ?: localValidationError

    val scrollState = rememberScrollState()

    AlertDialog(
        onDismissRequest = {
            if (!isLoading) onDismiss()
        },
        shape = RoundedCornerShape(20.dp),
        containerColor = SalonPaper,
        tonalElevation = 8.dp,
        modifier = Modifier.testTag("dialog_auth_modal"),
        title = {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column(modifier = Modifier.weight(1f)) {
                    Text(
                        text = if (selectedTabIndex == 0) "Inscription client(e)" else "Connexion client(e)",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = SalonNavy,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = "Obligatoire avant de réserver votre créneau",
                        fontSize = 12.sp,
                        color = SalonTerracotta,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                IconButton(
                    onClick = onDismiss,
                    enabled = !isLoading,
                    modifier = Modifier.size(32.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Close,
                        contentDescription = "Fermer",
                        tint = SalonInkSoft
                    )
                }
            }
        },
        text = {
            Column(
                modifier = Modifier
                    .fillMaxWidth()
                    .verticalScroll(scrollState)
                    .padding(top = 4.dp),
                verticalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                // Tab Switcher Inscription / Connexion
                TabRow(
                    selectedTabIndex = selectedTabIndex,
                    containerColor = SalonCream,
                    contentColor = SalonTerracotta,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            Modifier.tabIndicatorOffset(tabPositions[selectedTabIndex]),
                            color = SalonTerracotta
                        )
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                ) {
                    Tab(
                        selected = selectedTabIndex == 0,
                        onClick = {
                            selectedTabIndex = 0
                            localValidationError = null
                        },
                        text = {
                            Text(
                                text = "Créer un compte",
                                fontWeight = if (selectedTabIndex == 0) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 12.sp
                            )
                        }
                    )
                    Tab(
                        selected = selectedTabIndex == 1,
                        onClick = {
                            selectedTabIndex = 1
                            localValidationError = null
                        },
                        text = {
                            Text(
                                text = "Se connecter",
                                fontWeight = if (selectedTabIndex == 1) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 12.sp
                            )
                        }
                    )
                }

                // Error banner
                AnimatedVisibility(visible = displayError != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(SalonDangerBg)
                            .padding(10.dp)
                    ) {
                        Text(
                            text = displayError ?: "",
                            color = SalonDangerFg,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Google Sign-In button (fast registration / login)
                OutlinedButton(
                    onClick = {
                        localValidationError = null
                        onGoogleSignIn(context)
                    },
                    enabled = !isLoading,
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.outlinedButtonColors(
                        containerColor = SalonCream,
                        contentColor = SalonNavy
                    ),
                    border = ButtonDefaults.outlinedButtonBorder.copy(brush = androidx.compose.ui.graphics.SolidColor(SalonLine)),
                    modifier = Modifier
                        .fillMaxWidth()
                        .height(44.dp)
                        .testTag("btn_google_signin")
                ) {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.Center
                    ) {
                        // Google "G" Badge
                        Box(
                            modifier = Modifier
                                .size(22.dp)
                                .clip(CircleShape)
                                .background(SalonPaper)
                                .border(1.dp, SalonLine, CircleShape),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "G",
                                fontWeight = FontWeight.Black,
                                fontSize = 13.sp,
                                color = SalonTerracotta
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = if (selectedTabIndex == 0) "S'inscrire avec Google" else "Continuer avec Google",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SalonNavy
                        )
                    }
                }

                Row(
                    modifier = Modifier.fillMaxWidth(),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    HorizontalDivider(modifier = Modifier.weight(1f), color = SalonLine)
                    Text(
                        text = "  OU PAR EMAIL  ",
                        fontSize = 10.sp,
                        fontWeight = FontWeight.Bold,
                        color = SalonInkSoft
                    )
                    HorizontalDivider(modifier = Modifier.weight(1f), color = SalonLine)
                }

                // Inscription fields
                if (selectedTabIndex == 0) {
                    OutlinedTextField(
                        value = nom,
                        onValueChange = {
                            nom = it
                            localValidationError = null
                        },
                        label = { Text("Nom et prénom *") },
                        placeholder = { Text("ex: Aminata Koné") },
                        leadingIcon = {
                            Icon(Icons.Default.Person, contentDescription = null, tint = SalonTerracotta)
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SalonTerracotta,
                            unfocusedBorderColor = SalonLine
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_auth_nom")
                    )

                    OutlinedTextField(
                        value = telephone,
                        onValueChange = {
                            telephone = it
                            localValidationError = null
                        },
                        label = { Text("Numéro de téléphone *") },
                        placeholder = { Text("ex: 07 08 45 67 89") },
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Phone),
                        leadingIcon = {
                            Icon(Icons.Default.Phone, contentDescription = null, tint = SalonTerracotta)
                        },
                        singleLine = true,
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SalonTerracotta,
                            unfocusedBorderColor = SalonLine
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_auth_tel")
                    )

                    // Salon favori selector chips
                    if (salons.isNotEmpty()) {
                        Column {
                            Text(
                                text = "Votre salon habituel :",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SalonNavy
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Row(
                                modifier = Modifier.fillMaxWidth(),
                                horizontalArrangement = Arrangement.spacedBy(6.dp)
                            ) {
                                salons.forEach { salon ->
                                    val isSelected = salon.id == selectedSalonId
                                    Box(
                                        modifier = Modifier
                                            .weight(1f)
                                            .clip(RoundedCornerShape(8.dp))
                                            .background(if (isSelected) SalonTerracotta else SalonCream)
                                            .border(1.dp, if (isSelected) SalonTerracotta else SalonLine, RoundedCornerShape(8.dp))
                                            .clickable { selectedSalonId = salon.id }
                                            .padding(vertical = 6.dp, horizontal = 4.dp),
                                        contentAlignment = Alignment.Center
                                    ) {
                                        Text(
                                            text = salon.quartier,
                                            fontSize = 10.sp,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                            color = if (isSelected) SalonPaper else SalonNavy
                                        )
                                    }
                                }
                            }
                        }
                    }
                }

                OutlinedTextField(
                    value = email,
                    onValueChange = {
                        email = it
                        localValidationError = null
                    },
                    label = { Text("Adresse Email *") },
                    placeholder = { Text("ex: client@abidjan.ci") },
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                    leadingIcon = {
                        Icon(Icons.Default.Email, contentDescription = null, tint = SalonTerracotta)
                    },
                    singleLine = true,
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SalonTerracotta,
                        unfocusedBorderColor = SalonLine
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_auth_email")
                )

                OutlinedTextField(
                    value = motDePasse,
                    onValueChange = {
                        motDePasse = it
                        localValidationError = null
                    },
                    label = { Text("Mot de passe *") },
                    visualTransformation = if (passwordVisible) VisualTransformation.None else PasswordVisualTransformation(),
                    leadingIcon = {
                        Icon(Icons.Default.Lock, contentDescription = null, tint = SalonTerracotta)
                    },
                    trailingIcon = {
                        IconButton(onClick = { passwordVisible = !passwordVisible }) {
                            Icon(
                                imageVector = if (passwordVisible) Icons.Default.Visibility else Icons.Default.VisibilityOff,
                                contentDescription = if (passwordVisible) "Masquer" else "Afficher",
                                tint = SalonInkSoft
                            )
                        }
                    },
                    singleLine = true,
                    keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                    colors = OutlinedTextFieldDefaults.colors(
                        focusedBorderColor = SalonTerracotta,
                        unfocusedBorderColor = SalonLine
                    ),
                    modifier = Modifier
                        .fillMaxWidth()
                        .testTag("input_auth_password")
                )

                Text(
                    text = if (selectedTabIndex == 0) {
                        "En vous inscrivant, vous pourrez sécuriser vos réservations dans nos 3 salons et consulter vos rendez-vous."
                    } else {
                        "Connectez-vous pour retrouver vos coordonnées et finaliser votre réservation."
                    },
                    fontSize = 11.sp,
                    color = SalonInkSoft,
                    lineHeight = 15.sp
                )
            }
        },
        confirmButton = {
            Button(
                onClick = {
                    if (selectedTabIndex == 0) {
                        // Validation Inscription
                        if (nom.isBlank()) {
                            localValidationError = "Veuillez renseigner votre nom complet."
                            return@Button
                        }
                        if (telephone.trim().length < 8) {
                            localValidationError = "Veuillez renseigner un numéro de téléphone valide (au moins 8 chiffres)."
                            return@Button
                        }
                        if (email.isBlank() || !email.contains("@")) {
                            localValidationError = "Veuillez renseigner une adresse email valide."
                            return@Button
                        }
                        if (motDePasse.length < 6) {
                            localValidationError = "Le mot de passe doit comporter au moins 6 caractères."
                            return@Button
                        }
                        onRegisterWithEmail(nom.trim(), telephone.trim(), email.trim(), motDePasse, selectedSalonId)
                    } else {
                        // Validation Connexion
                        if (email.isBlank() || !email.contains("@")) {
                            localValidationError = "Veuillez entrer une adresse email valide."
                            return@Button
                        }
                        if (motDePasse.isBlank()) {
                            localValidationError = "Veuillez entrer votre mot de passe."
                            return@Button
                        }
                        onLoginWithEmail(email.trim(), motDePasse)
                    }
                },
                enabled = !isLoading,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SalonTerracotta),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btn_auth_submit")
            ) {
                if (isLoading) {
                    CircularProgressIndicator(
                        color = SalonPaper,
                        strokeWidth = 2.dp,
                        modifier = Modifier.size(18.dp)
                    )
                } else {
                    Text(
                        text = if (selectedTabIndex == 0) "Créer mon compte client" else "Se connecter et continuer",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        dismissButton = {
            TextButton(
                onClick = onDismiss,
                enabled = !isLoading
            ) {
                Text("Annuler", color = SalonInkSoft, fontSize = 13.sp)
            }
        }
    )
}
