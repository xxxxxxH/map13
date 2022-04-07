package cn.nba.map.live.gps.navigation.ui

import android.os.Bundle
import cn.nba.map.live.gps.navigation.R
import cn.nba.map.live.gps.navigation.base.BasePage
import cn.nba.map.live.gps.navigation.utils.*
import com.mapbox.search.ResponseInfo
import com.mapbox.search.SearchOptions
import com.mapbox.search.result.SearchResult
import com.mapbox.search.result.SearchSuggestion
import kotlinx.android.synthetic.main.layout_search.*

class SearchActivity : BasePage(R.layout.layout_search) {


    override fun initView() {

    }

    override fun initData(savedInstanceState: Bundle?) {
        super.initData(savedInstanceState)
        sheet.apply {
            addOnCategory(savedInstanceState) {
                loading.show(supportFragmentManager, "")
                searchEngine.search(
                    it.geocodingCanonicalName,
                    SearchOptions(),
                    callback
                )
            }

            addOnFavorite {
                loading.show(supportFragmentManager, "")
                route2Result(it.coordinate, ResultActivity::class.java)
            }
            addOnHistory {
                loading.show(supportFragmentManager, "")
                it.coordinate?.let { point ->
                    route2Result(point, ResultActivity::class.java)
                } ?: kotlin.run {
                    searchEngine.search(
                        it.name,
                        SearchOptions(),
                        callback
                    )
                }
            }

            addOnSearchResult { searchResult, responseInfo ->
                loading.show(supportFragmentManager, "")
                searchResult.coordinate?.let {
                    route2Result(it, ResultActivity::class.java)
                } ?: kotlin.run {
                    searchEngine.search(
                        searchResult.name,
                        SearchOptions(),
                        callback
                    )
                }
            }
        }
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