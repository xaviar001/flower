package com.xaviar.frm;

import java.util.HashMap;

import flexjson.JSONDeserializer;
import flexjson.JSONSerializer;

public class FsmState {

	public final static int STATE_BEFORE_INIT = 10 ; // FIRST_RUN;
	public final static int STATE_INIT = 20 ; // FIRST_RUN;
	public final static int STATE_WAIT_FOR_SERVER_INIT_APPROVED = 30 ; // FIRST_RUN;
	public final static int STATE_RE_INIT = 40 ; // Need to run init again
	public final static int STATE_SUCCESS_TRANSFER_INIT = 50 ; // SUCCESS TO TRansfer init data;
	public final static int STATE_HAVE_EVENTS_DATA_TO_TRANSFER = 70 ; // new event exists to transfer
	public final static int STATE_SUCCESS_TRANSFER_EVENTS_DATA = 90 ; // success to transfers events	
	public final static int STATE_ERROR = 9999 ; // ERROR
	
	private int state = 0;
	
	public void setState(int state) {
		this.state = state;		
	}
	
	public int getState()
	{
     return this.state;		
	}

	public static String toJson(FsmState fsmState) {
		   return new JSONSerializer().exclude("*.class").serialize(fsmState);
		}

	
	public static int getStateID(String genericData) {
		Object fsmStateObj =  new JSONDeserializer<FsmState>().deserialize( genericData );
		HashMap map = (HashMap) fsmStateObj;
		int stateID = (Integer) map.get("state");
		return stateID;
	}	
	
	public static FsmState fromJson(String genericData) {
		Object fsmStateObj =  new JSONDeserializer<FsmState>().deserialize( genericData );
		HashMap map = (HashMap) fsmStateObj;
		int stateID = (Integer) map.get("state");
		FsmState fsmState = new FsmState();
		fsmState.setState(stateID);
		return fsmState;
	}	
	
}
