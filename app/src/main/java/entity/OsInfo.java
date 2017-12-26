package entity;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.List;

public class OsInfo extends DataSupport {
    /*
    enum InfoType {
		TPYE_PROCESSOR_ID=1,			//cpu id
		TYPE_BASEBOARD_SERIAL,			//主板序列号
		TYPE_HD_SERIAL,					//硬盘序列号
		TYPE_MAC_ADDRESS,				//mac地址
		TYPE_IP_ADDRESS,				//ip地址
		TYPE_BIOSS_SERIAL,				//bioss序列号
		TYPE_GATEWAY_IP,				//网关IP地址
		TYPE_GATEWAY_MAC,				//网关MAC地址
		TYPE_PROCESSOR_ARCHITECTURE,	//系统中的CPU的体系结构
		TYPE_OS_VERSION,				//操作系统信息

		TYPE_COMPUTER_NAME,				//主机名
		TYPE_CPU_NAME,					//CPU型号
	};
     */
	private long id ;	//数据库自增主键

    private String processorId; 							//cpu id

	private String baseboardSerial; 						// 主板序列号
	private List<String> hdSerial ; 		// 硬盘序列号
	private List<String> macAddress ; 	// mac地址
	private List<String> ipAddress ; 	// ip地址
	private String biosSeria; 								// bioss序列号
	private String gatewayIp; 								// 网关IP地址
	private String gatewayMac; 								// 网关MAC地址
	private String processorArchitecture; 					// 系统中的CPU的体系结构
	private String osVersion; 								// 操作系统信息
	private String publicIP;        						//公网ip
	private List<String> IMEI ;          //国际移动设备身份码，全世界唯一
	private String location;        						//地理位置,格式："经度" + "," + "维度"
	private String clientPushTime;    						//客户端推送时间

	public String getProcessorId() {
		return processorId;
	}

	public void setProcessorId(String processorId) {
		this.processorId = processorId;
	}

	public String getBaseboardSerial() {
		return baseboardSerial;
	}

	public void setBaseboardSerial(String baseboardSerial) {
		this.baseboardSerial = baseboardSerial;
	}

	public List<String> getHdSerial() {
		return hdSerial;
	}

	public void setHdSerial(List<String> hdSerial) {
		this.hdSerial = hdSerial;
	}

	public List<String> getMacAddress() {
		return macAddress;
	}

	public void setMacAddress(List<String> macAddress) {
		this.macAddress = macAddress;
	}

	public List<String> getIpAddress() {
		return ipAddress;
	}

	public void setIpAddress(List<String> ipAddress) {
		this.ipAddress = ipAddress;
	}

	public String getBiosSeria() {
		return biosSeria;
	}

	public void setBiosSeria(String biosSeria) {
		this.biosSeria = biosSeria;
	}

	public String getGatewayIp() {
		return gatewayIp;
	}

	public void setGatewayIp(String gatewayIp) {
		this.gatewayIp = gatewayIp;
	}

	public String getGatewayMac() {
		return gatewayMac;
	}

	public void setGatewayMac(String gatewayMac) {
		this.gatewayMac = gatewayMac;
	}

	public String getProcessorArchitecture() {
		return processorArchitecture;
	}

	public void setProcessorArchitecture(String processorArchitecture) {
		this.processorArchitecture = processorArchitecture;
	}

	public String getOsVersion() {
		return osVersion;
	}

	public void setOsVersion(String osVersion) {
		this.osVersion = osVersion;
	}

	public String getPublicIP() {
		return publicIP;
	}

	public void setPublicIP(String publicIP) {
		this.publicIP = publicIP;
	}

	public List<String> getIMEI() {
		return IMEI;
	}

	public void setIMEI(List<String> IMEI) {
		this.IMEI = IMEI;
	}

	public String getLocation() {
		return location;
	}

	public void setLocation(String location) {
		this.location = location;
	}

	public String getClientPushTime() {
		return clientPushTime;
	}

	public void setClientPushTime(String clientPushTime) {
		this.clientPushTime = clientPushTime;
	}

	public long getId() {
		return id;
	}

	public void setId(long id) {
		this.id = id;
	}



	@Override
	public String toString() {
		return "OsInfo{" +
				"id=" + id +
				", processorId='" + processorId + '\'' +
				", baseboardSerial='" + baseboardSerial + '\'' +
				", hdSerial=" + hdSerial +
				", macAddress=" + macAddress +
				", ipAddress=" + ipAddress +
				", biosSeria='" + biosSeria + '\'' +
				", gatewayIp='" + gatewayIp + '\'' +
				", gatewayMac='" + gatewayMac + '\'' +
				", processorArchitecture='" + processorArchitecture + '\'' +
				", osVersion='" + osVersion + '\'' +
				", publicIP='" + publicIP + '\'' +
				", IMEI=" + IMEI +
				", location='" + location + '\'' +
				", clientPushTime='" + clientPushTime + '\'' +
				'}';
	}
}
