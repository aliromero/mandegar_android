package co.romero.mandegar.fragment

import android.graphics.PorterDuff
import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.romero.mandegar.R
import co.romero.mandegar.Util.Utils
import co.romero.mandegar.activity.MainActivity
import co.romero.mandegar.adapter.GalleryAdapter
import co.romero.mandegar.adapter.GroupAdapter
import co.romero.mandegar.adapter.NotificationAdapter
import co.romero.mandegar.adapter.PostSliderAdapter
import com.baoyz.widget.PullRefreshLayout
import kotlinx.android.synthetic.main.fragment_enter_mobile.view.*
import kotlinx.android.synthetic.main.fragment_notification.view.*
import kotlinx.android.synthetic.main.fragment_social.view.*
import kotlinx.android.synthetic.main.fragment_social_post_image.view.*
import kotlinx.android.synthetic.main.fragment_support.view.*


class SupportFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var index: String? = null
    private var tab: Int? = null
    private var postId: Int? = null
    private var utils: Utils? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            index = it.getString("index")
            tab = it.getInt("tab")
            postId = it.getInt("postId")
            utils = Utils.getInstance(context)

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View
        return when (index) {

            "groups" -> {
                view = inflater.inflate(R.layout.fragment_support, container, false)
                showGroups(view)
                view
            }
            else -> null
        }
    }

    private fun showGroups(view:View)
    {

        val data: MutableList<String> = mutableListOf()
        data.add(0, "https://www.ekito.fr/people/wp-content/uploads/2017/01/sample-1.jpg")
        data.add(1, "http://cdn3.nflximg.net/images/3093/2043093.jpg")
        data.add(2, "https://www.ekito.fr/people/wp-content/uploads/2017/01/sample-1.jpg")

        val booksAdapter = GroupAdapter(context,data)
        view.recyclerView.layoutManager = LinearLayoutManager(context)
        view.recyclerView.adapter = booksAdapter
    }





    companion object {


        fun newInstance(index: String, tab: Int = 0,postId: Int = 0): SupportFragment {
            val fragment = SupportFragment()
            val b = Bundle()
            b.putString("index", index)
            b.putInt("tab", tab)
            b.putInt("postId", postId)
            fragment.arguments = b
            return fragment
        }
    }

}
