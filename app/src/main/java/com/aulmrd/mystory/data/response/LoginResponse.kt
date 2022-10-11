package com.aulmrd.mystory.data.response

import com.aulmrd.mystory.data.result.LoginResult
import com.google.gson.annotations.SerializedName

data class LoginResponse(

    @field:SerializedName("loginResult")
  val LoginResult: LoginResult? = null,

    @field:SerializedName("error")
  val error: Boolean,

    @field:SerializedName("message")
  val message: String
)

