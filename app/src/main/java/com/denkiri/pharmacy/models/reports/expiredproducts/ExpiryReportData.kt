package com.denkiri.pharmacy.models.reports.expiredproducts
import com.denkiri.pharmacy.models.reports.collectionReport.TotalCollection
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class ExpiryReportData {
    @SerializedName("error")
    @Expose
    var error = false

    @SerializedName("status")
    @Expose
    var status = 0

    @SerializedName("expiryreport")
    @Expose
    var expiryreport: List<ExpiryReport>? = null
    @SerializedName("totallose")
    @Expose
    var totallose: TotalLose? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}