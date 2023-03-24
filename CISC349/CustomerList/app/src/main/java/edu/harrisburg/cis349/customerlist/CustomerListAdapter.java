package edu.harrisburg.cis349.customerlist;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import java.util.List;

public class CustomerListAdapter extends BaseAdapter {

    protected Context context;
    protected List<Customer> customerList;

    public CustomerListAdapter(Context context, List<Customer> list)
    {
        this.context = context;
        this.customerList = list;
    }

    @Override
    public int getCount() {
        if (null != customerList)
            return customerList.size();
        return 0;
    }

    @Override
    public Object getItem(int i) {
        if (null != customerList)
            return customerList.get(i);
        return null;
    }

    @Override
    public long getItemId(int i) {
        if (null != customerList)
            return customerList.get(i).hashCode();
        return 0;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.customer_list_layout,
                viewGroup, false );
        TextView name = view.findViewById(R.id.name);
        TextView address = view.findViewById(R.id.address);
        TextView phone = view.findViewById(R.id.phone);
        name.setText(customerList.get(i).getName());
        address.setText(customerList.get(i).getAddress());
        phone.setText(customerList.get(i).getPhone());
        return view;
    }
}
