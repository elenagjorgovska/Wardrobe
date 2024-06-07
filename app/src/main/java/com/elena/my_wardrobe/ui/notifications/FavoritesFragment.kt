package com.elena.my_wardrobe.ui.notifications

import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import com.elena.my_wardrobe.OutfitRepository
import com.elena.mywardrobe.databinding.FragmentSettingsBinding
import com.elena.my_wardrobe.ui.adapters.OutfitListAdapter
import com.elena.my_wardrobe.ui.adapters.OutfitModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FavoritesFragment : Fragment() {

    private var _binding: FragmentSettingsBinding? = null
    private lateinit var adapter: OutfitListAdapter
    val repo:OutfitRepository by lazy {
        OutfitRepository(requireContext())
    }

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val favoritesViewModel =
            ViewModelProvider(this)[FavoritesViewModel::class.java]

        _binding = FragmentSettingsBinding.inflate(inflater, container, false)
        val root: View = binding.root
        readOutfit()



        return root
    }


    private fun readOutfit()
    {


        val outfits = arrayListOf<OutfitModel>()

        val favorites = repo.getAllOutfits()

        favorites.forEach { outfit ->
            val current = OutfitModel(
                outfit.rName,
                outfit.rDesc)
            outfits.add(current)
        }
        binding.outfitFavorites.layoutManager = LinearLayoutManager(this.context)
        adapter = OutfitListAdapter(outfits)
        binding.outfitFavorites.adapter = adapter
//



    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}