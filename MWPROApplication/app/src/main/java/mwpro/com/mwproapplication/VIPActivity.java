package mwpro.com.mwproapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
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
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RadioButton;
import android.widget.SeekBar;
import android.widget.TextView;
import android.widget.Toast;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.json.JSONException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import java.io.IOException;
import java.io.StringReader;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import mwpro.com.mwproapplication.data.UserVo;
import mwpro.com.mwproapplication.ui.CustomProgressDialog;

import static mwpro.com.mwproapplication.Constants.GETJSONOBJECT;
import static mwpro.com.mwproapplication.Constants.GetMatchString;
import static mwpro.com.mwproapplication.Constants.MyXmlToJSON;
import static mwpro.com.mwproapplication.Constants.context;
import static mwpro.com.mwproapplication.Constants.getButtonName;
import static mwpro.com.mwproapplication.Constants.vibrate;


public class VIPActivity extends Activity {

    ImageView m_ctrlBannerButton = null;
    TextView m_ctrlTitle = null;
    TextView m_ctrlSecondTitle = null;
    TextView    m_ctrlVip_Helper = null;
    TextView    m_ctrlMember_Helper = null;
    TextView    m_ctrlMarks = null;
    TextView m_ctrlThirdTitle = null;

    EditText m_ctrlPhone = null;
    EditText m_ctrlNfcCard = null;
    EditText m_ctrlPrenom = null;
    EditText m_ctrlNom = null;
    EditText m_ctrlEmail = null;

    TextInputLayout m_ctrlPhone_Hint = null;
    TextInputLayout m_ctrlNfc_Hint = null;
    TextInputLayout m_ctrlFirstName_Hint = null;
    TextInputLayout m_ctrlLastName_Hint = null;
    TextInputLayout m_ctrlEmail_Hint = null;
    RadioButton m_ctrlMember = null;
    RadioButton m_ctrlVip = null;
    DiscreteSeekBar m_ctrlVipVal = null;
    Button      m_ctrlCancel = null;
    Button      m_ctrlApply = null;

    int         _couponIndex = 0;
    int         n_TransactionAmount = 0;
    CustomProgressDialog customProgressDialog = null;

    public void ControlLink()
    {
        m_ctrlBannerButton = (ImageView)findViewById(R.id.banner_button);
        m_ctrlTitle = (TextView)findViewById(R.id.title);
        m_ctrlSecondTitle = (TextView)findViewById(R.id.second_title);
        m_ctrlThirdTitle = (TextView)findViewById(R.id.third_title);
        m_ctrlPhone = (EditText)findViewById(R.id.phone);

        m_ctrlPhone_Hint = (TextInputLayout)findViewById(R.id.phone_title);
        m_ctrlNfc_Hint = (TextInputLayout)findViewById(R.id.nfccard_title);
        m_ctrlFirstName_Hint = (TextInputLayout)findViewById(R.id.firstname_title);
        m_ctrlLastName_Hint = (TextInputLayout)findViewById(R.id.lastname_title);
        m_ctrlEmail_Hint = (TextInputLayout)findViewById(R.id.email_title);
        m_ctrlNfcCard = (EditText)findViewById(R.id.nfccard);
        m_ctrlPrenom = (EditText) findViewById(R.id.prenom);
        m_ctrlNom = (EditText)findViewById(R.id.nom);
        m_ctrlEmail = (EditText)findViewById(R.id.email);
        m_ctrlMember = (RadioButton)findViewById(R.id.member);
        m_ctrlVip = (RadioButton)findViewById(R.id.vip);
        m_ctrlVipVal = (DiscreteSeekBar) findViewById(R.id.vip_value);
        m_ctrlCancel = (Button)findViewById(R.id.cancel);
        m_ctrlApply = (Button)findViewById(R.id.confirm);
        m_ctrlVip_Helper = (TextView)findViewById(R.id.vip_help);
        m_ctrlMember_Helper = (TextView)findViewById(R.id.member_help);
        m_ctrlMarks = (TextView)findViewById(R.id.marks);
        customProgressDialog = new CustomProgressDialog(this, "");

        m_ctrlCancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                Toast.makeText(VIPActivity.this, getButtonName("lab_PayCancel", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                Intent i = new Intent(VIPActivity.this, ShowMoneyActivity.class);

                startActivity(i);

                finish();
            }
        });

        m_ctrlBannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent i = new Intent(VIPActivity.this, MainActivity.class);

                startActivity(i);

                finish();
            }
        });
        m_ctrlApply.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Constants.currentUser.User_AccessKey = m_ctrlNfcCard.getText().toString();

                if(m_ctrlVip.isChecked()) {
                    Constants.currentUser.user_type = 1;
                    if(m_ctrlVipVal.getProgress() >= Constants.currentMarket.market_vip_value)
                    {
                        Constants.Percent = "" + m_ctrlVipVal.getProgress();
                    }
                }else {
                    Constants.currentUser.user_type = 3;

                    Constants.Percent = "";
                }
                UserSetAndParse(m_ctrlPhone.getText().toString(), m_ctrlPrenom.getText().toString(), m_ctrlNom.getText().toString(), m_ctrlEmail.getText().toString(), m_ctrlNfcCard.getText().toString(), "" + Constants.currentUser.user_type, Constants.Percent, (int) Constants.currentMontant,_couponIndex);
            }
        });

        m_ctrlVip.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    m_ctrlMember.setChecked(false);
                    m_ctrlVipVal.setVisibility(View.VISIBLE);
                    m_ctrlVipVal.setEnabled(true);

                    m_ctrlVipVal.setMin((int)Constants.currentMarket.market_vip_value);
                    m_ctrlVipVal.setMax(100);
                    m_ctrlVipVal.setProgress((int)Constants.currentMarket.market_vip_value);

                    m_ctrlMarks.setText(m_ctrlVipVal.getProgress() + "%");
                }else
                {
                    m_ctrlMember.setChecked(true);

                    m_ctrlVipVal.setVisibility(View.INVISIBLE);

                    m_ctrlVipVal.setMin(Constants.currentMarket.market_member_value);
                    m_ctrlVipVal.setMax((int)Constants.currentMarket.market_vip_value);
                    m_ctrlVipVal.setProgress(Constants.currentMarket.market_member_value);
                    m_ctrlMarks.setText(m_ctrlVipVal.getProgress() + "%");
                }
            }
        });

        m_ctrlVipVal.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                m_ctrlMarks.setText(seekBar.getProgress()+ "%");
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
                m_ctrlMarks.setText(seekBar.getProgress()+ "%");
            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
                m_ctrlMarks.setText(seekBar.getProgress()+ "%");
            }
        });

        m_ctrlMember.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    m_ctrlVip.setChecked(false);

                    m_ctrlVipVal.setVisibility(View.INVISIBLE);

                    m_ctrlVipVal.setMin(Constants.currentMarket.market_member_value);
                    m_ctrlVipVal.setMax((int)Constants.currentMarket.market_vip_value);
                    m_ctrlVipVal.setProgress(Constants.currentMarket.market_member_value);
                    m_ctrlMarks.setText(m_ctrlVipVal.getProgress() + "%");


                }else
                {
                    m_ctrlVip.setChecked(true);

                    m_ctrlVipVal.setVisibility(View.VISIBLE);
                    m_ctrlVipVal.setEnabled(true);

                    m_ctrlVipVal.setMin((int)Constants.currentMarket.market_vip_value);
                    m_ctrlVipVal.setMax(100);
                    m_ctrlVipVal.setProgress((int)Constants.currentMarket.market_vip_value);

                    m_ctrlMarks.setText(m_ctrlVipVal.getProgress() + "%");
                }
            }
        });

        m_ctrlPhone.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();

                m_ctrlApply.setEnabled(canValidate());
            }
        });
        m_ctrlEmail.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = s.toString();

                m_ctrlApply.setEnabled(canValidate());
            }
        });
    }

    public void setData()
    {
        m_ctrlTitle.setText(Constants.viewVipText1);
        m_ctrlSecondTitle.setText(Constants.viewVipText2);
        m_ctrlThirdTitle.setText(getButtonName("lab_tel", Constants.CurrentLang) + "  " + getButtonName("lab_obligatoire", Constants.CurrentLang));

        m_ctrlPhone.setText(Constants.currentUser.user_phone);
        m_ctrlPrenom.setText(Constants.currentUser.user_name);
        m_ctrlNom.setText(Constants.currentUser.user_lastname);
        m_ctrlEmail.setText(Constants.currentUser.user_email);
        m_ctrlPhone.setSelection(m_ctrlPhone.getText().toString().length());
        m_ctrlPrenom.setSelection(m_ctrlPrenom.getText().toString().length());
        m_ctrlNom.setSelection(m_ctrlNom.getText().toString().length());
        m_ctrlEmail.setSelection(m_ctrlEmail.getText().toString().length());
        m_ctrlPhone_Hint.setHint(getButtonName("lab_Phone", Constants.CurrentLang));
        m_ctrlNfc_Hint.setHint(getButtonName("lab_idNFC", Constants.CurrentLang));
        m_ctrlFirstName_Hint.setHint(getButtonName("lab_prenom", Constants.CurrentLang));
        m_ctrlLastName_Hint.setHint(getButtonName("lab_nom", Constants.CurrentLang));
        m_ctrlEmail_Hint.setHint(getButtonName("lab_email", Constants.CurrentLang));
        m_ctrlVip_Helper.setText(getButtonName("lab_vip", Constants.CurrentLang));
        m_ctrlMember_Helper.setText(getButtonName("lab_membre", Constants.CurrentLang));
        m_ctrlApply.setText(getButtonName("btn_valider", Constants.CurrentLang));
        m_ctrlCancel.setText(getButtonName("btn_annuler", Constants.CurrentLang));
    }
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_vip);

        ControlLink();

        setData();

        Intent i = getIntent();

        String strCoupon = i.getStringExtra("CouponNumber");

        if(strCoupon != null)
        {
            _couponIndex = Integer.parseInt(strCoupon);
        }

        m_ctrlVipVal.setEnabled(false);
        m_ctrlVipVal.setVisibility(View.GONE);
        m_ctrlVipVal.setMin(Constants.currentMarket.market_member_value);
        m_ctrlVipVal.setProgress(Constants.currentMarket.market_member_value);

        Constants.currentUser.User_AccessKey = "";

        if(Constants.currentMarket.Partner_AllowChangeUserType)
        {
            m_ctrlMember.setChecked(true);
            m_ctrlVip.setChecked(false);
        }else
        {
            findViewById(R.id.radio_group).setVisibility(View.INVISIBLE);
            m_ctrlVipVal.setVisibility(View.INVISIBLE);
        }

        if(Constants.currentMarket.market_vip_value == 0)
        {
            findViewById(R.id.radio_group).setVisibility(View.INVISIBLE);
            m_ctrlVipVal.setVisibility(View.INVISIBLE);
        }

        m_ctrlApply.setEnabled(canValidate());

        if(Constants.ExecutePaymentAfterVip == true)
        {
            if(Constants.currentMarket.market_vip_value == 0)
            {
                findViewById(R.id.radio_group).setVisibility(View.INVISIBLE);
                m_ctrlVipVal.setVisibility(View.INVISIBLE);
            }
        }

        m_ctrlMarks.setText(m_ctrlVipVal.getProgress() + "%");

        if (Constants.currentMarket.market_activity == 2606) {  // test si Mairie ... pas de VIP)

            Constants.currentMarket.Partner_AllowChangeUserType = false;

            m_ctrlVipVal.setMin(0);
            m_ctrlVip.setChecked(true);
            m_ctrlMember.setChecked(false);
        }

    }
    public boolean isValidEmail(String email)
    {
        String expression = "^[\\w\\.-]+@([\\w\\-]+\\.)+[A-Z]{2,4}$";
        Pattern pattern = Pattern.compile(expression, Pattern.CASE_INSENSITIVE);
        Matcher matcher = pattern.matcher(email);
        return matcher.matches();

    }
    public void UserSetAndParse(String phone, String  firstname,String  lastname,String email,String nfc, String usertype, String percentage, int TransactionAmount, int coupon)
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

    public boolean canValidate()
    {
        if(m_ctrlPhone.getText().toString().length() > 5)
        {
            if(m_ctrlEmail.getText().toString().trim().equals(""))
                return true;
            else
                return isValidEmail(m_ctrlEmail.getText().toString().trim());
        }

        return false;
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
    Handler UserSetAndParseHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            String str = (String) msg.obj;
            customProgressDialog.dismiss();
            if (msg.arg2 != Constants.NET_ERR) {
                try {
                    MyXmlToJSON(str);
                    InterpretUserSetAndParse();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            } else {
                vibrate(VIPActivity.this);
                Toast.makeText(VIPActivity.this, getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                Intent i = new Intent(VIPActivity.this, ShowMoneyActivity.class);

                startActivity(i);

                finish();

                return;
            }
        }
    };

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Toast.makeText(VIPActivity.this, getButtonName("lab_PayCancel", Constants.CurrentLang), Toast.LENGTH_LONG).show();

        Intent i = new Intent(VIPActivity.this, ShowMoneyActivity.class);

        startActivity(i);

        finish();
    }

    public void InterpretUserSetAndParse() throws IOException, XmlPullParserException, JSONException {

        String strUser_Phone = "";

        Constants.ErrorNumber = GetMatchString("ErrorNumber");

        if (Constants.ErrorNumber.equals("9")) {

            Toast.makeText(VIPActivity.this, getButtonName("lab_ErrorPhoneFormat", Constants.CurrentLang), Toast.LENGTH_LONG).show();

            Intent i = new Intent(VIPActivity.this, ShowMoneyActivity.class);

            startActivity(i);

            finish();
            return;
        } else if (Constants.ErrorNumber.equals("30")) {
            Toast.makeText(VIPActivity.this, getButtonName("lab_UserPhoneExist", Constants.CurrentLang), Toast.LENGTH_LONG).show();

            vibrate(VIPActivity.this);
            Intent i = new Intent(VIPActivity.this, ShowMoneyActivity.class);

            startActivity(i);

            finish();
            return;
        } else if (Constants.ErrorNumber.equals("31")) {
            Toast.makeText(VIPActivity.this, getButtonName("lab_UserPhoneExistAsPartner", Constants.CurrentLang), Toast.LENGTH_LONG).show();

            vibrate(VIPActivity.this);
            Intent i = new Intent(VIPActivity.this, ShowMoneyActivity.class);
            startActivity(i);
            finish();
            return;
        } else if (Constants.ErrorNumber.equals("241")) {
            vibrate(VIPActivity.this);
            Intent i = new Intent(VIPActivity.this, ShowMoneyActivity.class);
            startActivity(i);
            finish();
            return;
        } else if (Constants.ErrorNumber.equals("243")) {
            vibrate(VIPActivity.this);
            Intent i = new Intent(VIPActivity.this, ShowMoneyActivity.class);

            startActivity(i);

            finish();
            return;
        } else if (!Constants.ErrorNumber.equals("0")) {
            //    erreurHome = _controller.res.g('lab_vip_no_ok');
            Intent i = new Intent(VIPActivity.this, ShowMoneyActivity.class);

            startActivity(i);

            finish();
            return;
        }

        Constants.ProcessToken = GetMatchString("ProcessToken");

        Constants.currentUser.user_language = GetMatchString("User_Language");

        Constants.currentUser.User_PhoneCountry = GetMatchString("User_PhoneCountry");

        Constants.currentUser = new UserVo(GETJSONOBJECT());

        Constants.currentUser.user_phone = GetMatchString("User_PhoneNumber");

        Constants.currentMontant = Float.parseFloat(GETJSONOBJECT().getJSONObject("Data").getString("Transaction_Amount"));

        if(_couponIndex > 0)
        {
            giveCoupon(_couponIndex);

            return;

        }else if(n_TransactionAmount > 0)
        {
            if(Constants.currentMarket.market_activity == 2606)
            {
                executePayment(Constants.currentMontant, 0, 0, "", 0, "");

                return;
            }else
            {
                Intent i = new Intent(VIPActivity.this, ViewStatusActivity.class);

                startActivity(i);

                finish();

                return;
            }
        }else
        {
            Toast.makeText(VIPActivity.this, getButtonName("lab_vip_ok", Constants.CurrentLang), Toast.LENGTH_LONG).show();

            Intent i = new Intent(VIPActivity.this, ShowMoneyActivity.class);

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
                Toast.makeText(VIPActivity.this, getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                Intent i = new Intent(VIPActivity.this, ViewStatusActivity.class);

                startActivity(i);

                finish();
            }
        }
    };
    public void executePaymentInterpret() throws JSONException {

        String strTagName = "";
        String strMessage = "";
        String strProcessToken = "";

        Constants.ErrorNumber = GetMatchString("ErrorNumber");

        if (Constants.ErrorNumber.equals("200"))   // test si Partner Insolvent et en théorie pas de règlement par CB car la CB recrédite
        {
            if (Constants.currentMarket.market_cb == false) {

                Toast.makeText(this, getButtonName("lab_ValidAccount", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                Constants.AmountCB = Constants.currentMarket.market_ToPay;

                Intent i = new Intent(context, ViewCreditCard.class);

                startActivity(i);

                finish();

                return;
            }
            else {
                Toast.makeText(this, getButtonName("lab_depositError", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                Intent i = new Intent(this, ShowMoneyActivity.class);

                startActivity(i);

                finish();

                return;
            }

        }

        strMessage = GetMatchString("Message");
        strProcessToken = GetMatchString("ProcessToken");

        if (Constants.ErrorNumber.equals("0")) {
            Constants.ProcessToken = strProcessToken;
            Toast.makeText(this, getButtonName("lab_credit_ok", Constants.CurrentLang), Toast.LENGTH_LONG).show();

            Constants.currentMarket.market_ToPay = 0;


            Intent i = new Intent(this, ShowMoneyActivity.class);

            startActivity(i);

            finish();
        }
        else if (Constants.ErrorNumber.equals("250"))   // test si erreur Payline - code Payline dans Message
        {
            String[] arrays = strMessage.split(" : ");

            Toast.makeText(this, arrays[1], Toast.LENGTH_LONG).show();
            Intent i = new Intent(this, ShowMoneyActivity.class);

            startActivity(i);

            finish();
        }
        else if (Constants.ErrorNumber.equals("257") || Constants.ErrorNumber.equals("256")) {
            Toast.makeText(this, strMessage, Toast.LENGTH_LONG).show();

            Constants.currentUser.cards.remove(Constants.CCIndex);
            Intent i = new Intent(this, ViewCCPay.class);

            startActivity(i);

            finish();
        }
        else {
            Toast.makeText(this, strMessage, Toast.LENGTH_LONG).show();

            Intent i = new Intent(VIPActivity.this, ShowMoneyActivity.class);

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

    public void executeCoupon(String pValue, int pCoupon){
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

        MyDBProcessing processing = new MyDBProcessing();

        customProgressDialog.show();
        customProgressDialog.setCancelable(false);

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        processing.SendPost(strApi, executeCouponHandler);
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

                    interpretExecuteCoupon(strObj);
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else
            {
                Toast.makeText(VIPActivity.this, getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                Intent i = new Intent(context, ShowMoneyActivity.class);

                startActivity(i);

                finish();
            }
        }
    };

    public void interpretExecuteCoupon(String strXml) throws XmlPullParserException, IOException, JSONException {

        Constants.ErrorNumber = GetMatchString("ErrorNumber");

        if(!Constants.ErrorNumber.equals("0")) {
            Toast.makeText(context, getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show(); //

            Intent i = new Intent(VIPActivity.this, ShowMoneyActivity.class);

            ((Activity)context).finish();

            return;
        }else {
            Constants.ProcessToken = GetMatchString("ProcessToken");

            Toast.makeText(VIPActivity.this, getButtonName("lab_credit_ok", Constants.CurrentLang), Toast.LENGTH_LONG).show(); //
            Constants.currentMarket.market_ToPay = 0;
            Constants.currentMarket.market_status = "1";

            Intent i = new Intent(VIPActivity.this, ShowMoneyActivity.class);

            finish();
        }
    }
    public void giveCoupon(int pIndex){
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
}

