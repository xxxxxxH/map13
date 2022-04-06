package cn.nba.map13.ui

import android.content.Intent
import androidx.lifecycle.lifecycleScope
import cn.nba.map13.R
import cn.nba.map13.base.BasePage
import cn.nba.map13.utils.*
import com.mapbox.maps.Style
import kotlinx.android.synthetic.main.activity_main.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext

class MapActivity : BasePage(R.layout.activity_main) {

    override fun initView() {
        requestPermission({
            actionBtn.click {
                buildOptionDialog({
                    //near
                                  startActivity(Intent(this,NearbyActivity::class.java))
                }, {
                    //street
                    startActivity(Intent(this,StreetviewActivity::class.java))
                }, {
                    //inter
                    startActivity(Intent(this,InteractiveActivity::class.java))
                }, {
                    //outDour
                    mapView.loadStyle(Style.OUTDOORS)
                }, {
                    //sat
                    mapView.loadStyle(Style.SATELLITE)
                }, {
                    //light
                    mapView.loadStyle(Style.LIGHT)
                }, {
                    //tra
                    mapView.loadStyle(Style.TRAFFIC_DAY)
                }, {
                    //search
                    startActivity(Intent(this,SearchActivity::class.java))
                })
            }

        }, {
            showToast("no permission app will finish")
            lifecycleScope.launch(Dispatchers.IO) {
                delay(1000)
                withContext(Dispatchers.Main) {
                    finish()
                }
            }
        })
    }
}