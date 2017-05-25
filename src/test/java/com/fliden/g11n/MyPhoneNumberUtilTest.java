package com.fliden.g11n;

import com.google.i18n.phonenumbers.AsYouTypeFormatter;
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber;
import org.junit.Assert;
import org.junit.Test;

import java.util.Locale;
import java.util.Set;

/**
 * Unit test for simple App.
 */
public class MyPhoneNumberUtilTest {

    @Test
    public void getRegionCodeForCountryCode() throws Exception {
        Assert.assertEquals("US", MyPhoneNumberUtil.getRegionCodeForCountryCode(1));
    }

    @Test
    public void getCountryCodeForRegion() throws Exception {
        Assert.assertEquals(1, MyPhoneNumberUtil.getCountryCodeForRegion("US"));
    }

    @Test
    public void isValidPhoneNumber() throws Exception {
        Assert.assertTrue(MyPhoneNumberUtil.isValidPhoneNumber("+14085578117"));
        Assert.assertFalse(MyPhoneNumberUtil.isValidPhoneNumber("+140855781170"));
    }

    @Test
    public void isValidPhoneNumber1() throws Exception {
        Assert.assertTrue(MyPhoneNumberUtil.isValidPhoneNumber("4085578117", "US"));
        Assert.assertFalse(MyPhoneNumberUtil.isValidPhoneNumber("40855781170", "US"));
    }

    @Test
    public void isValidPhoneNumber2() throws Exception {
        Assert.assertTrue(MyPhoneNumberUtil.isValidPhoneNumber("4085578117", 1));
        Assert.assertFalse(MyPhoneNumberUtil.isValidPhoneNumber("40855781170", 1));
    }

    @Test
    public void formatPhoneNumber() throws Exception {
        Assert.assertEquals("+1 408-557-8117", MyPhoneNumberUtil.formatPhoneNumber("+14085578117"));
    }

    @Test
    public void formatPhoneNumber1() throws Exception {
        Assert.assertEquals("+1 408-557-8117", MyPhoneNumberUtil.formatPhoneNumber("+14085578117", MyPhoneNumberUtil.PhoneNumFormat.INTERNATIONAL));
        Assert.assertEquals("(408) 557-8117", MyPhoneNumberUtil.formatPhoneNumber("+14085578117", MyPhoneNumberUtil.PhoneNumFormat.NATIONAL));
    }

    @Test
    public void splitPhoneNumberUS() throws Exception {
        String parts[] = MyPhoneNumberUtil.splitPhoneNumber("+14085578117");
        Assert.assertEquals("408", parts[0]);
        Assert.assertEquals("5578117", parts[1]);
    }

    @Test
    public void splitPhoneNumberSWE() throws Exception {
        String parts[] = MyPhoneNumberUtil.splitPhoneNumber("+4640434343");
        Assert.assertEquals("040", parts[0]);
        Assert.assertEquals("434343", parts[1]);
    }

    @Test
    public void printExampleNumbers() {
        PhoneNumberUtil util = PhoneNumberUtil.getInstance();
        Set<String> regions = util.getSupportedRegions();
        for (String region : regions) {

            Phonenumber.PhoneNumber exampleNumber  = util.getExampleNumber(region);

            Locale l = new Locale("", region);
            String iso3 = "N/A";
            try {
                iso3 = l.getISO3Country();
            }catch (Exception e) {}

            for (PhoneNumberUtil.PhoneNumberType t : PhoneNumberUtil.PhoneNumberType.values()) {
                Phonenumber.PhoneNumber num = util.getExampleNumberForType(region, t);
                if (num != null)
                    System.out.println(region + "|"
                                    + iso3 + "|"
                                    + l.getCountry() + "|"
                                    + l.getDisplayCountry() + "|"
                                    + util.getCountryCodeForRegion(region) + "|"
                                    + t.name() + "|"
                                    + util.format(num, PhoneNumberUtil.PhoneNumberFormat.NATIONAL) + "|"
                                    + util.format(num, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL)
                    );
            }
        }
    }

    @Test
    public void printRegions(){
        System.out.println("Supported regions: ");
        Set<String> regions = MyPhoneNumberUtil.getSupportedRegions();
        for(String region : regions){
            Locale l = new Locale("", region);
            System.out.println(region + " -> " + l.getDisplayCountry());
        }
    }

    @Test
    public void testRawLib() {
        String swedishNumberStr = "+4604087654";

        PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
        try {
            Phonenumber.PhoneNumber swedishNumberProto = phoneUtil.parse(swedishNumberStr, "US");
            System.out.println("Is valid number: " + phoneUtil.isValidNumber(swedishNumberProto));
            System.out.println("Country code: " + swedishNumberProto.getCountryCode());
            System.out.println(phoneUtil.getRegionCodeForNumber(swedishNumberProto));
            System.out.println("Get Region from country code: " + phoneUtil.getRegionCodeForCountryCode(46));
            System.out.println("International number format: " + phoneUtil.format(swedishNumberProto, PhoneNumberUtil.PhoneNumberFormat.INTERNATIONAL));

            AsYouTypeFormatter formatter = phoneUtil.getAsYouTypeFormatter("SE");
            System.out.println(formatter.inputDigit('0'));
            System.out.println(formatter.inputDigit('4'));
            System.out.println(formatter.inputDigit('0'));
            System.out.println(formatter.inputDigit('8'));
            System.out.println(formatter.inputDigit('7'));
            System.out.println(formatter.inputDigit('4'));
            System.out.println(formatter.inputDigit('3'));
            System.out.println(formatter.inputDigit('1'));

        } catch (NumberParseException e) {
            System.err.println("NumberParseException was thrown: " + e.toString());
        }

        System.out.println("@Test - testEmptyCollection");
    }}
