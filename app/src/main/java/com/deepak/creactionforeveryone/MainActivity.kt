package com.deepak.creactionforeveryone

import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.DialogInterface
import android.content.Intent
import android.graphics.Color
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.view.animation.AccelerateDecelerateInterpolator
import android.view.animation.Animation
import android.view.animation.AnimationUtils
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.res.ResourcesCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.applovin.sdk.AppLovinSdk
import com.applovin.sdk.AppLovinSdkConfiguration
import com.bumptech.glide.Glide
import com.deepak.creactionforeveryone.Fragment.Categories
import com.deepak.creactionforeveryone.Fragment.HomeFragment
import com.deepak.creactionforeveryone.Fragment.Profile
import com.deepak.creactionforeveryone.Fragment.Settings
import com.deepak.creactionforeveryone.databinding.ActivityMainBinding
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.tabs.TabLayout
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.tab_background.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var view: View
    private lateinit var mAuth: FirebaseAuth
    //app update
    val UPDATE_CODE = 22
    lateinit var appUpdateManager: AppUpdateManager
    private lateinit var binding: ActivityMainBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Make sure to set the mediation provider value to "max" to ensure proper functionality
        AppLovinSdk.getInstance( this@MainActivity ).setMediationProvider( "max" )
        AppLovinSdk.getInstance( this@MainActivity ).initializeSdk({ configuration: AppLovinSdkConfiguration ->
            // AppLovin SDK is initialized, start loading ads
        })
            //login dialog
        binding.marqueeText.setSelected(true)
        // Adding the gif here using glide library
        Glide.with(this).load(R.drawable.querekaquiz).into(querekaquiz)
        //change the font family of text view in tabs(Make sure internet is on, for the first time to prevent crash)
        //if(internet is on)
        try {
            val typeface = ResourcesCompat.getFont(application, R.font.poppins_medium)
            tv1.setTypeface(typeface)
        } catch (e: Exception) {
            Log.e("mainActivityError", e.message.toString())
        }
        val f = HomeFragment()
        val s = Categories()
        val t = Profile()
        val f4th = Settings()
        setCurrentFragment(f)
        binding.tabLayout.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.page_1-> setCurrentFragment(f)
                R.id.page_2-> setCurrentFragment(s)
                R.id.page_3-> setCurrentFragment(t)
                R.id.page_4-> setCurrentFragment(f4th)
                R.id.page_5-> setCurrentFragment(f4th)
            }
            true
        }
        //data retrive for rcv
         mAuth = FirebaseAuth.getInstance()
        //compolsory app update methode
          inappupdate()
        //Now retrive our hedrer data
        FirebaseDatabase.getInstance().getReference("user")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString()).addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    val namedata =  snapshot.child("name").value.toString()
                    val picdata =  snapshot.child("pic").value.toString()
                    val income =  snapshot.child("income").value.toString()
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@MainActivity,error.message.toString(), Toast.LENGTH_SHORT).show()
                }
            })
    }

    private fun setCurrentFragment(f: Fragment) {
        val transaction: FragmentTransaction = supportFragmentManager.beginTransaction()
        transaction.setCustomAnimations(
            android.R.anim.slide_in_left,
            android.R.anim.slide_out_right
        )
        transaction.replace(binding.frameLayout.id, f)
        transaction.commit()
    }
    private fun inappupdate() {
        appUpdateManager = AppUpdateManagerFactory.create(this)
        val task : com.google.android.play.core.tasks.Task<AppUpdateInfo> = appUpdateManager.appUpdateInfo
        task.addOnSuccessListener {
            if (it.updateAvailability() == UpdateAvailability.UPDATE_AVAILABLE
                && it.isUpdateTypeAllowed(AppUpdateType.IMMEDIATE)){
                try {
                    appUpdateManager.startUpdateFlowForResult(
                        it,
                        AppUpdateType.IMMEDIATE,
                        this,
                        UPDATE_CODE
                    )

                } catch (e: InternalError){ }
            }
        }
        appUpdateManager.registerListener(listner)
    }
    val listner: InstallStateUpdatedListener = InstallStateUpdatedListener{ state ->

        if (state.installStatus() == InstallStatus.DOWNLOADING){
            popup()
        }
    }

    private fun popup() {
        val sneekbar = Snackbar.make(
            findViewById(android.R.id.content), "App update done", Snackbar.LENGTH_INDEFINITE
        )
        sneekbar.setAction("Reloaded", object : View.OnClickListener {
            override fun onClick(v: View?) {
                appUpdateManager.completeUpdate()
            }

        })
        sneekbar.setTextColor(Color.parseColor("#FF0000"))
        sneekbar.show()
    }
    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        if (requestCode == UPDATE_CODE){
            if (resultCode != RESULT_OK){
            }
        }
    }

    //CHEK USER LOGIN STATUS

    //when on back presed click app closed
    override fun onBackPressed() {
        // Create the object of AlertDialog Builder class
        val builder = AlertDialog.Builder(this@MainActivity)
        // Set the message show for the Alert time
        builder.setMessage("Do you want to exit the app")
        // Set Alert Title
        builder.setTitle("Are you sure ?")
        // Set Cancelable false for when the user clicks on the outside the Dialog Box then it will remain show
        builder.setCancelable(false)
        // Set the positive button with yes name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setPositiveButton("Yes") {
            // When the user click yes button then app will close
                dialog, which ->
            finish()
        }
        // Set the Negative button with No name Lambda OnClickListener method is use of DialogInterface interface.
        builder.setNegativeButton("No") {
            // If user click no then dialog box is canceled.
                dialog, which -> dialog.cancel()
        }
        // Create the Alert dialog
        val alertDialog = builder.create()
        alertDialog.setOnShowListener(object : DialogInterface.OnShowListener {
            @SuppressLint("ResourceAsColor")
            override fun onShow(dialog: DialogInterface?) {
                alertDialog.getButton(AlertDialog.BUTTON_POSITIVE).setTextColor(R.color.md_theme_light_onSurface)
                alertDialog.getButton(AlertDialog.BUTTON_NEGATIVE).setTextColor(R.color.md_theme_light_onSurface)
            }
        })
        // Show the Alert Dialog box
        alertDialog.show()

    }

}