package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.UserProfile

/**
 * Gestionnaire de persistance locale du profil utilisateur / client.
 */
class UserManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("salons_user_profile_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val KEY_NOM = "user_nom"
        private const val KEY_TELEPHONE = "user_telephone"
        private const val KEY_EMAIL = "user_email"
        private const val KEY_IS_REGISTERED = "user_is_registered"
        private const val KEY_DATE_INSCRIPTION = "user_date_inscription"
        private const val KEY_SALON_PREFERE = "user_salon_prefere"
        private const val KEY_FIREBASE_UID = "user_firebase_uid"
        private const val KEY_AUTH_PROVIDER = "user_auth_provider"

        const val DEFAULT_SALON_PREFERE = "angre"
    }

    /**
     * Charge le profil utilisateur enregistré. Par défaut, le client n'est pas encore inscrit.
     */
    fun getUserProfile(): UserProfile {
        val nom = prefs.getString(KEY_NOM, "") ?: ""
        val telephone = prefs.getString(KEY_TELEPHONE, "") ?: ""
        val email = prefs.getString(KEY_EMAIL, "") ?: ""
        val isRegistered = prefs.getBoolean(KEY_IS_REGISTERED, false)
        val dateInscription = prefs.getString(KEY_DATE_INSCRIPTION, "") ?: ""
        val salonPrefere = prefs.getString(KEY_SALON_PREFERE, DEFAULT_SALON_PREFERE) ?: DEFAULT_SALON_PREFERE
        val firebaseUid = prefs.getString(KEY_FIREBASE_UID, null)
        val authProvider = prefs.getString(KEY_AUTH_PROVIDER, "local") ?: "local"

        return UserProfile(
            nom = nom,
            telephone = telephone,
            email = email,
            isRegistered = isRegistered,
            dateInscription = dateInscription,
            salonPrefereId = salonPrefere,
            firebaseUid = firebaseUid,
            authProvider = authProvider
        )
    }

    /**
     * Enregistre ou inscrit un nouveau profil client.
     */
    fun registerUserProfile(
        nom: String,
        telephone: String,
        email: String,
        salonPrefereId: String = DEFAULT_SALON_PREFERE,
        firebaseUid: String? = null,
        authProvider: String = "local",
        dateInscription: String = "19/09/2026"
    ): UserProfile {
        prefs.edit()
            .putString(KEY_NOM, nom.trim())
            .putString(KEY_TELEPHONE, telephone.trim())
            .putString(KEY_EMAIL, email.trim())
            .putString(KEY_SALON_PREFERE, salonPrefereId)
            .putString(KEY_DATE_INSCRIPTION, dateInscription)
            .putString(KEY_FIREBASE_UID, firebaseUid)
            .putString(KEY_AUTH_PROVIDER, authProvider)
            .putBoolean(KEY_IS_REGISTERED, true)
            .apply()

        return getUserProfile()
    }

    /**
     * Met à jour les informations de base du compte utilisateur.
     */
    fun saveUserProfile(
        nom: String,
        telephone: String,
        email: String,
        salonPrefereId: String = DEFAULT_SALON_PREFERE
    ): UserProfile {
        val current = getUserProfile()
        val dateInscription = if (current.dateInscription.isNotBlank()) current.dateInscription else "19/09/2026"
        prefs.edit()
            .putString(KEY_NOM, nom.trim())
            .putString(KEY_TELEPHONE, telephone.trim())
            .putString(KEY_EMAIL, email.trim())
            .putString(KEY_SALON_PREFERE, salonPrefereId)
            .putString(KEY_DATE_INSCRIPTION, dateInscription)
            .putBoolean(KEY_IS_REGISTERED, true)
            .apply()

        return getUserProfile()
    }

    /**
     * Réinitialise ou déconnecte le profil.
     */
    fun resetUserProfile(): UserProfile {
        prefs.edit()
            .putString(KEY_NOM, "")
            .putString(KEY_TELEPHONE, "")
            .putString(KEY_EMAIL, "")
            .putString(KEY_FIREBASE_UID, null)
            .putString(KEY_AUTH_PROVIDER, "local")
            .putString(KEY_DATE_INSCRIPTION, "")
            .putBoolean(KEY_IS_REGISTERED, false)
            .apply()

        return UserProfile(
            nom = "",
            telephone = "",
            email = "",
            isRegistered = false,
            dateInscription = "",
            salonPrefereId = DEFAULT_SALON_PREFERE,
            firebaseUid = null,
            authProvider = "local"
        )
    }
}
