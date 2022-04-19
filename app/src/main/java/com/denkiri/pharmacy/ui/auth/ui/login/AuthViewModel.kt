package com.denkiri.pharmacy.ui.auth.ui.login

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.oauth.Oauth
import com.denkiri.pharmacy.models.oauth.Profile
import com.denkiri.pharmacy.storage.repository.ChangePasswordRepository
import com.denkiri.pharmacy.storage.repository.SignInRepository

class AuthViewModel (application: Application): AndroidViewModel(application){
    internal  var signInRepository: SignInRepository
    internal var changePasswordRepository:ChangePasswordRepository
    private val changePasswordObservable = MediatorLiveData<Resource<Oauth>>()
    private val signInObservable = MediatorLiveData<Resource<Oauth>>()
    init {
        changePasswordRepository= ChangePasswordRepository(application)
        signInRepository = SignInRepository(application)
        changePasswordObservable.addSource(changePasswordRepository.signInObservable){ data-> changePasswordObservable.setValue(data)}
        signInObservable.addSource(signInRepository.signInObservable){ data-> signInObservable.setValue(data)}
    }
    fun signIn(parameters: Oauth) {
        signInRepository.signIn(parameters)

    }
    fun changePassword(parameters: Oauth) {
        changePasswordRepository.signIn(parameters)

    }
    fun observeChangePassword(): LiveData<Resource<Oauth>> {
        return changePasswordObservable
    }
    fun observeSignIn(): LiveData<Resource<Oauth>> {
        return signInObservable
    }
    fun saveProfile(data:Oauth){
        signInRepository.saveProfile(data)

    }


}