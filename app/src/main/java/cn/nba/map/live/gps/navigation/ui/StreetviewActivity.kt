package cn.nba.map.live.gps.navigation.ui

import android.annotation.SuppressLint
import android.view.MotionEvent
import cn.nba.map.live.gps.navigation.R
import cn.nba.map.live.gps.navigation.base.BasePage
import cn.nba.map.live.gps.navigation.utils.loadStyle
import cn.nba.map.live.gps.navigation.utils.setCamera
import cn.nba.map.live.gps.navigation.utils.setCameraChangeListener
import cn.nba.map.live.gps.navigation.utils.setCurrentLocation
import com.mapbox.geojson.Point
import com.mapbox.maps.Style
import kotlinx.android.synthetic.main.layout_street.*

@SuppressLint("ClickableViewAccessibility", "Lifecycle")
class StreetviewActivity : BasePage(R.layout.layout_street) {
    private var isTopTouch = false
    private var isBottomTouch = false
    override fun initView() {
        setMapView1()
        setMapView2()
    }

    private fun setMapView1() {
        mapView1.let {
            it.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    isTopTouch = true
                } else if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                    isTopTouch = false
                }
                false
            }
            it.setCurrentLocation()
            it.setCameraChangeListener { d, d2 ->
                if (isTopTouch && !isBottomTouch) {
                    mapView2.setCamera(Point.fromLngLat(d, d2))
                }
            }
            it.loadStyle(Style.OUTDOORS)
        }
    }

    private fun setMapView2() {
        mapView2.let {
            it.setOnTouchListener { _, event ->
                if (event.action == MotionEvent.ACTION_DOWN) {
                    isBottomTouch = true
                } else if (event.action == MotionEvent.ACTION_UP || event.action == MotionEvent.ACTION_CANCEL) {
                    isBottomTouch = false
                }
                false
            }

            it.setCurrentLocation()
            it.setCameraChangeListener { lng, lat ->
                if (!isTopTouch && isBottomTouch) {
                    mapView1.setCamera(Point.fromLngLat(lng, lat))
                }
            }
            it.loadStyle(Style.SATELLITE_STREETS)
        }
    }

    override fun onStart() {
        super.onStart()
        mapView1.onStart()
        mapView2.onStart()
    }

    override fun onStop() {
        super.onStop()
        mapView1.onStop()
        mapView2.onStop()
    }

    override fun onDestroy() {
        super.onDestroy()
        mapView1.onDestroy()
        mapView2.onDestroy()
    }
}