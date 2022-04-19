package com.denkiri.pharmacy.models.reports.mpesareport
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class MpesaReportData {
    @SerializedName("error")
    @Expose
    var error = false

    @SerializedName("status")
    @Expose
    var status = 0

    @SerializedName("mpesareport")
    @Expose
    var mpesareport: List<MpesaReport>? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}