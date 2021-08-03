# Brewery Spring MVC Monolith

This repository contains source code examples used to support my on-line courses about the Spring Framework.
[Spring Security Core: Beginner to Guru](https://www.udemy.com/course/spring-security-core-beginner-to-guru/?referralCode=306F288EB78688C0F3BC)

This repo is a fork from his repo. The kxenmaster branch is my development branch.

## Getting started

Before adding Spring Security to the POM, there is no login form. After adding the dependency to the classpath, a default
login form, using HTTP Basic Auth, is generated. The default username in 'user', and you will find the generated password
in the console when you start the application.

Add this to the POM
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```