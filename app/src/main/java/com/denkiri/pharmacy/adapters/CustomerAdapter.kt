package com.denkiri.pharmacy.adapters

import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.customer.Customer
import com.denkiri.pharmacy.models.product.Product
import com.denkiri.pharmacy.utils.OnCategoryItemClick
import com.denkiri.pharmacy.utils.OnCustomerItemClick
import java.util.ArrayList

class CustomerAdapter  (private val type: Int, private  val context: Context, private  var modelList: List<Customer>?, private val recyclerListener: OnCustomerItemClick):
    RecyclerView.Adapter<CustomerItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): CustomerItemViewHolder {
        var itemView: View? =null
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.customer_item,parent,false)
        return CustomerItemViewHolder(type,itemView!!,recyclerListener)
    }
    override fun onBindViewHolder(holder: CustomerItemViewHolder, position: Int) {
        val model= modelList!![position]
        holder.customerName.text =model.customerName
        holder.addressValue.text=model.address
        holder.contactValue.text=model.contact
        if(model.status==1){
            holder.status.visibility=View.VISIBLE
            holder.disable.visibility=View.VISIBLE
            holder.statusB.visibility=View.INVISIBLE
            holder.enable.visibility=View.INVISIBLE

        }
        else{
            holder.statusB.visibility=View.VISIBLE
            holder.enable.visibility=View.VISIBLE
            holder.status.visibility=View.INVISIBLE
            holder.disable.visibility=View.INVISIBLE
        }
    }
    override fun getItemCount(): Int {
        return  if (null!= modelList)modelList!!.size else 0
    }
    fun refresh(modelList: List<Customer>?){
        this.modelList =modelList
        notifyDataSetChanged()
    }
    fun filterList(filteredCustomerList: ArrayList<Customer>) {
        this.modelList = filteredCustomerList
        notifyDataSetChanged()
    }
}