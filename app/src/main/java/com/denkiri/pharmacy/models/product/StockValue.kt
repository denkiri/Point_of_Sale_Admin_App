package com.denkiri.pharmacy.models.product
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Entity(
        indices = [(Index("stockValue"))],

        primaryKeys = ["stockValue"]
)
class StockValue {
    @SerializedName("stockValue")
    @Expose
    var stockValue:Double=0.0
    @Ignore
    constructor(stockValue: Double) {
        this.stockValue =stockValue
    }
    constructor()
}