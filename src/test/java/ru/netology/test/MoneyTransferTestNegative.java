package ru.netology.test;

import lombok.val;
import org.junit.jupiter.api.Test;
import ru.netology.data.DataHelper;
import ru.netology.page.DahsboardPage;
import ru.netology.page.LoginPage;

import static com.codeborne.selenide.Selenide.open;
import static org.junit.jupiter.api.Assertions.assertEquals;

public class MoneyTransferTestNegative {

    @Test
    void shouldInvalidVerification() {
        val loginPage = open("http://localhost:9999", LoginPage.class);
        val authInfo = DataHelper.getAuthInfoInvalid();
        loginPage.invalidLogin(authInfo);
    }

    @Test
    void shouldTransferMoneyFromFirstToSecondMoreThanExist() {
        val loginPage = open("http://localhost:9999", LoginPage.class);
        val authInfo = DataHelper.getAuthInfoValid();
        val verificationPage = loginPage.validLogin(authInfo);
        val verificationCode = DataHelper.getVerificationCode();
        verificationPage.validVerify(verificationCode);

        val dahsboardPage = new DahsboardPage();
        int startBalanceOfFirstCard = dahsboardPage.getBalanceOfFirstCard();
        int startBalanceOfSecondCard = dahsboardPage.getBalanceOfSecondCard();

        val transferPage = dahsboardPage.replenishBalanceSecondCard();
        val transferFrom1To2Card = DataHelper.getFirstCardInfo();
        int transfer = 20000;
        transferPage.transfer(transferFrom1To2Card, transfer);
        val balanceFirstCardAfterTrans = DataHelper.getBalanceCardMinus(startBalanceOfFirstCard, transfer);
        val balanceSecondCardAfterTrans = DataHelper.getBalanceCardPlus(startBalanceOfSecondCard, transfer);

        assertEquals(balanceFirstCardAfterTrans, dahsboardPage.getBalanceOfFirstCard());
        assertEquals(balanceSecondCardAfterTrans, dahsboardPage.getBalanceOfSecondCard());
    }
}