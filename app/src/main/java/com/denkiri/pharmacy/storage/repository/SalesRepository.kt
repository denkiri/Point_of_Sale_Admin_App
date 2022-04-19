package com.denkiri.pharmacy.storage.repository

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.reports.accountReceivable.AccountsReceivableData
import com.denkiri.pharmacy.models.reports.collectionReport.TotalCollection
import com.denkiri.pharmacy.models.reports.salesReport.*
import com.denkiri.pharmacy.network.NetworkUtils
import com.denkiri.pharmacy.network.RequestService
import com.denkiri.pharmacy.storage.PharmacyDatabase
import com.denkiri.pharmacy.storage.daos.*
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class SalesRepository (application: Application){
    private val context: Context
    private val profileDao: ProfileDao
    private val db: PharmacyDatabase
    val salesObservable = MutableLiveData<Resource<SalesReportData>>()
    private val totalSalesDao: TotalSalesDao
    private val totalCostDao: TotalCostDao
    private val totalProfitDao: TotalProfitDao
    private val totalSalesBalanceDao: TotalSalesBalanceDao
    init {
        db = PharmacyDatabase.getDatabase(application)!!
        profileDao = db.profileDao()
        totalSalesDao=db.totalSalesDao()
        totalCostDao=db.totalCostDao()
        totalProfitDao=db.totalProfitDao()
        totalSalesBalanceDao=db.totalSalesBalanceDao()
        context =application.applicationContext
    }
    fun saveTotalSales(totalSales: TotalSales?) {
        if (totalSales!=null) {
            totalSalesDao.delete()
            totalSalesDao.insertTotalSales(totalSales)
        }
    }
    fun saveTotalCost(totalCost: TotalCost?) {
        if (totalCost!=null) {
            totalCostDao.delete()
            totalCostDao.insertTotalCost(totalCost)
        }
    }
    fun saveTotalProfit(totalProfit: TotalProfit?) {
        if (totalProfit!=null) {
            totalProfitDao.delete()
            totalProfitDao.insertTotalProfit(totalProfit)
        }
    }
    fun saveTotalSalesBalance(totalSalesBalance: TotalSalesBalance?) {
        if (totalSalesBalance!=null) {
            totalSalesBalanceDao.delete()
            totalSalesBalanceDao.insertTotalBalance(totalSalesBalance)
        }
    }
    fun getTotalSales(): LiveData<TotalSales> {
        return totalSalesDao.getTotalSales()
    }
    fun getTotalCost(): LiveData<TotalCost> {
        return totalCostDao.getTotalCost()
    }
    fun getTotalProfit(): LiveData<TotalProfit> {
        return totalProfitDao.getTotalProfit()
    }
    fun getTotalSalesBalance(): LiveData<TotalSalesBalance> {
        return totalSalesBalanceDao.getTotalBalance()
    }
    fun getSales() {
        setIsLoading()

        if (NetworkUtils.isConnected(context)) {
            execute()
        } else {
            setIsError(context.getString(R.string.no_connection))
        }
    }
    fun getDaySales(date: String) {
        setIsLoading()

        if (NetworkUtils.isConnected(context)) {
            daySales(date)
        } else {
            setIsError(context.getString(R.string.no_connection))
        }
    }

    private fun execute() {
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).salesReport()
            call.enqueue(object : Callback<SalesReportData> {
                override fun onFailure(call: Call<SalesReportData>?, t: Throwable?) {
                    setIsError(t.toString())
                }

                override fun onResponse(call: Call<SalesReportData>?, response: Response<SalesReportData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                if (response.body()!!.totalsales != null) {
                                    saveTotalSales(response.body()!!.totalsales)
                                } else {
                                    saveTotalSales(TotalSales(0.0))
                                }
                                if (response.body()!!.totalsalescost != null) {
                                    saveTotalCost(response.body()!!.totalsalescost)
                                } else {
                                    saveTotalCost(TotalCost(0.0))
                                }
                                if (response.body()!!.totalprofit != null) {
                                    saveTotalProfit(response.body()!!.totalprofit)
                                } else {
                                    saveTotalProfit(TotalProfit(0.0))
                                }
                                if (response.body()!!.totalbalance != null) {
                                    saveTotalSalesBalance(response.body()!!.totalbalance)
                                } else {
                                    saveTotalSalesBalance(TotalSalesBalance(0.0))
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
    private fun daySales(dates:String) {
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).daySalesReport(dates)
            call.enqueue(object : Callback<SalesReportData> {
                override fun onFailure(call: Call<SalesReportData>?, t: Throwable?) {
                    setIsError(t.toString())
                }

                override fun onResponse(call: Call<SalesReportData>?, response: Response<SalesReportData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                if (response.body()!!.totalsales != null) {
                                    saveTotalSales(response.body()!!.totalsales)
                                } else {
                                    saveTotalSales(TotalSales(0.0))
                                }
                                if (response.body()!!.totalsalescost != null) {
                                    saveTotalCost(response.body()!!.totalsalescost)
                                } else {
                                    saveTotalCost(TotalCost(0.0))
                                }
                                if (response.body()!!.totalprofit != null) {
                                    saveTotalProfit(response.body()!!.totalprofit)
                                } else {
                                    saveTotalProfit(TotalProfit(0.0))
                                }
                                if (response.body()!!.totalbalance != null) {
                                    saveTotalSalesBalance(response.body()!!.totalbalance)
                                } else {
                                    saveTotalSalesBalance(TotalSalesBalance(0.0))
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
        salesObservable.postValue(Resource.loading(null))
    }

    private fun setIsSuccessful(parameters: SalesReportData) {
        salesObservable.postValue(Resource.success(parameters))
    }

    private fun setIsError(message: String) {
        salesObservable.postValue(Resource.error(message, null))
    }
    fun getToken(): String {
        return profileDao.fetch().token.toString()
    }
}