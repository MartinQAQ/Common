package com.z_martin.common.common.adapterHelper.provider;

import android.content.Context;

import androidx.annotation.NonNull;

import com.z_martin.common.common.adapterHelper.BaseViewHolder;

import java.util.List;


public abstract class BaseItemProvider<T,V extends BaseViewHolder> {

    public Context mContext;
    public List<T> mData;

    //Rewrite this method to return viewType
    public abstract int viewType();

    //Rewrite this method to return layout
    public abstract int layout();

    public abstract void convert(@NonNull V helper, T data, int position);

    public void convertPayloads(@NonNull V helper, T data, int position, @NonNull List<Object> payloads){}

    //Subclasses override this method if you want to implement an item click event
    public void onClick(V helper, T data, int position){}

    //Subclasses override this method if you want to implement an item long press event
    public boolean onLongClick(V helper, T data, int position){return false;}
}
