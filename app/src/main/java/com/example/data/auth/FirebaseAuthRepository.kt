package com.example.data.auth

import android.content.Context
import android.util.Log
import androidx.credentials.CredentialManager
import androidx.credentials.CustomCredential
import androidx.credentials.GetCredentialRequest
import androidx.credentials.exceptions.GetCredentialException
import com.example.data.model.ReservationEntity
import com.example.data.model.UserProfile
import com.google.android.libraries.identity.googleid.GetGoogleIdOption
import com.google.android.libraries.identity.googleid.GoogleIdTokenCredential
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.UserProfileChangeRequest
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.SetOptions
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.suspendCancellableCoroutine
import kotlinx.coroutines.withContext
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale
import kotlin.coroutines.resume

/**
 * Gestionnaire d'authentification Firebase Auth & persistance cloud Firestore.
 * S'exécute avec gestion gracieuse du mode hors-ligne / fallback local.
 */
class FirebaseAuthRepository(private val context: Context) {

    private val auth: FirebaseAuth? = try {
        FirebaseAuth.getInstance()
    } catch (e: Exception) {
        Log.w(TAG, "Firebase Auth non initialisé : ${e.message}")
        null
    }

    private val firestore: FirebaseFirestore? = try {
        FirebaseFirestore.getInstance()
    } catch (e: Exception) {
        Log.w(TAG, "Firestore non initialisé : ${e.message}")
        null
    }

    private val credentialManager: CredentialManager by lazy {
        CredentialManager.create(context)
    }

    companion object {
        private const val TAG = "FirebaseAuthRepository"
    }

    val isFirebaseAvailable: Boolean
        get() = auth != null

    val isFirestoreAvailable: Boolean
        get() = firestore != null

    val currentFirebaseUser: FirebaseUser?
        get() = auth?.currentUser

    /**
     * Inscription d'un nouveau client avec Email et Mot de passe via Firebase Auth.
     */
    suspend fun registerWithEmailAndPassword(
        nom: String,
        email: String,
        password: String,
        telephone: String,
        salonPrefereId: String
    ): Result<UserProfile> = withContext(Dispatchers.IO) {
        val firebaseAuth = auth
        val todayStr = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(Date())

        if (firebaseAuth == null) {
            // Mode local si Firebase n'est pas lié à un projet Google Services
            val profile = UserProfile(
                nom = nom,
                email = email,
                telephone = telephone,
                isRegistered = true,
                dateInscription = todayStr,
                salonPrefereId = salonPrefereId,
                firebaseUid = "local_${System.currentTimeMillis()}",
                authProvider = "local"
            )
            return@withContext Result.success(profile)
        }

        try {
            val authResult = firebaseAuth.createUserWithEmailAndPassword(email.trim(), password).awaitTask()
            val user = authResult.user

            if (user != null) {
                // Mettre à jour le nom affiché dans Firebase
                try {
                    val profileUpdate = UserProfileChangeRequest.Builder()
                        .setDisplayName(nom.trim())
                        .build()
                    user.updateProfile(profileUpdate).awaitTask()
                } catch (e: Exception) {
                    Log.w(TAG, "Impossible de mettre à jour le display name : ${e.message}")
                }

                val profile = UserProfile(
                    nom = nom.trim(),
                    email = email.trim(),
                    telephone = telephone.trim(),
                    isRegistered = true,
                    dateInscription = todayStr,
                    salonPrefereId = salonPrefereId,
                    firebaseUid = user.uid,
                    authProvider = "firebase_email"
                )

                // Sauvegarder dans Firestore
                syncUserToFirestore(profile)

                Result.success(profile)
            } else {
                Result.failure(Exception("Échec de création du compte client."))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Erreur inscription Firebase: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * Connexion d'un client existant avec Email et Mot de passe.
     */
    suspend fun signInWithEmailAndPassword(
        email: String,
        password: String
    ): Result<UserProfile> = withContext(Dispatchers.IO) {
        val firebaseAuth = auth
        if (firebaseAuth == null) {
            return@withContext Result.failure(Exception("Service Firebase non disponible."))
        }

        try {
            val authResult = firebaseAuth.signInWithEmailAndPassword(email.trim(), password).awaitTask()
            val user = authResult.user

            if (user != null) {
                // Récupérer le profil depuis Firestore si disponible
                val firestoreProfile = fetchUserFromFirestore(user.uid)
                val profile = firestoreProfile ?: UserProfile(
                    nom = user.displayName ?: user.email?.substringBefore("@") ?: "Client",
                    email = user.email ?: email,
                    telephone = "",
                    isRegistered = true,
                    dateInscription = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(Date()),
                    salonPrefereId = "angre",
                    firebaseUid = user.uid,
                    authProvider = "firebase_email"
                )
                Result.success(profile)
            } else {
                Result.failure(Exception("Utilisateur non trouvé."))
            }
        } catch (e: Exception) {
            Log.e(TAG, "Erreur connexion email: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * Connexion avec Google via Credential Manager & Firebase Auth.
     */
    suspend fun signInWithGoogle(
        activityContext: Context,
        serverClientId: String? = null
    ): Result<UserProfile> = withContext(Dispatchers.IO) {
        val firebaseAuth = auth
        if (firebaseAuth == null) {
            return@withContext Result.failure(Exception("Firebase Auth n'est pas initialisé."))
        }

        try {
            val googleIdOptionBuilder = GetGoogleIdOption.Builder()
                .setAutoSelectEnabled(false)

            if (!serverClientId.isNullOrBlank()) {
                googleIdOptionBuilder.setServerClientId(serverClientId)
            } else {
                // Fallback direct option
                googleIdOptionBuilder.setFilterByAuthorizedAccounts(false)
            }

            val request = GetCredentialRequest.Builder()
                .addCredentialOption(googleIdOptionBuilder.build())
                .build()

            val result = credentialManager.getCredential(
                request = request,
                context = activityContext
            )

            val credential = result.credential
            if (credential is CustomCredential && credential.type == GoogleIdTokenCredential.TYPE_GOOGLE_ID_TOKEN_CREDENTIAL) {
                val googleIdTokenCredential = GoogleIdTokenCredential.createFrom(credential.data)
                val idToken = googleIdTokenCredential.idToken

                val firebaseCredential = GoogleAuthProvider.getCredential(idToken, null)
                val authResult = firebaseAuth.signInWithCredential(firebaseCredential).awaitTask()
                val user = authResult.user

                if (user != null) {
                    val todayStr = SimpleDateFormat("dd/MM/yyyy", Locale.FRANCE).format(Date())
                    val existing = fetchUserFromFirestore(user.uid)
                    val profile = existing ?: UserProfile(
                        nom = user.displayName ?: "Client Google",
                        email = user.email ?: "",
                        telephone = user.phoneNumber ?: "",
                        isRegistered = true,
                        dateInscription = todayStr,
                        salonPrefereId = "angre",
                        firebaseUid = user.uid,
                        authProvider = "firebase_google"
                    )

                    syncUserToFirestore(profile)
                    Result.success(profile)
                } else {
                    Result.failure(Exception("Compte Google introuvable."))
                }
            } else {
                Result.failure(Exception("Identifiant Google non reconnu."))
            }
        } catch (e: GetCredentialException) {
            Log.w(TAG, "Annulation ou échec Google Credential: ${e.message}")
            Result.failure(e)
        } catch (e: Exception) {
            Log.e(TAG, "Erreur connexion Google: ${e.message}", e)
            Result.failure(e)
        }
    }

    /**
     * Synchronise le profil utilisateur vers Firestore.
     */
    suspend fun syncUserToFirestore(profile: UserProfile) = withContext(Dispatchers.IO) {
        val db = firestore ?: return@withContext
        val uid = profile.firebaseUid ?: return@withContext

        try {
            val userMap = hashMapOf(
                "uid" to uid,
                "nom" to profile.nom,
                "email" to profile.email,
                "telephone" to profile.telephone,
                "salonPrefereId" to profile.salonPrefereId,
                "dateInscription" to profile.dateInscription,
                "authProvider" to profile.authProvider,
                "lastUpdate" to System.currentTimeMillis()
            )
            db.collection("users").document(uid).set(userMap, SetOptions.merge()).awaitTask()
            Log.d(TAG, "Profil synchronisé avec succès sur Firestore.")
        } catch (e: Exception) {
            Log.w(TAG, "Erreur de synchronisation Firestore user: ${e.message}")
        }
    }

    /**
     * Récupère un profil utilisateur depuis Firestore.
     */
    private suspend fun fetchUserFromFirestore(uid: String): UserProfile? = withContext(Dispatchers.IO) {
        val db = firestore ?: return@withContext null
        try {
            val doc = db.collection("users").document(uid).get().awaitTask()
            if (doc != null && doc.exists()) {
                UserProfile(
                    nom = doc.getString("nom") ?: "",
                    email = doc.getString("email") ?: "",
                    telephone = doc.getString("telephone") ?: "",
                    isRegistered = true,
                    dateInscription = doc.getString("dateInscription") ?: "",
                    salonPrefereId = doc.getString("salonPrefereId") ?: "angre",
                    firebaseUid = uid,
                    authProvider = doc.getString("authProvider") ?: "firebase"
                )
            } else {
                null
            }
        } catch (e: Exception) {
            Log.w(TAG, "Impossible de charger l'utilisateur Firestore: ${e.message}")
            null
        }
    }

    /**
     * Sauvegarde une réservation dans Firestore pour synchronisation cloud.
     */
    suspend fun syncReservationToFirestore(reservation: ReservationEntity) = withContext(Dispatchers.IO) {
        val db = firestore ?: return@withContext
        try {
            val resMap = hashMapOf(
                "codeReservation" to reservation.codeReservation,
                "salonId" to reservation.salonId,
                "salonNom" to reservation.salonNom,
                "salonQuartier" to reservation.salonQuartier,
                "salonAdresse" to reservation.salonAdresse,
                "serviceId" to reservation.serviceId,
                "serviceNom" to reservation.serviceNom,
                "optionsNoms" to reservation.optionsNoms,
                "optionsPrixTotal" to reservation.optionsPrixTotal,
                "dateStr" to reservation.dateStr,
                "heure" to reservation.heure,
                "dureeTotaleMin" to reservation.dureeTotaleMin,
                "prixTotal" to reservation.prixTotal,
                "statut" to reservation.statut,
                "modePaiement" to reservation.modePaiement,
                "referencePaiement" to reservation.referencePaiement,
                "clientNom" to reservation.clientNom,
                "clientTelephone" to reservation.clientTelephone,
                "timestamp" to reservation.timestamp
            )
            db.collection("reservations").document(reservation.codeReservation).set(resMap).awaitTask()
            Log.d(TAG, "Réservation ${reservation.codeReservation} sauvegardée sur Firestore.")
        } catch (e: Exception) {
            Log.w(TAG, "Erreur de synchronisation réservation Firestore: ${e.message}")
        }
    }

    /**
     * Déconnexion Firebase Auth.
     */
    fun signOut() {
        try {
            auth?.signOut()
        } catch (e: Exception) {
            Log.w(TAG, "Erreur signOut: ${e.message}")
        }
    }
}

/**
 * Extension d'attente asynchrone non-bloquante pour les Task Google / Firebase.
 */
private suspend fun <T> com.google.android.gms.tasks.Task<T>.awaitTask(): T =
    suspendCancellableCoroutine { cont ->
        addOnSuccessListener { result ->
            if (cont.isActive) cont.resume(result)
        }
        addOnFailureListener { exception ->
            if (cont.isActive) cont.resumeWith(Result.failure(exception))
        }
        addOnCanceledListener {
            if (cont.isActive) cont.cancel()
        }
    }
