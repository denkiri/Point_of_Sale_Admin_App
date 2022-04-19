package com.denkiri.pharmacy.models.reports.salesReport

import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

class CustomerTransactionsData {
    @SerializedName("error")
    @Expose
    var error = false

    @SerializedName("status")
    @Expose
    var status = 0

    @SerializedName("customertransactions")
    @Expose
    var salesreport: List<SalesReport>? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
}