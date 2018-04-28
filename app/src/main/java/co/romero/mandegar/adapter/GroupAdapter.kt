package co.romero.mandegar.adapter


import android.content.Context
import android.os.Build
import android.support.v7.widget.RecyclerView
import android.text.Html
import android.text.Html.fromHtml
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import android.widget.TextView
import co.romero.mandegar.R
import co.romero.mandegar.Util.Utils
import com.bumptech.glide.Glide
import android.text.Spanned
import android.widget.LinearLayout


class GroupAdapter(private val mContext: Context?, private val items: List<String>) : RecyclerView.Adapter<GroupAdapter.CustomViewHolder>() {


    private var onItemClickListener: OnItemClickListener? = null
    private var utils: Utils? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_group, null)
        this.utils = Utils.getInstance(mContext)

        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(customViewHolder: CustomViewHolder, i: Int) {
        val messageItem = items[i]

//        Glide.with(mContext!!).load(messageItem).into(customViewHolder.cover!!)

        val listener = View.OnClickListener { onItemClickListener!!.onItemClick(messageItem) }

        customViewHolder.ll_click!!.setOnClickListener(listener)

//        customViewHolder.tv_text!!.text = fromHtml("<b>aliromero</b> تصویر شما را میپسندد.<font color='#A0A4A2'><small> 2m</small></font>")

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
//            var tv_text: TextView? = null


            init {
                this.ll_click = view.findViewById(R.id.ll_click)
//                this.tv_text = view.findViewById(R.id.tv_text)

            }
        }

        fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
            this.onItemClickListener = onItemClickListener

        }


        interface OnItemClickListener {
            fun onItemClick(item: String)
        }

    }