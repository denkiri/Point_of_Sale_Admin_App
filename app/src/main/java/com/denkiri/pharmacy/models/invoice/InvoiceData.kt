package com.denkiri.pharmacy.models.invoice
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class InvoiceData {
    @SerializedName("error")
    @Expose
    var error = false

    @SerializedName("status")
    @Expose
    var status = 0

    @SerializedName("success")
    @Expose
    var success = false

    @SerializedName("invoices")
    @Expose
    var invoices: List<Invoice>? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}