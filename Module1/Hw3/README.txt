																	1. Maven
							1.1 go to builders directory
							Open cmd, type: 
							mvn install
							cd admin/target
							java -jar admin-maven.jar hi
							Message should appear in the cmd.
						
							Jar file: builders/admin/target/admin-maven.jar
							War file: builders/web/target/web-1.0.war
							
									1.2 To make a jar file only do the following : 
									open cmd, type:
									cd admin 
									mvn package
									cd target
									java -jar admin-maven.jar hi
									Message should appear in the cmd.

							1.3 As an optional way, you can deploy war file with one command:
							go to builders/web and open cmd. Type:
							mvn tomcat7:run
							Go to http://localhost:8080/web-1.0/
							
									1.4 To run tests go to builders dir, open cmd and type:
									mvn test


																	2. Gradle 
							2.1 Go to builders directory and open cmd
							Type: 
							gradle build
							
							Jar file: builders/admin/build/libs/admin.jar
							War file: builders/web/build/libs/web.war
																	
							As an optional way, you can deploy war file with one command:
							go to builders/web and open cmd. Type:
							gradle tomcatRunWar
							Go to http://localhost:8080/web

									2.2 To build only jar file type from the builders directory:
									cd admin
									gradle jar
									cd build/libs
									java -jar admin.jar hi

							2.3 To build only war do the following:
							Go to builders directory and open cmd
							Type:
							cd web
							gradle war
							cd build/libs
							cp web.war Path/to/tomcat/webapps
							cd Path/to/tomcat/bin
							startup.bat
							Go to http://localhost:8080/web/
							
									2.4 To run tests open cmd, go to builders dir and type:
									gradle test

							
																	3. Ant
							3.1 Go to builders directory and open cmd, type:
							ant
							
							Jar file: builders/admin/dist/admin.jar
							War file: builders/web/web.war

									3.2 To make jar file only do the following, type in cmd:
									cd admin
									ant jar
									cd dist
									java -jar admin.jar hi

							3.3 To build war file only do the following:
							Go to builders directory and open cmd
							Type:
							cd web
							ant war
							cp web.war Path/to/tomcat/webapps
							cd Path/to/tomcat/bin
							startup.bat
							Go to http://localhost:8080/web/ 
							
							3.4 To run tests go to builders dir, open cmd and type:
							ant test