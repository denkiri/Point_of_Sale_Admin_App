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
import com.denkiri.pharmacy.models.product.Product
import com.denkiri.pharmacy.utils.OnCategoryItemClick
import java.text.NumberFormat
import java.util.*

class ReOrderAdapter  (private val type: Int, private  val context: Context, private  var modelList: ArrayList<Product>?, private val recyclerListener: OnCategoryItemClick): RecyclerView.Adapter<ProductViewHolder>()  {
    var filterList = ArrayList<Product>()
    init {
        filterList= modelList!!
    }
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ProductViewHolder {
        var itemView: View? =null
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.product_item,parent,false)
        return ProductViewHolder(type,itemView!!,recyclerListener)
    }
    @SuppressLint("SetTextI18n")
    override fun onBindViewHolder(holder: ProductViewHolder, position: Int) {
        val format: NumberFormat = NumberFormat.getCurrencyInstance()
        format.currency = Currency.getInstance("KSh")
        val model= modelList!![position]
        holder.delete.visibility= View.INVISIBLE
        holder.edit.visibility= View.INVISIBLE

        holder.productName.text = model.productName
        holder.quantity_left.text= model.qtyLeft.toString()
        holder.price.text=format.format(model.price).toString()
        holder.cost.text=format.format(model.cost).toString()
        holder.unit.text=model.unit
        holder.supplier.text=model.suplierName
        holder.category.text=model.category
        holder.code.text=model.productCode
        holder.reOrder.text=model.reorder.toString()
        holder.expiryDate.text=model.expirationDate
        holder.brand.text=model.brand
        if (model.reorder!=null && model.qtyLeft!=null) {
            if (model.reorder!! >= model.qtyLeft!!){
                holder.quantity_left.setTextColor(Color.parseColor("#ffcc0000"))
                holder.quantity.setTextColor(Color.parseColor("#ffcc0000"))
            }

        }
    }
    override fun getItemCount(): Int {
        return  if (null!= modelList)modelList!!.size else 0
    }
    fun refresh(modelList: ArrayList<Product>?){
        this.modelList =modelList
        notifyDataSetChanged()
    }

    fun getFilter(): Filter {
        return object : Filter() {
            override fun performFiltering(constraint: CharSequence?): FilterResults {
                val charString = constraint.toString()
                if (charString.isEmpty()) filterList = modelList!! else {
                    var filteredList = ArrayList<Product>()
                    modelList
                        ?.filter { it.productName!!.toLowerCase(Locale.ROOT).contains(charString.toLowerCase(
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
                filterList = results?.values as ArrayList<Product>
                notifyDataSetChanged()
            }

        }
    }
    fun filterList(filteredProductList: ArrayList<Product>) {

        this.modelList = filteredProductList
        notifyDataSetChanged()
    }

}