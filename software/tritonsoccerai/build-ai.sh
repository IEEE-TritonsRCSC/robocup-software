mvn clean install
mvn clean compile assembly:single
mvn exec:java
java -jar target/triton-soccer-ai-1.0-SNAPSHOT-jar-with-dependencies.jar --team=yellow