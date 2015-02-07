FROM ubuntu
MAINTAINER Cloud by Example
RUN apt-key adv --keyserver keyserver.ubuntu.com --recv-keys B97B0AFCAA1A47F044F244A07FCC7D46ACCC4CF8

RUN echo "deb http://apt.postgresql.org/pub/repos/apt/ precise-pgdg main" > /etc/apt/sources.list.d/pgdg.list
RUN locale-gen --no-purge en_US.UTF-8 
ENV LC_ALL en_US.UTF-8 
RUN update-locale LANG=en_US.UTF-8

RUN apt-get update && apt-get install -y python-software-properties software-properties-common \
	postgresql-9.3 postgresql-client-9.3 postgresql-contrib-9.3

USER postgres

RUN echo "host all  all    0.0.0.0/0  md5" >> /etc/postgresql/9.3/main/pg_hba.conf
RUN echo "listen_addresses='*'" >> /etc/postgresql/9.3/main/postgresql.conf

EXPOSE 5432

CMD ["/usr/lib/postgresql/9.3/bin/postgres", "-D", "/var/lib/postgresql/9.3/main", "-c", "config_file=/etc/postgresql/9.3/main/postgresql.conf"]
