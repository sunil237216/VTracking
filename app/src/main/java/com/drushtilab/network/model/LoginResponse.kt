package com.drushtilab.network.model

data class LoginResponse(
    val EmailId: Any,
    val LockCount: Int,
    val LoginType: Int,
    val Token: String,
    val TokenId: String,
    val UserId: Int,
    val designation: Any,
    val displayName: Any,
    val mobileNumber: Any
)