package com.denkiri.pharmacy.models.receipt
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class ReceiptData {
    @SerializedName("error")
    @Expose
    var error: Boolean? = null

    @SerializedName("status")
    @Expose
    var status: Int? = null

    @SerializedName("success")
    @Expose
    var success: Boolean? = null

    @SerializedName("receipts")
    @Expose
    var receipts: List<Receipt>? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}