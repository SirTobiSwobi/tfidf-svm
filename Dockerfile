FROM java
ENV version=1.1.2
MAINTAINER Tobias Eljasik-Swoboda ${version}
EXPOSE 8080/tcp
EXPOSE 8081/tcp
ADD ./target/tfidfsvm-${version}-SNAPSHOT.jar /opt/tfidfsvm/target/tfidfsvm-${version}-SNAPSHOT.jar
ADD ./tfidfsvm.yml /opt/tfidfsvm/target/tfidfsvm.yml
RUN java -jar /opt/tfidfsvm/target/tfidfsvm-${version}-SNAPSHOT.jar server /opt/tfidfsvm/target/tfidfsvm.yml