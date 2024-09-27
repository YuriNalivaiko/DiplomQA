package ru.netology.web.test;

import com.codeborne.selenide.Configuration;
import com.codeborne.selenide.junit5.SoftAssertsExtension;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.RegisterExtension;
import ru.netology.web.data.DataGenerator;
import ru.netology.web.data.SQLHelper;
import ru.netology.web.page.PaymentPage;

import static com.codeborne.selenide.AssertionMode.SOFT;
import static com.codeborne.selenide.Selenide.open;
import static com.codeborne.selenide.Selenide.sleep;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class PaymentTest {
    @RegisterExtension
    static SoftAssertsExtension softAsserts = new SoftAssertsExtension();
    PaymentPage paymentPage = new PaymentPage();
    DataGenerator dataGenerator = new DataGenerator();


    @BeforeEach
    void setup() {
        open("http://localhost:8080");
    }

    @AfterEach
    void cleanDB() {
        SQLHelper.cleanDatabase();
    }

    @Test
    @DisplayName("Сценарий 1.1 Переход к форме заполнения данных карты нажатием кнопки 'Купить'")
    void shouldOpenFormByButtonPay() {
        paymentPage.openFormToPay();
    }

    @Test
    @DisplayName("Сценарий 1.3 Отправка запроса на покупку тура при заполнении полей форм карты валидными данными (переход к форме через кнопку 'Купить')")
    void shouldSendRequestWithValidDataByButtonPay() {
        Configuration.assertionMode = SOFT;
        paymentPage.openFormToPay();
        var number = dataGenerator.getApprovednumberCard();
        var month = DataGenerator.getMonthCard(DataGenerator.generateValidDateCard());
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateValidCVC();
        var messageOk = paymentPage.getMessageOk();
        paymentPage.fillFormWithValidData(number, month, year, holder, cvc, messageOk);
        String actual = SQLHelper.getStatusFromPayment();
        String expected = dataGenerator.getApproved();
        assertEquals(expected, actual);
    }

    @Test
    @DisplayName("Сценарий 1.4 Отправка пустого поля 'Номер карты' при запросе покупки тура (переход к форме через кнопку 'Купить')")
    void sendRequestWithEmptyFieldCardNumberInPayForm() {
        paymentPage.openFormToPay();
        var number = dataGenerator.getEmpty();
        var month = DataGenerator.getMonthCard(DataGenerator.generateValidDateCard());
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateValidCVC();
        var messageError = paymentPage.getErrorEmpty();
        paymentPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }

    @Test
    @DisplayName("Сценарий 1.5 Отправка пустого поля 'Месяц' при запросе покупки тура (переход к форме через кнопку 'Купить')")
    void sendRequestWithEmptyFieldMonthInPayForm() {
        paymentPage.openFormToPay();
        var number = dataGenerator.getApprovednumberCard();
        var month = dataGenerator.getEmpty();
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateValidCVC();
        var messageError = paymentPage.getErrorEmpty();
        paymentPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }

    @Test
    @DisplayName("Сценарий 1.6 Отправка пустого поля 'Год' при запросе покупки тура (переход к форме через кнопку 'Купить')")
    void sendRequestWithEmptyFieldYearInPayForm() {
        paymentPage.openFormToPay();
        var number = dataGenerator.getApprovednumberCard();
        var month = DataGenerator.getMonthCard(DataGenerator.generateValidDateCard());
        var year = dataGenerator.getEmpty();
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateValidCVC();
        var messageError = paymentPage.getErrorEmpty();
        paymentPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }

    @Test
    @DisplayName("Сценарий 1.7 Отправка пустого поля 'Владелец' при запросе покупки тура (переход к форме через кнопку 'Купить')")
    void sendRequestWithEmptyFieldHolderInPayForm() {
        paymentPage.openFormToPay();
        var number = dataGenerator.getApprovednumberCard();
        var month = DataGenerator.getMonthCard(DataGenerator.generateValidDateCard());
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = dataGenerator.getEmpty();
        var cvc = DataGenerator.generateValidCVC();
        var messageError = paymentPage.getErrorEmptyHolder();
        paymentPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }

    @Test
    @DisplayName("Сценарий 1.8 Отправка пустого поля 'CVC/CVV' при запросе покупки тура (переход к форме через кнопку 'Купить')")
    void sendRequestWithEmptyFieldCVCInPayForm() {
        paymentPage.openFormToPay();
        var number = dataGenerator.getApprovednumberCard();
        var month = DataGenerator.getMonthCard(DataGenerator.generateValidDateCard());
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = dataGenerator.getEmpty();
        var messageError = paymentPage.getErrorEmpty();
        paymentPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }

    @Test
    @DisplayName("Сценарий 1.9 Ввод карты со статусом 'DECLINED' в поле 'Номер карты' при запросе покупки тура (переход к форме через кнопку 'Купить')")
    void sendRequestWithDeclinedCardInPayForm() {
        Configuration.assertionMode = SOFT;
        paymentPage.openFormToPay();
        var number = dataGenerator.getDeclinednumberCard();
        var month = DataGenerator.getMonthCard(DataGenerator.generateValidDateCard());
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateValidCVC();
        var messageNOk = paymentPage.getMessageNotOk();
        paymentPage.fillFormWithDeclinedOrNonExistingCard(number, month, year, holder, cvc, messageNOk);
        Long actual = SQLHelper.getNumberRowsFromDBTablePayment();
        assertEquals(0, actual);
    }

    @Test
    @DisplayName("Сценарий 1.10 Ввод карты с количеством символов менее 16 в поле 'Номер карты' при запросе покупки тура (переход к форме через кнопку 'Купить')")
    void sendRequestWithFieldCardNumberLess16SymbolInPayForm() {
        paymentPage.openFormToPay();
        var number = DataGenerator.generateCardLess16Symbol();
        var month = DataGenerator.getMonthCard(DataGenerator.generateValidDateCard());
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateValidCVC();
        var messageError = paymentPage.getErrorEmpty();
        paymentPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }

    @Test
    @DisplayName("Сценарий 1.11 Ввод дополнительной цифры перед номером карты со статусом 'APPROVED' в поле 'Номер карты' при запросе покупки тура (переход к форме через кнопку 'Купить')")
    void sendRequestWith1SymbolAndApprovedCardNumberInPayForm() {
        paymentPage.openFormToPay();
        var number = DataGenerator.generateCardWith17SymbolAtBeginning(dataGenerator.getApprovednumberCard());
        var month = DataGenerator.getMonthCard(DataGenerator.generateValidDateCard());
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateValidCVC();
        var messageNOk = paymentPage.getMessageNotOk();
        paymentPage.fillFormWithDeclinedOrNonExistingCard(number, month, year, holder, cvc, messageNOk);
    }

    @Test
    @DisplayName("Сценарий 1.12 Ввод дополнительной цифры после номера карты со статусом 'APPROVED' в поле 'Номер карты' при запросе покупки тура (переход к форме через кнопку 'Купить')")
    void sendRequestWithApprovedCardNumberAndNextSymbolInPayForm() {
        paymentPage.openFormToPay();
        var number = DataGenerator.generateCardWith17SymbolInEnd(dataGenerator.getApprovednumberCard());
        var month = DataGenerator.getMonthCard(DataGenerator.generateValidDateCard());
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateValidCVC();
        var messageOk = paymentPage.getMessageOk();
        paymentPage.fillFormWithValidData(number, month, year, holder, cvc, messageOk);
    }

    @Test
    @DisplayName("Сценарий 1.13 Ввод невалидных значений (букв,символов) в поле 'Номер карты' при запросе покупки тура (переход к форме через кнопку 'Купить')")
    void sendRequestWithInvalidDataFieldCardNumberInPayForm() {
        paymentPage.openFormToPay();
        var number = DataGenerator.generateCardWith16InvalidSymbol(DataGenerator.generateStringWithInvalidSymbol());
        var month = DataGenerator.getMonthCard(DataGenerator.generateValidDateCard());
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateValidCVC();
        var messageError = paymentPage.getErrorEmpty();
        paymentPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }

    @Test
    @DisplayName("Сценарий 1.14 Ввод номера месяца в формате 'м' в поле 'Месяц' при запросе покупки тура (переход к форме через кнопку 'Купить')")
    void sendRequestWith1SymbolInFieldMonthInPayForm() {
        paymentPage.openFormToPay();
        var number = dataGenerator.getApprovednumberCard();
        var month = DataGenerator.generateMonthOrYear1Symbol();
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateValidCVC();
        var messageError = paymentPage.getErrorEmpty();
        paymentPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }

    @Test
    @DisplayName("Сценарий 1.15 Ввод значения превышающего 12 в поле 'Месяц' при запросе покупки тура (переход к форме через кнопку 'Купить')")
    void sendRequestWithMonthMore12InPayForm() {
        paymentPage.openFormToPay();
        var number = dataGenerator.getApprovednumberCard();
        var month = DataGenerator.generateMonthMore12();
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateValidCVC();
        var messageError = paymentPage.getErrorMonth();
        paymentPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }

    @Test
    @DisplayName("Сценарий 1.16 Ввод значения '00' в поле 'Месяц' при запросе покупки тура (переход к форме через кнопку 'Купить')")
    void sendRequestWithMonth00InPayForm() {
        paymentPage.openFormToPay();
        var number = dataGenerator.getApprovednumberCard();
        var month = dataGenerator.getMonth00();
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateValidCVC();
        var messageError = paymentPage.getErrorMonth();
        paymentPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }

    @Test
    @DisplayName("Сценарий 1.17 Ввод невалидных значений (букв,символов) в поле 'Месяц' при запросе покупки тура (переход к форме через кнопку 'Купить')")
    void sendRequestWithInvalidSymbolInFieldMonthInPayForm() {
        paymentPage.openFormToPay();
        var number = dataGenerator.getApprovednumberCard();
        var month = DataGenerator.generateMonthOrYearWithInvalidSymbol(DataGenerator.generateStringWithInvalidSymbol());
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateValidCVC();
        var messageError = paymentPage.getErrorEmpty();
        paymentPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }

    @Test
    @DisplayName("Сценарий 1.18 Ввод года в формате 'г' в поле 'Год' при запросе покупки тура (переход к форме через кнопку 'Купить')")
    void sendRequestWith1SymbolInFieldYearInPayForm() {
        paymentPage.openFormToPay();
        var number = dataGenerator.getApprovednumberCard();
        var month = DataGenerator.getMonthCard(DataGenerator.generateValidDateCard());
        var year = DataGenerator.generateMonthOrYear1Symbol();
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateValidCVC();
        var messageError = paymentPage.getErrorEmpty();
        paymentPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }

    @Test
    @DisplayName("Сценарий 1.19 Ввод невалидных значений (букв,символов) в поле 'Год' при запросе покупки тура (переход к форме через кнопку 'Купить')")
    void sendRequestWithInvalidSymbolInFieldYearInPayForm() {
        paymentPage.openFormToPay();
        var number = dataGenerator.getApprovednumberCard();
        var month = DataGenerator.getMonthCard(DataGenerator.generateValidDateCard());
        var year = DataGenerator.generateMonthOrYearWithInvalidSymbol(DataGenerator.generateStringWithInvalidSymbol());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateValidCVC();
        var messageError = paymentPage.getErrorEmpty();
        paymentPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }

    @Test
    @DisplayName("Сценарий 1.20 Ввод года в формате 'гггг' в поле 'Год' при запросе покупки тура (переход к форме через кнопку 'Купить')")
    void sendRequestWith4SymbolInFieldYearInPayForm() {
        paymentPage.openFormToPay();
        var number = dataGenerator.getApprovednumberCard();
        var month = DataGenerator.getMonthCard(DataGenerator.generateValidDateCard());
        var year = DataGenerator.generateYearCard4Symbol();
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateValidCVC();
        var messageError = paymentPage.getErrorExpiredYear();
        paymentPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }

    @Test
    @DisplayName("Сценарий 1.21 Ввод истекшего срока действия карты при запросе покупки тура (переход к форме через кнопку 'Купить')")
    void sendRequestWithExpiredDateInFieldYearInPayForm() {
        paymentPage.openFormToPay();
        var number = dataGenerator.getApprovednumberCard();
        var month = DataGenerator.getMonthCard(DataGenerator.generateDateExpiredCard());
        var year = DataGenerator.getYearCard(DataGenerator.generateDateExpiredCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateValidCVC();
        var messageError = paymentPage.getErrorExpiredYear();
        paymentPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }

    @Test
    @DisplayName("Сценарий 1.22 Ввод недействительного срока действия карты при запросе покупки тура (переход к форме через кнопку 'Купить')")
    void sendRequestWithNotExistingDateInFieldYearInPayForm() {
        paymentPage.openFormToPay();
        var number = dataGenerator.getApprovednumberCard();
        var month = DataGenerator.getMonthCard(DataGenerator.generateCardWithNotExistDate());
        var year = DataGenerator.getYearCard(DataGenerator.generateCardWithNotExistDate());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateValidCVC();
        var messageError = paymentPage.getErrorNotExistYear();
        paymentPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }

    @Test
    @DisplayName("Сценарий 1.23 Ввод невалидных значений (буквы, символы) в поле 'CVC/CVV' при запросе покупки тура (переход к форме через кнопку 'Купить')")
    void sendRequestWithInvalidSymbolInFieldCVCInPayForm() {
        paymentPage.openFormToPay();
        var number = dataGenerator.getApprovednumberCard();
        var month = DataGenerator.getMonthCard(DataGenerator.generateValidDateCard());
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateCVCWithInvalidSymbol(DataGenerator.generateStringWithInvalidSymbol());
        var messageError = paymentPage.getErrorEmpty();
        paymentPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }

    @Test
    @DisplayName("Сценарий 1.24 Ввод 2-значного числа в поле 'CVC/CVV' при запросе покупки тура (переход к форме через кнопку 'Купить')")
    void sendRequestWith2SymbolInFieldCVCInPayForm() {
        paymentPage.openFormToPay();
        var number = dataGenerator.getApprovednumberCard();
        var month = DataGenerator.getMonthCard(DataGenerator.generateValidDateCard());
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateCVCWith2Symbol();
        var messageError = paymentPage.getErrorEmpty();
        paymentPage.fillFormWithEmptyOrErrorDataField(number, month, year, holder, cvc, messageError);
    }

    @Test
    @DisplayName("Сценарий 1.25 Ввод рандомного 16-значного номера карты в поле 'Номер карты' при запросе покупки тура (переход к форме через кнопку 'Купить')")
    void sendRequestWithNonExistCardInPayForm() {
        Configuration.assertionMode = SOFT;
        paymentPage.openFormToPay();
        var number = DataGenerator.generateCardWith16Symbol();
        var month = DataGenerator.getMonthCard(DataGenerator.generateValidDateCard());
        var year = DataGenerator.getYearCard(DataGenerator.generateValidDateCard());
        var holder = DataGenerator.generateNameOfHolderCard();
        var cvc = DataGenerator.generateValidCVC();
        var messageNOk = paymentPage.getMessageNotOk();
        paymentPage.fillFormWithDeclinedOrNonExistingCard(number, month, year, holder, cvc, messageNOk);
        Long actual = SQLHelper.getNumberRowsFromDBTablePayment();
        assertEquals(0, actual);
    }

    @Test
    @DisplayName("Сценарий 1.26 Запрос на покупку тура с пустыми полями анкеты, а затем с валидными данными без перезагрузки страницы (переход к форме через кнопку 'Купить')")
    void sendRequestWithEmptyAllFieldAndThenValidDataInPayForm() {
        Configuration.assertionMode = SOFT;
        paymentPage.openFormToPay();
        var emptyField = DataGenerator.generateAllEmptyField();
        var validField = DataGenerator.generateValidField(dataGenerator.getApprovednumberCard());
        paymentPage.fillFormWithEmptyAllFieldAndThenValidData(emptyField, validField);
        sleep(1000);
    }

}
