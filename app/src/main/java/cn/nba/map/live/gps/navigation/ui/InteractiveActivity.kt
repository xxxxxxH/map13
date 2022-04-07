package cn.nba.map.live.gps.navigation.ui

import androidx.recyclerview.widget.LinearLayoutManager
import cn.nba.map.live.gps.navigation.R
import cn.nba.map.live.gps.navigation.base.BasePage
import cn.nba.map.live.gps.navigation.utils.InteractiveItem
import cn.nba.map.live.gps.navigation.utils.getInterActiveData
import cn.nba.map.live.gps.navigation.utils.showToast
import kotlinx.android.synthetic.main.layout_inter.*
import uk.co.ribot.easyadapter.EasyRecyclerAdapter

class InteractiveActivity : BasePage(R.layout.layout_inter) {
    override fun initView() {
        loading.show(supportFragmentManager, "")
        getInterActiveData({
            showToast("nodata")
            finish()
        }, {
            val adapter = EasyRecyclerAdapter(this@InteractiveActivity, InteractiveItem::class.java, it)
            recycler.layoutManager = LinearLayoutManager(this)
            recycler.adapter = adapter
            loading.dismiss()
        })

    }
}