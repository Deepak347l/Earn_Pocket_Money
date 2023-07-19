package com.deepak.creactionforeveryone.Fragment.categoriesAssest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.widget.Toast
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.MaxReward
import com.applovin.mediation.MaxRewardedAdListener
import com.applovin.mediation.ads.MaxRewardedAd
import com.applovin.mediation.nativeAds.MaxNativeAdListener
import com.applovin.mediation.nativeAds.MaxNativeAdLoader
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.deepak.creactionforeveryone.MainActivity
import com.deepak.creactionforeveryone.R
import com.deepak.creactionforeveryone.databinding.ActivityRewardfromadsBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import java.util.concurrent.TimeUnit

class Rewardfromads : AppCompatActivity(), MaxRewardedAdListener {
    private lateinit var rewardedAd: MaxRewardedAd
    private var retryAttempt = 0.0
    private lateinit var nativeAdLoader: MaxNativeAdLoader
    private var nativeAd: MaxAd? = null
    private lateinit var binding: ActivityRewardfromadsBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityRewardfromadsBinding.inflate(layoutInflater)
        setContentView(binding.root)
        createnativeAd()

        rewardedAd = MaxRewardedAd.getInstance( "f36531f56dec0423", this )
        rewardedAd.setListener( this )

        rewardedAd.loadAd()

        val key = intent.getStringExtra("key")
        val key1 = intent.getStringExtra("key1")
        val key11 = intent.getStringExtra("key11")
        val key11x = intent.getStringExtra("key11x")
        val key111x = intent.getStringExtra("key111x")
        val key111 = intent.getStringExtra("key111")

        if(key == "ads"){
            val randomwin = (1..100).random()
            val win = randomwin/100.toFloat()//total earn uptp 1 by bonous
            binding.BalanceWr.text = win.toString()
            binding.button2WWRRRr.setOnClickListener {
                if ( rewardedAd.isReady() )
                {
                    FirebaseDatabase.getInstance().getReference("user")
                        .child(FirebaseAuth.getInstance().currentUser?.uid.toString()).addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val income =  snapshot.child("income").value.toString().toFloat()
                                val fincome =  income + win
                                val hashMap = HashMap<String, Any>()
                                hashMap.put("income", fincome.toString())
                                FirebaseDatabase.getInstance().getReference("user")
                                    .child(FirebaseAuth.getInstance().currentUser?.uid.toString()).updateChildren(hashMap)
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Log.e("mainActivityError", error.message)
                            }
                        })
                    rewardedAd.showAd();
                }else{
                    Toast.makeText(this,"Please click again",Toast.LENGTH_SHORT).show()
                }
            }
        }
        else if(key1 == "ads1"){

            val randomwin = (1..10).random()
            val win = randomwin/100.toFloat()//total earn uptp 0.1 by bonous
            binding.BalanceWr.text = win.toString()

            binding.button2WWRRRr.setOnClickListener {
                if ( rewardedAd.isReady() )
                {
                    FirebaseDatabase.getInstance().getReference("user")
                        .child(FirebaseAuth.getInstance().currentUser?.uid.toString()).addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val income =  snapshot.child("income").value.toString().toFloat()
                                val fincome =  income + win
                                val hashMap = HashMap<String, Any>()
                                hashMap.put("income", fincome.toString())
                                FirebaseDatabase.getInstance().getReference("user")
                                    .child(FirebaseAuth.getInstance().currentUser?.uid.toString()).updateChildren(hashMap)
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Log.e("mainActivityError", error.message)
                            }
                        })
                    rewardedAd.showAd();
                }else{
                    Toast.makeText(this,"Please click again",Toast.LENGTH_SHORT).show()
                }
            }

        }
        //here we do our other codes
        else if(key11x == "ads11"){
            binding.BalanceWr.text = key11.toString()
            val win = key11.toString().toFloat()//total earn uptp 0.1 by bonous
            binding.button2WWRRRr.setOnClickListener {
                if ( rewardedAd.isReady() )
                {
                    FirebaseDatabase.getInstance().getReference("user")
                        .child(FirebaseAuth.getInstance().currentUser?.uid.toString()).addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val income =  snapshot.child("income").value.toString().toFloat()
                                val fincome =  income + win
                                val hashMap = HashMap<String, Any>()
                                hashMap.put("income", fincome.toString())
                                FirebaseDatabase.getInstance().getReference("user")
                                    .child(FirebaseAuth.getInstance().currentUser?.uid.toString()).updateChildren(hashMap)
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Log.e("mainActivityError", error.message)
                            }
                        })
                    rewardedAd.showAd();
                }else{
                    Toast.makeText(this,"Please click again",Toast.LENGTH_SHORT).show()
                }
            }

        }
        else if(key111x == "ads111"){

            binding.BalanceWr.text = key111.toString()
            val win = key111.toString().toFloat()//total earn uptp 0.1 by bonous
            binding.button2WWRRRr.setOnClickListener {
                if ( rewardedAd.isReady() )
                {
                    FirebaseDatabase.getInstance().getReference("user")
                        .child(FirebaseAuth.getInstance().currentUser?.uid.toString()).addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val income =  snapshot.child("income").value.toString().toFloat()
                                val fincome =  income + win
                                val hashMap = HashMap<String, Any>()
                                hashMap.put("income", fincome.toString())
                                FirebaseDatabase.getInstance().getReference("user")
                                    .child(FirebaseAuth.getInstance().currentUser?.uid.toString()).updateChildren(hashMap)
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Log.e("mainActivityError", error.message)
                            }
                        })
                    rewardedAd.showAd();
                }else{
                    Toast.makeText(this,"Please click again",Toast.LENGTH_SHORT).show()
                }

            }

        }
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
                binding.nativeAdContainer1zzzz.removeAllViews()
                binding.nativeAdContainer1zzzz.addView( nativeAdView )
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

    // MAX Ad Listener
    override fun onAdLoaded(maxAd: MaxAd)
    {
        // Rewarded ad is ready to be shown. rewardedAd.isReady() will now return 'true'
        // Reset retry attempt
        retryAttempt = 0.0
    }

    override fun onAdLoadFailed(adUnitId: String?, error: MaxError?)
    {
        // Rewarded ad failed to load
        // We recommend retrying with exponentially higher delays up to a maximum delay (in this case 64 seconds)
        retryAttempt++
        val delayMillis = TimeUnit.SECONDS.toMillis( Math.pow( 2.0, Math.min( 6.0, retryAttempt ) ).toLong() )

        Handler().postDelayed( { rewardedAd.loadAd() }, delayMillis )
    }

    override fun onAdDisplayFailed(ad: MaxAd?, error: MaxError?)
    {
        // Rewarded ad failed to display. We recommend loading the next ad
        rewardedAd.loadAd()
    }

    override fun onAdDisplayed(maxAd: MaxAd) {}

    override fun onAdClicked(maxAd: MaxAd) {}

    override fun onAdHidden(maxAd: MaxAd)
    {

        Toast.makeText(this,"Susessfully credited",Toast.LENGTH_SHORT).show()
        finish()
    }

    override fun onRewardedVideoStarted(maxAd: MaxAd) {} // deprecated

    override fun onRewardedVideoCompleted(maxAd: MaxAd) {} // deprecated

    override fun onUserRewarded(maxAd: MaxAd, maxReward: MaxReward)
    {
        // Rewarded ad was displayed and user should receive the reward
    }
}