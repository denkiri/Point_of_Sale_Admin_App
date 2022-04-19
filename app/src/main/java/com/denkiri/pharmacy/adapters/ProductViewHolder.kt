package com.denkiri.pharmacy.adapters

import android.view.View
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.utils.OnCategoryItemClick
import java.lang.ref.WeakReference

class ProductViewHolder (type: Int, itemView: View, listener: OnCategoryItemClick): RecyclerView.ViewHolder(itemView), View.OnClickListener{
    private val listenerWeakReference: WeakReference<OnCategoryItemClick> = WeakReference(listener)
    var itemVew:View
    lateinit var edit: ImageView
    lateinit var delete:ImageView
    lateinit var addNewProduct:ImageView
    var quantity_left: TextView
    var productName: TextView
    var price: TextView
    var cost: TextView
    var unit: TextView
    var supplier: TextView
    var category: TextView
    var code: TextView
    var expiryDate:TextView
    var reOrder:TextView
    var quantity:TextView
    var pullOut:Button
    var brand:TextView

    override fun onClick(v: View?) {
        if ( v == edit  ) {
            listenerWeakReference.get()?.edit(adapterPosition)
        }
        if ( v == pullOut) {
            listenerWeakReference.get()?.delete(adapterPosition)
        }
        if ( v == addNewProduct) {
            listenerWeakReference.get()?.onClickListener(adapterPosition)
        }

    }
    init {
        this.itemVew=itemView
        quantity_left=itemView.findViewById(R.id.quantityLeft)
        productName=itemView.findViewById(R.id.productName)
        price=itemView.findViewById(R.id.priceValue)
        cost=itemView.findViewById(R.id.costValue)
        unit=itemView.findViewById(R.id.unit)
        supplier=itemView.findViewById(R.id.supplier)
        category=itemView.findViewById(R.id.category)
        code=itemView.findViewById(R.id.code)
        expiryDate=itemView.findViewById(R.id.expiryDate)
        reOrder=itemView.findViewById(R.id.Reorder)
        addNewProduct=itemView.findViewById(R.id.addNewProduct)
        quantity=itemView.findViewById(R.id.quantity)
        pullOut=itemView.findViewById(R.id.pullOut)
        brand=itemView.findViewById(R.id.brand)
        if (type==0) {
            delete=itemView.findViewById(R.id.delete)
            edit =itemView.findViewById(R.id.edit)
            pullOut.setOnClickListener(this)
            addNewProduct.setOnClickListener(this)
            delete.setOnClickListener(this)
            edit.setOnClickListener(this)
        }

    }


}