package cn.nba.map13.utils

import android.Manifest
import android.content.Intent
import android.graphics.Bitmap
import android.graphics.BitmapFactory
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.widget.ImageView
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import cn.nba.map13.R
import cn.nba.map13.entity.DataEntity
import cn.nba.map13.entity.NearbyEntity
import com.bumptech.glide.Glide
import com.hjq.permissions.OnPermissionCallback
import com.hjq.permissions.XXPermissions
import com.mapbox.geojson.Point
import com.mapbox.maps.CameraOptions
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.maps.dsl.cameraOptions
import com.mapbox.maps.plugin.animation.flyTo
import com.mapbox.maps.plugin.annotation.AnnotationConfig
import com.mapbox.maps.plugin.annotation.annotations
import com.mapbox.maps.plugin.annotation.generated.PointAnnotationOptions
import com.mapbox.maps.plugin.annotation.generated.createPointAnnotationManager
import com.mapbox.maps.plugin.gestures.addOnMapClickListener
import com.mapbox.maps.plugin.gestures.gestures
import com.mapbox.maps.plugin.locationcomponent.OnIndicatorPositionChangedListener
import com.mapbox.maps.plugin.locationcomponent.location
import com.mapbox.search.MapboxSearchSdk
import com.mapbox.search.ResponseInfo
import com.mapbox.search.record.FavoriteRecord
import com.mapbox.search.record.HistoryRecord
import com.mapbox.search.result.SearchResult
import com.mapbox.search.result.SearchSuggestion
import com.mapbox.search.ui.view.SearchBottomSheetView
import com.mapbox.search.ui.view.category.Category
import com.yarolegovich.lovelydialog.LovelyCustomDialog

var targetEntity: DataEntity? = null

val searchEngine by lazy {
    MapboxSearchSdk.getSearchEngine()
}

fun AppCompatActivity.buildMapview(): MapView {
    return MapView(this)
}

fun MapView.mapBox(): MapboxMap {
    return this.getMapboxMap()
}


fun AppCompatActivity.searchSheet(): SearchBottomSheetView {
    return SearchBottomSheetView(this)
}


fun searchCallback(
    callbackError: () -> Unit,
    callBackSuggestions: (List<SearchSuggestion>, ResponseInfo) -> Unit,
    callBackResult: (List<SearchSuggestion>, List<SearchResult>, ResponseInfo) -> Unit
): SearchCallbackExt {
    return object : SearchCallbackExt {
        override fun onResult(
            suggestions: List<SearchSuggestion>,
            results: List<SearchResult>,
            responseInfo: ResponseInfo
        ) {
            callBackResult(suggestions, results, responseInfo)
        }

        override fun onCategoryResult(
            suggestion: SearchSuggestion,
            results: List<SearchResult>,
            responseInfo: ResponseInfo
        ) {

        }

        override fun onResult(
            suggestion: SearchSuggestion,
            result: SearchResult,
            responseInfo: ResponseInfo
        ) {

        }

        override fun onError(e: Exception) {
            callbackError()
        }

        override fun onSuggestions(
            suggestions: List<SearchSuggestion>,
            responseInfo: ResponseInfo
        ) {
            callBackSuggestions(suggestions, responseInfo)
        }
    }
}

fun MapView.setCurrentLocation() {
    val listener = object : OnIndicatorPositionChangedListener {
        override fun onIndicatorPositionChanged(point: Point) {
            setTag(R.id.appViewMyLocationId, point)
            getMapboxMap().setCamera(CameraOptions.Builder().center(point).build())
            gestures.focalPoint = getMapboxMap().pixelForCoordinate(point)
            location.removeOnIndicatorPositionChangedListener(this)
        }
    }
    location.addOnIndicatorPositionChangedListener(listener)
}

fun MapView.setCameraClick(block: (Double, Double) -> Unit) {
    getMapboxMap().addOnMapClickListener {
        block(it.longitude(), it.latitude())
        true
    }
}

fun MapView.moveMap(p: Point) {
    getMapboxMap().flyTo(cameraOptions {
        center(p)
        zoom(14.0)
    })
}

fun MapView.loadStyle(style: String) {
    getMapboxMap().apply {
        setCamera(cameraOptions {
            zoom(14.0)
        })
        loadStyleUri(style) {
            addMyLocationPoint()
        }
    }
}

fun MapView.addMyLocationPoint() {
    location.updateSettings {
        enabled = true
        pulsingEnabled = true
    }
}

fun MapView.setCameraChangeListener(block: (Double, Double) -> Unit) {
    getMapboxMap().addOnCameraChangeListener {
        getMapboxMap().cameraState.center.let {
            block(it.longitude(), it.latitude())
        }
    }
}


fun MapView.setCamera(center: Point) {
    getMapboxMap().setCamera(
        cameraOptions {
            center(center)
            zoom(14.0)
        }
    )
}

fun MapView.addMarker(p: Point) {
    val bitmap: Bitmap = BitmapFactory.decodeResource(this.context.resources, R.mipmap.red_marker)
    annotations.cleanup()
    val markerManager = annotations.createPointAnnotationManager(AnnotationConfig())
    val pointAnnotationOptions = PointAnnotationOptions()
        .withPoint(p)
        .withIconImage(bitmap)
    markerManager.create(pointAnnotationOptions)
}

fun SearchBottomSheetView.addOnCategory(
    savedInstanceState: Bundle?,
    block: (Category) -> Unit
) {
    initializeSearch(savedInstanceState, SearchBottomSheetView.Configuration())
    addOnCategoryClickListener {
        block(it)
    }
}

fun SearchBottomSheetView.addOnFavorite(block: (FavoriteRecord) -> Unit) {
    addOnFavoriteClickListener {
        block(it)
    }
}

fun SearchBottomSheetView.addOnHistory(block: (HistoryRecord) -> Unit) {
    addOnHistoryClickListener {
        block(it)
    }
}

fun SearchBottomSheetView.addOnSearchResult(block: (SearchResult, ResponseInfo) -> Unit) {
    addOnSearchResultClickListener { searchResult, responseInfo ->
        block(searchResult, responseInfo)
    }
}

fun AppCompatActivity.route2Result(it: Point, clazz: Class<*>) {
    startActivity(Intent(this, clazz).apply {
        putExtra("lat", it.latitude())
        putExtra("lng", it.longitude())
    })
}

fun AppCompatActivity.requestPermission(onGranted: () -> Unit, onDenied: () -> Unit) {
    XXPermissions.with(this).permission(Manifest.permission.ACCESS_FINE_LOCATION)
        .request(object : OnPermissionCallback {
            override fun onGranted(permissions: MutableList<String>?, all: Boolean) {
                if (all) {
                    onGranted()
                } else {
                    onDenied()
                }
            }

            override fun onDenied(permissions: MutableList<String>?, never: Boolean) {
                super.onDenied(permissions, never)
                onDenied()
            }
        })
}

fun AppCompatActivity.showToast(s: String) {
    Toast.makeText(this, s, Toast.LENGTH_SHORT).show()
}

fun AppCompatActivity.buildOptionDialog(
    blockNearby: () -> Unit,
    blockStreet: () -> Unit,
    blockInter: () -> Unit,
    blockOut: () -> Unit,
    blockSat: () -> Unit,
    blockLight: () -> Unit,
    blockTra: () -> Unit,
    blockSearch: () -> Unit
) {
    val v = LayoutInflater.from(this).inflate(R.layout.dialog_option, null)
    LovelyCustomDialog(this)
        .setTitle("Select Your Option")
        .setView(v)
        .setListener(R.id.nearby, true) { blockNearby() }
        .setListener(R.id.streetview, true) { blockStreet() }
        .setListener(R.id.inter, true) { blockInter() }
        .setListener(R.id.outdoor, true) { blockOut() }
        .setListener(R.id.satellite, true) { blockSat() }
        .setListener(R.id.light, true) { blockLight() }
        .setListener(R.id.traffic, true) { blockTra() }
        .setListener(R.id.search, true) { blockSearch() }
        .show()
}

fun View.click(block: (View) -> Unit) {
    setOnClickListener { block(this) }
}

fun AppCompatActivity.getFileFromAssets() :ArrayList<NearbyEntity>{
    val result = ArrayList<NearbyEntity>()
    val assetsManager = this@getFileFromAssets.resources.assets
    val files = assetsManager.list("img")
    if (files!!.isNotEmpty()) {
        files.forEach {
            val inputStream = assetsManager.open("img/$it")
            val bitmap = BitmapFactory.decodeStream(inputStream)
            val e = NearbyEntity(it, bitmap)
            result.add(e)
        }
    }
    return result
}

fun ImageView.load(any: Any) {
    Glide.with(this.context).load(any).into(this)
}