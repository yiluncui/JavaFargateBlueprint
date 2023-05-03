This blueprint creates a containerized web service project. The project uses [AWS Copilot CLI](https://aws.amazon.com/containers/copilot/) to build and deploy a containerized [Spring Boot](https://spring.io/projects/spring-boot) Java web service backed by [Amazon DynamoDB](https://aws.amazon.com/dynamodb/) on [Amazon Elastic Container Service (Amazon ECS)](https://aws.amazon.com/pm/ecs/).
The project deploys a containerized app to an Amazon ECS cluster on AWS Fargate serverless compute. The app stores data in a DynamoDB table. After your workflow runs successfully, the sample web service is publicly available through the Application Load Balancer.

## Usage

```
mvn -Dmaven.test.skip=true package
java -jar -Dspring.profiles.active=prod target/CustomerService-0.0.1.jar --server.port=8080
curl localhost:8080/api/health
```

Using the API requires a connection to DynamoDB.
You can use [DynamoDB Local](https://docs.aws.amazon.com/amazondynamodb/latest/developerguide/DynamoDBLocal.html) for local development (see "Running Tests" more for details).

## Running Locally with Docker

Build the application using the commands mentioned above, then build and run a container:

```
docker build -t test .
docker run -p 8080:8080 test
curl localhost:8080/api/health
```

## Running Tests

The project contains integration tests that require a DynamoDB connection. You can use **DynamoDB Local** to run the tests against a local database.

```
# Set up the volume for DynamoDB Local
mkdir docker
mkdir docker/dynamodb
sudo chmod 777 docker/dynamodb

# Run the database
docker run --name dynamodb -d -p 8000:8000 \
-w /home/dynamodblocal \
-v $(pwd)/docker/dynamodb:/home/dynamodblocal/data \
amazon/dynamodb-local:latest -jar DynamoDBLocal.jar -sharedDb -dbPath ./data

# Set fake credentials
export AWS_ACCESS_KEY_ID=123
export AWS_SECRET_ACCESS_KEY=123
export AWS_SESSION_TOKEN=123

# Run tests
mvn test

# Cleanup
docker stop dynamodb
docker rm dynamodb
```

## API Usage Examples

Adding a customer:

```
POST [host]/api/customers HTTP/1.1
Content-Type: application/json

{"id": "123", "name":"bob", "email":"bob@emailservices.com", "accountNumber":"57369235"}
```

Getting a list of customers:

```
GET [host]/api/customers HTTP/1.1
```

Getting a customer by ID:

```
GET [host]/api/customers/[id] HTTP/1.1
```

Info about the API is found at this location: `src/main/java/com/amazon/customerService/controller/CustomerController.java`

## Deploying and using the app

Upon project creation, the Deploy workflow will run automatically. Upon completion, the REST API will be publicly available through the Application Load Balancer.

You can use AWS Copilot CLI to see status of your services, logs, and more. [Install AWS Copilot CLI](https://aws.github.io/copilot-cli/docs/overview/) in your local environment and [configure it](https://aws.github.io/copilot-cli/docs/credentials/) to access the same account, that you deployed your CodeCatalyst application to. Run `copilot svc ls` from your `app` repository to see a list of the deployed services. You can use `copilot svc logs` or `copilot svc status` to see logs or status of your services. You can learn more about AWS Copilot commands [here](https://aws.github.io/copilot-cli/docs/overview/).

