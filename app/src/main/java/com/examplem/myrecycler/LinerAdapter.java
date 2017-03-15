
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

// 适配器
public class LinerAdapter extends RecyclerView.Adapter<LinerAdapter.ViewHolder>{

    private List<String> mData = new ArrayList<>();

    private OnItemClickListener mOnItemClickListener;
    private OnItemLongClickListener mLongClickListener;

    // 数据的填充
    public void setData(List<String> data){
        mData.clear();
        mData.addAll(data);
        notifyDataSetChanged();
    }

    // 删除某条数据
    public void removeData(int position) {
        mData.remove(position);
        notifyItemRemoved(position);
        notifyItemRangeChanged(position,mData.size()-position);

        // 最常用
//        notifyDataSetChanged();
//        notifyItemRemoved(position);
//        notifyItemInserted(position);
//        notifyItemRangeChanged(position,mData.size()-position);
    }

    // 添加某条数据
    public void addData(int position) {
        mData.add(position,"insert ok");
        notifyItemInserted(position);
        notifyItemRangeChanged(position,mData.size()-position);
    }


    // 创建视图的ViewHolder
    @Override
    public ViewHolder onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_recycler,parent,false);
        ViewHolder holder = new ViewHolder(itemView);
        return holder;
    }

    // 视图和数据进行绑定
    @Override
    public void onBindViewHolder(ViewHolder holder, final int position) {
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

    // item数量
    @Override
    public int getItemCount() {
        return mData.size();
    }

    // 移动item
    public void itemMoved(int fromPosition, int toPosition,List<String> data) {

        mData.clear();
        mData.addAll(data);

//        Collections.swap(mData,fromPosition,toPosition);
        notifyItemMoved(fromPosition, toPosition);
        notifyItemRangeChanged(fromPosition,mData.size()-fromPosition);

    }

    class ViewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.item_text)
        TextView mTvItem;

        public ViewHolder(View itemView) {
            super(itemView);
            // 使用Butterknife
            ButterKnife.bind(this,itemView);
        }
    }

    // 设置监听的方法：实现我们接口的初始化
    public  void setOnItemClickListener(OnItemClickListener onItemClickListener){
        mOnItemClickListener = onItemClickListener;
    }

    public void setOnItemLongClickListener(OnItemLongClickListener onItemLongClickListener){
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
