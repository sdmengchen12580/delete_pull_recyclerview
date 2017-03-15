package com.examplem.myrecycler;

import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

/**
 * Created by gqq on 2017/3/13.
 */

public class StaggerAdapter extends RecyclerView.Adapter<StaggerAdapter.ViewHolder>{

    // 数据
    private List<String> mData = new ArrayList<>();

    // 高的随机数据
    private List<Integer> mHeights = new ArrayList<>();

    private LinerAdapter.OnItemClickListener mOnItemClickListener;
    private LinerAdapter.OnItemLongClickListener mLongClickListener;

    // 设置数据
    public void setData(List<String> data,List<Integer> heights){
        mHeights.clear();
        mHeights.addAll(heights);
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    // 移动item
    public void itemMoved(int fromPosition, int toPosition,List<String> data) {

        mData.clear();
        mData.addAll(data);

//        Collections.swap(mData,fromPosition,toPosition);
        notifyItemMoved(fromPosition, toPosition);
        notifyItemRangeChanged(fromPosition,mData.size()-fromPosition);

    }



    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler,parent,false);
        ViewHolder viewHolder = new ViewHolder(view);
        return viewHolder;
    }

    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {

        // item的宽高需要随机
        ViewGroup.LayoutParams layoutParams = holder.mTvItem.getLayoutParams();
        layoutParams.height = mHeights.get(position);
        holder.mTvItem.setLayoutParams(layoutParams);

        holder.mTvItem.setText(mData.get(position));

        if (mOnItemClickListener!=null) {

            holder.mTvItem.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    // 可以直接在这里完成，不方便在这里写的话
                    // 接口回调的方式
                    mOnItemClickListener.onItemClick(position);
                }
            });
        }
        if (mLongClickListener!=null){
            holder.mTvItem.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {

                    mLongClickListener.onItemLongClick(position);

                    // 不会触发点击的
                    return true;
                }
            });
        }

    }

    @Override
    public int getItemCount() {
        return mData.size();
    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.item_text)
        TextView mTvItem;

        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
        }
    }

    // 设置监听的方法：实现我们接口的初始化
    public  void setOnItemClickListener(LinerAdapter.OnItemClickListener onItemClickListener){
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(LinerAdapter.OnItemLongClickListener onItemLongClickListener){
        mLongClickListener = onItemLongClickListener;
    }

    // 点击监听
    public interface OnItemClickListener{
        void onItemClick(int position);
    }

    // 长按的监听
    public interface OnItemLongClickListener{
        void onItemLongClick(int position);
    }
}
