package com.denkiri.pharmacy.storage.repository
import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.dashboard.*
import com.denkiri.pharmacy.models.expense.DayExpense
import com.denkiri.pharmacy.models.expense.ExpenseReportData
import com.denkiri.pharmacy.models.expense.MonthlyExpense
import com.denkiri.pharmacy.models.expense.YearlyExpense
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

class ExpenseReportRepository (application: Application) {
    val expenseReportObservable = MutableLiveData<Resource<ExpenseReportData>>()
    private val profileDao: ProfileDao
    private val dayExpenseDao: DayExpenseDao
    private val monthlyExpenseDao: MonthlyExpenseDao
    private val yearlyExpenseDao: YearlyExpenseDao
    private val db: PharmacyDatabase
    private val preferenceManager: PreferenceManager = PreferenceManager(application)
    private val context: Context
    init {
        db =PharmacyDatabase.getDatabase(application)!!
        profileDao = db.profileDao()
        dayExpenseDao=db.dayExpenseDao()
        monthlyExpenseDao=db.monthlyExpenseDao()
        yearlyExpenseDao=db.yearlyExpenseDao()
        context =application.applicationContext
    }
    fun saveDayExpense(dayExpense: DayExpense?) {
        if (dayExpense !=null) {
            dayExpenseDao.delete()
            dayExpenseDao.insertDayExpense(dayExpense)
        }
    }
    fun saveMonthlyExpense(monthlyExpense: MonthlyExpense?) {
        if (monthlyExpense !=null) {
            monthlyExpenseDao.delete()
            monthlyExpenseDao.insertMonthlyExpense(monthlyExpense)
        }
    }
    fun saveYearExpense(yearExpense: YearlyExpense?) {
        if (yearExpense !=null) {
            yearlyExpenseDao.delete()
            yearlyExpenseDao.insertYearlyExpense(yearExpense)
        }
    }
    fun getDayExpense(): LiveData<DayExpense> {
        return dayExpenseDao.getDayExpense()
    }
    fun getMonthlyExpense(): LiveData<MonthlyExpense> {
        return monthlyExpenseDao.getMonthlyExpense()
    }
    fun getYearlyExpense(): LiveData<YearlyExpense> {
        return yearlyExpenseDao.getYearlyExpense()
    }
    fun getExpenseData() {
        setIsLoading()
        if (NetworkUtils.isConnected(context)) {
            execute()
        } else {
            setIsError(context.getString(R.string.no_connection))
        }
    }
    private fun execute() {
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).expenseReport()
            call.enqueue(object : Callback<ExpenseReportData> {
                override fun onFailure(call: Call<ExpenseReportData>?, t: Throwable?) {
                    setIsError(t.toString())
                }
                override fun onResponse(call: Call<ExpenseReportData>?, response: Response<ExpenseReportData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                if (response.body()!!.dayExpense != null) {
                                    saveDayExpense(response.body()!!.dayExpense)
                                } else {
                                    saveDayExpense(DayExpense(0.0))
                                }
                                if (response.body()!!.monthlyExpense != null) {
                                    saveMonthlyExpense(response.body()!!.monthlyExpense)
                                } else {
                                    saveMonthlyExpense(MonthlyExpense(0.0))
                                }
                                if (response.body()!!.yearlyExpense!= null) {
                                    saveYearExpense(response.body()!!.yearlyExpense)
                                } else {
                                    saveYearExpense(YearlyExpense(0.0))
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
        expenseReportObservable.postValue(Resource.loading(null))
    }
    private fun setIsSuccessful(parameters:ExpenseReportData){
        expenseReportObservable.postValue(Resource.success(parameters))
    }
    private fun setIsError(message: String){
        expenseReportObservable.postValue(Resource.error(message, null))
    }

}