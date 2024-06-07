package com.elena.my_wardrobe

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import androidx.room.Update

@Dao
    interface OutfitDAO {

        @Insert(onConflict = OnConflictStrategy.REPLACE)
        fun insertOutfit(outfit: Outfit)

        @Query("Select * from outfits")
        fun gelAllOutfit(): List<Outfit>

        @Update
        fun updateOutfit(outfit: Outfit)

//        @Delete
//        fun deleteOutfit(outfit: Outfit)
        @Query("DELETE FROM outfits")
        open fun delete()
    }
