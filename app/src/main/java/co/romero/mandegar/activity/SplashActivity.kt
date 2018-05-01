package co.romero.mandegar.activity

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.view.View
import android.view.WindowManager
import co.romero.mandegar.R
import co.romero.mandegar.Util.PrefManager
import co.romero.mandegar.Util.Utils
import kotlinx.android.synthetic.main.activity_splash.*


class SplashActivity : Activity() {

    private lateinit var mRegistrationBroadcastReceiver: BroadcastReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val prefManager = PrefManager(this)
        prefManager.isFirstTimeLaunch = false
        loadUi()
        Log.e("iiiii", "on create")
        data()








//        mRegistrationBroadcastReceiver = object : BroadcastReceiver() {
//            override fun onReceive(context: Context, intent: Intent) {
//                if (intent.action == Config.REGISTRATION_COMPLETE) {
//                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL)
//                    data()
//                }
//            }
//        }

        }




    private fun data() {
        var mainIntent: Intent

        var time = 1500
        val prefManager = PrefManager(this)
        if (prefManager.isFirstTimeLaunch) {
            time = 1500
        } else {
            time = 0
        }
        val t = object : Thread() {
            override fun run() {
                try {
                    var time = 0
                    while (time < time) {
                        Thread.sleep(100)
                        time += 100
                    }
                } catch (e: InterruptedException) {
                    // do nothing
                } finally {

                    mainIntent = if (Utils.getInstance(this@SplashActivity)!!.get_api_token() != "") {
                        Intent(applicationContext, GroupsActivity::class.java)
                    } else {
                        Intent(applicationContext, SignUpActivity::class.java)
                    }

                    startActivity(mainIntent)
                    finish()
                }
            }
        }
        t.start()



    }

    private fun loadUi() {
        window.setFlags(WindowManager.LayoutParams.FLAG_FULLSCREEN,
                WindowManager.LayoutParams.FLAG_FULLSCREEN)
        iv_sp.visibility = View.VISIBLE
        iv_sp.alpha = 0.0f
        iv_sp.animate()
                .alpha(1.0f)
                .translationYBy(0f)
                .translationY(-200f).duration = 500

    }

    override fun onPause() {
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onResume() {
        super.onResume()


    }




}


