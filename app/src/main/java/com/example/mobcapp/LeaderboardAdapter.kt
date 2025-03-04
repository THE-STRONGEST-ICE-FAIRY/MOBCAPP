package com.example.mobcapp

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView

class LeaderboardAdapter(private var leaderboardData: List<Pair<String, Int>>) :
    RecyclerView.Adapter<LeaderboardAdapter.ViewHolder>() {

    class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        val playerName: TextView = view.findViewById(R.id.textView_PlayerName)
        val playerScore: TextView = view.findViewById(R.id.textView_PlayerScore)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val view = LayoutInflater.from(parent.context)
            .inflate(R.layout.item_leaderboard, parent, false)
        return ViewHolder(view)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val (name, score) = leaderboardData[position]
        holder.playerName.text = name
        holder.playerScore.text = "$score"
    }

    override fun getItemCount() = leaderboardData.size

    fun updateData(newData: List<Pair<String, Int>>) {
        leaderboardData = newData
        notifyDataSetChanged()
    }
}