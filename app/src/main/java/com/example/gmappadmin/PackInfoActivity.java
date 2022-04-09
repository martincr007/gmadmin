package com.example.gmappadmin;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.FragmentTransaction;
import android.os.Bundle;

public class PackInfoActivity extends AppCompatActivity {
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_pack_info);
        //AppPremiumFragment packFragment = new AppPremiumFragment();
        AppLiteFragment packFragment = new AppLiteFragment();
        FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
        fragmentTransaction.replace(R.id.frame_layout_pack, packFragment);
        fragmentTransaction.commit();
    }
}