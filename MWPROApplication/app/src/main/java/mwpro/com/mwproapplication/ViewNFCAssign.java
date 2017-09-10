package mwpro.com.mwproapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONException;
import org.w3c.dom.Text;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;

import mwpro.com.mwproapplication.data.UserVo;
import mwpro.com.mwproapplication.ui.CustomProgressDialog;
import retrofit2.http.GET;

import static mwpro.com.mwproapplication.Constants.GETJSONOBJECT;
import static mwpro.com.mwproapplication.Constants.GetMatchString;
import static mwpro.com.mwproapplication.Constants.MyXmlToJSON;
import static mwpro.com.mwproapplication.Constants.getButtonName;
import static mwpro.com.mwproapplication.Constants.vibrate;


public class ViewNFCAssign extends Activity {

    ImageView m_ctrlBannerButton = null;
    TextView m_ctrlTitle = null;
    TextView m_ctrlUser_phone_enter = null;
    TextView m_ctrlLab_idNFC = null;
    EditText m_ctrlNfcNumber = null;
    TextView m_ctrlLab_Tel = null;
    EditText m_ctrlPhone = null;
    Button m_ctrlCancel = null;
    Button m_ctrlValider = null;
    int n_TransactionAmount = 0;
    int _couponIndex = 0;
    int _countErreur = 0;
    CustomProgressDialog customProgressDialog = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_nfcassign);

        customProgressDialog = new CustomProgressDialog(this, "");
        Intent i = getIntent();

        String strCoupon = i.getStringExtra("Coupon");

        if(strCoupon != null)
        {
            _couponIndex = Integer.parseInt(strCoupon);
        }
        m_ctrlBannerButton = (ImageView)findViewById(R.id.banner_button);
        m_ctrlTitle = (TextView)findViewById(R.id.title);
        m_ctrlUser_phone_enter = (TextView)findViewById(R.id.user_phone_enter);
        m_ctrlLab_idNFC = (TextView)findViewById(R.id.lab_idNFC);
        m_ctrlNfcNumber = (EditText)findViewById(R.id.nfcNumber);
        m_ctrlLab_Tel = (TextView)findViewById(R.id.lab_tel);
        m_ctrlPhone = (EditText)findViewById(R.id.phone);
        m_ctrlCancel = (Button)findViewById(R.id.idCancel);
        m_ctrlValider = (Button)findViewById(R.id.idValider);
        m_ctrlTitle.setText(getButtonName("lab_nfcAssign", Constants.CurrentLang));
        m_ctrlUser_phone_enter.setText(getButtonName("lab_UserPhoneEnter", Constants.CurrentLang));
        m_ctrlLab_idNFC.setText(getButtonName("lab_idNFC", Constants.CurrentLang));

        m_ctrlNfcNumber.setEnabled(false);
        m_ctrlNfcNumber.setText(Constants.currentUser.User_AccessKey);
        m_ctrlLab_Tel.setText(getButtonName("lab_tel", Constants.CurrentLang));
        m_ctrlCancel.setText(getButtonName("btn_annuler", Constants.CurrentLang));
        m_ctrlValider.setText(getButtonName("btn_valider", Constants.CurrentLang));

        m_ctrlCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

            Toast.makeText(ViewNFCAssign.this, getButtonName("lab_PayCancel", Constants.CurrentLang), Toast.LENGTH_LONG).show();

            Intent i = new Intent(ViewNFCAssign.this, ShowMoneyActivity.class);

            startActivity(i);

            finish();
            }
        });

        m_ctrlValider.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                UserSetAndParse(m_ctrlPhone.getText().toString(), "", "", "", m_ctrlNfcNumber.getText().toString(), "",  "", (int)Constants.currentMontant,_couponIndex);
            }
        });

        m_ctrlBannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ViewNFCAssign.this, getButtonName("lab_PayCancel", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                Intent i = new Intent(ViewNFCAssign.this, ShowMoneyActivity.class);

                startActivity(i);

                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Toast.makeText(ViewNFCAssign.this, getButtonName("lab_PayCancel", Constants.CurrentLang), Toast.LENGTH_LONG).show();

        Intent i = new Intent(ViewNFCAssign.this, ShowMoneyActivity.class);

        startActivity(i);

        finish();
    }

    public void UserSetAndParse(String phone, String  firstname, String  lastname, String email, String nfc, String usertype, String percentage, int TransactionAmount, int coupon)
    {

        n_TransactionAmount = TransactionAmount;
        String strApi = "";

        strApi = "{Api:'UserSetAndParse',";
        strApi += "Partner_PinCode:'" + Constants.codeMarket + "',";
        strApi += "User_PhoneNumber:'" + phone + "',";
        strApi += "User_PhoneCountry:'" + Constants.Current_Country + "',";
        strApi += "User_FirstName:'" + firstname + "',";
        strApi += "User_LastName:'" + lastname + "',";
        strApi += "User_EMail:'" + email + "',";
        strApi += "User_AccessKey:'" + nfc + "',";
        strApi += "User_Language:'" + Constants.CurrentLang + "',";
        strApi += "User_Country:'" + Constants.Current_Country + "',";
        strApi += "User_Type:'" + usertype + "',";
        strApi += "User_Percentage:'" + percentage + "',";
        strApi += "Partner_PhoneNumber:'" + Constants.codeTel + "',";
        strApi += "Partner_PhoneCountry:'" + Constants.Current_Country + "',";
        strApi += "Transaction_Amount:'" + TransactionAmount + "',";
        strApi += "ProcessToken:'" + Constants.ProcessToken + "',";
        strApi += "Application_Token:'" + Constants.Application_Token + "',";
        strApi += "Application_ServerId:'" + Constants.APPLICATION_SERVER_ID + "',";
        strApi += "Application_Version:'" + Constants.Application_Version + "'}";

        MyDBProcessing myDBProcessing = MyDBProcessing.getInstance();

        customProgressDialog.show();

        customProgressDialog.setCancelable(false);

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        myDBProcessing.SendPost(strApi, UserSetAndParseHandler);
    }

    Handler UserSetAndParseHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            String str = (String) msg.obj;
            customProgressDialog.dismiss();
            if (msg.arg2 != Constants.NET_ERR) {
                try {
                    MyXmlToJSON(str);
                    InterpretUserSetAndParse(str);
                } catch (JSONException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            } else {
                vibrate(ViewNFCAssign.this);

                Toast.makeText(ViewNFCAssign.this, getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                Intent i = new Intent(ViewNFCAssign.this, ShowMoneyActivity.class);

                startActivity(i);

                finish();

                return;
            }
        }
    };

    public void InterpretUserSetAndParse(String str) throws JSONException, IOException, XmlPullParserException {

        String strUser_Phone = "";

        Constants.ErrorNumber = GetMatchString("ErrorNumber");

        if (Constants.ErrorNumber.equals("9")) {

            Toast.makeText(ViewNFCAssign.this, getButtonName("lab_ErrorPhoneFormat", Constants.CurrentLang), Toast.LENGTH_LONG).show();

            Intent i = new Intent(ViewNFCAssign.this, ShowMoneyActivity.class);

            startActivity(i);

            finish();
            return;
        } else if (Constants.ErrorNumber.equals("30")) {
            Toast.makeText(ViewNFCAssign.this, getButtonName("lab_UserPhoneExist", Constants.CurrentLang), Toast.LENGTH_LONG).show();
            vibrate(ViewNFCAssign.this);
            Intent i = new Intent(ViewNFCAssign.this, ShowMoneyActivity.class);

            startActivity(i);

            finish();
            return;
        } else if (Constants.ErrorNumber.equals("31")) {
            Toast.makeText(ViewNFCAssign.this, getButtonName("lab_UserPhoneExistAsPartner", Constants.CurrentLang), Toast.LENGTH_LONG).show();
            vibrate(ViewNFCAssign.this);
            Intent i = new Intent(ViewNFCAssign.this, ShowMoneyActivity.class);

            startActivity(i);

            finish();
            return;
        } else if (Constants.ErrorNumber.equals("241")) {
            //   erreurHome = _controller.res.g('pEvt.result.ErrorMessage');
            vibrate(ViewNFCAssign.this);
            Intent i = new Intent(ViewNFCAssign.this, ShowMoneyActivity.class);

            startActivity(i);

            finish();
            return;
        } else if (Constants.ErrorNumber.equals("243")) {
            vibrate(ViewNFCAssign.this);
            Toast.makeText(ViewNFCAssign.this, getButtonName("lab_UserPhoneExistAsPartner", Constants.CurrentLang), Toast.LENGTH_LONG).show();
            Intent i = new Intent(ViewNFCAssign.this, ShowMoneyActivity.class);

            startActivity(i);

            finish();
            return;
        } else if (!Constants.ErrorNumber.equals("0")) {
            vibrate(ViewNFCAssign.this);
            Toast.makeText(ViewNFCAssign.this, getButtonName("lab_vip_no_ok", Constants.CurrentLang), Toast.LENGTH_LONG).show();
            Intent i = new Intent(ViewNFCAssign.this, ShowMoneyActivity.class);

            startActivity(i);

            finish();
            return;
        }

        Constants.ProcessToken = GetMatchString("ProcessToken");
        Constants.currentUser.user_language = GetMatchString("User_Language");
        Constants.currentUser.User_PhoneCountry = GetMatchString("User_PhoneCountry");

        Constants.currentUser = new UserVo(str);

        Constants.currentUser.user_phone = GetMatchString("User_PhoneNumber");

        GETJSONOBJECT().getJSONObject("Data").getString("Transaction_Amount");

        Constants.currentMontant = Float.parseFloat(GetMatchString("User_PhoneNumber"));

        if(_couponIndex > 0)
        {
            giveCoupon(_couponIndex);

        }else if(n_TransactionAmount > 0)
        {
            if(Constants.currentMarket.market_activity == 2606)
            {
                executePayment(Constants.currentMontant, 0, 0, "", 0, "");
            }else
            {
                Intent i = new Intent(ViewNFCAssign.this, ViewStatusActivity.class);

                startActivity(i);

                finish();
            }
        }else
        {
            Toast.makeText(ViewNFCAssign.this, getButtonName("lab_vip_ok", Constants.CurrentLang), Toast.LENGTH_LONG).show();

            Intent i = new Intent(ViewNFCAssign.this, ShowMoneyActivity.class);

            startActivity(i);

            finish();
        }
    }
    public  void giveCoupon(int pIndex){
        //@author SH  @date 03/02/2013

        switch (pIndex) {
            case 1 :
                executeCoupon(Constants.currentMarket.market_coupon_1, pIndex);
                break;
            case 2 :
                executeCoupon(Constants.currentMarket.market_coupon_2, pIndex);
                break;
        }
    }

    public  void executeCoupon(String pValue, int pCoupon){
        String strApi = "";

        strApi += "{api: 'TransactionAddNewVoucher',";
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

        customProgressDialog.show();

        customProgressDialog.setCancelable(false);

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        MyDBProcessing processing = new MyDBProcessing();

        processing.SendPost(strApi, executeCouponHandler);
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
    public  void interpretExecuteCoupon() throws XmlPullParserException, IOException, JSONException {
        Constants.ErrorNumber = Constants.GetMatchString("ErrorNumber");

        if(!Constants.ErrorNumber.equals("0"))
        {
            Toast.makeText(ViewNFCAssign.this, getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show(); //

            Intent i = new Intent(ViewNFCAssign.this, ShowMoneyActivity.class);

            finish();

            return;
        }
        Constants.ProcessToken = Constants.GetMatchString("ProcessToken");

        if(Constants.ErrorNumber.equals("0"))
        {

            Toast.makeText(ViewNFCAssign.this, getButtonName("lab_credit_ok", Constants.CurrentLang), Toast.LENGTH_LONG).show(); //lab_credit_ok
            Constants.currentMarket.market_ToPay = 0;
            Constants.currentMarket.market_status = "1";

            Intent i = new Intent(ViewNFCAssign.this, ShowMoneyActivity.class);

            startActivity(i);

            finish();

        }
    }

    Handler executeCouponHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {

            String strObj = (String) msg.obj;
            customProgressDialog.dismiss();
            if(msg.arg2 != Constants.NET_ERR)
            {
                try {
                    MyXmlToJSON(strObj);

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
                Toast.makeText(ViewNFCAssign.this, getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                Intent i = new Intent(ViewNFCAssign.this, ShowMoneyActivity.class);

                startActivity(i);

                ViewNFCAssign.this.finish();
            }
        }
    };
    public  void executePayment(float pTotalPrice, float pUsedPrice, float pTag, String percentage, float UsedCB, String CodePin){

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
        strApi += "Partner_ExternalTag:'" + Constants.currentMarket.market_ExternalTag + "',";
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

                    executePaymentInterpret(str);

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else
            {
                Toast.makeText(ViewNFCAssign.this, getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                Intent i = new Intent(ViewNFCAssign.this, ViewStatusActivity.class);
                startActivity(i);
                finish();
            }
        }
    };
    public  void executePaymentInterpret(String strData) throws JSONException {

        String strTagName = "";
        String strMessage = "";
        String strProcessToken = "";

        Constants.ErrorNumber = GetMatchString("ErrorNumber");

        if (Constants.ErrorNumber.equals("200"))   // test si Partner Insolvent et en théorie pas de règlement par CB car la CB recrédite
        {
            if (Constants.currentMarket.market_cb == false) {

                Toast.makeText(ViewNFCAssign.this, getButtonName("lab_ValidAccount", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                Constants.AmountCB = Constants.currentMarket.market_ToPay;

                Intent i = new Intent(ViewNFCAssign.this, ViewCreditCard.class);

                startActivity(i);

                finish();

                return;

            }
            else {
                Toast.makeText(ViewNFCAssign.this, getButtonName("lab_depositError", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                Intent i = new Intent(ViewNFCAssign.this, ShowMoneyActivity.class);

                startActivity(i);

                finish();

                return;
            }
        }

        strMessage = GetMatchString("Message");
        strProcessToken = GetMatchString("ProcessToken");

        if (Constants.ErrorNumber.equals("0")) {
            Constants.ProcessToken = strProcessToken;
            Toast.makeText(ViewNFCAssign.this, getButtonName("lab_credit_ok", Constants.CurrentLang), Toast.LENGTH_LONG).show();

            Constants.currentMarket.market_ToPay = 0;


            Intent i = new Intent(ViewNFCAssign.this, ShowMoneyActivity.class);

            startActivity(i);

            finish();

            return;
        }else if(Constants.ErrorNumber.equals("20"))
        {
            _countErreur ++;


            Toast.makeText(ViewNFCAssign.this, getButtonName("lab_codePinError", Constants.CurrentLang) + " " + _countErreur + "/3", Toast.LENGTH_LONG).show();
            if(_countErreur == 3)
            {
                Intent i = new Intent(ViewNFCAssign.this, ShowMoneyActivity.class);

                startActivity(i);

                finish();
            }

            return;
        }
        else if (Constants.ErrorNumber.equals("250"))   // test si erreur Payline - code Payline dans Message
        {
            String[] arrays = strMessage.split(" : ");

            Toast.makeText(ViewNFCAssign.this, arrays[1], Toast.LENGTH_LONG).show();
            Intent i = new Intent(ViewNFCAssign.this, ShowMoneyActivity.class);

            startActivity(i);

            finish();
        }
        else if (Constants.ErrorNumber.equals("257") || Constants.ErrorNumber.equals("256")) {
            Toast.makeText(ViewNFCAssign.this, strMessage, Toast.LENGTH_LONG).show();

            Constants.currentUser.cards.remove(Constants.CCIndex);
            Intent i = new Intent(ViewNFCAssign.this, ViewCCPay.class);

            startActivity(i);

            finish();
        }
        else {
            Toast.makeText(ViewNFCAssign.this, strMessage, Toast.LENGTH_LONG).show();

            Intent i = new Intent(ViewNFCAssign.this, ShowMoneyActivity.class);

            startActivity(i);

            finish();
        }
    }
}
