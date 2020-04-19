package com.demo.news.Utils;

import net.sf.json.JSONObject;
import org.apache.commons.codec.binary.Base64;

import java.nio.charset.StandardCharsets;
import java.security.PrivateKey;
import java.security.PublicKey;
import java.security.Signature;
import java.util.*;

public class SignUtil {
   private static final String SIGNATURE_ALGORITHM = "SHA256withRSA";

    /**
     * 签名
     */
    public static String sign(String plain_text, String priK) {
        try {
            Signature Sign = Signature.getInstance(SIGNATURE_ALGORITHM);
            PrivateKey privateKey = RSA.restorePrivateKey(Base64.decodeBase64(priK));
            Sign.initSign(privateKey);
            Sign.update(plain_text.getBytes(StandardCharsets.UTF_8));
            return Base64.encodeBase64URLSafeString(Sign.sign());
        } catch (Exception e) {
            e.printStackTrace();
        }
        return "";
    }

    /**
     * 验签
     */
    public static boolean verifySign(String plain_text, String signedBase64, String pubK) {
        boolean SignedSuccess = false;
        try {
            Signature verifySign = Signature.getInstance(SIGNATURE_ALGORITHM);
            PublicKey publicKey = RSA.restorePublicKey(Base64.decodeBase64(pubK));
            verifySign.initVerify(publicKey);
            verifySign.update(plain_text.getBytes(StandardCharsets.UTF_8));
            byte[] signed = Base64.decodeBase64(signedBase64);
            SignedSuccess = verifySign.verify(signed);
        } catch (Exception e) {
            e.printStackTrace();
        }
        return SignedSuccess;
    }

    /**
     * 将键值对字符串("key1=value1&key2=value2&...") 转换为 Map
     */
    public static Map<String, Object> keyValuePairsStr2Map(String parisStr) {
        Map<String, Object> map = new HashMap<>();
        try {
            if (!ValUtil.isEmpty(parisStr)) {
                String[] parisStrArr = parisStr.split("&");
                for (String item : parisStrArr) {
                    int index = item.indexOf("=");
                    String key = item.substring(0, index);
                    String value = item.substring(index + 1);
                    map.put(key, value);
                }
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return map;
    }

//    /**
//     * 将键值对字符串("key1=value1&key2=value2&...") 转换为 Json
//     */
//    public static String keyValuePairsStr2Json(String parisStr) {
//        StringBuilder sb = new StringBuilder("");
//        if (!StringUtils.isEmpty(parisStr)) {
//            sb.append("{");
//            String[] parisStrArr = parisStr.split("&");
//            for (String item : parisStrArr) {
//                int index = item.indexOf("=");
//                String key = item.substring(0, index);
//                String value = item.substring(index + 1);
//
//                sb.append("\"").append(key).append("\":");
//                if (value.startsWith("{") || value.startsWith("[")) {
//                    sb.append(value);
//                } else {
//                    sb.append("\"").append(value);
//                }
//                sb.append("\",");
//            }
//            sb.deleteCharAt(sb.length() - 1).append("}");
//        }
//        return sb.toString();
//    }

    /**
     * 将JSONObject 转换为 键值对字符串("key1=value1&key2=value2&...")，并按value值进行排序
     */
    public static String JsonObject2keyValuePairsStr(JSONObject jo) {
        StringBuilder sb = new StringBuilder("");
        if (jo != null) {
            //JSONObject jo = JSONObject.fromObject(o);
            SortedMap<Object, Object> map = new TreeMap<>();

            Iterator it = jo.keys();
            while (it.hasNext()) {
                Object key = it.next();
                Object value = jo.get(key);
                map.put(key, value);
            }

            for (Object key: map.keySet()){
                Object value = jo.get(key);
                sb.append(key).append("=");
                if (value instanceof JSONObject) {
                    sb.append(value.toString());
                } else {
                    sb.append(value);
                }
                sb.append("&");
            }
            if (sb.length() > 0) {
                sb.deleteCharAt(sb.length() - 1);
            }
        }
        return sb.toString();
    }

}
