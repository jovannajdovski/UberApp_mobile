package com.example.uberapp_tim12.security;

import android.util.Base64;
import android.util.Log;

import org.json.JSONException;
import org.json.JSONObject;

import java.io.UnsupportedEncodingException;
import java.util.Date;
import java.util.function.Function;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;

public class JwtUtil {
    private String SECRET_KEY = "secret";

    public String extractUsername(String token) {
        return extractClaim(token, Claims::getSubject);
    }

    public Integer extractId(String token) {
        String id = extractClaim(token, Claims::getId);
        return Integer.parseInt(id);
    }

    public String extractRole(String token) {
        return extractAllClaims(token).get("role",String.class);
    }

    public <T> T extractClaim(String token, Function<Claims, T> claimsResolver) {
        final Claims claims = extractAllClaims(token);
        return claimsResolver.apply(claims);
    }
    private Claims extractAllClaims(String token) {
        return Jwts.parserBuilder().setSigningKey(SECRET_KEY).build().parseClaimsJws(token).getBody();
    }

    public String decoded(String JWTEncoded) {
        try {
            String[] split = JWTEncoded.split("\\.");
            Log.d("JWT_DECODED", "Header: " + getJson(split[0]));
            Log.d("JWT_DECODED", "Body: " + getJson(split[1]));
            return getJson(split[1]);
        } catch (UnsupportedEncodingException e) {
            //Error
        }
        return "";
    }

    private static String getJson(String strEncoded) throws UnsupportedEncodingException{
        byte[] decodedBytes = Base64.decode(strEncoded, Base64.URL_SAFE);
        return new String(decodedBytes, "UTF-8");
    }

    public String getUsername(String token) {
        String body = decoded(token);
        JSONObject object = null;
        try {
            object = new JSONObject(body);
            return object.getString("sub");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

    public Integer getId(String token) {
        String body = decoded(token);
        JSONObject object = null;
        try {
            object = new JSONObject(body);
            return object.getInt("jti");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return 0;
    }

    public String getRole(String token) {
        String body = decoded(token);
        JSONObject object = null;
        try {
            object = new JSONObject(body);
            return object.getString("role");
        } catch (JSONException e) {
            e.printStackTrace();
        }
        return "";
    }

}
