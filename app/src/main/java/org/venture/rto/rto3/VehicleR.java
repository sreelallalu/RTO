package org.venture.rto.rto3;

import android.os.Bundle;
import android.support.v4.app.Fragment;
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
public class VehicleR extends Fragment {

  private   TextView v_veNo,v_name,v_penalty,v_adrs,v_engo,v_type,v_classf;
   private EditText V_no;
   private Button V_check;
    private Button V_refresh;



    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.vehicle,

                container, false);

           V_no= (EditText) view.findViewById(R.id.vehi_no);

           V_check=(Button)view.findViewById(R.id.vehi_ok);
        v_veNo=(TextView)view.findViewById(R.id.bvehi);
           v_name=(TextView)view.findViewById(R.id.vehi_name);
        v_penalty=(TextView)view.findViewById(R.id.vehi_penalty);


        v_engo=(TextView)view.findViewById(R.id.vehi_engno);
        v_type=(TextView)view.findViewById(R.id.type_regno);
        v_classf=(TextView)view.findViewById(R.id.vehi_classf);

        v_adrs=(TextView)view.findViewById(R.id.vehi_addrs);

        V_refresh=(Button)view.findViewById(R.id.vrefresh);
         V_no.getText().clear();
  // final Animation scale= (Animation) AnimationUtils.loadAnimation(getActivity(),R.anim.alpha);

        V_refresh.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               V_no.setText("");
                v_veNo.setText("");
                v_veNo.setText("");
                v_name.setText("");
                v_penalty.setText("");



                v_engo.setText("");
                v_type.setText("");
                v_classf.setText("");
                v_adrs.setText("");
            }
        });


        V_check.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String encodeImagez=null;


                encodeImagez =V_no.getText().toString();

               /* if(encodeImagez==null||encodeImagez==""){

                    Toast.makeText(getActivity(),"null value",Toast.LENGTH_SHORT).show();
                }*/

                if(encodeImagez.trim().equals("")){

                    Toast.makeText(getActivity(),"null value",Toast.LENGTH_SHORT).show();


                }
               else

                {
                    if(isConnectedWifi(getActivity())||isConnectedMobile(getActivity()))
                    {
                        if(isConnected(getActivity())) {


                            HashMap<String, String> hashMap = new HashMap<String, String>();





                            hashMap.put("vehino", encodeImagez);

                            PostResponseAsyncTask task = new PostResponseAsyncTask(getActivity(), hashMap, new AsyncResponse() {

                                public void processFinish(String s) {


                                    try {


                                        JSONObject qj = new JSONObject(s);
                                        String ss1 = null, ss2 = null, ss3 = null, ss4 = null, ss5 = null,ss6=null,ss7=null,ss8=null;
                                        ss1 = qj.getString("vehicle_no");

                                        ss2 = qj.getString("vehicle_own_name");
                                        ss3=qj.getString("vehicle_penality");
                                        ss4 = qj.getString("vehicle_Engin");

                                        ss5 = qj.getString("vehicle_Type");
                                        ss6 = qj.getString("vehicle_Class");
                                        ss7 = qj.getString("vehicle_Adr");
                                        //ss6 = qj.getString("vehicle_Adr");
                                       // ss7 = qj.getString("vehicle_Adr");
                                        //ss8 = qj.getString("vehicle_Adr");


                                        v_veNo.setText(ss1);

                                        v_name.setText(ss2);
                                        v_penalty.setText(ss3);
                                        v_engo.setText(ss4);
                                        v_type.setText(ss5);
                                        v_classf.setText(ss6);
                                        v_adrs.setText(ss7);





                                    } catch (JSONException e1) {
                                        e1.printStackTrace();
                                    }

                                    //st.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();


                                }

                            });
                            task.execute("http://rto.venturesoftwares.org/vehicle_cc.php");

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

                            ///
                        }else{
                    Toast.makeText(getActivity(),"no connection",Toast.LENGTH_LONG).show();
                }


            }else{
                Toast.makeText(getActivity(),"turn on wifi",Toast.LENGTH_LONG).show();
            }
                }

                /*else{
                Toast.makeText(getActivity(),"bh",Toast.LENGTH_SHORT).show();
                 }*/
                V_no.getText().clear();
             }
        });
        return view;

    }


}
