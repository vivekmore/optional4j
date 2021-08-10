package poptional.performance;

import gopt.Goptional;
import org.openjdk.jmh.annotations.*;
import org.openjdk.jmh.infra.Blackhole;
import poptional.Poptional;
import poptional.test.model.*;

import java.util.Optional;

import static poptional.performance.ModelUtil.YEAR_NOT_FOUND;
import static poptional.performance.ModelUtil.getCodeIfElse;

@State(Scope.Benchmark)
public class BasicProgram {

    private Order order;

//    @Param({"0", "1", "2", "3", "4", "5", "6"})
    @Param({"6"})
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
            Optional.ofNullable(null);
            Poptional.ofNullable(null);
            Goptional.fromNullable(null);
        }
    }


//    @Benchmark
//    public void noop() {
//    }
//
//    @Benchmark
//    public void java_ifElse(Blackhole blackhole) {
//        blackhole.consume(getCodeIfElse(order));
//    }

    @Benchmark
    public void poptional(Blackhole blackhole) {

        blackhole.consume(Poptional.ofNullable(order)
                .flatMap(Order::getCustomer)
                .flatMap(Customer::getAddress)
                .flatMap(Address::getCountry)
                .flatMap(Country::getIsoCode)
                .flatMap(IsoCode::getAlphaCode2)
                .flatMap(AlphaCode2::getYear)
                .orElse(YEAR_NOT_FOUND)
        );
    }
//
//    @Benchmark
//    public void java_optional(Blackhole blackhole) {
//
//        blackhole.consume(Optional.ofNullable(order)
//                .map(Order::getCustomerPlain)
//                .map(Customer::getAddressPlain)
//                .map(Address::getCountryPlain)
//                .map(Country::getIsoCodePlain)
//                .map(IsoCode::getCodePlain)
//                .map(AlphaCode2::getYearPlain)
//                .orElse(YEAR_NOT_FOUND)
//        );
//    }
//
//    @Benchmark
//    public void guava_optional(Blackhole blackhole) {
//
//        blackhole.consume(Goptional.fromNullable(order)
//                .transform(Order::getCustomerPlain)
//                .transform(Customer::getAddressPlain)
//                .transform(Address::getCountryPlain)
//                .transform(Country::getIsoCodePlain)
//                .transform(IsoCode::getCodePlain)
//                .transform(AlphaCode2::getYearPlain)
//                .or(YEAR_NOT_FOUND)
//        );
//    }
}