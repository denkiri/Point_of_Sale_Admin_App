package com.denkiri.pharmacy.models.reports.salesReport

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName


@Entity(
        indices = [(Index("totalsales"))],
        primaryKeys = ["totalsales"]
)

class TotalSales {
    @SerializedName("totalsales")
    @Expose
    var totalsales: Double = 0.0
    @Ignore
    constructor(totalsales: Double){
        this.totalsales = totalsales
    }
    constructor()
}