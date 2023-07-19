package com.deepak.creactionforeveryone

import android.Manifest
import android.annotation.SuppressLint
import android.app.AlertDialog
import android.content.*
import android.content.pm.PackageManager
import android.graphics.Color
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.MenuItem
import android.view.View
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.content.res.ResourcesCompat
import androidx.core.view.GravityCompat
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import com.applovin.sdk.AppLovinSdk
import com.applovin.sdk.AppLovinSdkConfiguration
import com.bumptech.glide.Glide
import com.deepak.creactionforeveryone.Fragment.*
import com.deepak.creactionforeveryone.databinding.ActivityMainBinding
import com.deepak.creactionforeveryone.navigationAssest.History
import com.deepak.creactionforeveryone.navigationAssest.Reedem
import com.deepak.creactionforeveryone.navigationAssest.Withdraw
import com.deepak.creactionforeveryone.navigationAssest.claimEarning
import com.google.android.material.navigation.NavigationView
import com.google.android.material.snackbar.Snackbar
import com.google.android.play.core.appupdate.AppUpdateInfo
import com.google.android.play.core.appupdate.AppUpdateManager
import com.google.android.play.core.appupdate.AppUpdateManagerFactory
import com.google.android.play.core.install.InstallStateUpdatedListener
import com.google.android.play.core.install.model.AppUpdateType
import com.google.android.play.core.install.model.InstallStatus
import com.google.android.play.core.install.model.UpdateAvailability
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.ktx.auth
import com.google.firebase.database.*
import com.google.firebase.ktx.Firebase
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.tab_background.*
import java.util.*

class MainActivity : AppCompatActivity() {
    private lateinit var mAuth: FirebaseAuth
    //app update
    val UPDATE_CODE = 22
    lateinit var appUpdateManager: AppUpdateManager
    private lateinit var binding: ActivityMainBinding
    private var QUERKA_URI = "http://1090.set.qureka.com/"
    //view
    private lateinit var hView: View
    //now hader name
    private lateinit var userName: TextView
    private lateinit var userEmail:TextView
    private lateinit var userId:TextView
    private lateinit var copyButton: ImageView
    private lateinit var userImage: ImageView

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)
        // Make sure to set the mediation provider value to "max" to ensure proper functionality
        AppLovinSdk.getInstance( this@MainActivity ).setMediationProvider( "max" )
        AppLovinSdk.getInstance( this@MainActivity ).initializeSdk({ configuration: AppLovinSdkConfiguration ->
            // AppLovin SDK is initialized, start loading ads
        })
        //Notification permission
        FirebaseMessaging.getInstance().subscribeToTopic("/topics/earnpoketmoney")
        Notificationpermission()
            //login dialog
        binding.marqueeText.setSelected(true)
        // Adding the gif here using glide library
        Glide.with(this).load(R.drawable.querekaquiz).into(querekaquiz)

        binding.NavView.itemIconTintList = null
        //nav bar open by 3line
        binding.menubtn.setOnClickListener {
            binding.Drawer.openDrawer(GravityCompat.START)
        }
        //nav bar haderlayout controll here we take
        hView = binding.NavView.getHeaderView(0)
        userName = hView.findViewById(R.id.usernameff)
        userEmail = hView.findViewById(R.id.following)
        userId = hView.findViewById(R.id.followers)
        copyButton = hView.findViewById(R.id.c)
        userImage = hView.findViewById(R.id.name)
        //nav bar handel clicks
        binding.NavView.setNavigationItemSelectedListener(object :
            NavigationView.OnNavigationItemSelectedListener {
            override fun onNavigationItemSelected(item: MenuItem): Boolean {
                val id = item.itemId
                _drawer.closeDrawer(GravityCompat.START)
                when (id) {
                    R.id.page_1A -> {
                        val intent = Intent(this@MainActivity, Withdraw::class.java)
                        startActivity(intent)
                    }
                    R.id.page_2A -> {
                        val intent = Intent(this@MainActivity, claimEarning::class.java)
                        startActivity(intent)
                    }
                    R.id.page_3A -> {
                        val intent = Intent(this@MainActivity, Reedem::class.java)
                        startActivity(intent)
                    }
                    R.id.page_4A -> {
                        val intent = Intent(this@MainActivity, Reedem::class.java)
                        startActivity(intent)
                    }
                    R.id.page_5A -> {
                        val intent = Intent(this@MainActivity, History::class.java)
                        startActivity(intent)
                    }
                    R.id.page_6A -> {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://doc-hosting.flycricket.io/earn-pocket-money-privacy-policy/dab7e189-fc37-4d02-8caf-b8fc46118b31/privacy")
                        )
                        startActivity(intent)
                    }
                    R.id.page_7A -> {
                        Firebase.auth.signOut()
                        val intent = Intent(this@MainActivity, Login::class.java)
                        startActivity(intent)
                        Toast.makeText(
                            this@MainActivity,
                            "You susessfully logout",
                            Toast.LENGTH_SHORT
                        ).show()
                        finish()
                    }
                    //our profile click
                    R.id.page_8A -> {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://t.me/earnpocketcashbydipakdev")
                        )
                        startActivity(intent)
                    }
                    R.id.page_9A -> {
                        val intent = Intent(
                            Intent.ACTION_VIEW,
                            Uri.parse("https://youtube.com/@likestar5821")
                        )
                        startActivity(intent)
                    }
                    else -> {
                        return true
                    }
                }
                return true
            }
        })
        binding.refferbtnImage.setOnClickListener {
            FirebaseDatabase.getInstance().getReference("manul").child("link").addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        val link = snapshot.child("data").value.toString()
                        val sendIntent = Intent().apply {
                            action = Intent.ACTION_SEND
                            putExtra(Intent.EXTRA_TEXT, link)
                            type = "text/plain"
                        }
                        val shareIntent = Intent.createChooser(sendIntent, null)
                        startActivity(shareIntent)
                    }catch(e:Exception){
                        Log.e("finderror", e.message.toString())
                    }
                    //custom ad set up
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("finderror", error.message.toString())
                }
            })
        }
        binding.querekaquiz.setOnClickListener {
            val builder = CustomTabsIntent.Builder()
            val customBuilder = builder.build()
            customBuilder.launchUrl(it.context, Uri.parse(QUERKA_URI))
        }
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
        val f5th = MoreFragment()
        setCurrentFragment(f)
        binding.tabLayout.setOnNavigationItemSelectedListener{
            when(it.itemId){
                R.id.page_1-> setCurrentFragment(f)
                R.id.page_2-> setCurrentFragment(s)
                R.id.page_3-> setCurrentFragment(t)
                R.id.page_4-> setCurrentFragment(f4th)
                R.id.page_5-> setCurrentFragment(f5th)
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
                    try {
                    val income =  snapshot.child("income").value.toString().toFloat()
                        binding.incomebalance.setText(String.format("₹ %.2f",income))
                        val namedata = snapshot.child("name").value.toString()
                        val userImagedata = snapshot.child("pic").value.toString()
                        val emaildata = snapshot.child("email").value.toString()
                        val iddata = snapshot.child("id").value.toString()

                        userName.text = namedata
                        userEmail.text = emaildata
                        userId.text = iddata
                        Glide.with(this@MainActivity).load(userImagedata).circleCrop().into(userImage)

                        copyButton.setOnClickListener {
                            val c: ClipboardManager = getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
                            val clipData = ClipData.newPlainText("TextView", userId.text.toString())
                            c.setPrimaryClip(clipData)
                            Toast.makeText(this@MainActivity, "Copied", Toast.LENGTH_SHORT).show();
                        }
                    }catch(e:Exception){
                        Log.e("finderror", e.message.toString())
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@MainActivity,error.message.toString(), Toast.LENGTH_SHORT).show()
                }
            })
        //now setup navdrawer
    }

    private fun Notificationpermission() {
        val permissions = arrayOf("android.permission.POST_NOTIFICATIONS")
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU){
            val prmNotif = ActivityCompat.checkSelfPermission(this,Manifest.permission.POST_NOTIFICATIONS)
            if (prmNotif != PackageManager.PERMISSION_GRANTED){
                ActivityCompat.requestPermissions(this,permissions,555)
            }
        }
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