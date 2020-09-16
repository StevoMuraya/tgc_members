package com.example.tgc_members_app.ui.scan;

import android.Manifest;
import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.tgc_members_app.R;
import com.example.tgc_members_app.clsGlobal;
import com.google.android.material.snackbar.Snackbar;
import com.google.zxing.Result;
import com.karumi.dexter.Dexter;
import com.karumi.dexter.PermissionToken;
import com.karumi.dexter.listener.PermissionDeniedResponse;
import com.karumi.dexter.listener.PermissionGrantedResponse;
import com.karumi.dexter.listener.PermissionRequest;
import com.karumi.dexter.listener.single.DialogOnDeniedPermissionListener;
import com.karumi.dexter.listener.single.PermissionListener;

public class ScanFragment extends Fragment {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String shared_user_id,shared_name,shared_phone,shared_date_time, shared_qr_code;

    private CodeScanner codeScanner;
    private CodeScannerView scanView;
    private TextView qr_code_scan;
    private Button retry_scan;
    private View root;

    public View onCreateView(@NonNull LayoutInflater inflater,
                             ViewGroup container, Bundle savedInstanceState) {
        pref = getActivity().getSharedPreferences("uza.conf", Context.MODE_PRIVATE);
        editor = pref.edit();

        shared_user_id = pref.getString("shared_user_id","");
        clsGlobal.shared_user_id = shared_user_id;

        shared_name = pref.getString("shared_name","");
        clsGlobal.shared_name = shared_name;

        shared_phone = pref.getString("shared_phone","");
        clsGlobal.shared_phone = shared_phone;

        shared_date_time = pref.getString("shared_date_time","");
        clsGlobal.shared_date_time = shared_date_time;

        shared_qr_code = pref.getString("shared_qr_code","");
        clsGlobal.shared_qr_code = shared_qr_code;

        root = inflater.inflate(R.layout.fragment_scan, container, false);
        return root;
    }


    @Override
    public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);

        scanView = view.findViewById(R.id.scanner_view);
        codeScanner = new CodeScanner(getActivity(),scanView);
        qr_code_scan = view.findViewById(R.id.qr_code_scan);
        retry_scan = view.findViewById(R.id.retry_scan);

        retry_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                codeScanner.startPreview();
                retry_scan.setVisibility(View.GONE);
                qr_code_scan.setText("");
            }
        });

        codeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {

                getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        qr_code_scan.setText(result.getText());
                        retry_scan.setVisibility(View.VISIBLE);
                    }
                });
            }
        });
    }

    @Override
    public void onResume() {
        super.onResume();
        cameraPermission();
    }

    private void cameraPermission(){
        Dexter.withContext(getActivity())
                .withPermission(Manifest.permission.CAMERA)
                .withListener(new PermissionListener() {
                    @Override public void onPermissionGranted(PermissionGrantedResponse response) {
                        codeScanner.startPreview();
                    }
                    @Override public void onPermissionDenied(PermissionDeniedResponse response) {
                        Snackbar.make(root, "You need to allow camera permission to capture QR Code", Snackbar.LENGTH_LONG).show();
                    }
                    @Override public void onPermissionRationaleShouldBeShown(PermissionRequest permission, PermissionToken token) {
                        token.continuePermissionRequest();
                    }
                }).check();

    }
}
