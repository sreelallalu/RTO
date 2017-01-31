package org.venture.rto.rto3;

import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.EachExceptionsHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.HashMap;

import static org.venture.rto.rto3.NetworkChecker.isConnected;
import static org.venture.rto.rto3.NetworkChecker.isConnectedMobile;
import static org.venture.rto.rto3.NetworkChecker.isConnectedWifi;

/**
 * Created by SLR on 6/1/2016.
 */
public class LicenS extends android.support.v4.app.Fragment {

    private TextView v_lics_no,v_name,v_penalty,v_Exp,v_adrs,v_valid,v_classtype;
  private   EditText V_no;
    private Button V_check,V_refresh;
//android:background="@drawable/buttonscale
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.lics,
                container, false);
        V_no= (EditText) view.findViewById(R.id.lic_no);
        V_check=(Button)view.findViewById(R.id.lic_send);

        v_name=(TextView)view.findViewById(R.id.lic_name);
        v_penalty=(TextView)view.findViewById(R.id.lic_penatlty);

        v_Exp=(TextView)view.findViewById(R.id.lic_regno);

        v_adrs=(TextView)view.findViewById(R.id.lic_addrs);
        v_lics_no=(TextView)view.findViewById(R.id.bvehi1);

        v_valid=(TextView)view.findViewById(R.id.validpoint);
        v_classtype=(TextView)view.findViewById(R.id.typelicense);

        V_refresh=(Button)view.findViewById(R.id.vrefresh1);

        V_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String encodeImagez=null;
                encodeImagez= V_no.getText().toString();


                if(encodeImagez.trim().equals("")){

                    Toast.makeText(getActivity(),"null value",Toast.LENGTH_SHORT).show();
                }else

                {
                    if (isConnectedWifi(getActivity()) || isConnectedMobile(getActivity())) {
                        if (isConnected(getActivity()))


                        {


                            HashMap<String, String> hashMap = new HashMap<String, String>();

                            //Intent i=new Intent(getActivity(),MainActivity.class).putExtra("TEXTR",encodeImagez);
                            //startActivity(i);


                            hashMap.put("licenseno", encodeImagez);

                            PostResponseAsyncTask task = new PostResponseAsyncTask(getActivity(), hashMap, new AsyncResponse() {

                                public void processFinish(String s) {


                                    try {
                                        JSONObject qj = new JSONObject(s);
                                        String ss1 = null, ss2 = null, ss3 = null, ss4 = null, ss5 = null,ss6=null,ss7=null;
                                        ss1 = qj.getString("license_no");

                                        ss2 = qj.getString("license_own_name");
                                        ss3 = qj.getString("license_penalty");
                                        ss4 = qj.getString("license_Validpoint");
                                        ss5 = qj.getString("license_Exp");
                                        ss6 = qj.getString("license_Class");
                                        ss7 = qj.getString("license_Adr");
                                        //ss6 = qj.getString("license_Adr");
                                        //ss7 = qj.getString("license_Adr");

                                        if (ss1 != " " || ss2 != null || ss3 != null || ss4 != null||ss5!=null||ss6!=null||ss7!=null) {
                                            v_lics_no.setText(ss1);
                                            v_name.setText(ss2);
                                            v_penalty.setText(ss3);
                                            v_valid.setText(ss4);
                                            v_Exp.setText(ss5);
                                            v_classtype.setText(ss6);
                                            v_adrs.setText(ss7);


                                        } else {
                                            v_lics_no.setText("");
                                            v_name.setText("");
                                            v_penalty.setText("");
                                            v_valid.setText("");
                                            v_Exp.setText("");
                                            v_classtype.setText("");
                                            v_adrs.setText("");

                                        }
                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }

                                    //st.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();
                                }

                            });
                            task.execute("http://rto.venturesoftwares.org/license_cc.php");

                            task.setEachExceptionsHandler(new EachExceptionsHandler() {
                                @Override
                                public void handleIOException(IOException e) {
                                    Toast.makeText(getActivity(), "time out", Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void handleMalformedURLException(MalformedURLException e) {
                                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void handleProtocolException(ProtocolException e) {
                                    Toast.makeText(getActivity(), e.toString(), Toast.LENGTH_SHORT).show();
                                }

                                @Override
                                public void handleUnsupportedEncodingException(UnsupportedEncodingException e) {
                                    Toast.makeText(getActivity(), "handle pro", Toast.LENGTH_SHORT).show();
                                }
                            });


                        }else {
                            Toast.makeText(getActivity(), "no connection", Toast.LENGTH_LONG).show();
                        }

                        ////
                    } else{
                        Toast.makeText(getActivity(),"turn on wifi",Toast.LENGTH_LONG).show();
                    }


                }


            }
        });
       V_refresh.setOnClickListener(new View.OnClickListener() {
           @Override
           public void onClick(View v) {

               V_no.setText("");
               v_lics_no.setText("");
               v_name.setText("");
               v_penalty.setText("");
               v_Exp.setText("");
               v_adrs.setText("");

               v_valid.setText("");
               v_classtype.setText("");

           }
       });

        return view;

    }




}
