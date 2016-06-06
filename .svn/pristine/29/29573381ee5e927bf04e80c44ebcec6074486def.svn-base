package org.bigmouth.nvwa.servicelogic.handler;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.bigmouth.nvwa.codec.tlv.annotation.TLVAttribute;

public class TerminalRequest extends BaseRequest {

	@TLVAttribute(tag = 50)
	private String hsman;
	@TLVAttribute(tag = 51)
	private String hstype;
	@TLVAttribute(tag = 52)
	private int platSdk;
	@TLVAttribute(tag = 53)
	private short extPlat;
	@TLVAttribute(tag = 54)
	private short screenWidth;
	@TLVAttribute(tag = 55)
	private short screenHeight;
	@TLVAttribute(tag = 56)
	private byte dpi;
	@TLVAttribute(tag = 57)
	private int ramSize;
	@TLVAttribute(tag = 58)
	private int romSize;
	@TLVAttribute(tag = 59)
	private String imsi;
	@TLVAttribute(tag = 60)
	private String imei;
	@TLVAttribute(tag = 62)
	private short lac;
	@TLVAttribute(tag = 63)
	private short cellId;
	@TLVAttribute(tag = 64)
	private String mcc;
	@TLVAttribute(tag = 65)
	private String mnc;
	@TLVAttribute(tag = 66)
	private short operators;
	@TLVAttribute(tag = 67)
	private String romVer;
	@TLVAttribute(tag = 75)
	// TODO 0:wifi 1:2g 2:3g
	private short networkType;
	@TLVAttribute(tag = 69)
	private int channelId;
	@TLVAttribute(tag = 2003)
	private String marketPkg;
	@TLVAttribute(tag = 1536)
	private int netIdentifier = -2; //default value -2: old_client, -1:wifi, 0:unkown ...

	public int getNetIdentifier() {
		return netIdentifier;
	}

	public void setNetIdentifier(int netIdentifier) {
		this.netIdentifier = netIdentifier;
	}

	public String getHsman() {
		if (null != hsman) {
			hsman = hsman.trim().replaceAll(" ", "_");
		}
		return hsman;
	}

	public void setHsman(String hsman) {
		this.hsman = hsman;
	}

	public String getHstype() {
		if (null != hstype) {
			hstype = hstype.trim().replaceAll(" ", "_");
		}
		return hstype;
	}

	public void setHstype(String hstype) {
		this.hstype = hstype;
	}

	public int getPlatSdk() {
		return platSdk;
	}

	public void setPlatSdk(int platSdk) {
		this.platSdk = platSdk;
	}

	public short getExtPlat() {
		return extPlat;
	}

	public void setExtPlat(short extPlat) {
		this.extPlat = extPlat;
	}

	public short getScreenWidth() {
		return screenWidth;
	}

	public void setScreenWidth(short screenWidth) {
		this.screenWidth = screenWidth;
	}

	public short getScreenHeight() {
		return screenHeight;
	}

	public void setScreenHeight(short screenHeight) {
		this.screenHeight = screenHeight;
	}

	public byte getDpi() {
		return dpi;
	}

	public void setDpi(byte dpi) {
		this.dpi = dpi;
	}

	public int getRamSize() {
		return ramSize;
	}

	public void setRamSize(int ramSize) {
		this.ramSize = ramSize;
	}

	public int getRomSize() {
		return romSize;
	}

	public void setRomSize(int romSize) {
		this.romSize = romSize;
	}

	public String getImsi() {
		return imsi;
	}

	public void setImsi(String imsi) {
		this.imsi = imsi;
	}

	public String getImei() {
		return imei;
	}

	public void setImei(String imei) {
		this.imei = imei;
	}

	public short getLac() {
		return lac;
	}

	public void setLac(short lac) {
		this.lac = lac;
	}

	public short getCellId() {
		return cellId;
	}

	public void setCellId(short cellId) {
		this.cellId = cellId;
	}

	public String getMcc() {
		return mcc;
	}

	public void setMcc(String mcc) {
		this.mcc = mcc;
	}

	public String getMnc() {
		return mnc;
	}

	public void setMnc(String mnc) {
		this.mnc = mnc;
	}

	public short getOperators() {
		return operators;
	}

	public void setOperators(short operators) {
		this.operators = operators;
	}
	
	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}

	public short getNetworkType() {
		return networkType;
	}

	public void setNetworkType(short networkType) {
		this.networkType = networkType;
	}

	public String getRomVer() {
		return romVer;
	}

	public void setRomVer(String romVer) {
		this.romVer = romVer;
	}

	public int getChannelId() {
		return channelId;
	}

	public void setChannelId(int channelId) {
		this.channelId = channelId;
	}

	public String getMarketPkg() {
		return marketPkg;
	}

	public void setMarketPkg(String marketPkg) {
		this.marketPkg = marketPkg;
	}
}
