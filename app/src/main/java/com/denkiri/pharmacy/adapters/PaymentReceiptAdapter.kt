package com.denkiri.pharmacy.adapters
import android.content.Context
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.RecyclerView
import com.denkiri.pharmacy.R
import com.denkiri.pharmacy.models.receipt.Receipt
import com.denkiri.pharmacy.utils.OnCategoryClick
class PaymentReceiptAdapter(private val type: Int, private  val context: Context, private  var modelList: List<Receipt>?, private val recyclerListener: OnCategoryClick): RecyclerView.Adapter<PaymentReceiptViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): PaymentReceiptViewHolder {
        var itemView: View? =null
        itemView = LayoutInflater.from(parent.context).inflate(R.layout.payment_receipt,parent,false)
        return PaymentReceiptViewHolder(type,itemView!!,recyclerListener)
    }
    override fun onBindViewHolder(holder: PaymentReceiptViewHolder, position: Int) {
        val model= modelList!![position]
        if(model.paymentStatus==2){
            holder.receipt.visibility= View.VISIBLE
        }
        if(model.paymentStatus==1){
            holder.receipt.visibility= View.INVISIBLE
        }
        holder.businessCode.text =model.businessCode
        holder.invoiceNumber.text =model.invoiceNumber
        holder.date.text =model.date
        holder.amountValue.text =model.amount
        holder.codeValue.text =model.mpesaReceiptNumber
        holder.phoneValue.text =model.phoneNumber
        holder.detailsValue.text =model.paymentDetails

    }
    override fun getItemCount(): Int {
        return  if (null!= modelList)modelList!!.size else 0
    }
    fun refresh(modelList: List<Receipt>?){
        this.modelList =modelList
        notifyDataSetChanged()
    }
}