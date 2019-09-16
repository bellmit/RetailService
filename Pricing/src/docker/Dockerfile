FROM java:8
EXPOSE 8080
ADD Pricing-1.0.0.jar /tmp/Pricing-1.0.0.jar
CMD ["java","-Dserver.port=8080","-Dspring.profiles.active=dev","-jar","/tmp/Pricing-1.0.0.jar","com.retail.pricing.PricingApplication",">>","productprice.log","&"]