package com.denkiri.pharmacy.models.reports.expiredproducts

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName

@Entity(
        indices = [(Index("totalLose"))],
        primaryKeys = ["totalLose"]
)
class TotalLose {
    @SerializedName("totallose")
    @Expose
    var totalLose: Double = 0.0
    @Ignore
    constructor(totalLose: Double){
        this.totalLose = totalLose
    }
    constructor()
}