# brogrammers-csv-plugin

![alt text](https://am22.akamaized.net/tms/cnt/uploads/2014/12/94db027dfe61ac0d4d2b222b41806159512d8f6ce8d54dc090c20827d1c55b39.jpg)



**About:**
- CSV reader-writer plugin with performance and good engineering practices in mind.

**Dependencies Used:**
- JUnit

**Build Tool:**
- Maven

**Build Plugins Used:**
- Maven surefire (for unit tests)
- Maven failsafe (for integration tests)
- Jacoco (for line and branch coverage on unit tests)
- Pitest (for mutation testing on unit tests)

**Unit Tests:**
- <code>mvn test</code>
- To see jacoco output go to: target/jacoco-ut/index.html

**Integration Tests:**
- <code>mvn integration-test</code>

**Mutation Testing:**
- <code>mvn test org.pitest:pitest-maven:mutationCoverage</code>
- To see pitest output go to: target/pit-reports/index.html

**Apply Coverage Rules (for Jacoco & Pitest):**
- <code>mvn verify</code>

**Package Application:**
- <code>mvn clean package</code>