package com.lendico.iban;


import com.neovisionaries.i18n.CountryCode;

import java.util.HashSet;
import java.util.Set;

/***
 * @author Malik Lalani
 */
public class IBANGenerator {
    // Append 00 for Calculation for Check Digits.
    private static final String DEFAULT_CHECK_DIGITS = "00";
    private static final int MOD = 97;
    private static final long MAX = 999999999;

    // Set to save IBAN History.
    private Set<String> IBANHistory = new HashSet<>();

    /***
     * Calculates Check Digits based on https://en.wikipedia.org/wiki/International_Bank_Account_Number.
     *
     * @param bban        The BBAN.
     * @param countryCode Country Code for IBAN.
     * @return String of Check Digits (Length 2).
     */
    public String calculateCheckDigits(String bban, CountryCode countryCode) {

        // Put Initial Four Characters to back of IBAN String.
        final String reversedIban = bban + countryCode.name() + DEFAULT_CHECK_DIGITS;

        long total = 0;
        for (int i = 0; i < reversedIban.length(); i++) {

            // Get the Numeric value
            int num = Character.getNumericValue(reversedIban.charAt(i));

            if (num > 9)
                total *= 100;
            else
                total *= 10;

            total += num;

            // Calculate subsequent MOD if Length is getting big to hold.
            if (total > MAX)
                total = (total % MOD);
        }

        Integer mod = (int) (total % MOD);
        Integer checkDigitIntValue = (98 - mod);
        String checkDigit = Integer.toString(checkDigitIntValue);
        // If its a single digit, then append 0.
        return checkDigitIntValue > 9 ? checkDigit : "0" + checkDigit;
    }

    /***
     * Check whether the generated IBAN already exists in history set.
     *
     * @param iban
     * @return True if IBAN already exists.
     */
    private synchronized boolean checkExistingIBAN(String iban) {
        if (IBANHistory.contains(iban)) {
            return true;
        } else {
            IBANHistory.add(iban);
            return false;
        }
    }

    /***
     * Generates IBAN for Given Country name.
     *
     * @param country Name of Country for IBAN (String).
     * @return IBAN String.
     */
    public String generateIBAN(String country) {
        System.out.println("Generating IBAN for " + country);

        // Get ENUM Country code.
        CountryCode countryCode;
        try {
            countryCode = CountryCode.findByName(country).get(0);
        } catch (IndexOutOfBoundsException ex) {
            System.out.println("Invalid Country: " + country);
            return null;
        }
        // The BBAN for given Country.
        String bban = BBANEntryFormat.getBBANforCountry(countryCode);
        // Check Digits for BBAN and Country Code.
        String checkDigits = calculateCheckDigits(bban, countryCode);

        // Concatenate Country Code + Check Digits + BBAN.
        String iban = countryCode.name() + checkDigits + bban;
        System.out.println("IBAN for " + country + " is " + iban);

        // Check for Duplication.
        if (checkExistingIBAN(iban)) {
            return generateIBAN(country);
        }

        return iban;
    }


    public static void main(String[] args) {
        IBANGenerator ibanGenerator = new IBANGenerator();
        System.out.println(ibanGenerator.generateIBAN("Austria"));
    }
}
