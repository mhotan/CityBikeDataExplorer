sudo service neo4j-service stop
sudo rm -rf /var/lib/neo4j/data/graph.db/
sudo cp -a target/citibike.db /var/lib/neo4j/data/graph.db
sudo chown -R neo4j /var/lib/neo4j/data/graph.db/
sudo service neo4j-service start
