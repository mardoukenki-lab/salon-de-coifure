package com.example.ui.components

import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccessTime
import androidx.compose.material.icons.filled.CalendarMonth
import androidx.compose.material.icons.filled.ChevronLeft
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material.icons.filled.Schedule
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.DatePicker
import androidx.compose.material3.DatePickerDefaults
import androidx.compose.material3.DatePickerDialog
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.SelectableDates
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.rememberDatePickerState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
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
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Date
import java.util.Locale

/**
 * Composant Date and Time Picker permettant de sélectionner précisément
 * le jour (avec calendrier interactif DatePickerDialog) et la tranche horaire.
 */
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DateTimePickerSection(
    selectedDateDisplay: String,
    selectedTimeStr: String?,
    onCustomDateSelected: (dateDisplay: String, dateKey: String) -> Unit,
    onTimePicked: (String) -> Unit
) {
    var showDatePickerDialog by remember { mutableStateOf(false) }

    val popularHours = listOf(
        "09h00", "10h00", "11h00", "12h00",
        "13h30", "14h30", "15h30", "16h30", "17h30", "18h30"
    )

    Card(
        shape = RoundedCornerShape(14.dp),
        colors = CardDefaults.cardColors(containerColor = SalonPaper),
        elevation = CardDefaults.cardElevation(defaultElevation = 1.dp),
        modifier = Modifier
            .fillMaxWidth()
            .border(1.dp, SalonLine, RoundedCornerShape(14.dp))
            .testTag("component_date_time_picker")
    ) {
        Column(modifier = Modifier.padding(16.dp)) {
            // Header
            Row(
                modifier = Modifier.fillMaxWidth(),
                horizontalArrangement = Arrangement.SpaceBetween,
                verticalAlignment = Alignment.CenterVertically
            ) {
                Row(verticalAlignment = Alignment.CenterVertically) {
                    Box(
                        modifier = Modifier
                            .size(32.dp)
                            .clip(CircleShape)
                            .background(SalonTerracottaSoft.copy(alpha = 0.4f)),
                        contentAlignment = Alignment.Center
                    ) {
                        Icon(
                            imageVector = Icons.Default.CalendarMonth,
                            contentDescription = null,
                            tint = SalonTerracotta,
                            modifier = Modifier.size(18.dp)
                        )
                    }
                    Spacer(modifier = Modifier.width(8.dp))
                    Column {
                        Text(
                            text = "Date & Heure du RDV",
                            fontSize = 14.sp,
                            fontWeight = FontWeight.Bold,
                            color = SalonNavy
                        )
                        Text(
                            text = "Choisissez votre moment idéal",
                            fontSize = 11.sp,
                            color = SalonInkSoft
                        )
                    }
                }

                // Button to open full interactive DatePicker
                OutlinedButton(
                    onClick = { showDatePickerDialog = true },
                    shape = RoundedCornerShape(8.dp),
                    contentPadding = PaddingValues(horizontal = 10.dp, vertical = 6.dp),
                    modifier = Modifier.testTag("btn_open_calendar_picker")
                ) {
                    Icon(
                        imageVector = Icons.Default.CalendarMonth,
                        contentDescription = null,
                        tint = SalonTerracotta,
                        modifier = Modifier.size(14.dp)
                    )
                    Spacer(modifier = Modifier.width(4.dp))
                    Text(
                        text = "Calendrier",
                        fontSize = 12.sp,
                        color = SalonTerracotta,
                        fontWeight = FontWeight.SemiBold
                    )
                }
            }

            Spacer(modifier = Modifier.height(12.dp))

            // Selected date indicator
            Box(
                modifier = Modifier
                    .fillMaxWidth()
                    .clip(RoundedCornerShape(8.dp))
                    .background(SalonCream)
                    .border(1.dp, SalonTerracottaSoft, RoundedCornerShape(8.dp))
                    .padding(horizontal = 12.dp, vertical = 8.dp)
            ) {
                Row(
                    modifier = Modifier.fillMaxWidth(),
                    horizontalArrangement = Arrangement.SpaceBetween,
                    verticalAlignment = Alignment.CenterVertically
                ) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Icon(
                            imageVector = Icons.Default.Schedule,
                            contentDescription = null,
                            tint = SalonTerracotta,
                            modifier = Modifier.size(16.dp)
                        )
                        Spacer(modifier = Modifier.width(6.dp))
                        Text(
                            text = "Date : $selectedDateDisplay",
                            fontSize = 13.sp,
                            fontWeight = FontWeight.SemiBold,
                            color = SalonNavy
                        )
                    }
                    if (selectedTimeStr != null) {
                        Box(
                            modifier = Modifier
                                .clip(RoundedCornerShape(6.dp))
                                .background(SalonTerracotta)
                                .padding(horizontal = 8.dp, vertical = 2.dp)
                        ) {
                            Text(
                                text = selectedTimeStr,
                                fontSize = 11.sp,
                                fontWeight = FontWeight.Bold,
                                color = SalonPaper
                            )
                        }
                    }
                }
            }

            Spacer(modifier = Modifier.height(14.dp))

            // Quick Time Slot Picker
            Text(
                text = "Heure souhaitée (Créneau rapide) :",
                fontSize = 12.sp,
                fontWeight = FontWeight.SemiBold,
                color = SalonInkSoft
            )

            Spacer(modifier = Modifier.height(8.dp))

            LazyRow(
                horizontalArrangement = Arrangement.spacedBy(8.dp),
                modifier = Modifier.fillMaxWidth()
            ) {
                items(popularHours) { hour ->
                    val isHourSelected = selectedTimeStr?.startsWith(hour.substring(0, 2)) == true

                    Box(
                        modifier = Modifier
                            .clip(RoundedCornerShape(8.dp))
                            .background(if (isHourSelected) SalonTerracotta else SalonPaper)
                            .border(
                                1.dp,
                                if (isHourSelected) SalonTerracotta else SalonLine,
                                RoundedCornerShape(8.dp)
                            )
                            .clickable { onTimePicked(hour) }
                            .padding(horizontal = 12.dp, vertical = 8.dp)
                            .testTag("time_chip_$hour"),
                        contentAlignment = Alignment.Center
                    ) {
                        Text(
                            text = hour,
                            fontSize = 12.sp,
                            fontWeight = if (isHourSelected) FontWeight.Bold else FontWeight.Medium,
                            color = if (isHourSelected) SalonPaper else SalonNavy
                        )
                    }
                }
            }
        }
    }

    // Material 3 DatePickerDialog
    if (showDatePickerDialog) {
        val datePickerState = rememberDatePickerState(
            selectableDates = object : SelectableDates {
                override fun isSelectableDate(utcTimeMillis: Long): Boolean {
                    // Allow today and future days (up to 30 days ahead)
                    val today = Calendar.getInstance().apply {
                        set(Calendar.HOUR_OF_DAY, 0)
                        set(Calendar.MINUTE, 0)
                        set(Calendar.SECOND, 0)
                        set(Calendar.MILLISECOND, 0)
                    }.timeInMillis
                    val futureLimit = today + (30L * 24 * 60 * 60 * 1000)
                    return utcTimeMillis >= today && utcTimeMillis <= futureLimit
                }
            }
        )

        DatePickerDialog(
            onDismissRequest = { showDatePickerDialog = false },
            confirmButton = {
                TextButton(
                    onClick = {
                        val selectedMillis = datePickerState.selectedDateMillis
                        if (selectedMillis != null) {
                            val cal = Calendar.getInstance().apply {
                                timeInMillis = selectedMillis
                            }
                            val displayFormat = SimpleDateFormat("EEE d MMM", Locale.FRENCH)
                            val keyFormat = SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH)
                            val display = displayFormat.format(cal.time).replaceFirstChar { it.uppercase() }
                            val key = keyFormat.format(cal.time)
                            onCustomDateSelected(display, key)
                        }
                        showDatePickerDialog = false
                    },
                    modifier = Modifier.testTag("btn_confirm_datepicker")
                ) {
                    Text("Valider la date", color = SalonTerracotta, fontWeight = FontWeight.Bold)
                }
            },
            dismissButton = {
                TextButton(onClick = { showDatePickerDialog = false }) {
                    Text("Annuler", color = SalonInkSoft)
                }
            },
            colors = DatePickerDefaults.colors(
                containerColor = SalonPaper
            )
        ) {
            DatePicker(
                state = datePickerState,
                colors = DatePickerDefaults.colors(
                    headlineContentColor = SalonNavy,
                    titleContentColor = SalonTerracotta,
                    selectedDayContainerColor = SalonTerracotta,
                    selectedDayContentColor = SalonPaper,
                    todayDateBorderColor = SalonTerracotta,
                    todayContentColor = SalonTerracotta
                )
            )
        }
    }
}
