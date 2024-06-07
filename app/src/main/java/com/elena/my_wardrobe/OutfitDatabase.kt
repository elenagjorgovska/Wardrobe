package com.elena.my_wardrobe

import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase

@Database(
    entities = [Outfit::class],
    version = 1,
    exportSchema = true,

)
abstract class OutfitDatabase : RoomDatabase() {

    abstract fun outfitDao() : OutfitDAO

    companion object {
        private var INSTANCE: OutfitDatabase? = null

        fun getInstance(context: Context): OutfitDatabase? {
            if (INSTANCE == null) {
                synchronized(OutfitDatabase::class) {
                    INSTANCE = Room.databaseBuilder(context.applicationContext,
                        OutfitDatabase::class.java, "recipes.db").allowMainThreadQueries()
                        .build()
                }
            }
            return INSTANCE
        }

        fun destroyInstance() {
            INSTANCE = null
        }

    }
}