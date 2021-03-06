package com.sirapp.PO;

import com.google.gson.annotations.SerializedName;
import com.sirapp.Invoice.response.InvoiceCompanyDto;
import com.sirapp.Invoice.response.InvoiceTotalsItemDto;
import com.sirapp.Invoice.response.ProductsItemDto;

import java.util.List;

public class PODtoPO {

    @SerializedName("template_type")
    private String template_type;

    @SerializedName("signature_of_receiver")
    private String signatureOfReceiver;

    @SerializedName("is_viewed")
    private String isViewed;

    @SerializedName("notes")
    private String notes;

    @SerializedName("payment_iban")
    private String paymentIban;

    @SerializedName("shipping_city")
    private String shippingCity;

    @SerializedName("shipping_address_1")
    private String shippingAddress1;

    @SerializedName("order_status_id")
    private String orderStatusId;

    @SerializedName("link")
    private String link;

    @SerializedName("shipping_address_2")
    private String shippingAddress2;

    @SerializedName("warehouse_id")
    private String warehouse_id;

    @SerializedName("currency_code")
    private String currencyCode;

    @SerializedName("order_date")
    private String order_date;

    @SerializedName("payment_currency")
    private String paymentCurrency;

    @SerializedName("payment_swift_bic")
    private String paymentSwiftBic;

    @SerializedName("products")
    private List<ProductsItemDto> products;

    @SerializedName("paid_amount_date")
    private String paidAmountDate;

    @SerializedName("total")
    private String total;

    @SerializedName("is_deleted")
    private String isDeleted;

    @SerializedName("paid_amount_payment_method")
    private String paidAmountPaymentMethod;

    @SerializedName("purchase_order_id")
    private String purchase_order_id;

    @SerializedName("logo")
    private String logo;

    @SerializedName("company")
    private InvoiceCompanyDto company;

    @SerializedName("shipping_firstname")
    private String shippingFirstname;

    @SerializedName("credit_terms")
    private String creditTerms;

    @SerializedName("shipping_postcode")
    private String shippingPostcode;

    @SerializedName("company_stamp")
    private String companyStamp;

    @SerializedName("shipping_lastname")
    private String shippingLastname;

    @SerializedName("company_id")
    private String companyId;

    @SerializedName("currency_symbol")
    private String currencySymbol;

    @SerializedName("purchase_order_no")
    private String purchase_order_no;

    @SerializedName("signature_of_maker")
    private String signatureOfMaker;

    @SerializedName("due_date")
    private String dueDate;

    @SerializedName("ref_no")
    private String refNo;

    @SerializedName("totals")
    private List<InvoiceTotalsItemDto> totals;

    @SerializedName("payment_bank_name")
    private String paymentBankName;

    @SerializedName("warehouse")
    private Object warehouse;

    @SerializedName("is_inclusive")
    private String isInclusive;

    @SerializedName("date_added")
    private String dateAdded;

    @SerializedName("pdf")
    private String pdf;

    @SerializedName("date_modified")
    private String dateModified;

    @SerializedName("user_id")
    private String userId;

    @SerializedName("shipping_zone")
    private Object shippingZone;

//    @SerializedName("customer_id")
//    private String customerId;

    @SerializedName("shipping_country")
    private String shippingCountry;

    @SerializedName("currency_id")
    private String currencyId;

    @SerializedName("status")
    private String status;

//    @SerializedName("customer")
//    private InvoiceCustomerDto customer;


    @SerializedName("supplier")
    private POSupplierDto supplier;


    public String getTemplate_type() {
        return template_type;
    }

    public void setTemplate_type(String template_type) {
        this.template_type = template_type;
    }

    public String getSignatureOfReceiver() {
        return signatureOfReceiver;
    }

    public void setSignatureOfReceiver(String signatureOfReceiver) {
        this.signatureOfReceiver = signatureOfReceiver;
    }

    public String getIsViewed() {
        return isViewed;
    }

    public void setIsViewed(String isViewed) {
        this.isViewed = isViewed;
    }

    public String getNotes() {
        return notes;
    }

    public void setNotes(String notes) {
        this.notes = notes;
    }

    public String getPaymentIban() {
        return paymentIban;
    }

    public void setPaymentIban(String paymentIban) {
        this.paymentIban = paymentIban;
    }

    public String getShippingCity() {
        return shippingCity;
    }

    public void setShippingCity(String shippingCity) {
        this.shippingCity = shippingCity;
    }

    public String getShippingAddress1() {
        return shippingAddress1;
    }

    public void setShippingAddress1(String shippingAddress1) {
        this.shippingAddress1 = shippingAddress1;
    }

    public String getOrderStatusId() {
        return orderStatusId;
    }

    public void setOrderStatusId(String orderStatusId) {
        this.orderStatusId = orderStatusId;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public String getShippingAddress2() {
        return shippingAddress2;
    }

    public void setShippingAddress2(String shippingAddress2) {
        this.shippingAddress2 = shippingAddress2;
    }


    public String getWarehouse_id() {
        return warehouse_id;
    }

    public void setWarehouse_id(String warehouse_id) {
        this.warehouse_id = warehouse_id;
    }

    public String getCurrencyCode() {
        return currencyCode;
    }

    public void setCurrencyCode(String currencyCode) {
        this.currencyCode = currencyCode;
    }



    public String getPaymentCurrency() {
        return paymentCurrency;
    }

    public void setPaymentCurrency(String paymentCurrency) {
        this.paymentCurrency = paymentCurrency;
    }

    public String getPaymentSwiftBic() {
        return paymentSwiftBic;
    }

    public void setPaymentSwiftBic(String paymentSwiftBic) {
        this.paymentSwiftBic = paymentSwiftBic;
    }

    public List<ProductsItemDto> getProducts() {
        return products;
    }

    public void setProducts(List<ProductsItemDto> products) {
        this.products = products;
    }

    public String getPaidAmountDate() {
        return paidAmountDate;
    }

    public void setPaidAmountDate(String paidAmountDate) {
        this.paidAmountDate = paidAmountDate;
    }

    public String getTotal() {
        return total;
    }

    public void setTotal(String total) {
        this.total = total;
    }

    public String getIsDeleted() {
        return isDeleted;
    }

    public void setIsDeleted(String isDeleted) {
        this.isDeleted = isDeleted;
    }

    public String getPaidAmountPaymentMethod() {
        return paidAmountPaymentMethod;
    }

    public void setPaidAmountPaymentMethod(String paidAmountPaymentMethod) {
        this.paidAmountPaymentMethod = paidAmountPaymentMethod;
    }



    public String getLogo() {
        return logo;
    }

    public void setLogo(String logo) {
        this.logo = logo;
    }

    public InvoiceCompanyDto getCompany() {
        return company;
    }

    public void setCompany(InvoiceCompanyDto company) {
        this.company = company;
    }

    public String getShippingFirstname() {
        return shippingFirstname;
    }

    public void setShippingFirstname(String shippingFirstname) {
        this.shippingFirstname = shippingFirstname;
    }

    public String getCreditTerms() {
        return creditTerms;
    }

    public void setCreditTerms(String creditTerms) {
        this.creditTerms = creditTerms;
    }

    public String getShippingPostcode() {
        return shippingPostcode;
    }

    public void setShippingPostcode(String shippingPostcode) {
        this.shippingPostcode = shippingPostcode;
    }

    public String getCompanyStamp() {
        return companyStamp;
    }

    public void setCompanyStamp(String companyStamp) {
        this.companyStamp = companyStamp;
    }

    public String getShippingLastname() {
        return shippingLastname;
    }

    public void setShippingLastname(String shippingLastname) {
        this.shippingLastname = shippingLastname;
    }

    public String getCompanyId() {
        return companyId;
    }

    public void setCompanyId(String companyId) {
        this.companyId = companyId;
    }

    public String getCurrencySymbol() {
        return currencySymbol;
    }

    public void setCurrencySymbol(String currencySymbol) {
        this.currencySymbol = currencySymbol;
    }

    public String getOrder_date() {
        return order_date;
    }

    public void setOrder_date(String order_date) {
        this.order_date = order_date;
    }

    public String getPurchase_order_id() {
        return purchase_order_id;
    }

    public void setPurchase_order_id(String purchase_order_id) {
        this.purchase_order_id = purchase_order_id;
    }

    public String getPurchase_order_no() {
        return purchase_order_no;
    }

    public void setPurchase_order_no(String purchase_order_no) {
        this.purchase_order_no = purchase_order_no;
    }

    public String getSignatureOfMaker() {
        return signatureOfMaker;
    }

    public void setSignatureOfMaker(String signatureOfMaker) {
        this.signatureOfMaker = signatureOfMaker;
    }

    public String getDueDate() {
        return dueDate;
    }

    public void setDueDate(String dueDate) {
        this.dueDate = dueDate;
    }

    public String getRefNo() {
        return refNo;
    }

    public void setRefNo(String refNo) {
        this.refNo = refNo;
    }

    public List<InvoiceTotalsItemDto> getTotals() {
        return totals;
    }

    public void setTotals(List<InvoiceTotalsItemDto> totals) {
        this.totals = totals;
    }

    public String getPaymentBankName() {
        return paymentBankName;
    }

    public void setPaymentBankName(String paymentBankName) {
        this.paymentBankName = paymentBankName;
    }

    public Object getWarehouse() {
        return warehouse;
    }

    public void setWarehouse(Object warehouse) {
        this.warehouse = warehouse;
    }

    public String getIsInclusive() {
        return isInclusive;
    }

    public void setIsInclusive(String isInclusive) {
        this.isInclusive = isInclusive;
    }

    public String getDateAdded() {
        return dateAdded;
    }

    public void setDateAdded(String dateAdded) {
        this.dateAdded = dateAdded;
    }

    public String getPdf() {
        return pdf;
    }

    public void setPdf(String pdf) {
        this.pdf = pdf;
    }

    public String getDateModified() {
        return dateModified;
    }

    public void setDateModified(String dateModified) {
        this.dateModified = dateModified;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public Object getShippingZone() {
        return shippingZone;
    }

    public void setShippingZone(Object shippingZone) {
        this.shippingZone = shippingZone;
    }

//    public String getCustomerId() {
//        return customerId;
//    }
//
//    public void setCustomerId(String customerId) {
//        this.customerId = customerId;
//    }

    public String getShippingCountry() {
        return shippingCountry;
    }

    public void setShippingCountry(String shippingCountry) {
        this.shippingCountry = shippingCountry;
    }

    public String getCurrencyId() {
        return currencyId;
    }

    public void setCurrencyId(String currencyId) {
        this.currencyId = currencyId;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }


    public POSupplierDto getSupplier() {
        return supplier;
    }

    public void setSupplier(POSupplierDto supplier) {
        this.supplier = supplier;
    }
}
