package top.spencer.crabscore.model.model.staff;

import com.alibaba.fastjson.JSONObject;
import top.spencer.crabscore.base.BaseModel;
import top.spencer.crabscore.base.MyCallback;
import top.spencer.crabscore.common.CommonConstant;

/**
 * @author spencercjh
 */
public class GetAllGroupModel extends BaseModel {
    /**
     * 参数表Integer competitionId, Integer pageNum, Integer pageSize, String jwt
     *
     * @param myCallBack myCallBack
     */
    @Override
    public void execute(MyCallback<JSONObject> myCallBack) {
        String url = CommonConstant.URL + "staff/competition/" + mvpParams[0] + "/groups?pageNum=" +
                mvpParams[1] + "&pageSize=" + mvpParams[2];
        requestGetAPI(url, myCallBack, mvpParams[3]);
    }
}
