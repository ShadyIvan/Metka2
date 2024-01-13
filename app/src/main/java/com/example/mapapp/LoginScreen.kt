package com.example.mapapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

class LoginScreen: AppCompatActivity() {

    private var loginUser = ""
    private var passwordGroup = ""
    private val db = Firebase.firestore

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.loginscreen)

        val loginEdit = findViewById<EditText>(R.id.login)
        val passwordEdit = findViewById<EditText>(R.id.password_acc)
        val enterAcc = findViewById<Button>(R.id.enterAcc)
        val regAcc = findViewById<Button>(R.id.regAcc)

        enterAcc.setOnClickListener {
            var check_size = 0
            loginUser = loginEdit.text.toString()
            passwordGroup = passwordEdit.text.toString()

            if(loginUser == "" || passwordGroup==""){
                val toast_check = Toast
                    .makeText(applicationContext, "Поля не заполнены",
                        Toast.LENGTH_SHORT)
                toast_check.show()
            }else{
                db.collection("users").get().addOnSuccessListener { documents ->
                    for (document in documents) {
                        // обработка каждого документа
                        var login = document.getString("login")
                        var password = document.getString("password")
                        var nickname = document.getString("nickname")
                        check_size+=1
                        if (login == loginUser){
                            if (password == passwordGroup){
                                dataGroup.enterData(nickname.toString())
                                val homeIntent = Intent(this@LoginScreen, MainActivity::class.java)
                                startActivity(homeIntent)
                                finish()
                                break
                            }else {
                                passwordEdit.text = null
                                val toast_pass = Toast
                                    .makeText(applicationContext, "Такой пароль не найден",
                                        Toast.LENGTH_SHORT)
                                toast_pass.show()
                                break }
                        }else if(check_size == documents.size() && login!=loginUser ){
                            loginEdit.text = null
                            passwordEdit.text = null
                            val toast = Toast
                                .makeText(applicationContext, "Такой логин не найден",
                                    Toast.LENGTH_SHORT)
                            toast.show()
                            break }
                    }
                }
            }
        }

        regAcc.setOnClickListener {
            val homeIntent_reg = Intent(this@LoginScreen, regacc::class.java)
            startActivity(homeIntent_reg)
        }

    }
}