package com.example.navigatour

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import com.example.navigatour.databinding.ActivityLoginBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.ktx.Firebase

private const val TAG = "LoginActivity"


class LoginActivity : AppCompatActivity() {
    private lateinit var binding: ActivityLoginBinding
    private lateinit var userPassword: String
    private lateinit var userEmail: String
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        auth = Firebase.auth

        super.onCreate(savedInstanceState)
        binding = ActivityLoginBinding.inflate(layoutInflater)

        binding.loginButton.setOnClickListener{
            userPassword = binding.loginPassword.text.toString()
            userEmail = binding.loginEmail.text.toString()
            if(userPassword.isNotEmpty() && userEmail.isNotEmpty()){
                logInUser(userEmail, userPassword)
            }else{
                Toast.makeText(baseContext, "One or more fields are empty, " +
                        "ensure that all fields all fill",
                    Toast.LENGTH_SHORT).show()
            }
        }

        binding.createAccountButton.setOnClickListener{
            val intent = Intent(this, RegistrationActivity::class.java)
            startActivity(intent)
        }

        setContentView(binding.root)
    }

    private fun logInUser(email :String, password: String){
        auth.signInWithEmailAndPassword(email, password)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Sign in success, update UI with the signed-in user's information
                    Log.d(TAG, "signInWithEmail:success")
                    val user = auth.currentUser
                    Toast.makeText(baseContext, "User Signed In",
                        Toast.LENGTH_SHORT).show()
                    val intent = Intent(this, MainActivity::class.java)
                    startActivity(intent)

                } else {
                    // If sign in fails, display a message to the user.
                    Log.w(TAG, "signInWithEmail:failure", task.exception)
                    Toast.makeText(baseContext, "Authentication failed: " + task.exception?.message.toString(),
                        Toast.LENGTH_SHORT).show()
//                    updateUI(null)
                }
            }
    }
}