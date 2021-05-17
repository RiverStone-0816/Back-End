cd /root/api-server;
git reset --hard HEAD;
git pull;
export "MAVEN_OPTS=-Xmx2048m -Xms1024m -XX:MaxPermSize=512m -Djava.awt.headless=true";
mvn package;
yes | cp -f /root/api-server/target/ippbx.api-*.jar /usr/local/server/;
service premium-api-server stop;
service premium-api-server start;
tail -f /usr/local/server/logs/_`date +%Y-%m-%d`*.log;
