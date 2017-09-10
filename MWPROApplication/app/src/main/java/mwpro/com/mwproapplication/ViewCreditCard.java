package mwpro.com.mwproapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Typeface;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import org.json.JSONException;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.util.ArrayList;
import mwpro.com.mwproapplication.data.Card;
import mwpro.com.mwproapplication.ui.CustomProgressDialog;
import mwpro.com.mwproapplication.ui.wheel.ArrayWheelAdapter;
import mwpro.com.mwproapplication.ui.wheel.NumericWheelAdapter;
import mwpro.com.mwproapplication.ui.wheel.WheelView;
import static mwpro.com.mwproapplication.Constants.GetMatchString;
import static mwpro.com.mwproapplication.Constants.MyXmlToJSON;
import static mwpro.com.mwproapplication.Constants.getButtonName;
import static mwpro.com.mwproapplication.Constants.vibrate;

public class ViewCreditCard extends Activity implements View.OnClickListener{
    ImageView m_ctrlBannerButton = null;
    TextView m_ctrlTitle = null;
    WheelView m_ctrlSpinner = null;
    EditText m_ctrlCardNumber = null;
    EditText m_ctrlCardName = null;
    WheelView m_ctrlCardMonth = null;
    WheelView m_ctrlCardYear = null;
    EditText m_ctrlCardCW = null;
    Button m_ctrlAnnuller = null;
    Button m_ctrlValider = null;
    ArrayList<String> cardType = null;
    ArrayList<String> monthType = null;
    ArrayList<String> yearType = null;
    CustomProgressDialog customProgressDialog = null;
    Handler sendHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
        String data = (String)msg.obj;

        customProgressDialog.dismiss();
        if(msg.arg2 != Constants.NET_SUC)
        {
            Toast.makeText(ViewCreditCard.this, getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG);
            return;
        }

        try {

            MyXmlToJSON(data);

            interpret();
        } catch (JSONException e) {
            e.printStackTrace();
        }
        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_credit_card);

        m_ctrlValider = (Button)findViewById(R.id.valider);
        m_ctrlAnnuller = (Button)findViewById(R.id.annuller);
        m_ctrlBannerButton = (ImageView)findViewById(R.id.banner_button);
        m_ctrlTitle = (TextView)findViewById(R.id.title);
        m_ctrlSpinner = (WheelView)findViewById(R.id.spinner);
        m_ctrlCardNumber = (EditText)findViewById(R.id.cardNumber);
        m_ctrlCardName = (EditText)findViewById(R.id.cardName);
        m_ctrlCardMonth = (WheelView)findViewById(R.id.cardMonth);
        m_ctrlCardYear = (WheelView)findViewById(R.id.cardYear);
        m_ctrlCardCW = (EditText)findViewById(R.id.cardCW);

        m_ctrlTitle.setText(getButtonName("lab_ViewCB", Constants.CurrentLang));

        m_ctrlCardNumber.setHint(getButtonName("lab_CCnumber", Constants.CurrentLang));
        m_ctrlCardName.setHint(getButtonName("lab_nom", Constants.CurrentLang));
        m_ctrlCardCW.setHint(getButtonName("lab_CW", Constants.CurrentLang));
        m_ctrlValider.setText(getButtonName("btn_valider", Constants.CurrentLang));
        m_ctrlAnnuller.setText(getButtonName("btn_annuler", Constants.CurrentLang));

        customProgressDialog = new CustomProgressDialog(this, "");
        cardType = new ArrayList<String>();

        cardType.add("Sélectionner");
        cardType.add("MASTERCARD");
        cardType.add("VISA");
        cardType.add("CB");

        m_ctrlSpinner.setViewAdapter(new DateArrayAdapter(ViewCreditCard.this, cardType, 0));

        monthType = new ArrayList<String>();

        monthType.add(getButtonName("lab_month", Constants.CurrentLang));

        for(int i = 1; i < 13; i  ++)
        {
            if(i < 10)
            {
                monthType.add("0" + i);
            }else
                monthType.add("" + i);
        }

        m_ctrlCardMonth.setViewAdapter(new DateArrayAdapter(ViewCreditCard.this, monthType, 0));


        yearType = new ArrayList<String>();

        yearType.add(getButtonName("lab_year", Constants.CurrentLang));

        for(int i = 2016; i < 2026; i  ++)
        {
            yearType.add("" + i);
        }

        m_ctrlCardYear.setViewAdapter(new DateArrayAdapter(ViewCreditCard.this, yearType, 0));
        m_ctrlValider.setOnClickListener(this);
        m_ctrlAnnuller.setOnClickListener(this);
        m_ctrlBannerButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(ViewCreditCard.this, getButtonName("lab_PayCancel", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                if(Constants.IdPayClick == true)
                {
                    Intent i = new Intent(ViewCreditCard.this, ShowMoneyActivity.class);

                    startActivity(i);
                }else
                {
                    Intent i = new Intent(ViewCreditCard.this, ViewCCList.class);

                    startActivity(i);
                }

                finish();
            }
        });
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Toast.makeText(ViewCreditCard.this, getButtonName("lab_PayCancel", Constants.CurrentLang), Toast.LENGTH_LONG).show();

        if(Constants.IdPayClick == true)
        {
            Intent i = new Intent(ViewCreditCard.this, ShowMoneyActivity.class);

            startActivity(i);
        }else
        {
            Intent i = new Intent(ViewCreditCard.this, ViewCCList.class);

            startActivity(i);
        }

        finish();
    }

    @Override
    public void onClick(View v) {
        if(v == m_ctrlAnnuller)
        {
            Toast.makeText(ViewCreditCard.this, getButtonName("lab_PayCancel", Constants.CurrentLang), Toast.LENGTH_LONG).show();

            if(Constants.IdPayClick == true)
            {
                Intent i = new Intent(ViewCreditCard.this, ShowMoneyActivity.class);

                startActivity(i);
            }else
            {
                Intent i = new Intent(ViewCreditCard.this, ViewCCList.class);

                startActivity(i);
            }

            finish();

            return;
        }

        if(v == m_ctrlValider)
        {

            if(m_ctrlCardYear.getCurrentItem() == 0)
            {
                vibrate(ViewCreditCard.this);

                Toast.makeText(ViewCreditCard.this, getButtonName("lab_CBnotRegistered", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                return;
            }
            if(m_ctrlCardMonth.getCurrentItem() == 0)
            {
                vibrate(ViewCreditCard.this);
                Toast.makeText(ViewCreditCard.this, getButtonName("lab_CBnotRegistered", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                return;
            }

            if(m_ctrlCardNumber.getText().toString().trim().equals(""))
            {
                vibrate(ViewCreditCard.this);
                Toast.makeText(ViewCreditCard.this, getButtonName("lab_CBnotRegistered", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                return;
            }
            String strApi = "";
            if(Constants.IdPayClick == true)
            {
                strApi = "{Api:'UserCreditCardAddNew',";
                strApi += "User_PhoneNumber:'" + Constants.currentUser.user_phone + "',";
                strApi += "User_PhoneCountry:'" + Constants.currentUser.User_PhoneCountry + "',";
                strApi += "User_PinCode:'',";
                strApi += "TokenCustomer:'" + Constants.User_WalletId + "',";

            }else
            {
                strApi = "{Api:'PartnerCreditCardAddNew',";
                strApi += "Partner_PhoneNumber:'" + Constants.codeTel + "',";
                strApi += "Partner_PhoneCountry:'" + Constants.currentMarket.market_country + "',";
                strApi += "Partner_PinCode:'"+ Constants.codeMarket+"',";
                strApi += "TokenCustomer:'" + Constants.currentMarket.market_WalletId + "',";
            }
            strApi += "Index:'" + 1 + "',";
            strApi += "Type:'" + cardType.get(m_ctrlSpinner.getCurrentItem()) + "',";
            strApi += "Number:'" + m_ctrlCardNumber.getText().toString() + "',";
            strApi += "HolderName:'" + m_ctrlCardName.getText().toString() + "',";
            strApi += "Month:'" + monthType.get(m_ctrlCardMonth.getCurrentItem())+ "',";
            strApi += "Year:'" + yearType.get(m_ctrlCardYear.getCurrentItem())+ "',";
            strApi += "Code:'" + m_ctrlCardCW.getText().toString()+ "',";
            strApi += "PaymentSystem:'" + "Mobile"+ "',";
            strApi += "TokenBank:'" + ""+ "',";
            strApi += "ProcessToken:'" + Constants.ProcessToken+ "',";
            strApi += "Application_Token:'" + Constants.Application_Token+ "',";
            strApi += "Application_ServerId:'" + Constants.APPLICATION_SERVER_ID+ "',";

            strApi += "Application_Version:'" + Constants.Application_Version+ "'}";

            MyDBProcessing processing = MyDBProcessing.getInstance();

            customProgressDialog.show();
            customProgressDialog.setCancelable(false);

            View view = this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }

            processing.SendPost(strApi, sendHandler);
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
    public void interpret() throws JSONException {

        Constants.ErrorNumber = GetMatchString("ErrorNumber");

        Constants.Message = GetMatchString("Message");

        if(!Constants.ErrorNumber.equals("0")) {
            String[] temp = Constants.Message.split("] : ");
            if(temp.length > 1)
                Toast.makeText(ViewCreditCard.this, temp[1], Toast.LENGTH_LONG).show();
            else
                Toast.makeText(ViewCreditCard.this, Constants.Message, Toast.LENGTH_LONG).show();
            return;
        }

        Constants.ProcessToken = GetMatchString("ProcessToken");
        Constants.CCToken = GetMatchString("CreditCard_Token");

        Constants.currentMarket.market_cb = true;

        Card card = new Card();

        if(cardType.get(m_ctrlSpinner.getCurrentItem()).equals("MASTERCARD"))
        {
            card.nImage = R.drawable.mastercard;
        }else
        if(cardType.get(m_ctrlSpinner.getCurrentItem()).equals("VISA"))
        {
            card.nImage = R.drawable.visa;
        }else
        if(cardType.get(m_ctrlSpinner.getCurrentItem()).equals("CB"))
        {
            card.nImage = R.drawable.cb;
        }else
        if(cardType.get(m_ctrlSpinner.getCurrentItem()).equals("AMEX"))
        {
            card.nImage = R.drawable.amex;
        }else
        if(cardType.get(m_ctrlSpinner.getCurrentItem()).equals("DINERS"))
        {
            card.nImage = R.drawable.diners;
        }else
        {
            card.nImage = -1;
        }

        //card.strExpDate = monthAdater.getItem(m_ctrlCardMonth.getSelectedItemPosition()) + "/" + yearAdapter.getItem(m_ctrlCardYear.getSelectedItemPosition());

        card.strNumber = m_ctrlCardNumber.getText().toString();

        card.nIndex = 1;

        Constants.currentUser.cards.add(card);

        if(Constants.IdPayClick == true)
        {
            Intent i = new Intent(ViewCreditCard.this, ViewCCPay.class);

            startActivity(i);

            finish();

            return;
        }else
        {
            Intent i = new Intent(ViewCreditCard.this, ViewCCList.class);

            startActivity(i);

            finish();

            return;
        }
    }

    private class DateNumericAdapter extends NumericWheelAdapter {
        int currentItem;
        int currentValue;

        public DateNumericAdapter(Context context, int minValue, int maxValue,
                                  int current) {
            super(context, minValue, maxValue);
            this.currentValue = current;
            setTextSize(20);
        }

        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
                view.setTextColor(0xFF0000F0);
            }
            view.setTypeface(null, Typeface.BOLD);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }

    private class DateArrayAdapter extends ArrayWheelAdapter<String> {
        int currentItem;
        int currentValue;

        public DateArrayAdapter(Context context, ArrayList<String> items, int current) {
            super(context, items);
            this.currentValue = current;
            setTextSize(20);
        }

        @Override
        protected void configureTextView(TextView view) {
            super.configureTextView(view);
            if (currentItem == currentValue) {
                view.setTextColor(0xFF0000F0);
            }
            view.setTypeface(null, Typeface.BOLD);
        }

        @Override
        public View getItem(int index, View cachedView, ViewGroup parent) {
            currentItem = index;
            return super.getItem(index, cachedView, parent);
        }
    }

}
