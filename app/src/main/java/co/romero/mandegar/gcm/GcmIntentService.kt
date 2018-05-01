package co.romero.mandegar.gcm

import android.app.IntentService
import android.content.Intent
import android.content.SharedPreferences
import android.preference.PreferenceManager
import android.support.v4.content.LocalBroadcastManager
import android.util.Log
import android.widget.Toast


import com.google.android.gms.gcm.GcmPubSub
import com.google.android.gms.gcm.GoogleCloudMessaging
import com.google.android.gms.iid.InstanceID

import org.json.JSONException
import org.json.JSONObject

import java.io.IOException
import java.util.HashMap

import co.romero.mandegar.R
import co.romero.mandegar.Util.Utils
import co.romero.mandegar.app.App
import co.romero.mandegar.app.Config
import co.romero.mandegar.interfaces.RespoDataInterface
import co.romero.mandegar.model.Customer
import co.romero.mandegar.network.EndPoints
import co.romero.mandegar.response.Respo


class GcmIntentService : IntentService(TAG) {


    override fun onHandleIntent(intent: Intent?) {
        val key = intent!!.getStringExtra(KEY)
        when (key) {
            SUBSCRIBE -> {
                // subscribe to a topic
                val topic = intent.getStringExtra(TOPIC)
                subscribeToTopic(topic)
            }
            UNSUBSCRIBE -> {
            }
            else ->
                // if key is specified, register with GCM
                registerGCM()
        }

    }

    /**
     * Registering with GCM and obtaining the gcm registration id
     */
    private fun registerGCM() {
        Log.i("iiiii","registerGCM")
        val sharedPreferences = PreferenceManager.getDefaultSharedPreferences(this)

        try {
            val instanceID = InstanceID.getInstance(this)
            val token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null)

            Log.e(TAG, "GCM Registration Token: $token")
            Utils.getInstance(this)!!.setRegId(token)

            // sending the registration id to our server
            sendRegistrationToServer(token)

            sharedPreferences.edit().putBoolean(Config.SENT_TOKEN_TO_SERVER, true).apply()
        } catch (e: Exception) {
            Log.e(TAG, "Failed to complete token refresh", e)

            sharedPreferences.edit().putBoolean(Config.SENT_TOKEN_TO_SERVER, false).apply()
        }

        // Notify UI that registration has completed, so the progress indicator can be hidden.
        val registrationComplete = Intent(Config.REGISTRATION_COMPLETE)
        LocalBroadcastManager.getInstance(this).sendBroadcast(registrationComplete)
    }

    private fun sendRegistrationToServer(token: String) {

        // checking for valid login session
        val user = Utils.getInstance(this)!!.getUser()
                ?: // TODO
                // user not found, redirecting him to login screen
                return

        EndPoints.getInstance(this)!!.updateGcm(token, object : RespoDataInterface {
            override fun data(response: Respo) {

                if (response.isStatus) {
                    val registrationComplete = Intent(Config.SENT_TOKEN_TO_SERVER)
                    LocalBroadcastManager.getInstance(applicationContext).sendBroadcast(registrationComplete)

                } else {
                    Toast.makeText(applicationContext, "Unable to send gcm registration id to our sever. ", Toast.LENGTH_LONG).show()
                }
            }

            override fun status(msg: String?) {
            }
        })


    }

    fun unsubscribeFromTopic(topic: String) {
        val pubSub = GcmPubSub.getInstance(applicationContext)
        val instanceID = InstanceID.getInstance(applicationContext)
        var token: String? = null
        try {
            token = instanceID.getToken(getString(R.string.gcm_defaultSenderId),
                    GoogleCloudMessaging.INSTANCE_ID_SCOPE, null)
            if (token != null) {
                pubSub.unsubscribe(token, "")
                Log.e(TAG, "Unsubscribed from topic: $topic")
            } else {
                Log.e(TAG, "error: gcm registration id is null")
            }
        } catch (e: IOException) {
            Log.e(TAG, "Topic unsubscribe error. Topic: " + topic + ", error: " + e.message)
            Toast.makeText(applicationContext, "Topic subscribe error. Topic: " + topic + ", error: " + e.message, Toast.LENGTH_SHORT).show()
        }

    }

    companion object {

        private val TAG = GcmIntentService::class.java.simpleName

        val KEY = "key"
        val TOPIC = "topic"
        val SUBSCRIBE = "subscribe"
        val UNSUBSCRIBE = "unsubscribe"

        /**
         * Subscribe to a topic
         */
        fun subscribeToTopic(topic: String) {
            Log.i("iiiii","subscribeToTopic")
            val pubSub = GcmPubSub.getInstance(App.getInstance().applicationContext)
            val instanceID = InstanceID.getInstance(App.getInstance().applicationContext)
            var token: String? = null
            try {
                token = instanceID.getToken(App.getInstance().applicationContext.getString(R.string.gcm_defaultSenderId),
                        GoogleCloudMessaging.INSTANCE_ID_SCOPE, null)
                if (token != null) {
                    pubSub.subscribe(token, "/topics/$topic", null)
                    Log.e(TAG, "Subscribed to topic: $topic")
                } else {
                    Log.e(TAG, "error: gcm registration id is null")
                }
            } catch (e: IOException) {
                Log.e(TAG, "Topic subscribe error. Topic: " + topic + ", error: " + e.message)
                Toast.makeText(App.getInstance().applicationContext, "Topic subscribe error. Topic: " + topic + ", error: " + e.message, Toast.LENGTH_SHORT).show()
            }

        }
    }
}
