package mwpro.com.mwproapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.KeyEvent;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.Toast;
import com.kyleduo.switchbutton.SwitchButton;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import mwpro.com.mwproapplication.data.Card;
import mwpro.com.mwproapplication.data.MarketVo;
import mwpro.com.mwproapplication.data.MyTitles;
import mwpro.com.mwproapplication.ui.CustomAdapter;
import mwpro.com.mwproapplication.ui.CustomProgressDialog;

import static mwpro.com.mwproapplication.Constants.GETJSONOBJECT;
import static mwpro.com.mwproapplication.Constants.GetMatchString;
import static mwpro.com.mwproapplication.Constants.MyXmlToJSON;
import static mwpro.com.mwproapplication.Constants.codeMarket;
import static mwpro.com.mwproapplication.Constants.vibrate;

public class MainActivity extends Activity implements View.OnClickListener{

    EditText m_ctrlPhone_number = null;
    EditText m_ctrlPincode = null;
    boolean bKeyFlag = false;
    Button m_ctrlBtnLogin = null;
    Button m_ctrlBtnSign = null;
    Button m_ctrlBtnPincode = null;
    Button m_ctrlBtnActivate = null;


    SwitchButton m_ctrlBtnLanguage = null;
    SharedPreferences preferences = null;
    ImageView m_ctrlBannerButton = null;

    public final String LOGINTITLE = "btn_login";
    public final String PINCODETITLE = "btn_SendPinCode";
    public final String SIGNUPTITLE = "btn_SignIn";
    public final String ACTIVATE = "btn_cc";
    public final String LTELEPHONE = "lab_Phone";
    public final String LPINCODE = "lab_params";
    public int nLoginNumber = -1;
    public int nPinCodeNumber = -1;
    public int nSignUpNumber = -1;
    public int nActivateNumber = -1;
    public int nLTelePhone = -1;
    public int nLPincode = -1;
    CustomProgressDialog dialog = null;
    Spinner m_ctrlSelLang = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        dialog = new CustomProgressDialog(MainActivity.this, "");
        m_ctrlPhone_number = (EditText)findViewById(R.id.phone_number);
        m_ctrlPincode = (EditText)findViewById(R.id.pincode);
        m_ctrlBtnLogin = (Button)findViewById(R.id.btn_login);
        m_ctrlBtnSign = (Button)findViewById(R.id.btn_signup);
        m_ctrlBtnPincode = (Button)findViewById(R.id.btn_pincode);
        m_ctrlBtnActivate = (Button)findViewById(R.id.btn_activate);
        m_ctrlBtnLanguage = (SwitchButton)findViewById(R.id.sb_text_state);
        m_ctrlBannerButton = (ImageView)findViewById(R.id.banner_button);


        TextInputLayout layout = (TextInputLayout)findViewById(R.id.phone_number_help);
        TextInputLayout pin_layout = (TextInputLayout)findViewById(R.id.phone_pin_help);
        layout.setHint(getButtonName(LTELEPHONE, Constants.CurrentLang));
        pin_layout.setHint(getButtonName(LPINCODE, Constants.CurrentLang));

        preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);
        String strPhoneNumber = Constants.codeTel;
        String strPincode = Constants.codeMarket;

        m_ctrlPhone_number.setText(strPhoneNumber);
        m_ctrlPincode.setText(strPincode);

        Constants.Current_Country = preferences.getString("CURRENT_COUNTRY", "FR");
        Constants.CurrentLang = preferences.getString("CURRENT_LANG", "FR");
        Constants.Application_Version = preferences.getString("APPLICATIONVERSION", Build.VERSION.RELEASE);
        Constants.codeMarket = preferences.getString(Constants.PINCODE, "");
        Constants.codeTel = preferences.getString(Constants.PHONE_NUMBER, "");

        m_ctrlSelLang = (Spinner) findViewById(R.id.spinner);
        //spin.setOnItemSelectedListener(this);
        String[] countryNames={"France","English"};
        int flags[] = {R.drawable.france, R.drawable.america};
        CustomAdapter customAdapter=new CustomAdapter(getApplicationContext(),flags,countryNames);

        m_ctrlSelLang.setAdapter(customAdapter);

        if(strPhoneNumber.equals(Constants.EMPTY) || strPincode.equals(Constants.EMPTY))
        {
            m_ctrlBtnLogin.setEnabled(false);
        }else
        {
            m_ctrlBtnLogin.setEnabled(true);
        }

        initUI();

        m_ctrlPhone_number.setOnTouchListener(new View.OnTouchListener() {
            @Override
            public boolean onTouch(View v, MotionEvent event) {
                if(event.getAction() == KeyEvent.ACTION_DOWN)
                {
                    if(bKeyFlag == true)
                        return true;
                    if(v.isInEditMode())
                    {
                        return false;
                    }else
                    {
                        bKeyFlag = true;

                        return true;
                    }
                }

                if(event.getAction() == KeyEvent.ACTION_UP)
                {
                    if(bKeyFlag == true) {
                        bKeyFlag = false;
                    }else
                        return false;

                    if(event.getActionIndex() == event.getPointerCount() - 1)
                    {
                        v.requestFocus();

                        String str = m_ctrlPhone_number.getText().toString();
                        m_ctrlPhone_number.extendSelection(str.length());
                    }

                }


                return false;
            }
        });

        m_ctrlPhone_number.requestFocus();
    }

    public void initButtonUI()
    {
        preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        m_ctrlBtnLogin.setText(getButtonName(LOGINTITLE, Constants.CurrentLang));

        m_ctrlBtnSign.setText(getButtonName(SIGNUPTITLE, Constants.CurrentLang));

        m_ctrlBtnPincode.setText(getButtonName(PINCODETITLE, Constants.CurrentLang));
        m_ctrlBtnActivate.setText(getButtonName(ACTIVATE, Constants.CurrentLang));

        if(Constants.FirstLogin == false)
            m_ctrlBtnActivate.setEnabled(true);
        else
            m_ctrlBtnActivate.setEnabled(false);
    }

    public void SaveFile(String strPincode, String strPhone, String strApplicationVersion, String currentCountry, String currentLang)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(MainActivity.this);

        SharedPreferences.Editor edit = preferences.edit();

        edit.putString(Constants.PINCODE, strPincode);
        edit.putString("PHONE_NUMBER", strPhone);
        edit.putString("APPLICATIONVERSION", strApplicationVersion);
        edit.putString("CURRENT_COUNTRY", currentCountry);
        edit.putString("CURRENT_LANG", currentLang);

        edit.commit();
    }
    public void initUI()
    {
        m_ctrlBtnLogin.setOnClickListener(this);
        m_ctrlBtnSign.setOnClickListener(this);
        m_ctrlBtnPincode.setOnClickListener(this);
        m_ctrlBtnActivate.setOnClickListener(this);
        m_ctrlBannerButton.setOnClickListener(this);
        m_ctrlBtnLanguage.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton compoundButton, boolean language) {
                if(language == true)
                {
                    Constants.CurrentLang = "US";
                    Constants.Current_Country = "US";
                }else {
                    Constants.CurrentLang = "FR";
                    Constants.Current_Country = "FR";
                }
                if(!m_ctrlPhone_number.getText().toString().equals("") && !m_ctrlPincode.getText().toString().equals(""))
                {
                    SaveFile(m_ctrlPincode.getText().toString(), m_ctrlPhone_number.getText().toString(), Build.VERSION.RELEASE, Constants.Current_Country, Constants.CurrentLang);
                }
                initButtonUI();
            }
        });

        initButtonUI();

        m_ctrlSelLang.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if(position == 0)
                {
                    Constants.CurrentLang = "FR";
                    Constants.Current_Country = "FR";
                }else
                {
                    Constants.CurrentLang = "US";
                    Constants.Current_Country = "US";
                }

                if(!m_ctrlPhone_number.getText().toString().equals("") && !m_ctrlPincode.getText().toString().equals(""))
                {
                    SaveFile(m_ctrlPincode.getText().toString(), m_ctrlPhone_number.getText().toString(), Build.VERSION.RELEASE, Constants.Current_Country, Constants.CurrentLang);
                }
                initButtonUI();
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {

            }
        });


        if(Constants.CurrentLang.equals("FR")) {
            m_ctrlBtnLanguage.setChecked(false);

            m_ctrlSelLang.setSelection(0);
        }else
        {
            m_ctrlBtnLanguage.setChecked(true);
            m_ctrlSelLang.setSelection(1);
        }

        m_ctrlPhone_number.setText(Constants.codeTel);
        m_ctrlPincode.setText(Constants.codeMarket);

        m_ctrlPhone_number.setSelection(Constants.codeTel.length());
        m_ctrlPincode.setSelection(Constants.codeMarket.length());
        m_ctrlPhone_number.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String strResult = s.toString();

                String strPinCode = m_ctrlPincode.getEditableText().toString();
                if(!strResult.equals("") && strPinCode.length() >= 4 && strPinCode.length() <= 10)
                {
                    m_ctrlBtnLogin.setEnabled(true);

                    return;
                }

                m_ctrlBtnLogin.setEnabled(false);
            }
        });
        m_ctrlPincode.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {}

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {}

            @Override
            public void afterTextChanged(Editable s) {
                String strResult = s.toString();
                String strPhoneNumber = m_ctrlPhone_number.getEditableText().toString();
                if(!strPhoneNumber.equals("") && strResult.length() >= 4 && strResult.length() <= 10)
                {
                    m_ctrlBtnLogin.setEnabled(true);

                    return;
                }

                m_ctrlBtnLogin.setEnabled(false);
            }
        });
    }

    private int CompareVersion(String a,String b){
        int number1 = Integer.parseInt(a.substring(0, 1));
        int number2 = Integer.parseInt(b.substring(0, 1));
        int number10 = Integer.parseInt(a.substring(2, 3));
        int number20 = Integer.parseInt(b.substring(2, 3));

        if (number1 > number2 ) {
            return 2;
        }
        else if ((number1 == number2) && (number10 > number20)) {
            return 1;
        }
        return 0;
    }

    @Override
    public void onClick(View view) {
        switch (view.getId())
        {
            case R.id.btn_login:
                Login();
                break;
            case R.id.btn_pincode:
                Pincode();
                break;
            case R.id.btn_activate:
                Activate();
                break;
            case R.id.btn_signup:
                Signup();
                break;
            case R.id.banner_button:
                BannerButton();
                break;
        }
    }

    public void BannerButton()
    {
        Intent i = new Intent(MainActivity.this, ViewAdmin.class);

        startActivity(i);

        finish();
    }
    public void Login()
    {
        if(m_ctrlPhone_number.getText().toString().equals(Constants.EMPTY) || m_ctrlPincode.getText().toString().equals(Constants.EMPTY))
        {
            m_ctrlBtnLogin.setEnabled(false);

            return;
        }

        dialog.setCancelable(false);

        dialog.show();

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        MyDBProcessing processing = new MyDBProcessing();

        processing.SendPost(PartnerLoginJSON(), PartnerLogin);
    }

    public String PartnerLoginJSON()
    {

        Constants.codeTel = m_ctrlPhone_number.getText().toString();
        Constants.codeMarket = m_ctrlPincode.getText().toString();

        String strApi =  "{ Api: 'PartnerLogin', Partner_PinCode: '" + Constants.codeMarket + "', Partner_PhoneNumber:'" + Constants.codeTel + "', ";

        strApi += "Partner_PhoneCountry:'" + Constants.Current_Country +"',";
        strApi += "Language:'" + Constants.CurrentLang +"',";
        strApi += "Partner_PhonePushCode:'"+ Constants.Partner_PhonePushCode +"',";
        strApi += "Partner_PhoneCellularType:'android',";
        strApi += "channels_id:'3',";
        strApi += "ProcessToken:'"+ Constants.ProcessToken+"',";
        strApi += "Application_Token:'"+ "cc3d6bbcfa281ed05aba24540f6fb1503de0c952eb"+"',";
        strApi += "Application_ServerId:'"+ Constants.APPLICATION_SERVER_ID +"',";
        strApi += "Application_Version:'" + Build.VERSION.RELEASE +"'}";

        return strApi;
    }
    private Handler PartnerLogin = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            String str = (String)msg.obj;

            if(msg.arg2 == Constants.NET_ERR)
            {
                Toast.makeText(MainActivity.this, getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                vibrate(MainActivity.this);

                dialog.dismiss();

                return;
            }

            SaveFile(m_ctrlPincode.getText().toString(), m_ctrlPhone_number.getText().toString(), Build.VERSION.RELEASE, Constants.Current_Country, Constants.CurrentLang);

            try {

                MyXmlToJSON(str);

                PartnerLoginInfo();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public String CreditCardJSON()
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
    public void PartnerLoginInfo() throws JSONException {

        Constants.ErrorNumber = GetMatchString("ErrorNumber");

        if(Constants.ErrorNumber.equals("11"))
        {
            Toast.makeText(MainActivity.this, getButtonName("lab_errorAssist", Constants.CurrentLang), Toast.LENGTH_LONG).show();
            dialog.dismiss();
            return;
        }else
        {
            if(!Constants.ErrorNumber.equals("0")) {
                dialog.dismiss();
                Toast.makeText(MainActivity.this, getButtonName("lab_ErrorLogin", Constants.CurrentLang), Toast.LENGTH_LONG).show();
                vibrate(MainActivity.this);

                return;
            }else
            {
                Constants.FirstLogin = false;
            }
        }
        Constants.currentMarket = new MarketVo(GETJSONOBJECT());

        Constants.ProcessToken = GetMatchString("ProcessToken");
        Constants.currentMarket.Partner_PhoneNumber = GetMatchString("Partner_PhoneNumber");

        int nResult = CompareVersion(Constants.currentMarket.market_Version, Constants.Application_Version);
        if(nResult == 2)
        {
            vibrate(MainActivity.this);

            Toast.makeText(MainActivity.this, getButtonName("lab_PayCancel", Constants.CurrentLang), Toast.LENGTH_LONG).show();

            return;
        }


        Constants.Current_Country = GetMatchString("Partner_PhoneCountry");
        Constants.currentMarket.market_code = Constants.codeMarket;
        Constants.market_admin = Constants.currentMarket.Partner_AllowManagement;
        Constants.currentMarket.market_country = GetMatchString("Partner_PhoneCountry");

        SaveFile(codeMarket, Constants.codeTel, Build.VERSION.RELEASE, Constants.Current_Country, Constants.CurrentLang);

        //dialog.show();

        MyDBProcessing processing = new MyDBProcessing();
        processing.SendPost(CreditCardJSON(), CreditCardLogin);
    }
    public void CreditCardInfo() throws JSONException {

        Constants.ErrorNumber = GetMatchString("ErrorNumber");

        if(Constants.ErrorNumber.equals("11"))
        {
            Toast.makeText(MainActivity.this, getButtonName("lab_errorAssist", Constants.CurrentLang), Toast.LENGTH_LONG).show();

            return;
        }
        if(!Constants.ErrorNumber.equals("0"))
        {
            Constants.currentMarket.market_cb = false;
            if(Constants.currentMarket.market_Cluster.equals("0") && Constants.currentMarket.market_Insolvent.equals("false"))
            {
                Intent i = new Intent(MainActivity.this, ShowMoneyActivity.class);

                startActivity(i);

                finish();

                return;
            }else if(Constants.currentMarket.market_BalanceColor == null || Constants.currentMarket.market_BalanceColor.equals("") || Constants.currentMarket.market_BalanceColor.equals("Green"))
            {
                Intent i = new Intent(MainActivity.this, ShowMoneyActivity.class);

                startActivity(i);

                finish();

                return;
            }else
            {

                Toast.makeText(MainActivity.this, getButtonName("lab_ActiviateAccount", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                Intent i = new Intent(MainActivity.this, ViewCCPay.class);

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

            Toast.makeText(MainActivity.this, getButtonName("lab_ToPay", Constants.CurrentLang), Toast.LENGTH_LONG).show();

            Intent i = new Intent(MainActivity.this, ViewCCPay.class);

            startActivity(i);

            finish();

            return;
        }else
        {
            Intent i = new Intent(MainActivity.this, ShowMoneyActivity.class);

            startActivity(i);

            finish();

            return;
        }

    }
    private Handler CreditCardLogin = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            String str = (String)msg.obj;

            dialog.dismiss();
            try {
                MyXmlToJSON(str);

                CreditCardInfo();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };


    public void Pincode()
    {
        if(m_ctrlPhone_number.getText().toString().equals(""))
        {
            Toast.makeText(MainActivity.this, "Entrez le téléphone", Toast.LENGTH_LONG).show();

            return;
        }

        String strApi = "{Api: 'PartnerPinCodeGet',";

        strApi += "Partner_PhoneNumber:'" + m_ctrlPhone_number.getText().toString() + "',";

        strApi += "Partner_PhoneCountry:'" + m_ctrlPincode.getText().toString() + "',";

        strApi += "ProcessToken:'" + Constants.ProcessToken + "',";
        strApi += "Application_Token:'" + Constants.Application_Token + "',";
        strApi += "Application_ServerId:'" + Constants.APPLICATION_SERVER_ID + "',";
        strApi += "Application_Version:'" + Constants.Application_Version + "'}";

        MyDBProcessing processing = MyDBProcessing.getInstance();

        processing.SendPost(strApi, pinCodeHandler);

        dialog.show();

        dialog.setCancelable(false);

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    Handler pinCodeHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            dialog.dismiss();

            if(msg.arg2 == Constants.NET_ERR)
            {
                Toast.makeText(MainActivity.this, getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                vibrate(MainActivity.this);

                return;
            }else
            {
                String str = (String)msg.obj;

                try {
                    MyXmlToJSON(str);

                    Constants.ErrorNumber = GetMatchString("ErrorNumber");

                    if(Constants.ErrorNumber.equals("0"))
                    {
                        Toast.makeText(MainActivity.this, "PinCode envoyé par email à l'administrateur", Toast.LENGTH_LONG).show();

                        Constants.ProcessToken = GetMatchString("ProcessToken");
                        return;
                    }else
                    {
                        if(Constants.ErrorNumber.equals("9"))
                        {
                            Toast.makeText(MainActivity.this, getButtonName("lab_PhonePro", Constants.CurrentLang) + "?", Toast.LENGTH_LONG).show();
                            vibrate(MainActivity.this);
                            return;
                        }else
                        {
                            Toast.makeText(MainActivity.this, getButtonName("lab_errorAssist", Constants.CurrentLang) + "?", Toast.LENGTH_LONG).show();

                            vibrate(MainActivity.this);

                            return;
                        }
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }
        }
    };
    public void Activate()
    {
        Intent i = new Intent(MainActivity.this, ViewCCList.class);

        startActivity(i);

        finish();
    }

    public void Signup()
    {
        Intent i = new Intent(MainActivity.this, SignActivity.class);

        startActivity(i);

        finish();
    }

    public String getButtonName(String strTagName, String strLanguage)
    {
        String strResult = "";

        if(strTagName.equals(""))
            return strResult;

        if(strTagName.equals(LOGINTITLE))
        {
            if(nLoginNumber != -1)
            {
                MyTitles title = Constants.myTitle.get(nLoginNumber);

                if(strLanguage.equals("FR"))
                    strResult = title.strFrace;
                else
                    strResult = title.strEng;

                return strResult;
            }
        }

        if(strTagName.equals(PINCODETITLE))
        {
            if(nPinCodeNumber != -1)
            {
                MyTitles title = Constants.myTitle.get(nPinCodeNumber);

                if(strLanguage.equals("FR"))
                    strResult = title.strFrace;
                else
                    strResult = title.strEng;

                return strResult;
            }
        }

        if(strTagName.equals(SIGNUPTITLE))
        {
            if(nSignUpNumber != -1)
            {
                MyTitles title = Constants.myTitle.get(nSignUpNumber);

                if(strLanguage.equals("FR"))
                    strResult = title.strFrace;
                else
                    strResult = title.strEng;

                return strResult;
            }
        }

        if(strTagName.equals(LPINCODE))
        {
            if(nLPincode != -1)
            {
                MyTitles title = Constants.myTitle.get(nLPincode);

                if(strLanguage.equals("FR"))
                    strResult = title.strFrace;
                else
                    strResult = title.strEng;

                return strResult;
            }
        }
        if(strTagName.equals(ACTIVATE))
        {
            if(nActivateNumber != -1)
            {
                MyTitles title = Constants.myTitle.get(nActivateNumber);

                if(strLanguage.equals("FR"))
                    strResult = title.strFrace;
                else
                    strResult = title.strEng;

                return strResult;
            }
        }

        if(strTagName.equals(LTELEPHONE))
        {
            if(nLTelePhone != -1)
            {
                MyTitles title = Constants.myTitle.get(nLTelePhone);

                if(strLanguage.equals("FR"))
                    strResult = title.strFrace;
                else
                    strResult = title.strEng;

                return strResult;
            }
        }

        for(int i = 0; i < Constants.myTitle.size(); i ++)
        {
            MyTitles title = Constants.myTitle.get(i);

            if(title.strTag.equals(strTagName))
            {
                if(strLanguage.equals("FR"))
                    strResult = title.strFrace;
                else
                    strResult = title.strEng;

                if(strTagName.equals(LOGINTITLE))
                {
                    nLoginNumber = i;
                }

                if(strTagName.equals(PINCODETITLE))
                {
                    nPinCodeNumber = i;
                }

                if(strTagName.equals(SIGNUPTITLE))
                {
                    nSignUpNumber = i;
                }

                if(strTagName.equals(ACTIVATE))
                {
                    nActivateNumber = i;
                }

                if(strTagName.equals(LPINCODE))
                {
                    nLPincode = i;
                }
                break;
            }
        }

        return strResult;
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
