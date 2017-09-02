package corve.nohsedge;

import android.content.Intent;
import android.net.Uri;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.View;

import java.util.UUID;

public class MainActivity extends AppCompatActivity {
         
private String uuid = "b39de08ded8ac21c";

    public String getUuid() {
        return uuid;
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
    }

    public void openBrowser(View V) {
        Intent browserIntent = new Intent(Intent.ACTION_VIEW, Uri.parse("https://api.superfanu.com/6.0.0/gen/link_track.php?platform=Android&uuid=" + getUuid() + "&nid=305&lkey=nohsstampede-edgetime-module"));
        startActivity(browserIntent);

    }


}
