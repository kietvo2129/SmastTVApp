package autonsi.smasttvapp;

import androidx.appcompat.app.AppCompatActivity;
import androidx.cardview.widget.CardView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.content.SharedPreferences;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.Editable;
import android.text.TextUtils;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.WindowManager;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.IOException;
import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;
import java.util.Observer;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    ViewFragment viewFragment;
    String webUrl = Url.webUrl;
    ArrayAdapter<String> arrayAdapter;
    public static TextView tvTime, lineName;
    CardView cv_linename;
    ArrayList<String> arrayLineNoAdapter;
    public static String line_no = "";
    public static String id = "";
    Handler mHandler = new Handler();
    public static int numActual = 0;
    public static int numDefective = 0;
    public static int targetQty = 0, defHour = 0, actualHour = 0, targetHour = 0, eff = 0;
    String line_no_d = "";
    DecimalFormat formatter = new DecimalFormat("#,###,###");

    public static ArrayList<DetailDataMaster> detailDataMaster;
    ImageView setting;
    public static int timesetting = 0;
    SharedPreferences sharedPreferences; // lư thông tin check box

    public static ArrayList<NoticeMaster> noticeMasterArrayList;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        tvTime = findViewById(R.id.tvTime);
        lineName = findViewById(R.id.lineName);
        cv_linename = findViewById(R.id.QQQ);
        setting = findViewById(R.id.setting);

        sharedPreferences = getSharedPreferences("timesetting", MODE_PRIVATE);
        timesetting = sharedPreferences.getInt("giay", 3);

        setting.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                inputtime();
            }
        });


        // gettime();

        getLineName();

        getWindow().addFlags(WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);

        cv_linename.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                mHandler.removeCallbacks(runnable);
                getLineName();
                showLineName();

            }
        });
        FragmentManager fragmentManager = this.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        viewFragment = new ViewFragment();
        fragmentTransaction.replace(R.id.framContent, viewFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }


    private void inputtime() {
        AlertDialog.Builder builder = new AlertDialog.Builder(MainActivity.this);
        View viewInflated = LayoutInflater.from(MainActivity.this).inflate(R.layout.number_input_layout, null);
        builder.setTitle("Input time setting (Minimum 1s)");
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        builder.setView(viewInflated);


        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {

                if (input.getText().toString().length() == 0 || input.getText().toString().equals("0")) {
                    input.setError("Input here!");
                } else {
                    timesetting = Integer.parseInt(input.getText().toString());
                    SharedPreferences.Editor editor = sharedPreferences.edit();
                    editor.putInt("giay", timesetting);
                    editor.commit();
                    dialog.dismiss();
                }
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.cancel();

            }
        });
        builder.show();
    }

    final Runnable runnable = new Runnable() {
        @Override
        public void run() {
            getData();
            mHandler.postDelayed(this, 1000);
            SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
            Date myDate = new Date();
            String filename = timeStampFormat.format(myDate);
            tvTime.setText(filename);
        }
    };

    private void getData() {
//        new Timer().scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//          }
//        }, 0, 1000);
        new getData().execute(webUrl + "Display/GetData_Display1?line_no=" + line_no);
        Log.d("getData", webUrl + "Display/GetData_Display1?line_no=" + line_no);

        new getDataNotice().execute(webUrl + "Display/GetDataDisplay3_issue?line_no=" + line_no);
        Log.d("getDataNotice", webUrl + "Display/GetDataDisplay3_issue?line_no=" + line_no);

    }

    private class getDataNotice extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return Url.NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            noticeMasterArrayList = new ArrayList<>();
            String id, code, name, content, waiting_time, line_no, line_name,
                    product_code, product_name, work_date;
            ArrayList<String> arrimage = new ArrayList<>();
            try {
                JSONObject jsonObject = new JSONObject(s);

                if (!jsonObject.getBoolean("resut")) {
                    return;
                }
                JSONObject Issue_Info = jsonObject.getJSONObject("Issue_Info");

                id = Issue_Info.getString("id");
                code = Issue_Info.getString("code");
                name = Issue_Info.getString("name");
                content = Issue_Info.getString("content");
                waiting_time = Issue_Info.getString("waiting_time");
                line_no = Issue_Info.getString("line_no");
                line_name = Issue_Info.getString("line_name");
                product_code = Issue_Info.getString("product_code");
                product_name = Issue_Info.getString("product_name");
                work_date = Issue_Info.getString("work_date");
                JSONArray Issue_Img = jsonObject.getJSONArray("Issue_Img");
                if (Issue_Img.length() != 0) {
                    for (int i = 0; i < Issue_Img.length(); i++) {
                        JSONObject jsonObject1 = Issue_Img.getJSONObject(i);
                        String image = jsonObject1.getString("image");
                        arrimage.add(image);
                    }
                }
                noticeMasterArrayList.add(new NoticeMaster(id, code, name, content, waiting_time, line_no, line_name,
                        product_code, product_name, work_date,arrimage));
            } catch (JSONException e) {
                e.printStackTrace();
            }
        }

    }

    private class getData extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return Url.NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                JSONObject jsonObject = new JSONObject(s);

                if (!jsonObject.getBoolean("result")) {
                    numActual = 0;
                    numDefective = 0;
                    //AlerError.Baoloi(jsonObject.getString("message"), MainActivity.this);
                    return;
                }
                JSONObject Data = jsonObject.getJSONObject("Data");
                id = Data.getString("id");
                targetQty = Data.getInt("Target_qty");
                defHour = Data.getInt("Defect_hour");
                actualHour = Data.getInt("Actual_hour");
                targetHour = Data.getInt("Target_hour");
                eff = Data.getInt("Efficiency");
                numActual = Data.getInt("Actual_qty");
                numDefective = Data.getInt("Defect_qty");
                line_no_d = Data.getString("line_no_d");

                getDetail();

            } catch (JSONException e) {
                e.printStackTrace();
                // AlerError.Baoloi("Could not connect to server", MainActivity.this);
            }
        }

    }

    private void getDetail() {
        new getDetail().execute(webUrl + "Display/getdaytime?line_no_d=" + line_no_d);
        Log.d("getDetail", webUrl + "Display/getdaytime?line_no_d=" + line_no_d);


    }

    private class getDetail extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return Url.NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);

            detailDataMaster = new ArrayList<>();
            String ldt, work_ymd, work_time, start_time1, start_time, end_time, end_time1, stt;
            int prod_qty, done_qty, refer_qty, defect_qty, efficiency;
            try {
                JSONArray jsonArray = new JSONArray(s);
                if (jsonArray.length() == 0) {
                    AlerError.Baoloi("No Data", MainActivity.this);
                    return;
                }

                for (int i = 0; i < jsonArray.length(); i++) {

                    JSONObject jsonObject = jsonArray.getJSONObject(i);

                    ldt = jsonObject.getString("ldt");
                    prod_qty = jsonObject.getInt("prod_qty");
                    done_qty = jsonObject.getInt("done_qty");
                    refer_qty = jsonObject.getInt("refer_qty");
                    defect_qty = jsonObject.getInt("defect_qty");
                    efficiency = jsonObject.getInt("efficiency");
                    work_ymd = jsonObject.getString("work_ymd");
                    work_time = jsonObject.getString("work_time");
                    start_time1 = jsonObject.getString("start_time1");
                    start_time = jsonObject.getString("start_time");
                    end_time = jsonObject.getString("end_time");
                    end_time1 = jsonObject.getString("end_time1");
                    stt = jsonObject.getString("stt");

                    detailDataMaster.add(new DetailDataMaster(ldt, work_ymd
                            , work_time, start_time1, start_time, end_time, end_time1, stt, prod_qty, done_qty, refer_qty, defect_qty, efficiency));
                }

                checkdetail();
            } catch (JSONException e) {
                e.printStackTrace();
                // AlerError.Baoloi("Could not connect to server", MainActivity.this);
            }
        }
    }

    private void checkdetail() {
        Collections.sort(detailDataMaster, new Comparator<DetailDataMaster>() {
            @Override
            public int compare(DetailDataMaster fruit2, DetailDataMaster fruit1) {
                return fruit2.start_time.compareTo(fruit1.start_time);
            }
        });

        for (int i = 0; i < 5; i++) {
            if (detailDataMaster.size() < 5) {
                detailDataMaster.add(new DetailDataMaster("", "", "",
                        "---", "", "", "", "", 0,
                        0, 0, 0, 0));
            }
        }
    }


//    private void gettime() {
//        tvTime = findViewById(R.id.tvTime);
//        new Timer().scheduleAtFixedRate(new TimerTask() {
//            @Override
//            public void run() {
//                runOnUiThread(new Runnable() {
//
//                    @Override
//                    public void run() {
//                        SimpleDateFormat timeStampFormat = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
//                        Date myDate = new Date();
//                        String filename = timeStampFormat.format(myDate);
//                        tvTime.setText(filename);
//                    }
//                });
//
//            }
//        }, 0, 1000);
//    }

    @Override
    protected void onDestroy() {
        getWindow().clearFlags(android.view.WindowManager.LayoutParams.FLAG_KEEP_SCREEN_ON);
        super.onDestroy();
    }

    private void getLineName() {
        new getlineName().execute(webUrl + "Display/GetLineno");
    }

    private class getlineName extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return Url.NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            try {
                arrayLineNoAdapter = new ArrayList<>();
                arrayAdapter = new ArrayAdapter<String>(MainActivity.this, android.R.layout.select_dialog_singlechoice);
                JSONArray jsonArray = new JSONArray(s);
                if (jsonArray == null || jsonArray.length() == 0) {
                    AlerError.Baoloi("\"WO\" does not exist today. Please, insert \"Plan Master\" for today.", MainActivity.this);
                    return;
                }
                for (int i = 0; i < jsonArray.length(); i++) {
                    JSONObject jsonObject = jsonArray.getJSONObject(i);
                    String line_nm = jsonObject.getString("line_nm");
                    String line_no = jsonObject.getString("line_no");
                    arrayAdapter.add(line_nm);
                    arrayLineNoAdapter.add(line_no);
                }
                lineName.setText(arrayAdapter.getItem(0));
                line_no = arrayLineNoAdapter.get(0);

                mHandler.post(runnable);

            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", MainActivity.this);
            }
        }

    }

    private void showLineName() {

        AlertDialog.Builder builderSingle = new AlertDialog.Builder(MainActivity.this);
        builderSingle.setTitle("Select One Line:");
        builderSingle.setNegativeButton("cancel", new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                dialog.dismiss();
            }
        });

        builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int i) {
                lineName.setText(arrayAdapter.getItem(i));
                line_no = arrayLineNoAdapter.get(i);
                dialog.dismiss();
            }
        });
        builderSingle.show();
    }
}
