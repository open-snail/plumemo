FROM openjdk:8-jdk-alpine
MAINTAINER byteblogs@aliyun.com

ADD . .

RUN pwd
RUN ls -l

ENV MAVEN_VERSION=3.6.3

# install maven ${MAVEN_VERSION}
RUN mkdir -p /usr/local/maven
WORKDIR /usr/local/maven
RUN wget http://mirrors.tuna.tsinghua.edu.cn/apache/maven/maven-3/${MAVEN_VERSION}/binaries/apache-maven-${MAVEN_VERSION}-bin.tar.gz

RUN tar -zxvf apache-maven-${MAVEN_VERSION}-bin.tar.gz && \
    rm -fr apache-maven-${MAVEN_VERSION}-bin.tar.gz

#对外暴漏的端口号
EXPOSE 8086

WORKDIR /

RUN /usr/local/maven/apache-maven-${MAVEN_VERSION}/bin/mvn clean package

#将本地项目jar包拷贝到Docker容器中的位置
RUN cp ./target/helloblog-v1.1.0.jar ./

RUN rm -rf target
RUN rm -rf repository
RUN rm -rf pom.xml
RUN rm -rf Dockerfile

#加入时区
RUN apk add --no-cache tzdata
RUN /bin/cp /usr/share/zoneinfo/Asia/Shanghai /etc/localtime && echo 'Asia/Shanghai' >/etc/timezone

#开机启动
ENTRYPOINT ["java","-jar","/helloblog-v1.1.0.jar"]