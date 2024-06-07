package com.elena.my_wardrobe.ui.home

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.elena.mywardrobe.databinding.FragmentHomeBinding
import com.elena.my_wardrobe.ui.adapters.OutfitListAdapter
import com.elena.my_wardrobe.ui.adapters.OutfitModel
import android.util.Log
import com.example.nazdravje.ui.home.HomeViewModel
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class HomeFragment : Fragment() {

    private lateinit var adapter: OutfitListAdapter
    private lateinit var user :ArrayList<OutfitModel>
    private var _binding: FragmentHomeBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        ViewModelProvider(this).get(HomeViewModel::class.java)

        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        val root: View = binding.root

//        val textView: TextView = binding.textHome
//        homeViewModel.text.observe(viewLifecycleOwner) {
//            textView.text = it
//        }
        userList()
        readOutfit()


        return root
    }

    private fun userList(){
        user = arrayListOf(


        )
    }



    private fun readOutfit()
    {
        val db = Firebase.firestore

        val recipes = arrayListOf<OutfitModel>()


        db.collection("Outfit")
            .get()
            .addOnSuccessListener { result ->
                for (document in result) {
                    Log.d("Firebase", "${document.id} => ${document.data}")

                    val current = OutfitModel(
                    document.data["Title"].toString(),
                    document.data["Description"].toString(),
                    )
                    recipes.add(current)
                }
                binding.outfitList.layoutManager = LinearLayoutManager(this.context)
                adapter = OutfitListAdapter(recipes)
                binding.outfitList.adapter = adapter
            }
            .addOnFailureListener { exception ->
                Log.w("Firebase", "Error getting documents.", exception)
            }

    }


    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}