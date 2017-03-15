package com.examplem.myrecycler;

import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.helper.ItemTouchHelper;
import android.widget.Toast;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by Administrator on 2017/3/13.
 */
public class SecondActivity extends AppCompatActivity {


    @BindView(R.id.recyclerView)
    RecyclerView mRecyclerView;

    private List<String> mData;
    private LinerAdapter mLinearAdapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second);
        ButterKnife.bind(this);

        initView();
        initData();

    }

    // 初始化布局相关
    private void initView() {

        // 1. 设置布局管理器：让他展示的样式是什么
        // StaggeredGridLayoutManager、LinearLayoutManager、GridLayoutManager
        // ListView
//        mRecyclerView.setLayoutManager(new LinearLayoutManager(this));

        // GridLayout
        mRecyclerView.setLayoutManager(new GridLayoutManager(this,4));

        // 2. 如果添加或删除item，可以设置动画,他为我们提供一个可以直接使用的动画：DefaultItemAnimator
        mRecyclerView.setItemAnimator(new DefaultItemAnimator());

        // 3. 设置分割线:默认提供的一个：DividerItemDecoration，都可以自己定义，可以在item布局中设置
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.VERTICAL));
        mRecyclerView.addItemDecoration(new DividerItemDecoration(this,DividerItemDecoration.HORIZONTAL));

        // 4. 设置适配器
        mLinearAdapter = new LinerAdapter();
        mRecyclerView.setAdapter(mLinearAdapter);

        //5. 设置item的点击事件
        mLinearAdapter.setOnItemClickListener(new LinerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(int position) {
                Toast.makeText(SecondActivity.this, "click"+position, Toast.LENGTH_SHORT).show();
            }
        });

        //6. 长按事件
        mLinearAdapter.setOnItemLongClickListener(new LinerAdapter.OnItemLongClickListener() {
            @Override
            public void onItemLongClick(int position) {
                Toast.makeText(SecondActivity.this, "onlongClick"+position, Toast.LENGTH_SHORT).show();
                // 删除数据
//                mLinearAdapter.removeData(position);
            }
        });

        // 7. 设置拖动和滑动删除
//        ItemTouchHelper.Callback callback = new ItemTouchHelper.Callback() {
//            /**
//             * 方向：
//             * dragDirs：拖动：LEFT、RIGHT、START、END、UP、DOWN
//             * swipeDirs：滑动：LEFT、RIGHT、START、END、UP、DOWN
//             * 如果设置为0的时候，表示没有相应的功能
//             */
//
//            // 拿到设置的移动的方向设置
//            @Override
//            public int getMovementFlags(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder) {
//                int dragFlags = 0;
//                int swipeFlags = 0;
//                // 设置的方向(拖动、滑动)
//                if (recyclerView.getLayoutManager() instanceof GridLayoutManager){
//                    dragFlags = ItemTouchHelper.UP|ItemTouchHelper.DOWN|ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;
//                    swipeFlags = 0;
//                }else {
//                    dragFlags = ItemTouchHelper.UP|ItemTouchHelper.DOWN;
//                    swipeFlags = ItemTouchHelper.LEFT|ItemTouchHelper.RIGHT;
//                }
//                // 将我们设置的Flags设置给ItemTouchHelper
//                return makeMovementFlags(dragFlags,swipeFlags);
//            }
//            // 拖动的事件处理
//            @Override
//            public boolean onMove(RecyclerView recyclerView, RecyclerView.ViewHolder viewHolder, RecyclerView.ViewHolder target) {
//                return false;
//            }
//            // 滑动
//            @Override
//            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
//            }
//        };

        ItemTouchHelper.Callback callback = new ItemTouchHelper.SimpleCallback(ItemTouchHelper.UP|ItemTouchHelper.DOWN,ItemTouchHelper.RIGHT) {

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
                mLinearAdapter.itemMoved(fromPosition,toPosition,mData);
                // 表示执行了拖动
                return true;
            }

            // 滑动
            @Override
            public void onSwiped(RecyclerView.ViewHolder viewHolder, int direction) {
                // 滑动的时候删除
                int position = viewHolder.getAdapterPosition();
                mLinearAdapter.removeData(position);
            }
        };

        // 帮助类
        ItemTouchHelper itemTouchHelper = new ItemTouchHelper(callback);
        // 跟RecyclerView关联起来
        itemTouchHelper.attachToRecyclerView(mRecyclerView);

    }

    @OnClick(R.id.btnAdd)
    public void addData(){
        mLinearAdapter.addData(1);
    }

    // 数据填充
    private void initData() {
        mData = new ArrayList<>();

        for (int i='A';i<'z';i++){
            mData.add(""+(char)i);
        }
        mLinearAdapter.setData(mData);
    }
}
