package com.denkiri.pharmacy.ui.users
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.denkiri.pharmacy.models.cashier.UsersData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.customer.CustomerData
import com.denkiri.pharmacy.models.oauth.Oauth
import com.denkiri.pharmacy.models.oauth.Profile
import com.denkiri.pharmacy.storage.repository.CustomerActionRepository
import com.denkiri.pharmacy.storage.repository.SignInRepository
import com.denkiri.pharmacy.storage.repository.UsersActionRepository
import com.denkiri.pharmacy.storage.repository.UsersRepository

class UsersViewModel (application: Application) : AndroidViewModel(application) {
    internal var usersRepository: UsersRepository
    private val usersObservable = MediatorLiveData<Resource<UsersData>>()
    internal  var signInRepository: SignInRepository
    private val signInObservable = MediatorLiveData<Resource<Oauth>>()
    internal var userActionRepository: UsersActionRepository
    private val userActionObservable = MediatorLiveData<Resource<UsersData>>()
   init {
       userActionRepository = UsersActionRepository(application)
       userActionObservable.addSource(userActionRepository.usersActionObservable) { data -> userActionObservable.setValue(data)}
       usersRepository= UsersRepository(application)
       usersObservable.addSource(usersRepository.usersObservable) { data -> usersObservable.setValue(data) }
       signInRepository = SignInRepository(application)
       signInObservable.addSource(signInRepository.signInObservable){ data-> signInObservable.setValue(data)}
   }
    fun observeUsers(): LiveData<Resource<UsersData>> {
        return usersObservable
    }
    fun getUsers() {
        usersRepository.getUsers()
    }
    fun getOuthProfile(): LiveData<Profile> {
        return signInRepository.getProfile()
    }
    fun observeUserAction(): LiveData<Resource<UsersData>> {
        return userActionObservable
    }
    fun deleteUser(userId:Int?){
        if (userId != null) {
            userActionRepository.delete(userId)
        }
    }
    fun addUser(username:String?,password:String?,name:String?,code:String?){
        userActionRepository.addUser(username!!, password!!, name!!,code!!)
    }
    fun editUser(password:String?,name:String?,userId:String?){
        userActionRepository.editUser(password!!, name!!,userId!!)
    }

}