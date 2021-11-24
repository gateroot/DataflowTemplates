/*
 * Copyright (C) 2018 Google LLC
 *
 * Licensed under the Apache License, Version 2.0 (the "License"); you may not
 * use this file except in compliance with the License. You may obtain a copy of
 * the License at
 *
 *   http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS, WITHOUT
 * WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied. See the
 * License for the specific language governing permissions and limitations under
 * the License.
 */
package com.google.cloud.teleport.templates;

import com.google.cloud.teleport.templates.common.JavascriptTextTransformer.JavascriptTextTransformerOptions;
import com.google.cloud.teleport.templates.common.JavascriptTextTransformer.TransformTextViaJavascript;
import org.apache.beam.runners.dataflow.options.DataflowPipelineOptions;
import org.apache.beam.sdk.Pipeline;
import org.apache.beam.sdk.coders.StringUtf8Coder;
import org.apache.beam.sdk.io.Compression;
import org.apache.beam.sdk.io.FileIO;
import org.apache.beam.sdk.io.FileIO.Write.FileNaming;
import org.apache.beam.sdk.io.TextIO;
import org.apache.beam.sdk.options.Description;
import org.apache.beam.sdk.options.PipelineOptionsFactory;
import org.apache.beam.sdk.options.Validation;
import org.apache.beam.sdk.options.ValueProvider;
import org.apache.beam.sdk.transforms.Contextful;
import org.apache.beam.sdk.transforms.DoFn;
import org.apache.beam.sdk.transforms.SimpleFunction;
import org.apache.beam.sdk.transforms.windowing.BoundedWindow;
import org.apache.beam.sdk.transforms.windowing.PaneInfo;
import org.apache.beam.sdk.values.KV;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Templated pipeline to read text from TextIO, apply a javascript UDF to it, and write it to GCS.
 */
public class CustomTemplateUdfTest {

  /**
   * Options supported by {@link CustomTemplateUdfTest}.
   */
  public interface Options extends DataflowPipelineOptions, JavascriptTextTransformerOptions {

    @Description("The GCS location of the text you'd like to process")
    ValueProvider<String> getInputFilePattern();

    void setInputFilePattern(ValueProvider<String> value);

    @Description("GCS path to javascript fn for transforming output")
    ValueProvider<String> getJavascriptTextTransformGcsPath();

    void setJavascriptTextTransformGcsPath(ValueProvider<String> jsTransformPath);

    @Validation.Required
    @Description("UDF Javascript Function Name")
    ValueProvider<String> getJavascriptTextTransformFunctionName();

    void setJavascriptTextTransformFunctionName(
        ValueProvider<String> javascriptTextTransformFunctionName);
  }

  private static final Logger LOG = LoggerFactory.getLogger(CustomTemplateUdfTest.class);

  private static final String BIGQUERY_SCHEMA = "BigQuery Schema";
  private static final String NAME = "name";
  private static final String TYPE = "type";
  private static final String MODE = "mode";

  static class WriteFile extends DoFn<String, byte[]> {

    @ProcessElement
    public void processElement(ProcessContext context) throws Exception {
    }
  }

  public static void main(String[] args) {
    Options options = PipelineOptionsFactory.fromArgs(args).withValidation().as(Options.class);
    Pipeline pipeline = Pipeline.create(options);

    pipeline
        .apply(FileIO.match().filepattern(options.getInputFilePattern()))
        .apply(FileIO.readMatches().withCompression(Compression.UNCOMPRESSED))
        .apply(
            TransformTextViaJavascript.newBuilder()
                .setFileSystemPath(options.getJavascriptTextTransformGcsPath())
                .setFunctionName(options.getJavascriptTextTransformFunctionName())
                .build())
        .apply(
            FileIO.<String, KV<String, String>>writeDynamic()
                .withNumShards(1)
                .by(KV::getKey)
                .via(Contextful.fn(KV::getValue), TextIO.sink())
                .withDestinationCoder(StringUtf8Coder.of())
                .withNaming(new SimpleFunction<String, FileNaming>() {
                  @Override
                  public FileNaming apply(String destination) {
                    return FileIO.Write.relativeFileNaming(
                        ValueProvider.StaticValueProvider.of(""), new FileNaming() {
                          @Override
                          public String getFilename(BoundedWindow window, PaneInfo pane,
                              int numShards, int shardIndex, Compression compression) {
                            return destination;
                          }
                        });
                  }
                })
                .to("gs://int-ca-nims-tdm-poc-dataflow/output/")
        );

    pipeline.run();
  }
}
