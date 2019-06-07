package com.dawidparzyk.cookli.search

import com.dawidparzyk.cookli.BasePresenter

class SearchPresenter : BasePresenter<SearchPresenter.View>() {

    fun search(query: String) {
        if (query.trim().isBlank()) {
            view?.showQueryRequiredMessage()
        } else
            view?.showSearchResults(query)
    }

    interface View {
        fun showQueryRequiredMessage()
        fun showSearchResults(query: String)
    }
}