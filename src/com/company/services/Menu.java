package com.company.services;

import com.company.entities.Loan;
import com.company.entities.User;
import com.company.utils.StringUtils;

import java.util.List;
import java.util.Scanner;

public class Menu {

    private static UserService userService = new UserService();

    public void runMenu(){
        List<User> users = userService.createListOfRandomUsers();
        System.out.println(StringUtils.STARS + "\nUsers\n" + StringUtils.STARS);
        printUsers(users);
        printMenu();
        Scanner scanner = new Scanner(System.in);
        int choice;

        do{
            choice = scanner.nextInt();
            runSelectedItem(choice, users);
        } while (choice != 0);

    }

    private void printMenu(){
        System.out.println("1) погрупуй юзерів по віку\n" +
                "2) отримай список повнолітніх юзерів з непогашеними кредитами\n" +
                "3) отримай ліст неповнолітніх хлопців з погашеними кредитами\n" +
                "4) погрупуй юзерів з непогашеними кредитами по банку => Map<Bank, List<User>>\n" +
                "5) погрупуй погашені кредити по банку => Map<Bank, List<Loan>>\n" +
                "6) Отримай список унікальних імен жінок котрі погашені кредити в Mono банку\n" +
                "7) Сформуй мапу де ключем буде імя юзера а значенням середній розмір заборгованості\n" +
                "8) Сформуй мапу де ключем буде імя юзера а значенням топ 3 найбільших кредитів\n" +
                "9) Сформуй мапу де ключем буде імя юзера а значенням топ 3 кредитів з найбільшою заборгованістю\n" +
                "10) Сформуй розподіл між статтю та середньої заборгованості по протермінованим кредитам та погрупуй по \n" +
                "\tвіковим категоріям => 0-10 років -> MALE-$950 & FEMALE-$300; 10-20 -> MALE-$90 & FEMALE-$3500 ...\n");
    }

    private String printUsers(List<User> users){
        for (User user : users){
            System.out.println("(" + user.getId() + ") " + user.getName() + " " + user.getSalary()
                    + " " + user.getGender().getDescription() + " " + user.getAge());
            System.out.println(loansPrintData(user.getLoans()));
            System.out.println(StringUtils.STARS);
        }
        System.out.println(StringUtils.STARS);
        System.out.println("Users count: " + users.size());
        return "";
    }

    private String loansPrintData(List<Loan> loans){
        String printData = "";
        for (Loan loan : loans){
            printData = printData.concat("(" + loan.getId() + ") " + loan.getStart() + " - " + loan.getDue()
                        + ", amount - " + loan.getAmount() + ", balance- " + loan.getBalance() + " "
                        + loan.getBank().toString() + "\n" + StringUtils.STARS + "\n");
        }
        printData = printData.concat("Loans count: " + loans.size());
        return printData;
    }
    private void runSelectedItem(int item, List<User> users){
        switch (item){
            case 1:
                System.out.println("Group by user age");
                userService.groupByAge(users).forEach((key, value) ->
                        System.out.println("Age: " + key + "\nUsers: \n" + printUsers(value)));
                break;
            case 2:
                printUsers(userService.getAdultUsersWithDoneLoans(users));
                break;
            case 3:
                printUsers(userService.getAdultBoysWithDoneLoans(users));
                break;
            case 4:
                userService.groupUsersByBankWithDoneLoans(users).forEach((key, value) ->
                        System.out.println("Bank: " + key.toString() + "\nUsers: \n" + printUsers(value)));
                break;
            case 5:
                userService.groupDoneLoansByBank(users).forEach((key, value) ->
                        System.out.println("Bank: " + key.toString() + "\nLoans: \n" + loansPrintData(value)));
                break;
            case 6:
                userService.uniqueFemaleNamesWithDoneLoans(users).forEach(System.out::println);
                break;
            case 7:
                userService.usersAverageLoanAmount(users).forEach((key, value) ->
                        System.out.println(key + " - " + value));
                break;
            case 8:
                userService.usersWithBiggestThreeLoansAmount(users).forEach((key, value) ->
                        System.out.println("User: " + key + "\nLoans: \n" + loansPrintData(value)));
                break;
            case 9:
                userService.usersWithBiggestThreeLoansAmountAndBalance(users).forEach((key, value) ->
                        System.out.println("User: " + key + "\nLoans: \n" + loansPrintData(value)));
                break;
            case 10:
                break;
            case -1:
                for (int i = 0; i < 50; ++i) System.out.println();
                break;
            default:
                System.out.println("Wrong menu element!");
                break;
        }
    }

}
