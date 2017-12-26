package fragment;

import android.app.Fragment;
import android.content.DialogInterface;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.os.Handler;
import android.support.annotation.Nullable;
import android.support.v4.widget.SwipeRefreshLayout;
import android.support.v7.app.AlertDialog;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.Toast;

import org.litepal.crud.DataSupport;

import java.util.List;

import aa.os_info_sdk.MainActivity;
import aa.os_info_sdk.R;
import entity.History;
import entity.OSInfoAndroid;
import entity.OsInfo;

/**
 * Created by ancun on 2017/12/15.
 */

public class FragmentHistory extends Fragment implements SwipeRefreshLayout.OnRefreshListener{

    //当前视图
    private View view;
    //下拉刷新
    private SwipeRefreshLayout swiper;
    List<OsInfo> osInfos ;
    List<History> histories;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, Bundle savedInstanceState) {
        view = inflater.inflate(R.layout.fragment_history,container,false);
        swiper = (SwipeRefreshLayout)view.findViewById(R.id.id_swipe_ly);
        //下拉刷新
        swiper.setOnRefreshListener(this);
        swiper.setColorSchemeResources(android.R.color.holo_blue_light, android.R.color.holo_red_light, android.R.color.holo_orange_light, android.R.color.holo_green_light);
        return view;
    }

    @Override
    public void onActivityCreated(@Nullable Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);


//        Toast.makeText(view.getContext(),DataSupport.findBySQL("select * from OSInfo ").toString(),Toast.LENGTH_LONG).show();
        refreshOSInfo();
    }

    @Override
    public void onHiddenChanged(boolean hidden) {
        // TODO Auto-generated method stub
        super.onHiddenChanged(hidden);
        if (!hidden) {
//            refreshOSInfo();
        }
    }



    public void refreshOSInfo(){
//        osInfos = DataSupport.order("id desc").find(OsInfo.class);
//        ArrayAdapter<OsInfo> adapter = new ArrayAdapter<OsInfo>(
//                view.getContext(), android.R.layout.simple_list_item_1, osInfos);
//        ListView listView = (ListView)view.findViewById(R.id.list_view);
//        listView.setAdapter(adapter);
//        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view,
//                                    int position, long id) {
//                OsInfo osInfo = osInfos.get(position);
//                Toast.makeText(view.getContext(), String.valueOf(osInfo.getId()),
//                        Toast.LENGTH_SHORT).show();
//            }
//        });
        histories = DataSupport.order("id desc").find(History.class);
        ArrayAdapter<History> adapter = new ArrayAdapter<History>(
                view.getContext(), android.R.layout.simple_list_item_1, histories);
        ListView listView = (ListView)view.findViewById(R.id.list_view);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                History history = histories.get(position);
                showOsinfoByID(history.getId());

            }
        });
    }


    private void showOsinfoByID(long id) {
        OsInfo osInfo = DataSupport.find(OsInfo.class,id);;
        new AlertDialog.Builder(view.getContext())
                .setTitle("OSInfo信息")
                .setMessage(osInfo.toString())
                .setPositiveButton("确定", null)
                .show();
    }

    @Override
    public void onRefresh() {
        //刷新
        new Handler().postDelayed(new Runnable() {
            @Override
            public void run() {
                refreshOSInfo();
                //结束后停止刷新
                swiper.setRefreshing(false);
            }
        }, 500);



    }
}
