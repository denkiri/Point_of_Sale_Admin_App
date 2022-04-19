package com.denkiri.pharmacy.models.order
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class PurchaseOrderData {
    @SerializedName("error")
    @Expose
    var error: Boolean? = null

    @SerializedName("status")
    @Expose
    var status: Int? = null

    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("purchaseorderlist")
    @Expose
    var purchaseorderlist: List<PurchaseOrder>? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}