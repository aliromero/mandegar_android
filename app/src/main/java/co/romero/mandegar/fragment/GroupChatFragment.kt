package co.romero.mandegar.fragment

import android.os.Bundle
import android.os.Handler
import android.support.v4.app.Fragment
import android.support.v7.widget.GridLayoutManager
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Toast
import co.romero.mandegar.R
import co.romero.mandegar.Util.Utils
import co.romero.mandegar.activity.MainActivity
import co.romero.mandegar.adapter.GalleryAdapter
import co.romero.mandegar.adapter.NotificationAdapter
import co.romero.mandegar.adapter.PostSliderAdapter
import kotlinx.android.synthetic.main.fragment_social.view.*
import kotlinx.android.synthetic.main.fragment_social_post_image.view.*
import com.baoyz.widget.PullRefreshLayout
import kotlinx.android.synthetic.main.fragment_notification.view.*


class GroupChatFragment : Fragment() {
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
            "group" -> {
                view = inflater.inflate(R.layout.activity_chats, container, false)
//                showNotifications(view)
                view
            }

            else -> null

        }

    }


    private fun initUi(view: View) {



        val data: MutableList<String> = mutableListOf()
        data.add(0,"https://www.ekito.fr/people/wp-content/uploads/2017/01/sample-1.jpg")
        data.add(1,"http://cdn3.nflximg.net/images/3093/2043093.jpg")
        data.add(2,"https://www.ekito.fr/people/wp-content/uploads/2017/01/sample-1.jpg")

        data.add(3,"http://www.cameraegg.org/wp-content/uploads/2013/03/Canon-EOS-100D-Rebel-SL1-Sample-Image.jpg")

        data.add(4,"https://www.ekito.fr/people/wp-content/uploads/2017/01/sample-1.jpg")

        data.add(5,"http://cdn3.nflximg.net/images/3093/2043093.jpg")
        data.add(6,"http://www.cameraegg.org/wp-content/uploads/2013/03/Canon-EOS-100D-Rebel-SL1-Sample-Image.jpg")

        data.add(7,"http://cdn3.nflximg.net/images/3093/2043093.jpg")
        data.add(8,"http://www.cameraegg.org/wp-content/uploads/2013/03/Canon-EOS-100D-Rebel-SL1-Sample-Image.jpg")
        data.add(9,"https://www.ekito.fr/people/wp-content/uploads/2017/01/sample-1.jpg")
        data.add(10,"http://cdn3.nflximg.net/images/3093/2043093.jpg")


        data.add(11,"http://cdn3.nflximg.net/images/3093/2043093.jpg")
        data.add(12,"http://www.cameraegg.org/wp-content/uploads/2013/03/Canon-EOS-100D-Rebel-SL1-Sample-Image.jpg")
        data.add(13,"https://www.ekito.fr/people/wp-content/uploads/2017/01/sample-1.jpg")
        data.add(14,"http://cdn3.nflximg.net/images/3093/2043093.jpg")
        data.add(15,"http://cdn3.nflximg.net/images/3093/2043093.jpg")




        val booksAdapter = GalleryAdapter(context,data)


//        val manager = SpannedGridLayoutManager(
//                SpannedGridLayoutManager.GridSpanLookup { position ->
//                    // Conditions for 2x2 items
//                    if (position % 6 == 0 || position % 6 == 4) {
//                        SpannedGridLayoutManager.SpanInfo(2, 2)
//                    } else {
//                        SpannedGridLayoutManager.SpanInfo(1, 1)
//                    }
//                },
//                3, // number of columns
//                1f // how big is default item
//        )
//
        view.rv_pic.layoutManager = GridLayoutManager(context,2)

        view.rv_pic.adapter = booksAdapter


        booksAdapter.setOnItemClickListener(object: GalleryAdapter.OnItemClickListener {
            override fun onItemClick(item: String) {
                val main = activity as MainActivity

                main.showHideActions(false,false,false)
                main.displayFragment(main.getFragment(main.socialPostFragment,"index","postId","postImage","2"),"شبکه اجتماعی")

            }
        })



        view.rv_pic.addOnScrollListener(object : RecyclerView.OnScrollListener() {
            override fun onScrolled(recyclerView: RecyclerView?, dx: Int, dy: Int) {
                super.onScrolled(recyclerView, dx, dy)
                if (dy > 0 && view.fb_add.visibility == View.VISIBLE) {
                    view.fb_add.hide()
                } else if (dy < 0 &&  view.fb_add.visibility != View.VISIBLE) {
                    view.fb_add.show()
                }
            }
        })



        view.swipeRefreshLayout.setOnRefreshListener(PullRefreshLayout.OnRefreshListener {
            Handler().postDelayed({
                /* Create an Intent that will start the Menu-Activity. */
                view.swipeRefreshLayout.setRefreshing(false)
            }, 1500)
        })


    }

    private fun showImagePost(view: View,postId:Int)
    {

//        view.iv_back.setOnClickListener {
//            val main = activity as MainActivity
//            main.backToSocial()
//        }
        view.tv_description.text = "لورم ایپسوم متن ساختگی با تولید سادگی نامفهوم از صنعت چاپ، و با استفاده از طراحان گرافیک است، چاپگرها و متون بلکه روزنامه و مجله در ستون و سطرآنچنان که لازم است، و برای شرایط فعلی تکنولوژی مورد نیاز، و کاربردهای متنوع با هدف بهبود ابزارهای کاربردی می باشد، کتابهای زیادی در شصت و سه درصد گذشته حال و آینده، شناخت فراوان جامعه و متخصصان را می طلبد، تا با نرم افزارها شناخت بیشتری را برای طراحان رایانه ای علی الخصوص طراحان خلاقی، و فرهنگ پیشرو در زبان فارسی ایجاد کرد، در این صورت می توان امید داشت که تمام و دشواری موجود در ارائه راهکارها، و شرایط سخت تایپ به پایان رسد و زمان مورد نیاز شامل حروفچینی دستاوردهای اصلی، و جوابگوی سوالات پیوسته اهل دنیای موجود طراحی اساسا مورد استفاده قرار گیرد."

        if (postId == 5) {
            view.cover_single.visibility = View.GONE
            val data: MutableList<String> = mutableListOf()
            data.add(0, "https://www.ekito.fr/people/wp-content/uploads/2017/01/sample-1.jpg")
            data.add(1, "http://cdn3.nflximg.net/images/3093/2043093.jpg")
            data.add(2, "https://www.ekito.fr/people/wp-content/uploads/2017/01/sample-1.jpg")

            view.pager.adapter = PostSliderAdapter(context, data)
            view.indicator.setViewPager(view.pager)
        } else {

            view.pager.visibility = View.GONE
            view.indicator.visibility = View.GONE
        }


    }

    private fun showNotifications(view: View)
    {
        val data: MutableList<String> = mutableListOf()
        data.add(0, "https://www.ekito.fr/people/wp-content/uploads/2017/01/sample-1.jpg")
        data.add(1, "http://cdn3.nflximg.net/images/3093/2043093.jpg")
        data.add(2, "https://www.ekito.fr/people/wp-content/uploads/2017/01/sample-1.jpg")

        val booksAdapter = NotificationAdapter(context,data)
        view.rv_notification.layoutManager = LinearLayoutManager(context)
        view.rv_notification.adapter = booksAdapter

    }


    companion object {


        fun newInstance(index: String, tab: Int = 0,postId: Int = 0): GroupChatFragment {
            val fragment = GroupChatFragment()
            val b = Bundle()
            b.putString("index", index)
            b.putInt("tab", tab)
            b.putInt("postId", postId)
            fragment.arguments = b
            return fragment
        }
    }

}
