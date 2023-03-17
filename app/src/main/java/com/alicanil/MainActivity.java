package com.alicanil;

import android.app.Activity;
import android.app.FragmentTransaction;
import android.content.Intent;
import android.os.Bundle;

public class MainActivity extends Activity{

    private MainFragment frag;
    private ShareFragment shareFragment;
    private String from = "main";

    @Override
    // ana sayfa activity mainin olusturulmasi
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        if(findViewById(R.id.container) != null){

            if (savedInstanceState != null) {
                return;
            }
            frag = new MainFragment();
            FragmentTransaction transaction = getTransaction();
            transaction.add(R.id.container, frag);
            transaction.addToBackStack(null);
            transaction.commit();
            from = "main";

        }
    }
    private FragmentTransaction getTransaction(){
        FragmentTransaction transaction = getFragmentManager().beginTransaction();
        return transaction;
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if(from.equals("main"))
            frag.onActivityResult(requestCode, resultCode, data);
        else if(from.equals("share"))
            shareFragment.onActivityResult(requestCode, resultCode, data);
    }
}
