package poptional.performance;

import lombok.experimental.UtilityClass;
import poptional.test.model.*;

import java.util.Random;
import java.util.UUID;

@UtilityClass
public class Util {

    public static final Random RANDOM = new Random(System.currentTimeMillis());
    public static final int BOUND = 100;
    public static final double NULL_PROB = 0.1;

    public static Order[] createOrders(int size) {
        Order[] orders = new Order[size];
        for (int i = 0; i < size; i++) {
            orders[i] = newOrder(NULL_PROB);
        }
        return orders;
    }

    public static Order newOrder(double nullProbability) {

        if (nullProbability > 0 && RANDOM.nextInt(BOUND) < (BOUND * NULL_PROB)) {
            return null;
        }

        Order order = new Order();
        order.setCustomer(newCustomer(nullProbability));
        return order;
    }

    public static Customer newCustomer(double nullProbability) {


        if (nullProbability > 0 && RANDOM.nextInt(BOUND) < (BOUND * NULL_PROB)) {
            return null;
        }

        Customer customer = new Customer();
        customer.setAddress(newAddress(nullProbability));
        return customer;
    }

    public static Address newAddress(double nullProbability) {

        if (nullProbability > 0 && RANDOM.nextInt(BOUND) < (BOUND * NULL_PROB)) {
            return null;
        }

        Address address = new Address();
        address.setCountry(newCountry(nullProbability));
        return address;
    }

    public static Country newCountry(double nullProbability) {

        if (nullProbability > 0 && RANDOM.nextInt(BOUND) < (BOUND * NULL_PROB)) {
            return null;
        }

        Country country = new Country();
        country.setIsoCode(newIsoCode(nullProbability));
        return country;
    }

    public static IsoCode newIsoCode(double nullProbability) {

        if (nullProbability > 0 && RANDOM.nextInt(BOUND) < (BOUND * NULL_PROB)) {
            return null;
        }

        IsoCode isoCode = new IsoCode();
        isoCode.setCode(newCode(nullProbability));
        return isoCode;
    }

    public static Code newCode(double nullProbability) {

        if (nullProbability > 0 && RANDOM.nextInt(BOUND) < (BOUND * NULL_PROB)) {
            return null;
        }

        Code code = new Code();
        code.setCode(RANDOM.nextInt(99999));
        return code;
    }
}
