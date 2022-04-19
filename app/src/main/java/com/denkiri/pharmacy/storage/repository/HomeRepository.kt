package com.denkiri.pharmacy.storage.repository

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.dashboard.*
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
class HomeRepository(application: Application)  {
    val homeObservable = MutableLiveData<Resource<DashboardData>>()
    private val profileDao: ProfileDao
    private val dayCostDao: DayCostDao
    private val dayProfitDao:DayProfitDao
    private val daySaleDao:DaySaleDao
    private val monthCostDao:MonthCostDao
    private val monthProfitDao:MonthProfitDao
    private val lastMonthProfitDao:LastMonthProfitDao
    private val monthSaleDao:MonthSaleDao
    private val yearCostDao:YearCostDao
    private val yearProfitDao:YearProfitDao
    private val yearSaleDao:YearSaleDao
    private val yearVatDao:VatDao
    private val db: PharmacyDatabase
    private val preferenceManager: PreferenceManager = PreferenceManager(application)
    private val context: Context
    init {
        db =PharmacyDatabase.getDatabase(application)!!
        profileDao = db.profileDao()
        dayCostDao=db.dayCostDao()
        dayProfitDao=db.dayProfitDao()
        daySaleDao=db.daySaleDao()
        monthCostDao=db.monthCostDao()
        monthProfitDao=db.monthProfitDao()
        lastMonthProfitDao=db.lastMonthProfitDao()
        monthSaleDao=db.monthSaleDao()
        yearCostDao=db.yearCostDao()
        yearProfitDao=db.yearProfitDao()
        yearSaleDao=db.yearSaleDao()
        yearVatDao=db.yearVatDao()
        context =application.applicationContext
    }
    fun saveDashboardData(data:DashboardData){
       // dayCostDao.delete()
      //  dayProfitDao.delete()
     //   daySaleDao.delete()
     //   monthCostDao.delete()
     //   monthProfitDao.delete()
       // yearCostDao.delete()
       // yearProfitDao.delete()
       // yearSaleDao.delete()
      //  data.dayCost?.let { dayCostDao.insertDayCost(it) }
      //  data.dayProfit?.let { dayProfitDao.insertDayProfit(it) }
      //  data.daySale?.let { daySaleDao.insertDaySale(it) }
      //  data.monthCost?.let { monthCostDao.insertMonthCost(it) }
  //      data.monthProfit?.let { monthProfitDao.insertMonthProfit(it) }
       // data.yearCost?.let { yearCostDao.insertYearCost(it) }
       // data.yearProfit?.let { yearProfitDao.insertYearProfit(it) }
       // data.yearSale?.let { yearSaleDao.insertYearSale(it) }
    }
    fun saveYearSale(yearSale: YearSale?) {
        if (yearSale !=null) {
            yearSaleDao.delete()
            yearSaleDao.insertYearSale(yearSale)
        }
    }
    fun saveYearCost(yearCost: YearCost?) {
        if (yearCost !=null) {
            yearCostDao.delete()
            yearCostDao.insertYearCost(yearCost)
        }
    }
    fun saveYearProfit(yearProfit: YearProfit?) {
        if (yearProfit !=null) {
            yearProfitDao.delete()
            yearProfitDao.insertYearProfit(yearProfit)
        }
    }
    fun saveMonthSale(monthSale: MonthSale?) {
        if (monthSale !=null) {
            monthSaleDao.delete()
            monthSaleDao.insertMonthSale(monthSale)
        }
    }
    fun saveMonthCost(monthCost: MonthCost?) {
        if (monthCost !=null) {
            monthCostDao.delete()
            monthCostDao.insertMonthCost(monthCost)
        }
    }

    fun saveMonthProfit(monthProfit: MonthProfit?) {
        if (monthProfit !=null) {
            monthProfitDao.delete()
            monthProfitDao.insertMonthProfit(monthProfit)
        }
    }
    fun saveLastMonthProfit(lastMonthProfit: LastMonthProfit?) {
        if (lastMonthProfit !=null) {
            lastMonthProfitDao.delete()
            lastMonthProfitDao.insertLastMonthProfit(lastMonthProfit)
        }
    }
    fun saveDaySale(daySale: DaySale?) {
        if (daySale !=null) {
            daySaleDao.delete()
            daySaleDao.insertDaySale(daySale)
        }
    }
    fun saveDayCost(dayCost: DayCost?) {
        if (dayCost !=null) {
            dayCostDao.delete()
            dayCostDao.insertDayCost(dayCost)
        }
    }
    fun saveDayProfit(dayProfit: DayProfit?) {
        if (dayProfit !=null) {
            dayProfitDao.delete()
            dayProfitDao.insertDayProfit(dayProfit)
        }
    }
    fun saveYearVat(vat: Vat?) {
        if (vat !=null) {
            yearVatDao.delete()
            yearVatDao.insertYearVat(vat)
        }
    }
    fun getDayCost(): LiveData<DayCost> {
        return dayCostDao.getDayCost()
    }
    fun getDayProfit(): LiveData<DayProfit> {
        return dayProfitDao.getDayProfit()
    }
    fun getDaySale(): LiveData<DaySale> {
        return daySaleDao.getDaySale()
    }
    fun getMonthCost(): LiveData<MonthCost> {
        return monthCostDao.getMonthCost()
    }
    fun getMonthProfit(): LiveData<MonthProfit> {
        return monthProfitDao.getMonthProfit()
    }
    fun getLastMonthProfit(): LiveData<LastMonthProfit> {
        return lastMonthProfitDao.getLastMonthProfit()
    }
    fun getMonthSale(): LiveData<MonthSale> {
        return monthSaleDao.getMonthSale()
    }
    fun getYearCost(): LiveData<YearCost> {
        return yearCostDao.getYearCost()
    }
    fun getYearProfit(): LiveData<YearProfit> {
        return yearProfitDao.getYearProfit()
    }
    fun getYearSale(): LiveData<YearSale> {
        return yearSaleDao.getYearSale()
    }
    fun getYearVat(): LiveData<Vat> {
        return yearVatDao.getYearVat()
    }

    fun getData() {
        setIsLoading()

        if (NetworkUtils.isConnected(context)) {
            execute()
        } else {
            setIsError(context.getString(R.string.no_connection))
        }
    }
    private fun execute() {
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).home()
            call.enqueue(object : Callback<DashboardData> {
                override fun onFailure(call: Call<DashboardData>?, t: Throwable?) {
                    setIsError(t.toString())
                }
                override fun onResponse(call: Call<DashboardData>?, response: Response<DashboardData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                if (response.body()!!.reOrder != null) {
                                    preferenceManager.saveReOrderTotal(response.body()!!.reOrder!!)
                                } else {
                                    preferenceManager.saveReOrderTotal(0)
                                }
                                if (response.body()!!.creditDue != null) {
                                    preferenceManager.saveCreditDue(response.body()!!.creditDue!!)
                                } else {
                                    preferenceManager.saveCreditDue(0)
                                }
                                if (response.body()!!.expiry != null) {
                                    preferenceManager.saveExpiry(response.body()!!.expiry!!)
                                } else {
                                    preferenceManager.saveExpiry(0)
                                }
                                if (response.body()!!.monthSale != null) {
                                    saveMonthSale(response.body()!!.monthSale)
                                } else {
                                    saveMonthSale(MonthSale(0.0))
                                }
                                if (response.body()!!.monthCost != null) {
                                    saveMonthCost(response.body()!!.monthCost)
                                } else {
                                    saveMonthCost(MonthCost(0.0))
                                }
                                if (response.body()!!.monthProfit != null) {
                                    saveMonthProfit(response.body()!!.monthProfit)
                                } else {
                                    saveMonthProfit(MonthProfit(0.0))
                                }
                               if (response.body()!!.daySale != null) {
                                    saveDaySale(response.body()!!.daySale)
                                } else {
                                    saveDaySale(DaySale(0.0))
                                }
                             if (response.body()!!.dayCost != null) {
                                    saveDayCost(response.body()!!.dayCost)
                                } else {
                                    saveDayCost(DayCost(0.0))
                                }
                               if (response.body()!!.dayProfit != null) {
                                    saveDayProfit(response.body()!!.dayProfit)
                                } else {
                                    saveDayProfit(DayProfit(0.0))
                                }
                                if (response.body()!!.yearProfit != null) {
                                    saveYearProfit(response.body()!!.yearProfit)
                                } else {
                                    saveYearProfit(YearProfit(0.0))
                                }
                                if (response.body()!!.yearSale != null) {
                                    saveYearSale(response.body()!!.yearSale)
                                } else {
                                    saveYearSale(YearSale(0.0))
                                }
                                if (response.body()!!.yearCost != null) {
                                    saveYearCost(response.body()!!.yearCost)
                                } else {
                                    saveYearCost(YearCost(0.0))
                                }
                                if (response.body()!!.vat != null) {
                                    saveYearVat(response.body()!!.vat)
                                } else {
                                    saveYearVat(Vat(0.0))
                                }
                                if (response.body()!!.lastMonthProfit != null) {
                                    saveLastMonthProfit(response.body()!!.lastMonthProfit)
                                } else {
                                    saveLastMonthProfit(LastMonthProfit(0.0))
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
        homeObservable.postValue(Resource.loading(null))
    }
    private fun setIsSuccessful(parameters:DashboardData){
        homeObservable.postValue(Resource.success(parameters))
    }
    private fun setIsError(message: String){
        homeObservable.postValue(Resource.error(message, null))
    }

}