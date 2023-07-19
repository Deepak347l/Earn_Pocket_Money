package com.deepak.creactionforeveryone.Fragment.categoriesAssest

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import com.anupkumarpanwar.scratchview.ScratchView
import com.deepak.creactionforeveryone.R
import com.deepak.creactionforeveryone.databinding.ActivitySkratchBinding
import com.google.firebase.database.DatabaseError

class Skratch : AppCompatActivity() {
    private lateinit var binding: ActivitySkratchBinding
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySkratchBinding.inflate(layoutInflater)
        setContentView(binding.root)
        val randomwin = (1..10).random()
        val win = randomwin/100.toFloat()
        binding.scratchViewwin.text = "₹" + win.toString()
        binding.scratchView.setRevealListener(object : ScratchView.IRevealListener {
            override fun onRevealed(scratchView: ScratchView) {
                                val intent = Intent(this@Skratch, Rewardfromads::class.java)
                                intent.putExtra("key111x", "ads111")
                                intent.putExtra("key111", win.toString())
                                startActivity(intent)
                                finish()
            }
            override fun onRevealPercentChangedListener(scratchView: ScratchView, percent: Float) {}
        })
    }
}
