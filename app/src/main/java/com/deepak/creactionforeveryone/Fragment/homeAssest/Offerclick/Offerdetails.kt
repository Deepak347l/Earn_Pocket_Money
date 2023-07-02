package com.deepak.creactionforeveryone.Fragment.homeAssest.Offerclick

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.bumptech.glide.Glide
import com.deepak.creactionforeveryone.databinding.ActivityMainBinding
import com.deepak.creactionforeveryone.databinding.ActivityOfferdetailsBinding

class Offerdetails : AppCompatActivity() {
    private lateinit var binding: ActivityOfferdetailsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityOfferdetailsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val app_name = intent.getStringExtra("app_name")
        val app_conversion = intent.getStringExtra("app_conversion")
        val app_des = intent.getStringExtra("app_des")
        val app_amount = intent.getStringExtra("app_amount")
        val app_img = intent.getStringExtra("app_img")
        val app_link = intent.getStringExtra("app_link")
        binding.appName.text = app_name.toString()
        binding.previewImgName.text = app_name.toString()
        binding.previewImgDess.text = app_conversion.toString()
        binding.playBtn.text = app_amount.toString()
        binding.previewImgDes.text = app_des.toString()
        Glide.with(this).load(app_img.toString()).into(binding.previewImg)
        binding.playBtn.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse(app_link))
            startActivity(intent)
        }
    }
}