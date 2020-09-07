export DOCKER_HOST_IP=127.0.0.1
docker-compose rm -svf
docker-compose down
docker-compose up --build