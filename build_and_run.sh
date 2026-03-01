#!/bin/bash

NETWORK_NAME="mynet"
SUBNET="172.25.0.0/16"


#Check whether the network already exists
if ! docker network inspect $NETWORK_NAME >/dev/null 2>/dev/null; then
  echo "Creating Docker network $NETWORK_NAME with subnet $SUBNET..."
  docker network create --subnet=$SUBNET $NETWORK_NAME
else
  echo "Docker network $NETWORK_NAME already exists."
fi

#Build and run containers
docker compose down -v
docker compose up --build