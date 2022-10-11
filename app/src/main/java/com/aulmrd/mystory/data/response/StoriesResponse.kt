package com.aulmrd.mystory.data.response

import android.os.Parcelable
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Parcelize
data class StoriesResponse(
    @field:SerializedName("listStory")
    val listStory: List<ListStoryItem>? = null,
    @field:SerializedName("error")
    val error: Boolean,
    @field:SerializedName("message")
    val message: String
) :Parcelable

@Parcelize
data class ListStoryItem(
    @field:SerializedName("photoUrl")
    val photoUrl: String? = null,
    @field:SerializedName("name")
    val name: String? = null,
    @field:SerializedName("description")
    val description: String? = null
) :Parcelable
