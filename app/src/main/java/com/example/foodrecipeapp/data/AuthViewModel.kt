package com.example.foodrecipeapp.data

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.security.crypto.EncryptedSharedPreferences
import androidx.security.crypto.MasterKeys
import com.google.firebase.firestore.ListenerRegistration
import com.google.firebase.firestore.ktx.toObject
import com.google.firebase.firestore.ktx.toObjects
import kotlinx.coroutines.tasks.await

class AuthViewModel : ViewModel() {

    private val userLiveData = MutableLiveData<Admin?>()
    private var listener: ListenerRegistration? = null

    // Remove snapshot listener when view model is destroyed
    override fun onCleared() {
        super.onCleared()
        listener?.remove()
    }

    // Return observable live data
    fun getUserLiveData(): LiveData<Admin?> {
        return userLiveData
    }

    // Return user from live data
    fun getUser(): Admin? {
        return userLiveData.value
    }

    // TODO(1): Login
    suspend fun login(ctx: Context, email: String, password: String, remember: Boolean = false): Boolean {
        // TODO(1A): Get the user record with matching email + password
        //           Return false is no matching found
        val customer = ADMIN
            .whereEqualTo("email", email)
            .whereEqualTo("password",password)
            .get()
            .await()
            .toObjects<Admin>()
            .firstOrNull() ?: return false

        // TODO(1B): Setup snapshot listener
        //           Update live data -> user
        listener?.remove()
        listener = ADMIN.document(customer.id).addSnapshotListener{doc, _ ->
            userLiveData.value = doc?.toObject()
        }


        // TODO(6A): Handle remember-me -> add shared preferences
        if(remember){
            getPreferences(ctx)
                .edit()
                .putString("email", email)
                .putString("password", password)
                .apply()
        }

        return true
    }

    // TODO(2): Logout
    fun logout(ctx: Context) {
        // TODO(2A): Remove snapshot listener
        //           Update live data -> null
        listener?.remove()
        userLiveData.value = null


        // TODO(6B): Handle remember-me -> clear shared preferences
        getPreferences(ctx)
            .edit()
            //.remove("email") //if want to remove individually
            //.remove("password")
            .clear()
            .apply()


        //[OR] ctx.deleteSharedPreferences("AUTH")
    }

    // TODO(6): Get shared preferences
    private fun getPreferences(ctx: Context): SharedPreferences {
        // return ctx.getSharedPreferences("AUTH", Context.MODE_PRIVATE)
        return EncryptedSharedPreferences.create(
            "AUTH",
            MasterKeys.getOrCreate(MasterKeys.AES256_GCM_SPEC),
            ctx,
            EncryptedSharedPreferences.PrefKeyEncryptionScheme.AES256_SIV,
            EncryptedSharedPreferences.PrefValueEncryptionScheme.AES256_GCM
        )
    }

    // TODO(7): Auto login from shared preferences
    suspend fun loginFromPreferences(ctx: Context) {
        val p = getPreferences(ctx)
        val email = p.getString("email", null)
        val password = p.getString("password", null)

        if(email != null && password != null){
            login(ctx, email, password)
        }
    }

}