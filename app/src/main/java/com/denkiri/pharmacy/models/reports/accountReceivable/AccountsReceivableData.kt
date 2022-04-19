package com.denkiri.pharmacy.models.reports.accountReceivable
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class AccountsReceivableData {
    @SerializedName("error")
    @Expose
    var error = false

    @SerializedName("status")
    @Expose
    var status = 0

    @SerializedName("accountreceivablereport")
    @Expose
    var accountreceivablereport: List<AccountsReceivable>? = null

    @SerializedName("totalbalance")
    @Expose
    var totalbalance: TotalBalance? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
}