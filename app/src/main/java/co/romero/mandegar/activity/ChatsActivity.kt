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
import android.util.Log
import android.view.View
import android.widget.Toast
import co.romero.mandegar.R
import co.romero.mandegar.Util.NotificationUtils
import co.romero.mandegar.Util.Utils
import co.romero.mandegar.adapter.ChatRoomThreadAdapter
import co.romero.mandegar.app.Config
import co.romero.mandegar.interfaces.RespoDataInterface
import co.romero.mandegar.model.Message
import co.romero.mandegar.network.EndPoints
import co.romero.mandegar.response.Respo
import kotlinx.android.synthetic.main.activity_chats.*


class ChatsActivity : AppCompatActivity() {
    private lateinit var messageArrayList: List<Message>
    private lateinit var mAdapter: ChatRoomThreadAdapter
    private lateinit var utils: Utils
    private lateinit var endPoints: EndPoints

    override fun onCreate(savedInstanceState: Bundle?) {

        super.onCreate(savedInstanceState)
        messageArrayList = Message.listAll(Message::class.java)
        setContentView(R.layout.activity_chats)
        if (!intent.hasExtra("chatroom_id")) {
            finish()
        }

        utils = Utils.getInstance(applicationContext)!!
        endPoints = EndPoints.getInstance(applicationContext)!!


        tv_title.text = "گروه عمومی"
        // self user id is to identify the message owner
        val selfUserId = Utils.getInstance(applicationContext)!!.getCustomerId()
       mAdapter = ChatRoomThreadAdapter(this, messageArrayList, selfUserId.toInt())
        val layoutManager = LinearLayoutManager(this)
        rv_chats.layoutManager = layoutManager
        rv_chats.itemAnimator = DefaultItemAnimator()
        rv_chats.adapter = mAdapter

        rv_chats.layoutManager.scrollToPosition(mAdapter.itemCount-1 )





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
        message.message = text_message
        message.customer_id = utils.getCustomerId().toInt()
        message.chatroom_id = intent.getIntExtra("chatroom_id",0)
        message.createdAt = ""

        message.save()

        messageArrayList = Message.listAll(Message::class.java)
        mAdapter = ChatRoomThreadAdapter(this, messageArrayList, utils.getCustomerId().toInt())
        mAdapter.notifyDataSetChanged()
        if (mAdapter.itemCount > 1) {
            // scrolling to bottom of the recycler view
            rv_chats.layoutManager.scrollToPosition(mAdapter.itemCount - 1)
        }


        endPoints.sendMessage(text_message,intent.getIntExtra("chatroom_id",0),object: RespoDataInterface {
            override fun data(response: Respo) {

            }

            override fun status(msg: String?) {
            }
        })


    }

    override fun onBackPressed() {
        finish()
        overridePendingTransition(R.anim.slide_from_right, R.anim.slide_to_left)
        super.onBackPressed()
    }
    override fun onPause() {
        super.onPause()
    }


    override fun onResume() {
        super.onResume()


    }
}


