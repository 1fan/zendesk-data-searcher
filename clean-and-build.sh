#!/bin/bash

rm -rf deployment

mkdir deployment
mkdir deployment/config

mvn clean install
cp target/datasearcher-0.0.1-SNAPSHOT.jar deployment/
cp src/main/resources/application.properties deployment/config/application.properties
cp src/main/resources/*.json deployment/config/

cp run.sh deployment/run.sh
chmod +x deployment/run.sh