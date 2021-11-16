#!/bin/bash

mvn compile exec:java \
-Dexec.mainClass=com.google.cloud.teleport.templates.CustomTemplateUdfTest \
-Dexec.cleanupDaemonThreads=false \
-Dexec.args=" \
--project=sekine-test \
--stagingLocation=gs://nims-tdm-dataflow-test/staging \
--tempLocation=gs://nims-tdm-dataflow-test/temp \
--templateLocation=gs://nims-tdm-dataflow-test/templates/udf-test.json \
--runner=DataflowRunner \
--region=asia-northeast1"