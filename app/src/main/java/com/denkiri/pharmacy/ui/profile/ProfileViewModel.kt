package com.denkiri.pharmacy.ui.profile

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.ViewModel
import com.denkiri.pharmacy.models.cashier.UsersData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.oauth.Oauth
import com.denkiri.pharmacy.models.oauth.Profile
import com.denkiri.pharmacy.storage.repository.SignInRepository
import com.denkiri.pharmacy.storage.repository.UsersActionRepository

class ProfileViewModel (application: Application) : AndroidViewModel(application) {
    internal  var signInRepository: SignInRepository
    private val signInObservable = MediatorLiveData<Resource<Oauth>>()
    internal var userActionRepository: UsersActionRepository
    private val userActionObservable = MediatorLiveData<Resource<Oauth>>()

    init {
        userActionRepository = UsersActionRepository(application)
        userActionObservable.addSource(userActionRepository.updateProfileObservable) { data -> userActionObservable.setValue(data)}
        signInRepository = SignInRepository(application)
        signInObservable.addSource(signInRepository.signInObservable){ data-> signInObservable.setValue(data)}

    }
    fun getOuthProfile(): LiveData<Profile> {
        return signInRepository.getProfile()
    }
    fun updateProfile(username:String?,name:String?,email:String?,mobile:String?,userId:String?){
        userActionRepository.updateProfile(username!!, name!!,email!!,mobile!!,userId!!)
    }
    fun observeUserAction(): LiveData<Resource<Oauth>> {
        return userActionObservable
    }
    fun saveProfile(data:Oauth){
        signInRepository.saveProfile(data)

    }

}