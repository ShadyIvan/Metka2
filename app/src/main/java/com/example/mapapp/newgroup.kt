package com.example.mapapp

import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.view.marginBottom
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.FieldValue
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.util.concurrent.CompletableFuture


class newgroup: AppCompatActivity() {

    private var nameGroup = ""
    private var passwordGroup = ""

    private val db = Firebase.firestore

    @SuppressLint("MissingInflatedId", "SuspiciousIndentation")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.dialog)

        val mapGroupAll = findViewById<Button>(R.id.btn_option_1)
        val searchGroupMap = findViewById<Button>(R.id.search_button)
        val addButtonGroup = findViewById<Button>(R.id.add_button)
        val button_delete =  findViewById<Button>(R.id.delete_button)
        val mLayoutGroupContainer = findViewById<LinearLayout>(R.id.buttons_layout)
        val map_group_data = mutableListOf<String>()
        val createdButtons = mutableListOf<String>()


        db.collection("name_group_array").addSnapshotListener { snapshot, e ->
            map_group_data.clear()
            createdButtons.clear()
            mLayoutGroupContainer.removeAllViews()

            for(document in  snapshot!!.documents){
                val data = document.data!!
                val data_name = document.id
                if(data_name == "group"){
                    for((key, value) in data){
                        map_group_data.add(value.toString())
                    }
                }
            }

            for (group_data in map_group_data){
                db.collection("name_group_array").document("$group_data users").get().addOnSuccessListener { document_users ->
                    val data = document_users!!.data
                    if(data != null){
                        for ((key, value) in data!!) {
                                if(value == dataGroup.leaveDataNickname()){
                                    if(group_data !in createdButtons){
                                    val newButton = Button(this)
                                    newButton.text = group_data
                                    newButton.setBackgroundResource(R.drawable.rounded_button)
                                    newButton.transformationMethod = null;
                                    val layoutParams = LinearLayout.LayoutParams(
                                        LinearLayout.LayoutParams.MATCH_PARENT,
                                        LinearLayout.LayoutParams.WRAP_CONTENT
                                    )

                                    layoutParams.height = 130
                                    layoutParams.setMargins(10, 20, 10, 10)
                                    newButton.layoutParams = layoutParams

                                    newButton.setOnClickListener {
                                        dataGroup.enterDataGroup(group_data)
                                        val intent = Intent(this@newgroup, MainActivity::class.java)
                                        intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
                                        startActivity(intent)
                                        finish()
                                    }
                                    createdButtons.add(group_data)
                                    mLayoutGroupContainer.addView(newButton)
                                    break
                                }
                            }

                        }
                    }
                }
            }
        }


        mapGroupAll.setOnClickListener {
            dataGroup.enterDataGroup("markers")
            val intent = Intent(this@newgroup, MainActivity::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
            finish()
        }

        searchGroupMap.setOnClickListener {
            SearchGroup()
        }

        addButtonGroup.setOnClickListener {
            NameGroupNew()
        }

        button_delete.setOnClickListener {
            val dialog = Dialog(this)
            dialog.setContentView(R.layout.deletegroup)

            val deleteButton_data = dialog.findViewById<EditText>(R.id.deleteButton)
            val button_confirm = dialog.findViewById<Button>(R.id.confirm_button)

            button_confirm.setOnClickListener {
                val name_group_delete = deleteButton_data.text.toString()
                //проверка группы на существование
                db.collection("name_group_array").document("group").get().addOnSuccessListener { document ->
                    val data = document!!.data
                    var check_group_data = false
                    if(data != null){
                        for ((key, value) in data!!) {
                            if(value.toString() == name_group_delete){
                                check_group_data = true
                            }
                        }
                        if(check_group_data){
                            db.collection(name_group_delete).document("settings").get().addOnSuccessListener { document_admin ->
                                val admin_group = document_admin.getString("admin")
                                //для админа
                                if (dataGroup.leaveDataNickname() == admin_group){
                                    db.collection("name_group_array").document("group").update(name_group_delete, FieldValue.delete())
                                    db.collection("name_group_array").document("$name_group_delete users").delete()
                                    db.collection(name_group_delete).get().addOnSuccessListener { document_elements ->
                                    for(documets_delete in document_elements){
                                        db.collection(name_group_delete).document(documets_delete.id).delete()
                                    }
                                    }
                                }else{
                                //для участника
                                    db.collection("name_group_array").document("$name_group_delete users").get().addOnSuccessListener { document_user ->
                                            val data_user = document_user!!.data
                                            if (data_user != null) {
                                                var iser_check_delete = false
                                                for ((key_user, value_user) in data_user!!) {
                                                    if (value_user.toString() == dataGroup.leaveDataNickname()) {
                                                        iser_check_delete = true
                                                    }
                                                }
                                                if (iser_check_delete){
                                                    db.collection("name_group_array").document("$name_group_delete users")
                                                        .update(dataGroup.leaveDataNickname(), FieldValue.delete())
                                                }else{
                                                    val toast = Toast.makeText(applicationContext, "Вы не состоите в группе!",
                                                        Toast.LENGTH_SHORT)
                                                    toast.show()
                                                }
                                            }
                                        }
                                }
                            }
                        }else{
                            val toast = Toast.makeText(applicationContext, "Такой группы не существует!",
                                Toast.LENGTH_SHORT)
                            toast.show()
                        }
                    }
                }
                dialog.dismiss()
            }
            dialog.show()
        }

    }

     fun SearchGroup() {
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.searchgroup)

        val nameButton_search = dialog.findViewById<EditText>(R.id.searcnName)
        val passgroup_search = dialog.findViewById<EditText>(R.id.passwordGroupSearch)
        val button_add_search =  dialog.findViewById<Button>(R.id.search)

        button_add_search.setOnClickListener {
            var nameGroupData = nameButton_search.text.toString()
            var passwordGroup = passgroup_search.text.toString()

            db.collection("name_group_array").document("group").get().addOnSuccessListener { document ->
                val data = document!!.data
                var check_data = false
                if (data != null) {
                    for ((key, value) in data!!) {
                        if(nameGroupData == value) {
                            db.collection(value).document("settings").get().addOnSuccessListener { document ->
                                var password_check =  document.getString("password")
                                if (password_check == passwordGroup){
                                    db.collection("name_group_array").document("$nameGroupData users").update(
                                        hashMapOf(
                                            dataGroup.leaveDataNickname() to dataGroup.leaveDataNickname()
                                        ) as Map<String, Any>
                                    )
                                }else {
                                    val toast = Toast.makeText(applicationContext, "Пароль не верный!",
                                        Toast.LENGTH_SHORT)
                                    toast.show()
                                }
                            }
                            check_data=true
                        }
                    }
                    if (!check_data) {
                        val toast = Toast.makeText(applicationContext, "Такая группа не существует!",
                            Toast.LENGTH_SHORT)
                        toast.show()
                    }
                }
            }
            dialog.dismiss()
        }
        dialog.show()
    }

    @SuppressLint("ShowToast", "SuspiciousIndentation")
    private fun NameGroupNew(){
        val dialog = Dialog(this)
        dialog.setContentView(R.layout.namegroup)

        val nameButton = dialog.findViewById<EditText>(R.id.namegroup)
        val passgroup = dialog.findViewById<EditText>(R.id.passwordgroup)
        val button_add =  dialog.findViewById<Button>(R.id.add_button_dialog)


        button_add.setOnClickListener {
            nameGroup = nameButton.text.toString()
            passwordGroup = passgroup.text.toString()
            var check = true

                db.collection("name_group_array").document("group").get().addOnSuccessListener { document ->

                val data = document!!.data
                    if (data != null) {
                        for ((key, value) in data!!) {
                            if(nameGroup == value) {
                                check = false
                                nameButton.text = null
                                passgroup.text = null
                                val toast = Toast.makeText(applicationContext, "Такая группа уже существует!",
                                    Toast.LENGTH_SHORT)
                                toast.show()
                            }
                        }
                    }
                    if (check) {
                        createNewButton(nameGroup, passwordGroup)
                    }
                }
            dialog.dismiss()
        }
        dialog.show()
    }

    private fun createNewButton(nameGroup: String, passwordGroup: String) {

        db.collection(nameGroup).document("settings").set(
            hashMapOf(
                "namegroup" to nameGroup,
                "password" to passwordGroup,
                "admin" to dataGroup.leaveDataNickname()
            ))

        db.collection("name_group_array").document("group").get().addOnSuccessListener { document ->
            val data = document!!.data
            if (data != null) {
                db.collection("name_group_array").document("group").update(
                    hashMapOf(
                        nameGroup to nameGroup,
                    ) as Map<String, Any>
                )
            }else {
                db.collection("name_group_array").document("group").set(
                    hashMapOf(
                        nameGroup to nameGroup,)
                    )
                }
            }

        db.collection("name_group_array").document("$nameGroup users").set(
            hashMapOf(
                dataGroup.leaveDataNickname() to dataGroup.leaveDataNickname()
            ))
    }

}