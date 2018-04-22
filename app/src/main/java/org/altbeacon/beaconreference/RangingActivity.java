package org.altbeacon.beaconreference;

import android.os.Bundle;
import android.os.RemoteException;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.FrameLayout;

import org.altbeacon.beacon.Beacon;
import org.altbeacon.beacon.BeaconConsumer;
import org.altbeacon.beacon.BeaconManager;
import org.altbeacon.beacon.RangeNotifier;
import org.altbeacon.beacon.Region;

import java.util.Collection;

public class RangingActivity extends AppCompatActivity implements BeaconConsumer {
    protected static final String TAG = "RangingActivity";
    private BeaconManager beaconManager = BeaconManager.getInstanceForApplication(this);
    FrameLayout frameLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_ranging);
        frameLayout=(FrameLayout)findViewById(R.id.framelayout);
        Waiting_Fragment fragment1=new Waiting_Fragment();
        getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,fragment1).commit();

        beaconManager.bind(this);
    }

    @Override 
    protected void onDestroy() {
        super.onDestroy();
        beaconManager.unbind(this);
    }

    @Override 
    protected void onPause() {
        super.onPause();
        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(true);
    }

    @Override 
    protected void onResume() {
        super.onResume();
        if (beaconManager.isBound(this)) beaconManager.setBackgroundMode(false);
    }

    @Override
    public void onBeaconServiceConnect() {
        beaconManager.setRangeNotifier(new RangeNotifier() {
           @Override
           public void didRangeBeaconsInRegion(Collection<Beacon> beacons, Region region) {
              if (beacons.size() > 0) {
                 //EditText editText = (EditText)RangingActivity.this.findViewById(R.id.rangingText);
                 Beacon firstBeacon = beacons.iterator().next();
                // logToDisplay("The first beacon " + firstBeacon.toString() + " is about " + firstBeacon.getDistance() + " meters away.");
                  Log.e(TAG,firstBeacon.toString());
                  String x =firstBeacon.getId1().toString();
                  Double d=firstBeacon.getDistance();
                  // x=x.substring(24);
                  Log.e("Ranging","The first beacon " + firstBeacon.getId1().toString() + " is about " + firstBeacon.getDistance() + " meters away.");

                  if(x.equals("00112233-4455-6677-8899-aabbccddeeff")) {
                      Payment_received_fragment fragment=new Payment_received_fragment();
                      getSupportFragmentManager().beginTransaction().replace(R.id.framelayout,fragment).commit();
                  }

              }
           }

        });

        try {
            beaconManager.startRangingBeaconsInRegion(new Region("myRangingUniqueId", null, null, null));
        } catch (RemoteException e) {   }
    }

    private void logToDisplay(final String line) {
        runOnUiThread(new Runnable() {
            public void run() {
              //  EditText editText = (EditText)RangingActivity.this.findViewById(R.id.rangingText);
               // editText.append(line+"\n");
            }
        });
    }
}
