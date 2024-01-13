package com.example.mapapp

import android.app.Dialog
import android.content.Context
import android.widget.ImageButton
import com.google.android.gms.maps.model.BitmapDescriptorFactory
import com.google.android.gms.maps.model.MarkerOptions

object changemarker {
    private var icon_id = ""

    fun changeMarker(marker: MarkerOptions, context: Context) {
        val dialog = Dialog(context)
        dialog.setContentView(R.layout.markericon)

        val marker1 = dialog.findViewById<ImageButton>(R.id.marker1)
        val marker2 = dialog.findViewById<ImageButton>(R.id.marker2)
        val marker3 = dialog.findViewById<ImageButton>(R.id.marker3)
        val marker4 = dialog.findViewById<ImageButton>(R.id.marker4)
        val marker5 = dialog.findViewById<ImageButton>(R.id.marker5)
        val marker6 = dialog.findViewById<ImageButton>(R.id.marker6)
        val marker7 = dialog.findViewById<ImageButton>(R.id.marker7)
        val marker8 = dialog.findViewById<ImageButton>(R.id.marker8)
        val marker9 = dialog.findViewById<ImageButton>(R.id.marker9)
        val marker10 = dialog.findViewById<ImageButton>(R.id.marker10)

        marker1.setOnClickListener {
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker10))
            icon_id = "marker10"
            dataGroup.iconEner(icon_id)
            dialog.dismiss()
        }
        marker2.setOnClickListener {
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker1))
            icon_id = "marker1"
            dataGroup.iconEner(icon_id)
            dialog.dismiss()
        }
        marker3.setOnClickListener {
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker2))
            icon_id = "marker2"
            dataGroup.iconEner(icon_id)
            dialog.dismiss()
        }
        marker4.setOnClickListener {
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker3))
            icon_id = "marker3"
            dataGroup.iconEner(icon_id)
            dialog.dismiss()
        }
        marker5.setOnClickListener {
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker4))
            icon_id = "marker4"
            dataGroup.iconEner(icon_id)
            dialog.dismiss()
        }
        marker6.setOnClickListener {
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker5))
            icon_id = "marker5"
            dataGroup.iconEner(icon_id)
            dialog.dismiss()
        }
        marker7.setOnClickListener {
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker6))
            icon_id = "marker6"
            dataGroup.iconEner(icon_id)
            dialog.dismiss()
        }
        marker8.setOnClickListener {
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker7))
            icon_id = "marker7"
            dataGroup.iconEner(icon_id)
            dialog.dismiss()
        }
        marker9.setOnClickListener {
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker8))
            icon_id = "marker8"
            dataGroup.iconEner(icon_id)
            dialog.dismiss()
        }
        marker10.setOnClickListener {
            marker.icon(BitmapDescriptorFactory.fromResource(R.drawable.marker9))
            icon_id = "marker9"
            dataGroup.iconEner(icon_id)
            dialog.dismiss()
        }
        dialog.show()
    }
}