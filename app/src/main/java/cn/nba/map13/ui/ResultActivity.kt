package cn.nba.map13.ui

import cn.nba.map13.R
import cn.nba.map13.base.BasePage
import cn.nba.map13.utils.addMarker
import cn.nba.map13.utils.moveMap
import cn.nba.map13.utils.setCurrentLocation
import com.mapbox.geojson.Point
import kotlinx.android.synthetic.main.layout_result.*

class ResultActivity : BasePage(R.layout.layout_result) {

    private val lat by lazy {
        intent.getDoubleExtra("lat", 0.0)
    }
    private val lng by lazy {
        intent.getDoubleExtra("lng", 0.0)
    }

    override fun initView() {
        mapView.setCurrentLocation()
        mapView.addMarker(Point.fromLngLat(lng, lat))
        mapView.moveMap(Point.fromLngLat(lng, lat))
    }
}