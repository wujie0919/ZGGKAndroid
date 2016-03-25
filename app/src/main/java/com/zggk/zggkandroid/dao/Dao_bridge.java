package com.zggk.zggkandroid.dao;

import com.zggk.zggkandroid.common.DBHelperSingleton;
import com.zggk.zggkandroid.entity.Mod_bridge;

public class Dao_bridge {
	
	/*
	 * 插入数据
	 */
	public void insertBridgeInfo(Mod_bridge bridge){
		DBHelperSingleton.getInstance().insertData(bridge);
	}
	
	/*
	 * 删除数据
	 */
	public void deleteBridge(Mod_bridge bridge) {
		DBHelperSingleton.getInstance().deleteData(Mod_bridge.class, "");
	}
}
