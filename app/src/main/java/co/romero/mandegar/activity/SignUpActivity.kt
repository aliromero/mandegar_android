package co.romero.mandegar.activity

import android.content.Intent
import android.os.Bundle
import android.os.PersistableBundle
import android.support.v4.app.Fragment
import android.support.v7.app.AppCompatActivity
import android.view.View
import co.romero.mandegar.R
import co.romero.mandegar.Util.Utils
import co.romero.mandegar.fragment.SignUpFragment
import com.google.firebase.iid.FirebaseInstanceId
import kotlinx.android.synthetic.main.activity_signup.*


class SignUpActivity : AppCompatActivity() {

    lateinit var enterMobileFragment: SignUpFragment
    lateinit var enterEmailFragment: SignUpFragment
    lateinit var enterCodeFragment: SignUpFragment
    lateinit var enterNameFragment: SignUpFragment
    lateinit var enterImageFragment: SignUpFragment
    lateinit var enterPasswordFragment: SignUpFragment


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_signup)
        enterMobileFragment = SignUpFragment.newInstance("enter_mobile")
        enterEmailFragment = SignUpFragment.newInstance("enter_email")
        enterCodeFragment = SignUpFragment.newInstance("enter_code")
        enterNameFragment = SignUpFragment.newInstance("enter_name")
        enterImageFragment = SignUpFragment.newInstance("enter_image")
        enterPasswordFragment = SignUpFragment.newInstance("enter_password")

        initUi()

    }

    fun getFragment(fragment: Fragment, argType1: String, argType2: String, arg1: String, arg2: String, argType3: String? = "", arg3: String? = ""): Fragment {
        val args = Bundle()
        args.putString(argType1, arg1)
        args.putString(argType3, arg3)
        args.putInt(argType2, arg2.toInt())
        fragment.arguments = args
        return fragment
    }

    private fun initUi() {

        setSupportActionBar(toolbar)
        displayFragment(enterEmailFragment)

        iv_close.setOnClickListener {
            finish()
        }

        if (FirebaseInstanceId.getInstance().token != null)
            Utils.getInstance(applicationContext)!!.setRegId(FirebaseInstanceId.getInstance().token!!)
        else {
            startActivity(Intent(this, SplashActivity::class.java))
            finish()
        }

    }


    fun displayFragment(newFragment: Fragment?, title: String = "ثبت نام/ورود") {

        if (newFragment != null) {

            val frgManager = supportFragmentManager
            val fragmentTransaction = frgManager.beginTransaction()
            if (enterMobileFragment.isAdded) {
                fragmentTransaction.remove(enterMobileFragment)
            }
            if (enterEmailFragment.isAdded) {
                fragmentTransaction.remove(enterEmailFragment)

            }
            if (enterCodeFragment.isAdded) {
                fragmentTransaction.remove(enterCodeFragment)

            }
            if (enterNameFragment.isAdded) {
                fragmentTransaction.hide(enterNameFragment)
            }

            if (enterImageFragment.isAdded) {
                fragmentTransaction.hide(enterImageFragment)
            }
            if (enterPasswordFragment.isAdded) {
                fragmentTransaction.hide(enterPasswordFragment)
            }

            if (newFragment.isAdded) {
                fragmentTransaction.show(newFragment)
            } else {
                fragmentTransaction.add(R.id.contentContainer, newFragment)
            }

            tv_title.text = title


//            fragmentTransaction.commitAllowingStateLoss()
            fragmentTransaction.commit()
//            frgManager.executePendingTransactions()

        }
    }


}


