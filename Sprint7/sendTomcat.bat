if not exist war mkdir war 
jar -cvf war\Andrana.war -C Andrana\ .
xcopy war\Andrana.war C:\apache-tomcat-10.1.17\webapps\
pause