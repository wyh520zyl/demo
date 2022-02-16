package com.example.viewpager;


import android.content.Context;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.viewpager.widget.ViewPager;

public class DetailInfoFragment extends Fragment  {
    private static final String ARG_PARAM = "param";
    private int mParam;//用来表示当前需要展示的是哪一页
    public View view=null;
    private int recommend=0;
    private  ViewPager viewPager;
    private Context mContext;


    public static DetailInfoFragment newInstance(Context context, int param, ViewPager viewpager) {
        DetailInfoFragment fragment = new DetailInfoFragment();
        fragment.viewPager = viewpager;
        fragment.mContext = context;
        Bundle args = new Bundle();
        args.putInt(ARG_PARAM, param);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        System.out.println("滑动一下："+mParam);
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
    public View onCreateView(LayoutInflater inflater,  ViewGroup container,
                             Bundle savedInstanceState) {
        System.out.println("滑动一下1："+mParam);
        //根据mParam来判断当前展示的是哪一页，根据页数的不同展示不同的信息
        switch (mParam) {
            case 0:  //聊天
                System.out.println("进入聊天0"+mParam);
                view = inflater.inflate(R.layout.chat, container, false);
                return view;
            case 1:
                System.out.println("进入推荐1"+mParam);
                view = inflater.inflate(R.layout.recommend, container, false);
                init(view);
                return view;
            case 2:
                System.out.println("进入好友2"+mParam);
                view = inflater.inflate(R.layout.friends, container, false);
                return view;
            default:
                break;
        }
        return view;
    }
    private void init(View view) {
        ChildViewPager viewPager=view.findViewById(R.id.viewpager1);
        FragmentManager messageFragmentManager = ((MainActivity)mContext).getSupportFragmentManager();
        ViewPageAdapter1 viewPageAdapter = new ViewPageAdapter1(mContext,viewPager, messageFragmentManager);
        viewPager.setAdapter(viewPageAdapter);
    }
}

