package corve.nohsedge;

import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.wearable.activity.WearableActivity;
import android.widget.ListView;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.Calendar;

public class MainActivity extends WearableActivity {
    private ListView mList;
    private ArrayList<EdgeClass> classes = new ArrayList<>();
    //object that contains title, text, time, and date for an edge class

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_edge_view);
        mList = findViewById(R.id.listview);
        classes = getCurrentEdgeClasses();
        // Enables Always-on
        setAmbientEnabled();
    }

    private ArrayList<EdgeClass> getCurrentEdgeClasses(){

    }
}
