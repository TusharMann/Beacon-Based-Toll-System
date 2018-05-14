package org.altbeacon.beaconreference;


import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.google.firebase.firestore.FirebaseFirestore;

public class Waiting_Fragment extends Fragment {

    FirebaseFirestore fs;
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        // Inflate the layout for this fragment
        View view= inflater.inflate(R.layout.fragment_waiting, container, false);
       /* fs=FirebaseFirestore.getInstance();
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
                });*/

        return view;
    }

}
