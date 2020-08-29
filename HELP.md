0. To login to the VDI:
> ssh root@174.138.33.104

1. To start up the services:
> export DOCKER_HOST_IP=127.0.0.1
> docker-compose rm -svf
> docker-compose down
> docker-compose up --build

2. To execute MySQL client:
> docker-compose exec mysql sh -c 'mysql -umysqluser -P3307 -p"$MYSQL_ENV_MYSQL_ROOT_PASSWORD"'
> Enter mysql password

3. To register a new service (MySQL):
> curl -i -X POST -H "Accept:application/json" -H "Content-Type:application/json" localhost:8083/connectors/ -d '{ "name": "inventory-connector", "config": { "connector.class": "io.debezium.connector.mysql.MySqlConnector", "tasks.max": "1", "database.hostname": "mysql", "database.port": "3306", "database.user": "debezium", "database.password": "dbz", "database.server.id": "184054", "database.server.name": "dbserver1", "database.whitelist": "inventory", "database.history.kafka.bootstrap.servers": "kafka:9092", "database.history.kafka.topic": "dbhistory.inventory" } }'
> curl -H "Accept:application/json" localhost:8083/connectors/

or Postgresql:
 > curl -i -X POST -H "Accept:application/json" -H "Content-Type:application/json" localhost:8083/connectors/ -d '{ "name": "inventory-connector", "config": { "connector.class": "io.debezium.connector.postgresql.PostgresConnector", "tasks.max": "1", "database.hostname": "postgres", "database.port": "5433", "database.user": "debezium", "database.password": "dbz", "database.server.id": "184054", "database.server.name": "dbserver1", "database.whitelist": "inventory", "database.history.kafka.bootstrap.servers": "kafka:9092", "database.history.kafka.topic": "dbhistory.inventory" } }'
> curl -H "Accept:application/json" localhost:8083/connectors/
 
4. To consume events via commandline:
> kafka-console-consumer --bootstrap-server localhost:9092 --topic dbserver1.inventory.address \
--from-beginning \
--formatter kafka.tools.DefaultMessageFormatter \
-- property print.key=true \
-- property print.value=true \
-- property key.deserializer=org.apache.kafka.common.serializer.StringDeserializer \
-- property value.deserializer=org.apache.kafka.common.serializer.LongDeserializer \

5. To create new MySQL databases:
> docker run --rm -d --name mysql1 -p 3308:3306 -e MYSQL_ROOT_PASSWORD=alwaysbekind -e MYSQL_DATABASE=party mysql:5.7.22
> docker exec -it mysql bash -l
> mysqldump -uroot 

6. To create new Postgres databases:
> docker run --rm --name postgres1 -e POSTGRES_PASSWORD=alwaysbekind -e POSTGRES_DB=party POSTGRES_USER=postgres -d -p 5433:5432 postgres:12.4
> psql -h localhost -U postgres -d party
