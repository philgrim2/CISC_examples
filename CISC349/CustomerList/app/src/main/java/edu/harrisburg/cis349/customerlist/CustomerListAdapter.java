package edu.harrisburg.cis349.customerlist;

import android.content.Context;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;

import com.android.volley.RequestQueue;

import java.util.List;

public class CustomerListAdapter extends BaseAdapter {

    protected Context context;
    protected List<Customer> customerList;
    protected RequestQueue queue;

    public CustomerListAdapter(Context context, List<Customer> list, RequestQueue queue)
    {
        this.context = context;
        this.customerList = list;
        this.queue = queue;
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
        return null;
    }
}
