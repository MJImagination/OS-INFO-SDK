package util;/**
 * Created by ancun on 2017/12/5.
 */



import java.io.*;
import java.util.*;

import entity.OsInfo;
import util.SevenZip.Compression.LZMA.Decoder;
import util.SevenZip.Compression.LZMA.Encoder;
import util.SevenZip.InfoType;

/**
 * <p/>
 *
 * @author:<a href="mailto:ouyangxu@ancun.com">ouyangxu</a><br>
 * create at:2017年12月05日 13:38
 **/
public class LzmaUtils {
    public static void main(String[] args) {
//        System.out.println(LzmaUtils.decodeToOsInfo("5d000000010000848de769c60f43d0a82c92207c9db6d7e34c10cefb1037a6899b86471f0494a41077f5e1fa3133dbe5bebd1c52b89e7fefd9ce253356924f7b656f0766133164371ea6d360a28c4501666ba70690963d5791000df9b24d6bf3832ba5925a450d36b2d9e04439f9ab50fb1f81016c99816c9200"));

        OsInfo osInfo = new OsInfo();
        osInfo.setProcessorId("BFEBFBFF000306C3");
        osInfo.setBaseboardSerial("/8V6WG82/CN7016359F03UJ/");
        osInfo.getHdSerial().add("W3TMK8QL");
        osInfo.getMacAddress().add("38:A4: ED: B1: DD: 63");
        osInfo.getIpAddress().add("10.0.10.163");
        osInfo.setBiosSeria("8V6WG82");
        osInfo.setGatewayIp("10.0.10.254");
        osInfo.setGatewayMac("58:6A:B1:7D:A0:54");
        osInfo.setProcessorArchitecture("64");
        osInfo.setOsVersion("Windows7");
        osInfo.setPublicIP("115.236.89.211");
        osInfo.getIMEI().add("869718020313547");
        osInfo.getIMEI().add("869718020313554");
        osInfo.setLocation("120.101709,120.101709");
        osInfo.setClientPushTime("2017-12-12 14:14:14");

        String osInfoStr = "";
        osInfoStr = encodeToOsInfo(osInfo);
        System.out.println(osInfoStr);
        System.out.println(decodeToOsInfo(osInfoStr));
    }

    private static long outSize = 1024;

    public static String encodeToOsInfo(OsInfo osInfo) {
        return toHexString(encodeLzma(encodeOsInfo(osInfo)));
    }

    public static byte[] encodeOsInfo(OsInfo osInfo) {
        if (osInfo == null) {
            System.out.println("OsInfo解析：数据不完整");
        }
        byte[] osInfoBytes = new byte[2];

        byte[] temp = new byte[0];

        temp = osInfo.getProcessorId().getBytes();
        osInfoBytes = concatOsInfo(InfoType.TPYE_PROCESSOR_ID, osInfoBytes, temp);

        temp = osInfo.getBaseboardSerial().getBytes();
        osInfoBytes = concatOsInfo(InfoType.TYPE_BASEBOARD_SERIAL, osInfoBytes, temp);

        temp = osInfo.getHdSerial().get(0).getBytes();
        osInfoBytes = concatOsInfo(InfoType.TYPE_HD_SERIAL, osInfoBytes, temp);

        String macAddress = "";
        for(String mac : osInfo.getMacAddress()) {
            for(String str : mac.split(":")) {
                macAddress += str.trim();
            }
        }
        osInfoBytes = concatOsInfo(InfoType.TYPE_MAC_ADDRESS, osInfoBytes, hexStringToByte(macAddress.toLowerCase()));

        temp = convertStrToIpByte(osInfo.getIpAddress());
        osInfoBytes = concatOsInfo(InfoType.TYPE_IP_ADDRESS, osInfoBytes, temp);

        temp = osInfo.getBiosSeria().getBytes();
        osInfoBytes = concatOsInfo(InfoType.TYPE_BIOSS_SERIAL, osInfoBytes, temp);

        temp = strIPToLong(osInfo.getGatewayIp()).getBytes();
        osInfoBytes = concatOsInfo(InfoType.TYPE_GATEWAY_IP, osInfoBytes, temp);

        String gatewayMAC = "";
        for(String str : osInfo.getGatewayMac().split(":")) {
            gatewayMAC += str.trim();
        }
        osInfoBytes = concatOsInfo(InfoType.TYPE_GATEWAY_MAC, osInfoBytes, hexStringToByte(gatewayMAC.toLowerCase()));

        temp = osInfo.getProcessorArchitecture().getBytes();
        osInfoBytes = concatOsInfo(InfoType.TYPE_PROCESSOR_ARCHITECTURE, osInfoBytes, temp);

        temp = osInfo.getOsVersion().getBytes();
        osInfoBytes = concatOsInfo(InfoType.TYPE_OS_VERSION, osInfoBytes, temp);

        temp = osInfo.getPublicIP().getBytes();
        osInfoBytes = concatOsInfo(InfoType.TYPE_PUBLIC_IP, osInfoBytes, temp);

        temp = osInfo.getLocation().getBytes();
        osInfoBytes = concatOsInfo(InfoType.TYPE_LOCATION, osInfoBytes, temp);

        temp = osInfo.getClientPushTime().getBytes();
        osInfoBytes = concatOsInfo(InfoType.TYPE_CLIENT_PUSH_TIME, osInfoBytes, temp);

        temp = new byte[0];
        for(String str : osInfo.getIMEI()) {
            temp = concatBytes(temp, str.trim().getBytes());
        }
        osInfoBytes = concatOsInfo(InfoType.TYPE_IMEI, osInfoBytes, temp);
        byte[] result = new byte[osInfoBytes.length - 2];
        System.arraycopy(osInfoBytes, 0, result, 0, osInfoBytes.length - 2);
        return result;
    }

    public static byte[] concatOsInfo(int type, byte[] data1, byte[] data2) {
        data1[data1.length - 2] = (byte) type;
        data1[data1.length - 1] = (byte) data2.length;

        byte[] data3 = new byte[data1.length + data2.length + 2];
        System.arraycopy(data1, 0, data3, 0, data1.length);
        System.arraycopy(data2, 0, data3, data1.length, data2.length);
        return data3;
    }

    public static byte[] concatBytes(byte[] data1, byte[] data2) {
        byte[] data3 = new byte[data1.length + data2.length];
        System.arraycopy(data1, 0, data3, 0, data1.length);
        System.arraycopy(data2, 0, data3, data1.length, data2.length);
        return data3;
    }

    private static byte[] convertStrToIpByte(List<String> ipAddress) {
        byte[] ipBytes = new byte[0];
        for (String ip : ipAddress) {
            String ipStr = strIPToLong(ip);
            ipBytes = concatBytes(ipBytes, ipStr.getBytes());
        }
        return ipBytes;
    }

    private static String strIPToLong(String ip) {
        String ipStr = "";
        long ipLong = 0l;
        String[] ips = ip.split("\\.");
        if (ips.length == 4) {
            for (int i = 3; i >= 0; i--) {
                ipLong += Long.parseLong(ips[i]) & 0xff;
                if(i != 0) ipLong = ipLong << 8;
            }
            ipStr = String.valueOf(ipLong);
        } else {
            return ipStr;
        }
        return ipStr;
    }

    public static OsInfo decodeToOsInfo(String osInfoHex) {
        return decodeToOsInfo(hexStringToByte(osInfoHex));
    }

    public static OsInfo decodeToOsInfo(byte[] src) {
        Map<Integer,byte[]> osInfoMap = decodeOsInfoLzma(src);
        OsInfo osInfo = new OsInfo();
        if (null == osInfoMap || osInfoMap.size() < 10) {
            System.out.println("OsInfo解析：数据不完整");
        }
        osInfo.setProcessorId(new String(osInfoMap.get(InfoType.TPYE_PROCESSOR_ID)));
        osInfo.setBaseboardSerial(new String(osInfoMap.get(InfoType.TYPE_BASEBOARD_SERIAL)));
        osInfo.setBiosSeria(new String(osInfoMap.get(InfoType.TYPE_BIOSS_SERIAL)));
        osInfo.setProcessorArchitecture(new String(osInfoMap.get(InfoType.TYPE_PROCESSOR_ARCHITECTURE)));
        osInfo.setOsVersion(new String(osInfoMap.get(InfoType.TYPE_OS_VERSION)));
        osInfo.setPublicIP(new String(osInfoMap.get(InfoType.TYPE_PUBLIC_IP) == null ? new byte[0] : osInfoMap.get(InfoType.TYPE_PUBLIC_IP)));
        osInfo.setLocation(new String(osInfoMap.get(InfoType.TYPE_LOCATION) == null ? new byte[0] : osInfoMap.get(InfoType.TYPE_LOCATION)));
        osInfo.setClientPushTime(new String(osInfoMap.get(InfoType.TYPE_CLIENT_PUSH_TIME) == null ? new byte[0] : osInfoMap.get(InfoType.TYPE_CLIENT_PUSH_TIME)));

        byte[] gatewayIpBytes = osInfoMap.get(InfoType.TYPE_GATEWAY_IP);
        if (gatewayIpBytes.length >= 10) {
            osInfo.setGatewayIp(convertByteToIpStr(gatewayIpBytes,0));
        }

        osInfo.setGatewayMac(macFormat(osInfoMap.get(InfoType.TYPE_GATEWAY_MAC),0));

        //ip处理
        byte[] ipBytes = osInfoMap.get(InfoType.TYPE_IP_ADDRESS);
        int count = ipBytes.length / 10;
        for (int i = 0;i < count;i++) {
            osInfo.getIpAddress().add(convertByteToIpStr(ipBytes,i));
        }

        //mac处理
        byte[] macBytes = osInfoMap.get(InfoType.TYPE_MAC_ADDRESS);
        int macCount = macBytes.length / 6;
        for (int i = 0; i < macCount; i++) {
            osInfo.getMacAddress().add(macFormat(macBytes,i));
        }

        //硬盘序列号处理
        byte[] hdBytes = osInfoMap.get(InfoType.TYPE_HD_SERIAL);
        osInfo.getHdSerial().add(new String(hdBytes));

        //IMEI处理
        byte[] IMEIBytes = osInfoMap.get(InfoType.TYPE_IMEI) == null ? new byte[0] : osInfoMap.get(InfoType.TYPE_IMEI);
        int IMEICount = IMEIBytes.length / 15;
        for (int i = 0; i < IMEICount; i++) {
            osInfo.getIMEI().add(convertByteToIMEIStr(IMEIBytes, i));
        }
        return osInfo;
    }

    public static byte[] hexStringToByte(String hex) {
        int len = (hex.length() / 2);
        byte[] result = new byte[len];
        char[] achar = hex.toCharArray();
        for (int i = 0; i < len; i++) {
            int pos = i * 2;
            result[i] = (byte) (toByte(achar[pos]) << 4 | toByte(achar[pos + 1]));
        }
        return result;
    }

    private static int toByte(char c) {
        byte b = (byte) "0123456789abcdef".indexOf(c);
        return b;
    }

    public static Map<Integer,byte[]> decodeOsInfoLzma(byte[] src) {
        Map<Integer,byte[]> osInfoMap = new HashMap<>();
        byte[] osInfoBytes = decodeLzma(src);
        int start = 0;
        while (start >= 0) {
            start = decodeOsInfo(osInfoBytes, start, osInfoMap);
        }
        return osInfoMap;
    }

    public static byte[] decodeLzma(byte[] src) {
        ByteArrayInputStream bais = null;
        ByteArrayOutputStream baos = null;
        byte[] result = null;
        try {
            bais = new ByteArrayInputStream(src);
            baos = new ByteArrayOutputStream();
            decodeLzma(bais, baos);
            result = baos.toByteArray();
        } catch (IOException e) {
            e.printStackTrace();
        }finally {
            if (baos != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
            if (bais != null) {
                try {
                    baos.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
        return result;
    }

    public static void decodeLzma(final InputStream in, final OutputStream out)
            throws IOException {
        Decoder decoder = new Decoder();
        byte[] properties = new byte[5];
        if (in.read(properties) != 5) {
            throw new IOException("LZMA file has no header!");
        }

        if (!decoder.SetDecoderProperties(properties)) {
            throw new IOException("Decoder properties cannot be set!");
        }
        if (!decoder.Code(in, out, outSize)) {
            throw new IOException("Decoding unsuccessful!");
        }
    }

    public static byte[] encodeLzma(byte[] text) {
        byte[] result = new byte[0];
        ByteArrayInputStream inStream = new ByteArrayInputStream(text);
        ByteArrayOutputStream outStream = new ByteArrayOutputStream();
        try {
            boolean eos = true;
            Encoder encoder = new Encoder();
            encoder.SetEndMarkerMode(eos);
            encoder.WriteCoderProperties(outStream);

            encoder.Code(inStream, outStream, -1, -1, null);
            result = outStream.toByteArray();
        } catch (Exception e) {
            e.printStackTrace();
        } finally {
            try {
                if (inStream != null) {
                    inStream.close();
                }

                if (outStream != null) {
                    outStream.close();
                }
            }catch (Exception e) {
                e.printStackTrace();
            }
        }
        return result;
    }

    private static int decodeOsInfo(byte[] src, int start, Map<Integer, byte[]> osInfoMap) {
        if (src.length < start + 2) {
            return -1;
        }
        byte key = src[start];
        byte length = src[start + 1];
        int end = start + 2 + length;
        if (length == 0) {
            osInfoMap.put((int) key,new byte[0]);
            return end;
        }
        if (length < 0 ) {
            return -1;
        }
        if (src.length < end) {
            return -1;
        }
        byte[] content = Arrays.copyOfRange(src, start + 2, end);
        osInfoMap.put((int) key,content);
        return end;
    }

    private static String convertByteToIpStr(byte[] ipBytes,int count) {
        int start = count * 10;
        String ipStr = new String(Arrays.copyOfRange(ipBytes,start,start + 10));
        long ip = Long.valueOf(ipStr);
        return longIPToStr(ip);
    }

    private static String longIPToStr(long longIP) {
        String ip = "";
        for (int i = 3; i >= 0; i--) {
            ip  += String.valueOf((longIP & 0xff));
            if(i != 0){
                ip += ".";
            }
            longIP = longIP >> 8;
        }
        return ip;
    }

    private static String macFormat(byte[] macBytes,int count) {
        int start = count * 6;
        String macHex = toHexString(Arrays.copyOfRange(macBytes, start, start + 6));
        return macFormat(macHex);
    }

    private static String macFormat(String macHex) {
        if (macHex == null || macHex.length() < 12) {
            return macHex;
        }
        macHex = macHex.toUpperCase();
        String[] macs = new String[6];
        macs[0] = macHex.substring(0,2);
        macs[1] = macHex.substring(2,4);
        macs[2] = macHex.substring(4,6);
        macs[3] = macHex.substring(6,8);
        macs[4] = macHex.substring(8,10);
        macs[5] = macHex.substring(10,12);
        return String.format("%s-%s-%s-%s-%s-%s",macs);
    }

    private static String convertByteToIMEIStr(byte[] IMEIBytes,int count) {
        int start = count * 15;
        String IMEI = new String(Arrays.copyOfRange(IMEIBytes,start,start + 15));
        return IMEI;
    }

    public static String toHexString(byte[] b){
        StringBuffer buffer = new StringBuffer();
        for (int i = 0; i < b.length; ++i){
            buffer.append(toHexString(b[i]));
        }
        return buffer.toString();
    }
    public static String toHexString(byte b){
        String s = Integer.toHexString(b & 0xFF);
        if (s.length() == 1){
            return "0" + s;
        }else{
            return s;
        }
    }
}
