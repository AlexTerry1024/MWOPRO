package mwpro.com.mwproapplication;


import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Handler;
import android.os.Vibrator;
import android.widget.Toast;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;
import android.os.Message;
import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import mwpro.com.mwproapplication.data.MarketVo;
import mwpro.com.mwproapplication.data.MyTitles;
import mwpro.com.mwproapplication.data.UserVo;
import mwpro.com.mwproapplication.data.XmlToJson;

public class Constants {
    public static int APPLICATION_SERVER_ID = 1;
    public static int NET_ERR = 3030;
    public static int NET_SUC = 1024;
    public static int CCIndex = 0;
    public static int currentCoupon = 0;

    public static String PHONE_NUMBER = "PHONE_NUMBER";
    public static String PINCODE = "PINCODE";
    public static String EMPTY = "";
    public static String Application_Version="4.2.2";
    public static String _APPKEY = "723963818cd42349e0b6157b760166e8c7ce3fddeVArHs8wTOndk79WXjwo61DF";
    public static String OS = "android";
    public static String ErrorNumber = "";
    public static String Source = "";
    public static String Message = "";
    public static String ProcessToken = "";
    public static String WaitCount = "";
    public static String RecallCount = "";
    public static String Partner_Id = "";
    public static String User_Id = "";
    public static String Partner_PhonePushCode = "";
    public static String PUSH_CODE = "";
    public static String Partner_Language = "";
    public static String CurrentLang = "FR";
    public static String Current_Country = "FR";
    public static String Application_Token = "cc3d6bbcfa281ed05aba24540f6fb1503de0c952eb";
    public static String viewVipText1 = "";
    public static String viewVipText2 = "";
    public static String GCM_SENDER_ID = "1083433096344";
    public static String CCToken = "";
    public static String CodeError = "";
    public static String CurrentPercentage = "";
    public static String paymentError = "";
    public static String Percent = "";
    public static String User_WalletId = "";
    public static String Email = "";
    public static String CodePinText = "";
    public static String codeTel = "";
    public static String codeMarket = "";
    public static String codePinText = "";

    public static ArrayList<MyTitles> myTitle = new ArrayList<MyTitles>();
    public static MarketVo currentMarket = null;
    public static UserVo currentUser = null;
    public static Context context = null;
    public static JSONObject jsonObject = null;
    Vibrator vibrator = null;

    public static float currentMontant = 0;
    public static float AmountCB = 0;
    public static float CurrentTotalAmount = 0;
    public static float CurrentUsedPrice = 0;
    public static float CurrentUsedCB = 0;
    public static float CurrentTag = 0;
    public static float PaylineRetry = 0;
    public static float AmountToPay = 0;
    public static float AmountUsed = 0;

    public static boolean ExecutePaymentAfterVip = false;
    public static boolean IdPayClick = false;
    public static boolean FirstLogin = true;
    public static boolean CB = false;
    public static boolean market_admin = false;

    public static void vibrate(Context con)
    {
        if(con == null)
            return;

        Vibrator vibrator = (Vibrator)con.getSystemService(Context.VIBRATOR_SERVICE);
        vibrator.vibrate(500);
    }
    public static void MyXmlToJSON(String str) throws JSONException {
        XmlToJson json = new  XmlToJson.Builder(str).build();

        JSONObject jsons = json.toJson();

        jsonObject = jsons.getJSONObject("Response");

        int a = 0;
        a++;
    }
    public static JSONObject GETJSONOBJECT()
    {
        return jsonObject;
    }
    public static String GetMatchString(String strText) throws JSONException {
        String strResult = "";

        if(jsonObject != null)
        {
            if(jsonObject.has(strText))
            {
                return jsonObject.getString(strText);
            }
        }

        return strResult;
    }

    public static String getButtonName(String strTagName, String strLanguage)
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

    static Handler executePaymentHandle = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            String str = (String)msg.obj;

            if(msg.arg2 != Constants.NET_ERR)
            {
                try {
                    MyXmlToJSON(str);
                    executePaymentInterpret(str);
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else
            {
                Toast.makeText(context, getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                Intent i = new Intent(context, ViewStatusActivity.class);
                context.startActivity(i);
            }
        }
    };
    public static void executePaymentInterpret(String strData) throws XmlPullParserException, IOException, JSONException {

        String strTagName = "";
        String strMessage = "";
        String strProcessToken = "";

        Constants.ErrorNumber = GetMatchString("ErrorNumber");

        if (Constants.ErrorNumber.equals("200"))   // test si Partner Insolvent et en théorie pas de règlement par CB car la CB recrédite
        {

            if (Constants.currentMarket.market_cb == false) {

                Toast.makeText(context, getButtonName("lab_ValidAccount", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                Constants.AmountCB = Constants.currentMarket.market_ToPay;

                return;
            }
            else {
                Toast.makeText(context, getButtonName("lab_depositError", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                Intent i = new Intent(context, ShowMoneyActivity.class);

                context.startActivity(i);

                ((Activity)context).finish();

                return;
            }

        }

        strMessage = GetMatchString("Message");
        strProcessToken = GetMatchString("ProcessToken");


        if (Constants.ErrorNumber.equals("0")) {
            Constants.ProcessToken = strProcessToken;
            Toast.makeText(context, getButtonName("lab_credit_ok", Constants.CurrentLang), Toast.LENGTH_LONG).show();

            Constants.currentMarket.market_ToPay = 0;


            Intent i = new Intent(context, ShowMoneyActivity.class);

            context.startActivity(i);

            ((Activity)context).finish();

            return;
        }
        else if (Constants.ErrorNumber.equals("250"))   // test si erreur Payline - code Payline dans Message
        {
           String[] arrays = strMessage.split(" : ");

            Toast.makeText(context, arrays[1], Toast.LENGTH_LONG).show();
            Intent i = new Intent(context, ShowMoneyActivity.class);

            context.startActivity(i);

            ((Activity)context).finish();
        }
        else if (Constants.ErrorNumber.equals("257") || Constants.ErrorNumber.equals("256")) {
                Toast.makeText(context, strMessage, Toast.LENGTH_LONG).show();

            Constants.currentUser.cards.remove(Constants.CCIndex);
                Intent i = new Intent(context, ViewCCPay.class);

                context.startActivity(i);

                ((Activity)context).finish();
        }
        else {
            Toast.makeText(context, strMessage, Toast.LENGTH_LONG).show();

            Intent i = new Intent(context, ShowMoneyActivity.class);

            context.startActivity(i);

            ((Activity)context).finish();
        }
    }

    public static String formatNumber(String number, int maxDecimals, boolean forceDecimals, boolean siStyle) //MaxDecials default:2 forceDecimals:true, siStyle:false
    {
        int i = 0;

        double inc = Math.pow(10, maxDecimals);

        String str = "";
        str += Math.round(inc * Float.parseFloat(number)) / inc;

        boolean hasSep = str.indexOf(".") == -1;

        int sep = 0;
        if(hasSep == true)
        {
            sep = str.length();
        }else
        {
            sep = str.indexOf(".");
        }

        String ret = (hasSep && forceDecimals) ? "": (siStyle ? "," : ".") + str.substring(sep+1);


        if (forceDecimals) {
            for(int j = 0; j <= maxDecimals - (str.length() - (hasSep ? sep -1 : sep)); j ++)
            {
                ret += "0";
            }
        }

        while(i + 3 < (str.substring(0, 1) == "-" ? sep - 1:sep))
        {
            ret = (siStyle ? "." : ",") + str.substring(sep - (i += 3), 3) + ret;
        }

        return str.substring(0, sep - i) + ret;
    }
    /*public static void executePayment(float pTotalPrice, float pUsedPrice, float pTag, String percentage, float UsedCB, String CodePin){

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
        strApi += "Partner_PhoneNumber:'" + Constants._PHONE_NUMBER + "',";
        strApi += "Partner_PhoneCountry:'" + Constants.Current_Country + "',";
        strApi += "Transaction_AmountPaid:'" + UsedCB + "',";
        strApi += "ProcessToken:'" + Constants.ProcessToken + "',";
        strApi += "Application_Token:'" + Constants.Application_Token + "',";
        strApi += "Application_ServerId:'" + Constants.APPLICATION_SERVER_ID + "',";
        strApi += "Application_Version:'" + Build.VERSION.RELEASE + "',";
        strApi += "CreditCard_Token:'" + Constants.CCToken + "'}";

        MyDBProcessing processing = new MyDBProcessing();

        processing.SendPost(strApi, executePaymentHandle);
    }

    static Handler  PayCCHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {

            String strObj = (String)msg.obj;

            if(msg.arg2 != Constants.NET_ERR)
            {
                try {
                    Toast.makeText(context, getButtonName("lab_PayOk", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                    interpretPayCC(strObj);
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else
            {

                Toast.makeText(context, getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                Intent i = new Intent(context, ViewCCPay.class);

                context.startActivity(i);

                ((Activity)context).finish();
            }
        }
    };

    public static void interpretPayCC(String strXml) throws XmlPullParserException, IOException {
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
                Constants.executePayment(CurrentTotalAmount, CurrentUsedPrice, CurrentTag, CurrentPercentage, CurrentUsedCB, "");
            }else
            {
                Toast.makeText(context, getButtonName("lab_PayOk", CurrentLang), Toast.LENGTH_LONG).show();
                Constants.currentMarket.market_ToPay = 0;
                Constants.currentMarket.market_status = "1";

                Intent i = new Intent(context, ShowMoneyActivity.class);

                ((Activity)context).finish();
            }
        }else
        {
            Constants.viewVipText1 = strMessage;
            Constants.paymentError = strMessage;

            Intent i = new Intent(context, ViewStatusActivity.class);

            context.startActivity(i);
        }
    }

    public static void PayCC(float amount, String CodeError, String wallet, String Tag, String Country){

        Constants.viewVipText1 = getButtonName("lab_processCB", Constants.CurrentLang);
        Constants.CodeError = CodeError;
        String strApi = "{api: 'TransactionAddNewCreditCardPayment',";

        strApi += "Partner_PinCode:'" + Constants.currentMarket.market_code + "',";
        strApi += "Partner_ExternalTag:'" + Tag + "',";
        strApi += "User_PhoneNumber:'" + wallet + "',";
        strApi += "User_PhoneCountry:'" + Country + "',";
        strApi += "Payment_Amount:'" + amount + "',";
        strApi += "Partner_PhoneNumber:'" + Constants._PHONE_NUMBER + "',";
        strApi += "Partner_PhoneCountry:'" + Constants.CurrentLang + "',";
        strApi += "ProcessToken:'" + Constants.ProcessToken + "',";
        strApi += "Application_Token:'" + Constants.Application_Token + "',";
        strApi += "Application_ServerId:'" + Constants.APPLICATION_SERVER_ID + "',";
        strApi += "Application_Version:'" + Build.VERSION.RELEASE + "'}";

        MyDBProcessing processing = new MyDBProcessing();

        processing.SendPost(strApi, PayCCHandler);
    }

    static Handler executeCouponHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {

            String strObj = (String) msg.obj;

            if(msg.arg2 != Constants.NET_ERR)
            {
                try {
                    interpretExecuteCoupon(strObj);
                } catch (XmlPullParserException e) {
                    e.printStackTrace();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }else
            {
                Toast.makeText(context, getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                Intent i = new Intent(context, ShowMoneyActivity.class);

                context.startActivity(i);

                ((Activity)context).finish();
            }
        }
    };

    public static void executeCoupon(String pValue, float pCoupon){
        String strApi = "";

        strApi += "{api: 'TransactionAddNewVoucher',";
        strApi += "Partner_PinCode:'" + Constants.currentMarket.market_code + "',";
        strApi += "User_PhoneNumber:'" + Constants.currentUser.user_phone + "',";
        strApi += "User_PhoneCountry:'" + Constants.currentUser.User_PhoneCountry + "',";
        strApi += "Voucher_Type:'" + pCoupon + "',";
        strApi += "Voucher_Amount:'" + pValue + "',";
        strApi += "Partner_PhoneNumber:'" + Constants._PHONE_NUMBER + "',";
        strApi += "Partner_PhoneCountry:'" + Constants.Current_Country + "',";
        strApi += "ProcessToken:'" + Constants.ProcessToken + "',";
        strApi += "Application_Token:'" + Constants.Application_Token + "',";
        strApi += "Application_ServerId:'" + Constants.APPLICATION_SERVER_ID + "',";
        strApi += "Application_Version:'" + Build.VERSION.RELEASE + "',";

        MyDBProcessing processing = new MyDBProcessing();

        processing.SendPost(strApi, executeCouponHandler);



    }

    public static void interpretExecuteCoupon(String strXml) throws XmlPullParserException, IOException {
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

                    if(!Constants.ErrorNumber.equals("0"))
                    {
                        Toast.makeText(context, getButtonName("lab_http_error", CurrentLang), Toast.LENGTH_LONG).show(); //

                        Intent i = new Intent(context, ShowMoneyActivity.class);

                        ((Activity)context).finish();

                        return;
                    }
                }
                if (strTagName != null && strTagName.equals("ProcessToken")) {

                    Constants.ProcessToken = xpp.getText();
                }
            }

            eventType = xpp.next();
        }


        if(Constants.ErrorNumber.equals("0"))
        {

            Toast.makeText(context, getButtonName("lab_credit_ok", CurrentLang), Toast.LENGTH_LONG).show(); //
            Constants.currentMarket.market_ToPay = 0;
            Constants.currentMarket.market_status = "1";

            Intent i = new Intent(context, ShowMoneyActivity.class);

            ((Activity)context).finish();

        }
    }

    public static void giveCoupon(int pIndex){
        //@author SH  @date 03/02/2013

        switch (pIndex) {
            case 1 :
                executeCoupon(Constants.currentMarket.market_coupon_1, pIndex);
                break;
            case 2 :
                executeCoupon(Constants.currentMarket.market_coupon_2, pIndex);
                break;
        }
    }*/
}
