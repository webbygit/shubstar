package com.salestrackmobileapp.android.adapter;

import android.app.Activity;
import android.app.Dialog;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.drawable.ColorDrawable;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.view.inputmethod.EditorInfo;
import android.view.inputmethod.InputMethodManager;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.gson.Gson;
import com.google.gson.GsonBuilder;
import com.orm.query.Condition;
import com.orm.query.Select;

import com.salestrackmobileapp.android.R;
import com.salestrackmobileapp.android.activities.DashboardActivity;
import com.salestrackmobileapp.android.activities.GoalsActivities;
import com.salestrackmobileapp.android.custome_views.Custome_BoldTextView;
import com.salestrackmobileapp.android.custome_views.Custome_TextView;
import com.salestrackmobileapp.android.fragments.CartFragment;
import com.salestrackmobileapp.android.my_cart.ProductInCart;
import com.salestrackmobileapp.android.networkManager.NetworkManager;
import com.salestrackmobileapp.android.networkManager.ServiceHandler;
import com.salestrackmobileapp.android.utils.CommonUtils;
import com.salestrackmobileapp.android.utils.Connectivity;
import com.salestrackmobileapp.android.utils.PrefsHelper;
import com.salestrackmobileapp.android.utils.ProductPriceList;
import com.salestrackmobileapp.android.utils.RecyclerClick;
import com.salestrackmobileapp.android.utils.SuperclassExclusionStrategy;
import com.squareup.picasso.Picasso;

import org.json.JSONArray;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.net.URLEncoder;
import java.text.DecimalFormat;
import java.util.Iterator;
import java.util.List;

import javax.net.ssl.HttpsURLConnection;

import retrofit.Callback;
import retrofit.RetrofitError;
import retrofit.client.Response;

import static android.content.Context.INPUT_METHOD_SERVICE;
import static com.salestrackmobileapp.android.fragments.CartFragment.sumValues;




public class CartAdapter extends RecyclerView.Adapter<CartAdapter.ViewHolder> {

    RecyclerClick rvClick;
    Context context;
    List<ProductInCart> productInCartList;
    ProductInCart productInCart;
    Double discountAmt = 0.0;
    Double priceAmt = 0.0;
    public static int sumPrice;
    Double discount;
    public PrefsHelper sharedPreference;
    String STATE = "";


    public CartAdapter(Context context, RecyclerClick rvClick) {
        this.context = context;
        this.rvClick = rvClick;
        productInCartList = ProductInCart.listAll(ProductInCart.class);
        sharedPreference = new PrefsHelper(context);

    }

    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, final int viewType) {
        LayoutInflater inflater = LayoutInflater.from(parent.getContext());
        View view = inflater.inflate(R.layout.cart_item_layout, parent, false);
        CartAdapter.ViewHolder vh = new CartAdapter.ViewHolder(view, rvClick);
        view.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                rvClick.productClick(v, viewType);
            }
        });
        return vh;
    }

    @Override
    public void onBindViewHolder(final ViewHolder holder, final int position) {

        final ProductInCart productInCartItem = productInCartList.get(position);
        STATE = CartFragment.STATE;
        if (position == 0) {

            mProgressDialog = new ProgressDialog(context);
            mProgressDialog.setMessage("Loading...");
            mProgressDialog.setCanceledOnTouchOutside(true);
            mProgressDialog.show();
            Log.e("ProgressDialog", ":::True");

        }
        if (position == productInCartList.size() - 1) {
            Log.e("hideDialog", ":::true");
            hideDialog();
        }

        getProductPriceList(productInCartItem.productID, holder.product_tax);
        if (Connectivity.isNetworkAvailable(context)) {
            SendPostRequest task = new SendPostRequest(holder.product_tax, productInCartItem.productID);
            task.execute();
        } else {
            productInCart = Select.from(ProductInCart.class).where(Condition.prop("variant_id").eq(productInCartItem.variantID)).first();
            if (productInCart != null) {
                Double CGSTTaxValue = productInCart.cGSTPercentage;
                Double SGSTTaxValue = productInCart.sGSTPercentage;


                holder.product_tax.setText("GST: " + " " + Double.parseDouble(new DecimalFormat("##.##").format(CGSTTaxValue + SGSTTaxValue)) + "%");

            }
        }
//        Log.e("PRODUCT_ID_",":::"+position+":::"+productInCartItem.productID);
//        Log.e("STATE",":::"+STATE);


//        for (UOMArray uomArray1 : uomArray) {
////            if (listUomArray.contains(uomArray1.getUOM())) {
////
////            } else {
////                listUomArray.add(uomArray1.getUOM() + "");
////                listUomIdArray.add(uomArray1.getUOMID());
////            }
//            if (productInCartItem.uomID.equals(uomArray1.getUOMID())){
//                holder.unit_type.setText("Unit Type : "+uomArray1.getUOM());
//            }
//            else{
//                holder.unit_type.setText("Unit Type : "+"Pcs");
//            }
//
//            Log.e("UOM_ARRAY","::::"+uomArray1.getUOM());
//            Log.e("UOM_ARRAYID","::::"+uomArray1.getUOMID());
//
//        }

        Log.e("UOM_ID", ":::::" + productInCartItem.uomID);
        if (productInCartItem.uomID == 364) {
            holder.unit_type.setText("Unit Type : " + "Pieces");


        } else if (productInCartItem.uomID == 384) {
            holder.unit_type.setText("Unit Type : " + "Dozen");


        } else if (productInCartItem.uomID == 614) {
            holder.unit_type.setText("Unit Type : " + "Box");


        } else {
            holder.unit_type.setText("Unit Type : " + "Pieces");


        }


        try {
            if (!productInCartItem.price.equals(null)) {

                sumPrice += productInCartItem.price;
            }
        } catch (Exception ex) {
            ex.printStackTrace();
            Log.d("", "exception sum price");
        }
        //holder.productImg
//        Log.e("BASE_PRICE",":::"+productInCartItem.price);


        Picasso.with(context).load(productInCartItem.imageUrl).placeholder(context.getResources().getDrawable(R.drawable.calendar_icon)).error(R.drawable.calendar_icon).into(holder.productImg);
        holder.mainProductTitle.setText(productInCartItem.productName + "");
        Double price = productInCartItem.price * productInCartItem.qty;
        holder.mrpPrice.setText("" + productInCartItem.price + "");
        holder.mrp_product_tv_original.setText("₹" + productInCartItem.priceOriginal);
        holder.mrp_product_tv_original.setPaintFlags(holder.mrp_product_tv_original.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
        holder.iv_edit.setTag(productInCartItem.productID);

        holder.mrpPrice.clearFocus();
        holder.mrpPrice.setFocusable(false);
        holder.mrpPrice.setCursorVisible(false);
        holder.mrpPrice.setFocusableInTouchMode(false);
        if (productInCartItem.allowPriceEdit) {
            holder.iv_edit.setVisibility(View.VISIBLE);

        } else {
            holder.iv_edit.setVisibility(View.INVISIBLE);

        }
        holder.iv_edit.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Log.e("onCLick", ":::true");
//                if (productInCartItem.productID == CommonUtils.productID) {
                Log.e("requestFocus", "::::true");

                holder.mrpPrice.setFocusable(true);
                holder.mrpPrice.setFocusableInTouchMode(true);
                holder.mrpPrice.requestFocus();
                holder.mrpPrice.setCursorVisible(true);
                holder.mrpPrice.postDelayed(new Runnable() {
                    @Override
                    public void run() {
                        InputMethodManager keyboard = (InputMethodManager)
                                context.getSystemService(Context.INPUT_METHOD_SERVICE);
                        if (keyboard != null) {
                            keyboard.showSoftInput(holder.mrpPrice, 0);
                        }
                    }
                }, 200);


//                } else {
//                    Log.e("clearFocus", "::::true");
//
//                    holder.mrpPrice.clearFocus();
//                    holder.mrpPrice.setFocusable(false);
//                    holder.mrpPrice.setCursorVisible(false);
//                    holder.mrpPrice.setFocusableInTouchMode(false);
//
//                }

//                CommonUtils.productID = ((Integer) holder.iv_edit.getTag());
//                notifyDataSetChanged();


            }
        });

        priceAmt = productInCartItem.price;


        if (productInCartItem.dealCategory != null) {
            if (productInCartItem.dealCategory.equals("product")) {
                holder.afterDiscount.setVisibility(View.GONE);
                holder.sizeOfProduct.setVisibility(View.GONE);
            }
        }

        if (productInCartItem.dealCategory != null) {
            if (productInCartItem.dealCategory.equals("product")) {
                if (!(productInCartItem.dealType == null || productInCartItem.dealType.equals(""))) {
                    if (productInCartItem.dealType.equals("Amount")) {
                        try {
                            discountAmt = Double.valueOf(productInCartItem.dealAmount);
                            holder.sizeOfProduct.setText("₹" + " " + productInCartItem.dealAmount + " off");
                            Double priceNew = price - Double.valueOf(productInCartItem.dealAmount);
                            holder.afterDiscount.setText(priceNew + "");
//                        holder.mrpPrice.setPaintFlags(holder.mrpPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
//                        holder.mrpPrice.setTextColor(context.getResources().getColor(R.color.dark_dim_gray));
                            holder.afterDiscount.setTextColor(context.getResources().getColor(R.color.green));
                        } catch (Exception e) {

                        }

                    } else {
                        Double amount = 0.0;
                        try {

                            amount = Double.valueOf(productInCartItem.dealAmount);
                            discount = (amount / 100) * priceAmt;
                            holder.sizeOfProduct.setText("" + productInCartItem.dealAmount + " % off");
                            // holder.mrpPrice.setPaintFlags(holder.mrpPrice.getPaintFlags() | Paint.STRIKE_THRU_TEXT_FLAG);
                            discountAmt = priceAmt - discount;
                            /*  discountAmt=discountAmt*productInCartItem.qty;*/
                            holder.afterDiscount.setText(discountAmt + "");
                            // holder.mrpPrice.setTextColor(context.getResources().getColor(R.color.dark_dim_gray));
                            holder.afterDiscount.setTextColor(context.getResources().getColor(R.color.green));
                        } catch (Exception e) {

                        }

                    }
                } else {
                    holder.afterDiscount.setVisibility(View.GONE);
                    holder.sizeOfProduct.setVisibility(View.GONE);
                }

            } else {
                holder.afterDiscount.setVisibility(View.GONE);
                holder.sizeOfProduct.setVisibility(View.GONE);


            }
        }
        holder.mrpPrice.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if (s.length() != 0) {


                    String amount = holder.mrpPrice.getText().toString().trim();
                    try {
                        Log.e("afterTextChanged", ":::true");

                        Double amountInt = Double.parseDouble(amount);
                        productInCart = Select.from(ProductInCart.class).where(Condition.prop("variant_id").eq(productInCartItem.variantID)).first();
                        if (productInCart != null) {
                            productInCart.price = amountInt;
                            productInCart.save();




                   /* if (productInCartItem.dealType!=null) {

                        if(productInCartItem.dealType.equals("Amount")) {
                            discountAmt = discountAmt * currentQtyInt;
                            priceAmt = priceAmt * currentQtyInt;
                            holder.mrpPrice.setText(priceAmt + "");
                          //  holder.sizeOfProduct.setText(discountAmt + "");
                        }
                    }*/
                            sumValues();
//                            CommonUtils.productID=0;
//                            notifyDataSetChanged();
                        }
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                }

            }
        });
        holder.mrpPrice.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.e("hide_not", "::::::");

                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT)) || (actionId == EditorInfo.IME_ACTION_DONE) || (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    //do what you want on the press of 'done'
                    Log.e("hide", "true");
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(holder.mrpPrice.getWindowToken(), 0);
                    holder.mrpPrice.clearFocus();
                    holder.mrpPrice.setFocusable(false);
                    holder.mrpPrice.setCursorVisible(false);
                    holder.mrpPrice.setFocusableInTouchMode(false);
                    return true;

                }
                return false;
            }
        });


        holder.qtyTv.addTextChangedListener(new TextWatcher() {

            @Override
            public void afterTextChanged(Editable s) {


            }

            @Override
            public void beforeTextChanged(CharSequence s, int start,
                                          int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start,
                                      int before, int count) {
                if (s.length() != 0) {


                    String currentQtyString = holder.qtyTv.getText().toString().trim();
                    int currentQtyInt = Integer.parseInt(currentQtyString);
                    productInCart = Select.from(ProductInCart.class).where(Condition.prop("variant_id").eq(productInCartItem.variantID)).first();
                    if (productInCart != null) {
                        productInCart.qty = currentQtyInt;
                        productInCart.save();




                   /* if (productInCartItem.dealType!=null) {

                        if(productInCartItem.dealType.equals("Amount")) {
                            discountAmt = discountAmt * currentQtyInt;
                            priceAmt = priceAmt * currentQtyInt;
                            holder.mrpPrice.setText(priceAmt + "");
                          //  holder.sizeOfProduct.setText(discountAmt + "");
                        }
                    }*/
                        sumValues();
                        holder.mrpPrice.clearFocus();
                        holder.mrpPrice.setFocusable(false);
                        holder.mrpPrice.setCursorVisible(false);
                        holder.mrpPrice.setFocusableInTouchMode(false);
                    }
                }


            }
        });

        holder.qtyTv.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                Log.e("hide_not", "::::::");

                if ((event != null && (event.getKeyCode() == KeyEvent.KEYCODE_NAVIGATE_NEXT)) || (actionId == EditorInfo.IME_ACTION_DONE) || (event != null && (event.getKeyCode() == KeyEvent.KEYCODE_ENTER))
                        || (actionId == EditorInfo.IME_ACTION_NEXT)) {
                    //do what you want on the press of 'done'
                    Log.e("hide", "true");
                    InputMethodManager imm = (InputMethodManager) context.getSystemService(INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(holder.qtyTv.getWindowToken(), 0);

                    CommonUtils.productID = 0;
                    //  notifyDataSetChanged();

                    return true;

                }
                return false;
            }
        });
//        holder.qtyTv.setOnKeyListener(new View.OnKeyListener()
//        {
//            /**
//             * This listens for the user to press the enter button on
//             * the keyboard and then hides the virtual keyboard
//             */
//            public boolean onKey(View arg0, int arg1, KeyEvent event) {
//                // If the event is a key-down event on the "enter" button
//                if ( (event.getAction() == KeyEvent.ACTION_DOWN  ) &&
//                        (arg1           == KeyEvent.KEYCODE_ENTER)   )
//                {
//                    InputMethodManager imm = (InputMethodManager)context.getSystemService(INPUT_METHOD_SERVICE);
//                    imm.hideSoftInputFromWindow( holder.qtyTv.getWindowToken(), 0);
//                    return true;
//                }
//                return false;
//            }
//        } );


        // holder.afterDiscount.setText("");
        holder.plusImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentQtyString = holder.qtyTv.getText().toString().trim();
                if (currentQtyString.length() != 0) {
                    int currentQtyInt = Integer.parseInt(currentQtyString);
                    productInCart = Select.from(ProductInCart.class).where(Condition.prop("variant_id").eq(productInCartItem.variantID)).first();
                    if (currentQtyInt >= 0) {
                        currentQtyInt = currentQtyInt + 1;
                    } else {
                        currentQtyInt = 1;
                    }
                    holder.qtyTv.setText(currentQtyInt + "");
                    holder.qtyTv.clearFocus();
                    if (productInCart != null) {
                        productInCart.qty = currentQtyInt;
                        productInCart.CartQty = 1;
                        productInCart.save();
                        Log.e("QTY:::", ":::" + productInCart.qty);

                   /* if (productInCartItem.dealType!=null) {

                        if(productInCartItem.dealType.equals("Amount")) {
                            discountAmt = discountAmt * currentQtyInt;
                            priceAmt = priceAmt * currentQtyInt;
                            holder.mrpPrice.setText(priceAmt + "");
                          //  holder.sizeOfProduct.setText(discountAmt + "");
                        }
                    }*/
                        sumValues();
                    }
                } else {
                    productInCart = Select.from(ProductInCart.class).where(Condition.prop("variant_id").eq(productInCartItem.variantID)).first();
                    int currentQtyInt = 1;
                    holder.qtyTv.setText(currentQtyInt + "");
                    holder.qtyTv.clearFocus();
                    if (productInCart != null) {
                        productInCart.qty = currentQtyInt;

                        productInCart.save();
                        Log.e("QTY:::", ":::" + productInCart.qty);

                   /* if (productInCartItem.dealType!=null) {

                        if(productInCartItem.dealType.equals("Amount")) {
                            discountAmt = discountAmt * currentQtyInt;
                            priceAmt = priceAmt * currentQtyInt;
                            holder.mrpPrice.setText(priceAmt + "");
                          //  holder.sizeOfProduct.setText(discountAmt + "");
                        }
                    }*/
                        sumValues();
                    }


                }
            }
        });


        holder.minusImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String currentQtyString = holder.qtyTv.getText().toString().trim();
                if (currentQtyString.length() != 0) {

                    if (Integer.parseInt(currentQtyString) > 1) {
                        int currentQtyInt = Integer.parseInt(currentQtyString);
                        if (currentQtyInt >= 0) {
                            if (currentQtyInt == 0) {
                                currentQtyInt = 0;
                                if (productInCartItem != null) {
                                    productInCartItem.delete();
                                }
                            } else {
                                currentQtyInt = currentQtyInt - 1;
                            }
                        } else {
                            currentQtyInt = 1;
                        }
                        holder.qtyTv.setText(currentQtyInt + "");
                        holder.qtyTv.clearFocus();
                        if (productInCartItem != null) {
                            productInCartItem.qty = currentQtyInt;
                            productInCartItem.save();
                            Log.e("QTY:::", ":::minus:::::" + productInCart.qty);

                            sumValues();
                        }

                    } else {


                        SharedPreferences preferences = context.getSharedPreferences("MyPref", 0);
                        SharedPreferences.Editor editor = preferences.edit();
                        Log.e("***ProductID", "::::::" + productInCartItem.productID);

                        editor.putBoolean("" + productInCartItem.productID, false);
                        editor.commit();
                        productInCartItem.delete();
                        // finalHolder.product.delete();
                        remove(productInCartItem);
                        sumValues();
                        // notifyDataSetChanged();
                    }
                }
            }

        });
        holder.ivClear.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                //  holder.qtyTv.setText("");
                holder.qtyTv.getText().clear();
            }
        });


        holder.delImg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // ProductInCart    productInCart = Select.from(ProductInCart.class).where(Condition.prop("product_id").eq(productInCartItem.productID)).first();


                //  Select.from(ProductInCart.class).where(Condition.prop("product_id").eq(productInCartItem.productID)).first().delete();
                // finalHolder.product.delete();

                Log.e("***ProductID", "::::::" + productInCartItem.productID);
                SharedPreferences preferences = context.getSharedPreferences("MyPref", 0);
                SharedPreferences.Editor editor = preferences.edit();
                editor.putBoolean("" + productInCartItem.productID, false);
                editor.commit();
                productInCartItem.delete();
                remove(productInCartItem);
                sumValues();
                // notifyDataSetChanged();


            }
        });

//        Log.e("&&&Quantity","::::"+productInCartItem.qty);

        holder.qtyTv.setText(productInCartItem.qty + "");
        holder.qtyTv.clearFocus();

    }

    public void remove(ProductInCart data) {

        if (productInCartList.size() == 0) {
        } else {
            data.getId();
            int position = productInCartList.indexOf(data);
            if (productInCartList.size() > position) {
                productInCartList.remove(position);
                notifyItemRemoved(position);
            }
            // notifyDataSetChanged();

            if (productInCartList.size() == 0) {
                AlertDialog.Builder alert = new AlertDialog.Builder(context);
                alert.setMessage("Start your Day!..");
                alert.setCancelable(false);
                alert.setPositiveButton("add product", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context, GoalsActivities.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        //   ((BaseActivity) context).sharedPreference.saveStringData(PrefsHelper.BUSINESS_ID, null);
                        intent.putExtra("nameActivity", "AllProduct");
                        context.startActivity(intent);
                    }
                });
                alert.setNegativeButton("home", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        Intent intent = new Intent(context, DashboardActivity.class);
                        intent.addFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
                        context.startActivity(intent);
                    }
                });
                alert.show();
            }
        }
    }

    @Override
    public int getItemCount() {
        return productInCartList.size();
    }

    private ProgressDialog mProgressDialog;
    Gson builder;

    public void getProductPriceList(final int productId, final TextView productTax) {
        if (Connectivity.isNetworkAvailable(context)) {
            showDialog();

            builder = new GsonBuilder().addDeserializationExclusionStrategy(new SuperclassExclusionStrategy())
                    .addSerializationExclusionStrategy(new SuperclassExclusionStrategy()).create();
            ServiceHandler serviceHandler = NetworkManager.createRetrofitService(context, ServiceHandler.class, sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN), NetworkManager.BASE_URL);

            ProductPriceList.deleteAll(ProductPriceList.class);

            Log.e("ProductID", ":::::" + productId);

            serviceHandler.getProductPriceList(productId, new Callback<Response>() {
                @Override
                public void success(Response response, Response response2) {
                    String arr = CommonUtils.getServerResponse(response);
                    ProductPriceList.deleteAll(ProductPriceList.class);
//                    Log.e("ProductPrice::", ":::::" + arr);


                    try {
                        JSONArray jsonArr = new JSONArray(arr);


                        for (int i = 0; i < jsonArr.length(); i++) {
                            ProductPriceList productPriceList = builder.fromJson(jsonArr.get(i).toString(), ProductPriceList.class);
                            JSONObject jsonObject = (JSONObject) jsonArr.get(i);

                            String StateName = jsonObject.getString("StateName");
                            Double CGSTTaxValue = jsonObject.getDouble("CGSTTaxValue");
                            Double SGSTTaxValue = jsonObject.getDouble("SGSTTaxValue");
                            Double Price = jsonObject.getDouble("Price");
//                            Log.e("%%%CGST","::::"+CGSTTaxValue);
//                            Log.e("%%%SGST","::::"+SGSTTaxValue);

                            if (STATE.contains(StateName)) {
//                                Log.e("%%%SGST",":::STATE_WISE:"+SGSTTaxValue);
//                                Log.e("%%%CGST",":::STATE_WISE:"+CGSTTaxValue);
                                Double tax = CGSTTaxValue + SGSTTaxValue;
//                                Log.e("%%%TAX_PRODUCT",""+tax);
                                productTax.setText("GST: " + " " + Double.parseDouble(new DecimalFormat("##.##").format(CGSTTaxValue + SGSTTaxValue)) + "%");
                                productInCart = Select.from(ProductInCart.class).where(Condition.prop("product_id").eq(productId)).first();
                                if (productInCart != null) {
                                    productInCart.sGSTPercentage = SGSTTaxValue;
                                    productInCart.cGSTPercentage = CGSTTaxValue;
                                    productInCart.save();
                                }


                            }
//

                            productPriceList.save();

                        }


                        sumValues();
                        hideDialog();


                    } catch (Exception ex) {
                        ex.printStackTrace();
                        hideDialog();
                    }

                }

                @Override
                public void failure(RetrofitError error) {
                    error.printStackTrace();
                    try {
                        if (error.getMessage().equals("timeout")) {
//                            getProductPriceList();
                        } else {
                            productInCart = Select.from(ProductInCart.class).where(Condition.prop("product_id").eq(productId)).first();
                            if (productInCart != null) {
                                Double CGSTTaxValue = productInCart.cGSTPercentage;
                                Double SGSTTaxValue = productInCart.sGSTPercentage;


                                productTax.setText("GST: " + " " + Double.parseDouble(new DecimalFormat("##.##").format(CGSTTaxValue + SGSTTaxValue)) + "%");

                            }

                        }
                        hideDialog();
//                    getAllGoalsAccDate();
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }
            });


        } else {
            productInCart = Select.from(ProductInCart.class).where(Condition.prop("product_id").eq(productId)).first();
            if (productInCart != null) {
                Double CGSTTaxValue = productInCart.cGSTPercentage;
                Double SGSTTaxValue = productInCart.sGSTPercentage;


                productTax.setText("GST: " + " " + Double.parseDouble(new DecimalFormat("##.##").format(CGSTTaxValue + SGSTTaxValue)) + "%");

            }


        }
    }

    public class SendPostRequest extends AsyncTask<String, Void, String> {
        Integer productId;
        Custome_TextView productTax;
        HttpURLConnection conn = null;
        String server_response = "";

        public SendPostRequest(Custome_TextView target, Integer ProductID) {
            productTax = target;
            productId = ProductID;
        }

        protected void onPreExecute() {
//            showDialog();
        }


        protected String doInBackground(String... arg0) {

            try {
                String basicAuth = "bearer " + sharedPreference.getStringValue(PrefsHelper.ACCESS_TOKEN);
//                Log.e("Authentication","::::"+basicAuth);


                URL url = new URL("https://salestrackapi.azurewebsites.net/api/Products/GetProductPriceList?ProductID=" + productId);
//                Log.e("url",":::"+url);


                conn = (HttpURLConnection) url.openConnection();
                conn.setReadTimeout(15000 /* milliseconds */);
                conn.setConnectTimeout(15000 /* milliseconds */);
                conn.setRequestProperty("Authorization", basicAuth);
                conn.setRequestProperty("content", "application/json");
                conn.setRequestMethod("GET");

                int responseCode = conn.getResponseCode();
//                Log.e("responseCode",":::"+responseCode);


                if (responseCode == HttpURLConnection.HTTP_OK) {
                    server_response = readStream(conn.getInputStream());
//                    Log.v("CatalogClient", server_response);
                }
                return server_response;
            } catch (Exception e) {
//                Log.e("jsonException",":::"+e.toString());
                e.printStackTrace();
            }
            return server_response;

        }


        @Override
        protected void onPostExecute(String result) {
//            Log.e("jsonResult","::::"+result);

            try {
                JSONArray jsonArr = new JSONArray(result);


                for (int i = 0; i < jsonArr.length(); i++) {
                    //ProductPriceList productPriceList = builder.fromJson(jsonArr.get(i).toString(), ProductPriceList.class);
                    JSONObject jsonObject = (JSONObject) jsonArr.get(i);

                    String StateName = jsonObject.getString("StateName");
                    Double CGSTTaxValue = jsonObject.getDouble("CGSTTaxValue");
                    Double SGSTTaxValue = jsonObject.getDouble("SGSTTaxValue");
                    Double Price = jsonObject.getDouble("Price");
//                            Log.e("%%%CGST","::::"+CGSTTaxValue);
//                            Log.e("%%%SGST","::::"+SGSTTaxValue);

                    if (STATE.contains(StateName)) {
//                                Log.e("%%%SGST",":::STATE_WISE:"+SGSTTaxValue);
//                                Log.e("%%%CGST",":::STATE_WISE:"+CGSTTaxValue);
                        Double tax = CGSTTaxValue + SGSTTaxValue;
//                                Log.e("%%%TAX_PRODUCT",""+tax);
                        productTax.setText("GST: " + " " + Double.parseDouble(new DecimalFormat("##.##").format(CGSTTaxValue + SGSTTaxValue)) + "%");
                        productInCart = Select.from(ProductInCart.class).where(Condition.prop("product_id").eq(productId)).first();
                        if (productInCart != null) {
                            productInCart.sGSTPercentage = SGSTTaxValue;
                            productInCart.cGSTPercentage = CGSTTaxValue;
                            productInCart.save();
                        }


                    }
//


                }


                sumValues();


            } catch (Exception ex) {
                ex.printStackTrace();

            }

        }


    }

    private String readStream(InputStream in) {
        BufferedReader reader = null;
        StringBuffer response = new StringBuffer();
        try {
            reader = new BufferedReader(new InputStreamReader(in));
            String line = "";
            while ((line = reader.readLine()) != null) {
                response.append(line);
            }
        } catch (IOException e) {
            e.printStackTrace();
        } finally {
            if (reader != null) {
                try {
                    reader.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return response.toString();
    }

    public String getPostDataString(JSONObject params) throws Exception {

        StringBuilder result = new StringBuilder();
        boolean first = true;

        Iterator<String> itr = params.keys();

        while (itr.hasNext()) {

            String key = itr.next();
            Object value = params.get(key);

            if (first)
                first = false;
            else
                result.append("&");

            result.append(URLEncoder.encode(key, "UTF-8"));
            result.append("=");
            result.append(URLEncoder.encode(value.toString(), "UTF-8"));

        }
        return result.toString();
    }


    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {
        // each data item is just a string in this case
        public Custome_BoldTextView mainProductTitle;
        public Custome_TextView sizeOfProduct, afterDiscount, mrp_product_tv_original, product_tax, tvSubTitle;
        public TextView unit_type;
        public EditText qtyTv, mrpPrice;
        public ImageView productImg, plusImg, minusImg, delImg, ivClear, iv_edit;
        RecyclerClick recyclerClick;


        public ViewHolder(View v, RecyclerClick recyclerClick) {
            super(v);
            mainProductTitle = (Custome_BoldTextView) v.findViewById(R.id.product_title_tv);
            sizeOfProduct = (Custome_TextView) v.findViewById(R.id.product_size_tv);
            mrp_product_tv_original = (Custome_TextView) v.findViewById(R.id.mrp_product_tv_original);
            mrpPrice = (EditText) v.findViewById(R.id.mrp_product_tv);
            productImg = (ImageView) v.findViewById(R.id.product_item_img);
            qtyTv = (EditText) v.findViewById(R.id.qty_tv);
            unit_type = (TextView) v.findViewById(R.id.unit_type);
            plusImg = (ImageView) v.findViewById(R.id.plus_img);
            minusImg = (ImageView) v.findViewById(R.id.minus_img);
            delImg = (ImageView) v.findViewById(R.id.del_img);
            afterDiscount = (Custome_TextView) v.findViewById(R.id.after_discount);
            product_tax = (Custome_TextView) v.findViewById(R.id.product_tax);
            tvSubTitle = v.findViewById(R.id.tvSubTitle);
            ivClear = (ImageView) v.findViewById(R.id.ivClear);
            iv_edit = v.findViewById(R.id.iv_edit);
            this.recyclerClick = recyclerClick;
        }

        @Override
        public void onClick(View v) {
            recyclerClick.productClick(v, getLayoutPosition());
        }
    }

    public void showDialog() {

        if (mProgressDialog != null && !mProgressDialog.isShowing()) {
            Log.e("showDialog_c", ":::true");
            mProgressDialog.show();


        }
    }

    public void hideDialog() {


        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            Log.e("hideDialog_c", "::::::true");
            mProgressDialog.dismiss();

        }
    }
}
