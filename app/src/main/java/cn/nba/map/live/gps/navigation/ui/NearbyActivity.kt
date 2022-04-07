package cn.nba.map.live.gps.navigation.ui

import androidx.recyclerview.widget.GridLayoutManager
import cn.nba.map.live.gps.navigation.R
import cn.nba.map.live.gps.navigation.base.BasePage
import cn.nba.map.live.gps.navigation.entity.NearbyEntity
import cn.nba.map.live.gps.navigation.utils.*
import com.mapbox.search.ResponseInfo
import com.mapbox.search.SearchOptions
import com.mapbox.search.result.SearchResult
import com.mapbox.search.result.SearchSuggestion
import kotlinx.android.synthetic.main.activity_nearby.*
import uk.co.ribot.easyadapter.EasyRecyclerAdapter

class NearbyActivity : BasePage(R.layout.activity_nearby), ItemCallback {

    override fun initView() {
        val data = getFileFromAssets()
        val adapter = EasyRecyclerAdapter(this@NearbyActivity, NearbyItem::class.java, data, this)
        recycler.layoutManager = GridLayoutManager(this, 2)
        recycler.adapter = adapter
    }

    override fun itemClick(position: Int, any: Any) {
        loading.show(supportFragmentManager, "")
        val entity: NearbyEntity = any as NearbyEntity
        searchEngine.search(
            entity.name.substring(0, entity.name.length - 4),
            SearchOptions(),
            callback
        )
    }

    override fun onError() {
        super.onError()
        loading.dismiss()
    }

    override fun suggestions(suggestions: List<SearchSuggestion>, responseInfo: ResponseInfo) {
        super.suggestions(suggestions, responseInfo)
        suggestions.firstOrNull()?.let {
            searchEngine.select(suggestions, callback)
        } ?: kotlin.run {
            loading.dismiss()
            showToast("No suggestions found")
        }
    }

    override fun result(suggestions: List<SearchSuggestion>, results: List<SearchResult>, responseInfo: ResponseInfo) {
        super.result(suggestions, results, responseInfo)
        loading.dismiss()
        results.firstOrNull()?.coordinate?.let {
            route2Result(it, ResultActivity::class.java)
        } ?: kotlin.run {
            showToast("No suggestions found")
        }
    }
}