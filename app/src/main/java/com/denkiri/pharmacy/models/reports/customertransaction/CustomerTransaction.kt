package com.denkiri.pharmacy.models.reports.customertransaction

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class CustomerTransaction {
    @SerializedName("customer_id")
    @Expose
    var customerId:Int? = 0

    @SerializedName("customer_name")
    @Expose
    var customerName: String? = null

    @SerializedName("address")
    @Expose
    var address: String? = null

    @SerializedName("contact")
    @Expose
    var contact: String? = null

    @SerializedName("membership_number")
    @Expose
    var membershipNumber: String? = null

    @SerializedName("first_name")
    @Expose
    var firstName: String? = null

    @SerializedName("middle_name")
    @Expose
    var middleName: String? = null

    @SerializedName("last_name")
    @Expose
    var lastName: String? = null
}