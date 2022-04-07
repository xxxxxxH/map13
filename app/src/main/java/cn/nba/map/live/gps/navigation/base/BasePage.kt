package cn.nba.map.live.gps.navigation.base

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import cn.nba.map.live.gps.navigation.utils.copyFiles
import cn.nba.map.live.gps.navigation.utils.initStarCore
import cn.nba.map.live.gps.navigation.utils.searchCallback
import com.mapbox.search.ResponseInfo
import com.mapbox.search.result.SearchResult
import com.mapbox.search.result.SearchSuggestion
import com.roger.catloadinglibrary.CatLoadingView

abstract class BasePage(id: Int) : AppCompatActivity(id) {

    val loading by lazy {
        CatLoadingView()
    }

    var callback = searchCallback({
        onError()
    }, { suggestions, responseInfo ->
        suggestions(suggestions, responseInfo)
    }, { suggestions, results, responseInfo ->
        result(suggestions, results, responseInfo)
    })


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        copyFiles()
        initStarCore()
        initView()
        initData(savedInstanceState)
    }

    abstract fun initView()

    open fun initData(savedInstanceState: Bundle?) {}

    open fun onError() {}

    open fun suggestions(
        suggestions: List<SearchSuggestion>,
        responseInfo: ResponseInfo
    ) {
    }

    open fun result(
        suggestions: List<SearchSuggestion>,
        results: List<SearchResult>,
        responseInfo: ResponseInfo
    ) {
    }
}