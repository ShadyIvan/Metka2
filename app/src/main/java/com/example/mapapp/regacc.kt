package com.example.mapapp

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase


class regacc: AppCompatActivity() {

    private var loginUser_reg = ""
    private var passwordGroup_reg = ""
    private var nickname_reg_acc = ""
    private val db = Firebase.firestore

    @SuppressLint("MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.registracia)

        val nickname_reg = findViewById<EditText>(R.id.nickname_reg)
        val login_reg = findViewById<EditText>(R.id.login_reg)
        val password_reg = findViewById<EditText>(R.id.password_acc_reg)
        val regAcc_butt = findViewById<Button>(R.id.regAcc_reg)

        regAcc_butt.setOnClickListener {
            var check = 0
            var check_size = 0
            nickname_reg_acc = nickname_reg.text.toString()
            loginUser_reg = login_reg.text.toString()
            passwordGroup_reg = password_reg.text.toString()

            if(nickname_reg_acc == "" || loginUser_reg == "" || passwordGroup_reg == ""){
                val toast_check = Toast
                    .makeText(applicationContext, "Поля не заполнены",
                        Toast.LENGTH_SHORT)
                toast_check.show()
            }else{
                db.collection("users").get().addOnSuccessListener { documents ->
                    for (document in documents ) {
                        // обработка каждого документа
                        var login  = document.getString("login")
                        var nickname  = document.getString("nickname")
                        check_size+=1
                        if(loginUser_reg == login){
                            login_reg.text = null
                            val toast = Toast
                                .makeText(applicationContext, "Такой логин уже зарегистрирован!",
                                    Toast.LENGTH_SHORT)
                            toast.show()
                            check = 1
                            break }
                        if(nickname_reg_acc == nickname){
                            nickname_reg.text = null
                            val toast = Toast
                                .makeText(applicationContext, "Такой никнейм уже занят!",
                                    Toast.LENGTH_SHORT)
                            toast.show()
                            check = 1
                            break
                        }
                        if (check_size == documents.size() && check == 0){
                            db.collection("users").document(loginUser_reg).set(
                                hashMapOf(
                                    "login" to loginUser_reg,
                                    "password" to passwordGroup_reg,
                                    "nickname" to nickname_reg_acc))
                            val homeIntent = Intent(this@regacc, LoginScreen::class.java)
                            startActivity(homeIntent)
                            finish() } } }
            }
        }
    }
}