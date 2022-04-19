package com.denkiri.pharmacy.models.reports.inventoryreport
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class InventoryReportData {
    @SerializedName("error")
    @Expose
    var error = false

    @SerializedName("status")
    @Expose
    var status = 0

    @SerializedName("inventoryreport")
    @Expose
    var inventoryreport: List<InventoryReport>? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}