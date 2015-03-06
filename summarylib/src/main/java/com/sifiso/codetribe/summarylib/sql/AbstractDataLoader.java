package com.sifiso.codetribe.summarylib.sql;

import android.annotation.SuppressLint;
import android.content.Context;
import android.support.v4.content.AsyncTaskLoader;

import java.util.List;


@SuppressLint("NewApi")
public abstract class AbstractDataLoader<E extends List<?>> extends AsyncTaskLoader<E> {

	public AbstractDataLoader(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}
	protected E mLastDataList=null;
	protected abstract E buildList();
	@Override
	public E loadInBackground() {
		// TODO Auto-generated method stub
		return buildList();
	}
	
	@Override
	public void deliverResult(E data) {
		if(isReset())
		{
			emptyDataList(data);
			return;
		}
		E oldDataList=mLastDataList;
		mLastDataList=data;
		if(isStarted())
		{
			super.deliverResult(data);
		}
		if(oldDataList!=null&&oldDataList != data && oldDataList.size()>0)
		{
			emptyDataList(oldDataList);
		}
	}
	
	@Override
	protected void onStartLoading() {
		if (mLastDataList != null) {
			deliverResult(mLastDataList);
		}
		if (takeContentChanged() || mLastDataList == null
				|| mLastDataList.size() == 0) {
			forceLoad();
		}
	}
	/**
	 * Must be called from the UI thread, triggered by a call to stopLoading().
	 */
	@Override
	protected void onStopLoading() {
		// Attempt to cancel the current load task if possible.
		cancelLoad();
	}
	/**
	 * Must be called from the UI thread, triggered by a call to cancel(). Here,
	 * we make sure our Cursor is closed, if it still exists and is not already
	 * closed.
	 */
	@Override
	public void onCanceled(E dataList) {
		if (dataList != null && dataList.size() > 0) {
			emptyDataList(dataList);
		}
	}
	/**
	 * Must be called from the UI thread, triggered by a call to reset(). Here,
	 * we make sure our Cursor is closed, if it still exists and is not already
	 * closed.
	 */
	@Override
	protected void onReset() {
		super.onReset();
		// Ensure the loader is stopped
		onStopLoading();
		if (mLastDataList != null && mLastDataList.size() > 0) {
			emptyDataList(mLastDataList);
		}
		mLastDataList = null;
	}
	
	protected void emptyDataList(E dataList) {
		if (dataList != null && dataList.size() > 0) {
			for (int i = 0; i < dataList.size(); i++) {
				dataList.remove(i);
			}
		}
	}

}
