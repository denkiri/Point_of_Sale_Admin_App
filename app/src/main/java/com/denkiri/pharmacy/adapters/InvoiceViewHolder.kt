package com.denkiri.pharmacy.adapters

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.utils.OnCategoryItemClick
import java.lang.ref.WeakReference

class InvoiceViewHolder (type: Int, itemView: View, listener: OnCategoryItemClick): RecyclerView.ViewHolder(itemView), View.OnClickListener {
    private val listenerWeakReference: WeakReference<OnCategoryItemClick> = WeakReference(listener)
    var itemVew:View
    var invoiceNumber: TextView
    lateinit var dial: ImageView
    var date: TextView
    var name: TextView
    var address: TextView
    var contact: TextView
    var due: TextView
    var balance: TextView
    lateinit var payment: Button
    override fun onClick(v: View?) {
        if ( v == payment  ) {
            listenerWeakReference.get()?.edit(adapterPosition)
        }
        if ( v == dial  ) {
            listenerWeakReference.get()?.dial(adapterPosition)
        }

    }
    init {
        this.itemVew=itemView
        invoiceNumber=itemView.findViewById(R.id.invoiceNumber)

        date=itemView.findViewById(R.id.date)
        name=itemView.findViewById(R.id.name)
        address=itemView.findViewById(R.id.address)
        contact=itemView.findViewById(R.id.contact)
        due=itemView.findViewById(R.id.due)
        balance=itemView.findViewById(R.id.balance)
        if (type==0) {
            payment =itemView.findViewById(R.id.payment)
            payment.setOnClickListener(this)
            dial=itemView.findViewById(R.id.dial)
            dial.setOnClickListener(this)
        }

    }

}