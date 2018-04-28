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




class HomeFragment : Fragment() {
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
                view = inflater.inflate(R.layout.pager_home, container, false)
                pager(view)
                view
            }
            KEY_AMOOZESH -> {
                view = inflater.inflate(R.layout.fragment_home, container, false)
                home(view)
                view
            }
            KEY_CAT_AMOOZESH -> {
                view = inflater.inflate(R.layout.fragment_category, container, false)
                initCategoryUi(view)
                view
            }
            else -> null

        }

    }

    private fun pager(view: View) {

        val pagerAdapter = PagerAdapter(childFragmentManager)
        pagerAdapter.addFragment(HomeFragment.newInstance("1"), ANGIZESHI)
        pagerAdapter.addFragment(HomeFragment.newInstance("2"), IDE_HAYE_MEMARI)
        pagerAdapter.addFragment(HomeFragment.newInstance("3"), FILE_HAYE_MEMARI)
        pagerAdapter.addFragment(HomeFragment.newInstance(KEY_AMOOZESH), SAFHE_NAKHOST)


        view.view_pager.adapter = pagerAdapter
        view.tabs.setViewPager(view.view_pager)
        view.tabs.setTypeface(Utils.getInstance(context)!!.IranSans(), 0)
        view.tabs.isSmoothScrollWhenClickTab = true

        view.view_pager.currentItem = 5
        view.view_pager.offscreenPageLimit = 4
    }


    private fun home(view: View) {
        val url_maps = HashMap<String, String>()
        url_maps["Big Bang Theory"] = "https://www.ekito.fr/people/wp-content/uploads/2017/01/sample-1.jpg"
        url_maps["House of Cards"] = "http://cdn3.nflximg.net/images/3093/2043093.jpg"
        url_maps["Game of Thrones"] = "http://www.cameraegg.org/wp-content/uploads/2013/03/Canon-EOS-100D-Rebel-SL1-Sample-Image.jpg"


        for (name in url_maps) {
            val textSliderView = TextSliderView(context)
            // initialize a SliderLayout
            textSliderView
                    .description(name.key)
                    .image(name.value)
                    .setScaleType(BaseSliderView.ScaleType.Fit)
                    .setOnSliderClickListener {

                    }

            //add your extra information
            textSliderView.bundle(Bundle())
            textSliderView.bundle
                    .putString("extra", name.key)

            view.slider.addSlider(textSliderView)
        }
        view.slider.setPresetTransformer(SliderLayout.Transformer.Default)
        view.slider.setPresetIndicator(SliderLayout.PresetIndicators.Center_Bottom)
        view.slider.setCustomAnimation(DescriptionAnimation())
        view.slider.setDuration(5000)
        view.slider.addOnPageChangeListener(object : ViewPagerEx.OnPageChangeListener {
            override fun onPageScrollStateChanged(state: Int) {

            }

            override fun onPageScrolled(position: Int, positionOffset: Float, positionOffsetPixels: Int) {

            }

            override fun onPageSelected(position: Int) {

            }
        })


        val booksAdapter = BooksAdapter(context, url_maps)
        view.gridview.adapter = booksAdapter


    }


    private fun initCategoryUi(view: View) {
        val adapter =  SimpleDemoAdapter(100, 5, false, false, true, true)
        view.recyclerView.layoutManager = StickyHeaderLayoutManager()
        view.recyclerView.adapter = adapter
    }

    companion object {


        fun newInstance(index: String, tab: Int = 0): HomeFragment {
            val fragment = HomeFragment()
            val b = Bundle()
            b.putString("index", index)
            b.putInt("tab", tab)
            fragment.arguments = b
            return fragment
        }
    }

}
