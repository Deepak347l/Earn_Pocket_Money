package com.deepak.creactionforeveryone.navigationAssest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.recyclerview.widget.LinearLayoutManager
import com.deepak.creactionforeveryone.MainActivity
import com.deepak.creactionforeveryone.R
import com.deepak.creactionforeveryone.databinding.ActivityHistoryBinding
import com.deepak.creactionforeveryone.navigationAssest.historyAssest.Historyadapter
import com.deepak.creactionforeveryone.navigationAssest.historyAssest.Historymodel
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.ChildEventListener
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.annotations.Nullable

class History : AppCompatActivity() {
    private lateinit var  withdralarray: ArrayList<Historymodel>
    private lateinit var withdraladapter: Historyadapter
    private lateinit var binding:ActivityHistoryBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityHistoryBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.leftbtn151.setOnClickListener {
            finish()
        }
        //set array for recylerview
        withdralarray = ArrayList()
        withdraladapter = Historyadapter(withdralarray, this)
        binding.rcb.layoutManager = LinearLayoutManager(this)
        binding.rcb.adapter = withdraladapter
        //now we call methode for get data
        getData()
    }
    private fun getData() {
        FirebaseDatabase.getInstance().getReference("withdrawl")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString())
            .addChildEventListener(object :
            ChildEventListener {
            override fun onChildAdded(
                snapshot: DataSnapshot,
                @Nullable previousChildName: String?
            ) {
                // on below line we are hiding our progress bar.
                binding.test.visibility = View.GONE
                snapshot.getValue(Historymodel::class.java)?.let { withdralarray.add(it) }
                // notifying our adapter that data has changed.
                withdraladapter.notifyDataSetChanged()
            }
            override fun onChildChanged(
                snapshot: DataSnapshot,
                @Nullable previousChildName: String?
            ) {
                // this method is called when new child is added
                // we are notifying our adapter and making progress bar
                // visibility as gone.
                binding.test.visibility = View.GONE
                withdraladapter.notifyDataSetChanged()
            }

            override fun onChildRemoved(snapshot: DataSnapshot) {
                // notifying our adapter when child is removed.
                withdraladapter.notifyDataSetChanged()
                binding.test.visibility = View.GONE
            }

            override fun onChildMoved(
                snapshot: DataSnapshot,
                @Nullable previousChildName: String?
            ) {
                // notifying our adapter when child is moved.
                withdraladapter.notifyDataSetChanged()
                binding.test.visibility = View.GONE
            }

            override fun onCancelled(error: DatabaseError) {
                Toast.makeText(
                    this@History,
                    "Fail to get the data" + error.message,
                    Toast.LENGTH_SHORT
                ).show()
            }
        })
    }
}