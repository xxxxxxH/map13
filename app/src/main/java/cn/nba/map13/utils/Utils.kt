package cn.nba.map13.utils

import androidx.appcompat.app.AppCompatActivity
import com.mapbox.maps.MapView
import com.mapbox.maps.MapboxMap
import com.mapbox.search.ResponseInfo
import com.mapbox.search.result.SearchResult
import com.mapbox.search.result.SearchSuggestion
import com.mapbox.search.ui.view.SearchBottomSheetView

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