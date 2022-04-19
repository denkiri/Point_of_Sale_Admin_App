package com.denkiri.pharmacy.models.dashboard

import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Entity(
        indices = [(Index("vat"))],

        primaryKeys = ["vat"]
)
class Vat {
    @SerializedName("vat")
    @Expose
    var vat:Double = 0.0
    @Ignore
    constructor(vat: Double) {
        this.vat = vat
    }
    constructor()
}