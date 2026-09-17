package com.example.ui.screens

import androidx.compose.foundation.Image
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
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowForward
import androidx.compose.material.icons.filled.AutoAwesome
import androidx.compose.material.icons.filled.Close
import androidx.compose.material.icons.filled.ContentCut
import androidx.compose.material.icons.filled.Info
import androidx.compose.material.icons.filled.LocationOn
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material.icons.filled.Search
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.Storefront
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontFamily
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.example.R
import com.example.data.model.HairService
import com.example.data.model.HairstyleItem
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
fun Step0SalonScreen(
    salons: List<Salon>,
    filteredSalons: List<Salon>,
    filteredServices: List<HairService>,
    filteredHairstyles: List<HairstyleItem>,
    searchQuery: String,
    selectedCategory: String,
    onSearchQueryChange: (String) -> Unit,
    onCategorySelected: (String) -> Unit,
    onClearSearch: () -> Unit,
    onSalonSelected: (Salon) -> Unit,
    onServiceSelected: (HairService) -> Unit = {},
    onHairstyleSelected: (HairstyleItem) -> Unit = {}
) {
    val isFiltering = searchQuery.isNotBlank() || selectedCategory != "Tous"

    val categories = listOf(
        "Tous",
        "Salons",
        "Tresses",
        "Twists",
        "Soins",
        "Locks",
        "Tissage"
    )

    LazyColumn(
        modifier = Modifier
            .fillMaxSize()
            .background(SalonCream)
            .testTag("screen_step_0"),
        contentPadding = PaddingValues(16.dp),
        verticalArrangement = Arrangement.spacedBy(14.dp)
    ) {
        // 1. Barre de Recherche au sommet de l'écran principal
        item {
            Card(
                shape = RoundedCornerShape(16.dp),
                colors = CardDefaults.cardColors(containerColor = SalonPaper),
                elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                Column(modifier = Modifier.padding(14.dp)) {
                    OutlinedTextField(
                        value = searchQuery,
                        onValueChange = onSearchQueryChange,
                        placeholder = {
                            Text(
                                text = "Rechercher un salon ou service (ex: tresses, Angré...)",
                                fontSize = 13.sp,
                                color = SalonInkSoft
                            )
                        },
                        leadingIcon = {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = "Rechercher",
                                tint = SalonTerracotta
                            )
                        },
                        trailingIcon = {
                            if (searchQuery.isNotBlank()) {
                                IconButton(
                                    onClick = onClearSearch,
                                    modifier = Modifier.testTag("btn_clear_search")
                                ) {
                                    Icon(
                                        imageVector = Icons.Default.Close,
                                        contentDescription = "Effacer la recherche",
                                        tint = SalonInkSoft
                                    )
                                }
                            }
                        },
                        singleLine = true,
                        shape = RoundedCornerShape(12.dp),
                        colors = OutlinedTextFieldDefaults.colors(
                            focusedBorderColor = SalonTerracotta,
                            unfocusedBorderColor = SalonLine,
                            focusedContainerColor = SalonPaper,
                            unfocusedContainerColor = SalonCream.copy(alpha = 0.5f)
                        ),
                        modifier = Modifier
                            .fillMaxWidth()
                            .testTag("search_bar_main")
                    )

                    Spacer(modifier = Modifier.height(10.dp))

                    // Catégories défilables horizontalement
                    LazyRow(
                        horizontalArrangement = Arrangement.spacedBy(8.dp),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        items(categories) { category ->
                            val isSelected = selectedCategory == category
                            Box(
                                modifier = Modifier
                                    .clip(RoundedCornerShape(20.dp))
                                    .background(if (isSelected) SalonNavy else SalonCream)
                                    .border(
                                        1.dp,
                                        if (isSelected) SalonTerracotta else SalonLine,
                                        RoundedCornerShape(20.dp)
                                    )
                                    .clickable { onCategorySelected(category) }
                                    .padding(horizontal = 14.dp, vertical = 7.dp)
                                    .testTag("chip_category_$category"),
                                contentAlignment = Alignment.Center
                            ) {
                                Text(
                                    text = category,
                                    fontSize = 12.sp,
                                    fontWeight = if (isSelected) FontWeight.Bold else FontWeight.Medium,
                                    color = if (isSelected) SalonPaper else SalonNavy
                                )
                            }
                        }
                    }
                }
            }
        }

        // 2. Mode Filtrage ACTIF (affiche les salons, prestations ou coiffures correspondantes)
        if (isFiltering) {
            val totalResults = filteredSalons.size + filteredServices.size + filteredHairstyles.size

            item {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Text(
                        text = "Résultats de recherche ($totalResults)",
                        fontWeight = FontWeight.Bold,
                        fontSize = 15.sp,
                        color = SalonNavy,
                        fontFamily = FontFamily.Serif
                    )
                    Text(
                        text = "Filtre : $selectedCategory",
                        fontSize = 12.sp,
                        color = SalonTerracotta,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            if (totalResults == 0) {
                item {
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SalonPaper),
                        modifier = Modifier.fillMaxWidth()
                    ) {
                        Column(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(24.dp),
                            horizontalAlignment = Alignment.CenterHorizontally
                        ) {
                            Icon(
                                imageVector = Icons.Default.Search,
                                contentDescription = null,
                                tint = SalonInkSoft,
                                modifier = Modifier.size(36.dp)
                            )
                            Spacer(modifier = Modifier.height(8.dp))
                            Text(
                                text = "Aucun salon ou prestation ne correspond à « $searchQuery »",
                                fontSize = 14.sp,
                                fontWeight = FontWeight.SemiBold,
                                color = SalonNavy
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Essayez un autre mot-clé (ex: tresses, spa, Yopougon, twists).",
                                fontSize = 12.sp,
                                color = SalonInkSoft
                            )
                            Spacer(modifier = Modifier.height(12.dp))
                            OutlinedButton(onClick = onClearSearch) {
                                Text("Réinitialiser les filtres", fontSize = 12.sp)
                            }
                        }
                    }
                }
            }

            // Section Salons trouvés
            if (filteredSalons.isNotEmpty()) {
                item {
                    Text(
                        text = "Salons correspondants (${filteredSalons.size})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = SalonTerracotta
                    )
                }
                items(filteredSalons) { salon ->
                    SalonCardItem(
                        salon = salon,
                        onClick = { onSalonSelected(salon) }
                    )
                }
            }

            // Section Prestations trouvées
            if (filteredServices.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Prestations & Tarifs correspondants (${filteredServices.size})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = SalonTerracotta
                    )
                }
                items(filteredServices) { service ->
                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SalonPaper),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, SalonLine, RoundedCornerShape(14.dp))
                            .clickable { onServiceSelected(service) }
                            .testTag("service_search_result_${service.id}")
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(14.dp),
                            horizontalArrangement = Arrangement.SpaceBetween,
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Column(modifier = Modifier.weight(1f)) {
                                Row(verticalAlignment = Alignment.CenterVertically) {
                                    Box(
                                        modifier = Modifier
                                            .clip(RoundedCornerShape(6.dp))
                                            .background(SalonTerracottaSoft)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = service.tag,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = SalonTerracotta
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "${service.dureeMin} min",
                                        fontSize = 11.sp,
                                        color = SalonInkSoft
                                    )
                                }
                                Spacer(modifier = Modifier.height(4.dp))
                                Text(
                                    text = service.nom,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 14.sp,
                                    color = SalonNavy
                                )
                                Text(
                                    text = service.description,
                                    fontSize = 12.sp,
                                    color = SalonInkSoft,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Spacer(modifier = Modifier.width(10.dp))

                            Column(horizontalAlignment = Alignment.End) {
                                Text(
                                    text = "${service.prixReference} F",
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 15.sp,
                                    color = SalonTerracotta
                                )
                                Spacer(modifier = Modifier.height(4.dp))
                                Button(
                                    onClick = { onServiceSelected(service) },
                                    shape = RoundedCornerShape(8.dp),
                                    colors = ButtonDefaults.buttonColors(containerColor = SalonNavy),
                                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                                ) {
                                    Text("Réserver", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                                }
                            }
                        }
                    }
                }
            }

            // Section Modèles de coiffure trouvés
            if (filteredHairstyles.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(6.dp))
                    Text(
                        text = "Idées de Coiffures du Catalogue (${filteredHairstyles.size})",
                        fontWeight = FontWeight.Bold,
                        fontSize = 13.sp,
                        color = SalonTerracotta
                    )
                }
                items(filteredHairstyles) { item ->
                    val imgRes = if (item.imageResName == "img_coiffure_showcase") {
                        R.drawable.img_coiffure_showcase
                    } else {
                        R.drawable.img_salon_hero
                    }

                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SalonPaper),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, SalonLine, RoundedCornerShape(14.dp))
                            .clickable { onHairstyleSelected(item) }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(68.dp)
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
                                            .background(SalonNavySoft)
                                            .padding(horizontal = 6.dp, vertical = 2.dp)
                                    ) {
                                        Text(
                                            text = item.categorie,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = SalonNavy
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "${item.prixEstime} FCFA",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SalonTerracotta
                                    )
                                }
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = item.titre,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = SalonNavy
                                )
                                Text(
                                    text = item.description,
                                    fontSize = 11.sp,
                                    color = SalonInkSoft,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = { onHairstyleSelected(item) },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SalonTerracotta),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text("Choisir", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        } else {
            // 3. Vue Normale (Accueil sans filtre)
            item {
                // Hero card avec visuel authentique
                Card(
                    shape = RoundedCornerShape(16.dp),
                    colors = CardDefaults.cardColors(containerColor = SalonPaper),
                    elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
                    modifier = Modifier.fillMaxWidth()
                ) {
                    Column {
                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .height(145.dp)
                        ) {
                            Image(
                                painter = painterResource(id = R.drawable.img_salon_hero),
                                contentDescription = "Salons de coiffure Abidjan",
                                modifier = Modifier.fillMaxSize(),
                                contentScale = ContentScale.Crop
                            )
                            Box(
                                modifier = Modifier
                                    .fillMaxSize()
                                    .background(SalonNavy.copy(alpha = 0.35f))
                            )
                            Box(
                                modifier = Modifier
                                    .padding(12.dp)
                                    .align(Alignment.BottomStart)
                                    .clip(RoundedCornerShape(8.dp))
                                    .background(SalonNavy.copy(alpha = 0.85f))
                                    .padding(horizontal = 10.dp, vertical = 5.dp)
                            ) {
                                Text(
                                    text = "Haute Coiffure & Soins à Abidjan",
                                    color = SalonPaper,
                                    fontSize = 12.sp,
                                    fontWeight = FontWeight.SemiBold
                                )
                            }
                        }

                        Column(modifier = Modifier.padding(16.dp)) {
                            Text(
                                text = "Étape 0 · Choisir un salon",
                                fontSize = 12.sp,
                                color = SalonTerracotta,
                                fontWeight = FontWeight.Bold,
                                letterSpacing = 0.5.sp
                            )
                            Spacer(modifier = Modifier.height(4.dp))
                            Text(
                                text = "Où souhaitez-vous être coiffée ?",
                                fontSize = 20.sp,
                                fontWeight = FontWeight.Bold,
                                color = SalonNavy,
                                fontFamily = FontFamily.Serif
                            )
                            Spacer(modifier = Modifier.height(6.dp))
                            Text(
                                text = "Trois adresses prestigieuses sous une enseigne unique. Réservation synchronisée en temps réel.",
                                fontSize = 13.sp,
                                color = SalonInkSoft,
                                lineHeight = 18.sp
                            )
                        }
                    }
                }
            }

            item {
                // Bannière de réassurance groupe
                Box(
                    modifier = Modifier
                        .fillMaxWidth()
                        .clip(RoundedCornerShape(10.dp))
                        .background(SalonTerracottaSoft.copy(alpha = 0.5f))
                        .border(1.dp, SalonTerracottaSoft, RoundedCornerShape(10.dp))
                        .padding(12.dp)
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Info,
                            contentDescription = null,
                            tint = SalonTerracotta,
                            modifier = Modifier.size(20.dp)
                        )
                        Spacer(modifier = Modifier.width(10.dp))
                        Text(
                            text = "Les 3 salons appartiennent au même groupe — un profil, des tarifs transparents et un historique partagé.",
                            fontSize = 12.sp,
                            color = SalonNavy,
                            lineHeight = 17.sp,
                            fontWeight = FontWeight.Medium
                        )
                    }
                }
            }

            // Liste des salons
            items(salons) { salon ->
                SalonCardItem(
                    salon = salon,
                    onClick = { onSalonSelected(salon) }
                )
            }

            // Vitrine d'idées de coiffures (gérée par les admins)
            if (filteredHairstyles.isNotEmpty()) {
                item {
                    Spacer(modifier = Modifier.height(6.dp))
                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        horizontalArrangement = Arrangement.SpaceBetween,
                        verticalAlignment = Alignment.CenterVertically
                    ) {
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.AutoAwesome,
                                contentDescription = null,
                                tint = SalonTerracotta,
                                modifier = Modifier.size(18.dp)
                            )
                            Spacer(modifier = Modifier.width(6.dp))
                            Text(
                                text = "Tendances & Coiffures du moment",
                                fontWeight = FontWeight.Bold,
                                fontSize = 15.sp,
                                color = SalonNavy,
                                fontFamily = FontFamily.Serif
                            )
                        }
                    }
                }

                items(filteredHairstyles.take(3)) { hairstyle ->
                    val imgRes = if (hairstyle.imageResName == "img_coiffure_showcase") {
                        R.drawable.img_coiffure_showcase
                    } else {
                        R.drawable.img_salon_hero
                    }

                    Card(
                        shape = RoundedCornerShape(14.dp),
                        colors = CardDefaults.cardColors(containerColor = SalonPaper),
                        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
                        modifier = Modifier
                            .fillMaxWidth()
                            .border(1.dp, SalonLine, RoundedCornerShape(14.dp))
                            .clickable { onHairstyleSelected(hairstyle) }
                    ) {
                        Row(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(12.dp),
                            verticalAlignment = Alignment.CenterVertically
                        ) {
                            Box(
                                modifier = Modifier
                                    .size(68.dp)
                                    .clip(RoundedCornerShape(10.dp))
                            ) {
                                Image(
                                    painter = painterResource(id = imgRes),
                                    contentDescription = hairstyle.titre,
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
                                            text = hairstyle.categorie,
                                            fontSize = 10.sp,
                                            fontWeight = FontWeight.Bold,
                                            color = SalonTerracotta
                                        )
                                    }
                                    Spacer(modifier = Modifier.width(6.dp))
                                    Text(
                                        text = "${hairstyle.prixEstime} FCFA",
                                        fontSize = 12.sp,
                                        fontWeight = FontWeight.Bold,
                                        color = SalonNavy
                                    )
                                }
                                Spacer(modifier = Modifier.height(3.dp))
                                Text(
                                    text = hairstyle.titre,
                                    fontWeight = FontWeight.Bold,
                                    fontSize = 13.sp,
                                    color = SalonNavy
                                )
                                Text(
                                    text = hairstyle.description,
                                    fontSize = 11.sp,
                                    color = SalonInkSoft,
                                    maxLines = 2,
                                    overflow = TextOverflow.Ellipsis
                                )
                            }

                            Spacer(modifier = Modifier.width(8.dp))

                            Button(
                                onClick = { onHairstyleSelected(hairstyle) },
                                shape = RoundedCornerShape(8.dp),
                                colors = ButtonDefaults.buttonColors(containerColor = SalonTerracotta),
                                contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp)
                            ) {
                                Text("Choisir", fontSize = 11.sp, fontWeight = FontWeight.Bold)
                            }
                        }
                    }
                }
            }
        }

        item {
            Spacer(modifier = Modifier.height(16.dp))
        }
    }
}

@Composable
fun SalonCardItem(
    salon: Salon,
    onClick: () -> Unit
) {
    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SalonPaper),
        elevation = CardDefaults.cardElevation(defaultElevation = 2.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, SalonLine, RoundedCornerShape(14.dp))
            .clickable { onClick() }
            .testTag("salon_card_${salon.id}")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.Top
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(42.dp)
                            .clip(CircleShape)
                            .background(SalonRose.copy(alpha = 0.4f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = salon.quartier.take(1),
                            fontWeight = FontWeight.Bold,
                            fontSize = 18.sp,
                            color = SalonTerracotta
                        )
                    }
                    Spacer(modifier = Modifier.width(12.dp))
                    Column {
                        Text(
                            text = salon.nom,
                            fontSize = 17.sp,
                            fontWeight = FontWeight.Bold,
                            color = SalonNavy,
                            fontFamily = FontFamily.Serif
                        )
                        Row(verticalAlignment = Alignment.CenterVertically) {
                            Icon(
                                imageVector = Icons.Default.LocationOn,
                                contentDescription = null,
                                tint = SalonTerracotta,
                                modifier = Modifier.size(13.dp)
                            )
                            Spacer(modifier = Modifier.width(3.dp))
                            Text(
                                text = "${salon.distanceKm} km · ${salon.adresse}",
                                fontSize = 12.sp,
                                color = SalonInkSoft
                            )
                        }
                    }
                }

                Row(
                    verticalAlignment = Alignment.CenterVertically,
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(SalonCream)
                        .padding(horizontal = 6.dp, vertical = 3.dp)
                ) {
                    Icon(
                        imageVector = Icons.Default.Star,
                        contentDescription = null,
                        tint = SalonTerracotta,
                        modifier = Modifier.size(13.dp)
                    )
                    Spacer(modifier = Modifier.width(2.dp))
                    Text(
                        text = salon.note.toString(),
                        fontSize = 12.sp,
                        fontWeight = FontWeight.Bold,
                        color = SalonNavy
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            // Badges row: live slot count and hours
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(SalonOkBg)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "${salon.creneauxDispoAujourdhui} créneaux dispo aujourd'hui",
                        fontSize = 11.sp,
                        color = SalonOkFg,
                        fontWeight = FontWeight.SemiBold
                    )
                }

                Box(
                    modifier = Modifier
                        .clip(RoundedCornerShape(6.dp))
                        .background(SalonCream)
                        .padding(horizontal = 8.dp, vertical = 4.dp)
                ) {
                    Text(
                        text = "Climatisé · WiFi",
                        fontSize = 11.sp,
                        color = SalonInkSoft
                    )
                }
            }

            Spacer(modifier = Modifier.height(10.dp))

            Text(
                text = salon.photoDesc,
                fontSize = 12.sp,
                color = SalonInkSoft,
                fontStyle = androidx.compose.ui.text.font.FontStyle.Italic
            )

            Spacer(modifier = Modifier.height(12.dp))

            Button(
                onClick = onClick,
                shape = RoundedCornerShape(8.dp),
                colors = ButtonDefaults.buttonColors(containerColor = SalonTerracotta),
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("btn_select_salon_${salon.id}")
            ) {
                Text(
                    text = "Choisir ${salon.nom}",
                    fontWeight = FontWeight.SemiBold,
                    fontSize = 13.sp
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
