package com.elena.my_wardrobe

import android.content.Context
import android.os.AsyncTask
import android.util.Log

class OutfitRepository(context: Context) {
    var db: OutfitDAO = OutfitDatabase.getInstance(context)?.outfitDao()!!


    //Fetch All the Users
    fun getAllOutfits(): List<Outfit> {
        return db.gelAllOutfit()
    }

    // Insert new user
    fun addOutfit(outfit: Outfit) {
        insertAsyncTask(db).execute(outfit)
        Log.e("ROOM", outfit.toString())
    }

    // update user
    fun updateOutfit(outfit: Outfit) {
        db.updateOutfit(outfit)
    }

    // Delete user
    fun deleteOutfit() {// outfit: Outfit
//        db.deleteOutfit(outfit)
        db.delete()
    }

    private class insertAsyncTask internal constructor(private val outfit: OutfitDAO) :
        AsyncTask<Outfit, Void, Void>() {

        override fun doInBackground(vararg params: Outfit): Void? {
            outfit.insertOutfit(params[0])
            return null
        }
    }
}