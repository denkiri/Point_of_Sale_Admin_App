package com.denkiri.pharmacy.models.reports.cashreport

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class CashReportData {
    @SerializedName("error")
    @Expose
    var error = false

    @SerializedName("status")
    @Expose
    var status = 0

    @SerializedName("cashreport")
    @Expose
    var cashreport: List<CashReport>? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}