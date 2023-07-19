package com.deepak.creactionforeveryone.navigationAssest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import com.androidnetworking.AndroidNetworking
import com.androidnetworking.common.Priority
import com.androidnetworking.error.ANError
import com.androidnetworking.interfaces.JSONArrayRequestListener
import com.deepak.creactionforeveryone.MainActivity
import com.deepak.creactionforeveryone.databinding.ActivityClaimEarningBinding
import com.deepak.creactionforeveryone.navigationAssest.claimEarningAssest.Earningadapter
import com.deepak.creactionforeveryone.navigationAssest.claimEarningAssest.Earningmodel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener
import org.json.JSONArray
import org.json.JSONObject

class claimEarning : AppCompatActivity(), Earningadapter.DownloadClickInterface {
    private lateinit var  notesRVModalArrayList: ArrayList<Earningmodel>
    private lateinit var notesRVAdapter: Earningadapter
    private lateinit var binding:ActivityClaimEarningBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityClaimEarningBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.leftbtn1511.setOnClickListener {
            finish()
        }
        notesRVModalArrayList = ArrayList()
        notesRVAdapter = Earningadapter(notesRVModalArrayList, this, this)
        binding.rcbxzaxa.layoutManager = LinearLayoutManager(this)
        binding.rcbxzaxa.adapter = notesRVAdapter
        AndroidNetworking.initialize(this)
        val uid = FirebaseAuth.getInstance().currentUser?.uid
        val url = "https://cpalead.com/dashboard/reports/conversion_api.php?id=2379391&subid="+uid.toString()
        AndroidNetworking.get(url)
            .setPriority(Priority.HIGH)
            .build().getAsJSONArray(object: JSONArrayRequestListener {
                override fun onResponse(response: JSONArray?) {
                    if(response != null){
                        for (i in 0 until response.length()){
                            val jsonObject: JSONObject = response.getJSONObject(i)
                            val campid = jsonObject.getString("campid")
                            val country = jsonObject.getString("country")
                            val status = jsonObject.getBoolean("status")
                            val subid = jsonObject.getString("subid")
                            val timestamp = jsonObject.getString("timestamp")
                            notesRVModalArrayList
                                .add(Earningmodel(campid.toInt(),country,status,subid,timestamp.toInt()))
                        }
                        binding.progressAaaasdfsmall1XXX.visibility = View.GONE
                        notesRVAdapter.notifyDataSetChanged()
                    }else{
                        binding.progressAaaasdfsmall1XXX.visibility = View.GONE
                    }
                }
                override fun onError(anError: ANError?) {
                    binding.progressAaaasdfsmall1XXX.visibility = View.GONE
                    Log.e("error", anError?.message.toString())
                    Toast.makeText(this@claimEarning,anError?.message.toString(), Toast.LENGTH_SHORT).show()
                }
            })
    }

    override fun onDownloadClick(
        position: Int,
        notesRVModal: Earningmodel,
        holder: Earningadapter.ViewHolder
    ) {
        try{
            FirebaseDatabase.getInstance().getReference("instalTracking")
                .child(FirebaseAuth.getInstance().currentUser?.uid.toString()).child(notesRVModal.campid.toString()).addValueEventListener(object :
                    ValueEventListener {
                    override fun onDataChange(snapshot: DataSnapshot) {
                        val conversion =  snapshot.child("conversion").value
                        if (conversion == false){
                            val amount =  snapshot.child("amount").value.toString().toFloat()
                            FirebaseDatabase.getInstance().getReference("user")
                                .child(FirebaseAuth.getInstance().currentUser?.uid.toString()).addListenerForSingleValueEvent(object :
                                    ValueEventListener {
                                    override fun onDataChange(snapshot: DataSnapshot) {
                                        val income =  snapshot.child("income").value.toString().toFloat()
                                        val fincome =  income + amount
                                        val hashMap = HashMap<String, Any>()
                                        hashMap.put("income", fincome.toString())
                                        FirebaseDatabase.getInstance().getReference("user")
                                            .child(FirebaseAuth.getInstance().currentUser?.uid.toString()).updateChildren(hashMap)
                                    }
                                    override fun onCancelled(error: DatabaseError) {
                                        Log.e("mainActivityError", error.message)
                                    }
                                })
                            val hashMapx = HashMap<String, Any>()
                            hashMapx.put("conversion", true)
                            FirebaseDatabase.getInstance().getReference("instalTracking")
                                .child(FirebaseAuth.getInstance().currentUser?.uid.toString()).child(notesRVModal.campid.toString()).updateChildren(hashMapx)
                            Toast.makeText(this@claimEarning,"Done!",Toast.LENGTH_SHORT).show()
                            finish()
                        }else{
                            Toast.makeText(this@claimEarning,"You allradey claimed",Toast.LENGTH_SHORT).show()
                        }
                    }
                    override fun onCancelled(error: DatabaseError) {
                        Log.e("mainActivityError", error.message)
                    }
                })
        }catch (e:Exception){
            Log.e("finderrorvvv",e.message.toString())
            Toast.makeText(this@claimEarning,e.message.toString(),Toast.LENGTH_SHORT).show()
        }
    }
    }
