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




class SearchFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var index: String? = null
    private var tab: Int? = null
    private var utils: Utils? = null


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            index = it.getString("index")
            utils = Utils.getInstance(context)

        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment

        val view = inflater.inflate(R.layout.fragment_search, container, false)
        initUi(view)
       return view




    }




    private fun initUi(view: View) {

    }



}
