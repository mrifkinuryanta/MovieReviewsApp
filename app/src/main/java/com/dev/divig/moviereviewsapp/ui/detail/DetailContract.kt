package com.dev.divig.moviereviewsapp.ui.detail

import androidx.lifecycle.LiveData
import com.dev.divig.moviereviewsapp.base.BaseContract
import com.dev.divig.moviereviewsapp.base.model.Resource
import com.dev.divig.moviereviewsapp.data.local.model.MovieEntity
import com.dev.divig.moviereviewsapp.data.local.model.ReviewEntity
import com.dev.divig.moviereviewsapp.data.network.model.response.movie.BaseMovieResponse
import com.dev.divig.moviereviewsapp.data.network.model.response.movie.Review
import com.dev.divig.moviereviewsapp.data.network.model.response.movie.detail.MovieDetailResponse

interface DetailContract {
    interface View : BaseContract.BaseView {
        fun fetchDataMovie(movie: MovieEntity)
        fun fetchDataReview(review: ReviewEntity?)
        fun getMovieDetail(movieId: Int, isSearch: Boolean)
        fun getMovie(id: Int, isSearch: Boolean, update: Boolean)
        fun getReviewsByMovieId(id: Int, update: Boolean)
        fun setFavoriteMovie()
        fun enterFullScreen()
        fun exitFullScreen()
    }

    interface ViewModel {
        fun getMovieLiveData(): LiveData<Resource<MovieEntity>>
        fun getReviewLiveData(): LiveData<Resource<ReviewEntity?>>
        fun setFavoriteMovie(movie: MovieEntity)
        fun getMovie(id: Int, isSearch: Boolean, update: Boolean)
        fun getReviewsByMovieId(movieId: Int, update: Boolean)
    }

    interface Repository {
        suspend fun insertMovie(movie: MovieEntity): Long
        suspend fun updateMovie(movie: MovieEntity): Int
        suspend fun setFavoriteMovie(movie: MovieEntity, newState: Boolean): Int
        suspend fun getDetailMovie(id: Int): MovieEntity
        suspend fun getDetailMovieFromNetwork(id: Int): MovieDetailResponse
        suspend fun insertReviews(reviews: List<ReviewEntity>): List<Long>
        suspend fun getReviews(movieId: Int): List<ReviewEntity>
        suspend fun getReviewsFromNetwork(movieId: Int): BaseMovieResponse<List<Review>>
    }
}