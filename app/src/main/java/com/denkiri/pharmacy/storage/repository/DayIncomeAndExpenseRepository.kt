package com.denkiri.pharmacy.storage.repository
import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.expense.*
import com.denkiri.pharmacy.network.NetworkUtils
import com.denkiri.pharmacy.network.RequestService
import com.denkiri.pharmacy.storage.PharmacyDatabase
import com.denkiri.pharmacy.storage.PreferenceManager
import com.denkiri.pharmacy.storage.daos.DayExpenseDao
import com.denkiri.pharmacy.storage.daos.DayIncomeDao
import com.denkiri.pharmacy.storage.daos.DayNetProfitDao
import com.denkiri.pharmacy.storage.daos.ProfileDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class DayIncomeAndExpenseRepository (application: Application){
    val dayincomeAndExpenseReportObservable = MutableLiveData<Resource<DayIncomeAndExpenseData>>()
    private val profileDao: ProfileDao
    private val dayIncomeDao: DayIncomeDao
    private val dayExpenseDao: DayExpenseDao
    private val dayNetProfitDao: DayNetProfitDao
    private val db: PharmacyDatabase
    private val preferenceManager: PreferenceManager = PreferenceManager(application)
    private val context: Context
    init {
        db = PharmacyDatabase.getDatabase(application)!!
        profileDao = db.profileDao()
        dayIncomeDao=db.dayIncomeDao()
        dayExpenseDao=db.dayExpenseDao()
        dayNetProfitDao=db.dayNetProfitDao()
        context =application.applicationContext
    }
    fun saveDayIncome(dayIncome: DayIncome?) {
        if (dayIncome !=null) {
            dayIncomeDao.delete()
            dayIncomeDao.insertDayIncome(dayIncome)
        }
    }
    fun saveDayExpense(dayExpense: DayExpense?) {
        if (dayExpense !=null) {
            dayExpenseDao.delete()
            dayExpenseDao.insertDayExpense(dayExpense)
        }
    }
    fun saveDayNetProfit(dayNetProfit: DayNetProfit?) {
        if (dayNetProfit !=null) {
            dayNetProfitDao.delete()
            dayNetProfitDao.insertDayNetProfit(dayNetProfit)
        }
    }
    fun getDayIncome(): LiveData<DayIncome> {
        return dayIncomeDao.getDayIncome()
    }
    fun getDayNetProfit(): LiveData<DayNetProfit> {
        return dayNetProfitDao.getDayNetProfit()
    }
    fun getDayExpense(): LiveData<DayExpense> {
        return dayExpenseDao.getDayExpense()
    }
    fun getIncomeAndExpenseData(day: String) {
        setIsLoading()
        if (NetworkUtils.isConnected(context)) {
            execute(day)
        } else {
            setIsError(context.getString(R.string.no_connection))
        }
    }
    private fun execute(day:String) {
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).dayIncomeReport(day)
            call.enqueue(object : Callback<DayIncomeAndExpenseData> {
                override fun onFailure(call: Call<DayIncomeAndExpenseData>?, t: Throwable?) {
                    setIsError(t.toString())
                }
                override fun onResponse(call: Call<DayIncomeAndExpenseData>?, response: Response<DayIncomeAndExpenseData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                if (response.body()!!.dayIncome != null) {
                                    saveDayIncome(response.body()!!.dayIncome)
                                } else {
                                    saveDayIncome(DayIncome(0.0))
                                }
                                if (response.body()!!.dayExpense != null) {
                                    saveDayExpense(response.body()!!.dayExpense)
                                } else {
                                    saveDayExpense(DayExpense(0.0))
                                }
                                if (response.body()!!.dayNetProfit != null) {
                                    saveDayNetProfit(response.body()!!.dayNetProfit)
                                } else {
                                    saveDayNetProfit(DayNetProfit(0.0))
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

    fun getToken(): String {
        return profileDao.fetch().token.toString()
    }
    private fun setIsLoading(){
        dayincomeAndExpenseReportObservable.postValue(Resource.loading(null))
    }
    private fun setIsSuccessful(parameters: DayIncomeAndExpenseData){
        dayincomeAndExpenseReportObservable.postValue(Resource.success(parameters))
    }
    private fun setIsError(message: String){
        dayincomeAndExpenseReportObservable.postValue(Resource.error(message, null))
    }
}