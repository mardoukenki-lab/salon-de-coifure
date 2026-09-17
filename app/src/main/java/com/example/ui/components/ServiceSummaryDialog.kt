package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
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
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.ReceiptLong
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
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

/**
 * Dialogue récapitulatif des prestations sélectionnées et du coût total.
 * Permet de valider et continuer vers le choix du créneau ou de modifier.
 */
@Composable
fun ServiceSummaryDialog(
    salon: Salon,
    service: HairService,
    selectedOptions: List<ServiceOption>,
    totalPrix: Int,
    totalDureeMin: Int,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit
) {
    AlertDialog(
        onDismissRequest = onDismiss,
        shape = RoundedCornerShape(20.dp),
        containerColor = SalonPaper,
        tonalElevation = 6.dp,
        modifier = Modifier.testTag("dialog_service_summary"),
        title = {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.spacedBy(10.dp)
            ) {
                Box(
                    modifier = Modifier
                        .size(38.dp)
                        .clip(CircleShape)
                        .background(SalonTerracottaSoft.copy(alpha = 0.35f)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.ReceiptLong,
                        contentDescription = null,
                        tint = SalonTerracotta,
                        modifier = Modifier.size(20.dp)
                    )
                }
                Column {
                    Text(
                        text = "Récapitulatif de la prestation",
                        fontSize = 17.sp,
                        fontWeight = FontWeight.Bold,
                        color = SalonNavy,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = "Paiement à la caisse du salon",
                        fontSize = 12.sp,
                        color = SalonTerracotta,
                        fontWeight = FontWeight.SemiBold
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
                // Salon indicator
                Row(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(SalonCream)
                        .padding(horizontal = 12.dp, vertical = 8.dp),
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Icon(
                        imageVector = Icons.Default.LocationOn,
                        contentDescription = null,
                        tint = SalonTerracotta,
                        modifier = Modifier.size(16.dp)
                    )
                    Spacer(modifier = Modifier.width(6.dp))
                    Column {
                        Text(
                            text = salon.nom,
                            fontSize = 13.sp,
                            fontWeight = FontWeight.Bold,
                            color = SalonNavy
                        )
                        Text(
                            text = "${salon.quartier} · ${salon.adresse}",
                            fontSize = 11.sp,
                            color = SalonInkSoft
                        )
                    }
                }

                // Service Details
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
                            imageVector = Icons.Default.ContentCut,
                            contentDescription = null,
                            tint = SalonTerracotta,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Column {
                            Text(
                                text = service.nom,
                                fontSize = 14.sp,
                                fontWeight = FontWeight.Bold,
                                color = SalonNavy
                            )
                            Text(
                                text = "Base : ~${service.dureeMin} min",
                                fontSize = 11.sp,
                                color = SalonInkSoft
                            )
                        }
                    }
                    Text(
                        text = "${String.format("%,d", service.prixReference).replace(',', ' ')} FCFA",
                        fontSize = 14.sp,
                        fontWeight = FontWeight.Bold,
                        color = SalonNavy
                    )
                }

                // Options list if any
                if (selectedOptions.isNotEmpty()) {
                    HorizontalDivider(color = SalonLine, thickness = 1.dp)
                    Text(
                        text = "Options & Soins choisis :",
                        fontSize = 11.sp,
                        fontWeight = FontWeight.SemiBold,
                        color = SalonInkSoft
                    )
                    selectedOptions.forEach { opt ->
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(vertical = 1.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Box(
                                    modifier = Modifier
                                        .size(6.dp)
                                        .clip(CircleShape)
                                        .background(SalonTerracotta)
                                )
                                Spacer(modifier = Modifier.width(6.dp))
                                Text(
                                    text = opt.nom,
                                    fontSize = 12.sp,
                                    color = SalonInk
                                )
                            }
                            Text(
                                text = "+${String.format("%,d", opt.prix).replace(',', ' ')} F",
                                fontSize = 12.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SalonTerracotta
                            )
                        }
                    }
                }

                HorizontalDivider(color = SalonLine, thickness = 1.dp)

                // Total Duree & Total Cost Row
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.AccessTime,
                            contentDescription = null,
                            tint = SalonInkSoft,
                            modifier = Modifier.size(14.dp)
                        )
                        Spacer(modifier = Modifier.width(4.dp))
                        Text(
                            text = "Durée totale : ~${totalDureeMin} min",
                            fontSize = 12.sp,
                            color = SalonInkSoft
                        )
                    }

                    Column(horizontalAlignment = Alignment.End) {
                        Text(
                            text = "Coût total",
                            fontSize = 10.sp,
                            color = SalonInkSoft
                        )
                        Text(
                            text = "${String.format("%,d", totalPrix).replace(',', ' ')} FCFA",
                            fontSize = 18.sp,
                            fontWeight = FontWeight.ExtraBold,
                            color = SalonTerracotta
                        )
                    }
                }

                // Payment note
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(8.dp))
                        .background(SalonOkBg.copy(alpha = 0.5f))
                        .padding(horizontal = 10.dp, vertical = 6.dp)
                ) {
                    Text(
                        text = "💳 Le paiement s'effectuera directement à la caisse du salon le jour de votre rendez-vous.",
                        fontSize = 11.sp,
                        color = SalonOkFg,
                        lineHeight = 15.sp
                    )
                }
            }
        },
        confirmButton = {
            Button(
                onClick = onConfirm,
                shape = RoundedCornerShape(10.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SalonTerracotta),
                modifier = Modifier.testTag("btn_dialog_confirm")
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
        },
        dismissButton = {
            OutlinedButton(
                onClick = onDismiss,
                shape = RoundedCornerShape(10.dp),
                modifier = Modifier.testTag("btn_dialog_dismiss")
            ) {
                Text(
                    text = "Modifier",
                    fontSize = 13.sp,
                    color = SalonInkSoft
                )
            }
        }
    )
}
