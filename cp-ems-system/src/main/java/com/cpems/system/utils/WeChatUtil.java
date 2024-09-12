package com.cpems.system.utils;

import cn.binarywang.wx.miniapp.api.WxMaService;
import cn.binarywang.wx.miniapp.api.impl.WxMaServiceImpl;
import cn.binarywang.wx.miniapp.bean.WxMaSubscribeMessage;
import cn.binarywang.wx.miniapp.config.impl.WxMaDefaultConfigImpl;
import com.alibaba.fastjson.JSON;
import com.alibaba.fastjson.JSONObject;
import com.cpems.common.constant.WxConstants;
import com.cpems.common.utils.DateUtils;
import com.cpems.common.utils.StringUtils;
import com.cpems.system.domain.vo.InspectionRecordVo;
import me.chanjar.weixin.common.error.WxErrorException;

import java.util.HashMap;
import java.util.Map;

/**
 * @Author cpems
 * @Date 2023/11/21 16:49
 **/
public class WeChatUtil {

    /**
     * 获取access_token
     *
     * @return access_token
     */
    public static JSONObject getAccessToken() {
        Map<String, String> requestUrlParam = new HashMap<>();
        //小程序appId
        requestUrlParam.put("appid", WxConstants.APP_ID);
        //小程序secret
        requestUrlParam.put("secret", WxConstants.APP_SECRET);
        //默认参数(获取access_token使用)
        requestUrlParam.put("grant_type", "client_credential");
        //发送get请求读取调用微信接口获取access_token
        return JSON.parseObject(HttpClientUtil.doGet(WxConstants.ACCESS_TOKEN_REQUEST_URL, requestUrlParam));
    }

    /**
     * 获取sessionKey和openid
     *
     * @param loginCode 小程序登录code
     * @return
     */
    public static JSONObject getSessionKeyOrOpenId(String loginCode) {
        String requestUrl = WxConstants.LOGIN_REQUEST_URL;
        Map<String, String> requestUrlParam = new HashMap<>();
        //小程序appId
        requestUrlParam.put("appid", WxConstants.APP_ID);
        //小程序secret
        requestUrlParam.put("secret", WxConstants.APP_SECRET);
        //小程序端返回的code
        requestUrlParam.put("js_code", loginCode);
        //默认参数
        requestUrlParam.put("grant_type", WxConstants.GRANT_TYPE_OPENID);
        //发送post请求读取调用微信接口获取openid用户唯一标识
        return JSON.parseObject(HttpClientUtil.doPost(requestUrl, requestUrlParam));
    }

    /**
     * 推送巡检消息给指定的用户
     */
    public static Boolean pushInspectionMessage(String openId, InspectionRecordVo inspection) {
        /*String requestUrl = WxConstants.MESSAGE_SUBSCRIBE_SEND_URL + "?access_token=" + getAccessToken().get("access_token").toString();
        JSONObject requestUrlParam = new JSONObject();
        requestUrlParam.put("template_id", WxConstants.INSPECTION_TEMPLATE_ID);
        requestUrlParam.put("page", WxConstants.INSPECTION_PAGE);
        requestUrlParam.put("touser", openId);
        JSONObject data = new JSONObject();
        data.put("character_string1", new JSONObject().fluentPut("value", inspection.getId().toString()));
        data.put("thing2", new JSONObject().fluentPut("value", inspection.getPlanContent()));
        data.put("thing3", new JSONObject().fluentPut("value", inspection.getInspectionPerson()));
        data.put("time4", new JSONObject().fluentPut("value", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,inspection.getPlanTime())));
        data.put("thing5", new JSONObject().fluentPut("value", StringUtils.isNotBlank(inspection.getInspectionRemark()) ? inspection.getInspectionRemark() : "暂无内容"));
        requestUrlParam.put("data", data);
        System.out.println(requestUrlParam);
        JSONObject result = JSON.parseObject(HttpClientUtil.doPostJson(requestUrl, requestUrlParam.toString()));
        System.out.println(result);
        return true;*/
        WxMaDefaultConfigImpl config = new WxMaDefaultConfigImpl();
        config.setAppid(WxConstants.APP_ID);
        config.setSecret(WxConstants.APP_SECRET);
        config.setToken(WxConstants.TOKEN);
        config.setAesKey(WxConstants.AES_KEY);
        config.setMsgDataFormat(WxConstants.MSG_DATA_FORMAT);
        WxMaService service = new WxMaServiceImpl();
        service.setWxMaConfig(config);
        Map<String, String> data = new HashMap<>();
        data.put("character_string1", inspection.getId().toString());
        data.put("thing2", inspection.getPlanContent());
        data.put("thing3", inspection.getInspectionPerson());
        data.put("time4", DateUtils.parseDateToStr(DateUtils.YYYY_MM_DD_HH_MM_SS,inspection.getPlanTime()));
        data.put("thing5",StringUtils.isNotBlank(inspection.getInspectionRemark()) ? inspection.getInspectionRemark() : "暂无内容");
        WxMaSubscribeMessage wxMaSubscribeMessage = WxMaSubscribeMessage.builder()
            .toUser(openId)
            .templateId(WxConstants.INSPECTION_TEMPLATE_ID)
            .page(WxConstants.INSPECTION_PAGE)
            .build();
        // 设置将推送的消息
        data.forEach((k, v) -> {
            wxMaSubscribeMessage.addData(new WxMaSubscribeMessage.Data(k, v));
        });
        try {
            service.getMsgService().sendSubscribeMsg(wxMaSubscribeMessage);
        } catch (WxErrorException e) {
            e.printStackTrace();
        }
        return true;
    }
}
