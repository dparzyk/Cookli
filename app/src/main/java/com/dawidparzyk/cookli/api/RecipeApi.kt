package com.dawidparzyk.cookli.api

import com.dawidparzyk.cookli.BuildConfig
import com.dawidparzyk.cookli.recipe.RecipesContainer
import com.google.gson.FieldNamingPolicy
import com.google.gson.GsonBuilder
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET
import retrofit2.http.Query

interface RecipeApi {

  @GET("search?key=" + BuildConfig.FOOD2FORK_API_KEY)
  fun search(@Query("q") query: String): retrofit2.Call<RecipesContainer>

  companion object Factory {
    fun create(): RecipeApi {
      val gson = GsonBuilder()
          .setFieldNamingPolicy(FieldNamingPolicy.LOWER_CASE_WITH_UNDERSCORES)
          .create()

      val retrofit = Retrofit.Builder()
          .baseUrl("http://food2fork.com/api/")
          .addConverterFactory(GsonConverterFactory.create(gson))
          .build()

      return retrofit.create(RecipeApi::class.java)
    }
  }
}