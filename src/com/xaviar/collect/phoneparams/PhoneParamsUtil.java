package com.xaviar.collect.phoneparams;

import java.util.ArrayList;
import java.util.List;

import android.content.Context;
import android.telephony.TelephonyManager;
import android.telephony.gsm.GsmCellLocation;

public class PhoneParamsUtil {
	
	public static List<PhoneParams> getPhoneParams(Context ctx) {
		List<PhoneParams> paramsList = new ArrayList<PhoneParams>();	
		TelephonyManager telMgr = (TelephonyManager) ctx.getSystemService(Context.TELEPHONY_SERVICE);
		PhoneParams phoneParams = getPhoneParams(telMgr);
		paramsList.add(phoneParams);
		return paramsList;
	}
	
	
	public static String getSubscriberID(Context ctx) {
		TelephonyManager telMgr = (TelephonyManager) ctx
				.getSystemService(Context.TELEPHONY_SERVICE);
		return telMgr.getSubscriberId();
	}

	public static PhoneParams getPhoneParams(final TelephonyManager telMgr) {

		PhoneParams phoneParams = new PhoneParams();

		String callStateStr = getCallState(telMgr);
		phoneParams.setCallState(callStateStr);

		GsmCellLocation cellLocation = (GsmCellLocation) telMgr
				.getCellLocation();
		if(null != cellLocation)
		{
		String cellLocationString = cellLocation.getLac() + " "
				+ cellLocation.getCid();
		phoneParams.setCellLocation(cellLocationString);
		}
		
		String deviceId = telMgr.getDeviceId();
		phoneParams.setDeviceID(deviceId);

		String deviceSoftwareVersion = telMgr.getDeviceSoftwareVersion();
		phoneParams.setDeviceSoftwareVersion(deviceSoftwareVersion);

		String line1Number = telMgr.getLine1Number();
		phoneParams.setLine1Number(line1Number);

		String networkCountryIso = telMgr.getNetworkCountryIso();
		phoneParams.setNetworkCountryIso(networkCountryIso);

		String networkOperator = telMgr.getNetworkOperator();
		phoneParams.setNetworkOperator(networkOperator);

		String networkOperatorName = telMgr.getNetworkOperatorName();
		phoneParams.setNetworkOperatorName(networkOperatorName);

		String phoneType = getPhoneType(telMgr);
		phoneParams.setPhoneType(phoneType);

		String simCountryIso = telMgr.getSimCountryIso();
		phoneParams.setSimCountryIso(simCountryIso);

		String simOperator = telMgr.getSimOperator();
		phoneParams.setSimOperator(simOperator);

		String simOperatorName = telMgr.getSimOperatorName();
		phoneParams.setSimOperatorName(simOperatorName);

		String simSerialNumber = telMgr.getSimSerialNumber();
		phoneParams.setSimSerialNumber(simSerialNumber);

		String simSubscriberId = telMgr.getSubscriberId();
		phoneParams.setSimSubscriberId(simSubscriberId);

		String simstate = getSimState(telMgr);
		phoneParams.setSimstate(simstate);

		return phoneParams;
	}

	private static String getSimState(TelephonyManager telMgr) {
		int simState = telMgr.getSimState();

		String simStateString = "NA";

		switch (simState) {

		case TelephonyManager.SIM_STATE_ABSENT:

			simStateString = "ABSENT";

			break;

		case TelephonyManager.SIM_STATE_NETWORK_LOCKED:

			simStateString = "NETWORK_LOCKED";

			break;

		case TelephonyManager.SIM_STATE_PIN_REQUIRED:

			simStateString = "PIN_REQUIRED";

			break;

		case TelephonyManager.SIM_STATE_PUK_REQUIRED:

			simStateString = "PUK_REQUIRED";

			break;

		case TelephonyManager.SIM_STATE_READY:

			simStateString = "STATE_READY";

			break;

		case TelephonyManager.SIM_STATE_UNKNOWN:

			simStateString = "STATE_UNKNOWN";

			break;

		}

		return simStateString;
	}

	private static String getPhoneType(TelephonyManager telMgr) {
		int phoneType = telMgr.getPhoneType();

		String phoneTypeString = "NA";

		switch (phoneType) {

		case TelephonyManager.PHONE_TYPE_GSM:

			phoneTypeString = "GSM";

			break;

		case TelephonyManager.PHONE_TYPE_NONE:

			phoneTypeString = "NONE";

			break;

		}
		return phoneTypeString;

	}

	private static String getCallState(TelephonyManager telMgr) {
		int callState = telMgr.getCallState();
		String callStateString = "NA";

		switch (callState) {

		case TelephonyManager.CALL_STATE_IDLE:

			callStateString = "IDLE";

			break;

		case TelephonyManager.CALL_STATE_OFFHOOK:

			callStateString = "OFFHOOK";

			break;

		case TelephonyManager.CALL_STATE_RINGING:

			callStateString = "RINGING";

			break;

		}
		return callStateString;
	}

}
