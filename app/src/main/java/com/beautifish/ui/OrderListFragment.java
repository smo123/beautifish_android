package com.beautifish.ui;


import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;

import com.beautifish.R;
import com.beautifish.adapter.OrderAdapter;
import com.beautifish.app.App;
import com.beautifish.models.Order;
import com.beautifish.models.Orders;
import com.beautifish.utils.HttpAsyncTask;

import java.util.ArrayList;

public class OrderListFragment extends BaseFragment implements HttpAsyncTask.IHttpAsyncTask {

    public static final String TAG = OrderListFragment.class.getName();
    private static final int REQUIRE_TYPE_IN_MONEY_HISTORY = 1;
    private static final int REQUIRE_TYPE_OPENT_RED_PACKET = 2;
    private static final String PARAM_FROM_USER_ID = "fromUserId";

    private Button btnBack;
    private ListView listViewOrder;
    private OrderAdapter orderAdapter;

    private FragmentActivity activity;
    private int getRequireType;
    private String fromUserId;

    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;

    private Orders orders;
    private ArrayList<Order> ordersArrayList;

    public static OrderListFragment newInstance(String fromUserId) {
        OrderListFragment fragment = new OrderListFragment();
        Bundle args = new Bundle();
        args.putString(PARAM_FROM_USER_ID, fromUserId);
        fragment.setArguments(args);
        return fragment;
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        activity = getActivity();
        fragmentManager = activity.getSupportFragmentManager();//Fragment管理器
        if (getArguments() != null) {
            fromUserId = getArguments().getString(PARAM_FROM_USER_ID);
        }
        HttpAsyncTask.getInstance(this, REQUIRE_TYPE_IN_MONEY_HISTORY, fromUserId);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_orders, container, false);
        btnBack = (Button)v.findViewById(R.id.btn_back);
        listViewOrder = (ListView)v.findViewById(R.id.list_order);
        listViewOrder.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                String phone = App.getData4SP(activity, App.SP_PACKAGE_USER, App.SP_KEY_PHONE);
                if(phone.equalsIgnoreCase("18775769566")){
                    Order order = (Order)parent.getItemAtPosition(position);
                    String isOpen = order.getIsOpen();
                    if(isOpen == null||!isOpen.equalsIgnoreCase("1")){
                        String red_packet_id = order.getMoneyID();
                        String red_packet_receiver_id = order.getToUserId();
                    }else {
                        App.toast(activity, getResources().getString(R.string.had_get_red_packet));
                    }
                }
            }
        });
        btnBack.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fragmentManager.popBackStack();
            }
        });
        return v;
    }

    @Override
    public void onPreExecute() {

    }

    @Override
    public Object doInBackground(Object... params) {
        getRequireType = (Integer)params[0];
        switch (getRequireType) {
            case REQUIRE_TYPE_IN_MONEY_HISTORY:
                fromUserId = (String) params[1];
                orders = App.inMoneyHistory(activity, App.IN_MONEY_HISTORY, fromUserId);
                break;
            case REQUIRE_TYPE_OPENT_RED_PACKET:
                String moneyID = (String) params[1];
                orders = App.updateRedPacket(activity, App.OPEN_RED_PACKET, moneyID);
                break;
        }
        return null;
    }

    @Override
    public void onProgressUpdate(Integer... values) {

    }

    @Override
    public void onPostExecute(Object result) {
        switch (getRequireType){
            case REQUIRE_TYPE_IN_MONEY_HISTORY:
                if(orders != null){
                    ordersArrayList = orders.getData();
                    orderAdapter = new OrderAdapter(activity, ordersArrayList);
                    listViewOrder.setAdapter(orderAdapter);
                }
                break;
            case REQUIRE_TYPE_OPENT_RED_PACKET:
                if(orders != null){
                    App.log(TAG, "----------orders------->"+orders.getMsg());
                }
                break;
        }
    }

}
