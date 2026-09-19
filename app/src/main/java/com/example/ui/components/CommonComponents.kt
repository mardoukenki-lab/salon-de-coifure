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
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Lock
import androidx.compose.material.icons.filled.Person
import androidx.compose.material.icons.filled.PersonAdd
import androidx.compose.material.icons.filled.Phone
import androidx.compose.material.icons.filled.Security
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Badge
import androidx.compose.material3.BadgedBox
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.NavigationBar
import androidx.compose.material3.NavigationBarItem
import androidx.compose.material3.NavigationBarItemDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableLongStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.AdminUser
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
import com.example.ui.viewmodel.AppTab

@Composable
fun SalonTopBar(
    currentStep: Int,
    currentTab: AppTab,
    isUserRegistered: Boolean = false,
    clientNom: String = "",
    loggedAdmin: AdminUser? = null,
    onBackClick: () -> Unit,
    onAccountClick: () -> Unit = {},
    onAdminSecretTrigger: () -> Unit = {}
) {
    var lastLogoClickTime by remember { mutableLongStateOf(0L) }

    Column(
        modifier = Modifier
            .fillMaxWidth()
            .background(SalonPaper)
            .border(width = 1.dp, color = SalonLine)
            .padding(horizontal = 14.dp, vertical = 10.dp)
    ) {
        Row(
            modifier = Modifier.fillMaxWidth(),
            verticalAlignment = Alignment.CenterVertically,
            horizontalArrangement = Arrangement.SpaceBetween
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                modifier = Modifier.weight(1f)
            ) {
                if ((currentTab == AppTab.RESERVER && currentStep > 0) || currentTab == AppTab.PROFIL) {
                    IconButton(
                        onClick = onBackClick,
                        modifier = Modifier
                            .size(36.dp)
                            .testTag("btn_top_back")
                    ) {
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                            contentDescription = "Retour",
                            tint = SalonNavy
                        )
                    }
                    Spacer(modifier = Modifier.width(6.dp))
                }

                // Brand Logo with secret double-tap trigger!
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(RoundedCornerShape(10.dp))
                        .background(
                            Brush.linearGradient(
                                listOf(SalonTerracotta, SalonNavy)
                            )
                        )
                        .clickable {
                            val now = System.currentTimeMillis()
                            if (now - lastLogoClickTime < 650) {
                                // Double-tap detected on logo!
                                onAdminSecretTrigger()
                                lastLogoClickTime = 0L
                            } else {
                                lastLogoClickTime = now
                            }
                        }
                        .testTag("logo_salon_network"),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ContentCut,
                        contentDescription = "Logo Réseau (Appuyez 2 fois pour l'accès admin)",
                        tint = SalonPaper,
                        modifier = Modifier.size(20.dp)
                    )
                }

                Spacer(modifier = Modifier.width(10.dp))

                Column {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text(
                            text = "Réseau de coiffure",
                            fontWeight = FontWeight.Bold,
                            fontSize = 16.sp,
                            color = SalonNavy,
                            fontFamily = FontFamily.Serif
                        )
                        if (loggedAdmin != null) {
                            Spacer(modifier = Modifier.width(6.dp))
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(if (loggedAdmin.isSuperAdmin) SalonTerracotta else SalonNavy)
                                    .clickable { onAdminSecretTrigger() }
                                    .padding(horizontal = 5.dp, vertical = 2.dp)
                                    .testTag("badge_admin_active")
                            ) {
                                Text(
                                    text = if (loggedAdmin.isSuperAdmin) "SuperAdmin" else "Admin",
                                    color = SalonPaper,
                                    fontSize = 9.sp,
                                    fontWeight = FontWeight.Bold
                                )
                            }
                        }
                    }
                    Text(
                        text = "Yopougon · Treichville 13ème · Angré",
                        fontSize = 11.sp,
                        color = SalonInkSoft
                    )
                }
            }

            // Right Action Controls
            Row(verticalAlignment = Alignment.CenterVertically) {
                if (loggedAdmin != null) {
                    IconButton(
                        onClick = onAdminSecretTrigger,
                        modifier = Modifier
                            .size(34.dp)
                            .testTag("btn_top_admin_quick")
                    ) {
                        Icon(
                            imageVector = Icons.Default.Security,
                            contentDescription = "Panneau Admin",
                            tint = SalonTerracotta,
                            modifier = Modifier.size(20.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(4.dp))
                }

                // User / Account Action Pill
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(12.dp))
                        .background(if (isUserRegistered) SalonOkBg else SalonCream)
                        .border(
                            1.dp,
                            if (isUserRegistered) SalonOkFg else SalonTerracottaSoft,
                            RoundedCornerShape(12.dp)
                        )
                        .clickable { onAccountClick() }
                        .padding(horizontal = 8.dp, vertical = 5.dp)
                        .testTag("btn_top_account")
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = if (isUserRegistered) Icons.Default.CheckCircle else Icons.Default.PersonAdd,
                            contentDescription = "Mon compte",
                            tint = if (isUserRegistered) SalonOkFg else SalonTerracotta,
                            modifier = Modifier.size(13.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = if (isUserRegistered) {
                                if (clientNom.isNotBlank()) clientNom.take(8) else "Inscrit"
                            } else {
                                "Inscription"
                            },
                            fontSize = 11.sp,
                            color = if (isUserRegistered) SalonOkFg else SalonTerracotta,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }
    }
}

@Composable
fun BookingStepper(currentStep: Int) {
    val steps = listOf(
        "Salon",
        "Style",
        "Options",
        "Devis",
        "Créneau",
        "Paiement",
        "Confirmé"
    )

    Row(
        modifier = Modifier.fillMaxWidth(),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        steps.forEachIndexed { index, title ->
            val isPassed = index < currentStep
            val isCurrent = index == currentStep

            Column(
                horizontalAlignment = Alignment.CenterHorizontally,
                modifier = Modifier.weight(1f)
            ) {
                Box(
                    modifier = Modifier
                        .size(22.dp)
                        .clip(CircleShape)
                        .background(
                            when {
                                isCurrent -> SalonTerracotta
                                isPassed -> SalonOkBg
                                else -> SalonCream
                            }
                        )
                        .border(
                            1.dp,
                            when {
                                isCurrent -> SalonTerracotta
                                isPassed -> SalonOkFg
                                else -> SalonLine
                            },
                            CircleShape
                        ),
                    contentAlignment = Alignment.Center
                ) {
                    if (isPassed) {
                        Icon(
                            imageVector = Icons.Default.Check,
                            contentDescription = null,
                            tint = SalonOkFg,
                            modifier = Modifier.size(13.dp)
                        )
                    } else {
                        Text(
                            text = (index).toString(),
                            fontSize = 10.sp,
                            fontWeight = FontWeight.Bold,
                            color = if (isCurrent) SalonPaper else SalonInkSoft
                        )
                    }
                }
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = title,
                    fontSize = 9.sp,
                    maxLines = 1,
                    fontWeight = if (isCurrent) FontWeight.Bold else FontWeight.Normal,
                    color = when {
                        isCurrent -> SalonTerracotta
                        isPassed -> SalonNavy
                        else -> SalonInkSoft.copy(alpha = 0.7f)
                    }
                )
            }
        }
    }
}

@Composable
fun SalonBottomNav(
    currentTab: AppTab,
    reservationsCount: Int,
    onTabSelected: (AppTab) -> Unit
) {
    NavigationBar(
        containerColor = SalonPaper,
        tonalElevation = 6.dp,
        modifier = Modifier.border(width = 1.dp, color = SalonLine)
    ) {
        NavigationBarItem(
            selected = currentTab == AppTab.RESERVER,
            onClick = { onTabSelected(AppTab.RESERVER) },
            icon = {
                Icon(
                    imageVector = Icons.Default.ContentCut,
                    contentDescription = "Réserver",
                    modifier = Modifier.testTag("nav_reserver")
                )
            },
            label = { Text("Réserver") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = SalonTerracotta,
                selectedTextColor = SalonTerracotta,
                indicatorColor = SalonTerracottaSoft.copy(alpha = 0.4f),
                unselectedIconColor = SalonInkSoft,
                unselectedTextColor = SalonInkSoft
            )
        )

        NavigationBarItem(
            selected = currentTab == AppTab.MES_RDV,
            onClick = { onTabSelected(AppTab.MES_RDV) },
            icon = {
                if (reservationsCount > 0) {
                    BadgedBox(
                        badge = {
                            Badge(containerColor = SalonTerracotta) {
                                Text(
                                    reservationsCount.toString(),
                                    color = SalonPaper,
                                    fontSize = 10.sp
                                )
                            }
                        }
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = "Mes RDV",
                            modifier = Modifier.testTag("nav_mes_rdv")
                        )
                    }
                } else {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = "Mes RDV",
                        modifier = Modifier.testTag("nav_mes_rdv")
                    )
                }
            },
            label = { Text("Mes RDV") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = SalonTerracotta,
                selectedTextColor = SalonTerracotta,
                indicatorColor = SalonTerracottaSoft.copy(alpha = 0.4f),
                unselectedIconColor = SalonInkSoft,
                unselectedTextColor = SalonInkSoft
            )
        )

        NavigationBarItem(
            selected = currentTab == AppTab.SALONS_INFO,
            onClick = { onTabSelected(AppTab.SALONS_INFO) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Storefront,
                    contentDescription = "Nos Salons",
                    modifier = Modifier.testTag("nav_salons_info")
                )
            },
            label = { Text("Nos Salons") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = SalonTerracotta,
                selectedTextColor = SalonTerracotta,
                indicatorColor = SalonTerracottaSoft.copy(alpha = 0.4f),
                unselectedIconColor = SalonInkSoft,
                unselectedTextColor = SalonInkSoft
            )
        )

        NavigationBarItem(
            selected = currentTab == AppTab.PROFIL,
            onClick = { onTabSelected(AppTab.PROFIL) },
            icon = {
                Icon(
                    imageVector = Icons.Default.Person,
                    contentDescription = "Mon Profil",
                    modifier = Modifier.testTag("nav_profil")
                )
            },
            label = { Text("Mon Profil") },
            colors = NavigationBarItemDefaults.colors(
                selectedIconColor = SalonTerracotta,
                selectedTextColor = SalonTerracotta,
                indicatorColor = SalonTerracottaSoft.copy(alpha = 0.4f),
                unselectedIconColor = SalonInkSoft,
                unselectedTextColor = SalonInkSoft
            )
        )
    }
}
