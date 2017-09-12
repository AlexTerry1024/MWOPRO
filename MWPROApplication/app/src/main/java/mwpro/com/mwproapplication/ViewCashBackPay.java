package mwpro.com.mwproapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Handler;
import android.os.Message;
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
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;

import mwpro.com.mwproapplication.ui.CustomProgressDialog;

import static mwpro.com.mwproapplication.Constants.GetMatchString;
import static mwpro.com.mwproapplication.Constants.MyXmlToJSON;

import static mwpro.com.mwproapplication.Constants.formatNumber;
import static mwpro.com.mwproapplication.Constants.getButtonName;


public class ViewCashBackPay extends Activity implements View.OnClickListener{

    TextView m_ctrlTitle = null;
    TextView m_ctrlIDMontant_Help = null;
    EditText m_ctrlIDMontant = null;
    TextView m_ctrlLabelTipsortotal = null;
    TextInputLayout m_ctrlTipsLabel = null;
    EditText m_ctrlIDTips = null;
    TextInputLayout m_ctrlLabCCPayTotal = null;
    EditText m_ctrlTotal = null;
    TextInputLayout m_ctrlEnterPinCode = null;
    EditText m_ctrlCodePin = null;
    TextView m_ctrlLab_Number = null;
    Button m_ctrlIDCancel = null;
    Button m_ctrlIDValider = null;
    Button m_ctrlIDPinCodeSend = null;
    TextView m_ctrlSendPinCodeText = null;
    int nErrorCount = 0;
    ImageView m_ctrlBanner;
    CustomProgressDialog customProgressDialog = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_cash_back_pay);

        customProgressDialog = new CustomProgressDialog(this, "");
        m_ctrlTitle = (TextView)findViewById(R.id.title);
        m_ctrlTitle.setText(Constants.viewVipText1);
        m_ctrlIDMontant_Help = (TextView)findViewById(R.id.idMontant_title);
        m_ctrlIDMontant_Help.setText(getButtonName("lab_amount", Constants.CurrentLang));
        m_ctrlIDMontant = (EditText)findViewById(R.id.idMontant);
        m_ctrlIDMontant.setEnabled(false);
        m_ctrlLabelTipsortotal = (TextView)findViewById(R.id.labeltipsortotal);
        m_ctrlLabelTipsortotal.setText(getButtonName("lab_tipsOrtotal", Constants.CurrentLang));
        m_ctrlTipsLabel = (TextInputLayout)findViewById(R.id.tipsLabel);
        m_ctrlIDTips = (EditText)findViewById(R.id.idTips);
        m_ctrlLabCCPayTotal = (TextInputLayout)findViewById(R.id.ccpayTotal);
        m_ctrlLabCCPayTotal.setHint(getButtonName("lab_CCpayTotal", Constants.CurrentLang));
        m_ctrlTotal = (EditText)findViewById(R.id.total);

        m_ctrlBanner = (ImageView)findViewById(R.id.banner_button);
        m_ctrlEnterPinCode = (TextInputLayout)findViewById(R.id.codePin_Help);
        m_ctrlEnterPinCode.setHint(getButtonName("lab_enterCodePin", Constants.CurrentLang));
        m_ctrlCodePin = (EditText)findViewById(R.id.codePin);
        m_ctrlLab_Number = (TextView)findViewById(R.id.lab_number);
        m_ctrlLab_Number.setText(getButtonName("lab_6numbers", Constants.CurrentLang));
        m_ctrlIDCancel = (Button)findViewById(R.id.idCancel);
        m_ctrlIDCancel.setText(getButtonName("btn_annuler", Constants.CurrentLang));
        m_ctrlIDValider = (Button)findViewById(R.id.idValider);
        m_ctrlIDValider.setText(getButtonName("btn_valider", Constants.CurrentLang));
        m_ctrlIDPinCodeSend = (Button)findViewById(R.id.idPinCodeSend);
        m_ctrlIDPinCodeSend.setText(getButtonName("btn_SendPinCode", Constants.CurrentLang));
        m_ctrlSendPinCodeText = (TextView)findViewById(R.id.idPinCodeText);
        m_ctrlSendPinCodeText.setText(getButtonName("lab_SendPinCodeText", Constants.CurrentLang));

        if(!Constants.currentMarket.market_country.equals("US"))
        {
            findViewById(R.id.TipsGroup).setVisibility(View.GONE);
        }

        m_ctrlIDMontant.setText(formatNumber("" + Constants.currentMontant, 2, true, false));

        m_ctrlTipsLabel.setHint(getButtonName("lab_tips", Constants.CurrentLang) + " : " + formatNumber( "" + Constants.currentMontant * 0.15, 2, true, false) + " ?");

        m_ctrlTotal.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();

                if(str.equals(""))
                    str = "0.00";
                m_ctrlIDTips.setText(formatNumber("" + (Float.parseFloat(str) - Float.parseFloat(m_ctrlIDMontant.getText().toString())), 2, true, false));

                if(Float.parseFloat(m_ctrlIDTips.getText().toString()) < 0)
                    m_ctrlIDTips.setText("0.00");
            }
        });

        m_ctrlIDTips.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {

                m_ctrlTotal.setText(formatNumber("" + (Float.parseFloat(m_ctrlIDTips.getText().toString()) + Float.parseFloat(m_ctrlIDMontant.getText().toString())), 2, true, false));
            }
        });
        m_ctrlIDPinCodeSend.setOnClickListener(this);
        m_ctrlIDCancel.setOnClickListener(this);
        m_ctrlIDValider.setOnClickListener(this);
    }

    public void onClickSend()
    {
        String strApi = "{Api: 'UserPinCodeGet',";

        strApi += "User_PhoneCountry:'" + Constants.currentUser.User_PhoneCountry + "',";
        strApi += "User_PhoneNumber:'" + Constants.currentUser.user_phone + "',";
        strApi += "ProcessToken:'" + Constants.ProcessToken + "',";
        strApi += "Application_Token:'" + Constants.Application_Token + "',";
        strApi += "Application_ServerId:'" + Constants.APPLICATION_SERVER_ID + "',";
        strApi += "Application_Version:'" + Constants.Application_Version + "'}";

        customProgressDialog.show();
        customProgressDialog.setCancelable(false);

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        MyDBProcessing processing = MyDBProcessing.getInstance();

        processing.SendPost(strApi, sendPincode);
    }

    Handler sendPincode = new Handler()
    {
        @Override
        public void handleMessage(Message msg){

            customProgressDialog.dismiss();

            String str = (String) msg.obj;

            if(msg.arg2 == Constants.NET_ERR)
            {
                Toast.makeText(ViewCashBackPay.this, getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                return;
            }

            try {
                MyXmlToJSON(str);

                Constants.ErrorNumber = GetMatchString("ErrorNumber");

                if(Constants.ErrorNumber.equals("20"))
                {
                    Toast.makeText(ViewCashBackPay.this, getButtonName("lab_PincodeUnlnownUser", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                    return;
                }

                if(Constants.ErrorNumber.equals("23"))
                {
                    Toast.makeText(ViewCashBackPay.this, getButtonName("lab_DisabledUser", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                    return;
                }
                if(Constants.ErrorNumber.equals("0"))
                {
                    Constants.ProcessToken = GetMatchString("ProcessToken");
                    Toast.makeText(ViewCashBackPay.this, getButtonName("lab_PinCodeSent", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                    m_ctrlIDPinCodeSend.setEnabled(false);

                    return;
                }else
                {
                    Toast.makeText(ViewCashBackPay.this, getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    };
    Handler SendHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {

            customProgressDialog.dismiss();
            String strObj = (String) msg.obj;

            if(msg.arg2 == Constants.NET_ERR)
            {
                Toast.makeText(ViewCashBackPay.this, getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();
                return;
            }
            try {
                MyXmlToJSON(strObj);
                Send();
            } catch (XmlPullParserException e) {
                e.printStackTrace();
            } catch (IOException e) {
                e.printStackTrace();
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public void Send() throws XmlPullParserException, IOException, JSONException {

        Constants.ErrorNumber = GetMatchString("ErrorNumber");

        if(Constants.ErrorNumber.equals("20"))
        {
            Toast.makeText(ViewCashBackPay.this, getButtonName("lab_PincodeUnlnownUser", Constants.CurrentLang), Toast.LENGTH_LONG).show();;

            return;
        }
        if(Constants.ErrorNumber.equals("23"))
        {
            Toast.makeText(ViewCashBackPay.this, getButtonName("lab_DisabledUser", Constants.CurrentLang), Toast.LENGTH_LONG).show();;

            return;
        }

        if(!Constants.ErrorNumber.equals("0"))
        {
            Toast.makeText(ViewCashBackPay.this, getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();;

            return;
        }

        Constants.ProcessToken = GetMatchString("ProcessToken");

        m_ctrlIDPinCodeSend.setEnabled(true);

        Toast.makeText(ViewCashBackPay.this, getButtonName("lab_PinCodeSent", Constants.CurrentLang), Toast.LENGTH_LONG).show();;
    }
    public void onClickCancel()
    {
        Toast.makeText(ViewCashBackPay.this, getButtonName("lab_PayCancel", Constants.CurrentLang), Toast.LENGTH_LONG).show();

        Intent i = new Intent(ViewCashBackPay.this, ShowMoneyActivity.class);

        startActivity(i);

        finish();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Toast.makeText(ViewCashBackPay.this, getButtonName("lab_PayCancel", Constants.CurrentLang), Toast.LENGTH_LONG).show();

        Intent i = new Intent(ViewCashBackPay.this, ShowMoneyActivity.class);

        startActivity(i);

        finish();
    }

    Handler validerHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {

            customProgressDialog.dismiss();
            String str = (String)msg.obj;

            if(msg.arg2 == Constants.NET_ERR)
            {
                Toast.makeText(ViewCashBackPay.this, getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                Intent i = new Intent(ViewCashBackPay.this, ShowMoneyActivity.class);

                startActivity(i);

                finish();

                return;
            }

            try {
                MyXmlToJSON(str);
                interpretValidData();
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
    };

    public void interpretValidData() throws JSONException {

        Constants.ErrorNumber = GetMatchString("ErrorNumber");

        if(Constants.ErrorNumber.equals("1"))
        {
            Toast.makeText(ViewCashBackPay.this, getButtonName("lab_UnderMaintenance", Constants.CurrentLang), Toast.LENGTH_LONG).show();

            Intent i = new Intent(ViewCashBackPay.this, MainActivity.class);

            startActivity(i);

            finish();

            return;
        }

        if(Constants.ErrorNumber.equals("20"))
        {
            nErrorCount ++;

            m_ctrlCodePin.setText("");

            Toast.makeText(ViewCashBackPay.this, getButtonName("lab_codePinError", Constants.CurrentLang) + nErrorCount + "/3", Toast.LENGTH_LONG).show();

            if(nErrorCount == 3) {
                Intent i = new Intent(ViewCashBackPay.this, ShowMoneyActivity.class);

                startActivity(i);

                finish();

                return;
            }

            return;
        }

        if(Constants.ErrorNumber.equals("0"))
        {
            Constants.ProcessToken = GetMatchString("ProcessToken");

            Toast.makeText(ViewCashBackPay.this, getButtonName("lab_credit_ok", Constants.CurrentLang), Toast.LENGTH_LONG).show();

            Constants.currentMarket.market_ToPay = 0;

            Intent i = new Intent(ViewCashBackPay.this, ShowMoneyActivity.class);

            startActivity(i);

            finish();

            return;

        }

        if(Constants.ErrorNumber.equals("200"))
        {
            if(Constants.currentMarket.market_cb == false)
            {
                Toast.makeText(ViewCashBackPay.this, getButtonName("lab_ValidAccount", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                Constants.AmountCB = Constants.currentMarket.market_ToPay;

                Intent i = new Intent(ViewCashBackPay.this, ViewCreditCard.class);

                startActivity(i);

                finish();

                return;
            }else
            {
                Toast.makeText(ViewCashBackPay.this, getButtonName("lab_depositError", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                Intent i = new Intent(ViewCashBackPay.this, ShowMoneyActivity.class);

                startActivity(i);

                finish();

                return;
            }
        }

        if(Constants.ErrorNumber.equals("250"))
        {
            String str = GetMatchString("Message");

            if(!str.equals(""))
            {
                Toast.makeText(ViewCashBackPay.this, str.split(" : ")[1], Toast.LENGTH_LONG).show();
            }

            Intent i = new Intent(ViewCashBackPay.this, ShowMoneyActivity.class);

            startActivity(i);

            finish();

            return;
        }

        if(Constants.ErrorNumber.equals("257") || Constants.ErrorNumber.equals("256"))
        {
            String str = GetMatchString("Message");

            Toast.makeText(ViewCashBackPay.this, str.split(" : ")[1], Toast.LENGTH_LONG).show();

            Constants.currentUser.cards.remove(Constants.CCIndex);

            Intent i = new Intent(ViewCashBackPay.this, ViewCCList.class);

            startActivity(i);

            return;
        }
        if(Constants.ErrorNumber.equals("251") || Constants.ErrorNumber.equals("253")) {
            String str = GetMatchString("Message");

            Toast.makeText(ViewCashBackPay.this, str.split(" : ")[1], Toast.LENGTH_LONG).show();

            Intent i = new Intent(ViewCashBackPay.this, ViewCCList.class);

            startActivity(i);

            return;
        }else
        {
            Intent i = new Intent(ViewCashBackPay.this, ShowMoneyActivity.class);

            startActivity(i);

            return;
        }
    }

    public void onClickValider()
    {
        String strApi = "{Api: 'TransactionAddNewCashBack',";

        strApi += "Partner_PinCode:'" + Constants.currentMarket.market_code + "',";
        strApi += "Partner_ExternalTag:'" + Constants.currentMarket.market_ExternalTag + "',";
        strApi += "User_PhoneNumber:'" + Constants.currentUser.user_phone + "',";
        strApi += "User_PhoneCountry:'" + Constants.currentUser.User_PhoneCountry + "',";
        strApi += "User_PinCode:'" + m_ctrlCodePin.getText().toString() + "',";
        strApi += "Transaction_Amount:'" + Constants.currentMontant + "',";
        strApi += "Transaction_AmountUsed:'" + Constants.currentUser.AmountUsed + "',";
        strApi += "Transaction_AmountCashBack:'" + Constants.currentUser.AmountCashBack + "',";
        strApi += "User_Type:'" + Constants.currentUser.user_type + "',";
        strApi += "User_Percentage:'" + Constants.Percent + "',";
        strApi += "Partner_PhoneNumber:'" + Constants.codeTel + "',";
        strApi += "Partner_PhoneCountry:'" + Constants.Current_Country + "',";
        strApi += "Transaction_AmountPaid:'" + 0 + "',";
        strApi += "ProcessToken:'" + Constants.ProcessToken + "',";
        strApi += "Application_Token:'" + Constants.Application_Token + "',";
        strApi += "Application_ServerId:'" + Constants.APPLICATION_SERVER_ID + "',";
        strApi += "Application_Version:'" + Constants.Application_Version + "',";
        strApi += "CreditCard_Token:'" + Constants.CCToken + "'}";

        MyDBProcessing processing = MyDBProcessing.getInstance();

        processing.SendPost(strApi, validerHandler);

        customProgressDialog.show();

        customProgressDialog.setCancelable(false);

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }
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
    @Override
    public void onClick(View v) {
        if(v == m_ctrlIDValider)
        {
            onClickValider();
        }

        if(v == m_ctrlIDPinCodeSend)
        {
            onClickSend();
        }

        if(v == m_ctrlBanner)
        {
            Intent i = new Intent(ViewCashBackPay.this, ViewAdmin.class);

            startActivity(i);

            finish();
        }
        if(v == m_ctrlIDCancel)
            onClickCancel();
    }
}
