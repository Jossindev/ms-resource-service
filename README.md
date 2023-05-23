
*To run application in 3 containers separately:
 
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

*To run application in one container:
```
docker-compose -f docker-compose-full.yml up
```