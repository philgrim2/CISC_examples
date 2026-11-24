package edu.harrisburgu.cisc349.customerlistagain;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import java.util.List;

public class CustomerAdapter extends BaseAdapter {
    List<Customer> customers;
    private Context context;
    TextView name;
    TextView address;
    TextView phone;
    public CustomerAdapter(Context context, List<Customer> customers)
    {
        this.customers = customers;
        this.context = context;
    }

    @Override
    public int getCount() {
        return null == customers ? 0 : customers.size();
    }

    @Override
    public Object getItem(int i) {
        return null == customers ? null : customers.get(i);
    }

    @Override
    public long getItemId(int i) {
        return i;
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.layout_customer_list,
                viewGroup, false );
        name = view.findViewById(R.id.customer_name);
        address = view.findViewById(R.id.customer_address);
        phone = view.findViewById(R.id.customer_phone);

        name.setText(customers.get(i).getName());
        address.setText(customers.get(i).getAddress());
        phone.setText(customers.get(i).getPhone());

        return view;
    }
}
