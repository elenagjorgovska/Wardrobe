package com.elena.my_wardrobe

import com.google.firebase.auth.FirebaseUser

object GlobalData {

    var firebaseUser: FirebaseUser? = null
    var isAnonymous: Boolean = false

    var currentOutfit: Outfit? = null
}