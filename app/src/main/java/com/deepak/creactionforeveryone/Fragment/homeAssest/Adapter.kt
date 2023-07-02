package com.deepak.creactionforeveryone.Fragment.homeAssest

import android.content.Context
import android.os.Build
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.RequiresApi
import androidx.recyclerview.widget.RecyclerView
import com.bumptech.glide.Glide
import com.deepak.creactionforeveryone.Fragment.homeAssest.Model.Offer
import com.deepak.creactionforeveryone.R

class Adapter (
    offerRVModalArrayList: ArrayList<Offer>,
    context: Context,
    downloadClickInterface: DownloadClickInterface,
) :
    RecyclerView.Adapter<Adapter.ViewHolder>() {
    // creating variables for our list, context, interface and position.
    private var offerRVModalArrayList: ArrayList<Offer>
    private val context: Context
    private val downloadClickInterface: DownloadClickInterface
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // inflating our layout file on below line.
        return try {
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.install_items, parent, false)
            ViewHolder(view)
        } catch (e: Exception) {
            Log.e("adapterError", e.message.toString())
            val view: View =
                LayoutInflater.from(context).inflate(R.layout.install_items, parent, false)
            ViewHolder(view)
        }

    }
    //
    @RequiresApi(Build.VERSION_CODES.M)
    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val offer:Offer = offerRVModalArrayList[position]
        holder.headText.text = offer.title
        val rewardamount =  (offer.amount.toFloat() * 80) / 2
        holder.rewardamountText.text = "Get ₹" + rewardamount.toInt().toString()
            holder.descText.text =  offer.description
        Glide.with(holder.itemView.context).load(offer.creatives.get(0).url).into(holder.offerImage)
        holder.downloadClickButton.setOnClickListener { downloadClickInterface.onDownloadClick(
            position,
            offer,holder,
        ) }
    }

    override fun getItemCount(): Int {
        return offerRVModalArrayList.size
    }

    class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView)  {
        val headText: TextView
        val descText: TextView
        val offerImage: ImageView
        val downloadClickButton: LinearLayout
        val rewardamountText: TextView

        init {
            headText = itemView.findViewById(R.id.head1)
            descText = itemView.findViewById(R.id.desc1)
            offerImage = itemView.findViewById(R.id.imageview2xxxmmm)
            downloadClickButton = itemView.findViewById(R.id.bn5)
            rewardamountText = itemView.findViewById(R.id.textview26)
        }
    }

    interface DownloadClickInterface {
        fun onDownloadClick(position: Int, notesRVModal: Offer,holder: ViewHolder)
    }
    // creating a constructor.
    init {
        this.offerRVModalArrayList = offerRVModalArrayList
        this.context = context
        this.downloadClickInterface = downloadClickInterface
    }
}