package com.ldy.drawer.chart;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.util.Log;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private LinearLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mLayout = (LinearLayout) findViewById(R.id.layout_chart);
        addChartView();

        final ArcHeaderView headerView = (ArcHeaderView) findViewById(R.id.arc_header_view);

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1f, 0.5f, 1.0f, 0.5f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                Log.i("MainActivity", "onAnimationUpdate value=" + value);
                headerView.setControlOffset(value);
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(4000L);
        valueAnimator.start();
    }

    private void addChartView() {
        mLayout.removeAllViews();
        ComboView view = new ComboView(this);
        view.setList(getList());
        mLayout.addView(view);
    }
    private ArrayList<Bean> getList() {
        ArrayList<Bean> list = new ArrayList<Bean>();
        for (int i = 0; i < 8; i ++) {
            Bean bean = new Bean();
            bean.age = (i % 2 == 0) ? (42 + i * 2) : (30 - i * 2);
            bean.name = (i % 2 == 0) ? "速度" : "你妹";
            list.add(bean);
        }
        return list;
    }
}
