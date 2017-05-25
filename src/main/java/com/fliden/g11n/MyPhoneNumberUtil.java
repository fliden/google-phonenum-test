package com.fliden.g11n;

import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;

import java.util.Set;

import static com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat.E164;
import static com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat.RFC3966;

public class MyPhoneNumberUtil {

    public static void main( String[] args ) {
        System.out.println( "Check the test instead" );
    }

    public static enum PhoneNumFormat {
        E164, INTERNATIONAL, NATIONAL, RFC3966;
    }

    /**
     * Return the Region from a Country Calling code
     * @param code
     * @return
     */
    public static String getRegionCodeForCountryCode(int code) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        return phoneUtil.getRegionCodeForCountryCode(code);
    }

    /**
     * Return the Country Calling Code from a Region
     * @param region
     * @return
     */
    public static int getCountryCodeForRegion(String region) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        return phoneUtil.getCountryCodeForRegion(region);
    }

    /**
     * Validate a phone number
     * @param num Phone number to validate. Need to start with + and country code else it defaults to the US.
     * @return
     */
    public static boolean isValidPhoneNumber(String num) {
        Phonenumber.PhoneNumber number = parsePhoneNumber(num, "US");
        if (number == null) return false;
        return PhoneNumberUtil.getInstance().isValidNumber(number);
    }

    /**
     * Validate a phone number of a specific region
     * @param num Phone number to validate. Does not need to start with + if regions is provided.
     * @param region Default region of phone number.
     * @return
     */
    public static boolean isValidPhoneNumber(String num, String region) {
        Phonenumber.PhoneNumber number = parsePhoneNumber(num, region);
        if (number == null) return false;
        return PhoneNumberUtil.getInstance().isValidNumber(number);
    }

    /**
     * Validate a phone number of a specific country calling code.
     * @param num Phone number to validate. Does not need to start with + if country calling code is provided.
     * @param code Country calling code. Maps internally to a region.
     * @return
     */
    public static boolean isValidPhoneNumber(String num, int code) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber number = parsePhoneNumber(num, phoneUtil.getRegionCodeForCountryCode(code));
        if (number == null) return false;
        return phoneUtil.isValidNumber(number);
    }

    /**
     * Format a phone number using INTERNATIONAL format
     * @param num Phone number to format. Need to start with + and country code.
     * @return
     */
    public static String formatPhoneNumber(String num) {
        Phonenumber.PhoneNumber number = parsePhoneNumber(num, "US");
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        if (number == null) return null;
        return phoneUtil.format(number, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL);
    }

    /**
     * Format a phone number using specified format
     * @param num Phone number to format. Need to start with + and country code.
     * @return
     */
    public static String formatPhoneNumber(String num, PhoneNumFormat format) {
        Phonenumber.PhoneNumber number = parsePhoneNumber(num, "US");
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        if (number == null) return null;
        return phoneUtil.format(number, getGoogleFormat(format));
    }

    public static String[] splitPhoneNumber(String num) {
        return splitPhoneNumber(num, true);
    }

    public static String[] splitPhoneNumber(String num, boolean localAreaCode) {
        String parts[] = new String[2];
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        Phonenumber.PhoneNumber number = parsePhoneNumber(num, "US");
        String nationalSignificantNumber = phoneUtil.getNationalSignificantNumber(number);
        int nationalDestinationCodeLength = phoneUtil.getLengthOfNationalDestinationCode(number);

        //System.out.println("getLengthOfGraphicalAreaCode: " + phoneUtil.getLengthOfGeographicalAreaCode(number));

        if (nationalDestinationCodeLength > 0) {
            parts[0] =  nationalSignificantNumber.substring(0, nationalDestinationCodeLength);
            parts[1] =  nationalSignificantNumber.substring(nationalDestinationCodeLength, nationalSignificantNumber.length());

            // check if we need extra digit
            if (localAreaCode) {
                String formattedNationalNumber = phoneUtil.format(number, PhoneNumberUtil.PhoneNumberFormat.NATIONAL);
                String plainNationalNumberFormatPlain = formattedNationalNumber.replaceAll("[^0-9]", "");
                String localAreaCodePortion = plainNationalNumberFormatPlain.replace(nationalSignificantNumber, "");
                parts[0] = localAreaCodePortion + parts[0];
            }
        }
        return parts;
    }

    public static Set<String> getSupportedRegions() {
        return PhoneNumberUtil.getInstance().getSupportedRegions();
    }


    private static Phonenumber.PhoneNumber parsePhoneNumber(String num, String region) {
        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber phoneNumber = phoneUtil.parse(num, region);
            return phoneNumber;
        } catch (NumberParseException e) {
            return null;
        }
    }

    private static PhoneNumberUtil.PhoneNumberFormat getGoogleFormat(PhoneNumFormat format) {
        switch (format) {
            case INTERNATIONAL: return PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL;
            case NATIONAL: return PhoneNumberUtil.PhoneNumberFormat.NATIONAL;
            case E164: return E164;
            case RFC3966: return RFC3966;
        }
        return null;
    }

}
