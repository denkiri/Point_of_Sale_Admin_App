package com.denkiri.pharmacy.ui.home

import android.app.Application
import android.os.Build
import androidx.annotation.RequiresApi
import androidx.lifecycle.*
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.dashboard.*
import com.denkiri.pharmacy.models.oauth.Oauth
import com.denkiri.pharmacy.models.oauth.Profile
import com.denkiri.pharmacy.models.product.*
import com.denkiri.pharmacy.models.reports.salesReport.*
import com.denkiri.pharmacy.models.reports.salesReport.TotalCost
import com.denkiri.pharmacy.storage.PreferenceManager
import com.denkiri.pharmacy.storage.repository.HomeRepository
import com.denkiri.pharmacy.storage.repository.ProductReportRepository
import com.denkiri.pharmacy.storage.repository.SalesRepository
import com.denkiri.pharmacy.storage.repository.SignInRepository
import java.text.SimpleDateFormat
import java.time.LocalDateTime
import java.time.format.DateTimeFormatter
import java.util.*

class HomeViewModel(application: Application) : AndroidViewModel(application) {
    private val preferenceManager: PreferenceManager = PreferenceManager(application)
    internal var homeRepository: HomeRepository
    internal var productReportRepository:ProductReportRepository
    private val  productReportObservable=MediatorLiveData<Resource<ProductReportData>>()
    private val homeObservable = MediatorLiveData<Resource<DashboardData>>()
    internal var salesRepository: SalesRepository
    private val salesObservable = MediatorLiveData<Resource<SalesReportData>>()
    internal  var signInRepository: SignInRepository
    private val signInObservable = MediatorLiveData<Resource<Oauth>>()
    private val _text = MutableLiveData<String>().apply {
        value = "This is home Fragment"
    }
    val text: LiveData<String> = _text
    private val _time=MutableLiveData<String>().apply {
        val simpleDateFormat = SimpleDateFormat("yyyy.MMM.dd")
        val currentDateAndTime: String = simpleDateFormat.format(Date())
        value=currentDateAndTime
    }
    private val _month=MutableLiveData<String>().apply {
        val simpleDateFormat = SimpleDateFormat("MMMM")
        val currentDateAndTime: String = simpleDateFormat.format(Date())
        value=currentDateAndTime
    }
    private val _year=MutableLiveData<String>().apply {
        val simpleDateFormat = SimpleDateFormat("yyyy")
        val currentDateAndTime: String = simpleDateFormat.format(Date())
        value=currentDateAndTime
    }
    val time:LiveData<String> = _time
    val month:LiveData<String> = _month
    val year:LiveData<String> = _year
    init {
        salesRepository = SalesRepository(application)
        salesObservable.addSource(salesRepository.salesObservable) { data -> salesObservable.setValue(data) }
        homeRepository = HomeRepository(application)
        homeObservable.addSource(homeRepository.homeObservable) { data -> homeObservable.setValue(data) }
        productReportRepository= ProductReportRepository(application)
        productReportObservable.addSource(productReportRepository.reportObservable) { data -> productReportObservable.setValue(data) }
        signInRepository = SignInRepository(application)
        signInObservable.addSource(signInRepository.signInObservable){ data-> signInObservable.setValue(data)}

    }
    fun observeDashboard(): LiveData<Resource<DashboardData>> {
        return homeObservable
    }
    fun observeProductReport(): LiveData<Resource<ProductReportData>> {
        return productReportObservable
    }
    fun getDashboard() {
        homeRepository.getData()

    }
    fun getProductReport() {
        productReportRepository.getData()

    }
    fun getOuthProfile(): LiveData<Profile> {
        return signInRepository.getProfile()
    }
    fun saveDashboardData(data:DashboardData){
        homeRepository.saveDashboardData(data)

    }
    fun getProductTotal(): Int {
        return preferenceManager.getProductTotal()
    }
    fun getEstimatedSales(): LiveData<EstimatedSales> {
        return productReportRepository.getEstimatedSales()
    }
    fun getEstimatedProfit(): LiveData<EstimatedProfit> {
        return productReportRepository.getEstimatedProfit()
    }
    fun getStockValue(): LiveData<StockValue> {
        return productReportRepository.getStockValue()
    }
    fun getItems(): LiveData<Items> {
        return productReportRepository.getItems()
    }
    fun getReOrder(): Int {
        return preferenceManager.getReOrderTotal()
    }
    fun getCreditDue(): Int {
        return preferenceManager.getCreditDue()
    }
    fun getExpiry(): Int {
        return preferenceManager.getExpiry()
    }
    fun getTotalProduct(): Int {
        return preferenceManager.getProductTotal()
    }
    fun getYearSale(): LiveData<YearSale> {
        return homeRepository.getYearSale()
    }
    fun getYearCost(): LiveData<YearCost> {
        return homeRepository.getYearCost()
    }
    fun getYearProfit(): LiveData<YearProfit> {
        return homeRepository.getYearProfit()
    }
    fun getMonthProfit(): LiveData<MonthProfit> {
        return homeRepository.getMonthProfit()
    }
    fun getMonthSale(): LiveData<MonthSale> {
        return homeRepository.getMonthSale()
    }
    fun getMonthCost(): LiveData<MonthCost> {
        return homeRepository.getMonthCost()
    }
    fun getDayProfit(): LiveData<DayProfit> {
        return homeRepository.getDayProfit()
    }
    fun getDayCost(): LiveData<DayCost> {
        return homeRepository.getDayCost()
    }
    fun getDaySale(): LiveData<DaySale> {
        return homeRepository.getDaySale()
    }
    fun getSalesReport() {
        salesRepository.getSales()
    }
    fun getTotalSales(): LiveData<TotalSales> {
        return salesRepository.getTotalSales()
    }
    fun getTotalCost(): LiveData<TotalCost> {
        return salesRepository.getTotalCost()
    }
    fun getTotalProfit(): LiveData<TotalProfit> {
        return salesRepository.getTotalProfit()
    }
    fun getTotalSalesBalance(): LiveData<TotalSalesBalance> {
        return salesRepository.getTotalSalesBalance()
    }
    fun getYearVat(): LiveData<Vat> {
        return homeRepository.getYearVat()
    }
    fun getLastMonthProfit(): LiveData<LastMonthProfit> {
        return homeRepository.getLastMonthProfit()
    }
    fun observeSalesReport(): LiveData<Resource<SalesReportData>> {
        return salesObservable
    }



}