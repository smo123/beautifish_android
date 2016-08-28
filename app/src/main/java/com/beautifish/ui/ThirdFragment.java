package com.beautifish.ui;


import android.content.Intent;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;

import com.beautifish.R;
import com.beautifish.app.App;
import com.beautifish.models.Orders;
import com.beautifish.utils.HttpAsyncTask;

public class ThirdFragment extends BaseFragment implements HttpAsyncTask.IHttpAsyncTask, View.OnClickListener{

    public static final String TAG = ThirdFragment.class.getName();
    private static final int REQUIRE_TYPE_BUY = 1;//购买
    private static final int REQUIRE_TYPE_IN_MONEY = 2;//购买
    private static final String ARG_PARAM1 = "param1";
    private static final String ARG_PARAM2 = "param2";

    //red packet code : 红包功能使用的常量
    private static final int MESSAGE_TYPE_RECV_RED_PACKET = 5;
    private static final int MESSAGE_TYPE_SEND_RED_PACKET = 6;
    private static final int MESSAGE_TYPE_SEND_RED_PACKET_ACK = 7;
    private static final int MESSAGE_TYPE_RECV_RED_PACKET_ACK = 8;
    private static final int REQUEST_CODE_SEND_RED_PACKET = 16;
    private static final int ITEM_RED_PACKET = 16;
    //end of red packet code

    private FragmentActivity activity;
    private Button btnInMoney, btnOutMoney, btnGetRP, btnChange, btnLogout;

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    private int getRequireType;
    private String mParam1;
    private String mParam2;

    private Orders orders;

    public static ThirdFragment newInstance(String param1, String param2) {
        ThirdFragment fragment = new ThirdFragment();
        Bundle args = new Bundle();
        args.putString(ARG_PARAM1, param1);
        args.putString(ARG_PARAM2, param2);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        fragmentManager = activity.getSupportFragmentManager();//Fragment管理器
        if (getArguments() != null) {
            mParam1 = getArguments().getString(ARG_PARAM1);
            mParam2 = getArguments().getString(ARG_PARAM2);
        }
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_third, container, false);
        btnInMoney = (Button)view.findViewById(R.id.btn_in_money);
        btnOutMoney = (Button)view.findViewById(R.id.btn_out_money);
        btnGetRP = (Button)view.findViewById(R.id.btn_get_red_package);
        btnChange = (Button)view.findViewById(R.id.btn_my_change);
        btnLogout = (Button)view.findViewById(R.id.btn_logout);
        btnInMoney.setOnClickListener(this);
        btnOutMoney.setOnClickListener(this);
        btnGetRP.setOnClickListener(this);
        btnChange.setOnClickListener(this);
        btnLogout.setOnClickListener(this);

        String phone = App.getData4SP(activity, App.SP_PACKAGE_USER, App.SP_KEY_PHONE);
        if(phone != null&&phone.equalsIgnoreCase("18775769566")){
            btnChange.setVisibility(View.VISIBLE);
            btnGetRP.setText(getResources().getString(R.string.btn_get_red_package));
        }

        return view;
    }

    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_in_money:
                HttpAsyncTask.getInstance(ThirdFragment.this, REQUIRE_TYPE_BUY);
                break;
            case R.id.btn_out_money:
                OutMoneyFragment outMoneyFragment = OutMoneyFragment.newInstance();
                transaction = fragmentManager.beginTransaction();
                transaction.replace(R.id.layout_main_content, outMoneyFragment);
                transaction.addToBackStack(null);
                transaction.commit();
                break;
            case R.id.btn_get_red_package:
                String phone = App.getData4SP(activity, App.SP_PACKAGE_USER, App.SP_KEY_PHONE);
                if(!phone.equalsIgnoreCase("-1")){
                    OrderListFragment orderListFragment = OrderListFragment.newInstance(phone);
                    transaction = fragmentManager.beginTransaction();
                    transaction.replace(R.id.layout_main_content, orderListFragment);
                    transaction.addToBackStack(null);
                    transaction.commit();
                }else {
                    App.toast(activity, getResources().getString(R.string.invalite_login));
                }
                break;
            case R.id.btn_my_change:
                break;
            case R.id.btn_logout:
                Intent intent = new Intent();
                intent.setClass(activity, LoginActivity.class);
                startActivity(intent);
                activity.finish();
                break;
        }
    }

    @Override
    public void onPreExecute() {
        //LoadingDialog.showDialog(activity);
    }

    @Override
    public Object doInBackground(Object... params) {
        getRequireType = (Integer)params[0];
        switch (getRequireType){
            case REQUIRE_TYPE_BUY:
                break;
            case REQUIRE_TYPE_IN_MONEY:
                String url = params[1]+"";
                String fromUserId = params[2]+"";
                String toUserId = params[3]+"";
                String moneyAmount = params[4]+"";
                String moneyID = params[5]+"";
                String moneyMsgDirect = params[6]+"";
                String takenMoney = params[7]+"";
                orders = App.inMoney(activity, url, fromUserId, toUserId, moneyAmount, moneyID, moneyMsgDirect, takenMoney);
                break;
        }
        return null;
    }

    @Override
    public void onProgressUpdate(Integer... values) {

    }

    @Override
    public void onPostExecute(Object result) {
        //LoadingDialog.dismissDialog();
        switch (getRequireType){
            case REQUIRE_TYPE_BUY:
                break;
            case REQUIRE_TYPE_IN_MONEY:
                if(orders != null){
                    App.log(TAG, "----------orders------->"+orders.getMsg());
                }
                break;
        }
    }

}
