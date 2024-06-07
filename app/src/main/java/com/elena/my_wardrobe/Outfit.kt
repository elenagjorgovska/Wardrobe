package com.elena.my_wardrobe
import androidx.room.Entity
import androidx.room.Index
import androidx.room.PrimaryKey


@Entity(tableName = "outfits", indices = [Index(value = ["rName"], unique = true)])
data class Outfit(
    @PrimaryKey(autoGenerate = true) var outfitID: Int? = null,
    var rName: String,
    val rDesc: String)
