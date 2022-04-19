package com.denkiri.pharmacy.models.expense
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class RecordedExpenseData {
    @SerializedName("error")
    @Expose
    var error: Boolean? = null

    @SerializedName("status")
    @Expose
    var status: Int? = null
    @SerializedName("totalExpense")
    @Expose
    var totalExpenses: TotalExpense? = null
    @SerializedName("expenses")
    @Expose
    var expenses: List<RecordedExpense>? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}