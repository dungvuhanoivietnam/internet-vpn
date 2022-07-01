package com.example.wise_memory_optimizer.ui.battery

import androidx.room.Database
import androidx.room.DatabaseConfiguration
import androidx.room.InvalidationTracker
import androidx.room.RoomDatabase
import androidx.sqlite.db.SupportSQLiteOpenHelper

@Database(entities = [HistoryCharge::class], version = 1)
abstract class AppDataBase : RoomDatabase() {
    abstract fun historyChargeDao(): HistoryChargeDao
}