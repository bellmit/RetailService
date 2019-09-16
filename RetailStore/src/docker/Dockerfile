FROM java:8
EXPOSE 8081
ADD RetailStore-1.0.0.jar /tmp/RetailStore-1.0.0.jar
CMD ["java","-Dserver.port=8081","-Dspring.profiles.active=dev","-jar","/tmp/RetailStore-1.0.0.jar","com.retail.RetailStoreApplication",">>","retail.log","&"]
