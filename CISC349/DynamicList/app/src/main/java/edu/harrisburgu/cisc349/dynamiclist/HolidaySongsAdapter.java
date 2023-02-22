package edu.harrisburgu.cisc349.dynamiclist;

import android.content.Context;
import android.graphics.Bitmap;
import android.icu.util.TimeUnit;
import android.util.LruCache;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import com.android.volley.RequestQueue;
import com.android.volley.toolbox.ImageLoader;
import com.android.volley.toolbox.NetworkImageView;

import java.util.ArrayList;

public class HolidaySongsAdapter extends BaseAdapter {
    private static final int IMAGE_SIZE = 256;
    private Context context;
    private ArrayList<HolidaySongs> holidaySongs;
    private ImageLoader imageLoader;

    public HolidaySongsAdapter(Context context, ArrayList<HolidaySongs> holidaySongs, RequestQueue queue)
    {
        this.context = context;
        this.holidaySongs = holidaySongs;

        imageLoader = new ImageLoader(queue, new ImageLoader.ImageCache() {
            private final LruCache<String, Bitmap> mCache = new LruCache<String,
                    Bitmap>(20);

            @Override
            public Bitmap getBitmap(String url) {
                return mCache.get(url);
            }

            @Override
            public void putBitmap(String url, Bitmap bitmap) {
                mCache.put(url, bitmap);

            }
        });
    }

    @Override
    public int getCount() {
        if (null == holidaySongs) return 0;
        else return holidaySongs.size();
    }

    @Override
    public Object getItem(int i) {
        if (null == holidaySongs) return null;
        else return holidaySongs.get(i);
    }

    @Override
    public long getItemId(int i) {
        if (null == holidaySongs) return 0;
        else return holidaySongs.get(i).hashCode();
    }

    @Override
    public View getView(int i, View view, ViewGroup viewGroup) {
        view = LayoutInflater.from(context).inflate(R.layout.layout_list_item,
                viewGroup, false );

        HolidaySongs album = holidaySongs.get(i);

        TextView tv = view.findViewById(R.id.albumName);
        tv.setText(album.getName());
        tv = view.findViewById(R.id.artistName);
        tv.setText(album.getArtist());
        tv = view.findViewById(R.id.danceability);
        tv.setText(String.format("%s: %.3f", context.getString(R.string.danceability),
                album.getDanceability()));
        tv = view.findViewById(R.id.duration);
        tv.setText(String.format("%d:%d", (album.getDurationMs()/1000) /60,
                (album.getDurationMs()/1000) % 60));

        NetworkImageView image = (NetworkImageView) view.findViewById(R.id.albumImageView);
        //image.setMaxHeight(IMAGE_SIZE);
        //image.setMaxWidth(IMAGE_SIZE);
        //image.setScaleType(ImageView.ScaleType.CENTER);

        image.setImageUrl(album.getImage(), imageLoader);

        return view;
    }
}
