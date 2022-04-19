package com.denkiri.pharmacy.adapters

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.cardview.widget.CardView
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.utils.OnCustomerItemClick
import java.lang.ref.WeakReference

class UserItemViewHolder (type: Int, itemView: View, listener: OnCustomerItemClick): RecyclerView.ViewHolder(itemView),
    View.OnClickListener {
    override fun onClick(p0: View?) {
        if ( p0== edit  ) {
            listenerWeakReference.get()?.edit(adapterPosition)
        }
        if (p0 == delete) {
            listenerWeakReference.get()?.delete(adapterPosition)
        }
        if (p0 == cardView) {
            listenerWeakReference.get()?.onClickListener(adapterPosition)
        }
    }
    private val listenerWeakReference: WeakReference<OnCustomerItemClick> = WeakReference(listener)
    var itemVew: View
    var name: TextView
    lateinit var cardView:CardView
    lateinit var edit: ImageView
    lateinit var delete: ImageView

    init {
        this.itemVew=itemView
        name=itemView.findViewById(R.id.name)
        if (type==0) {
            cardView=itemView.findViewById(R.id.card_view)
            cardView.setOnClickListener(this)
            delete = itemView.findViewById(R.id.delete)
            delete.setOnClickListener(this)
            edit =itemView.findViewById(R.id.edit)
            edit.setOnClickListener(this)
        }
    }
}