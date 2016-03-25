package com.zggk.zggkandroid.service;

import com.zggk.zggkandroid.interfaces.Interfaces;

import android.app.Service;
import android.content.Intent;
import android.location.Location;
import android.location.LocationListener;
import android.location.LocationManager;
import android.os.Bundle;
import android.os.IBinder;
import android.util.Log;

public class LocationSvc extends Service implements LocationListener {

	private LocationManager mLocaManager;

	@Override
	public IBinder onBind(Intent intent) {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public void onCreate() {
		// TODO Auto-generated method stub
		super.onCreate();
		mLocaManager = (LocationManager) getSystemService(LOCATION_SERVICE);
	}

	@Override
	public void onStart(Intent intent, int startId) {
		if (mLocaManager.getProvider(LocationManager.NETWORK_PROVIDER) != null) {
			mLocaManager.requestLocationUpdates(
					LocationManager.NETWORK_PROVIDER, 0, 0, this);
		} else if (mLocaManager.getProvider(LocationManager.GPS_PROVIDER) != null) {
			mLocaManager.requestLocationUpdates(LocationManager.GPS_PROVIDER,
					0, 0, this);
		} else {
			Log.d("xsh", "定位失败");
		}
	}

	@Override
	public void onLocationChanged(Location location) {
		// TODO Auto-generated method stub
		Interfaces.getInstance().sendLocationInfo(location);

		mLocaManager.removeUpdates(this);
		stopSelf();

	}

	@Override
	public void onStatusChanged(String provider, int status, Bundle extras) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderEnabled(String provider) {
		// TODO Auto-generated method stub

	}

	@Override
	public void onProviderDisabled(String provider) {
		// TODO Auto-generated method stub

	}

}
