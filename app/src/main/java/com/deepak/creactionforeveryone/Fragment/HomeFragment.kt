package com.deepak.creactionforeveryone.Fragment

import android.content.Intent
import android.net.Uri
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.browser.customtabs.CustomTabsIntent
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentTransaction
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONObjectRequestListener
import com.applovin.mediation.MaxAd
import com.applovin.mediation.MaxError
import com.applovin.mediation.nativeAds.MaxNativeAdListener
import com.applovin.mediation.nativeAds.MaxNativeAdLoader
import com.applovin.mediation.nativeAds.MaxNativeAdView
import com.bumptech.glide.Glide
import com.deepak.creactionforeveryone.Fragment.homeAssest.Adapter
import com.deepak.creactionforeveryone.Fragment.homeAssest.Model.Creative
import com.deepak.creactionforeveryone.Fragment.homeAssest.Model.CustomAd
import com.deepak.creactionforeveryone.Fragment.homeAssest.Model.Offer
import com.deepak.creactionforeveryone.Fragment.homeAssest.Offerclick.Offerdetails
import com.deepak.creactionforeveryone.R
import com.deepak.creactionforeveryone.databinding.FragmentHomeBinding
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.json.JSONArray
import org.json.JSONObject


class HomeFragment : Fragment(), Adapter.DownloadClickInterface {
    private lateinit var  offerRVModalArrayList: ArrayList<Offer>
    private lateinit var offerRVAdapter: Adapter
    private var QUERKA_URI = "http://1090.set.qureka.com/"
    private var _binding : FragmentHomeBinding? = null
    private val binding get() = _binding!!
    private lateinit var nativeAdLoader: MaxNativeAdLoader
    private var nativeAd: MaxAd? = null
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentHomeBinding.inflate(inflater, container, false)
        // Inflate the layout for this fragment
        offerRVModalArrayList = ArrayList()
        offerRVAdapter = Adapter(offerRVModalArrayList, context!!, this)
        binding.recycler.layoutManager = LinearLayoutManager(context)
        binding.recycler.adapter = offerRVAdapter
        AndroidNetworking.initialize(context)
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val url = "https://cpalead.com/dashboard/reports/campaign_json.php?id=2379391&country=IN&show=30&subid="+uid.toString()
        AndroidNetworking.get(url)
            .setPriority(Priority.HIGH)
            .build()
            .getAsJSONObject(object : JSONObjectRequestListener {
                override fun onResponse(response: JSONObject?) {
                    val jsonArray: JSONArray = response!!.getJSONArray("offers")
                    try{
                        for (i in 0 until jsonArray.length()) {
                            val jsonObject: JSONObject = jsonArray.getJSONObject(i)
                            val title = jsonObject.getString("title")
                            val amount = jsonObject.getString("amount")
                            val dating = jsonObject.getString("dating")
                            val description = jsonObject.getString("description")
                            val epc = jsonObject.getString("epc")
                            val link = jsonObject.getString("link")
                            val mobile_app = jsonObject.getString("mobile_app")
                            val mobile_app_icon_url = jsonObject.getString("mobile_app_icon_url")
                            val mobile_app_id = jsonObject.getString("mobile_app_id")
                            val mobile_app_minimum_version = jsonObject.getString("mobile_app_minimum_version")
                            val mobile_app_type = jsonObject.getString("mobile_app_type")
                            val offerwall_only = jsonObject.getString("offerwall_only")
                            val payout_currency = jsonObject.getString("payout_currency")
                            val payout_type = jsonObject.getString("payout_type")
                            val preview_url = jsonObject.getString("preview_url")
                            val rank = jsonObject.getString("rank")
                            val ratio = jsonObject.getString("ratio")
                            val traffic_type = jsonObject.getString("traffic_type")
                            val jsonArray1: JSONArray = jsonObject.getJSONArray("creatives")
                            val conversion = jsonObject.getString("conversion")
                            val campid = jsonObject.getString("campid")
                            for (j in 0 until jsonArray1.length()){
                                val jsonObject1: JSONObject = jsonArray1.getJSONObject(j)
                                val size = jsonObject1.getString("size")
                                val urls = jsonObject1.getString("url")
                                offerRVModalArrayList
                                    .add(Offer(amount,dating.toBoolean(),description,epc,link,mobile_app.toInt()
                                        ,mobile_app_icon_url,mobile_app_id,mobile_app_minimum_version,mobile_app_type
                                        ,offerwall_only.toBoolean(),payout_currency,payout_type,preview_url,rank.toInt(),ratio,title,traffic_type,
                                        listOf(Creative(size,urls)),conversion,campid.toInt()
                                    ))
                            }
                            binding.progressSmall1.visibility = View.GONE
                            offerRVAdapter.notifyDataSetChanged()
                        }
                    }catch (e:Exception){
                        Log.e("finderror", e.message.toString())
                    }
                }

                override fun onError(anError: ANError?) {
                    binding.progressSmall1.visibility = View.GONE
                    Log.e("error", anError?.message.toString())
                    Toast.makeText(context,anError?.message.toString(),Toast.LENGTH_SHORT).show()
                }

            })
        //Now retrive our hedrer data
        FirebaseDatabase.getInstance().getReference("user")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString()).addValueEventListener(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        val income = snapshot.child("income").value.toString().toFloat()
                        binding.incomebalanceamsa.setText(String.format("₹ %.2f",income))
                    }catch(e:Exception){
                        Log.e("finderror", e.message.toString())
                    }
                    //custom ad set up
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("finderror", error.message.toString())
                }
            })
        FirebaseDatabase.getInstance().getReference("CustomAd")
            .child("Ad").addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        if(snapshot.exists())
                        {
                            binding.banerAd1text.text = snapshot.child("des").value.toString()
                            Glide.with(context!!).load(snapshot.child("img").value.toString()).into(binding.banerAd1img)
                            binding.materialCardViewforcAds1.setOnClickListener {
                                val intent = Intent(context,Offerdetails::class.java)
                                intent.putExtra("app_name", snapshot.child("name").value.toString())
                                intent.putExtra("app_conversion", snapshot.child("rule").value.toString())
                                intent.putExtra("app_des", snapshot.child("des").value.toString())
                                intent.putExtra("app_amount", "Get ₹" + snapshot.child("amount").value.toString())
                                intent.putExtra("app_img", snapshot.child("img").value.toString())
                                intent.putExtra("app_link", snapshot.child("link").value.toString())
                                startActivity(intent)
                            }
                        }else{
                            binding.materialCardViewforcAds1.visibility = View.GONE
                        }
                    }
                    catch(e:Exception)
                    {
                        Log.e("finderror", e.message.toString())
                    }
                    //custom ad set up
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("finderror", error.message.toString())
                }
            })
        FirebaseDatabase.getInstance().getReference("CustomAd")
            .child("Ad1").addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        if(snapshot.exists())
                        {
                            binding.banerAd2text.text = snapshot.child("des").value.toString()
                            Glide.with(context!!).load(snapshot.child("img").value.toString()).into(binding.banerAd2img)
                            binding.materialCardViewforcAds2.setOnClickListener {
                                val intent = Intent(context,Offerdetails::class.java)
                                intent.putExtra("app_name", snapshot.child("name").value.toString())
                                intent.putExtra("app_conversion", snapshot.child("rule").value.toString())
                                intent.putExtra("app_des", snapshot.child("des").value.toString())
                                intent.putExtra("app_amount", "Get ₹" + snapshot.child("amount").value.toString())
                                intent.putExtra("app_img", snapshot.child("img").value.toString())
                                intent.putExtra("app_link", snapshot.child("link").value.toString())
                                startActivity(intent)
                            }
                            binding.quizad1.visibility = View.VISIBLE
                            binding.quizad1.setOnClickListener {
                                val builder = CustomTabsIntent.Builder()
                                val customBuilder = builder.build()
                                customBuilder.launchUrl(it.context, Uri.parse(QUERKA_URI))
                            }
                        }else{
                            binding.materialCardViewforcAds2.visibility = View.GONE
                        }
                    }
                    catch(e:Exception)
                    {
                        Log.e("finderror", e.message.toString())
                    }
                    //custom ad set up
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("finderror", error.message.toString())
                }
            })
        FirebaseDatabase.getInstance().getReference("CustomAd")
            .child("Ad2").addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                        if(snapshot.exists())
                        {
                            binding.banerAd3text.text = snapshot.child("des").value.toString()
                            Glide.with(context!!).load(snapshot.child("img").value.toString()).into(binding.banerAd3img)
                            binding.materialCardViewforcAds3.setOnClickListener {
                                val intent = Intent(context,Offerdetails::class.java)
                                intent.putExtra("app_name", snapshot.child("name").value.toString())
                                intent.putExtra("app_conversion", snapshot.child("rule").value.toString())
                                intent.putExtra("app_des", snapshot.child("des").value.toString())
                                intent.putExtra("app_amount", "Get ₹" + snapshot.child("amount").value.toString())
                                intent.putExtra("app_img", snapshot.child("img").value.toString())
                                intent.putExtra("app_link", snapshot.child("link").value.toString())
                                startActivity(intent)
                            }
                        }else{
                            binding.materialCardViewforcAds3.visibility = View.GONE
                        }
                    }
                    catch(e:Exception)
                    {
                        Log.e("finderror", e.message.toString())
                    }
                    //custom ad set up
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("finderror", error.message.toString())
                }
            })
        binding.quizad.setOnClickListener {
            val builder = CustomTabsIntent.Builder()
            val customBuilder = builder.build()
            customBuilder.launchUrl(it.context, Uri.parse(QUERKA_URI))
        }
        binding.bn5.setOnClickListener {
            val s = Categories()
           setCurrentFragment(s)
        }
        binding.bn6.setOnClickListener {
            val t = Profile()
            setCurrentFragmentz(t)
        }
        createnativeAd()
        binding.adView.loadAd()
        return binding.root
    }

    private fun setCurrentFragmentz(t: Profile) {
        val transaction = fragmentManager?.beginTransaction()
        transaction?.setCustomAnimations(
            android.R.anim.slide_in_left,
            android.R.anim.slide_out_right
        )
        transaction?.replace(R.id.frameLayout, t)
        transaction?.commit()
    }

    private fun setCurrentFragment(s: Categories) {
        val transaction = fragmentManager?.beginTransaction()
        transaction?.setCustomAnimations(
            android.R.anim.slide_in_left,
            android.R.anim.slide_out_right
        )
        transaction?.replace(R.id.frameLayout, s)
        transaction?.commit()
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
                binding.nativeAdContainer.removeAllViews()
                binding.nativeAdContainer.addView( nativeAdView )
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

    override fun onDownloadClick(position: Int, notesRVModal: Offer, holder: Adapter.ViewHolder) {
       val intent = Intent(context,Offerdetails::class.java)
        intent.putExtra("app_name", notesRVModal.title)
        intent.putExtra("app_conversion", notesRVModal.conversion)
        intent.putExtra("campid", notesRVModal.campid.toString())
        intent.putExtra("app_des", notesRVModal.description)
        val rewardamount =  (notesRVModal.amount.toFloat() * 80) / 2
        intent.putExtra("app_amount",  rewardamount.toInt().toString())
        intent.putExtra("app_img", notesRVModal.creatives.get(0).url)
        intent.putExtra("app_link", notesRVModal.link)
        startActivity(intent)
    }

}