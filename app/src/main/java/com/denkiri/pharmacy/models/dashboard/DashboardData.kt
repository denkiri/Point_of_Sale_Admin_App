package com.denkiri.pharmacy.models.dashboard

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


class DashboardData {
    @SerializedName("error")
    @Expose
    var error: Boolean? = null

    @SerializedName("status")
    @Expose
    var status: Int? = null

    @SerializedName("daySale")
    @Expose
    var daySale: DaySale? = null

    @SerializedName("dayCost")
    @Expose
    var dayCost: DayCost? = null

    @SerializedName("dayProfit")
    @Expose
    var dayProfit: DayProfit? = null

    @SerializedName("monthSale")
    @Expose
    var monthSale: MonthSale? = null

    @SerializedName("monthCost")
    @Expose
    var monthCost: MonthCost? = null

    @SerializedName("monthProfit")
    @Expose
    var monthProfit: MonthProfit? = null
    @SerializedName("lastmonthProfit")
    @Expose
    var lastMonthProfit: LastMonthProfit? = null

    @SerializedName("yearSale")
    @Expose
    var yearSale: YearSale? = null

    @SerializedName("yearCost")
    @Expose
    var yearCost: YearCost? = null

    @SerializedName("yearProfit")
    @Expose
    var yearProfit: YearProfit? = null

    @SerializedName("reOrder")
    @Expose
    var reOrder : Int? = null

    @SerializedName("creditDue")
    @Expose
    var creditDue : Int? = null

    @SerializedName("expiry")
    @Expose
    var expiry : Int? = null
    @SerializedName("vat")
    @Expose
    var vat: Vat? = null
    @SerializedName("message")
    @Expose
    var message: String? = null
}