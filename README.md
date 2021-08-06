# Brewery Spring MVC Monolith
by John Thompson

This repository contains source code examples used to support my on-line courses about the Spring Framework.
[Spring Security Core: Beginner to Guru](https://www.udemy.com/course/spring-security-core-beginner-to-guru/?referralCode=306F288EB78688C0F3BC)

This repo is a fork from his repo. The kxenmaster branch is my development branch.

## Getting started

Before adding Spring Security (SpSec) to the POM, there is no login form. After adding the dependency to the classpath, a default
login form, using HTTP Basic Auth, is generated. The default username in 'user', and you will find the generated password
in the console when you start the application.

Add this to the POM
```xml
<dependency>
    <groupId>org.springframework.boot</groupId>
    <artifactId>spring-boot-starter-security</artifactId>
</dependency>
```

## Java Config

Use Java config to override the default behavior of SpSec by providing a [`WebSecurityConfigurerAdapter`](https://docs.spring.io/spring-security/site/docs/current/reference/html5/#oauth2login-provide-websecurityconfigureradapter).
Override the [`WebSecurityConfigurerAdapter.congifure(HttpSecurity)`](https://docs.spring.io/spring-security/site/docs/current/api/org/springframework/security/config/annotation/web/configuration/WebSecurityConfigurerAdapter.html#configure(org.springframework.security.config.annotation.web.builders.HttpSecurity) method
which provides the default behavior.

Here the antMatchers cover the index page, login, static resources, and the front-end Find Beers page.

```
    @Override
    protected void configure(HttpSecurity http) throws Exception {
        http
            .authorizeRequests(authorize ->
                authorize
                    .antMatchers("/", "/webjars/**", "/login", "/resources/**").permitAll()
                    .antMatchers("/beers/find", "/beers*").permitAll()
            )
            .authorizeRequests() // all the paths
            .anyRequest().authenticated()
            .and()
            .formLogin().and()
            .httpBasic();
    }
```

For a REST API, include the HTTP method. Without the HTTP method, the matchers don't care what method is used.
```
.antMatchers(HttpMethod.GET, "/api/v1/beer/*").permitAll()
```

MVC Matchers use the same syntax as that used to define the request mapping.

Given mappings in a `@RestController`:
```java
@RequestMapping("/api/v1/")
@GetMapping(path = {"beerUpc/{upc}"}, produces = { "application/json" })
```

To add `permitAll` to this mapping using `mvcMatchers`:
```java
.mvcMatchers(HttpMethod.GET, "/api/v1/beerUpc/{upc}").permitAll()
```

## Tests

To test the integration of SpSec with the controllers you need `@WebMvcTest` on the class. You then want to get the autowired
web context (wac). That's standard for any MVC test.

This will stuff user credentials into the wac without going through the authentication manager.
```
@WithMockUser("someuser")
```

You don't use that if you are using HTTP Basic Auth. In that case you use this which does go through the authentication manager.
For this example, fred & bob are configured in the `application.properties` as the default user/password. The `httpBasic()` method
does the Base64 encoding and sets the `Authroization: Basic` header.
```
mockMvc.perform(get("/beers/find").with(httpBasic("fred", "bob")))
```