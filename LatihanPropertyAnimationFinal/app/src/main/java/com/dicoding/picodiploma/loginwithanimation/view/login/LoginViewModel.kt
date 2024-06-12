package com.dicoding.picodiploma.loginwithanimation.view.login

import android.util.Log
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.api.ErrorResponse
import com.dicoding.picodiploma.loginwithanimation.data.pref.UserModel
import com.google.gson.Gson
import kotlinx.coroutines.launch
import retrofit2.HttpException
import android.content.ContentValues.TAG
import com.dicoding.picodiploma.loginwithanimation.data.api.LoginResponse

class LoginViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading : LiveData<Boolean> = _isLoading

    private val _loginResponse = MutableLiveData<LoginResponse>()
    val loginResponse: LiveData<LoginResponse> = _loginResponse

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage


    fun login(email: String, password: String){
        viewModelScope.launch {
            _isLoading.postValue(true)
            try {
                val response = repository.login(email, password)
                saveSession(
                    UserModel(
                        response.loginResult.userId,
                        response.loginResult.name,
                        email,
                        response.loginResult.token,
                        true
                    )
                )
                _loginResponse.postValue(response)
                Log.d(TAG, "onSuccess: ${response.message}")
            } catch (e: HttpException) {
                val jsonInString = e.response()?.errorBody()?.string()
                val errorBody = try {
                    Gson().fromJson(jsonInString, ErrorResponse::class.java)
                } catch (ex: Exception) {
                    null
                }
                val errorMessage = errorBody?.message ?: "An unknown error occurred"
                _errorMessage.postValue(errorMessage)
                Log.d(TAG, "onError: $errorMessage")
            }catch (e: Exception) {
                _errorMessage.postValue(e.message ?: "An unknown error occurred")
                Log.d(TAG, "onError: ${e.message}")
            }finally {
                _isLoading.postValue(false)
            }
        }
    }

    fun saveSession(user: UserModel) {
        viewModelScope.launch {
            repository.saveSession(user)
        }
    }
}