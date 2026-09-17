package com.example.data.db

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.data.model.ReservationEntity
import kotlinx.coroutines.flow.Flow

@Dao
interface ReservationDao {
    @Query("SELECT * FROM reservations ORDER BY id DESC")
    fun getAllReservations(): Flow<List<ReservationEntity>>

    @Query("SELECT * FROM reservations WHERE id = :id LIMIT 1")
    suspend fun getReservationById(id: Int): ReservationEntity?

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertReservation(reservation: ReservationEntity): Long

    @Query("UPDATE reservations SET statut = :nouveauStatut WHERE id = :id")
    suspend fun updateReservationStatus(id: Int, nouveauStatut: String)

    @Query("DELETE FROM reservations WHERE id = :id")
    suspend fun deleteReservation(id: Int)
}
