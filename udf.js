function transform(zipFilePathStr) {
  var logger = Packages.org.slf4j.LoggerFactory.getLogger('javascript-udf');
  var Files = Java.type("java.nio.file.Files");
  var Paths = Java.type("java.nio.file.Paths");
  var File = Java.type("java.io.File");

  logger.info("zipFilePathStr: " + zipFilePathStr);

  var zipFilePath = Paths.get(zipFilePathStr);
  var tmpFolderPath = zipFilePath.getParent();

  var ZipUnCompressUtils = Java.type("com.google.cloud.teleport.templates.common.ZipUnCompressUtils")
  var result = ZipUnCompressUtils.unzip(zipFilePathStr, tmpFolderPath.toString());
  logger.info("result: " + result);

  var tmpFile = new File(tmpFolderPath.toString());
  var JSONUtils = Java.type("com.google.cloud.teleport.templates.common.JSONUtils");
  logger.info("list: " + JSONUtils.ArrayStringToJSON(tmpFile.list()));
  var FileUtils = Java.type("com.google.cloud.teleport.templates.common.FileUtils");
  var xmlFiles = FileUtils.getXmlFiles(tmpFolderPath);
  var xml = JSONUtils.ArrayStringToJSON(xmlFiles);
  logger.info("xml: " + xml);
  return xml;
}