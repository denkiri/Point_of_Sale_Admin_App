package com.denkiri.pharmacy.models.expense
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class DayIncomeAndExpenseData {
    @SerializedName("error")
    @Expose
    var error: Boolean? = null
    @SerializedName("status")
    @Expose
    var status: Int? = null
    @SerializedName("dayIncome")
    @Expose
    var dayIncome: DayIncome? = null
    @SerializedName("dayExpense")
    @Expose
    var dayExpense: DayExpense? = null
    @SerializedName("dayNetReport")
    @Expose
    var dayNetProfit: DayNetProfit? = null
    @SerializedName("income")
    @Expose
    var income: List<Income>? = null
    @SerializedName("expense")
    @Expose
    var expenses: List<RecordedExpense>? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
}