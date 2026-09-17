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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Shield
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
fun Step3DevisScreen(
    salon: Salon,
    service: HairService,
    selectedOptions: List<ServiceOption>,
    totalPrix: Int,
    totalDureeMin: Int,
    onModifyOptionsClick: () -> Unit,
    onValidateDevisClick: () -> Unit
) {
    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SalonCream)
            .testTag("screen_step_3"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Étape 3 · Le devis",
                    fontSize = 12.sp,
                    color = SalonTerracotta,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Devis transparent",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = SalonNavy,
                    fontFamily = FontFamily.Serif
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Transparence tarifaire absolue. Aucune surprise en salon, le montant total est validé avant le créneau.",
                    fontSize = 13.sp,
                    color = SalonInkSoft,
                    lineHeight = 18.sp
                )
            }
        }

        item {
            // Main Devis Card
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SalonPaper),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, SalonLine, RoundedCornerShape(16.dp))
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Text(
                            text = "Détail de la prestation",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = SalonNavy
                        )
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(SalonOkBg)
                                .padding(horizontal = 8.dp, vertical = 3.dp)
                        ) {
                            Text(
                                text = "Prix garanti",
                                fontSize = 11.sp,
                                color = SalonOkFg,
                                fontWeight = FontWeight.SemiBold
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    // Base Service
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .clip(RoundedCornerShape(8.dp))
                            .background(SalonCream)
                            .padding(12.dp),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column(modifier = Modifier.weight(1f)) {
                            Text(
                                text = service.nom,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = SalonNavy
                            )
                            Text(
                                text = "Prestation de base (~${service.dureeMin} min)",
                                fontSize = 11.sp,
                                color = SalonInkSoft
                            )
                        }
                        Text(
                            text = "${String.format("%,d", service.prixReference).replace(',', ' ')} F",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = SalonNavy
                        )
                    }

                    if (selectedOptions.isNotEmpty()) {
                        Spacer(modifier = Modifier.height(10.dp))
                        Text(
                            text = "Options sélectionnées (${selectedOptions.size})",
                            fontSize = 12.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SalonInkSoft
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        selectedOptions.forEach { opt ->
                            Row(
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .padding(vertical = 4.dp, horizontal = 4.dp),
                                horizontalArrangement = Arrangement.SpaceBetween,
                                verticalAlignment = Alignment.CenterVertically
                            ) {
                                Text(
                                    text = "+ ${opt.nom}",
                                    fontSize = 13.sp,
                                    color = SalonInk
                                )
                                Text(
                                    text = "+${String.format("%,d", opt.prix).replace(',', ' ')} F",
                                    fontSize = 13.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SalonTerracotta
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(12.dp))
                    HorizontalDivider(color = SalonLine, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(12.dp))

                    // Salon info in devis
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.LocationOn,
                            contentDescription = null,
                            tint = SalonTerracotta,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "${salon.nom} · ${salon.adresse}",
                            fontSize = 12.sp,
                            color = SalonInkSoft
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    // Duration in devis
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = SalonInkSoft,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Durée estimée au salon : ~$totalDureeMin min",
                            fontSize = 12.sp,
                            color = SalonInkSoft
                        )
                    }

                    Spacer(modifier = Modifier.height(14.dp))
                    HorizontalDivider(color = SalonLine, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(14.dp))

                    // Total Row
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = "Total à payer",
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = SalonNavy
                            )
                            Text(
                                text = "Toutes taxes & soins inclus",
                                fontSize = 10.sp,
                                color = SalonInkSoft
                            )
                        }
                        Text(
                            text = "${String.format("%,d", totalPrix).replace(',', ' ')} FCFA",
                            fontSize = 22.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SalonTerracotta
                        )
                    }
                }
            }
        }

        item {
            // Trust Guarantee callout
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(SalonOkBg)
                    .border(1.dp, SalonOkFg.copy(alpha = 0.3f), RoundedCornerShape(10.dp))
                    .padding(14.dp)
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.Shield,
                        contentDescription = null,
                        tint = SalonOkFg,
                        modifier = Modifier.size(22.dp)
                    )
                    Spacer(modifier = Modifier.width(10.dp))
                    Text(
                        text = "Engagement qualité Réseau Abidjan : le prix affiché est contractuel. Aucun supplément ne sera exigé sur place.",
                        fontSize = 12.sp,
                        color = SalonOkFg,
                        lineHeight = 17.sp,
                        fontWeight = FontWeight.Medium
                    )
                }
            }
        }

        item {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                OutlinedButton(
                    onClick = onModifyOptionsClick,
                    shape = RoundedCornerShape(8.dp),
                    modifier = Modifier
                        .weight(1f)
                        .testTag("btn_modify_options")
                ) {
                    Text(
                        text = "Modifier options",
                        color = SalonInkSoft,
                        fontSize = 12.sp
                    )
                }

                Button(
                    onClick = onValidateDevisClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SalonTerracotta),
                    modifier = Modifier
                        .weight(1.4f)
                        .testTag("btn_validate_devis")
                ) {
                    Text(
                        text = "Choisir le créneau",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(4.dp))
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
