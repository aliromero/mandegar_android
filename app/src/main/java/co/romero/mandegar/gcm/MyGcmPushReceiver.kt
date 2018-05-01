/**
 * Copyright 2015 Google Inc. All Rights Reserved.
 *
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */

package co.romero.mandegar.gcm

import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.support.v4.content.LocalBroadcastManager
import android.text.TextUtils
import android.util.Log
import android.widget.Toast
import co.romero.mandegar.Util.Utils
import co.romero.mandegar.activity.ChatsActivity
import co.romero.mandegar.app.App
import co.romero.mandegar.app.Config
import co.romero.mandegar.model.Customer
import co.romero.mandegar.model.Message
import co.romero.mandegar.model.Message2

import com.google.android.gms.gcm.GcmListenerService

import org.json.JSONException
import org.json.JSONObject

class MyGcmPushReceiver : GcmListenerService() {

    private var notificationUtils: NotificationUtils? = null

    /**
     * Called when message is received.
     *
     * @param from   SenderID of the sender.
     * @param bundle Data bundle containing message data as key/value pairs.
     * For Set of keys use data.keySet().
     */

    override fun onMessageReceived(from: String?, bundle: Bundle?) {
        Log.i("iiiii","ddd2d")
        val title = bundle!!.getString("title")
        val isBackground = java.lang.Boolean.valueOf(bundle.getString("is_background"))
        val flag = bundle.getString("flag")
        val data = bundle.getString("data")
        Log.d(TAG, "From: " + from!!)
        Log.d(TAG, "title: " + title!!)
        Log.d(TAG, "isBackground: $isBackground")
        Log.d(TAG, "flag: " + flag!!)
        Log.d(TAG, "data: " + data!!)

        if (flag == null)
            return

        if (Utils.getInstance(this)!!.getUser() == null) {
            // user is not logged in, skipping push notification
            Log.e(TAG, "user is not logged in, skipping push notification")
            return
        }

        if (from.startsWith("/topics/")) {
            // message received from some topic.
        } else {
            // normal downstream message.
        }

        when (Integer.parseInt(flag)) {
            Config.PUSH_TYPE_CHATROOM ->
                // push notification belongs to a chat room
                processChatRoomPush(title, isBackground, data)
//            Config.PUSH_TYPE_USER ->
                // push notification is specific to user
//                processUserMessage(title, isBackground, data)
        }
    }

    /**
     * Processing chat room push message
     * this message will be broadcasts to all the activities registered
     */
    private fun processChatRoomPush(title: String?, isBackground: Boolean, data: String?) {
        Log.i("iiiii","dddd")
        if (!isBackground) {

            try {
                val datObj = JSONObject(data)

                val chatRoomId = datObj.getInt("chatroom_id")

                val mObj = datObj.getJSONObject("item_message")
                val message = Message2()
                message.message = mObj.getString("message")
                message.id = mObj.getString("message_id")
                message.createdAt = mObj.getString("created_at")

                val uObj = datObj.getJSONObject("user")

                // skip the message if the message belongs to same user as
                // the user would be having the same message when he was sending
                // but it might differs in your scenario
                if (uObj.getString("user_id") == Utils.getInstance(this)!!.getCustomerId()) {
                    Log.e(TAG, "Skipping the push message as it belongs to same user")
                    return
                }

                val user = Customer()
                user.setId(uObj.getString("user_id"))
                user.email = uObj.getString("email")
                user.name = uObj.getString("name")
                message.user = user

                // verifying whether the app is in background or foreground
                if (!NotificationUtils.isAppIsInBackground(applicationContext)) {

                    // app is in foreground, broadcast the push message
                    val pushNotification = Intent(Config.PUSH_NOTIFICATION)
                    pushNotification.putExtra("type", Config.PUSH_TYPE_CHATROOM)
                    pushNotification.putExtra("message", message)
                    pushNotification.putExtra("chat_room_id", chatRoomId)
                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification)

                    // play notification sound
                    val notificationUtils = NotificationUtils()
                    notificationUtils.playNotificationSound()
                } else {

                    // app is in background. show the message in notification try
                    val resultIntent = Intent(applicationContext, ChatsActivity::class.java)
                    resultIntent.putExtra("chat_room_id", chatRoomId)
                    showNotificationMessage(applicationContext, title, user.name + " : " + message.message, message.createdAt, resultIntent)
                }

            } catch (e: JSONException) {
                Log.e(TAG, "json parsing error: " + e.message)
                Toast.makeText(applicationContext, "Json parse error: " + e.message, Toast.LENGTH_LONG).show()
            }

        } else {
            // the push notification is silent, may be other operations needed
            // like inserting it in to SQLite
        }
    }

    /**
     * Processing user specific push message
     * It will be displayed with / without image in push notification tray
     */
//    private fun processUserMessage(title: String?, isBackground: Boolean, data: String?) {
//        if (!isBackground) {
//
//            try {
//                val datObj = JSONObject(data)
//
//                val imageUrl = datObj.getString("image")
//
//                val mObj = datObj.getJSONObject("message")
//                val message = Message()
//                message.message = mObj.getString("message")
//                message.setId(mObj.getString("message_id"))
//                message.createdAt = mObj.getString("created_at")
//
//                val uObj = datObj.getJSONObject("user")
//                val user = User()
//                user.setId(uObj.getString("user_id"))
//                user.setEmail(uObj.getString("email"))
//                user.setName(uObj.getString("name"))
//                message.setUser(user)
//
//                // verifying whether the app is in background or foreground
//                if (!NotificationUtils.isAppIsInBackground(applicationContext)) {
//
//                    // app is in foreground, broadcast the push message
//                    val pushNotification = Intent(Config.PUSH_NOTIFICATION)
//                    pushNotification.putExtra("type", Config.PUSH_TYPE_USER)
//                    pushNotification.putExtra("message", message)
//                    LocalBroadcastManager.getInstance(this).sendBroadcast(pushNotification)
//
//                    // play notification sound
//                    val notificationUtils = NotificationUtils()
//                    notificationUtils.playNotificationSound()
//                } else {
//
//                    // app is in background. show the message in notification try
//                    val resultIntent = Intent(applicationContext, MainActivity::class.java)
//
//                    // check for push notification image attachment
//                    if (TextUtils.isEmpty(imageUrl)) {
//                        showNotificationMessage(applicationContext, title, user.getName() + " : " + message.message, message.createdAt, resultIntent)
//                    } else {
//                        // push notification contains image
//                        // show it with the image
//                        showNotificationMessageWithBigImage(applicationContext, title, message.message, message.createdAt, resultIntent, imageUrl)
//                    }
//                }
//            } catch (e: JSONException) {
//                Log.e(TAG, "json parsing error: " + e.message)
//                Toast.makeText(applicationContext, "Json parse error: " + e.message, Toast.LENGTH_LONG).show()
//            }
//
//        } else {
//            // the push notification is silent, may be other operations needed
//            // like inserting it in to SQLite
//        }
//    }

    /**
     * Showing notification with text only
     */
    private fun showNotificationMessage(context: Context, title: String?, message: String, timeStamp: String, intent: Intent) {
        notificationUtils = NotificationUtils(context)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        notificationUtils!!.showNotificationMessage(title, message, timeStamp, intent)
    }

    /**
     * Showing notification with text and image
     */
    private fun showNotificationMessageWithBigImage(context: Context, title: String?, message: String, timeStamp: String, intent: Intent, imageUrl: String) {
        notificationUtils = NotificationUtils(context)
        intent.flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        notificationUtils!!.showNotificationMessage(title, message, timeStamp, intent, imageUrl)
    }

    companion object {

        private val TAG = MyGcmPushReceiver::class.java.simpleName
    }
}
