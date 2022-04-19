package com.denkiri.pharmacy.models.order
import androidx.room.Entity
import androidx.room.Index
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
@Entity(
        indices = [(Index("transactionId"))],
        primaryKeys = ["transactionId"]
)
class Order {
    @field:SerializedName("id")
    @Expose
    var transactionId : Int?= 0
    @field:SerializedName("product_name")
    @Expose
    var product: String? = null
    @field:SerializedName("product_code")
    @Expose
    var code: String? = null
    @field:SerializedName("cost")
    @Expose
    var price: Double= 0.00
    @field:SerializedName("qty")
    @Expose
    var qty: String? = null
    @field:SerializedName("total_cost")
    @Expose
    var totalCost: Double= 0.00
    @field:SerializedName("discount")
    @Expose
    var discount: Double= 0.00
    @field:SerializedName("tax")
    @Expose
    var vat:Double= 0.00
}