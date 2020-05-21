package com.pearldroidos.intentservicereceiver.intent

import android.content.pm.PackageManager
import android.os.Build
import android.os.Bundle
import android.widget.Toast
import androidx.annotation.RequiresApi
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import com.pearldroidos.intentservicereceiver.R
import kotlinx.android.synthetic.main.activity_camera.*

class CameraActivity : AppCompatActivity() {
    private val CAMERA_PERMISSION = android.Manifest.permission.CAMERA

    //SDK >= JELLY_BEAN
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    private val READ_EXTERNAL_PERMISSION = android.Manifest.permission.READ_EXTERNAL_STORAGE

    private val GRANTED = PackageManager.PERMISSION_GRANTED
    private val REQUEST_CODE_CAMERA = 777
    private val REQUEST_CODE_READ = 999


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_camera)


        btnCamera.setOnClickListener {
            cameraPermission()
        }


        btnReadEx.setOnClickListener {
            readExternalStoragePermission()
        }
    }

    private fun cameraPermission() {
        //Have you allowed this permission yet?
        if (ContextCompat.checkSelfPermission(this, CAMERA_PERMISSION) == GRANTED) {
            //If yes -- doing something
            toast("$CAMERA_PERMISSION  You have allow already")
        } else {
            //After Android M: Users will get permission dialog
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(CAMERA_PERMISSION), REQUEST_CODE_CAMERA)
            } else {
                //Less than Android M: Users can use features without checking permissions
                toast("$CAMERA_PERMISSION  You can use the camera without checking permissions")
            }
        }
    }

    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    private fun readExternalStoragePermission() {
        //Have you allowed this permission yet?
        if (ContextCompat.checkSelfPermission(this, READ_EXTERNAL_PERMISSION) == GRANTED) {
            //If yes -- doing something
            toast("$READ_EXTERNAL_PERMISSION  You have allow already")
        } else {
            //After Android M: Users will get permission dialog
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
                requestPermissions(arrayOf(READ_EXTERNAL_PERMISSION), REQUEST_CODE_READ)
            } else {
                //Less than Android M: Users can use features without checking permissions
                toast("$READ_EXTERNAL_PERMISSION  You can use read external storage without checking permissions")
            }
        }
    }

    private fun toast(msg: String) {
        Toast.makeText(this, msg, Toast.LENGTH_LONG).show()
    }


    //Handle after users click permission dialog
    @RequiresApi(Build.VERSION_CODES.JELLY_BEAN)
    override fun onRequestPermissionsResult(
        requestCode: Int,
        permissions: Array<out String>,
        grantResults: IntArray
    ) {
        //Every results will be a request code, permissions, grant result
        //We will always check requestCode first that whet is the permission going to do next?
        when (requestCode) {
            REQUEST_CODE_CAMERA -> {
                if (permissions.isNotEmpty() && grantResults[0] == GRANTED) {
                    toast("$CAMERA_PERMISSION  You have allow already")
                } else {
                    toast("The request of $CAMERA_PERMISSION was denied")
                }
            }
            REQUEST_CODE_READ -> {
                if (permissions.isNotEmpty() && grantResults[0] == GRANTED) {
                    toast("$READ_EXTERNAL_PERMISSION  You have allow already")
                } else {
                    toast("The request of $READ_EXTERNAL_PERMISSION was denied")
                }
            }
        }

    }
}
