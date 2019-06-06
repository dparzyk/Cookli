package com.dawidparzyk.cookli.favorite

import android.os.Bundle
import android.support.v7.widget.LinearLayoutManager
import android.view.View
import com.dawidparzyk.cookli.ChildActivity
import com.dawidparzyk.cookli.R
import com.dawidparzyk.cookli.recipe.Recipe
import com.dawidparzyk.cookli.recipe.RecipeRepositoryImpl
import com.dawidparzyk.cookli.recipe.recipeIntent
import com.dawidparzyk.cookli.recipe.RecipeAdapter
import kotlinx.android.synthetic.main.activity_list.*
import kotlinx.android.synthetic.main.view_error.*
import kotlinx.android.synthetic.main.view_loading.*
import kotlinx.android.synthetic.main.view_noresults.*

class FavoritesActivity : ChildActivity(), FavoritePresenter.View {

    private val presenter: FavoritePresenter by lazy { FavoritePresenter(RecipeRepositoryImpl.getRepository(this)) }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_list)

        presenter.attachView(this)
        presenter.fetchFavoriteRecipes()
    }

    override fun showEmptyRecipes() {
        loadingContainer.visibility = View.GONE
        errorContainer.visibility = View.GONE
        list.visibility = View.VISIBLE
        noresultsContainer.visibility = View.VISIBLE
        noresultsTitle.text = getString(R.string.nofavorites)
    }

    override fun showRecipes(recipes: List<Recipe>) {
        loadingContainer.visibility = View.GONE
        errorContainer.visibility = View.GONE
        list.visibility = View.VISIBLE
        noresultsContainer.visibility = View.GONE

        list.layoutManager = LinearLayoutManager(this)
        list.adapter = RecipeAdapter(recipes, object : RecipeAdapter.BaseListener {
            override fun onClickItem(item: Recipe) {
                startActivity(recipeIntent(item.sourceUrl))
            }

            override fun onRemoveFavorite(item: Recipe) {
                presenter.removeFavorite(item)
                (list.adapter as RecipeAdapter).removeItem(item)
                (list.adapter as RecipeAdapter).notifyItemRemoved(recipes.indexOf(item))
                if (list.adapter!!.itemCount == 0) {
                    showEmptyRecipes()
                }
            }
        })
    }

    override fun showLoading() {
        loadingContainer.visibility = View.VISIBLE
        errorContainer.visibility = View.GONE
        list.visibility = View.GONE
        noresultsContainer.visibility = View.GONE
    }

    override fun refreshFavoriteResults(recipeIndex: Int) {
        list.adapter!!.notifyItemChanged(recipeIndex)
    }

    override fun onDestroy() {
        super.onDestroy()
        presenter.detachView()
    }
}
