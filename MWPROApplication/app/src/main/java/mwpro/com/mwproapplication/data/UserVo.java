package mwpro.com.mwproapplication.data;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

public class UserVo {

    public int user_id = 0;
    public String user_phone = "";
    public String user_gender = "";
    public String  user_name = "";
    public String user_lastname = "";
    public String user_email = "";
    public String new_money = "";
    public String new_money_vip = "";
    public String new_money_member = "";
    public int user_type = 0;
    public String dispo = "";
    public String decote = "";
    public String VoucherAmount = "";
    public String market_user_value = "0.00";
    public int market_member_value = 0;
    public int market_sponsored_value = 0;
    public int market_vip_value = 0;
    public String CashBackAmountInCaseOfUse = "";
    public String VoucherInCaseofUse = "";
    public String NewExpectedCashBackClassicUserInCaseOfUse = "";
    public String NewExpectedCashBackVipUserInCaseOfUse = "";
    public String market_VouchersCountToSend = "";
    public String market_VouchersCostToSend = "";
    public String market_NBSponsored = "";
    public boolean market_UserCB = false;

    public float SubsidyAmount = 0;
    public String market_ExtraName = "";
    public String User_PhoneCountry = null;
    public boolean user_VoucherDenied = false;
    public boolean user_CashBackDenied = false;
    public String AmountInCaseOfUse = "";
    public String SubsidyInCaseofUse = "";
    public float AmountUsed = 0;
    public float Amount = 0;
    public String User_WalletId = "";
    public boolean user_voucherPending = false;
    public String user_language = "";
    public ArrayList<Card> card = new ArrayList<Card>();
    public float AmountCashBack = 0;
    public String User_AccessKey = "";
    public String User_AccessKeyType = "0";
    public ArrayList<Card> cards = new ArrayList<Card>();
    public String user_CashBackDeadLineAlert = "False";
    public UserVo()
    {

    }
    public UserVo(String str) throws IOException, XmlPullParserException {
        if(str == null)
            return;
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();

        xpp.setInput( new StringReader( str ) );
        int eventType = xpp.getEventType();
        String strTagName = "";

        while (eventType != XmlPullParser.END_DOCUMENT) {

            if (eventType == XmlPullParser.START_DOCUMENT) {

            } else if (eventType == XmlPullParser.START_TAG) {
                strTagName = xpp.getName();

            } else if (eventType == XmlPullParser.END_TAG) {

            } else if (eventType == XmlPullParser.TEXT) {
                if (strTagName != null && strTagName.equals("User_PhoneNumber")) {
                    user_phone = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("User_AccessKey")) {
                    User_AccessKey = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("User_Gender")) {
                    User_AccessKey = xpp.getText();
                }

                if (strTagName != null && strTagName.equals("User_Gender")) {
                    user_gender = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("User_GenderText")) {
                    user_gender = xpp.getText();
                }

                if (strTagName != null && strTagName.equals("User_FirstName")) {
                    user_name = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("User_LastName")) {
                    user_lastname = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("User_EMail")) {
                    user_email = xpp.getText();
                }

                if (strTagName != null && strTagName.equals("Transaction_NewExpectedCashBackVipUser")) {
                    new_money_vip = xpp.getText();
                }

                if (strTagName != null && strTagName.equals("Transaction_NewExpectedCashBackClassicUser")) {
                    new_money_member = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("Transaction_NewExpectedCashBackVipUserInCaseOfUse")) {
                    NewExpectedCashBackVipUserInCaseOfUse = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("Transaction_NewExpectedCashBackClassicUserInCaseOfUse")) {
                    NewExpectedCashBackClassicUserInCaseOfUse = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("Transaction_VouchersDenied")) {
                    if(xpp.getText().equals("True"))
                        user_VoucherDenied = true;
                }
                if (strTagName != null && strTagName.equals("Transaction_CashBackDenied")) {
                    if(xpp.getText().equals("True"))
                        user_CashBackDenied = true;
                }
                if (strTagName != null && strTagName.equals("User_Type")) {
                    user_type = Integer.parseInt(xpp.getText());
                }
                if (strTagName != null && strTagName.equals("User_PhoneCountry")) {
                    User_PhoneCountry = xpp.getText();
                }

                if (strTagName != null && strTagName.equals("User_Percentage")) {
                    market_user_value = xpp.getText();
                }

                if (strTagName != null && strTagName.equals("Partner_PercentageClassicUser")) {
                    market_member_value = (int)Float.parseFloat(xpp.getText());
                }
                if (strTagName != null && strTagName.equals("Partner_PercentageVipUser")) {
                    market_vip_value = (int)Float.parseFloat(xpp.getText());
                }
                if (strTagName != null && strTagName.equals("Partner_VouchersCountToSend")) {
                    market_VouchersCountToSend = xpp.getText();
                }

                if (strTagName != null && strTagName.equals("Partner_VouchersCostToSend")) {
                    market_VouchersCostToSend = xpp.getText();
                }

                if (strTagName != null && strTagName.equals("User_SponsoredUsersCount")) {
                    market_NBSponsored = xpp.getText();
                }

                if (strTagName != null && strTagName.equals("Cluster_Name")) {
                    market_ExtraName = xpp.getText();
                }

                if (strTagName != null && strTagName.equals("Transaction_CashBackAmount")) {
                    decote = xpp.getText();
                }

                if (strTagName != null && strTagName.equals("Transaction_VouchersAmount")) {
                    VoucherAmount = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("Transaction_SubsidyAmount")) {
                    SubsidyAmount = Float.parseFloat(xpp.getText());
                }

                if (strTagName != null && strTagName.equals("Transaction_AmountInCaseOfUse")) {
                    AmountInCaseOfUse = xpp.getText();
                }

                if (strTagName != null && strTagName.equals("Transaction_CashBackAmountInCaseOfUse")) {
                    CashBackAmountInCaseOfUse = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("Transaction_VouchersAmountInCaseOfUse")) {
                    VoucherInCaseofUse = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("Transaction_SubsidyAmountInCaseOfUse")) {
                    SubsidyInCaseofUse = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("Transaction_AvailableAmount")) {
                    AmountUsed = Float.parseFloat(xpp.getText());
                }

                if (strTagName != null && strTagName.equals("Transaction_Amount")) {
                    Amount = Float.parseFloat(xpp.getText());
                }
                if (strTagName != null && strTagName.equals("User_WalletId")) {
                    User_WalletId = xpp.getText();
                }

                if (strTagName != null && strTagName.equals("Transaction_VouchersAvailabilityPending")) {
                    if(xpp.getText().equals("True"))
                        user_voucherPending = true;
                }

                if (strTagName != null && strTagName.equals("User_Language")) {
                    user_language = xpp.getText();
                }

                if(strTagName != null && strTagName.equals("Transaction_DeadlineAlert"))
                {
                    user_CashBackDeadLineAlert = xpp.getText();
                }
            }
            eventType = xpp.next();
        }
    }

    public UserVo(JSONObject response1) throws  JSONException {
        if(response1 == null)
            return;

        JSONObject response = response1.getJSONObject("Data");

        if(response.has("User_PhoneNumber"))
        {
            user_phone = response.getString("User_PhoneNumber");
        }

        if(response.has("User_AccessKey"))
        {
            User_AccessKey = response.getString("User_AccessKey");
        }
        if(response.has("User_Gender"))
        {
            user_gender = response.getString("User_Gender");
        }
        if(response.has("User_GenderText"))
        {
            user_gender = response.getString("User_GenderText");
        }
        if(response.has("User_FirstName"))
        {
            user_name = response.getString("User_FirstName");
        }
        if(response.has("User_LastName"))
        {
            user_lastname = response.getString("User_LastName");
        }
        if(response.has("User_EMail"))
        {
            user_email = response.getString("User_EMail");
        }
        if(response.has("Transaction_NewExpectedCashBackVipUser"))
        {
            new_money_vip = response.getString("Transaction_NewExpectedCashBackVipUser");
        }
        if(response.has("Transaction_NewExpectedCashBackClassicUser"))
        {
            new_money_member = response.getString("Transaction_NewExpectedCashBackClassicUser");
        }
        if(response.has("Transaction_NewExpectedCashBackVipUserInCaseOfUse"))
        {
            NewExpectedCashBackVipUserInCaseOfUse = response.getString("Transaction_NewExpectedCashBackVipUserInCaseOfUse");
        }
        if(response.has("Transaction_NewExpectedCashBackClassicUserInCaseOfUse"))
        {
            NewExpectedCashBackClassicUserInCaseOfUse = response.getString("Transaction_NewExpectedCashBackClassicUserInCaseOfUse");
        }

        if(response.has("Transaction_VouchersDenied"))
        {
            if(response.getString("Transaction_VouchersDenied").toLowerCase().equals("true"))
                user_VoucherDenied = true;
        }

        if(response.has("Transaction_CashBackDenied"))
        {
            if(response.getString("Transaction_CashBackDenied").toLowerCase().equals("true"))
                user_CashBackDenied = true;
        }
        if(response.has("User_Type"))
        {
            user_type = Integer.parseInt(response.getString("User_Type"));
        }

        if(response.has("User_PhoneCountry"))
        {
            User_PhoneCountry = response.getString("User_PhoneCountry");
        }

        if(response.has("User_Percentage"))
        {
            market_user_value = response.getString("User_Percentage");
        }

        if(response.has("Partner_PercentageClassicUser"))
        {
            market_member_value = (int)Float.parseFloat(response.getString("Partner_PercentageClassicUser"));
        }
        if(response.has("Partner_PercentageVipUser"))
        {
            market_vip_value = (int)Float.parseFloat(response.getString("Partner_PercentageVipUser"));
        }

        if(response.has("Partner_VouchersCountToSend"))
        {
            market_VouchersCountToSend = response.getString("Partner_VouchersCountToSend");
        }
        if(response.has("Partner_VouchersCostToSend"))
        {
            market_VouchersCostToSend = response.getString("Partner_VouchersCostToSend");
        }
        if(response.has("User_SponsoredUsersCount"))
        {
            market_NBSponsored = response.getString("User_SponsoredUsersCount");
        }
        if(response.has("Cluster_Name"))
        {
            market_ExtraName = response.getString("Cluster_Name");
        }

        if(response.has("Transaction_CashBackAmount"))
        {
            decote = response.getString("Transaction_CashBackAmount");
        }
        if(response.has("Transaction_VouchersAmount"))
        {
            VoucherAmount = response.getString("Transaction_VouchersAmount");
        }
        if(response.has("Transaction_SubsidyAmount"))
        {
            SubsidyAmount = Float.parseFloat(response.getString("Transaction_SubsidyAmount"));
        }
        if(response.has("Transaction_AmountInCaseOfUse"))
        {
            AmountInCaseOfUse = response.getString("Transaction_AmountInCaseOfUse");
        }

        if(response.has("Transaction_CashBackAmountInCaseOfUse"))
        {
            CashBackAmountInCaseOfUse = response.getString("Transaction_CashBackAmountInCaseOfUse");
        }
        if(response.has("Transaction_VouchersAmountInCaseOfUse"))
        {
            VoucherInCaseofUse = response.getString("Transaction_VouchersAmountInCaseOfUse");
        }
        if(response.has("Transaction_SubsidyAmountInCaseOfUse"))
        {
            SubsidyInCaseofUse = response.getString("Transaction_SubsidyAmountInCaseOfUse");
        }
        if(response.has("Transaction_AvailableAmount"))
        {
            AmountUsed = Float.parseFloat(response.getString("Transaction_AvailableAmount"));
        }
        if(response.has("Transaction_Amount"))
        {
            Amount = Float.parseFloat(response.getString("Transaction_Amount"));
        }
        if(response.has("User_WalletId"))
        {
            User_WalletId = response.getString("User_WalletId");
        }

        if(response.has("Transaction_VouchersAvailabilityPending"))
        {
            user_voucherPending = response.getString("Transaction_VouchersAvailabilityPending").toLowerCase().equals("true");
        }
        if(response.has("User_Language"))
        {
            user_language = response.getString("User_Language");
        }

        if(response.has("Transaction_DeadlineAlert"))
        {
            user_CashBackDeadLineAlert = response.getString("Transaction_DeadlineAlert");
        }
        /*XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();

        xpp.setInput( new StringReader( str ) );
        int eventType = xpp.getEventType();
        String strTagName = "";

        while (eventType != XmlPullParser.END_DOCUMENT) {

            if (eventType == XmlPullParser.START_DOCUMENT) {

            } else if (eventType == XmlPullParser.START_TAG) {
                strTagName = xpp.getName();

            } else if (eventType == XmlPullParser.END_TAG) {

            } else if (eventType == XmlPullParser.TEXT) {
                if (strTagName != null && strTagName.equals("User_PhoneNumber")) {
                    user_phone = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("User_AccessKey")) {
                    User_AccessKey = xpp.getText();
                }


                if (strTagName != null && strTagName.equals("User_Gender")) {
                    user_gender = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("User_GenderText")) {
                    user_gender = xpp.getText();
                }

                if (strTagName != null && strTagName.equals("User_FirstName")) {
                    user_name = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("User_LastName")) {
                    user_lastname = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("User_EMail")) {
                    user_email = xpp.getText();
                }

                if (strTagName != null && strTagName.equals("Transaction_NewExpectedCashBackVipUser")) {
                    new_money_vip = xpp.getText();
                }

                if (strTagName != null && strTagName.equals("Transaction_NewExpectedCashBackClassicUser")) {
                    new_money_member = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("Transaction_NewExpectedCashBackVipUserInCaseOfUse")) {
                    NewExpectedCashBackVipUserInCaseOfUse = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("Transaction_NewExpectedCashBackClassicUserInCaseOfUse")) {
                    NewExpectedCashBackClassicUserInCaseOfUse = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("Transaction_VouchersDenied")) {
                    if(xpp.getText().equals("True"))
                        user_VoucherDenied = true;
                }
                if (strTagName != null && strTagName.equals("Transaction_CashBackDenied")) {
                    if(xpp.getText().equals("True"))
                        user_CashBackDenied = true;
                }
                if (strTagName != null && strTagName.equals("User_Type")) {
                    user_type = Integer.parseInt(xpp.getText());
                }
                if (strTagName != null && strTagName.equals("User_PhoneCountry")) {
                    User_PhoneCountry = xpp.getText();
                }

                if (strTagName != null && strTagName.equals("User_Percentage")) {
                    market_user_value = xpp.getText();
                }

                if (strTagName != null && strTagName.equals("Partner_PercentageClassicUser")) {
                    market_member_value = (int)Float.parseFloat(xpp.getText());
                }
                if (strTagName != null && strTagName.equals("Partner_PercentageVipUser")) {
                    market_vip_value = (int)Float.parseFloat(xpp.getText());
                }
                if (strTagName != null && strTagName.equals("Partner_VouchersCountToSend")) {
                    market_VouchersCountToSend = xpp.getText();
                }

                if (strTagName != null && strTagName.equals("Partner_VouchersCostToSend")) {
                    market_VouchersCostToSend = xpp.getText();
                }

                if (strTagName != null && strTagName.equals("User_SponsoredUsersCount")) {
                    market_NBSponsored = xpp.getText();
                }

                if (strTagName != null && strTagName.equals("Cluster_Name")) {
                    market_ExtraName = xpp.getText();
                }

                if (strTagName != null && strTagName.equals("Transaction_CashBackAmount")) {
                    decote = xpp.getText();
                }

                if (strTagName != null && strTagName.equals("Transaction_VouchersAmount")) {
                    VoucherAmount = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("Transaction_SubsidyAmount")) {
                    SubsidyAmount = Float.parseFloat(xpp.getText());
                }

                if (strTagName != null && strTagName.equals("Transaction_AmountInCaseOfUse")) {
                    AmountInCaseOfUse = xpp.getText();
                }

                if (strTagName != null && strTagName.equals("Transaction_CashBackAmountInCaseOfUse")) {
                    CashBackAmountInCaseOfUse = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("Transaction_VouchersAmountInCaseOfUse")) {
                    VoucherInCaseofUse = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("Transaction_SubsidyAmountInCaseOfUse")) {
                    SubsidyInCaseofUse = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("Transaction_AvailableAmount")) {
                    AmountUsed = Float.parseFloat(xpp.getText());
                }

                if (strTagName != null && strTagName.equals("Transaction_Amount")) {
                    Amount = Float.parseFloat(xpp.getText());
                }
                if (strTagName != null && strTagName.equals("User_WalletId")) {
                    User_WalletId = xpp.getText();
                }

                if (strTagName != null && strTagName.equals("Transaction_VouchersAvailabilityPending")) {
                    if(xpp.getText().equals("True"))
                        user_voucherPending = true;
                }

                if (strTagName != null && strTagName.equals("User_Language")) {
                    user_language = xpp.getText();
                }
            }
            eventType = xpp.next();
        }*/
    }
}