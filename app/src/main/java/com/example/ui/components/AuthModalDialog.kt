package com.example.ui.components

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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Visibility
import androidx.compose.material.icons.filled.VisibilityOff
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
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
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.input.VisualTransformation
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.ui.theme.SalonCream
import com.example.ui.theme.SalonDangerBg
import com.example.ui.theme.SalonDangerFg
import com.example.ui.theme.SalonInk
import com.example.ui.theme.SalonInkSoft
import com.example.ui.theme.SalonLine
import com.example.ui.theme.SalonNavy
import com.example.ui.theme.SalonPaper
import com.example.ui.theme.SalonTerracotta
import com.example.ui.theme.SalonTerracottaSoft

/**
 * Dialogue d'inscription / connexion obligatoire avant de confirmer une réservation
 */
@Composable
fun AuthModalDialog(
    onDismiss: () -> Unit,
    onAuthSuccess: (nom: String, telephone: String, email: String) -> Unit
) {
    var selectedTabIndex by remember { mutableIntStateOf(0) } // 0 = Inscription, 1 = Connexion

    // Form fields
    var nom by remember { mutableStateOf("") }
    var telephone by remember { mutableStateOf("") }
    var email by remember { mutableStateOf("") }
    var motDePasse by remember { mutableStateOf("") }
    var passwordVisible by remember { mutableStateOf(false) }
    var errorMessage by remember { mutableStateOf<String?>(null) }
    var isLoading by remember { mutableStateOf(false) }

    AlertDialog(
        onDismissRequest = onDismiss,
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
                Column {
                    Text(
                        text = if (selectedTabIndex == 0) "Inscription visiteur" else "Connexion",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.Bold,
                        color = SalonNavy,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = "Obligatoire pour finaliser votre réservation",
                        fontSize = 12.sp,
                        color = SalonTerracotta,
                        fontWeight = FontWeight.SemiBold
                    )
                }
                IconButton(onClick = onDismiss, modifier = Modifier.size(32.dp)) {
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
                        .clip(RoundedCornerShape(8.dp))
                ) {
                    Tab(
                        selected = selectedTabIndex == 0,
                        onClick = {
                            selectedTabIndex = 0
                            errorMessage = null
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
                            errorMessage = null
                        },
                        text = {
                            Text(
                                text = "J'ai déjà un compte",
                                fontWeight = if (selectedTabIndex == 1) FontWeight.Bold else FontWeight.Medium,
                                fontSize = 12.sp
                            )
                        }
                    )
                }

                // Error banner
                AnimatedVisibility(visible = errorMessage != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(SalonDangerBg)
                            .padding(8.dp)
                    ) {
                        Text(
                            text = errorMessage ?: "",
                            color = SalonDangerFg,
                            fontSize = 11.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }

                // Inscription fields
                if (selectedTabIndex == 0) {
                    OutlinedTextField(
                        value = nom,
                        onValueChange = { nom = it },
                        label = { Text("Nom complet *") },
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
                        onValueChange = { telephone = it },
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
                }

                OutlinedTextField(
                    value = email,
                    onValueChange = { email = it },
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
                    onValueChange = { motDePasse = it },
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
                        "Vos informations permettent de recevoir vos rappels de RDV par SMS et de retrouver vos réservations."
                    } else {
                        "Connectez-vous pour continuer votre prise de rendez-vous en toute sécurité."
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
                        // Inscription validation
                        if (nom.isBlank()) {
                            errorMessage = "Veuillez renseigner votre nom complet."
                            return@Button
                        }
                        if (telephone.isBlank()) {
                            errorMessage = "Veuillez renseigner un numéro de téléphone valide."
                            return@Button
                        }
                        if (email.isBlank() || !email.contains("@")) {
                            errorMessage = "Veuillez renseigner une adresse email valide."
                            return@Button
                        }
                        if (motDePasse.length < 4) {
                            errorMessage = "Le mot de passe doit comporter au moins 4 caractères."
                            return@Button
                        }
                        onAuthSuccess(nom.trim(), telephone.trim(), email.trim())
                    } else {
                        // Connexion validation
                        if (email.isBlank()) {
                            errorMessage = "Veuillez entrer votre email."
                            return@Button
                        }
                        if (motDePasse.isBlank()) {
                            errorMessage = "Veuillez entrer votre mot de passe."
                            return@Button
                        }
                        val userNom = if (email.contains("@")) {
                            email.substringBefore("@").replaceFirstChar { it.uppercase() }
                        } else "Client Salons"
                        onAuthSuccess(userNom, telephone.ifBlank { "07 08 45 67 89" }, email.trim())
                    }
                },
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
                        text = if (selectedTabIndex == 0) "S'inscrire et continuer" else "Se connecter et continuer",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.Bold
                    )
                }
            }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) {
                Text("Annuler", color = SalonInkSoft, fontSize = 13.sp)
            }
        }
    )
}
