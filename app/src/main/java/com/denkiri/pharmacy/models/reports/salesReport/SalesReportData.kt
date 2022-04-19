package com.denkiri.pharmacy.models.reports.salesReport
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class SalesReportData {
    @SerializedName("error")
    @Expose
    var error = false

    @SerializedName("status")
    @Expose
    var status = 0

    @SerializedName("salesreport")
    @Expose
    var salesreport: List<SalesReport>? = null

    @SerializedName("totalsales")
    @Expose
    var totalsales: TotalSales? = null

    @SerializedName("totalsalescost")
    @Expose
    var totalsalescost: TotalCost? = null

    @SerializedName("totalprofit")
    @Expose
    var totalprofit: TotalProfit? = null

    @SerializedName("totalbalance")
    @Expose
    var totalbalance: TotalSalesBalance? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}