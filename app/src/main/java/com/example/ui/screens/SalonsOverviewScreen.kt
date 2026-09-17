package com.example.ui.screens

import android.content.Intent
import android.net.Uri
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
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.Call
import androidx.compose.material.icons.filled.Check
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Star
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.data.model.Salon
import com.example.ui.theme.SalonCream
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

@Composable
fun SalonsOverviewScreen(
    salons: List<Salon>,
    onSelectSalonForBooking: (Salon) -> Unit
) {
    val context = LocalContext.current

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SalonCream)
            .testTag("screen_salons_overview"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(16.dp)
    ) {
        item {
            Column {
                Text(
                    text = "Notre Réseau à Abidjan",
                    fontSize = 22.sp,
                    fontWeight = FontWeight.Bold,
                    color = SalonNavy,
                    fontFamily = FontFamily.Serif
                )
                Spacer(modifier = Modifier.height(4.dp))
                Text(
                    text = "3 salons indépendants réunis sous une identité unique d'excellence. Même niveau d'exigence, même grille tarifaire transparente.",
                    fontSize = 13.sp,
                    color = SalonInkSoft,
                    lineHeight = 18.sp
                )
            }
        }

        items(salons) { salon ->
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SalonPaper),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier
                    .fillMaxWidth()
                    .border(1.dp, SalonLine, RoundedCornerShape(16.dp))
                    .testTag("overview_salon_${salon.id}")
            ) {
                Column(modifier = Modifier.padding(18.dp)) {
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Column {
                            Text(
                                text = salon.nom,
                                fontSize = 18.sp,
                                fontWeight = FontWeight.Bold,
                                color = SalonNavy,
                                fontFamily = FontFamily.Serif
                            )
                            Row(verticalAlignment = Alignment.CenterVertically) {
                                Icon(
                                    imageVector = Icons.Default.LocationOn,
                                    contentDescription = null,
                                    tint = SalonTerracotta,
                                    modifier = Modifier.size(14.dp)
                                )
                                Spacer(modifier = Modifier.width(3.dp))
                                Text(
                                    text = "${salon.adresse} (${salon.distanceKm} km)",
                                    fontSize = 12.sp,
                                    color = SalonInkSoft
                                )
                            }
                        }

                        Row(
                            verticalAlignment = Alignment.CenterVertically,
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(SalonCream)
                                .padding(horizontal = 8.dp, vertical = 4.dp)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Star,
                                contentDescription = null,
                                tint = SalonTerracotta,
                                modifier = Modifier.size(14.dp)
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(
                                text = salon.note.toString(),
                                fontSize = 13.sp,
                                fontWeight = FontWeight.Bold,
                                color = SalonNavy
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))
                    Text(
                        text = salon.photoDesc,
                        fontSize = 12.sp,
                        color = SalonNavySoft,
                        lineHeight = 16.sp
                    )

                    Spacer(modifier = Modifier.height(10.dp))
                    HorizontalDivider(color = SalonLine, thickness = 1.dp)
                    Spacer(modifier = Modifier.height(10.dp))

                    // Horaires
                    Row(verticalAlignment = Alignment.Top) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = SalonTerracotta,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(8.dp))
                        Column {
                            Text(
                                text = "Horaires d'ouverture",
                                fontSize = 11.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SalonInkSoft
                            )
                            Text(
                                text = salon.horaires,
                                fontSize = 12.sp,
                                color = SalonInk
                            )
                        }
                    }

                    Spacer(modifier = Modifier.height(10.dp))

                    // Équipements inclus
                    val equipements = listOf("Climatisation", "WiFi", "Sièges confort", "Produits pros", "Paiement Mobile Money")
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(6.dp)
                    ) {
                        equipements.take(3).forEach { eq ->
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(6.dp))
                                    .background(SalonCream)
                                    .padding(horizontal = 8.dp, vertical = 4.dp)
                            ) {
                                Text(
                                    text = "✓ $eq",
                                    fontSize = 10.sp,
                                    color = SalonInkSoft
                                )
                            }
                        }
                    }

                    Spacer(modifier = Modifier.height(14.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.spacedBy(8.dp)
                    ) {
                        OutlinedButton(
                            onClick = {
                                val dialIntent = Intent(Intent.ACTION_DIAL, Uri.parse("tel:${salon.telephone.replace(" ", "")}"))
                                context.startActivity(dialIntent)
                            },
                            shape = RoundedCornerShape(8.dp),
                            modifier = Modifier.weight(1f)
                        ) {
                            Icon(
                                imageVector = Icons.Default.Call,
                                contentDescription = null,
                                modifier = Modifier.size(16.dp),
                                tint = SalonNavy
                            )
                            Spacer(modifier = Modifier.width(4.dp))
                            Text(text = "Appeler", fontSize = 12.sp, color = SalonNavy)
                        }

                        Button(
                            onClick = { onSelectSalonForBooking(salon) },
                            shape = RoundedCornerShape(8.dp),
                            colors = ButtonDefaults.buttonColors(containerColor = SalonTerracotta),
                            modifier = Modifier
                                .weight(1.5f)
                                .testTag("btn_book_at_${salon.id}")
                        ) {
                            Text(text = "Réserver ici", fontSize = 12.sp, fontWeight = FontWeight.SemiBold)
                            Spacer(modifier = Modifier.width(4.dp))
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

        item {
            Spacer(modifier = Modifier.height(20.dp))
        }
    }
}
