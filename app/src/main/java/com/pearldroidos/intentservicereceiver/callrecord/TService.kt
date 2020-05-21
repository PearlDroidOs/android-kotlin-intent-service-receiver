package com.pearldroidos.intentservicereceiver.callrecord

import android.app.Service
import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.content.IntentFilter
import android.media.MediaRecorder
import android.os.Bundle
import android.os.Environment
import android.os.Handler
import android.os.IBinder
import android.telephony.TelephonyManager
import android.util.Log
import android.widget.Toast
import java.io.File
import java.io.IOException
import java.text.SimpleDateFormat
import java.util.*


class TService : Service() {


    var name: String? = null
    var phonenumber: String? = null
    var audio_format: String? = null
    var Audio_Type: String? = null
    var audioSource = 0
    var context: Context? = null
    private val handler: Handler? = null
    var timer: Timer? = null
    var offHook = false
    var ringing: kotlin.Boolean? = false
    var toast: Toast? = null
    var isOffHooks = false
    private var brCall: CallBr? = null

    companion object{
        val ACTION_IN = "android.intent.action.PHONE_STATE"
        val ACTION_OUT = "android.intent.action.NEW_OUTGOING_CALL"
        var audiofile: File? = null
        var recordstarted = false
        var recorder = MediaRecorder()
    }



    override fun onBind(intent: Intent?): IBinder? {
        // TODO Auto-generated method stub
        return null;
    }

    override fun onDestroy() {
        Log.d("service", "destroy");
        super.onDestroy()
    }

    override fun onStartCommand(intent: Intent?, flags: Int, startId: Int): Int {
        val filter = IntentFilter()
        filter.addAction(ACTION_OUT)
        filter.addAction(ACTION_IN)
        brCall = CallBr()
        this.registerReceiver(brCall, filter)
        return START_NOT_STICKY;
    }

    class CallBr : BroadcastReceiver() {
        var bundle: Bundle? = null
        var state: String? = null
        var inCall: String? = null
        var outCall: String? = null
        var wasRinging = false
        override fun onReceive(
            context: Context,
            intent: Intent
        ) {
            if (intent.action == ACTION_IN) {
                if (intent.extras.also { bundle = it } != null) {
                    state = bundle!!.getString(TelephonyManager.EXTRA_STATE)
                    if (state == TelephonyManager.EXTRA_STATE_RINGING) {
                        inCall = bundle!!.getString(TelephonyManager.EXTRA_INCOMING_NUMBER)
                        wasRinging = true
                        Toast.makeText(context, "IN : $inCall", Toast.LENGTH_LONG).show()
                    } else if (state == TelephonyManager.EXTRA_STATE_OFFHOOK) {
                        Toast.makeText(context, "State Extra offHook", Toast.LENGTH_LONG).show()
                        if (wasRinging) {
                            Toast.makeText(context, "ANSWERED", Toast.LENGTH_LONG).show()
                            val out =
                                SimpleDateFormat("dd-MM-yyyy hh-mm-ss").format(Date())
                            val sampleDir = File(
                                Environment.getExternalStorageDirectory(),
                                "/TestRecordingDasa1"
                            )
                            if (!sampleDir.exists()) {
                                sampleDir.mkdirs()
                            }
                            val file_name = "Record"
                            try {
                                audiofile = File.createTempFile(file_name, ".amr", sampleDir)
                                Log.d("Pearl", "${sampleDir.absolutePath}")
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                            val path =
                                Environment.getExternalStorageDirectory()
                                    .absolutePath
                            //                          recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_CALL);
                            recorder.setAudioSource(MediaRecorder.AudioSource.VOICE_COMMUNICATION)
                            recorder.setOutputFormat(MediaRecorder.OutputFormat.AMR_NB)
                            recorder.setAudioEncoder(MediaRecorder.AudioEncoder.AMR_NB)
                            recorder.setOutputFile(audiofile?.absolutePath)
                            try {
                                recorder.prepare()
                            } catch (e: IllegalStateException) {
                                e.printStackTrace()
                            } catch (e: IOException) {
                                e.printStackTrace()
                            }
                            recorder.start()
                            recordstarted = true
                        }
                    } else if (state == TelephonyManager.EXTRA_STATE_IDLE) {
                        wasRinging = false
                        Toast.makeText(context, "REJECT || DISCO", Toast.LENGTH_LONG).show()
                        Log.d("Pearl","1  ${recorder}  ${audiofile?.path}")
                        if (recordstarted) {
                            recorder.stop()
                            recordstarted = false
                            Log.d("Pearl","2  ${recorder}  ${audiofile?.path}")
                        }
                    }
                }
            } else if (intent.action == ACTION_OUT) {
                if (intent.extras.also { bundle = it } != null) {
                    outCall = intent.getStringExtra(Intent.EXTRA_PHONE_NUMBER)
                    Toast.makeText(context, "OUT : $outCall", Toast.LENGTH_LONG).show()
                }
            }
        }
    }


}

