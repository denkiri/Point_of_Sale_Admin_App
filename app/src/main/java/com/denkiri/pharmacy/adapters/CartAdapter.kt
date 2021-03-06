package com.denkiri.pharmacy.adapters

import android.annotation.SuppressLint
import android.content.Context
import android.graphics.Color
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Filter
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.order.Order
import java.text.NumberFormat
import java.util.*


class CartAdapter (
        private val type: Int,
        private val context: Context,
        private var modelList: ArrayList<Order>?,
        private val recyclerListener: OnCartItemClick
): RecyclerView.Adapter<CartViewHolder>()  {
    var filterList = ArrayList<Order>()
    init {
        filterList= modelList!!
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CartViewHolder {
        var itemView: View? =null
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.cart_item, parent, false)
        return CartViewHolder(type, itemView!!, recyclerListener)
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: CartViewHolder, position: Int) {
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.currency = Currency.getInstance("KSh")
        val model= modelList!![position]
        holder.productName.text = model.product
        holder.code.text=model.code
        holder.priceValue.text=format.format(model.price).toString()
        holder.vatValue.text=format.format(model.vat).toString()
        holder.discountValue.text=format.format(model.discount).toString()
        holder.amountValue.text=format.format(model.totalCost).toString()
        holder.totalAmountValue.text=format.format(model.totalCost).toString()
        holder.quantityValue.text=model.qty
    }
    override fun getItemCount(): Int {
        return  if (null!= modelList)modelList!!.size else 0
    }
    fun refresh(modelList: ArrayList<Order>?){
        this.modelList =modelList
        notifyDataSetChanged()
    }

    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                if (charString.isEmpty()) filterList = modelList!! else {
                    var filteredList = ArrayList<Order>()
                    modelList
                            ?.filter { it.product!!.toLowerCase(Locale.ROOT).contains(
                                    charString.toLowerCase(
                                            Locale.ROOT
                                    )
                            )
                            }
                            ?.forEach { filteredList.add(it) }
                    filteredList = modelList!!
                }
                return FilterResults().apply { values = filterList }
            }


            @Suppress("UNCHECKED_CAST")
            override fun publishResults(constraint: CharSequence?, results: FilterResults?) {
                filterList = results?.values as ArrayList<Order>
                notifyDataSetChanged()
            }

        }
    }
    fun filterList(filteredProductList: ArrayList<Order>) {

        this.modelList = filteredProductList
        notifyDataSetChanged()
    }

}