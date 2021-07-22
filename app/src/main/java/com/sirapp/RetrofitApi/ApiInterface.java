package com.sirapp.RetrofitApi;

import com.sirapp.DN.DebitNoteResponseDto;
import com.sirapp.Estimate.EstimateResponseDto;
import com.sirapp.Invoice.ResponseListOfInvoice.ResponseListInociceDto;
import com.sirapp.Invoice.response.InvoiceResponseDto;
import com.sirapp.CN.CreditNoteResponseDto;
import com.sirapp.PO.POResponseDto;
import com.sirapp.PV.PVResponseDto;
import com.sirapp.Receipts.ReceiptResponseDto;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.Header;
import retrofit2.http.POST;

public interface ApiInterface {

    @FormUrlEncoded
    @POST("invoice/detail/{invoice_id}")
    Call<InvoiceResponseDto> getInvoiceDetail(@Header ("Access-Token") String string, @Field("invoice_id") String id, @Field("language") String language);


    @FormUrlEncoded
    @POST("invoice/detail/{invoice_id}")
    Call<ResponseBody> getInvoiceDetail2(@Header ("Access-Token") String string, @Field("invoice_id") String id, @Field("language") String language);


    @FormUrlEncoded
    @POST("invoice/getListingByCompany/{company_id}")
    Call<ResponseListInociceDto> getListOfInvoice(@Header ("Access-Token") String string, @Field("company_id") String id, @Field("language") String language);


    @FormUrlEncoded
    @POST("receipt/detail/{receipt_id}")
    Call<ReceiptResponseDto> getReceiptDetail(@Header ("Access-Token") String string, @Field("receipt_id") String id, @Field("language") String language);


    @FormUrlEncoded
    @POST("estimate/detail/{estimate_id}")
    Call<EstimateResponseDto> getEstimateDetail(@Header ("Access-Token") String string, @Field("estimate_id") String id, @Field("language") String language);


    @FormUrlEncoded
    @POST("creditnote/detail/{credit_note_id}")
    Call<CreditNoteResponseDto> getCreditNoteDetail(@Header ("Access-Token") String string, @Field("credit_note_id") String id, @Field("language") String language);


    @FormUrlEncoded
    @POST("debitnote/detail/{debit_note_id}")
    Call<DebitNoteResponseDto> getDebitNoteDetail(@Header ("Access-Token") String string, @Field("debit_note_id") String id, @Field("language") String language);


    @FormUrlEncoded
    @POST("purchaseorder/detail/{purchase_order_id}")
    Call<POResponseDto> getPODetail(@Header ("Access-Token") String string, @Field("purchase_order_id") String id, @Field("language") String language);


    @FormUrlEncoded
    @POST("paymentvoucher/detail/{payment_voucher_id}")
    Call<PVResponseDto> getPVDetail(@Header ("Access-Token") String string, @Field("payment_voucher_id") String id, @Field("language") String language);

}