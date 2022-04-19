package com.denkiri.pharmacy.storage.repository

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.cashier.Users
import com.denkiri.pharmacy.models.cashier.UsersData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.customer.CustomerData
import com.denkiri.pharmacy.models.oauth.Oauth
import com.denkiri.pharmacy.network.NetworkUtils
import com.denkiri.pharmacy.network.RequestService
import com.denkiri.pharmacy.storage.PharmacyDatabase
import com.denkiri.pharmacy.storage.PreferenceManager
import com.denkiri.pharmacy.storage.daos.ProfileDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class UsersActionRepository (application: Application)  {
    private val preferenceManager: PreferenceManager = PreferenceManager(application)
    val usersActionObservable = MutableLiveData<Resource<UsersData>>()
    val updateProfileObservable=MutableLiveData<Resource<Oauth>>()
    private val context: Context
    private val profileDao: ProfileDao
    private val db: PharmacyDatabase
    init {
        db = PharmacyDatabase.getDatabase(application)!!
        profileDao = db.profileDao()
        context =application.applicationContext

    }
    fun addUser(username:String,password:String,name:String,code: String) {
        setIsLoading(Observable.ADD_USER)

        if (NetworkUtils.isConnected(context)) {
            execute(username, password, name,code)
        } else {
            setIsError(Observable.ADD_USER, context.getString(R.string.no_connection))
        }

    }
    fun updateUser(password:String,name:String,userId:String) {
        setIsLoading(Observable.EDIT_USER)

        if (NetworkUtils.isConnected(context)) {
            editUser(password,name, userId)
        } else {
            setIsError(Observable.EDIT_USER, context.getString(R.string.no_connection))
        }

    }
    fun updateProfile(username:String,name:String,email: String,mobile: String,userId:String) {
        setIsLoading(Observable.UPDATE_PROFILE)

        if (NetworkUtils.isConnected(context)) {
            update(username,name,email,mobile,userId)
        } else {
            setIsError(Observable.UPDATE_PROFILE, context.getString(R.string.no_connection))
        }

    }
    fun delete(userId: Int){
        setIsLoading(Observable.DELETE_USER)

        if (NetworkUtils.isConnected(context)) {
            deleteUser(userId)
        } else {
            setIsError(Observable.DELETE_USER, context.getString(R.string.no_connection))
        }

    }
    enum class Observable {
        DELETE_USER,
        EDIT_USER,
        UPDATE_PROFILE,
        ADD_USER
    }

    fun getToken(): String {
        return profileDao.fetch().token.toString()
    }
    fun deleteUser( userId: Int){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).deleteUser( userId.toString()
            )
            call.enqueue(object : Callback<UsersData> {
                override fun onFailure(call: Call<UsersData>?, t: Throwable?) {
                    setIsError(Observable.DELETE_USER, t.toString())
                }
                override fun onResponse(call: Call<UsersData>?, response: Response<UsersData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable.DELETE_USER,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.DELETE_USER,it) }
                            }
                        }
                        else {
                            setIsError(Observable.DELETE_USER, response.toString())
                        }
                    } else {
                        setIsError(Observable.DELETE_USER, "Error Loading In")
                    }
                }
            })
        }
    }
    fun editUser(password:String,name:String,userId:String){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).editUser(password,name, userId)
            call.enqueue(object : Callback<UsersData> {
                override fun onFailure(call: Call<UsersData>?, t: Throwable?) {
                    setIsError(Observable.EDIT_USER, t.toString())
                }
                override fun onResponse(call: Call<UsersData>?, response: Response<UsersData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable.EDIT_USER,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.EDIT_USER,it) }
                            }
                        }
                        else {
                            setIsError(Observable.EDIT_USER, response.toString())
                        }
                    } else {
                        setIsError(Observable.EDIT_USER, "Error Loading In")
                    }
                }
            })
        }

    }
    fun update(username:String,name:String,email:String,mobile:String,userId:String){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).updateUser(username,name,email,mobile,userId)
            call.enqueue(object : Callback<Oauth> {
                override fun onFailure(call: Call<Oauth>?, t: Throwable?) {
                    setIsError(Observable.UPDATE_PROFILE, t.toString())
                }
                override fun onResponse(call: Call<Oauth>?, response: Response<Oauth>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable.UPDATE_PROFILE,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.UPDATE_PROFILE,it) }
                            }
                        }
                        else {
                            setIsError(Observable.UPDATE_PROFILE, response.toString())
                        }
                    } else {
                        setIsError(Observable.UPDATE_PROFILE, "Error Loading In")
                    }
                }
            })
        }

    }
    fun execute(username:String,password:String,name:String,code:String){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).addUser(username, password,name,code)
            call.enqueue(object : Callback<UsersData> {
                override fun onFailure(call: Call<UsersData>?, t: Throwable?) {
                    setIsError(Observable.ADD_USER, t.toString())
                }
                override fun onResponse(call: Call<UsersData>?, response: Response<UsersData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable.ADD_USER,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.ADD_USER,it) }
                            }
                        }
                        else {
                            setIsError(Observable.ADD_USER, response.toString())
                        }
                    } else {
                        setIsError(Observable.ADD_USER, "Error Loading In")
                    }
                }
            })
        }

    }
    private fun setIsLoading(observable: UsersActionRepository.Observable) {
        when(observable) {
            Observable.EDIT_USER -> usersActionObservable.postValue(Resource.loading(null))
            Observable.DELETE_USER -> usersActionObservable.postValue(Resource.loading(null))
             Observable.ADD_USER -> usersActionObservable.postValue(Resource.loading(null))
            Observable.UPDATE_PROFILE -> updateProfileObservable.postValue(Resource.loading(null))

        }
    }
    private fun <T> setIsSuccesful(observable: UsersActionRepository.Observable, data: T?) {
        when (observable) {
            Observable.EDIT_USER -> usersActionObservable.postValue(Resource.success(data as UsersData))
            Observable.DELETE_USER -> usersActionObservable.postValue(Resource.success(data as UsersData))
            Observable.ADD_USER -> usersActionObservable.postValue(Resource.success(data as UsersData))
            Observable.UPDATE_PROFILE -> updateProfileObservable.postValue(Resource.success(data as Oauth))

        }
    }
    private fun setIsError(observable: UsersActionRepository.Observable, message: String) {
        when (observable) {
            Observable.EDIT_USER -> usersActionObservable.postValue(Resource.error(message, null))
            Observable.DELETE_USER -> usersActionObservable.postValue(Resource.error(message, null))
            Observable.ADD_USER -> usersActionObservable.postValue(Resource.error(message, null))
            Observable.UPDATE_PROFILE -> updateProfileObservable.postValue(Resource.error(message, null))

        }
    }
}