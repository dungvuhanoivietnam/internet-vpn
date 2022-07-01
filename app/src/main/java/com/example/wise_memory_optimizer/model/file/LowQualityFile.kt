package com.example.wise_memory_optimizer.model.file

import java.io.File
import java.io.Serializable

data class LowQualityFile(var file: File?, var dateTime: String, var isSelected: Boolean = false) : Serializable