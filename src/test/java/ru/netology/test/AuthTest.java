package ru.netology.test;


import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.logevents.SelenideLogger;
import io.qameta.allure.selenide.AllureSelenide;
import org.junit.jupiter.api.AfterAll;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import com.codeborne.selenide.Condition;
import org.junit.jupiter.api.*;
import ru.netology.data.DataGenerator;

import java.time.Duration;

import static com.codeborne.selenide.Selenide.*;


class AuthTest {

    @BeforeAll
    static void addListener(){
        SelenideLogger.addListener("AllureSelenide", new AllureSelenide());
    }

    @AfterAll
    static void removeListener(){
        SelenideLogger.removeListener("AllureSelenide");
    }

    @BeforeEach
    void setup() {
        open("http://localhost:9999");
    }

    String name = DataGenerator.generateName();
    String phone = DataGenerator.generatePhone();
    String city = DataGenerator.generateCity();


    @Test
    void shouldTest() {
        Configuration.holdBrowserOpen = true;
        $("[data-test-id='city'] input").setValue(city);
        $("[data-test-id=date] input").doubleClick().sendKeys(DataGenerator.generateDate(3));
        $("[data-test-id='name'] input").setValue(name);
        $("[data-test-id='phone'] input").setValue(phone);
        $("[data-test-id='agreement']").click();
        $$("button").find(Condition.exactText("Запланировать")).click();
        $(".notification_status_ok").shouldBe(Condition.visible);
        $("[data-test-id='success-notification'] .notification__content").shouldHave(Condition.exactText("Встреча успешно запланирована на " + DataGenerator.generateDate(3)));
        $("[data-test-id=date] input").doubleClick().sendKeys(DataGenerator.generateDate(5));
        $("[data-test-id='success-notification']").shouldBe(Condition.visible, Duration.ofSeconds(15));
        $$("button").find(Condition.exactText("Запланировать")).click();
        $("[data-test-id=replan-notification]").shouldHave(Condition.text("Необходимо подтверждение"));
        $("[data-test-id=replan-notification]").$$("button").find(Condition.exactText("Перепланировать")).click();
        $(".notification_status_ok").shouldBe(Condition.visible);
        $(".notification__content").shouldHave(Condition.exactText("Встреча успешно запланирована на " + DataGenerator.generateDate(5)));


    }
}
