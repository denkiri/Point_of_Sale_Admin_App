package com.denkiri.pharmacy.models.reports.salesReport
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
@Entity(
        indices = [(Index("balance"))],
        primaryKeys = ["balance"]
)
class TotalSalesBalance {
    @SerializedName("balance")
    @Expose
    var balance: Double = 0.0
    @Ignore
    constructor(balance: Double){
        this.balance = balance
    }
    constructor()
}