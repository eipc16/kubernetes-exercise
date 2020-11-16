FROM mariadb:latest

ADD db_scripts/ /docker-entrypoint-initdb.d

ENV MYSQL_ROOT_PASSWORD test123
ENV MYSQL_DATABASE cinema_tickets_app_db
ENV MYSQL_USER test
ENV MYSQL_PASSWORD test123

EXPOSE 3306

CMD ["mysqld"]
