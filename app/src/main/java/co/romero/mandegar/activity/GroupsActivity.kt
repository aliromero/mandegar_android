package co.romero.mandegar.activity

import android.app.Activity
import android.content.Intent
import android.os.Bundle
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.widget.Toast
import co.romero.mandegar.R
import co.romero.mandegar.Util.Utils
import co.romero.mandegar.adapter.GroupAdapter
import kotlinx.android.synthetic.main.fragment_support.*


class GroupsActivity : AppCompatActivity() {
    private lateinit var utils: Utils


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_support)
        utils = Utils.getInstance(applicationContext)!!
        val myList: MutableList<String> = mutableListOf()
        val adapter = GroupAdapter(applicationContext,myList)
        myList.add("گروه عمومی")
        myList.add("گروه عمومی")
        recyclerView.layoutManager = LinearLayoutManager(applicationContext)
        recyclerView.adapter = adapter

        adapter.setOnItemClickListener(object: GroupAdapter.OnItemClickListener {
            override fun onItemClick(item: String) {
                startActivity(Intent(this@GroupsActivity,ChatsActivity::class.java))
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
            }
        })




    }


}


