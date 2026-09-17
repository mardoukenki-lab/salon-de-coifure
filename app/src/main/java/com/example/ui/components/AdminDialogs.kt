package com.example.ui.components

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.layout.fillMaxHeight
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
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.AddCircle
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Delete
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material.icons.filled.Email
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.PhotoCamera
import androidx.compose.material.icons.filled.Refresh
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material.icons.filled.Warning
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.ScrollableTabRow
import androidx.compose.material3.Tab
import androidx.compose.material3.TabRowDefaults
import androidx.compose.material3.TabRowDefaults.tabIndicatorOffset
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableIntStateOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.window.Dialog
import androidx.compose.ui.window.DialogProperties
import com.example.R
import com.example.data.model.AdminUser
import com.example.data.model.HairService
import com.example.data.model.HairstyleItem
import com.example.data.model.Salon
import com.example.data.model.ServiceOption
import com.example.data.repository.AdminManager
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
import com.example.ui.theme.SalonRose
import com.example.ui.theme.SalonTerracotta
import com.example.ui.theme.SalonTerracottaSoft

/**
 * Boîte de dialogue secrète de connexion administrateur (déclenchée par double-tap sur le logo).
 */
@Composable
fun AdminLoginDialog(
    isSuperAdminConfigured: Boolean,
    superAdminUser: AdminUser?,
    errorMessage: String?,
    onDismiss: () -> Unit,
    onLoginSubmit: (email: String, nom: String) -> Unit
) {
    var email by remember { mutableStateOf("") }
    var nom by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SalonNavy.copy(alpha = 0.65f))
                .padding(20.dp),
            contentAlignment = Alignment.Center
        ) {
            Card(
                shape = RoundedCornerShape(22.dp),
                colors = CardDefaults.cardColors(containerColor = SalonPaper),
                elevation = CardDefaults.cardElevation(defaultElevation = 8.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("dialog_admin_login")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(22.dp)
                ) {
                    // Header with Secret Shield
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(42.dp)
                                    .clip(RoundedCornerShape(12.dp))
                                    .background(
                                        Brush.linearGradient(
                                            listOf(SalonTerracotta, SalonNavy)
                                        )
                                    ),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = "Admin",
                                    tint = SalonPaper,
                                    modifier = Modifier.size(24.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(12.dp))
                            Column {
                                Text(
                                    text = "Espace Administrateur",
                                    fontSize = 18.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SalonNavy,
                                    fontFamily = FontFamily.Serif
                                )
                                Text(
                                    text = "Accès réservé au réseau de salons",
                                    fontSize = 12.sp,
                                    color = SalonInkSoft
                                )
                            }
                        }

                        IconButton(
                            onClick = onDismiss,
                            modifier = Modifier.testTag("btn_close_admin_login")
                        ) {
                            Icon(
                                imageVector = Icons.Default.Close,
                                contentDescription = "Fermer",
                                tint = SalonInkSoft
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    // Notice explaining the Super Admin rules
                    if (!isSuperAdminConfigured) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(SalonTerracottaSoft.copy(alpha = 0.6f))
                                .border(1.dp, SalonTerracottaSoft, RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.Top) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = SalonTerracotta,
                                    modifier = Modifier
                                        .size(20.dp)
                                        .padding(top = 2.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Column {
                                    Text(
                                        text = "Initialisation du Super Administrateur",
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 13.sp,
                                        color = SalonNavy
                                    )
                                    Spacer(modifier = Modifier.height(3.dp))
                                    Text(
                                        text = "Le premier email qui se connecte sera automatiquement enregistré comme Super Administrateur. Il pourra ensuite autoriser jusqu'à 3 administrateurs supplémentaires pour gérer les prix et les coiffures.",
                                        fontSize = 12.sp,
                                        color = SalonNavy,
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }
                    } else {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(12.dp))
                                .background(SalonNavySoft.copy(alpha = 0.5f))
                                .border(1.dp, SalonLine, RoundedCornerShape(12.dp))
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.Lock,
                                    contentDescription = null,
                                    tint = SalonNavy,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(10.dp))
                                Text(
                                    text = "Connectez-vous avec l'email du Super Admin ou l'un des 3 administrateurs autorisés.",
                                    fontSize = 12.sp,
                                    color = SalonNavy,
                                    lineHeight = 16.sp
                                )
                            }
                        }
                    }

                    if (errorMessage != null) {
                        Spacer(modifier = Modifier.height(12.dp))
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(SalonDangerBg)
                                .border(1.dp, SalonDangerFg.copy(alpha = 0.4f), RoundedCornerShape(10.dp))
                                .padding(12.dp)
                        ) {
                            Row(verticalAlignment = Alignment.Top) {
                                Icon(
                                    imageVector = Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = SalonDangerFg,
                                    modifier = Modifier
                                        .size(18.dp)
                                        .padding(top = 1.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = errorMessage,
                                    fontSize = 12.sp,
                                    color = SalonDangerFg,
                                    lineHeight = 16.sp,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(16.dp))

                    if (!isSuperAdminConfigured) {
                        OutlinedTextField(
                            value = nom,
                            onValueChange = { nom = it },
                            label = { Text("Votre nom complet") },
                            placeholder = { Text("Ex: Kra Martial") },
                            leadingIcon = {
                                Icon(
                                    imageVector = Icons.Default.Person,
                                    contentDescription = null,
                                    tint = SalonTerracotta
                                )
                            },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_admin_name"),
                            colors = OutlinedTextFieldDefaults.colors(
                                focusedBorderColor = SalonTerracotta,
                                unfocusedBorderColor = SalonLine
                            ),
                            shape = RoundedCornerShape(12.dp)
                        )
                        Spacer(modifier = Modifier.height(10.dp))
                    }

                    OutlinedTextField(
                        value = email,
                        onValueChange = { email = it },
                        label = { Text("Email administrateur") },
                        placeholder = { Text("Ex: kramartial@gmail.com") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Email,
                                contentDescription = null,
                                tint = SalonTerracotta
                            )
                        },
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_admin_email"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SalonTerracotta,
                            unfocusedBorderColor = SalonLine
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    OutlinedTextField(
                        value = password,
                        onValueChange = { password = it },
                        label = { Text("Mot de passe / Code d'accès") },
                        placeholder = { Text("••••••••") },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Lock,
                                contentDescription = null,
                                tint = SalonTerracotta
                            )
                        },
                        visualTransformation = PasswordVisualTransformation(),
                        singleLine = true,
                        keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Password),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("input_admin_password"),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SalonTerracotta,
                            unfocusedBorderColor = SalonLine
                        ),
                        shape = RoundedCornerShape(12.dp)
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    Button(
                        onClick = {
                            onLoginSubmit(email, nom)
                        },
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(50.dp)
                            .testTag("btn_submit_admin_login"),
                        colors = ButtonDefaults.buttonColors(containerColor = SalonTerracotta),
                        shape = RoundedCornerShape(12.dp),
                        enabled = email.isNotBlank()
                    ) {
                        Icon(
                            imageVector = if (!isSuperAdminConfigured) Icons.Default.AutoAwesome else Icons.Default.Check,
                            contentDescription = null,
                            modifier = Modifier.size(18.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Text(
                            text = if (!isSuperAdminConfigured) "Activer comme Super Admin" else "Connexion Administrateur",
                            fontSize = 15.sp,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    OutlinedButton(
                        onClick = onDismiss,
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(46.dp)
                            .testTag("btn_cancel_admin_login"),
                        shape = RoundedCornerShape(12.dp)
                    ) {
                        Text(
                            text = "Retour à l'espace client",
                            color = SalonInkSoft,
                            fontSize = 13.sp
                        )
                    }
                }
            }
        }
    }
}

/**
 * Tableau de bord d'administration complet :
 * - Onglet Équipe Admins (Super Admin gère jusqu'à 3 administrateurs secondaires)
 * - Onglet Tarifs (modification dynamique des prix de tous les services et options)
 * - Onglet Catalogue Coiffures (ajout de visuels et gestion des coupes)
 * - Onglet Salons (informations et état des 3 adresses)
 */
@OptIn(ExperimentalLayoutApi::class)
@Composable
fun AdminDashboardDialog(
    loggedAdmin: AdminUser,
    superAdminUser: AdminUser?,
    secondaryAdmins: List<AdminUser>,
    services: List<HairService>,
    options: List<ServiceOption>,
    hairstyles: List<HairstyleItem>,
    salons: List<Salon>,
    successMessage: String?,
    errorMessage: String?,
    onDismiss: () -> Unit,
    onLogout: () -> Unit,
    onAddSecondaryAdmin: (nom: String, email: String) -> Unit,
    onRemoveSecondaryAdmin: (email: String) -> Unit,
    onUpdateServicePrice: (serviceId: String, newPrice: Int) -> Unit,
    onUpdateOptionPrice: (optionId: String, newPrice: Int) -> Unit,
    onResetPrices: () -> Unit,
    onAddHairstyle: (titre: String, categorie: String, prix: Int, desc: String, img: String, salon: String) -> Unit,
    onDeleteHairstyle: (id: String) -> Unit,
    onClearMessages: () -> Unit
) {
    var selectedTab by remember { mutableIntStateOf(0) }
    val tabs = listOf("Équipe Admins", "Gestion Tarifs", "Images & Coiffures", "Salons")

    Dialog(
        onDismissRequest = onDismiss,
        properties = DialogProperties(usePlatformDefaultWidth = false)
    ) {
        Box(
            modifier = Modifier
                .fillMaxSize()
                .background(SalonCream)
                .testTag("dialog_admin_dashboard")
        ) {
            Column(modifier = Modifier.fillMaxSize()) {
                // Top Header Bar
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .background(SalonNavy)
                        .padding(horizontal = 16.dp, vertical = 12.dp)
                ) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(38.dp)
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(SalonTerracotta),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Security,
                                    contentDescription = null,
                                    tint = SalonPaper,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Text(
                                        text = "Panneau Administration",
                                        color = SalonPaper,
                                        fontWeight = FontWeight.Bold,
                                        fontSize = 16.sp,
                                        fontFamily = FontFamily.Serif
                                    )
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(if (loggedAdmin.isSuperAdmin) SalonTerracotta else SalonOkFg)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = if (loggedAdmin.isSuperAdmin) "Super Admin" else "Admin",
                                            color = SalonPaper,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold
                                        )
                                    }
                                }
                                Text(
                                    text = "${loggedAdmin.nom} · ${loggedAdmin.email}",
                                    color = SalonPaper.copy(alpha = 0.8f),
                                    fontSize = 11.sp
                                )
                            }
                        }

                        Row(verticalAlignment = Alignment.CenterVertically) {
                            IconButton(
                                onClick = onLogout,
                                modifier = Modifier.testTag("btn_admin_logout")
                            ) {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Déconnexion",
                                    tint = SalonPaper
                                )
                            }
                            IconButton(
                                onClick = onDismiss,
                                modifier = Modifier.testTag("btn_admin_close")
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Fermer",
                                    tint = SalonPaper
                                )
                            }
                        }
                    }
                }

                // Sub-tabs
                ScrollableTabRow(
                    selectedTabIndex = selectedTab,
                    containerColor = SalonPaper,
                    contentColor = SalonNavy,
                    edgePadding = 12.dp,
                    indicator = { tabPositions ->
                        TabRowDefaults.SecondaryIndicator(
                            modifier = Modifier.tabIndicatorOffset(tabPositions[selectedTab]),
                            color = SalonTerracotta,
                            height = 3.dp
                        )
                    }
                ) {
                    tabs.forEachIndexed { index, title ->
                        Tab(
                            selected = selectedTab == index,
                            onClick = {
                                selectedTab = index
                                onClearMessages()
                            },
                            text = {
                                Text(
                                    text = title,
                                    fontWeight = if (selectedTab == index) FontWeight.Bold else FontWeight.Medium,
                                    fontSize = 13.sp,
                                    color = if (selectedTab == index) SalonTerracotta else SalonInkSoft
                                )
                            },
                            modifier = Modifier.testTag("tab_admin_$index")
                        )
                    }
                }

                // Alert feedback (success/error)
                if (successMessage != null || errorMessage != null) {
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 16.dp, vertical = 8.dp)
                            .clip(RoundedCornerShape(10.dp))
                            .background(if (successMessage != null) SalonOkBg else SalonDangerBg)
                            .border(
                                1.dp,
                                if (successMessage != null) SalonOkFg else SalonDangerFg,
                                RoundedCornerShape(10.dp)
                            )
                            .padding(12.dp)
                    ) {
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(
                                verticalAlignment = Alignment.CenterVertically,
                                modifier = Modifier.weight(1f)
                            ) {
                                Icon(
                                    imageVector = if (successMessage != null) Icons.Default.CheckCircle else Icons.Default.Warning,
                                    contentDescription = null,
                                    tint = if (successMessage != null) SalonOkFg else SalonDangerFg,
                                    modifier = Modifier.size(18.dp)
                                )
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = successMessage ?: errorMessage ?: "",
                                    fontSize = 12.sp,
                                    color = if (successMessage != null) SalonNavy else SalonDangerFg,
                                    fontWeight = FontWeight.Medium
                                )
                            }
                            IconButton(
                                onClick = onClearMessages,
                                modifier = Modifier.size(24.dp)
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Close,
                                    contentDescription = "Effacer",
                                    tint = SalonInkSoft,
                                    modifier = Modifier.size(16.dp)
                                )
                            }
                        }
                    }
                }

                // Tab Content Body
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .weight(1f)
                ) {
                    when (selectedTab) {
                        0 -> AdminTeamTab(
                            loggedAdmin = loggedAdmin,
                            superAdminUser = superAdminUser,
                            secondaryAdmins = secondaryAdmins,
                            onAddAdmin = onAddSecondaryAdmin,
                            onRemoveAdmin = onRemoveSecondaryAdmin
                        )
                        1 -> AdminPricesTab(
                            services = services,
                            options = options,
                            onUpdateServicePrice = onUpdateServicePrice,
                            onUpdateOptionPrice = onUpdateOptionPrice,
                            onResetPrices = onResetPrices
                        )
                        2 -> AdminHairstylesTab(
                            hairstyles = hairstyles,
                            onAddHairstyle = onAddHairstyle,
                            onDeleteHairstyle = onDeleteHairstyle
                        )
                        3 -> AdminSalonsOverviewTab(salons = salons)
                    }
                }
            }
        }
    }
}

/**
 * Onglet 1 : Gestion de l'équipe d'administrateurs (Super Admin + 3 Admins max).
 */
@Composable
private fun AdminTeamTab(
    loggedAdmin: AdminUser,
    superAdminUser: AdminUser?,
    secondaryAdmins: List<AdminUser>,
    onAddAdmin: (nom: String, email: String) -> Unit,
    onRemoveAdmin: (email: String) -> Unit
) {
    var newAdminNom by remember { mutableStateOf("") }
    var newAdminEmail by remember { mutableStateOf("") }

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // Super Admin Card
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SalonPaper),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(36.dp)
                                    .clip(CircleShape)
                                    .background(SalonTerracottaSoft),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.AutoAwesome,
                                    contentDescription = null,
                                    tint = SalonTerracotta,
                                    modifier = Modifier.size(20.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = "Super Administrateur",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = SalonNavy
                                )
                                Text(
                                    text = "Premier email enregistré · Droits suprêmes",
                                    fontSize = 11.sp,
                                    color = SalonInkSoft
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(SalonTerracotta.copy(alpha = 0.15f))
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Titulaire",
                                color = SalonTerracotta,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = SalonLine)
                    Spacer(modifier = Modifier.height(10.dp))

                    val superName = superAdminUser?.nom ?: loggedAdmin.nom
                    val superEmail = superAdminUser?.email ?: loggedAdmin.email
                    val superDate = superAdminUser?.dateAjout ?: "Initialisation"

                    Text(
                        text = "Nom : $superName",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SalonNavy
                    )
                    Text(
                        text = "Email : $superEmail",
                        fontSize = 13.sp,
                        color = SalonInk
                    )
                    Text(
                        text = "Enregistré le : $superDate",
                        fontSize = 11.sp,
                        color = SalonInkSoft
                    )
                }
            }
        }

        // Secondary Admins Section
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SalonPaper),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(16.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Administrateurs Secondaires",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = SalonNavy
                            )
                            Text(
                                text = "Capacité : ${secondaryAdmins.size} / ${AdminManager.MAX_SECONDARY_ADMINS} autorisés",
                                fontSize = 12.sp,
                                color = if (secondaryAdmins.size >= AdminManager.MAX_SECONDARY_ADMINS) SalonDangerFg else SalonInkSoft,
                                fontWeight = FontWeight.Medium
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(10.dp))
                                .background(SalonNavySoft)
                                .padding(horizontal = 10.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "${secondaryAdmins.size}/3",
                                fontWeight = FontWeight.Bold,
                                fontSize = 12.sp,
                                color = SalonNavy
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Le Super Admin peut ajouter 3 autres administrateurs pour gérer les prix et ajouter les visuels de coiffures.",
                        fontSize = 12.sp,
                        color = SalonInkSoft,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(14.dp))

                    if (secondaryAdmins.isEmpty()) {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(10.dp))
                                .background(SalonCream)
                                .padding(16.dp),
                            contentAlignment = Alignment.Center
                        ) {
                            Text(
                                text = "Aucun administrateur secondaire pour le moment.\nUtilisez le formulaire ci-dessous pour en ajouter.",
                                fontSize = 12.sp,
                                color = SalonInkSoft,
                                textAlign = TextAlign.Center,
                                lineHeight = 16.sp
                            )
                        }
                    } else {
                        Column(verticalArrangement = Arrangement.spacedBy(8.dp)) {
                            secondaryAdmins.forEachIndexed { index, admin ->
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .clip(RoundedCornerShape(12.dp))
                                        .background(SalonCream)
                                        .border(1.dp, SalonLine, RoundedCornerShape(12.dp))
                                        .padding(12.dp)
                                ) {
                                    Row(
                                        modifier = Modifier.fillMaxWidth(),
                                        horizontalArrangement = Arrangement.SpaceBetween,
                                        verticalAlignment = Alignment.CenterVertically
                                    ) {
                                        Row(
                                            verticalAlignment = Alignment.CenterVertically,
                                            modifier = Modifier.weight(1f)
                                        ) {
                                            Box(
                                                modifier = Modifier
                                                    .size(32.dp)
                                                    .clip(CircleShape)
                                                    .background(SalonNavy.copy(alpha = 0.1f)),
                                                contentAlignment = Alignment.Center
                                            ) {
                                                Text(
                                                    text = "#${index + 1}",
                                                    fontSize = 11.sp,
                                                    fontWeight = FontWeight.Bold,
                                                    color = SalonNavy
                                                )
                                            }
                                            Spacer(modifier = Modifier.width(10.dp))
                                            Column {
                                                Text(
                                                    text = admin.nom,
                                                    fontWeight = FontWeight.Bold,
                                                    fontSize = 13.sp,
                                                    color = SalonNavy
                                                )
                                                Text(
                                                    text = admin.email,
                                                    fontSize = 12.sp,
                                                    color = SalonInk
                                                )
                                                Text(
                                                    text = "Ajouté le : ${admin.dateAjout} · Droits : Prix & Coiffures",
                                                    fontSize = 10.sp,
                                                    color = SalonInkSoft
                                                )
                                            }
                                        }

                                        if (loggedAdmin.isSuperAdmin) {
                                            IconButton(
                                                onClick = { onRemoveAdmin(admin.email) },
                                                modifier = Modifier.testTag("btn_remove_admin_${admin.email}")
                                            ) {
                                                Icon(
                                                    imageVector = Icons.Default.Delete,
                                                    contentDescription = "Supprimer",
                                                    tint = SalonDangerFg
                                                )
                                            }
                                        }
                                    }
                                }
                            }
                        }
                    }

                    // Add admin form (accessible only by Super Admin)
                    if (loggedAdmin.isSuperAdmin) {
                        Spacer(modifier = Modifier.height(16.dp))
                        HorizontalDivider(color = SalonLine)
                        Spacer(modifier = Modifier.height(14.dp))

                        if (secondaryAdmins.size < AdminManager.MAX_SECONDARY_ADMINS) {
                            Text(
                                text = "Ajouter un nouvel administrateur",
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = SalonNavy
                            )
                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = newAdminNom,
                                onValueChange = { newAdminNom = it },
                                label = { Text("Nom de l'administrateur") },
                                placeholder = { Text("Ex: Aïcha Diabaté") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Person,
                                        contentDescription = null,
                                        tint = SalonTerracotta
                                    )
                                },
                                singleLine = true,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_new_admin_nom"),
                                shape = RoundedCornerShape(10.dp)
                            )

                            Spacer(modifier = Modifier.height(8.dp))

                            OutlinedTextField(
                                value = newAdminEmail,
                                onValueChange = { newAdminEmail = it },
                                label = { Text("Email de l'administrateur") },
                                placeholder = { Text("Ex: aicha.d@salons.ci") },
                                leadingIcon = {
                                    Icon(
                                        imageVector = Icons.Default.Email,
                                        contentDescription = null,
                                        tint = SalonTerracotta
                                    )
                                },
                                singleLine = true,
                                keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Email),
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .testTag("input_new_admin_email"),
                                shape = RoundedCornerShape(10.dp)
                            )

                            Spacer(modifier = Modifier.height(12.dp))

                            Button(
                                onClick = {
                                    if (newAdminEmail.isNotBlank()) {
                                        onAddAdmin(newAdminNom, newAdminEmail)
                                        newAdminNom = ""
                                        newAdminEmail = ""
                                    }
                                },
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .height(48.dp)
                                    .testTag("btn_add_secondary_admin"),
                                colors = ButtonDefaults.buttonColors(containerColor = SalonNavy),
                                shape = RoundedCornerShape(10.dp),
                                enabled = newAdminEmail.isNotBlank()
                            ) {
                                Icon(imageVector = Icons.Default.PersonAdd, contentDescription = null)
                                Spacer(modifier = Modifier.width(8.dp))
                                Text(
                                    text = "Ajouter cet administrateur (${secondaryAdmins.size + 1}/3)",
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        } else {
                            Box(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .clip(RoundedCornerShape(10.dp))
                                    .background(SalonDangerBg)
                                    .padding(12.dp)
                            ) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Icon(
                                        imageVector = Icons.Default.Info,
                                        contentDescription = null,
                                        tint = SalonDangerFg,
                                        modifier = Modifier.size(18.dp)
                                    )
                                    Spacer(modifier = Modifier.width(8.dp))
                                    Text(
                                        text = "Limite maximale de 3 administrateurs secondaires atteinte. Supprimez-en un pour pouvoir en ajouter un autre.",
                                        fontSize = 12.sp,
                                        color = SalonDangerFg,
                                        lineHeight = 16.sp
                                    )
                                }
                            }
                        }
                    } else {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "💡 Connecté en tant qu'administrateur délégué. Seul le Super Admin peut ajouter ou supprimer d'autres administrateurs.",
                            fontSize = 11.sp,
                            color = SalonInkSoft
                        )
                    }
                }
            }
        }
    }
}

/**
 * Onglet 2 : Gestion dynamique des prix des prestations et des options.
 */
@Composable
private fun AdminPricesTab(
    services: List<HairService>,
    options: List<ServiceOption>,
    onUpdateServicePrice: (serviceId: String, newPrice: Int) -> Unit,
    onUpdateOptionPrice: (optionId: String, newPrice: Int) -> Unit,
    onResetPrices: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Tarifs officiels du réseau",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = SalonNavy,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = "Les prix modifiés sont répercutés instantanément chez les clientes.",
                        fontSize = 12.sp,
                        color = SalonInkSoft
                    )
                }

                OutlinedButton(
                    onClick = onResetPrices,
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("btn_reset_prices")
                ) {
                    Icon(
                        imageVector = Icons.Default.Refresh,
                        contentDescription = null,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text("Défauts", fontSize = 11.sp)
                }
            }
        }

        // Services list
        item {
            Text(
                text = "Prestations Principales (${services.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = SalonTerracotta
            )
        }

        items(services) { service ->
            var editingPrice by remember(service.prixReference) {
                mutableStateOf(service.prixReference.toString())
            }
            var isSaved by remember(service.prixReference) { mutableStateOf(false) }

            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SalonPaper),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = service.nom,
                                fontWeight = FontWeight.Bold,
                                fontSize = 14.sp,
                                color = SalonNavy
                            )
                            Text(
                                text = "${service.tag} · Durée : ${service.dureeMin} min",
                                fontSize = 11.sp,
                                color = SalonInkSoft
                            )
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(SalonTerracottaSoft)
                                .padding(horizontal = 10.dp, vertical = 5.dp)
                        ) {
                            Text(
                                text = "${service.prixReference} FCFA",
                                fontWeight = FontWeight.Bold,
                                fontSize = 13.sp,
                                color = SalonTerracotta
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = SalonLine)
                    Spacer(modifier = Modifier.height(10.dp))

                    // Price Editor with Quick Steppers (-500 / +500)
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        OutlinedButton(
                            onClick = {
                                val current = editingPrice.toIntOrNull() ?: service.prixReference
                                val updated = (current - 500).coerceAtLeast(1000)
                                editingPrice = updated.toString()
                                onUpdateServicePrice(service.id, updated)
                                isSaved = true
                            },
                            modifier = Modifier.size(36.dp),
                            contentPadding = PaddingValues(0.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("-500", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.width(6.dp))

                        OutlinedTextField(
                            value = editingPrice,
                            onValueChange = {
                                editingPrice = it.filter { char -> char.isDigit() }
                                isSaved = false
                            },
                            singleLine = true,
                            suffix = { Text("FCFA", fontSize = 11.sp) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .weight(1f)
                                .height(50.dp)
                                .testTag("input_price_${service.id}"),
                            shape = RoundedCornerShape(8.dp)
                        )

                        Spacer(modifier = Modifier.width(6.dp))

                        OutlinedButton(
                            onClick = {
                                val current = editingPrice.toIntOrNull() ?: service.prixReference
                                val updated = current + 500
                                editingPrice = updated.toString()
                                onUpdateServicePrice(service.id, updated)
                                isSaved = true
                            },
                            modifier = Modifier.size(36.dp),
                            contentPadding = PaddingValues(0.dp),
                            shape = RoundedCornerShape(8.dp)
                        ) {
                            Text("+500", fontSize = 10.sp, fontWeight = FontWeight.Bold)
                        }

                        Spacer(modifier = Modifier.width(8.dp))

                        Button(
                            onClick = {
                                val newP = editingPrice.toIntOrNull() ?: service.prixReference
                                onUpdateServicePrice(service.id, newP)
                                isSaved = true
                            },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SalonTerracotta),
                            contentPadding = PaddingValues(horizontal = 12.dp, vertical = 6.dp),
                            modifier = Modifier.height(42.dp)
                        ) {
                            Icon(
                                imageVector = if (isSaved) Icons.Default.Check else Icons.Default.Edit,
                                contentDescription = null,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(if (isSaved) "Actif" else "Sauver", fontSize = 11.sp)
                        }
                    }
                }
            }
        }

        // Options list
        item {
            Spacer(modifier = Modifier.height(10.dp))
            Text(
                text = "Options & Add-ons (${options.size})",
                fontWeight = FontWeight.Bold,
                fontSize = 14.sp,
                color = SalonTerracotta
            )
        }

        items(options) { option ->
            var editingOptionPrice by remember(option.prix) {
                mutableStateOf(option.prix.toString())
            }

            Card(
                shape = RoundedCornerShape(12.dp),
                colors = CardDefaults.cardColors(containerColor = SalonPaper),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Column(modifier = Modifier.weight(1f)) {
                        Text(
                            text = option.nom,
                            fontWeight = FontWeight.SemiBold,
                            fontSize = 13.sp,
                            color = SalonNavy
                        )
                        Text(
                            text = "Catégorie : ${option.categorie}",
                            fontSize = 11.sp,
                            color = SalonInkSoft
                        )
                    }

                    Row(verticalAlignment = Alignment.CenterVertically) {
                        OutlinedTextField(
                            value = editingOptionPrice,
                            onValueChange = {
                                editingOptionPrice = it.filter { char -> char.isDigit() }
                            },
                            singleLine = true,
                            suffix = { Text("F", fontSize = 10.sp) },
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .width(100.dp)
                                .height(46.dp),
                            shape = RoundedCornerShape(8.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        IconButton(
                            onClick = {
                                val p = editingOptionPrice.toIntOrNull() ?: option.prix
                                onUpdateOptionPrice(option.id, p)
                            },
                            modifier = Modifier.size(36.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Check,
                                contentDescription = "Valider",
                                tint = SalonOkFg
                            )
                        }
                    }
                }
            }
        }
    }
}

/**
 * Onglet 3 : Gestion des images et catalogue de coiffures (ajout de photos/modèles).
 */
@Composable
private fun AdminHairstylesTab(
    hairstyles: List<HairstyleItem>,
    onAddHairstyle: (titre: String, categorie: String, prix: Int, desc: String, img: String, salon: String) -> Unit,
    onDeleteHairstyle: (id: String) -> Unit
) {
    var isAddingNew by remember { mutableStateOf(false) }
    var titre by remember { mutableStateOf("") }
    var categorie by remember { mutableStateOf("Tresses") }
    var prixEstime by remember { mutableStateOf("8000") }
    var description by remember { mutableStateOf("") }
    var selectedImagePreset by remember { mutableStateOf("img_coiffure_showcase") }
    var salonNom by remember { mutableStateOf("Tous nos salons") }

    val categoriesList = listOf("Tresses", "Twists", "Soins", "Locks", "Tissage", "Lissage")
    val imagePresets = listOf(
        "img_coiffure_showcase" to "Photo Studio Tresses",
        "img_salon_hero" to "Photo Salon & Coiffure"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Catalogue & Visuels Coiffures",
                        fontWeight = FontWeight.Bold,
                        fontSize = 16.sp,
                        color = SalonNavy,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = "${hairstyles.size} modèles visibles dans la recherche cliente",
                        fontSize = 12.sp,
                        color = SalonInkSoft
                    )
                }

                Button(
                    onClick = { isAddingNew = !isAddingNew },
                    shape = RoundedCornerShape(10.dp),
                    colors = ButtonDefaults.buttonColors(
                        containerColor = if (isAddingNew) SalonNavy else SalonTerracotta
                    ),
                    modifier = Modifier.testTag("btn_toggle_add_hairstyle")
                ) {
                    Icon(
                        imageVector = if (isAddingNew) Icons.Default.Close else Icons.Default.Add,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Text(if (isAddingNew) "Fermer" else "Ajouter", fontSize = 12.sp)
                }
            }
        }

        // Form to add a new hairstyle
        if (isAddingNew) {
            item {
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SalonPaper),
                    elevation = CardDefaults.cardElevation(defaultElevation = 3.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Publier un nouveau modèle de coiffure",
                            fontWeight = FontWeight.Bold,
                            fontSize = 15.sp,
                            color = SalonNavy
                        )
                        Spacer(modifier = Modifier.height(12.dp))

                        OutlinedTextField(
                            value = titre,
                            onValueChange = { titre = it },
                            label = { Text("Nom du modèle") },
                            placeholder = { Text("Ex: Knotless Braids Bohème Ombrée") },
                            singleLine = true,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_hairstyle_title"),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        // Category choice
                        Text(
                            text = "Catégorie :",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SalonNavy
                        )
                        Spacer(modifier = Modifier.height(4.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            categoriesList.take(3).forEach { cat ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (categorie == cat) SalonTerracotta else SalonCream)
                                        .clickable { categorie = cat }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = cat,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (categorie == cat) SalonPaper else SalonNavy
                                    )
                                }
                            }
                        }
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(6.dp)
                        ) {
                            categoriesList.drop(3).forEach { cat ->
                                Box(
                                    modifier = Modifier
                                        .clip(RoundedCornerShape(8.dp))
                                        .background(if (categorie == cat) SalonTerracotta else SalonCream)
                                        .clickable { categorie = cat }
                                        .padding(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text(
                                        text = cat,
                                        fontSize = 11.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = if (categorie == cat) SalonPaper else SalonNavy
                                    )
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = prixEstime,
                            onValueChange = { prixEstime = it.filter { c -> c.isDigit() } },
                            label = { Text("Tarif indicatif") },
                            suffix = { Text("FCFA") },
                            singleLine = true,
                            keyboardOptions = KeyboardOptions(keyboardType = KeyboardType.Number),
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_hairstyle_price"),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Spacer(modifier = Modifier.height(10.dp))

                        OutlinedTextField(
                            value = description,
                            onValueChange = { description = it },
                            label = { Text("Description & Conseils d'entretien") },
                            placeholder = { Text("Mèches premium incluses, finition soignée...") },
                            maxLines = 3,
                            modifier = Modifier
                                .fillMaxWidth()
                                .testTag("input_hairstyle_desc"),
                            shape = RoundedCornerShape(10.dp)
                        )

                        Spacer(modifier = Modifier.height(12.dp))

                        // Image selector
                        Text(
                            text = "Visuel haute qualité associé :",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SalonNavy
                        )
                        Spacer(modifier = Modifier.height(6.dp))
                        Row(
                            modifier = Modifier.fillMaxWidth(),
                            horizontalArrangement = Arrangement.spacedBy(8.dp)
                        ) {
                            imagePresets.forEach { (resName, label) ->
                                val isSelected = selectedImagePreset == resName
                                Box(
                                    modifier = Modifier
                                        .weight(1f)
                                        .clip(RoundedCornerShape(10.dp))
                                        .background(if (isSelected) SalonNavy else SalonCream)
                                        .border(
                                            1.dp,
                                            if (isSelected) SalonTerracotta else SalonLine,
                                            RoundedCornerShape(10.dp)
                                        )
                                        .clickable { selectedImagePreset = resName }
                                        .padding(8.dp),
                                    contentAlignment = Alignment.Center
                                ) {
                                    Row(verticalAlignment = Alignment.CenterVertically) {
                                        Icon(
                                            imageVector = Icons.Default.PhotoCamera,
                                            contentDescription = null,
                                            tint = if (isSelected) SalonPaper else SalonInkSoft,
                                            modifier = Modifier.size(16.dp)
                                        )
                                        Spacer(modifier = Modifier.width(6.dp))
                                        Text(
                                            text = label,
                                            fontSize = 11.sp,
                                            color = if (isSelected) SalonPaper else SalonNavy,
                                            fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Normal
                                        )
                                    }
                                }
                            }
                        }

                        Spacer(modifier = Modifier.height(14.dp))

                        Button(
                            onClick = {
                                if (titre.isNotBlank()) {
                                    val p = prixEstime.toIntOrNull() ?: 8000
                                    onAddHairstyle(
                                        titre,
                                        categorie,
                                        p,
                                        description.ifBlank { "Prestation soignée en salon." },
                                        selectedImagePreset,
                                        salonNom
                                    )
                                    titre = ""
                                    description = ""
                                    isAddingNew = false
                                }
                            },
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(48.dp)
                                .testTag("btn_save_hairstyle"),
                            colors = ButtonDefaults.buttonColors(containerColor = SalonTerracotta),
                            shape = RoundedCornerShape(10.dp),
                            enabled = titre.isNotBlank()
                        ) {
                            Icon(imageVector = Icons.Default.AddCircle, contentDescription = null)
                            Spacer(modifier = Modifier.width(8.dp))
                            Text(text = "Publier ce modèle au catalogue", fontWeight = FontWeight.Bold)
                        }
                    }
                }
            }
        }

        // List of existing hairstyles
        items(hairstyles) { item ->
            val imgRes = if (item.imageResName == "img_coiffure_showcase") {
                R.drawable.img_coiffure_showcase
            } else {
                R.drawable.img_salon_hero
            }

            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SalonPaper),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Box(
                        modifier = Modifier
                            .size(70.dp)
                            .clip(RoundedCornerShape(10.dp))
                    ) {
                        Image(
                            painter = painterResource(id = imgRes),
                            contentDescription = item.titre,
                            contentScale = ContentScale.Crop,
                            modifier = Modifier.fillMaxSize()
                        )
                    }

                    Spacer(modifier = Modifier.width(12.dp))

                    Column(modifier = Modifier.weight(1f)) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(SalonTerracottaSoft)
                                    .padding(horizontal = 6.dp, vertical = 2.dp)
                            ) {
                                Text(
                                    text = item.categorie,
                                    fontSize = 10.sp,
                                    fontWeight = FontWeight.Bold,
                                    color = SalonTerracotta
                                )
                            }
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "${item.prixEstime} FCFA",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.Bold,
                                color = SalonNavy
                            )
                        }

                        Spacer(modifier = Modifier.height(4.dp))
                        Text(
                            text = item.titre,
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = SalonNavy
                        )
                        Text(
                            text = item.description,
                            fontSize = 11.sp,
                            color = SalonInkSoft,
                            maxLines = 2
                        )
                    }

                    IconButton(
                        onClick = { onDeleteHairstyle(item.id) },
                        modifier = Modifier.testTag("btn_delete_hairstyle_${item.id}")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Delete,
                            contentDescription = "Supprimer",
                            tint = SalonDangerFg
                        )
                    }
                }
            }
        }
    }
}

/**
 * Onglet 4 : Vue des salons du réseau et statistiques.
 */
@Composable
private fun AdminSalonsOverviewTab(salons: List<Salon>) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .padding(16.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item {
            Text(
                text = "Salons du réseau à Abidjan",
                fontWeight = FontWeight.Bold,
                fontSize = 16.sp,
                color = SalonNavy,
                fontFamily = FontFamily.Serif
            )
            Text(
                text = "Supervision des trois adresses partenaires",
                fontSize = 12.sp,
                color = SalonInkSoft
            )
        }

        items(salons) { salon ->
            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(containerColor = SalonPaper),
                elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Box(
                                modifier = Modifier
                                    .size(34.dp)
                                    .clip(CircleShape)
                                    .background(SalonNavySoft),
                                contentAlignment = Alignment.Center
                            ) {
                                Icon(
                                    imageVector = Icons.Default.Storefront,
                                    contentDescription = null,
                                    tint = SalonNavy,
                                    modifier = Modifier.size(18.dp)
                                )
                            }
                            Spacer(modifier = Modifier.width(10.dp))
                            Column {
                                Text(
                                    text = salon.nom,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = SalonNavy
                                )
                                Text(
                                    text = salon.quartier,
                                    fontSize = 12.sp,
                                    color = SalonTerracotta,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(8.dp))
                                .background(SalonOkBg)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Text(
                                text = "Actif · ${salon.creneauxDispoAujourdhui} créneaux",
                                color = SalonOkFg,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(8.dp))
                    Text(
                        text = "Adresse : ${salon.adresse}",
                        fontSize = 12.sp,
                        color = SalonInk
                    )
                    Text(
                        text = "Horaires : ${salon.horaires}",
                        fontSize = 11.sp,
                        color = SalonInkSoft
                    )
                    Text(
                        text = "Contact caisse : ${salon.telephone}",
                        fontSize = 11.sp,
                        color = SalonInkSoft
                    )
                }
            }
        }
    }
}
