rm -rf deployment

mkdir deployment

mvn clean install
cp target/datasearcher-0.0.1-SNAPSHOT.jar deployment/
cp src/main/resources/application.properties deployment/application.properties
cp src/main/resources/*.json deployment/
cp run.sh deployment/run.sh

chmod +x deployment/run.sh

