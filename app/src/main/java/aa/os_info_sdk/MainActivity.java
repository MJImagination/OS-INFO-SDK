package aa.os_info_sdk;

import android.Manifest;
import android.app.Fragment;
import android.app.FragmentManager;
import android.app.FragmentTransaction;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.location.Location;
import android.location.LocationManager;
import android.net.Uri;
import android.net.wifi.ScanResult;
import android.net.wifi.WifiInfo;
import android.net.wifi.WifiManager;
import android.os.Bundle;
import android.support.annotation.NonNull;
import android.support.design.widget.BottomNavigationView;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.telephony.TelephonyManager;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.lang.reflect.Method;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import entity.OSInfoAndroid;
import entity.OsInfo;
import fragment.FragmentMain;
import fragment.FragmentHistory;
import fragment.FragmentWebView;
import util.AndoirdInfoUtil;

import static util.AndoirdInfoUtil.getMacAddress;
import static util.LzmaUtils.encodeToOsInfo;

public class MainActivity extends AppCompatActivity {

    //fragment相关
    private FragmentManager fragmentManager;
    private FragmentTransaction transaction;
    private FragmentMain fragmentMain;
    private FragmentHistory fragmentHistory;
    private Fragment currentFragment;
    private FragmentWebView fragmentWebView;

    //需要申请的权限数组
    String[] requirePermissions = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
    private static final int PERMISSIONS_REQUEST_CODE = 1;  //申请权限的requestCode

    //OSInfo信息获得
    private TelephonyManager mTelephonyManager; //电话管理相关
    private LocationManager locationManager;    //地理位置
    private String locationProvider;            //地理位置
//    private OSInfoAndroid osInfo;               //android OSInfo类
//    private OsInfo osInfoPC;                    //pc端 osinfo类
//    private String publicIP;                    //外网地址
    public String osInfoStr;                    //osInfo编码后的字符串
    public OsInfo osInfoPC;                     //持久化对象

    //bottom菜单
    private BottomNavigationView.OnNavigationItemSelectedListener mOnNavigationItemSelectedListener
            = new BottomNavigationView.OnNavigationItemSelectedListener() {
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            switch (item.getItemId()) {
                case R.id.navigation_home:
                    if (fragmentMain == null) {
                        fragmentMain = new FragmentMain();
                    }
                    return switchFragment(fragmentMain);
                case R.id.navigation_dashboard:
                    if (fragmentHistory == null) {
                        fragmentHistory = new FragmentHistory();
                    }
                    return switchFragment(fragmentHistory);
                case R.id.navigation_notifications:
                    if (fragmentWebView == null) {
                        fragmentWebView = new FragmentWebView();
                    }
                    return switchFragment(fragmentWebView);
            }
            return false;
        }

    };



    public boolean switchFragment(Fragment fragment) {
        transaction = fragmentManager.beginTransaction();
        if (fragment.isAdded()) {
            transaction.hide(currentFragment).show(fragment).commit();
        } else {
            transaction.hide(currentFragment).add(R.id.content, fragment).commit();
        }
        currentFragment = fragment;
        return true;

    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        //注册底部菜单监听事件
        BottomNavigationView navigation = (BottomNavigationView) findViewById(R.id.navigation);
        navigation.setOnNavigationItemSelectedListener(mOnNavigationItemSelectedListener);

        //设置主fragment
        fragmentManager = getFragmentManager();
        transaction = fragmentManager.beginTransaction();

        fragmentMain = new FragmentMain();
        transaction.add(R.id.content, fragmentMain).show(fragmentMain).commit();
        currentFragment = fragmentMain;

        //权限检查，检查成功后初始化OSInfo信息
        mTelephonyManager = (TelephonyManager) getSystemService(Context.TELEPHONY_SERVICE);
        permission();

//        Toast.makeText(this,"IMEI:"+(getIMEI(MainActivity.this)==null?"":getIMEI(MainActivity.this).toString()),Toast.LENGTH_LONG).show();

    }

    //推送结果回显
    public void showResponse(final String response) {
        runOnUiThread(new Runnable() {  //更新主线程
            @Override
            public void run() {
                TextView response_test = (TextView) findViewById(R.id.response_test);
                response_test.setText(response);
            }
        });
    }

    /**
     * 获得权限
     */
    private void permission() {

        //未授权的权限集合
        List<String> unPsermissions = new ArrayList<>();
        for (int i = 0; i < requirePermissions.length; i++) {
            if (ContextCompat.checkSelfPermission(this, requirePermissions[i]) != PackageManager.PERMISSION_GRANTED) {
                unPsermissions.add(requirePermissions[i]);
            }
        }
        if (unPsermissions.isEmpty()) { //为空表示都授权了
//            Toast.makeText(this, "权限都授予了", Toast.LENGTH_SHORT).show();
//            getPublicIP();
//            initOSinfo();   //初始化OSInfo信息
        } else {

            //准备授权的数组
            String[] readyPermissions = unPsermissions.toArray(new String[unPsermissions.size()]);
            ActivityCompat.requestPermissions(this, readyPermissions, PERMISSIONS_REQUEST_CODE);
        }

    }

    /**
     * 如果第一次拒绝，后面几次引导设置
     *
     * @param name
     */
    private void showSetting(String name) {
        //当拒绝了授权后，为提升用户体验，可以以弹窗的方式引导用户到设置中去进行设置
        new AlertDialog.Builder(MainActivity.this)
                .setMessage("应用需要开启" + name + "权限")
                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
                    @Override
                    public void onClick(DialogInterface dialog, int which) {
                        //引导用户到设置中去进行设置
                        Intent intent = new Intent();
                        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
                        intent.setData(Uri.fromParts("package", getPackageName(), null));
                        startActivity(intent);

                    }
                })
                .setNegativeButton("取消", null)
                .create()
                .show();
    }

    /**
     * 请求权限后的回调
     *
     * @param requestCode
     * @param permissions
     * @param grantResults
     */
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
        switch (requestCode) {
            case PERMISSIONS_REQUEST_CODE:
                if (grantResults.length > 0) {
                    List<String> deniedPermissionList = new ArrayList<>();
                    boolean countFlag = true;
                    for (int i = 0; i < grantResults.length; i++) {
                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
                            deniedPermissionList.add(permissions[i]);
                            if (countFlag) {
//                                Toast.makeText(this, "请开启相关权限", Toast.LENGTH_SHORT).show();
                            }
                            countFlag = false; //控制弹窗只显示一次
                        }
                    }
                    if (deniedPermissionList.isEmpty()) {
                        //已经全部授权
//                        call();
                    } else {


                        //勾选了对话框中”Don’t ask again”的选项, 返回false
                        for (String deniedPermission : deniedPermissionList) {
                            boolean flag = false;
                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
                                flag = shouldShowRequestPermissionRationale(deniedPermission);
                            }
                            if (!flag) {
                                //拒绝授权
                                showSetting("获取手机号码、IMEI、定位");//重新申请权限
                                return;
                            }
                        }
                        //拒绝授权
                        showSetting("获取手机号码、IMEI、定位");//重新申请权限

                        //其他逻辑(这里当权限都同意的话就执行打电话逻辑)
                    }
                }
                break;
            default:
                break;
        }
    }

    /****************************************OSInfo信息*******************************/
    public TelephonyManager getTelephonyManager(Context context) {
        // 获取telephony系统服务，用于取得SIM卡和网络相关信息
        if (mTelephonyManager == null) {
            mTelephonyManager = (TelephonyManager) context
                    .getSystemService(Context.TELEPHONY_SERVICE);
        }
        return mTelephonyManager;
    }


    /**
     * 唯一的设备ID： GSM手机的 IMEI 和 CDMA手机的 MEID
     * 取得手机IMEI
     * available.
     */
    public String getDeviceId(Context context) {
        String mDeviceId = getTelephonyManager(context).getDeviceId();
        return mDeviceId;
    }

    /**
     * 取得IMEI SV
     *
     * @param context
     * @return
     */
    public String getDeviceSoftwareVersion(Context context) {
        String mDeviceSoftwareVersion = getTelephonyManager(context).getDeviceSoftwareVersion();
        return mDeviceSoftwareVersion;
    }

    /**
     * 取得手机IMSI
     *
     * @param context
     * @return
     */
    public String getSubscriberId(Context context) {
        String mSubscriberId = getTelephonyManager(context).getSubscriberId();
        return mSubscriberId;
    }

    /**
     * 获取手机MEID
     *
     * @param context
     * @return
     */
    public String getMEID(Context context) {
        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            String meid1 = getTelephonyManager(context).getMeid(1);
            String meid0 = getTelephonyManager(context).getMeid(0);
            String meid2 = getTelephonyManager(context).getMeid(2);
            TelephonyManager manager = (TelephonyManager) MainActivity.this.getApplicationContext().getSystemService(Context.TELEPHONY_SERVICE);
            Method method = null;
            try {
                method = manager.getClass().getMethod("getDeviceId", int.class);
                String imei1 = manager.getDeviceId();

                String imei2 = (String) method.invoke(manager, 1);

                String meid = (String) method.invoke(manager, 2);
            } catch (Exception e) {
                e.printStackTrace();
            }

            return "MEID= " + meid0 + meid1 + meid2;
        }
        return null;
    }

    public Map<String, String> getIMEI(Context context) {


        try {
            Map<String, String> map = new HashMap<>();
            Method method = mTelephonyManager.getClass().getMethod("getDeviceId", int.class);

            String imei1 = mTelephonyManager.getDeviceId();

            String imei2 = (String) method.invoke(mTelephonyManager, 1);
            map.put("IMEI1", imei1);
            map.put("IMEI2", imei2);
            return map;
        } catch (Exception e) {

        }
        return null;

    }

    /**
     * 获得手机号
     *
     * @param context
     * @return
     */
    public String getLine1Number(Context context) {
        String mSubscriberId = getTelephonyManager(context).getLine1Number();
        return mSubscriberId;
    }


    public String getLocation() {
        //获取地理位置管理器
        locationManager = (LocationManager) getSystemService(Context.LOCATION_SERVICE);
        //获取所有可用的位置提供器
        List<String> providers = locationManager.getProviders(true);
        if (providers.contains(LocationManager.GPS_PROVIDER)) {
            //如果是GPS
            locationProvider = LocationManager.GPS_PROVIDER;
        } else if (providers.contains(LocationManager.NETWORK_PROVIDER)) {
            //如果是Network
            locationProvider = LocationManager.NETWORK_PROVIDER;
        } else {
            Toast.makeText(this, "没有可用的位置提供器", Toast.LENGTH_SHORT).show();
            return "";
        }
        //权限判断
        if (ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_FINE_LOCATION) != PackageManager.PERMISSION_GRANTED && ActivityCompat.checkSelfPermission(this, Manifest.permission.ACCESS_COARSE_LOCATION) != PackageManager.PERMISSION_GRANTED) {
            return "";
        }
        //获取Location
        Location location = locationManager.getLastKnownLocation(locationProvider);
        if (location != null) {
            return location.getLongitude() + "," + location.getLatitude();
        }
        //监视地理位置变化
//        locationManager.requestLocationUpdates(locationProvider, 3000, 1, locationListener);

        return "";
    }



    public String initOSinfo(String publicIP) {
        osInfoPC = null;
        Map<String, String> netWorkInfo = AndoirdInfoUtil.getMAC(MainActivity.this);
        Map<String, String> IMEI = getIMEI(MainActivity.this);
        OSInfoAndroid osInfo = new OSInfoAndroid();
        osInfo.setPublicIP(publicIP);
        if (ContextCompat.checkSelfPermission(this, requirePermissions[1]) == PackageManager.PERMISSION_GRANTED) {
            osInfo.setLocation(getLocation());
        }
        if (ContextCompat.checkSelfPermission(this, requirePermissions[0]) == PackageManager.PERMISSION_GRANTED) {
            osInfo.setIp(netWorkInfo.get("ipAddress"));
            osInfo.setNetMask(netWorkInfo.get("netmask"));
            osInfo.setGateway(netWorkInfo.get("gateway"));
            osInfo.setServerAddress(netWorkInfo.get("serverAddress"));
            osInfo.setDns1(netWorkInfo.get("dns1"));
            osInfo.setDns2(netWorkInfo.get("dns2"));
            osInfo.setMAC(getMacAddress(MainActivity.this));
            if (IMEI != null) { //虚拟机没有卡槽
                osInfo.setIMEI1(IMEI.get("IMEI1"));
                osInfo.setIMEI2(IMEI.get("IMEI2"));
            }
        }
        osInfo.setCpuVersion(AndoirdInfoUtil.getCpuName());

        osInfo.setClientPushTime(new SimpleDateFormat("yyyy-MM-dd hh:mm:SSSS").format(new Date()));
        osInfo.setAndroidVersion(AndoirdInfoUtil.getSystemVersion());
        osInfo.setAppVersionName(AndoirdInfoUtil.getAPPVersionName(MainActivity.this));
        osInfo.setGatewayMac(getConnectedWifiMacAddress(MainActivity.this));

        osInfoPC = osInfo.getPcOsinfo();
        osInfoStr = encodeToOsInfo(osInfoPC);

        OSInfoAndroid osInfoAndroid =osInfo;

        osInfoPC.save();
        return osInfoStr;

    }


    public  String getConnectedWifiMacAddress(Context context) {
        String connectedWifiMacAddress = null;
        WifiManager wifiManager = (WifiManager) context.getSystemService(Context.WIFI_SERVICE);
        List<ScanResult> wifiList;

        if (wifiManager != null) {
            wifiList = wifiManager.getScanResults();
            WifiInfo info = wifiManager.getConnectionInfo();
            if (wifiList != null && info != null) {
                for (int i = 0; i < wifiList.size(); i++) {
                    ScanResult result = wifiList.get(i);
                    if (info.getBSSID().equals(result.BSSID)) {
                        connectedWifiMacAddress = result.BSSID;
                    }
                }
            }
        }
        return connectedWifiMacAddress;
    }




}
