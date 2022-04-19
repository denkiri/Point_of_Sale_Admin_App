package com.denkiri.pharmacy.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R

class ExpiryItemViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
    var itemVew:View
    var date: TextView
    var amountLose:TextView
    var costValue:TextView
    var quantity:TextView
    var code:TextView
    var category:TextView
    var productName:TextView
    var description:TextView
    init {
        this.itemVew=itemView
        code=itemView.findViewById(R.id.code)
        description=itemView.findViewById(R.id.description)
        productName=itemView.findViewById(R.id.productName)
        category=itemView.findViewById(R.id.category)
        date=itemView.findViewById(R.id.date)
        amountLose=itemView.findViewById(R.id.amount)
        costValue=itemView.findViewById(R.id.costValue)
        quantity=itemView.findViewById(R.id.quantity)

    }
}