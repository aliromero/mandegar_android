package co.romero.mandegar.activity

import android.app.Activity
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.os.Bundle
import android.os.Handler
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AppCompatActivity
import android.view.View
import android.view.WindowManager
import co.romero.mandegar.R
import co.romero.mandegar.Util.PrefManager
import co.romero.mandegar.Util.Utils
import co.romero.mandegar.app.Config
import com.google.firebase.iid.FirebaseInstanceId
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_splash.*




class SplashActivity : Activity() {

    private lateinit var mRegistrationBroadcastReceiver: BroadcastReceiver
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_splash)
        val prefManager = PrefManager(this)
        prefManager.isFirstTimeLaunch = true
        loadUi()
        mRegistrationBroadcastReceiver = object : BroadcastReceiver() {
            override fun onReceive(context: Context, intent: Intent) {
                if (intent.action == Config.REGISTRATION_COMPLETE) {
                    FirebaseMessaging.getInstance().subscribeToTopic(Config.TOPIC_GLOBAL)
                    data()
                }
            }
        }






    }


    private fun data() {
        var mainIntent: Intent

        val t = object : Thread() {
            override fun run() {
                try {
                    var time = 0
                    while (time < 1500) {
                        Thread.sleep(100)
                        time += 100
                    }
                } catch (e: InterruptedException) {
                    // do nothing
                } finally {

                    mainIntent = if (Utils.getInstance(this@SplashActivity)!!.getCustomerId() != "") {
                        Intent(applicationContext, GroupsActivity::class.java)
                    } else {
                        Intent(applicationContext, SignUpActivity::class.java)
                    }
                    finish()
                    startActivity(mainIntent)
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
        LocalBroadcastManager.getInstance(this).unregisterReceiver(mRegistrationBroadcastReceiver)
        super.onPause()
    }

    override fun onDestroy() {
        super.onDestroy()
    }

    override fun onStart() {
        super.onStart()
        if (FirebaseInstanceId.getInstance().token != null) {
            Utils.getInstance(applicationContext)!!.setRegId(FirebaseInstanceId.getInstance().token!!)
            data()
        } else {
            LocalBroadcastManager.getInstance(this).registerReceiver(mRegistrationBroadcastReceiver,
                    IntentFilter(Config.REGISTRATION_COMPLETE))
        }
    }

}


