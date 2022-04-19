package com.denkiri.pharmacy.adapters

import android.view.View
import android.widget.Button
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.utils.OnReportClick
import java.lang.ref.WeakReference

class OrderViewHolder (type: Int, itemView: View, listener: OnReportClick): RecyclerView.ViewHolder(itemView), View.OnClickListener {
    private val listenerWeakReference: WeakReference<OnReportClick> = WeakReference(listener)
    var itemVew: View
    var supplierName: TextView
    var invoiceNumber: TextView
    var date: TextView
    var dueDate:TextView
   // var cost: TextView
    var status: TextView
    var receipt: Button
    var payButton:Button
    var cardView: CardView
    var card:CardView
    init {
        this.itemVew=itemView
        cardView=itemView.findViewById(R.id.card_view)
        card=itemView.findViewById(R.id.card)
        supplierName=itemView.findViewById(R.id.supplierName)
        invoiceNumber=itemView.findViewById(R.id.invoiceNumber)
        date=itemView.findViewById(R.id.date)
        dueDate=itemView.findViewById(R.id.dueDate)
       // cost=itemView.findViewById(R.id.cost)
        status=itemView.findViewById(R.id.status)
        receipt=itemView.findViewById(R.id.receipt)
        payButton=itemView.findViewById(R.id.payButton)


        if (type==0) {
            receipt = itemView.findViewById(R.id.receipt)
            receipt.setOnClickListener(this)
            payButton.setOnClickListener(this)

        }
    }

    override fun onClick(v: View?) {
        if (v == receipt) {
            listenerWeakReference.get()?.onClickListener(adapterPosition)
        }
        if (v == payButton) {
            listenerWeakReference.get()?.selected(adapterPosition)
        }
    }

}