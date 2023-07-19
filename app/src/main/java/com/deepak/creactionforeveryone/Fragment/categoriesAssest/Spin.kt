package com.deepak.creactionforeveryone.Fragment.categoriesAssest

import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.MotionEvent
import android.view.View
import android.view.animation.Animation
import android.view.animation.DecelerateInterpolator
import android.view.animation.RotateAnimation
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.deepak.creactionforeveryone.databinding.ActivitySpinBinding
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.database.DataSnapshot
import com.google.firebase.database.DatabaseError
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.database.ValueEventListener

class Spin : AppCompatActivity(),Animation.AnimationListener {
    var count = 0
    var flag = false
    lateinit var wonamt:String
    private lateinit var binding: ActivitySpinBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySpinBinding.inflate(layoutInflater)
        setContentView(binding.root)
        binding.btnSpin.setOnTouchListener(PowerTouchListener())
        intspiner()
    }
    val prizes = floatArrayOf(1.0F, 4.0F, 2.5F, 2.0F, 1.5F, 8F,0F, 20F)
    var mSpinDuration:Long = 0
    var mSpinRevolution = 0f
    private fun intspiner() {}
    fun startSpiner(){
        mSpinRevolution = 3600f
        mSpinDuration = 5000
        if (count >= 30){
            mSpinDuration = 1000
            mSpinRevolution = (3600 * 2).toFloat()
        }
        if (count >= 60){
            mSpinDuration = 15000
            mSpinRevolution = (3600 * 3).toFloat()
        }
        val end = Math.floor(Math.random() * 3600).toInt()
        val numberOfPrizes = prizes.size
        val degreesPerPrize = 360/numberOfPrizes
        val shift = 0
        val prizeIndex = (shift + end) % numberOfPrizes

        wonamt = prizes[prizeIndex].toString()
        val rotateAnimation = RotateAnimation(0f,mSpinRevolution + end,Animation.RELATIVE_TO_SELF,0.5f,Animation.RELATIVE_TO_SELF,0.5f)
        rotateAnimation.interpolator = DecelerateInterpolator()
        rotateAnimation.repeatCount = 0
        rotateAnimation.duration = mSpinDuration
        rotateAnimation.setAnimationListener(this)
        rotateAnimation.fillAfter = true
        binding.ivWheel.startAnimation(rotateAnimation)
    }

    override fun onAnimationStart(animation: Animation?) {}

    override fun onAnimationEnd(animation: Animation?) {
        val intent = Intent(this@Spin,Rewardfromads::class.java)
        intent.putExtra("key11x","ads11")
        intent.putExtra("key11",wonamt)
        startActivity(intent)
    }

    override fun onAnimationRepeat(animation: Animation?) {}
    private inner class PowerTouchListener:View.OnTouchListener{
        override fun onTouch(v: View?, event: MotionEvent?): Boolean {

            when(event!!.action){
                MotionEvent.ACTION_DOWN -> {
                    flag = true
                    count = 0
                    Thread{
                        while (flag){
                            count++
                            if (count == 100 ){
                                try{
                                    Thread.sleep(100)
                                }catch (e:InterruptedException){
                                    Toast.makeText(this@Spin,e.message.toString(),Toast.LENGTH_SHORT).show()
                                }
                                count = 0
                            }
                            try{
                                Thread.sleep(10)
                            }catch (e:InterruptedException){
                                Toast.makeText(this@Spin,e.message.toString(),Toast.LENGTH_SHORT).show()
                            }
                        }
                    }.start()
                    return true
                }
                MotionEvent.ACTION_UP -> {
                    flag = false
                    FirebaseDatabase.getInstance().getReference("user")
                        .child(FirebaseAuth.getInstance().currentUser?.uid.toString()).addListenerForSingleValueEvent(object :
                            ValueEventListener {
                            override fun onDataChange(snapshot: DataSnapshot) {
                                val income =  snapshot.child("income").value.toString().toFloat()
                                if(income >= 4.0){
                                    val fincome =  income - 4.0
                                    val hashMap = HashMap<String, Any>()
                                    hashMap.put("income", fincome.toString())
                                    FirebaseDatabase.getInstance().getReference("user")
                                        .child(FirebaseAuth.getInstance().currentUser?.uid.toString()).updateChildren(hashMap)
                                    startSpiner()
                                }
                                else{
                                    Toast.makeText(this@Spin,"Low balance",Toast.LENGTH_SHORT).show()
                                    finish()
                                }
                            }
                            override fun onCancelled(error: DatabaseError) {
                                Log.e("mainActivityError", error.message)
                            }
                        })
                    return false
                }
            }
            return false
        }
    }
}
