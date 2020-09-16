package com.example.tgc_members_app.ui.home;

import android.content.Context;
import android.content.SharedPreferences;
import android.graphics.Bitmap;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProviders;

import com.example.tgc_members_app.R;
import com.example.tgc_members_app.clsGlobal;
import com.google.zxing.WriterException;

import androidmads.library.qrgenearator.QRGContents;
import androidmads.library.qrgenearator.QRGEncoder;

import static android.content.ContentValues.TAG;

public class HomeFragment extends Fragment {
    private SharedPreferences pref;
    private SharedPreferences.Editor editor;
    private String shared_user_id,shared_name,shared_phone,shared_date_time, shared_qr_code;

    private ImageView qr_image;
    private TextView qr_code;

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


        View root = inflater.inflate(R.layout.fragment_home, container, false);
        return root;
    }

        @Override
        public void onViewCreated(View view, @Nullable Bundle savedInstanceState) {
            super.onViewCreated(view, savedInstanceState);
        qr_image = view.findViewById(R.id.qr_code_image_home);
        qr_code = view.findViewById(R.id.qr_code_text);

        qr_code.setText(clsGlobal.shared_qr_code);

        QRGEncoder qrgEncoder = new QRGEncoder(clsGlobal.shared_qr_code,null, QRGContents.Type.TEXT,400);
        Bitmap qrBitmap = qrgEncoder.getBitmap();
        qr_image.setImageBitmap(qrBitmap);

    }
}
