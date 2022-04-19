package com.denkiri.pharmacy.models.reports.collectionReport
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class CollectionReport {
    @SerializedName("transaction_id")
    @Expose
    var transactionId:Int? = 0

    @SerializedName("date")
    @Expose
    var date: String? = null

    @SerializedName("name")
    @Expose
    var name: String? = null

    @SerializedName("invoice")
    @Expose
    var invoice: String? = null
    @SerializedName("amount_received")
    @Expose
    var amountReceived: Double? = 0.0
    @SerializedName("amount")
    @Expose
    var amount: Double? = 0.0

    @SerializedName("remarks")
    @Expose
    var remarks: String? = null

    @SerializedName("balance")
    @Expose
    var balance: Double? =0.0

    @SerializedName("invoice_number")
    @Expose
    var invoiceNumber: String? = null

    @SerializedName("cashier")
    @Expose
    var cashier: String? = null

    @SerializedName("type")
    @Expose
    var type: String? = null

    @SerializedName("due_date")
    @Expose
    var dueDate: String? = null

    @SerializedName("total_amount")
    @Expose
    var totalAmount: Double? = 0.0

    @SerializedName("cash")
    @Expose
    var cash: Any? = null

    @SerializedName("month")
    @Expose
    var month: String? = null

    @SerializedName("year")
    @Expose
    var year: String? = null

    @SerializedName("p_amount")
    @Expose
    var pAmount: Double? = 0.0

    @SerializedName("vat")
    @Expose
    var vat: String? = null

    @SerializedName("time")
    @Expose
    var time: String? = null
}