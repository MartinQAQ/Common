package com.z_martin.common.common.adapterHelper;

import android.util.SparseArray;
import android.view.View;
import android.view.ViewGroup;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.recyclerview.widget.RecyclerView;

import com.z_martin.common.common.adapterHelper.provider.BaseItemProvider;
import com.z_martin.common.common.adapterHelper.util.MultiTypeDelegate;
import com.z_martin.common.common.adapterHelper.util.ProviderDelegate;

import java.util.List;

public abstract class MultipleItemRvAdapter<T, V extends BaseViewHolder> extends BaseQuickAdapter<T, V> {

    private SparseArray<BaseItemProvider> mItemProviders;
    protected ProviderDelegate mProviderDelegate;
    private MultiTypeDelegate<T> mMultiTypeDelegate;

    public MultipleItemRvAdapter(@Nullable List<T> data) {
        super(data);
    }

    public void finishInitialize() {
        mProviderDelegate = new ProviderDelegate();

        setMultiTypeDelegate(new MultiTypeDelegate<T>() {
            @Override
            protected int getItemType(T t) {
                return getViewType(t);
            }
        });

        registerItemProvider();

        mItemProviders = mProviderDelegate.getItemProviders();

        for (int i = 0; i < mItemProviders.size(); i++) {
            int key = mItemProviders.keyAt(i);
            BaseItemProvider provider = mItemProviders.get(key);
            provider.mData = mData;
            getMultiTypeDelegate().registerItemType(key, provider.layout());
        }
    }

    protected abstract int getViewType(T t);

    public abstract void registerItemProvider();

    @Override
    protected void bindViewClickListener(V baseViewHolder) {
        if (baseViewHolder == null) {
            return;
        }

        bindClick(baseViewHolder);

        super.bindViewClickListener(baseViewHolder);
    }

    @Override
    protected V onCreateDefViewHolder(ViewGroup parent, int viewType) {
        if (getMultiTypeDelegate() == null) {
            throw new IllegalStateException("please use setMultiTypeDelegate first!");
        }
        int layoutId = getMultiTypeDelegate().getLayoutId(viewType);
        return createBaseViewHolder(parent, layoutId);
    }

    @Override
    protected int getDefItemViewType(int position) {
        if (getMultiTypeDelegate() == null) {
            throw new IllegalStateException("please use setMultiTypeDelegate first!");
        }
        return getMultiTypeDelegate().getDefItemViewType(mData, position);
    }

    @Override
    protected void convert(@NonNull V helper, T item) {
        int itemViewType = helper.getItemViewType();
        BaseItemProvider provider = mItemProviders.get(itemViewType);

        provider.mContext = helper.itemView.getContext();

        int position = helper.getLayoutPosition() - getHeaderLayoutCount();
        provider.convert(helper, item, position);
    }

    @Override
    protected void convertPayloads(@NonNull V helper, T item, @NonNull List<Object> payloads) {
        int itemViewType = helper.getItemViewType();
        BaseItemProvider provider = mItemProviders.get(itemViewType);

        int position = helper.getLayoutPosition() - getHeaderLayoutCount();
        provider.convertPayloads(helper, item, position, payloads);
    }

    private void bindClick(final V helper) {
        OnItemClickListener clickListener = getOnItemClickListener();
        OnItemLongClickListener longClickListener = getOnItemLongClickListener();

        if (clickListener != null && longClickListener != null) {
            // If you have set up a sub-entry click monitor and sub-entries long press listen
            return;
        }

        if (clickListener == null) {
            //Callback to itemProvider if no click listener is set
            helper.itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {
                    int position = helper.getAdapterPosition();
                    if (position == RecyclerView.NO_POSITION) {
                        return;
                    }
                    position -= getHeaderLayoutCount();

                    int itemViewType = helper.getItemViewType();
                    BaseItemProvider provider = mItemProviders.get(itemViewType);

                    provider.onClick(helper, mData.get(position), position);
                }
            });
        }

        if (longClickListener == null) {
            // If you do not set a long press listener, callback to the itemProvider
            helper.itemView.setOnLongClickListener(new View.OnLongClickListener() {
                @Override
                public boolean onLongClick(View v) {
                    int position = helper.getAdapterPosition();
                    if (position == RecyclerView.NO_POSITION) {
                        return false;
                    }
                    position -= getHeaderLayoutCount();

                    int itemViewType = helper.getItemViewType();
                    BaseItemProvider provider = mItemProviders.get(itemViewType);
                    return provider.onLongClick(helper, mData.get(position), position);
                }
            });
        }
    }

    public void setMultiTypeDelegate(MultiTypeDelegate<T> multiTypeDelegate) {
        mMultiTypeDelegate = multiTypeDelegate;
    }

    public MultiTypeDelegate<T> getMultiTypeDelegate() {
        return mMultiTypeDelegate;
    }

}
