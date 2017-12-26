package util;

public interface HttpCallbackListener {
    /**
     * http请求成功
     * @param response
     */
    void onFinish(String response);

    /**
     * http请求失败
     * @param e
     */
    void onError(Exception e);
}