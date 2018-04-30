package co.romero.mandegar.adapter

import android.content.Context
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView

import java.text.ParseException
import java.text.SimpleDateFormat
import java.util.ArrayList
import java.util.Calendar

import co.romero.mandegar.R
import co.romero.mandegar.Util.Utils
import co.romero.mandegar.model.Message


class ChatRoomThreadAdapter(private val mContext: Context, private val messageArrayList: List<Message>, public val userId: Int) : RecyclerView.Adapter<RecyclerView.ViewHolder>() {
    private val SELF = userId

    inner class ViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        internal var message: TextView
        internal var timestamp: TextView

        init {
            message = itemView.findViewById<View>(R.id.message) as TextView
            timestamp = itemView.findViewById<View>(R.id.timestamp) as TextView
        }
    }


    init {

        val calendar = Calendar.getInstance()
        today = calendar.get(Calendar.DAY_OF_MONTH).toString()
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): RecyclerView.ViewHolder {
        val itemView: View

        // view type is to identify where to render the chat message
        // left or right
        if (viewType == SELF) {
            // self message
            itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.chat_item_self, parent, false)
        } else {
            // others message
            itemView = LayoutInflater.from(parent.context)
                    .inflate(R.layout.chat_item_other, parent, false)
        }


        return ViewHolder(itemView)
    }


    override fun getItemViewType(position: Int): Int {
        val message = messageArrayList[position]
        return if (message.customer_id == userId) {
            SELF
        } else position

    }


    override fun onBindViewHolder(holder: RecyclerView.ViewHolder, position: Int) {
        val message = messageArrayList[position]
        (holder as ViewHolder).message.text = message.message

        var timestamp = getTimeStamp(message.createdAt)

//        if (message.user.name != null)
//            timestamp = message.user.name + ", " + timestamp

        timestamp = "1 2 3"

        holder.timestamp.text = timestamp
    }

    override fun getItemCount(): Int {
        return messageArrayList.size
    }

    companion object {

        private val TAG = ChatRoomThreadAdapter::class.java.simpleName
        private var today: String? = null

        fun getTimeStamp(dateStr: String): String {
            var format = SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
            var timestamp = ""

            today = if (today!!.length < 2) "0" + today!! else today

            try {
                val date = format.parse(dateStr)
                val todayFormat = SimpleDateFormat("dd")
                val dateToday = todayFormat.format(date)
                format = if (dateToday == today) SimpleDateFormat("hh:mm a") else SimpleDateFormat("dd LLL, hh:mm a")
                val date1 = format.format(date)
                timestamp = date1.toString()
            } catch (e: ParseException) {
                e.printStackTrace()
            }

            return timestamp
        }
    }
}

