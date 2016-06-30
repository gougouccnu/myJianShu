package com.ggccnu.myjianshu.fragment;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.ggccnu.myjianshu.R;
import com.ggccnu.myjianshu.adapter.ArticleAdapter;
import com.ggccnu.myjianshu.mode.Article;
import com.ggccnu.myjianshu.widget.BaseFragment;

import java.util.ArrayList;
import java.util.List;

import cn.bmob.v3.BmobQuery;
import cn.bmob.v3.listener.FindListener;

/**
 * Created by lishaowei on 16/6/4.
 */
public class CateDetailFragment extends BaseFragment implements SwipeRefreshLayout.OnRefreshListener {

    private static final String TAG = "CateDetailFragment" ;
    private static final int UPDATE_ARTICLES = 1;
    private static final int FIRST_LOAD_ITEMS = 2;
    private static final int LOAD_NEW_ITEMS = 3;
    private SwipeRefreshLayout mSwipeRefreshLayout;
    private RecyclerView rvArticle;
    private List<Article> mArticleList = new ArrayList<>();

    private int mCid;
    private ArticleAdapter mArticleAdapter;
    private Handler mUpdateItemsHandler,mLoadItemsHandler;
    //use weak reference or can not switch back tab
    //private RecyclerView.LayoutManager mLayoutManager = new LinearLayoutManager(getActivity());//TODO: use other get context

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        if (getArguments() != null) {
            mCid = getArguments().getInt("cid");
            queryArticlesByCategoryID(mCid);
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
        queryNewArticlesByCategoryID(mCid);
         mUpdateItemsHandler =  new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if (msg.what == LOAD_NEW_ITEMS) {
                    // notify data changed
                    mArticleAdapter.notifyDataSetChanged();
                    mSwipeRefreshLayout.setRefreshing(false);
                }
            }
        };
        mSwipeRefreshLayout.postDelayed(new Runnable() {
            @Override
            public void run() {
                mSwipeRefreshLayout.setRefreshing(false);
            }
        }, 2000);
    }



    private void initViews(View view) {
        rvArticle = (RecyclerView) view.findViewById(R.id.rv_article);
        mSwipeRefreshLayout = (SwipeRefreshLayout) view.findViewById(R.id.swpRefreshLayout);
        mSwipeRefreshLayout.setOnRefreshListener(this);

        rvArticle.setLayoutManager(new LinearLayoutManager(getActivity()));
        mLoadItemsHandler = new Handler() {
            @Override
            public void handleMessage(Message msg) {
                if(msg.what == FIRST_LOAD_ITEMS) {
                    mArticleAdapter = new ArticleAdapter(mArticleList, getActivity());
                    rvArticle.setAdapter(mArticleAdapter);
                    mArticleAdapter.setOnItemClickLitener(new ArticleAdapter.OnItemClickLitener() {
                        @Override
                        public void onItemClick(View view, int position) {
                            Toast.makeText(getActivity(), "item " + position + " clicked", Toast.LENGTH_LONG).show();
                        }

                        @Override
                        public void onItemLongClick(View view, int position) {

                        }
                    });
                }
            }
        };
    }

    private void queryArticlesByCategoryID(Integer categoryID) {
        BmobQuery<Article> articlesBmobQuery = new BmobQuery<>();
        articlesBmobQuery.order("createdAt");
        articlesBmobQuery.addWhereEqualTo("cid", categoryID);
        articlesBmobQuery.findObjects(getActivity(), new FindListener<Article>() {
            @Override
            public void onSuccess(List<Article> list) {
                if (list != null && list.size() > 0) {
                    mArticleList.addAll(list);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message msg = new Message();
                            msg.what = FIRST_LOAD_ITEMS;
                            //msg.obj = mArticleList;
                            mLoadItemsHandler.sendMessage(msg);
                        }
                    }).start();
                }
                Log.d(TAG, "query new Articles onSuccess");
            }

            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "query new Articles onError");
            }
        });
    }

    private void queryNewArticlesByCategoryID(Integer categoryID) {
        BmobQuery<Article> articlesBmobQuery = new BmobQuery<>();
        articlesBmobQuery.order("createdAt");
        articlesBmobQuery.addWhereEqualTo("cid", categoryID);
        articlesBmobQuery.findObjects(getActivity(), new FindListener<Article>() {
            @Override
            public void onSuccess(List<Article> list) {
                if (list != null && list.size() > 0) {
                    mArticleList.addAll(list);
                    new Thread(new Runnable() {
                        @Override
                        public void run() {
                            Message msg = new Message();
                            msg.what = LOAD_NEW_ITEMS;
                            //msg.obj = mArticleList;
                            mUpdateItemsHandler.sendMessage(msg);
                        }
                    }).start();
                }
                Log.d(TAG, "query new Articles onSuccess");
            }

            @Override
            public void onError(int i, String s) {
                Log.d(TAG, "query new Articles onError");
            }
        });
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        // TODO:
    }

    @Override
    public void fetchData() {

    }

    @Override
    public void onDestroyView() {
        super.onDestroyView();
    }
}
