package com.denkiri.pharmacy.storage.repository

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.order.OrderData
import com.denkiri.pharmacy.models.order.TotalInvoiceAmount
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

class OrderActionRepository (application: Application) {
    val orderActionObservable = MutableLiveData<Resource<OrderData>>()
    private val context: Context
    private val profileDao: ProfileDao
    private val orderDao: OrderDao
    private val totalAmountDao: TotalAmountDao
    private val db: PharmacyDatabase
    private val preferenceManager: PreferenceManager = PreferenceManager(application)
    init {
        db =PharmacyDatabase.getDatabase(application)!!
        context =application.applicationContext
        profileDao = db.profileDao()
        orderDao=db.orderDao()
        totalAmountDao=db.totalAmountDao()
    }

        fun saveTotalAmount(totalAmount: TotalInvoiceAmount) {
            GlobalScope.launch(context = Dispatchers.Main)
            {
            totalAmountDao.delete()
            totalAmount.totalAmount?.let { totalAmountDao.insertTotalAmount(totalAmount) }
        }
    }
    fun saveSupplierId(supplierId: String?) {
        GlobalScope.launch(context = Dispatchers.Main)
        {
            preferenceManager.saveSupplierId(supplierId!!)
        }
    }
    fun getTotalAmount(): LiveData<TotalInvoiceAmount> {

        return totalAmountDao.getTotalAmount()
    }
    fun addOrder(price:String,cost:String,quantity:String,expiryDate:String,productCode:String,invoice:String,status:String,vat:String,discount:String) {
        setIsLoading(Observable.ADD_ORDER)

        if (NetworkUtils.isConnected(context)) {
            execute(price, cost, quantity, expiryDate, productCode, invoice, status, vat, discount)
        } else {
            setIsError(Observable.ADD_ORDER, context.getString(R.string.no_connection))
        }
    }
    fun deleteCart( productId: String?,quantity: String?,productCode: String?,invoice:String?) {
        setIsLoading(Observable.DELETE_CART_ITEM)

        if (NetworkUtils.isConnected(context)) {
            deleteCartItem(productId, quantity, productCode, invoice)
        } else {
            setIsError(Observable.DELETE_CART_ITEM, context.getString(R.string.no_connection))
        }
    }
    fun execute(price:String,cost:String,quantity:String,expiryDate:String,productCode:String,invoice:String,status:String,vat:String,discount:String){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).addOrder(price,cost,quantity,expiryDate,productCode, invoice, status, vat, discount)
            call.enqueue(object : Callback<OrderData> {
                override fun onFailure(call: Call<OrderData>?, t: Throwable?) {
                    setIsError(Observable.ADD_ORDER, t.toString())
                }
                override fun onResponse(call: Call<OrderData>?, response: Response<OrderData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                saveOrder(response.body()!!)
                                if (response.body()!!.totalAmount != null) {
                                    preferenceManager.saveTotalAmount(response.body()!!.totalAmount.toString()!!)
                                } else {
                                    preferenceManager.saveTotalAmount("0")
                                }
                                if (response.body()!!.totalAmount != null) {
                                    saveTotalAmount(response.body()!!.totalAmount!!)
                                } else {
                                    saveTotalAmount(TotalInvoiceAmount(0.0))
                                }
                                setIsSuccesful(Observable.ADD_ORDER,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.ADD_ORDER,it) }
                            }
                        }
                        else {
                            setIsError(Observable.ADD_ORDER, response.toString())
                        }
                    } else {
                        setIsError(Observable.ADD_ORDER, "Error Loading In")
                    }
                }
            })
        }

    }

    fun deleteCartItem( productId: String?,quantity: String?,productCode: String?,invoice:String?){
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).deleteOrder(productId, quantity, productCode, invoice)
            call.enqueue(object : Callback<OrderData> {
                override fun onFailure(call: Call<OrderData>?, t: Throwable?) {
                    setIsError(Observable.DELETE_CART_ITEM, t.toString())
                }
                override fun onResponse(call: Call<OrderData>?, response: Response<OrderData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                if (response.body()!!.totalAmount != null) {
                                    saveTotalAmount(response.body()!!.totalAmount!!)

                                } else {
                                    saveTotalAmount(TotalInvoiceAmount(0.0))
                                }
                                setIsSuccesful(Observable.DELETE_CART_ITEM,response.body()!!)
                            } else {
                                response.body()?.message?.let { setIsError(Observable.DELETE_CART_ITEM,it) }
                            }
                        }
                        else {
                            setIsError(Observable.DELETE_CART_ITEM, response.toString())
                        }
                    } else {
                        setIsError(Observable.DELETE_CART_ITEM, "Error Loading In")
                    }
                }
            })
        }

    }
    fun getToken(): String {
        return profileDao.fetch().token.toString()
    }

    fun saveOrder(data:OrderData){
        GlobalScope.launch(context = Dispatchers.Main)
        {
        orderDao.delete()
        data.order?.let { orderDao.insertAll(it) }
    }}
    fun getProducts(){
     //   productsObservable.postValue(Resource.success(productDao.getAll()))

    }
    enum class Observable {

        ADD_ORDER,
        DELETE_CART_ITEM

    }
    private fun setIsLoading(observable: Observable) {
        when(observable) {
            Observable.ADD_ORDER -> orderActionObservable.postValue(Resource.loading(null))
            Observable.DELETE_CART_ITEM -> orderActionObservable.postValue(Resource.loading(null))
            }
    }
    private fun <T> setIsSuccesful(observable: Observable, data: T?) {
        when (observable) {
            Observable.ADD_ORDER -> orderActionObservable.postValue(Resource.success(data as OrderData))
            Observable.DELETE_CART_ITEM -> orderActionObservable.postValue(Resource.success(data as OrderData))
          }
    }
    private fun setIsError(observable: Observable, message: String) {
        when (observable) {
            Observable.ADD_ORDER -> orderActionObservable.postValue(Resource.error(message, null))
            Observable.DELETE_CART_ITEM -> orderActionObservable.postValue(Resource.error(message, null))
             }
    }
}