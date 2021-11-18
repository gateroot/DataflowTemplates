package com.google.cloud.teleport.templates.common;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.ArrayList;
import java.util.List;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public class FileUtils {
  private static final Logger LOG = LoggerFactory.getLogger(FileUtils.class);

  public static List<String> getXmlFiles(Path start) {
    XmlFileVisitor visitor = new XmlFileVisitor();
    try {
      Files.walkFileTree(start, visitor);
    } catch (IOException e) {
      LOG.info(e.toString());
    }
    List<String> result = new ArrayList<>();
    for (Path p : visitor.paths) {
      result.add(p.toString());
    }
    return result;
  }
}
