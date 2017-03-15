package com.examplem.myrecycler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

// 瀑布流
public class StaggerActivity extends AppCompatActivity {

    /**
     * 1. 设置布局管理：StaggeredGridLayoutMananger
     * 2. 设置分割线、动画
     * 3. 设置适配器、设置数据(展示的数据，高度)
     */

    @BindView(R.id.recyclerView_stagger)
    RecyclerView mRecyclerView;
    private StaggerAdapter mStaggerAdapter;
    private List<String> mData;
    private List<Integer> mHeights;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_stagger);
        ButterKnife.bind(this);

        initView();

        initData();
    }

    // 初始化数据
    private void initData() {

        mData = new ArrayList<>();
        mHeights = new ArrayList<>();
        for (int i='A';i<'z';i++){
            mData.add(""+(char)i);

            // 采用随机数来添加:Math.random():0-1
            mHeights.add((int) (200+Math.random()*400));
        }
        mStaggerAdapter.setData(mData, mHeights);
    }

    // 初始化视图
    private void initView() {
        mRecyclerView.setLayoutManager(new StaggeredGridLayoutManager(3, StaggeredGridLayoutManager.VERTICAL));
        // 3. 设置分割线:默认提供的一个：DividerItemDecoration，都可以自己定义，可以在item布局中设置
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL));

        mStaggerAdapter = new StaggerAdapter();
        mRecyclerView.setAdapter(mStaggerAdapter);

        // 设置拖动事件
        ItemTouchHelper.Callback callback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP| ItemTouchHelper.DOWN| ItemTouchHelper.LEFT| ItemTouchHelper.RIGHT,0) {

            // 拖动
            @Override
            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {

                int fromPosition = viewHolder.getAdapterPosition();// 得到拖动的ViewHolder的position
                int toPosition = target.getAdapterPosition();// 得到目标的ViewHolder的position

                // 向下移动
                if (fromPosition<toPosition){

                    // 分别把中间的item位置进行调换
                    for (int i=fromPosition;i<toPosition;i++){
                        // 数据集合：通过一个集合的工具类Collections
                        Collections.swap(mData,i,i+1);
                    }

                }else {

                    // 向上
                    for (int i=fromPosition;i>toPosition;i--){
                        Collections.swap(mData,i,i-1);
                    }
                }
                mStaggerAdapter.itemMoved(fromPosition,toPosition,mData);
                // 表示执行了拖动
                return true;
            }

            // 滑动
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {

            }
        };

        // 帮助类
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        // 跟RecyclerView关联起来
        itemTouchHelper.attachToRecyclerView(mRecyclerView);


        //设置item的点击事件
        mStaggerAdapter.setOnItemClickListener(new LinerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(StaggerActivity.this, "click"+position, Toast.LENGTH_SHORT).show();
            }
        });

        //长按事件
        mStaggerAdapter.setOnItemLongClickListener(new LinerAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                Toast.makeText(StaggerActivity.this, "onlongClick"+position, Toast.LENGTH_SHORT).show();
                // 删除数据
//                mLinearAdapter.removeData(position);
            }
        });

    }
}
