package br.com.camargo.vinicius.employees.firebase

import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class FirebaseHelper {
    companion object {
        fun getDatabase() = Firebase.firestore
    }
}