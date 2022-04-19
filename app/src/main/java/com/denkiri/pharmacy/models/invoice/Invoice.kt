package com.denkiri.pharmacy.models.invoice
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class Invoice {
    @SerializedName("transaction_id")
    @Expose
    var transactionId = 0
    @SerializedName("invoice_number")
    @Expose
    var invoiceNumber: String? = null
    @SerializedName("cashier")
    @Expose
    var cashier: String? = null
    @SerializedName("date")
    @Expose
    var date: String? = null
    @SerializedName("type")
    @Expose
    var type: String? = null
    @SerializedName("amount")
    @Expose
    var amount: String? = null
    @SerializedName("due_date")
    @Expose
    var dueDate: String? = null
    @SerializedName("name")
    @Expose
    var name: String? = null
    @SerializedName("balance")
    @Expose
    var balance: Double = 0.00
    @SerializedName("total_amount")
    @Expose
    var totalAmount: Double = 0.00
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
    var pAmount: String? = null
    @SerializedName("vat")
    @Expose
    var vat: String? = null
    @SerializedName("time")
    @Expose
    var time: String? = null
    @SerializedName("customer_id")
    @Expose
    var customerId = 0
    @SerializedName("customer_name")
    @Expose
    var customerName: String? = null
    @SerializedName("address")
    @Expose
    var address: String? = null
    @SerializedName("contact")
    @Expose
    var contact: String? = null
    @SerializedName("membership_number")
    @Expose
    var membershipNumber: Any? = null
    @SerializedName("first_name")
    @Expose
    var firstName: String? = null
    @SerializedName("middle_name")
    @Expose
    var middleName: String? = null
    @SerializedName("last_name")
    @Expose
    var lastName: String? = null
}