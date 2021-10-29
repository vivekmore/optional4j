package optional4j.performance;

import optional4j.test.model.Address;
import optional4j.test.model.AlphaCode2;
import optional4j.test.model.Country;
import optional4j.test.model.Customer;
import optional4j.test.model.IsoCode;
import optional4j.test.model.Order;
import optional4j.test.model.Year;
import optional4j.test.model.*;

public class ModelUtil {

    static final Year YEAR_NOT_FOUND = new Year(0);

    public static Object getCodeIfElse(Order order) {

        if (order == null) {
            return YEAR_NOT_FOUND;
        }

        Customer customer = order.getCustomerPlain();
        if (customer == null) {
            return YEAR_NOT_FOUND;
        }

        Address address = customer.getAddressPlain();
        if (address == null) {
            return YEAR_NOT_FOUND;
        }

        Country country = address.getCountryPlain();
        if (country == null) {
            return YEAR_NOT_FOUND;
        }

        IsoCode isoCode = country.getIsoCodePlain();
        if (isoCode == null) {
            return YEAR_NOT_FOUND;
        }

        AlphaCode2 code = isoCode.getCodePlain();
        if (code == null) {
            return YEAR_NOT_FOUND;
        }

        Year year = code.getYearPlain();
        if (year == null) {
            return YEAR_NOT_FOUND;
        }

        return year;
    }
}
