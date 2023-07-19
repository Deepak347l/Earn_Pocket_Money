package com.deepak.creactionforeveryone.Fragment

import android.app.Activity
import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.os.Handler
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxAdListener
import com.applovin.mediation.MaxError
import com.applovin.mediation.ads.MaxInterstitialAd
import com.applovin.mediation.nativeAds.MaxNativeAdListener
import com.applovin.mediation.nativeAds.MaxNativeAdLoader
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.deepak.creactionforeveryone.Fragment.categoriesAssest.Rewardfromads
import com.deepak.creactionforeveryone.Fragment.categoriesAssest.Skratch
import com.deepak.creactionforeveryone.Fragment.categoriesAssest.Spin
import com.deepak.creactionforeveryone.databinding.FragmentCategoriesBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import java.util.concurrent.TimeUnit
import com.google.firebase.database.ValueEventListener


class Categories : Fragment(), MaxAdListener {
    private var _binding : FragmentCategoriesBinding? = null
    private val binding get() = _binding!!
    private lateinit var nativeAdLoader: MaxNativeAdLoader
    private var nativeAd: MaxAd? = null
    private lateinit var interstitialAd: MaxInterstitialAd
    private var QUERKA_URI = "http://1090.set.qureka.com/"
    private var retryAttempt = 0.0
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentCategoriesBinding.inflate(inflater, container, false)
        createnativeAd()

        interstitialAd = MaxInterstitialAd( "c838522c4c0efd62", context as Activity?)
        interstitialAd.setListener( this )

        // Load the first ad
        interstitialAd.loadAd()

        FirebaseDatabase.getInstance().getReference("user")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString()).addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        val income = snapshot.child("income").value.toString().toFloat()
                        binding.coinC.setText(String.format("₹ %.2f",income))
                    }catch(e:Exception){
                        Log.e("finderror", e.message.toString())
                    }
                    //custom ad set up
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("finderror", error.message.toString())
                }
            })

        binding.bn1.setOnClickListener {
            FirebaseDatabase.getInstance().getReference("user")
                .child(FirebaseAuth.getInstance().currentUser?.uid.toString()).addValueEventListener(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val bounous =  snapshot.child("bounous").value
                        if(bounous == false){
                            val intent = Intent(context,Rewardfromads::class.java)
                            intent.putExtra("key","ads")
                            startActivity(intent)
                            val hashMap = HashMap<String, Any>()
                            hashMap.put("bounous",true)
                            FirebaseDatabase.getInstance().getReference("user")
                                .child(FirebaseAuth.getInstance().currentUser?.uid.toString()).updateChildren(hashMap)
                        }else{
                            Toast.makeText(context,"You already claimed", Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Log.e("mainActivityError", error.message)
                    }
                })
        }
        binding.bn2.setOnClickListener {
            if ( interstitialAd.isReady() )
            {
                interstitialAd.showAd();
            }else{
                val intent = Intent(context, Rewardfromads::class.java)
                intent.putExtra("key1","ads1")
                startActivity(intent)
            }
        }
        binding.bn3.setOnClickListener {
            val intent = Intent(context, Spin::class.java)
            startActivity(intent)
        }
        binding.bn4.setOnClickListener {
            val intent = Intent(context, Skratch::class.java)
            startActivity(intent)
        }
        binding.quizad1xx.setOnClickListener {
            val builder = CustomTabsIntent.Builder()
            val customBuilder = builder.build()
            customBuilder.launchUrl(it.context, Uri.parse(QUERKA_URI))
        }
        binding.quizad1x.setOnClickListener {
            val builder = CustomTabsIntent.Builder()
            val customBuilder = builder.build()
            customBuilder.launchUrl(it.context, Uri.parse(QUERKA_URI))
        }
        return binding.root
    }

    override fun onAdLoaded(ad: MaxAd?) { retryAttempt = 0.0}
    override fun onAdDisplayed(ad: MaxAd?) {}
    override fun onAdHidden(ad: MaxAd?) {
        val intent = Intent(context,Rewardfromads::class.java)
        intent.putExtra("key1","ads1")
        startActivity(intent)
        interstitialAd.loadAd()
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
        nativeAdLoader = MaxNativeAdLoader( "ed8d5e49cf23cc40", context )
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
                binding.nativeAdContainerAd.removeAllViews()
                binding.nativeAdContainerAd.addView( nativeAdView )
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

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}

