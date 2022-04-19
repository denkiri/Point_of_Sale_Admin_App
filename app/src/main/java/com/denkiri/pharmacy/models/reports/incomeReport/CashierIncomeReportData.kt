package com.denkiri.pharmacy.models.reports.incomeReport
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class CashierIncomeReportData {
    @SerializedName("error")
    @Expose
    var error= false
    @SerializedName("status")
    @Expose
    var status = 0
    @SerializedName("dayCashierIncome")
    @Expose
    var dayCashierIncome: DayCashierIncome? = null
    @SerializedName("monthlyCashierIncome")
    @Expose
    var monthlyCashierIncome: MonthlyCashierIncome? = null
    @SerializedName("lastMonthCashierIncome")
    @Expose
    var lastMonthCashierIncome: LastMonthCashierIncome? = null
    @SerializedName("yearCashierIncome")
    @Expose
    var yearCashierIncome: YearCashierIncome? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
}