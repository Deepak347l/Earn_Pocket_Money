package com.deepak.creactionforeveryone.Fragment.homeAssest.Offerclick

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.browser.customtabs.CustomTabsIntent
import com.bumptech.glide.Glide
import com.deepak.creactionforeveryone.MainActivity
import com.deepak.creactionforeveryone.databinding.ActivityMainBinding
import com.deepak.creactionforeveryone.databinding.ActivityOfferdetailsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.FirebaseDatabase

class Offerdetails : AppCompatActivity() {
    private lateinit var binding: ActivityOfferdetailsBinding
    private var QUERKA_URI = "http://1090.set.qureka.com/"
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOfferdetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val app_name = intent.getStringExtra("app_name")
        val app_conversion = intent.getStringExtra("app_conversion")
        val app_des = intent.getStringExtra("app_des")
        val campid = intent.getStringExtra("campid")
        val app_amount = intent.getStringExtra("app_amount")
        val app_img = intent.getStringExtra("app_img")
        val app_link = intent.getStringExtra("app_link")
        binding.appName.text = app_name.toString()
        binding.previewImgName.text = app_name.toString()
        binding.previewImgDess.text = app_conversion.toString()
        binding.playBtn.text = "Get ₹" + app_amount.toString()
        binding.previewImgDes.text = app_des.toString()
        Glide.with(this).load(app_img.toString()).into(binding.previewImg)
        binding.playBtn.setOnClickListener {
            val hashMap = HashMap<String, Any>()
            hashMap.put("conversion", false)
            hashMap.put("amount", app_amount.toString())
            hashMap.put("campid", campid.toString())
            FirebaseDatabase.getInstance().getReference("instalTracking")
                .child(FirebaseAuth.getInstance().currentUser?.uid.toString()).child(campid.toString()).setValue(hashMap)
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(app_link))
            startActivity(intent)
        }
        binding.quizada.setOnClickListener {
            val builder = CustomTabsIntent.Builder()
            val customBuilder = builder.build()
            customBuilder.launchUrl(it.context, Uri.parse(QUERKA_URI))
        }
        binding.leftbtn.setOnClickListener {
            finish()
        }
    }
}