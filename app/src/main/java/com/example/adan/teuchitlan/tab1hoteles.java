package com.example.adan.teuchitlan;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import io.reactivex.annotations.Nullable;

/**
 * Created by robert on 4/30/18.
 */

public class tab1hoteles extends Fragment {

    View v;

    public tab1hoteles(){
    }

    @Nullable
    @Override

    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle saveInstanceState){
        v = inflater.inflate(R.layout.tab1hoteles, container, false);
        return v;
    }
}
