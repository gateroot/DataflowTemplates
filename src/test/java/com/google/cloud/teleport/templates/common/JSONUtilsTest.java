package com.google.cloud.teleport.templates.common;

import static org.junit.jupiter.api.Assertions.*;

import java.util.ArrayList;
import java.util.List;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;

class JSONUtilsTest {
  @Test
  void ArrayStringToJSON() {
    List<String> input = new ArrayList<>();
    input.add("A");
    input.add("B");
    input.add("C");
    String result = JSONUtils.ArrayStringToJSON(input);
    assertEquals("[\"A\",\"B\",\"C\"]", result);
  }

  @Test
  void ArrayStringToJSON2() {
    String[] input = {"A", "B", "C"};
    String result = JSONUtils.ArrayStringToJSON(input);
    assertEquals("[\"A\",\"B\",\"C\"]", result);
  }
}