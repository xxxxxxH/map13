package cn.nba.map.live.gps.navigation.ui

import android.content.Intent
import cn.nba.map.live.gps.navigation.R
import cn.nba.map.live.gps.navigation.base.BasePage
import cn.nba.map.live.gps.navigation.utils.requestPermission

class LunachActivity : BasePage(R.layout.activity_splash) {

    override fun initView() {
        requestPermission({
            startActivity(Intent(this, MapActivity::class.java))
            finish()
        }, {
            finish()
        })
    }
}