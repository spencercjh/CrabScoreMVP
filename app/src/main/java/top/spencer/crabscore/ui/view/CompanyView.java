package top.spencer.crabscore.ui.view;

import com.alibaba.fastjson.JSONObject;

/**
 * @author spencercjh
 */
public interface CompanyView extends MyRecycleListView {
    /**
     * 当数据请求成功后，调用此接口显示数据。这个请求一般是该页面里最重要的一个请求，别的请求会在具体的View里写
     *
     * @param successData 成功数据源
     */
    void showGetAllCompanyResponse(JSONObject successData);

    /**
     * 当数据请求成功后，调用此接口显示数据。这个请求一般是该页面里最重要的一个请求，别的请求会在具体的View里写
     *
     * @param successData 成功数据源
     */
    void showUserBindCompanyResponse(JSONObject successData);
}
