package com.iceboy.destinedchat.ui.fragment;

import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.ListView;
import android.widget.TextView;

import com.iceboy.destinedchat.R;
import com.iceboy.destinedchat.app.Constant;
import com.iceboy.destinedchat.model.area.City;
import com.iceboy.destinedchat.model.area.Country;
import com.iceboy.destinedchat.model.area.Province;
import com.iceboy.destinedchat.ui.activity.ChatActivity;
import com.iceboy.destinedchat.ui.activity.ChooseAreaActivity;
import com.iceboy.destinedchat.ui.activity.WeatherActivity;
import com.iceboy.destinedchat.utils.HttpUtils;
import com.iceboy.destinedchat.utils.JSONUtils;
import com.iceboy.destinedchat.utils.ThreadUtils;

import org.litepal.LitePal;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.OnClick;
import okhttp3.Call;
import okhttp3.Callback;
import okhttp3.Response;

/**
 * Created by hncboy on 2018/6/17.
 * 选择城市
 */
public class ChooseAreaFragment extends BaseFragment {

    public static final int LEVEL_PROVINCE = 0;
    public static final int LEVEL_CITY = 1;
    public static final int LEVEL_COUNTRY = 2;

    private ArrayAdapter<String> mAdapter;
    private List<String> mDataList = new ArrayList<>(); //保存当前显示在屏幕上的内容
    private List<Province> mProvinceList; //省列表
    private List<City> mCityList; //市列表
    private List<Country> mCountryList; //县列表
    private Province mSelectedProvince; //选中的省份
    private City mSelectedCity; //选中的城市
    private int mCurrentLevel; //当前选中的级别

    @BindView(R.id.list_view)
    ListView mListView;

    @BindView(R.id.title)
    TextView mTitle;

    @BindView(R.id.toolbar_function1)
    ImageView mBack;

    @BindView(R.id.toolbar_function2)
    ImageView mFunction2;

    @Override
    protected int getLayoutRes() {
        return R.layout.fragment_choose_area;
    }

    @Override
    protected void init() {
        initToolbar();
        initListView();
        queryProvinces(); //每次新建活动都加载省级数据
    }

    @OnClick(R.id.toolbar_function1)
    public void onClick(View view) {
        switch (view.getId()) {
            case R.id.toolbar_function1:
                if (mCurrentLevel == LEVEL_COUNTRY) { //当前选中的为县则退回到市
                    queryCities();
                } else if (mCurrentLevel == LEVEL_CITY) { //当前选中的为市则退回到省
                    queryProvinces();
                }
                break;
        }
    }

    private void initListView() {
        //初始化ArrayAdapter，布局使用系统的simple_mlist_item_1，dataList为要显示的数据
        mAdapter = new ArrayAdapter<>(getContext(), android.R.layout.simple_list_item_1, mDataList);
        mListView.setAdapter(mAdapter);
        mListView.setOnItemClickListener(mOnItemClickListener);
    }

    private void initToolbar() {
        mBack.setImageDrawable(getContext().getDrawable(R.drawable.ic_arrow_back_white_24dp));
        mFunction2.setVisibility(View.GONE);
    }

    /**
     * ListView的点击监听
     */
    private AdapterView.OnItemClickListener mOnItemClickListener = new AdapterView.OnItemClickListener() {

        @Override
        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
            if (mCurrentLevel == LEVEL_PROVINCE) { //当前选中的级别为省
                mSelectedProvince = mProvinceList.get(position);
                queryCities();
            } else if (mCurrentLevel == LEVEL_CITY) { //当前选中的级别为市
                mSelectedCity = mCityList.get(position); //具体选中的县
                queryCounties();
            } else if (mCurrentLevel == LEVEL_COUNTRY) {
                //获取这个县的天气id
                String weatherId = mCountryList.get(position).getWeatherId();
                if (getActivity() instanceof ChooseAreaActivity) {
                    //如果该碎片在ChooseAreaActivity中，则直接选择城市
                    startActivity(WeatherActivity.class, Constant.Extra.WEATHER_ID, weatherId);
                } else if (getActivity() instanceof WeatherActivity) {
                    //如果该碎片在WeatherActivity中，则关闭滑动菜单，显示下拉刷新进度条，请求新城市的天气
                    WeatherActivity activity = (WeatherActivity) getActivity();
                    activity.mDrawerLayout.closeDrawers();
                    activity.mSwipeRefresh.setRefreshing(true);
                    activity.requestWeather(weatherId);
                }
            }
        }
    };

    /**
     * 查询全国所有的省，优先从数据库查询，如果没有查到再去服务器上查询
     */
    private void queryProvinces() {
        mTitle.setText(getString(R.string.China));
        mBack.setVisibility(View.GONE);
        mProvinceList = LitePal.findAll(Province.class); //获取数据库中的省数据
        if (mProvinceList.size() > 0) {
            mDataList.clear();
            for (Province province : mProvinceList) {
                mDataList.add(province.getProvinceName()); //将数据添加到dataList
            }
            mAdapter.notifyDataSetChanged(); //更新列表
            mListView.setSelection(0); //将选中行设为第一行
            mCurrentLevel = LEVEL_PROVINCE; //切换到省
        } else {
            queryFromServer(Constant.sAreaAddress, Constant.Extra.PROVINCE); //从服务器中获取省的数据
        }
    }

    /**
     * 查询选中省内所有的市，优先从数据库查询，如果没有查到再去服务器上查询
     */
    private void queryCities() {
        mTitle.setText(mSelectedProvince.getProvinceName());
        mBack.setVisibility(View.VISIBLE);
        mCityList = LitePal.where("provinceId = ?",
                String.valueOf(mSelectedProvince.getId())).find(City.class); //按照当前选中的省获取市
        if (mCityList.size() > 0) {
            mDataList.clear();
            for (City city : mCityList) {
                mDataList.add(city.getCityName()); //将市的数据添加到dataList
            }
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            mCurrentLevel = LEVEL_CITY; //切换到市
        } else {
            int provinceCode = mSelectedProvince.getProvinceCode();
            String address = Constant.sAreaAddress + "/" + provinceCode;
            queryFromServer(address, Constant.Extra.CITY); //从服务器中获取市的数据
        }
    }

    /**
     * 查询选中市所有的县，优先从数据库查询，如果没有查到再去服务器上查询
     */
    private void queryCounties() {
        mTitle.setText(mSelectedCity.getCityName());
        mBack.setVisibility(View.VISIBLE);
        mCountryList = LitePal.where("cityId = ?",
                String.valueOf(mSelectedCity.getId())).find(Country.class); //按照当前选中的市获取县
        if (mCountryList.size() > 0) {
            mDataList.clear();
            for (Country country : mCountryList) {
                mDataList.add(country.getCountryName()); //将县的数据添加到dataList
            }
            mAdapter.notifyDataSetChanged();
            mListView.setSelection(0);
            mCurrentLevel = LEVEL_COUNTRY; //切换到县
        } else {
            int provinceCode = mSelectedProvince.getProvinceCode();
            int cityCode = mSelectedCity.getCityCode();
            String address = Constant.sAreaAddress + "/" + provinceCode + "/" + cityCode;
            queryFromServer(address, Constant.Extra.COUNTRY); //从服务器中获取县的数据
        }
    }

    /**
     * 根据传入的地址和类型从服务器上查询省市县数据
     */
    private void queryFromServer(String address, final String type) {
        showProgress(getString(R.string.loading));
        HttpUtils.sendOkHttpRequest(address, new Callback() {

            @Override
            public void onFailure(Call call, final IOException e) {
                ThreadUtils.runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        hideProgress();
                        toast(getString(R.string.loading_failed));
                        e.printStackTrace();
                    }
                });
            }

            //得到回调的数据相应response
            @Override
            public void onResponse(Call call, Response response) throws IOException {
                String responseText = response.body().string();
                boolean result = false;
                if (Constant.Extra.PROVINCE.equals(type)) {
                    result = JSONUtils.handleProvinceResponse(responseText);
                } else if (Constant.Extra.CITY.equals(type)) {
                    result = JSONUtils.handleCityResponse(responseText, mSelectedProvince.getId());
                } else if (Constant.Extra.COUNTRY.equals(type)) {
                    result = JSONUtils.handleCountryResponse(responseText, mSelectedCity.getId());
                }
                if (result) {
                    //解析和处理成功则加载数据，更新UI要在主线程中完成
                    ThreadUtils.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            hideProgress();
                            if (Constant.Extra.PROVINCE.equals(type)) {
                                queryProvinces();
                            } else if (Constant.Extra.CITY.equals(type)) {
                                queryCities();
                            } else if (Constant.Extra.COUNTRY.equals(type)) {
                                queryCounties();
                            }
                        }
                    });
                }
            }
        });
    }
}
