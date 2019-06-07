
package com.dawidparzyk.cookli.search

import android.content.Context
import android.os.Bundle
import android.support.design.widget.Snackbar
import android.view.inputmethod.InputMethodManager
import com.dawidparzyk.cookli.ChildActivity
import com.dawidparzyk.cookli.R
import com.dawidparzyk.cookli.searchresults.searchResultsIntent
import kotlinx.android.synthetic.main.activity_search.*

class SearchActivity : ChildActivity(), SearchPresenter.View {

    private val presenter = SearchPresenter()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        presenter.attachView(this)

        searchButton.setOnClickListener {
            val query = ingredients.text.toString().trim()
            presenter.search(query)
        }
    }

    override fun showQueryRequiredMessage() {
        val view = this.currentFocus
        if (view != null) {
            val imm = getSystemService(Context.INPUT_METHOD_SERVICE) as InputMethodManager
            imm.hideSoftInputFromInputMethod(view.windowToken, 0)
        }
        Snackbar.make(searchButton, getString(R.string.search_query_required), Snackbar.LENGTH_LONG).show()
    }

    override fun showSearchResults(query: String) {
        startActivity(searchResultsIntent(query))
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}