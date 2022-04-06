package cn.nba.map13.base

import android.app.Application
import cn.nba.map13.R
import com.mapbox.android.core.location.LocationEngineProvider
import com.mapbox.search.MapboxSearchSdk

class App:Application() {
    override fun onCreate() {
        super.onCreate()
        MapboxSearchSdk.initialize(
            application = this,
            accessToken = getString(R.string.mapbox_access_token),
            locationEngine = LocationEngineProvider.getBestLocationEngine(this)
        )
    }
}