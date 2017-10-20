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

        EvaluationAnalyseView analyzeView1 = (EvaluationAnalyseView) findViewById(R.id.arc_analyze_view1);
        analyzeView1.setAnalyzeList(getAnalyzeList(8));
        EvaluationAnalyseView analyzeView2 = (EvaluationAnalyseView) findViewById(R.id.arc_analyze_view2);
        analyzeView2.setAnalyzeList(getAnalyzeList(7));
        EvaluationAnalyseView analyzeView3 = (EvaluationAnalyseView) findViewById(R.id.arc_analyze_view3);
        analyzeView3.setAnalyzeList(getAnalyzeList(6));
        EvaluationAnalyseView analyzeView4 = (EvaluationAnalyseView) findViewById(R.id.arc_analyze_view4);
        analyzeView4.setAnalyzeList(getAnalyzeList(5));
        EvaluationAnalyseView analyzeView5 = (EvaluationAnalyseView) findViewById(R.id.arc_analyze_view5);
        analyzeView5.setAnalyzeList(getAnalyzeList(4));
        EvaluationAnalyseView analyzeView6 = (EvaluationAnalyseView) findViewById(R.id.arc_analyze_view6);
        analyzeView6.setAnalyzeList(getAnalyzeList(3));
        EvaluationAnalyseView analyzeView7 = (EvaluationAnalyseView) findViewById(R.id.arc_analyze_view7);
        analyzeView7.setAnalyzeList(getAnalyzeList(2));
        EvaluationAnalyseView analyzeView8 = (EvaluationAnalyseView) findViewById(R.id.arc_analyze_view8);
        analyzeView8.setAnalyzeList(getAnalyzeList(1));
    }

    private ArrayList<EvaluationAnalyseInfo> getAnalyzeList(int count) {
        ArrayList<EvaluationAnalyseInfo> list = new ArrayList<>();
        for (int i = 0; i < count; i ++) {
            EvaluationAnalyseInfo analyzeInfo = new EvaluationAnalyseInfo();
            analyzeInfo.setValue("关系技能爱" + i);
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
