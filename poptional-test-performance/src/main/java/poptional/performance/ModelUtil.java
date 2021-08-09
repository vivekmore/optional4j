package poptional.performance;

import poptional.Poptional;
import poptional.test.model.*;

import java.util.Optional;
import java.util.Random;

public class ModelUtil {

    static final Order DEFAULT_ORDER = Util.newOrder(0);
    static final Customer DEFAULT_CUSTOMER = Util.newCustomer(0);
    static final Address DEFAULT_ADDRESS = Util.newAddress(0);
    static final Country DEFAULT_COUNTRY = Util.newCountry(0);
    static final IsoCode DEFAULT_ISO_CODE = Util.newIsoCode(0);
    static final Code DEFAULT_CODE = new Code(new Random(System.currentTimeMillis()).nextInt());


    public static Object getCodeIfElse(Order order) {
        if (order == null) {
            return DEFAULT_CODE;
        }

        Customer customer = order.getCustomerPlain();
        if (customer == null) {
            return DEFAULT_CODE;
        }

        Address address = customer.getAddressPlain();
        if (address == null) {
            return DEFAULT_CODE;
        }

        Country country = address.getCountryPlain();
        if (country == null) {
            return DEFAULT_CODE;
        }

        IsoCode isoCode = country.getIsoCodePlain();
        if (isoCode == null) {
            return DEFAULT_CODE;
        }

        Code code = isoCode.getCodePlain();
        if (code == null) {
            return DEFAULT_CODE;
        }

        return code;
    }
}
