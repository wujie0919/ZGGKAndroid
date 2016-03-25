package com.zggk.zggkandroid.interfaces;

import android.location.Location;

public class Interfaces {

	private static Interfaces instance = new Interfaces();

	private Interfaces() {

	}

	public static Interfaces getInstance() {
		return instance;
	}

	private OnLocationListener mLocationListener;

	public interface OnLocationListener {
		public void onLocation(Location location);
	}

	public void setLocationListenr(OnLocationListener listener) {
		this.mLocationListener = listener;
	}

	public void sendLocationInfo(Location location) {
		if (mLocationListener != null) {
			mLocationListener.onLocation(location);
		}
	}

	private ChangePage changePage;

	public interface ChangePage {
		public void doChangePage(int position);
	}

	public void setChangePage(ChangePage changePage) {
		this.changePage = changePage;
	}

	public void startChangePage(int position) {
		if (changePage != null) {
			changePage.doChangePage(position);
		}
	}

}
