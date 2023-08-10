# Getting started

https://docs.aws.amazon.com/cdk/v2/guide/getting_started.html

* Create an AWS account
* Create a role for deployment and generate keys
* Install AWS CLI https://aws.amazon.com/cli/
* Create .aws/config .aws/credentials
* Install node: https://nodejs.org/en
* Install AWS CDK `npm install -g aws-cdk`
* Turn on developer mode on your Windows (to allow for symlinks) `start ms-settings:developers`
* Boostrap AWS CDK `cdk bootstrap aws://ACCOUNT-NUMBER/REGION`
* Install Maven https://maven.apache.org/download.cgi
* Create an empty repo in git
* Clone the empty repo: `cd C:\Users\andreas\IdeaProjects` `git clone https://github.com/andreas-jonasson/InfrastructureAsCodeDemo.git`
* Create a CDK project: `cd InfrastructureAsCodeDemo\` `cdk init app --language java`
* Open the project in your favorite IDE
* Change to java 11 in the pom.xml: `<source>11</source><target>11</target>`
* Test that `cdk synth` generates a yaml-file

You are now ready for: https://docs.aws.amazon.com/cdk/api/v2/
