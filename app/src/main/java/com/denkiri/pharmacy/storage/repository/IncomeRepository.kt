package com.denkiri.pharmacy.storage.repository

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.expense.IncomeData
import com.denkiri.pharmacy.models.expense.RecordedExpenseData
import com.denkiri.pharmacy.models.expense.TotalIncome
import com.denkiri.pharmacy.network.NetworkUtils
import com.denkiri.pharmacy.network.RequestService
import com.denkiri.pharmacy.storage.PharmacyDatabase
import com.denkiri.pharmacy.storage.daos.ProfileDao
import com.denkiri.pharmacy.storage.daos.TotalIncomeDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class IncomeRepository (application: Application){
    private val context: Context
    private val profileDao: ProfileDao
    private val totalIncomeDao: TotalIncomeDao
    private val db: PharmacyDatabase
    val incomeObservable = MutableLiveData<Resource<IncomeData>>()
    init {
        db = PharmacyDatabase.getDatabase(application)!!
        totalIncomeDao=db.totalIncomeDao()
        profileDao = db.profileDao()
        context =application.applicationContext
    }
    fun saveTotalIncome(totalIncome: TotalIncome?) {
        if (totalIncome !=null) {
            totalIncomeDao.delete()
            totalIncomeDao.insertTotalIncome(totalIncome)
        }
    }
    fun getTotalIncome(): LiveData<TotalIncome> {
        return totalIncomeDao.getTotalIncome()
    }

    fun getRecordedIncome() {
        setIsLoading()

        if (NetworkUtils.isConnected(context)) {
            execute()
        } else {
            setIsError(context.getString(R.string.no_connection))
        }
    }

    private fun execute() {
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).income()
            call.enqueue(object : Callback<IncomeData> {
                override fun onFailure(call: Call<IncomeData>?, t: Throwable?) {
                    setIsError(t.toString())
                }

                override fun onResponse(call: Call<IncomeData>?, response: Response<IncomeData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                if (response.body()!!.totalIncome != null) {
                                    saveTotalIncome(response.body()!!.totalIncome)
                                } else {
                                    saveTotalIncome(TotalIncome(0.0))
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
        incomeObservable.postValue(Resource.loading(null))
    }

    private fun setIsSuccessful(parameters: IncomeData) {
        incomeObservable.postValue(Resource.success(parameters))
    }

    private fun setIsError(message: String) {
        incomeObservable.postValue(Resource.error(message, null))
    }
    fun getToken(): String {
        return profileDao.fetch().token.toString()
    }

}