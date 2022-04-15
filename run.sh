# clear target to rebuild and execute build steps

echo "rebuild project"
mvn clean install

echo "run project"
cd integration-tests
quarkus dev
