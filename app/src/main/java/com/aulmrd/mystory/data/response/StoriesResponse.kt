package com.aulmrd.mystory.data.response

import android.os.Parcelable
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StoriesResponse(
    @field:SerializedName("listStory")
    val listStory: List<ListStoryItem>,

    @field:SerializedName("error")
    val error: Boolean,

    @field:SerializedName("message")
    val message: String
) :Parcelable

@Parcelize
data class ListStoryItem(
    @field:SerializedName("photoUrl")
    val photoUrl: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("lon")
    val lon: String,

    @field:SerializedName("id")
    val id: String,

    @field:SerializedName("lat")
    val lat: String
) :Parcelable
