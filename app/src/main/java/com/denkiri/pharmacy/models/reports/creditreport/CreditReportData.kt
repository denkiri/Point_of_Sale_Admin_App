package com.denkiri.pharmacy.models.reports.creditreport
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class CreditReportData {
    @SerializedName("error")
    @Expose
    var error = false

    @SerializedName("status")
    @Expose
    var status = 0

    @SerializedName("creditreport")
    @Expose
    var creditreport: List<CreditReport>? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}