package com.denkiri.pharmacy.storage.repository

import android.app.Application
import android.content.Context
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.dashboard.DayCost
import com.denkiri.pharmacy.models.product.*
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

class ProductReportRepository (application: Application): AndroidViewModel(application)  {
    private val db: PharmacyDatabase
   val reportObservable = MutableLiveData<Resource<ProductReportData>>()
   private val profileDao: ProfileDao
   private val estimatedSalesDao: EstimatedSalesDao
   private  val estimatedProfitDao:EstimatedProfitDao
   private  val  stockValueDao:StockValueDao
   private  val  itemsDao:ItemsDao
    private val preferenceManager: PreferenceManager = PreferenceManager(application)
    private val context: Context
    init {
        db =PharmacyDatabase.getDatabase(application)!!
        profileDao = db.profileDao()
        estimatedSalesDao=db.estimatedSalesDao()
        estimatedProfitDao=db.estimatedProfitDao()
        stockValueDao=db.stockValueDao()
        itemsDao=db.itemsDao()
        context =application.applicationContext
    }
    fun saveEstimatedSales(estimatedSales: EstimatedSales?) {
        if (estimatedSales !=null) {
            estimatedSalesDao.delete()
            estimatedSalesDao.insertEstimatedSales(estimatedSales)
        }
    }
    fun saveEstimatedProfit(estimatedProfit: EstimatedProfit?) {
        if (estimatedProfit !=null) {
            estimatedProfitDao.delete()
            estimatedProfitDao.insertEstimatedProfit(estimatedProfit)
        }
    }
    fun saveStockValue(stockValue: StockValue?) {
        if (stockValue !=null) {
            stockValueDao.delete()
            stockValueDao.insertStockValue(stockValue)
        }
    }
    fun saveItems(items: Items?) {
        if (items !=null) {
            itemsDao.delete()
            itemsDao.insertItems(items)
        }
    }
    fun getData() {
        setIsLoading()

        if (NetworkUtils.isConnected(context)) {
            execute()
        } else {
            setIsError(context.getString(R.string.no_connection))
        }
    }
    fun getEstimatedSales(): LiveData<EstimatedSales> {
        return estimatedSalesDao.getEstimatedSales()
    }
    fun getEstimatedProfit(): LiveData<EstimatedProfit> {
        return estimatedProfitDao.getEstimatedProfit()
    }
    fun getStockValue(): LiveData<StockValue> {
        return stockValueDao.getStockValue()
    }
    fun getItems(): LiveData<Items> {
        return itemsDao.getItems()
    }
    private fun execute() {
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).productsReport()
            call.enqueue(object : Callback<ProductReportData> {
                override fun onFailure(call: Call<ProductReportData>?, t: Throwable?) {
                    setIsError(t.toString())
                }
                override fun onResponse(call: Call<ProductReportData>?, response: Response<ProductReportData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                preferenceManager.saveProductTotal(response.body()!!.productsTotal)
                                if (response.body()!!.items != null) {
                                    saveItems(response.body()!!.items)
                                } else {
                                    saveItems(Items(0))
                                }

                                if (response.body()!!.estimatedProfit != null) {
                                    saveEstimatedProfit(response.body()!!.estimatedProfit)
                                } else {
                                    saveEstimatedProfit(EstimatedProfit(0.0))
                                }
                                if (response.body()!!.stockValue != null) {
                                    saveStockValue(response.body()!!.stockValue)
                                } else {
                                    saveStockValue(StockValue(0.0))
                                }
                                if (response.body()!!.estimatedSales != null) {
                                    saveEstimatedSales(response.body()!!.estimatedSales)
                                } else {
                                    saveEstimatedSales(EstimatedSales(0.0))
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
        reportObservable.postValue(Resource.loading(null))
    }
    private fun setIsSuccessful(parameters:ProductReportData){
        reportObservable.postValue(Resource.success(parameters))
    }
    private fun setIsError(message: String){
        reportObservable.postValue(Resource.error(message, null))
    }
}