package com.denkiri.pharmacy.storage.repository

import android.app.Application
import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.dashboard.DaySale
import com.denkiri.pharmacy.models.dashboard.MonthCost
import com.denkiri.pharmacy.models.dashboard.YearCost
import com.denkiri.pharmacy.models.reports.accountReceivable.AccountsReceivableData
import com.denkiri.pharmacy.models.reports.accountReceivable.TotalBalance
import com.denkiri.pharmacy.network.NetworkUtils
import com.denkiri.pharmacy.network.RequestService
import com.denkiri.pharmacy.storage.PharmacyDatabase
import com.denkiri.pharmacy.storage.daos.DaySaleDao
import com.denkiri.pharmacy.storage.daos.ProfileDao
import com.denkiri.pharmacy.storage.daos.TotalBalanceDao
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.GlobalScope
import kotlinx.coroutines.launch
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class AccountReceivableRepository (application: Application) {
    private val context: Context
    private val totalBalanceDao: TotalBalanceDao
    private val profileDao: ProfileDao
    private val db: PharmacyDatabase
    val accountsReceivableObservable = MutableLiveData<Resource<AccountsReceivableData>>()
    init {

        db = PharmacyDatabase.getDatabase(application)!!
        profileDao = db.profileDao()
        context =application.applicationContext
        totalBalanceDao=db.totalBalanceDao()
    }
    fun saveTotalBalance(totalBalance: TotalBalance?) {
        if (totalBalance!=null) {
            totalBalanceDao.delete()
            totalBalanceDao.insertTotalBalance(totalBalance)
        }
    }
    fun getAccountsReceivables() {
        setIsLoading()

        if (NetworkUtils.isConnected(context)) {
            execute()
        } else {
            setIsError(context.getString(R.string.no_connection))
        }
    }
    private fun execute() {
        GlobalScope.launch(context = Dispatchers.Main) {
            val call = RequestService.getService(getToken()).accountReceivable()
            call.enqueue(object : Callback<AccountsReceivableData> {
                override fun onFailure(call: Call<AccountsReceivableData>?, t: Throwable?) {
                    setIsError(t.toString())
                }

                override fun onResponse(call: Call<AccountsReceivableData>?, response: Response<AccountsReceivableData>?) {
                    if (response != null) {
                        if (response.isSuccessful) {
                            if (response.body()?.status == 1) {
                                if (response.body()!!.totalbalance != null) {
                                    saveTotalBalance(response.body()!!.totalbalance)
                                } else {
                                    saveTotalBalance(TotalBalance(0.0))
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
        accountsReceivableObservable.postValue(Resource.loading(null))
    }

    private fun setIsSuccessful(parameters: AccountsReceivableData) {
        accountsReceivableObservable.postValue(Resource.success(parameters))
    }

    private fun setIsError(message: String) {
        accountsReceivableObservable.postValue(Resource.error(message, null))
    }
    fun getToken(): String {
        return profileDao.fetch().token.toString()
    }
    fun getTotalBalance(): LiveData<TotalBalance> {
        return totalBalanceDao.getTotalBalance()
    }

}