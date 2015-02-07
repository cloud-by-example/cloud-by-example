FROM cloudbyexample/cbe:apache

MAINTAINER Cloud By Example

RUN yum -y install openssh-server; yum clean all
RUN echo "root:password" > passwd_file;chpasswd < passwd_file;rm passwd_file

EXPOSE 22

ENTRYPOINT service sshd start && /usr/sbin/httpd -DFOREGROUND
