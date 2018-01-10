package corve.nohsedge;

import android.os.Bundle;
import android.util.Log;

import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.common.api.PendingResult;
import com.google.android.gms.common.api.ResultCallback;
import com.google.android.gms.wearable.DataApi;
import com.google.android.gms.wearable.PutDataMapRequest;
import com.google.android.gms.wearable.PutDataRequest;
import com.google.android.gms.wearable.Wearable;
import com.google.android.gms.wearable.WearableListenerService;

import java.util.concurrent.TimeUnit;

/**
 * Created by corve on 1/10/2018.
 */

public class MobileWearableService extends WearableListenerService
        implements GoogleApiClient.ConnectionCallbacks, GoogleApiClient.OnConnectionFailedListener {
    private GoogleApiClient mGoogleApiClient;
    private boolean nodeConnected = false;

    @Override
    public void onCreate() {
        super.onCreate();
        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .addApi(Wearable.API)
                .addConnectionCallbacks(this)
                .addOnConnectionFailedListener(this)
                .build();
        mGoogleApiClient.connect();
    }

    @Override
    public void onConnected(Bundle bundle) {
        nodeConnected = true;
    }

    @Override
    public void onConnectionSuspended(int i) {
        nodeConnected = false;
    }

    @Override
    public void onConnectionFailed(ConnectionResult connectionResult) {
        nodeConnected = false;
    }

    private void sendData(final String dataToSend) {
        new Thread(new Runnable() {
            @Override
            public void run() {
                long TIMEOUT_S = 30;
                if (!nodeConnected) {
                    mGoogleApiClient.blockingConnect(TIMEOUT_S, TimeUnit.SECONDS);
                }
                if (!nodeConnected) {
                    Log.e("WEAR APP", "Failed to connect to mGoogleApiClient within " + TIMEOUT_S + " seconds");
                    return;
                }

                if (mGoogleApiClient.isConnected()) {
                    String PATH_DATA = "/edge";
                    PutDataMapRequest putDataMapRequest = PutDataMapRequest.create(PATH_DATA);
                    putDataMapRequest.getDataMap().putString("data", dataToSend);
                    PutDataRequest request = putDataMapRequest.asPutDataRequest();

                    PendingResult<DataApi.DataItemResult> pendingResult =
                            Wearable.DataApi.putDataItem(mGoogleApiClient, request);

                    pendingResult.setResultCallback(new ResultCallback<DataApi.DataItemResult>() {
                        @Override
                        public void onResult(DataApi.DataItemResult dataItemResult) {
                            Log.e("WEAR APP", "APPLICATION Result has come");
                        }
                    });

                } else {
                    Log.e("WEAR APP", "No Google API Client connection");
                }
            }
        }).start();
    }
}
