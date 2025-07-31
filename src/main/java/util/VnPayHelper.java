/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package util;

    
import javax.crypto.Mac;
import javax.crypto.spec.SecretKeySpec;
import java.net.URLEncoder;
import java.util.*;
/**
 *
 * @author Diep Quoc Thang - CE182036
 */
public class VnPayHelper {

    public static String hmacSHA512(String key, String data) throws Exception {
        Mac hmac512 = Mac.getInstance("HmacSHA512");
        SecretKeySpec secretKey = new SecretKeySpec(key.getBytes("UTF-8"), "HmacSHA512");
        hmac512.init(secretKey);
        byte[] bytes = hmac512.doFinal(data.getBytes("UTF-8"));
        StringBuilder hash = new StringBuilder();
        for (byte b : bytes) {
            hash.append(String.format("%02x", b));
        }
        return hash.toString();
    }

    // ⚠️ Tạo chuỗi ký KHÔNG encode
    public static String buildHashData(Map<String, String> params) {
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        StringBuilder sb = new StringBuilder();

        for (String key : keys) {
            if ("vnp_SecureHash".equals(key)) continue;

            String value = params.get(key);
            if (value != null && value.length() > 0) {
                if (sb.length() > 0) sb.append('&');
                sb.append(key).append('=').append(value); // ❗ Không encode
            }
        }
        return sb.toString();
    }

    // ⚠️ Dùng để tạo query string (encode key & value)
    public static String buildQueryString(Map<String, String> params) throws Exception {
        List<String> keys = new ArrayList<>(params.keySet());
        Collections.sort(keys);
        StringBuilder query = new StringBuilder();
        for (String key : keys) {
            String value = params.get(key);
            if (value != null && value.length() > 0) {
                if (query.length() > 0) query.append('&');
                query.append(URLEncoder.encode(key, "UTF-8"));
                query.append('=');
                query.append(URLEncoder.encode(value, "UTF-8"));
            }
        }
        return query.toString();
    }
}
