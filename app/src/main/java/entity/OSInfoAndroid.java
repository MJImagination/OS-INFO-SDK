package entity;

import org.litepal.crud.DataSupport;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by ancun on 2017/12/13.
 */

public class OSInfoAndroid {
    private long id ;
    private String publicIP;        //公网ip
    private String ip;              //ip地址
    private String netMask;         //子网掩码
    private String gateway;         //网关
    private String serverAddress;   //服务器地址（路由器地址）
    private String dns1;            //域名解析1
    private String dns2;            //域名解析2
    private String IMEI1;           //插槽1（国际移动设备身份码，全世界唯一）
    private String IMEI2;           //插槽2（国际移动设备身份码，全世界唯一）
    private String MEDI;            // 移动设备识别码,CDMA手机的身份识别码
    private String MAC;             // 网卡物理地址
    private String gatewayMac;      //网关mac
    private String cpuVersion;      //cpu型号
    private String location;        //地理位置,格式："经度" + "," + "维度"
    private String clientPushTime;   //客户端推送时间
    private String androidVersion;  //android版本
    private String appVersionName;   //app版本名

    public String getPublicIP() {
        return publicIP;
    }

    public void setPublicIP(String publicIP) {
        this.publicIP = publicIP;
    }

    public String getIp() {
        return ip;
    }

    public void setIp(String ip) {
        this.ip = ip;
    }

    public String getNetMask() {
        return netMask;
    }

    public void setNetMask(String netMask) {
        this.netMask = netMask;
    }

    public String getGateway() {
        return gateway;
    }

    public void setGateway(String gateway) {
        this.gateway = gateway;
    }

    public String getServerAddress() {
        return serverAddress;
    }

    public void setServerAddress(String serverAddress) {
        this.serverAddress = serverAddress;
    }

    public String getDns1() {
        return dns1;
    }

    public void setDns1(String dns1) {
        this.dns1 = dns1;
    }

    public String getDns2() {
        return dns2;
    }

    public void setDns2(String dns2) {
        this.dns2 = dns2;
    }

    public String getIMEI1() {
        return IMEI1;
    }

    public void setIMEI1(String IMEI1) {
        this.IMEI1 = IMEI1;
    }

    public String getIMEI2() {
        return IMEI2;
    }

    public void setIMEI2(String IMEI2) {
        this.IMEI2 = IMEI2;
    }

    public String getMEDI() {
        return MEDI;
    }

    public void setMEDI(String MEDI) {
        this.MEDI = MEDI;
    }

    public String getMAC() {
        return MAC;
    }

    public void setMAC(String MAC) {
        this.MAC = MAC;
    }

    public String getCpuVersion() {
        return cpuVersion;
    }

    public void setCpuVersion(String cpuVersion) {
        this.cpuVersion = cpuVersion;
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

    public String getAndroidVersion() {
        return androidVersion;
    }

    public void setAndroidVersion(String androidVersion) {
        this.androidVersion = androidVersion;
    }

    public String getAppVersionName() {
        return appVersionName;
    }

    public void setAppVersionName(String appVersionName) {
        this.appVersionName = appVersionName;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getGatewayMac() {
        return gatewayMac;
    }

    public void setGatewayMac(String gatewayMac) {
        this.gatewayMac = gatewayMac;
    }

    @Override
    public String toString() {
        return "OSInfoAndroid{" +
                "id=" + id +
                ", publicIP='" + publicIP + '\'' +
                ", ip='" + ip + '\'' +
                ", netMask='" + netMask + '\'' +
                ", gateway='" + gateway + '\'' +
                ", serverAddress='" + serverAddress + '\'' +
                ", dns1='" + dns1 + '\'' +
                ", dns2='" + dns2 + '\'' +
                ", IMEI1='" + IMEI1 + '\'' +
                ", IMEI2='" + IMEI2 + '\'' +
                ", MEDI='" + MEDI + '\'' +
                ", MAC='" + MAC + '\'' +
                ", gatewayMac='" + gatewayMac + '\'' +
                ", cpuVersion='" + cpuVersion + '\'' +
                ", location='" + location + '\'' +
                ", clientPushTime='" + clientPushTime + '\'' +
                ", androidVersion='" + androidVersion + '\'' +
                ", appVersionName='" + appVersionName + '\'' +
                '}';
    }

    public OsInfo getPcOsinfo(){
        OsInfo osInfo = new OsInfo();
        osInfo.setProcessorId("");
        osInfo.setBaseboardSerial("");
        List<String> hdSerial = new ArrayList<>();
        hdSerial.add("");
        osInfo.setHdSerial(hdSerial);
//        osInfo.getMacAddress().add("");
//        osInfo.getIpAddress().add("");
        osInfo.setBiosSeria("");
        osInfo.setGatewayIp("");
        osInfo.setGatewayMac("");
        osInfo.setProcessorArchitecture("");
        osInfo.setOsVersion("");
        osInfo.setPublicIP("");
//        osInfo.getIMEI().add("");
//        osInfo.getIMEI().add("");
        osInfo.setLocation("");
        osInfo.setClientPushTime("");
        osInfo.setLocation("");
        osInfo.setPublicIP("");
        osInfo.setGatewayIp("");

        List<String> macAddress = new ArrayList<>();
        macAddress.add(getMAC() ==  null ?"":getMAC());
        osInfo.setMacAddress(macAddress);
        List<String> ipAddress = new ArrayList<>();
        ipAddress.add(getIp() == null ?"":getIp());
        osInfo.setIpAddress(ipAddress);
        osInfo.setGatewayIp(getGateway()== null ? "" :getGateway());
        osInfo.setPublicIP(getPublicIP() == null ? "":getPublicIP());
        List<String> imeis = new ArrayList<>();
        imeis.add(getIMEI1() == null ? "":getIMEI1());
        imeis.add(getIMEI2() == null ? "":getIMEI2());
        osInfo.setIMEI(imeis);
        osInfo.setLocation(getLocation() == null ? "" : getLocation());
        osInfo.setClientPushTime(getClientPushTime());
        osInfo.setProcessorArchitecture(getCpuVersion() == null ? "":getCpuVersion());
//        osInfo.getIpAddress().add(getIp() == null ? "" : getIp());
        osInfo.setOsVersion(getAndroidVersion() == null ? "": getAndroidVersion());
        osInfo.setGatewayMac(getGatewayMac() == null ? "" : getGatewayMac());
        return osInfo;
    }
}
