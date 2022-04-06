package cn.nba.map13.ui

import androidx.recyclerview.widget.GridLayoutManager
import androidx.recyclerview.widget.LinearLayoutManager
import cn.nba.map13.R
import cn.nba.map13.base.BasePage
import cn.nba.map13.entity.DataEntity
import cn.nba.map13.utils.*
import com.mapbox.geojson.Point
import kotlinx.android.synthetic.main.layout_details.*
import uk.co.ribot.easyadapter.EasyRecyclerAdapter

class DetailsActivity:BasePage(R.layout.layout_details),ItemCallback {

    private val url by lazy {
        "https://www.google.com/streetview/feed/gallery/collection/" + targetEntity?.key + ".json"
    }

    override fun initView() {
        loading.show(supportFragmentManager, "")
        getDetailsData(url, targetEntity!!,{
            showToast("nodata")
            finish()
        },{
            val adapter = EasyRecyclerAdapter(this@DetailsActivity, DetailsItem::class.java, it, this)
            recycler.layoutManager = GridLayoutManager(this, 2)
            recycler.adapter = adapter
            loading.dismiss()
        })
    }

    override fun itemClick(position: Int, any: Any) {
        val entity : DataEntity = any as DataEntity
        mapView.moveMap(Point.fromLngLat(entity.lng, entity.lat))
        mapView.addMarker(Point.fromLngLat(entity.lng, entity.lat))
    }
}