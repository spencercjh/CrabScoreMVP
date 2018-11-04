package top.spencer.crabscore.model.constant;

import top.spencer.crabscore.model.model.administrator.AllCompanyModel;
import top.spencer.crabscore.model.model.administrator.AllUserByStatusModel;
import top.spencer.crabscore.model.model.administrator.AllUserModel;
import top.spencer.crabscore.model.model.common.*;
import top.spencer.crabscore.model.model.common.rank.FatnessRankModel;
import top.spencer.crabscore.model.model.common.rank.QualityRankModel;
import top.spencer.crabscore.model.model.common.rank.TasteRankModel;

/**
 * 具体Model类，常量用于反射
 *
 * @author spencercjh
 */
public interface Token {
    String API_LOGIN = LoginModel.class.getName();

    String API_REGIST = RegistModel.class.getName();

    String API_SEND_CODE = SendCodeModel.class.getName();

    String API_VERIFY_CODE = VerifyCodeModel.class.getName();

    String API_LOGIN_OR_REGIST = PhoneLoginModel.class.getName();

    String API_FORGET_PASSWORD = ForgetPasswordModel.class.getName();

    String API_FATNESS_RANK = FatnessRankModel.class.getName();

    String API_QUALITY_RANK = QualityRankModel.class.getName();

    String API_TASTE_RANK = TasteRankModel.class.getName();

    String API_PRESENT_COMPETITION_PROPERTY = PresentCompetitionPropertyModel.class.getName();

    String API_ALL_USER = AllUserModel.class.getName();

    String API_ALL_USER_BY_STATUS = AllUserByStatusModel.class.getName();

    String API_ALL_COMPANY = AllCompanyModel.class.getName();
}