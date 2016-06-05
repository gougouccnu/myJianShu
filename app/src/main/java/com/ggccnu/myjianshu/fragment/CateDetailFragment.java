package com.ggccnu.myjianshu.fragment;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v4.app.Fragment;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.ggccnu.myjianshu.R;
import com.ggccnu.myjianshu.adapter.ArticleAdapter;
import com.ggccnu.myjianshu.mode.Article;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by lishaowei on 16/6/4.
 */
public class CateDetailFragment extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView rvArticle;
    private List<Article> mArticleList = new ArrayList<>();

    private int mCid;
    private ArticleAdapter mArticleAdapter;
    //use weak reference or can not switch back tab
    //private RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());//TODO: use other get context

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCid = getArguments().getInt("cid");
            List<Article> articleListTemp = (List<Article>) getArguments().getSerializable("articles");
            mArticleList.clear();
            if (articleListTemp != null && articleListTemp.size() > 0) {
                for (int i = 0; i < articleListTemp.size(); i++) {
                    if (articleListTemp.get(i).getCid() == mCid) {
                        mArticleList.add(articleListTemp.get(i));
                    }
                }
            } else {
                Log.d("CateDetailFragment", "get fragment article error!");
            }
        } else {
            Log.d("CateDetailFragment", "getArguments null!");
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.item_cate_detail_fragment, container, false);
        initViews(view);
        return view;
    }


    @Override
    public void onRefresh() {

    }

    private void initViews(View view) {
        rvArticle = (RecyclerView) view.findViewById(R.id.rv_article);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swpRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        rvArticle.setLayoutManager(new LinearLayoutManager(getActivity()));
        mArticleAdapter = new ArticleAdapter(mArticleList);
        rvArticle.setAdapter(mArticleAdapter);
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO:
    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
