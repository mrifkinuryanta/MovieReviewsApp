package com.dev.divig.moviereviewsapp.ui.main.movie

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dev.divig.moviereviewsapp.base.model.Resource
import com.dev.divig.moviereviewsapp.data.local.model.MovieEntity
import com.dev.divig.moviereviewsapp.utils.Constant
import com.dev.divig.moviereviewsapp.utils.Utils
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class MovieViewModel @Inject constructor(private val repository: MovieRepository) :
    ViewModel(),
    MovieContract.ViewModel {

    private val repositoryLiveData = MutableLiveData<Resource<List<MovieEntity>>>()

    override fun getMoviesLiveData(): LiveData<Resource<List<MovieEntity>>> = repositoryLiveData

    override fun getMovies(update: Boolean) {
        repositoryLiveData.value = Resource.Loading()
        viewModelScope.launch(Dispatchers.IO) {
            try {
                var movies = repository.getMovies()
                if (movies.isEmpty() || update) {
                    val response = repository.getMoviesFromNetwork()

                    if (response.success == false) {
                        viewModelScope.launch(Dispatchers.Main) {
                            repositoryLiveData.value =
                                Resource.Error(response.statusMessage.orEmpty())
                        }
                    } else {
                        val movieList = ArrayList<MovieEntity>()
                        response.results.forEach { item ->
                            val genres = StringBuilder().append("")
                            item.genreIds?.forEachIndexed { index, value ->
                                if (index < item.genreIds.size - 1) {
                                    genres.append("${Utils.getGenreName(value)}, ")
                                } else {
                                    genres.append(Utils.getGenreName(value))
                                }
                            }

                            val movieEntity = MovieEntity(
                                item.id,
                                item.title,
                                item.overview,
                                genres.toString(),
                                item.releaseDate,
                                0,
                                item.voteAverage,
                                item.posterPath,
                                item.backdropPath,
                                null
                            )
                            movieList.add(movieEntity)
                        }
                        repository.insertMovies(movieList)
                    }
                    movies = repository.getMovies()
                }
                viewModelScope.launch(Dispatchers.Main) {
                    repositoryLiveData.value = Resource.Success(movies)
                }
            } catch (e: Exception) {
                viewModelScope.launch(Dispatchers.Main) {
                    repositoryLiveData.value = Resource.Error(e.message.orEmpty())
                }
            }
        }
    }

    override fun getMovieFilters(
        movies: List<MovieEntity>,
        type: Int,
        genre: String?
    ): List<MovieEntity> {
        return when (type) {
            Constant.TYPE_NOW_PLAYING_MOVIES -> {
                movies.filter {
                    Utils.dateToMillis(it.releaseDate) <= Utils.dateToMillis(Utils.getDate())
                }.sortedByDescending { Utils.dateToMillis(it.releaseDate) }
            }
            else -> {
                movies.filter { item ->
                    Utils.splitGenre(item.genres).find { it == genre } == genre
                }
            }
        }
    }
}