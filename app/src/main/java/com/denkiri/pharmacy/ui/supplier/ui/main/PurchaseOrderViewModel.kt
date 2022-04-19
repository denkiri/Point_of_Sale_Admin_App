package com.denkiri.pharmacy.ui.supplier.ui.main

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.MediatorLiveData
import com.denkiri.pharmacy.models.custom.Resource
import com.denkiri.pharmacy.models.order.*
import com.denkiri.pharmacy.models.product.ProductData
import com.denkiri.pharmacy.storage.PreferenceManager
import com.denkiri.pharmacy.storage.repository.*
import kotlin.random.Random
class PurchaseOrderViewModel (application: Application) : AndroidViewModel(application) {
    private val preferenceManager: PreferenceManager = PreferenceManager(application)
    internal var purchaseOrderFormRepository: PurchaseOrderFormRepository
    internal var productsRepository: ProductsRepository
    internal var orderActionRepository: OrderActionRepository
    internal var purchaseRepository: PurchaseRepository
    internal val orderRepository:OrderRepository
    private val orderActionObservable= MediatorLiveData<Resource<OrderData>>()
    private val productsObservable = MediatorLiveData<Resource<ProductData>>()
    private val purchaseObservable=MediatorLiveData<Resource<PurchaseOrderData>>()
    private val orderObservable=MediatorLiveData<Resource<OrderData>>()
    private val offlineOrdersObservable = MediatorLiveData<Resource<List<Order>>>()
init {
    purchaseOrderFormRepository= PurchaseOrderFormRepository(application)
    productsRepository = ProductsRepository(application)
    orderActionRepository= OrderActionRepository(application)
    purchaseRepository= PurchaseRepository(application)
    orderRepository= OrderRepository(application)
    productsObservable.addSource(productsRepository.productObservable) { data -> productsObservable.setValue(data) }
    orderActionObservable.addSource(orderActionRepository.orderActionObservable) { data -> orderActionObservable.setValue(data) }
    purchaseObservable.addSource(purchaseRepository.purchaseListObservable){ data ->purchaseObservable.setValue(data) }
    orderObservable.addSource(orderRepository.orderObservable){ data ->orderObservable.setValue(data) }
    offlineOrdersObservable.addSource(orderRepository.offlineOrdersObservable) { data -> offlineOrdersObservable.setValue(data) }

}
    fun getSupplierProducts(supplier: String?){

        productsRepository.getSupplierProducts(supplier!!)

    }
    fun observeCartAction(): LiveData<Resource<OrderData>> {
        return orderActionObservable
    }
    fun observePurchasesOrder(): LiveData<Resource<PurchaseOrderData>> {
        return purchaseObservable
    }
    fun getTotalAmount(): LiveData<TotalInvoiceAmount> {
        return orderActionRepository.getTotalAmount()
    }
    fun getOrder(invoice:String?) {
        orderRepository.getOrder(invoice!!)
    }
    fun saveSupplierId(supplier:String?) {
        orderActionRepository.saveSupplierId(supplier!!)
    }
    fun getOfflineOrders() {
        orderRepository.getOfflineOrders()
    }
    fun observeAddOrderAction(): LiveData<Resource<OrderData>> {
        return orderActionObservable
    }
    fun observeProducts(): LiveData<Resource<ProductData>> {
        return productsObservable
    }
    fun getRandPassword(n: Int): String
    {
        val characterSet = "003232303232023232023456789ABCDEFGHIJKLMNOPQRSTUVWXYZ"

        val random = Random(System.nanoTime())
        val password = StringBuilder()

        for (i in 0 until n) {
            val rIndex = random.nextInt(characterSet.length)
            password.append(characterSet[rIndex])
        }

        return password.toString()
    }

    fun saveInvoiceNumber(invoice:String){
        purchaseOrderFormRepository.saveInvoiceNumber(invoice)
    }
    fun saveOrders(data: OrderData){
        orderActionRepository.saveOrder(data)

    }
    fun observeCartData(): LiveData<Resource<OrderData>> {
        return orderObservable
    }
    fun addOrder(price:String?,cost:String?,quantity:String?,expiryDate:String?,productCode:String?,invoice:String?,status:String?,vat:String?,discount:String?){
        if (quantity != null) {
            if (expiryDate != null) {
                if (productCode != null) {
                    if (invoice != null) {
                        if (status != null) {
                            if (vat != null) {
                                if (discount != null) {
                                    orderActionRepository.addOrder(price!!, cost!!, quantity, expiryDate, productCode, invoice, status, vat, discount)
                                }
                            }
                        }
                    }
                }
            }
        }
    }
    fun deleteCartItem(productId: String?,quantity: String?,productCode: String?,invoice:String?){
        orderActionRepository.deleteCart(productId!!, quantity!!,productCode!!,invoice!!)
    }
    fun getInvoiceItems(invoice:String){
        orderRepository.getOrder(invoice)
    }
    fun getPurchasesInvoice(){
        purchaseRepository.getItems()
    }
    fun savePurchasesInvoice(invoice:String?,supplier: String?,dateDelivered: String?,dueDate: String?,receiptNumber:String?, paymentStatus: String?){
        purchaseRepository.saveOrder(invoice!!, supplier!!, dateDelivered!!, dueDate!!, receiptNumber!!, paymentStatus!!)
    }

    fun completePayment(pid:String?){
        purchaseRepository.complete(pid!!)
    }
    fun saveTotal(data: OrderData){
        orderRepository.saveTotal(data)

    }
    fun observeOfflineOrders(): LiveData<Resource<List<Order>>> {
        return offlineOrdersObservable
    }

}