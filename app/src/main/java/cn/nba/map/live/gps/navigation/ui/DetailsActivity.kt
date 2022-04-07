package cn.nba.map.live.gps.navigation.ui

import androidx.recyclerview.widget.GridLayoutManager
import cn.nba.map.live.gps.navigation.R
import cn.nba.map.live.gps.navigation.base.BasePage
import cn.nba.map.live.gps.navigation.entity.DataEntity
import cn.nba.map.live.gps.navigation.utils.*
import com.mapbox.geojson.Point
import kotlinx.android.synthetic.main.layout_details.*
import uk.co.ribot.easyadapter.EasyRecyclerAdapter

class DetailsActivity : BasePage(R.layout.layout_details), ItemCallback {

    private val url by lazy {
        "https://www.google.com/streetview/feed/gallery/collection/" + targetEntity?.key + ".json"
    }

    override fun initView() {
        loading.show(supportFragmentManager, "")
        getDetailsData(url, targetEntity!!, {
            showToast("nodata")
            finish()
        }, {
            val adapter = EasyRecyclerAdapter(this@DetailsActivity, DetailsItem::class.java, it, this)
            recycler.layoutManager = GridLayoutManager(this, 2)
            recycler.adapter = adapter
            loading.dismiss()
        })
    }

    override fun itemClick(position: Int, any: Any) {
        val entity: DataEntity = any as DataEntity
        mapView.moveMap(Point.fromLngLat(entity.lng, entity.lat))
        mapView.addMarker(Point.fromLngLat(entity.lng, entity.lat))
    }
}