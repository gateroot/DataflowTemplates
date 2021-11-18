function transform(file) {
  var logger = Packages.org.slf4j.LoggerFactory.getLogger('javascript-udf');
  var Files = Java.type("java.nio.file.Files");
  var Paths = Java.type("java.nio.file.Paths");
  var File = Java.type("java.io.File");

  logger.info("file: " + file);

  var path = Paths.get(file);
  var tmp = path.getParent();

  var ZipUnCompressUtils = Java.type(
      "com.google.cloud.teleport.templates.common.ZipUnCompressUtils")
  var result = ZipUnCompressUtils.unzip(path.toString(), tmp.toString());
  logger.info("result: " + result);

  var tmpFile = new File(tmp.toString() + "/test");
  var JSONUtils = Java.type(
      "com.google.cloud.teleport.templates.common.JSONUtils");
  logger.info("list: " + JSONUtils.ArrayStringToJSON(tmpFile.list()));
  var FileUtils = Java.type(
      "com.google.cloud.teleport.templates.common.FileUtils");
  var xmlFiles = FileUtils.getXmlFiles(tmp);
  var xml = JSONUtils.ArrayStringToJSON(xmlFiles);
  logger.info("xml: " + xml);
  return xml;
}