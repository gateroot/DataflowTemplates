#!/bin/bash

gcloud dataflow jobs run udf-test \
    --project int-ca-nims-tdm-poc \
    --gcs-location gs://int-ca-nims-tdm-poc-dataflow/dataflow/templates/udf-test.json\
    --region asia-northeast1 \
    --parameters \
javascriptTextTransformFunctionName=transform,\
javascriptTextTransformGcsPath=gs://int-ca-nims-tdm-poc-dataflow/udf/udf.js,\
inputFilePattern=gs://int-ca-nims-tdm-poc-dataflow/source/\*.zip