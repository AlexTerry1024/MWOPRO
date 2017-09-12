package mwpro.com.mwproapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
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
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.drive.internal.GetMetadataRequest;
import com.pepperonas.materialdialog.MaterialDialog;

import org.json.JSONException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;

import mwpro.com.mwproapplication.data.MyTitles;
import mwpro.com.mwproapplication.data.UserVo;
import mwpro.com.mwproapplication.ui.CustomProgressDialog;

import static com.google.android.gms.analytics.internal.zzy.i;

public class ViewSelectPhoneActivity extends Activity implements View.OnClickListener{

    String strCouNumber = "";

    ImageView m_ctrlBannerButton = null;
    TextView m_ctrlTitle = null;
    TextView m_ctrlPhone_helper = null;
    EditText m_ctrlPhoneNumber = null;
    Button  m_ctrlCancelButton = null;
    Button m_ctrlApplyButton = null;
    int nIndex = 1;
    CustomProgressDialog customDialog = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_select_phone);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(ViewSelectPhoneActivity.this);

        Intent i = this.getIntent();

        strCouNumber = i.getStringExtra("CouponNumber");

        m_ctrlBannerButton = (ImageView)findViewById(R.id.banner_button);
        m_ctrlTitle = (TextView)findViewById(R.id.title);
        m_ctrlPhone_helper = (TextView)findViewById(R.id.phone_helper);
        m_ctrlPhoneNumber = (EditText)findViewById(R.id.input_phone);
        m_ctrlCancelButton = (Button)findViewById(R.id.btn_cancel);
        m_ctrlApplyButton = (Button)findViewById(R.id.btn_apply);

        if(strCouNumber != null)
        {
            if(strCouNumber.equals("2"))
            {
                nIndex = 2;
                m_ctrlTitle.setText(getButtonName("lb_coupon_message", Constants.CurrentLang) + Constants.currentMarket.market_coupon_2 + getButtonName("lb_coupon_money", Constants.CurrentLang));
            }else
            {
                m_ctrlTitle.setText(getButtonName("lb_coupon_message", Constants.CurrentLang) + Constants.currentMarket.market_coupon_1 + getButtonName("lb_coupon_money", Constants.CurrentLang));
            }
        }else
        {
            m_ctrlTitle.setText(getButtonName("lb_coupon_message", Constants.CurrentLang) + Constants.currentMarket.market_coupon_1 + getButtonName("lb_coupon_money", Constants.CurrentLang));
        }

        m_ctrlPhoneNumber.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(s.toString().equals(""))
                {
                    m_ctrlApplyButton.setEnabled(false);
                }else
                    m_ctrlApplyButton.setEnabled(true);
            }
        });

        m_ctrlApplyButton.setEnabled(false);

        m_ctrlCancelButton.setOnClickListener(this);
        m_ctrlApplyButton.setOnClickListener(this);
        m_ctrlBannerButton.setOnClickListener(this);
        m_ctrlPhone_helper.setText(getButtonName("lab_tel", Constants.CurrentLang));
        m_ctrlApplyButton.setText(getButtonName("btn_valider", Constants.CurrentLang));
        m_ctrlCancelButton.setText(getButtonName("btn_annuler", Constants.CurrentLang));

        if(Constants.CurrentLang.equals("US"))
        {
            m_ctrlPhoneNumber.setHint("Input your phone number.");
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

            if(title.strTag.equals(strTagName)) {
                if(strLanguage.equals("FR"))
                    strResult = title.strFrace;
                else
                    strResult = title.strEng;
            }
        }
        return strResult;
    }

    @Override
    public void onClick(View v) {
        if(v == m_ctrlCancelButton)
            finish();
        if(v == m_ctrlApplyButton)
        {
            customDialog = new CustomProgressDialog(ViewSelectPhoneActivity.this, "");

            customDialog.show();

            customDialog.setCancelable(false);

            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            String strApi = "";

            Constants.currentUser = new UserVo();
            Constants.currentUser.user_phone = "";
            Constants.currentUser.User_AccessKey = m_ctrlPhoneNumber.getText().toString();
            if (Constants.currentUser.User_PhoneCountry == null)
                Constants.currentUser.User_PhoneCountry = Constants.Current_Country;

            strApi = "{ Api: 'UserParse'," +
                "Language: '" + Constants.CurrentLang + "'," +
                "Partner_PhoneNumber:'" +  Constants.codeTel + "'," +
                "Partner_PinCode:'" +  Constants.codeMarket + "'," +
                "Partner_PhoneCountry:'" +  Constants.Current_Country + "'," +
                "User_PhoneNumber:'" + Constants.currentUser.user_phone +"',"+
                "User_PhoneCountry:'"  + Constants.currentUser.User_PhoneCountry + "'," +
                "User_AccessKey: '" + Constants.currentUser.User_AccessKey + "'," +
                "User_AccessKeyType:'" + Constants.currentUser.User_AccessKeyType + "'," +
                "Transaction_Amount:'" + Constants.currentMontant + "'," +
                "ProcessToken:'" +  Constants.ProcessToken + "'," +
                "Application_Token:'" + Constants.Application_Token + "'," +
                "Application_ServerId:'" + Constants.APPLICATION_SERVER_ID + "'," +
                "Application_Version:'" + Constants.Application_Version + "'}";
            MyDBProcessing processing = new MyDBProcessing();

            processing.SendPost(strApi, handles);

        }else if(v == m_ctrlBannerButton)
        {
            Intent i = new Intent(ViewSelectPhoneActivity.this, MainActivity.class);

            startActivity(i);

            finish();
        }else
        {
            Toast.makeText(ViewSelectPhoneActivity.this, getButtonName("lab_PayCancel", Constants.CurrentLang), Toast.LENGTH_LONG).show();

            Intent i = new Intent(ViewSelectPhoneActivity.this, ShowMoneyActivity.class);

            startActivity(i);

            finish();
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Toast.makeText(ViewSelectPhoneActivity.this, getButtonName("lab_PayCancel", Constants.CurrentLang), Toast.LENGTH_LONG).show();

        Intent i = new Intent(ViewSelectPhoneActivity.this, ShowMoneyActivity.class);

        startActivity(i);

        finish();
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
    Handler handles = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
        String obj = (String)msg.obj;

        customDialog.dismiss();

        if(msg.arg2 == Constants.NET_ERR)
        {
            Toast.makeText(ViewSelectPhoneActivity.this, getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();

            return;
        }

        try {
            Constants.MyXmlToJSON(obj);
            interpretResult();
        } catch (XmlPullParserException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        }
    };

    public void interpretResult() throws XmlPullParserException, IOException, JSONException {

        Constants.ErrorNumber = Constants.GetMatchString("ErrorNumber");


            if(Constants.ErrorNumber.equals("23"))
            {
                Toast.makeText(ViewSelectPhoneActivity.this, getButtonName("lb_market_off", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                return;
            }

            if(Constants.ErrorNumber.equals("22"))
            {
                Intent i = new Intent(ViewSelectPhoneActivity.this, ViewNFCAssign.class);

                i.putExtra("CouponNumber", strCouNumber);

                startActivity(i);

                finish();

                return;
            }

            if(Constants.ErrorNumber.equals("241") || Constants.ErrorNumber.equals("21")|| Constants.ErrorNumber.equals("9"))
            {
                Toast.makeText(ViewSelectPhoneActivity.this, getButtonName("lb_AccessKeyUnknown", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                return;
            }

            if(Constants.ErrorNumber.equals("243"))
            {
                Toast.makeText(ViewSelectPhoneActivity.this, getButtonName("lab_DisabledUser", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                return;
            }

            if(Constants.ErrorNumber.equals("20"))
            {
                showUserAddNew();

                return;
            }

            if(!Constants.ErrorNumber.equals("0"))
            {
                Toast.makeText(ViewSelectPhoneActivity.this, getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                Intent i = new Intent(ViewSelectPhoneActivity.this, ShowMoneyActivity.class);

                startActivity(i);

                finish();

                return;
            }

        Constants.currentUser = new UserVo(Constants.GETJSONOBJECT());

        Constants.currentUser.user_phone = Constants.GetMatchString("User_PhoneNumber");

        Constants.currentUser.User_PhoneCountry = Constants.GetMatchString("User_PhoneCountry");

        Constants.ProcessToken = Constants.GetMatchString("ProcessToken");
        if(strCouNumber.equals(""))
            return;

        giveCoupon(Integer.parseInt(strCouNumber));
    }

    public void giveCoupon(int pIndex){
        switch (pIndex)
        {
            case 1 :
                executeCoupon(Constants.currentMarket.market_coupon_1, pIndex);
                break;
            case 2 :
                executeCoupon(Constants.currentMarket.market_coupon_2, pIndex);
                break;
        }
    }

    Handler executeCouponHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {

            String strObj = (String) msg.obj;

            if(msg.arg2 != Constants.NET_ERR)
            {
                try {
                    Constants.MyXmlToJSON(strObj);
                    interpretExecuteCoupon();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else
            {
                Toast.makeText(ViewSelectPhoneActivity.this, getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                Intent i = new Intent(ViewSelectPhoneActivity.this, ShowMoneyActivity.class);

                startActivity(i);

                finish();
            }
        }
    };

    public void executeCoupon(String pValue, int pCoupon){
        String strApi = "";

        strApi += "{Api: 'TransactionAddNewVoucher',";
        strApi += "Partner_PinCode:'" + Constants.currentMarket.market_code + "',";
        strApi += "User_PhoneNumber:'" + Constants.currentUser.user_phone + "',";
        strApi += "User_PhoneCountry:'" + Constants.currentUser.User_PhoneCountry + "',";
        strApi += "Voucher_Type:'" + pCoupon + "',";
        strApi += "Voucher_Amount:'" + pValue + "',";
        strApi += "Partner_PhoneNumber:'" + Constants.codeTel + "',";
        strApi += "Partner_PhoneCountry:'" + Constants.Current_Country + "',";
        strApi += "ProcessToken:'" + Constants.ProcessToken + "',";
        strApi += "Application_Token:'" + Constants.Application_Token + "',";
        strApi += "Application_ServerId:'" + Constants.APPLICATION_SERVER_ID + "',";
        strApi += "Application_Version:'" + Build.VERSION.RELEASE + "'}";

        customDialog.show();

        customDialog.setCancelable(false);

        MyDBProcessing processing = MyDBProcessing.getInstance();

        processing.SendPost(strApi, executeCouponHandler);

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
    }

    public void interpretExecuteCoupon() throws XmlPullParserException, IOException, JSONException {

        Constants.ErrorNumber = Constants.GetMatchString("ErrorNumber");

        customDialog.dismiss();
        if(!Constants.ErrorNumber.equals("0"))
        {
            Toast.makeText(ViewSelectPhoneActivity.this, getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show(); //

            Intent i = new Intent(ViewSelectPhoneActivity.this, ShowMoneyActivity.class);

            finish();

            return;
        }
        Constants.ProcessToken = Constants.GetMatchString("ProcessToken");


        if(Constants.ErrorNumber.equals("0"))
        {

            Toast.makeText(ViewSelectPhoneActivity.this, getButtonName("lab_credit_ok", Constants.CurrentLang), Toast.LENGTH_LONG).show(); //lab_credit_ok
            Constants.currentMarket.market_ToPay = 0;
            Constants.currentMarket.market_status = "1";

            Intent i = new Intent(ViewSelectPhoneActivity.this, ShowMoneyActivity.class);

            startActivity(i);

            finish();

        }
    }

    public void showUserAddNew()
    {
        new MaterialDialog.Builder(ViewSelectPhoneActivity.this).message(getButtonName("lb_AddNewUser", Constants.CurrentLang))
                .positiveText(getButtonName("btn_oui", Constants.CurrentLang))
                .negativeText(getButtonName("btn_non", Constants.CurrentLang))
                .buttonCallback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        Toast.makeText(ViewSelectPhoneActivity.this, "OK", Toast.LENGTH_SHORT).show();


                        Constants.currentUser = new UserVo();

                        Constants.currentUser.user_phone = m_ctrlPhoneNumber.getText().toString();
                        Constants.ExecutePaymentAfterVip = true;

                        Intent i = new Intent(ViewSelectPhoneActivity.this, VIPActivity.class);

                        i.putExtra("CouponNumber", strCouNumber);

                        startActivity(i);

                        dialog.dismiss();

                        finish();
                    }

                    @Override
                    public void onNegative(MaterialDialog dialog) {
                        super.onNegative(dialog);
                        Toast.makeText(ViewSelectPhoneActivity.this, "Cancel", Toast.LENGTH_SHORT).show();

                        dialog.dismiss();
                    }
                }).show();
    }
}
