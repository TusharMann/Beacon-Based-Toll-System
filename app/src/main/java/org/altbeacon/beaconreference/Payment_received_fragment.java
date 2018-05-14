package org.altbeacon.beaconreference;

import android.content.SharedPreferences;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;

import java.util.HashMap;
import java.util.Map;

import static android.content.Context.MODE_PRIVATE;


public class Payment_received_fragment extends Fragment {

    FirebaseFirestore fs;

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view=inflater.inflate(R.layout.fragment_payment_received, container, false);
        TextView textView=(TextView)view.findViewById(R.id.payment);
        fs=FirebaseFirestore.getInstance();

        SharedPreferences sharedPreferences = getActivity().getSharedPreferences("Details",MODE_PRIVATE);
        String phone=sharedPreferences.getString("phone","NULL");
        String plate=sharedPreferences.getString("numplate","NULL");
        String aadhar=sharedPreferences.getString("aadhar","NULL");

        Map<String, Object> pay = new HashMap<>();
        pay.put("Phone Number",phone );
        pay.put("Aadhar Number",aadhar);
        pay.put("Vehicle Number",plate);
        pay.put("Time",new java.util.Date());

        fs.collection("payment")
                .add(pay)
                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                    @Override
                    public void onSuccess(DocumentReference documentReference) {
                        Log.d("aag1", "User data added with ID: " + documentReference.getId());
                    }
                })
                .addOnFailureListener(new OnFailureListener() {
                    @Override
                    public void onFailure(@NonNull Exception e) {
                        Log.w("aag1", "Error adding document", e);
                    }
                });

        String result="Payment for \nCar number "+plate+" received from \nPhone "+phone+"\nat "+new java.util.Date();
        textView.setText(result);


        return view;
    }

}