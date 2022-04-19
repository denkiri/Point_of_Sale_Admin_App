package com.denkiri.pharmacy.models.order
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class PurchaseOrder {
    @SerializedName("transaction_id")
    @Expose
    var transactionId: Int? = null

    @SerializedName("invoice_number")
    @Expose
    var invoiceNumber: String? = null

    @SerializedName("date_order")
    @Expose
    var dateOrder: String? = null

    @SerializedName("suplier")
    @Expose
    var suplier: String? = null

    @SerializedName("date_deliver")
    @Expose
    var dateDeliver: String? = null

    @SerializedName("p_name")
    @Expose
    var pName: String? = null

    @SerializedName("qty")
    @Expose
    var qty: String? = null

    @SerializedName("cost")
    @Expose
    var cost: Double = 0.00

    @SerializedName("payment_status")
    @Expose
    var paymentStatus: String? = null

    @SerializedName("remark")
    @Expose
    var remark: String? = null

    @SerializedName("receipt_number")
    @Expose
    var receiptNumber: String? = null

    @SerializedName("due_date")
    @Expose
    var dueDate: String? = null

    @SerializedName("suplier_id")
    @Expose
    var suplierId: Int? = null

    @SerializedName("suplier_name")
    @Expose
    var suplierName: String? = null

    @SerializedName("suplier_address")
    @Expose
    var suplierAddress: String? = null

    @SerializedName("suplier_contact")
    @Expose
    var suplierContact: String? = null

    @SerializedName("contact_person")
    @Expose
    var contactPerson: String? = null
}