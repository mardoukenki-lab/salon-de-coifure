package com.example.data.repository

import com.example.data.db.ReservationDao
import com.example.data.model.Creneau
import com.example.data.model.CreneauStatut
import com.example.data.model.HairService
import com.example.data.model.HairstyleItem
import com.example.data.model.ReservationEntity
import com.example.data.model.Salon
import com.example.data.model.ServiceOption
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import java.text.SimpleDateFormat
import java.util.Calendar
import java.util.Locale
import kotlin.random.Random

data class CrossSiteAlternative(
    val salonAlternatif: Salon,
    val creneauId: String,
    val heureDebut: String,
    val heureFin: String,
    val distanceKm: Double
)

class SalonRepository(
    private val reservationDao: ReservationDao,
    private val adminManager: AdminManager? = null
) {
    private val _salons = listOf(
        Salon(
            id = "yopougon",
            nom = "Salon Yopougon",
            quartier = "Yopougon",
            adresse = "Maroc · Carrefour Sainte Rita",
            distanceKm = 2.1,
            creneauxDispoAujourdhui = 3,
            horaires = "Mar - Sam: 08h30 - 19h30 · Dim: 10h00 - 18h00",
            telephone = "+225 07 08 12 34 56",
            note = 4.9,
            bufferMin = 15,
            photoDesc = "Spacieux, ambiance chaleureuse et sièges massants"
        ),
        Salon(
            id = "treichville",
            nom = "Salon Treichville 13ème",
            quartier = "Treichville",
            adresse = "Avenue 13 · Rue 12 face pharmacie",
            distanceKm = 4.6,
            creneauxDispoAujourdhui = 2,
            horaires = "Mar - Sam: 09h00 - 20h00 · Dim: 11h00 - 18h30",
            telephone = "+225 05 44 23 45 67",
            note = 4.8,
            bufferMin = 15,
            photoDesc = "Au cœur de Treichville, climatisation et bar à thé"
        ),
        Salon(
            id = "angre",
            nom = "Salon Angré",
            quartier = "Angré",
            adresse = "Cocody Angré 8ème Tranche · Carrefour Mahou",
            distanceKm = 7.3,
            creneauxDispoAujourdhui = 5,
            horaires = "Lun - Sam: 08h30 - 20h00 · Dim: 12h00 - 19h00",
            telephone = "+225 01 56 78 90 12",
            note = 5.0,
            bufferMin = 15,
            photoDesc = "Espace VIP privatisable, spécialistes lace et soins"
        )
    )

    private val _services = listOf(
        HairService(
            id = "tresses",
            nom = "Tresses africaines",
            description = "Nattes collées, box braids, knotless braids avec finitions parfaites",
            dureeMin = 120,
            prixReference = 8000,
            tag = "Prestation phare",
            iconType = "braids"
        ),
        HairService(
            id = "vanilles",
            nom = "Vanilles & Twists",
            description = "Passion twists, senegalese twists ultra légères et soignées",
            dureeMin = 90,
            prixReference = 6000,
            tag = "Tendance",
            iconType = "twists"
        ),
        HairService(
            id = "soin",
            nom = "Soin profond réparateur",
            description = "Bain d'huiles tièdes, karité brut d'Abidjan & vapeur ionique",
            dureeMin = 45,
            prixReference = 5000,
            tag = "Soin naturel",
            iconType = "treatment"
        ),
        HairService(
            id = "defrisage",
            nom = "Défrisage & Brushing",
            description = "Lissage protecteur sans agresser la fibre, brushing souple et soyeux",
            dureeMin = 60,
            prixReference = 7000,
            tag = "Classique",
            iconType = "smooth"
        ),
        HairService(
            id = "tissage",
            nom = "Pose Tissage & Perruque",
            description = "Pose invisible avec ou sans colle, customisation frontale soignée",
            dureeMin = 90,
            prixReference = 10000,
            tag = "Prestige",
            iconType = "wig"
        ),
        HairService(
            id = "locks",
            nom = "Départ & Entretien Locks",
            description = "Tournage au gel d'aloe vera pur et resserrage durable des repousses",
            dureeMin = 90,
            prixReference = 8500,
            tag = "Spécialité",
            iconType = "locks"
        )
    )

    private val _options = listOf(
        ServiceOption(
            id = "opt_meches",
            nom = "Mèches premium fournies",
            categorie = "Mèches",
            prix = 3000,
            dureeMinAjoutee = 0,
            description = "Fibres pré-étirées haute qualité, teintes au choix"
        ),
        ServiceOption(
            id = "opt_bijoux",
            nom = "Bijoux de tête & cauris",
            categorie = "Accessoires",
            prix = 1500,
            dureeMinAjoutee = 0,
            description = "Anneaux métalliques dorés, perles bois et cauris d'Afrique"
        ),
        ServiceOption(
            id = "opt_soin_apres",
            nom = "Soin apaisant après-pose",
            categorie = "Soins",
            prix = 2000,
            dureeMinAjoutee = 15,
            description = "Sérum rafraîchissant menthe poivrée & brume hydratante"
        ),
        ServiceOption(
            id = "opt_ext_longues",
            nom = "Extensions extra longues",
            categorie = "Longueur",
            prix = 4500,
            dureeMinAjoutee = 30,
            description = "Longueur jusqu'au bas du dos ou aux hanches"
        ),
        ServiceOption(
            id = "opt_shampoing",
            nom = "Shampoing clarifiant & massage",
            categorie = "Lavage",
            prix = 2500,
            dureeMinAjoutee = 15,
            description = "Massage relaxant du cuir chevelu et élimination des impuretés"
        ),
        ServiceOption(
            id = "opt_casque",
            nom = "Séchage casque vapeur",
            categorie = "Vapeur",
            prix = 1500,
            dureeMinAjoutee = 10,
            description = "Pénétration optimale des actifs hydratants"
        )
    )

    // Dynamic slot cache per salon and date to simulate real-time availability
    private val _creneauxState = MutableStateFlow<Map<String, List<Creneau>>>(emptyMap())
    val creneauxState: StateFlow<Map<String, List<Creneau>>> = _creneauxState.asStateFlow()

    init {
        initializeCreneaux()
    }

    private fun initializeCreneaux() {
        val days = getAvailableDays()
        val allSlotsMap = mutableMapOf<String, List<Creneau>>()

        val baseHours = listOf(
            "09h00" to "10h30",
            "10h30" to "12h00",
            "13h00" to "14h00",
            "14h00" to "15h00",
            "15h30" to "16h30",
            "16h30" to "17h30",
            "17h30" to "18h30"
        )

        for (salon in _salons) {
            for (day in days) {
                val key = "${salon.id}_${day.second}"
                val slots = baseHours.mapIndexed { index, (start, end) ->
                    // Make specific slots unavailable intentionally to demonstrate cross-site redirection
                    val isUnavailable = when {
                        // In Yopougon, 13h00 and 15h30 are taken (as in mockup specification!)
                        salon.id == "yopougon" && (start == "13h00" || start == "15h30") -> true
                        // In Treichville, 14h00 and 16h30 are taken, but 15h30 is FREE!
                        salon.id == "treichville" && (start == "14h00" || start == "16h30") -> true
                        // In Angre, 10h30 is taken
                        salon.id == "angre" && (start == "10h30") -> true
                        else -> false
                    }

                    Creneau(
                        id = "${salon.id}_${day.second}_$index",
                        salonId = salon.id,
                        dateStr = day.second,
                        heureDebut = start,
                        heureFin = end,
                        statut = if (isUnavailable) CreneauStatut.RESERVE else CreneauStatut.LIBRE
                    )
                }
                allSlotsMap[key] = slots
            }
        }
        _creneauxState.value = allSlotsMap
    }

    fun getSalons(): List<Salon> = _salons

    fun getSalonById(id: String): Salon? = _salons.find { it.id == id }

    fun getServices(): List<HairService> {
        if (adminManager == null) return _services
        return _services.map { service ->
            val customPrice = adminManager.getServicePrice(service.id, service.prixReference)
            service.copy(prixReference = customPrice)
        }
    }

    fun getServiceById(id: String): HairService? = getServices().find { it.id == id }

    fun getOptions(): List<ServiceOption> {
        if (adminManager == null) return _options
        return _options.map { option ->
            val customPrice = adminManager.getOptionPrice(option.id, option.prix)
            option.copy(prix = customPrice)
        }
    }

    fun getHairstyles(): List<HairstyleItem> = adminManager?.getHairstyles() ?: emptyList()

    fun updateServicePrice(serviceId: String, newPrice: Int) {
        adminManager?.setServicePrice(serviceId, newPrice)
    }

    fun updateOptionPrice(optionId: String, newPrice: Int) {
        adminManager?.setOptionPrice(optionId, newPrice)
    }

    fun addHairstyle(item: HairstyleItem) {
        adminManager?.addHairstyle(item)
    }

    fun deleteHairstyle(id: String) {
        adminManager?.deleteHairstyle(id)
    }

    fun getAvailableDays(): List<Pair<String, String>> {
        val list = mutableListOf<Pair<String, String>>()
        val cal = Calendar.getInstance()
        val formatDisplay = SimpleDateFormat("EEE d MMM", Locale.FRENCH)
        val formatKey = SimpleDateFormat("yyyy-MM-dd", Locale.FRENCH)

        for (i in 0..5) {
            val label = when (i) {
                0 -> "Aujourd'hui"
                1 -> "Demain"
                else -> {
                    val raw = formatDisplay.format(cal.time)
                    raw.replaceFirstChar { if (it.isLowerCase()) it.titlecase(Locale.FRENCH) else it.toString() }
                }
            }
            val key = formatKey.format(cal.time)
            list.add(label to key)
            cal.add(Calendar.DAY_OF_YEAR, 1)
        }
        return list
    }

    fun getCreneaux(salonId: String, dateKey: String): List<Creneau> {
        val key = "${salonId}_$dateKey"
        val existing = _creneauxState.value[key]
        if (existing != null && existing.isNotEmpty()) return existing

        val baseHours = listOf(
            "09h00" to "10h30",
            "10h30" to "12h00",
            "13h00" to "14h00",
            "14h00" to "15h00",
            "15h30" to "16h30",
            "16h30" to "17h30",
            "17h30" to "18h30"
        )
        val generated = baseHours.mapIndexed { index, (start, end) ->
            val isUnavailable = (index == 1 && salonId == "angre") ||
                    (index == 2 && salonId == "yopougon") ||
                    (index == 4 && salonId == "treichville")

            Creneau(
                id = "${salonId}_${dateKey}_$index",
                salonId = salonId,
                dateStr = dateKey,
                heureDebut = start,
                heureFin = end,
                statut = if (isUnavailable) CreneauStatut.RESERVE else CreneauStatut.LIBRE
            )
        }
        val mutable = _creneauxState.value.toMutableMap()
        mutable[key] = generated
        _creneauxState.value = mutable
        return generated
    }

    /**
     * Cross-site availability check:
     * When a slot is unavailable at the requested salon, find if the SAME time slot
     * is available at Treichville or Angré, or nearest free slot!
     */
    fun findCrossSiteAlternative(
        currentSalonId: String,
        targetSlotStart: String,
        dateKey: String
    ): CrossSiteAlternative? {
        val otherSalons = _salons.filter { it.id != currentSalonId }

        // 1. First priority: exactly the same time slot at another sister salon
        for (other in otherSalons) {
            val otherSlots = getCreneaux(other.id, dateKey)
            val match = otherSlots.find { it.heureDebut == targetSlotStart && it.statut == CreneauStatut.LIBRE }
            if (match != null) {
                return CrossSiteAlternative(
                    salonAlternatif = other,
                    creneauId = match.id,
                    heureDebut = match.heureDebut,
                    heureFin = match.heureFin,
                    distanceKm = other.distanceKm
                )
            }
        }

        // 2. Second priority: any free slot in another salon on that day
        for (other in otherSalons) {
            val otherSlots = getCreneaux(other.id, dateKey)
            val freeSlot = otherSlots.firstOrNull { it.statut == CreneauStatut.LIBRE }
            if (freeSlot != null) {
                return CrossSiteAlternative(
                    salonAlternatif = other,
                    creneauId = freeSlot.id,
                    heureDebut = freeSlot.heureDebut,
                    heureFin = freeSlot.heureFin,
                    distanceKm = other.distanceKm
                )
            }
        }

        return null
    }

    /**
     * Updates slot status to VERROUILLE (Hold during payment)
     */
    fun lockCreneau(creneauId: String) {
        val currentMap = _creneauxState.value.toMutableMap()
        for ((key, list) in currentMap) {
            if (list.any { it.id == creneauId }) {
                currentMap[key] = list.map {
                    if (it.id == creneauId) it.copy(statut = CreneauStatut.VERROUILLE) else it
                }
                break
            }
        }
        _creneauxState.value = currentMap
    }

    /**
     * Releases hold back to LIBRE
     */
    fun unlockCreneau(creneauId: String) {
        val currentMap = _creneauxState.value.toMutableMap()
        for ((key, list) in currentMap) {
            if (list.any { it.id == creneauId }) {
                currentMap[key] = list.map {
                    if (it.id == creneauId) it.copy(statut = CreneauStatut.LIBRE) else it
                }
                break
            }
        }
        _creneauxState.value = currentMap
    }

    /**
     * Confirms the booking and persists it in Room database
     */
    suspend fun confirmReservation(
        salon: Salon,
        service: HairService,
        options: List<ServiceOption>,
        dateStr: String,
        heure: String,
        totalPrix: Int,
        creneauId: String,
        clientNom: String,
        clientTelephone: String,
        modePaiement: String
    ): ReservationEntity {
        // Mark creneau as RESERVE
        val currentMap = _creneauxState.value.toMutableMap()
        for ((key, list) in currentMap) {
            if (list.any { it.id == creneauId }) {
                currentMap[key] = list.map {
                    if (it.id == creneauId) it.copy(statut = CreneauStatut.RESERVE) else it
                }
                break
            }
        }
        _creneauxState.value = currentMap

        val optionsTotal = options.sumOf { it.prix }
        val optionsSummary = if (options.isEmpty()) "Aucune option" else options.joinToString(", ") { it.nom }
        val randomNum = Random.nextInt(1000, 9999)
        val code = "ABJ-${salon.quartier.take(3).uppercase()}-$randomNum"
        val refPaiement = "CI-MM-${System.currentTimeMillis().toString().takeLast(6)}"
        val dureeTotale = service.dureeMin + options.sumOf { it.dureeMinAjoutee }

        val entity = ReservationEntity(
            codeReservation = code,
            salonId = salon.id,
            salonNom = salon.nom,
            salonQuartier = salon.quartier,
            salonAdresse = salon.adresse,
            serviceId = service.id,
            serviceNom = service.nom,
            optionsNoms = optionsSummary,
            optionsPrixTotal = optionsTotal,
            dateStr = dateStr,
            heure = heure,
            dureeTotaleMin = dureeTotale,
            prixTotal = totalPrix,
            statut = "CONFIRMEE",
            modePaiement = modePaiement,
            referencePaiement = refPaiement,
            clientNom = clientNom,
            clientTelephone = clientTelephone
        )

        val insertedId = reservationDao.insertReservation(entity)
        return entity.copy(id = insertedId.toInt())
    }

    fun getAllReservations(): Flow<List<ReservationEntity>> = reservationDao.getAllReservations()

    suspend fun cancelReservation(id: Int) {
        reservationDao.updateReservationStatus(id, "ANNULEE")
    }
}
