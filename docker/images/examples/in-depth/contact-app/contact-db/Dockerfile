FROM cloudbyexample/cbe:postgresql-9.3

MAINTAINER Cloud by Example

# add SQL scripts
ADD ./schema.sql .
ADD ./security_schema.sql .

USER postgres

RUN    /etc/init.d/postgresql start &&\
    psql --command "CREATE USER contact WITH SUPERUSER PASSWORD 'contact';" &&\
    createdb -O contact contact

RUN    /etc/init.d/postgresql start &&\
    psql -U postgres -d contact -f schema.sql  &&\
    psql -U postgres -d contact -f security_schema.sql