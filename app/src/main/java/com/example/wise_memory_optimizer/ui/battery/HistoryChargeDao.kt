package com.example.wise_memory_optimizer.ui.battery

import androidx.room.*

@Dao
abstract class HistoryChargeDao {
    @Insert
    fun insert(vararg historyCharge: HistoryCharge?) {
    }

    @Update
    fun update(vararg historyCharge: HistoryCharge?) {
    }

    @Delete
    fun delete(historyCharge: HistoryCharge) {
    }

    @Query("SELECT * FROM HISTORYCHARGE")
    abstract fun getItems(): HistoryCharge?
}