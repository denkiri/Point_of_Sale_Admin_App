package com.denkiri.pharmacy.models.expense
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class IncomeReportData {
    @SerializedName("error")
    @Expose
    var error: Boolean? = null

    @SerializedName("status")
    @Expose
    var status: Int? = null

    @SerializedName("dayIncome")
    @Expose
    var dayIncome: DayIncome? = null

    @SerializedName("monthlyIncome")
    @Expose
    var monthlyIncome: MonthlyIncome? = null

    @SerializedName("yearlyIncome")
    @Expose
    var yearlyIncome: YearlyIncome? = null
    @SerializedName("dayNetReport")
    @Expose
    var dayNetProfit: DayNetProfit? = null

    @SerializedName("monthlyNetReport")
    @Expose
    var monthlyNetProfit: MonthlyNetProfit? = null
    @SerializedName("yearlyNetReport")
    @Expose
    var yearlyNetProfit: YearlyNetProfit? = null
    @SerializedName("totalIncome")
    @Expose
    var totalIncome: TotalIncome? = null
    @SerializedName("totalExpense")
    @Expose
    var totalExpenses: TotalExpense? = null
    @SerializedName("netProfit")
    @Expose
    var netProfit: NetProfit? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}