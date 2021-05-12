package com.receipt.invoice.stock.sirproject.RetrofitApi;

import com.receipt.invoice.stock.sirproject.DN.DebitNoteResponseDto;
import com.receipt.invoice.stock.sirproject.Estimate.EstimateResponseDto;
import com.receipt.invoice.stock.sirproject.Invoice.ResponseListOfInvoice.ResponseListInociceDto;
import com.receipt.invoice.stock.sirproject.Invoice.response.InvoiceResponseDto;
import com.receipt.invoice.stock.sirproject.CN.CreditNoteResponseDto;
import com.receipt.invoice.stock.sirproject.PO.POResponseDto;
import com.receipt.invoice.stock.sirproject.Receipts.ReceiptResponseDto;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("invoice/detail/{invoice_id}")
    Call<InvoiceResponseDto> getInvoiceDetail(@Header ("Access-Token") String string, @Field("invoice_id") String id);


    @FormUrlEncoded
    @POST("invoice/getListingByCompany/{company_id}")
    Call<ResponseListInociceDto> getListOfInvoice(@Header ("Access-Token") String string, @Field("company_id") String id);


    @FormUrlEncoded
    @POST("receipt/detail/{receipt_id}")
    Call<ReceiptResponseDto> getReceiptDetail(@Header ("Access-Token") String string, @Field("receipt_id") String id);


    @FormUrlEncoded
    @POST("estimate/detail/{estimate_id}")
    Call<EstimateResponseDto> getEstimateDetail(@Header ("Access-Token") String string, @Field("estimate_id") String id);


    @FormUrlEncoded
    @POST("creditnote/detail/{credit_note_id}")
    Call<CreditNoteResponseDto> getCreditNoteDetail(@Header ("Access-Token") String string, @Field("credit_note_id") String id);


    @FormUrlEncoded
    @POST("debitnote/detail/{debit_note_id}")
    Call<DebitNoteResponseDto> getDebitNoteDetail(@Header ("Access-Token") String string, @Field("debit_note_id") String id);


    @FormUrlEncoded
    @POST("purchaseorder/detail/{purchase_order_id}")
    Call<POResponseDto> getPODetail(@Header ("Access-Token") String string, @Field("purchase_order_id") String id);
//    @Url String url

}