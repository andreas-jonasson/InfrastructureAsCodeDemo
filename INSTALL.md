# Getting started

https://docs.aws.amazon.com/cdk/v2/guide/getting_started.html



* Create an AWS account
* Create a role for deployment and generate keys
* Install AWS CLI https://aws.amazon.com/cli/
* Create .aws/config .aws/credentials
* Install node: https://nodejs.org/en
* Install AWS CDK `npm install -g aws-cdk` `npm install aws-cdk-lib`
* Turn on developer mode on your Windows (to allow for symlinks) `start ms-settings:developers`
* Boostrap AWS CDK `cdk bootstrap aws://ACCOUNT-NUMBER/REGION`
* Install Maven https://maven.apache.org/download.cgi
* Create an empty project in git
* Clone the empty repo: `cd C:\Users\andreas\IdeaProjects` `git clone https://github.com/andreas-jonasson/InfrastructureAsCodeDemo.git`
* Create a CDK project: `cd InfrastructureAsCodeDemo\` `cdk init app --language java`
* Change to java 11 in the pom.xml: `<source>11</source><target>11</target>`