package com.denkiri.pharmacy.ui.main
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.dashboard.LastMonthProfit
import com.denkiri.pharmacy.models.oauth.Oauth
import com.denkiri.pharmacy.models.oauth.Profile
import com.denkiri.pharmacy.models.payment.PaymentResponseData
import com.denkiri.pharmacy.models.receipt.ReceiptData
import com.denkiri.pharmacy.models.reports.salesReport.SalesReportData
import com.denkiri.pharmacy.models.reports.salesReport.TotalProfit
import com.denkiri.pharmacy.models.subscription.Subscription
import com.denkiri.pharmacy.models.subscription.SubscriptionData
import com.denkiri.pharmacy.models.supplier.SupplierData
import com.denkiri.pharmacy.storage.repository.*

class SubscriptionViewModel(application: Application): AndroidViewModel(application) {
    internal var receiptRepository:ReceiptRepository
    internal var subscriptionActionRepository:SubscriptionActionRepository
    internal var homeRepository: HomeRepository
    internal  var signInRepository: SignInRepository
    private  val subscriptionActionObservable = MediatorLiveData<Resource<PaymentResponseData>>()
    private val signInObservable = MediatorLiveData<Resource<Oauth>>()
    internal  var subscriptionRepository: SubscriptionRepository
    private val subscriptionObservable = MediatorLiveData<Resource<SubscriptionData>>()
    private val receiptObservable = MediatorLiveData<Resource<ReceiptData>>()
    init {
        receiptRepository= ReceiptRepository(application)
        subscriptionActionRepository= SubscriptionActionRepository(application)
        homeRepository = HomeRepository(application)
         signInRepository = SignInRepository(application)
        receiptObservable.addSource(receiptRepository.receiptObservable){ data->   receiptObservable.setValue(data)}
        signInObservable.addSource(signInRepository.signInObservable){ data-> signInObservable.setValue(data)}
        subscriptionRepository = SubscriptionRepository(application)
        subscriptionObservable.addSource(subscriptionRepository.subscriptionObservable){ data->   subscriptionObservable.setValue(data)}
        subscriptionActionObservable.addSource(subscriptionActionRepository.subscriptionActionObservable){ data->   subscriptionActionObservable.setValue(data)}
    }
    fun subscription(parameters: SubscriptionData) {
        subscriptionRepository.getSubscription(parameters)

    }
    fun observeSubscriptionAction(): LiveData<Resource<PaymentResponseData>> {
        return subscriptionActionObservable
    }
    fun subscribe(amount: String?,phone:String?,code:String?){
        subscriptionActionRepository.subscribe(amount!!,phone!!,code!!)
    }
    fun completeSubscription(amount: String?,phone:String?,code:String?){
        subscriptionActionRepository.completeSub(amount!!,phone!!,code!!)
    }
    fun getOuthProfile(): LiveData<Profile> {
        return signInRepository.getProfile()
    }
    fun observeSubscription(): LiveData<Resource<SubscriptionData>> {
        return subscriptionObservable
    }
    fun observeReceipt(): LiveData<Resource<ReceiptData>> {
        return receiptObservable
    }
    fun getReceipt(code: String?){
        receiptRepository.getReceipts(code!!)
    }
    fun saveSubscription(data: SubscriptionData){
        subscriptionRepository.saveSubscription(data)

    }
    fun getSubscription(): LiveData<Subscription> {
        return subscriptionRepository.getSubscription()
    }
    fun getLastMonthProfit(): LiveData<LastMonthProfit> {
        return homeRepository.getLastMonthProfit()
    }

}