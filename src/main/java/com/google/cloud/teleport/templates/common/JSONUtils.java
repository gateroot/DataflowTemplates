package com.google.cloud.teleport.templates.common;

import java.util.List;
import org.json.JSONArray;

public class JSONUtils {
  public static String ArrayStringToJSON(List<String> strings) {
    JSONArray array = new JSONArray(strings);
    return array.toString();
  }
  public static String ArrayStringToJSON(String[] strings) {
    JSONArray array = new JSONArray(strings);
    return array.toString();
  }
}
