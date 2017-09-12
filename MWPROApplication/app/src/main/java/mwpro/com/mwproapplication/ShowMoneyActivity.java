package mwpro.com.mwproapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.location.Criteria;
import android.location.Location;
import android.location.LocationManager;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v4.app.ActivityCompat;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.location.LocationListener;
import com.pepperonas.materialdialog.MaterialDialog;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.text.DecimalFormat;

import mwpro.com.mwproapplication.data.Card;
import mwpro.com.mwproapplication.data.MyTitles;
import mwpro.com.mwproapplication.data.UserVo;
import mwpro.com.mwproapplication.ui.CustomProgressDialog;
import retrofit2.http.GET;

import static mwpro.com.mwproapplication.Constants.GETJSONOBJECT;
import static mwpro.com.mwproapplication.Constants.GetMatchString;
import static mwpro.com.mwproapplication.Constants.MyXmlToJSON;
import static mwpro.com.mwproapplication.Constants.vibrate;

public class ShowMoneyActivity extends Activity implements View.OnClickListener, LocationListener {

    ImageView m_ctrlBannerButton = null;
    TextView m_ctrlExternalTag = null;
    TextView m_ctrlMarket_company = null;
    EditText m_ctrlMarket_money = null;
    TextInputLayout m_ctrlMarket_money_help = null;
    EditText m_ctrlMarket_telephony = null;
    TextInputLayout m_ctrlMarket_telephony_help = null;
    Button m_ctrlMoney_btn_valider = null;
    Button m_ctrlMoney_btn_membre = null;
    Button m_ctrlCoupon1 = null;
    Button m_ctrlCoupon2 = null;
    private String provider;
    private LocationManager locationManager;
    private float fOldat = 0;
    private float fOldLon = 0;
    private boolean bEditFlag = false;
    CustomProgressDialog customProgressDialog = null;
    private int nCursorPos = 0;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_show_money);

        m_ctrlBannerButton = (ImageView) findViewById(R.id.banner_button);
        m_ctrlExternalTag = (TextView) findViewById(R.id.extenalTag);
        m_ctrlMarket_company = (TextView) findViewById(R.id.market_company);
        m_ctrlMarket_money = (EditText) findViewById(R.id.market_money);
        m_ctrlMarket_money_help = (TextInputLayout) findViewById(R.id.market_money_help);
        m_ctrlMarket_telephony = (EditText) findViewById(R.id.market_telephony);
        m_ctrlMarket_telephony_help = (TextInputLayout) findViewById(R.id.market_telephony_help);
        m_ctrlMoney_btn_valider = (Button) findViewById(R.id.money_btn_valider);
        m_ctrlMoney_btn_membre = (Button) findViewById(R.id.money_btn_membre);
        m_ctrlCoupon1 = (Button) findViewById(R.id.coupon1);
        m_ctrlCoupon2 = (Button) findViewById(R.id.coupon2);

        m_ctrlCoupon1.setVisibility(View.GONE);
        m_ctrlCoupon2.setVisibility(View.GONE);
        m_ctrlMoney_btn_valider.setEnabled(false);

        Constants.viewVipText1 = getButtonName("lab_not_membre", Constants.CurrentLang);
        Constants.viewVipText2 = getButtonName("lab_creer_vip", Constants.CurrentLang);

        customProgressDialog = new CustomProgressDialog(this, "");

        m_ctrlMarket_money.addTextChangedListener(amount(m_ctrlMarket_money));
        if(Constants.currentMarket.market_activity == 2606)
            m_ctrlMoney_btn_membre.setVisibility(View.GONE);
        else
            m_ctrlMoney_btn_membre.setVisibility(View.VISIBLE);
        m_ctrlMarket_telephony.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {


            }
            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }
            @Override
            public void afterTextChanged(Editable s) {
                String strMoney = s.toString();
                String strTelephony = m_ctrlMarket_telephony.getText().toString();
                if (strMoney.equals("") || strTelephony.equals(""))
                    m_ctrlMoney_btn_valider.setEnabled(false);
                else
                    m_ctrlMoney_btn_valider.setEnabled(true);
            }

        });

        m_ctrlBannerButton.setOnClickListener(this);

        m_ctrlMoney_btn_valider.setOnClickListener(this);

        Constants.currentMontant = 0;

        if(Constants.currentMarket.market_taxi == true) //taxi
        {
            Geo();
        }

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ShowMoneyActivity.this);

        if(Constants.currentMarket.market_status.equals("0"))
        {
            m_ctrlExternalTag.setText(Constants.currentMarket.market_ExternalTag + ":" + getButtonName("lab_demo", Constants.CurrentLang) + formatDate(Constants.currentMarket.market_expdemo, Constants.CurrentLang));
        }else
            m_ctrlExternalTag.setText(Constants.currentMarket.market_ExternalTag + ":" + getButtonName("lab_active", Constants.CurrentLang));

        Constants.currentUser = new UserVo();

        Constants.ExecutePaymentAfterVip = false;

        boolean bFirst = false;
        boolean bSecond = false;
        if(Float.parseFloat(Constants.currentMarket.market_coupon_1.replace(",", ".")) > 0)
        {
            m_ctrlCoupon1.setVisibility(View.VISIBLE);

            bFirst = true;
        }else
        {
            m_ctrlCoupon1.setVisibility(View.GONE);
        }
        if(Float.parseFloat(Constants.currentMarket.market_coupon_2.replace(",", ".")) > 0)
        {
            m_ctrlCoupon2.setVisibility(View.VISIBLE);

            bSecond = true;
        }else
        {
            m_ctrlCoupon2.setVisibility(View.GONE);
        }

        if(bFirst && bSecond)
        {
            findViewById(R.id.free_space).setVisibility(View.VISIBLE);
        }else
        {
            findViewById(R.id.free_space).setVisibility(View.GONE);
        }

        m_ctrlCoupon1.setText(getButtonName("lb_coupon",  Constants.CurrentLang) + " " + Constants.currentMarket.market_coupon_1 + getButtonName("lb_coupon_money", Constants.CurrentLang));
        m_ctrlCoupon2.setText(getButtonName("lb_coupon",  Constants.CurrentLang) + " " + Constants.currentMarket.market_coupon_2 + getButtonName("lb_coupon_money", Constants.CurrentLang));
        m_ctrlMarket_telephony_help.setHint(getButtonName("lab_tel",  Constants.CurrentLang));
        m_ctrlMarket_money_help.setHint(getButtonName("lab_montant",  Constants.CurrentLang));
        m_ctrlMoney_btn_valider.setText(getButtonName("btn_scanner",  Constants.CurrentLang));
        m_ctrlMoney_btn_membre.setText(getButtonName("btn_vip",  Constants.CurrentLang));
        m_ctrlMarket_company.setText(Constants.currentMarket.market_name);
        m_ctrlCoupon1.setOnClickListener(this);
        m_ctrlCoupon2.setOnClickListener(this);
        m_ctrlMoney_btn_membre.setOnClickListener(this);
        Constants.ExecutePaymentAfterVip = false;
        m_ctrlMarket_money.setSelection(m_ctrlMarket_money.getText().toString().length());

        m_ctrlMarket_money.requestFocus();
    }

    public String formatDate(String date, String pays)
    {
        String strResult = "";

        String strDate = date;

        int nPos = strDate.indexOf("/");

        String strDay = "";
        String strMonth = "";
        String strYear = "";

        strDay = strDate.substring(0, nPos);

        strDate = strDate.substring(nPos + 1);

        nPos = strDate.indexOf("/");

        strMonth = strDate.substring(0, nPos);

        strYear = strDate.substring(nPos + 1);

        if(strDay.length() == 1)
            strDay = "0" + strDay;

        if(strMonth.length() == 1)
            strMonth = "0" + strMonth;

        if(pays.toUpperCase().equals("FR"))
        {
            return strDay + "/" + strMonth + "/" + strYear;
        }else
        {
            return strYear + "/" + strMonth + "/" + strDay;
        }

    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent i = new Intent(ShowMoneyActivity.this, MainActivity.class);

        startActivity(i);

        finish();
    }

    public String getButtonName(String strTagName, String strLanguage)
    {
        String strResult = "";

        if(strTagName.equals(""))
            return strResult;

        for(int i = 0; i < Constants.myTitle.size(); i ++)
        {
            MyTitles title = Constants.myTitle.get(i);

            if(title.strTag.equals(strTagName)) {
                if (strLanguage.equals("FR"))
                    strResult = title.strFrace;
                else
                    strResult = title.strUS;

                break;
            }
        }
        return strResult;
    }
    public void Geo()
    {
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);

        if(locationManager != null)
        {
            Criteria criteria = new Criteria();
            provider = locationManager.getBestProvider(criteria, false);
            if (ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, android.Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {

                return;
            }
            Location location = locationManager.getLastKnownLocation(provider);

            if(location != null)
                location.setTime(3000);
        }
    }
    @Override
    public void onClick(View v) {
       switch (v.getId())
       {
           case R.id.banner_button:
               Intent i = new Intent(ShowMoneyActivity.this, ViewAdmin.class);

               startActivity(i);

               finish();
               break;
           case R.id.money_btn_valider:

                //Toast.makeText(ShowMoneyActivity.this, "Hello world start1", Toast.LENGTH_LONG).show();
               if(Float.parseFloat(m_ctrlMarket_money.getText().toString()) > 0)
               {
                    //Toast.makeText(ShowMoneyActivity.this, "Hello world start2", Toast.LENGTH_LONG).show();
                    Constants.ExecutePaymentAfterVip = true;
                    Constants.currentMontant = Float.parseFloat(m_ctrlMarket_money.getText().toString());
                    Constants.currentUser.User_AccessKey = m_ctrlMarket_telephony.getText().toString();
                    Constants.CCToken = "";
                    MyDBProcessing processing = new MyDBProcessing();

                    String strApi = "{Api: 'UserParse',";
                    strApi += "Language:'" + Constants.CurrentLang + "',";
                    strApi += "Partner_PinCode: '" + Constants.codeMarket +"',";
                    strApi += "User_PhoneNumber:'',";
                    strApi += "Partner_PhoneNumber: '" + Constants.codeTel +"', ";
                    strApi += "Partner_PhoneCountry: '" + Constants.Current_Country +"', ";
                    strApi += "User_PhoneCountry: '" + Constants.Current_Country +"', ";
                    strApi += "User_AccessKey: '" + Constants.currentUser.User_AccessKey +"', ";
                    strApi += "User_AccessKeyType: '" + Constants.currentUser.User_AccessKeyType +"', ";
                    strApi += "Transaction_Amount: '" +Constants.currentMontant +"', ";
                    strApi += "ProcessToken: '" +Constants.ProcessToken +"', ";
                    strApi += "Application_Token: '" +Constants.Application_Token +"', ";
                    strApi += "Application_ServerId: '" +Constants.APPLICATION_SERVER_ID +"', ";
                    strApi += "Application_Version: '" +Constants.Application_Version +"'}";

                   //Toast.makeText(ShowMoneyActivity.this, strApi, Toast.LENGTH_LONG).show();
                   customProgressDialog.show();

                   customProgressDialog.setCancelable(false);
                   //Toast.makeText(ShowMoneyActivity.this, "Hello world start3", Toast.LENGTH_LONG).show();
                   processing.SendPost(strApi, validerHandler);

                   View view = this.getCurrentFocus();
                   if (view != null) {
                       InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                       imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
                   }

                   //Toast.makeText(ShowMoneyActivity.this, "Hello world start4", Toast.LENGTH_LONG).show();
               }
               break;
           case R.id.money_btn_membre:
               Intent k = new Intent(ShowMoneyActivity.this, VIPActivity.class);

               startActivity(k);

               finish();
               break;
           case R.id.coupon1:
               Intent is = new Intent(ShowMoneyActivity.this, ViewSelectPhoneActivity.class);

               is.putExtra("CouponNumber", "1");

               startActivity(is);

               finish();
               break;
           case R.id.coupon2:
               Intent iis = new Intent(ShowMoneyActivity.this, ViewSelectPhoneActivity.class);
               iis.putExtra("CouponNumber", "2");
               startActivity(iis);
               finish();
               break;
       }
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
    Handler validerHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            String str = (String)msg.obj;
            customProgressDialog.dismiss();
            if(msg.arg2 != Constants.NET_ERR)
            {
                try {
                    MyXmlToJSON(str);
                    interpertValider();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else
            {
                Toast.makeText(ShowMoneyActivity.this, getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                vibrate(ShowMoneyActivity.this);

                return;
            }
        }
    };

    public void interpertValider() throws JSONException {

        String strTagName = "";
        String strUserPhoneNumber = "";
        String strUserPhoneCountry = "";
        String strProcessToken = "";
        String Partner_Insolvent = "false";

        Constants.ErrorNumber = GetMatchString("ErrorNumber");

        if(Constants.ErrorNumber.equals("23"))
        {
            Toast.makeText(ShowMoneyActivity.this, getButtonName("lb_market_off", Constants.CurrentLang), Toast.LENGTH_LONG).show();

            vibrate(ShowMoneyActivity.this);

            return;
        }

        if(Constants.ErrorNumber.equals("22"))
        {
            vibrate(ShowMoneyActivity.this);
            Intent i = new Intent(ShowMoneyActivity.this, ViewNFCAssign.class);

            startActivity(i);

            finish();

            return;
        }

        if(Constants.ErrorNumber.equals("241") || Constants.ErrorNumber.equals("21")|| Constants.ErrorNumber.equals("9"))
        {
            Toast.makeText(ShowMoneyActivity.this, getButtonName("lb_AccessKeyUnknown", Constants.CurrentLang), Toast.LENGTH_LONG).show();
            vibrate(ShowMoneyActivity.this);
            return;
        }

        if(Constants.ErrorNumber.equals("243"))
        {
            Toast.makeText(ShowMoneyActivity.this, getButtonName("lab_DisabledUser", Constants.CurrentLang), Toast.LENGTH_LONG).show();
            vibrate(ShowMoneyActivity.this);
            return;
        }

        if(Constants.ErrorNumber.equals("20"))
        {
            showUserAddNew();
            vibrate(ShowMoneyActivity.this);
            return;
        }

        if(!Constants.ErrorNumber.equals("0"))
        {
            Toast.makeText(ShowMoneyActivity.this, getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();
            vibrate(ShowMoneyActivity.this);
            return;
        }

        strUserPhoneNumber = GetMatchString("User_PhoneNumber");
        strUserPhoneCountry = GetMatchString("User_PhoneCountry");
        Constants.ProcessToken = strProcessToken = GetMatchString("ProcessToken");
        Partner_Insolvent = GetMatchString("Partner_Insolvent");

        Constants.currentUser = new UserVo(GETJSONOBJECT());

        Constants.currentUser.user_phone = strUserPhoneNumber;
        Constants.currentUser.User_PhoneCountry = strUserPhoneCountry;

        if(Partner_Insolvent.toLowerCase().equals("true"))
        {
            Constants.currentMarket.market_Insolvent = "true";

            MyDBProcessing processing = new MyDBProcessing();

            customProgressDialog.show();

            customProgressDialog.setCancelable(false);

            processing.SendPost(CreditCardsListJSON(), CreditHandler);

            return;
        }else
        {
            if(Constants.currentMarket.market_activity == 2606)
            {
                Constants.currentUser.user_type = 1;

                executePayment(Constants.currentMontant, 0 , 0, "", 0, "");

                return;
            }else
            {
                Constants.viewVipText1 = getButtonName("lab_membre", Constants.CurrentLang) + " " + Constants.currentUser.user_phone;

                Intent i = new Intent(ShowMoneyActivity.this, ViewStatusActivity.class);

                startActivity(i);

                return;
            }
        }
    }
    public void CreditInterpret() throws XmlPullParserException, IOException, JSONException {

        Constants.ErrorNumber = GetMatchString("ErrorNumber");

        if(Constants.ErrorNumber.equals("11"))
        {
            Toast.makeText(ShowMoneyActivity.this, getButtonName("lab_errorAssist", Constants.CurrentLang), Toast.LENGTH_LONG).show();
            vibrate(ShowMoneyActivity.this);
            return;
        }
        if(!Constants.ErrorNumber.equals("0"))
        {
            Constants.currentMarket.market_cb = false;
            if(Constants.currentMarket.market_Cluster.equals("0") && Constants.currentMarket.market_Insolvent.equals("false"))
            {
                vibrate(ShowMoneyActivity.this);

                Intent i = new Intent(ShowMoneyActivity.this, ShowMoneyActivity.class);

                startActivity(i);

                finish();

                return;
            }else if(Constants.currentMarket.market_BalanceColor == null || Constants.currentMarket.market_BalanceColor.equals("") || Constants.currentMarket.market_BalanceColor.equals("Green"))
            {
                vibrate(ShowMoneyActivity.this);

                Intent i = new Intent(ShowMoneyActivity.this, ShowMoneyActivity.class);

                startActivity(i);

                finish();

                return;
            }else
            {

                Toast.makeText(ShowMoneyActivity.this, getButtonName("lab_ActiviateAccount", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                vibrate(ShowMoneyActivity.this);

                Intent i = new Intent(ShowMoneyActivity.this, ViewCCPay.class);

                startActivity(i);

                finish();

                return;
            }
        }


        Constants.ProcessToken = GetMatchString("ProcessToken");

        if(GETJSONOBJECT().has("Data"))
        {
            JSONObject data = GETJSONOBJECT().getJSONObject("Data");

            if(data.has("Row"))
            {
                Object object = data.get("Row");

                if(object instanceof JSONArray)
                {
                    JSONArray array = data.getJSONArray("Row");
                    for(int i = 0; i < array.length(); i ++)
                    {
                        JSONObject obj = array.getJSONObject(i);
                        Constants.currentMarket.card.add(new Card());
                        if(obj.has("Token"))
                        {
                            Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).nIndex = Constants.currentMarket.card.size() - 1;
                            Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strToken = obj.getString("Token");
                        }
                        if(obj.has("Tag"))
                        {
                            Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strTag = obj.getString("Tag");

                            if(Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strTag.length() == 0)
                            {
                                Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strTagMini = "";
                            }else
                            {
                                Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strTagMini = Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strTag.substring(0, 1).toUpperCase();
                            }
                        }
                        if(obj.has("Main"))
                        {
                            Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strMain = obj.getString("Main");
                        }
                        if (obj.has("Type")) {
                            Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strType = obj.getString("Type");
                            String strType = Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strType;
                            if(strType.toUpperCase().equals("MASTERCARD"))
                            {
                                Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).nImage = R.drawable.mastercard;
                            }
                            if(strType.toUpperCase().equals("VISA"))
                            {
                                Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).nImage = R.drawable.visa;
                            }
                            if(strType.toUpperCase().equals("CB"))
                            {
                                Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).nImage = R.drawable.cb;
                            }
                            if(strType.toUpperCase().equals("AMEX"))
                            {
                                Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).nImage = R.drawable.amex;
                            }
                            if(strType.toUpperCase().equals("DINERS"))
                            {
                                Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).nImage = R.drawable.diners;
                            }
                        }
                        if (obj.has("Number")) {
                            Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strNumber = obj.getString("Number");
                            Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strNumberMini = Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strNumber.substring(Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strNumber.length() - 8);
                        }
                        if (obj.has("Month")) {
                            Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strMonth = obj.getString("Month");
                        }

                        if (obj.has("Year")) {

                            Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strYear = obj.getString("Year");
                            Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strExpDate = Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strMonth + "/" + Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strYear.substring(2, 4);
                        }
                    }
                }else if(object instanceof JSONObject)
                {
                    JSONObject obj = data.getJSONObject("Row");
                    Constants.currentMarket.card.add(new Card());
                    if(obj.has("Token"))
                    {
                        Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).nIndex = Constants.currentMarket.card.size() - 1;
                        Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strToken = obj.getString("Token");
                    }
                    if(obj.has("Tag"))
                    {
                        Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strTag = obj.getString("Tag");

                        if(Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strTag.length() == 0)
                        {
                            Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strTagMini = "";
                        }else
                        {
                            Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strTagMini = Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strTag.substring(0, 1).toUpperCase();
                        }
                    }
                    if(obj.has("Main"))
                    {
                        Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strMain = obj.getString("Main");
                    }
                    if (obj.has("Type")) {
                        Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strType = obj.getString("Type");
                        String strType = Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strType;
                        if(strType.toUpperCase().equals("MASTERCARD"))
                        {
                            Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).nImage = R.drawable.mastercard;
                        }
                        if(strType.toUpperCase().equals("VISA"))
                        {
                            Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).nImage = R.drawable.visa;
                        }
                        if(strType.toUpperCase().equals("CB"))
                        {
                            Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).nImage = R.drawable.cb;
                        }
                        if(strType.toUpperCase().equals("AMEX"))
                        {
                            Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).nImage = R.drawable.amex;
                        }
                        if(strType.toUpperCase().equals("DINERS"))
                        {
                            Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).nImage = R.drawable.diners;
                        }
                    }
                    if (obj.has("Number")) {
                        Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strNumber = obj.getString("Number");
                        Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strNumberMini = Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strNumber.substring(Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strNumber.length() - 8);
                    }
                    if (obj.has("Month")) {
                        Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strMonth = obj.getString("Month");
                    }

                    if (obj.has("Year")) {

                        Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strYear = obj.getString("Year");
                        Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strExpDate = Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strMonth + "/" + Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strYear.substring(2, 4);
                    }
                }
            }
        }

        Constants.currentMarket.market_cb = true;

        if(Constants.currentMarket.market_ToPay > 0 && Float.parseFloat(Constants.currentMarket.market_demotrial) == 0 || Constants.currentMarket.market_demotrial == null){

            Constants.AmountCB = Constants.currentMarket.market_ToPay;

            Toast.makeText(ShowMoneyActivity.this, getButtonName("lab_ToPay", Constants.CurrentLang), Toast.LENGTH_LONG).show();

            Intent i = new Intent(ShowMoneyActivity.this, ViewCCPay.class);

            startActivity(i);

            finish();

            return;
        }else
        {
            //goto Show Money
            Intent i = new Intent(ShowMoneyActivity.this, ShowMoneyActivity.class);

            startActivity(i);

            finish();

            return;
        }

    }

    Handler executePaymentHandle = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            String str = (String)msg.obj;

            customProgressDialog.dismiss();

            if(msg.arg2 != Constants.NET_ERR)
            {
                try {
                    MyXmlToJSON(str);
                    executePaymentInterpret();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else
            {
                Toast.makeText(ShowMoneyActivity.this, getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                vibrate(ShowMoneyActivity.this);
                Intent i = new Intent(ShowMoneyActivity.this, ViewStatusActivity.class);

                startActivity(i);

                finish();
            }
        }
    };
    public void executePaymentInterpret() throws JSONException {

        Constants.ErrorNumber = GetMatchString("ErrorNumber");

        if (Constants.ErrorNumber.equals("200"))   // test si Partner Insolvent et en théorie pas de règlement par CB car la CB recrédite
        {
            vibrate(ShowMoneyActivity.this);
            if (Constants.currentMarket.market_cb == false) {

                Toast.makeText(ShowMoneyActivity.this, getButtonName("lab_ValidAccount", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                Constants.AmountCB = Constants.currentMarket.market_ToPay;

                Intent i = new Intent(ShowMoneyActivity.this, ViewCreditCard.class);

                startActivity(i);

                finish();

                return;

            }
            else {
                Toast.makeText(ShowMoneyActivity.this, getButtonName("lab_depositError", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                return;
            }
        }

        Constants.Message = GetMatchString("Message");
        Constants.ProcessToken = GetMatchString("ProcessToken");

        if (Constants.ErrorNumber.equals("0")) {

            Toast.makeText(ShowMoneyActivity.this, getButtonName("lab_credit_ok", Constants.CurrentLang), Toast.LENGTH_LONG).show();

            Constants.currentMarket.market_ToPay = 0;


            Intent i = new Intent(ShowMoneyActivity.this, ShowMoneyActivity.class);

            startActivity(i);

            finish();
        }
        else if (Constants.ErrorNumber.equals("250"))   // test si erreur Payline - code Payline dans Message
        {
            String[] arrays = Constants.Message.split(" : ");
            vibrate(ShowMoneyActivity.this);
            Toast.makeText(ShowMoneyActivity.this, arrays[1], Toast.LENGTH_LONG).show();

            return;
        }
        else if (Constants.ErrorNumber.equals("257") || Constants.ErrorNumber.equals("256")) {
            vibrate(ShowMoneyActivity.this);
            Toast.makeText(ShowMoneyActivity.this, Constants.Message, Toast.LENGTH_LONG).show();

            Constants.currentUser.cards.remove(Constants.CCIndex);
            Intent i = new Intent(ShowMoneyActivity.this, ViewCCPay.class);

            startActivity(i);

            finish();
        }
        else {
            vibrate(ShowMoneyActivity.this);
            Toast.makeText(ShowMoneyActivity.this, Constants.Message, Toast.LENGTH_LONG).show();

            Intent i = new Intent(ShowMoneyActivity.this, ShowMoneyActivity.class);

            startActivity(i);

            finish();
        }
    }
    public void executePayment(float pTotalPrice, float pUsedPrice, float pTag, String percentage, float UsedCB, String CodePin){
        Constants.CurrentTotalAmount = pTotalPrice;
        Constants.CurrentUsedPrice = pUsedPrice;
        Constants.CurrentPercentage = percentage;
        Constants.CurrentUsedCB = UsedCB;
        Constants.CurrentTag = pTag;

        if (Constants.PaylineRetry == 0)
            Constants.viewVipText1 = getButtonName("lab_processTransac", Constants.CurrentLang);

        Constants.PaylineRetry += 1;

        String strApi = "";

        strApi += "{Api:'TransactionAddNewCashBack',";
        strApi += "Partner_PinCode:'" + Constants.currentMarket.market_code + "',";
        strApi += "Partner_PinCode:'" + Constants.currentMarket.market_ExternalTag + "',";
        strApi += "User_PhoneNumber:'" + Constants.currentUser.user_phone + "',";
        strApi += "User_PhoneCountry:'" + Constants.currentUser.User_PhoneCountry + "',";
        strApi += "User_PinCode:'" + CodePin + "',";
        strApi += "Transaction_Amount:'" + Constants.CurrentTotalAmount + "',";
        strApi += "Transaction_AmountUsed:'" + Constants.CurrentUsedPrice + "',";
        strApi += "Transaction_AmountCashBack:'" + Constants.currentUser.AmountCashBack + "',";
        strApi += "User_Type:'" + Constants.currentUser.user_type + "',";
        strApi += "User_Percentage:'" + Constants.CurrentPercentage + "',";
        strApi += "Partner_PhoneNumber:'" + Constants.codeTel + "',";
        strApi += "Partner_PhoneCountry:'" + Constants.Current_Country + "',";
        strApi += "Transaction_AmountPaid:'" + UsedCB + "',";
        strApi += "ProcessToken:'" + Constants.ProcessToken + "',";
        strApi += "Application_Token:'" + Constants.Application_Token + "',";
        strApi += "Application_ServerId:'" + Constants.APPLICATION_SERVER_ID + "',";
        strApi += "Application_Version:'" + Build.VERSION.RELEASE + "',";
        strApi += "CreditCard_Token:'" + Constants.CCToken + "'}";

        MyDBProcessing processing = new MyDBProcessing();

        customProgressDialog.show();

        customProgressDialog.setCancelable(false);

        processing.SendPost(strApi, executePaymentHandle);


    }
    private Handler CreditHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            String str = (String)msg.obj;
            customProgressDialog.dismiss();
            try {
                MyXmlToJSON(str);

                CreditInterpret();

            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    public String CreditCardsListJSON()
    {
        String strApi = "";

        strApi = "{Api:'CreditCardsList', Account_WalletId:'" +Constants.currentMarket.market_WalletId+ "',";
        strApi += "Account_PhoneNumber:'" + Constants.codeTel +"',";
        strApi += "Account_PhoneCountry:'" + Constants.currentMarket.market_country +"',";
        strApi += "ProcessToken: '" + Constants.ProcessToken +"',";
        strApi += "Application_Token: 'cc3d6bbcfa281ed05aba24540f6fb1503de0c952eb',";
        strApi += "Application_ServerId: '" + Constants.APPLICATION_SERVER_ID +"',";
        strApi += "Application_Version: '" + Constants.Application_Version +"'}";

        return strApi;
    }
    @Override
    public void onLocationChanged(Location location) {
        //Show Progress

        if(location.getAccuracy() <= 100 && (location.getLatitude() - fOldat > 0.001 || location.getLongitude() - fOldLon > 0.001))
        {
            SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ShowMoneyActivity.this);

            SharedPreferences.Editor edit = preferences.edit();

            edit.putString("Phone_number", Constants.PHONE_NUMBER);

            edit.putFloat("Latitude", (float) location.getLatitude());
            edit.putFloat("Longitude", (float) location.getLongitude());

            edit.commit();
        }
    }

    public void showUserAddNew()
    {
        new MaterialDialog.Builder(ShowMoneyActivity.this).message(getButtonName("lb_AddNewUser", Constants.CurrentLang))
            .positiveText(getButtonName("btn_oui", Constants.CurrentLang))
            .negativeText(getButtonName("btn_non", Constants.CurrentLang))
            .buttonCallback(new MaterialDialog.ButtonCallback() {
                @Override
                public void onPositive(MaterialDialog dialog) {
                    super.onPositive(dialog);

                    Constants.currentUser.user_phone = m_ctrlMarket_telephony.getText().toString();

                    Constants.viewVipText1 = getButtonName("lab_not_membre", Constants.CurrentLang) + Constants.currentUser.user_phone;
                    Constants.viewVipText2 = getButtonName("lab_creer_vip", Constants.CurrentLang);

                    Intent i = new Intent(ShowMoneyActivity.this, VIPActivity.class);
                    startActivity(i);
                    finish();
                    dialog.dismiss();
                }

                @Override
                public void onNegative(MaterialDialog dialog) {
                    super.onNegative(dialog);
                    dialog.dismiss();
                }
            }).show();
    }

    @Override
    public boolean dispatchTouchEvent(MotionEvent event) {
        View view = getCurrentFocus();
        boolean ret = super.dispatchTouchEvent(event);

        if (view instanceof EditText) {
            View w = getCurrentFocus();
            int scrcoords[] = new int[2];
            w.getLocationOnScreen(scrcoords);
            float x = event.getRawX() + w.getLeft() - scrcoords[0];
            float y = event.getRawY() + w.getTop() - scrcoords[1];

            if (event.getAction() == MotionEvent.ACTION_UP
                    && (x < w.getLeft() || x >= w.getRight()
                    || y < w.getTop() || y > w.getBottom()) ) {
                InputMethodManager imm = (InputMethodManager)getSystemService(Context.INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(getWindow().getCurrentFocus().getWindowToken(), 0);
            }
        }
        return ret;
    }
}
