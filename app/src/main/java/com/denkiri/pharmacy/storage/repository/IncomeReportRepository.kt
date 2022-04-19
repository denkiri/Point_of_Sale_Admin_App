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
import com.denkiri.pharmacy.storage.daos.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
class IncomeReportRepository (application: Application) {
    val incomeReportObservable = MutableLiveData<Resource<IncomeReportData>>()
    private val profileDao: ProfileDao
    private val dayIncomeDao: DayIncomeDao
    private val monthlyIncomeDao: MonthlyIncomeDao
    private val yearlyIncomeDao: YearlyIncomeDao
    private val dayNetProfitDao: DayNetProfitDao
    private val monthlyNetProfitDao: MonthlyNetProfitDao
    private val yearlyNetProfitDao: YearlyNetProfitDao
    private val totalIncomeDao: TotalIncomeDao
    private val totalExpenseDao: TotalExpenseDao
    private val netProfitDao: NetProfitDao
    private val db: PharmacyDatabase
    private val preferenceManager: PreferenceManager = PreferenceManager(application)
    private val context: Context
    init {
        db = PharmacyDatabase.getDatabase(application)!!
        profileDao = db.profileDao()
        dayIncomeDao=db.dayIncomeDao()
        monthlyIncomeDao=db.monthlyIncomeDao()
        yearlyIncomeDao=db.yearlyIncomeDao()
        dayNetProfitDao=db.dayNetProfitDao()
        monthlyNetProfitDao=db.monthlyNetProfitDao()
        yearlyNetProfitDao=db.yearlyNetProfitDao()
        totalIncomeDao=db.totalIncomeDao()
        totalExpenseDao=db.totalExpenseDao()
        netProfitDao=db.netProfitDao()
        context =application.applicationContext
    }
    fun saveDayIncome(dayIncome: DayIncome?) {
        if (dayIncome !=null) {
            dayIncomeDao.delete()
            dayIncomeDao.insertDayIncome(dayIncome)
        }
    }
    fun saveDayNetProfit(dayNetProfit: DayNetProfit?) {
        if (dayNetProfit !=null) {
            dayNetProfitDao.delete()
            dayNetProfitDao.insertDayNetProfit(dayNetProfit)
        }
    }
    fun saveMonthlyIncome(monthlyIncome: MonthlyIncome?) {
        if (monthlyIncome !=null) {
            monthlyIncomeDao.delete()
            monthlyIncomeDao.insertMonthlyIncome(monthlyIncome)
        }
    }
    fun saveMonthlyNetProfit(monthlyNetProfit: MonthlyNetProfit?) {
        if (monthlyNetProfit !=null) {
            monthlyNetProfitDao.delete()
            monthlyNetProfitDao.insertMonthlyNetProfit(monthlyNetProfit)
        }
    }
    fun saveYearIncome(yearIncome: YearlyIncome?) {
        if (yearIncome !=null) {
            yearlyIncomeDao.delete()
            yearlyIncomeDao.insertYearlyIncome(yearIncome)
        }
    }
    fun saveYearlyNetProfit(yearlyNetProfit: YearlyNetProfit?) {
        if (yearlyNetProfit !=null) {
            yearlyNetProfitDao.delete()
            yearlyNetProfitDao.insertYearlyNetProfit(yearlyNetProfit)
        }
    }
    fun saveTotalIncome(totalIncome: TotalIncome?) {
        if (totalIncome !=null) {
            totalIncomeDao.delete()
            totalIncomeDao.insertTotalIncome(totalIncome)
        }
    }
    fun saveTotalExpense(totalExpense: TotalExpense?) {
        if (totalExpense !=null) {
            totalExpenseDao.delete()
            totalExpenseDao.insertTotalExpense(totalExpense)
        }
    }
    fun saveNetProfit(netProfit: NetProfit?) {
        if (netProfit !=null) {
            netProfitDao.delete()
            netProfitDao.insertTotalProfit(netProfit)
        }
    }

    fun getDayIncome(): LiveData<DayIncome> {
        return dayIncomeDao.getDayIncome()
    }
    fun getDayNetProfit(): LiveData<DayNetProfit> {
        return dayNetProfitDao.getDayNetProfit()
    }
    fun getMonthlyIncome(): LiveData<MonthlyIncome> {
        return monthlyIncomeDao.getMonthlyIncome()
    }
    fun getMonthlyNetProfit(): LiveData<MonthlyNetProfit> {
        return monthlyNetProfitDao.getMonthlyNetProfit()
    }
    fun getYearlyIncome(): LiveData<YearlyIncome> {
        return yearlyIncomeDao.getYearlyIncome()
    }
    fun getYearlyNetProfit(): LiveData<YearlyNetProfit> {
        return yearlyNetProfitDao.getYearlyNetProfit()
    }
    fun getTotalIncome(): LiveData<TotalIncome> {
        return totalIncomeDao.getTotalIncome()
    }
    fun getTotalExpenses(): LiveData<TotalExpense> {
        return totalExpenseDao.getTotalExpense()
    }
    fun getNetProfit(): LiveData<NetProfit> {
        return netProfitDao.getTotalProfit()
    }
    fun getIncomeData() {
        setIsLoading()
        if (NetworkUtils.isConnected(context)) {
            execute()
        } else {
            setIsError(context.getString(R.string.no_connection))
        }
    }
    private fun execute() {
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).incomeReport()
            call.enqueue(object : Callback<IncomeReportData> {
                override fun onFailure(call: Call<IncomeReportData>?, t: Throwable?) {
                    setIsError(t.toString())
                }
                override fun onResponse(call: Call<IncomeReportData>?, response: Response<IncomeReportData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                if (response.body()!!.dayIncome != null) {
                                    saveDayIncome(response.body()!!.dayIncome)
                                } else {
                                    saveDayIncome(DayIncome(0.0))
                                }
                                if (response.body()!!.monthlyIncome != null) {
                                    saveMonthlyIncome(response.body()!!.monthlyIncome)
                                } else {
                                    saveMonthlyIncome(MonthlyIncome(0.0))
                                }
                                if (response.body()!!.yearlyIncome!= null) {
                                    saveYearIncome(response.body()!!.yearlyIncome)
                                } else {
                                    saveYearIncome(YearlyIncome(0.0))
                                }
                                if (response.body()!!.yearlyIncome!= null) {
                                    saveYearIncome(response.body()!!.yearlyIncome)
                                } else {
                                    saveYearIncome(YearlyIncome(0.0))
                                }
                                if (response.body()!!.dayNetProfit != null) {
                                    saveDayNetProfit(response.body()!!.dayNetProfit)
                                } else {
                                    saveDayNetProfit(DayNetProfit(0.0))
                                }
                                if (response.body()!!.monthlyNetProfit != null) {
                                    saveMonthlyNetProfit(response.body()!!.monthlyNetProfit)
                                } else {
                                    saveMonthlyNetProfit(MonthlyNetProfit(0.0))
                                }
                                if (response.body()!!.yearlyNetProfit != null) {
                                    saveYearlyNetProfit(response.body()!!.yearlyNetProfit)
                                } else {
                                    saveYearlyNetProfit(YearlyNetProfit(0.0))
                                }
                                if (response.body()!!.totalIncome != null) {
                                    saveTotalIncome(response.body()!!.totalIncome)
                                } else {
                                    saveTotalIncome(TotalIncome(0.0))
                                }
                                if (response.body()!!.totalExpenses != null) {
                                    saveTotalExpense(response.body()!!.totalExpenses)
                                } else {
                                    saveTotalExpense(TotalExpense(0.0))
                                }
                                if (response.body()!!.netProfit != null) {
                                    saveNetProfit(response.body()!!.netProfit)
                                } else {
                                    saveNetProfit(NetProfit(0.0))
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
        incomeReportObservable.postValue(Resource.loading(null))
    }
    private fun setIsSuccessful(parameters: IncomeReportData){
        incomeReportObservable.postValue(Resource.success(parameters))
    }
    private fun setIsError(message: String){
        incomeReportObservable.postValue(Resource.error(message, null))
    }

}