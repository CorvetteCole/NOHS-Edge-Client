package corve.nohsedge;

import android.content.Context;
import android.graphics.drawable.Drawable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

import java.util.ArrayList;

/**
 * Created by corve on 9/21/2017.
 */

public class EdgeBaseAdapter extends BaseAdapter {
    private static final String TAG = "EDGE_BASE_ADAPTER";
    ArrayList<SingleRow> list;
    Context context;
    String[] EdgeTitle;
    String[] EdgeText;
    String[] EdgeTime;
    String[] EdgeDate;
    EdgeBaseAdapter(Context c, String[] Title, String[] Text, String[] Time, String[] Date){
        list = new ArrayList<SingleRow>();
        context = c;
        EdgeTitle = Title;
        EdgeText = Text;
        EdgeTime = Time;
        EdgeDate = Date;
        for(int i= 0; i< EdgeViewActivity.EdgeTitle.length; i++){
            list.add(new SingleRow(EdgeTitle[i], EdgeText[i], EdgeTime[i], EdgeDate[i]));
        }
    }
    @Override
    public int getCount() {
        return list.size();
    }
    @Override
    public Object getItem(int i) {
        return list.get(i);
    }
    @Override
    public long getItemId(int i) {
        return i;
    }
    @Override
    public View getView(int i, View convertView, ViewGroup parent) {
        LayoutInflater inflater = (LayoutInflater)
                context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);

        View row = inflater.inflate(R.layout.edge_list_item,parent, false);

        TextView mText = row.findViewById(R.id.edgesubtitle);
        TextView mTitle = row.findViewById(R.id.edgetitle);
        TextView mTime = row.findViewById(R.id.edgedetail);
        TextView mDate = row.findViewById(R.id.edgedate);
        ImageView mImage = row.findViewById(R.id.edgethumbnail);

        SingleRow contactrow = list.get(i);

        mTitle.setText(contactrow.title);
        mText.setText(contactrow.text);
        mTime.setText(contactrow.edgetime);
        mDate.setText(contactrow.edgedate);
        Drawable drawable = null;
        mImage.setImageDrawable(drawable);

        return row;
    }}