package co.romero.mandegar.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.romero.mandegar.Constant.Constants.*
import co.romero.mandegar.R
import co.romero.mandegar.Util.Utils
import co.romero.mandegar.adapter.PagerAdapter
import com.daimajia.slider.library.Animations.DescriptionAnimation
import com.daimajia.slider.library.SliderLayout
import com.daimajia.slider.library.SliderTypes.BaseSliderView
import com.daimajia.slider.library.SliderTypes.TextSliderView
import com.daimajia.slider.library.Tricks.ViewPagerEx
import kotlinx.android.synthetic.main.fragment_home.view.*
import kotlinx.android.synthetic.main.pager_home.view.*
import co.romero.mandegar.adapter.BooksAdapter
import android.widget.GridView
import co.romero.mandegar.adapter.SimpleDemoAdapter
import kotlinx.android.synthetic.main.fragment_category.view.*
import org.zakariya.stickyheaders.SectioningAdapter
import co.romero.mandegar.R.id.recyclerView
import org.zakariya.stickyheaders.StickyHeaderLayoutManager




class TutorialFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var index: String? = null
    private var tab: Int? = null
    private var utils: Utils? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            index = it.getString("index")
            tab = it.getInt("tab")
            utils = Utils.getInstance(context)

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View
        return when (index) {
            KEY_PAGER -> {
                view = inflater.inflate(R.layout.pager_tutorial, container, false)
                pager(view)
                view
            }
            "1" -> {
                view = inflater.inflate(R.layout.fragment_category, container, false)
                initCategoryUi(view)
                view
            }
            "2" -> {
                view = inflater.inflate(R.layout.fragment_category, container, false)
                initCategoryUi(view)
                view
            }
            "3" -> {
                view = inflater.inflate(R.layout.fragment_category, container, false)
                initCategoryUi(view)
                view
            }

            else -> null

        }

    }

    private fun pager(view: View) {

        val pagerAdapter = PagerAdapter(childFragmentManager)
        pagerAdapter.addFragment(TutorialFragment.newInstance("2"), PHOTOSHOP)
        pagerAdapter.addFragment(TutorialFragment.newInstance("3"), VRAY)
        pagerAdapter.addFragment(TutorialFragment.newInstance("1"), MAX)


        view.view_pager.adapter = pagerAdapter
        view.tabs.setViewPager(view.view_pager)
        view.tabs.setTypeface(Utils.getInstance(context)!!.IranSans(), 0)
        view.tabs.isSmoothScrollWhenClickTab = true

        view.view_pager.currentItem = 3
        view.view_pager.offscreenPageLimit = 3
    }




    private fun initCategoryUi(view: View) {
        val adapter =  SimpleDemoAdapter(20, 5, false, false, true, true)
        view.recyclerView.layoutManager = StickyHeaderLayoutManager()
        view.recyclerView.adapter = adapter
    }

    companion object {


        fun newInstance(index: String, tab: Int = 0): TutorialFragment {
            val fragment = TutorialFragment()
            val b = Bundle()
            b.putString("index", index)
            b.putInt("tab", tab)
            fragment.arguments = b
            return fragment
        }
    }

}
