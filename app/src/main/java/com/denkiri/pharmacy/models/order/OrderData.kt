package com.denkiri.pharmacy.models.order
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class OrderData {
 @SerializedName("error")
 @Expose
 var error = false
 @SerializedName("status")
 @Expose
 var status = 0
 @SerializedName("purchases")
 @Expose
 var order: List<Order>? = null
 @SerializedName("totalAmount")
 @Expose
 var totalAmount: TotalInvoiceAmount? = null

 @SerializedName("message")
 @Expose
 var message: String? = null
}