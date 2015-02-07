FROM cloudbyexample/cbe:tomcat-7.0.57

MAINTAINER Cloud by Example

# setup webapp
RUN cd /usr/local/tomcat/webapps; \
    wget http://cloudbyexample.org/repo/cbe/0.10/contact-single-tier-webapp/contact.war; \
    chmod 755 /usr/local/tomcat/webapps/contact.war; \
    mkdir /usr/local/tomcat/webapps/contact; \
    cd /usr/local/tomcat/webapps/contact; \
    unzip /usr/local/tomcat/webapps/contact.war; \
    chmod -R 755 /usr/local/tomcat/webapps/contact
ADD ./contact-app.properties .
ADD ./setenv.sh .

RUN cp contact-app.properties /usr/local/tomcat
RUN cp setenv.sh /usr/local/tomcat/bin
