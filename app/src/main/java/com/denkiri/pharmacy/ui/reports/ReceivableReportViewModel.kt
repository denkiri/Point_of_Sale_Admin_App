package com.denkiri.pharmacy.ui.reports
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.dashboard.MonthCost
import com.denkiri.pharmacy.models.reports.accountReceivable.AccountsReceivableData
import com.denkiri.pharmacy.models.reports.accountReceivable.TotalBalance
import com.denkiri.pharmacy.storage.repository.AccountReceivableRepository
class ReceivableReportViewModel (application: Application) : AndroidViewModel(application) {
    internal var accountReceivableRepository: AccountReceivableRepository
    private val accountsReceivableObservable = MediatorLiveData<Resource<AccountsReceivableData>>()
    init {
        accountReceivableRepository = AccountReceivableRepository(application)
        accountsReceivableObservable.addSource(accountReceivableRepository.accountsReceivableObservable) { data -> accountsReceivableObservable.setValue(data) }
    }
    fun getAccountsReceivables() {
        accountReceivableRepository.getAccountsReceivables()
    }
    fun getTotalBalance(): LiveData<TotalBalance> {
        return accountReceivableRepository.getTotalBalance()
    }
    fun observeAccountsReceivables(): LiveData<Resource<AccountsReceivableData>> {
        return accountsReceivableObservable
    }
}