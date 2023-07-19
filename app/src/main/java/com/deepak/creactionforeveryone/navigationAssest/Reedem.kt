package com.deepak.creactionforeveryone.navigationAssest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.deepak.creactionforeveryone.Fragment.homeAssest.Model.CustomAd
import com.deepak.creactionforeveryone.MainActivity
import com.deepak.creactionforeveryone.databinding.ActivityReedemBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.*
import com.google.firebase.database.annotations.Nullable

class Reedem : AppCompatActivity() {
    private lateinit var binding:ActivityReedemBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityReedemBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.leftbtn15.setOnClickListener {
            finish()
        }
        val referid = binding.usernameWWUUAAAAA.text.toString()
        //here our excutable code
        binding.button.setOnClickListener {
            FirebaseDatabase.getInstance().getReference("user").addChildEventListener(object :
                ChildEventListener {
                override fun onChildAdded(
                    snapshot: DataSnapshot,
                    @Nullable previousChildName: String?
                ) {
                    val sdata = snapshot.getValue(CustomAd::class.java)
                    if(sdata!!.id == referid){
                        FirebaseDatabase.getInstance().getReference("user")
                            .child(FirebaseAuth.getInstance().currentUser?.uid.toString()).addListenerForSingleValueEvent(object :
                                ValueEventListener {
                                override fun onDataChange(snapshot: DataSnapshot) {
                                    try {
                                        val income =  snapshot.child("income").value.toString().toFloat()
                                        val referidvalid = snapshot.child("refered").value
                                        if (referidvalid == false) {
                                            val referIncome = 0.50.toFloat()
                                            val finalincome = (income + referIncome)
                                            val hashMap = HashMap<String, Any>()
                                            hashMap.put("income", finalincome.toString())
                                            hashMap.put("refered", true)
                                            //all done but this part changed
                                            var totalReffer = sdata.total_reffer!!.toInt()
                                            val counter = ++totalReffer
                                            hashMap.put("total_reffer", counter.toString())
                                            FirebaseDatabase.getInstance().getReference("user")
                                                .child(FirebaseAuth.getInstance().currentUser?.uid.toString()).updateChildren(hashMap)

                                        }
                                        Toast.makeText(this@Reedem,"Applied",Toast.LENGTH_SHORT).show()
                                    }catch(e:Exception){
                                        Log.e("finderror", e.message.toString())
                                    }
                                }
                                override fun onCancelled(error: DatabaseError) {
                                    Toast.makeText(this@Reedem,error.message.toString(), Toast.LENGTH_SHORT).show()
                                }
                            })
                    }
                    else{
                        Toast.makeText(this@Reedem,"Invalid/Allerady",Toast.LENGTH_SHORT).show()
                    }
                }

                override fun onChildChanged(
                    snapshot: DataSnapshot,
                    @Nullable previousChildName: String?
                ) {
                }

                override fun onChildRemoved(snapshot: DataSnapshot) {
                }

                override fun onChildMoved(
                    snapshot: DataSnapshot,
                    @Nullable previousChildName: String?
                ) {
                }

                override fun onCancelled(error: DatabaseError) {
                    Toast.makeText(
                        this@Reedem,
                        "Fail to get the data" + error.message,
                        Toast.LENGTH_SHORT
                    ).show()
                }
            })
        }
    }
}