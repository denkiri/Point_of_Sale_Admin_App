package com.denkiri.pharmacy.storage
import android.content.Context
import androidx.room.Database
import androidx.room.Room
import androidx.room.RoomDatabase
import com.denkiri.pharmacy.models.brand.Brand
import com.denkiri.pharmacy.models.category.Category
import com.denkiri.pharmacy.models.dashboard.*
import com.denkiri.pharmacy.models.expense.*
import com.denkiri.pharmacy.models.oauth.Profile
import com.denkiri.pharmacy.models.order.TotalInvoiceAmount
import com.denkiri.pharmacy.models.product.EstimatedProfit
import com.denkiri.pharmacy.models.product.EstimatedSales
import com.denkiri.pharmacy.models.product.Items
import com.denkiri.pharmacy.models.product.StockValue
import com.denkiri.pharmacy.models.reports.accountReceivable.TotalBalance
import com.denkiri.pharmacy.models.reports.collectionReport.TotalCollection
import com.denkiri.pharmacy.models.reports.expiredproducts.TotalLose
import com.denkiri.pharmacy.models.reports.salesReport.TotalCost
import com.denkiri.pharmacy.models.reports.salesReport.TotalProfit
import com.denkiri.pharmacy.models.reports.salesReport.TotalSales
import com.denkiri.pharmacy.models.reports.salesReport.TotalSalesBalance
import com.denkiri.pharmacy.models.subscription.Subscription
import com.denkiri.pharmacy.models.order.Order
import com.denkiri.pharmacy.models.reports.incomeReport.DayCashierIncome
import com.denkiri.pharmacy.models.reports.incomeReport.LastMonthCashierIncome
import com.denkiri.pharmacy.models.reports.incomeReport.MonthlyCashierIncome
import com.denkiri.pharmacy.models.reports.incomeReport.YearCashierIncome
import com.denkiri.pharmacy.storage.daos.*

@Database(entities = [Profile::class,Category::class ,DayCost::class, DayProfit::class, DaySale::class,MonthCost::class, MonthProfit::class, MonthSale::class, YearCost::class, YearProfit::class, YearSale::class,TotalBalance::class,TotalCollection::class,TotalSalesBalance::class,TotalSales::class,TotalCost::class,TotalProfit::class,Vat::class,Items::class,EstimatedSales::class,EstimatedProfit::class,StockValue::class, TotalLose::class,Subscription::class,LastMonthProfit::class,Brand::class, TotalInvoiceAmount::class,Order::class,YearlyExpense::class,MonthlyExpense::class,DayExpense::class,DayIncome::class,MonthlyIncome::class,YearlyIncome::class,DayNetProfit::class,MonthlyNetProfit::class,YearlyNetProfit::class,NetProfit::class,TotalIncome::class,TotalExpense::class, DayCashierIncome::class, LastMonthCashierIncome::class, MonthlyCashierIncome::class, YearCashierIncome::class],version = 1,exportSchema = false)
 abstract class PharmacyDatabase :RoomDatabase() {
     companion object{
         private lateinit var INSTANCE:PharmacyDatabase
         fun getDatabase(context: Context):PharmacyDatabase?{
             synchronized(PharmacyDatabase::class.java){
                 INSTANCE = Room.databaseBuilder(context.applicationContext,
                     PharmacyDatabase::class.java,"pharmacy_database"
                 )
                     .fallbackToDestructiveMigration()
                     .allowMainThreadQueries()
                     .build()
             }
             return INSTANCE
         }}
    abstract fun profileDao(): ProfileDao
    abstract fun  dayCostDao():DayCostDao
    abstract fun  daySaleDao():DaySaleDao
    abstract fun  dayProfitDao():DayProfitDao
    abstract fun  monthCostDao():MonthCostDao
    abstract fun  monthProfitDao():MonthProfitDao
    abstract fun monthSaleDao():MonthSaleDao
    abstract fun yearProfitDao():YearProfitDao
    abstract fun yearCostDao():YearCostDao
    abstract fun yearSaleDao():YearSaleDao
    abstract fun categoryDao():CategoryDao
    abstract fun brandDao():BrandDao
    abstract fun totalBalanceDao():TotalBalanceDao
    abstract fun totalCollectionDao():TotalCollectionDao
    abstract fun totalSalesBalanceDao():TotalSalesBalanceDao
    abstract fun totalCostDao():TotalCostDao
    abstract fun totalSalesDao():TotalSalesDao
    abstract fun totalProfitDao():TotalProfitDao
    abstract fun yearVatDao():VatDao
    abstract fun estimatedSalesDao():EstimatedSalesDao
    abstract fun estimatedProfitDao():EstimatedProfitDao
    abstract fun stockValueDao():StockValueDao
    abstract fun itemsDao():ItemsDao
    abstract fun totalLoseDao():TotalLoseDao
    abstract fun subscriptionDao():SubscriptionDao
    abstract fun lastMonthProfitDao():LastMonthProfitDao
    abstract fun totalAmountDao():TotalAmountDao
    abstract fun orderDao():OrderDao
    abstract fun yearlyExpenseDao():YearlyExpenseDao
    abstract fun monthlyExpenseDao():MonthlyExpenseDao
    abstract fun dayExpenseDao():DayExpenseDao
    abstract fun yearlyIncomeDao():YearlyIncomeDao
    abstract fun monthlyIncomeDao():MonthlyIncomeDao
    abstract fun dayIncomeDao():DayIncomeDao
    abstract fun dayNetProfitDao():DayNetProfitDao
    abstract fun monthlyNetProfitDao():MonthlyNetProfitDao
    abstract fun yearlyNetProfitDao():YearlyNetProfitDao
    abstract fun totalIncomeDao():TotalIncomeDao
    abstract fun totalExpenseDao():TotalExpenseDao
    abstract fun netProfitDao():NetProfitDao
    abstract fun dayCashierIncomeDao():DayCashierIncomeDao
    abstract fun monthlyCashierIncomeDao():MonthlyCashierIncomeDao
    abstract fun lastMonthCashierIncomeDao():LastMonthCashierIncomeDao
    abstract fun yearCashierIncomeDao():YearCashierIncomeDao
 }
