package com.bebeep.doubleslidemenu;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.databinding.DataBindingUtil;
import android.os.Bundle;
import android.util.Log;
import android.view.View;

import com.bebeep.doubleslidemenu.databinding.ActivityMainBinding;
import com.bebeep.slidemenu.DoubleSlideMenu;


public class MainActivity extends Activity implements View.OnClickListener{
    private ActivityMainBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        binding = DataBindingUtil.setContentView(this,R.layout.activity_main);
        init();
    }

    @SuppressLint("NewApi")
    private void init(){

        binding.flMain.setMySlideMenu(binding.msmParent);
        binding.msmParent.setMenuLocation(DoubleSlideMenu.DragState.LEFT);
        binding.msmParent.setOffsetX(0.6f);
        binding.msmParent.setOnDragstateChangeListener(new DoubleSlideMenu.onDragStateChangeListener() {
            @Override
            public void onOpen() {
                Log.e("TAG","open");
            }

            @Override
            public void onClose() {
                Log.e("TAG","close");
            }

            @Override
            public void onDraging(float fraction) {
//                Log.e("TAG","Draging:"+fraction);
            }
        });


        binding.tvMenu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.e("TAG","text");
            }
        });

    }





    @Override
    public void onClick(View v) {
        switch (v.getId()){
            case R.id.btn_open:
                binding.msmParent.setMenuLocation(DoubleSlideMenu.DragState.LEFT);
                break;
            case R.id.btn_close:
                binding.msmParent.setMenuLocation(DoubleSlideMenu.DragState.RIGHT);
                break;
        }
    }





}
