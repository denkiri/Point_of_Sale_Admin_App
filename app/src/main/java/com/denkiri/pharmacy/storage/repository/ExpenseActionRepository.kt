package com.denkiri.pharmacy.storage.repository

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.category.CategoryData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.expense.ExpenseData
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
class ExpenseActionRepository  (application: Application) {
    private val preferenceManager: PreferenceManager = PreferenceManager(application)
    val expenseActionObservable = MutableLiveData<Resource<ExpenseData>>()
    private val context: Context
    private val profileDao: ProfileDao
    private val db: PharmacyDatabase
    init {
        db = PharmacyDatabase.getDatabase(application)!!
        profileDao = db.profileDao()
        context =application.applicationContext

    }
    fun addExpense(name: String,amount: String){
        setIsLoading(Observable.ADD_EXPENSE)

        if (NetworkUtils.isConnected(context)) {
            execute(name,amount)
        } else {
            setIsError(Observable.ADD_EXPENSE, context.getString(R.string.no_connection))
        }
    }
    fun editExpense(name: String,amount: String,cid: String) {
        setIsLoading(Observable.EDIT_EXPENSE)

        if (NetworkUtils.isConnected(context)) {
            updateExpense(name,amount,cid)
        } else {
            setIsError(Observable.EDIT_EXPENSE, context.getString(R.string.no_connection))
        }
    }
    fun activateExpense(categoryId:String) {
        setIsLoading(Observable.ACTIVATE_EXPENSE)

        if (NetworkUtils.isConnected(context)) {
            activate(categoryId)
        } else {
            setIsError(Observable.ACTIVATE_EXPENSE, context.getString(R.string.no_connection))
        }
    }
    fun deactivateExpense(categoryId:String) {
        setIsLoading(Observable.DEACTIVATE_EXPENSE)

        if (NetworkUtils.isConnected(context)) {
            deactivate(categoryId)
        } else {
            setIsError(Observable.DEACTIVATE_EXPENSE, context.getString(R.string.no_connection))
        }
    }
    fun deleteExpense(id:Int) {
        setIsLoading(Observable.DELETE_EXPENSE)

        if (NetworkUtils.isConnected(context)) {
            delete(id)
        } else {
            setIsError(Observable.DELETE_EXPENSE, context.getString(R.string.no_connection))
        }
    }
    fun execute(name:String,amount: String){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).addExpense(name,amount)
            call.enqueue(object : Callback< ExpenseData> {
                override fun onFailure(call: Call< ExpenseData>?, t: Throwable?) {
                    setIsError(Observable.ADD_EXPENSE, t.toString())
                }
                override fun onResponse(call: Call< ExpenseData>?, response: Response< ExpenseData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable.ADD_EXPENSE,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.ADD_EXPENSE,it) }
                            }
                        }
                        else {
                            setIsError(Observable.ADD_EXPENSE, response.toString())
                        }
                    } else {
                        setIsError(Observable.ADD_EXPENSE, "Error Loading In")
                    }
                }
            })
        }

    }

    fun delete( cid: Int){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).deleteExpense( cid.toString()
            )
            call.enqueue(object : Callback< ExpenseData> {
                override fun onFailure(call: Call< ExpenseData>?, t: Throwable?) {
                    setIsError(Observable.DELETE_EXPENSE, t.toString())
                }
                override fun onResponse(call: Call< ExpenseData>?, response: Response< ExpenseData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable.DELETE_EXPENSE,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.DELETE_EXPENSE,it) }
                            }
                        }
                        else {
                            setIsError(Observable.DELETE_EXPENSE, response.toString())
                        }
                    } else {
                        setIsError(Observable.DELETE_EXPENSE, "Error Loading In")
                    }
                }
            })
        }
    }
    fun updateExpense( name: String,amount: String,cid:String){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).editExpense(name,amount,cid
            )
            call.enqueue(object : Callback< ExpenseData> {
                override fun onFailure(call: Call< ExpenseData>?, t: Throwable?) {
                    setIsError(Observable.EDIT_EXPENSE, t.toString())
                }
                override fun onResponse(call: Call< ExpenseData>?, response: Response< ExpenseData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable.EDIT_EXPENSE,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.EDIT_EXPENSE,it) }
                            }
                        }
                        else {
                            setIsError(Observable.EDIT_EXPENSE, response.toString())
                        }
                    } else {
                        setIsError(Observable.EDIT_EXPENSE, "Error Loading In")
                    }
                }
            })
        }
    }

    fun activate( id: String){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).activateExpense( id.toString()
            )
            call.enqueue(object : Callback< ExpenseData> {
                override fun onFailure(call: Call< ExpenseData>?, t: Throwable?) {
                    setIsError(Observable.ACTIVATE_EXPENSE, t.toString())
                }
                override fun onResponse(call: Call< ExpenseData>?, response: Response< ExpenseData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable.ACTIVATE_EXPENSE,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.ACTIVATE_EXPENSE,it) }
                            }
                        }
                        else {
                            setIsError(Observable.ACTIVATE_EXPENSE, response.toString())
                        }
                    } else {
                        setIsError(Observable.ACTIVATE_EXPENSE, "Error Loading In")
                    }
                }
            })
        }
    }
    fun deactivate( id: String){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).deactivateExpense( id.toString()
            )
            call.enqueue(object : Callback< ExpenseData> {
                override fun onFailure(call: Call< ExpenseData>?, t: Throwable?) {
                    setIsError(Observable.DEACTIVATE_EXPENSE, t.toString())
                }
                override fun onResponse(call: Call< ExpenseData>?, response: Response< ExpenseData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable.DEACTIVATE_EXPENSE,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.DEACTIVATE_EXPENSE,it) }
                            }
                        }
                        else {
                            setIsError(Observable.DEACTIVATE_EXPENSE, response.toString())
                        }
                    } else {
                        setIsError(Observable.DEACTIVATE_EXPENSE, "Error Loading In")
                    }
                }
            })
        }
    }
    enum class Observable {
        DELETE_EXPENSE,
        EDIT_EXPENSE,
        ACTIVATE_EXPENSE,
        DEACTIVATE_EXPENSE,
        ADD_EXPENSE
    }
    fun getToken(): String {
        return profileDao.fetch().token.toString()
    }
    private fun setIsLoading(observable: Observable) {
        when(observable) {
            Observable.EDIT_EXPENSE -> expenseActionObservable.postValue(Resource.loading(null))
            Observable.DELETE_EXPENSE -> expenseActionObservable.postValue(Resource.loading(null))
            Observable.ADD_EXPENSE -> expenseActionObservable.postValue(Resource.loading(null))
            Observable.ACTIVATE_EXPENSE -> expenseActionObservable.postValue(Resource.loading(null))
            Observable.DEACTIVATE_EXPENSE -> expenseActionObservable.postValue(Resource.loading(null))
        }
    }
    private fun <T> setIsSuccesful(observable: Observable, data: T?) {
        when (observable) {
            Observable.EDIT_EXPENSE -> expenseActionObservable.postValue(Resource.success(data as ExpenseData))
            Observable.DELETE_EXPENSE-> expenseActionObservable.postValue(Resource.success(data as  ExpenseData))
            Observable.ADD_EXPENSE->expenseActionObservable.postValue(Resource.success(data as  ExpenseData))
            Observable.ACTIVATE_EXPENSE-> expenseActionObservable.postValue(Resource.success(data as  ExpenseData))
            Observable.DEACTIVATE_EXPENSE-> expenseActionObservable.postValue(Resource.success(data as  ExpenseData))
        }
    }
    private fun setIsError(observable: Observable, message: String) {
        when (observable) {
            Observable.EDIT_EXPENSE -> expenseActionObservable.postValue(Resource.error(message, null))
            Observable.DELETE_EXPENSE-> expenseActionObservable.postValue(Resource.error(message, null))
            Observable.ADD_EXPENSE-> expenseActionObservable.postValue(Resource.error(message, null))
            Observable.ACTIVATE_EXPENSE-> expenseActionObservable.postValue(Resource.error(message, null))
            Observable.DEACTIVATE_EXPENSE-> expenseActionObservable.postValue(Resource.error(message, null))
        }
    }
}