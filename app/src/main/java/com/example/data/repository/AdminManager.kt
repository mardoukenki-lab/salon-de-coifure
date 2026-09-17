package com.example.data.repository

import android.content.Context
import android.content.SharedPreferences
import com.example.data.model.AdminUser
import com.example.data.model.HairstyleItem
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import java.util.UUID

/**
 * Gestionnaire persistant de l'espace administrateur du réseau de salons d'Abidjan.
 * - Enregistrement automatique du premier email comme Super Administrateur
 * - Limitation stricte à 3 administrateurs secondaires
 * - Gestion et mise à jour dynamique des tarifs et des visuels de coiffures
 */
class AdminManager(context: Context) {
    private val prefs: SharedPreferences =
        context.getSharedPreferences("salons_admin_prefs", Context.MODE_PRIVATE)

    companion object {
        private const val PREF_SUPER_ADMIN_EMAIL = "super_admin_email"
        private const val PREF_SUPER_ADMIN_NAME = "super_admin_name"
        private const val PREF_SUPER_ADMIN_DATE = "super_admin_date"
        private const val PREF_SECONDARY_ADMINS = "secondary_admins_data"
        private const val PREF_HAIRSTYLES_DATA = "hairstyles_catalogue_data"
        private const val PREFIX_SERVICE_PRICE = "service_price_"
        private const val PREFIX_OPTION_PRICE = "option_price_"
        const val MAX_SECONDARY_ADMINS = 3
    }

    /**
     * Vérifie si un Super Administrateur a déjà été initialisé.
     */
    fun isSuperAdminConfigured(): Boolean {
        return !prefs.getString(PREF_SUPER_ADMIN_EMAIL, null).isNullOrBlank()
    }

    /**
     * Retourne le Super Administrateur s'il existe.
     */
    fun getSuperAdmin(): AdminUser? {
        val email = prefs.getString(PREF_SUPER_ADMIN_EMAIL, null) ?: return null
        val name = prefs.getString(PREF_SUPER_ADMIN_NAME, "Super Administrateur") ?: "Super Administrateur"
        val date = prefs.getString(PREF_SUPER_ADMIN_DATE, "Initial") ?: "Initial"
        return AdminUser(
            email = email,
            nom = name,
            isSuperAdmin = true,
            dateAjout = date,
            canManagePrices = true,
            canManageImages = true
        )
    }

    /**
     * Tente de connecter un administrateur.
     * Si aucun Super Admin n'est configuré, le premier email devient automatiquement Super Admin.
     */
    fun login(email: String, nom: String = ""): Result<AdminUser> {
        val cleanEmail = email.trim().lowercase(Locale.ROOT)
        if (cleanEmail.isBlank()) {
            return Result.failure(IllegalArgumentException("Veuillez saisir une adresse email valide."))
        }

        val dateFormat = SimpleDateFormat("dd/MM/yyyy HH:mm", Locale.FRENCH)
        val todayStr = dateFormat.format(Date())

        if (!isSuperAdminConfigured()) {
            // Premier email à se connecter => devient automatiquement Super Admin !
            val adminNom = if (nom.isNotBlank()) nom.trim() else "Super Admin (${cleanEmail.substringBefore("@")})"
            prefs.edit()
                .putString(PREF_SUPER_ADMIN_EMAIL, cleanEmail)
                .putString(PREF_SUPER_ADMIN_NAME, adminNom)
                .putString(PREF_SUPER_ADMIN_DATE, todayStr)
                .apply()

            val superAdmin = AdminUser(
                email = cleanEmail,
                nom = adminNom,
                isSuperAdmin = true,
                dateAjout = todayStr,
                canManagePrices = true,
                canManageImages = true
            )
            return Result.success(superAdmin)
        }

        // Vérifier si c'est le Super Admin
        val superAdmin = getSuperAdmin()
        if (superAdmin != null && superAdmin.email.equals(cleanEmail, ignoreCase = true)) {
            return Result.success(superAdmin)
        }

        // Vérifier si c'est l'un des 3 administrateurs secondaires
        val secondaryAdmins = getSecondaryAdmins()
        val found = secondaryAdmins.find { it.email.equals(cleanEmail, ignoreCase = true) }
        if (found != null) {
            return Result.success(found)
        }

        val superEmail = superAdmin?.email ?: "le Super Admin"
        return Result.failure(
            IllegalAccessException(
                "Accès refusé : L'adresse « $cleanEmail » n'est pas autorisée comme administrateur.\n" +
                "Seul le Super Admin ($superEmail) peut enregistrer jusqu'à 3 administrateurs secondaires."
            )
        )
    }

    /**
     * Retourne la liste des administrateurs secondaires autorisés (maximum 3).
     */
    fun getSecondaryAdmins(): List<AdminUser> {
        val raw = prefs.getString(PREF_SECONDARY_ADMINS, "") ?: ""
        if (raw.isBlank()) return emptyList()

        return raw.split(";;;").mapNotNull { entry ->
            val parts = entry.split("|||")
            if (parts.size >= 3) {
                AdminUser(
                    nom = parts[0],
                    email = parts[1].lowercase(Locale.ROOT),
                    isSuperAdmin = false,
                    dateAjout = parts[2],
                    canManagePrices = true,
                    canManageImages = true
                )
            } else null
        }
    }

    /**
     * Ajoute un administrateur secondaire (limité à 3).
     */
    fun addSecondaryAdmin(nom: String, email: String): Result<AdminUser> {
        val cleanEmail = email.trim().lowercase(Locale.ROOT)
        val cleanNom = nom.trim().ifBlank { "Admin ${cleanEmail.substringBefore("@")}" }

        if (cleanEmail.isBlank()) {
            return Result.failure(IllegalArgumentException("L'adresse email est requise."))
        }

        val superAdmin = getSuperAdmin()
        if (superAdmin != null && superAdmin.email.equals(cleanEmail, ignoreCase = true)) {
            return Result.failure(IllegalArgumentException("Cette adresse email est déjà celle du Super Administrateur."))
        }

        val currentAdmins = getSecondaryAdmins().toMutableList()
        if (currentAdmins.size >= MAX_SECONDARY_ADMINS) {
            return Result.failure(
                IllegalStateException("Quota maximum atteint : Vous ne pouvez pas ajouter plus de $MAX_SECONDARY_ADMINS administrateurs.")
            )
        }

        if (currentAdmins.any { it.email.equals(cleanEmail, ignoreCase = true) }) {
            return Result.failure(IllegalArgumentException("Cet administrateur est déjà enregistré."))
        }

        val dateFormat = SimpleDateFormat("dd/MM/yyyy", Locale.FRENCH)
        val newAdmin = AdminUser(
            nom = cleanNom,
            email = cleanEmail,
            isSuperAdmin = false,
            dateAjout = dateFormat.format(Date()),
            canManagePrices = true,
            canManageImages = true
        )

        currentAdmins.add(newAdmin)
        saveSecondaryAdmins(currentAdmins)
        return Result.success(newAdmin)
    }

    /**
     * Supprime un administrateur secondaire pour libérer un créneau d'admin.
     */
    fun removeSecondaryAdmin(email: String): Boolean {
        val current = getSecondaryAdmins().filterNot { it.email.equals(email.trim(), ignoreCase = true) }
        saveSecondaryAdmins(current)
        return true
    }

    private fun saveSecondaryAdmins(admins: List<AdminUser>) {
        val serialized = admins.joinToString(";;;") { "${it.nom}|||${it.email}|||${it.dateAjout}" }
        prefs.edit().putString(PREF_SECONDARY_ADMINS, serialized).apply()
    }

    // ==========================================
    // GESTION DES TARIFS (SERVICES & OPTIONS)
    // ==========================================

    fun getServicePrice(serviceId: String, defaultPrice: Int): Int {
        return prefs.getInt(PREFIX_SERVICE_PRICE + serviceId, defaultPrice)
    }

    fun setServicePrice(serviceId: String, newPrice: Int) {
        prefs.edit().putInt(PREFIX_SERVICE_PRICE + serviceId, newPrice).apply()
    }

    fun getOptionPrice(optionId: String, defaultPrice: Int): Int {
        return prefs.getInt(PREFIX_OPTION_PRICE + optionId, defaultPrice)
    }

    fun setOptionPrice(optionId: String, newPrice: Int) {
        prefs.edit().putInt(PREFIX_OPTION_PRICE + optionId, newPrice).apply()
    }

    fun resetAllPrices() {
        val editor = prefs.edit()
        prefs.all.keys.forEach { key ->
            if (key.startsWith(PREFIX_SERVICE_PRICE) || key.startsWith(PREFIX_OPTION_PRICE)) {
                editor.remove(key)
            }
        }
        editor.apply()
    }

    // ==========================================
    // GESTION DES COIFFURES & IMAGES
    // ==========================================

    fun getHairstyles(): List<HairstyleItem> {
        val raw = prefs.getString(PREF_HAIRSTYLES_DATA, null)
        if (raw.isNullOrBlank()) {
            val defaults = getDefaultHairstyles()
            saveHairstyles(defaults)
            return defaults
        }

        return raw.split(";;;").mapNotNull { entry ->
            val parts = entry.split("|||")
            if (parts.size >= 6) {
                HairstyleItem(
                    id = parts[0],
                    titre = parts[1],
                    categorie = parts[2],
                    prixEstime = parts[3].toIntOrNull() ?: 8000,
                    description = parts[4],
                    imageResName = parts[5],
                    salonNom = if (parts.size >= 7) parts[6] else "Tous nos salons"
                )
            } else null
        }
    }

    fun addHairstyle(item: HairstyleItem) {
        val list = getHairstyles().toMutableList()
        list.add(0, item) // Ajouter en début de vitrine
        saveHairstyles(list)
    }

    fun deleteHairstyle(id: String) {
        val list = getHairstyles().filterNot { it.id == id }
        saveHairstyles(list)
    }

    private fun saveHairstyles(items: List<HairstyleItem>) {
        val serialized = items.joinToString(";;;") {
            "${it.id}|||${it.titre}|||${it.categorie}|||${it.prixEstime}|||${it.description}|||${it.imageResName}|||${it.salonNom}"
        }
        prefs.edit().putString(PREF_HAIRSTYLES_DATA, serialized).apply()
    }

    private fun getDefaultHairstyles(): List<HairstyleItem> {
        return listOf(
            HairstyleItem(
                id = "hs_knotless",
                titre = "Knotless Braids Bohème",
                categorie = "Tresses",
                prixEstime = 8000,
                description = "Nattes sans nœuds ultra légères avec mèches ondulées aux pointes.",
                imageResName = "img_coiffure_showcase",
                salonNom = "Yopougon & Angré"
            ),
            HairstyleItem(
                id = "hs_twists",
                titre = "Passion Twists Caramel",
                categorie = "Twists",
                prixEstime = 6000,
                description = "Vanilles soyeuses et volumineuses aux reflets chauds et légers.",
                imageResName = "img_salon_hero",
                salonNom = "Treichville 13ème"
            ),
            HairstyleItem(
                id = "hs_locks",
                titre = "Départ & Entretien Locks",
                categorie = "Locks",
                prixEstime = 8500,
                description = "Tournage au gel d'aloe vera pur et resserrage durable des repousses.",
                imageResName = "img_salon_hero",
                salonNom = "Angré & Yopougon"
            ),
            HairstyleItem(
                id = "hs_spa",
                titre = "Soin Spa Karité & Vapeur",
                categorie = "Soins",
                prixEstime = 5000,
                description = "Bain de vapeur ozonée, massage cuir chevelu et masque réparateur.",
                imageResName = "img_coiffure_showcase",
                salonNom = "Tous nos salons"
            ),
            HairstyleItem(
                id = "hs_perruque",
                titre = "Pose Perruque HD Lace",
                categorie = "Tissage",
                prixEstime = 10000,
                description = "Customisation frontale invisible et collage hypoallergénique protecteur.",
                imageResName = "img_coiffure_showcase",
                salonNom = "Espace VIP Angré"
            )
        )
    }
}
