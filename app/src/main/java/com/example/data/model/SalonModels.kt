package com.example.data.model

import androidx.room.Entity
import androidx.room.PrimaryKey

/**
 * Entité représentant un salon de coiffure du réseau à Abidjan.
 */
data class Salon(
    val id: String,
    val nom: String,
    val quartier: String,
    val adresse: String,
    val distanceKm: Double,
    val creneauxDispoAujourdhui: Int,
    val horaires: String,
    val telephone: String,
    val note: Double = 4.9,
    val bufferMin: Int = 15,
    val actif: Boolean = true,
    val photoDesc: String = "Salon moderne et climatisé"
)

/**
 * Prestation de coiffure proposée par le réseau.
 */
data class HairService(
    val id: String,
    val nom: String,
    val description: String,
    val dureeMin: Int,
    val prixReference: Int,
    val tag: String,
    val iconType: String
)

/**
 * Option / Add-on personnalisable.
 */
data class ServiceOption(
    val id: String,
    val nom: String,
    val categorie: String,
    val prix: Int,
    val dureeMinAjoutee: Int = 0,
    val description: String = ""
)

/**
 * Statut d'un créneau horaire.
 */
enum class CreneauStatut {
    LIBRE,
    VERROUILLE,
    RESERVE
}

/**
 * Créneau horaire spécifique à un salon et un jour.
 */
data class Creneau(
    val id: String,
    val salonId: String,
    val dateStr: String,
    val heureDebut: String,
    val heureFin: String,
    val statut: CreneauStatut,
    val bufferMin: Int = 15
)

/**
 * Opérateur de paiement Mobile Money.
 */
enum class MobileMoneyOperator(
    val nomAffiche: String,
    val couleurHex: Long,
    val indicatif: String
) {
    ORANGE_MONEY("Orange Money", 0xFFFF8A00, "07"),
    MTN_MOMO("MTN MoMo", 0xFFFFCC00, "05"),
    MOOV_MONEY("Moov Money", 0xFF006699, "01"),
    WAVE("Wave", 0xFF1DC3F0, "07")
}

/**
 * Entité Room pour la persistance locale des réservations du client.
 */
@Entity(tableName = "reservations")
data class ReservationEntity(
    @PrimaryKey(autoGenerate = true)
    val id: Int = 0,
    val codeReservation: String,
    val salonId: String,
    val salonNom: String,
    val salonQuartier: String,
    val salonAdresse: String,
    val serviceId: String,
    val serviceNom: String,
    val optionsNoms: String,
    val optionsPrixTotal: Int,
    val dateStr: String,
    val heure: String,
    val dureeTotaleMin: Int,
    val prixTotal: Int,
    val statut: String, // "CONFIRMEE", "ANNULEE"
    val modePaiement: String,
    val referencePaiement: String,
    val clientNom: String,
    val clientTelephone: String,
    val timestamp: Long = System.currentTimeMillis()
)

/**
 * Utilisateur administrateur du réseau (Super Admin ou Admin délégué).
 */
data class AdminUser(
    val email: String,
    val nom: String,
    val isSuperAdmin: Boolean = false,
    val dateAjout: String = "",
    val canManagePrices: Boolean = true,
    val canManageImages: Boolean = true
)

/**
 * Modèle de coiffure dans le catalogue / vitrine du réseau.
 */
data class HairstyleItem(
    val id: String,
    val titre: String,
    val categorie: String, // "Tresses", "Twists", "Soins", "Lissage", "Tissage", "Locks"
    val prixEstime: Int,
    val description: String,
    val imageResName: String = "img_coiffure_showcase",
    val salonNom: String = "Tous nos salons"
)

/**
 * Profil utilisateur du client du réseau de salons.
 */
data class UserProfile(
    val nom: String = "",
    val telephone: String = "",
    val email: String = "",
    val isRegistered: Boolean = false,
    val dateInscription: String = "",
    val salonPrefereId: String = "angre",
    val firebaseUid: String? = null,
    val authProvider: String = "local" // "firebase_email", "firebase_google", "local"
)
