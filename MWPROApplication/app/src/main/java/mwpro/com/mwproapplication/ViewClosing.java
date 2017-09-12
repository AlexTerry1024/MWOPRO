/*
 * Copyright (c) 2017. Truiton (http://www.truiton.com/).
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Contributors:
 * Mohit Gupt (https://github.com/mohitgupt)
 *
 */

package mwpro.com.mwproapplication;

import android.app.Activity;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.xmlpull.v1.XmlPullParserException;

import java.io.IOException;
import java.util.ArrayList;

import de.codecrafters.tableview.listeners.TableDataClickListener;
import mwpro.com.mwproapplication.ui.CustomProgressDialog;
import mwpro.com.mwproapplication.ui.MySimpleTableDataAdapter;
import mwpro.com.mwproapplication.ui.MySimpleTableHeaderAdapter;
import mwpro.com.mwproapplication.ui.MyTableView;

import static mwpro.com.mwproapplication.Constants.GETJSONOBJECT;
import static mwpro.com.mwproapplication.Constants.GetMatchString;
import static mwpro.com.mwproapplication.Constants.MyXmlToJSON;
import static mwpro.com.mwproapplication.Constants.getButtonName;
import static mwpro.com.mwproapplication.Constants.vibrate;

public class ViewClosing extends Fragment {

    Context con = null;
    CustomProgressDialog customProgressDialog = null;
    int nSelected = -1;
    ArrayList<String[]> strData = new ArrayList<String[]>();
    ArrayList<String[]> strRealData = new ArrayList<String[]>();
    MyTableView m_ctrlTableView = null;
    Button m_ctrlMailMe = null;
    public static ViewClosing newInstance() {
        ViewClosing fragment = new ViewClosing();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view =  inflater.inflate(R.layout.fragment_item_three, container, false);

        con = this.getContext();

        m_ctrlTableView = (MyTableView) view.findViewById(R.id.tableView);

        m_ctrlMailMe = (Button)view.findViewById(R.id.btn_mail_me);

        MySimpleTableHeaderAdapter adapter = new MySimpleTableHeaderAdapter(con , new String[] {getButtonName("lab_Partner_User", Constants.CurrentLang), getButtonName("lab_Total_Cashback", Constants.CurrentLang), getButtonName("lab_Total_Used", Constants.CurrentLang), getButtonName("lb_coupon", Constants.CurrentLang)});

        m_ctrlTableView.setHeaderAdapter(adapter);

        m_ctrlTableView.setSaveEnabled(true);

        customProgressDialog = new CustomProgressDialog(this.getContext(), "");

        m_ctrlMailMe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                onClickMailMe();
            }
        });

        m_ctrlTableView.addDataClickListener(new TableDataClickListener() {
            @Override
            public void onDataClicked(int rowIndex, Object clickedData) {
                m_ctrlTableView.getDataAdapter().selectChanged(rowIndex);
            }
        });

        transactionsListByDate();

        Toast.makeText(ViewClosing.this.getContext(), getButtonName("lb_titre_closing", Constants.CurrentLang), Toast.LENGTH_LONG).show();

        return view;
    }

    public void transactionsListByDate(){

        String strApi = "{Api: 'TransactionsListByDate',";
        strApi += "Partner_PinCode:'" + Constants.codeMarket + "',";
        strApi += "Partner_PhoneNumber:'" + Constants.codeTel + "',";
        strApi += "Partner_PhoneCountry:'" + Constants.CurrentLang + "',";
        strApi += "ProcessToken:'" + Constants.ProcessToken + "',";
        strApi += "Application_Token:'" + Constants.Application_Token + "',";
        strApi += "Application_ServerId:'" + Constants.APPLICATION_SERVER_ID + "',";
        strApi += "Application_Version:'" + Constants.Application_Version + "'}";

        MyDBProcessing processing = MyDBProcessing.getInstance();

        processing.SendPost(strApi, getList);

        customProgressDialog.setCancelable(false);

        customProgressDialog.show();

        Activity act = (Activity)this.getContext();
    }

    Handler getList = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            String str = (String) msg.obj;

            customProgressDialog.dismiss();

            if(msg.arg2 != Constants.NET_SUC) {

                Toast.makeText(ViewClosing.this.getContext(), getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                vibrate(ViewClosing.this.getActivity());

                return;
            }

            try {
                MyXmlToJSON(str);
                interpretList(str);

            } catch (JSONException e) {
                e.printStackTrace();
            }
        }
    };

    public void interpretList(String str) throws JSONException {

        Constants.ErrorNumber = GetMatchString("ErrorNumber");

        if (!Constants.ErrorNumber.equals("0")) {
            Toast.makeText(this.getContext(), getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();

            vibrate(ViewClosing.this.getActivity());

            return;
        }

        Constants.ProcessToken = GetMatchString("ProcessToken");

        if(GETJSONOBJECT().has("Data"))
        {
            JSONObject dataObject = GETJSONOBJECT().getJSONObject("Data");

            if(dataObject.has("Row"))
            {
                Object row = dataObject.get("Row");

                if(row instanceof JSONObject)
                {
                    JSONObject obj = dataObject.getJSONObject("Row");

                    String[] perItem = new String[4];

                    if(obj.has("ExternalTag"))
                        perItem[0] = obj.getString("ExternalTag");
                    if(obj.has("Total_CashBack"))
                        perItem[1] = obj.getString("Total_CashBack");
                    if(obj.has("Total_Payment"))
                        perItem[2] = obj.getString("Total_Payment");
                    if(obj.has("Total_Voucher"))
                        perItem[3] = obj.getString("Total_Voucher");

                    strData.add(perItem);
                }else if(row instanceof JSONArray)
                {
                    JSONArray arrays = dataObject.getJSONArray("Row");
                    for (int i = 0; i < arrays.length(); i ++) {

                        JSONObject obj = arrays.getJSONObject(i);

                        String[] perItem = new String[4];

                        if(obj.has("ExternalTag"))
                            perItem[0] = obj.getString("ExternalTag");
                        if(obj.has("Total_CashBack"))
                            perItem[1] = obj.getString("Total_CashBack");
                        if(obj.has("Total_Payment"))
                            perItem[2] = obj.getString("Total_Payment");
                        if(obj.has("Total_Voucher"))
                            perItem[3] = obj.getString("Total_Voucher");

                        strData.add(perItem);
                    }
                }
            }
        }
        m_ctrlTableView.setDataAdapter(new MySimpleTableDataAdapter(this.getContext(), strData));
    }

    public void onClickMailMe()
    {
        String strApi = "{Api: 'TransactionsListByDatePost',";

        strApi += "Partner_PinCode:'" + Constants.codeMarket + "',";
        strApi += "Partner_PhoneNumber:'" + Constants.codeTel + "',";
        strApi += "Partner_PhoneCountry:'" + Constants.Current_Country + "',";
        strApi += "ProcessToken:'" + Constants.ProcessToken + "',";
        strApi += "Application_Token:'" + Constants.Application_Token + "',";
        strApi += "Application_ServerId:'" + Constants.APPLICATION_SERVER_ID + "',";
        strApi += "Application_Version:'" + Constants.Application_Version + "'}";

        MyDBProcessing processing = MyDBProcessing.getInstance();

        processing.SendPost(strApi, handle);

        customProgressDialog.show();

        customProgressDialog.setCancelable(false);
    }

    Handler handle = new Handler()
    {
        @Override
        public void handleMessage(Message msg) {
            String str = (String) msg.obj;

            customProgressDialog.dismiss();
            if(msg.arg1 != Constants.NET_ERR)
            {
                try {
                    MyXmlToJSON(str);

                    Constants.ErrorNumber = GetMatchString("ErrorNumber");

                    if(Constants.ErrorNumber.equals("0"))
                    {
                        Constants.ProcessToken = GetMatchString("ProcessToken");

                        Toast.makeText(ViewClosing.this.getContext(), getButtonName("lab_cloture_ok", Constants.CurrentLang), Toast.LENGTH_LONG).show();

                        return;
                    }else
                    {
                        Toast.makeText(ViewClosing.this.getContext(), getButtonName("lab_http_error", Constants.CurrentLang), Toast.LENGTH_LONG).show();
                    }
                } catch (JSONException e) {
                    e.printStackTrace();
                }
            }else
            {
                vibrate(ViewClosing.this.getActivity());
            }
        }
    };
}
