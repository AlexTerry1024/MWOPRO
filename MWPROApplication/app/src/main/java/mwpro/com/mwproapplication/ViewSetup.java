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

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import mwpro.com.mwproapplication.data.MyTitles;

public class ViewSetup extends Fragment {

    Context con;
    TextView m_ctrlBottomText;
    public static ViewSetup newInstance() {
        ViewSetup fragment = new ViewSetup();
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
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
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        con = this.getContext();

        View view =  inflater.inflate(R.layout.fragment_item_one, container, false);

        Button btnStart = (Button)view.findViewById(R.id.start);

        btnStart.setText(getButtonName("btn_home", Constants.CurrentLang));

        TextView topText = (TextView)view.findViewById(R.id.topText);

        topText.setText(getButtonName("lab_Intro_Setup", Constants.CurrentLang));
        m_ctrlBottomText = (TextView)view.findViewById(R.id.bottom_text);

        if(Constants.APPLICATION_SERVER_ID == 0 || Constants.APPLICATION_SERVER_ID == 3)
        {
            m_ctrlBottomText.setText("P: " + Constants.Application_Version);
        }else if(Constants.APPLICATION_SERVER_ID == 1)
        {
            m_ctrlBottomText.setText("T: " + Constants.Application_Version);
        }else
        {
            m_ctrlBottomText.setText("D: " + Constants.Application_Version);
        }

        btnStart.setOnClickListener(new View.OnClickListener(){

            @Override
            public void onClick(View v) {
                Intent i = new Intent(con, MainActivity.class);

                startActivity(i);
            }
        });
        return view;
    }
}
