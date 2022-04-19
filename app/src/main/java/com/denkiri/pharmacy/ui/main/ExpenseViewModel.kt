package com.denkiri.pharmacy.ui.main
import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import androidx.lifecycle.MutableLiveData
import com.denkiri.pharmacy.models.cashier.UsersData
import com.denkiri.pharmacy.models.category.CategoryData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.dashboard.DashboardData
import com.denkiri.pharmacy.models.dashboard.MonthSale
import com.denkiri.pharmacy.models.expense.*
import com.denkiri.pharmacy.models.reports.incomeReport.*
import com.denkiri.pharmacy.storage.repository.*
import java.text.SimpleDateFormat
import java.time.YearMonth
import java.time.format.DateTimeFormatter
import java.util.*
import kotlin.random.Random

class ExpenseViewModel (application: Application): AndroidViewModel(application){
    internal var usersRepository: UsersRepository
    private val usersObservable = MediatorLiveData<Resource<UsersData>>()
    internal var expenseRepository: ExpenseRepository
    internal var recordedExpenseRepository: RecordedExpenseRepository
    internal var expenseActionRepository: ExpenseActionRepository
    internal var recordedExpenseActionRepository:RecordedExpenseActionRepository
    internal var expenseReportRepository:ExpenseReportRepository
    internal var incomeReportRepository:IncomeReportRepository
    internal var incomeAndExpenseRepository:DayIncomeAndExpenseRepository
    internal var incomeRepository:IncomeRepository
    internal var incomeActionRepository:IncomeActionRepository
    internal var cashierIncomeReportRepository:CashierIncomeReportRepository
    private val expenseObservable = MediatorLiveData<Resource<ExpenseData>>()
    private val expenseActionObservable= MediatorLiveData<Resource<ExpenseData>>()
    private val recordedExpenseObservable = MediatorLiveData<Resource<RecordedExpenseData>>()
    private val recordedExpenseActionObservable= MediatorLiveData<Resource<RecordedExpenseData>>()
    private val expenseReportObservable = MediatorLiveData<Resource<ExpenseReportData>>()
    private val incomeReportObservable = MediatorLiveData<Resource<IncomeReportData>>()
    private val incomeAndExpenseObservable = MediatorLiveData<Resource<DayIncomeAndExpenseData>>()
    private val incomeObservable = MediatorLiveData<Resource<IncomeData>>()
    private val incomeActionObservable = MediatorLiveData<Resource<IncomeData>>()
    private val cashierIncomeReportObservable = MediatorLiveData<Resource<CashierIncomeReportData>>()
    init {
        expenseActionRepository= ExpenseActionRepository(application)
        recordedExpenseActionRepository=RecordedExpenseActionRepository(application)
        recordedExpenseRepository=RecordedExpenseRepository(application)
        expenseRepository = ExpenseRepository(application)
        expenseReportRepository = ExpenseReportRepository(application)
        incomeReportRepository = IncomeReportRepository(application)
        incomeAndExpenseRepository = DayIncomeAndExpenseRepository(application)
        incomeRepository= IncomeRepository(application)
        incomeActionRepository= IncomeActionRepository(application)
        usersRepository= UsersRepository(application)
        cashierIncomeReportRepository= CashierIncomeReportRepository(application)
        usersObservable.addSource(usersRepository.usersObservable) { data -> usersObservable.setValue(data) }
        expenseObservable.addSource(expenseRepository.expensesObservable) { data -> expenseObservable.setValue(data) }
        expenseActionObservable.addSource(expenseActionRepository.expenseActionObservable){ data ->expenseActionObservable.setValue(data)}
        recordedExpenseObservable.addSource(recordedExpenseRepository.recordedExpensesObservable) { data -> recordedExpenseObservable.setValue(data) }
        recordedExpenseActionObservable.addSource(recordedExpenseActionRepository.recordedExpenseActionObservable){ data ->recordedExpenseActionObservable.setValue(data)}
        expenseReportObservable.addSource(expenseReportRepository.expenseReportObservable) { data -> expenseReportObservable.setValue(data) }
        incomeReportObservable.addSource(incomeReportRepository.incomeReportObservable) { data -> incomeReportObservable.setValue(data) }
        incomeAndExpenseObservable.addSource(incomeAndExpenseRepository.dayincomeAndExpenseReportObservable) { data -> incomeAndExpenseObservable.setValue(data) }


        incomeObservable.addSource(incomeRepository.incomeObservable) { data -> incomeObservable.setValue(data) }
        incomeActionObservable.addSource(incomeActionRepository.incomeActionObservable) { data -> incomeActionObservable.setValue(data) }
        cashierIncomeReportObservable.addSource(cashierIncomeReportRepository.incomeReportObservable) { data -> cashierIncomeReportObservable.setValue(data) }

    }
    fun observeUsers(): LiveData<Resource<UsersData>> {
        return usersObservable
    }
    fun getUsers() {
        usersRepository.getUsers()
    }
    fun getRandPassword(n: Int): String
    {
        val characterSet = "003232303232023232023456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"

        val random = Random(System.nanoTime())
        val password = StringBuilder()

        for (i in 0 until n) {
            val rIndex = random.nextInt(characterSet.length)
            password.append(characterSet[rIndex])
        }

        return password.toString()
    }
    private val _time = MutableLiveData<String>().apply {
        val simpleDateFormat = SimpleDateFormat("dd.MMM.YYYY")
        val currentDateAndTime: String = simpleDateFormat.format(Date())
        value=currentDateAndTime
    }
    private val _timeB = MutableLiveData<String>().apply {
        val simpleDateFormat = SimpleDateFormat("MM/dd/YYYY")
        val currentDateAndTime: String = simpleDateFormat.format(Date())
        value=currentDateAndTime
    }
    private val _month = MutableLiveData<String>().apply {
        val simpleDateFormat = SimpleDateFormat("MMMM")
        val currentDateAndTime: String = simpleDateFormat.format(Date())
        value=currentDateAndTime
    }
    private val _lastMonth = MutableLiveData<String>().apply {
        val simpleDateFormat = SimpleDateFormat("MMMM")
        val currentDateAndTime: String = simpleDateFormat.format(Date())
        value=currentDateAndTime
    }

    private val _year = MutableLiveData<String>().apply {
        val simpleDateFormat = SimpleDateFormat("yyyy")
        val currentDateAndTime: String = simpleDateFormat.format(Date())
        value=currentDateAndTime
    }
    val timeB:LiveData<String> =_timeB
    val time: LiveData<String> = _time
    val month: LiveData<String> = _month
    val year: LiveData<String> = _year
    fun observeExpenses(): LiveData<Resource<ExpenseData>> {
        return expenseObservable
    }
    fun observeRecordedExpenses(): LiveData<Resource<RecordedExpenseData>> {
        return recordedExpenseObservable
    }
    fun getExpenses() {
        expenseRepository.getExpenses()
    }
    fun getActiveExpenses(){
        expenseRepository.getActiveExpenses()
    }
    fun getIncome() {
        incomeRepository.getRecordedIncome()
    }
    fun getExpenseReport() {
        expenseReportRepository.getExpenseData()
    }
    fun getIncomeReport() {
        incomeReportRepository.getIncomeData()
    }
    fun deleteExpense(id: Int?){
        expenseActionRepository.deleteExpense(id!!)
    }
    fun deleteIncome(id: Int?){
        incomeActionRepository.deleteIncome(id!!)
    }
    fun observeExpenseAction(): LiveData<Resource<ExpenseData>> {
        return expenseActionObservable
    }
    fun addExpense(name:String?,amount:String?){
        expenseActionRepository.addExpense(name!!,amount!!)
    }
    fun addIncome(service: String?,amount: String?,month:String?,date:String?,year:String?,cashier:String?,items:String?){
        incomeActionRepository.addIncome(service!!,amount!!,month!!,date!!,year!!,cashier!!,items!!)
    }
    fun editIncome(service: String?,amount: String?,month:String?,date:String?,year:String?,cashier:String?,items:String?,cid:String?){
        incomeActionRepository.editIncome(service!!,amount!!,month!!,date!!,year!!,cashier!!,items!!,cid!!)
    }
    fun editExpense(name:String?,amount:String?,cid:String?){
        if (amount != null) {
            if (name != null) {
                expenseActionRepository.editExpense(name,amount,cid!!)
            }
        }
    }
    fun activateExpense(cid: String?){
        expenseActionRepository.activateExpense(cid!!)
    }
    fun getCashierIncome(cid: String?){
        cashierIncomeReportRepository.getCashierIncomeData(cid!!)
    }
    fun getDayIncomeAndExpense(day: String?){
        incomeAndExpenseRepository.getIncomeAndExpenseData(day!!)
    }
    fun deactivateExpense(cid: String?){
        expenseActionRepository.deactivateExpense(cid!!)
    }
    fun recordExpense(expense:String?,amount:String?,month:String?,date:String?,year:String?,payee:String?,receiptNumber:String?){
        recordedExpenseActionRepository.recordExpense(expense!!,amount!!,month!!,date!!, year!!, payee!!, receiptNumber!!)
    }
    fun editRecordedExpense(expense:String?,amount:String?,month:String?,date:String?,year:String?,payee:String?,receiptNumber:String?,cid: String?){
        recordedExpenseActionRepository.editRecordedExpense(expense!!,amount!!,month!!,date!!, year!!, payee!!, receiptNumber!!,cid!!)
    }
    fun deleteRecordedExpense(id: Int?){
        recordedExpenseActionRepository.deleteRecordedExpense(id!!)
    }
    fun getRecordedExpenses() {
        recordedExpenseRepository.getRecordedExpenses()
    }
    fun getDayExpense(): LiveData<DayExpense> {
        return expenseReportRepository.getDayExpense()
    }
    fun getDayExpenseB(): LiveData<DayExpense> {
        return incomeAndExpenseRepository.getDayExpense()
    }
    fun getMonthlyExpense(): LiveData<MonthlyExpense> {
        return expenseReportRepository.getMonthlyExpense()
    }
    fun getYearExpense(): LiveData<YearlyExpense> {
        return expenseReportRepository.getYearlyExpense()
    }
    fun observeExpenseReport(): LiveData<Resource<ExpenseReportData>> {
        return expenseReportObservable
    }
    fun observeRecordedExpenseAction(): LiveData<Resource<RecordedExpenseData>> {
        return recordedExpenseActionObservable
    }
    fun observeIncomeReport(): LiveData<Resource<IncomeReportData>> {
        return incomeReportObservable
    }
    fun getDayIncome(): LiveData<DayIncome> {
        return incomeReportRepository.getDayIncome()
    }
    fun getDayCashierIncome(): LiveData<DayCashierIncome> {
        return cashierIncomeReportRepository.getDayIncome()
    }
    fun getDayIncomeB(): LiveData<DayIncome> {
        return incomeAndExpenseRepository.getDayIncome()
    }
    fun getMonthlyIncome(): LiveData<MonthlyIncome> {
        return incomeReportRepository.getMonthlyIncome()
    }
    fun getCashierMonthlyIncome(): LiveData<MonthlyCashierIncome> {
        return cashierIncomeReportRepository.getMonthlyIncome()
    }
    fun getCashierLastMonthIncome(): LiveData<LastMonthCashierIncome> {
        return cashierIncomeReportRepository.getLastMonthIncome()
    }
    fun getYearIncome(): LiveData<YearlyIncome> {
        return incomeReportRepository.getYearlyIncome()
    }
    fun getCashierYearIncome(): LiveData<YearCashierIncome> {
        return cashierIncomeReportRepository.getYearlyIncome()
    }
    fun getDayNetProfit(): LiveData<DayNetProfit> {
        return incomeReportRepository.getDayNetProfit()
    }
    fun getDayNetProfitB(): LiveData<DayNetProfit> {
        return incomeAndExpenseRepository.getDayNetProfit()
    }
    fun getMonthlyNetProfit(): LiveData<MonthlyNetProfit> {
        return incomeReportRepository.getMonthlyNetProfit()
    }
    fun getYearlyNetProfit(): LiveData<YearlyNetProfit> {
        return incomeReportRepository.getYearlyNetProfit()
    }
    fun getTotalIncome(): LiveData<TotalIncome> {
        return incomeReportRepository.getTotalIncome()
    }
    fun getTotalExpenses(): LiveData<TotalExpense> {
        return incomeReportRepository.getTotalExpenses()
    }
    fun getNetProfit(): LiveData<NetProfit> {
        return incomeReportRepository.getNetProfit()
    }
    fun observeIncomeData(): LiveData<Resource<IncomeData>> {
        return incomeObservable
    }
    fun observeCashierIncomeData(): LiveData<Resource<CashierIncomeReportData>> {
        return cashierIncomeReportObservable
    }
    fun observeIncomeActionData(): LiveData<Resource<IncomeData>> {
        return incomeActionObservable
    }
    fun observeDayIncomeAndExpenseReport(): LiveData<Resource<DayIncomeAndExpenseData>> {
        return incomeAndExpenseObservable
    }
}