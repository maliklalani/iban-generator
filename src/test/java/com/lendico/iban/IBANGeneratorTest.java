package com.lendico.iban;

import com.neovisionaries.i18n.CountryCode;
import org.junit.Assert;
import org.junit.Test;

import java.math.BigDecimal;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.TimeUnit;

public class IBANGeneratorTest {
    @Test
    public void testCalculateCheckDigits() {
        IBANGenerator ibanGenerator = new IBANGenerator();

        // Get ENUM Country code.
        CountryCode countryCode = CountryCode.findByName("Austria").get(0);
        // The BBAN for given Country.
        String bban = "1904 3002 3457 3201".replace(" ", "");
        // Check Digits for BBAN and Country Code.
        String checkDigits = ibanGenerator.calculateCheckDigits(bban, countryCode);
        // Compare.
        Assert.assertEquals(checkDigits, "61");

        // Get ENUM Country code.
        countryCode = CountryCode.findByName("Germany").get(0);
        // The BBAN for given Country.
        bban = "3704 0044 0532 0130 00".replace(" ", "");
        // Check Digits for BBAN and Country Code.
        checkDigits = ibanGenerator.calculateCheckDigits(bban, countryCode);
        // Compare.
        Assert.assertEquals(checkDigits, "89");

        // Get ENUM Country code.
        countryCode = CountryCode.findByName("Netherlands").get(0);
        // The BBAN for given Country.
        bban = "ABNA 0417 1643 00".replace(" ", "");
        // Check Digits for BBAN and Country Code.
        checkDigits = ibanGenerator.calculateCheckDigits(bban, countryCode);
        // Compare.
        Assert.assertEquals(checkDigits, "91");
    }

    private Boolean validateIBAN(String iban) {

        if (iban == null)
            return false;

        // Integer String of IBAN.
        String intIBAN = "";
        // First 4 chars.
        String init4Chars = iban.substring(0, 4);
        // First 4 chars concatenated in the end.
        iban = iban.substring(4) + init4Chars;

        for (int i = 0; i < iban.length(); i++) {
            intIBAN += Character.getNumericValue(iban.charAt(i));
        }

        BigDecimal ibanIntValue = new BigDecimal(intIBAN);
        if ((ibanIntValue.remainder(new BigDecimal(97))).intValue() == 1)
            return true;
        else
            return false;

    }

    @Test
    public void testGenerateIBAN() {
        IBANGenerator ibanGenerator = new IBANGenerator();
        String deIban = ibanGenerator.generateIBAN("Germany");
        Assert.assertEquals(validateIBAN(deIban), true);

        String atIban = ibanGenerator.generateIBAN("Austria");
        Assert.assertEquals(validateIBAN(atIban), true);

        String nlIban = ibanGenerator.generateIBAN("Netherlands");
        Assert.assertEquals(validateIBAN(nlIban), true);

        String nullIban = ibanGenerator.generateIBAN("Dummy");
        Assert.assertEquals(validateIBAN(nullIban), false);
    }

    /***
     * Testing Parallel calculations for IBAN.
     */
    @Test
    public void testGenerateIBANThreaded() {
        IBANGenerator ibanGenerator = new IBANGenerator();
        ExecutorService executorService = Executors.newFixedThreadPool(20);

        executorService.execute(() ->
                Assert.assertEquals(validateIBAN(ibanGenerator.generateIBAN("Netherlands")), true));
        executorService.execute(() ->
                Assert.assertEquals(validateIBAN(ibanGenerator.generateIBAN("Netherlands")), true));
        executorService.execute(() ->
                Assert.assertEquals(validateIBAN(ibanGenerator.generateIBAN("Netherlands")), true));
        executorService.execute(() ->
                Assert.assertEquals(validateIBAN(ibanGenerator.generateIBAN("Netherlands")), true));
        executorService.execute(() ->
                Assert.assertEquals(validateIBAN(ibanGenerator.generateIBAN("Netherlands")), true));

        executorService.execute(() ->
                Assert.assertEquals(validateIBAN(ibanGenerator.generateIBAN("Germany")), true));
        executorService.execute(() ->
                Assert.assertEquals(validateIBAN(ibanGenerator.generateIBAN("Germany")), true));
        executorService.execute(() ->
                Assert.assertEquals(validateIBAN(ibanGenerator.generateIBAN("Germany")), true));
        executorService.execute(() ->
                Assert.assertEquals(validateIBAN(ibanGenerator.generateIBAN("Germany")), true));
        executorService.execute(() ->
                Assert.assertEquals(validateIBAN(ibanGenerator.generateIBAN("Germany")), true));

        executorService.execute(() ->
                Assert.assertEquals(validateIBAN(ibanGenerator.generateIBAN("Austria")), true));
        executorService.execute(() ->
                Assert.assertEquals(validateIBAN(ibanGenerator.generateIBAN("Austria")), true));
        executorService.execute(() ->
                Assert.assertEquals(validateIBAN(ibanGenerator.generateIBAN("Austria")), true));
        executorService.execute(() ->
                Assert.assertEquals(validateIBAN(ibanGenerator.generateIBAN("Austria")), true));
        executorService.execute(() ->
                Assert.assertEquals(validateIBAN(ibanGenerator.generateIBAN("Austria")), true));

        executorService.shutdown();
        try {
            executorService.awaitTermination(Long.MAX_VALUE, TimeUnit.NANOSECONDS);
        } catch (InterruptedException e) {
            System.out.println(e.getMessage());
        }
    }
}
