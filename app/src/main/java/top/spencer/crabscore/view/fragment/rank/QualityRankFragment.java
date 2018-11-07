package top.spencer.crabscore.view.fragment.rank;

import android.os.Bundle;
import android.os.Handler;
import android.os.Looper;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.TextView;
import butterknife.BindView;
import com.alibaba.fastjson.JSONObject;
import top.spencer.crabscore.R;
import top.spencer.crabscore.base.BaseFragment;
import top.spencer.crabscore.model.entity.Competition;
import top.spencer.crabscore.model.entity.Group;
import top.spencer.crabscore.presenter.RankListPresenter;
import top.spencer.crabscore.util.SharedPreferencesUtil;
import top.spencer.crabscore.view.adapter.QualityRankListAdapter;
import top.spencer.crabscore.view.view.MyRecycleListView;
import top.spencer.crabscore.view.widget.EmptyRecyclerView;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

/**
 * @author spencercjh
 */
public class QualityRankFragment extends BaseFragment implements MyRecycleListView {
    @BindView(R.id.recycler_view_list)
    EmptyRecyclerView rankListView;
    @BindView(R.id.textview_empty)
    TextView emptyText;
    @BindView(R.id.swipe_refresh_layout)
    SwipeRefreshLayout swipeRefreshLayout;
    private Competition presentCompetition;
    private QualityRankListAdapter qualityRankListAdapter;
    private RankListPresenter rankListPresenter;
    private List<Group> groupList = new ArrayList<>(10);
    private int pageNum = 1;
    private boolean repeat = false;

    /**
     * 取得实例
     *
     * @param name 测试参数
     * @return fragment
     */
    public static QualityRankFragment newInstance(String name) {
        Bundle args = new Bundle();
        args.putString("name", name);
        QualityRankFragment fragment = new QualityRankFragment();
        fragment.setArguments(args);
        return fragment;
    }

    /**
     * 重写onDestroyView 断开view
     */
    @Override
    public void onDestroyView() {
        super.onDestroyView();
        rankListPresenter.detachView();
    }

    /**
     * 获得fragment的layout的Id
     *
     * @return layout Id
     */
    @Override
    public int getContentViewId() {
        return R.layout.fragment_list;
    }

    /**
     * 初始化视图
     *
     * @param view               view
     * @param savedInstanceState saveInstanceState
     */
    @SuppressWarnings("Duplicates")
    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        rankListPresenter = new RankListPresenter();
        rankListPresenter.attachView(this);
        SharedPreferencesUtil.getInstance(getContext(), "PROPERTY");
        presentCompetition = (Competition) (SharedPreferencesUtil.getData(
                "PRESENT_COMPETITION", new Competition()));
        setRecycleView();
    }

    /**
     * 初始化RecycleView
     */
    @Override
    public void setRecycleView() {
        qualityRankListAdapter = new QualityRankListAdapter(groupList);
        if (groupList.size() == 0) {
            rankListView.setEmptyView(emptyText);
        }
        rankListView.setAdapter(qualityRankListAdapter);
        swipeRefreshLayout.setOnRefreshListener(new SwipeRefreshLayout.OnRefreshListener() {
            @Override
            public void onRefresh() {
                rankListPresenter.getQualityRank(presentCompetition.getCompetitionId(), pageNum, pageSize);
            }
        });
        final LinearLayoutManager layoutManager = new LinearLayoutManager(getContext());
        rankListView.setLayoutManager(layoutManager);
        rankListView.addItemDecoration(new DividerItemDecoration(Objects.requireNonNull(getContext()),
                DividerItemDecoration.VERTICAL));
        final int[] lastVisibleItemPosition = {0};
        rankListView.addOnScrollListener(new RecyclerView.OnScrollListener() {
            @Override
            public void onScrollStateChanged(RecyclerView recyclerView, int newState) {
                super.onScrollStateChanged(recyclerView, newState);
                if (newState == RecyclerView.SCROLL_STATE_IDLE
                        && lastVisibleItemPosition[0] + 1 == qualityRankListAdapter.getItemCount()) {
                    swipeRefreshLayout.setRefreshing(false);
                    if (!repeat) {
                        rankListPresenter.getQualityRank(presentCompetition.getCompetitionId(), pageNum, pageSize);
                    }
                }
            }

            @Override
            public void onScrolled(RecyclerView recyclerView, int dx, int dy) {
                super.onScrolled(recyclerView, dx, dy);
                lastVisibleItemPosition[0] = layoutManager.findLastVisibleItemPosition();

            }
        });
    }

    @Override
    public void showResponse1(JSONObject successData) {

    }

    /**
     * 初始发起请求
     *
     * @param savedInstanceState savedInstanceState
     */
    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
        rankListPresenter.getQualityRank(presentCompetition.getCompetitionId(), pageNum, pageSize);
    }

    /**
     * getQualityRank请求成功
     *
     * @param successData 成功数据源
     */
    @Override
    public void showData(JSONObject successData) {
        pageNum++;
        repeat = rankListPresenter.dealGroupListJSON(successData.getJSONArray("result"), groupList);
        if (repeat) {
            return;
        }
        new Handler(Looper.getMainLooper()).post(new Runnable() {

            @Override
            public void run() {
                swipeRefreshLayout.setRefreshing(false);
                qualityRankListAdapter.notifyDataSetChanged();
            }
        });

    }
}