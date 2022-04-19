package com.denkiri.pharmacy.adapters

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.utils.OnCategoryItemClick
import com.denkiri.pharmacy.utils.OnSupplierItemClick
import java.lang.ref.WeakReference

class SupplierViewHolder (type: Int,itemView: View, listener: OnSupplierItemClick): RecyclerView.ViewHolder(itemView), View.OnClickListener {
    override fun onClick(v: View?) {
        if ( v == edit  ) {
            listenerWeakReference.get()?.edit(adapterPosition)
        }
        if ( v == dial  ) {
            listenerWeakReference.get()?.dial(adapterPosition)
        }
        if (v==delete){
            listenerWeakReference.get()?.delete(adapterPosition)

        }
        if(v==view){
            listenerWeakReference.get()?.view(adapterPosition)
        }
        if (v == enable) {
            listenerWeakReference.get()?.enable(adapterPosition)
        }
        if (v == disable) {
            listenerWeakReference.get()?.disable(adapterPosition)
        }
        if (v == invoice) {
            listenerWeakReference.get()?.onClickListener(adapterPosition)
        }
    }
    private val listenerWeakReference: WeakReference<OnSupplierItemClick> = WeakReference(listener)
    var itemVew:View
    lateinit var edit: ImageView
    lateinit var dial: ImageView
    lateinit var delete:ImageView
    lateinit var invoice:Button
    lateinit var view:ImageView
    var status:TextView
    var statusB:TextView
    var enable: Button
    var disable: Button
    var supplier_title: TextView
    var person:TextView
    var address:TextView
    var number:TextView

    init {
        this.itemVew=itemView
        supplier_title=itemView.findViewById(R.id.supplier_title)
        person=itemView.findViewById(R.id.person)
        address=itemView.findViewById(R.id.address)
        number=itemView.findViewById(R.id.number)
        status=itemView.findViewById(R.id.status)
        statusB=itemView.findViewById(R.id.status2)
        enable=itemView.findViewById(R.id.enable)
        disable=itemView.findViewById(R.id.disable)
        view=itemView.findViewById(R.id.view)
        invoice=itemView.findViewById(R.id.invoice)
        if (type==0) {
            edit =itemView.findViewById(R.id.edit)
            dial=itemView.findViewById(R.id.dial)
            delete=itemView.findViewById(R.id.delete)
            edit.setOnClickListener(this)
            dial.setOnClickListener(this)
            delete.setOnClickListener(this)
            enable=itemView.findViewById(R.id.enable)
            enable.setOnClickListener(this)
            disable=itemView.findViewById(R.id.disable)
            disable.setOnClickListener(this)
            view.setOnClickListener(this)
            invoice=itemView.findViewById(R.id.invoice)
            invoice.setOnClickListener(this)
        }

    }

}