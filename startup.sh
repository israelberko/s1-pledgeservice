docker-compose rm -svf
docker-compose down
docker-compose up --build
curl -i -X POST -H "Accept:application/json" -H "Content-Type:application/json" localhost:8083/connectors/ --data-binary "@sql/postgres-connect-config.json"