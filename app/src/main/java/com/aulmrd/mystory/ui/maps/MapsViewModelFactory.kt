package com.aulmrd.mystory.ui.maps

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.aulmrd.mystory.data.repository.StoryRepository

class MapsViewModelFactory private constructor(private val storyRepository: StoryRepository ) : ViewModelProvider.NewInstanceFactory() {

    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(MapsViewModels::class.java)){
            return MapsViewModels(storyRepository) as T
        }
        throw illegalArgumentException("Unknown view model class : ${modelClass.name}")
    }

    companion object {
        @Volatile
        private var instance: MapsViewModelFactory? = null
        fun getInstance(context: Context): MapsViewModelFactory =
            instance?: synchronized(this) {
                instance?: MapsViewModelFactory(Injection.provideRepository(context))
            }.also { instance = it }
    }
}