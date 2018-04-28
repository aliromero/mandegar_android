package co.romero.mandegar.fragment

import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import co.romero.mandegar.R
import co.romero.mandegar.Util.Utils
import co.romero.mandegar.adapter.SimpleDemoAdapter
import kotlinx.android.synthetic.main.fragment_category.view.*


class CategoryFragment : Fragment() {
    private var index: String? = null
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

        val view = inflater.inflate(R.layout.fragment_category, container, false)
        initUi(view)
        return view


    }


    private fun initUi(view: View) {

        view.recyclerView.layoutManager = LinearLayoutManager(context)
        view.recyclerView.adapter = SimpleDemoAdapter(5, 5, true, false, false, true)

    }

    companion object {


        fun newInstance(index: String): CategoryFragment {
            val fragment = CategoryFragment()
            val b = Bundle()
            b.putString("index", index)
            fragment.arguments = b
            return fragment
        }
    }

}
