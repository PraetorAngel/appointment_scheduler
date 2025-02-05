# c868 - Appointment System

A scheduling application designed to manage appointments for customers and contacts, featuring scheduling functionalities, custom reports, and localization.

## Author Information
**Author:** PraetorAngel
**Application Version:** v4.2.0  

## Technologies
**IDE:** IntelliJ IDEA 2024.2
**JDK:** OpenJDK 17.0.2  
**JavaFX SDK:** 17.0.2  
**MySQL Connector Driver:** 8.0.40

**Operating System:**  
- **OS Version:** Microsoft Windows 10 Pro  
- **Build Number:** 19045  
- **Architecture:** 64-bit  

## Finish Build Date
01-27-2025

## Initial Setup
1.	Install and Set Up Database
   
	A. Install MySQL Server & Workbench
		Download MySQL Installer from MySQL's website.
		Run it and install:
			MySQL Server
			MySQL Workbench (optional if using command line)
		Set a root password when prompted.

	B. Verify Installation
		Open Command Prompt and run:
			mysql -u root -p

		Enter the root password.
	
	C. Create Database
		Run in MySQL Workbench:
			SOURCE c868_db_ddl.txt;

	D. Insert Initial Data
		Run in MySQL Workbench:
			SOURCE c868_db_dml.txt;

	Connection details if necessary:
		Refer to JDBC_source.txt located in the \setup_files\ folder in the project root
		
3. 	Confirm all other dependencies are installed
	A.	Java 17 (v17.0.2 or higher) 
			If necessary, download it here: https://download.java.net/java/GA/jdk17.0.2/dfd4a8d0985749f896bed50d7138ee7f/8/GPL/openjdk-17.0.2_windows-x64_bin.zip
			

## Running the Program
1.	Launch Executable (choose one). Admin privileges are not required.
	A.	c868final.jar
		Must be launched from command prompt, powershell, or terminal using the following command:
			java -jar c868final.jar
	B.  c868finale.exe
		Double click the c868finale.exe file to run


2. Log in using the provided credentials (choose one set):  
	A.	Username: `test`  
		Password: `test`  
    B.	Username: `admin`  
		Password: `admin`  

3.	Refer to user_guide.pdf if additional help is needed using the program once authenticated.

## Maintenance		
1.	To add additional users, use the query editor in MySQL Workbench, or enter the following in command line.
	Open Command Prompt and run:
		mysql -u root -p

	Enter the root password.

	Then type the following commands, one at a time, into mysql. Replace 'JackSparrow' with the chosen new Username and replace "Leverage" with the chosen password.
		CREATE USER 'JackSparrow'@'%' IDENTIFIED BY 'Leverage';
		GRANT ALL PRIVILEGES ON *.* TO 'JackSparrow'@'%' WITH GRANT OPTION;
		FLUSH PRIVILEGES;
	
** Modifying the database itself via sql commands is not part of maintenance or expected modification, and is beyond the scope of this readme **

## Modification
1.	Open the project in IntelliJ IDEA. 
		If necessary, download it here: https://download.jetbrains.com/idea/ideaIU-2024.3.1.1.exe
   
	Continue to step 2 below. In case of issue, proceed with the remaining steps here.
   
	Ensure the required JDK OpenJDK 17.0.2 is properly configured in your project settings.
		If necessary, download it here: https://download.java.net/java/GA/jdk17.0.2/dfd4a8d0985749f896bed50d7138ee7f/8/GPL/openjdk-17.0.2_windows-x64_bin.zip
    Ensure the required JavaFX SDK 17.0.2 is properly configured in your project settings.
		If necessary, download it here: https://download2.gluonhq.com/openjfx/17.0.2/openjfx-17.0.2_windows-x64_bin-sdk.zip
	Ensure that the JDBC driver located at /lib/mysql-connector-j-8.4.0.jar has been added to sources/libraries in project settings. 
		If necessary, download it here: https://repo1.maven.org/maven2/com/mysql/mysql-connector-j/8.4.0/mysql-connector-j-8.4.0.jar

2.	Make the desired additions or changes
	
	Example: To add support for additional languages, create .properties files for all classes which currently have them, translated into the desired language. The .properties files go in the /resources/ section of the project.
		e.g. ViewAppointments_de.properties will go in /%projectroot%/src/main/resources/com/ogborn/c868final/
