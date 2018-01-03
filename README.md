# WIP WIP WIP 

## TODO 
 * java part out of this repo and into its own and use git submodule
 * build and run in seperate processes

# Description 

Uses NodePKI to generate SSL certificates for each server/service and preconfigures those servers to use the certificates. 

Installs the following servers:
 * Socks5 proxy
 * NodePKI
 * Nexus
 * Gitlab
 * RabbitMQ
 * Example java application

# Installation

```
$ git clone --recurse-submodules https://github.com/marcelmaatkamp/java-spring-boot-example.git
```

## Proxy

```
$ docker-compose up -d proxy
```

 * Install a proxy switcher
 * Set proxy url to socks5://localhost:1080

## NodePKI

```
$ docker-compose up -d proxy nodepki
$ docker-compose run nodepki ash -c \
    "cd /root/nodepki && node /root/nodepki/nodepkictl.js useradd --username admin --password admin"
```

Goto http://nodepki and login with `username=admin` and `password=admin`

### Generate a new `root certificate`

Goto http://nodepki:5000/cacerts and install the newly gegerated `Root Certificate` 

[[screenshot nodepki root certs]]

### Install and trust the new `root certificate` in keychain (mac)

[[screenshot keychain mac]

### Generate new `server certificates`

Goto http://nodepki:5000/request and install the following servers with the `Common name`

   * gitlab
   * nexus
   * rabbitmq
   * graylog
   * project

## Gitlab

```
$ docker-compose run gitlab bash -c \
    "cd /etc/gitlab/ssl &&\ 
     ln -s domain.key gitlab.key &&\
     ln -s cacert.crt gitlab.crt &&\
     cp root.pem /etc/gitlab/trusted-certs"
$ docker-compose up -d gitlab
```

Goto https://gitlab, set admin password for user `root` and login 

## Nexus

```
$ docker-compose run nodepki ash -c 'cd /certs/nexus &&\
   openssl x509 -in root.pem -inform PEM -out root.crt &&\ 
   cp root.crt /usr/local/share/ca-certificates &&\
   update-ca-certificates &&\
   openssl pkcs12 -export -in signed.crt -inkey nexus.key -chain -CAfile nexus.crt -name nexus -out nexus.p12'

And use password `password`

$ docker-compose run nexus ash -c 'cd /certs/nexus &&\
   keytool -importkeystore -deststorepass password -destkeystore /nexus-data/keystore.jks -srckeystore nexus.p12 -srcstoretype PKCS12'
```

If succceeded: 

```
Enter source keystore password:  
Entry for alias nexus successfully imported.
Import command completed: 1 entries successfully imported, 0 entries failed or cancelled
```

Start Nexus:

```
$ docker-compose up -d nexus
```

Goto https://nexus:8443

## Proxy
| Name | URL | 
| ---------| -------- |
| jfrog | https://dl.bintray.com/jfrog/jfrog-jars   |
| gradle.plugin   | https://plugins.gradle.org/m2/    | 

## GROUP `remote-repos`  
 
| Name | 
| ---------| 
| maven-central | 
| maven-public  | 
| `maven-releases` |
| jfrog   |

## GROUP `gradle.plugins`  

| Name | 
| ---------| 
| gradle.plugin   | 
 
Now you can proxy artifacts and plugins in `build.gradle` like this through nexus:

```
buildscript {
    repositories {
        maven { url 'https://nexus/repository/remote-repos/' }
        maven { url 'https://nexus/repository/gradle.plugins/' }
    }

    dependencies {
        classpath 'se.transmode.gradle:gradle-docker:1.2'
        classpath "org.codehaus.groovy:groovy-all:2.3.6"
        classpath "nl.eveoh:gradle-aspectj:1.6"
        classpath "org.jfrog.buildinfo:build-info-extractor-gradle:4.0.0"
        classpath 'com.bmuschko:gradle-docker-plugin:3.2.1'
    }
}
plugins {
    id 'org.springframework.boot' version '1.5.9.RELEASE'
    id "com.bmuschko.nexus" version "2.3.1"
}

repositories {
    maven { url 'https://nexus/repository/remote-repos/' }
}
dependencies {
    compile "org.springframework.boot:spring-boot-starter-amqp"
    compile "org.springframework.boot:spring-boot-starter-jetty"
    compile "org.springframework.boot:spring-boot-starter-actuator"
    compile "org.springframework.boot:spring-boot-devtools"
    compile "org.springframework.boot:spring-boot-starter-data-jpa"
    testCompile "org.springframework.boot:spring-boot-starter-test"
    testCompile "junit:junit"
}

```

## RabbitMQ

```
$ docker-compose up -d rabbitmq
```

Goto https://rabbitmq:15671

## Project

```
$ docker-compose run --entrypoint ash producer -c 'sudo update-ca-certificates && sudo ln -sf /etc/ssl/certs/java/cacerts $JAVA_HOME/jre/lib/security/cacerts'
```

## Git clone project 

```
$ git clone
```

## Build
```
$ gradle assemble
```

## Test
```
$ gradle test
```

## Run
```
$ gradle bootRun
```

## Documentation
```
$ gradle javadoc
```

# Deploy

## Nexus

`~/.gradle/gradle.properties`:

```
nexusUsername = admin
nexusPassword = admin123
```

```
$ gradle uploadArchives
```

## Docker

```
$ gradle dockerPushImage
```

```
$ docker-compose up -d proxy graylog rabbitmq
$ docker-compose up producer
```

# "I want to fix a bug!!!"

```
$ git clone \
 cd producer-xml &&\
 docker-compose up -d proxy rabbitmq graylog2 &&\
 gradle dockerBuildImage &&\
 docker-compose up producer
```

and attach a debugger on port 5005 (see docker-compose.yml, JAVA_OPTS)
