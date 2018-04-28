package co.romero.mandegar.activity

import android.animation.Animator
import android.animation.AnimatorListenerAdapter
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.DefaultItemAnimator
import android.support.v7.widget.LinearLayoutManager
import android.text.Editable
import android.text.TextUtils
import android.text.TextWatcher
import android.view.View
import android.widget.Toast
import co.romero.mandegar.R
import co.romero.mandegar.Util.NotificationUtils
import co.romero.mandegar.Util.Utils
import co.romero.mandegar.adapter.ChatRoomThreadAdapter
import co.romero.mandegar.app.Config
import co.romero.mandegar.model.Message

import kotlinx.android.synthetic.main.activity_chats.*
import java.util.ArrayList


class ChatsActivity : AppCompatActivity() {
    private lateinit var mRegistrationBroadcastReceiver: BroadcastReceiver
    private lateinit var messageArrayList: ArrayList<Message>
    private lateinit var mAdapter: ChatRoomThreadAdapter


    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)

        setContentView(R.layout.activity_chats)
        tv_title.text = "گروه عمومی"
        messageArrayList = ArrayList<Message>()
        // self user id is to identify the message owner
        val selfUserId = Utils.getInstance(applicationContext)!!.getCustomerId()
       mAdapter = ChatRoomThreadAdapter(this, messageArrayList, selfUserId)
        val layoutManager = LinearLayoutManager(this)
        rv_chats.layoutManager = layoutManager
        rv_chats.itemAnimator = DefaultItemAnimator()
        rv_chats.adapter = mAdapter



        mRegistrationBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == Config.PUSH_NOTIFICATION) {
                    // new push message is received
//                    handlePushNotification(intent)
                }
            }
        }


        btn_send.setOnClickListener {
            sendMessage()
        }
        et_message.addTextChangedListener(textWatcher)

        iv_close.setOnClickListener {
            finish()
            overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        }
    }

    var textWatcher:TextWatcher = object: TextWatcher {
        override fun beforeTextChanged(s:CharSequence, start:Int, count:Int, after:Int) {}
        override fun onTextChanged(s:CharSequence, start:Int, before:Int, count:Int) {}
        override fun afterTextChanged(s:Editable) {
           if (s.isNotEmpty()) {
               hideAction(true)
           } else {
               hideAction(false)
           }
        }
    }

    private fun hideAction(animate:Boolean) {
        if (animate) {




            btn_file.animate()
                    .setDuration(100)
                    .translationX(100f)
                    .setListener(object:AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation:Animator) {
                            super.onAnimationEnd(animation)
                            btn_file.visibility = View.GONE
                            btn_send.visibility = View.VISIBLE
                            btn_send.animate()
                                    .translationX(0f)

                                    .setListener(object:AnimatorListenerAdapter() {
                                    })


                        }
                    })

        } else {
            btn_send.animate()
                    .setDuration(100)
                    .translationX(100f)

                    .setListener(object:AnimatorListenerAdapter() {
                        override fun onAnimationEnd(animation:Animator) {
                            super.onAnimationEnd(animation)
                            btn_send.visibility = View.GONE
                            btn_file.animate()
                                    .translationX(0f)
                                    .setListener(object:AnimatorListenerAdapter() {
                                        override fun onAnimationEnd(animation:Animator) {
                                            super.onAnimationEnd(animation)
                                            btn_file.visibility = View.VISIBLE
                                            btn_send.visibility = View.GONE
                                        }
                                    })


                        }
                    })
        }

    }
    private fun sendMessage() {
        val text_message = et_message.text.toString().trim({ it <= ' ' })

        if (TextUtils.isEmpty(text_message)) {
            Toast.makeText(applicationContext, "Enter a message", Toast.LENGTH_SHORT).show()
            return
        }

        et_message.setText("")

        val message = Message()
        message.id = "1"
        message.message = text_message
        message.createdAt = ""


        messageArrayList.add(message)

        mAdapter.notifyDataSetChanged()
        if (mAdapter.itemCount > 1) {
            // scrolling to bottom of the recycler view
            rv_chats.layoutManager.smoothScrollToPosition(rv_chats, null, mAdapter.itemCount - 1)
        }


    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        super.onBackPressed()
    }
    override fun onPause() {
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver)
        super.onPause()
    }


    override fun onResume() {
        super.onResume()

        LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                IntentFilter(Config.PUSH_NOTIFICATION))

        NotificationUtils.clearNotifications(applicationContext)
    }
}


