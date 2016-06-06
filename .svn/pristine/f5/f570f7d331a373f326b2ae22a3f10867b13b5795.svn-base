package org.bigmouth.nvwa.codec;

import org.apache.commons.lang.builder.ToStringBuilder;
import org.apache.commons.lang.builder.ToStringStyle;
import org.bigmouth.nvwa.codec.tlv.annotation.TLVAttribute;


public class BizObject {

	@TLVAttribute(tag = 6)
	private String imsi;

	@TLVAttribute(tag = 7)
	private String imei;

	@TLVAttribute(tag = 10)
	private String hsman;

	@TLVAttribute(tag = 11)
	private String hstype;

	@TLVAttribute(tag = 9)
	private int vmVer;

	@TLVAttribute(tag = 34)
	private byte clientVer;

	@TLVAttribute(tag = 26)
	private int lac;

	@TLVAttribute(tag = 35)
	private int wh;

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

	public String getHsman() {
		return hsman;
	}

	public void setHsman(String hsman) {
		this.hsman = hsman;
	}

	public String getHstype() {
		return hstype;
	}

	public void setHstype(String hstype) {
		this.hstype = hstype;
	}

	public int getVmVer() {
		return vmVer;
	}

	public void setVmVer(int vmVer) {
		this.vmVer = vmVer;
	}

	public byte getClientVer() {
		return clientVer;
	}

	public void setClientVer(byte clientVer) {
		this.clientVer = clientVer;
	}

	public int getLac() {
		return lac;
	}

	public void setLac(int lac) {
		this.lac = lac;
	}

	public int getWh() {
		return wh;
	}

	public void setWh(int wh) {
		this.wh = wh;
	}

	@Override
	public String toString() {
		return ToStringBuilder.reflectionToString(this, ToStringStyle.DEFAULT_STYLE);
	}

}
