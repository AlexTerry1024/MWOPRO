package mwpro.com.mwproapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
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
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import de.codecrafters.tableview.listeners.TableDataClickListener;
import de.codecrafters.tableview.model.TableColumnWeightModel;
import mwpro.com.mwproapplication.data.Card;
import mwpro.com.mwproapplication.data.MyTitles;
import mwpro.com.mwproapplication.ui.CustomProgressDialog;
import mwpro.com.mwproapplication.ui.MySimpleTableDataAdapter;
import mwpro.com.mwproapplication.ui.MySimpleTableHeaderAdapter;
import mwpro.com.mwproapplication.ui.MyTableView;

import static mwpro.com.mwproapplication.Constants.GETJSONOBJECT;
import static mwpro.com.mwproapplication.Constants.GetMatchString;
import static mwpro.com.mwproapplication.Constants.MyXmlToJSON;
import static mwpro.com.mwproapplication.Constants.vibrate;

public class ViewCCList extends Activity implements View.OnClickListener{

    MyTableView m_ctrlTableView = null;
    MySimpleTableHeaderAdapter adapter = null;
    ArrayList<String[]> strData = new ArrayList<String[]>();
    ArrayList<String[]> strRealData = new ArrayList<String[]>();
    MySimpleTableDataAdapter myDataAdapter = null;
    CustomProgressDialog customDialog = null;
    ImageView m_ctrlBanner = null;
    Button m_ctrlCancel = null;
    Button m_ctrlChange = null;
    Button m_ctrlPay = null;
    int nSelected = -1;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_activate);

        m_ctrlTableView = (MyTableView)findViewById(R.id.tableView);

        m_ctrlBanner = (ImageView)findViewById(R.id.banner_button);

        m_ctrlCancel = (Button)findViewById(R.id.cancel);
        m_ctrlChange = (Button)findViewById(R.id.change);
        m_ctrlPay = (Button)findViewById(R.id.pay);
        strData.clear();
        strRealData.clear();
        adapter = new MySimpleTableHeaderAdapter(ViewCCList.this , new String[] {getButtonName("lab_CBList", Constants.CurrentLang), getButtonName("lab_expdate", Constants.CurrentLang)});

        m_ctrlTableView.setHeaderAdapter(adapter);

        m_ctrlTableView.setColumnCount(2);

        TableColumnWeightModel columnModel = new TableColumnWeightModel(2);

        columnModel.setColumnWeight(0, 1);
        columnModel.setColumnWeight(1, 1);

        m_ctrlTableView.setHeaderElevation(11);

        m_ctrlTableView.setColumnModel(columnModel);

        customDialog = new CustomProgressDialog(ViewCCList.this, "");

        customDialog.show();

        customDialog.setCancelable(false);

        View view = this.getCurrentFocus();
        if (view != null) {
            InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
            imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
        }

        getCBList(Constants.currentMarket.market_WalletId, Constants.codeTel,Constants.CurrentLang);

        m_ctrlPay.setVisibility(View.GONE);
        m_ctrlChange.setEnabled(false);

        m_ctrlChange.setOnClickListener(this);
        m_ctrlCancel.setOnClickListener(this);
        m_ctrlPay.setOnClickListener(this);
        m_ctrlChange.setText(getButtonName("lab_addCB", Constants.CurrentLang));
        m_ctrlCancel.setText(getButtonName("btn_annuler", Constants.CurrentLang));
        m_ctrlPay.setText(getButtonName("lab_CCpay", Constants.CurrentLang));
        m_ctrlTableView.addDataClickListener(new TableDataClickListener() {
            @Override
            public void onDataClicked(int rowIndex, Object clickedData) {
                if(myDataAdapter != null) {
                    myDataAdapter.selectChanged(rowIndex);
                    nSelected = rowIndex;
                }
            }
        });
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

    Handler interpretHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            String strObj = (String) msg.obj;

            customDialog.dismiss();

            if(msg.arg2 != Constants.NET_SUC) {
                Toast.makeText(ViewCCList.this, getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                return;
            }

            try {

                MyXmlToJSON(strObj);

                interpret();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
    public void interpret() throws JSONException {
        Constants.ErrorNumber = GetMatchString("ErrorNumber");

        if(Constants.ErrorNumber.equals("11")) {
            Toast.makeText(ViewCCList.this, getButtonName("lab_errorAssist", Constants.CurrentLang), Toast.LENGTH_LONG).show();

            Intent i = new Intent(ViewCCList.this, MainActivity.class);

            startActivity(i);

            finish();

            return;
        }else if(!Constants.ErrorNumber.equals("0")) {
            m_ctrlChange.setEnabled(true);

            vibrate(ViewCCList.this);

            Toast.makeText(ViewCCList.this, getButtonName("lab_CBnotRegistered", Constants.CurrentLang), Toast.LENGTH_LONG).show();

            Constants.currentMarket.market_cb = false;

            return;
        }

        m_ctrlChange.setEnabled(true);

        Constants.ProcessToken = GetMatchString("ProcessToken");

        if(GETJSONOBJECT().has("Data"))
        {
            JSONObject dataObject = GETJSONOBJECT().getJSONObject("Data");

            if(dataObject.has("Row"))
            {
                Object row = dataObject.get("Row");
                Constants.currentMarket.card.clear();
                if(row instanceof JSONArray)
                {
                    JSONArray arrays = dataObject.getJSONArray("Data");

                    for (int i = 0; i < arrays.length(); i ++)
                    {
                        JSONObject obj = arrays.getJSONObject(i);

                        strData.add(new String[8]);

                        Card card = new Card();

                        card.nIndex = i;
                        if(obj.has("Token"))
                        {
                            strData.get(strData.size() - 1)[0] = obj.getString("Token");

                            card.strToken = obj.getString("Token");
                        }

                        if(obj.has("Tag"))
                        {
                            strData.get(strData.size() - 1)[1] = obj.getString("Tag");

                            card.strTag = obj.getString("Tag");

                            card.strTagMini = card.strTag.toUpperCase().substring(0, 1);
                        }

                        if(obj.has("Main"))
                        {
                            strData.get(strData.size() - 1)[2] = obj.getString("Main");

                            card.strMain = obj.getString("Main");
                        }

                        if(obj.has("Type"))
                        {
                            strData.get(strData.size() - 1)[3] = obj.getString("Type");

                            String strType = obj.getString("Type");

                            if(strType.toUpperCase().equals("MASTERCARD"))
                            {
                                card.nImage = R.drawable.mastercard;
                            }
                            if(strType.toUpperCase().equals("VISA"))
                            {
                                card.nImage = R.drawable.visa;
                            }
                            if(strType.toUpperCase().equals("CB"))
                            {
                                card.nImage = R.drawable.cb;
                            }
                            if(strType.toUpperCase().equals("AMEX"))
                            {
                                card.nImage = R.drawable.amex;
                            }
                            if(strType.toUpperCase().equals("DINERS"))
                            {
                                card.nImage = R.drawable.diners;
                            }

                            card.strType = strType.substring(0, 4);
                        }

                        if(obj.has("Number"))
                        {
                            strData.get(strData.size() - 1)[4] = obj.getString("Number");

                            card.strNumber = obj.getString("Number");

                            card.strNumberMini = card.strNumber.substring(card.strNumber.length() - 8, card.strNumber.length());
                        }

                        if(obj.has("Month"))
                        {
                            strData.get(strData.size() - 1)[5] = obj.getString("Month");


                        }
                        if(obj.has("Year"))
                        {
                            strData.get(strData.size() - 1)[6] = obj.getString("Year");
                        }
                        if(obj.has("HolderName"))
                        {
                            strData.get(strData.size() - 1)[7] = obj.getString("HolderName");
                        }

                        card.strExpDate = card.strMonth + "/" + card.strYear.substring(2, 4);

                        Constants.currentMarket.card.add(card);
                    }
                }else if(row instanceof JSONObject)
                {
                    JSONObject obj = dataObject.getJSONObject("Row");

                    strData.add(new String[8]);

                    Card card = new Card();

                    card.nIndex = 0;
                    if(obj.has("Token"))
                    {
                        strData.get(strData.size() - 1)[0] = obj.getString("Token");

                        card.strToken = obj.getString("Token");
                    }

                    if(obj.has("Tag"))
                    {
                        strData.get(strData.size() - 1)[1] = obj.getString("Tag");

                        card.strTag = obj.getString("Tag");

                        card.strTagMini = card.strTag.toUpperCase().substring(0, 1);
                    }

                    if(obj.has("Main"))
                    {
                        strData.get(strData.size() - 1)[2] = obj.getString("Main");

                        card.strMain = obj.getString("Main");
                    }

                    if(obj.has("Type"))
                    {
                        strData.get(strData.size() - 1)[3] = obj.getString("Type");

                        String strType = obj.getString("Type");

                        if(strType.toUpperCase().equals("MASTERCARD"))
                        {
                            card.nImage = R.drawable.mastercard;
                        }
                        if(strType.toUpperCase().equals("VISA"))
                        {
                            card.nImage = R.drawable.visa;
                        }
                        if(strType.toUpperCase().equals("CB"))
                        {
                            card.nImage = R.drawable.cb;
                        }
                        if(strType.toUpperCase().equals("AMEX"))
                        {
                            card.nImage = R.drawable.amex;
                        }
                        if(strType.toUpperCase().equals("DINERS"))
                        {
                            card.nImage = R.drawable.diners;
                        }

                        card.strType = strType.substring(0, 4);
                    }

                    if(obj.has("Number"))
                    {
                        strData.get(strData.size() - 1)[4] = obj.getString("Number");

                        card.strNumber = obj.getString("Number");

                        card.strNumberMini = card.strNumber.substring(card.strNumber.length() - 8, card.strNumber.length());
                    }

                    if(obj.has("Month"))
                    {
                        strData.get(strData.size() - 1)[5] = obj.getString("Month");

                        card.strMonth = obj.getString("Month");

                    }
                    if(obj.has("Year"))
                    {
                        strData.get(strData.size() - 1)[6] = obj.getString("Year");

                        card.strYear = obj.getString("Year");
                    }
                    if(obj.has("HolderName"))
                    {
                        strData.get(strData.size() - 1)[7] = obj.getString("HolderName");
                    }

                    card.strExpDate = card.strMonth + "/" + card.strYear.substring(2, 4);

                    Constants.currentMarket.card.add(card);
                }
            }
        }
        for(int i = 0; i < strData.size(); i ++)
        {
            String[] perItem = new String[2];

            perItem[0] = strData.get(i)[4];
            perItem[1] = strData.get(i)[5].length() == 1 ? "0" + strData.get(i)[5] : strData.get(i)[5] ;

            perItem[1] += "/" + strData.get(i)[6];

            strRealData.add(perItem);
        }
        Constants.currentMarket.market_cb = true;

        if(Constants.currentMarket.market_status.equals("0") || Constants.currentMarket.market_ToPay > 0)
        {
            m_ctrlPay.setVisibility(View.VISIBLE);
        }

        myDataAdapter = new MySimpleTableDataAdapter(ViewCCList.this, strRealData);

        m_ctrlTableView.setDataAdapter(myDataAdapter);
    }
    public void getCBList(String strWallet, String phone, String country)
    {
        Constants.currentMarket.market_WalletId = strWallet;

        String strApi = "{Api: 'CreditCardsList',";

        strApi += "Account_WalletId:'" + strWallet + "',";
        strApi += "Account_PhoneNumber:'" + phone +"',";
        strApi += "Account_PhoneCountry:'" + country + "',";
        strApi += "ProcessToken:'"+ Constants.ProcessToken +"',";
        strApi += "Application_Token:'" + Constants.Application_Token + "',";
        strApi += "Application_ServerId:'" + Constants.APPLICATION_SERVER_ID + "',";
        strApi += "Application_Version:'" + Constants.Application_Version + "'}";

        MyDBProcessing processing = MyDBProcessing.getInstance();

        customDialog.show();

        customDialog.setCancelable(false);

        processing.SendPost(strApi, interpretHandler);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        if (Constants.market_admin && !Constants.currentMarket.market_Cluster.equals("0")) // Test si Boss ou pas démo ou pas solo
        {
            Intent i = new Intent(ViewCCList.this, ViewAdmin.class);

            startActivity(i);

            finish();
        }
        else
        {
            Intent i = new Intent(ViewCCList.this, MainActivity.class);

            startActivity(i);

            finish();
        }
    }

    @Override
    public void onClick(View v) {
        if(v == m_ctrlBanner)
        {
            Intent i = new Intent(ViewCCList.this, ViewAdmin.class);

            startActivity(i);

            finish();
        }
        if (v == m_ctrlCancel)
        {
            if (Constants.market_admin && !Constants.currentMarket.market_Cluster.equals("0")) // Test si Boss ou pas démo ou pas solo
            {
                Intent i = new Intent(ViewCCList.this, ViewAdmin.class);

                startActivity(i);

                finish();
            }
            else
            {
                Intent i = new Intent(ViewCCList.this, MainActivity.class);

                startActivity(i);

                finish();
            }
        }
        if (v == m_ctrlChange)
        {
            Constants.AmountCB = Constants.currentMarket.market_ToPay;

            Intent i = new Intent(ViewCCList.this, ViewCreditCard.class);

            startActivity(i);

            finish();
        }
        if (v == m_ctrlPay)
        {
            Toast.makeText(ViewCCList.this, getButtonName("lab_ActivateAccount", Constants.CurrentLang), Toast.LENGTH_LONG).show();

            Constants.AmountCB = Constants.currentMarket.market_ToPay;

            Constants.IdPayClick = false;

            Intent i = new Intent(ViewCCList.this, ViewCCPay.class);

            startActivity(i);

            finish();
        }
    }
}
