package org.venture.rto.rto3;

import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Build;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;

import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.EachExceptionsHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import org.json.JSONObject;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.HashMap;

/**
 * Created by SLR on 6/9/2016.
 */
public class Register extends AppCompatActivity {

    String send;
    TextView rt;
    Button ok;
     EditText h;
    @Override

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.register);
         rt=(TextView)findViewById(R.id.checkingreg);
        ok=(Button)findViewById(R.id.okbuttoni);
        h=(EditText)findViewById(R.id.registeredit);

        SharedPreferences prefs = getPreferences(MODE_PRIVATE);
        Boolean status = prefs.getBoolean("register", false);
        if (status) {
            finish();
            Intent i = new Intent(Register.this, MainActivity.class);
            startActivity(i);

        }


        else{
            phonestatus();

        ok.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               send=h.getText().toString();
                try{
                DataB bn=new DataB(Register.this);
                    bn.open();
                    bn.createEntry(send);
                    bn.close();


                }catch (Exception e){
                    e.printStackTrace();
                }
                TelephonyManager tm = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
                String device_id = tm.getDeviceId();



                String manufacturer = Build.MANUFACTURER;
                String model = Build.MODEL;

                HashMap<String, String> hashMap = new HashMap<String, String>();





                hashMap.put("keycno",send);
                hashMap.put("deviceid",device_id);
                hashMap.put("manufacture",manufacturer);
                hashMap.put("modelid",model);

                PostResponseAsyncTask task = new PostResponseAsyncTask(Register.this, hashMap, new AsyncResponse() {

                    public void processFinish(String s) {


                        try {String ss1;
                            JSONObject qj = new JSONObject(s);
                              ss1 = qj.getString("gets");
                           if(ss1.trim().equals("succ")){
                               Toast.makeText(Register.this, "Register complete",
                                       Toast.LENGTH_LONG).show();
                               SharedPreferences.Editor editor = getPreferences(
                                       MODE_PRIVATE).edit();
                               editor.putBoolean("register", true);

                               editor.commit();
                               finish();
                               Intent i = new Intent(Register.this,
                                       MainActivity.class);
                               startActivity(i);


                           }else{
                               Toast.makeText(Register.this,s,Toast.LENGTH_SHORT).show();
                           }




                        } catch (Exception e1) {
                            e1.printStackTrace();
                        }

                        //st.makeText(getActivity(), s, Toast.LENGTH_SHORT).show();


                    }

                });
                task.execute("http://rto.venturesoftwares.org/pass_cc.php");

                task.setEachExceptionsHandler(new EachExceptionsHandler() {
                    @Override
                    public void handleIOException(IOException e) {
                        Toast.makeText(Register.this, "time out", Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void handleMalformedURLException(MalformedURLException e) {
                        Toast.makeText(Register.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void handleProtocolException(ProtocolException e) {
                        Toast.makeText(Register.this, e.toString(), Toast.LENGTH_SHORT).show();
                    }

                    @Override
                    public void handleUnsupportedEncodingException(UnsupportedEncodingException e) {
                        Toast.makeText(Register.this, "handle pro", Toast.LENGTH_SHORT).show();
                    }
                });


            }
        });

    }}

    private void phonestatus() {

    }
}
