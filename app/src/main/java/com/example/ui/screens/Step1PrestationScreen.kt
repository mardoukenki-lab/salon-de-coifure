package com.example.ui.screens

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
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.GridItemSpan
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.lazy.grid.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Spa
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
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.HairService
import com.example.data.model.Salon
import com.example.ui.theme.SalonCream
import com.example.ui.theme.SalonInk
import com.example.ui.theme.SalonInkSoft
import com.example.ui.theme.SalonLine
import com.example.ui.theme.SalonNavy
import com.example.ui.theme.SalonNavySoft
import com.example.ui.theme.SalonPaper
import com.example.ui.theme.SalonRose
import com.example.ui.theme.SalonTerracotta
import com.example.ui.theme.SalonTerracottaSoft

@Composable
fun Step1PrestationScreen(
    salon: Salon,
    services: List<HairService>,
    selectedService: HairService?,
    onChangeSalonClick: () -> Unit,
    onServiceSelected: (HairService) -> Unit
) {
    LazyVerticalGrid(
        columns = GridCells.Fixed(2),
        modifier = Modifier
            .fillMaxSize()
            .background(SalonCream)
            .testTag("screen_step_1"),
        contentPadding = PaddingValues(16.dp),
        horizontalArrangement = Arrangement.spacedBy(12.dp),
        verticalArrangement = Arrangement.spacedBy(12.dp)
    ) {
        item(span = { GridItemSpan(2) }) {
            // Salon indicator
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(10.dp))
                    .background(SalonPaper)
                    .border(1.dp, SalonLine, RoundedCornerShape(10.dp))
                    .padding(horizontal = 14.dp, vertical = 10.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = SalonTerracotta,
                        modifier = Modifier.size(18.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = salon.nom,
                            fontWeight = FontWeight.Bold,
                            fontSize = 13.sp,
                            color = SalonNavy
                        )
                        Text(
                            text = "${salon.quartier} · ${salon.distanceKm} km",
                            fontSize = 11.sp,
                            color = SalonInkSoft
                        )
                    }
                }
                TextButton(
                    onClick = onChangeSalonClick,
                    modifier = Modifier.testTag("btn_change_salon")
                ) {
                    Text(
                        text = "Changer",
                        color = SalonTerracotta,
                        fontSize = 12.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }
        }

        item(span = { GridItemSpan(2) }) {
            Column {
                Text(
                    text = "Étape 1 · Le style",
                    fontSize = 12.sp,
                    color = SalonTerracotta,
                    fontWeight = FontWeight.Bold,
                    letterSpacing = 0.5.sp
                )
                Spacer(modifier = Modifier.height(2.dp))
                Text(
                    text = "Choisir la prestation",
                    fontSize = 20.sp,
                    fontWeight = FontWeight.Bold,
                    color = SalonNavy,
                    fontFamily = FontFamily.Serif
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "Tarif de référence transparent affiché avant sélection des options.",
                    fontSize = 13.sp,
                    color = SalonInkSoft
                )
            }
        }

        items(services) { service ->
            val isSelected = selectedService?.id == service.id

            Card(
                shape = RoundedCornerShape(14.dp),
                colors = CardDefaults.cardColors(
                    containerColor = if (isSelected) SalonTerracottaSoft.copy(alpha = 0.25f) else SalonPaper
                ),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(
                        width = if (isSelected) 2.dp else 1.dp,
                        color = if (isSelected) SalonTerracotta else SalonLine,
                        shape = RoundedCornerShape(14.dp)
                    )
                    .clickable { onServiceSelected(service) }
                    .testTag("service_card_${service.id}")
            ) {
                Column(
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(12.dp)
                ) {
                    // Prestation icon / banner block
                    Box(
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(54.dp)
                            .clip(RoundedCornerShape(8.dp))
                            .background(SalonRose.copy(alpha = 0.3f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = when (service.iconType) {
                                "treatment" -> Icons.Default.Spa
                                else -> Icons.Default.ContentCut
                            },
                            contentDescription = null,
                            tint = SalonTerracotta,
                            modifier = Modifier.size(26.dp)
                        )
                    }

                    Spacer(modifier = Modifier.height(8.dp))

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(4.dp))
                            .background(SalonCream)
                            .padding(horizontal = 6.dp, vertical = 2.dp)
                    ) {
                        Text(
                            text = service.tag,
                            fontSize = 9.sp,
                            color = SalonTerracotta,
                            fontWeight = FontWeight.Bold
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = service.nom,
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = SalonNavy,
                        maxLines = 1
                    )

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(
                        text = service.description,
                        fontSize = 11.sp,
                        color = SalonInkSoft,
                        lineHeight = 14.sp,
                        maxLines = 2
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = SalonInkSoft,
                            modifier = Modifier.size(12.dp)
                        )
                        Spacer(modifier = Modifier.width(3.dp))
                        Text(
                            text = "~${service.dureeMin} min",
                            fontSize = 11.sp,
                            color = SalonInkSoft
                        )
                    }

                    Spacer(modifier = Modifier.height(6.dp))

                    Text(
                        text = "À partir de",
                        fontSize = 10.sp,
                        color = SalonInkSoft
                    )
                    Text(
                        text = "${String.format("%,d", service.prixReference).replace(',', ' ')} F",
                        fontSize = 16.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SalonTerracotta
                    )

                    Spacer(modifier = Modifier.height(8.dp))

                    Button(
                        onClick = { onServiceSelected(service) },
                        shape = RoundedCornerShape(6.dp),
                        colors = ButtonDefaults.buttonColors(
                            containerColor = if (isSelected) SalonNavy else SalonTerracotta
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .height(34.dp)
                            .testTag("btn_select_service_${service.id}")
                    ) {
                        Text(
                            text = if (isSelected) "Sélectionné ✓" else "Choisir",
                            fontSize = 11.sp,
                            fontWeight = FontWeight.SemiBold
                        )
                    }
                }
            }
        }

        item(span = { GridItemSpan(2) }) {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}
