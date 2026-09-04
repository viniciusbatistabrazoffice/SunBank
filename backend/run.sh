#!/usr/bin/env bash
set -e

export PGPASSWORD=admin123
DB_NAME=sunbank

if ! psql -h localhost -U postgres -d postgres -tc "SELECT 1 FROM pg_database WHERE datname = '$DB_NAME'" | grep -q 1; then
    psql -h localhost -U postgres -d postgres -c "CREATE DATABASE \"$DB_NAME\";"
fi

./mvnw spring-boot:run
