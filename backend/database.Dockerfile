FROM mysql
COPY database/initialization.sql /docker-entrypoint-initdb.d/
EXPOSE 3306