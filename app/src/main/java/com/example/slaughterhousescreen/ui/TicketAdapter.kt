package com.example.slaughterhousescreen.ui

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.core.view.updatePadding
import androidx.recyclerview.widget.RecyclerView
import com.example.slaughterhousescreen.R
import com.example.slaughterhousescreen.data.CurrentQ

class TicketAdapter(
    private var currentQList: List<CurrentQ?>
) : RecyclerView.Adapter<TicketAdapter.ItemViewHolder>() {

    private val maxItems = 7 // Maximum number of items to display

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val status: TextView = itemView.findViewById(R.id.cell_status)
        val ticketNumber: TextView = itemView.findViewById(R.id.cell_ticket_number)
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_ticket, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return maxItems // Return the maximum number of items
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        if (position < currentQList.size) {
            val item = currentQList[position]
            holder.status.text = item?.CounterId.toString()
            holder.ticketNumber.text = item?.TicketNo ?: " "
        } else {
            // If no data, set empty values or keep them as placeholders

            holder.status.text = " \n -" // Keeps the layout even if empty

            holder.status.textSize = 16.7f // Set text size in SP (e.g., 20f for 20sp)


            holder.ticketNumber.text = " \n -" // Keeps the layout even if empty
            holder.ticketNumber.textSize = 16.7f // Keeps the layout even if empty

        }
    }
}
