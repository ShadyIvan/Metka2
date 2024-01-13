package com.example.mapapp

import android.content.Intent
import android.content.pm.PackageManager
import android.os.Bundle
import android.os.Handler
import android.Manifest
import android.os.Looper
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat

class SplashScreen : AppCompatActivity() {

    private val LOCATION_PERMISSION_REQUEST_CODE = 123

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.splash_screen)

        findViewById<ImageView>(R.id.splash_logo).setImageResource(R.drawable.screen2)

        //первый вход
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED) {
            // Разрешение уже предоставлено, можно отслеживать геолокацию
            val homeIntent = Intent(this@SplashScreen, LoginScreen::class.java)
            Handler(Looper.getMainLooper()).postDelayed({
                startActivity(homeIntent)
                finish()
            }, SPLASH_TIME_OUT.toLong())
        } else {
            ActivityCompat.requestPermissions(this, arrayOf(Manifest.permission.ACCESS_FINE_LOCATION), LOCATION_PERMISSION_REQUEST_CODE)
        }
    }

    companion object {
        const val SPLASH_TIME_OUT = 2000
    }

    override fun onRequestPermissionsResult(requestCode: Int, permissions: Array<String>, grantResults: IntArray) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults)
        when (requestCode) {
            LOCATION_PERMISSION_REQUEST_CODE -> {
                if (grantResults.isNotEmpty() && grantResults[0] == PackageManager.PERMISSION_GRANTED) {

                    val homeIntent = Intent(this@SplashScreen, LoginScreen::class.java)
                    Handler(Looper.getMainLooper()).postDelayed({
                        //Do some stuff here, like implement deep linking
                        startActivity(homeIntent)
                        finish()
                    }, SPLASH_TIME_OUT.toLong())
                    // Разрешение получено, можно отслеживать геолокацию
                } else {
                    val homeIntent = Intent(this@SplashScreen, LoginScreen::class.java)
                    Handler(Looper.getMainLooper()).postDelayed({
                        //Do some stuff here, like implement deep linking
                        startActivity(homeIntent)
                        finish()
                    }, SPLASH_TIME_OUT.toLong())
                    // Отказано в разрешении, геолокация недоступна
                }
                return
            }
        }
    }

}