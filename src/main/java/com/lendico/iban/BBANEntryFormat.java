package com.lendico.iban;

import com.neovisionaries.i18n.CountryCode;

import java.util.EnumMap;

/***
 * Defines BBAN EntryFormat  - Collection of multiple BBAN Entries.
 */
public class BBANEntryFormat {
    // Array of BBAN Entries in order.
    private final BBANEntry[] bbanEntries;

    // Map - Key: CountryCodes, Value: BBANEntryFormat.
    // Need to define EntryFormat hard-code.
    private static final EnumMap<CountryCode, BBANEntryFormat> countryBbans;

    /***
     * Getter for BBAN Entries.
     *
     * @return BBANEntry[]
     */
    public BBANEntry[] getBbanEntries() {
        return bbanEntries;
    }

    /***
     * Constructor
     *
     * @param bbanEntries BBANEntry[]
     */
    private BBANEntryFormat(BBANEntry... bbanEntries) {
        this.bbanEntries = bbanEntries;
    }


    static {
        countryBbans = new EnumMap<>(CountryCode.class);

        // Austria -
        countryBbans.put(CountryCode.AT, new BBANEntryFormat(new BBANEntry(16, BBANEntryType.N)));
        // Germany -
        countryBbans.put(CountryCode.DE, new BBANEntryFormat(new BBANEntry(18, BBANEntryType.N)));
        // Netherlands -
        countryBbans.put(CountryCode.NL, new BBANEntryFormat(new BBANEntry(4, BBANEntryType.A),
                new BBANEntry(10, BBANEntryType.N)));
    }

    /***
     * Generates BBAN based on Entries.
     *
     * @param countryCode Country Code Enum.
     * @return String of BBAN.
     */
    public static String getBBANforCountry(CountryCode countryCode) {
        BBANEntryFormat format = countryBbans.get(countryCode);
        BBANEntry[] entries = format.getBbanEntries();
        StringBuffer bban = new StringBuffer();
        for (BBANEntry bbanEntry : entries) {
            bban.append(bbanEntry.getRandomString());
        }

        return bban.toString();
    }
}
