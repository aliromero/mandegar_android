package co.romero.mandegar.Util


import android.content.Context
import android.content.SharedPreferences
import android.graphics.Bitmap
import android.graphics.Color
import android.graphics.Typeface
import android.graphics.drawable.ColorDrawable
import android.preference.PreferenceManager
import android.support.v7.app.AlertDialog
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.WindowManager
import android.widget.LinearLayout
import android.widget.TextView
import co.romero.mandegar.BuildConfig
import co.romero.mandegar.Constant.Constants
import co.romero.mandegar.Constant.Constants.*
import co.romero.mandegar.R
import co.romero.mandegar.SecurePreferences
import co.romero.mandegar.model.Customer
import java.io.File
import java.io.FileOutputStream

import java.lang.ref.WeakReference
import java.text.SimpleDateFormat
import java.util.*


class Utils protected constructor(private val context: Context) {
    private val TAG = Utils::class.java.name
    private val prefs_secure: SecurePreferences
    private val prefs: SharedPreferences

    fun IranSans(): Typeface {

        return Typeface.createFromAsset(context.assets, Constants.IRANS_SANS)

    }

    fun getApiAddress(): String {
        return POROTOCOL + DOMAIN + FOLDER + API
    }


    init {
        this.prefs_secure = SecurePreferences(context, BuildConfig.APPLICATION_ID, "__AliRomero__TempGozar.ir&~&Lastech.ir&~&", true)
        this.prefs = PreferenceManager.getDefaultSharedPreferences(context)

    }


    fun show_alert(context: Context, title: String, msg: String): AlertDialog {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val promptView = inflater.inflate(R.layout.custom_dialog_two_btn, null)
        val builder = AlertDialog.Builder(context)
        val dialog = builder.setView(promptView).create()
        val tv_title = promptView.findViewById<TextView>(R.id.tv_title)
        val tv_content = promptView.findViewById<TextView>(R.id.tv_content)
        val tv_negative = promptView.findViewById<TextView>(R.id.tv_negative)
        val tv_positive = promptView.findViewById<TextView>(R.id.tv_positive)
        val ll_positive = promptView.findViewById<LinearLayout>(R.id.ll_positive)
        val ll_negative = promptView.findViewById<LinearLayout>(R.id.ll_negative)
        tv_title.text = title
        tv_content.text = msg
        tv_negative.text = "بستن"
        ll_positive.visibility = View.GONE


        ll_negative.setOnClickListener { dialog.dismiss() }

        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        dialog.window!!.attributes = lp
        return dialog
    }


    fun save_api_token(api_token:String) {
        if (api_token == "")
            prefs_secure.removeValue("api_token")
        else
        prefs_secure.put("api_token", api_token)
    }

    fun get_api_token():String{
        return if (prefs_secure.getString("api_token") != null)
            prefs_secure.getString("api_token")
        else ""
    }
    fun set_email(value: String? = "") {
        if (value == null || value == "")
            prefs_secure.removeValue("email")
        else
            prefs_secure.put("email", value)

    }

    fun set_mobile(value: String? = "") {
        if (value == null || value == "")
            prefs_secure.removeValue("mobile")
        else
            prefs_secure.put("mobile", value)

    }

    fun set_name(value: String) {
        prefs_secure.put("name", value)

    }


    fun get_email(): String {
        return if (prefs_secure.getString("email") != null)
            prefs_secure.getString("email")
        else ""
    }


    fun get_mobile(): String {
        return if (prefs_secure.getString("mobile") != null)
            "0" + prefs_secure.getString("mobile")
        else ""
    }


    fun get_name(): String {
        return if (prefs_secure.getString("name") != null)
            prefs_secure.getString("name")
        else ""
    }


    fun save(context: Context, bitmap: Bitmap): File {
        val timeStamp = SimpleDateFormat("ddMMyy_HHmms").format(Date())
        val fname = "Image-$timeStamp.jpg"
        val file = File(context.filesDir.absolutePath, fname)
        //        File appDirectory = new File(Environment.getExternalStorageDirectory() + "/myfile");
        //        appDirectory.mkdirs();
        //        File file = new File (Environment.getExternalStorageDirectory() + "/myfile", fname);
        if (file.exists()) file.delete()
        try {
            val out = FileOutputStream(file)
            bitmap.compress(Bitmap.CompressFormat.JPEG, 65, out)
            out.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return file
    }

    fun getUser(): Customer? {
        if (prefs_secure.getString("customer_id") != null) {
            val id: String?
            val name: String?
            val email: String?
            val pic: String?
            id = getCustomerId()
            name = get_name()
            email = get_email()


            val customer = Customer()
            customer.name = name
            customer.id = id.toLong()
            customer.email = email
            customer.pic = ""
            customer.save()
            return customer
        }
        return null
    }

    fun addNotification(msg:String)
    {
        var oldNotifications: String? = getNotifications()

        if (oldNotifications != null) {
            oldNotifications += "|$msg"
        } else {
            oldNotifications = msg
        }

        prefs_secure.put("notification_chats", oldNotifications)
    }

    fun getNotifications(): String? {
        return prefs_secure.getString("notification_chats")
    }

    fun scaleBitmap(bm: Bitmap, maxWidth: Int, maxHeight: Int): Bitmap {
        var bm = bm
        var width = bm.width
        var height = bm.height

        Log.v("Pictures", "Width and height are $width--$height")

        if (width > height) {
            // landscape
            val ratio = width.toFloat() / maxWidth
            width = maxWidth
            height = (height / ratio).toInt()
        } else if (height > width) {
            // portrait
            val ratio = height.toFloat() / maxHeight
            height = maxHeight
            width = (width / ratio).toInt()
        } else {
            // square
            height = maxHeight
            width = maxWidth
        }
        bm = Bitmap.createScaledBitmap(bm, width, height, true)
        return bm
    }




    fun show_dialog(context: Context?, view: View): AlertDialog {

        val builder = AlertDialog.Builder(context!!)
        builder.setView(view).create()
        val dialog = builder.create()
        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val width = (context.resources.displayMetrics.widthPixels * 0.85).toInt()
        dialog.window!!.setLayout(width, WindowManager.LayoutParams.WRAP_CONTENT)
        return dialog
    }


    fun show_error_network(context: Context): AlertDialog {
        val inflater = context.getSystemService(Context.LAYOUT_INFLATER_SERVICE) as LayoutInflater

        val promptView = inflater.inflate(R.layout.custom_dialog_two_btn, null)
        val builder = AlertDialog.Builder(context)
        val dialog = builder.setView(promptView).create()
        val tv_title = promptView.findViewById<TextView>(R.id.tv_title)
        val tv_content = promptView.findViewById<TextView>(R.id.tv_content)
        val tv_negative = promptView.findViewById<TextView>(R.id.tv_negative)
        val ll_positive = promptView.findViewById<LinearLayout>(R.id.ll_positive)
        val ll_negative = promptView.findViewById<LinearLayout>(R.id.ll_negative)
        tv_title.text = "خطای اتصال"
        tv_content.text = "خطا در اتصال به اینترنت.\rلطفا اتصال اینترنت خود را بررسی کنید و مجدد تلاش کنید."
        tv_negative.text = "بستن"
        ll_positive.visibility = View.GONE


        ll_negative.setOnClickListener { dialog.dismiss() }

        dialog.window!!.setBackgroundDrawable(ColorDrawable(Color.TRANSPARENT))
        val lp = WindowManager.LayoutParams()
        lp.copyFrom(dialog.window!!.attributes)
        lp.width = WindowManager.LayoutParams.MATCH_PARENT
        lp.height = WindowManager.LayoutParams.MATCH_PARENT
        dialog.window!!.attributes = lp

        return dialog
    }

    fun getCustomerId():String {
        return if (prefs_secure.getString("customer_id") != null)
            prefs_secure.getString("customer_id")
        else ""
    }

    fun setCustomerId(customer_id: String) {

        prefs_secure.put("customer_id", customer_id)
    }

    fun setLogOut()
    {
        set_mobile("")
        set_email("")
        setCustomerId("")
        save_api_token("")

    }
    fun setRegId(regId:String) {
        if(regId == "") {
            prefs_secure.removeValue("reg_id")
        } else {
            prefs_secure.put("reg_id", regId)
        }


    }

    fun getRegId():String
    {
        return if (prefs_secure.getString("reg_id") != null)
            prefs_secure.getString("reg_id")
        else ""
    }

    companion object {

        private var myWeakInstance: WeakReference<Utils>? = null

        fun getInstance(context: Context?): Utils? {
            if (myWeakInstance == null || myWeakInstance!!.get() == null) {
                myWeakInstance = WeakReference(Utils(context!!.applicationContext))
            }
            return myWeakInstance!!.get()
        }
    }
}