# Central Audit Archiver Serverless

## Build

It is required to build prior to deploying. You can build the deployment artifact using Gradle or Maven.

### Gradle

In order to build using Gradle simply run

```bash
./gradlew build # to build the application jar
```

The expected result should be similar to:

```bash
Starting a Gradle Daemon, 1 incompatible Daemon could not be reused, use --status for details
:compileJava
:processResources
:classes
:jar
:assemble
:buildZip
:compileTestJava UP-TO-DATE
:processTestResources UP-TO-DATE
:testClasses UP-TO-DATE
:test UP-TO-DATE
:check UP-TO-DATE
:build

BUILD SUCCESSFUL

Total time: 8.195 secs
```

## Deploy

After having built the deployment artifact using Gradle or Maven as described above you can deploy by simply running

```bash
serverless deploy
```

The expected result should be similar to:

```bash
Serverless: Creating Stack...
Serverless: Checking Stack create progress...
.....
Serverless: Stack create finished...
Serverless: Uploading CloudFormation file to S3...
Serverless: Uploading service .zip file to S3...
Serverless: Updating Stack...
Serverless: Checking Stack update progress...
..............................
Serverless: Stack update finished...
Service Information

```

## Scaling

By default, AWS Lambda limits the total concurrent executions across all functions within a given region to 100. The default limit is a safety limit that protects you from costs due to potential runaway or recursive functions during initial development and testing. To increase this limit above the default, follow the steps in [To request a limit increase for concurrent executions](http://docs.aws.amazon.com/lambda/latest/dg/concurrent-executions.html#increase-concurrent-executions-limit).
