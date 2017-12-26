package util;

import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.net.ConnectivityManager;
import android.net.DhcpInfo;
import android.net.NetworkInfo;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.telephony.TelephonyManager;
import android.util.Log;

import java.io.BufferedReader;
import java.io.FileReader;
import java.io.IOException;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.NetworkInterface;
import java.net.SocketException;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Map;

/**
 * Created by ancun on 2017/12/11.
 */

public class AndoirdInfoUtil {

    public static String getIP(Context context) {    //废弃

        try {
            for (Enumeration<NetworkInterface> en = NetworkInterface.getNetworkInterfaces(); en.hasMoreElements(); ) {
                NetworkInterface intf = en.nextElement();
                for (Enumeration<InetAddress> enumIpAddr = intf.getInetAddresses(); enumIpAddr.hasMoreElements(); ) {
                    InetAddress inetAddress = enumIpAddr.nextElement();
                    if (!inetAddress.isLoopbackAddress() && (inetAddress instanceof Inet4Address)) {
                        return inetAddress.getHostAddress().toString();
                    }
                }
            }
        } catch (SocketException ex) {
            ex.printStackTrace();
        }
        return null;
    }

    //获得ip、网关、子网掩码
    public static Map<String, String> getIPAddress(Context ctx) {
        WifiManager wifi_service = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcpInfo = wifi_service.getDhcpInfo();
//        WifiInfo wifiinfo = wifi_service.getConnectionInfo();

        String ipAddress = long2ip(dhcpInfo.ipAddress); //ip地址
        String getwayIpS = long2ip(dhcpInfo.gateway);//网关地址
        String netmaskIpS = long2ip(dhcpInfo.netmask);//子网掩码地址

        Map<String, String> map = new HashMap<>();
        map.put("ipAddress", ipAddress);
        map.put("getwayIpS", getwayIpS);
        map.put("netmaskIpS", netmaskIpS);
        return map;
    }

    public static String long2ip(long ip) {
        StringBuffer sb = new StringBuffer();
        sb.append(String.valueOf((int) (ip & 0xff)));
        sb.append('.');
        sb.append(String.valueOf((int) ((ip >> 8) & 0xff)));
        sb.append('.');
        sb.append(String.valueOf((int) ((ip >> 16) & 0xff)));
        sb.append('.');
        sb.append(String.valueOf((int) ((ip >> 24) & 0xff)));
        return sb.toString();
    }

    private static String intToIp(int paramInt) {
        return (paramInt & 0xFF) + "." + (0xFF & paramInt >> 8) + "." + (0xFF & paramInt >> 16) + "."
                + (0xFF & paramInt >> 24);
    }

    public static Map<String,String> getMAC(Context ctx) {
        WifiManager wifi_service = (WifiManager) ctx.getSystemService(Context.WIFI_SERVICE);
        DhcpInfo dhcpInfo = wifi_service.getDhcpInfo();
        WifiInfo wifiinfo = wifi_service.getConnectionInfo();
        StringBuilder sb = new StringBuilder();

        Map<String,String> map = new HashMap<>();
        map.put("ipAddress",intToIp(dhcpInfo.ipAddress));
        map.put("netmask",intToIp(dhcpInfo.netmask));
        map.put("gateway",intToIp(dhcpInfo.gateway));
        map.put("serverAddress",intToIp(dhcpInfo.serverAddress));
        map.put("dns1",intToIp(dhcpInfo.dns1));
        map.put("dns2",intToIp(dhcpInfo.dns2));

        return map;
    }


    public static String getCurrentNetType(Context context) {
        String type = "";
        ConnectivityManager cm = (ConnectivityManager) context.getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo info = cm.getActiveNetworkInfo();
        if (info == null) {
            type = "null";
        } else if (info.getType() == ConnectivityManager.TYPE_WIFI) {
            type = "wifi";
        } else if (info.getType() == ConnectivityManager.TYPE_MOBILE) {
            int subType = info.getSubtype();
            if (subType == TelephonyManager.NETWORK_TYPE_CDMA || subType == TelephonyManager.NETWORK_TYPE_GPRS
                    || subType == TelephonyManager.NETWORK_TYPE_EDGE) {
                type = "2g";
            } else if (subType == TelephonyManager.NETWORK_TYPE_UMTS || subType == TelephonyManager.NETWORK_TYPE_HSDPA
                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_A || subType == TelephonyManager.NETWORK_TYPE_EVDO_0
                    || subType == TelephonyManager.NETWORK_TYPE_EVDO_B) {
                type = "3g";
            } else if (subType == TelephonyManager.NETWORK_TYPE_LTE) {// LTE是3g到4g的过渡，是3.9G的全球标准
                type = "4g";
            }
        }
        return type;
    }


//*************************************

    /**
     * 获取CPU型号
     *
     * @return
     */
    public static String getCpuName() {

        String str1 = "/proc/cpuinfo";
        String str2 = "";

        try {
            FileReader fr = new FileReader(str1);
            BufferedReader localBufferedReader = new BufferedReader(fr);
            while ((str2 = localBufferedReader.readLine()) != null) {
                if (str2.contains("Hardware")) {
                    return str2.split(":")[1];
                }
            }
            localBufferedReader.close();
        } catch (IOException e) {
        }
        return null;

    }


    /**
     * 获取apk的版本号 currentVersionCode
     *
     * @param ctx
     * @return
     */
    public static String getAPPVersionName(Context ctx) {
        int currentVersionCode = 0;
        String appVersionName=null;
        PackageManager manager = ctx.getPackageManager();
        try {
            PackageInfo info = manager.getPackageInfo(ctx.getPackageName(), 0);
            appVersionName = info.versionName; // 版本名
            currentVersionCode = info.versionCode; // 版本号
            System.out.println(currentVersionCode + " " + appVersionName);
        } catch (PackageManager.NameNotFoundException e) {
            e.printStackTrace();
        }
        return appVersionName;
    }


//*********************************************




    public static String getMacAddress(Context context) {
//      该方法在6.0 系统 不再开发，返回 02:00:00:00:00:00


        WifiManager wifiMgr = (WifiManager) context
                .getSystemService(Context.WIFI_SERVICE);
        WifiInfo wifiInfo = wifiMgr.getConnectionInfo();
        Log.i("----getMacAddress--0--", "" + wifiInfo.getMacAddress());

        String macAddress = null;
        StringBuffer buf = new StringBuffer();
        NetworkInterface networkInterface = null;
        try {
            networkInterface = NetworkInterface.getByName("eth1");
            if (networkInterface == null) {
                networkInterface = NetworkInterface.getByName("wlan0");
            }
            if (networkInterface == null) {
                return "02:00:00:00:00:02";
            }
            byte[] addr = networkInterface.getHardwareAddress();


            for (byte b : addr) {
                buf.append(String.format("%02X:", b));
            }
            if (buf.length() > 0) {
                buf.deleteCharAt(buf.length() - 1);
            }
            macAddress = buf.toString();
        } catch (SocketException e) {
            e.printStackTrace();
            return "02:00:00:00:00:02";
        }
        Log.i("----getMacAddress--1--", "" + buf.toString());
        return macAddress;
    }

    /**
     * 获取当前手机系统版本号
     * @return
     */
    public static String getSystemVersion() {
        return android.os.Build.VERSION.RELEASE;
    }

}
