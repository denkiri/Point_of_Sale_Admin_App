package com.denkiri.pharmacy.storage.repository
import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.expense.RecordedExpenseData
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
class RecordedExpenseActionRepository  (application: Application){
    private val preferenceManager: PreferenceManager = PreferenceManager(application)
    val recordedExpenseActionObservable = MutableLiveData<Resource<RecordedExpenseData>>()
    private val context: Context
    private val profileDao: ProfileDao
    private val db: PharmacyDatabase
    init {
        db = PharmacyDatabase.getDatabase(application)!!
        profileDao = db.profileDao()
        context =application.applicationContext
    }
    fun recordExpense(expense:String,amount: String,month:String,date:String,year:String,payee:String,receiptNumber:String) {
        setIsLoading(Observable.ADD_RECORDED_EXPENSE)
        if (NetworkUtils.isConnected(context)) {
            execute(expense, amount, month, date, year, payee, receiptNumber)
        } else {
            setIsError(Observable.ADD_RECORDED_EXPENSE, context.getString(R.string.no_connection))
        }
    }
    fun editRecordedExpense(expense:String,amount: String,month:String,date:String,year:String,payee:String,receiptNumber:String,cid: String) {
        setIsLoading(Observable.EDIT_RECORDED_EXPENSE)

        if (NetworkUtils.isConnected(context)) {
            updateExpense(expense, amount, month, date, year, payee, receiptNumber,cid)
        } else {
            setIsError(Observable.EDIT_RECORDED_EXPENSE, context.getString(R.string.no_connection))
        }
    }

    fun execute(expense:String,amount: String,month:String,date:String,year:String,payee:String,receiptNumber:String){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).recordExpense(expense, amount, month, date, year, payee, receiptNumber)
            call.enqueue(object : Callback<RecordedExpenseData> {
                override fun onFailure(call: Call<RecordedExpenseData>?, t: Throwable?) {
                    setIsError(Observable.ADD_RECORDED_EXPENSE, t.toString())
                }
                override fun onResponse(call: Call<RecordedExpenseData>?, response: Response<RecordedExpenseData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable.ADD_RECORDED_EXPENSE,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.ADD_RECORDED_EXPENSE,it) }
                            }
                        }
                        else {
                            setIsError(Observable.ADD_RECORDED_EXPENSE, response.toString())
                        }
                    } else {
                        setIsError(Observable.ADD_RECORDED_EXPENSE, "Error Loading In")
                    }
                }
            })
        }

    }

    fun deleteRecordedExpense( cid: Int){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).deleteRecordedExpense(cid.toString())
            call.enqueue(object : Callback<RecordedExpenseData> {
                override fun onFailure(call: Call<RecordedExpenseData>?, t: Throwable?) {
                    setIsError(Observable.DELETE_RECORDED_EXPENSE, t.toString())
                }
                override fun onResponse(call: Call<RecordedExpenseData>?, response: Response<RecordedExpenseData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable.DELETE_RECORDED_EXPENSE,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.DELETE_RECORDED_EXPENSE,it) }
                            }
                        }
                        else {
                            setIsError(Observable.DELETE_RECORDED_EXPENSE, response.toString())
                        }
                    } else {
                        setIsError(Observable.DELETE_RECORDED_EXPENSE, "Error Loading In")
                    }
                }
            })
        }
    }
    fun updateExpense(expense:String,amount: String,month:String,date:String,year:String,payee:String,receiptNumber:String,cid: String){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).editRecordedExpense(expense, amount, month, date, year, payee, receiptNumber,cid)
            call.enqueue(object : Callback<RecordedExpenseData> {
                override fun onFailure(call: Call<RecordedExpenseData>?, t: Throwable?) {
                    setIsError(Observable.EDIT_RECORDED_EXPENSE, t.toString())
                }
                override fun onResponse(call: Call<RecordedExpenseData>?, response: Response<RecordedExpenseData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                setIsSuccesful(Observable.EDIT_RECORDED_EXPENSE,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.EDIT_RECORDED_EXPENSE,it) }
                            }
                        }
                        else {
                            setIsError(Observable.EDIT_RECORDED_EXPENSE, response.toString())
                        }
                    } else {
                        setIsError(Observable.EDIT_RECORDED_EXPENSE, "Error Loading In")
                    }
                }
            })
        }
    }
    enum class Observable {
        DELETE_RECORDED_EXPENSE,
        EDIT_RECORDED_EXPENSE,
        ADD_RECORDED_EXPENSE
    }
    fun getToken(): String {
        return profileDao.fetch().token.toString()
    }
    private fun setIsLoading(observable: Observable) {
        when(observable) {
            Observable.EDIT_RECORDED_EXPENSE -> recordedExpenseActionObservable.postValue(Resource.loading(null))
            Observable.DELETE_RECORDED_EXPENSE -> recordedExpenseActionObservable.postValue(Resource.loading(null))
            Observable.ADD_RECORDED_EXPENSE -> recordedExpenseActionObservable.postValue(Resource.loading(null))
             }
    }
    private fun <T> setIsSuccesful(observable: Observable, data: T?) {
        when (observable) {
            Observable.EDIT_RECORDED_EXPENSE -> recordedExpenseActionObservable.postValue(Resource.success(data as RecordedExpenseData))
            Observable.DELETE_RECORDED_EXPENSE-> recordedExpenseActionObservable.postValue(Resource.success(data as RecordedExpenseData))
            Observable.ADD_RECORDED_EXPENSE->recordedExpenseActionObservable.postValue(Resource.success(data as RecordedExpenseData))
            }
    }
    private fun setIsError(observable: Observable, message: String) {
        when (observable) {
            Observable.EDIT_RECORDED_EXPENSE -> recordedExpenseActionObservable.postValue(Resource.error(message, null))
            Observable.DELETE_RECORDED_EXPENSE-> recordedExpenseActionObservable.postValue(Resource.error(message, null))
            Observable.ADD_RECORDED_EXPENSE-> recordedExpenseActionObservable.postValue(Resource.error(message, null))
            }
    }
}