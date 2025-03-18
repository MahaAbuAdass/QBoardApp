package com.example.slaughterhousescreen.ui

import android.content.Context
import android.graphics.Color
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.example.slaughterhousescreen.R
import com.example.slaughterhousescreen.data.CurrentQ
import com.example.slaughterhousescreen.util.PreferenceManager

class TicketAdapter(
    private var currentQList: List<CurrentQ?>,
    var context: Context,
    private val bgColor: String,
    val fontColor : Int
) : RecyclerView.Adapter<TicketAdapter.ItemViewHolder>() {

    private val maxItems = 7 // Maximum number of items to display

    inner class ItemViewHolder(itemView: View) : RecyclerView.ViewHolder(itemView) {
        val status: TextView = itemView.findViewById(R.id.cell_status)
        val ticketNumber: TextView = itemView.findViewById(R.id.cell_ticket_number)
        val firstCard : CardView = itemView.findViewById(R.id.card_1)
        val secondCard : CardView = itemView.findViewById(R.id.card_2_num_cell)

    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ItemViewHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.cell_ticket, parent, false)
        return ItemViewHolder(view)
    }

    override fun getItemCount(): Int {
        return maxItems // Return the maximum number of items
    }

    override fun onBindViewHolder(holder: ItemViewHolder, position: Int) {
        var selectedDeviceType = PreferenceManager.getSelectedDevice(context)

        val headerBackgroundColor = try {
            if (bgColor.isNotEmpty()) Color.parseColor(bgColor) else Color.WHITE
        } catch (e: IllegalArgumentException) {
            Log.e("TicketAdapter", "Invalid color code: $bgColor", e)
            Color.BLACK // Fallback to black if the color is invalid
        }

        holder.firstCard.setCardBackgroundColor(headerBackgroundColor)
        holder.secondCard.setCardBackgroundColor(headerBackgroundColor)
        holder.ticketNumber.setTextColor(fontColor)
        holder.status.setTextColor(fontColor)


        if (position < currentQList.size) {
            val item = currentQList[position]
            holder.status.text = item?.CounterId.toString()
            holder.ticketNumber.text = item?.TicketNo ?: " "

            when(selectedDeviceType){
                "smartTv" -> {
                    holder.status.textSize = 31.5f // Set text size in SP (e.g., 20f for 20sp)
                    holder.ticketNumber.textSize = 31.5f// Keeps the layout even if empty
                }

                "mediaPlayer"-> {
                    holder.status.textSize = 43.6f
                    holder.ticketNumber.textSize = 43.6f
                }
            }


        } else {
            // If no data, set empty values or keep them as placeholders

            when(selectedDeviceType){
                "smartTv" -> {
                    holder.status.text = " \n -" // Keeps the layout even if empty
                    holder.status.textSize = 16.3f // Set text size in SP (e.g., 20f for 20sp)
                    holder.ticketNumber.text = " \n -" // Keeps the layout even if empty
                    holder.ticketNumber.textSize = 16.3f // Keeps the layout even if empty
                }

                "mediaPlayer"-> {
                    holder.status.text = " \n -"
                    holder.status.textSize = 23f
                    holder.ticketNumber.text = " \n -"
                    holder.ticketNumber.textSize = 23f
                }
            }
        }
    }
}
