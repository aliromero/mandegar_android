package co.romero.mandegar.adapter


import android.content.Context
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ImageView
import co.romero.mandegar.R
import co.romero.mandegar.Util.Utils
import com.bumptech.glide.Glide


class GalleryAdapter(private val mContext: Context?, private val items: List<String>) : RecyclerView.Adapter<GalleryAdapter.CustomViewHolder>() {


    private var onItemClickListener: OnItemClickListener? = null
    private var utils: Utils? = null

    override fun onCreateViewHolder(viewGroup: ViewGroup, i: Int): CustomViewHolder {
        val view = LayoutInflater.from(viewGroup.context).inflate(R.layout.item_gallery, null)
        this.utils = Utils.getInstance(mContext)

        return CustomViewHolder(view)
    }

    override fun onBindViewHolder(customViewHolder: CustomViewHolder, i: Int) {
        val messageItem = items[i]

        Glide.with(mContext!!).load(messageItem).into(customViewHolder.cover!!)

        val listener = View.OnClickListener { onItemClickListener!!.onItemClick(messageItem) }


        customViewHolder.cover!!.setOnClickListener(listener)
    }



    override fun getItemCount(): Int {
        return items.size
    }

    inner class CustomViewHolder(view: View) : RecyclerView.ViewHolder(view) {
        var cover: ImageView? = null


        init {
            this.cover = view.findViewById(R.id.iv_cover)

        }
    }

    fun setOnItemClickListener(onItemClickListener: OnItemClickListener) {
        this.onItemClickListener = onItemClickListener

    }


    interface OnItemClickListener {
        fun onItemClick(item: String)
    }

}