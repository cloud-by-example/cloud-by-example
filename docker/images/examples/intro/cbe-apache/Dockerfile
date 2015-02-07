FROM centos:centos6

MAINTAINER Cloud by Example

RUN yum update -y; yum clean all
RUN yum -y install httpd; yum clean all
RUN mkdir -p /var/www/html

EXPOSE 80

USER root

ENTRYPOINT /usr/sbin/httpd -DFOREGROUND
