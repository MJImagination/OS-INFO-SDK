package fragment;

import android.app.Fragment;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import org.json.JSONException;
import org.json.JSONObject;
import org.litepal.crud.DataSupport;

import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import aa.os_info_sdk.MainActivity;
import aa.os_info_sdk.R;
import entity.History;
import entity.OSInfoAndroid;
import util.AndoirdInfoUtil;
import util.HttpCallbackListener;
import util.HttpUtil;

/**
 * Created by ancun on 2017/12/15.
 */

public class FragmentMain extends Fragment {
    //用于关联主activity
    private MainActivity relateActivity;
    //当前视图
    private View view;

    //交互组件
    private EditText editText_PARTENER_KEY; //接入者key
    private EditText editText_SECERT_KEY;   //接入者scret
    private EditText editText_PRODUCT_KEY;  //产品key
    private EditText editText_SAVEPOINT_KEY;//保全点key
    private EditText editText_GROUP_KEY;    //企业/部门key

    private TextView response_test;//显示推送结果
    private Button button_sdk;
    //常量
    private String publicIP;
    private String osInfoStr = "";


    //handler
    public static final int UPDATE_TEXT = 1;
    public static final int GET_PUBLIC_IP = 2;

    private Handler handler = new Handler() {
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case UPDATE_TEXT:
                    //UI 操作
                    response_test.setText(msg.obj.toString());
                    break;
                case GET_PUBLIC_IP:
                    sendHttpRequest();
                    break;
                default:
                    break;
            }
        }
    };

    public FragmentMain newInstance() {
        return new FragmentMain();
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        //取得主activity对象
        relateActivity = (MainActivity) getActivity();
        //获取当前视图对象
        view = inflater.inflate(R.layout.fragment_main, container, false);

        button_sdk = (Button) view.findViewById(R.id.save_data);
        response_test = (TextView) view.findViewById(R.id.response_test);
//        button_sdk.setOnClickListener(MyFragment.this);
        button_sdk.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
//                relateActivity.initOSinfo("8888888");
                getPublicIP();
            }
        });



        initButtno();

        return view;
    }



    //获得公网IP
    private void getPublicIP() {
        new Thread() {
            @Override
            public void run() {   //开启线程
                String url = "http://ip.chinaz.com/getip.aspx";
                String path = url;
                HttpUtil.sendHttpRequest(path, new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        try {
                            publicIP = new JSONObject(response).get("ip").toString();
                            osInfoStr = relateActivity.initOSinfo(publicIP);

                            Message message = new Message();
                            message.what = GET_PUBLIC_IP;
                            handler.sendMessage(message); //  将Message

                        } catch (JSONException e) {
                            e.printStackTrace();
                        }
                    }

                    @Override
                    public void onError(Exception e) {
                        osInfoStr = relateActivity.initOSinfo("");
                        Message message = new Message();
                        message.what = GET_PUBLIC_IP;
                        handler.sendMessage(message); //  将Message
                        sendHttpRequest();
                    }
                });
            }
        }.start();
    }


    //初始化 活动编辑框并赋值
    private void initButtno() {
        editText_PARTENER_KEY = (EditText) view.findViewById(R.id.PARTNER_KEY);
        editText_PARTENER_KEY.setText("24dd625e52c3669b8aaa802989ee6988");

        editText_SECERT_KEY = (EditText)view.findViewById(R.id.SECRET_KEY);
        editText_SECERT_KEY.setText("6b3feea4db1932b79c59c676474bb18a1fd34155");

        editText_PRODUCT_KEY = (EditText)view.findViewById(R.id.ProductKey);
        editText_PRODUCT_KEY.setText("I-00010001");

        editText_SAVEPOINT_KEY = (EditText)view.findViewById(R.id.SavePointKey);
        editText_SAVEPOINT_KEY.setText("X-00010001");

        editText_GROUP_KEY = (EditText)view.findViewById(R.id.GroupKey);
        editText_GROUP_KEY.setText("G-00010000");

    }

    //推送数据
    private void sendHttpRequest() {
        response_test.setText("正在推送，请稍后...");
        new Thread() {
            @Override
            public void run() {
                EditText value1EditText = (EditText)view.findViewById(R.id.value1);
                String value1 = value1EditText.getText().toString();
                EditText value2EditText = (EditText)view.findViewById(R.id.value2);
                String valve2 = value2EditText.getText().toString();


                String url = "http://10.14.19.32:9001/interfaceService/testAndroid";
                String PARTENER_KEY = editText_PARTENER_KEY.getText().toString();
                String SECERT_KEY = editText_SECERT_KEY.getText().toString();
                String PRODUCT_KEY = editText_PRODUCT_KEY.getText().toString();
                String SAVEPOINT_KEY = editText_SAVEPOINT_KEY.getText().toString();
                String GROUP_KEY = editText_GROUP_KEY.getText().toString();
                String osInfo = osInfoStr;
                String clientIp = AndoirdInfoUtil.getIP(view.getContext());
                Map<String, String> map = new HashMap<>();
                map.put("PARTENER_KEY", PARTENER_KEY);
                map.put("SECERT_KEY", SECERT_KEY);
                map.put("PRODUCT_KEY", PRODUCT_KEY);
                map.put("SAVEPOINT_KEY", SAVEPOINT_KEY);
                map.put("GROUP_KEY", GROUP_KEY);
                map.put("osInfo", osInfo);
                map.put("clientIp", clientIp);
                map.put("value1", value1);
                map.put("value2", valve2);

                StringBuffer sb = new StringBuffer();
                sb.append("?");
                if (map != null && !map.isEmpty()) {
                    for (Map.Entry<String, String> entry : map.entrySet()) {
                        sb.append(entry.getKey()).append("=").append(entry.getValue()).append("&");
                    }
                    sb.deleteCharAt(sb.length() - 1);
                }

                String path = url + sb.toString();

                HttpUtil.sendHttpRequest(path, new HttpCallbackListener() {
                    @Override
                    public void onFinish(String response) {
                        History history = new History();
                        history.setDate(new SimpleDateFormat("yyyy-MM-dd hh:mm:SSSS").format(new Date()));
                        history.setStatus(response);
                        history.setOsInfoID(relateActivity.osInfoPC.getId());
                        history.save();
                        Message message = new Message();
                        message.what = UPDATE_TEXT;
                        message.obj = response;
                        handler.sendMessage(message); //  将Message

                    }

                    @Override
                    public void onError(Exception e) {
                        History history = new History();
                        history.setDate(new SimpleDateFormat("yyyy-MM-dd hh:mm:SSSS").format(new Date()));
                        history.setStatus(e.toString());
                        history.setOsInfoID(relateActivity.osInfoPC.getId());
                        history.save();
                        Message message = new Message();
                        message.what = UPDATE_TEXT;
                        message.obj = e;
                        handler.sendMessage(message); //  将Message
                    }
                });
            }
        }.start();
    }
}
