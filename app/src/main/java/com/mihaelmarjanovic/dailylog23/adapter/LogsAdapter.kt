package com.mihaelmarjanovic.dailylog23.adapter

import android.content.Context
import android.net.Uri
import android.provider.MediaStore
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.mihaelmarjanovic.dailylog23.R
import com.mihaelmarjanovic.dailylog23.models.Logs


class LogsAdapter(private val context: Context, val listener: LogsClickListener): RecyclerView.Adapter<LogsAdapter.LogsViewHolder>() {

    private val LogsList = ArrayList<Logs>()
    private val fullList = ArrayList<Logs>()
    var isImageFitToScreen = false

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): LogsViewHolder {
        return LogsViewHolder(
            LayoutInflater.from(context).inflate(R.layout.log_item, parent, false)
        )

    }

    override fun onBindViewHolder(holder: LogsViewHolder, position: Int) {
        val width: Float = context.resources.getDimension(R.dimen.width)
        val height = context.resources.getDimension(R.dimen.height)

        val currentLog = LogsList[position]
        holder.title.text = currentLog.title
        holder.log.text = currentLog.log
        holder.time.text = currentLog.timeOfLog

        if(currentLog.image.toString().isNotEmpty() || currentLog.image.toString() != "") {
            holder.image.setLayoutParams(LinearLayout.LayoutParams(width.toInt(), height.toInt()));
            holder.image.setAdjustViewBounds(true);
            holder.image.setImageBitmap(
                MediaStore.Images.Media.getBitmap(
                    context.contentResolver,
                    Uri.parse(currentLog.image)
                )
            )
        }
        else{
            holder.image.setImageDrawable(null);
            holder.image.setLayoutParams(LinearLayout.LayoutParams(0, 0));
        }

        holder.logs_layout.setOnClickListener{
            listener.onItemClicked(LogsList[holder.adapterPosition])
        }
        holder.logs_layout.setOnLongClickListener{
            listener.onLongItemClicked(LogsList[holder.adapterPosition], holder.logs_layout)
            true
        }

        holder.image.setOnClickListener{
                if(isImageFitToScreen) {
                    isImageFitToScreen=false;
                    holder.image.setLayoutParams(LinearLayout.LayoutParams(width.toInt(), height.toInt()));
                    holder.image.setAdjustViewBounds(true);

                }else{
                    isImageFitToScreen=true;
                    holder.image.setLayoutParams(LinearLayout.LayoutParams(LinearLayout.LayoutParams.MATCH_PARENT, LinearLayout.LayoutParams.MATCH_PARENT));
                    holder.image.setScaleType(ImageView.ScaleType.FIT_XY);
                }
        }

        holder.image.setOnLongClickListener{
            holder.image.rotation = holder.image.rotation + 90;
            true
        }

    }

    override fun getItemCount(): Int {
        return LogsList.size
    }

    fun updateList(newList: List<Logs>){
        fullList.clear()
        fullList.addAll(newList)

        LogsList.clear()
        LogsList.addAll(fullList)
        notifyDataSetChanged()
    }

    inner class LogsViewHolder(itemView: View): RecyclerView.ViewHolder(itemView){
        val logs_layout = itemView.findViewById<CardView>(R.id.cardLayout)
        val title = itemView.findViewById<TextView>(R.id.tvTitle)
        val log = itemView.findViewById<TextView>(R.id.tvLog)
        val time = itemView.findViewById<TextView>(R.id.tvTime)
        val image = itemView.findViewById<ImageView>(R.id.ivListLogImage)
    }

    interface LogsClickListener{
        fun onItemClicked(logs: Logs)
        fun onLongItemClicked(logs: Logs, cardView: CardView)
    }

}