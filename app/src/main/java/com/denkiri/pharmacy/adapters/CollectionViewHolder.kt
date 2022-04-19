package com.denkiri.pharmacy.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R

class CollectionViewHolder(itemView: View): RecyclerView.ViewHolder(itemView) {
    var itemVew:View
    var invoiceNumber: TextView
    var date:TextView
    var collectionDate:TextView
    var customerName:TextView
    var amountPaid:TextView
    var balance:TextView
    var remarks:TextView

    init {
        this.itemVew=itemView
        invoiceNumber=itemView.findViewById(R.id.invoiceNumber)
        date=itemView.findViewById(R.id.date)
        collectionDate=itemView.findViewById(R.id.date3)
        customerName=itemView.findViewById(R.id.customerName)
        amountPaid=itemView.findViewById(R.id.amountPaid)
        balance=itemView.findViewById(R.id.balance)
        remarks=itemView.findViewById(R.id.remarks)
    }
}