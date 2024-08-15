package com.product.api.auth;

import java.util.HashMap;
import java.util.Map;

import com.alibaba.fastjson.JSONObject;
import com.product.api.service.ApiCallService;
import com.product.api.util.CommonUtil;

/**
 * 授权服务类，主要提供了所有授权服务都会用到的获取授权令牌的功能
 */
public class AuthService {

    /**
     * 通过临时令牌换取授权令牌
     * @param host 请求的主机名，包括域名和端口
     * @param params 请求参数，必填client_id、client_secret、redirect_uri和code，scope和view可选
     * @param needRefreshToken 是否需要返回refreshToken
     * @return getToken请求的json串
     */
    public static String getToken(String host, Map<String, String> params, boolean needRefreshToken){
        String urlHead = "https://" + host + "/openapi/";
        String namespace = "system.oauth2";
        String name = "getToken";
        int version = 1;
        String protocol = "http";
        if(params != null){
            if(params.get("client_id") == null || params.get("client_secret") == null
                    || params.get("redirect_uri") == null || params.get("code") == null){
                System.out.println("params is invalid, lack neccessary key!");
                return null;
            }
            params.put("grant_type", "authorization_code");
            params.put("need_refresh_token", Boolean.toString(needRefreshToken));
            String appKey = params.get("client_id");
            String urlPath = CommonUtil.buildInvokeUrlPath(namespace, name, version, protocol, appKey);
            String result = ApiCallService.callApiTest(urlHead, urlPath, null, params);
            return result;
        }
        return null;
    }

    /**
     * 通过长时令牌换取授权令牌
     * @param host 请求的主机名，包括域名和端口
     * @param params 请求参数，必填client_id、client_secret、redirect_uri和refresh_token，scope和view可选
     * @return
     */
    public static String refreshToken(String host, Map<String, String> params){
        String urlHead = "https://" + host + "/openapi/";
        String namespace = "system.oauth2";
        String name = "getToken";
        int version = 1;
        String protocol = "param2";
        if(params != null){
            if(params.get("client_id") == null || params.get("client_secret") == null
                    || params.get("redirect_uri") == null
                    || params.get("refresh_token") == null){
                System.out.println("params is invalid, lack neccessary key!");
                return null;
            }
            params.put("grant_type", "refresh_token");
            String appKey = params.get("client_id");
            String urlPath = CommonUtil.buildInvokeUrlPath(namespace, name, version, protocol, appKey);
            String result = ApiCallService.callApiTest(urlHead, urlPath, null, params);
            return result;
        }
        return null;
    }

    public static String getAccessToken(String host, String clientId, String appSecret, String redirectUri, String refreshToken) {
        Map<String, String> params = new HashMap<>();
        params.put("client_id", clientId);
        params.put("redirect_uri", redirectUri);
        params.put("client_secret", appSecret);
        params.put("refresh_token", refreshToken);
        String refreshTokenResult = refreshToken(host, params);
        JSONObject jsonObject = JSONObject.parseObject(refreshTokenResult);
        return jsonObject.getString("access_token");
    }


    /*
     * Api test demo
     * */
    public static void main(String[] args){
        String host = "gw.open.1688.com";//国际交易请用"gw.api.alibaba.com"
        String client_id = "";
        String appSecret = "";
        String redirect_uri = "";

        //Test getting token from refreshToken
        String refreshToken = "";
        Map<String, String> params = new HashMap<String, String>();
        params.put("client_id", client_id);
        params.put("redirect_uri", redirect_uri);
        params.put("client_secret", appSecret);
        params.put("refresh_token", refreshToken);
        String refreshTokenResult = refreshToken(host, params);
        System.out.println("用长时令牌换取授权令牌的返回结果：" + refreshTokenResult);
        JSONObject jsonObject1 = JSONObject.parseObject(refreshTokenResult);
        System.out.println("accessToken:" + jsonObject1.get("access_token"));
        //test call api
        String urlPath = "param2/1/" + client_id;
        String urlHead = "http://" + host + "/openapi/";
        Map<String, String> param = new HashMap<String, String>();

        // Create offerQueryParam as a JSON string and URL encode it
        Map<String, Object> offerQueryParam = new HashMap<>();
        offerQueryParam.put("keyword", "balaclava");
        offerQueryParam.put("beginPage", 1);
        offerQueryParam.put("pageSize", 10);
        offerQueryParam.put("country", "en");
        String offerQueryParamJson = JSONObject.toJSONString(offerQueryParam);
        param.put("offerQueryParam", offerQueryParamJson);
        param.put("access_token", (String)jsonObject1.get("access_token"));

        String result = ApiCallService.callApiTest(urlHead, urlPath, appSecret, param);
        System.out.println(result);
    }

}
