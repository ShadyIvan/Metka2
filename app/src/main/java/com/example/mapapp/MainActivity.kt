package com.example.mapapp

import android.Manifest
import android.annotation.SuppressLint
import android.app.Dialog
import android.content.Context
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.drawable.BitmapDrawable
import android.location.Geocoder
import android.location.Location
import android.os.Bundle
import android.os.CountDownTimer
import android.util.Log
import android.view.View
import android.widget.Button
import android.widget.CheckBox
import android.widget.EditText
import android.widget.ImageButton
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ScrollView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.core.app.ActivityCompat
import androidx.core.content.ContextCompat
import androidx.core.graphics.scale
import com.google.android.gms.location.FusedLocationProviderClient
import com.google.android.gms.location.LocationServices
import com.google.android.gms.maps.CameraUpdateFactory
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.LatLng
import com.google.android.gms.maps.model.MarkerOptions
import com.google.firebase.database.DatabaseReference
import com.google.firebase.database.FirebaseDatabase
import com.google.firebase.firestore.QuerySnapshot
import com.google.firebase.firestore.ktx.firestore
import com.google.firebase.ktx.Firebase
import java.io.IOException
import java.lang.Integer.min
import java.util.Locale
import java.util.UUID


@Suppress("DEPRECATION")
class MainActivity : AppCompatActivity(), OnMapReadyCallback,
    GoogleMap.OnMyLocationButtonClickListener,
    GoogleMap.OnMyLocationClickListener {

    lateinit var map: GoogleMap
    private var isoffed = false;
    private var point = LatLng(52.28870867676121, 104.2803759506037)

    private val db = Firebase.firestore

    private var nickname = dataGroup.leaveDataNickname()

    lateinit var button_filter: Button
    lateinit var  filter_group: ScrollView
    lateinit var users_markers: CheckBox
    lateinit var turism_markers: CheckBox
    lateinit var cloub_markers: CheckBox
    lateinit var cinema_markers: CheckBox
    private var filter_check = false

    @SuppressLint("SuspiciousIndentation", "MissingInflatedId")
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        var mapFragment = supportFragmentManager.findFragmentById(R.id.map) as SupportMapFragment
        mapFragment.getMapAsync(this)

        //строка поиска
        val searchBtn = findViewById<ImageButton>(R.id.searchBtn)
        searchBtn.setOnClickListener(View.OnClickListener {
            searchLocation()
        })

        //меню группы (снизу справа)
        val fullscreenButton: Button = findViewById(R.id.btn_show_popup_menu)
        fullscreenButton.setOnClickListener {
            val intent = Intent(this@MainActivity, newgroup::class.java)
            intent.flags = Intent.FLAG_ACTIVITY_CLEAR_TASK or Intent.FLAG_ACTIVITY_NEW_TASK
            startActivity(intent)
        }

        button_filter = findViewById<Button>(R.id.button_filter)
        filter_group = findViewById<ScrollView>(R.id.filter_group)
        users_markers  = findViewById<CheckBox>(R.id.users_markers)
        turism_markers = findViewById<CheckBox>(R.id.turism_markers)
        cloub_markers = findViewById<CheckBox>(R.id.cloub_markers)
        cinema_markers = findViewById<CheckBox>(R.id.cinema_markers)

    }

    @SuppressLint("SuspiciousIndentation")
    override fun onMapReady(googleMap: GoogleMap) {
        map = googleMap
        map.setOnMyLocationButtonClickListener(this)
        map.setOnMyLocationClickListener(this)

        map.isMyLocationEnabled = ContextCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) == PackageManager.PERMISSION_GRANTED

        map.isBuildingsEnabled = true
        map.isIndoorEnabled = true
        map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 12f))
        map.setMinZoomPreference(12f)


        var traficonoff: ImageView = findViewById(R.id.traficonoff)
        traficonoff.setOnClickListener {
            if (!isoffed) {
                map.isTrafficEnabled = true;
                traficonoff.setImageResource(R.drawable.on)
                isoffed = true
            } else {
                map.isTrafficEnabled = false;
                traficonoff.setImageResource(R.drawable.off)
                isoffed = false
            }
        }

        var start = true
        if(start){
            dataGroup.enter_defualte_group(dataGroup.dataGroupMarkers())
            start = false
            db.collection(dataGroup.leave_defualte_group()).addSnapshotListener { snapshot, e ->
                if (snapshot != null && !snapshot.isEmpty) {
                    // Обновление карты на основе изменений в базе данных
                    updateMarker(map, snapshot)
                }
            }
        }

        //фильтр
        button_filter.setOnClickListener{
            start = false
            if (!filter_check){
                users_markers.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked) {
                        dataGroup.enter_defualte_group(dataGroup.dataGroupMarkers())

                        db.collection(dataGroup.leave_defualte_group()).addSnapshotListener { snapshot, e ->
                            if (snapshot != null && !snapshot.isEmpty) {
                                // Обновление карты на основе изменений в базе данных
                                updateMarker(map, snapshot)
                            }
                        }

                        turism_markers.isChecked = false
                        cloub_markers.isChecked = false
                        cinema_markers.isChecked = false
                    }else{
                        buttonView.isChecked = false
                        if(!users_markers.isChecked && !cloub_markers.isChecked && !turism_markers.isChecked && !cinema_markers.isChecked){
                            map.clear()
                        }
                    }
                }

                turism_markers.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked) {
                        dataGroup.enter_defualte_group("turism")
                        val name_group = dataGroup.leave_defualte_group()
                        db.collection(name_group).addSnapshotListener { snapshot, e ->
                            if (snapshot != null && !snapshot.isEmpty) {
                                // Обновление карты на основе изменений в базе данных
                                updateMarker(map, snapshot)
                            }
                        }
                        users_markers.isChecked = false
                        cloub_markers.isChecked = false
                        cinema_markers.isChecked = false
                    } else {
                        buttonView.isChecked = false
                        if(!users_markers.isChecked && !cloub_markers.isChecked && !turism_markers.isChecked && !cinema_markers.isChecked){
                            map.clear()
                        }
                    }
                }
                cloub_markers.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked) {
                        dataGroup.enter_defualte_group("cloub")
                        val name_group = dataGroup.leave_defualte_group()
                        db.collection(name_group).addSnapshotListener { snapshot, e ->
                            if (snapshot != null && !snapshot.isEmpty) {
                                // Обновление карты на основе изменений в базе данных
                                updateMarker(map, snapshot)
                            }
                        }
                        turism_markers.isChecked = false
                        users_markers.isChecked = false
                        cinema_markers.isChecked = false
                    } else {
                        buttonView.isChecked = false
                        if(!users_markers.isChecked && !cloub_markers.isChecked && !turism_markers.isChecked && !cinema_markers.isChecked){
                            map.clear()
                        }
                    }
                }

                cinema_markers.setOnCheckedChangeListener { buttonView, isChecked ->
                    if (isChecked) {
                        dataGroup.enter_defualte_group("cinema")
                        val name_group = dataGroup.leave_defualte_group()
                        db.collection(name_group).addSnapshotListener { snapshot, e ->
                            if (snapshot != null && !snapshot.isEmpty) {
                                // Обновление карты на основе изменений в базе данных
                                updateMarker(map, snapshot)
                            }
                        }
                        turism_markers.isChecked = false
                        cloub_markers.isChecked = false
                        users_markers.isChecked = false
                    } else {
                        buttonView.isChecked = false
                        if(!users_markers.isChecked && !cloub_markers.isChecked && !turism_markers.isChecked && !cinema_markers.isChecked){
                            map.clear()
                        }
                    }
                }

                filter_group.visibility = View.VISIBLE
                filter_check = true
            }else {
                filter_group.visibility = View.INVISIBLE
                filter_check = false
            }

        }

        map.setOnMapClickListener { point ->
            showPopup(point)
        }

        //информация об маркере
        map.setOnMarkerClickListener {marker ->
            marker.title = ""
            marker.snippet = ""
            //получение данных о маркере для информационного окна
            db.collection(dataGroup.leave_defualte_group()).get().addOnSuccessListener { documents ->
                for (document in documents) {
                    if (document.id != "settings"){
                        var title_new =  document.getString("title")
                        var disc_new =  document.getString("disc")
                        var lat_new =  document.get("lat").toString()
                        var lng_new =  document.get("lng").toString()
                        var nicname_info = document.getString("nickname")
                        var point_2 = LatLng(lat_new.toDouble()!!, lng_new.toDouble()!!)
                        var point_1 = marker.position
                        var id_check = document.id

                        if (point_2==point_1){
                            map.moveCamera(CameraUpdateFactory.newLatLng(point_1))
                            infomarker.infoMarker(title_new.toString(),
                                disc_new.toString(), nicname_info.toString(), this, id_check, map)
                        }
                    }
                    }
            }
            false
        }

    }

//добавление на карту маркеров
    fun updateMarker(map: GoogleMap, snapshot: QuerySnapshot){
        map.clear()

            for (document in snapshot.documents) {
                if (document.id != "settings"){
                    var title_new =  document.getString("title")
                    var disc_new =  document.getString("disc")
                    var lat_new =  document.get("lat").toString()
                    var lng_new =  document.get("lng").toString()
                    var icon_marker = document.getString("icon")
                    var point_2 = LatLng(lat_new.toDouble()!!, lng_new.toDouble()!!)

                    if (icon_marker != ""){
                        val resourceId = resources.getIdentifier(icon_marker, "drawable", packageName)
                        val iconDrawable = ContextCompat.getDrawable(this, resourceId)
                        val bitmap = (iconDrawable as BitmapDrawable).bitmap
                        val marker = MarkerOptions()
                            .title(title_new)
                            .snippet(disc_new)
                            .position(point_2)
                            .icon(BitmapDescriptorFactory.fromBitmap(bitmap.scale(100, 100, false)))
                        map.addMarker(marker)
                    }else {
                        val marker = MarkerOptions()
                            .title(title_new)
                            .snippet(disc_new)
                            .position(point_2)
                        map.addMarker(marker)
                    }
                }

            }
    }

    //Работа с метками и диалоговое окно для меток
    private fun showPopup(point: LatLng) {
        val marker = MarkerOptions()
        marker.position(LatLng(point.latitude, point.longitude))
        var icon_check = false

        val dialog = Dialog(this)
        dialog.setContentView(R.layout.marker_option)

        val titleOption = dialog.findViewById<EditText>(R.id.editTextMarkerTitle)
        val commOption = dialog.findViewById<EditText>(R.id.editTextMarkerDescription)
        val saveOption = dialog.findViewById<Button>(R.id.buttonSaveMarker)
        val changeOption = dialog.findViewById<Button>(R.id.changeMarker)

        //диалоговое окно для названия и описания маркера
        saveOption.setOnClickListener {
            marker.title(titleOption.text.toString())
            marker.snippet(commOption.text.toString())

            if (!icon_check) {
                dataGroup.iconEner("")
            }

            var id_count = ""
            db.collection(dataGroup.dataGroupMarkers()).get()
                .addOnSuccessListener { document_markers_null ->
                    if (document_markers_null.documents.size == 1){
                        db.collection(dataGroup.dataGroupMarkers()).document("1").set(
                            hashMapOf(
                                "title" to marker.title.toString(),
                                "disc" to marker.snippet.toString(),
                                "lat" to marker.position.latitude,
                                "lng" to marker.position.longitude,
                                "nickname" to nickname,
                                "id" to "1",
                                "group" to "users_markers",
                                "icon" to dataGroup.DataIcon()))
                    }else{
                        if (document_markers_null.size() >= 21 ){
                            var markers_array = mutableListOf<Int>()
                            for(document in document_markers_null){
                                        if(document.id != "settings"){
                                                var data_document_array = document.data
                                                for((key_array, value_array ) in data_document_array){
                                                    if (key_array == "id"){
                                                        var attay_int = value_array.toString()
                                                        markers_array.add(attay_int.toInt())
                                                    }
                                                }
                                        }
                                    }
                            val minimal_marker = markers_array.min()
                            db.collection(dataGroup.dataGroupMarkers()).document(minimal_marker.toString()).delete()

                            markers_array.remove(minimal_marker)
                            id_count = ((markers_array.max()+1).toString())

                            db.collection(dataGroup.dataGroupMarkers()).document(id_count).set(
                                hashMapOf(
                                    "title" to marker.title.toString(),
                                    "disc" to marker.snippet.toString(),
                                    "lat" to marker.position.latitude,
                                    "lng" to marker.position.longitude,
                                    "nickname" to nickname,
                                    "id" to id_count,
                                    "group" to "users_markers",
                                    "icon" to dataGroup.DataIcon()))
                        }else{
                            var markers_add_Add = mutableListOf<Int>()
                            for(document in document_markers_null){
                                if(document.id != "settings"){
                                        for(document_array_add in document_markers_null){
                                            var data_document_array_add = document_array_add.data
                                            for((key_array_add, value_array_add ) in data_document_array_add){
                                                if (key_array_add == "id"){
                                                    var attay_int_add = value_array_add.toString()
                                                    markers_add_Add.add(attay_int_add.toInt())
                                                }
                                            }
                                        }
                                }
                            }
                                    id_count = (markers_add_Add.max()+1).toString()
                                    db.collection(dataGroup.dataGroupMarkers()).document(id_count).set(
                                        hashMapOf(
                                            "title" to marker.title.toString(),
                                            "disc" to marker.snippet.toString(),
                                            "lat" to marker.position.latitude,
                                            "lng" to marker.position.longitude,
                                            "nickname" to nickname,
                                            "id" to id_count,
                                            "group" to "users_markers",
                                            "icon" to dataGroup.DataIcon()))
                        }
                    }
                }

            dialog.dismiss()
        }

                //выбор маркера
                changeOption.setOnClickListener { marker
                    icon_check = true
                    changemarker.changeMarker(marker, this)
                }

                dialog.show()
                //перенос камеры к маркеру
                map.moveCamera(CameraUpdateFactory.newLatLngZoom(point, 17f))

    }


    //поиск адреса
    private fun searchLocation() {
        val geocoder = Geocoder(this, Locale.getDefault())
        val addresses =
            geocoder.getFromLocation(point.latitude.toDouble(), point.longitude.toDouble(), 1)
        val cityName = addresses!![0].locality

        val searchTxt = findViewById<EditText>(R.id.search)
        val location = cityName + searchTxt.text.toString()

        try {
            val addressList = geocoder.getFromLocationName(location, 1)
            if (addressList != null && addressList.size > 0) {
                val address = addressList[0]

                val latLng = LatLng(address.latitude, address.longitude)

                map?.animateCamera(CameraUpdateFactory.newLatLngZoom(latLng, 17f))
                Toast.makeText(
                    applicationContext,
                    address.latitude.toString() + " " + address.longitude,
                    Toast.LENGTH_LONG
                ).show()
            }
        } catch (e: IOException) {
            e.printStackTrace()
        }
    }

    override fun onMyLocationClick(location: Location) {
        Toast.makeText(this, "Текущая локация:\n$location", Toast.LENGTH_LONG)
            .show()
    }

    override fun onMyLocationButtonClick(): Boolean {
        Toast.makeText(this, "Моё местоположение", Toast.LENGTH_SHORT)
            .show()
        return false
    }

    override fun onPause() {
        super.onPause()
        val sharedPref = getSharedPreferences("com.example.mapapp", Context.MODE_PRIVATE)
        val editor = sharedPref.edit()
        editor.putString("state", "my_state")
        editor.apply()
    }

    override fun onResume() {
        super.onResume()
        val sharedPref = getSharedPreferences("com.example.mapapp", Context.MODE_PRIVATE)
        val state = sharedPref.getString("state", "")
        // Восстановление состояния приложения
    }
}