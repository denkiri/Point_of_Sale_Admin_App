package com.denkiri.pharmacy.adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.supplier.Supplier
import com.denkiri.pharmacy.utils.OnSupplierClick
class SupplierItemAdapter (private val type: Int, private  val context: Context, private  var modelList: List<Supplier>?, private val recyclerListener: OnSupplierClick): RecyclerView.Adapter<SupplierItemViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): SupplierItemViewHolder {
        var itemView: View? =null
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.select_surpplier_item,parent,false)
        return SupplierItemViewHolder(type,itemView!!,recyclerListener)
    }
    override fun onBindViewHolder(holder: SupplierItemViewHolder, position: Int) {
        val model= modelList!![position]
        holder.supplier_title.text =model.suplierName

    }
    override fun getItemCount(): Int {
        return  if (null!= modelList)modelList!!.size else 0
    }
    fun refresh(modelList: List<Supplier>?){
        this.modelList =modelList
        notifyDataSetChanged()
    }
}