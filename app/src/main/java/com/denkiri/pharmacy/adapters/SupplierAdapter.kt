package com.denkiri.pharmacy.adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.supplier.Supplier
import com.denkiri.pharmacy.utils.OnCategoryItemClick
import com.denkiri.pharmacy.utils.OnSupplierItemClick

class SupplierAdapter (private val type: Int,private  val context: Context, private  var modelList: List<Supplier>?, private val recyclerListener: OnSupplierItemClick): RecyclerView.Adapter<SupplierViewHolder>()  {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupplierViewHolder {
        var itemView: View? =null
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.supplier_item,parent,false)
        return SupplierViewHolder(type,itemView!!,recyclerListener)
    }
    override fun onBindViewHolder(holder: SupplierViewHolder, position: Int) {
        val model= modelList!![position]
        holder.supplier_title.text =model.suplierName
        holder.person.text=model.contactPerson
        holder.address.text=model.suplierAddress
        holder.number.text=model.suplierContact
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
    fun refresh(modelList: List<Supplier>?){
        this.modelList =modelList
        notifyDataSetChanged()
    }
}