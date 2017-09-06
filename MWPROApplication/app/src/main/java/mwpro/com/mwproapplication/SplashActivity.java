package mwpro.com.mwproapplication;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Handler;
import android.os.Message;
import android.preference.PreferenceManager;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MotionEvent;
import android.widget.Toast;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import mwpro.com.mwproapplication.data.Card;
import mwpro.com.mwproapplication.data.MarketVo;
import mwpro.com.mwproapplication.data.MyTitles;
import okhttp3.MediaType;
import okhttp3.OkHttpClient;

import static mwpro.com.mwproapplication.Constants.GETJSONOBJECT;
import static mwpro.com.mwproapplication.Constants.GetMatchString;
import static mwpro.com.mwproapplication.Constants.MyXmlToJSON;

public class SplashActivity extends AppCompatActivity {
    private Thread mThread;
    private boolean isFinish = false;
    OkHttpClient m_ctrlOKHttpClient = null;
    public static final MediaType JSON = MediaType.parse("application/x-www-form-urlencoded; charset=utf-8");
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        overridePendingTransition(R.anim.slide_in_left, R.anim.slide_out_right);

        setContentView(R.layout.activity_splash);

        overridePendingTransition(R.anim.slide_in_right,R.anim.slide_out_left);

        SharedPreferences preferences = PreferenceManager.getDefaultSharedPreferences(SplashActivity.this);

        Constants.Current_Country = preferences.getString("CURRENT_COUNTRY", "FR");
        Constants.CurrentLang = preferences.getString("CURRENT_LANG", "FR");
        Constants.Application_Version = preferences.getString("APPLICATIONVERSION", Build.VERSION.RELEASE);
        Constants.codeMarket = preferences.getString(Constants.PINCODE, "");
        Constants.codeTel = preferences.getString(Constants.PHONE_NUMBER, "");

        MyDBProcessing process = MyDBProcessing.getInstance();
        process.SendPost(bowlingJson(), interprets);
    }

    private Handler interprets = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            String str = (String)msg.obj;


            if(msg.arg2 == Constants.NET_ERR)
            {
                Toast.makeText(SplashActivity.this, getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                finish();

                return;
            }
            try {

                MyXmlToJSON(str);

                if(GETJSONOBJECT().has("Data"))
                {
                    JSONObject data = GETJSONOBJECT().getJSONObject("Data");

                    if(data.has("Row"))
                    {
                        JSONArray languageArray = data.getJSONArray("Row");

                        getTwoLanguage(languageArray);
                    }
                }

                interpret();

                if(Constants.codeTel.equals(Constants.EMPTY) || Constants.codeMarket.equals(Constants.EMPTY))
                {
                    Intent i = new Intent(SplashActivity.this, MainActivity.class);

                    startActivity(i);

                    finish();

                    return;
                }

                MyDBProcessing processing = MyDBProcessing.getInstance();

                processing.SendPost(PartnerLoginJSON(), Partnerlogin);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };
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
    public void getTwoLanguage(JSONArray languageArray) throws JSONException {

        Constants.myTitle.clear();

        for(int i = 0; i < languageArray.length(); i ++)
        {
            JSONObject obj = languageArray.getJSONObject(i);

            Constants.myTitle.add(new MyTitles());

            Constants.myTitle.get(Constants.myTitle.size() - 1).strTag = obj.getString("Tag");
            Constants.myTitle.get(Constants.myTitle.size() - 1).strFrace = obj.getString("TextFR");
            Constants.myTitle.get(Constants.myTitle.size() - 1).strEng = obj.getString("TextEN");
            Constants.myTitle.get(Constants.myTitle.size() - 1).strUS = obj.getString("TextUS");
        }

    }
    public void interpret() throws  JSONException {

        Constants.ErrorNumber = GetMatchString("ErrorNumber");
        Constants.ProcessToken = GetMatchString("ProcessToken");
    }
    public String PartnerLoginJSON()
    {
        String strApi = "{ Api: 'PartnerLogin', Partner_PinCode: '" + Constants.codeMarket + "', Partner_PhoneNumber:'" + Constants.codeTel + "', ";

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
    private Handler Partnerlogin = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            String str = (String)msg.obj;

            if(msg.arg2 == Constants.NET_ERR)
            {
                Toast.makeText(SplashActivity.this, getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                finish();

                return;
            }

            try {
                MyXmlToJSON(str);
                PartnerLoginInfo();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };


    public void PartnerLoginInfo() throws JSONException {

        Constants.ErrorNumber = GetMatchString("ErrorNumber");

        if(!Constants.ErrorNumber.equals("0")) {

            Toast.makeText(SplashActivity.this, getButtonName("lab_errorAssist", Constants.CurrentLang), Toast.LENGTH_LONG).show();

            Intent i = new Intent(SplashActivity.this, MainActivity.class);

            startActivity(i);

            finish();

            return;
        }

        Constants.Source = GetMatchString("Source");
        Constants.Message = GetMatchString("Message");
        Constants.ProcessToken = GetMatchString("ProcessToken");
        Constants.FirstLogin = false;
        Constants.currentMarket = new MarketVo(GETJSONOBJECT());

        Constants.currentMarket.Partner_PhoneNumber = GetMatchString("Partner_PhoneNumber");

        int nResult = CompareVersion(Constants.currentMarket.market_Version, Constants.Application_Version);

        if(nResult == 2)
        {
            Toast.makeText(SplashActivity.this, getButtonName("lab_PayCancel", Constants.CurrentLang), Toast.LENGTH_LONG).show();

            Intent i = new Intent(SplashActivity.this, MainActivity.class);

            startActivity(i);

            finish();

            return;
        }

        Constants.currentMarket.market_code = Constants.codeMarket;
        Constants.market_admin = Constants.currentMarket.Partner_AllowManagement;
        Constants.currentMarket.market_country = GetMatchString("Partner_PhoneCountry");
        Constants.Current_Country = GetMatchString("Partner_PhoneCountry");
        MyDBProcessing processing = new MyDBProcessing();
        processing.SendPost(CreditCardJSON(), CreditCardPass);
    }
    public String CreditCardJSON()
    {
        String strApi = "";

        strApi = "{Api:'CreditCardsList', Account_WalletId:'" +Constants.currentMarket.market_WalletId+ "',";
        strApi += "Account_PhoneNumber:'" + Constants.codeTel +"',";
        strApi += "Account_PhoneCountry:'" + Constants.currentMarket.market_country +"',";
        strApi += "ProcessToken: '" + Constants.ProcessToken.toString() +"',";
        strApi += "Application_Token: 'cc3d6bbcfa281ed05aba24540f6fb1503de0c952eb',";
        strApi += "Application_ServerId: '" + Constants.APPLICATION_SERVER_ID +"',";
        strApi += "Application_Version: '" + Constants.Application_Version +"'}";

        return strApi;
    }
    public void CreditCardList() throws  JSONException {

        Constants.ErrorNumber = GetMatchString("ErrorNumber");

        if (Constants.ErrorNumber.equals("11")) {

            Toast.makeText(SplashActivity.this, getButtonName("lab_errorAssist", Constants.CurrentLang), Toast.LENGTH_LONG).show();

            Intent i = new Intent(SplashActivity.this, MainActivity.class);

            startActivity(i);

            finish();

            return;
        }


        if(!Constants.ErrorNumber.equals("0"))
        {
            Constants.currentMarket.market_cb = false;
            if(Constants.currentMarket.market_Cluster.equals("0") && Constants.currentMarket.market_Insolvent.equals("false"))
            {
                Intent i = new Intent(SplashActivity.this, ShowMoneyActivity.class);

                startActivity(i);

                finish();

                return;
            }else if(Constants.currentMarket.market_BalanceColor == null || Constants.currentMarket.market_BalanceColor.equals("") || Constants.currentMarket.market_BalanceColor.equals("Green"))
            {
                Intent i = new Intent(SplashActivity.this, ShowMoneyActivity.class);

                startActivity(i);

                finish();

                return;
            }else
            {
                Toast.makeText(SplashActivity.this, getButtonName("lab_ActivateAccount", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                Intent i = new Intent(SplashActivity.this, ViewCCPay.class);

                startActivity(i);

                finish();

                return;

            }
        }else
        {
            JSONObject obj = GETJSONOBJECT();

            JSONObject dataObj = obj.getJSONObject("Data");

            if(dataObj.has("Row"))
            {
                Object com = dataObj.get("Row");

                if (com instanceof JSONArray)
                {
                    JSONArray rowResult = dataObj.getJSONArray("Row");

                    for(int i = 0; i < rowResult.length(); i ++) {
                        JSONObject perObject = rowResult.getJSONObject(i);

                        Constants.currentMarket.card.add(new Card());

                        Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).nIndex = Constants.currentMarket.card.size() - 1;

                        if(perObject.has("Token")) {
                            Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strToken = perObject.getString("Token");
                        }

                        if (perObject.has("Tag")) {
                            Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strTag = perObject.getString("Tag");

                            if(Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strTag.length() == 0)
                            {
                                Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strTagMini = "";
                            }else
                            {
                                Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strTagMini = Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strTagMini.substring(0, 1).toUpperCase();
                            }
                        }
                        if (perObject.has("Main")) {
                            Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strMain = perObject.getString("Main");
                        }

                        if (perObject.has("Type")) {
                            Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strType = perObject.getString("Type");
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

                        if (perObject.has("Number")) {
                            Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strNumber = perObject.getString("Number");
                            Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strNumberMini = Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strNumber.substring(Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strNumber.length() - 8);
                        }
                        if (perObject.has("Month")) {
                            Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strMonth = perObject.getString("Month");
                        }

                        if (perObject.has("Year")) {

                            Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strYear = perObject.getString("Year");
                            Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strExpDate = Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strMonth + "/" + Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strYear.substring(2, 4);
                        }
                    }
                }else if(com instanceof JSONObject)
                {
                    JSONObject objects = dataObj.getJSONObject("Row");

                    Constants.currentMarket.card.add(new Card());

                    Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).nIndex = Constants.currentMarket.card.size() - 1;

                    if(objects.has("Token")) {
                        Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strToken = objects.getString("Token");
                    }

                    if (objects.has("Tag")) {
                        Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strTag = objects.getString("Tag");

                        if(Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strTag.length() == 0)
                        {
                            Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strTagMini = "";
                        }else
                        {
                            Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strTagMini = Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strTag.substring(0, 1).toUpperCase();
                        }
                    }

                    if (objects.has("Main")) {
                        Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strMain = objects.getString("Main");
                    }

                    if (objects.has("Type")) {
                        Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strType = objects.getString("Type");
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

                    if (objects.has("Number")) {
                        Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strNumber = objects.getString("Number");
                        Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strNumberMini = Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strNumber.substring(Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strNumber.length() - 8);
                    }
                    if (objects.has("Month")) {
                        Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strMonth = objects.getString("Month");
                    }

                    if (objects.has("Year")) {

                        Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strYear = objects.getString("Year");
                        Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strExpDate = Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strMonth + "/" + Constants.currentMarket.card.get(Constants.currentMarket.card.size() - 1).strYear.substring(2, 4);
                    }
                }
            }

            Constants.currentMarket.market_cb = true;
            Constants.ProcessToken = GetMatchString("ProcessToken");
            if(Constants.currentMarket.market_ToPay > 0 && Float.parseFloat(Constants.currentMarket.market_demotrial) == 0 || Constants.currentMarket.market_demotrial == null){

                Toast.makeText(SplashActivity.this, getButtonName("lab_ToPay", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                Constants.AmountCB = Constants.currentMarket.market_ToPay;

                Intent i = new Intent(SplashActivity.this, ViewCCPay.class);

                startActivity(i);

                finish();

                return;
            }else
            {
                //goto Show Money
                Intent i = new Intent(SplashActivity.this, ShowMoneyActivity.class);

                startActivity(i);

                finish();

                return;
            }
        }
    }
    private Handler CreditCardPass = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            String str = (String)msg.obj;

            if(msg.arg2 == Constants.NET_ERR)
            {
                Toast.makeText(SplashActivity.this, getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                finish();

                return;
            }
            try {

                MyXmlToJSON(str);
                CreditCardList();

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    String bowlingJson() {
        return "{'Api':'TranslateContext',"
                + "'Context':'MobileApplicationPartners',"
                + "'ProcessToken':'',"
                + "'Application_Token':cc3d6bbcfa281ed05aba24540f6fb1503de0c952eb,"
                + "'Application_ServerId':'" + Constants.APPLICATION_SERVER_ID + "',"
                + "'Application_Version':'" + Build.VERSION.RELEASE + "'}";
    }

    private Handler mHandler = new Handler() {

        @Override
        public void handleMessage(Message msg) {
            if (msg.what == 0 && (!isFinish)) {
                stop();
            }
            super.handleMessage(msg);
        }

    };

    @Override
    protected void onDestroy() {
        try {
            mThread.interrupt();
            mThread = null;
        } catch (Exception e) {
        }
        super.onDestroy();
    }
    private void stop() {

    }
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        if (event.getAction() == MotionEvent.ACTION_DOWN) {
            stop();
        }
        return super.onTouchEvent(event);
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
}
