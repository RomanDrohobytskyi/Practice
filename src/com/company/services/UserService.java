package com.company.services;

import com.company.entities.Bank;
import com.company.entities.Loan;
import com.company.entities.User;
import com.company.enums.Gender;

import java.math.BigDecimal;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static java.util.stream.Collectors.groupingBy;
import static java.util.stream.Collectors.toList;
import static java.util.stream.Collectors.toMap;

public class UserService {

    /**
     * 1) погрупуй юзерів по віку
     */
    public Map<Integer, List<User>> groupByAge(List<User> users){
        return  users == null
                ? Collections.emptyMap()
                : users
                    .stream()
                    .collect(groupingBy(User::getAge));
    }

    /**
     *  2) отримай список повнолітніх юзерів з непогашеними кредитами
     */
    public List<User> getAdultUsersWithDoneLoans(List<User> users){
        return users == null
                ? Collections.emptyList()
                : users
                    .stream()
                    .filter(user -> user.getAge() >= 18
                            && user.getLoans()
                        .stream()
                        .anyMatch(loan -> loan.getDue().after(new Date())))
                    .collect(toList());
    }

    /**
     * 3) отримай ліст неповнолітніх хлопців з погашеними кредитами
     */
    public List<User> getAdultBoysWithDoneLoans(List<User> users){
        return users.stream()
                .filter(user -> user.getAge() >= 18
                        && user.getGender().getId() == Gender.MALE.getId()
                        && user.getLoans()
                    .stream()
                    .anyMatch(loan -> loan.getDue().before(new Date())))
                .collect(toList());
    }

    /**
     * 4) погрупуй юзерів з непогашеними кредитами по банку => Map<Bank, List<User>>
     */
    public Map<Bank, List<User>> groupUsersByBankWithDoneLoans(List<User> users){
        return users.stream()
                .flatMap (user -> user.getLoans()
                        .stream()
                        .filter(loan -> loan.getDue().after(new Date()))
                        .flatMap(loan ->  Stream.of (
                        new AbstractMap.SimpleEntry<>(loan.getBank(),user))))
                .collect(
                        Collectors.groupingBy(Map.Entry::getKey,
                            Collectors.mapping(Map.Entry::getValue,
                            Collectors.toList())));
    }

    /**
     *  5) погрупуй погашені кредити по банку => Map<Bank, List<Loan>>
     */
    public Map<Bank, List<Loan>> groupDoneLoansByBank(List<User> users){
        return users.stream()
                .flatMap (user -> user.getLoans()
                        .stream()
                        .filter(loan -> loan.getDue().before(new Date()))
                        .flatMap(loan ->  Stream.of (
                                new AbstractMap.SimpleEntry<>(loan.getBank(),loan))))
                .collect(
                        Collectors.groupingBy(Map.Entry::getKey,
                                Collectors.mapping(Map.Entry::getValue,
                                        Collectors.toList())));
    }

    /**
     * 6) Отримай список унікальних імен жінок котрі погашені кредити в Mono банку
     */
    public List<String> uniqueFemaleNamesWithDoneLoans(List<User> users){
        return users.stream()
                .filter(user -> user.getGender().getId() == Gender.FEMALE.getId())
                .filter(user ->  user.getLoans()
                    .stream()
                    .anyMatch(loan -> loan.getDue().before(new Date())
                            && loan.getBank().getId() == 2L))
                .map(User::getName)
                .distinct()
                .collect(toList());
    }

    /**
     * 7) Сформуй мапу де ключем буде імя юзера а значенням середній розмір заборгованості
     */
    public Map<String, Double> usersAverageLoanAmount(List<User> users){
        return
            users
                .stream()
                .collect(toMap(User::getName,
                    user ->  user.getLoans()
                        .stream()
                        .mapToDouble(loan -> loan.getAmount().doubleValue())
                        .average().getAsDouble(), (a1, a2) -> a2)
            );
    }

    /**
     * 8) Сформуй мапу де ключем буде імя юзера а значенням топ 3 найбільших кредитів
     */
    public Map<String, List<Loan>> usersWithBiggestThreeLoansAmount(List<User> users){
        return
            users
                .stream()
                .collect(toMap(User::getName,
                        user -> user.getLoans()
                                .stream()
                                .sorted(Comparator.comparing(Loan::getAmount).reversed())
                                .limit(3)
                                .collect(toList()), (a1, a2) -> a2)
                    );
    }

    /**
     * 9) Сформуй мапу де ключем буде імя юзера а значенням топ 3 кредитів з найбільшою заборгованістю
     */
    public Map<String, List<Loan>> usersWithBiggestThreeLoansAmountAndBalance(List<User> users){
        return
            users
                .stream()
                .collect(toMap(User::getName,
                        user -> user.getLoans()
                                .stream()
                                .sorted(Comparator.comparingInt(o ->
                                        (o.getAmount().intValue() - o.getBalance().intValue())))
                                .limit(3)
                                .collect(toList()), (a1, a2) -> a2)
                );
    }

    /**
     *
     * @return - List<User> list of handmade users
     */
    public List<User> createListOfRandomUsers(){
        List<User> users = new ArrayList<>();
        Bank privat = new Bank(1L, "Privat Bank");
        Bank mono = new Bank(2L, "Mono Bank");
        Bank revolute = new Bank(3L, "Revolute Bank");
        Bank nbu = new Bank(4L, "NBU Bank");
        Bank oshchad = new Bank(5L, "Oshchad Band");

        /* ******************************************************************************************************************************************** */
        List<Loan> loans1 = new ArrayList<>();
        loans1.add(new Loan(1L, new GregorianCalendar(2014, 2, 11).getTime(),
                new GregorianCalendar(2015, 3, 20).getTime(), new BigDecimal(1800), new BigDecimal(5000), privat));

        loans1.add(new Loan(2L,  new GregorianCalendar(2018, 10, 23).getTime(),
                new GregorianCalendar(2019, 10, 23).getTime(), new BigDecimal(3800), new BigDecimal(6000), mono));

        loans1.add(new Loan(3L, new GregorianCalendar(2020, 1, 30).getTime(),
                new GregorianCalendar(2021, 12, 31).getTime(), new BigDecimal(4400), new BigDecimal(100), privat));

        loans1.add(new Loan(4L, new GregorianCalendar(2020, 2, 25).getTime(),
                new GregorianCalendar(2020, 2, 28).getTime(), new BigDecimal(350), new BigDecimal(250), privat ));

        loans1.add(new Loan(5L, new GregorianCalendar(2014, 2, 5).getTime(),
                new GregorianCalendar(2014, 10, 10).getTime(), new BigDecimal(8900), new BigDecimal(0), privat ));

        users.add(new User(1L, 18, "Volodymyr Petrovych", Gender.MALE, new BigDecimal(3000),loans1));

        /* ******************************************************************************************************************************************** */
        /* *****************************All past dates************************************************************************************************* */
        List<Loan> loans2 = new ArrayList<>();
        loans2.add(new Loan(6L, new GregorianCalendar(2013, 2, 16).getTime(),
                new GregorianCalendar(2015, 3, 20).getTime(), new BigDecimal(123), new BigDecimal(0), revolute));

        loans2.add(new Loan(7L,  new GregorianCalendar(2017, 1, 2).getTime(),
                new GregorianCalendar(2018, 10, 23).getTime(), new BigDecimal(3800), new BigDecimal(4222), nbu));

        users.add(new User(2L, 14, "Petro Oleksandrowych", Gender.MALE, new BigDecimal(1700),loans2));

        /* ******************************************************************************************************************************************** */
        List<Loan> loans3 = new ArrayList<>();

        loans3.add(new Loan(9L, new GregorianCalendar(2014, 5, 16).getTime(),
                new GregorianCalendar(2015, 3, 20).getTime(), new BigDecimal(552), new BigDecimal(2133), oshchad));

        loans3.add(new Loan(10L,  new GregorianCalendar(2016, 10, 14).getTime(),
                new GregorianCalendar(2019, 10, 23).getTime(), new BigDecimal(345), new BigDecimal(6000), oshchad));

        loans3.add(new Loan(11L, new GregorianCalendar(2020, 1, 30).getTime(),
                new GregorianCalendar(2021, 12, 31).getTime(), new BigDecimal(400), new BigDecimal(342), privat));

        loans3.add(new Loan(12L, new GregorianCalendar(2020, 2, 25).getTime(),
                new GregorianCalendar(2020, 2, 28).getTime(), new BigDecimal(30), new BigDecimal(23444), nbu));

        loans3.add(new Loan(13L, new GregorianCalendar(2014, 2, 5).getTime(),
                new GregorianCalendar(2014, 10, 10).getTime(), new BigDecimal(8900), new BigDecimal(9000), mono));

        users.add(new User(4L, 14, "Oleksandra Adnriivna", Gender.FEMALE, new BigDecimal(6405),loans3));

        /* ******************************************************************************************************************************************** */
        List<Loan> loans4 = new ArrayList<>();

        loans4.add(new Loan(14L, new GregorianCalendar(2014, 2, 21).getTime(),
                new GregorianCalendar(2015, 3, 20).getTime(), new BigDecimal(0), new BigDecimal(5000), mono));

        loans4.add(new Loan(15L,  new GregorianCalendar(2018, 10, 16).getTime(),
                new GregorianCalendar(2019, 10, 23).getTime(), new BigDecimal(3800), new BigDecimal(6000), oshchad));

        loans4.add(new Loan(16L, new GregorianCalendar(2020, 1, 30).getTime(),
                new GregorianCalendar(2021, 12, 31).getTime(), new BigDecimal(0), new BigDecimal(0), revolute));

        users.add(new User(3L, 23, "Dmytro Viktorovych", Gender.MALE, new BigDecimal(500),loans4));

        /* ******************************************************************************************************************************************** */
        List<Loan> loans5 = new ArrayList<>();

        loans5.add(new Loan(17L, new GregorianCalendar(2014, 2, 22).getTime(),
                new GregorianCalendar(2015, 3, 20).getTime(), new BigDecimal(1800), new BigDecimal(0), oshchad));

        loans5.add(new Loan(18L,  new GregorianCalendar(2018, 10, 17).getTime(),
                new GregorianCalendar(2019, 10, 23).getTime(), new BigDecimal(233), new BigDecimal(6000), oshchad));

        loans5.add(new Loan(19L, new GregorianCalendar(2020, 1, 30).getTime(),
                new GregorianCalendar(2021, 12, 31).getTime(), new BigDecimal(400), new BigDecimal(100), privat));

        loans5.add(new Loan(20L, new GregorianCalendar(2020, 2, 25).getTime(),
                new GregorianCalendar(2020, 2, 28).getTime(), new BigDecimal(30), new BigDecimal(2520), nbu));

        loans5.add(new Loan(21L, new GregorianCalendar(2014, 2, 5).getTime(),
                new GregorianCalendar(2014, 10, 10).getTime(), new BigDecimal(8900), new BigDecimal(9040), mono));

        users.add(new User(5L, 75, "Viktoria Olehivna", Gender.FEMALE, new BigDecimal(1005),loans5));

        /* ******************************************************************************************************************************************** */
        List<Loan> loans6 = new ArrayList<>();

        loans6.add(new Loan(22L, new GregorianCalendar(2014, 2, 23).getTime(),
                new GregorianCalendar(2015, 3, 20).getTime(), new BigDecimal(1800), new BigDecimal(5000), oshchad));

        loans6.add(new Loan(23L,  new GregorianCalendar(2018, 10, 18).getTime(),
                new GregorianCalendar(2019, 10, 23).getTime(), new BigDecimal(3800), new BigDecimal(6000), oshchad));

        loans6.add(new Loan(24L, new GregorianCalendar(2020, 1, 30).getTime(),
                new GregorianCalendar(2021, 12, 31).getTime(), new BigDecimal(400), new BigDecimal(100), privat));

        loans6.add(new Loan(25L, new GregorianCalendar(2020, 2, 25).getTime(),
                new GregorianCalendar(2020, 2, 28).getTime(), new BigDecimal(30), new BigDecimal(250), nbu));

        loans6.add(new Loan(26L, new GregorianCalendar(2014, 2, 5).getTime(),
                new GregorianCalendar(2014, 10, 10).getTime(), new BigDecimal(8900), new BigDecimal(9000), mono));

        users.add(new User(6L, 23, "Olga Yaroslavivna", Gender.FEMALE, new BigDecimal(350),loans6));

        /* ******************************************************************************************************************************************** */
        /* **************************************All future dates ************************************************************************************* */
        List<Loan> loans7 = new ArrayList<>();

        loans7.add(new Loan(27L, new GregorianCalendar(2022, 2, 24).getTime(),
                new GregorianCalendar(2032, 3, 20).getTime(), new BigDecimal(0), new BigDecimal(5000), mono));

        loans7.add(new Loan(28L,  new GregorianCalendar(2021, 10, 10).getTime(),
                new GregorianCalendar(2022, 4, 23).getTime(), new BigDecimal(3800), new BigDecimal(6000), oshchad));

        loans7.add(new Loan(29L, new GregorianCalendar(2020, 1, 30).getTime(),
                new GregorianCalendar(2021, 12, 19).getTime(), new BigDecimal(0), new BigDecimal(0), revolute));

        users.add(new User(7L, 75, "Mykola Orestovych", Gender.MALE, new BigDecimal(6700), loans7));

        /* ******************************************************************************************************************************************** */
        /* *******************************************Non unique user name***************************************************************************** */
        List<Loan> loans8 = new ArrayList<>();

        loans8.add(new Loan(30L, new GregorianCalendar(2011, 5, 1).getTime(),
                new GregorianCalendar(2014, 3, 2).getTime(), new BigDecimal(152), new BigDecimal(2133), oshchad));

        loans8.add(new Loan(31L,  new GregorianCalendar(2012, 1, 4).getTime(),
                new GregorianCalendar(2019, 10, 3).getTime(), new BigDecimal(3765), new BigDecimal(8300), oshchad));

        loans8.add(new Loan(32L, new GregorianCalendar(2020, 1, 30).getTime(),
                new GregorianCalendar(2021, 12, 31).getTime(), new BigDecimal(4700), new BigDecimal(142), privat));

        loans8.add(new Loan(33L, new GregorianCalendar(2020, 2, 25).getTime(),
                new GregorianCalendar(2020, 2, 28).getTime(), new BigDecimal(70), new BigDecimal(1384), nbu));

        loans8.add(new Loan(34L, new GregorianCalendar(2014, 2, 5).getTime(),
                new GregorianCalendar(2014, 10, 10).getTime(), new BigDecimal(6950), new BigDecimal(9051), mono));

        users.add(new User(8L, 32, "Oleksandra Adnriivna", Gender.FEMALE, new BigDecimal(1205),loans8));

        return users;
    }

}
