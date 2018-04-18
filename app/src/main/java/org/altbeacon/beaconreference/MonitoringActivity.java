package org.altbeacon.beaconreference;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Build;
import android.os.Bundle;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import org.altbeacon.beacon.BeaconManager;

/**
 * 
 * @author dyoung
 * @author Matt Tyler
 */
public class MonitoringActivity extends AppCompatActivity {
	protected static final String TAG = "MonitoringActivity";
	private static final int PERMISSION_REQUEST_COARSE_LOCATION = 1;
	private static final int PERMISSIONS_REQUEST_CODE = 100;
	private static String[] mPermissions = { Manifest.permission.ACCESS_FINE_LOCATION};


	SharedPreferences sharedPreferences;
	EditText phnumber,aadhar;


	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_monitoring);
		//verifyBluetooth();
        logToDisplay("Application just launched");

		if (!havePermissions()) {
			Log.i(TAG, "Requesting permissions needed for this app.");
			requestPermissions();
		}

		if(!isBlueEnable()){
			Intent bluetoothIntent = new Intent(BluetoothAdapter.ACTION_REQUEST_ENABLE);
			startActivity(bluetoothIntent);
		}


		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.M) {
            // Android M Permission check
            if (this.checkSelfPermission(Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
                final AlertDialog.Builder builder = new AlertDialog.Builder(this);
                builder.setTitle("This app needs location access");
                builder.setMessage("Please grant location access so this app can detect beacons in the background.");
                builder.setPositiveButton(android.R.string.ok, null);
                builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

                    @TargetApi(23)
                    @Override
                    public void onDismiss(DialogInterface dialog) {
                        requestPermissions(new String[]{Manifest.permission.ACCESS_COARSE_LOCATION},
                                PERMISSION_REQUEST_COARSE_LOCATION);
                    }

                });
                builder.show();
            }
        }

		phnumber=(EditText)findViewById(R.id.phnumber);
		aadhar=(EditText)findViewById(R.id.aadhar);

		Button update,drive;
		update=(Button)findViewById(R.id.update);
		drive=(Button)findViewById(R.id.driving);

		sharedPreferences=getSharedPreferences("Details",MODE_PRIVATE);
		String phone=sharedPreferences.getString("phone","NULL");
		String aadhar1=sharedPreferences.getString("plate","NULL");


		if(phone.equals("NULL") || aadhar1.equals("NULL")) {

		}
		else
		{
			phnumber.setText(phone);
			aadhar.setText(aadhar1);
		}

		update.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {
				SharedPreferences.Editor editor = sharedPreferences.edit();
				editor.putString("phone", phnumber.getText().toString());
				editor.putString("plate", aadhar.getText().toString());
				editor.commit();

				Toast.makeText(getApplicationContext(),"Details Updated",Toast.LENGTH_LONG).show();
			}
		});

	}

	private boolean isBlueEnable() {
		BluetoothManager bluetoothManager = (BluetoothManager) getSystemService(BLUETOOTH_SERVICE);
		BluetoothAdapter bluetoothAdapter = bluetoothManager.getAdapter();
		return bluetoothAdapter.isEnabled();

	}

	private boolean havePermissions() {
		for(String permission:mPermissions){
			if(ActivityCompat.checkSelfPermission(this,permission)!= PackageManager.PERMISSION_GRANTED){
				return  false;
			}
		}
		return true;
	}

	private void requestPermissions() {
		ActivityCompat.requestPermissions(this,
				mPermissions, PERMISSIONS_REQUEST_CODE);
	}




	@Override
	public void onRequestPermissionsResult(int requestCode,
										   String permissions[], int[] grantResults) {
		switch (requestCode) {
			case PERMISSION_REQUEST_COARSE_LOCATION: {
				if (grantResults[0] == PackageManager.PERMISSION_GRANTED) {
					Log.d(TAG, "coarse location permission granted");
				} else {
					final AlertDialog.Builder builder = new AlertDialog.Builder(this);
					builder.setTitle("Functionality limited");
					builder.setMessage("Since location access has not been granted, this app will not be able to discover beacons when in the background.");
					builder.setPositiveButton(android.R.string.ok, null);
					builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

						@Override
						public void onDismiss(DialogInterface dialog) {
						}

					});
					builder.show();
				}
				return;
			}
		}
	}

	public void onRangingClicked(View view) {
		Intent myIntent = new Intent(this, RangingActivity.class);
		this.startActivity(myIntent);
	}

    @Override
    public void onResume() {
        super.onResume();
        ((BeaconReferenceApplication) this.getApplicationContext()).setMonitoringActivity(this);
    }

    @Override
    public void onPause() {
        super.onPause();
        ((BeaconReferenceApplication) this.getApplicationContext()).setMonitoringActivity(null);
    }

	private void verifyBluetooth() {

		try {
			if (!BeaconManager.getInstanceForApplication(this).checkAvailability()) {
				final AlertDialog.Builder builder = new AlertDialog.Builder(this);
				builder.setTitle("Bluetooth not enabled");			
				builder.setMessage("Please enable bluetooth in settings and restart this application.");
				builder.setPositiveButton(android.R.string.ok, null);
				builder.setOnDismissListener(new DialogInterface.OnDismissListener() {
					@Override
					public void onDismiss(DialogInterface dialog) {
						finish();
			            System.exit(0);					
					}					
				});
				builder.show();
			}			
		}
		catch (RuntimeException e) {
			final AlertDialog.Builder builder = new AlertDialog.Builder(this);
			builder.setTitle("Bluetooth LE not available");			
			builder.setMessage("Sorry, this device does not support Bluetooth LE.");
			builder.setPositiveButton(android.R.string.ok, null);
			builder.setOnDismissListener(new DialogInterface.OnDismissListener() {

				@Override
				public void onDismiss(DialogInterface dialog) {
					finish();
		            System.exit(0);					
				}
				
			});
			builder.show();
			
		}
		
	}	

    public void logToDisplay(final String line) {
    	runOnUiThread(new Runnable() {
    	    public void run() {

    	    }
    	});
    }

	@Override
	public boolean onCreateOptionsMenu(Menu menu) {
		// Inflate the menu; this adds items to the action bar if it is present.
		getMenuInflater().inflate(R.menu.menu, menu);
		return true;
	}

	@Override
	public boolean onOptionsItemSelected(MenuItem item) {
		// Handle action bar item clicks here. The action bar will
		// automatically handle clicks on the Home/Up button, so long
		// as you specify a parent activity in AndroidManifest.xml.
		int id = item.getItemId();

		//noinspection SimplifiableIfStatement
		if (id == R.id.choose) {


		}


		return super.onOptionsItemSelected(item);
	}


}
