package com.aulmrd.mystory.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import com.google.gson.annotations.SerializedName
import kotlinx.android.parcel.Parcelize

@Entity(tableName = "story_entity")
@Parcelize
data class StoryEntity(
    @field:SerializedName("photoUrl")
    val photoUrl: String,

    @field:SerializedName("createAt")
    val createAt: String,

    @field:SerializedName("name")
    val name: String,

    @field:SerializedName("description")
    val description: String,

    @field:SerializedName("lon")
    val lon: String,

    @PrimaryKey
    @field:SerializedName("id")
    val id: String,
    @field:SerializedName("lat")
    val lat: String
): Parcelable
