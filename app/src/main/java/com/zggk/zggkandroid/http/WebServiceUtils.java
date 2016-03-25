package com.zggk.zggkandroid.http;

import java.io.IOException;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import org.apache.http.client.HttpResponseException;
import org.json.JSONArray;
import org.json.JSONObject;
import org.ksoap2.SoapEnvelope;
import org.ksoap2.serialization.SoapObject;
import org.ksoap2.serialization.SoapSerializationEnvelope;
import org.ksoap2.transport.HttpTransportSE;
import org.xmlpull.v1.XmlPullParserException;

import com.zggk.zggkandroid.MainApplication;
import com.zggk.zggkandroid.entity.DmDinsp;
import com.zggk.zggkandroid.entity.DmDinspRecord;
import com.zggk.zggkandroid.entity.DmFinsp;
import com.zggk.zggkandroid.entity.DmFinspRecord;
import com.zggk.zggkandroid.entity.DssImage;

import android.os.Handler;
import android.os.Message;

/**
 * 访问WebService的工具类,
 *
 * 
 * @author xiaanming
 * 
 */
public class WebServiceUtils {

	// 广西公路采集系统


	public static final String NAMESPACE = "http://121.8.234.83:17001/";
	//方法名
	private static final String BASEDATADSSTPYEUPDATE="baseDataDssTpyeUpdate";
	private static final String GETLONGINUSERINFO="GetLonginUserinfo";
	private static final String BASEDATAROADUPDATE="baseDataRoadUpdate";
	private static final String DODMDINSP="doDmDinsp";
	private static final String REGULARCHECKDISEASELIST="regularCheckDiseaseList";
	private static final String DODMFINSP="doDmFinsp";

	// 含有3个线程的线程池
	private static final ExecutorService executorService = Executors
			.newFixedThreadPool(3);

	private static void callWebService(final String methodName,
			final HttpCallBack webServiceCallBack) {
		callWebService(MainApplication.WEB_SERVER_URL, methodName, null, webServiceCallBack);
	}

	private static void callWebService(final String methodName,
			Map<String, String> properties,
			final HttpCallBack webServiceCallBack) {
		callWebService(MainApplication.WEB_SERVER_URL, methodName, properties,
				webServiceCallBack);
	}
	/**
	 * 发送给服务器的参数必须按照他们的顺序来，所以此处要用有序map
	 * @param methodName
	 * @param propertie
	 * @param webServiceCallBack
	 */
	private static void callWebService_link(final String methodName,
			LinkedHashMap<String, String> propertie,
			final HttpCallBack webServiceCallBack) {
		callWebServiceLink(MainApplication.WEB_SERVER_URL, methodName, propertie,
				webServiceCallBack);
	}
	/**
	 * 
	 * @param url
	 *            WebService服务器地址
	 * @param methodName
	 *            WebService的调用方法名
	 * @param properties
	 *            WebService的参数
	 * @param webServiceCallBack
	 *            回调接口
	 */
	private static void callWebService(String url, final String methodName,
			Map<String, String> properties,
			final HttpCallBack webServiceCallBack) {
		// 创建HttpTransportSE对象，传递WebService服务器地址
		final HttpTransportSE httpTransportSE = new HttpTransportSE(url);
		// 创建SoapObject对象
		SoapObject soapObject = new SoapObject(NAMESPACE, methodName);

		// SoapObject添加参数
		if (properties != null) {
			for (Iterator<Map.Entry<String, String>> it = properties.entrySet()
					.iterator(); it.hasNext();) {
				Map.Entry<String, String> entry = it.next();
				soapObject.addProperty(entry.getKey(), entry.getValue());
			}
		}

		// 实例化SoapSerializationEnvelope，传入WebService的SOAP协议的版本号
		final SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		// soapEnvelope.setOutputSoapObject(soapObject);
		// 设置是否调用的是.Net开发的WebService
		soapEnvelope.dotNet = false;
		soapEnvelope.bodyOut = soapObject;
		httpTransportSE.debug = true;

		// 用于子线程与主线程通信的Handler
		final Handler mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				// 将返回值回调到callBack的参数中
				webServiceCallBack.callBack((SoapObject) msg.obj);
			}

		};

		// 开启线程去访问WebService
		executorService.submit(new Runnable() {

			@Override
			public void run() {
				SoapObject resultSoapObject = null;
				try {
					httpTransportSE.call(NAMESPACE + methodName, soapEnvelope);
					if (soapEnvelope.getResponse() != null) {
						// 获取服务器响应返回的SoapObject
						resultSoapObject = (SoapObject) soapEnvelope.bodyIn;
					}
				} catch (HttpResponseException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				} finally {
					// 将获取的消息利用Handler发送到主线程
					mHandler.sendMessage(mHandler.obtainMessage(0,
							resultSoapObject));
				}
			}
		});
	}
	
	/**
	 * 发送给服务器的参数必须按照他们的顺序来，所以此处要用有序map
	 * @param url
	 *            WebService服务器地址
	 * @param methodName
	 *            WebService的调用方法名
	 * @param properties
	 *            WebService的参数,
	 * @param webServiceCallBack
	 *            回调接口
	 */
	private static void callWebServiceLink(String url, final String methodName,
			LinkedHashMap<String, String> parHashMap,
			final HttpCallBack webServiceCallBack) {
		// 创建HttpTransportSE对象，传递WebService服务器地址
		final HttpTransportSE httpTransportSE = new HttpTransportSE(url);
		// 创建SoapObject对象
		SoapObject soapObject = new SoapObject(NAMESPACE, methodName);

		// SoapObject添加参数
		if (parHashMap != null) {
			for (Iterator<LinkedHashMap.Entry<String, String>> it = parHashMap.entrySet()
					.iterator(); it.hasNext();) {
				LinkedHashMap.Entry<String, String> entry = it.next();
				soapObject.addProperty(entry.getKey(), entry.getValue());
			}
		}

		// 实例化SoapSerializationEnvelope，传入WebService的SOAP协议的版本号
		final SoapSerializationEnvelope soapEnvelope = new SoapSerializationEnvelope(
				SoapEnvelope.VER10);
		// soapEnvelope.setOutputSoapObject(soapObject);
		// 设置是否调用的是.Net开发的WebService
		soapEnvelope.dotNet = false;
		soapEnvelope.bodyOut = soapObject;
//		soapEnvelope.setOutputSoapObject(soapObject); 
		httpTransportSE.debug = true;

		// 用于子线程与主线程通信的Handler
		final Handler mHandler = new Handler() {

			@Override
			public void handleMessage(Message msg) {
				super.handleMessage(msg);
				// 将返回值回调到callBack的参数中
				webServiceCallBack.callBack((SoapObject) msg.obj);
			}

		};

		// 开启线程去访问WebService
		executorService.submit(new Runnable() {

			@Override
			public void run() {
				SoapObject resultSoapObject = null;
				try {
					httpTransportSE.call(NAMESPACE + methodName, soapEnvelope);
					if (soapEnvelope.getResponse() != null) {
						// 获取服务器响应返回的SoapObject
						resultSoapObject = (SoapObject) soapEnvelope.bodyIn;
					}
				} catch (HttpResponseException e) {
					e.printStackTrace();
				} catch (IOException e) {
					e.printStackTrace();
				} catch (XmlPullParserException e) {
					e.printStackTrace();
				} finally {
					// 将获取的消息利用Handler发送到主线程
					mHandler.sendMessage(mHandler.obtainMessage(0,
							resultSoapObject));
				}
			}
		});
	}
	
	/**
	 * 
	 * 
	 * @author xiaanming
	 * 
	 */
	public interface HttpCallBack {
		public void callBack(SoapObject result);
	}
	
	/**
	 * 获取用户列表
	 * 
	 * @param callback
	 */
	public static void getLoginUserinfo(HttpCallBack callback) {
		callWebService(GETLONGINUSERINFO, callback);
	}

	/**
	 * 获取路线数据
	 * 
	 * @param callback
	 */
	public static void getRouteData(HttpCallBack callback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("usercode", MainApplication.getCurUserinfo().getUSER_CODE());
		callWebService(BASEDATAROADUPDATE, params, callback);
	}

	/**
	 * 获取病害类型数据
	 * 
	 * @param callback
	 */
	public static void getDiseaseType(HttpCallBack callback) {
		Map<String, String> params = new HashMap<String, String>();
		params.put("usercode", MainApplication.getCurUserinfo().getUSER_CODE());
		params.put("orgid", MainApplication.getCurUserinfo().getORG_ID());
		callWebService(BASEDATADSSTPYEUPDATE, params, callback);
	}
	/**
	 * 上传日常巡查数据
	 * @param list
	 * @param callBack
	 */
	public static void sendRiChangBingHai(List<DmDinsp> list,List<DmDinspRecord> records,List<DssImage> dssImages,HttpCallBack callBack) {
		LinkedHashMap<String, String> params=new LinkedHashMap<String, String>();
		params.put("dmDinsp",getJsonString(list));
		params.put("dmDinspRecord", getJsonString(records));
		params.put("images", getJsonString(dssImages));
		params.put("userId",  MainApplication.getCurUserinfo().getUSER_CODE());
		params.put("orgId", MainApplication.getCurUserinfo().getORG_ID());
		params.put("userName", MainApplication.getCurUserinfo().getUSER_NAME());
		params.put("ORG_NAME_EN", MainApplication.getCurUserinfo().getORG_NAME_EN());
		callWebService_link(DODMDINSP, params, callBack);
	}
	/**
	 * 更新病害信息
	 * @param callBack
	 */
	public static void regularCheckDiseaseList(LinkedHashMap<String, String> paramsMap,HttpCallBack callBack){
		callWebService_link(REGULARCHECKDISEASELIST, paramsMap, callBack);
	}
	/**
	 * 路面、路基、设施、绿化检查
	 * @param finsps
	 * @param finspRecords
	 * @param callBack
	 */
	public static void doDmFinsp(List<DmFinsp> finsps,List<DmFinspRecord> finspRecords,HttpCallBack callBack) {
		LinkedHashMap<String, String> params=new LinkedHashMap<String, String>();
		params.put("dmFinsp",getJsonString(finsps));
		params.put("dmFinspRecord", getJsonString(finspRecords));
		params.put("images", "");
		params.put("userId",  MainApplication.getCurUserinfo().getUSER_CODE());
		params.put("orgId", MainApplication.getCurUserinfo().getORG_ID());
		params.put("userName", MainApplication.getCurUserinfo().getUSER_NAME());
		params.put("ORG_NAME_EN", MainApplication.getCurUserinfo().getORG_NAME_EN());
		callWebService_link(DODMFINSP, params, callBack);
	}
	
	/**
	 * 生成json字符串
	 * @param object
	 * @return
	 */
	private static <E> String getJsonString(Object object){
		JSONArray jsonArray=new JSONArray();
		String json="";
		if (object.getClass()==ArrayList.class) {
			@SuppressWarnings("unchecked")
			List<E> objList=(List<E>)object;
			for (Iterator<E> iterator = objList.iterator(); iterator.hasNext();) {
				Object obj = (Object) iterator.next();
				JSONObject jsonObject=new JSONObject();
				if (obj!=null) {
					Field[] property_names = obj.getClass().getDeclaredFields();
					try {
						int num=property_names.length;
						for (int i = 0; i <num; i++) {
							Field field=property_names[i];
							field.setAccessible(true);
							String name=field.getName();
							if (name.equals("bId") || name.equals("filePath")) {
								continue;
							}
							if (field.get(obj) == null) {
								jsonObject.put(name, "");
							}else {
								jsonObject.put(name, field.get(obj).toString());
							}
						}
					} catch (Exception e) {
						// TODO: handle exception
						e.printStackTrace();
					}
				}
				jsonArray.put(jsonObject);
			}
		}
		return jsonArray==null?json:jsonArray.toString();
	}
	
	
}
