package com.denkiri.pharmacy.storage.repository
import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.reports.incomeReport.*
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
class CashierIncomeReportRepository  (application: Application) {
    val incomeReportObservable = MutableLiveData<Resource<CashierIncomeReportData>>()
    private val profileDao: ProfileDao
    private val dayIncomeDao: DayCashierIncomeDao
    private val monthlyIncomeDao: MonthlyCashierIncomeDao
    private val lastMonthIncomeDao: LastMonthCashierIncomeDao
    private val yearlyIncomeDao: YearCashierIncomeDao
    private val db: PharmacyDatabase
    private val preferenceManager: PreferenceManager = PreferenceManager(application)
    private val context: Context
    init {
        db = PharmacyDatabase.getDatabase(application)!!
        profileDao = db.profileDao()
        dayIncomeDao=db.dayCashierIncomeDao()
        monthlyIncomeDao=db.monthlyCashierIncomeDao()
        lastMonthIncomeDao=db.lastMonthCashierIncomeDao()
        yearlyIncomeDao=db.yearCashierIncomeDao()
        context =application.applicationContext
    }
    fun saveDayCashierIncome(dayCashierIncome: DayCashierIncome?) {
        if (dayCashierIncome !=null) {
            dayIncomeDao.delete()
            dayIncomeDao.insertDayCashierIncome(dayCashierIncome)
        }
    }
    fun saveMonthlyIncome(monthlyIncome: MonthlyCashierIncome?) {
        if (monthlyIncome !=null) {
            monthlyIncomeDao.delete()
            monthlyIncomeDao.insertMonthlyCashierIncome(monthlyIncome)
        }
    }
    fun saveLastMonthIncome(monthlyIncome: LastMonthCashierIncome?) {
        if (monthlyIncome !=null) {
            lastMonthIncomeDao.delete()
            lastMonthIncomeDao.insertLastMonthCashierIncome(monthlyIncome)
        }
    }
    fun saveYearIncome(yearIncome: YearCashierIncome?) {
        if (yearIncome !=null) {
            yearlyIncomeDao.delete()
            yearlyIncomeDao.insertYearCashierIncome(yearIncome)
        }
    }

    fun getDayIncome(): LiveData<DayCashierIncome> {
        return dayIncomeDao.getDayCashierIncome()
    }

    fun getMonthlyIncome(): LiveData<MonthlyCashierIncome> {
        return monthlyIncomeDao.getMonthlyCashierIncome()
    }
    fun getLastMonthIncome(): LiveData<LastMonthCashierIncome> {
        return lastMonthIncomeDao.getLastMonthCashierIncome()
    }
    fun getYearlyIncome(): LiveData<YearCashierIncome> {
        return yearlyIncomeDao.getYearCashierIncome()
    }
    fun getCashierIncomeData(cid:String) {
        setIsLoading()
        if (NetworkUtils.isConnected(context)) {
            execute(cid)
        } else {
            setIsError(context.getString(R.string.no_connection))
        }
    }
    private fun execute(cid:String) {
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).cashierIncomeReport(cid)
            call.enqueue(object : Callback<CashierIncomeReportData> {
                override fun onFailure(call: Call<CashierIncomeReportData>?, t: Throwable?) {
                    setIsError(t.toString())
                }
                override fun onResponse(call: Call<CashierIncomeReportData>?, response: Response<CashierIncomeReportData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                if (response.body()!!.dayCashierIncome != null) {
                                    saveDayCashierIncome(response.body()!!.dayCashierIncome)
                                } else {
                                    saveDayCashierIncome(DayCashierIncome(0.0))
                                }
                                if (response.body()!!.monthlyCashierIncome != null) {
                                    saveMonthlyIncome(response.body()!!.monthlyCashierIncome)
                                } else {
                                    saveMonthlyIncome(MonthlyCashierIncome(0.0))
                                }
                                if (response.body()!!.lastMonthCashierIncome != null) {
                                    saveLastMonthIncome(response.body()!!.lastMonthCashierIncome)
                                } else {
                                    saveLastMonthIncome(LastMonthCashierIncome(0.0))
                                }
                                if (response.body()!!.yearCashierIncome!= null) {
                                    saveYearIncome(response.body()!!.yearCashierIncome)
                                } else {
                                    saveYearIncome(YearCashierIncome(0.0))
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
    private fun setIsSuccessful(parameters: CashierIncomeReportData){
        incomeReportObservable.postValue(Resource.success(parameters))
    }
    private fun setIsError(message: String){
        incomeReportObservable.postValue(Resource.error(message, null))
    }

}