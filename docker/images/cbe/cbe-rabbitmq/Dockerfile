FROM ubuntu
MAINTAINER Cloud by Example

RUN apt-get update && apt-get install -y wget
RUN echo "deb http://www.rabbitmq.com/debian/ testing main" >> /etc/apt/sources.list
RUN wget http://www.rabbitmq.com/rabbitmq-signing-key-public.asc;apt-key add rabbitmq-signing-key-public.asc
RUN apt-get install -y rabbitmq-server

EXPOSE 5672

ENTRYPOINT /usr/lib/rabbitmq/bin/rabbitmq-server
