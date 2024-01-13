package com.example.mapapp

import android.content.Context
import android.util.Log
import android.widget.Button
import android.widget.ImageButton
import android.widget.TextView
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.model.MarkerOptions
import com.google.android.material.bottomsheet.BottomSheetDialog
import com.google.firebase.firestore.QueryDocumentSnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase

object infomarker {
    private val db = Firebase.firestore

    fun infoMarker(titleSet:String, discSet: String,nickname: String, context: Context, id_check: String, map: GoogleMap) {
        val dialog = BottomSheetDialog(context)
        val name = nickname
        dialog.setContentView(R.layout.infomarker)

        val markertitle = dialog.findViewById<TextView>(R.id.marker_title)
        val markercomm = dialog.findViewById<TextView>(R.id.marker_description)

        //лайки и дизлайки
        val enterlike = dialog.findViewById<ImageButton>(R.id.enterLike)
        val enterDislike = dialog.findViewById<ImageButton>(R.id.enterDislike)
        val countLike = dialog.findViewById<TextView>(R.id.LikeCount)
        val countDislike = dialog.findViewById<TextView>(R.id.DislikeCount)

        db.collection(dataGroup.leave_defualte_group()).document(id_check).addSnapshotListener { snapshot, e ->
            val data = snapshot?.data
            val keys = data?.keys
            val values = data?.values
            if (keys != null && values != null) {
                for ((index, key) in keys.withIndex()) {
                    if (key.toString() == "like") {
                        var countLike_check = values.elementAt(index).toString()
                        var likecount =  countLike_check
                        countLike!!.text = likecount
                    }
                }
            for ((index_2, key_2) in keys.withIndex()) {
                if (key_2.toString() == "dislike") {
                    var countLike_check = values.elementAt(index_2).toString()
                    var likecount =  countLike_check
                    countDislike!!.text = likecount
                }
            }
            }
        }

        var chech_add = true
        var check_delete = true
        var likeClicked = false
        var dislikeClicked = false

        enterlike?.setOnClickListener {
            if (dislikeClicked){
                dislikeClicked=false
                chech_add=false
                check_delete=true
                likeClicked = true
                db.collection(dataGroup.leave_defualte_group()).document(id_check).get().addOnSuccessListener { document_like ->
                    val data = document_like?.data
                    val keys = data?.keys
                    val values = data?.values
                    if (keys != null && values != null) {
                        for ((index, key) in keys.withIndex()) {
                            if (key.toString() == "dislike") {
                                var countLike_check = values.elementAt(index).toString()
                                var dislikecount =  (countLike_check.toInt()-1).toString()
                                db.collection(dataGroup.leave_defualte_group()).document(id_check).update("dislike", dislikecount)
                            }
                            if (key.toString() == "like") {
                                var countLike_check = values.elementAt(index).toString()
                                var dislikecount =  (countLike_check.toInt()+1).toString()
                                db.collection(dataGroup.leave_defualte_group()).document(id_check).update("like", dislikecount)
                            }
                        }
                    }
                }
            }else{
                db.collection(dataGroup.leave_defualte_group()).document(id_check).get().addOnSuccessListener { document_like ->
                    var check_like = false
                    val data = document_like?.data
                    val keys = data?.keys
                    val values = data?.values
                    if (keys != null && values != null) {
                        for ((index, key) in keys.withIndex()) {
                            if(key.toString() == "like"){
                                if (chech_add) {
                                    var countLike_check = values.elementAt(index).toString()
                                    var likecount =  (countLike_check.toInt()+1).toString()
                                    chech_add=false
                                    check_like = true
                                    likeClicked = true
                                    db.collection(dataGroup.leave_defualte_group()).document(id_check).update("like", likecount)
                                }else{
                                    likeClicked = false
                                    dislikeClicked=false
                                    var countLike_check = values.elementAt(index).toString()
                                    var likecount =  (countLike_check.toInt()-1).toString()
                                    chech_add = true
                                    check_like = true
                                    db.collection(dataGroup.leave_defualte_group()).document(id_check).update("like", likecount)
                                }
                            }
                        }
                        if (!check_like){
                            if (chech_add) {
                                likeClicked = true
                                chech_add=false
                                db.collection(dataGroup.leave_defualte_group()).document(id_check).update("like", 1)
                            }else{
                                chech_add = true
                                likeClicked = false
                                dislikeClicked=false
                                db.collection(dataGroup.leave_defualte_group()).document(id_check).update("like", 0)
                            }

                        }
                    }
                }
            }
        }

        enterDislike?.setOnClickListener {
            if (likeClicked){
                check_delete=false
                chech_add=true
                dislikeClicked = true
                likeClicked=false
                db.collection(dataGroup.leave_defualte_group()).document(id_check).get().addOnSuccessListener { document_like ->
                    val data = document_like?.data
                    val keys = data?.keys
                    val values = data?.values
                    if (keys != null && values != null) {
                        for ((index, key) in keys.withIndex()) {
                            if (key.toString() == "like") {
                                var countLike_check = values.elementAt(index).toString()
                                var likecount =  (countLike_check.toInt()-1).toString()
                                db.collection(dataGroup.leave_defualte_group()).document(id_check).update("like", likecount)
                            }
                            if (key.toString() == "dislike") {
                                var countLike_check = values.elementAt(index).toString()
                                var dislikecount =  (countLike_check.toInt()+1).toString()
                                db.collection(dataGroup.leave_defualte_group()).document(id_check).update("dislike", dislikecount)
                            }
                        }
                    }
                }
            }else{
                db.collection(dataGroup.leave_defualte_group()).document(id_check).get().addOnSuccessListener { document_like ->
                    var check_like = false
                    val data = document_like?.data
                    val keys = data?.keys
                    val values = data?.values
                    if (keys != null && values != null) {
                        for ((index, key) in keys.withIndex()) {
                            if(key.toString() == "dislike"){
                                if(check_delete){
                                    var countLike_check = values.elementAt(index).toString()
                                    var likecount =  (countLike_check.toInt()+1).toString()
                                    check_delete = false
                                    dislikeClicked = true
                                    check_like = true
                                    db.collection(dataGroup.leave_defualte_group()).document(id_check).update("dislike", likecount)
                                }else{
                                    var countLike_check = values.elementAt(index).toString()
                                    var likecount =  (countLike_check.toInt()-1).toString()
                                    check_delete = true
                                    check_like = true
                                    dislikeClicked = false
                                    likeClicked=false
                                    db.collection(dataGroup.leave_defualte_group()).document(id_check).update("dislike", likecount)
                                }
                            }
                        }
                        if (!check_like){
                            if (check_delete) {
                                check_delete = false
                                dislikeClicked = true
                                db.collection(dataGroup.leave_defualte_group()).document(id_check).update("dislike", 1)
                            }else{
                                check_delete = true
                                dislikeClicked = false
                                likeClicked=false
                                db.collection(dataGroup.leave_defualte_group()).document(id_check).update("dislike", 0)
                            }

                        }
                    }
                }
            }
        }

        //удалить маркер если вы хозяин
        if (name == dataGroup.leaveDataNickname()){
            val delete_marker_info = dialog.findViewById<Button>(R.id.delete_marker)
            delete_marker_info?.setOnClickListener {
                db.collection(dataGroup.dataGroupMarkers()).document(id_check).delete()
                dialog.dismiss()
            }
        }else{
            val delete_marker_info = dialog.findViewById<Button>(R.id.delete_marker)
            delete_marker_info!!.text = ""
        }

        markertitle!!.text = titleSet
        markercomm!!.text = discSet + "\n"+"автор "+name
        dialog.show();
    }

}
