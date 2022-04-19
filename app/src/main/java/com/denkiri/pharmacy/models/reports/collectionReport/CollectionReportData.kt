package com.denkiri.pharmacy.models.reports.collectionReport

import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName




class CollectionReportData {
    @SerializedName("error")
    @Expose
    var error = false

    @SerializedName("status")
    @Expose
    var status = 0

    @SerializedName("collectionreport")
    @Expose
    var collectionreport: List<CollectionReport>? = null

    @SerializedName("totalcollection")
    @Expose
    var totalcollection: TotalCollection? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}