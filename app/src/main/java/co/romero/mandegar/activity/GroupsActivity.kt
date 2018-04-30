package co.romero.mandegar.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import co.romero.mandegar.R
import co.romero.mandegar.Util.NotificationUtils
import co.romero.mandegar.Util.Utils
import co.romero.mandegar.adapter.GroupAdapter
import co.romero.mandegar.app.Config
import co.romero.mandegar.interfaces.RespoDataInterface
import co.romero.mandegar.model.ChatRoom
import co.romero.mandegar.network.EndPoints
import co.romero.mandegar.response.Respo
import com.facebook.stetho.Stetho
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.fragment_support.*


class GroupsActivity : AppCompatActivity() {
    private lateinit var utils: Utils
    internal lateinit var mRegistrationBroadcastReceiver: BroadcastReceiver
    private lateinit var endPoints: EndPoints
    private lateinit var groupList: MutableList<ChatRoom>
    private lateinit var adapter: GroupAdapter

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setContentView(R.layout.fragment_support)

        utils = Utils.getInstance(applicationContext)!!
        endPoints = EndPoints.getInstance(applicationContext)!!
        groupList = ChatRoom.listAll(ChatRoom::class.java)
        rv_groups.layoutManager = LinearLayoutManager(applicationContext)
        adapter = GroupAdapter(applicationContext, groupList)
        rv_groups.adapter = adapter
        adapter.setOnItemClickListener(object : GroupAdapter.OnItemClickListener {
            override fun onItemClick(item: ChatRoom) {
                val intent = Intent(this@GroupsActivity, ChatsActivity::class.java)
                intent.putExtra("chatroom_id",item.chatroomid)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
            }
        })

        endPoints.getGroups(object : RespoDataInterface {
            override fun data(response: Respo) {
                if (response.isStatus) {
                    for (my_group in response.groups) {
                        val model_group = ChatRoom.find(ChatRoom::class.java, "CHATROOMID = ?", my_group.id).asReversed()

                        if (model_group.size > 0) {
                            if (my_group.id.toInt() != model_group[0].chatroomid) {
                                val group = ChatRoom()
                                group.name = my_group.name
                                group.pic = my_group.pic
                                group.chatroomid = my_group.id.toInt()
                                if (my_group.message != null) {
                                    group.last_message = my_group.message.text
                                    group.time_last_message = my_group.message.created_at
                                }
                                group.save()
                            } else {

                                val group = ChatRoom.findById(ChatRoom::class.java, my_group.id.toInt())
                                group.name = my_group.name
                                group.pic = my_group.pic
                                group.chatroomid = my_group.id.toInt()
                                if (my_group.message != null) {
                                    group.last_message = my_group.message.text
                                    group.time_last_message = my_group.message.created_at
                                }
                                group.save()
                            }
                        } else {
                            val group = ChatRoom()
                            group.name = my_group.name
                            group.pic = my_group.pic
                            group.chatroomid = my_group.id.toInt()
                            if (my_group.message != null) {
                                group.last_message = my_group.message.text
                                group.time_last_message = my_group.message.created_at
                            }

                            group.save()

                        }
                    }


                    groupList = ChatRoom.listAll(ChatRoom::class.java)
                    rv_groups.adapter = adapter
                    adapter.notifyDataSetChanged()

                }
            }

            override fun status(msg: String?) {

            }
        })


//        adapter.setOnItemClickListener(object: GroupAdapter.OnItemClickListener {
//            override fun onItemClick(item: ChatRoom) {
//                startActivity(Intent(this@GroupsActivity,ChatsActivity::class.java))
//                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
//            }
//        })


        mRegistrationBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {

                // checking for type intent filter
                if (intent.action == Config.REGISTRATION_COMPLETE) {
                    // gcm successfully registered
                    // now subscribe to `global` topic to receive app wide notifications
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL)


                } else if (intent.action == Config.PUSH_NOTIFICATION) {
                    // new push notification is received

                    if (intent.getStringExtra("action") == "logout") {
                        two_user()
                    }
//                    txtMessage.setText(message)
                }
            }
        }


    }


    private fun two_user() {
        Utils.getInstance(applicationContext)!!.setLogOut()
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val builder = AlertDialog.Builder(this)
        builder.setCancelable(false)
        val promptView = inflater.inflate(R.layout.custom_dialog_two_btn, null)
        val dialog = builder.setView(promptView).create()
        val tv_title = promptView.findViewById<TextView>(R.id.tv_title)
        val tv_content = promptView.findViewById<TextView>(R.id.tv_content)
        val tv_negative = promptView.findViewById<TextView>(R.id.tv_negative)
        val tv_positive = promptView.findViewById<TextView>(R.id.tv_positive)
        val ll_positive = promptView.findViewById<LinearLayout>(R.id.ll_positive)
        val ll_negative = promptView.findViewById<LinearLayout>(R.id.ll_negative)
        tv_title.text = "هشدار امنیتی"
        tv_content.text = "یک دستگاه وارد حساب شما شد"
        tv_negative.text = "ورود مجدد"
        ll_positive.visibility = View.GONE


        ll_negative.setOnClickListener {
            dialog.dismiss()

            startActivity(Intent(this, SignUpActivity::class.java))
            finish()
        }
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        dialog.window!!.attributes = lp
        dialog.show()
    }


    override fun onResume() {
        super.onResume()

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                IntentFilter(Config.REGISTRATION_COMPLETE))

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                IntentFilter(Config.PUSH_NOTIFICATION))

        // clear the notification area when the app is opened
        NotificationUtils.clearNotifications(applicationContext)


    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver)
        super.onPause()
    }

}


