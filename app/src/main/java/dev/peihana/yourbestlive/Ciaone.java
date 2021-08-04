package dev.peihana.yourbestlive;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;

import com.rubengees.introduction.Slide;


public class Ciaone implements Slide.CustomViewBuilder {

    @NonNull
    @Override
    public View buildView(@NonNull LayoutInflater inflater, @NonNull ViewGroup parent) {
        return inflater.inflate(R.layout.single_tutorial, parent, false);
    }
}