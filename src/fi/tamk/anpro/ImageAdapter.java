package fi.tamk.anpro;

import android.content.Context;
import android.content.res.TypedArray;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Gallery;
import android.widget.ImageView;

// TODO: Javadoc-kommentit ym. pienemmät kommentit
public class ImageAdapter extends BaseAdapter
{
    int galleryItemBackground;
    private Context mContext;

    public static Integer[] mImageIds = {
            R.drawable.player_tex_0,
            R.drawable.icon,
            R.drawable.enemy1_tex_0,
            R.drawable.icon,
            R.drawable.projectilelaser_tex_0,
            R.drawable.icon
    };

    public ImageAdapter(Context c)
    {
        mContext = c;
        TypedArray a = c.obtainStyledAttributes(R.styleable.LevelSelectActivity);
        galleryItemBackground = a.getResourceId(
                R.styleable.LevelSelectActivity_android_galleryItemBackground, 0);
        a.recycle();
    }

    public int getCount()
    {
        return mImageIds.length;
    }

    public Object getItem(int position)
    {
        return position;
    }

    public long getItemId(int position)
    {
        return position;
    }

    public View getView(int position, View convertView, ViewGroup parent)
    {
        ImageView i = new ImageView(mContext);

        i.setImageResource(mImageIds[position]);
        i.setLayoutParams(new Gallery.LayoutParams(150, 100));
        i.setScaleType(ImageView.ScaleType.FIT_XY);
        i.setBackgroundResource(galleryItemBackground);

        return i;
    }
}
