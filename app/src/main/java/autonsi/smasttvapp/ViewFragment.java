package autonsi.smasttvapp;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.DialogInterface;
import android.graphics.Paint;
import android.os.AsyncTask;
import android.os.Bundle;


import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.RelativeLayout;
import android.widget.TextSwitcher;
import android.widget.TextView;
import android.widget.Toast;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.text.DecimalFormat;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.TimeUnit;

import io.reactivex.Observable;
import io.reactivex.Observer;
import io.reactivex.disposables.Disposable;
import io.reactivex.subjects.Subject;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link ViewFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class ViewFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    Boolean check = true;
    Handler handler = new Handler();

    TextView defeQty, targetQty, actualQty, defHour, actualHour, targetHour, eff;
    String webUrl = Url.webUrl;
    ProgressBar progressBar;
    RelativeLayout rl_actual, rl_defe;
    String vitri_bam = "";
    int numActual;
    int numDefective;
    DecimalFormat formatter = new DecimalFormat("#,###,###");


    public ViewFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment ViewFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static ViewFragment newInstance(String param1, String param2) {
        ViewFragment fragment = new ViewFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_view, container, false);
        defeQty = view.findViewById(R.id.defeQty);
        targetQty = view.findViewById(R.id.targetQty);
        actualQty = view.findViewById(R.id.actualQty);
        defHour = view.findViewById(R.id.defHour);
        actualHour = view.findViewById(R.id.actualHour);
        targetHour = view.findViewById(R.id.targetHour);
        eff = view.findViewById(R.id.eff);
        progressBar = view.findViewById(R.id.progressBar);
        rl_actual = view.findViewById(R.id.rl_actual);
        rl_defe = view.findViewById(R.id.rl_defec);
        rl_defe.setEnabled(false);

//        MainActivity.tvTime.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });


        rl_actual.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                check = false;
                handler.removeCallbacks(r);
                inputnum("Actual");
                vitri_bam = "APL";
                return true;
            }
        });
        rl_actual.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                check = false;
                handler.removeCallbacks(r);
                rl_actual.setEnabled(false);
                vitri_bam = "AP";
                sendDataCounting(1);
                DelayClick(rl_actual);

            }
        });
        rl_defe.setOnLongClickListener(new View.OnLongClickListener() {
            @Override
            public boolean onLongClick(View view) {
                inputnum("Defective");
                check = false;
                handler.removeCallbacks(r);
                vitri_bam = "DPL";
                return true;
            }
        });
        rl_defe.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                rl_defe.setEnabled(false);
                check = false;
                handler.removeCallbacks(r);
                vitri_bam = "DP";
                sendDataCounting(1);
                DelayClick(rl_defe);

            }
        });

        starthandler();
        thread.start();
        return view;
    }


    Thread thread = new Thread() {

        @Override
        public void run() {
            try {
                while (!thread.isInterrupted()) {
                    Thread.sleep(300);
                    getActivity().runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            setData();
                        }
                    });
                }
            } catch (InterruptedException e) {
            }
        }
    };



    private void setData() {
        numActual = MainActivity.numActual;
        numDefective = MainActivity.numDefective;
        targetQty.setText(formatter.format(MainActivity.targetQty));
        defHour.setText(formatter.format(MainActivity.defHour) + "/Hr");
        actualHour.setText(formatter.format(MainActivity.actualHour) + "/Hr");
        targetHour.setText(formatter.format(MainActivity.targetHour) + "/Hr");
        eff.setText("EFFICIENCY " + formatter.format(MainActivity.eff) + "%");
        progressBar.setProgress(MainActivity.eff);
        defeQty.setText(formatter.format(numDefective));
        actualQty.setText(formatter.format(numActual));
    }

    private void inputnum(final String key) {
        AlertDialog.Builder builder = new AlertDialog.Builder(getActivity());
        View viewInflated = LayoutInflater.from(getActivity()).inflate(R.layout.number_input_layout, null);
        builder.setTitle("Input " + key);
        final EditText input = (EditText) viewInflated.findViewById(R.id.input);
        builder.setView(viewInflated);
        final TextView tv_title = viewInflated.findViewById(R.id.tv_title);
        if (key.equals("Actual")) {
            int tong = numActual;
            String title = key + " new = " + formatter.format(numActual) + " + " + "0" + " = " + formatter.format(tong);
            tv_title.setText(title);
        } else if (key.equals("Defective")) {
            int tong = numDefective;
            String title = key + " new = " + formatter.format(numDefective) + " + " + "0" + " = " + formatter.format(tong);
            tv_title.setText(title);
        }


        input.addTextChangedListener(new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence charSequence, int i, int i1, int i2) {

            }

            @Override
            public void onTextChanged(CharSequence charSequence, int i, int i1, int i2) {

                int numinput2 = 0;
                if (input.getText().toString().length() == 0) {
                    numinput2 = 0;
                } else {
                    numinput2 = Integer.parseInt(input.getText().toString());
                }
                int tong = 0;
                if (key.equals("Actual")) {
                    tong = numActual + numinput2;
                    String title = key + " new = " + formatter.format(numActual) + " + " + formatter.format(numinput2) + " = " + formatter.format(tong);
                    tv_title.setText(title);
                } else if (key.equals("Defective")) {
                    tong = numDefective + numinput2;
                    String title = key + " new = " + formatter.format(numDefective) + " + " + formatter.format(numinput2) + " = " + formatter.format(tong);
                    tv_title.setText(title);
                }

            }

            @Override
            public void afterTextChanged(Editable editable) {

            }
        });


        builder.setPositiveButton(android.R.string.ok, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                check = true;
                starthandler();
                if (input.getText().toString().length() == 0 || input.getText().toString().equals("0")) {
                    return;
                } //else if (key.equals("Actual")) {
                //giatri_Input = Integer.parseInt(input.getText().toString().trim());
                sendDataCounting(Integer.parseInt(input.getText().toString().trim()));
                dialog.dismiss();
//                } else if (key.equals("Defective")) {
//                    giatri_Input=Integer.parseInt(input.getText().toString().trim());
//                    sendDataCounting(Integer.parseInt(input.getText().toString().trim()));
//                    dialog.dismiss();
//                }
            }
        });

        builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
            @Override
            public void onClick(DialogInterface dialog, int which) {
                check = true;
                starthandler();
                dialog.cancel();

            }
        });
        builder.show();
    }

    private void sendDataCounting(int value) {
        if (vitri_bam.equals("AP")) {
            new sendDataCount().execute(webUrl + "plan/updateActualToday?id=" + MainActivity.id + "&actual_qty=" + value + "&defect_qty=" + 0);
        } else if (vitri_bam.equals("DP")) {
            new sendDataCount().execute(webUrl + "plan/updateActualToday?id=" + MainActivity.id + "&actual_qty=" + 0 + "&defect_qty=" + value);
        } else if (vitri_bam.equals("APL")) {
            new sendDataCount().execute(webUrl + "plan/updateActualToday?id=" + MainActivity.id + "&actual_qty=" + value + "&defect_qty=" + 0);
        } else if (vitri_bam.equals("DPL")) {
            new sendDataCount().execute(webUrl + "plan/updateActualToday?id=" + MainActivity.id + "&actual_qty=" + 0 + "&defect_qty=" + value);
        }
    }

    private class sendDataCount extends AsyncTask<String, Void, String> {
        @Override
        protected String doInBackground(String... strings) {
            return Url.NoiDung_Tu_URL(strings[0]);
        }

        @Override
        protected void onPostExecute(String s) {
            super.onPostExecute(s);
            check = true;
            starthandler();
            try {
                JSONObject jsonObject = new JSONObject(s);
                if (jsonObject.getBoolean("result")) {
                    if (vitri_bam.equals("AP")) {
                        numActual = jsonObject.getInt("valueActual");
                        //numActual += 1;
                        //setAnimation(tv_actual, numActual + "");
                    } else if (vitri_bam.equals("DP")) {
                        //numDefective += 1;
                        numDefective = jsonObject.getInt("valueDefect");
                        //setAnimation(tv_defective, numDefective + "");
                    } else if (vitri_bam.equals("APL")) {
                        //numActual += giatri_Input;
                        numActual = jsonObject.getInt("valueActual");
                        //setAnimation(tv_actual, numActual + "");
                    } else if (vitri_bam.equals("DPL")) {
                        //numDefective += giatri_Input;
                        numDefective = jsonObject.getInt("valueDefect");
                        //setAnimation(tv_defective, numDefective + "");
                    }


                } else {
                    AlerError.Baoloi(jsonObject.getString("message"), getActivity());
                    return;
                }


            } catch (JSONException e) {
                e.printStackTrace();
                AlerError.Baoloi("Could not connect to server", getActivity());
            }
        }

    }

    private void DelayClick(final RelativeLayout rl) {
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                rl.setEnabled(true);
            }
        }, 500);
    }


    public void starthandler() {
        handler.postDelayed(r, MainActivity.timesetting*1000);
    }

    Runnable r = new Runnable() {
        @Override
        public void run() {
            if (check) {
                thread.interrupt();
                showFragmentB();
            } else {
                Log.e("LL", check + "");
                starthandler();
            }
        }
    };

    private void showFragmentB() {
        handler.removeCallbacks(r);
        DetailFragment detailFragment;
        FragmentManager fragmentManager = this.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.slide_in, R.animator.slide_out);
        detailFragment = new DetailFragment();
        fragmentTransaction.replace(R.id.framContent, detailFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }

    @Override
    public void onDestroy() {

        handler.removeCallbacks(r);
        super.onDestroy();
    }
}