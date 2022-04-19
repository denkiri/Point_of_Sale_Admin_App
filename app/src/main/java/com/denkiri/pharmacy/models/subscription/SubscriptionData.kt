package com.denkiri.pharmacy.models.subscription
import androidx.room.Ignore
import com.denkiri.pharmacy.models.oauth.Profile
import com.google.gson.annotations.Expose
import com.google.gson.annotations.SerializedName
class SubscriptionData {
    @SerializedName("error")
    @Expose
    var error: Boolean? = null

    @SerializedName("status")
    @Expose
    var status: Int? = null

    @SerializedName("subscription")
    @Expose
    var subscription: Subscription? = null

    @SerializedName("message")
    @Expose
    var message: String? = null
    @Ignore
    constructor(subscription: Subscription?) {
        this.subscription = subscription
    }
    constructor()
}