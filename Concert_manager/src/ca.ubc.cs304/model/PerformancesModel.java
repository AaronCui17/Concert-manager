package ca.ubc.cs304.model;

import java.sql.Date;

/**
 * The intent for this class is to update/store information about a single branch
 */
public class PerformancesModel {
	private final int showid;
	private final String sName;
	private final Date sDate;
	private final int sTime;
	private final String sAddress;
	private final int numPerformers;
	private final String conductor;
	private final String composer;
	
	public PerformancesModel(int showid, String sName, Date sDate, int sTime, String sAddress, int numPerformers, String conductor,
							 String composer) {
		this.showid = showid;
		this.sName = sName;
		this.sDate = sDate;
		this.sTime = sTime;
		this.sAddress = sAddress;
		this.numPerformers = numPerformers;
		this.conductor = conductor;
		this.composer = composer;
	}
	public int getshowid() {return showid;}
	public String getsName() {
		return sName;
	}

	public Date getsDate() {
		return sDate;
	}

	public int getsTime() {
		return sTime;
	}

	public String getsAddress() {
		return sAddress;
	}

	public int getNumPerformers() {
		return numPerformers;
	}

	public String getConductor() {
		return conductor;
	}

	public String getComposer() {
		return composer;
	}
}
