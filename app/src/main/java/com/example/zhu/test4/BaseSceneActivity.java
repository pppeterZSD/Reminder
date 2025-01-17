package com.example.zhu.test4;

import android.os.Bundle;
import android.support.annotation.IdRes;
import android.support.annotation.LayoutRes;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.transition.Scene;
import android.transition.Transition;
import android.transition.TransitionManager;
import android.view.View;
import android.view.ViewGroup;


public abstract class BaseSceneActivity extends AppCompatActivity {
    protected Scene scene1;
    protected Scene scene2;
    protected boolean isScene2;

    @Override
    protected void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    abstract Transition getTransition();
    protected void switchScene(Transition transition){
        TransitionManager.go(isScene2?scene1:scene2,transition);
        isScene2=!isScene2;
    }
    @Override
    public boolean onSupportNavigateUp() {
        onBackPressed();
        return true;
    }


    protected void initScene(@IdRes int rootView, @LayoutRes int scene1_layout,@LayoutRes int scene2_layout) {
        ViewGroup sceneRoot= (ViewGroup) findViewById(rootView);
        scene1= Scene.getSceneForLayout(sceneRoot,scene1_layout,this);
        scene2=Scene.getSceneForLayout(sceneRoot,scene2_layout,this);
        TransitionManager.go(scene1);
    }

    public void change(View view){
        switchScene(getTransition());
    }
}
