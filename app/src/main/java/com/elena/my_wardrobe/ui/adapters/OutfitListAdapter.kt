package com.elena.my_wardrobe.ui.adapters

import android.annotation.SuppressLint
import android.content.Intent
import android.view.LayoutInflater
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.elena.my_wardrobe.GlobalData
import com.elena.my_wardrobe.Outfit
import com.elena.my_wardrobe.OutfitView
import com.elena.my_wardrobe.OutfitRepository
import  com.elena.mywardrobe.databinding.OutfitCardBinding
import com.google.firebase.analytics.FirebaseAnalytics
import com.google.firebase.analytics.ktx.analytics
import com.google.firebase.analytics.ktx.logEvent
import com.google.firebase.ktx.Firebase

import java.util.ArrayList

class OutfitListAdapter(private var dataSet: ArrayList<OutfitModel>):

    RecyclerView.Adapter<OutfitListAdapter.OutfitHolder>() {
    class OutfitHolder(val binding: OutfitCardBinding) : RecyclerView.ViewHolder(binding.root) {
        @SuppressLint("InvalidAnalyticsName")
        fun bind(get: OutfitModel) {

            val repo = OutfitRepository(binding.root.context)
            binding.outfitName.text = get.rName
            binding.outfitDesc.text = get.rDesc
            binding.outfitFav.setOnClickListener {
                val outfit = Outfit(rName = get.rName, rDesc = get.rDesc)
                repo.addOutfit(outfit = outfit)
            }
            binding.cardBg.setOnClickListener {
                GlobalData.currentOutfit = Outfit(1,get.rName,get.rDesc)

                val intent: Intent = Intent(binding.root.context, OutfitView::class.java)
                binding.root.context.startActivity(intent)

                var firebaseAnalytics: FirebaseAnalytics = Firebase.analytics
                firebaseAnalytics.logEvent("outfit_opened") {

                    param("Outfit", "r:" + get.rName)

                }

            }

        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): OutfitHolder {
        val binding = OutfitCardBinding
            .inflate(LayoutInflater.from(parent.context), parent, false)
        return OutfitHolder(binding)
    }

    override fun onBindViewHolder(holder: OutfitHolder, position: Int) {
        holder.bind(dataSet.get(position))
    }

    override fun getItemCount(): Int {
        return dataSet.size
    }
    fun setRecipies(users: ArrayList<OutfitModel>) {
        this.dataSet = users
        notifyDataSetChanged()
    }
}