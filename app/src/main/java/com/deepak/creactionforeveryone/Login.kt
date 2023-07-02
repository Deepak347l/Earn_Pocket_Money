package com.deepak.creactionforeveryone

import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import android.widget.Toast
import androidx.annotation.Nullable
import androidx.appcompat.app.AppCompatActivity
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInAccount
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.android.gms.tasks.Task
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.FirebaseUser
import com.google.firebase.auth.GoogleAuthProvider
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.ktx.Firebase
import kotlinx.android.synthetic.main.activity_login.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import kotlinx.coroutines.tasks.await
import kotlinx.coroutines.withContext

class Login : AppCompatActivity() {

    private val RC_SIGN_IN: Int = 123
    private val TAG = "SignInActivity Tag"
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_login)
        // Configure Google Sign In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()
        googleSignInClient = GoogleSignIn.getClient(this, gso)
        auth = Firebase.auth

        scv.setOnClickListener {
            signIn()
        }

    }

    override fun onStart() {
        super.onStart()
        val currentuser = auth.currentUser
        if (currentuser != null){
            val intent = Intent(this, MainActivity::class.java)
            startActivity(intent)
            finish()
        }
    }

    private fun signIn() {
        val signInIntent = googleSignInClient.signInIntent
        startActivityForResult(signInIntent, RC_SIGN_IN)
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == RC_SIGN_IN) {
            val task = GoogleSignIn.getSignedInAccountFromIntent(data)
            handleSignInResult(task)
        }
    }

    private fun handleSignInResult(completedTask: Task<GoogleSignInAccount>) {
        try {
            val account =
                completedTask.getResult(ApiException::class.java)!!
            Log.d(TAG, "firebaseAuthWithGoogle:" + account.id)
            firebaseAuthWithGoogle(account.idToken!!)
        } catch (e: ApiException) {
            Log.w(TAG, "signInResult:failed code=" + e.statusCode)

        }
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        scv.visibility = View.GONE
        progressBar.visibility = View.VISIBLE

             auth.signInWithCredential(credential).addOnCompleteListener(this) { task ->
                 if (task.isSuccessful){
                 if (task.result.additionalUserInfo?.isNewUser == true){
                     val firebaseUser = auth.currentUser
                     updateUI(firebaseUser)
                 }
                 else {
                     val mainActivityIntent = Intent(this@Login, MainActivity::class.java)
                     startActivity(mainActivityIntent)
                     finish()
                 }
                 }else {
                     updateUI(null)
             }
             }
    }

    private fun updateUI(firebaseUser: FirebaseUser?) {
        if(firebaseUser != null) {
            val curentdeviseid = android.provider.Settings.Secure.getString(this@Login.contentResolver, android.provider.Settings.Secure.ANDROID_ID)
            val AlphaNumericString = ("ABCDEFGHIJKLMNOPQRSTUVWXYZ"
                    + "0123456789"
                    + "abcdefghijklmnopqrstuvxyz")
            val sb: StringBuilder = StringBuilder(10)
            for (i in 0 until 10) {
                // generate a random number between
                // 0 to AlphaNumericString variable length
                val index = (AlphaNumericString.length
                        * Math.random()).toInt()
                // add Character one by one in end of sb
                sb.append(AlphaNumericString[index])
            }
            val hashMap = HashMap<String, Any>()
            hashMap.put("uid", firebaseUser.uid)
            hashMap.put("name", firebaseUser.displayName.toString())
            hashMap.put("email", firebaseUser.email.toString())
            hashMap.put("referid", sb.toString())
            hashMap.put("divID",curentdeviseid.toString())
            hashMap.put("income", "0.0")
            hashMap.put("refered",false)
            hashMap.put("bounous",false)
            hashMap.put("total_reffer","0")
            hashMap.put("id",sb.toString())
            hashMap.put("pic",firebaseUser.photoUrl.toString())
            FirebaseDatabase.getInstance().getReference("user").child(firebaseUser.uid).setValue(hashMap)
            val mainActivityIntent = Intent(this@Login, MainActivity::class.java)
            startActivity(mainActivityIntent)
            finish()
            Toast.makeText(this@Login, "registered", Toast.LENGTH_SHORT).show()
        } else {
            scv.visibility = View.VISIBLE
            progressBar.visibility = View.GONE
        }
    }
}