package com.demo.moneytransfer;

import com.demo.moneytransfer.di.module.CentralModule;
import com.demo.moneytransfer.di.module.ConfigModule;
import com.demo.moneytransfer.domain.Account;
import com.demo.moneytransfer.server.JettyFactory;
import com.google.inject.Guice;
import com.google.inject.Injector;
import org.eclipse.jetty.server.Server;
import org.junit.BeforeClass;
import org.junit.Test;

import static io.restassured.RestAssured.given;
import static io.restassured.RestAssured.when;
import static org.hamcrest.Matchers.*;


public class IntegrationTest {

    @BeforeClass
    public static void setup() throws Exception {
        startServer();
    }

    @Test
    public void given_url_account_resource_returns_200_with_code_0_and_expected_usernames() {
        given().
                get("account").
                then().
                statusCode(200).
                body("code", is(0),"data.username", hasItems("james", "john"));

    }


    @Test
    public void given_valid_username_get_account_returns_200_with_expected_username_and_code_0() {

        given().
                get("account/{username}", "james").
                then().
                statusCode(200).
                body("data.username", equalTo("james"), "code", is(0));

    }

    @Test
    public void given_invalid_username_get_account_returns_400_with_code_4() {

        when().
                get("account/{username}", "foreign").
                then().
                statusCode(404).
                body("code", is(4));

    }


    @Test
    public void given_valid_account_create_account_returns_200_with_code_0() {
        Account account = new Account("samuel");
        String account_json = "{\"username\":\"samuel\"}";
        given().
                body(account_json).
                contentType("application/json").
                when().
                post("/account").
                then().
                statusCode(201);
    }

    @Test
    public void given_invalid_data_create_account_returns_400() {
        String account_json = "{}";
        given().
                body(account_json).
                contentType("application/json").
                when().
                post("/account").
                then().
                statusCode(400);
    }

    @Test
    public void given_the_same_source_and_destination_account_tranfer_returns_400_with_code_8() {
        given().
                param("to", "james").
                param("amount", 50).
                when().
                get("account/{username}/transfer", "james").
                then().
                statusCode(400).
                body("code", is(8));

    }

    @Test
    public void given_valid_parameters_tranfer_returns_200_with_code_0() {

        given().
                param("to", "john").
                param("amount", 50).
                when().
                get("account/{username}/transfer", "james").
                then().
                statusCode(200).
                body("code", is(0));

    }

    @Test
    public void given_amount_higher_than_balance_tranfer_returns_400_with_code_5() {
        given().
                param("to", "john").
                param("amount", 500).
                when().
                get("account/{username}/transfer", "james").
                then().
                statusCode(400).
                body("code", is(5));
    }

    private static void startServer() throws Exception {
        Injector injector = Guice.createInjector(new CentralModule(), new ConfigModule());
        JettyFactory jettyFactory = new JettyFactory(injector);
        Server server = jettyFactory.createServer();
        server.start();
    }
}

