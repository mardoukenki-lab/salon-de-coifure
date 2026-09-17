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
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.CheckCircle
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Checkbox
import androidx.compose.material3.CheckboxDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Surface
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
import com.example.data.model.ServiceOption
import com.example.ui.theme.SalonCream
import com.example.ui.theme.SalonDangerBg
import com.example.ui.theme.SalonInk
import com.example.ui.theme.SalonInkSoft
import com.example.ui.theme.SalonLine
import com.example.ui.theme.SalonNavy
import com.example.ui.theme.SalonPaper
import com.example.ui.theme.SalonTerracotta
import com.example.ui.theme.SalonTerracottaSoft

@Composable
fun Step2OptionsScreen(
    service: HairService,
    options: List<ServiceOption>,
    selectedOptionIds: Set<String>,
    totalPrix: Int,
    onToggleOption: (String) -> Unit,
    onContinueClick: () -> Unit
) {
    Box(
        modifier = Modifier
            .fillMaxSize()
            .background(SalonCream)
            .testTag("screen_step_2")
    ) {
        LazyColumn(
            modifier = Modifier.fillMaxSize(),
            contentPadding = PaddingValues(start = 16.dp, end = 16.dp, top = 16.dp, bottom = 96.dp),
            verticalArrangement = Arrangement.spacedBy(12.dp)
        ) {
            item {
                // Header card
                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(containerColor = SalonPaper),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column(modifier = Modifier.padding(16.dp)) {
                        Text(
                            text = "Étape 2 · Les options",
                            fontSize = 12.sp,
                            color = SalonTerracotta,
                            fontWeight = FontWeight.Bold,
                            letterSpacing = 0.5.sp
                        )
                        Spacer(modifier = Modifier.height(2.dp))
                        Text(
                            text = "Personnaliser votre coiffure",
                            fontSize = 20.sp,
                            fontWeight = FontWeight.Bold,
                            color = SalonNavy,
                            fontFamily = FontFamily.Serif
                        )
                        Spacer(modifier = Modifier.height(6.dp))

                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .clip(RoundedCornerShape(8.dp))
                                .background(SalonCream)
                                .padding(horizontal = 12.dp, vertical = 8.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column {
                                Text(
                                    text = service.nom,
                                    fontSize = 13.sp,
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
                                color = SalonTerracotta
                            )
                        }

                        Spacer(modifier = Modifier.height(8.dp))
                        Text(
                            text = "Sélectionnez vos add-ons. Chaque choix met à jour le devis en direct.",
                            fontSize = 12.sp,
                            color = SalonInkSoft
                        )
                    }
                }
            }

            items(options) { option ->
                val isSelected = selectedOptionIds.contains(option.id)

                Card(
                    shape = RoundedCornerShape(12.dp),
                    colors = CardDefaults.cardColors(
                        containerColor = if (isSelected) SalonDangerBg.copy(alpha = 0.6f) else SalonPaper
                    ),
                    elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                    modifier = Modifier
                        .fillMaxWidth()
                        .border(
                            width = if (isSelected) 1.5.dp else 1.dp,
                            color = if (isSelected) SalonTerracotta else SalonLine,
                            shape = RoundedCornerShape(12.dp)
                        )
                        .clickable { onToggleOption(option.id) }
                        .testTag("option_item_${option.id}")
                ) {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(14.dp),
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Checkbox(
                            checked = isSelected,
                            onCheckedChange = { onToggleOption(option.id) },
                            colors = CheckboxDefaults.colors(
                                checkedColor = SalonTerracotta,
                                uncheckedColor = SalonLine
                            )
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column(modifier = Modifier.weight(1f)) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Text(
                                    text = option.nom,
                                    fontSize = 14.sp,
                                    fontWeight = FontWeight.SemiBold,
                                    color = SalonNavy
                                )
                                if (option.dureeMinAjoutee > 0) {
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "+${option.dureeMinAjoutee} min",
                                        fontSize = 10.sp,
                                        color = SalonInkSoft
                                    )
                                }
                            }
                            Spacer(modifier = Modifier.height(2.dp))
                            Text(
                                text = option.description,
                                fontSize = 11.sp,
                                color = SalonInkSoft,
                                lineHeight = 15.sp
                            )
                        }
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "+${String.format("%,d", option.prix).replace(',', ' ')} F",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = SalonTerracotta
                        )
                    }
                }
            }
        }

        // Live devis bottom bar
        Surface(
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter),
            color = SalonPaper,
            shadowElevation = 8.dp,
            border = androidx.compose.foundation.BorderStroke(1.dp, SalonLine)
        ) {
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(horizontal = 16.dp, vertical = 12.dp),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Column {
                    Text(
                        text = "Devis en direct",
                        fontSize = 11.sp,
                        color = SalonInkSoft
                    )
                    Text(
                        text = "${String.format("%,d", totalPrix).replace(',', ' ')} FCFA",
                        fontSize = 18.sp,
                        fontWeight = FontWeight.ExtraBold,
                        color = SalonNavy
                    )
                }

                Button(
                    onClick = onContinueClick,
                    shape = RoundedCornerShape(8.dp),
                    colors = ButtonDefaults.buttonColors(containerColor = SalonTerracotta),
                    modifier = Modifier.testTag("btn_continue_to_devis")
                ) {
                    Text(
                        text = "Voir le devis",
                        fontSize = 13.sp,
                        fontWeight = FontWeight.SemiBold
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.ArrowForward,
                        contentDescription = null,
                        modifier = Modifier.size(16.dp)
                    )
                }
            }
        }
    }
}
