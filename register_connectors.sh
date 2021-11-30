curl -X DELETE  http://localhost:8083/connectors/pledge-connector
curl -X DELETE  http://localhost:8083/connectors/pledge-outbox-connector
curl -X DELETE http://localhost:8083/connectors/donation-outbox-connector
curl -i -X POST -H "Accept:application/json" -H "Content-Type:application/json" localhost:8083/connectors/ --data-binary "@rest-requests/postgres-connect-config-pledge.json"
curl -i -X POST -H "Accept:application/json" -H "Content-Type:application/json" localhost:8083/connectors/ --data-binary "@rest-requests/postgres-connect-config-pledge-outbox.json"
#curl -i -X POST -H "Accept:application/json" -H "Content-Type:application/json" localhost:8083/connectors/ --data-binary "@../s1-donorservice/rest-requests/postgres-connect-config-pledge.json"
