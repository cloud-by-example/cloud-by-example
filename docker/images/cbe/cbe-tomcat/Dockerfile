FROM centos:centos7

MAINTAINER Cloud by Example

RUN yum -y install wget tar unzip java; yum clean all
RUN cd /usr/local; \
	wget http://apache.claz.org/tomcat/tomcat-7/v7.0.57/bin/apache-tomcat-7.0.57.tar.gz; \
	tar xvf apache-tomcat-7.0.57.tar.gz; \
	rm -f xvf apache-tomcat-7.0.57.tar.gz
RUN ln -s /usr/local/apache-tomcat-7.0.57 /usr/local/tomcat

EXPOSE 8080
USER root

ENTRYPOINT /usr/local/tomcat/bin/catalina.sh run
