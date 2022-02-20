package com.anadi.prabhupadalectures.android

import android.app.Activity
import android.app.Application
import android.os.Bundle
import androidx.lifecycle.lifecycleScope
import com.anadi.prabhupadalectures.data.Database
import com.anadi.prabhupadalectures.data.DatabaseDriverFactory
import com.anadi.prabhupadalectures.datamodel.DataModel
import com.anadi.prabhupadalectures.network.api.createPrabhupadaApi
import com.anadi.prabhupadalectures.repository.RepositoryImpl
import kotlinx.coroutines.*

class PrabhupadaApp : Application() {

    companion object {
        lateinit var app: PrabhupadaApp
    }


    private val activityLifecycleCallbacks = object : ActivityLifecycleCallbacks {
        override fun onActivityPaused(activity: Activity) {}
        override fun onActivityStarted(activity: Activity) {}
        override fun onActivityDestroyed(activity: Activity) {}
        override fun onActivitySaveInstanceState(activity: Activity, outState: Bundle) {}
        override fun onActivityStopped(activity: Activity) {}
        override fun onActivityCreated(activity: Activity, savedInstanceState: Bundle?) {}
        override fun onActivityResumed(activity: Activity) {}
    }

    val api = createPrabhupadaApi()
    val repository = RepositoryImpl(api)
    val db = Database(DatabaseDriverFactory(this))
    val dataModel = DataModel(db, repository, BuildConfig.DEBUG)

    val applicationScope = CoroutineScope(SupervisorJob() + Dispatchers.Main)

    override fun onCreate() {
        super.onCreate()

        registerActivityLifecycleCallbacks(activityLifecycleCallbacks)
        app = this


        applicationScope.launch {
            delay(200L)
            dataModel.init()
        }
    }
}