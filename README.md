
To run application in container:
 
1.Create jar and build Dockerfile image
```
docker build -t ms-resource .
```
2.Create network for localstack and rabbitMQ
```
docker network create localstack_network
docker network create rabbit_network
```
3.Run container using docker compose
```
docker-compose up
```