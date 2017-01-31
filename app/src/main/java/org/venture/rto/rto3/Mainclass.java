package org.venture.rto.rto3;

/**
 * Created by SLR on 6/2/2016.
 */

import android.Manifest;
import android.app.Activity;
import android.content.Intent;
import android.content.IntentSender;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.graphics.Matrix;
import android.location.Location;
import android.location.LocationListener;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.location.LocationRequest;
import com.google.android.gms.location.LocationServices;
import com.kosalgeek.android.photoutil.CameraPhoto;
import com.kosalgeek.android.photoutil.ImageBase64;
import com.kosalgeek.android.photoutil.ImageLoader;
import com.kosalgeek.genasync12.AsyncResponse;
import com.kosalgeek.genasync12.EachExceptionsHandler;
import com.kosalgeek.genasync12.PostResponseAsyncTask;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.MalformedURLException;
import java.net.ProtocolException;
import java.util.HashMap;

import static org.venture.rto.rto3.NetworkChecker.isConnected;
import static org.venture.rto.rto3.NetworkChecker.isConnectedMobile;
import static org.venture.rto.rto3.NetworkChecker.isConnectedWifi;

/**
 * Created by SLR on 5/24/2016.
 */
public class Mainclass extends AppCompatActivity implements LocationListener ,GoogleApiClient.ConnectionCallbacks,
        GoogleApiClient.OnConnectionFailedListener {
    private final String TAG = Mainclass.this.getClass().getName();
    ;
    TextView e;
    // private JSONObject jsonObject;
    EditText vehi1, lis2;
     AutoCompleteTextView   lis1,lis3;
    final int CAMERA_DATA = 0;
    final int CAMERA_DATAD = 13323;
    String vehicle = null, license;
    Bitmap yourImage = null;
    Bitmap bitmap;
    String[] validate;
    String file_path;
    String[] offense = {"Parking Violations", "Overtaking dangerously", "Driving without Helmet",
            "Overspeed", "Driving under the influence of drugs or alchols", "Accidental offences",
            "Driving without registration and wallet point", "Dangerous or reckless driving", "Traffic violations"};

    String regis[]={"01","02","03","04","05","06","07","08","09"
            ,"10","11","12","13","14","15","16","17","18","19",
            "20","21","22","23","24","25","26","27","28","29",
            "30","31","32","33","34","35","36","37","38","39","40","41","42"
            ,"43","44","45","46","47","48","49","50","51","52","53","54","55",
            "56","57","58","59","60","61","62","63","64","65","66","67","68","69","70","71","72","73"};
    String regis1[]={"2000","2001","2002","2003","2004","2005","2006",
            "2007","2008","2009","2010","2011","2012","2013","2014","2015","2016"};




		
		
		
    ArrayAdapter<String> offenseAdapter;
    AutoCompleteTextView spinner1;
    Uri outputFileUri;
    ImageView cameraclick, viewclick;
    Button send,clearty;
    String encodeImage1 = null;
    TextView errorvehi2;

    /////lali
    CameraPhoto photocr;
    String idu;
    String picString;
    private String latitudek;
    String df = "cool";
    String dff = "gnnn";
    private final static int CONNECTION_FAILURE_RESOLUTION_REQUEST = 9000;
    private GoogleApiClient mGoogleApiClient;
    private LocationRequest mLocationRequest;
    private double currentLatitude;
    private double currentLongitude;

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.complaints);

        lis1 = (AutoCompleteTextView) findViewById(R.id.com_lic_no1);
        lis2 = (EditText) findViewById(R.id.com_lic_no2);
        lis3 = (AutoCompleteTextView) findViewById(R.id.com_lic_no3);

        vehi1 = (EditText) findViewById(R.id.com_vehi_no);



        /*vehi1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                String t3=lis2.getText().toString();
                if(t3!=null){

                    HashMap<String, String> vehihash = new HashMap<String, String>();
                    //String encodeImagez="cool";

                   vehihash.put("tag","licen");
                    vehihash.put("license",t3);
                    checkvalidation(vehihash);



                }

            }
        });*/


        Intent intent = getIntent();
     clearty=(Button)findViewById(R.id.clearqw);
        clearty.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                spinner1.setText("");
                vehi1.setText("");
                lis2.setText("");
                lis1.setText("");
                lis3.setText("");
            }
        });
        df = intent.getStringExtra("MyData");
        dff = intent.getStringExtra("MyData1");
        try {
            DataB bn = new DataB(Mainclass.this);
            bn.open();
            idu = bn.getName(1);
            bn.close();
            ;
        }catch (Exception e){
            e.printStackTrace();
        }

     //   Toast.makeText(Mainclass.this,idu,Toast.LENGTH_SHORT).show();

        // vehi1.setText(df);
        // lis2.setText(dff);
        cameraclick = (ImageView) findViewById(R.id.com_camera);

        photocr = new CameraPhoto(getApplicationContext());
        viewclick = (ImageView) findViewById(R.id.com_view);

        ArrayAdapter<String> adapter = new ArrayAdapter<String>(this,android.R.layout.select_dialog_singlechoice, offense);
        ArrayAdapter<String> adapter1 = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, regis);
        ArrayAdapter<String> adapter2 = new ArrayAdapter<String>(this,android.R.layout.simple_dropdown_item_1line, regis1);

        lis1.setThreshold(1);
        lis3.setThreshold(1);
        lis1.setAdapter(adapter1);
        lis3.setAdapter(adapter2);
        spinner1 = (AutoCompleteTextView) findViewById(R.id.com_offn);
        /*List<String> listoffense = new ArrayList(Arrays.asList(offense));

        offenseAdapter = new ArrayAdapter<String>(Mainclass.this,
                R.layout.spinner_item, listoffense);
        offenseAdapter.setDropDownViewResource(R.layout.spinner_compo);*/
       spinner1.setThreshold(1);
        //Set the adapter
        spinner1.setAdapter(adapter);
       // spinner1.setAdapter(offenseAdapter);
        send = (Button) findViewById(R.id.com_send);
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                // The next two lines tell the new client that “this” current class will handle connection stuff

                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                       //fourth line adds the LocationServices API endpoint from GooglePlayServices
                .addApi(LocationServices.API)
                .build();


        // Create the LocationRequest object
        mLocationRequest = LocationRequest.create()
                .setPriority(LocationRequest.PRIORITY_HIGH_ACCURACY)
                .setInterval(10 * 1000)        // 10 seconds, in milliseconds
                .setFastestInterval(1 * 1000); //
        mGoogleApiClient.connect();

        viewclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {


                ImageViewPopUpHelper.enablePopUpOnClick(Mainclass.this, viewclick);

            }
        });
        cameraclick.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
               /*  file_path = Environment.getExternalStorageDirectory().getAbsolutePath() +
                        "/Qrcode_Fast";
                File dir = new File(file_path);
                if(!dir.exists())
                    dir.mkdirs();

                String format = new SimpleDateFormat("yyyyMMddHHmmss",
                        java.util.Locale.getDefault()).format(new Date());

                 file = new File(dir, format + ".jpg");

               // outputFileUri = Uri.fromFile(file);
                 */
                cameracall();
            }
        });

        send.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

               String encodeImage="no pic";
                String qw1=lis1.getText().toString();
                String qw2=lis2.getText().toString();
                String qw3=lis3.getText().toString();



                  String  licenseno1 =qw1+"/"+qw2+"/"+qw3;





                String vehicleno1 = vehi1.getText().toString();
                String seoffense = spinner1.getText().toString();
                Bitmap bitmap = null;
                if(picString!=null){
                try {
                    bitmap = ImageLoader.init().from(picString).requestSize(200, 200).getBitmap();
                    encodeImage = ImageBase64.encode(bitmap);
                } catch (FileNotFoundException e1) {
                    e1.printStackTrace();
                }}

               if(licenseno1.trim().equals("")||vehicleno1.trim().equals("")||seoffense==null)
               {
                   Toast.makeText(Mainclass.this,"null value",Toast.LENGTH_SHORT).show();
               }
                else{

                   if(isConnectedWifi(Mainclass.this)||isConnectedMobile(Mainclass.this))
                   {
                       if(isConnected(Mainclass.this))
                          // layoutRipple1.setOnClickListener(new OnClickListener() {
                           {

                      try {
                    mGoogleApiClient.connect();


                   // Log.d(TAG, encodeImage);


                    HashMap<String, String> hashMap = new HashMap<String, String>();
                    //String encodeImagez="cool";

                    hashMap.put("images1", encodeImage);
                    hashMap.put("licno1", licenseno1);
                    hashMap.put("vehino1", vehicleno1);
                    hashMap.put("offensno1", seoffense);
                          hashMap.put("keyno",idu);
                    hashMap.put("latitude1",latitudek);
                    PostResponseAsyncTask task = new PostResponseAsyncTask(Mainclass.this, hashMap, new AsyncResponse() {
                        @Override
                        public void processFinish(String s) {


                            Toast.makeText(Mainclass.this, s, Toast.LENGTH_SHORT).show();
                        }

                    });
                    task.execute("http://rto.venturesoftwares.org/lalibase64.php");

                    task.setEachExceptionsHandler(new EachExceptionsHandler() {
                        @Override
                        public void handleIOException(IOException e) {
                            Toast.makeText(Mainclass.this, "time out", Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void handleMalformedURLException(MalformedURLException e) {
                            Toast.makeText(Mainclass.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void handleProtocolException(ProtocolException e) {
                            Toast.makeText(Mainclass.this, e.toString(), Toast.LENGTH_SHORT).show();
                        }

                        @Override
                        public void handleUnsupportedEncodingException(UnsupportedEncodingException e) {
                            Toast.makeText(Mainclass.this, "handle pro", Toast.LENGTH_SHORT).show();
                        }
                    });


                }  finally {
                    {
                        viewclick.invalidate();
                        viewclick.setImageBitmap(null);
                    }
                }

                // vehicle=vehi1.getText().toString();
                //license=lis2.getText().toString();

                //Toast.makeText(Mainclass.this,encodeImage1,Toast.LENGTH_SHORT).show();
                //new CheckAvialability().execute();
            }else{
                           Toast.makeText(Mainclass.this,"no connection",Toast.LENGTH_LONG).show();

                       }



            ///
            }else{
                       Toast.makeText(Mainclass.this,"turn on wifi",Toast.LENGTH_LONG).show();

                   }


               }

               }


        });

    }

       private String[] checkvalidation(HashMap<String, String> vehihash) {



           PostResponseAsyncTask task = new PostResponseAsyncTask(Mainclass.this, vehihash, new AsyncResponse() {
               @Override
               public void processFinish(String s) {


                   Toast.makeText(Mainclass.this, s, Toast.LENGTH_SHORT).show();
                   validate[0] =s;
               }

           });
           task.execute("http://rto.venturesoftwares.org/validationcheck.php");

           task.setEachExceptionsHandler(new EachExceptionsHandler() {
               @Override
               public void handleIOException(IOException e) {
                   Toast.makeText(Mainclass.this, "time out", Toast.LENGTH_SHORT).show();
               }

               @Override
               public void handleMalformedURLException(MalformedURLException e) {
                   Toast.makeText(Mainclass.this, e.toString(), Toast.LENGTH_SHORT).show();
               }

               @Override
               public void handleProtocolException(ProtocolException e) {
                   Toast.makeText(Mainclass.this, e.toString(), Toast.LENGTH_SHORT).show();
               }

               @Override
               public void handleUnsupportedEncodingException(UnsupportedEncodingException e) {
                   Toast.makeText(Mainclass.this, "handle pro", Toast.LENGTH_SHORT).show();
               }
           });
   return  validate;

          }


    @Override
    public void onConnected(Bundle bundle) {
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            // TODO: Consider calling
            //    ActivityCompat#requestPermissions
            // here to request the missing permissions, and then overriding
            //   public void onRequestPermissionsResult(int requestCode, String[] permissions,
            //                                          int[] grantResults)
            // to handle the case where the user grants the permission. See the documentation
            // for ActivityCompat#requestPermissions for more details.
            return;
        }
        Location location = LocationServices.FusedLocationApi.getLastLocation(mGoogleApiClient);

        if (location == null) {
            LocationServices.FusedLocationApi.requestLocationUpdates(mGoogleApiClient, mLocationRequest, (com.google.android.gms.location.LocationListener) this);

        } else {
            //If everything went fine lets get latitude and longitude
            currentLatitude = location.getLatitude();
            currentLongitude = location.getLongitude();
            latitudek=""+currentLatitude+ "," +currentLongitude;
            //Toast.makeText(Mainclass.this,latitudek,Toast.LENGTH_SHORT).show();
           return;
        }
    }

    public void onConnectionSuspended(int i) {

    }


    public void onConnectionFailed(ConnectionResult connectionResult) {
            /*
             * Google Play services can resolve some errors it detects.
             * If the error has a resolution, try sending an Intent to
             * start a Google Play services activity that can resolve
             * error.
             */
        if (connectionResult.hasResolution()) {
            try {
                // Start an Activity that tries to resolve the error
                connectionResult.startResolutionForResult(this, CONNECTION_FAILURE_RESOLUTION_REQUEST);
                    /*
                     * Thrown if Google Play services canceled the original
                     * PendingIntent
                     */
            } catch (IntentSender.SendIntentException e) {
                // Log the error
                e.printStackTrace();
            }
        } else {
                /*
                 * If no resolution is available, display a dialog to the
                 * user with the error.
                 */
            //Log.e("Error", "Location services connection failed with code " + connectionResult.getErrorCode());
        }
    }



    private void cameracall() {

        try {
            startActivityForResult(photocr.takePhotoIntent(),CAMERA_DATAD);
            photocr.addToGallery();


        } catch (IOException e1) {
            e1.printStackTrace();
        }




  /*try {
     Intent cameraIntent = new Intent(
              android.provider.MediaStore.ACTION_IMAGE_CAPTURE);
      outputFileUri = Uri.fromFile(file);


     // cameraIntent.putExtra("return-data", true);
      try{
 cameraIntent.putExtra(android.provider.MediaStore.EXTRA_OUTPUT, outputFileUri);}catch (Exception e){Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();}
    //  BitmapFactory.Options options = new BitmapFactory.Options();

      // downsizing image as it throws OutOfMemory Exception for larger
      // images
      //options.inSampleSize = 8;


      startActivityForResult(cameraIntent,CAMERA_DATAD);

   //   Intent i=new Intent(android.provider.MediaStore.ACTION_IMAGE_CAPTURE);

      //
     //
     // startActivityForResult(i,CAMERA_DATA);



  }catch (Exception e){


      Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();
  }*/



    }

   /* public String getStringImage(Bitmap bmp){
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        bmp.compress(Bitmap.CompressFormat.PNG, 100, baos);
        byte[] imageBytes = baos.toByteArray();
        String encodedImage = Base64.encodeToString(imageBytes, Base64.DEFAULT);
        return encodedImage;
    }*/


    public  void onActivityResult(int requestCode, int resultCode, Intent data) {


        if (resultCode == Activity.RESULT_OK)
        {


            switch (requestCode) {

                case CAMERA_DATAD:


                    Bitmap bitmap=null;
                    try {

                        String photoPath=photocr.getPhotoPath();
                        picString=photoPath;
                        bitmap= ImageLoader.init().from(photoPath).requestSize(512,512).getBitmap();
                        // try{
                        final Bitmap finalBitmap = bitmap;
                        //Thread set=new Thread(){
                        //  public void run(){
                        viewclick.setImageBitmap(getRealoaded(bitmap,90));
                        // }
                        // };
                        //}catch (Exception e){
                        //  Toast.makeText(Mainclass.this,"error",Toast.LENGTH_SHORT).show();
                    }
                    catch (FileNotFoundException e1) {
                        e1.printStackTrace();
                    }



                      /*  Bitmap bitmap = BitmapFactory.decodeFile(outputFileUri.getPath());
                       Bitmap bitmaps = resizeBitMapImage1(file_path, 800, 600);
                       String file_path1 = Environment.getExternalStorageDirectory().getAbsolutePath() +
                               "/Qrcode_Fast1";
                       File dir1 = new File(file_path1);
                       if(!dir1.exists())
                           dir1.mkdirs();
                       String format1 = new SimpleDateFormat("yyyyMMddHHmmss",
                               java.util.Locale.getDefault()).format(new Date());

                     File  file1 = new File(dir1, format1 + ".png");
                       FileOutputStream fOut1;
                       try {
                           fOut1 = new FileOutputStream(file1);
                           bitmap.compress(Bitmap.CompressFormat.PNG, 85, fOut1);
                           fOut1.flush();
                           fOut1.close();
                       } catch (Exception e) {
                           e.printStackTrace();
                       }


                       viewclick.setImageBitmap(bitmap);
                       encodeImage1=getStringImage(bitmap);

                   }catch (Exception e){ Toast.makeText(this, e.toString(), Toast.LENGTH_SHORT).show();}
                           // Toast.makeText(this, "okupload", Toast.LENGTH_SHORT).show();*/
                    break;






            }
        }else{Toast.makeText(this,e.toString(),Toast.LENGTH_SHORT).show();}
    }

    private Bitmap getRealoaded(Bitmap source,float angle) {

        Matrix matrix=new Matrix();
        matrix.postRotate(angle);
        Bitmap bitmap1=Bitmap.createBitmap(source,0,0,source.getWidth(),source.getHeight(),matrix,true);

        return bitmap1;
    }

    @Override
    public void onLocationChanged(Location location) {

    }

    @Override
    public void onStatusChanged(String provider, int status, Bundle extras) {

    }

    @Override
    public void onProviderEnabled(String provider) {

    }

    @Override
    public void onProviderDisabled(String provider) {

    }

    /*public static Bitmap resizeBitMapImage1(String filePath, int targetWidth, int targetHeight) {
        Bitmap bitMapImage = null;
        try {
            BitmapFactory.Options options = new BitmapFactory.Options();
            options.inJustDecodeBounds = true;
            BitmapFactory.decodeFile(filePath, options);
            double sampleSize = 0;
            Boolean scaleByHeight = Math.abs(options.outHeight - targetHeight) >= Math.abs(options.outWidth
                    - targetWidth);
            if (options.outHeight * options.outWidth * 2 >= 1638) {
                sampleSize = scaleByHeight ? options.outHeight / targetHeight : options.outWidth / targetWidth;
                sampleSize = (int) Math.pow(2d, Math.floor(Math.log(sampleSize) / Math.log(2d)));
            }
            options.inJustDecodeBounds = false;
            options.inTempStorage = new byte[128];
            while (true) {
                try {
                    options.inSampleSize = (int) sampleSize;
                    bitMapImage = BitmapFactory.decodeFile(filePath, options);
                    break;
                } catch (Exception ex) {
                    try {
                        sampleSize = sampleSize * 2;
                    } catch (Exception ex1) {

                    }
                }
            }
        } catch (Exception ex) {

        }
        return bitMapImage;
    }
          */

}
