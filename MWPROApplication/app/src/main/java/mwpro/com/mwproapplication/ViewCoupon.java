/*
 * Copyright (c) 2017. Truiton (http://www.truiton.com/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * Mohit Gupt (https://github.com/mohitgupt)
 *
 */

package mwpro.com.mwproapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.text.Editable;
import android.text.Selection;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.pepperonas.materialdialog.MaterialDialog;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.text.DecimalFormat;

import mwpro.com.mwproapplication.data.MyTitles;
import mwpro.com.mwproapplication.ui.CustomProgressDialog;

import static mwpro.com.mwproapplication.Constants.GETJSONOBJECT;
import static mwpro.com.mwproapplication.Constants.GetMatchString;
import static mwpro.com.mwproapplication.Constants.MyXmlToJSON;
import static mwpro.com.mwproapplication.Constants.vibrate;

public class ViewCoupon extends Fragment implements View.OnClickListener{

    TextView m_ctrlGiftTitle = null;
    TextView m_ctrlGift_One_Title = null;
    EditText m_ctrlGift_One_Result = null;
    TextView m_ctrlGift_One_Money = null;
    TextView m_ctrlGift_Two_Title = null;
    EditText m_ctrlGift_Two_Result = null;
    TextView m_ctrlGift_Two_Money = null;
    TextView m_ctrlSponso_Red_Title = null;
    EditText m_ctrlSponso_Red_Result = null;
    TextView m_ctrlSponso_Red_Money = null;
    TextView m_ctrlSponso_Title = null;
    EditText m_ctrlSponso_Result = null;
    TextView m_ctrlSponso_Money = null;
    CustomProgressDialog customDialog = null;
    Button m_ctrlBtnConfirm = null;
    Button   m_ctrlBtnMailing = null;

    MaterialDialog.Builder m_dMailingDialog = null;
    public static ViewCoupon newInstance() {
        ViewCoupon fragment = new ViewCoupon();
        return fragment;
    }

    @Override
    public void onResume() {
        super.onResume();
        m_ctrlGift_One_Result.requestFocus();

        InputMethodManager imm = (InputMethodManager)getActivity().getSystemService(Context.INPUT_METHOD_SERVICE);
        imm.toggleSoftInputFromWindow(
                m_ctrlGift_One_Result.getApplicationWindowToken(),
                InputMethodManager.SHOW_FORCED, 0);
        m_ctrlGift_One_Result.requestFocus();
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {

        View view =  inflater.inflate(R.layout.fragment_item_two, container, false);

        m_ctrlGiftTitle = (TextView)view.findViewById(R.id.gift_title);
        m_ctrlGift_One_Title = (TextView)view.findViewById(R.id.gift_one_title);
        m_ctrlGift_One_Result = (EditText) view.findViewById(R.id.gift_one_result);
        m_ctrlGift_One_Money  = (TextView)view.findViewById(R.id.gift_one_money);
        m_ctrlGift_Two_Title = (TextView)view.findViewById(R.id.gift_two_title);
        m_ctrlGift_Two_Result = (EditText) view.findViewById(R.id.gift_two_result);
        m_ctrlGift_Two_Money = (TextView)view.findViewById(R.id.gift_two_money);
        m_ctrlSponso_Red_Title = (TextView)view.findViewById(R.id.sponso_red_title);
        m_ctrlSponso_Red_Result = (EditText) view.findViewById(R.id.sponso_red_result);
        m_ctrlSponso_Red_Money = (TextView)view.findViewById(R.id.sponso_red_result_money);
        m_ctrlSponso_Title = (TextView)view.findViewById(R.id.sponso_title);
        m_ctrlSponso_Result = (EditText) view.findViewById(R.id.sponso_result);
        m_ctrlSponso_Money = (TextView)view.findViewById(R.id.sponso_result_money);
        m_ctrlBtnConfirm = (Button)view.findViewById(R.id.btn_confirm);
        m_ctrlBtnMailing = (Button)view.findViewById(R.id.btn_mail);

        m_ctrlBtnConfirm.setOnClickListener(this);
        m_ctrlBtnMailing.setOnClickListener(this);

        m_ctrlGiftTitle.setText(getButtonName("lab_VoucherTitle", Constants.CurrentLang));
        m_ctrlGift_One_Title.setText(getButtonName("lb_coupon", Constants.CurrentLang) + "1");
        m_ctrlGift_One_Money.setText(getButtonName("lb_coupon_money", Constants.CurrentLang));
        m_ctrlGift_Two_Title.setText(getButtonName("lb_coupon", Constants.CurrentLang) + "2");
        m_ctrlGift_Two_Money.setText(getButtonName("lb_coupon_money", Constants.CurrentLang));
        m_ctrlSponso_Red_Title.setText(getButtonName("lab_voucherSponsored", Constants.CurrentLang));
        m_ctrlSponso_Red_Money.setText(getButtonName("lb_coupon_money", Constants.CurrentLang));
        m_ctrlSponso_Title.setText(getButtonName("lab_voucherSponsor", Constants.CurrentLang));
        m_ctrlSponso_Money.setText(getButtonName("lb_coupon_money", Constants.CurrentLang));

        m_ctrlGift_One_Result.addTextChangedListener(amount(m_ctrlGift_One_Result));
        m_ctrlGift_Two_Result.addTextChangedListener(amount(m_ctrlGift_Two_Result));
        m_ctrlSponso_Red_Result.addTextChangedListener(amount(m_ctrlSponso_Red_Result));
        m_ctrlSponso_Result.addTextChangedListener(amount(m_ctrlSponso_Result));


        m_ctrlGift_One_Result.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    m_ctrlGift_One_Result.setSelection(m_ctrlGift_One_Result.getText().toString().length());
            }
        });

        m_ctrlGift_Two_Result.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    m_ctrlGift_Two_Result.setSelection(m_ctrlGift_Two_Result.getText().toString().length());
            }
        });

        m_ctrlSponso_Red_Result.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    m_ctrlSponso_Red_Result.setSelection(m_ctrlSponso_Red_Result.getText().toString().length());
            }
        });

        m_ctrlSponso_Result.setOnFocusChangeListener(new View.OnFocusChangeListener() {
            @Override
            public void onFocusChange(View v, boolean hasFocus) {
                if(hasFocus)
                    m_ctrlSponso_Result.setSelection(m_ctrlSponso_Result.getText().toString().length());
            }
        });

        if(Constants.currentMarket != null)
        {
            m_ctrlGift_One_Result.setText(Constants.currentMarket.market_coupon_1);

            m_ctrlGift_One_Result.extendSelection(Constants.currentMarket.market_coupon_1.length());

            m_ctrlGift_Two_Result.setText(Constants.currentMarket.market_coupon_2);

            m_ctrlGift_Two_Result.extendSelection(Constants.currentMarket.market_coupon_2.length());

            m_ctrlSponso_Result.setText(Constants.currentMarket.market_coupon_Sponsor);

            m_ctrlSponso_Result.extendSelection(Constants.currentMarket.market_coupon_Sponsor.length());

            m_ctrlSponso_Red_Result.setText(Constants.currentMarket.market_coupon_Sponsored);

            m_ctrlSponso_Red_Result.extendSelection(Constants.currentMarket.market_coupon_Sponsored.length());
        }
        customDialog = new CustomProgressDialog(getContext(), "");

        m_ctrlGift_One_Result.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().trim().equals(""))
                {
                    m_ctrlGift_One_Result.setText("0.00");
                }
            }
        });

        m_ctrlGift_Two_Result.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().trim().equals(""))
                {
                    m_ctrlGift_Two_Result.setText("0.00");
                }
            }
        });

        m_ctrlSponso_Red_Result.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().trim().equals(""))
                {
                    m_ctrlSponso_Red_Result.setText("0.00");
                }
            }
        });

        m_ctrlSponso_Result.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().trim().equals(""))
                {
                    m_ctrlSponso_Result.setText("0.00");
                }
            }
        });

        if(Constants.currentMarket != null && Constants.currentMarket.Partner_AllowMailingDiffusion == false)
        {
            m_ctrlBtnMailing.setEnabled(false);
        }else
        {
            if(Constants.currentMarket == null)
                m_ctrlBtnMailing.setEnabled(false);
            else
                m_ctrlBtnMailing.setEnabled(true);
        }

        m_ctrlBtnConfirm.setOnClickListener(this);
        m_ctrlBtnMailing.setOnClickListener(this);

        return view;
    }

    public static TextWatcher amount(final EditText editText) {
        return new TextWatcher() {
            String current = "";

            public void onTextChanged(CharSequence s, int start, int before, int count) {
                if (!s.toString().equals(current)) {
                    editText.removeTextChangedListener(this);

                    String cleanString = s.toString();

                    if (count != 0) {
                        String substr = cleanString.substring(cleanString.length() - 2);

                        if (substr.contains(".") || substr.contains(",")) {
                            cleanString += "0";
                        }
                    }

                    cleanString = cleanString.replaceAll("[.]", "");

                    double parsed = Double.parseDouble(cleanString);
                    DecimalFormat df = new DecimalFormat("0.00");
                    String formatted = df.format((parsed / 100));


                    formatted = formatted.replaceAll("[,]", ".");
                    //String strformatted = "";

                    //strformatted = Integer.parseInt(cleanString.substring(0, cleanString.length() - 2)) + "." + cleanString.substring(cleanString.length() - 2);

                    current = formatted;
                    editText.setText(formatted);
                    editText.setSelection(formatted.length());

                    editText.addTextChangedListener(this);
                }
            }

            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            public void afterTextChanged(Editable s) {}
        };
    }
    @Override
    public void onClick(View v) {

        String strApi = "";
        if(v == m_ctrlBtnConfirm)
        {
            String strOneResult = m_ctrlGift_One_Result.getText().toString().trim();

            if(strOneResult.equals(""))
            {
                strOneResult = "0.00";
            }

            String strTwoResult = m_ctrlGift_Two_Result.getText().toString().trim();

            if(strTwoResult.equals(""))
            {
                strTwoResult = "0.00";
            }

            String strSponse = m_ctrlSponso_Result.getText().toString().trim();

            if(strSponse.equals(""))
            {
                strSponse = "0.00";
            }

            String strSponsed = m_ctrlSponso_Red_Result.getText().toString().trim();

            if(strSponsed.equals(""))
            {
                strSponsed = "0.00";
            }
            strApi = "{Api: 'PartnerVoucherSettings',";
            strApi += "Language:'" + Constants.CurrentLang + "', ";
            strApi += "Partner_PinCode:'" + Constants.codeMarket + "', ";
            strApi += "Partner_PhoneNumber:'" + Constants.codeTel + "', ";
            strApi += "Partner_PhoneCountry:'" + Constants.Current_Country + "', ";
            strApi += "Partner_VoucherValueSponsor:'" + strSponse + "', ";
            strApi += "Partner_VoucherValueSponsored:'" + strSponsed + "', ";
            strApi += "Partner_VoucherValue1:'" + strOneResult + "', ";
            strApi += "Partner_VoucherValue2:'" + strTwoResult + "', ";
            strApi += "ProcessToken:'" + Constants.ProcessToken + "', ";
            strApi += "Application_Token:'" + Constants.Application_Token + "', ";
            strApi += "Application_ServerId:'" + Constants.APPLICATION_SERVER_ID + "', ";
            strApi += "Application_Version:'" + Build.VERSION.RELEASE + "'}";

            customDialog = new CustomProgressDialog(getContext(), "");

            customDialog.show();

            customDialog.setCancelable(false);

            MyDBProcessing processing = new MyDBProcessing();

            processing.SendPost(strApi, vaildHandler);

            Activity act = this.getActivity();

            View view = act.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)this.getContext().getSystemService(this.getContext().INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

        }else
        {
            if(v == m_ctrlBtnMailing)
            {
                strApi += "{Api:'VoucherDiffusionToParse',";
                strApi += "Language:'" + Constants.CurrentLang + "',";
                strApi += "Partner_PinCode:'" + Constants.codeMarket + "',";
                strApi += "Partner_PhoneNumber:'" + Constants.codeTel + "',";
                strApi += "Partner_PhoneCountry:'" + Constants.Current_Country + "',";
                strApi += "ProcessToken:'" + Constants.ProcessToken + "',";
                strApi += "Application_Token:'" + Constants.Application_Token + "',";
                strApi += "Application_ServerId:'" + Constants.APPLICATION_SERVER_ID + "',";
                strApi += "Application_Version:'" + Constants.Application_Version + "'}";

                customDialog = new CustomProgressDialog(getContext(), "");

                customDialog.show();

                customDialog.setCancelable(false);

                MyDBProcessing processing = new MyDBProcessing();

                processing.SendPost(strApi, mailing);

                View view = this.getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)this.getContext().getSystemService(this.getContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }
            }
        }
    }

    Handler mailing = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            String str = (String) msg.obj;

            customDialog.dismiss();
            if (msg.arg2 != Constants.NET_SUC) {

                Toast.makeText(getContext(), getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                vibrate(ViewCoupon.this.getActivity());

                return;
            } else {
                try {
                    MyXmlToJSON(str);
                    interpretMailing();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public void interpretMailing() throws JSONException {

        Constants.ErrorNumber = GetMatchString("ErrorNumber");

        if(!Constants.ErrorNumber.equals("0")) {
            Toast.makeText(getContext(), getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();
            vibrate(ViewCoupon.this.getActivity());
            return;
        }


        JSONObject obj = GETJSONOBJECT();

        if(obj.has("Data")) {

            JSONObject dataObject = obj.getJSONObject("Data");

            Constants.currentMarket.market_MailingVouchersCountToSend = dataObject.getString("Partner_VouchersCountToSend");
            Constants.currentMarket.market_MailingVouchersCostToSend =  dataObject.getString("Partner_VouchersCostToSend");
            Constants.currentMarket.CouponLastTime = dataObject.getString("VoucherDiffusion_ApproximateDuration");
        }

        String strMessage = "";

        strMessage = getButtonName("lab_mailingCoupon", Constants.CurrentLang);

        strMessage += '\n';

        strMessage += getButtonName("lab_value", Constants.CurrentLang) + ":";

        strMessage += Constants.currentMarket.market_coupon_1 ;

        strMessage += getButtonName("lb_coupon_money", Constants.CurrentLang) + '\n' + getButtonName("lab_NBCustomer", Constants.CurrentLang) + ":";

        strMessage += Constants.currentMarket.market_MailingVouchersCountToSend + '\n' + getButtonName("lab_Cost", Constants.CurrentLang) + ":";

        strMessage += Float.parseFloat(Constants.currentMarket.market_voucherfee);
        strMessage += " x ";
        strMessage += Float.parseFloat(Constants.currentMarket.market_MailingVouchersCountToSend) + " = ";
        strMessage += Float.parseFloat(Constants.currentMarket.market_MailingVouchersCostToSend) + getButtonName("lb_coupon_money", Constants.CurrentLang) + '\n';
        strMessage += getButtonName("lab_Timing", Constants.CurrentLang) + ":";
        strMessage += Math.round(Float.parseFloat(Constants.currentMarket.market_MailingVouchersCostToSend) / 60) + "min";

        new MaterialDialog.Builder(getContext()).message(strMessage)
                .positiveText(getButtonName("btn_valider", Constants.CurrentLang))
                .negativeText(getButtonName("btn_annuler", Constants.CurrentLang))
                .buttonCallback(new MaterialDialog.ButtonCallback() {
            @Override
            public void onPositive(MaterialDialog dialog) {
                super.onPositive(dialog);
                //Toast.makeText(getContext(), "OK", Toast.LENGTH_SHORT).show();

                if (!Constants.currentMarket.market_MailingVouchersCountToSend.equals("0")) {

                    Toast.makeText(ViewCoupon.this.getContext(), getButtonName("lab_ProcessingMailing", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                    String strApi = "{Api: 'VoucherDiffusionToExecute',";

                    strApi += "Language:'" + Constants.CurrentLang + "',";
                    strApi += "Partner_PinCode:'" + Constants.codeMarket + "',";
                    strApi += "Partner_PhoneNumber:'" + Constants.codeTel + "',";
                    strApi += "Partner_PhoneCountry:'" + Constants.Current_Country + "',";
                    strApi += "Voucher_Amount:'" + Constants.currentMarket.market_coupon_1 + "',";
                    strApi += "ProcessToken:'" + Constants.ProcessToken + "',";
                    strApi += "Application_Token:'" + Constants.Application_Token + "',";
                    strApi += "Application_ServerId:'" + Constants.APPLICATION_SERVER_ID + "',";
                    strApi += "Application_Version:'" + Constants.Application_Version + "'}";

                    customDialog.show();

                    MyDBProcessing processing = MyDBProcessing.getInstance();

                    processing.SendPost(strApi, VoucherDiffusionToExecute);
                }
                else
                    Toast.makeText(ViewCoupon.this.getContext(), getButtonName("lab_NoCustomer", Constants.CurrentLang), Toast.LENGTH_LONG).show();


            }

            @Override
            public void onNegative(MaterialDialog dialog) {
                super.onNegative(dialog);
                //Toast.makeText(getContext(), "Cancel", Toast.LENGTH_SHORT).show();
            }
        }).show();
    }

    Handler VoucherDiffusionToExecute = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String str = (String) msg.obj;
            customDialog.dismiss();
            if(msg.arg2 != Constants.NET_SUC) {
                Toast.makeText(getContext(), getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                vibrate(ViewCoupon.this.getActivity());

                return;
            }

            try {
                MyXmlToJSON(str);

                interpretVoucherDiffusionToExecute();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public void interpretVoucherDiffusionToExecute() throws JSONException {

        Constants.ErrorNumber = GetMatchString("ErrorNumber");

        if(Constants.ErrorNumber.equals("0")) {
            Constants.ProcessToken = GetMatchString("ProcessToken");

            Toast.makeText(ViewCoupon.this.getContext(), getButtonName("lab_ProcessingOK", Constants.CurrentLang), Toast.LENGTH_LONG).show();

            return;
        }
        if(Constants.ErrorNumber.equals("270")) {
            Toast.makeText(ViewCoupon.this.getContext(), getButtonName("lab_MailingInProcess", Constants.CurrentLang), Toast.LENGTH_LONG).show();
            vibrate(ViewCoupon.this.getActivity());
            return;
        }
        if(Constants.ErrorNumber.equals("3")) {
            Toast.makeText(ViewCoupon.this.getContext(), getButtonName("lab_CB_Error", Constants.CurrentLang), Toast.LENGTH_LONG).show();

            vibrate(ViewCoupon.this.getActivity());

            Intent i = new Intent(ViewCoupon.this.getContext(), ViewCCList.class);

            startActivity(i);

            return;
        }else
            Toast.makeText(ViewCoupon.this.getContext(), getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();
    }
    Handler vaildHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            String str = (String) msg.obj;

            customDialog.dismiss();
            if(msg.arg2 != Constants.NET_SUC)
            {
                Toast.makeText(getContext(), getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                vibrate(ViewCoupon.this.getActivity());

                return;
            }else
            {
                try {

                    MyXmlToJSON(str);

                    interpretValid();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };

    public void interpretValid() throws JSONException {

        Constants.ErrorNumber = GetMatchString("ErrorNumber");

        if(!Constants.ErrorNumber.equals("0"))
        {
            Toast.makeText(getContext(), getButtonName("lab_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();

            vibrate(ViewCoupon.this.getActivity());
        }else
        {
            Toast.makeText(getContext(), getButtonName("lab_update_OK", Constants.CurrentLang), Toast.LENGTH_LONG).show();
        }

        Constants.ProcessToken = GetMatchString("ProcessToken");
        if(Constants.currentMarket != null)
        {
            Constants.currentMarket.market_coupon_Sponsor = m_ctrlSponso_Result.getText().toString().trim();
            Constants.currentMarket.market_coupon_1 = m_ctrlGift_One_Result.getText().toString().trim();
            Constants.currentMarket.market_coupon_2 = m_ctrlGift_Two_Result.getText().toString().trim();
            Constants.currentMarket.market_coupon_Sponsored = m_ctrlSponso_Red_Result.getText().toString().trim();
        }
    }
    public String getButtonName(String strTagName, String strLanguage)
    {
        String strResult = "";

        if(strTagName.equals(""))
            return strResult;


        for(int i = 0; i < Constants.myTitle.size(); i ++)
        {
            MyTitles title = Constants.myTitle.get(i);

            if(title.strTag.equals(strTagName))
            {
                if(strLanguage.equals("FR"))
                    strResult = title.strFrace;
                else
                    strResult = title.strEng;
            }
        }

        return strResult;
    }

    Handler realMailing = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String str = (String) msg.obj;
            customDialog.dismiss();
            if(msg.arg2 != Constants.NET_ERR) {
                Toast.makeText(getContext(), getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                vibrate(ViewCoupon.this.getActivity());

                return;
            }
            try {

                MyXmlToJSON(str);

                interpretRealMailing();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public void interpretRealMailing() throws JSONException {

        Constants.ErrorNumber = GetMatchString("ErrorNumber");

        if(!Constants.ErrorNumber.equals("0"))
        {
            if(Constants.ErrorNumber.equals("270")) {
                Toast.makeText(getContext(), getButtonName("lab_MailingInProcess", Constants.CurrentLang), Toast.LENGTH_LONG).show();
                vibrate(ViewCoupon.this.getActivity());
                return;
            }else if(Constants.ErrorNumber.equals("3")) {
                Toast.makeText(getContext(), getButtonName("lab_CB_Error", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                Intent i = new Intent(getContext(), ViewCCList.class);

                startActivity(i);

                Activity con = (Activity)this.getContext();

                con.finish();
            }else {
                Toast.makeText(getContext(), getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();
                vibrate(ViewCoupon.this.getActivity());
                return;
            }

        }else
        {
            Toast.makeText(getContext(), getButtonName("lab_ProcessingOK", Constants.CurrentLang), Toast.LENGTH_LONG).show();
        }


        Constants.ProcessToken = GetMatchString("ProcessToken");
        if(Constants.currentMarket != null)
        {
            Constants.currentMarket.market_coupon_Sponsor = m_ctrlSponso_Result.getText().toString().trim();
            Constants.currentMarket.market_coupon_1 = m_ctrlGift_One_Result.getText().toString().trim();
            Constants.currentMarket.market_coupon_2 = m_ctrlGift_Two_Result.getText().toString().trim();
            Constants.currentMarket.market_coupon_Sponsored = m_ctrlSponso_Red_Result.getText().toString().trim();
        }

    }
    private void onSendMailing()
    {
        customDialog.dismiss();
        if(Constants.currentMarket != null)
        {
            if(!Constants.currentMarket.market_MailingVouchersCountToSend.equals("0"))
            {
                String strApi = "";

                strApi = "{Api: 'VoucherDiffusionToExecute',";
                strApi += "Language:'" + Constants.CurrentLang + "',";
                strApi += "Partner_PinCode:'" + Constants.codeMarket +"',";
                strApi += "Partner_PhoneNumber:'" +  Constants.codeTel + "',";
                strApi += "Partner_PhoneCountry:'" +  Constants.Current_Country + "',";
                strApi += "Voucher_Amount:'" +  Constants.currentMarket.market_coupon_1 + "',";
                strApi += "ProcessToken:'" +  Constants.ProcessToken + "',";
                strApi += "Application_Token:'" +  Constants.Application_Token + "',";
                strApi += "Application_ServerId:'" +  Constants.APPLICATION_SERVER_ID + "',";
                strApi += "Application_ServerId:'" +  Build.VERSION.RELEASE + "'}";

                customDialog = new CustomProgressDialog(getContext(), "");

                customDialog.show();

                customDialog.setCancelable(false);

                View view = this.getActivity().getCurrentFocus();
                if (view != null) {
                    InputMethodManager imm = (InputMethodManager)this.getContext().getSystemService(this.getContext().INPUT_METHOD_SERVICE);
                    imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                }

                MyDBProcessing procss = new MyDBProcessing();
                procss.SendPost(strApi, realMailing);
            }else
            {
                Toast.makeText(getContext(), getButtonName("lab_NoCustomer", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                vibrate(ViewCoupon.this.getActivity());

                return;
            }
        }else
        {
            Toast.makeText(getContext(), getButtonName("lab_NoCustomer", Constants.CurrentLang), Toast.LENGTH_LONG).show();

            vibrate(ViewCoupon.this.getActivity());

            return;
        }

    }

}
