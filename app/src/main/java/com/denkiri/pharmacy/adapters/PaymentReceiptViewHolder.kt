package com.denkiri.pharmacy.adapters

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.utils.OnCategoryClick
import java.lang.ref.WeakReference

class PaymentReceiptViewHolder(type: Int, itemView: View, listener: OnCategoryClick): RecyclerView.ViewHolder(itemView), View.OnClickListener {
    override fun onClick(v: View?) {
        if ( v == receipt ) {
            listenerWeakReference.get()?.selected(adapterPosition)
        }
    }
    private val listenerWeakReference: WeakReference<OnCategoryClick> = WeakReference(listener)
    var itemVew: View
    var businessCode:TextView
    var invoiceNumber:TextView
    var date:TextView
    var amountValue:TextView
    var codeValue:TextView
    var phoneValue:TextView
    var detailsValue:TextView
    var receipt: Button
    init {
        this.itemVew=itemView
        receipt=itemView.findViewById(R.id.receipt)
        businessCode=itemView.findViewById(R.id.businessCode)
        invoiceNumber=itemView.findViewById(R.id.invoiceNumber)
        date=itemView.findViewById(R.id.date)
        amountValue=itemView.findViewById(R.id.amountValue)
        codeValue=itemView.findViewById(R.id.codeValue)
        phoneValue=itemView.findViewById(R.id.phoneValue)
        detailsValue=itemView.findViewById(R.id.detailsValue)
        if (type==0) {
            receipt.setOnClickListener(this)
        }
    }
}