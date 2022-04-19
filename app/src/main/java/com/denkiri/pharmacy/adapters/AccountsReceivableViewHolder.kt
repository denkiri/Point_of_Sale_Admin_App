package com.denkiri.pharmacy.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R

class AccountsReceivableViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    var itemVew:View
    var invoiceNumber: TextView
    var date:TextView
    var date3:TextView
    var customerName:TextView
    var balance:TextView
    init {
        this.itemVew=itemView
        invoiceNumber=itemView.findViewById(R.id.invoiceNumber)
        date=itemView.findViewById(R.id.date)
        date3=itemView.findViewById(R.id.date3)
        customerName=itemView.findViewById(R.id.customerName)
        balance=itemView.findViewById(R.id.balance)
    }

}