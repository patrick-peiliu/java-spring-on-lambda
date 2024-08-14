package com.product.api.service;

import com.product.api.util.CommonUtil;
import java.io.IOException;
import java.util.Map;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.methods.HttpPost;
import org.apache.http.entity.StringEntity;
import org.apache.http.impl.client.CloseableHttpClient;
import org.apache.http.impl.client.HttpClients;
import org.apache.http.util.EntityUtils;

/**
 * api调用的服务类
 */
public class ApiCallService {

    /**
     * 调用api测试
     * @param urlHead 请求的url到openapi的部分，如http://gw.open.1688.com/openapi/
     * @param urlPath protocol/version/namespace/name/appKey
     * @param appSecretKey 测试的app密钥，如果为空表示不需要签名
     * @param params api请求参数map。如果api需要用户授权访问，那么必须完成授权流程，params中必须包含access_token参数
     * @return json格式的调用结果
     */
    public static String callApiTest(String urlHead, String urlPath, String appSecretKey, Map<String, String> params) {
        try (CloseableHttpClient httpClient = HttpClients.createDefault()) {
            HttpPost post = new HttpPost(urlHead + urlPath);
            post.setHeader("Content-type", "application/x-www-form-urlencoded; charset=UTF-8");

            StringBuilder paramString = new StringBuilder();
            if (params != null) {
                for (Map.Entry<String, String> entry : params.entrySet()) {
                    if (paramString.length() != 0) {
                        paramString.append("&");
                    }
                    paramString.append(entry.getKey()).append("=").append(entry.getValue());
                }
            }
            if (appSecretKey != null) {
                if (paramString.length() != 0) {
                    paramString.append("&");
                }
                paramString.append("_aop_signature=").append(CommonUtil.signatureWithParamsAndUrlPath(urlPath, params, appSecretKey));
            }

            post.setEntity(new StringEntity(paramString.toString()));
            HttpResponse response = httpClient.execute(post);

            int status = response.getStatusLine().getStatusCode();
            if (status >= 300 || status < 200) {
                throw new RuntimeException("invoke api failed, urlPath:" + urlPath
                        + " status:" + status + " response:" + EntityUtils.toString(response.getEntity()));
            }
            return CommonUtil.parserResponse(response);
        } catch (ClientProtocolException e) {
            System.out.println(e.getMessage());
        } catch (IOException e) {
            System.out.println(e.getMessage());
        }
        return "";
    }
}