package com.example.navigatour

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.navigatour.databinding.ActivityRegistrationBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase


private const val TAG = "RegistrationActivity"


class RegistrationActivity : AppCompatActivity() {
    private lateinit var binding: ActivityRegistrationBinding
    private lateinit var userPassword: String
    private lateinit var userEmail: String
    private lateinit var userUsername: String
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth
        super.onCreate(savedInstanceState)

        binding = ActivityRegistrationBinding.inflate(layoutInflater)
        setContentView(binding.root)

        val user = Firebase.auth.currentUser
        if (user != null) {
            // User is signed in
            Log.d("sign up page", "no user signed in")
        } else {
            // No user is signed in
            Log.d("sign up page", "user signed in")

        }

        binding.signUpButton.setOnClickListener{
            userPassword = binding.registrationPassword.text.toString()
            userEmail = binding.registrationEmail.text.toString()
            userUsername = binding.registrationUsername.text.toString()
            if(userPassword.isNotEmpty() && userEmail.isNotEmpty() && userUsername.isNotEmpty()){
                signUpUser(userEmail, userPassword)
            }else{
                Toast.makeText(baseContext, "One or more fields are empty, " +
                        "ensure that all fields all fill",
                    Toast.LENGTH_SHORT).show()
            }
        }

        binding.alreadyHasAccount.setOnClickListener{
            val intent = Intent(this, LoginActivity::class.java)
            startActivity(intent)
        }


    }

    private fun signUpUser(email: String, password: String){
        auth.createUserWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "createUserWithEmail:success")
                    val user = auth.currentUser
                    Log.d(TAG, user.toString())
                    Toast.makeText(baseContext, "Authentication is a success.",
                        Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)

                    intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
                    startActivity(intent)
                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "createUserWithEmail:failure")
                    Toast.makeText(baseContext, "Authentication failed - " + task.exception?.message.toString(),
                        Toast.LENGTH_SHORT).show()

                }
            }
    }
}