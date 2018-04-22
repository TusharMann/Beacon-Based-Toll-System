package org.altbeacon.beaconreference;

import android.Manifest;
import android.annotation.TargetApi;
import android.app.AlertDialog;
import android.app.Dialog;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothManager;
import android.content.ContentValues;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.Build;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.ActivityCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import org.altbeacon.beacon.BeaconManager;

import java.util.HashMap;
import java.util.Map;

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
	ImageView imageView;
	String phone;

	TT_Sqlite sqlite;
	SQLiteDatabase db;
	FirebaseFirestore fs;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		Log.d(TAG, "onCreate");
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_monitoring);
		sqlite=new TT_Sqlite(this,1);
		db=sqlite.getWritableDatabase();
		fs = FirebaseFirestore.getInstance();
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
		imageView=(ImageView) findViewById(R.id.add) ;

		Button update,drive;
		update=(Button)findViewById(R.id.update);
		drive=(Button)findViewById(R.id.driving);

		sharedPreferences=getSharedPreferences("Details",MODE_PRIVATE);
		phone=sharedPreferences.getString("phone","NULL");
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

				Map<String, Object> user = new HashMap<>();
				user.put("Phone Number", phnumber.getText().toString());
				user.put("Aadhar Number", aadhar.getText().toString());

// Add a new document with a generated ID
				fs.collection("user")
						.add(user)
						.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
							@Override
							public void onSuccess(DocumentReference documentReference) {
								Log.d("aag1", "DocumentSnapshot added with ID: " + documentReference.getId());
							}
						})
						.addOnFailureListener(new OnFailureListener() {
							@Override
							public void onFailure(@NonNull Exception e) {
								Log.w("aag1", "Error adding document", e);
							}
						});



				Toast.makeText(getApplicationContext(),"Details Updated",Toast.LENGTH_LONG).show();
			}
		});

		imageView.setOnClickListener(new View.OnClickListener() {
			@Override
			public void onClick(View view) {

				if(phone.equals("NULL")) {
					Toast.makeText(getApplicationContext(),"Enter user details first",Toast.LENGTH_LONG).show();
				}

				else{
					Dialog dialog=onCreateDialogInput();
					dialog.show();
				}

			}
		});
	}

	public Dialog onCreateDialogInput() {


		View viewInflated = LayoutInflater.from(getApplication()).inflate(R.layout.dialogview,null);
		final EditText numplate = (EditText) viewInflated.findViewById(R.id.numplate);
		final EditText rc = (EditText) viewInflated.findViewById(R.id.rc);

		AlertDialog.Builder builder = new AlertDialog.Builder(this);
		builder.setTitle("Enter Vehilce Details");
		builder.setView(viewInflated);


		builder.setPositiveButton("Add", new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				String num=numplate.getText().toString();
				String reg=rc.getText().toString();

				ContentValues cv=new ContentValues();
				cv.put(TT_Sqlite.pnum,num);
				cv.put(TT_Sqlite.rc,reg);
				db.insert(TT_Sqlite.Tname,null,cv);

				Map<String, Object> car = new HashMap<>();
				car.put("Number Plate", num);
				car.put("Registration Certificate", reg);
				car.put("User id", phone);

// Add a new document with a generated ID
				fs.collection("car")
						.add(car)
						.addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
							@Override
							public void onSuccess(DocumentReference documentReference) {
								Log.d("aag1", "DocumentSnapshot added with ID: " + documentReference.getId());
							}
						})
						.addOnFailureListener(new OnFailureListener() {
							@Override
							public void onFailure(@NonNull Exception e) {
								Log.w("aag1", "Error adding document", e);
							}
						});


				dialog.dismiss();

				Toast.makeText(getApplicationContext(),"Vehilce Added successfully",Toast.LENGTH_LONG).show();
			}
		});
		builder.setNegativeButton(android.R.string.cancel, new DialogInterface.OnClickListener() {
			@Override
			public void onClick(DialogInterface dialog, int which) {
				dialog.cancel();
			}
		});

		return builder.create();


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
			AlertDialog.Builder builderSingle = new AlertDialog.Builder(this);
			builderSingle.setTitle("Choose Vehicle");

			final ArrayAdapter<String> arrayAdapter = new ArrayAdapter<String>(this, android.R.layout.select_dialog_singlechoice);

			String[] columns={TT_Sqlite.pnum,TT_Sqlite.rc};
			Cursor cursor=db.query(TT_Sqlite.Tname,columns,null,null,null,null,null,null);
			while(cursor.moveToNext()) {

				int index1 = cursor.getColumnIndex(TT_Sqlite.pnum);
				int index2 = cursor.getColumnIndex(TT_Sqlite.rc);

				String plate = cursor.getString(index1);
				String reg = cursor.getString(index2);
				arrayAdapter.add(plate);
			}

			builderSingle.setAdapter(arrayAdapter, new DialogInterface.OnClickListener() {
				@Override
				public void onClick(DialogInterface dialogInterface, int i) {
					String plate=arrayAdapter.getItem(i);
					Toast.makeText(getApplicationContext(),plate,Toast.LENGTH_LONG).show();
				}
			});

			builderSingle.show();

			}


		return super.onOptionsItemSelected(item);
	}


}
