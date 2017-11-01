package com.ldy.drawer.chart;

import android.animation.ValueAnimator;
import android.app.Activity;
import android.os.Bundle;
import android.view.animation.LinearInterpolator;
import android.widget.LinearLayout;

import com.ldy.drawer.chart.view.ArcHeaderView;
import com.ldy.drawer.chart.view.CubeProgressView;
import com.ldy.drawer.chart.view.combo.Bean;
import com.ldy.drawer.chart.view.combo.ComboView;
import com.ldy.drawer.chart.view.analyse.CircleAnalyseInfo;
import com.ldy.drawer.chart.view.analyse.CircleAnalyseView;

import java.util.ArrayList;

public class MainActivity extends Activity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        initArcHeaderView();
        initChartView();
        initCubeProgressView();
        initCircleAnalyseView();
    }

    private void initCircleAnalyseView() {
        CircleAnalyseView analyzeView1 = (CircleAnalyseView) findViewById(R.id.arc_analyze_view1);
        analyzeView1.setAnalyzeList(getAnalyzeList(8));

        CircleAnalyseView analyzeView2 = (CircleAnalyseView) findViewById(R.id.arc_analyze_view2);
        analyzeView2.setAnalyzeList(getAnalyzeList(7));

        CircleAnalyseView analyzeView3 = (CircleAnalyseView) findViewById(R.id.arc_analyze_view3);
        analyzeView3.setAnalyzeList(getAnalyzeList(6));

        CircleAnalyseView analyzeView4 = (CircleAnalyseView) findViewById(R.id.arc_analyze_view4);
        analyzeView4.setAnalyzeList(getAnalyzeList(5));

        CircleAnalyseView analyzeView5 = (CircleAnalyseView) findViewById(R.id.arc_analyze_view5);
        analyzeView5.setAnalyzeList(getAnalyzeList(4));

        CircleAnalyseView analyzeView6 = (CircleAnalyseView) findViewById(R.id.arc_analyze_view6);
        analyzeView6.setAnalyzeList(getAnalyzeList(3));

        CircleAnalyseView analyzeView7 = (CircleAnalyseView) findViewById(R.id.arc_analyze_view7);
        analyzeView7.setAnalyzeList(getAnalyzeList(2));

        CircleAnalyseView analyzeView8 = (CircleAnalyseView) findViewById(R.id.arc_analyze_view8);
        analyzeView8.setAnalyzeList(getAnalyzeList(1));
    }

    private void initCubeProgressView() {
        CubeProgressView progressBar1 = (CubeProgressView) findViewById(R.id.cube_progress_bar_1);
        progressBar1.setCubeProgress(75);

        CubeProgressView progressBar2 = (CubeProgressView) findViewById(R.id.cube_progress_bar_2);
        progressBar2.setCubeMax(200);
        progressBar2.setCubeProgress(76);

        CubeProgressView progressBar3 = (CubeProgressView) findViewById(R.id.cube_progress_bar_3);
        progressBar3.setCubeProgress(10);
    }

    private void initArcHeaderView() {
        final ArcHeaderView headerView = (ArcHeaderView) findViewById(R.id.arc_header_view);
        headerView.setControlOffset(0.5f, false);

        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1f, 0.5f, 1.0f, 0.5f, 1.0f, 0.5f, 1.0f, 0.5f);
        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
            @Override
            public void onAnimationUpdate(ValueAnimator animation) {
                float value = (float) animation.getAnimatedValue();
                headerView.setControlOffset(value, true);
            }
        });
        valueAnimator.setInterpolator(new LinearInterpolator());
        valueAnimator.setDuration(10000L);
        valueAnimator.start();
    }

    private void initChartView() {
        LinearLayout layout = (LinearLayout) findViewById(R.id.layout_chart);
        layout.removeAllViews();
        ComboView view = new ComboView(this);
        view.setList(getList());
        layout.addView(view);
    }

    private ArrayList<Bean> getList() {
        ArrayList<Bean> list = new ArrayList<>();
        for (int i = 0; i < 8; i ++) {
            Bean bean = new Bean();
            bean.age = (i % 2 == 0) ? (42 + i * 2) : (30 - i * 2);
            bean.name = (i % 2 == 0) ? "速度" : "你妹";
            list.add(bean);
        }
        return list;
    }

    private ArrayList<CircleAnalyseInfo> getAnalyzeList(int count) {
        ArrayList<CircleAnalyseInfo> list = new ArrayList<>();
        for (int i = 0; i < count; i ++) {
            CircleAnalyseInfo analyzeInfo = new CircleAnalyseInfo();
            analyzeInfo.setValue("关系技能爱" + i);
            analyzeInfo.setRate(0.8f - (0.05f * i));
            list.add(analyzeInfo);
        }
        return list;
    }

}
