package com.elena.my_wardrobe.ui.dashboard

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.lifecycle.ViewModelProvider
import com.elena.my_wardrobe.MainActivity
import com.elena.mywardrobe.databinding.FragmentAddBinding
import com.elena.my_wardrobe.ui.home.HomeFragment
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class AddFragment : Fragment() {

    private var _binding: FragmentAddBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _binding!!
    var title: String = ""
    var description:String = ""
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View {
        val dashboardViewModel =
            ViewModelProvider(this).get(AddViewModel::class.java)

        _binding = FragmentAddBinding.inflate(inflater, container, false)
        val root: View = binding.root

        binding.title.addTextChangedListener (object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                title = s.toString()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })
        binding.description.addTextChangedListener (object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(s: CharSequence, start: Int,
                                       before: Int, count: Int) {
                description = s.toString()
            }

            override fun afterTextChanged(p0: Editable?) {}
        })

        binding.addOutfit.setOnClickListener{
            createOutfit()
            (activity as MainActivity).loadFragment(HomeFragment())
        }
//
        return root
    }

    fun createOutfit()
    {
        val db = Firebase.firestore
        // Create a new user with a first and last name
        val outfit = hashMapOf(
            "Description" to description,
            "Title" to title,

        )

        // Add a new document with a generated ID
        db.collection("Outfit")
            .add(outfit)
            .addOnSuccessListener { documentReference ->
                Log.d("Firebase", "DocumentSnapshot added with ID: ${documentReference.id}")
            }
            .addOnFailureListener { e ->
                Log.w("Firebase", "Error adding document", e)
            }
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}