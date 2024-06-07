package com.elena.my_wardrobe

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import com.elena.mywardrobe.databinding.ActivityOutfitViewBinding


class OutfitView : AppCompatActivity() {

    private lateinit var binding: ActivityOutfitViewBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOutfitViewBinding.inflate(layoutInflater)
        setContentView(binding.root)

        Log.e("Current Outfit", GlobalData.currentOutfit.toString())

        binding.outfitName.text = GlobalData.currentOutfit?.rName!!
        binding.outfitDesc.text = GlobalData.currentOutfit?.rDesc!!
        binding.outfitFav.setOnClickListener {
            val repo = OutfitRepository(binding.root.context)
            val outfit = GlobalData.currentOutfit
            if (outfit != null) {
                repo.addOutfit(outfit = Outfit(1, "My outfit", "This is very good outfit"))
            }
        }
    }
}