package com.deepak.creactionforeveryone.navigationAssest.historyAssest

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.deepak.creactionforeveryone.R


class Historyadapter(coursesArrayList: ArrayList<Historymodel>, context: Context) :
    RecyclerView.Adapter<Historyadapter.ViewHolder>() {
    // creating variables for our ArrayList and context
    private val coursesArrayList: ArrayList<Historymodel>
    private val context: Context
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        // passing our layout file for displaying our card item
        return ViewHolder(LayoutInflater.from(context).inflate(R.layout.history_item, parent, false))
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        // setting data to our text views from our modal class.
        val courses = coursesArrayList[position]
        holder.courseNameTV.setText("Status")
        holder.courseDurationTV.setText(courses.Amount.toString())
        if (courses.status == false){
            holder.courseDescTV.setText("Panding")
        }
        else{
            holder.courseDescTV.setText("Successful")
        }
    }

    override fun getItemCount(): Int {
        // returning the size of our array list.
        return coursesArrayList.size
    }

    inner class ViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        // creating variables for our text views.
        val courseNameTV: TextView
        val courseDurationTV: TextView
        val courseDescTV: TextView

        init {
            // initializing our text views.
            courseNameTV = itemView.findViewById(R.id.textView3)
            courseDurationTV = itemView.findViewById(R.id.status)
            courseDescTV = itemView.findViewById(R.id.tstatus)
        }
    }

    // creating constructor for our adapter class
    init {
        this.coursesArrayList = coursesArrayList
        this.context = context
    }
}