package top.spencer.crabscore.model.model.common;

import com.alibaba.fastjson.JSONObject;
import top.spencer.crabscore.base.BaseModel;
import top.spencer.crabscore.base.MyCallback;
import top.spencer.crabscore.common.CommonConstant;

import java.util.HashMap;
import java.util.Map;

/**
 * @author spencercjh
 */
public class VerifyCodeModel extends BaseModel {
    /**
     * 参数表String mobile, String code
     * 参数校验放在Activity里写了
     * common接口不传JWT
     *
     * @param myCallBack myCallBack
     */
    @Override
    public void execute(MyCallback<JSONObject> myCallBack) {
        String url = CommonConstant.URL + "common/code";
        Map<String, Object> postParams = new HashMap<>(2);
        postParams.put("mobile", mvpParams[0]);
        postParams.put(CommonConstant.CODE, mvpParams[1]);
        requestPostAPI(url, postParams, myCallBack, "");
    }
}
