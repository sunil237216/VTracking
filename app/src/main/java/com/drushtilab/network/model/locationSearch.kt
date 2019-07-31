package com.drushtilab.network.model

data class locationSearch(
    val D_Lat: Double,
    val D_Long: Double,
    val D_Time: String,
    val D_ZoneId: Int,
    val P_Lat: Double,
    val P_Long: Double,
    val P_Time: String,
    val P_ZoneId: Int,
    val Status: Int,
    val UserId: Int,
    val WinNo: String,
    val ZoneName:String
)