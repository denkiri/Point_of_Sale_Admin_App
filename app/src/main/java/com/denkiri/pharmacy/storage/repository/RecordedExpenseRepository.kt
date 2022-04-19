package com.denkiri.pharmacy.storage.repository
import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.expense.RecordedExpenseData
import com.denkiri.pharmacy.models.expense.TotalExpense
import com.denkiri.pharmacy.network.NetworkUtils
import com.denkiri.pharmacy.network.RequestService
import com.denkiri.pharmacy.storage.PharmacyDatabase
import com.denkiri.pharmacy.storage.daos.ProfileDao
import com.denkiri.pharmacy.storage.daos.TotalExpenseDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class RecordedExpenseRepository  (application: Application){
    private val context: Context
    private val profileDao: ProfileDao
    private val db: PharmacyDatabase
    private val totalExpenseDao: TotalExpenseDao
    val recordedExpensesObservable = MutableLiveData<Resource<RecordedExpenseData>>()
    init {
        db = PharmacyDatabase.getDatabase(application)!!
        profileDao = db.profileDao()
        totalExpenseDao=db.totalExpenseDao()
        context =application.applicationContext
    }
    fun saveTotalExpense(totalExpense: TotalExpense?) {
        if (totalExpense !=null) {
            totalExpenseDao.delete()
            totalExpenseDao.insertTotalExpense(totalExpense)
        }
    }
    fun getTotalExpenses(): LiveData<TotalExpense> {
        return totalExpenseDao.getTotalExpense()
    }

    fun getRecordedExpenses() {
        setIsLoading()

        if (NetworkUtils.isConnected(context)) {
            execute()
        } else {
            setIsError(context.getString(R.string.no_connection))
        }
    }

    private fun execute() {
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).recordedExpenses()
            call.enqueue(object : Callback<RecordedExpenseData> {
                override fun onFailure(call: Call<RecordedExpenseData>?, t: Throwable?) {
                    setIsError(t.toString())
                }

                override fun onResponse(call: Call<RecordedExpenseData>?, response: Response<RecordedExpenseData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                if (response.body()!!.totalExpenses != null) {
                                    saveTotalExpense(response.body()!!.totalExpenses)
                                } else {
                                    saveTotalExpense(TotalExpense(0.0))
                                }
                                setIsSuccessful(response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(it) }
                            }
                        } else {
                            setIsError("Error Loading Data")
                        }
                    } else {
                        setIsError("Error Loading Data")
                    }
                }
            })
        }
    }

    private fun setIsLoading() {
        recordedExpensesObservable.postValue(Resource.loading(null))
    }

    private fun setIsSuccessful(parameters:RecordedExpenseData) {
        recordedExpensesObservable.postValue(Resource.success(parameters))
    }

    private fun setIsError(message: String) {
        recordedExpensesObservable.postValue(Resource.error(message, null))
    }
    fun getToken(): String {
        return profileDao.fetch().token.toString()
    }

}