package com.denkiri.pharmacy.models.expense
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class ExpenseReportData {
    @SerializedName("error")
    @Expose
    var error: Boolean? = null

    @SerializedName("status")
    @Expose
    var status: Int? = null

    @SerializedName("dayExpense")
    @Expose
    var dayExpense: DayExpense? = null

    @SerializedName("monthlyExpense")
    @Expose
    var monthlyExpense: MonthlyExpense? = null

    @SerializedName("yearlyExpense")
    @Expose
    var yearlyExpense: YearlyExpense? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}