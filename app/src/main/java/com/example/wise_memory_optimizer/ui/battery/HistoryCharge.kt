package com.example.wise_memory_optimizer.ui.battery

import androidx.annotation.NonNull
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "historyCharge")
class HistoryCharge() {
    @PrimaryKey
    @NonNull
    var id: Long? = null
    var statusLevel: Int? = null

}