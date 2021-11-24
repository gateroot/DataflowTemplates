#!/bin/bash

mvn compile exec:java \
-Dexec.mainClass=com.google.cloud.teleport.templates.CustomTemplateUdfTest \
-Dexec.cleanupDaemonThreads=false \
-Dexec.args=" \
--project=int-ca-nims-tdm-poc \
--stagingLocation=gs://int-ca-nims-tdm-poc-dataflow/dataflow/staging \
--tempLocation=gs://int-ca-nims-tdm-poc-dataflow/dataflow/temp \
--templateLocation=gs://int-ca-nims-tdm-poc-dataflow/dataflow/templates/udf-test.json \
--runner=DataflowRunner \
--region=asia-northeast1"