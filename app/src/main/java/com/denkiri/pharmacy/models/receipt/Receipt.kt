package com.denkiri.pharmacy.models.receipt
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class Receipt {
    @SerializedName("id")
    @Expose
    var id: Int? = 0

    @SerializedName("date")
    @Expose
    var date: String? = null

    @SerializedName("amount")
    @Expose
    var amount: String? = null

    @SerializedName("month")
    @Expose
    var month: String? = null

    @SerializedName("year")
    @Expose
    var year: String? = null

    @SerializedName("paymentStatus")
    @Expose
    var paymentStatus: Int? = null

    @SerializedName("payment_details")
    @Expose
    var paymentDetails: String? = null

    @SerializedName("mpesa_amount")
    @Expose
    var mpesaAmount: String? = null

    @SerializedName("MpesaReceiptNumber")
    @Expose
    var mpesaReceiptNumber: String? = null

    @SerializedName("TransactionDate")
    @Expose
    var transactionDate: String? = null

    @SerializedName("PhoneNumber")
    @Expose
    var phoneNumber: String? = null

    @SerializedName("business_code")
    @Expose
    var businessCode: String? = null

    @SerializedName("invoice_number")
    @Expose
    var invoiceNumber: String? = null
}