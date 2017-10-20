package com.ldy.drawer.chart;

import android.app.Activity;
import android.os.Bundle;
import android.widget.LinearLayout;

import java.util.ArrayList;

public class MainActivity extends Activity {

    private LinearLayout mLayout;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

//        mLayout = (LinearLayout) findViewById(R.id.layout_chart);
//        addChartView();
//
//        final ArcHeaderView headerView = (ArcHeaderView) findViewById(R.id.arc_header_view);
//        headerView.setControlOffset(0.5f, false);
//
//        ValueAnimator valueAnimator = ValueAnimator.ofFloat(1f, 0.5f, 1.0f, 0.5f, 1.0f, 0.5f, 1.0f, 0.5f);
//        valueAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener() {
//            @Override
//            public void onAnimationUpdate(ValueAnimator animation) {
//                float value = (float) animation.getAnimatedValue();
//                Log.i("MainActivity", "onAnimationUpdate value=" + value);
//                headerView.setControlOffset(value, true);
//            }
//        });
//        valueAnimator.setInterpolator(new LinearInterpolator());
//        valueAnimator.setDuration(10000L);
//        valueAnimator.start();


        CubeProgressView progressBar1 = (CubeProgressView) findViewById(R.id.cube_progress_bar_1);
        progressBar1.setCubeProgress(30);

        CubeProgressView progressBar2 = (CubeProgressView) findViewById(R.id.cube_progress_bar_2);
        progressBar2.setCubeProgress(45);

        CubeProgressView progressBar3 = (CubeProgressView) findViewById(R.id.cube_progress_bar_3);
        progressBar3.setCubeProgress(1);

        EvaluationAnalyseView analyzeView = (EvaluationAnalyseView) findViewById(R.id.arc_analyze_view);
        analyzeView.setAnalyzeList(getAnalyzeList());
    }

    private ArrayList<EvaluationAnalyseInfo> getAnalyzeList() {
        ArrayList<EvaluationAnalyseInfo> list = new ArrayList<>();
        for (int i = 0; i < 8; i ++) {
            EvaluationAnalyseInfo analyzeInfo = new EvaluationAnalyseInfo();
            analyzeInfo.setValue("关系技能爱能爱");
            analyzeInfo.setRate(0.8f - (0.05f * i));
            list.add(analyzeInfo);
        }
        return list;
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
