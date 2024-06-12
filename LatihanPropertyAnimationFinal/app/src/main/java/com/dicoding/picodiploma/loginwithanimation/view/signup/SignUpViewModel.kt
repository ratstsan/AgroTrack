package com.dicoding.picodiploma.loginwithanimation.view.signup

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.dicoding.picodiploma.loginwithanimation.data.UserRepository
import com.dicoding.picodiploma.loginwithanimation.data.api.RegisterResponse
import kotlinx.coroutines.launch

class SignUpViewModel(private val repository: UserRepository) : ViewModel() {
    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    fun regUser(name: String, email: String, password: String, onSuccess: () -> Unit, onError: (String) -> Unit) {
        _isLoading.value = true
        viewModelScope.launch {
            try {
                val message: RegisterResponse = repository.registerUser(name, email, password)
                if (message.error == false) {
                    onSuccess()
                } else {
                    onError("Register gagal. Silakan coba lagi.")
                }
            } catch (e: Exception) {
                onError("Error. Silakan coba lagi nanti.")
            } finally {
                _isLoading.value = false
            }
        }
    }
}