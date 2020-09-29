package autonsi.smasttvapp;

import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.graphics.Color;
import android.os.Bundle;


import android.os.Handler;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.webkit.WebView;
import android.webkit.WebViewClient;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ViewFlipper;

import com.bumptech.glide.Glide;

import java.util.ArrayList;

/**
 * A simple {@link Fragment} subclass.
 * Use the {@link NoticeFragment#newInstance} factory method to
 * create an instance of this fragment.
 */
public class NoticeFragment extends Fragment {
    String webUrl = Url.webUrl;
    // TODO: Rename parameter arguments, choose names that match
    // the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";
    final Handler handler = new Handler();

    TextView location,tv_title;
    WebView contain;
    ArrayList<NoticeMaster> noticeMasterArrayList;
   // ImageView hinh1,hinh2,hinh3;
    ViewFlipper flipper;

    int timesetting = MainActivity.timesetting;
    // TODO: Rename and change types of parameters
    private String mParam1;
    private String mParam2;

    public NoticeFragment() {
        // Required empty public constructor
    }

    /**
     * Use this factory method to create a new instance of
     * this fragment using the provided parameters.
     *
     * @param param1 Parameter 1.
     * @param param2 Parameter 2.
     * @return A new instance of fragment NoticeFragment.
     */
    // TODO: Rename and change types and number of parameters
    public static NoticeFragment newInstance(String param1, String param2) {
        NoticeFragment fragment = new NoticeFragment();
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
        View view = inflater.inflate(R.layout.fragment_notice, container, false);
        starthandler();
        // Inflate the layout for this fragment
        contain = view.findViewById(R.id.container);
        tv_title = view.findViewById(R.id.tv_title);
        location = view.findViewById(R.id.location);
//        hinh1 = view.findViewById(R.id.hinh1);
//        hinh2 = view.findViewById(R.id.hinh2);
//        hinh3 = view.findViewById(R.id.hinh3);
        flipper = view.findViewById(R.id.flipper);

        contain.getSettings().setJavaScriptEnabled(true);
        contain.setBackgroundColor(Color.parseColor("#648991"));
        contain.setWebViewClient(new WebViewClient());
        thread.start();

        noticeMasterArrayList = MainActivity.noticeMasterArrayList;
        ArrayList<String> arrayList = noticeMasterArrayList.get(0).image;

        for (int i = 0;i<arrayList.size();i++){

            flipperImage(arrayList.get(i));
        }

//        if (arrayList.size()==0){
//
//        }else if (arrayList.size()==1){
//            Glide.with(getActivity())
//                    .load(webUrl + "Images/LineIssueDaily/"+ arrayList.get(0))
//                    .error(R.drawable.errorimage)
//                    .into(hinh1);
//        }else if (arrayList.size()==2){
//            Glide.with(getActivity())
//                    .load(webUrl + "Images/LineIssueDaily/"+ arrayList.get(0))
//                    .error(R.drawable.errorimage)
//                    .into(hinh1);
//            Glide.with(getActivity())
//                    .load(webUrl + "Images/LineIssueDaily/"+ arrayList.get(1))
//                    .error(R.drawable.errorimage)
//                    .into(hinh2);
//        }else {
//            Glide.with(getActivity())
//                    .load(webUrl + "Images/LineIssueDaily/"+ arrayList.get(0))
//                    .error(R.drawable.errorimage)
//                    .into(hinh1);
//            Glide.with(getActivity())
//                    .load(webUrl + "Images/LineIssueDaily/"+ arrayList.get(1))
//                    .error(R.drawable.errorimage)
//                    .into(hinh2);
//            Glide.with(getActivity())
//                    .load(webUrl + "Images/LineIssueDaily/"+ arrayList.get(2))
//                    .error(R.drawable.errorimage)
//                    .into(hinh3);
//        }

        return view;
    }

    private void flipperImage(String i) {

        ImageView imageView=new ImageView(getActivity());
        Glide.with(getActivity())
                .load(webUrl + "Images/LineIssueDaily/" + i)
                .error(R.drawable.errorimage)
                .into(imageView);
        flipper.addView(imageView);
        flipper.setFlipInterval(timesetting/3*1000);
        flipper.setAutoStart(true);
        flipper.setInAnimation(getActivity(),android.R.anim.slide_in_left);
        flipper.setOutAnimation(getActivity(),android.R.anim.slide_out_right);

    }

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
        noticeMasterArrayList = MainActivity.noticeMasterArrayList;
        tv_title.setText(noticeMasterArrayList.get(0).name);
        location.setText(noticeMasterArrayList.get(0).line_name +" ~ "+ noticeMasterArrayList.get(0).product_name + " ~ "
        +noticeMasterArrayList.get(0).work_date );
        contain.setBackgroundColor(Color.parseColor("#648991"));
        contain.loadData(noticeMasterArrayList.get(0).content,"text/html","UTF-8");

    }

    private void showFragmentB() {
        handler.removeCallbacks(r);
        ViewFragment viewFragment;
        FragmentManager fragmentManager = this.getFragmentManager();
        FragmentTransaction fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.setCustomAnimations(R.animator.slide_in,R.animator.slide_out);
        viewFragment = new ViewFragment();
        fragmentTransaction.replace(R.id.framContent, viewFragment);
        fragmentTransaction.commitAllowingStateLoss();
    }
}