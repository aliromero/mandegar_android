package co.romero.mandegar.activity

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v4.view.GestureDetectorCompat
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.view.*
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import co.romero.mandegar.R
import co.romero.mandegar.Util.NotificationUtils
import co.romero.mandegar.Util.Utils
import co.romero.mandegar.adapter.GroupAdapter
import co.romero.mandegar.app.Config
import co.romero.mandegar.gcm.GcmIntentService
import co.romero.mandegar.interfaces.RespoDataInterface
import co.romero.mandegar.model.ChatRoom
import co.romero.mandegar.model.Message2
import co.romero.mandegar.network.EndPoints
import co.romero.mandegar.response.Respo
import com.facebook.stetho.Stetho
import com.google.android.gms.common.ConnectionResult
import com.google.android.gms.common.GoogleApiAvailability
import kotlinx.android.synthetic.main.fragment_support.*
import java.util.ArrayList


class GroupsActivity : AppCompatActivity(), RecyclerView.OnItemTouchListener {
    override fun onTouchEvent(rv: RecyclerView?, e: MotionEvent?) {

    }

    override fun onInterceptTouchEvent(rv: RecyclerView?, e: MotionEvent?): Boolean {
        gestureDetector.onTouchEvent(e)
        return false
    }

    override fun onRequestDisallowInterceptTouchEvent(disallowIntercept: Boolean) {
    }

    private lateinit var utils: Utils
    internal lateinit var mRegistrationBroadcastReceiver: BroadcastReceiver
    private lateinit var endPoints: EndPoints
    private lateinit var groupList: MutableList<ChatRoom>
    private var adapter: GroupAdapter? = null
    private lateinit var gestureDetector: GestureDetectorCompat

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.fragment_support)
        utils = Utils.getInstance(applicationContext)!!
        endPoints = EndPoints.getInstance(applicationContext)!!
        groupList = ChatRoom.listAll(ChatRoom::class.java)

        initUi()



        mRegistrationBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                Log.e("iiiii", "ddddd")
                // checking for type intent filter
               if (intent.action == Config.PUSH_NOTIFICATION) {
                    Log.e("iiiii", "iiii")
                    // new push notification is received
                    handlePushNotification(intent)
                }
            }
        }

        subscribeToAllTopics()



        tv_status.text = "Updating..."
        endPoints.getGroups(object : RespoDataInterface {
            override fun data(response: Respo) {
                if (response.isStatus) {
                    for (my_group in response.groups) {
                        val model_groups = ChatRoom.find(ChatRoom::class.java, "CHATROOMID = ?", my_group.id).asReversed()

                        if (model_groups.size > 0) {
                            for (model_group in model_groups) {
                                if (my_group.id.toInt() != model_group.chatroomid) {
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
                                    if (model_group.pic != my_group.pic) {
                                        model_group.pic = my_group.pic
                                    }
                                    if(my_group.message != null) {
                                        if (model_group.last_message != my_group.message.text) {
                                            model_group.last_message = my_group.message.text
                                        }
                                    }
                                    if (!model_group.name.equals(my_group.name)) {
                                        model_group.name = my_group.name
                                    }
                                    model_group.save()
                                }
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
                    adapter?.setConversations(groupList)
                    tv_status.visibility = View.GONE

                }
            }

            override fun status(msg: String?) {
                tv_status.visibility = View.GONE
            }
        })


//        adapter.setOnItemClickListener(object: GroupAdapter.OnItemClickListener {
//            override fun onItemClick(item: ChatRoom) {
//                startActivity(Intent(this@GroupsActivity,ChatsActivity::class.java))
//                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
//            }
//        })


    }


    private fun initUi() {
        tv_status.visibility = View.VISIBLE
        tv_status.text = "Connecting..."
        val linearLayoutManager = LinearLayoutManager(applicationContext)
        linearLayoutManager.orientation = LinearLayoutManager.VERTICAL
        adapter = GroupAdapter(applicationContext, groupList)
        rv_groups.layoutManager = linearLayoutManager
        rv_groups.adapter = adapter
        rv_groups.itemAnimator = DefaultItemAnimator()
        rv_groups.itemAnimator.changeDuration = 0
        //fix slow recyclerview start
        rv_groups.setHasFixedSize(true)
        rv_groups.setItemViewCacheSize(10)
        rv_groups.isDrawingCacheEnabled = true
        rv_groups.drawingCacheQuality = View.DRAWING_CACHE_QUALITY_HIGH

        gestureDetector = GestureDetectorCompat(applicationContext, RecyclerViewBenOnGestureListener())





        adapter?.setOnItemClickListener(object : GroupAdapter.OnItemClickListener {
            override fun onItemClick(item: ChatRoom) {
                val intent = Intent(this@GroupsActivity, ChatsActivity::class.java)
                intent.putExtra("chatroom_id", item.chatroomid)
                startActivity(intent)
                overridePendingTransition(R.anim.slide_from_left, R.anim.slide_to_right)
            }
        })


    }

    // subscribing to global topic
    private fun subscribeToGlobalTopic() {
        val intent = Intent(this, GcmIntentService::class.java)
        intent.putExtra(GcmIntentService.KEY, GcmIntentService.SUBSCRIBE)
        intent.putExtra(GcmIntentService.TOPIC, Config.TOPIC_GLOBAL)
        startService(intent)
    }

    // Subscribing to all chat room topics
    // each topic name starts with `topic_` followed by the ID of the chat room
    // Ex: topic_1, topic_2
    private fun subscribeToAllTopics() {
        for (cr in groupList) {
            Log.i("aaaa","topic_" + cr.id)
            val intent = Intent(this, GcmIntentService::class.java)
            intent.putExtra(GcmIntentService.KEY, GcmIntentService.SUBSCRIBE)
            intent.putExtra(GcmIntentService.TOPIC, "topic_" + cr.id)
            startService(intent)
        }
    }

    private fun handlePushNotification(intent: Intent) {
        val type = intent.getIntExtra("type", -1)

        // if the push is of chat room message
        // simply update the UI unread messages count
        if (type == Config.PUSH_TYPE_CHATROOM) {
            val message = intent.getSerializableExtra("message") as Message2
            val chatRoomId = intent.getStringExtra("chat_room_id")

            if (message != null && chatRoomId != null) {
//                updateRow(chatRoomId, message)
            }
        } else if (type == Config.PUSH_TYPE_USER) {
            // push belongs to user alone
            // just showing the message in a toast
            val message = intent.getSerializableExtra("message") as Message2
            Toast.makeText(applicationContext, "New push: " + message.message, Toast.LENGTH_LONG).show()
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

        Log.e("iiiii", "resume")
        // register GCM registration complete receiver

        // register new push message receiver
        // by doing this, the activity will be notified each time a new message arrives
        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                IntentFilter(Config.PUSH_NOTIFICATION))

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                IntentFilter(Config.SENT_TOKEN_TO_SERVER))


        // clearing the notification tray
        co.romero.mandegar.gcm.NotificationUtils.clearNotifications()


    }

    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver)
        super.onPause()
    }



    private inner class RecyclerViewBenOnGestureListener : GestureDetector.SimpleOnGestureListener() {

        override fun onLongPress(e: MotionEvent) {


        }

    }

}


