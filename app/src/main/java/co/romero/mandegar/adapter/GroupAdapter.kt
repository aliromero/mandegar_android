package co.romero.mandegar.adapter


import android.content.Context
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.romero.mandegar.R
import co.romero.mandegar.Util.Utils
import android.text.Spanned
import android.widget.LinearLayout
import android.widget.TextView
import co.romero.mandegar.model.ChatRoom
import android.R.attr.data




class GroupAdapter(private val mContext: Context?, private val items: MutableList<ChatRoom>) : RecyclerView.Adapter<GroupAdapter.CustomViewHolder>() {


    private lateinit var onItemClickListener: OnItemClickListener
    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_group, null)

        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(customViewHolder: CustomViewHolder, i: Int) {
        val messageItem = items[i]

//        Glide.with(mContext!!).load(messageItem).into(customViewHolder.cover!!)

        val listener = View.OnClickListener {
            onItemClickListener.onItemClick(messageItem)
        }

        customViewHolder.ll_click!!.setOnClickListener(listener)




        customViewHolder.tv_message!!.text = messageItem.last_message
        customViewHolder.tv_title!!.text = messageItem.name

    }

    @SuppressWarnings("deprecation")
    fun fromHtml(html: String): Spanned {
        return if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.N) {
            Html.fromHtml(html, Html.FROM_HTML_MODE_LEGACY)
        } else {
            Html.fromHtml(html)
        }
    }



        override fun getItemCount(): Int {
            return items.size
        }

        inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
            var ll_click: LinearLayout? = null
            var tv_message: TextView? = null
            var tv_title: TextView? = null
//            var tv_text: TextView? = null


            init {
                this.ll_click = view.findViewById(R.id.ll_click)
                this.tv_message = view.findViewById(R.id.tv_message)
                this.tv_title = view.findViewById(R.id.tv_title)
//                this.tv_text = view.findViewById(R.id.tv_text)

            }
        }

        fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
            this.onItemClickListener = onItemClickListener

        }


        interface OnItemClickListener {
            fun onItemClick(item: ChatRoom)
        }

    }