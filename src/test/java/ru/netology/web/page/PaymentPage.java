package ru.netology.web.page;

import com.codeborne.selenide.Condition;
import com.codeborne.selenide.SelenideElement;
import lombok.Getter;
import org.openqa.selenium.By;


import java.time.Duration;

import static com.codeborne.selenide.Condition.*;
import static com.codeborne.selenide.Selectors.byXpath;
import static com.codeborne.selenide.Selenide.*;
import static ru.netology.web.data.DataGenerator.*;

public class PaymentPage {
    private SelenideElement buttonPay = $(By.xpath("//span[contains(text(),'Купить')]"));
    private SelenideElement buttonPayCredit = $(By.xpath("//span[contains(text(),'Купить в кредит')]"));
    private SelenideElement headingPay = $(By.xpath("//h3[text()='Оплата по карте']"));
    private SelenideElement headingPayCredit = $(By.xpath("//h3[text()='Кредит по данным карты']"));
    private SelenideElement fieldNumber = $$(".form-field").findBy(text("Номер карты")).find(".input__control");
    private SelenideElement fieldMonth = $$(".form-field .input-group__input-case").findBy(text("Месяц")).find(".input__control");
    private SelenideElement fieldYear = $$(".form-field .input-group__input-case").findBy(text("Год")).find(".input__control");
    private SelenideElement fieldHolder = $$(".form-field .input-group__input-case").findBy(text("Владелец")).find(".input__control");
    private SelenideElement fieldCVC = $$(".form-field .input-group__input-case").findBy(text("CVC/CVV")).find(".input__control");

    private SelenideElement buttonContinue = $$(".button__text").findBy(text("Продолжить"));
    private SelenideElement notificationOk = $(".notification_status_ok .notification__content");
    private SelenideElement notificationNOk = $(".notification_status_error .notification__content");
    private SelenideElement fieldError = $(".input__sub");
    private SelenideElement fieldErrorNumber = $$(".form-field .input_invalid").findBy(text("Номер карты")).find(".input__sub");
    private SelenideElement fieldErrorMonth = $$(".form-field .input_invalid").findBy(text("Месяц")).find(".input__sub");
    private SelenideElement fieldErrorYear = $$(".form-field .input_invalid").findBy(text("Год")).find(".input__sub");
    private SelenideElement fieldErrorHolder = $$(".form-field .input_invalid").findBy(text("Владелец")).find(".input__sub");
    private SelenideElement fieldErrorCVC = $$(".form-field .input_invalid").findBy(text("CVC/CVV")).find(".input__sub");

    @Getter
    private final String messageOk = "Операция одобрена Банком.";
    @Getter
    private final String messageNotOk = "Ошибка! Банк отказал в проведении операции.";
    @Getter
    private final String errorEmpty = "Неверный формат";
    @Getter
    private final String errorEmptyHolder = "Поле обязательно для заполнения";
    @Getter
    private final String errorMonth = "Неверно указан срок действия карты";
    @Getter
    private final String errorExpiredYear = "Истёк срок действия карты";
    @Getter
    private final String errorNotExistYear = "Неверно указан срок действия карты";

    public void openFormToPay() {
        buttonPay.click();
        headingPay.shouldBe(visible);
    }

    public void openFormToPayCredit() {
        buttonPayCredit.click();
        headingPayCredit.shouldBe(visible);
    }

    public void fillFormWithData(String number, String month, String year, String holder, String cvc) {
        fieldNumber.setValue(number);
        fieldMonth.setValue(month);
        fieldYear.setValue(year);
        fieldHolder.setValue(holder);
        fieldCVC.setValue(cvc);
        sleep(300);
        buttonContinue.click();
        sleep(300);

    }

    public void fillFormWithValidData(String number, String month, String year, String holder, String cvc, String messageOk) {
        fillFormWithData(number, month, year, holder, cvc);
        notificationOk.shouldBe(visible, Duration.ofSeconds(15)).shouldHave(Condition.text(messageOk));
    }

    public void fillFormWithEmptyOrErrorDataField(String number, String month, String year, String holder, String cvc, String errorOrEmpty) {
        fillFormWithData(number, month, year, holder, cvc);
        fieldError.shouldBe(visible).shouldHave(text(errorOrEmpty));
    }

    public void fillFormWithDeclinedOrNonExistingCard(String number, String month, String year, String holder, String cvc, String messageNOk) {
        fillFormWithData(number, month, year, holder, cvc);
        notificationNOk.shouldBe(visible, Duration.ofSeconds(15)).shouldHave(text(messageNotOk));
    }

    public void fillFormWithEmptyAllFieldAndThenValidData(Date dateEmpty, Date dateValid) {
        fillFormWithData(dateEmpty.getNumber(), dateEmpty.getMonth(), dateEmpty.getYear(), dateEmpty.getHolder(), dateEmpty.getCvc());
        fieldErrorNumber.shouldBe(visible);
        fieldErrorMonth.shouldBe(visible);
        fieldErrorYear.shouldBe(visible);
        fieldErrorHolder.shouldBe(visible);
        fieldErrorCVC.shouldBe(visible);
        fillFormWithData(dateValid.getNumber(), dateValid.getMonth(), dateValid.getYear(), dateValid.getHolder(), dateValid.getCvc());
        fieldErrorNumber.shouldNot(visible);
        fieldErrorMonth.shouldNot(visible);
        fieldErrorYear.shouldNot(visible);
        fieldErrorHolder.shouldNot(visible);
        fieldErrorCVC.shouldNot(visible);


    }


}




