package com.dev.divig.moviereviewsapp.ui.register

import androidx.lifecycle.LiveData
import com.dev.divig.moviereviewsapp.base.model.Resource
import com.dev.divig.moviereviewsapp.data.network.model.request.auth.AuthRequest
import com.dev.divig.moviereviewsapp.data.network.model.response.auth.BaseAuthResponse
import com.dev.divig.moviereviewsapp.data.network.model.response.auth.UserData

interface RegisterContract {

    interface View {
        fun initView()
        fun initViewModel()
    }

    interface ViewModel {
        fun getRegisterResponseLiveData() : LiveData<Resource<UserData>>
        fun registerUser(registerRequest: AuthRequest)
    }

    interface Repository {
        suspend fun postRegisterUser(registerRequest: AuthRequest): BaseAuthResponse<UserData, String>
    }
}