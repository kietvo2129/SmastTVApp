package autonsi.smasttvapp;

import android.annotation.SuppressLint;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;


import android.os.Handler;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ProgressBar;
import android.widget.TextView;

import java.text.DecimalFormat;
import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link DetailFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class DetailFragment extends Fragment {

    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    TextView defeQty, targetQty, actualQty,eff;
    TextView time1,time2,time3,time4,time5;
    TextView target1,target2,target3,target4,target5;
    TextView actual1,actual2,actual3,actual4,actual5;
    TextView defe1,defe2,defe3,defe4,defe5;
    TextView eff1,eff2,eff3,eff4,eff5;
    final Handler handler = new Handler();
    ProgressBar progressBar;
    DecimalFormat formatter = new DecimalFormat("#,###,###");
    public static ArrayList<DetailDataMaster> detailDataMaster;
    public DetailFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment DetailFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static DetailFragment newInstance(String param1, String param2) {
        DetailFragment fragment = new DetailFragment();
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
        View view = inflater.inflate(R.layout.fragment_detail, container, false);

        defeQty = view.findViewById(R.id.defeQty);
        targetQty = view.findViewById(R.id.targetQty);
        actualQty = view.findViewById(R.id.actualQty);
        eff = view.findViewById(R.id.eff);
        progressBar = view.findViewById(R.id.progressBar);


        time1 = view.findViewById(R.id.time1);
        time2 = view.findViewById(R.id.time2);
        time3 = view.findViewById(R.id.time3);
        time4 = view.findViewById(R.id.time4);
        time5 = view.findViewById(R.id.time5);

        target1 = view.findViewById(R.id.target1);
        target2 = view.findViewById(R.id.target2);
        target3 = view.findViewById(R.id.target3);
        target4 = view.findViewById(R.id.target4);
        target5 = view.findViewById(R.id.target5);

        actual1 = view.findViewById(R.id.actual1);
        actual2 = view.findViewById(R.id.actual2);
        actual3 = view.findViewById(R.id.actual3);
        actual4 = view.findViewById(R.id.actual4);
        actual5 = view.findViewById(R.id.actual5);


        defe1 = view.findViewById(R.id.defe1);
        defe2 = view.findViewById(R.id.defe2);
        defe3 = view.findViewById(R.id.defe3);
        defe4 = view.findViewById(R.id.defe4);
        defe5 = view.findViewById(R.id.defe5);

        eff1 = view.findViewById(R.id.eff1);
        eff2 = view.findViewById(R.id.eff2);
        eff3 = view.findViewById(R.id.eff3);
        eff4 = view.findViewById(R.id.eff4);
        eff5 = view.findViewById(R.id.eff5);

//        MainActivity.tvTime.addTextChangedListener(new TextWatcher() {
//            @Override
//            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
//
//            }
//
//            @Override
//            public void onTextChanged(CharSequence s, int start, int before, int count) {
//                setData();
//            }
//
//            @Override
//            public void afterTextChanged(Editable s) {
//            }
//        });





        starthandler();

//        // Inflate the layout for this fragment
//        final Handler handler = new Handler();
//        handler.postDelayed(new Runnable() {
//            @Override
//            public void run() {
//                showFragmentB();
//            }
//        }, MainActivity.timesetting*1000);

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


    public void starthandler() {
        handler.postDelayed(r, MainActivity.timesetting*1000);
    }

    Runnable r = new Runnable() {
        @Override
        public void run() {
            thread.interrupt();
            showFragmentB();

        }
    };
    private void setData() {
        detailDataMaster = MainActivity.detailDataMaster;
        targetQty.setText(formatter.format(MainActivity.targetQty));
        eff.setText("EFFICIENCY " + formatter.format(MainActivity.eff) + "%");
        progressBar.setProgress(MainActivity.eff);
        defeQty.setText(formatter.format(MainActivity.numDefective));
        actualQty.setText(formatter.format(MainActivity.numActual));

        time1.setText(detailDataMaster.get(0).start_time1);
        time2.setText(detailDataMaster.get(1).start_time1);
        time3.setText(detailDataMaster.get(2).start_time1);
        time4.setText(detailDataMaster.get(3).start_time1);
        time5.setText(detailDataMaster.get(4).start_time1);

        target1.setText(formatter.format(detailDataMaster.get(0).prod_qty));
        target2.setText(formatter.format(detailDataMaster.get(1).prod_qty));
        target3.setText(formatter.format(detailDataMaster.get(2).prod_qty));
        target4.setText(formatter.format(detailDataMaster.get(3).prod_qty));
        target5.setText(formatter.format(detailDataMaster.get(4).prod_qty));

        actual1.setText(formatter.format(detailDataMaster.get(0).done_qty));
        actual2.setText(formatter.format(detailDataMaster.get(1).done_qty));
        actual3.setText(formatter.format(detailDataMaster.get(2).done_qty));
        actual4.setText(formatter.format(detailDataMaster.get(3).done_qty));
        actual5.setText(formatter.format(detailDataMaster.get(4).done_qty));

        defe1.setText(formatter.format(detailDataMaster.get(0).defect_qty));
        defe2.setText(formatter.format(detailDataMaster.get(1).defect_qty));
        defe3.setText(formatter.format(detailDataMaster.get(2).defect_qty));
        defe4.setText(formatter.format(detailDataMaster.get(3).defect_qty));
        defe5.setText(formatter.format(detailDataMaster.get(4).defect_qty));

        eff1.setText(detailDataMaster.get(0).efficiency+"%");
        eff2.setText(detailDataMaster.get(1).efficiency+"%");
        eff3.setText(detailDataMaster.get(2).efficiency+"%");
        eff4.setText(detailDataMaster.get(3).efficiency+"%");
        eff5.setText(detailDataMaster.get(4).efficiency+"%");

        for (int i=0; i<detailDataMaster.size();i++){

           if (detailDataMaster.get(i).getStt().equals("3")){

               String color = "#E9A525";

               if (i == 0){
                   defe1.setBackgroundColor(Color.parseColor(color));
                   eff1.setBackgroundColor(Color.parseColor(color));
                   actual1.setBackgroundColor(Color.parseColor(color));
                   target1.setBackgroundColor(Color.parseColor(color));
                   time1.setBackgroundColor(Color.parseColor(color));
               }else if (i == 1){
                   defe2.setBackgroundColor(Color.parseColor(color));
                   eff2.setBackgroundColor(Color.parseColor(color));
                   actual2.setBackgroundColor(Color.parseColor(color));
                   target2.setBackgroundColor(Color.parseColor(color));
                   time2.setBackgroundColor(Color.parseColor(color));
               }else if (i == 2){
                   defe3.setBackgroundColor(Color.parseColor(color));
                   eff3.setBackgroundColor(Color.parseColor(color));
                   actual3.setBackgroundColor(Color.parseColor(color));
                   target3.setBackgroundColor(Color.parseColor(color));
                   time3.setBackgroundColor(Color.parseColor(color));
               }else if (i == 3){
                   defe4.setBackgroundColor(Color.parseColor(color));
                   eff4.setBackgroundColor(Color.parseColor(color));
                   actual4.setBackgroundColor(Color.parseColor(color));
                   target4.setBackgroundColor(Color.parseColor(color));
                   time4.setBackgroundColor(Color.parseColor(color));
               }else {
                   defe5.setBackgroundColor(Color.parseColor(color));
                   eff5.setBackgroundColor(Color.parseColor(color));
                   actual5.setBackgroundColor(Color.parseColor(color));
                   target5.setBackgroundColor(Color.parseColor(color));
                   time5.setBackgroundColor(Color.parseColor(color));
               }

           }

        }

    }

    private void showFragmentB() {
        handler.removeCallbacks(r);

        NoticeFragment noticeFragment;
        FragmentManager fragmentManager = this.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.slide_in,R.animator.slide_out);
        noticeFragment = new NoticeFragment();
        fragmentTransaction.replace(R.id.framContent, noticeFragment);
        fragmentTransaction.commitAllowingStateLoss();

    }
    @Override
    public void onDestroy() {

        handler.removeCallbacks(r);

        super.onDestroy();
    }
}