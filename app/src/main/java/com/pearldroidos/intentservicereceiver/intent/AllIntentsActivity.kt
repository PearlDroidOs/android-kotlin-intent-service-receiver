package com.pearldroidos.intentservicereceiver.intent

import android.app.AlertDialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.net.Uri
import android.os.Build
import android.os.Bundle
import android.telephony.SmsManager
import android.telephony.TelephonyManager
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.pearldroidos.intentservicereceiver.R
import kotlinx.android.synthetic.main.activity_all_intents.*

class AllIntentsActivity : AppCompatActivity(), View.OnClickListener {
    private lateinit var intentReceive: Intent
    private val myPhone = "098765xxxx"

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_all_intents)

        //Solution 1 - check a phone which can call
        val telMan = getSystemService(Context.TELEPHONY_SERVICE) as TelephonyManager
        if (telMan.phoneType == TelephonyManager.PHONE_TYPE_NONE) {
            tvSupportCall1.text = "Solution 1: This phone does not support calling"
            btnPhone.isEnabled = false
        } else {
            tvSupportCall1.text = "Solution 1: This phone support calling"
            btnPhone.isEnabled = true
        }

        //Solution 2 - check a phone which can call or not
        //If app error, you need to declare hardware.telephony permission
        //If not, it is fine that you do not want to set the permission
        if (packageManager.hasSystemFeature(PackageManager.FEATURE_TELEPHONY)) {
            tvSupportCall2.text = "Solution 2: This phone support calling"
            btnPhone.isEnabled = true
        } else {
            tvSupportCall1.text = "Solution 2: This phone does not support calling"
            btnPhone.isEnabled = false
        }


        //Check Permission in Dangerous case: Phone/ Camera/ Location/ Map/ SMS/ Storage/ Calendar/ Sensors/ Microphone/ Contact
        //=====  Have you done any permission yet? ============
        //If yes - get 'call' functions
        //If no - get 'requestPermission' but if your phone is under Android M,
        //Your phone will not be asked any permissions.
        if (ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.CALL_PHONE
            ) == PackageManager.PERMISSION_GRANTED || ContextCompat.checkSelfPermission(
                this,
                android.Manifest.permission.SEND_SMS
            ) == PackageManager.PERMISSION_GRANTED
        ) {
            //User allow permission
            call()
        } else {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                //Equal and More than Android M version
                //Can request more than one permission at the same time in 'arrayOf'
                //Set RequestCode
                requestPermissions(
                    arrayOf(
                        android.Manifest.permission.CALL_PHONE,
                        android.Manifest.permission.SEND_SMS
                    ), 999
                )
            } else {
                //Less than Android M version
                call()
            }
        }


        //Setting onClick
        btnWebView.setOnClickListener(this)
        btnPhoneDial.setOnClickListener(this)
        btnEmail.setOnClickListener(this)
        btnEmail2.setOnClickListener(this)
        btnSMS1.setOnClickListener(this)
    }

    private fun call() {
        btnPhone.setOnClickListener(this)
        btnSMS2.setOnClickListener(this)
    }

    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        if (requestCode == 999) {
            if (permissions.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {
                call()
            } else {
                Toast.makeText(this, "Permission Denied", Toast.LENGTH_LONG).show()
            }
        }
    }

    override fun onClick(v: View?) {
        when (v?.id) {
            R.id.btnWebView -> {
                intentReceive = Intent()
                intentReceive.action = Intent.ACTION_VIEW
                intentReceive.data = Uri.parse("http://${edtWebView.text}")
                startActivity(intentReceive)
            }
            R.id.btnPhone -> {
                intentReceive = Intent()
                //Intent.ACTION_CALL : call immediately - need to set CALL_PHONE permission as well
                intentReceive.action = Intent.ACTION_CALL
                intentReceive.data = Uri.parse("tel:$myPhone")
                try {
                    startActivity(intentReceive)
                } catch (ex: SecurityException) {
                }
            }
            R.id.btnPhoneDial -> {
                intentReceive = Intent()
                //Intent.DIAL  : navigate to  dial page of the system - don't need to set permission
                intentReceive.action = Intent.ACTION_DIAL
                intentReceive.data = Uri.parse("tel:$myPhone")
                try {
                    startActivity(intentReceive)
                } catch (ex: SecurityException) {
                }
            }
            R.id.btnSMS1 ->{
                intentReceive = Intent()
                intentReceive.action = Intent.ACTION_SENDTO
                intentReceive.data = Uri.parse("sms:$myPhone")
                intentReceive.putExtra("sms_body", "Test sendding SMS")
                startActivity(intentReceive)
            }
            R.id.btnSMS2 ->{
                val smsMan = SmsManager.getDefault()
                val sendTo =  myPhone
                val message = "Hello, testing sending SMS"

                AlertDialog.Builder(this).apply {
                    setPositiveButton("Send") { _, _ ->
                        smsMan.sendTextMessage(sendTo, null, message, null, null)
                        Toast.makeText(baseContext, "Send SMS already", Toast.LENGTH_LONG).show()
                    }

                    setNegativeButton("Cancel") { _, _ ->
                        Toast.makeText(baseContext, "Cancel Sending SMS", Toast.LENGTH_LONG).show()
                    }
                    setTitle("SMS Sending")
                    setMessage("Do you want to send the message?")
                    setCancelable(false) //Disable cancel outside areas of the dialog
                    show()
                }

            }
            R.id.btnEmail -> {
                intentReceive = Intent()
                intentReceive.action = Intent.ACTION_SEND
                intentReceive.type = "text/plain"
                intentReceive.putExtra(Intent.EXTRA_EMAIL, arrayOf("pearl.droidos@gmail.com"))

                startActivity(intentReceive)
            }
            R.id.btnEmail2 -> {
                intentReceive = Intent()
                intentReceive.action = Intent.ACTION_SEND
                intentReceive.apply {
                    type = "message/rfc822"
                    putExtra(Intent.EXTRA_EMAIL, arrayOf("pearl.droidos@gmail.com"))
                    putExtra(Intent.EXTRA_CC, arrayOf("dd@gmail.com"))
                    putExtra(Intent.EXTRA_SUBJECT, "Test to send email")
                    putExtra(Intent.EXTRA_TEXT, "Hi John! How are you today?")
                }
                startActivity(intentReceive)
            }
        }


    }
}













