package com.denkiri.pharmacy.models.product
import androidx.room.Entity
import androidx.room.Ignore
import androidx.room.Index
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Entity(
        indices = [(Index("itemsTotal"))],

        primaryKeys = ["itemsTotal"]
)
class Items {
    @SerializedName("items_total")
    @Expose
    var itemsTotal: Int? =0
    @Ignore
    constructor(items: Int) {
        this.itemsTotal = items
    }
    constructor()
}