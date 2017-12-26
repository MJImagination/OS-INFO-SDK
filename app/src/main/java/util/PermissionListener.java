package util;

/**
 * Created by ancun on 2017/12/13.
 */

public interface PermissionListener {
    /**
     * 获得了所有权限
     */
    void allGet();

    /**
     *用户拒绝
     */
    void deny();

    /**
     * 拒绝并勾选不再提示
     */
    void denyAndDoNotShow();
}
