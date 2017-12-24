# Installation

## Generate and install a new Root Certificate

```
$ docker-compose up -d proxy nodepki
```

Steps:

 * Install a proxy switcher
 * Set proxy url to socks5://localhost:1080
 * Goto https://nodepki, 
 * install the newly gegerated Root Certificate 
 * Generate and install the following servers
   * nexus
   * rabbitmq

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
