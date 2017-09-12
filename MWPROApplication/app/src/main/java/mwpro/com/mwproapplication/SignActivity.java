package mwpro.com.mwproapplication;
import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.design.widget.TextInputLayout;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.text.DecimalFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mwpro.com.mwproapplication.data.MyTitles;
import mwpro.com.mwproapplication.ui.CustomProgressDialog;

import static mwpro.com.mwproapplication.Constants.GETJSONOBJECT;
import static mwpro.com.mwproapplication.Constants.GetMatchString;
import static mwpro.com.mwproapplication.Constants.MyXmlToJSON;
import static mwpro.com.mwproapplication.Constants.vibrate;

public class SignActivity extends Activity implements View.OnClickListener{

    EditText m_ctrlBizNumber = null;
    EditText m_ctrlEmail = null;
    EditText m_ctrlBusinessName = null;
    EditText m_ctrlAddress = null;
    EditText m_ctrlZipCode = null;
    EditText m_ctrlCity = null;
    EditText m_ctrlMemberCashBack = null;
    EditText m_ctrlVIPCashBack = null;

    Button m_ctrlApply = null;
    Button m_ctrlCancel = null;
    ImageView m_ctrlBanner = null;

    TextInputLayout m_ctrlPhone_Hint = null;
    TextInputLayout m_ctrlEmail_Hint = null;
    TextInputLayout m_ctrlBusinessName_Hint = null;
    TextInputLayout m_ctrlAddress_Hint = null;
    TextInputLayout m_ctrlZip_Hint = null;
    TextInputLayout m_ctrlCity_Hint = null;
    TextInputLayout m_ctrlMember_Hint = null;
    TextInputLayout m_ctrlVip_Hint = null;
    CustomProgressDialog customProgressDialog = null;

    public void ControlLink()
    {
        m_ctrlBizNumber = (EditText)findViewById(R.id.biz_phone_number);
        m_ctrlEmail = (EditText)findViewById(R.id.email);
        m_ctrlBusinessName = (EditText)findViewById(R.id.business_name);
        m_ctrlAddress = (EditText)findViewById(R.id.address);
        m_ctrlZipCode = (EditText)findViewById(R.id.zipcode);
        m_ctrlCity = (EditText)findViewById(R.id.city);
        m_ctrlMemberCashBack = (EditText)findViewById(R.id.member_cashback);
        m_ctrlVIPCashBack = (EditText)findViewById(R.id.vip_cashback);
        m_ctrlApply = (Button)findViewById(R.id.confirm);
        m_ctrlCancel = (Button)findViewById(R.id.cancel);
        m_ctrlBanner = (ImageView)findViewById(R.id.banner_button);
        m_ctrlPhone_Hint = (TextInputLayout)findViewById(R.id.phone_hint);
        m_ctrlEmail_Hint = (TextInputLayout)findViewById(R.id.email_hint);
        m_ctrlBusinessName_Hint = (TextInputLayout)findViewById(R.id.bussiness_hint);
        m_ctrlAddress_Hint = (TextInputLayout)findViewById(R.id.address_hint);
        m_ctrlZip_Hint = (TextInputLayout)findViewById(R.id.zip_hint);
        m_ctrlCity_Hint = (TextInputLayout)findViewById(R.id.city_hint);;
        m_ctrlMember_Hint = (TextInputLayout)findViewById(R.id.member_hint);
        m_ctrlVip_Hint = (TextInputLayout)findViewById(R.id.vip_hint);

        m_ctrlApply.setOnClickListener(this);
        m_ctrlCancel.setOnClickListener(this);
        m_ctrlBanner.setOnClickListener(this);

        customProgressDialog = new CustomProgressDialog(this, "");

        m_ctrlMemberCashBack.setText("0.00");
        m_ctrlVIPCashBack.setText("0.00");
        m_ctrlVIPCashBack.addTextChangedListener(amount(m_ctrlVIPCashBack));
        m_ctrlMemberCashBack.addTextChangedListener(amount(m_ctrlMemberCashBack));
        m_ctrlMemberCashBack.extendSelection(m_ctrlMemberCashBack.getText().toString().length());
        m_ctrlVIPCashBack.extendSelection(m_ctrlVIPCashBack.getText().toString().length());
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
    public void setData()
    {
        m_ctrlApply.setText(getButtonName("btn_valider", Constants.CurrentLang));
        m_ctrlCancel.setText(getButtonName("btn_annuler", Constants.CurrentLang));
        m_ctrlCancel.setText(getButtonName("btn_annuler", Constants.CurrentLang));
        m_ctrlApply.setText(getButtonName("btn_valider", Constants.CurrentLang));

        m_ctrlPhone_Hint.setHint(getButtonName("lab_PhonePro", Constants.CurrentLang));
        m_ctrlEmail_Hint.setHint(getButtonName("lab_email", Constants.CurrentLang));

        m_ctrlBusinessName_Hint.setHint(getButtonName("lab_PartnerName", Constants.CurrentLang));
        m_ctrlAddress_Hint.setHint(getButtonName("lab_address", Constants.CurrentLang));
        m_ctrlZip_Hint.setHint(getButtonName("lab_ZipCode", Constants.CurrentLang));
        m_ctrlCity_Hint.setHint(getButtonName("lab_city", Constants.CurrentLang));
        m_ctrlMember_Hint.setHint(getButtonName("lab_CBMember", Constants.CurrentLang));
        m_ctrlVip_Hint.setHint(getButtonName("lab_CBVIP", Constants.CurrentLang));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_sign);

        ControlLink();

        setData();
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
    public boolean isValidEmail(String email)
    {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }
    @Override
    public void onClick(View v) {

        if(v == m_ctrlApply)
        {
            String strBizPhoneNumber = m_ctrlBizNumber.getText().toString();

            if(strBizPhoneNumber.trim().equals(""))
            {
                Toast.makeText(SignActivity.this, getButtonName("lab_fillalltabs", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                m_ctrlBizNumber.requestFocus();

                vibrate(SignActivity.this);

                return;
            }

            String strEmail = m_ctrlEmail.getText().toString();

            if(strEmail.trim().equals(""))
            {
                Toast.makeText(SignActivity.this, getButtonName("lab_fillalltabs", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                m_ctrlEmail.requestFocus();

                vibrate(SignActivity.this);

                return;
            }else
            {
                if(isValidEmail(strEmail) == false)
                {
                    Toast.makeText(SignActivity.this, getButtonName("lab_fillalltabs", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                    vibrate(SignActivity.this);

                    m_ctrlEmail.requestFocus();

                    return;
                }
            }

            String strBusinessName = m_ctrlBusinessName.getText().toString();

            if(strBusinessName.trim().equals(""))
            {
                Toast.makeText(SignActivity.this, getButtonName("lab_fillalltabs", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                m_ctrlBusinessName.requestFocus();

                vibrate(SignActivity.this);

                return;
            }

            String strAddress = m_ctrlAddress.getText().toString();

            if(strAddress.trim().equals(""))
            {
                Toast.makeText(SignActivity.this, getButtonName("lab_fillalltabs", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                m_ctrlAddress.requestFocus();

                vibrate(SignActivity.this);

                return;
            }

            String strZipCode = m_ctrlZipCode.getText().toString();

            if(strZipCode.trim().equals(""))
            {
                Toast.makeText(SignActivity.this, getButtonName("lab_fillalltabs", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                m_ctrlZipCode.requestFocus();

                vibrate(SignActivity.this);

                return;
            }

            String strCity = m_ctrlCity.getText().toString();

            if(strCity.trim().equals(""))
            {
                Toast.makeText(SignActivity.this, getButtonName("lab_fillalltabs", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                m_ctrlCity.requestFocus();

                vibrate(SignActivity.this);

                return;
            }

            String strMemberCashBack = m_ctrlMemberCashBack.getText().toString();

            if(strMemberCashBack.trim().equals(""))
            {
                Toast.makeText(SignActivity.this, getButtonName("lab_fillalltabs", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                m_ctrlMemberCashBack.requestFocus();

                vibrate(SignActivity.this);

                return;
            }

            String strVIPCashBack = m_ctrlVIPCashBack.getText().toString();

            if(strVIPCashBack.trim().equals(""))
            {
                Toast.makeText(SignActivity.this, getButtonName("lab_fillalltabs", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                m_ctrlVIPCashBack.requestFocus();

                vibrate(SignActivity.this);

                return;
            }

            String strApi = "{Api: 'PartnerAddNew'," +
                    "ProcessToken: '" + Constants.ProcessToken + "'," +
                    "Application_Token:'" +  Constants.Application_Token +"'," +
                    "Application_ServerId:'" +  Constants.APPLICATION_SERVER_ID + "'," +
                    "Application_Version:'" +  Constants.Application_Version + "'," +
                    "Language:'" + Constants.CurrentLang + "'," +
                    "Partner_PhoneNumber:'" + m_ctrlBizNumber.getText().toString() + "'," +
                    "Partner_PhoneCountry:'" + Constants.Current_Country +"'," +
                    "Partner_Language:'" + Constants.CurrentLang + "'," +
                    "Partner_Country:'" + Constants.Current_Country +"'," +
                    "Partner_CompanyName:'" + m_ctrlBusinessName.getText().toString() + "'," +
                    "Partner_Status: '0'," +
                    "Partner_Activity_Id: ''," +
                    "Partner_MessageTransactionAddNewCashBack: 'True'," +

                    "Partner_Gender: ''," +
                    "Partner_FirstName: ''," +
                    "Partner_LastName: ''," +
                    "Partner_Address:'" + m_ctrlAddress.getText().toString() + "'," +
                    "Partner_ZipCode:'" + m_ctrlZipCode.getText().toString() + "'," +
                    "Partner_City:'" + m_ctrlCity.getText().toString() + "'," +
                    "Partner_EMail:'" + m_ctrlEmail.getText().toString() + "'," +
                    "Partner_WebSite: ''," +
                    "Partner_SiretNumber: ''," +
                    "Partner_ContactPhoneNumber: ''," +
                    "Partner_Cluster_Id: ''," +
                    "Partner_Retainer: ''," +
                    "Partner_MonthlyFee: ''," +
                    "Partner_PercentageTransactionFee: ''," +
                    "Partner_PercentageVipUser:'" + m_ctrlVIPCashBack.getText().toString() + "'," +
                    "Partner_PercentageAlternateUser:'" + m_ctrlMemberCashBack.getText().toString() + "'," +
                    "Partner_PercentageClassicUser:'" + m_ctrlMemberCashBack.getText().toString() + "'," +
                    "Partner_VoucherValueSponsor: ''," +
                    "Partner_VoucherValueSponsored: ''," +
                    "Partner_AvailableDelayVoucher: ''," +
                    "Partner_AvailableDelayCashBack: ''," +
                    "Partner_ValidityVoucher: ''," +
                    "Partner_ValidityCashBack: ''," +
                    "Partner_FreeDeadline: ''" +
                    "}";

            MyDBProcessing processing = new MyDBProcessing();

            processing.SendPost(strApi, register);

            customProgressDialog.show();

            return;
        }

        if(v == m_ctrlCancel)
        {

            if(Constants.market_admin && Constants.currentMarket != null && !Constants.currentMarket.market_Cluster.equals("0"))
            {
                Intent i = new Intent(SignActivity.this, ViewAdmin.class);

                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }else
            {
                Intent i = new Intent(SignActivity.this, MainActivity.class);
                startActivity(i);
                overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
                finish();
            }
        }

        if(v == m_ctrlBanner)
        {
            Intent i = new Intent(SignActivity.this, MainActivity.class);
            startActivity(i);
            overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);
            finish();
        }

    }

    Handler register = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            String strResult = (String) msg.obj;

            int nSucessOrFail = msg.arg2;
            customProgressDialog.dismiss();
            if(nSucessOrFail != Constants.NET_SUC)
            {
                Toast.makeText(SignActivity.this, "Network Error", Toast.LENGTH_LONG).show();

                vibrate(SignActivity.this);

                return;
            }

            try {
                MyXmlToJSON(strResult);
                addNewInterpret();
            } catch (JSONException e) {
                customProgressDialog.dismiss();

                e.printStackTrace();
            }
        }
    };

    public void addNewInterpret() throws  JSONException {
        Constants.ErrorNumber = GetMatchString("ErrorNumber");

        if(!Constants.ErrorNumber.equals("0"))
        {
            Toast.makeText(SignActivity.this, getButtonName("lab_errorAssist", Constants.CurrentLang), Toast.LENGTH_LONG).show();

            vibrate(SignActivity.this);

            return;
        }

        Constants.ProcessToken = GetMatchString("ProcessToken");

        if(GETJSONOBJECT().has("Data"))
        {
            JSONObject object = GETJSONOBJECT().getJSONObject("Data");

            if(object.has("PinCode"))
            {
                Constants.codeMarket = object.getString("PinCode");
            }
        }

        Constants.codeTel = m_ctrlBizNumber.getText().toString();

        Toast.makeText(SignActivity.this, getButtonName("lab_PartnerNew", Constants.CurrentLang), Toast.LENGTH_LONG).show();

        SaveFile(Constants.codeMarket, Constants.codeTel, Build.VERSION.RELEASE, Constants.Current_Country, Constants.CurrentLang);

        Intent i = new Intent(SignActivity.this, MainActivity.class);

        startActivity(i);

        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

        finish();
    }

    public void SaveFile(String strPincode, String strPhone, String strApplicationVersion, String currentCountry, String currentLang)
    {
        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SignActivity.this);

        SharedPreferences.Editor edit = preferences.edit();

        edit.putString(Constants.PINCODE, strPincode);
        edit.putString("PHONE_NUMBER", strPhone);
        edit.putString("APPLICATIONVERSION", strApplicationVersion);
        edit.putString("CURRENT_COUNTRY", currentCountry);
        edit.putString("CURRENT_LANG", currentLang);

        edit.commit();
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
