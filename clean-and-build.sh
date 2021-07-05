#!/bin/bash

rm -rf deployment

mkdir deployment
mkdir deployment/config

mvn clean install
cp target/datasearcher-0.0.1-SNAPSHOT.jar deployment/
cp src/main/resources/*.* deployment/config/

cp run.sh deployment/run.sh
chmod +x deployment/run.sh