package com.denkiri.pharmacy.models.expense
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class RecordedExpense {
    @SerializedName("expense_id")
    @Expose
    var id: Int? = 0

    @SerializedName("expense")
    @Expose
    var expense: String? = null

    @SerializedName("amount")
    @Expose
    var amount: Double? =0.00

    @SerializedName("month")
    @Expose
    var month: String? = null

    @SerializedName("time")
    @Expose
    var time: String? = null

    @SerializedName("date")
    @Expose
    var date: String? = null

    @SerializedName("year")
    @Expose
    var year: String? = null

    @SerializedName("payee")
    @Expose
    var payee: String? = null

    @SerializedName("receipt_number")
    @Expose
    var receiptNumber: String? = null
    @SerializedName("name")
    @Expose
    var expenseName: String? = null


}