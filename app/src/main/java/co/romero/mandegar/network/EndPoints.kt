package co.romero.mandegar.network

import android.content.Context
import android.text.TextUtils
import co.romero.mandegar.app.App
import co.romero.mandegar.Util.Utils
import co.romero.mandegar.interfaces.RespoDataInterface
import co.romero.mandegar.interfaces.RespoInterface
import co.romero.mandegar.model.Respo
import co.romero.mandegar.request.*
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import java.lang.ref.WeakReference


class EndPoints protected constructor(private val context: Context) {
    private val TAG = EndPoints::class.java.name

    fun checkMobile(mobile: String, reg_id: String, respoDataInterface: RespoDataInterface) {
        if (!App.getInstance().isDataConnected) {
            respoDataInterface.status(" . لطفا اتصال اینترنت خود را بررسی کنید و مجدد تلاش کنید.")
        } else {
            val shopInterface = ServiceGenerator.createService(RespoInterface::class.java, context, Utils.getInstance(context)!!.getApiAddress())
            val task = CustomerRequest(mobile, reg_id)
            val call = shopInterface.checkMobile(task)
            call.enqueue(object : Callback<Respo> {
                override fun onResponse(call: Call<Respo>, response: retrofit2.Response<Respo>) {
                    if (response.isSuccessful) {
                        if (response.body()!!.isStatus) {
                            respoDataInterface.data(response.body()!!)
                        } else {
                            respoDataInterface.status(TextUtils.join("\r\n", response.body()?.error))
                        }


                    } else {
                        respoDataInterface.status("error")
                    }
                }

                override fun onFailure(call: Call<Respo>, t: Throwable) {
                    if (!t.message.isNullOrEmpty())
                        respoDataInterface.status(t.message)
                }
            })
        }

    }



    fun checkPass(email: String, password:String,reg_id: String, respoDataInterface: RespoDataInterface) {
        if (!App.getInstance().isDataConnected) {
            respoDataInterface.status(" . لطفا اتصال اینترنت خود را بررسی کنید و مجدد تلاش کنید.")
        } else {
            val shopInterface = ServiceGenerator.createService(RespoInterface::class.java, context, Utils.getInstance(context)!!.getApiAddress())
            val task = CustomerPassRequest(email, password,reg_id)
            val call = shopInterface.checkPassword(task)
            call.enqueue(object : Callback<Respo> {
                override fun onResponse(call: Call<Respo>, response: retrofit2.Response<Respo>) {
                    if (response.isSuccessful) {
                        if (response.body()!!.isStatus) {
                            respoDataInterface.data(response.body()!!)
                        } else {
                            respoDataInterface.status(TextUtils.join("\r\n", response.body()?.error))
                        }


                    } else {
                        respoDataInterface.status("error")
                    }
                }

                override fun onFailure(call: Call<Respo>, t: Throwable) {
                    if (!t.message.isNullOrEmpty())
                        respoDataInterface.status(t.message)
                }
            })
        }

    }



    fun checkCountry(respoDataInterface: RespoDataInterface) {
        if (!App.getInstance().isDataConnected) {
            respoDataInterface.status(" . لطفا اتصال اینترنت خود را بررسی کنید و مجدد تلاش کنید.")
        } else {
            val getCountry = ServiceGenerator.createService(RespoInterface::class.java, context, "http://ip-api.com/")
            val call = getCountry.checkCountry()
            call.enqueue(object : Callback<Respo?> {
                override fun onFailure(call: Call<Respo?>?, t: Throwable?) {

                    respoDataInterface.status(t!!.message)
                }

                override fun onResponse(call: Call<Respo?>?, response: Response<Respo?>?) {
                    if (response!!.isSuccessful) {
                        respoDataInterface.data(response.body()!!)
                    } else {
                        respoDataInterface.status("error")
                    }
                }
            })

        }
    }

    fun checkMail(email: String, country: String, reg_id: String, respoDataInterface: RespoDataInterface) {

        if (!App.getInstance().isDataConnected) {
            respoDataInterface.status(" . لطفا اتصال اینترنت خود را بررسی کنید و مجدد تلاش کنید.")
        } else {
            val shopInterface = ServiceGenerator.createService(RespoInterface::class.java, context, Utils.getInstance(context)!!.getApiAddress())

            val task = CustomerEmailRequest(email, country, reg_id)
            val call = shopInterface.checkEmail(task)
            call.enqueue(object : Callback<Respo> {
                override fun onResponse(call: Call<Respo>, response: retrofit2.Response<Respo>) {
                    if (response.isSuccessful) {
                        if (response.body()!!.isStatus) {
                            respoDataInterface.data(response.body()!!)
                        } else {
                            respoDataInterface.status(TextUtils.join("\r\n", response.body()?.error))
                        }


                    } else {
                        respoDataInterface.status("error")
                    }
                }

                override fun onFailure(call: Call<Respo>, t: Throwable) {
                    respoDataInterface.status(t.message)
                }
            })
        }

    }

    fun checkCode(code: Int, email: String, mobile: String, reg_id: String, respoDataInterface: RespoDataInterface) {

        if (!App.getInstance().isDataConnected) {
            respoDataInterface.status(" . لطفا اتصال اینترنت خود را بررسی کنید و مجدد تلاش کنید.")
        } else {
            val shopInterface = ServiceGenerator.createService(RespoInterface::class.java, context, Utils.getInstance(context)!!.getApiAddress())

            val task = CustomerCheckCodeRequest(code, mobile, email, reg_id)
            val call = shopInterface.checkLoginCode(task)
            call.enqueue(object : Callback<Respo> {
                override fun onResponse(call: Call<Respo>, response: retrofit2.Response<Respo>) {
                    if (response.isSuccessful) {
                        if (response.body()!!.isStatus) {
                            respoDataInterface.data(response.body()!!)
                        } else {
                            respoDataInterface.status(TextUtils.join("\r\n", response.body()?.error))
                        }


                    } else {
                        respoDataInterface.status("error")
                    }
                }

                override fun onFailure(call: Call<Respo>, t: Throwable) {
                    respoDataInterface.status(t.message)
                }
            })
        }

    }

    fun checkLoginOrRegister(mobile: String? = "", email: String? = "", name: String? = "", image: String? = "", respoDataInterface: RespoDataInterface) {

        if (!App.getInstance().isDataConnected) {
            respoDataInterface.status(" . لطفا اتصال اینترنت خود را بررسی کنید و مجدد تلاش کنید.")
        } else {
            val shopInterface = ServiceGenerator.createService(RespoInterface::class.java, context, Utils.getInstance(context)!!.getApiAddress())

            val task = CustomerLoginOrRegisterRequest(mobile, email, name, image)
            val call = shopInterface.customerLoginOrRegister(task)
            call.enqueue(object : Callback<Respo> {
                override fun onResponse(call: Call<Respo>, response: retrofit2.Response<Respo>) {
                    if (response.isSuccessful) {
                        if (response.body()!!.isStatus) {
                            respoDataInterface.data(response.body()!!)
                        } else {
                            respoDataInterface.status(TextUtils.join("\r\n", response.body()?.error))
                        }


                    } else {
                        respoDataInterface.status("error")
                    }
                }

                override fun onFailure(call: Call<Respo>, t: Throwable) {
                    respoDataInterface.status(t.message)
                }
            })
        }

    }


    companion object {

        private var myWeakInstance: WeakReference<EndPoints>? = null

        fun getInstance(context: Context?): EndPoints? {
            if (myWeakInstance == null || myWeakInstance!!.get() == null) {
                myWeakInstance = WeakReference(EndPoints(context!!.applicationContext))
            }
            return myWeakInstance!!.get()
        }
    }

}