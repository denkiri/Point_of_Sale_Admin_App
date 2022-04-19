package com.denkiri.pharmacy.models.dashboard
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import androidx.room.PrimaryKey
import com.google.gson.annotations.Expose

import com.google.gson.annotations.SerializedName
@Entity
class DayCost {
    @PrimaryKey()
    @SerializedName("day_cost")
    @Expose
    var dayCost: Double = 0.0
    @Ignore
    constructor(daycost: Double) {
        this.dayCost = daycost
    }
    constructor()

}