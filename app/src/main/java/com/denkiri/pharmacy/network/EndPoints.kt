package com.denkiri.pharmacy.network

import com.denkiri.pharmacy.models.brand.Brand
import com.denkiri.pharmacy.models.brand.BrandData
import com.denkiri.pharmacy.models.cashier.UsersData
import com.denkiri.pharmacy.models.category.CategoryData
import com.denkiri.pharmacy.models.customer.CustomerData
import com.denkiri.pharmacy.models.dashboard.DashboardData
import com.denkiri.pharmacy.models.expense.*
import com.denkiri.pharmacy.models.invoice.InvoiceData
import com.denkiri.pharmacy.models.oauth.Oauth
import com.denkiri.pharmacy.models.order.OrderData
import com.denkiri.pharmacy.models.payment.CheckPaymentResponseData
import com.denkiri.pharmacy.models.payment.PaymentResponseData
import com.denkiri.pharmacy.models.product.ProductData
import com.denkiri.pharmacy.models.product.ProductReportData
import com.denkiri.pharmacy.models.purchases.PurchaseOrder
import com.denkiri.pharmacy.models.order.PurchaseOrderData
import com.denkiri.pharmacy.models.receipt.ReceiptData
import com.denkiri.pharmacy.models.reports.accountReceivable.AccountsReceivableData
import com.denkiri.pharmacy.models.reports.cashreport.CashReport
import com.denkiri.pharmacy.models.reports.cashreport.CashReportData
import com.denkiri.pharmacy.models.reports.collectionReport.CollectionReportData
import com.denkiri.pharmacy.models.reports.creditreport.CreditReportData
import com.denkiri.pharmacy.models.reports.customertransaction.CustomerTransactionData
import com.denkiri.pharmacy.models.reports.expiredproducts.ExpiryReportData
import com.denkiri.pharmacy.models.reports.incomeReport.CashierIncomeReportData
import com.denkiri.pharmacy.models.reports.inventoryreport.InventoryReportData
import com.denkiri.pharmacy.models.reports.mpesareport.MpesaReportData
import com.denkiri.pharmacy.models.reports.salesReport.CustomerTransactionsData
import com.denkiri.pharmacy.models.reports.salesReport.SalesReportData
import com.denkiri.pharmacy.models.subscription.SubscriptionData
import com.denkiri.pharmacy.models.supplier.SupplierData
import com.denkiri.pharmacy.ui.reports.InventoryReport
import com.denkiri.pharmacy.ui.reports.SalesReport
import retrofit2.Call
import retrofit2.http.*
interface EndPoints {
    @FormUrlEncoded
    @POST("aouth/signinDemo.php")
    fun signIn(@Field("email") email: String?, @Field("password") password: String?): Call<Oauth>
    @GET("home/home.php")
    fun home(): Call<DashboardData>
    @GET("categories/categories.php")
    fun categories(): Call<CategoryData>
    @GET("categories/activeCategories.php")
    fun activeCategories(): Call<CategoryData>
    @GET("supplier/suppliers.php")
    fun suppliers(): Call<SupplierData>
    @GET("supplier/activeSupplier.php")
    fun activeSuppliers(): Call<SupplierData>
    @GET("products/Products.php")
    fun products(): Call<ProductData>
    @GET("products/expiringProducts.php")
    fun expiringProducts(): Call<ProductData>
    @GET("products/viewProductQty.php")
    fun reOrder(): Call<ProductData>
    @GET("customer/customers.php")
    fun customers(): Call<CustomerData>
    @GET("CustomerLedger/invoices.php")
    fun invoices():Call<InvoiceData>
    @GET("CustomerLedger/creditDue.php")
    fun creditDue():Call<InvoiceData>
    @GET("report/accountreceivable.php")
    fun accountReceivable():Call<AccountsReceivableData>
    @GET("report/collection.php")
    fun collectionReport():Call<CollectionReportData>
    @GET("report/salesreport.php")
    fun salesReport():Call<SalesReportData>
    @FormUrlEncoded
    @POST("report/dayreport.php")
    fun daySalesReport(@Field("date") dates: String?):Call<SalesReportData>
    @GET("report/cash.php")
    fun cashReport():Call<CashReportData>
    @GET("report/credit.php")
    fun creditReport():Call<CreditReportData>
    @GET("report/mpesa.php")
    fun mpesaReport():Call<MpesaReportData>
    @GET("report/inventoryreport.php")
    fun inventoryReport():Call<InventoryReportData>
    @GET("report/expiredproducts.php")
    fun expiryReport():Call<ExpiryReportData>
    @GET("report/customertransaction.php")
    fun customerTransactionReport():Call<CustomerTransactionData>
    @FormUrlEncoded
    @POST("purchaseOrderForm/saveInvoiceItems.php")
    fun addOrder(@Field("price") price: String?, @Field("cost") cost: String?, @Field("qty") quantity: String?, @Field("exdate") expiryDate: String?,@Field("pid") productCode: String?,@Field("invoice") invoice:String?,@Field("status") status: String?,@Field("vat") vat: String?,@Field("discount") discount: String?): Call<OrderData>
    @FormUrlEncoded
    @POST("CustomerLedger/addPayment.php")
    fun addPayment(@Field("name")name:String?,@Field("invoice")invoice:String?,@Field("tot")totalAmount:String?,@Field("amount")amount:String?,@Field("remarks")remarks:String?,@Field("balance")balance:String?): Call<InvoiceData>
    @FormUrlEncoded
    @POST("customer/addCustomer.php")
    fun addCustomer(@Field("fname")firstName:String?,@Field("mname")middleName:String?,@Field("lname")lastName:String?,@Field("address")address:String?,@Field("contact")contact:String?): Call<CustomerData>
    @FormUrlEncoded
    @POST("customer/editCustomer.php")
    fun editCustomer(@Field("fname")firstName:String?,@Field("mname")middleName:String?,@Field("lname")lastName:String?,@Field("address")address:String?,@Field("contact")contact:String?,@Field("memi")cid:String?,@Field("code")code:String?,@Field("name")name:String?): Call<CustomerData>
    @FormUrlEncoded
    @POST("customer/deleteCustomer.php")
    fun deleteCustomer(@Field("id")customerId:String?,@Field("name")customerName: String?): Call<CustomerData>

    @FormUrlEncoded
    @POST("products/addProduct.php")
    fun addProduct(@Field("price")price:String?,@Field("cost")cost:String?,@Field("bname")productName:String?,@Field("dname")productDescription:String?,@Field("supplier")supplier:String?,@Field("qty")quantity:String?,@Field("categ")category:String?,@Field("date_del")delivery:String?,@Field("ex_date")expiry:String?,@Field("unit")unit:String?,@Field("reorder")reorder:String?,@Field("brand")brand:String?,@Field("vat")vat:String?): Call<ProductData>
    @FormUrlEncoded
    @POST("products/increaseProduct.php")
    fun increaseProduct(@Field("price")price:String?,@Field("cost")cost:String?,@Field("qleft")quantity:String?,@Field("exdate")expiry:String?,@Field("pid")productCode:String?): Call<ProductData>
    @FormUrlEncoded
    @POST("products/pullOutProducts.php")
    fun pullOutProduct(@Field("code")productCode:String?,@Field("bname")productName:String?,@Field("dname")productDescription:String?,@Field("cost")cost:String?,@Field("qty")quantity:String?,@Field("categ")category:String?,@Field("exdate")expiry:String?): Call<ProductData>
    @FormUrlEncoded
    @POST("products/editProduct.php")
    fun editProduct(@Field("price")price:String?,@Field("cost")cost:String?,@Field("bname")productName:String?,@Field("dname")productDescription:String?,@Field("supplier")supplier:String?,@Field("qleft")quantity:String?,@Field("categ")category:String?,@Field("brand")brand:String?,@Field("vat")vat:String?,@Field("unit")unit:String?,@Field("reorder")reorder:String,@Field("ex_date")expiry:String?,@Field("pid")pid:String?): Call<ProductData>
    @FormUrlEncoded
    @POST("categories/deleteCategory.php")
    fun deleteCategory(@Field("cid")categoryId:String?): Call<CategoryData>
    @FormUrlEncoded
    @POST("categories/addCategory.php")
    fun addCategory(@Field("name")categoryName:String?): Call<CategoryData>
    @FormUrlEncoded
    @POST("categories/editCategory.php")
    fun editCategory(@Field("name")categoryName:String?,@Field("cid")categoryId:String?,@Field("category")category:String?): Call<CategoryData>
    @FormUrlEncoded
    @POST("supplier/addSupplier.php")
    fun addSupplier(@Field("name")suplierName:String?,@Field("address") suplierAddress:String?,@Field("contact")suplierContact:String?,@Field("cperson")contactPerson:String?): Call<SupplierData>
    @FormUrlEncoded
    @POST("supplier/editSupplier.php")
    fun editSupplier(@Field("name")suplierName:String?,@Field("address") suplierAddress:String?,@Field("contact")suplierContact:String?,@Field("cperson")contactPerson:String?,@Field("memi")suplierId:String?): Call<SupplierData>
    @FormUrlEncoded
    @POST("report/viewcustomertransaction.php")
    fun customerTransactions(@Field("name")customerName:String?): Call<CustomerTransactionsData>
    @GET("products/productsReport.php")
    fun productsReport(): Call<ProductReportData>
    @FormUrlEncoded
    @POST("customer/activateCustomer.php")
    fun activateCustomer(@Field("id")customerId:String?): Call<CustomerData>
    @FormUrlEncoded
    @POST("customer/deactivateCustomer.php")
    fun deactivateCustomer(@Field("id")customerId:String?): Call<CustomerData>
    @FormUrlEncoded
    @POST("supplier/deleteSupplier.php")
    fun deleteSupplier(@Field("id")supplierId:String?): Call<SupplierData>
    @FormUrlEncoded
    @POST("products/deleteProduct.php")
    fun deleteProduct(@Field("id")productId:String?): Call<ProductData>
    @FormUrlEncoded
    @POST("supplier/activateSupplier.php")
    fun activateSupplier(@Field("id")supplierId:String?): Call<SupplierData>
    @FormUrlEncoded
    @POST("supplier/deactivateSupplier.php")
    fun deactivateSupplier(@Field("id")supplierId:String?): Call<SupplierData>
    @FormUrlEncoded
    @POST("categories/activateCategory.php")
    fun activateCategory(@Field("id")categoryId:String?): Call<CategoryData>
    @FormUrlEncoded
    @POST("categories/deactivateCategory.php")
    fun deactivateCategory(@Field("id")categoryId:String?): Call<CategoryData>
    @GET("accounts/users.php")
    fun users(): Call<UsersData>
    @FormUrlEncoded
    @POST("accounts/addUser.php")
    fun addUser(@Field("username")username:String?,@Field("password")password:String?,@Field("name")name:String?,@Field("code")businessCode:String?): Call<UsersData>
    @FormUrlEncoded
    @POST("accounts/editUser.php")
    fun editUser(@Field("password")password:String?,@Field("name")name:String?,@Field("memi")userId:String?): Call<UsersData>
    @FormUrlEncoded
    @POST("accounts/deleteUser.php")
    fun deleteUser(@Field("memi")userId:String?): Call<UsersData>
    @FormUrlEncoded
    @POST("supplier/supplierProducts.php")
    fun supplierProducts(@Field("sid")supplierId:String?): Call<ProductData>
    @FormUrlEncoded
    @POST("aouth/update.php")
    fun updateUser(@Field("username")password:String?,@Field("name")email:String?,@Field("email")mobile:String,@Field("mobile")name:String,@Field("sid")userId:String?): Call<Oauth>
    @FormUrlEncoded
    @POST("aouth/changePassword.php")
    fun changePassword(@Field("email")email:String?,@Field("password")password:String?): Call<Oauth>
    @FormUrlEncoded
    @POST("customer/getCustomerSubscription.php")
    fun getCustomerSubscription(@Field("code") businessCode: String?): Call<SubscriptionData>
    @FormUrlEncoded
    @POST("payment/pay.php")
    fun subscribe(@Field("amount") amount: String?,@Field("phone") phone: String?,@Field("code") code: String?): Call<PaymentResponseData>
    @FormUrlEncoded
    @POST("payment/receipt.php")
    fun receipts(@Field("code")code:String?): Call<ReceiptData>
    @FormUrlEncoded
    @POST("payment/payPending.php")
    fun completePayment(@Field("amount") amount: String?,@Field("phone") phone: String?,@Field("code") code: String?): Call<PaymentResponseData>
    @FormUrlEncoded
    @POST("subscription/subscription.php")
    fun checkSubscription(@Field("code") code: String?): Call<CheckPaymentResponseData>
    @GET("brand/brands.php")
    fun brands(): Call<BrandData>
    @GET("brand/activeBrand.php")
    fun activeBrands(): Call<BrandData>
    @FormUrlEncoded
    @POST("brand/activateBrand.php")
    fun activateBrand(@Field("id")brandId:String?): Call<BrandData>
    @FormUrlEncoded
    @POST("brand/deactivateBrand.php")
    fun deactivateBrand(@Field("id")brandId:String?): Call<BrandData>
    @FormUrlEncoded
    @POST("brand/deleteBrand.php")
    fun deleteBrand(@Field("cid")brandId:String?): Call<BrandData>
    @FormUrlEncoded
    @POST("brand/addBrand.php")
    fun addBrand(@Field("name")brandName:String?): Call<BrandData>
    @FormUrlEncoded
    @POST("brand/editBrand.php")
    fun editBrand(@Field("name")brandName:String?,@Field("cid")brandId:String?,@Field("brand")brand:String?): Call<BrandData>
    @FormUrlEncoded
    @POST("categories/categoriesProducts.php")
    fun categoryProducts(@Field("category")category:String?): Call<ProductData>
    @FormUrlEncoded
    @POST("brand/brandProducts.php")
    fun brandProducts(@Field("brand")brand:String?): Call<ProductData>
    @FormUrlEncoded
    @POST("purchaseOrderForm/deleteInvoiceItem.php")
    fun deleteOrder(@Field("id") productId: String?,@Field("qty") quantity: String?, @Field("code") productCode: String?,@Field("pid") pid:String?): Call<OrderData>
    @GET("purchaseorderlist/purchaselist.php")
    fun purchasesInvoices(): Call<PurchaseOrderData>
    @FormUrlEncoded
    @POST("purchaseOrderForm/invoiceItems.php")
    fun invoiceItems(@Field("invoice")invoice:String?): Call<OrderData>
    @FormUrlEncoded
    @POST("purchaseOrderForm/saveInvoice.php")
    fun saveInvoice(@Field("invoice")invoice:String?,@Field("supplier") supplier: String?,@Field("date_delivered") dateDelivered: String?,@Field("due_date") dueDate: String?,@Field("receipt_number") receiptNumber:String?,@Field("status") paymentStatus: String?): Call<PurchaseOrderData>
    @FormUrlEncoded
    @POST("purchaseOrderForm/completePayment.php")
    fun completePayment(@Field("pid")pid:String?): Call<PurchaseOrderData>
    @FormUrlEncoded
    @POST("expense/addExpense.php")
    fun addExpense(@Field("name")name:String?,@Field("amount")amount: String?): Call<ExpenseData>
    @GET("expense/expenses.php")
    fun expenses(): Call<ExpenseData>
    @GET("expense/activeExpenses.php")
    fun activeExpenses(): Call<ExpenseData>
    @FormUrlEncoded
    @POST("expense/activateExpense.php")
    fun activateExpense(@Field("id")id:String?): Call<ExpenseData>
    @FormUrlEncoded
    @POST("expense/deactivateExpense.php")
    fun deactivateExpense(@Field("id")id:String?): Call<ExpenseData>
    @FormUrlEncoded
    @POST("expense/editExpense.php")
    fun editExpense(@Field("name")name:String?,@Field("amount")amount: String?,@Field("cid")cid: String?): Call<ExpenseData>
    @FormUrlEncoded
    @POST("expense/recordExpense.php")
    fun recordExpense(@Field("expense")expense:String?,@Field("amount")amount: String?,@Field("month")month: String?,@Field("date")date: String?,@Field("year")year: String?,@Field("payee")payee: String?,@Field("receipt")receiptNumber: String?): Call<RecordedExpenseData>
    @FormUrlEncoded
    @POST("expense/editRecordedExpense.php")
    fun editRecordedExpense(@Field("expense")expense:String?,@Field("amount")amount: String?,@Field("month")month: String?,@Field("date")date: String?,@Field("year")year: String?,@Field("payee")payee: String?,@Field("receipt")receiptNumber: String?,@Field("cid")cid: String?): Call<RecordedExpenseData>
    @FormUrlEncoded
    @POST("expense/deleteRecordedExpense.php")
    fun deleteRecordedExpense(@Field("cid")cid:String?): Call<RecordedExpenseData>
    @GET("expense/recordedExpense.php")
    fun recordedExpenses(): Call<RecordedExpenseData>
    @FormUrlEncoded
    @POST("expense/deleteExpense.php")
    fun deleteExpense(@Field("cid")cid:String?): Call<ExpenseData>
    @GET("expense/expenseReport.php")
    fun expenseReport(): Call<ExpenseReportData>
    @FormUrlEncoded
    @POST("income/recordIncome.php")
    fun recordIncome(@Field("service")expense:String?,@Field("amount")amount: String?,@Field("month")month: String?,@Field("date")date: String?,@Field("year")year: String?,@Field("cashier")cashier: String?,@Field("receipt")items: String?): Call<IncomeData>
    @FormUrlEncoded
    @POST("income/editIncome.php")
    fun editIncome(@Field("service")expense:String?,@Field("amount")amount: String?,@Field("month")month: String?,@Field("date")date: String?,@Field("year")year: String?,@Field("cashier")cashier: String?,@Field("receipt")items: String?,@Field("cid")cid: String?): Call<IncomeData>
    @FormUrlEncoded
    @POST("income/deleteIncome.php")
    fun deleteIncome(@Field("cid")cid:String?): Call<IncomeData>
    @GET("income/income.php")
    fun income(): Call<IncomeData>
    @GET("income/incomeReport.php")
    fun incomeReport(): Call<IncomeReportData>
    @FormUrlEncoded
    @POST("income/dayIncomeReport.php")
    fun dayIncomeReport(@Field("date")day:String?): Call<DayIncomeAndExpenseData>
    @FormUrlEncoded
    @POST("income/cashierReport.php")
    fun cashierIncomeReport(@Field("cid")cashierId:String?): Call<CashierIncomeReportData>








}