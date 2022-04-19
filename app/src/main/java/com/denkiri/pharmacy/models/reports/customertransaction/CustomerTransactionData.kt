package com.denkiri.pharmacy.models.reports.customertransaction

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
class CustomerTransactionData {
    @SerializedName("error")
    @Expose
    var error = false

    @SerializedName("status")
    @Expose
    var status = 0

    @SerializedName("customertransaction")
    @Expose
    var customertransaction: List<CustomerTransaction>? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}