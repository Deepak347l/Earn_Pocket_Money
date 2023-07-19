package com.deepak.creactionforeveryone.Fragment

import android.content.ClipData
import android.content.ClipboardManager
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import androidx.core.content.ContextCompat.getSystemService
import androidx.fragment.app.Fragment
import com.deepak.creactionforeveryone.databinding.FragmentProfileBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener


class Profile : Fragment() {
    private var _binding:FragmentProfileBinding? = null
    private val binding get() = _binding!!
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        _binding = FragmentProfileBinding.inflate(inflater, container, false)
        FirebaseDatabase.getInstance().getReference("user")
            .child(FirebaseAuth.getInstance().currentUser?.uid.toString()).addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                val refercodeget = snapshot.child("referid").value.toString()
                val totalrefercount = snapshot.child("total_reffer").value.toString()
                        binding.Refer.text = refercodeget
                if(totalrefercount == null){
                    binding.totalRefer.text = "0"
                    binding.totalEarn.text = "₹0.0"
                }else{
                    binding.totalRefer.text = totalrefercount
                    val total_earn_amount = totalrefercount.toFloat() * (15.00).toFloat()
                    binding.totalEarn.text = "₹" + total_earn_amount.toString()
                }
            }catch(e:Exception){
                        Log.e("finderror", e.message.toString())
                    }
                    //custom ad set up
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("finderror", error.message.toString())
                }
            })

        //refer button click listner
        binding.button2.setOnClickListener {
            FirebaseDatabase.getInstance().getReference("manul").child("link").addListenerForSingleValueEvent(object :
                ValueEventListener {
                override fun onDataChange(snapshot: DataSnapshot) {
                    try {
                val link = snapshot.child("data").value.toString()
                val sendIntent = Intent().apply {
                    action = Intent.ACTION_SEND
                    putExtra(Intent.EXTRA_TEXT, link)
                    type = "text/plain"
                }
                val shareIntent = Intent.createChooser(sendIntent, null)
                startActivity(shareIntent)
            }catch(e:Exception){
                        Log.e("finderror", e.message.toString())
                    }
                    //custom ad set up
                }
                override fun onCancelled(error: DatabaseError) {
                    Log.e("finderror", error.message.toString())
                }
            })
        }
        //copy refer code
        binding.imageview2.setOnClickListener {
            val c: ClipboardManager = context?.getSystemService(Context.CLIPBOARD_SERVICE) as ClipboardManager
            val clipData = ClipData.newPlainText("TextView",binding.Refer.text.toString())
            c.setPrimaryClip(clipData)
            Toast.makeText(context,"Copied",Toast.LENGTH_SHORT).show()
        }
        return binding.root
    }
    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }
}