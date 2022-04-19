package com.denkiri.pharmacy.storage.repository

import android.app.Application
import android.content.Context
import androidx.lifecycle.MutableLiveData
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.order.PurchaseOrderData
import com.denkiri.pharmacy.network.NetworkUtils
import com.denkiri.pharmacy.network.RequestService
import com.denkiri.pharmacy.storage.PharmacyDatabase
import com.denkiri.pharmacy.storage.PreferenceManager
import com.denkiri.pharmacy.storage.daos.OrderDao
import com.denkiri.pharmacy.storage.daos.ProfileDao
import com.denkiri.pharmacy.storage.daos.TotalAmountDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.http.Field

class PurchaseRepository(application: Application){
    private val context: Context
    private val profileDao: ProfileDao
    private val db: PharmacyDatabase
    private val orderDao: OrderDao
    private val totalAmountDao: TotalAmountDao
    val purchaseListObservable = MutableLiveData<Resource<PurchaseOrderData>>()
    private val preferenceManager: PreferenceManager = PreferenceManager(application)
    init {
        db = PharmacyDatabase.getDatabase(application)!!
        profileDao = db.profileDao()
        orderDao=db.orderDao()
        totalAmountDao=db.totalAmountDao()
        context =application.applicationContext
    }
    fun getItems() {
        setIsLoading()

        if (NetworkUtils.isConnected(context)) {
            execute()
        } else {
            setIsError(context.getString(R.string.no_connection))
        }
    }
    fun saveOrder(invoice:String,supplier: String,dateDelivered: String,dueDate: String,receiptNumber:String, paymentStatus: String) {
        setIsLoading()
        if (NetworkUtils.isConnected(context)) {
            saveInvoice(invoice, supplier, dateDelivered, dueDate, receiptNumber, paymentStatus)
        } else {
            setIsError(context.getString(R.string.no_connection))
        }
    }
    fun complete(pid:String) {
        setIsLoading()
        if (NetworkUtils.isConnected(context)) {
            completePayment(pid)
        } else {
            setIsError(context.getString(R.string.no_connection))
        }
    }
    fun saveSupplierId(supplierId: String?) {
        GlobalScope.launch(context = Dispatchers.Main)
        {
            preferenceManager.saveSupplierId(supplierId!!)
        }
    }
    private fun execute() {
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).purchasesInvoices()
            call.enqueue(object : Callback<PurchaseOrderData> {
                override fun onFailure(call: Call<PurchaseOrderData>?, t: Throwable?) {
                    setIsError(t.toString())
                }
                override fun onResponse(call: Call<PurchaseOrderData>?, response: Response<PurchaseOrderData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
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
    private fun saveInvoice(invoice:String,supplier: String,dateDelivered: String,dueDate: String,receiptNumber:String, paymentStatus: String) {
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).saveInvoice(invoice, supplier, dateDelivered, dueDate, receiptNumber, paymentStatus)
            call.enqueue(object : Callback<PurchaseOrderData> {
                override fun onFailure(call: Call<PurchaseOrderData>?, t: Throwable?) {
                    setIsError(t.toString())
                }
                override fun onResponse(call: Call<PurchaseOrderData>?, response: Response<PurchaseOrderData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                orderDao.delete()
                                totalAmountDao.delete()
                                saveSupplierId("0")
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
    private fun completePayment(pid:String) {
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).completePayment(pid)
            call.enqueue(object : Callback<PurchaseOrderData> {
                override fun onFailure(call: Call<PurchaseOrderData>?, t: Throwable?) {
                    setIsError(t.toString())
                }
                override fun onResponse(call: Call<PurchaseOrderData>?, response: Response<PurchaseOrderData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
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
        purchaseListObservable.postValue(Resource.loading(null))
    }

    private fun setIsSuccessful(parameters: PurchaseOrderData) {
        purchaseListObservable.postValue(Resource.success(parameters))
    }

    private fun setIsError(message: String) {
        purchaseListObservable.postValue(Resource.error(message, null))
    }

    fun getToken(): String {
        return profileDao.fetch().token.toString()
    }
}