package com.dicoding.courseschedule.ui

import android.app.Activity
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.dicoding.courseschedule.data.DataRepository
import java.lang.IllegalStateException
import java.lang.RuntimeException
import java.lang.reflect.InvocationTargetException

class ViewModelFactory(private val repo: DataRepository?) : ViewModelProvider.Factory {

    companion object{
        fun createFactory(activity: Activity): ViewModelFactory {
            val context = activity.applicationContext
                ?: throw IllegalStateException("Not yet attached to Application")

            return ViewModelFactory(DataRepository.getInstance(context))
        }
    }

    override fun <T : ViewModel?> create(modelClass: Class<T>): T {
        try {
            return modelClass.getConstructor(DataRepository::class.java).newInstance(repo)
        } catch (e: InstantiationException){
            throw RuntimeException("Cannot create an instance of $modelClass", e)
        } catch (e: IllegalAccessException){
            throw RuntimeException("Cannot create an instance of $modelClass", e)
        } catch (e: NoSuchMethodError){
            throw RuntimeException("Cannot create an instance of $modelClass", e)
        } catch (e: InvocationTargetException){
            throw RuntimeException("Cannot create an instance of $modelClass", e)
        }
    }
}