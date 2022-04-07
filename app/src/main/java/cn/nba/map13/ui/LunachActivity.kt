package cn.nba.map13.ui

import android.content.Intent
import cn.nba.map13.R
import cn.nba.map13.base.BasePage
import cn.nba.map13.utils.requestPermission

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