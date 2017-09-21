package corve.nohsedge;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.lang.reflect.Array;
import java.util.ArrayList;

/**
 * Created by Cole on 9/21/2017.
 */

public class EdgeClassAdapter extends BaseAdapter {
    private Context mContext;
    private LayoutInflater mInflater;
    private ArrayList<Object> mDataSource;

    public EdgeClassAdapter(Context context, ArrayList<Object> items) {
        mContext = context;
        mDataSource = items;
        mInflater = (LayoutInflater) mContext.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
    }

    //1
    @Override
    public int getCount() {
        return mDataSource.size();
    }

    //2
    @Override
    public Object getItem(int position) {
        return mDataSource.get(position);
    }

    //3
    @Override
    public long getItemId(int position) {
        return position;
    }

    //4
    @Override
    public View getView(int position, View convertView, ViewGroup parent) {
        // Get view for row item
        View rowView = mInflater.inflate(R.layout.list_item, parent, false);
        // Get title element
        TextView titleTextView =
                (TextView) rowView.findViewById(corve.nohsedge.R.id.recipe_list_title);

// Get subtitle element
        TextView subtitleTextView =
                (TextView) rowView.findViewById(corve.nohsedge.R.id.recipe_list_subtitle);

// Get detail element
        TextView detailTextView =
                (TextView) rowView.findViewById(corve.nohsedge.R.id.recipe_list_detail);

// Get thumbnail element
        ImageView thumbnailImageView =
                (ImageView) rowView.findViewById(corve.nohsedge.R.id.recipe_list_thumbnail);
        // 1
        Object recipe = (Object) getItem(position);

// 2
        titleTextView.setText("titletest");
        subtitleTextView.setText("subtitletest");
        detailTextView.setText("detailtest");

        return rowView;
    }
}
