package com.example.mapapp

object dataGroup {
    private var nickname = ""
    private var icon_id = ""
    private var markersGroup = "markers"
    private var default_marker = ""


    fun enterData(nick: String){
        nickname = nick
    }

    fun leaveDataNickname(): String {
        return nickname
    }

    fun iconEner(icon: String){
        icon_id = icon
    }

    fun DataIcon(): String {
        return icon_id
    }

    fun enterDataGroup(groupMarkers: String){
        markersGroup = groupMarkers
    }

    fun dataGroupMarkers(): String{
        return markersGroup
    }

    fun enter_defualte_group(dufualt_markers: String){
        default_marker = dufualt_markers
    }

    fun leave_defualte_group(): String{
        return default_marker
    }

}