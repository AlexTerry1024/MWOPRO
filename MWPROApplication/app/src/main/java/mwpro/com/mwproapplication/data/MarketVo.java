package mwpro.com.mwproapplication.data;

import android.content.SharedPreferences;

import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParser;
import org.xmlpull.v1.XmlPullParserException;
import org.xmlpull.v1.XmlPullParserFactory;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;

import mwpro.com.mwproapplication.Constants;

/**
 * Created by JavaWorld on 8/19/2017.
 */

public class MarketVo
{
    public String market_name = "";
    public String market_code = "";
    public String market_ExternalTag = "";
    public String market_WalletId = "";
    public String market_Version = "4.0.0";
    public String market_coupon_1 = "0.00";
    public String market_coupon_2 = "0.00";
    public String market_coupon_Sponsor = "0.00";
    public String market_coupon_Sponsored = "0.00";
    public String market_delayVoucher = "0.00";;
    public String market_validityVoucher = "0.00";;
    public String market_voucherfee = "0.00";;
    public String market_email = "";
    public String market_siret = "";
    public String market_pays = "fr_FR";
    public String market_country = "";
    public boolean market_vip = true;
    public int market_member_value = 0;
    public int market_sponsored_value = 0;
    public float market_vip_value = 0;
    public boolean market_admin = false;
    public Boolean market_allowCoupon = false;
    public String market_MailingVouchersCountToSend = "";
    public String market_MailingVouchersCostToSend = "";
    public String CouponLastTime = "";
    public Boolean market_cb = false;
    public String market_Insolvent = "";
    public float market_ToPay = 0;
    public String market_Cluster = "";
    public String market_demotrial = "";
    public String market_status = "";
    public String market_expdemo = "";
    public boolean market_taxi = false;
    public boolean market_subsidy = false;
    public int market_activity = 0;
    public int Partner_WalletId = 0;
    public String market_BalanceColor = "";
    public String market_Id = "";// si vide Partner patché
    public boolean Partner_AllowManagement = false;
    public boolean Partner_AllowChangePercentage = false;
    public boolean Partner_AllowChangeUserType = false;
    public boolean Partner_AllowUserVoucher1 = false;
    public boolean Partner_AllowUserVoucher2 = false;
    public boolean Partner_AllowMailingDiffusion = false;
    public String Partner_PhoneNumber = "";
    public ArrayList<Card> card = new ArrayList<Card>();
    public String Partner_VoucherValue1 = "";
    public String Partner_VoucherValue2 = "";
    public String market_signature = "";
    public String market_availableDelayCashBack = "";
    public String market_functionalWallet = "";
    public String market_percentageAlternateUser = "";
    public String market_validityCashBack = "";
    public String market_additionalCashBack = "";
    public boolean Partner_AllowVoucherDiffusion = false;
    public String Currency_Code = "";
    public String Currency_Symbol = "";
    public String Activity_Name = "";
    public String Partner_AllowUseVoucher1 = "";
    public String Partner_AllowUseVoucher2 = "";
    public MarketVo(String xml) throws XmlPullParserException, IOException {
        XmlPullParserFactory factory = XmlPullParserFactory.newInstance();
        factory.setNamespaceAware(true);
        XmlPullParser xpp = factory.newPullParser();

        xpp.setInput( new StringReader( xml ) );
        int eventType = xpp.getEventType();
        String strTagName = "";
        while (eventType != XmlPullParser.END_DOCUMENT) {

            if (eventType == XmlPullParser.START_DOCUMENT) {

            } else if (eventType == XmlPullParser.START_TAG) {
                strTagName = xpp.getName();

            } else if (eventType == XmlPullParser.END_TAG) {

            } else if (eventType == XmlPullParser.TEXT) {
                if (strTagName != null && strTagName.equals("Partner_CompanyName")) {
                    market_name  = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("Partner_EMail")) {
                    market_email = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("Partner_Status")) {
                    market_status = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("Partner_FinancialAmount")) {

                    String str = xpp.getText();
                    if (xpp.getText().length() != 0)
                        market_ToPay = Float.parseFloat(xpp.getText());
                }
                if (strTagName != null && strTagName.equals("Partner_BalanceColor")) {
                    market_BalanceColor = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("Partner_Insolvent")) {
                    market_Insolvent = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("Partner_TrialDurationRemaining")) {
                    market_demotrial = xpp.getText();
                }

                if (strTagName != null && strTagName.equals("Partner_FreeDeadline")) {
                    market_expdemo = xpp.getText();
                }

                if (strTagName != null && strTagName.equals("Partner_Language")) {
                    market_pays =  xpp.getText();
                }

                if (strTagName != null && strTagName.equals("Partner_WalletId")) {
                    market_WalletId = xpp.getText();
                }

                if (strTagName != null && strTagName.equals("Partner_Signature")) {
                    market_signature = xpp.getText();
                }

                if (strTagName != null && strTagName.equals("Partner_FunctionalWallet")) {
                    market_functionalWallet = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("Partner_PercentageVipUser")) {
                    String str = xpp.getText();
                    if(xpp.getText().length() != 0)
                        market_vip_value = Float.parseFloat(str);
                }

                if (strTagName != null && strTagName.equals("Partner_PercentageAlternateUser")) {
                    market_percentageAlternateUser = xpp.getText();
                }

                if (strTagName != null && strTagName.equals("Partner_PercentageClassicUser")) {

                    if(xpp.getText().length() != 0)
                        market_member_value =  (int)(Float.parseFloat(xpp.getText()));
                }
                if (strTagName != null && strTagName.equals("Partner_VoucherFee")) {
                    market_voucherfee = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("Partner_SiretNumber")) {
                    market_siret = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("Partner_VoucherValue1")) {
                    Partner_VoucherValue1 = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("Partner_VoucherValue2")) {
                    Partner_VoucherValue2 = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("Partner_VoucherValueSponsor")) {
                    market_coupon_Sponsor = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("Partner_VoucherValueSponsored")) {
                    market_coupon_Sponsored = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("Partner_AvailableDelayVoucher")) {
                    market_delayVoucher = xpp.getText();
                }

                if (strTagName != null && strTagName.equals("Partner_AvailableDelayCashBack")) {
                    market_availableDelayCashBack = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("Partner_ValidityVoucher")) {
                    market_validityVoucher = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("Partner_ValidityCashBack")) {
                    market_validityCashBack = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("Partner_AdditionalCashBack")) {
                    market_additionalCashBack = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("Partner_AllowChangeUserType")) {
                    Partner_AllowChangeUserType = Boolean.parseBoolean(xpp.getText());
                }
                if (strTagName != null && strTagName.equals("Partner_AllowChangePercentage")) {
                    Partner_AllowChangePercentage = Boolean.parseBoolean(xpp.getText());
                }
                if (strTagName != null && strTagName.equals("Partner_AllowUseVoucher1")) {
                    Partner_AllowUseVoucher1 = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("Partner_AllowUseVoucher2")) {
                    Partner_AllowUseVoucher2 = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("'Partner_Country'")) {
                    market_country = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("Partner_AllowManagement")) {
                    Partner_AllowManagement = Boolean.parseBoolean(xpp.getText());
                }
                if (strTagName != null && strTagName.equals("Partner_AllowVoucherDiffusion")) {
                    Partner_AllowVoucherDiffusion = Boolean.parseBoolean(xpp.getText());

                    if(Partner_AllowVoucherDiffusion == true)
                    {
                        Partner_AllowMailingDiffusion = true;
                    }
                }
                if (strTagName != null && strTagName.equals("Partner_ExternalTag")) {
                    market_ExternalTag = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("Partner_WordPressId")) {
                    market_Id = xpp.getText();
                }
                if (strTagName != null && strTagName.equals("Currency_Code")) {
                    Currency_Code = xpp.getText();
                }

                if (strTagName != null && strTagName.equals("Currency_Symbol")) {
                    Currency_Symbol = xpp.getText();
                }

                if (strTagName != null && strTagName.equals("Cluster_Type")) {
                    market_Cluster = xpp.getText();
                }

                if (strTagName != null && strTagName.equals("Activity_Id")) {
                    String str = xpp.getText();
                    if(xpp.getText().length() != 0)
                        market_activity = Integer.parseInt(xpp.getText());
                }

                if (strTagName != null && strTagName.equals("Activity_Name")) {
                    Activity_Name = xpp.getText();
                }

                if (strTagName != null && strTagName.equals("Version")) {
                    market_Version = xpp.getText();
                }
            }
            eventType = xpp.next();
        }

        if(Partner_AllowUseVoucher1.equals("True"))
        {
            market_coupon_1 = Partner_VoucherValue1;
        }

        if(Partner_AllowUseVoucher2.equals("True"))
        {
            market_coupon_2 = Partner_VoucherValue2;
        }



    }

    public MarketVo(JSONObject response1) throws  JSONException {

        JSONObject response = response1.getJSONObject("Data");
        if(response.has("Partner_CompanyName"))
        {
            market_name = response.getString("Partner_CompanyName");
        }

        if(response.has("Partner_EMail"))
        {
            market_email = response.getString("Partner_EMail");
        }

        if(response.has("Partner_Status"))
        {
            market_status = response.getString("Partner_Status");
        }
        if(response.has("Partner_FinancialAmount"))
        {
            market_ToPay = Float.parseFloat(response.getString("Partner_FinancialAmount"));
        }
        if(response.has("Partner_BalanceColor"))
        {
            market_BalanceColor = response.getString("Partner_BalanceColor");
        }

        if(response.has("Partner_Insolvent"))
        {
            market_Insolvent = response.getString("Partner_Insolvent");
        }

        if(response.has("Partner_TrialDurationRemaining"))
        {
            market_demotrial = response.getString("Partner_TrialDurationRemaining");
        }

        if(response.has("Partner_FreeDeadline"))
        {
            market_expdemo = response.getString("Partner_FreeDeadline");
        }
        if(response.has("Partner_Language"))
        {
            market_pays = response.getString("Partner_Language");
        }
        if(response.has("Partner_WalletId"))
        {
            market_WalletId = response.getString("Partner_WalletId");
        }
        if(response.has("Partner_Signature"))
        {
            market_signature = response.getString("Partner_Signature");
        }
        if(response.has("Partner_FunctionalWallet"))
        {
            market_functionalWallet = response.getString("Partner_FunctionalWallet");
        }

        if(response.has("Partner_PercentageVipUser"))
        {
            market_vip_value = Float.parseFloat(response.getString("Partner_PercentageVipUser"));
        }
        if(response.has("Partner_PercentageAlternateUser"))
        {
            market_percentageAlternateUser = response.getString("Partner_PercentageAlternateUser");
        }
        if(response.has("Partner_PercentageClassicUser"))
        {
            market_member_value = (int) Float.parseFloat(response.getString("Partner_PercentageClassicUser"));
        }
        if(response.has("Partner_VoucherFee"))
        {
            market_voucherfee = response.getString("Partner_VoucherFee");
        }
        if(response.has("Partner_SiretNumber"))
        {
            market_siret = response.getString("Partner_SiretNumber");
        }
        if(response.has("Partner_VoucherValue1"))
        {
            Partner_VoucherValue1 = response.getString("Partner_VoucherValue1");
        }
        if(response.has("Partner_VoucherValue2"))
        {
            Partner_VoucherValue2 = response.getString("Partner_VoucherValue2");
        }

        if(response.has("Partner_VoucherValueSponsor"))
        {
            market_coupon_Sponsor = response.getString("Partner_VoucherValueSponsor");
        }
        if(response.has("Partner_VoucherValueSponsored"))
        {
            market_coupon_Sponsored = response.getString("Partner_VoucherValueSponsored");
        }
        if(response.has("Partner_AvailableDelayVoucher"))
        {
            market_delayVoucher = response.getString("Partner_AvailableDelayVoucher");
        }
        if(response.has("Partner_AvailableDelayCashBack"))
        {
            market_availableDelayCashBack = response.getString("Partner_AvailableDelayCashBack");
        }
        if(response.has("Partner_ValidityVoucher"))
        {
            market_validityVoucher = response.getString("Partner_ValidityVoucher");
        }
        if(response.has("Partner_ValidityCashBack"))
        {
            market_validityCashBack = response.getString("Partner_ValidityCashBack");
        }

        if(response.has("Partner_AdditionalCashBack"))
        {
            market_additionalCashBack = response.getString("Partner_AdditionalCashBack");
        }
        if(response.has("Partner_AllowChangeUserType"))
        {
            Partner_AllowChangeUserType = Boolean.parseBoolean(response.getString("Partner_AllowChangeUserType"));
        }
        if(response.has("Partner_AllowChangePercentage"))
        {
            Partner_AllowChangePercentage = Boolean.parseBoolean(response.getString("Partner_AllowChangePercentage"));
        }
        if(response.has("Partner_AllowUseVoucher1"))
        {
            Partner_AllowUseVoucher1 = response.getString("Partner_AllowUseVoucher1");
        }
        if(response.has("Partner_AllowUseVoucher2"))
        {
            Partner_AllowUseVoucher2 = response.getString("Partner_AllowUseVoucher2");
        }
        if(response.has("Partner_Country"))
        {
            market_country = response.getString("Partner_Country");
        }
        if(response.has("Partner_AllowManagement"))
        {
            Partner_AllowManagement = Boolean.parseBoolean(response.getString("Partner_AllowManagement"));
        }
        if(response.has("Partner_AllowVoucherDiffusion"))
        {
            Partner_AllowVoucherDiffusion = Boolean.parseBoolean(response.getString("Partner_AllowVoucherDiffusion"));

            if(Partner_AllowVoucherDiffusion == true)
                Partner_AllowMailingDiffusion = true;
        }
        if(response.has("Partner_ExternalTag"))
        {
            market_ExternalTag = response.getString("Partner_ExternalTag");
        }
        if(response.has("Partner_WordPressId"))
        {
            market_Id = response.getString("Partner_WordPressId");
        }
        if(response.has("Currency_Code"))
        {
            Currency_Code = response.getString("Currency_Code");
        }
        if(response.has("Currency_Symbol"))
        {
            Currency_Symbol = response.getString("Currency_Symbol");
        }
        if(response.has("Cluster_Type"))
        {
            market_Cluster = response.getString("Cluster_Type");
        }
        if(response.has("Activity_Id"))
        {
            market_activity = Integer.parseInt(response.getString("Activity_Id"));
        }

        if(response.has("Activity_Name"))
        {
            Activity_Name = response.getString("Activity_Name");
        }

        if(response.has("Version"))
        {
            market_Version = response.getString("Version");
        }


        if(Partner_AllowUseVoucher1.toLowerCase().equals("true"))
        {
            market_coupon_1 = Partner_VoucherValue1;
        }

        if(Partner_AllowUseVoucher2.toLowerCase().equals("true"))
        {
            market_coupon_2 = Partner_VoucherValue2;
        }
    }
}

