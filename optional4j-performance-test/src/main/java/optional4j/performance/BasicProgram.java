package optional4j.performance;

import gopt.Goptional;
import optional4j.spec.Optional;
import optional4j.test.model.Address;
import optional4j.test.model.AlphaCode2;
import optional4j.test.model.Country;
import optional4j.test.model.Customer;
import optional4j.test.model.IsoCode;
import optional4j.test.model.Order;
import optional4j.test.model.Year;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;

@State(Scope.Benchmark)
public class BasicProgram {

    private Order order;

    @Param({"0", "1", "2", "3", "4", "5", "6"})
    private String height;

    @Setup
    public void setUp() {

        switch (height) {
            case "0":
                order = null;
                break;
            case "1":
                order = new Order(null);
                break;
            case "2":
                order = new Order(new Customer(null));
                break;
            case "3":
                order = new Order(new Customer(new Address(null)));
                break;
            case "4":
                order = new Order(new Customer(new Address(new Country(null))));
                break;
            case "5":
                order = new Order(new Customer(new Address(new Country(new IsoCode(null)))));
                break;
            case "6":
                order = new Order(new Customer(new Address(new Country(new IsoCode(new AlphaCode2(null))))));
                break;
            case "7":
                order = new Order(new Customer(new Address(new Country(new IsoCode(new AlphaCode2(new Year(null)))))));
                break;
            default:
                throw new IllegalStateException("Unsupported filling.");
        }

        new Order(new Customer(new Address(new Country(new IsoCode(new AlphaCode2(new Year(5)))))));

        for (int i = 0; i < 1000; i++) {
            java.util.Optional.ofNullable(null);
            Optional.ofNullable(null);
            Goptional.fromNullable(null);
        }
    }

    @Benchmark
    public void java_ifElse(Blackhole blackhole) {
        blackhole.consume(ModelUtil.getCodeIfElse(order));
    }

    @Benchmark
    public void poptional(Blackhole blackhole) {

        Optional<Order> order = new Order();

        Order order1 = null;

        blackhole.consume(Optional.ofNullable(order)
                .flatMap(Order::getCustomer)
                .flatMap(Customer::getAddress)
                .flatMap(Address::getCountry)
                .flatMap(Country::getIsoCode)
                .flatMap(IsoCode::getAlphaCode2)
                .flatMap(AlphaCode2::getYear)
                .orElse(ModelUtil.YEAR_NOT_FOUND)
        );
    }

    @Benchmark
    public void nullObject(Blackhole blackhole) {

//        blackhole.consume(OrderNullObject.ofNullObject(order)
//                .getCustomer()
//                .getAddress()
//                .getCountry()
//                .getIsoCode()
//                .getAlphaCode2()
//                .getYear()
//                .orElse(YEAR_NOT_FOUND));
    }

    @Benchmark
    public void java_optional(Blackhole blackhole) {

        blackhole.consume(java.util.Optional.ofNullable(order)
                .map(Order::getCustomerPlain)
                .map(Customer::getAddressPlain)
                .map(Address::getCountryPlain)
                .map(Country::getIsoCodePlain)
                .map(IsoCode::getCodePlain)
                .map(AlphaCode2::getYearPlain)
                .orElse(ModelUtil.YEAR_NOT_FOUND)
        );
    }

    @Benchmark
    public void guava_optional(Blackhole blackhole) {

        blackhole.consume(Goptional.fromNullable(order)
                .transform(Order::getCustomerPlain)
                .transform(Customer::getAddressPlain)
                .transform(Address::getCountryPlain)
                .transform(Country::getIsoCodePlain)
                .transform(IsoCode::getCodePlain)
                .transform(AlphaCode2::getYearPlain)
                .or(ModelUtil.YEAR_NOT_FOUND)
        );
    }
}