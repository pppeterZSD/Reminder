package com.example.zhu.test4;

import android.animation.ArgbEvaluator;
import android.content.Context;
import android.graphics.Canvas;
import android.graphics.LinearGradient;
import android.graphics.Paint;
import android.graphics.Shader;
import android.os.Build;
import android.support.annotation.ArrayRes;
import android.support.annotation.RequiresApi;
import android.util.AttributeSet;
import android.util.Log;
import android.view.Gravity;
import android.widget.LinearLayout;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;


public class GradientView extends LinearLayout {

    private Paint gradientPaint;
    private int[] currentGradient;
    private TextView date;
    private TextView name;
    private ArgbEvaluator evaluator;
    private int flag=0;
    private ArrayList<Integer> staticOlePosition=new ArrayList<>();
    private ArrayList<Integer> staticNewPosition=new ArrayList<>();

    public GradientView(Context context) {
        super(context);
    }

    public GradientView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public GradientView(Context context, AttributeSet attrs, int defStyleAttr) {
        super(context, attrs, defStyleAttr);
    }

    @RequiresApi(api = Build.VERSION_CODES.LOLLIPOP)
    public GradientView(Context context, AttributeSet attrs, int defStyleAttr, int defStyleRes) {
        super(context, attrs, defStyleAttr, defStyleRes);
    }

    {
        evaluator = new ArgbEvaluator();

        gradientPaint = new Paint(Paint.ANTI_ALIAS_FLAG);
        setWillNotDraw(false);

        setOrientation(VERTICAL);
        setGravity(Gravity.CENTER_HORIZONTAL);

    }

    private void initGradient() {
        float centerX = getWidth() * 0.5f;
        Shader gradient = new LinearGradient(
                centerX, 0, centerX, getHeight(),
                currentGradient, null,
                Shader.TileMode.MIRROR);
        gradientPaint.setShader(gradient);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        if (currentGradient != null) {
            initGradient();
        }
    }

    @Override
    protected void onDraw(Canvas canvas) {
        canvas.drawRect(0, 0, getWidth(), getHeight(), gradientPaint);
        super.onDraw(canvas);
    }

    public void setForecast(int position) {
        currentGradient = weatherToGradient(position%4);
        if (getWidth() != 0 && getHeight() != 0) {
            initGradient();
        }

        invalidate();

    }

    public void onScroll(float fraction,int oldPosition,int newPosition) {
        Log.d("realposition=",oldPosition%4+"/"+newPosition%4);
        Log.d("fraction=",fraction+"");
//        if(fraction<0.2){

        if(staticOlePosition.size()==0){
            staticOlePosition.add(oldPosition%4);
        }
        if(fraction==0){
            staticOlePosition.set(0,staticNewPosition.get(0));
            staticNewPosition.clear();
            Log.d("size=",staticNewPosition.size()+"");
            staticNewPosition.add((staticOlePosition.get(0)+1)%4);
            return;
        }

        if(staticNewPosition.size()==0){
                staticNewPosition.add(newPosition%4);
        }

            currentGradient = mix(1-fraction,
                    weatherToGradient(staticOlePosition.get(0)),
                    weatherToGradient(staticNewPosition.get(0)));
//        Log.d("getposition=",staticOlePosition.get(0)+"/"+staticNewPosition.get(0));


        initGradient();
        invalidate();
    }

    private int[] mix(float fraction, int[] c1, int[] c2) {
        return new int[]{
                (Integer) evaluator.evaluate(fraction, c1[0], c2[0]),
                (Integer) evaluator.evaluate(fraction, c1[1], c2[1]),
                (Integer) evaluator.evaluate(fraction, c1[2], c2[2])
        };
    }

    private int[] weatherToGradient(int positon) {

        switch (positon) {

            case 0:
                return colors(R.array.gradientClear);  //mint

            case 1:
                return colors(R.array.gradientMostlyCloudy); //yellow

            case 2:
                return colors(R.array.gradientPeriodicClouds); //redred

            case 3:
                return colors(R.array.gradientCloudy);   //darkblue

            case 4:
                return colors(R.array.gradientPeriodicClouds); //red

            default:
                throw new IllegalArgumentException();
        }
    }

    private int[] colors(@ArrayRes int res) {
        return getContext().getResources().getIntArray(res);
    }

}
