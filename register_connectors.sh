curl -X DELETE http://localhost:8083/connectors/
curl -i -X POST -H "Accept:application/json" -H "Content-Type:application/json" localhost:8083/connectors/ --data-binary "@sql/postgres-connect-config.json"
curl -i -X POST -H "Accept:application/json" -H "Content-Type:application/json" localhost:8083/connectors/ --data-binary "@../s1-donorservice/sql/postgres-connect-config.json"
