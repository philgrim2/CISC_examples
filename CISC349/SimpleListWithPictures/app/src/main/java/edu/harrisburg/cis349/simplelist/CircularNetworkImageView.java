package edu.harrisburg.cis349.simplelist;
import com.android.volley.toolbox.NetworkImageView;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.PorterDuff;
import android.graphics.PorterDuffXfermode;
import android.graphics.Rect;
import android.graphics.RectF;
import android.graphics.drawable.BitmapDrawable;
import android.util.AttributeSet;

import androidx.core.content.ContextCompat;

public class CircularNetworkImageView extends NetworkImageView {

    Context mContext;

    public CircularNetworkImageView(Context context) {
        super(context);
        mContext = context;
    }

    public CircularNetworkImageView(Context context, AttributeSet attrs) {
        super(context, attrs);
        mContext = context;
    }

    public CircularNetworkImageView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        mContext = context;
    }
    @Override
    public void setImageBitmap(Bitmap bitmap) {
        if (null != bitmap) {
            setImageDrawable(new BitmapDrawable(mContext.getResources(), getCircularBitmap(bitmap)));
        }
    }
    private Bitmap getCircularBitmap(Bitmap bitmap) {
        Bitmap output = Bitmap.createBitmap(bitmap.getWidth(), bitmap.getHeight(), Bitmap.Config.ARGB_8888);
        Canvas canvas = new Canvas(output);
        Integer width = bitmap.getWidth();
        Integer height = bitmap.getHeight();
        Integer strokeWidth = 6;

        //if (width > height) width = height;

        final Integer color = 0xffffffff;
        final Paint paint = new Paint();
        final Rect rect = new Rect(0,0,width,height);
        final RectF rectF = new RectF(rect);
        final float radius = Math.min(width,height) / 2;

        paint.setAntiAlias(true);
        canvas.drawARGB(0,0,0,0);
        paint.setColor(color);
        canvas.drawRoundRect(rectF,radius,radius,paint);

        paint.setXfermode(new PorterDuffXfermode(PorterDuff.Mode.SRC_IN));
        canvas.drawBitmap(bitmap, rect, rect, paint);

        paint.setStyle(Paint.Style.STROKE);
        paint.setStrokeWidth(strokeWidth);
        paint.setColor(ContextCompat.getColor(mContext, R.color.white));

        Integer halfCanvasWidth = canvas.getWidth() / 2;
        Integer halfCCanvasHeight = canvas.getHeight() / 2;
        Integer cRadius = Math.min(halfCanvasWidth,halfCCanvasHeight);
        canvas.drawCircle(halfCanvasWidth,halfCCanvasHeight,cRadius,paint);
        return output;

    }

}
