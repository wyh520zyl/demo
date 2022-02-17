package com.example.myapplication;

import android.annotation.SuppressLint;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.SystemClock;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;
import androidx.fragment.app.Fragment;
import androidx.viewpager.widget.ViewPager;
import java.util.ArrayList;
import java.util.List;

import static com.example.myapplication.recyclerdemo.RecyclerViewActivity.recyclerViewActivity;


public class PersonalFragment extends Fragment  {
    private static final String ARG_PARAM = "param";
    private static final int ACCESSORY_CONCEAL = 0;
    private static final int ACCESSORY_SHOW = 1;
    public static TextView personal_comment_text;
    private static ViewPager viewPager;
    public static int square_num=0;
    public static int square_page=1;
    private int mParam;//用来表示当前需要展示的是哪一页
    public View view = null;
    private static String toUid;
    public List<String[]> list = new ArrayList<>();
    public static PersonalFragment detailInfoFragment;
    public static String circle_id="";//动态id
    //个人信息页面的控件
    public static TextView personal_nickname,personal_address,personal_id,personal_age,personal_charm,personal_status,personal_constellation,personal_grade,personal_signature;
    public static ImageView personal_gender;

    //动态信息页面控件
    public static ListView personal_friends_list;
    public PersonalFragment() {
    }

    public static PersonalFragment newInstance(int param, ViewPager viewpager, String to_uid) {
        viewPager = viewpager;
        toUid=to_uid;
        PersonalFragment fragment = new PersonalFragment();
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        detailInfoFragment = this;
        if (getArguments() != null) {
            mParam = getArguments().getInt(ARG_PARAM);
        }
    }

    @Override
    public void onResume() {
        super.onResume();
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }


    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        System.out.println("滑动一下1：" + mParam);
        //根据mParam来判断当前展示的是哪一页，根据页数的不同展示不同的信息
        switch (mParam) {
            case 0:
                view = inflater.inflate(R.layout.personal_information_essential_information, container, false);
                setChatNearby();
                return view;
            case 1:
                view = inflater.inflate(R.layout.personal_information_dynamic, container, false);
                bindingFriends();
                return view;
            default:
                break;
        }
        return view;
    }


    private void setChatNearby() {
        personal_nickname=view.findViewById(R.id.personal_nickname);
        personal_gender=view.findViewById(R.id.personal_gender);
        personal_address=view.findViewById(R.id.personal_address);
        personal_id=view.findViewById(R.id.personal_id);
        personal_age=view.findViewById(R.id.personal_age);
        personal_charm=view.findViewById(R.id.personal_charm);
        personal_status=view.findViewById(R.id.personal_status);
        personal_constellation=view.findViewById(R.id.personal_constellation);
        personal_grade=view.findViewById(R.id.personal_grade);
        personal_signature=view.findViewById(R.id.personal_signature);
       // applyData();

    }

    private void bindingFriends() {
        personal_friends_list = view.findViewById(R.id.personal_friends_list);

        getPersonal();
    }
    public static void getPersonal(){
        List<String> list=new ArrayList();
        for(int i=0;i<20;i++){
            list.add(String.valueOf(i));
        }
            personal_friends_list.setAdapter(null);
         PersonalAdapter   personalAdapter=new PersonalAdapter(recyclerViewActivity,R.layout.asfsd,list);
            personal_friends_list.setAdapter(personalAdapter);
    }
}
