package com.deepak.creactionforeveryone.Fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.nativeAds.MaxNativeAdListener
import com.applovin.mediation.nativeAds.MaxNativeAdLoader
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.deepak.creactionforeveryone.databinding.FragmentMoreBinding


class MoreFragment : Fragment() {
    private var _binding:FragmentMoreBinding? = null
    private val binding get() = _binding!!
    private lateinit var nativeAdLoader: MaxNativeAdLoader
    private var nativeAd: MaxAd? = null
    private var QUERKA_URI = "http://1090.set.qureka.com/"
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentMoreBinding.inflate(inflater, container, false)
        binding.btn1.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/Deepak_Dev_Android"))
            startActivity(intent)
        }
        binding.btn2.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/Deepak_Dev_Android"))
            startActivity(intent)
        }
        binding.btn3.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/Deepak_Dev_Android"))
            startActivity(intent)
        }
        binding.btn4.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/earnpocketcashbydipakdev"))
            startActivity(intent)
        }
        binding.btn5.setOnClickListener {
            val intent = Intent(Intent.ACTION_VIEW, Uri.parse("https://t.me/deepak_dev_contact"))
            startActivity(intent)
        }
        binding.quizadMoreFragment.setOnClickListener {
            val builder = CustomTabsIntent.Builder()
            val customBuilder = builder.build()
            customBuilder.launchUrl(it.context, Uri.parse(QUERKA_URI))
        }
        createnativeAd()
        return binding.root
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
                binding.nativeAdContainerAdMoreFragment.removeAllViews()
                binding.nativeAdContainerAdMoreFragment.addView( nativeAdView )
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