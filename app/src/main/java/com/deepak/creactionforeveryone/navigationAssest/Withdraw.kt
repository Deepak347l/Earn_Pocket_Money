package com.deepak.creactionforeveryone.navigationAssest

import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import com.applovin.mediation.nativeAds.MaxNativeAdListener
import com.applovin.mediation.nativeAds.MaxNativeAdLoader
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.deepak.creactionforeveryone.MainActivity
import com.deepak.creactionforeveryone.databinding.ActivityWithdrawBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.TimeUnit

class Withdraw : AppCompatActivity(), MaxAdListener {
    private lateinit var binding:ActivityWithdrawBinding
    private lateinit var interstitialAd: MaxInterstitialAd
    private var retryAttempt = 0.0
    private lateinit var nativeAdLoader: MaxNativeAdLoader
    private var nativeAd: MaxAd? = null
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityWithdrawBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createnativeAd()
        createInterstitialAd()
        binding.leftbtn1.setOnClickListener {
            finish()
        }
        //set or update total balance
        FirebaseDatabase.getInstance().getReference("user")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString()).addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        val income =  snapshot.child("income").value.toString().toFloat()
                        binding.coinCWithdraw.setText(String.format("₹ %.2f",income))
                    }catch(e:Exception){
                        Log.e("finderror", e.message.toString())
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@Withdraw,error.message.toString(), Toast.LENGTH_SHORT).show()
                }
            })
        binding.bn61.setOnClickListener {
            validdata()
        }
    }

    private fun createInterstitialAd() {
        interstitialAd = MaxInterstitialAd( "c838522c4c0efd62", this)
        interstitialAd.setListener( this )

        // Load the first ad
        interstitialAd.loadAd()
    }

    private fun validdata() {
        val PaytmNumber = binding.usernameWWUU.text.toString()
        val Ammount = binding.usernameWWUU1.text.toString()
        if (PaytmNumber.isEmpty()){
            binding.usernameWWUU.setError("required")
            binding.usernameWWUU.requestFocus()
        }
        else if(Ammount.isEmpty()){
            binding.usernameWWUU1.setError("required")
            binding.usernameWWUU1.requestFocus()
        }
        else{
            wthdraw()
        }
    }

    private fun wthdraw() {
        FirebaseDatabase.getInstance().getReference("user")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString()).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        val income =  snapshot.child("income").value.toString().toFloat()
                        val iddata = snapshot.child("id").value.toString()
                        if (income < 1.00.toFloat()){
                            Toast.makeText(this@Withdraw,"Insuficent Balance",Toast.LENGTH_SHORT).show()
                        }else{
                            val Ammount = binding.usernameWWUU1.text.toString().toFloat()
                            if (Ammount <= income){
                            val updateAmounta = income-Ammount
                            val hashMap = HashMap<String,Any>()
                            hashMap.put("paytm",binding.usernameWWUU.getText().toString())
                            hashMap.put("uid",FirebaseAuth.getInstance().currentUser?.uid.toString())
                            hashMap.put("id",iddata.toString())
                            hashMap.put("status",false)
                            hashMap.put("Amount",Ammount.toString())
                            Toast.makeText(this@Withdraw,"Credited within 1 hours",Toast.LENGTH_SHORT).show()
                            FirebaseDatabase.getInstance().getReference("withdrawl")
                                .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
                                .push()
                                .setValue(hashMap)
                            val hashMap1 = HashMap<String, Any>()
                            hashMap1.put("income", updateAmounta.toString())
                            FirebaseDatabase.getInstance().getReference("user")
                                .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
                                .updateChildren(hashMap1)
                        }else{
                                Toast.makeText(this@Withdraw,"Insuficent Balance",Toast.LENGTH_SHORT).show()
                            }
                        }
                    }catch(e:Exception){
                        Log.e("finderror", e.message.toString())
                    }
                }
                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(this@Withdraw,error.message.toString(), Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onStart() {
        super.onStart()
        if  ( interstitialAd.isReady ) {
            interstitialAd.showAd()
        }
    }

    override fun onAdLoaded(ad: MaxAd?) { retryAttempt = 0.0}
    override fun onAdDisplayed(ad: MaxAd?) {}
    override fun onAdHidden(ad: MaxAd?) {
    }
    override fun onAdClicked(ad: MaxAd?) {}

    override fun onAdLoadFailed(adUnitId: String?, error: MaxError?) {
        // Interstitial ad failed to load
        // AppLovin recommends that you retry with exponentially higher delays up to a maximum delay (in this case 64 seconds)
        retryAttempt++
        val delayMillis = TimeUnit.SECONDS.toMillis( Math.pow( 2.0, Math.min( 6.0, retryAttempt ) ).toLong() )

        Handler().postDelayed( { interstitialAd.loadAd() }, delayMillis )
    }

    override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?) {
        interstitialAd.loadAd()
    }
    private fun createnativeAd() {
        nativeAdLoader = MaxNativeAdLoader( "ed8d5e49cf23cc40", this )
        nativeAdLoader.setNativeAdListener(object : MaxNativeAdListener() {

            override fun onNativeAdLoaded(nativeAdView: MaxNativeAdView?, ad: MaxAd)
            {
                // Clean up any pre-existing native ad to prevent memory leaks.
                if ( nativeAd != null )
                {
                    nativeAdLoader.destroy( nativeAd )
                }

                // Save ad for cleanup.
                nativeAd = ad

                // Add ad view to view.
                binding.nativeAdContainerAdWithdraw.removeAllViews()
                binding.nativeAdContainerAdWithdraw.addView( nativeAdView )
            }

            override fun onNativeAdLoadFailed(adUnitId: String, error: MaxError)
            {
                // We recommend retrying with exponentially higher delays up to a maximum delay
            }

            override fun onNativeAdClicked(ad: MaxAd)
            {
                // Optional click callback
            }
        })
        nativeAdLoader.loadAd()
    }

}