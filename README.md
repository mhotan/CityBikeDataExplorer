CityBikeDataExplorer
====================

*Team Project for Modern Databases and Their Application's Class*

###Setup - Installing Software
Current Process was used to 

<p>
#### Install [Java SE 8](http://www.oracle.com/technetwork/java/javase/downloads/jdk8-downloads-2133151.html)

* Make sure you Accept there Liscence agreement via the check box
* Pick the appropiate Operating system and continue to download
* Run the installer package

<p>
#### Install [Git](http://git-scm.com/downloads)
for version control

<p>
#### Install [SourceTree](http://www.sourcetreeapp.com/download/) (Optional for Windows and Mac) 
Provides easy to use and understand Git UI. Suggested this for new Gitters :thumbsup:

<p>
#### Install [Intellij](http://www.jetbrains.com/idea/download/) (Optional)
Interactive IDE that integrates gradle build system. Caveat, any modern IDE should work.

<p>
#### Install [Vagrant](http://www.vagrantup.com/) and [VirtualBox](https://www.virtualbox.org/)
Provides a consistent virtual machine development environment.

### Setup - Checking out from Github
<p>
#### Via Intellij 
*NOTE: This requires you to have Collaborator access to this project*

1. Open Intellij application.  You will be directed to **Welcome to Intellihj IDEA** welcome screen.
2. Select **Check out from Version Control**
3. A drop down will appear, select **Github**.
4. You may be asked for your credentials.  Provide your Github credentials.
5. Under **Vcs Repository URL:** select *https::github.com/mhotan/CityBikeDataExplorer.git*
6. Under **Parent Directory:** Use the directory where you store your local checked out version of your repositories
7. Leave **Directory Name:** as the default if you can.  If not rename to whatever

#### Via Source Tree
1. Select the **Add Repository** Button
2. Select **Clone Repository** Tab
3. Under **Source Path / URL:** paste https://github.com/mhotan/CityBikeDataExplorer.git
4. hit *enter* fource Source Tree to validate the repository
5. Set **Destination Path:** to be the empty directory you wish to check out the repository into
6. Hit **Clone**
7. In Intellij select **Open Project** and select the path you used for **Destination Path:**

#### Via Command Line
1. Create a new directory where ever you want to place the project.
2. type > git clone https://github.com/mhotan/CityBikeDataExplorer.git
3. In Intellij select **Open Project** and select the path stored the repository at.

###Building

<p>
#### Building in Intellij

* Build -> Rebuild Project

#### Fixing Build Issues

<p>
#####Might have to setup your JDK
* Set your JDK.  File -> Configure Project SDK to point to your Java 8 Home directory

#####Might have to refresh 
* Refresh Gradle Dependencies. View -> Tool Windows -> Gradle.  Select the Refresh button.  The two arrows in a circle.

###Executing

<p>
####Running specific classes
<p>
Within intellij right clicking on any class with the Java main function will run that specific class.

####Running the RESTful web service
<p>
* To run the web server. Execute "./gradlew clean build && java -jar server/build/libs/gs-rest-service-0.1.0.jar" while in the root directory of the project in your terminal.  This will rebuild the project and run an embedded tomcat server.
* Now the you can connect to http://localhost port: 8080.  Try http://localhost:8080/greeting?name=Your_Name to make a rest call and get a response.

#### Running under Vagrant
* To start the VM, run `vagrant up` in the project directory. The first time, this will initialize the VM, which might take 10-30 minutes and ~500MB bandwidth.
* To open an SSH session into the VM, run `vagrant ssh`. The project directory is available as `/vagrant/` in the VM.
* Ports 5432 (PostgreSQL), 7474 (Neo4j), and 8080 (web server) are forwarded from the host machine to the VM. For PostgreSQL, use the username/password `vagrant`/`vagrant`.
* Stop the VM using `vagrant halt`.

### Parsing Data

#### General
* Place zip/gzip/csv data files in core/src/main/resources/data.
* Create a class that implements CitiBikeReader
* Create an instance of DefaultCitiBikeParser passing in your instance CitiBikeReader.  
* Call DefaultCitiBikeParser.parse() and the tripdata will be forwarded to your Reader.


### Loading Data into Embedded Graph Database
* Build using "./gradlew clean build" from the project root directory (if needed)
* run "java -jar graph-load-db/build/libs/citibike-load-data-0.1.0.jar [path directory of csv or zip files]"

### Starting REST Service
* Build using "./gradlew clean build" from the project root directory (if needed)
* run "java -jar graph-db-driver/build/libs/citibike-graph-driver-0.1.0.jar"