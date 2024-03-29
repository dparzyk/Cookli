package com.dawidparzyk.cookli.searchresults

import com.dawidparzyk.cookli.BasePresenter
import com.dawidparzyk.cookli.recipe.Recipe
import com.dawidparzyk.cookli.recipe.RecipeRepository
import com.dawidparzyk.cookli.recipe.RepositoryCallback

class SearchResultPresenter(private val repository: RecipeRepository) : BasePresenter<SearchResultPresenter.View>() {

    private var recipes: List<Recipe>? = null

    interface View {
        fun showLoading()
        fun showRecipes(recipes: List<Recipe>)
        fun showEmptyRecipes()
        fun showError()
        fun refreshFavoriteStatus(recipeIndex: Int)
    }

    fun search(query: String) {
        view?.showLoading()
        repository.getRecipes(query, object : RepositoryCallback<List<Recipe>> {

            override fun onSuccess(recipes: List<Recipe>?) {
                this@SearchResultPresenter.recipes = recipes
                if (recipes != null && recipes.isNotEmpty()) {
                    view?.showRecipes(recipes)
                } else {
                    view?.showEmptyRecipes()
                }
            }

            override fun onError() {
                view?.showError()
            }
        })
    }

    fun addFavorite(recipe: Recipe) {
        recipe.isFavorited = true
        repository.addFavorite(recipe)
        val recipeIndex = recipes?.indexOf(recipe)
        if (recipeIndex != null) {
            view?.refreshFavoriteStatus(recipeIndex)
        }
    }

    fun removeFavorite(recipe: Recipe) {
        repository.removeFavorite(recipe)
        recipe.isFavorited = false
        val recipeIndex = recipes?.indexOf(recipe)
        if (recipeIndex != null)
            view?.refreshFavoriteStatus(recipeIndex)
    }
}