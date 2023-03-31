package edu.harrisburg.cis349.cameratest;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;

import java.util.ArrayList;
import java.util.List;

public class PictureListAdapter extends BaseAdapter  {
    List<Bitmap> pictures;
    private Context context;

    public PictureListAdapter(Context context, ArrayList<Bitmap> pictures) {
        this.context = context;
        this.pictures = pictures;
    }


    @Override
    public int getCount() {
        if (null == pictures) return 0;
        else return pictures.size();
    }

    @Override
    public Object getItem(int i) {
        if (null == pictures) return null;
        else return pictures.get(i);
    }

    @Override
    public long getItemId(int i) {
        if (null == pictures) return 0;
        else return pictures.get(i).hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.list_item,
                viewGroup, false);

        Log.d("PictureListAdapter", "Image index " + i);

        Bitmap picture = pictures.get(i);

        ImageView image = (ImageView) view.findViewById(R.id.listImageView);

        image.setImageBitmap(picture);

        TextView text = (TextView) view.findViewById(R.id.listImageInfo);
        text.setText("Image index " + i);

        return view;
    }
}
