function transform(json) {
  var logger = Packages.org.slf4j.LoggerFactory.getLogger('javascript-udf');
  var data = JSON.parse(json);
  var FileWriter=Java.type("java.io.FileWriter");
  var zipFile = data.dir + data.name;
  logger.info('this is logger test');

  var fw = new FileWriter(zipFile);
  fw.write(data.data);
  fw.close();  // forgetting to close it results in a truncated file
}