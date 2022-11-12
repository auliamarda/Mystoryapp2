package com.aulmrd.mystory.widget

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Bitmap
import android.widget.RemoteViews
import android.widget.RemoteViewsService
import com.aulmrd.mystory.R
import com.aulmrd.mystory.data.entity.StoryEntity
import com.aulmrd.mystory.data.room.Dao
import com.aulmrd.mystory.data.room.StoryDatabase
import com.bumptech.glide.Glide
import kotlinx.coroutines.runBlocking

class StackRemoteViewsFactory (private val mContext: Context): RemoteViewsService.RemoteViewsFactory {

    private var mWidgetItems = listOf<StoryEntity>()
    private lateinit var dao: Dao

    override fun onCreate() {
        dao = StoryDatabase.getInstance(mContext).dao()
        fetchDataDB()
    }

    override fun onDataSetChanged() {
        fetchDataDB()
    }

    override fun onDestroy() {
    }

    override fun getCount(): Int = mWidgetItems.size

    @SuppressLint("RemoteViewLayout")
    override fun getViewAt(position: Int): RemoteViews {
        val rv = RemoteViews(mContext.packageName, R.layout.item_story)
        try {
            val bitmap: Bitmap = Glide.with(mContext.applicationContext)
                .asBitmap()
                .load(mWidgetItems[position].photoUrl)
                .submit()
                .get()
            rv.setImageViewBitmap(R.id.img_avatar, bitmap)
        } catch (e: Exception) {
            e.printStackTrace()
        }
        return rv
    }

    override fun getLoadingView(): RemoteViews? = null
    override fun getViewTypeCount(): Int = 1
    override fun getItemId(position: Int): Long = 0
    override fun hasStableIds(): Boolean = false

    private fun fetchDataDB() {
        runBlocking {
            mWidgetItems = dao.getStoriesForWidget()
        }
    }
}