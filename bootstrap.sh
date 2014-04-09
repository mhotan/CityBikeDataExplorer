#!/bin/sh

# Add repositories and signing keys
echo "deb http://ppa.launchpad.net/webupd8team/java/ubuntu precise main" \
 > /etc/apt/sources.list.d/java.list
echo "deb http://apt.postgresql.org/pub/repos/apt/ precise-pgdg main" \
 > /etc/apt/sources.list.d/postgresql.list
echo "deb http://debian.neo4j.org/repo stable/" \
 > /etc/apt/sources.list.d/neo4j.list

apt-key adv --keyserver keyserver.ubuntu.com --recv-keys EEA14886
wget --quiet -O - https://www.postgresql.org/media/keys/ACCC4CF8.asc | apt-key add -
wget --quiet -O - http://debian.neo4j.org/neotechnology.gpg.key | apt-key add -

# Update sources
apt-get update

# Automatically accept the Oracle License Agreement
echo oracle-java8-installer shared/accepted-oracle-license-v1-1 select true | debconf-set-selections

# Install things
apt-get install -y oracle-java8-installer oracle-java8-set-default \
 postgresql-9.3-postgis postgresql-contrib neo4j

# Add a PostgreSQL superuser
su - postgres -c "createuser -drs vagrant"
su - postgres -c "echo \"ALTER ROLE vagrant ENCRYPTED PASSWORD 'vagrant';\" | psql"

# Allow access to databases from outside
echo "org.neo4j.server.webserver.address=0.0.0.0" >> /etc/neo4j/neo4j-server.properties
echo "listen_addresses = '*'" >> /etc/postgresql/9.3/main/postgresql.conf
echo "host all all 10.0.0.0/8 md5" >> /etc/postgresql/9.3/main/pg_hba.conf

service neo4j-service restart
service postgresql restart
