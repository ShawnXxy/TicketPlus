FROM tomcat:9 

# Copy files from local disk to image
COPY TicketPlus.war /usr/local/tomcat/webapps/
