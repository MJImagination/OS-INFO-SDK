package util;

/**
 * Created by ancun on 2017/12/12.
 */

public class PermissionUtil {
//    private Context context;
//    private Activity activity;
//
//    public PermissionUtil(Activity activity){
//        this.context = activity.getApplicationContext();
//        this.activity = activity;
//    }
//
//    private static final int PERMISSIONS_REQUEST_CODE = 1;// 申请权限的requestCode
//    /**
//     * 获得权限
//     */
//    private void permission(PermissionListener permissionListener) {
//        //需要的权限数组
//        String[] requirePermissions = {Manifest.permission.READ_PHONE_STATE, Manifest.permission.ACCESS_FINE_LOCATION, Manifest.permission.ACCESS_COARSE_LOCATION};
//        //未授权的权限集合
//        List<String> unPsermissions = new ArrayList<>();
//        for (int i = 0; i < requirePermissions.length; i++) {
//            if (ContextCompat.checkSelfPermission(context, requirePermissions[i]) != PackageManager.PERMISSION_GRANTED) {
//                unPsermissions.add(requirePermissions[i]);
//            }
//        }
//        if (unPsermissions.isEmpty()) { //为空表示都授权了
//            Toast.makeText(context, "权限都授予了", Toast.LENGTH_SHORT).show();
//            permissionListener.allGet();
//        } else {
//            //准备授权的数组
//            String[] readyPermissions = unPsermissions.toArray(new String[unPsermissions.size()]);
//            ActivityCompat.requestPermissions(activity, readyPermissions, PERMISSIONS_REQUEST_CODE);
//        }
//    }
//
//    /**
//     * 如果第一次拒绝，后面几次引导设置
//     *
//     * @param name
//     */
//    private void showSetting(String name) {
//        //当拒绝了授权后，为提升用户体验，可以以弹窗的方式引导用户到设置中去进行设置
//        new AlertDialog.Builder(activity)
//                .setMessage("应用需要开启" + name + "权限")
//                .setPositiveButton("设置", new DialogInterface.OnClickListener() {
//                    @Override
//                    public void onClick(DialogInterface dialog, int which) {
//                        //引导用户到设置中去进行设置
//                        Intent intent = new Intent();
//                        intent.setAction("android.settings.APPLICATION_DETAILS_SETTINGS");
//                        intent.setData(Uri.fromParts("package", activity.getPackageName(), null));
//                        activity.startActivity(intent);
//
//                    }
//                })
//                .setNegativeButton("取消", null)
//                .create()
//                .show();
//    }
//
//    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
//        super.onRequestPermissionsResult(requestCode, permissions, grantResults);
//        switch (requestCode) {
//            case PERMISSIONS_REQUEST_CODE:
//                if (grantResults.length > 0) {
//                    List<String> deniedPermissionList = new ArrayList<>();
//                    boolean countFlag = true;
//                    for (int i = 0; i < grantResults.length; i++) {
//                        if (grantResults[i] != PackageManager.PERMISSION_GRANTED) {
//                            deniedPermissionList.add(permissions[i]);
//                            if(countFlag){
////                                Toast.makeText(this, "请开启相关权限", Toast.LENGTH_SHORT).show();
//                            }
//                            countFlag = false; //控制弹窗只显示一次
//                        }
//                    }
//                    if (deniedPermissionList.isEmpty()) {
//                        //已经全部授权
//                        call();
//                    } else {
//
//
//                        //勾选了对话框中”Don’t ask again”的选项, 返回false
//                        for (String deniedPermission : deniedPermissionList) {
//                            boolean flag = false;
//                            if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.M) {
//                                flag = activity.shouldShowRequestPermissionRationale(deniedPermission);
//                            }
//                            if (!flag) {
//                                //拒绝授权
//                                showSetting("获取手机号码、IMEI、定位");//重新申请权限
//                                return ;
//                            }
//                        }
//                        //拒绝授权
//                        showSetting("获取手机号码、IMEI、定位");//重新申请权限
//
//                        //其他逻辑(这里当权限都同意的话就执行打电话逻辑)
//                    }
//                }
//                break;
//            default:
//                break;
//        }
//    }
//
//    @Override
//    public int getCount() {
//        return 0;
//    }
//
//    @Override
//    public Object getItem(int i) {
//        return null;
//    }
//
//    @Override
//    public long getItemId(int i) {
//        return 0;
//    }
//
//    @Override
//    public View getView(int i, View view, ViewGroup viewGroup) {
//        return null;
//    }
}
