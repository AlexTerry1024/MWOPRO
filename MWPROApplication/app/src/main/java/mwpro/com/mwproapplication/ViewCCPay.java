package mwpro.com.mwproapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.pepperonas.materialdialog.MaterialDialog;

import org.json.JSONException;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import de.codecrafters.tableview.listeners.TableDataClickListener;
import mwpro.com.mwproapplication.data.MyTitles;

import static mwpro.com.mwproapplication.Constants.GETJSONOBJECT;
import static mwpro.com.mwproapplication.Constants.GetMatchString;
import static mwpro.com.mwproapplication.Constants.MyXmlToJSON;
import static mwpro.com.mwproapplication.Constants.formatNumber;
import static mwpro.com.mwproapplication.Constants.getButtonName;

public class ViewCCPay extends AppCompatActivity implements View.OnClickListener{

    ImageView m_ctrlBannerButton;

    TextView textTitle = null;
    TextView lab_amount = null;
    EditText idMontant = null;
    TextView labeltipsortotal = null;
    TextView tipsLabel = null;
    EditText idTips = null;
    TextView totalLabel = null;
    EditText total = null;
    TextView entercodepin = null;
    EditText codePin = null;
    TextView lab6numbers = null;
    ImageView ccImage = null;
    TextView ccLabel = null;
    TextView Tag = null;
    TextView ccpayAlert = null;
    Button idCancel = null;
    Button idValider = null;
    Button idPinCode = null;
    TextView sendPincodeText = null;
    MyTableView tableView = null;
    MySimpleTableHeaderAdapter adapter = null;
    String _Amount = "";
    int _countErreur = 0;
    CustomProgressDialog customDialog = null;
    ImageView m_ctrlBanner = null;
    int nSelected = 0;

    //MaterialDialog.Builder ctrlTemplate = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.layout_ccpay);

        textTitle = (TextView)findViewById(R.id.title);

        textTitle.setText(Constants.viewVipText1);

        lab_amount = (TextView)findViewById(R.id.lab_amount);

        lab_amount.setText(getButtonName("lab_amount", Constants.CurrentLang));

        idMontant = (EditText)findViewById(R.id.idMontant);

        labeltipsortotal = (TextView)findViewById(R.id.labeltipsortotal);

        labeltipsortotal.setText(getButtonName("lab_tipsOrtotal", Constants.CurrentLang));

        tipsLabel = (TextView)findViewById(R.id.tipsLabel);
        idTips = (EditText)findViewById(R.id.idTips);
        idTips.setSingleLine();

        totalLabel = (TextView)findViewById(R.id.totallabel);

        totalLabel.setText(getButtonName("lab_CCpayTotal", Constants.CurrentLang));

        total = (EditText)findViewById(R.id.total);

        entercodepin = (TextView)findViewById(R.id.entercodepin);

        entercodepin.setText(getButtonName("lab_enterCodePin", Constants.CurrentLang));

        codePin = (EditText)findViewById(R.id.codePin);

        lab6numbers = (TextView)findViewById(R.id.lab6numbers);
        lab6numbers.setText(getButtonName("lab_6numbers", Constants.CurrentLang));
        ccImage = (ImageView)findViewById(R.id.CCImage);
        ccLabel = (TextView)findViewById(R.id.CCLabel);
        Tag = (TextView)findViewById(R.id.Tag);
        ccpayAlert = (TextView)findViewById(R.id.ccpayalert);
        ccpayAlert.setText(getButtonName("lab_CCpayAlert", Constants.CurrentLang));
        idCancel = (Button)findViewById(R.id.idCancel);
        idCancel.setText(getButtonName("btn_annuler", Constants.CurrentLang));
        idValider = (Button)findViewById(R.id.idValider);
        idValider.setText(getButtonName("btn_valider", Constants.CurrentLang));
        idPinCode = (Button)findViewById(R.id.idPinCodeCode);
        idPinCode.setText(getButtonName("btn_SendPinCode", Constants.CurrentLang));
        sendPincodeText = (TextView)findViewById(R.id.sendPincodeText);
        sendPincodeText.setText(getButtonName("lab_SendPinCodeText", Constants.CurrentLang));
        idValider.setEnabled(false);

        idValider.setOnClickListener(this);
        idPinCode.setOnClickListener(this);
        idCancel.setOnClickListener(this);

        m_ctrlBanner = (ImageView)findViewById(R.id.banner_button);

        customDialog = new CustomProgressDialog(this, "");
        if(!Constants.currentMarket.market_country.equals("US"))
        {
            findViewById(R.id.TipsGroup).setVisibility(View.GONE);
        }

        if(Constants.IdPayClick == false)
        {
            findViewById(R.id.idGroupPinCode).setVisibility(View.GONE);
            findViewById(R.id.idGroupSendPinCode).setVisibility(View.GONE);

            idValider.setEnabled(true);
        }else
        {
            findViewById(R.id.idGroupMessage).setVisibility(View.GONE);
        }

        if(Constants.currentMarket.market_ToPay > 0)
        {
            if(Constants.currentMarket.card.size() > 1)
            {
                selectMarket();

                findViewById(R.id.idItem).setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(View v) {
                        selectMarket();
                    }
                });
            }else
            {
                Constants.CCToken = Constants.currentMarket.card.get(0).strToken;
                Constants.CCIndex = Constants.currentMarket.card.get(0).nIndex;
                ccLabel.setText(Constants.currentMarket.card.get(0).strNumber + " " + Constants.currentMarket.card.get(0).strExpDate);
                ccImage.setImageResource(Constants.currentMarket.card.get(0).nImage);
                Tag.setText(Constants.currentMarket.card.get(0).strTag);
            }
        }else if(Constants.currentUser.cards.size() > 1)
        {
            selectUser();

            findViewById(R.id.idItem).setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    selectUser();
                }
            });
        }else
        {
            Constants.CCToken = Constants.currentUser.cards.get(0).strToken;
            Constants.CCIndex = Constants.currentUser.cards.get(0).nIndex;
            ccLabel.setText(Constants.currentUser.cards.get(0).strNumber + " " + Constants.currentMarket.card.get(0).strExpDate);
            ccImage.setImageResource(Constants.currentUser.cards.get(0).nImage);
            Tag.setText(Constants.currentUser.cards.get(0).strTag);
        }

        idMontant.setText(formatNumber("" + Constants.AmountCB, 2, true, false));
        tipsLabel.setText(getButtonName("lab_tips", Constants.CurrentLang) + " : " + formatNumber("" + Constants.currentMontant * 0.15, 2, true, false) + " ?") ;

        initUI();
    }
    public void selectMarket()
    {
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.mytable_view, null);

        tableView = (MyTableView)view.findViewById(R.id.mytableView);

        adapter = new MySimpleTableHeaderAdapter(ViewCCPay.this , new String[] {getButtonName("lab_ccType", Constants.CurrentLang), getButtonName("lab_CCnumber", Constants.CurrentLang), getButtonName("lab_expdate", Constants.CurrentLang), getButtonName("lab_ccTag", Constants.CurrentLang)});

        ArrayList<String[]> arrayList = new ArrayList<String[]>();

        for (int i = 0; i < Constants.currentMarket.card.size(); i ++)
        {
            String[] xml = new String[4];

            xml[0] = Constants.currentMarket.card.get(i).strType;
            xml[1] = Constants.currentMarket.card.get(i).strNumberMini;
            xml[2] = Constants.currentMarket.card.get(i).strExpDate;
            xml[3] = Constants.currentMarket.card.get(i).strTagMini;

            arrayList.add(xml);
        }

        tableView.setHeaderAdapter(adapter);

        MySimpleTableDataAdapter dataAdapter = new MySimpleTableDataAdapter(ViewCCPay.this, arrayList);

        tableView.setDataAdapter(dataAdapter);

        tableView.addDataClickListener(new TableDataClickListener() {
            @Override
            public void onDataClicked(int rowIndex, Object clickedData) {
                tableView.getDataAdapter().selectChanged(rowIndex);
                nSelected = rowIndex;
            }
        });
        tableView.getDataAdapter().selectChanged(nSelected);
        new MaterialDialog.Builder(ViewCCPay.this).customView(view).fullscreen(false)
                .positiveText(getButtonName("lab_OK", Constants.CurrentLang))

                .buttonCallback(new MaterialDialog.ButtonCallback()
                {
                    @Override
                    public void onPositive(MaterialDialog dialog)
                    {
                        super.onPositive(dialog);

                        int nSelectedData = tableView.getDataAdapter().getSelect();

                        Constants.CCToken = Constants.currentMarket.card.get(nSelectedData).strToken;
                        Constants.CCIndex = Constants.currentMarket.card.get(nSelectedData).nIndex;
                        ccLabel.setText(Constants.currentMarket.card.get(nSelectedData).strNumber + " " + Constants.currentMarket.card.get(nSelectedData).strExpDate);
                        ccImage.setImageResource(Constants.currentMarket.card.get(nSelectedData).nImage);
                        Tag.setText(Constants.currentMarket.card.get(nSelectedData).strTag);

                        dialog.dismiss();
                    }
                }).show();
    }
    public void selectUser()
    {
        LayoutInflater inflater = (LayoutInflater)getSystemService(LAYOUT_INFLATER_SERVICE);

        View view = inflater.inflate(R.layout.mytable_view, null);

        tableView = (MyTableView)view.findViewById(R.id.mytableView);

        adapter = new MySimpleTableHeaderAdapter(ViewCCPay.this , new String[] {getButtonName("lab_ccType", Constants.CurrentLang), getButtonName("lab_CCnumber", Constants.CurrentLang), getButtonName("lab_expdate", Constants.CurrentLang), getButtonName("lab_ccTag", Constants.CurrentLang)});

        ArrayList<String[]> arrayList = new ArrayList<String[]>();

        for (int i = 0; i < Constants.currentUser.cards.size(); i ++)
        {
            String[] xml = new String[4];

            xml[0] = Constants.currentUser.cards.get(i).strType;
            xml[1] = Constants.currentUser.cards.get(i).strNumberMini;
            xml[2] = Constants.currentUser.cards.get(i).strExpDate;
            xml[3] = Constants.currentUser.cards.get(i).strTagMini;

            arrayList.add(xml);
        }

        tableView.setHeaderAdapter(adapter);

        MySimpleTableDataAdapter dataAdapter = new MySimpleTableDataAdapter(ViewCCPay.this, arrayList);

        tableView.setDataAdapter(dataAdapter);

        tableView.addDataClickListener(new TableDataClickListener() {
            @Override
            public void onDataClicked(int rowIndex, Object clickedData) {
                tableView.getDataAdapter().selectChanged(rowIndex);
                nSelected = rowIndex;
            }
        });
        tableView.getDataAdapter().selectChanged(nSelected);
        new MaterialDialog.Builder(ViewCCPay.this).customView(view)
                .positiveText(getButtonName("lab_OK", Constants.CurrentLang))

                .buttonCallback(new MaterialDialog.ButtonCallback()
                {
                    @Override
                    public void onPositive(MaterialDialog dialog)
                    {
                        super.onPositive(dialog);

                        int nSelectedData = tableView.getDataAdapter().getSelect();

                        Constants.CCToken = Constants.currentUser.cards.get(nSelectedData).strToken;
                        Constants.CCIndex = Constants.currentUser.cards.get(nSelectedData).nIndex;
                        ccLabel.setText(Constants.currentUser.cards.get(nSelectedData).strNumber + " " + Constants.currentUser.cards.get(nSelectedData).strExpDate);
                        ccImage.setImageResource(Constants.currentUser.cards.get(nSelectedData).nImage);
                        Tag.setText(Constants.currentUser.cards.get(nSelectedData).strTag);

                        dialog.dismiss();
                    }

                }).show();
    }
    public void initUI()
    {
        codePin.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {

            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {

            }

            @Override
            public void afterTextChanged(Editable s) {
                if(codePin.getText().toString().equals("") || codePin.getText().toString().length() != 6)
                {
                    idValider.setEnabled(false);
                }else
                    idValider.setEnabled(true);
            }
        });

        total.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String str = total.getText().toString();

                Float x = Float.parseFloat(str.equals("")?"0":str);

                Float y = Float.parseFloat(idMontant.getText().toString().equals("")? "0": idMontant.getText().toString());

                idTips.setText(formatNumber("" + (x - y) , 2, true, false));

                if(Float.parseFloat(idTips.getText().toString()) < 0)
                {
                    idTips.setText("0.00");
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = total.getText().toString();

                Float x = Float.parseFloat(str.equals("")?"0":str);

                Float y = Float.parseFloat(idMontant.getText().toString().equals("")? "0": idMontant.getText().toString());

                idTips.setText(formatNumber("" + (x - y) , 2, true, false));

                if(Float.parseFloat(idTips.getText().toString()) < 0)
                {
                    idTips.setText("0.00");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = total.getText().toString();

                Float x = Float.parseFloat(str.equals("")?"0":str);

                Float y = Float.parseFloat(idMontant.getText().toString().equals("")? "0": idMontant.getText().toString());

                idTips.setText(formatNumber("" + (x - y) , 2, true, false));

                if(Float.parseFloat(idTips.getText().toString()) < 0)
                {
                    idTips.setText("0.00");
                }
            }
        });

        idTips.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                String str = idTips.getText().toString();

                Float x = Float.parseFloat(str.equals("")?"0":str);

                Float y = Float.parseFloat(idMontant.getText().toString().equals("")? "0": idMontant.getText().toString());

                total.setText(formatNumber("" + (x + y) , 2, true, false));

                if(Float.parseFloat(total.getText().toString()) < 0)
                {
                    total.setText("0.00");
                }
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                String str = idTips.getText().toString();

                Float x = Float.parseFloat(str.equals("")?"0":str);

                Float y = Float.parseFloat(idMontant.getText().toString().equals("")? "0": idMontant.getText().toString());

                total.setText(formatNumber("" + (x + y) , 2, true, false));

                if(Float.parseFloat(total.getText().toString()) < 0)
                {
                    total.setText("0.00");
                }
            }

            @Override
            public void afterTextChanged(Editable s) {
                String str = idTips.getText().toString();

                Float x = Float.parseFloat(str.equals("")?"0":str);

                Float y = Float.parseFloat(idMontant.getText().toString().equals("")? "0": idMontant.getText().toString());

                total.setText(formatNumber("" + (x + y) , 2, true, false));

                if(Float.parseFloat(total.getText().toString()) < 0)
                {
                    total.setText("0.00");
                }
            }
        });
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
                    strResult = title.strEng;
            }
        }
        return strResult;
    }

    @Override
    public void onClick(View v) {
        switch(v.getId())
        {
            case R.id.banner_button:
                Intent i = new Intent(ViewCCPay.this, ViewAdmin.class);

                startActivity(i);

                finish();
                break;
            case R.id.idValider:
                Valider();
                break;

            case R.id.idCancel:
                Annullar();
                break;

            case R.id.idPinCodeCode:
                PinCode();
                break;

        }
    }


    public void Annullar()
    {
        if(Constants.IdPayClick == true)
        {
            Intent i = new Intent(ViewCCPay.this, ShowMoneyActivity.class);

            startActivity(i);

            finish();
        }else
        {
            if (Constants.market_admin && !Constants.currentMarket.market_Cluster.equals("0")) // Test si Boss ou pas démo ou pas solo
            {
                Intent i = new Intent(ViewCCPay.this, ViewAdmin.class);

                startActivity(i);

                finish();
            }
            else
            {
                Intent i = new Intent(ViewCCPay.this, MainActivity.class);

                startActivity(i);

                finish();
            }
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Annullar();
    }

    public void Valider()
    {
        Toast.makeText(ViewCCPay.this, getButtonName("lab_processTransac", Constants.CurrentLang), Toast.LENGTH_LONG).show();

        if(codePin.getText().toString().equals(Constants.codeMarket))
        {
            Toast.makeText(ViewCCPay.this, "CodePin du Client !!", Toast.LENGTH_LONG).show();

            idPinCode.setEnabled(true);

            Constants.CodePinText = "";
        }else {
            Constants.currentMontant += Float.parseFloat(idTips.getText().toString().equals("")?"0":idTips.getText().toString());

            Float CCToPay = Float.parseFloat(idMontant.getText().toString().equals("")? "0" : idMontant.getText().toString()) + Float.parseFloat(idTips.getText().toString().equals("")? "0" : idTips.getText().toString());

            if(Constants.IdPayClick == false) {
                PayCC(Constants.AmountCB, "", "", "", "");
            }else {
                executePayment(Constants.currentMontant, Constants.AmountUsed, 0, Constants.Percent, CCToPay, codePin.getText().toString());
            }
        }
    }

    public void PinCode()
    {
        String strApi = "{Api: 'UserPinCodeGet',";

        strApi += "User_PhoneCountry:'" + Constants.currentUser.User_PhoneCountry + "',";
        strApi += "User_PhoneNumber:'" + Constants.currentUser.user_phone + "',";
        strApi += "ProcessToken:'" + Constants.ProcessToken + "',";
        strApi += "Application_Token:'" + Constants.Application_Token + "',";
        strApi += "Application_ServerId:'" + Constants.APPLICATION_SERVER_ID + "',";
        strApi += "Application_Version:'" + Constants.Application_Version + "'}";

        customDialog.show();
        customDialog.setCancelable(false);

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        MyDBProcessing processing = MyDBProcessing.getInstance();

        processing.SendPost(strApi, sendPincode);
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
    Handler sendPincode = new Handler()
    {
        @Override
        public void handleMessage(Message msg){

            customDialog.dismiss();

            String str = (String) msg.obj;

            if(msg.arg2 == Constants.NET_ERR)
            {
                Toast.makeText(ViewCCPay.this, getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                return;
            }

            try {
                MyXmlToJSON(str);

                Constants.ErrorNumber = GetMatchString("ErrorNumber");

                if(Constants.ErrorNumber.equals("20"))
                {
                    Toast.makeText(ViewCCPay.this, getButtonName("lab_PincodeUnlnownUser", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                    return;
                }

                if(Constants.ErrorNumber.equals("23"))
                {
                    Toast.makeText(ViewCCPay.this, getButtonName("lab_DisabledUser", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                    return;
                }
                if(Constants.ErrorNumber.equals("0"))
                {
                    Constants.ProcessToken = GetMatchString("ProcessToken");
                    Toast.makeText(ViewCCPay.this, getButtonName("lab_PinCodeSent", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                    idPinCode.setEnabled(false);

                    return;
                }else
                {
                    Toast.makeText(ViewCCPay.this, getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                    return;
                }
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    };
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

        customDialog.show();

        customDialog.setCancelable(false);

        processing.SendPost(strApi, executePaymentHandle);
    }
    Handler executePaymentHandle = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            String str = (String)msg.obj;

            customDialog.dismiss();
            if(msg.arg2 != Constants.NET_ERR)
            {
                try {
                    executePaymentInterpret(str);
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else
            {
                Toast.makeText(ViewCCPay.this, getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                Intent i = new Intent(ViewCCPay.this, ViewStatusActivity.class);

                startActivity(i);

                finish();

                return;
            }
        }
    };
    public void executePaymentInterpret(String strData) throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();

        xpp.setInput(new StringReader(strData));
        int eventType = xpp.getEventType();
        String strTagName = "";

        String strMessage = "";
        String strProcessToken = "";
        while (eventType != XmlPullParser.END_DOCUMENT) {

            if (eventType == XmlPullParser.START_DOCUMENT) {

            } else if (eventType == XmlPullParser.START_TAG) {
                strTagName = xpp.getName();

            } else if (eventType == XmlPullParser.END_TAG) {

            } else if (eventType == XmlPullParser.TEXT) {

                if (strTagName != null && strTagName.equals("ErrorNumber")) {
                    Constants.ErrorNumber = xpp.getText();

                    if (Constants.ErrorNumber.equals("200"))   // test si Partner Insolvent et en théorie pas de règlement par CB car la CB recrédite
                    {
//
                        if (Constants.currentMarket.market_cb == false) {

                            Toast.makeText(ViewCCPay.this, getButtonName("lab_ValidAccount", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                            Constants.AmountCB = Constants.currentMarket.market_ToPay;

                            Intent i = new Intent(ViewCCPay.this, ViewCreditCard.class);

                            startActivity(i);

                            finish();

                            return;

                        }
                        else {
                            Toast.makeText(ViewCCPay.this, getButtonName("lab_depositError", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                            Intent i = new Intent(ViewCCPay.this, ShowMoneyActivity.class);

                            startActivity(i);

                            finish();

                            return;
                        }
                    }
                }
                if (strTagName != null && strTagName.equals("Message")) {
                    strMessage = xpp.getText();
                }

                if (strTagName != null && strTagName.equals("ProcessToken")) {
                    strProcessToken = xpp.getText();
                }
            }

            eventType = xpp.next();
        }
        if (Constants.ErrorNumber.equals("0")) {
            Constants.ProcessToken = strProcessToken;
            Toast.makeText(ViewCCPay.this, getButtonName("lab_credit_ok", Constants.CurrentLang), Toast.LENGTH_LONG).show();
            Constants.currentMarket.market_ToPay = 0;

            Intent i = new Intent(ViewCCPay.this, ShowMoneyActivity.class);

            startActivity(i);
            finish();

            return;
        }else if(Constants.ErrorNumber.equals("20"))
        {
            _countErreur ++;

            codePin.setText("");

            Toast.makeText(ViewCCPay.this, getButtonName("lab_codePinError", Constants.CurrentLang) + " " + _countErreur + "/3", Toast.LENGTH_LONG).show();
            if(_countErreur == 3)
            {
                Intent i = new Intent(ViewCCPay.this, ShowMoneyActivity.class);

                startActivity(i);

                finish();
            }

            return;
        }
        else if (Constants.ErrorNumber.equals("250"))   // test si erreur Payline - code Payline dans Message
        {
            String[] arrays = strMessage.split(" : ");

            Toast.makeText(ViewCCPay.this, arrays[1], Toast.LENGTH_LONG).show();
            Intent i = new Intent(ViewCCPay.this, ShowMoneyActivity.class);

            startActivity(i);

            finish();
        }
        else if (Constants.ErrorNumber.equals("257") || Constants.ErrorNumber.equals("256")) {
            Toast.makeText(ViewCCPay.this, strMessage, Toast.LENGTH_LONG).show();

            Constants.currentUser.cards.remove(Constants.CCIndex);
            Intent i = new Intent(ViewCCPay.this, ViewCCPay.class);

            startActivity(i);

            finish();
        }
        else {
            Toast.makeText(ViewCCPay.this, strMessage, Toast.LENGTH_LONG).show();

            Intent i = new Intent(ViewCCPay.this, ShowMoneyActivity.class);

            startActivity(i);

            finish();
        }
    }

    Handler PayCCHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {

            String strObj = (String)msg.obj;
            customDialog.dismiss();
            if(msg.arg2 != Constants.NET_ERR)
            {
                try {
                    Toast.makeText(ViewCCPay.this, getButtonName("lab_PayOk", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                    interpretPayCC(strObj);
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else
            {

                Toast.makeText(ViewCCPay.this, getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                Intent i = new Intent(ViewCCPay.this, ViewCCPay.class);

                startActivity(i);

                finish();
            }
        }
    };

    public void interpretPayCC(String strXml) throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();

        xpp.setInput(new StringReader(strXml));
        int eventType = xpp.getEventType();
        String strTagName = "";

        String strMessage = "";
        while (eventType != XmlPullParser.END_DOCUMENT) {

            if (eventType == XmlPullParser.START_DOCUMENT) {

            } else if (eventType == XmlPullParser.START_TAG) {
                strTagName = xpp.getName();

            } else if (eventType == XmlPullParser.END_TAG) {

            } else if (eventType == XmlPullParser.TEXT) {

                if (strTagName != null && strTagName.equals("ErrorNumber")) {
                    Constants.ErrorNumber = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("Message")) {
                    strMessage = xpp.getText();
                }
            }

            eventType = xpp.next();
        }

        if(Constants.ErrorNumber.equals("0"))
        {
            if(Constants.CodeError.equals("200"))
            {
                executePayment(Constants.CurrentTotalAmount, Constants.CurrentUsedPrice, Constants.CurrentTag, Constants.CurrentPercentage, Constants.CurrentUsedCB, "");
            }else
            {
                Toast.makeText(ViewCCPay.this, getButtonName("lab_PayOk", Constants.CurrentLang), Toast.LENGTH_LONG).show();
                Constants.currentMarket.market_ToPay = 0;
                Constants.currentMarket.market_status = "1";

                Intent i = new Intent(ViewCCPay.this, ShowMoneyActivity.class);

                startActivity(i);

                finish();
            }
        }else
        {
            Constants.viewVipText1 = strMessage;
            Constants.paymentError = strMessage;

            Intent i = new Intent(ViewCCPay.this, ViewStatusActivity.class);

            startActivity(i);

            finish();
        }
    }

    public void PayCC(float amount, String CodeError, String wallet, String Tag, String Country){

        Constants.viewVipText1 = getButtonName("lab_processCB", Constants.CurrentLang);
        Constants.CodeError = CodeError;
        String strApi = "{Api: 'TransactionAddNewCreditCardPayment',";

        strApi += "Partner_PinCode:'" + Constants.currentMarket.market_code + "',";
        strApi += "Partner_ExternalTag:'" + Tag + "',";
        strApi += "User_PhoneNumber:'" + wallet + "',";
        strApi += "User_PhoneCountry:'" + Country + "',";
        strApi += "Payment_Amount:'" + amount + "',";
        strApi += "Partner_PhoneNumber:'" + Constants.codeTel + "',";
        strApi += "Partner_PhoneCountry:'" + Constants.CurrentLang + "',";
        strApi += "ProcessToken:'" + Constants.ProcessToken + "',";
        strApi += "Application_Token:'" + Constants.Application_Token + "',";
        strApi += "Application_ServerId:'" + Constants.APPLICATION_SERVER_ID + "',";
        strApi += "Application_Version:'" + Build.VERSION.RELEASE + "'}";

        MyDBProcessing processing = new MyDBProcessing();

        customDialog.show();

        customDialog.setCancelable(false);

        processing.SendPost(strApi, PayCCHandler);
    }

}
