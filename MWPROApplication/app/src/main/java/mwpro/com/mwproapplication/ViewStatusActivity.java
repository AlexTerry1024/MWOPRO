package mwpro.com.mwproapplication;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.MotionEvent;
import android.view.View;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RadioButton;
import android.widget.TextView;
import android.widget.Toast;
import com.kyleduo.switchbutton.SwitchButton;
import com.pepperonas.materialdialog.MaterialDialog;
import android.widget.Spinner;

import org.adw.library.widgets.discreteseekbar.DiscreteSeekBar;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;
import java.io.IOException;
import java.util.Calendar;
import java.util.Date;

import mwpro.com.mwproapplication.data.Card;
import mwpro.com.mwproapplication.data.MyTitles;
import mwpro.com.mwproapplication.ui.CustomOnOffAdapter;
import mwpro.com.mwproapplication.ui.CustomProgressDialog;

import static mwpro.com.mwproapplication.Constants.GETJSONOBJECT;
import static mwpro.com.mwproapplication.Constants.GetMatchString;
import static mwpro.com.mwproapplication.R.id.idGroupBottom;

public class ViewStatusActivity extends Activity implements View.OnClickListener{

    ImageView m_ctrlBanner = null;
    TextView m_ctrlTitle = null;
    TextView m_ctrlSecondtitle = null;
    ImageView m_ctrlStar = null;
    TextView m_ctrlLabelLeft = null;
    TextView m_ctrlLabelRight = null;
    RadioButton m_ctrlOption_Left = null;
    RadioButton m_ctrlOption_Right = null;
    TextView m_ctrlPro = null;
    DiscreteSeekBar m_ctrlVip_Value = null;
    EditText m_ctrlIDMoment = null;
    TextView m_ctrlIDMoment_Help = null;
    EditText m_ctrlIDNew_Money = null;
    TextView m_ctrlIDNew_Money_Help = null;
    EditText m_ctrlIDDispo = null;
    TextView m_ctrlIDDispo_Help = null;
    EditText m_ctrlIDCoupon = null;
    TextView m_ctrlIDCoupon_Help = null;
    EditText m_ctrlIDExtra = null;
    TextView m_ctrlIDExtra_Help = null;
    TextView m_ctrlIDLabelBottom = null;
    CheckBox m_ctrlCheck = null;

    CheckBox m_ctrlCheck_1 = null;

    Button m_ctrlCancel = null;
    Button m_ctrlApply = null;
    LinearLayout m_ctrlGroupMember = null;
    boolean _showName = true;

    String decote = "";
    String dispo = Constants.currentUser.dispo;
    float montant = Constants.currentMontant;
    String NBVoucherToSend = "";
    float CBlimit = 1;
    String VoucherCostToSend = "";
    String NextCashBackVipUser = "";
    String NextCashBackClassicUser = "";
    String newMoney = "";
    String idMontantCB = "0.0";
    String newDispo = "";
    Button m_ctrlIDPay = null;
    String[] arrSwitchNames = {"Off", "On"};
    TextView m_ctrlCheckHelp = null;
    TextView m_ctrlCheck_1_Help = null;
    CustomOnOffAdapter check, check1 = null;
    CustomProgressDialog progressDialog = null;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_status);

        m_ctrlBanner = (ImageView)findViewById(R.id.banner_button); //R.id.banner
        m_ctrlTitle = (TextView)findViewById(R.id.title);
        m_ctrlSecondtitle = (TextView)findViewById(R.id.secondtitle);
        m_ctrlStar = (ImageView)findViewById(R.id.star);
        m_ctrlLabelLeft = (TextView)findViewById(R.id.label_left);
        m_ctrlLabelRight = (TextView)findViewById(R.id.label_right);
        m_ctrlOption_Left = (RadioButton)findViewById(R.id.option_left);
        m_ctrlOption_Right = (RadioButton)findViewById(R.id.option_right);
        m_ctrlPro = (TextView)findViewById(R.id.pro);
        m_ctrlVip_Value = (DiscreteSeekBar)findViewById(R.id.vip_value);
        m_ctrlIDMoment = (EditText)findViewById(R.id.idMoment);
        m_ctrlIDMoment_Help = (TextView)findViewById(R.id.idMoment_help);
        m_ctrlIDNew_Money = (EditText)findViewById(R.id.idnew_money);
        m_ctrlIDNew_Money_Help = (TextView)findViewById(R.id.idnew_money_help);
        m_ctrlIDDispo = (EditText)findViewById(R.id.idDispo);
        m_ctrlIDDispo_Help = (TextView)findViewById(R.id.idDispo_help);
        m_ctrlIDCoupon = (EditText)findViewById(R.id.idCoupon);
        m_ctrlIDCoupon_Help = (TextView)findViewById(R.id.idCoupon_help);
        m_ctrlIDExtra = (EditText)findViewById(R.id.idExtra);
        m_ctrlIDExtra_Help = (TextView)findViewById(R.id.idExtra_help);
        m_ctrlIDLabelBottom = (TextView)findViewById(R.id.idLabelBottom);
        m_ctrlCheck = (CheckBox) findViewById(R.id.check);

        m_ctrlCheck_1 = (CheckBox)findViewById(R.id.check_1);
        m_ctrlCancel = (Button)findViewById(R.id.cancel);
        m_ctrlApply = (Button)findViewById(R.id.apply);
        m_ctrlGroupMember = (LinearLayout)findViewById(R.id.group_member);
        m_ctrlIDPay = (Button)findViewById(R.id.idpay);
        m_ctrlCheckHelp = (TextView)findViewById(R.id.check_help);
        m_ctrlCheck_1_Help = (TextView)findViewById(R.id.check_1_help);


        check = new CustomOnOffAdapter(ViewStatusActivity.this, arrSwitchNames);
        check1 = new CustomOnOffAdapter(ViewStatusActivity.this, arrSwitchNames);



        NextCashBackVipUser = Constants.currentUser.NewExpectedCashBackVipUserInCaseOfUse;
        NextCashBackClassicUser = Constants.currentUser.NewExpectedCashBackClassicUserInCaseOfUse;

        if(Constants.currentMarket.Partner_AllowChangeUserType == true)
        {
            m_ctrlGroupMember.setVisibility(View.VISIBLE);
            m_ctrlGroupMember.setEnabled(true);
        }else
        {
            m_ctrlGroupMember.setVisibility(View.GONE);
            m_ctrlGroupMember.setEnabled(false);
        }

        m_ctrlCancel.setText(getButtonName("btn_annuler", Constants.CurrentLang));
        m_ctrlApply.setText(getButtonName("btn_valider", Constants.CurrentLang));
        m_ctrlCheckHelp.setText(getButtonName("lab_deduire_dispo", Constants.CurrentLang));


        m_ctrlApply.setOnClickListener(this);
        m_ctrlCancel.setOnClickListener(this);
        m_ctrlIDPay.setOnClickListener(this);
        m_ctrlBanner.setOnClickListener(this);

        m_ctrlIDCoupon.setText(formatNumber(Constants.currentUser.VoucherAmount, 2, true, false));
        m_ctrlIDExtra.setText(formatNumber("" + Constants.currentUser.SubsidyAmount, 2, true, false));

        m_ctrlIDExtra_Help.setText(Constants.currentUser.market_ExtraName);
        m_ctrlIDDispo.setText(formatNumber(Constants.currentUser.decote, 2, true, false));
        m_ctrlIDDispo_Help.setText(getButtonName("lab_dispo", Constants.CurrentLang));
        m_ctrlIDNew_Money_Help.setText(getButtonName("lab_credit_dispo", Constants.CurrentLang));
        m_ctrlIDMoment_Help.setText(getButtonName("lab_a_payer", Constants.CurrentLang));
        m_ctrlCheck_1_Help.setText(getButtonName("lab_vip", Constants.CurrentLang));

        m_ctrlIDCoupon_Help.setText(getButtonName("lb_coupon", Constants.CurrentLang));
        m_ctrlLabelLeft.setText(getButtonName("lab_membre", Constants.CurrentLang));
        m_ctrlLabelRight.setText(getButtonName("lab_vip", Constants.CurrentLang));
        m_ctrlIDMoment.setText(formatNumber("" + Constants.currentMontant, 2, true, false));
        progressDialog = new CustomProgressDialog(ViewStatusActivity.this, "");
        m_ctrlTitle.setText(Constants.viewVipText1);
        m_ctrlPro.setText("5%");
        m_ctrlIDDispo.setEnabled(false);
        m_ctrlStar.setImageResource(R.drawable.onestars);

        if (Float.parseFloat(Constants.currentUser.market_NBSponsored) > 10 && Float.parseFloat(Constants.currentUser.market_NBSponsored) <= 50)
            m_ctrlStar.setImageResource(R.drawable.twostars);
        else if (Float.parseFloat(Constants.currentUser.market_NBSponsored) > 50)
            m_ctrlStar.setImageResource(R.drawable.threestars);

        if (Constants.currentUser.user_name.equals("")&& Constants.currentUser.user_lastname.equals(""))
            m_ctrlSecondtitle.setVisibility(View.INVISIBLE);
        else
            m_ctrlSecondtitle.setText(Constants.currentUser.user_gender + " " + Constants.currentUser.user_name + " " + Constants.currentUser.user_lastname);

        m_ctrlIDPay.setText(getButtonName("lab_addCB", Constants.CurrentLang));


        m_ctrlVip_Value.setEnabled(false);
        m_ctrlVip_Value.setVisibility(View.GONE);
        m_ctrlOption_Left.setEnabled(false);
        m_ctrlOption_Right.setEnabled(false);

        Constants.currentMontant = Constants.currentUser.Amount;

        if(Constants.currentMarket.market_activity == 2606)
        {
            Constants.currentMarket.Partner_AllowChangeUserType = false;

            m_ctrlOption_Right.setChecked(true);

            m_ctrlGroupMember.setEnabled(false);
        }

        if(Constants.currentMarket.Partner_AllowChangePercentage == true)
        {
            m_ctrlVip_Value.setEnabled(true);
            m_ctrlVip_Value.setVisibility(View.VISIBLE);

            m_ctrlGroupMember.setEnabled(true);
        }

        Constants.currentUser.decote = formatNumber(Constants.currentUser.decote, 2, true, false);

        if(Constants.currentMarket.market_subsidy == true)
        {
            findViewById(R.id.group_member).setVisibility(View.INVISIBLE);
        }

        if(Float.parseFloat(Constants.currentUser.VoucherAmount) == 0)
        {
            //findViewById(R.id.idGroupBottom).setVisibility(View.GONE);

            m_ctrlIDCoupon_Help.setVisibility(View.GONE);
            m_ctrlIDCoupon.setVisibility(View.GONE);
            m_ctrlIDExtra_Help.setVisibility(View.GONE);
            m_ctrlIDExtra.setVisibility(View.GONE);
        }

        if(Constants.currentUser.user_VoucherDenied == true)
        {
            if(Float.parseFloat(Constants.currentUser.VoucherAmount) > 0)
            {
                m_ctrlIDCoupon_Help.setVisibility(View.VISIBLE);
                m_ctrlIDCoupon.setVisibility(View.VISIBLE);
                m_ctrlIDCoupon.setEnabled(false);
            }

            m_ctrlIDLabelBottom.setText(getButtonName("lab_CouponNotValid", Constants.CurrentLang));
            m_ctrlIDLabelBottom.setVisibility(View.VISIBLE);
        }

        if (Constants.currentUser.user_voucherPending == true) {
            m_ctrlIDCoupon_Help.setVisibility(View.VISIBLE);
            m_ctrlIDCoupon.setVisibility(View.VISIBLE);
            m_ctrlIDCoupon.setEnabled(true);
            m_ctrlIDCoupon_Help.setText(getButtonName("lab_CouponComing", Constants.CurrentLang));

        }

        if (Constants.currentUser.user_CashBackDenied == true) {
            m_ctrlIDLabelBottom.setText(getButtonName("lab_CashBackNotValid", Constants.CurrentLang));
            m_ctrlIDLabelBottom.setVisibility(View.VISIBLE);

        }

        if (Constants.currentUser.SubsidyAmount == 0) {
            m_ctrlIDExtra_Help.setVisibility(View.GONE);
            m_ctrlIDExtra.setVisibility(View.GONE);
            m_ctrlIDExtra.setEnabled(false);
            m_ctrlIDExtra_Help.setEnabled(false);
        }

        if (Float.parseFloat(Constants.currentUser.decote) == 0 && Float.parseFloat(Constants.currentUser.VoucherAmount) == 0) {
            m_ctrlCheck.setEnabled(false);
        }

        Constants.IdPayClick = false;

        if(Constants.currentMarket.Partner_AllowChangeUserType)
        {
            m_ctrlOption_Right.setEnabled(true);
        }

        if(!Constants.paymentError.equals(""))
        {
            Toast.makeText(ViewStatusActivity.this, Constants.paymentError, Toast.LENGTH_LONG).show();
        }

        if(Constants.currentUser.user_name.equals("") && Constants.currentUser.user_lastname.equals(""))
        {
            m_ctrlSecondtitle.setVisibility(View.GONE);
        }

        m_ctrlIDNew_Money.setText(formatNumber("0", 2, true, false));

        if(Constants.currentUser.user_type == 1)
        {
            m_ctrlOption_Right.setChecked(true);
            m_ctrlOption_Left.setChecked(false);

            if(Constants.currentMarket.Partner_AllowChangeUserType == true)
                m_ctrlVip_Value.setVisibility(View.VISIBLE);

            m_ctrlVip_Value.setMin((int)Constants.currentMarket.market_vip_value);
            m_ctrlVip_Value.setMax(100);
            m_ctrlVip_Value.setProgress((int)Constants.currentMarket.market_vip_value);

            m_ctrlPro.setText(m_ctrlVip_Value.getProgress() + "%");
            m_ctrlVip_Value.setProgress(Constants.currentUser.market_vip_value);

            m_ctrlPro.setText(m_ctrlVip_Value.getProgress() + "%");
            if(Float.parseFloat(Constants.currentUser.market_user_value) > 0)
            {
                float ns = Math.round( Float.parseFloat(Constants.currentUser.market_user_value) * montant) / 100;
                m_ctrlIDNew_Money.setText(formatNumber("" + ns, 2, true, false));

                m_ctrlVip_Value.setProgress((int)(Float.parseFloat(Constants.currentUser.market_user_value)));

                m_ctrlPro.setText(Constants.currentUser.market_user_value + "%");
            }else {
                m_ctrlIDNew_Money.setText(formatNumber("" + Constants.currentUser.new_money_vip, 2, true, false));

            }
        }

        if(Constants.currentUser.user_type == 3)
        {
            m_ctrlOption_Right.setChecked(false);
            m_ctrlOption_Left.setChecked(true);
            m_ctrlOption_Left.setEnabled(true);

            m_ctrlVip_Value.setVisibility(View.GONE);
            m_ctrlVip_Value.setProgress(Constants.currentUser.market_member_value);
            m_ctrlIDNew_Money.setText(Constants.currentUser.new_money_member);
        }

        newMoney = m_ctrlIDNew_Money.getText().toString();

        m_ctrlIDNew_Money.setText(formatNumber(m_ctrlIDNew_Money.getText().toString(), 2, true, false));

        newDispo = Constants.currentUser.decote;



        if (Constants.currentMarket.market_name.toUpperCase().equals("CENTRE SAINT-JACQUES")) { // en attente de mettre un champs dans Partners pour spécificité
            findViewById(R.id.idGroupBottom).setVisibility(View.GONE);

            findViewById(R.id.switchGroup).setVisibility(View.GONE);
            findViewById(R.id.idLabelBottom).setVisibility(View.GONE);
            findViewById(R.id.group_member).setVisibility(View.GONE);
            Date today = new Date();

            Calendar cal = Calendar.getInstance();

            int nDay = cal.get(Calendar.DAY_OF_MONTH);
            if (nDay == 4) {
                m_ctrlIDNew_Money.setText("" + Float.parseFloat(m_ctrlIDNew_Money.getText().toString()) * 200);
            }
        } else {
            progressDialog.setCancelable(false);
            progressDialog.show();
            UserCB(Constants.currentUser.User_WalletId);
        }



        //UserCB(Constants.currentUser.User_WalletId);

        m_ctrlOption_Left.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked) {
                m_ctrlOption_Right.setChecked(false);

                //setRange(m_ctrlVip_Value, Constants.currentUser.market_member_value, 100, Constants.currentUser.market_member_value);
                m_ctrlVip_Value.setProgress(Constants.currentUser.market_member_value);
                m_ctrlVip_Value.setVisibility(View.GONE);

                if(m_ctrlCheck.isChecked() == true)
                {
                    newMoney = formatNumber(NextCashBackClassicUser, 2, true, false);
                }else
                    newMoney = formatNumber(Constants.currentUser.new_money_member, 2, true, false);

                m_ctrlIDNew_Money.setText(newMoney);

                m_ctrlPro.setText(m_ctrlVip_Value.getProgress() + "%");
            }
        }
    });

        m_ctrlOption_Right.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
        @Override
        public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
            if(isChecked) {
                m_ctrlOption_Left.setChecked(false);
                m_ctrlVip_Value.setVisibility(View.VISIBLE);

                m_ctrlVip_Value.setProgress(Constants.currentUser.market_vip_value);
                //setRange(m_ctrlVip_Value, Constants.currentUser.market_vip_value, 100, Constants.currentUser.market_vip_value);
                if (m_ctrlCheck.isChecked() == true) {
                    newMoney = formatNumber(NextCashBackVipUser, 2, true, false);
                    if (Float.parseFloat(newMoney) == 0) {
                        m_ctrlVip_Value.setVisibility(View.GONE);
                    }
                }
                else {
                    newMoney = formatNumber(Constants.currentUser.new_money_vip, 2, true, false);

                    m_ctrlPro.setText((m_ctrlVip_Value.getProgress()) + "%");
                }
                m_ctrlIDNew_Money.setText(newMoney);

                ShowFormVIP();
            }
            }
        });

        m_ctrlCheck_1.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    idMontantCB = m_ctrlIDMoment.getText().toString();
                }else
                {
                    idMontantCB = "0.00";
                }
            }
        });

        m_ctrlCheck.setOnCheckedChangeListener(new CompoundButton.OnCheckedChangeListener() {
            @Override
            public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
                if(isChecked)
                {
                    m_ctrlIDMoment.setText(formatNumber(Constants.currentUser.AmountInCaseOfUse, 2, true, false));
                    m_ctrlIDCoupon.setText(formatNumber(Constants.currentUser.VoucherInCaseofUse, 2, true, false));
                    m_ctrlIDExtra.setText(formatNumber(Constants.currentUser.SubsidyInCaseofUse.equals("")?"0.00":Constants.currentUser.SubsidyInCaseofUse, 2, true, false));
                    m_ctrlIDDispo.setText(formatNumber(Constants.currentUser.CashBackAmountInCaseOfUse, 2, true, false));

                    if(Float.parseFloat(Constants.currentUser.AmountInCaseOfUse) < CBlimit)
                    {
                        m_ctrlCheck_1.setVisibility(View.GONE);
                        m_ctrlCheck_1.setChecked(false);
                    }
                    if(m_ctrlOption_Left.isChecked())
                    {
                        newMoney = formatNumber(NextCashBackClassicUser, 2, true, false);
                    }

                    if(m_ctrlOption_Right.isChecked())
                    {
                        newMoney = formatNumber("" + Float.parseFloat(NextCashBackVipUser.equals("")?"0.00":NextCashBackVipUser) * Math.round(m_ctrlVip_Value.getProgress()) / 10 , 2, true, false);
                    }
                }else
                {
                    if (Constants.currentUser.market_UserCB == true && Constants.currentMontant >= CBlimit) {
                        m_ctrlCheck_1.setVisibility(View.VISIBLE);
                        m_ctrlCheck_1.setEnabled(true);
                        m_ctrlCheck_1.setChecked(false);

                    }
                    if (m_ctrlOption_Right.isChecked()) {
                        m_ctrlVip_Value.setVisibility(View.VISIBLE);
                        if (m_ctrlVip_Value.getProgress() == Constants.currentUser.market_vip_value)
                            newMoney = formatNumber(Constants.currentUser.new_money_vip, 2, true, false);
                        else
                            newMoney = formatNumber("" + Math.round(Constants.currentMarket.market_vip_value) / 10 * Constants.currentMontant * Math.round(m_ctrlVip_Value.getProgress()) / 100, 2, true, false);
                    }

                    if (m_ctrlOption_Left.isChecked()) {
                        m_ctrlVip_Value.setVisibility(View.GONE);
                        newMoney = formatNumber(Constants.currentUser.new_money_member, 2, true, false);
                    }
                    m_ctrlIDMoment.setText(formatNumber("" + Constants.currentMontant, 2, true, false));
                    m_ctrlIDCoupon.setText(formatNumber(Constants.currentUser.VoucherAmount, 2, true, false));;
                    m_ctrlIDExtra.setText(formatNumber("" + Constants.currentUser.SubsidyAmount, 2, true, false));
                    m_ctrlIDDispo.setText(formatNumber(Constants.currentUser.decote, 2, true, false));
                }

                m_ctrlIDNew_Money.setText(newMoney);
            }
        });



        m_ctrlVip_Value.setOnProgressChangeListener(new DiscreteSeekBar.OnProgressChangeListener() {
            @Override
            public void onProgressChanged(DiscreteSeekBar seekBar, int value, boolean fromUser) {
                if(m_ctrlCheck.isChecked())
                {
                    newMoney = formatNumber("" + Math.round(Constants.currentMarket.market_vip_value)  / 10 * (Constants.currentMontant - Float.parseFloat(Constants.currentUser.VoucherInCaseofUse) - Float.parseFloat(Constants.currentUser.decote))
                            * value / 100, 2, true, false );

                    if(Float.parseFloat(newMoney) < 0)
                        newMoney = "0.00";
                }else
                {
                    newMoney = formatNumber("" + Constants.currentMontant * Math.round(m_ctrlVip_Value.getProgress()) / 100, 2, true, false);
                }

                m_ctrlIDNew_Money.setText(newMoney);

                m_ctrlPro.setText(m_ctrlVip_Value.getProgress()+ "%");
            }

            @Override
            public void onStartTrackingTouch(DiscreteSeekBar seekBar) {
                if(m_ctrlCheck.isChecked())
                {
                    newMoney = formatNumber("" + Math.round(Constants.currentMarket.market_vip_value)  / 10 * (Constants.currentMontant - Float.parseFloat(Constants.currentUser.VoucherInCaseofUse) - Float.parseFloat(Constants.currentUser.decote))
                            * Math.round(m_ctrlVip_Value.getProgress()) / 100, 2, true, false );

                    if(Float.parseFloat(newMoney) < 0)
                        newMoney = "0.00";
                }else
                {
                    newMoney = formatNumber("" + Constants.currentMontant * Math.round(m_ctrlVip_Value.getProgress()) / 100, 2, true, false);
                }

                m_ctrlIDNew_Money.setText(newMoney);

                m_ctrlPro.setText(m_ctrlVip_Value.getProgress()+ "%");
            }

            @Override
            public void onStopTrackingTouch(DiscreteSeekBar seekBar) {
                if(m_ctrlCheck.isChecked())
                {
                    newMoney = formatNumber("" + Math.round(Constants.currentMarket.market_vip_value)  / 10 * (Constants.currentMontant - Float.parseFloat(Constants.currentUser.VoucherInCaseofUse) - Float.parseFloat(Constants.currentUser.decote))
                            * Math.round(m_ctrlVip_Value.getProgress()) / 100, 2, true, false );

                    if(Float.parseFloat(newMoney) < 0)
                        newMoney = "0.00";
                }else
                {
                    newMoney = formatNumber("" + Constants.currentMontant * Math.round(m_ctrlVip_Value.getProgress()) / 100, 2, true, false);
                }

                m_ctrlIDNew_Money.setText(newMoney);

                m_ctrlPro.setText(m_ctrlVip_Value.getProgress()+ "%");
            }
        });

    }

    public void UserCB(String str)
    {
        String strApi = "";
        strApi += "{Api:'CreditCardsList', Account_WalletId:'"+ str + "',";
        strApi += "Account_PhoneNumber:'" + Constants.currentUser.user_phone + "',";
        strApi += "Account_PhoneCountry:'" + Constants.currentUser.User_PhoneCountry + "',";
        strApi += "ProcessToken:'" + Constants.ProcessToken + "',";
        strApi += "Application_Token:'" + Constants.Application_Token + "',";
        strApi += "Application_ServerId:'" + Constants.APPLICATION_SERVER_ID + "',";
        strApi += "Application_Version:'" + Constants.Application_Version + "'}";

        MyDBProcessing myDBProcessing = MyDBProcessing.getInstance();

        progressDialog.show();
        progressDialog.setCancelable(false);

        myDBProcessing.SendPost(strApi, userCBHandler);

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
    Handler userCBHandler = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            String obj = (String) msg.obj;

            progressDialog.dismiss();
            if(msg.arg2 == Constants.NET_ERR)
            {
                Toast.makeText(ViewStatusActivity.this, getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                return;
            }

            try {
                Constants.MyXmlToJSON(obj);
                interpretUserCB();

            } catch (JSONException e) {
                e.printStackTrace();
            }

            View view = ViewStatusActivity.this.getCurrentFocus();
            if (view != null) {
                InputMethodManager imm = (InputMethodManager)getSystemService(INPUT_METHOD_SERVICE);
                imm.hideSoftInputFromWindow(view.getWindowToken(), 0);
            }
        }
    };

    public void interpretUserCB() throws JSONException {
        Constants.currentUser.cards.clear();

        Constants.ErrorNumber = Constants.GetMatchString("ErrorNumber");

        if(Constants.ErrorNumber.equals("11"))
        {
            Toast.makeText(ViewStatusActivity.this, getButtonName("lab_errorAssist", Constants.CurrentLang), Toast.LENGTH_LONG).show();

            Intent i = new Intent(ViewStatusActivity.this, ViewAdmin.class);

            startActivity(i);

            finish();

            return;
        }

        if(!Constants.ErrorNumber.equals("0"))
        {
            Constants.currentUser.market_UserCB = false;

            m_ctrlIDPay.setVisibility(View.VISIBLE);
            m_ctrlIDPay.setText(getButtonName("lab_addCB", Constants.CurrentLang));

            m_ctrlCheck_1.setVisibility(View.GONE);

            return;
        }
        Constants.ProcessToken = Constants.GetMatchString("ProcessToken");

        if(GETJSONOBJECT().has("Data"))
        {
            JSONObject dataObj = GETJSONOBJECT().getJSONObject("Data");

            if(dataObj.has("Row"))
            {
                Object objects = dataObj.get("Row");

                if(objects instanceof JSONArray)
                {
                    JSONArray obj = dataObj.getJSONArray("Row");

                    for(int i = 0; i < obj.length(); i ++) {
                        JSONObject object  = obj.getJSONObject(i);

                        Constants.currentUser.cards.add(new Card());
                        Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).nIndex =  Constants.currentUser.cards.size() - 1;
                        if(object.has("Token")) {
                            Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).strToken = object.getString("Token");
                        }

                        if(object.has("Tag")) {
                            Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).strTag = object.getString("Tag");
                            Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).strTagMini =  Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).strTag.substring(0, 1).toUpperCase();
                        }
                        if(object.has("Main")) {
                            Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).strMain = object.getString("Main");
                        }

                        if(object.has("Type")) {
                            Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).strType = object.getString("Type");

                            if(Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).strType.toUpperCase().equals("MASTERCARD"))
                            {
                                Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).nImage = R.drawable.mastercard;
                            }
                            if(Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).strType.toUpperCase().equals("VISA"))
                            {
                                Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).nImage = R.drawable.visa;
                            }
                            if(Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).strType.toUpperCase().equals("CB"))
                            {
                                Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).nImage = R.drawable.cb;
                            }
                            if(Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).strType.toUpperCase().equals("AMEX"))
                            {
                                Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).nImage = R.drawable.amex;
                            }
                            if(Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).strType.toUpperCase().equals("DINERS"))
                            {
                                Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).nImage = R.drawable.diners;
                            }
                            Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).strType = Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).strType.substring(0, 4);
                        }

                        if(object.has("Number"))
                        {
                            Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).strNumber = object.getString("Number");
                            Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).strNumberMini = object.getString("Number").substring(object.getString("Number").length() - 8);
                        }

                        if(object.has("Month"))
                        {
                            Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).strMonth = object.getString("Month");
                        }
                        if(object.has("Year"))
                        {
                            Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).strYear = object.getString("Year");
                            Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).strExpDate =  Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).strMonth + "/" + object.getString("Year").substring(2);
                        }
                    }

                }else if(objects instanceof JSONObject)
                {
                    JSONObject object  = dataObj.getJSONObject("Row");

                    Constants.currentUser.cards.add(new Card());
                    Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).nIndex =  Constants.currentUser.cards.size() - 1;
                    if(object.has("Token")) {
                        Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).strToken = object.getString("Token");
                    }

                    if(object.has("Tag")) {
                        Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).strTag = object.getString("Tag");
                        Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).strTagMini =  Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).strTag.substring(0, 1).toUpperCase();
                    }
                    if(object.has("Main")) {
                        Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).strMain = object.getString("Main");
                    }

                    if(object.has("Type")) {
                        Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).strType = object.getString("Type");

                        if(Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).strType.toUpperCase().equals("MASTERCARD"))
                        {
                            Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).nImage = R.drawable.mastercard;
                        }
                        if(Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).strType.toUpperCase().equals("VISA"))
                        {
                            Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).nImage = R.drawable.visa;
                        }
                        if(Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).strType.toUpperCase().equals("CB"))
                        {
                            Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).nImage = R.drawable.cb;
                        }
                        if(Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).strType.toUpperCase().equals("AMEX"))
                        {
                            Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).nImage = R.drawable.amex;
                        }
                        if(Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).strType.toUpperCase().equals("DINERS"))
                        {
                            Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).nImage = R.drawable.diners;
                        }
                        Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).strType = Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).strType.substring(0, 4);
                    }

                    if(object.has("Number"))
                    {
                        Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).strNumber = object.getString("Number");
                        Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).strNumberMini = object.getString("Number").substring(object.getString("Number").length() - 8);
                    }

                    if(object.has("Month"))
                    {
                        Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).strMonth = object.getString("Month");
                    }
                    if(object.has("Year"))
                    {
                        Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).strYear = object.getString("Year");
                        Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).strExpDate =  Constants.currentUser.cards.get( Constants.currentUser.cards.size() - 1).strMonth + "/" + object.getString("Year").substring(2);
                    }
                }
            }
        }

        Constants.ProcessToken = GetMatchString("ProcessToken");
        if(Constants.currentMarket.market_status.equals("0"))
        {
            m_ctrlCheck_1.setVisibility(View.VISIBLE);
            m_ctrlCheck_1.setEnabled(false);
            m_ctrlIDPay.setVisibility(View.GONE);
            m_ctrlCheck_1_Help.setText(getButtonName("demomode", Constants.CurrentLang));
        }else
        {
            Constants.currentUser.market_UserCB = true;

            m_ctrlCheck_1.setChecked(false);

            m_ctrlCheck_1.setVisibility(View.VISIBLE);
            m_ctrlIDPay.setVisibility(View.GONE);
            m_ctrlCheck_1.setEnabled(true);


            if(Constants.currentMontant >= CBlimit)
            {
                m_ctrlCheck_1_Help.setText(getButtonName("lab_CB", Constants.CurrentLang));

            }else
            {
                m_ctrlCheck_1.setEnabled(false);
                m_ctrlCheck_1.setChecked(false);
                m_ctrlCheck_1_Help.setText(getButtonName("lab_amount", Constants.CurrentLang) + "<" + CBlimit + getButtonName("lb_coupon_money", Constants.CurrentLang));
            }
        }
    }
    public String formatNumber(String number, int maxDecimals, boolean forceDecimals, boolean siStyle) //MaxDecials default:2 forceDecimals:true, siStyle:false
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

    public String getButtonName(String strTagName, String strLanguage)
    {
        String strResult = "";

        if(strTagName.equals(""))
            return strResult;

        for(int i = 0; i < Constants.myTitle.size(); i ++)
        {
            MyTitles title = Constants.myTitle.get(i);

            if(title.strTag.equals(strTagName)) {
                if(strLanguage.equals("FR"))
                    strResult = title.strFrace;
                else
                    strResult = title.strEng;
            }
        }
        return strResult;
    }

    @Override
    public void onClick(View v)
    {
        if(v == m_ctrlIDPay)
        {
            Toast.makeText(ViewStatusActivity.this, getButtonName("lab_EnterCB", Constants.CurrentLang), Toast.LENGTH_LONG).show();

            clickPay();

            return;
        }
        if(v == m_ctrlBanner)
        {
            Intent i = new Intent(ViewStatusActivity.this, MainActivity.class);

            startActivity(i);

            finish();
        }
        if(v == m_ctrlApply)
        {
            clickValider();
        }

        if(v == m_ctrlCancel)
        {
            Toast.makeText(ViewStatusActivity.this, getButtonName("lab_PayCancel", Constants.CurrentLang), Toast.LENGTH_LONG).show();

            Intent i = new Intent(ViewStatusActivity.this, ShowMoneyActivity.class);

            startActivity(i);

            finish();

            return;
        }
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();

        Toast.makeText(ViewStatusActivity.this, getButtonName("lab_PayCancel", Constants.CurrentLang), Toast.LENGTH_LONG).show();

        Intent i = new Intent(ViewStatusActivity.this, ShowMoneyActivity.class);

        startActivity(i);

        finish();
    }

    public void clickValider()
    {
        float dispo = 0;
        float decote = 0;
        float CbIncaseofuse = 0;

        m_ctrlStar.setVisibility(View.INVISIBLE);

        Toast.makeText(ViewStatusActivity.this, getButtonName("lab_processTransac", Constants.CurrentLang), Toast.LENGTH_LONG).show();

        if (m_ctrlCheck.isChecked() == true) {
            decote = Constants.currentUser.AmountUsed;
        }
        CbIncaseofuse = Constants.currentMontant - decote;

        Constants.currentUser.user_type = m_ctrlOption_Left.isChecked()?3:1;

        if (m_ctrlOption_Left.isChecked())
            Constants.Percent = "";
        else
            Constants.Percent = "" + (m_ctrlVip_Value.getProgress());

        Constants.currentUser.AmountCashBack = Float.parseFloat(m_ctrlIDNew_Money.getText().toString());

        if (Constants.IdPayClick == true)
        {
            Constants.AmountToPay = Constants.currentMontant;
            Constants.AmountUsed = decote;
            Constants.User_WalletId = Constants.currentUser.User_WalletId;
            Constants.CB = Constants.currentUser.market_UserCB;
            Constants.Email = Constants.currentUser.user_email;
            Constants.AmountCB = Constants.currentMontant;

            Intent i = new Intent(ViewStatusActivity.this, ViewCreditCard.class);

            startActivity(i);

            finish();

            return;
        } else if (m_ctrlCheck_1.isChecked()) {

            Constants.IdPayClick = true;
            Constants.AmountToPay = Constants.currentMontant;
            Constants.AmountUsed = decote;
            Constants.User_WalletId = Constants.currentUser.User_WalletId;
            Constants.CB = Constants.currentUser.market_UserCB;
            Constants.Email = Constants.currentUser.user_email;
            Constants.AmountCB = CbIncaseofuse;

            Intent i = new Intent(ViewStatusActivity.this, ViewCCPay.class);

            startActivity(i);

            finish();

            return;
        } else if (decote > 0) {
           Intent i = new Intent(ViewStatusActivity.this, ViewCashBackPay.class);

            startActivity(i);

            finish();

            return;

        } else {
            executePayment(Constants.currentMontant, decote, 0, Constants.Percent, Float.parseFloat(idMontantCB), "");
        }
    }

    public void clickPay()
    {
        Constants.IdPayClick = true;

        clickValider();
    }

    Handler executePaymentHandle = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            String str = (String)msg.obj;

            progressDialog.dismiss();
            if(msg.arg2 != Constants.NET_ERR)
            {
                try {

                    Constants.MyXmlToJSON(str);

                    executePaymentInterpret();

                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else
            {
                Toast.makeText(ViewStatusActivity.this, getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();
            }
        }
    };
    public void executePaymentInterpret() throws  JSONException {
        Constants.ErrorNumber = Constants.GetMatchString("ErrorNumber");

        if (Constants.ErrorNumber.equals("200"))   // test si Partner Insolvent et en théorie pas de règlement par CB car la CB recrédite
        {
            if (Constants.currentMarket.market_cb == false) {

                Toast.makeText(ViewStatusActivity.this, getButtonName("lab_ValidAccount", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                Constants.AmountCB = Constants.currentMarket.market_ToPay;

                Intent i = new Intent(ViewStatusActivity.this, ViewCreditCard.class);

                startActivity(i);

                finish();

            }
            else {
                Toast.makeText(ViewStatusActivity.this, getButtonName("lab_depositError", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                Intent i = new Intent(ViewStatusActivity.this, ShowMoneyActivity.class);

                startActivity(i);

                finish();
            }

            return;
        }


        Constants.Message = Constants.GetMatchString("Message");

        if (Constants.ErrorNumber.equals("0")) {
            Constants.ProcessToken = Constants.GetMatchString("ProcessToken");;
            Toast.makeText(ViewStatusActivity.this, getButtonName("lab_credit_ok", Constants.CurrentLang), Toast.LENGTH_LONG).show();

            Constants.currentMarket.market_ToPay = 0;

            Intent i = new Intent(ViewStatusActivity.this, ShowMoneyActivity.class);

            startActivity(i);

            finish();
        }
        else if (Constants.ErrorNumber.equals("250"))   // test si erreur Payline - code Payline dans Message
        {
           String[] arrays = Constants.Message.split(" : ");

            Toast.makeText(ViewStatusActivity.this, arrays[1], Toast.LENGTH_LONG).show();
            Intent i = new Intent(ViewStatusActivity.this, ShowMoneyActivity.class);

            startActivity(i);

            finish();
        }
        else if (Constants.ErrorNumber.equals("257") || Constants.ErrorNumber.equals("256")) {
                Toast.makeText(ViewStatusActivity.this, Constants.Message, Toast.LENGTH_LONG).show();

                Constants.currentUser.cards.remove(Constants.CCIndex);
                Intent i = new Intent(ViewStatusActivity.this, ViewCCPay.class);

                startActivity(i);

                finish();
        }
        else {
            Toast.makeText(ViewStatusActivity.this, Constants.Message, Toast.LENGTH_LONG).show();

            Intent i = new Intent(ViewStatusActivity.this, ShowMoneyActivity.class);

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

        progressDialog.show();

        progressDialog.setCancelable(false);

        processing.SendPost(strApi, executePaymentHandle);
    }

    public void ShowFormVIP()
    {
        String strText = "";

        strText += getButtonName("lab_upgrade_vip", Constants.CurrentLang);
        strText += '\n';
        strText += getButtonName("lab_sponsored", Constants.CurrentLang) + " : " + Constants.currentUser.market_VouchersCountToSend;

        strText += '\n';

        strText += getButtonName("lab_Cost", Constants.CurrentLang) + " : " + Constants.currentUser.market_VouchersCostToSend + getButtonName("lb_coupon_money", Constants.CurrentLang);

        new MaterialDialog.Builder(ViewStatusActivity.this).message(strText)
                .positiveText(getButtonName("lab_OK", Constants.CurrentLang))

                .buttonCallback(new MaterialDialog.ButtonCallback() {
                    @Override
                    public void onPositive(MaterialDialog dialog) {
                        super.onPositive(dialog);
                        dialog.dismiss();
                    }

                }).show();
    }
}
