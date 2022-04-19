package com.denkiri.pharmacy.adapters

import android.view.View
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R

class InventoryViewHolder (itemView: View): RecyclerView.ViewHolder(itemView) {
    var itemVew:View
    var code: TextView
    var category:TextView
    var invoiceNumber:TextView
    var productName:TextView
    var description:TextView
    var quantityStart:TextView
    var quantitySold:TextView
    var quantityEnd:TextView
    var priceValue:TextView
    var costValue:TextView
    init {
        this.itemVew=itemView
        code=itemView.findViewById(R.id.code)
        category=itemView.findViewById(R.id.category)
        invoiceNumber=itemView.findViewById(R.id.invoiceNumber)
        productName=itemView.findViewById(R.id.productName)
        description=itemView.findViewById(R.id.description)
        quantityStart=itemView.findViewById(R.id.quantityStart)
        quantitySold=itemView.findViewById(R.id.quantitySold)
        quantityEnd=itemView.findViewById(R.id.quantityEnd)
        priceValue=itemView.findViewById(R.id.priceValue)
        costValue=itemView.findViewById(R.id.costValue)

    }
}