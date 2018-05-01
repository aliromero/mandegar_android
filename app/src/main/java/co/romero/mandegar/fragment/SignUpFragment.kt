package co.romero.mandegar.fragment

import android.app.Activity
import android.content.Context
import android.content.Intent
import android.graphics.PorterDuff
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.WindowManager
import android.view.inputmethod.InputMethodManager
import co.romero.mandegar.R
import co.romero.mandegar.Util.Utils
import co.romero.mandegar.activity.SignUpActivity
import co.romero.mandegar.interfaces.RespoDataInterface
import co.romero.mandegar.response.Respo
import co.romero.mandegar.network.EndPoints
import kotlinx.android.synthetic.main.fragment_enter_code.view.*
import kotlinx.android.synthetic.main.fragment_enter_image.view.*
import kotlinx.android.synthetic.main.fragment_enter_mobile.view.*
import kotlinx.android.synthetic.main.fragment_enter_name.view.*

import kotlinx.android.synthetic.main.loading.*
import java.util.regex.Pattern
import android.os.CountDownTimer
import kotlinx.android.synthetic.main.fragment_enter_email.view.*
import android.telephony.TelephonyManager
import kotlinx.android.synthetic.main.fragment_enter_code.*
import java.util.*
import android.provider.MediaStore
import kotlinx.android.synthetic.main.fragment_enter_password.view.*
import android.text.method.HideReturnsTransformationMethod
import android.text.method.PasswordTransformationMethod
import co.romero.mandegar.activity.GroupsActivity
import com.bumptech.glide.Glide
import com.theartofdev.edmodo.cropper.CropImage
import java.io.IOException


class SignUpFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var index: String? = null
    private var exist_customer: Int? = null
    private var type: String? = null
    private var utils: Utils? = null
    private var endPoints: EndPoints? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            index = it.getString("index")
            exist_customer = it.getInt("exist_customer")
            type = it.getString("type")
            utils = Utils.getInstance(context)
            endPoints = EndPoints.getInstance(context)


        }
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        // Inflate the layout for this fragment
        val view: View
        return when (index) {
            "enter_mobile" -> {
                view = inflater.inflate(R.layout.fragment_enter_mobile, container, false)
                showEnterMobile(view)
                view
            }
            "enter_email" -> {
                view = inflater.inflate(R.layout.fragment_enter_email, container, false)
                showEnterEmail(view)
                view
            }
            "enter_code" -> {
                view = inflater.inflate(R.layout.fragment_enter_code, container, false)
                showEnterCode(view)
                view
            }

            "enter_name" -> {
                view = inflater.inflate(R.layout.fragment_enter_name, container, false)
                showEnterName(view)
                view
            }
            "enter_image" -> {
                view = inflater.inflate(R.layout.fragment_enter_image, container, false)
                showEnterImage(view)
                view
            }

            "enter_password" -> {
                view = inflater.inflate(R.layout.fragment_enter_password, container, false)
                showEnterPassword(view)
                view
            }
            else -> null

        }

    }

    private fun showEnterMobile(view: View) {

        view.et_mobile.background.mutate().setColorFilter(resources.getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP)
        view.et_pre_mobile.background.mutate().setColorFilter(resources.getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP)
        val main = activity as SignUpActivity
        view.btn_next1.setOnClickListener {
            pb_loading.visibility = View.VISIBLE
            val mobile_number = "0" + view.et_mobile.text.toString()
            if (isMobileValid(mobile_number)) {
                endPoints!!.checkMobile(mobile_number, utils!!.getRegId(), object : RespoDataInterface {
                    override fun data(response: Respo) {
                        pb_loading.visibility = View.GONE
                        if (response.isStatus) {
                            utils!!.set_email("")
                            utils!!.set_mobile(view.et_mobile.text.toString())
                            hideKeyboard(activity!!)
                            if (response.isExist_customer) {
                                startActivity(Intent(context, GroupsActivity::class.java))
                                activity!!.finish()
                            } else {
                                main.displayFragment(main.getFragment(main.enterNameFragment, "index", "none", "enter_name", "2"), "مرحله 3/4 - وارد کردن نام")
                            }

//                            if (response.isExist_customer) {
//                                main.displayFragment(main.getFragment(main.enterCodeFragment, "index", "exist_customer", "enter_code", "1","type","mobile"), "مرحله 2/2 - وارد کردن کد تایید")
//                            } else {
//                                main.displayFragment(main.getFragment(main.enterCodeFragment, "index", "exist_customer", "enter_code", "0","type","mobile"), "مرحله 2/4 - وارد کردن کد تایید")
//                            }


                        }
                    }

                    override fun status(msg: String?) {
                        pb_loading.visibility = View.GONE
                        if (msg!!.isNotEmpty()) {
                            val dialog = utils!!.show_alert(context!!, "خطا", msg)
                            dialog.show()
                            val width = (context!!.resources.displayMetrics.widthPixels * 0.85).toInt()
                            dialog.window!!.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
                        }
                    }
                })
            } else {
                pb_loading.visibility = View.GONE
                val dialog = utils!!.show_alert(context!!, "خطا در اطلاعات ارسالی", "شماره موبایل را درست وارد کنید")
                dialog.show()
                val width = (context!!.resources.displayMetrics.widthPixels * 0.85).toInt()
                dialog.window!!.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
            }

        }

        view.tv_external.setOnClickListener {

            val dialog = utils!!.show_alert(context!!, "خطا", "به زودی این قسمت راه اندازی خواهد شد")
            dialog.show()
            val width = (context!!.resources.displayMetrics.widthPixels * 0.85).toInt()
            dialog.window!!.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
//            pb_loading.visibility = View.VISIBLE
//
//            if (getUserCountry(context!!) == null) {
//                endPoints!!.checkCountry(object : RespoDataInterface {
//
//                    override fun data(response: Respo) {
//                        pb_loading.visibility = View.GONE
//                        if (response.country != "" && response.country != "Iran") {
//                            main.displayFragment(main.getFragment(main.enterEmailFragment, "index", "none", "enter_email", "2"))
//
//                        } else {
//                            val dialog = utils!!.show_alert(context!!, "خطا", "شما کاربر خارجی نیستید")
//                            dialog.show()
//                            val width = (context!!.resources.displayMetrics.widthPixels * 0.85).toInt()
//                            dialog.window!!.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
//                        }
//                    }
//
//                    override fun status(msg: String?) {
//                        pb_loading.visibility = View.GONE
//                        val dialog = utils!!.show_alert(context!!, "خطا", msg!!)
//                        dialog.show()
//                        val width = (context!!.resources.displayMetrics.widthPixels * 0.85).toInt()
//                        dialog.window!!.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
//                    }
//                })
//
//
//            } else {
//                pb_loading.visibility = View.GONE
//                if (getUserCountry(context!!) == "ir") {
//                    val dialog = utils!!.show_alert(context!!, "خطا", "شما کاربر خارجی نیستید")
//                    dialog.show()
//                    val width = (context!!.resources.displayMetrics.widthPixels * 0.85).toInt()
//                    dialog.window!!.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
//                } else {
//                    main.displayFragment(main.getFragment(main.enterEmailFragment, "index", "none", "enter_email", "2"))
//                }
//            }
        }
    }


    private fun showEnterEmail(view: View) {

        view.et_email.background.mutate().setColorFilter(resources.getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP)

        val main = activity as SignUpActivity
        view.btn_next0.setOnClickListener {
            pb_loading.visibility = View.VISIBLE

            if (isEmailValid(view.et_email.text.toString())) {
                endPoints!!.checkMail(view.et_email.text.toString(), getUserCountry(context!!)!!, utils!!.getRegId(), object : RespoDataInterface {
                    override fun data(response: Respo) {
                        pb_loading.visibility = View.GONE
                        if (response.isStatus) {
                            utils!!.set_mobile("")
                            utils!!.setCustomerId(response.customer.id)
                            hideKeyboard(activity!!)
                            if (response.isExist_customer) {
                                main.displayFragment(main.getFragment(main.enterPasswordFragment, "index", "exist_customer", "enter_password", "1", "type", "email"), "مرحله 2/2 - وارد کردن رمز عبور")
                            } else {
                                main.displayFragment(main.getFragment(main.enterPasswordFragment, "index", "exist_customer", "enter_password", "0", "type", "email"), "مرحله 2/2 - وارد کردن رمز عبور")
                            }
//                            if (response.isExist_customer) {
//                                main.displayFragment(main.getFragment(main.enterCodeFragment, "index", "exist_customer", "enter_code", "1", "type", "email"), "مرحله 2/2 - وارد کردن کد تایید")
//
//                            } else {
//                                main.displayFragment(main.getFragment(main.enterCodeFragment, "index", "exist_customer", "enter_code", "0", "type", "email"), "مرحله 2/4 - وارد کردن کد تایید")
//
//                            }
                            utils!!.set_email(view.et_email.text.toString())
                        }
                    }

                    override fun status(msg: String?) {
                        pb_loading.visibility = View.GONE
                        if (msg!!.isNotEmpty()) {
                            val dialog = utils!!.show_alert(context!!, "خطا", msg)
                            dialog.show()
                            val width = (context!!.resources.displayMetrics.widthPixels * 0.85).toInt()
                            dialog.window!!.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
                        }
                    }
                })
            } else {
                pb_loading.visibility = View.GONE
                val dialog = utils!!.show_alert(context!!, "خطا در اطلاعات ارسالی", "ایمیل خود را درست وارد کنید")
                dialog.show()
                val width = (context!!.resources.displayMetrics.widthPixels * 0.85).toInt()
                dialog.window!!.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
            }

        }


    }


    private fun showEnterPassword(view: View) {

        view.et_password.background.mutate().setColorFilter(resources.getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP)
        if (exist_customer == 1) {
            view.tv_forget.visibility = View.VISIBLE
            view.tv_passInfo.text = "برای ورود ، رمز عبور خود را وارد کنید"
        } else {
            view.tv_forget.visibility = View.GONE
            view.tv_passInfo.text = "برای ثبت نام ، یک رمز عبور وارد کنید"
        }

        view.tv_forget.setOnClickListener {
            pb_loading.visibility = View.VISIBLE
            endPoints!!.resetPass(utils!!.get_email(),object: RespoDataInterface {

                override fun data(response: Respo) {
                    pb_loading.visibility = View.GONE
                    if (response.isStatus) {
                        val dialog = utils!!.show_alert(context!!, "ارسال شد", "لینک نوسازی پسورد به ایمیلتان ارسال شد.")
                        dialog.show()
                        val width = (context!!.resources.displayMetrics.widthPixels * 0.85).toInt()
                        dialog.window!!.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)

                    }
                }

                override fun status(msg: String?) {
                    pb_loading.visibility = View.GONE
                    if (msg!!.isNotEmpty()) {
                        val dialog = utils!!.show_alert(context!!, "خطا", msg)
                        dialog.show()
                        val width = (context!!.resources.displayMetrics.widthPixels * 0.85).toInt()
                        dialog.window!!.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
                    }
                }
            })
        }
        var is_show = false
        view.btn_eye.setImageResource( R.drawable.ic_visibility_black_24dp)
        view.btn_eye.setOnClickListener {
            if (!is_show) {
                view.btn_eye.setImageResource( R.drawable.ic_visibility_off_black_24dp)
                view.et_password.transformationMethod = HideReturnsTransformationMethod.getInstance()
                is_show = true

            } else {
                view.btn_eye.setImageResource( R.drawable.ic_visibility_black_24dp)
                view.et_password.transformationMethod = PasswordTransformationMethod.getInstance()
                is_show = false

            }

        }





        val main = activity as SignUpActivity
        view.btn_next03.setOnClickListener {
            pb_loading.visibility = View.VISIBLE

            if (view.et_password.length() > 3) {

                endPoints!!.checkPass(utils!!.get_email(),view.et_password.text.toString(),utils!!.getRegId(),utils!!.getCustomerId(),object: RespoDataInterface {
                    override fun data(response: Respo) {
                        pb_loading.visibility = View.GONE
                        if (response.isStatus) {
                            hideKeyboard(activity!!)
                            utils!!.save_api_token(response.api_token)
                            when {
                                response.customer.name.isNullOrEmpty() -> main.displayFragment(main.getFragment(main.enterNameFragment, "index", "none", "enter_name", "2"), "مرحله 3/4 - وارد کردن نام")
                                response.customer.profiles.size == 0 -> main.displayFragment(main.getFragment(main.enterImageFragment, "index", "none", "enter_image", "2"), "مرحله 4/4 - انتخاب تصویر ")
                                else -> {
                                    startActivity(Intent(context, GroupsActivity::class.java))
                                    activity!!.finish()
                                }
                            }

                            utils!!.setCustomerId(response.customerId.toString())
                        }
                    }

                    override fun status(msg: String?) {
                        pb_loading.visibility = View.GONE
                        val dialog = utils!!.show_alert(context!!, "خطا", msg!!)
                        dialog.show()
                        val width = (context!!.resources.displayMetrics.widthPixels * 0.85).toInt()
                        dialog.window!!.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
                    }
                })

            } else {
                pb_loading.visibility = View.GONE
                val dialog = utils!!.show_alert(context!!, "خطا در اطلاعات ارسالی", "پسورد انتخابی باید بیش از 3 عدد باشد")
                dialog.show()
                val width = (context!!.resources.displayMetrics.widthPixels * 0.85).toInt()
                dialog.window!!.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
            }

        }


    }


    fun getUserCountry(context: Context): String? {
        try {
            val tm = context.getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
            val simCountry = tm.simCountryIso
            if (simCountry != null && simCountry.length == 2) { // SIM country code is available
                return simCountry.toLowerCase(Locale.US)
            } else if (tm.phoneType != TelephonyManager.PHONE_TYPE_CDMA) { // device is not 3G (would be unreliable)
                val networkCountry = tm.networkCountryIso
                if (networkCountry != null && networkCountry.length == 2) { // network country code is available
                    return networkCountry.toLowerCase(Locale.US)
                }
            }
        } catch (e: Exception) {
        }

        return null
    }

    private fun showEnterCode(view: View) {
        if (type == "email") {
            view.tv_send_code.text = "کد ارسال شده به ایمیل خود را وارد کنید"
        } else {
            view.tv_send_code.text = "کد ارسال شده به شماره موبایلتان را وارد کنید"
        }
        object : CountDownTimer(180000, 1000) {
            override fun onTick(millisUntilFinished: Long) {
                view.tv_timer.text = (millisUntilFinished / 1000).toString() + " ثانیه"
                //here you can have your logic to set text to edittext
            }

            override fun onFinish() {
                if (context != null) {
                    val main = activity as SignUpActivity
                    main.displayFragment(main.getFragment(main.enterMobileFragment, "index", "dd", "enter_mobile", "2"))
                    hideKeyboard(activity!!)
                }
            }
        }.start()
        val main = activity as SignUpActivity
        view.et_code.background.mutate().setColorFilter(resources.getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP)
        view.btn_next2.setOnClickListener {
            pb_loading.visibility = View.VISIBLE
            endPoints!!.checkCode(et_code.text.toString().toInt(), utils!!.get_email(), utils!!.get_mobile(), utils!!.getRegId(), object : RespoDataInterface {
                override fun data(response: Respo) {
                    pb_loading.visibility = View.GONE
                    if (response.isStatus) {
                        hideKeyboard(activity!!)
                        if (exist_customer == 1) {

                            startActivity(Intent(context, GroupsActivity::class.java))
                            activity!!.finish()
                        } else {
                            main.displayFragment(main.getFragment(main.enterNameFragment, "index", "none", "enter_name", "2"), "مرحله 3/4 - وارد کردن نام")
                        }

                        utils!!.setCustomerId(response.customerId.toString())
                    }
                }

                override fun status(msg: String?) {
                    pb_loading.visibility = View.GONE
                    val dialog = utils!!.show_alert(context!!, "خطا", msg!!)
                    dialog.show()
                    val width = (context!!.resources.displayMetrics.widthPixels * 0.85).toInt()
                    dialog.window!!.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
                }
            })


        }
    }

    private fun showEnterName(view: View) {
        view.et_name.background.mutate().setColorFilter(resources.getColor(R.color.blue), PorterDuff.Mode.SRC_ATOP)
        val main = activity as SignUpActivity
        view.btn_next3.setOnClickListener {

            endPoints!!.checkName(view.et_name.text.toString(),utils!!.getCustomerId(),object: RespoDataInterface {
                override fun data(response: Respo) {
                    pb_loading.visibility = View.GONE
                    if (response.isStatus) {
                        hideKeyboard(activity!!)

                        when {
                            response.customer.profiles.size == 0  -> main.displayFragment(main.getFragment(main.enterImageFragment, "index", "none", "enter_image", "2"), "مرحله 4/4 - انتخاب تصویر ")
                            else -> {
                                startActivity(Intent(context, GroupsActivity::class.java))
                                activity!!.finish()
                            }
                        }


                        utils!!.set_name(view.et_name.text.toString())
                    }
                }

                override fun status(msg: String?) {
                    pb_loading.visibility = View.GONE
                    val dialog = utils!!.show_alert(context!!, "خطا", msg!!)
                    dialog.show()
                    val width = (context!!.resources.displayMetrics.widthPixels * 0.85).toInt()
                    dialog.window!!.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
                }
            })

        }
    }

    private fun showEnterImage(view: View) {

        view.change_pic.setOnClickListener {
            val galleryIntent = Intent(
                    Intent.ACTION_PICK,
                    android.provider.MediaStore.Images.Media.EXTERNAL_CONTENT_URI)
            startActivityForResult(galleryIntent, 1001)
        }




        view.btn_next4.setOnClickListener {
            hideKeyboard(activity!!)
            startActivity(Intent(context, GroupsActivity::class.java))
            activity!!.finish()
        }
    }


    private fun hideKeyboard(activity: Activity) {
        val imm = activity.getSystemService(Activity.INPUT_METHOD_SERVICE) as InputMethodManager
        //Find the currently focused view, so we can grab the correct window token from it.
        var view = activity.currentFocus
        //If no view currently has focus, create a new one, just so we can grab a window token from it
        if (view == null) {
            view = View(activity)
        }
        imm.hideSoftInputFromWindow(view.windowToken, 0)
    }


    fun isEmailValid(email: String): Boolean {
        val regExpn = ("^(([\\w-]+\\.)+[\\w-]+|([a-zA-Z]{1}|[\\w-]{2,}))@"
                + "((([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\."
                + "([0-1]?[0-9]{1,2}|25[0-5]|2[0-4][0-9])\\.([0-1]?"
                + "[0-9]{1,2}|25[0-5]|2[0-4][0-9])){1}|"
                + "([a-zA-Z]+[\\w-]+\\.)+[a-zA-Z]{2,4})$")

        val pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(email)

        return matcher.matches()
    }

    fun isMobileValid(mobile: String): Boolean {
        val regExpn = "(\\+98|0)?9\\d{9}"

        val pattern = Pattern.compile(regExpn, Pattern.CASE_INSENSITIVE)
        val matcher = pattern.matcher(mobile)

        return matcher.matches()
    }


    companion object {


        fun newInstance(index: String, val2: Int? = 0, val3: String? = ""): SignUpFragment {
            val fragment = SignUpFragment()
            val b = Bundle()
            b.putString("index", index)
            b.putInt("exist_customer", val2!!)
            b.putString("type", val3!!)

            fragment.arguments = b
            return fragment
        }
    }

    override fun onActivityResult(requestCode: Int, resultCode: Int, data: Intent?) {
        super.onActivityResult(requestCode, resultCode, data)

        when (requestCode) {
            CropImage.CROP_IMAGE_ACTIVITY_REQUEST_CODE -> {
                val result = CropImage.getActivityResult(data)
                val resultUri = result.uri

                try {
                    view!!.btn_next4.isEnabled = false
                    pb_loading.visibility = View.VISIBLE
                    val bitmap = MediaStore.Images.Media.getBitmap(context!!.contentResolver, resultUri)
                    val file = utils!!.save(context!!, utils!!.scaleBitmap(bitmap, 500, 500))

                    Glide.with(context).load(resultUri).into(view!!.change_pic)
                    endPoints!!.checkPic(file, utils!!.getCustomerId(), object : RespoDataInterface {
                        override fun data(response: Respo) {
                            pb_loading.visibility = View.GONE
                            view!!.btn_next4.isEnabled = true
                        }

                        override fun status(msg: String?) {
                            pb_loading.visibility = View.GONE
                            view!!.btn_next4.isEnabled = true
                        }
                    })

                } catch (e: IOException) {
                    e.printStackTrace()
                }
            }
            1001 -> if (null != data) {
                val uri = data.data
                CropImage.activity(uri).setAspectRatio(1,1)
                        .start(context!!, this)

            }
            else -> {
            }
        }
    }





}
