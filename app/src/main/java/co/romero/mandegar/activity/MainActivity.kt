package co.romero.mandegar.activity

import android.annotation.SuppressLint
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.graphics.Color
import android.graphics.drawable.ColorDrawable
import android.os.Bundle
import android.support.v4.app.Fragment
import android.support.v4.content.LocalBroadcastManager
import android.support.v7.app.AlertDialog
import android.support.v7.app.AppCompatActivity
import android.view.Gravity
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import android.widget.Toast
import co.romero.mandegar.Constant.Constants.*
import co.romero.mandegar.R
import co.romero.mandegar.Util.NotificationUtils
import co.romero.mandegar.Util.Utils
import co.romero.mandegar.app.Config
import co.romero.mandegar.fragment.*
import co.romero.mandegar.helper.BottomNavigationViewHelper
import com.google.firebase.messaging.FirebaseMessaging
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.android.synthetic.main.drawer.*


class MainActivity : AppCompatActivity() {

    private lateinit var homeFragment: HomeFragment
    lateinit var testFragment: HomeFragment
    lateinit var tutorialFragment: TutorialFragment
    lateinit var socialFragment: SocialFragment
    lateinit var socialPostFragment: SocialFragment
    lateinit var notificationFragment: SocialFragment
    lateinit var groupFragment: GroupChatFragment
    lateinit var supportFragment: SupportFragment
    lateinit var searchFragment: SearchFragment
    internal lateinit var mRegistrationBroadcastReceiver: BroadcastReceiver

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        homeFragment = HomeFragment.newInstance("pager")
        testFragment = HomeFragment.newInstance("ee")
        tutorialFragment = TutorialFragment.newInstance("pager")
        socialFragment = SocialFragment.newInstance("pager")
        socialPostFragment = SocialFragment.newInstance("postImage", 0, 0)
        notificationFragment = SocialFragment.newInstance("notification", 0, 0)
        groupFragment = GroupChatFragment.newInstance("group", 0, 0)
        supportFragment = SupportFragment.newInstance("groups", 0, 0)
        searchFragment = SearchFragment()
        initUi()


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

    fun getFragment(fragment: Fragment, argType1: String, argType2: String, arg1: String, arg2: String): Fragment {
        val args = Bundle()
        args.putString(argType1, arg1)
        args.putInt(argType2, arg2.toInt())
        fragment.arguments = args
        return fragment
    }

    @SuppressLint("RestrictedApi")
    private fun initUi() {

        setSupportActionBar(toolbar)
        iv_menu.setOnClickListener {
            drawer_layout.openDrawer(Gravity.START, true)
        }

        iv_social.setOnClickListener {
            displayFragment(notificationFragment, "اعلانات")

        }

        iv_search.setOnClickListener {
            displayFragment(searchFragment, "جستجو در آموزش ها")
            showHideActions(false,true,false)
        }



        ll_logout.setOnClickListener {
            drawer_layout.closeDrawers()
          promp_logout()
        }




        btn_social.setOnClickListener {

            if (socialPostFragment.isAdded && socialPostFragment.isHidden) {
                displayFragment(socialPostFragment, "شبکه اجتماعی", false)
            } else {
                backToSocial()
            }

            bottom_bar.selectedItemId = R.id.empty
            btn_social.setBackgroundResource(R.drawable.background_oval_black_border)
            iv_search.visibility = View.GONE
            iv_filter.visibility = View.GONE
            fl_notif.visibility = View.VISIBLE
        }






        BottomNavigationViewHelper.disableShiftMode(bottom_bar)

        displayFragment(homeFragment, getString(R.string.my_app_name))
        iv_filter.visibility = View.GONE
        fl_notif.visibility = View.GONE


        bottom_bar.setOnNavigationItemSelectedListener {
            if (it.itemId == R.id.action_home) {
                displayFragment(homeFragment, getString(R.string.my_app_name))
                iv_search.visibility = View.VISIBLE
                iv_filter.visibility = View.GONE
                fl_notif.visibility = View.GONE
            }
            if (it.itemId == R.id.action_tutorial) {
                displayFragment(tutorialFragment, AMOOZESH)
                iv_search.visibility = View.VISIBLE
                iv_filter.visibility = View.GONE
                fl_notif.visibility = View.GONE
            }
            if (it.itemId == R.id.action_support) {
                displayFragment(supportFragment, POSHTIBANI)
                iv_search.visibility = View.GONE
                iv_filter.visibility = View.GONE
                fl_notif.visibility = View.GONE
            }
            if (it.itemId == R.id.action_shop) {
                displayFragment(testFragment, FOROSHGAH)
                iv_search.visibility = View.GONE
                iv_filter.visibility = View.GONE
                fl_notif.visibility = View.GONE
            }


            btn_social.setBackgroundResource(R.drawable.background_oval_white_border)
            true
        }
    }


    fun backToSocial() {
        displayFragment(socialFragment, "شبکه اجتماعی", true)
        bottom_bar.selectedItemId = R.id.empty
        btn_social.setBackgroundResource(R.drawable.background_oval_black_border)
        iv_search.visibility = View.GONE
        iv_filter.visibility = View.GONE
        fl_notif.visibility = View.VISIBLE

    }

    fun showHideActions(search: Boolean, filter: Boolean, lock: Boolean) {
        if (search) {
            iv_search.visibility = View.VISIBLE
        } else {
            iv_search.visibility = View.GONE
        }
        if (filter) {
            iv_filter.visibility = View.VISIBLE
        } else {
            iv_filter.visibility = View.GONE
        }


    }

    fun displayFragment(newFragment: Fragment?, title: String = getString(R.string.my_app_name), removeFragment: Boolean = false) {

        if (newFragment != null) {

            val frgManager = supportFragmentManager
            val fragmentTransaction = frgManager.beginTransaction()
            if (homeFragment.isAdded) {
                fragmentTransaction.hide(homeFragment)
            }
            if (testFragment.isAdded) {
                fragmentTransaction.hide(testFragment)

            }

            if (searchFragment.isAdded) {
                fragmentTransaction.hide(searchFragment)

            }


            if (tutorialFragment.isAdded) {
                fragmentTransaction.hide(tutorialFragment)

            }

            if (socialFragment.isAdded) {
                fragmentTransaction.hide(socialFragment)

            }
            if (notificationFragment.isAdded) {
                fragmentTransaction.hide(notificationFragment)

            }


            if (supportFragment.isAdded) {
                fragmentTransaction.hide(supportFragment)
            }


            if (socialPostFragment.isAdded) {
                if (removeFragment)
                    fragmentTransaction.remove(socialPostFragment)
                else
                    fragmentTransaction.hide(socialPostFragment)

            }
            if (newFragment.isAdded) {
                fragmentTransaction.show(newFragment)
            } else {
                fragmentTransaction.add(R.id.contentContainer, newFragment)
            }

            tv_title.text = title


            fragmentTransaction.commit()
//            frgManager.executePendingTransactions()

        }
    }


    override fun onBackPressed() {
        if (socialPostFragment.isAdded && socialPostFragment.isHidden) {
            displayFragment(socialPostFragment, "شبکه اجتماعی", false)
        } else if (homeFragment.isVisible || tutorialFragment.isVisible || testFragment.isVisible || supportFragment.isVisible) {
            super.onBackPressed()
        }else if (socialPostFragment.isVisible) {
            backToSocial()
        } else if (socialFragment.isAdded && socialFragment.isHidden) {
            backToSocial()
        } else if (socialFragment.isVisible) {
            super.onBackPressed()
            val frgManager = supportFragmentManager
            val fragmentTransaction = frgManager.beginTransaction()
            fragmentTransaction.remove(socialFragment)
            fragmentTransaction.commit()

        }  else {
            super.onBackPressed()
        }


    }


    private fun promp_logout()
    {
        val inflater = getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater
        val builder = AlertDialog.Builder(this)
        val promptView = inflater.inflate(R.layout.custom_dialog_two_btn, null)
        val dialog = builder.setView(promptView).create()
        val tv_title = promptView.findViewById<TextView>(R.id.tv_title)
        val tv_content = promptView.findViewById<TextView>(R.id.tv_content)
        val tv_negative = promptView.findViewById<TextView>(R.id.tv_negative)
        val tv_positive = promptView.findViewById<TextView>(R.id.tv_positive)
        val ll_positive = promptView.findViewById<LinearLayout>(R.id.ll_positive)
        val ll_negative = promptView.findViewById<LinearLayout>(R.id.ll_negative)
        tv_title.text = "خروج حساب کاربری"
        tv_content.text = "قصد خروج از حساب کاربری را دارید ؟"
        tv_negative.text = "انصراف"
        tv_positive.text = "بله"


        ll_positive.setOnClickListener {
            Utils.getInstance(applicationContext)!!.setLogOut()
            startActivity(Intent(this,SignUpActivity::class.java))
            finish()
        }

        ll_negative.setOnClickListener {
            dialog.dismiss()
        }
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        dialog.window!!.attributes = lp
        dialog.show()
    }


    private fun two_user()
    {
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

            startActivity(Intent(this,SignUpActivity::class.java))
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
}


