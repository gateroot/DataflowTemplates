#!/bin/bash

gcloud dataflow jobs run udf-test \
    --gcs-location gs://nims-tdm-dataflow-test/templates/udf-test.json \
    --region asia-northeast1 \
    --parameters \
javascriptTextTransformFunctionName=transform,\
javascriptTextTransformGcsPath=gs://nims-tdm-dataflow-test/udf.js,\
inputFilePattern=gs://nims-tdm-dataflow-test/data/\*.xml