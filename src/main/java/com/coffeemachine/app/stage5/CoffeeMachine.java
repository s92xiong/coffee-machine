package com.coffeemachine.app.stage5;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Scanner;

enum Resources {
    WATER("water", 400, "ml"),
    MILK("milk", 540, "ml"),
    BEANS("coffee beans", 120, "g"),
    CUPS("disposable cups", 9, null),
    MONEY("money", 550, "$");
    private final String name;
    private final int amount;
    private final String unit;

    Resources(String name, int amount, String unit) {
        this.name = name;
        this.amount = amount;
        this.unit = unit;
    }

    public String getName() {
        return name;
    }

    public String getUnit() {
        return unit;
    }

    public int getAmount() {
        return amount;
    }
}

enum MenuItems {
    ESPRESSO(4, 250, 0, 16),
    LATTE(7, 350, 75, 20),
    CAPPUCCINO(6, 200, 100, 12);

    private final int price;
    private final int waterReq;
    private final int milkReq;
    private final int beansReq;

    MenuItems(int price, int waterReq, int milkReq, int beansReq) {
        this.price = price; // $
        this.waterReq = waterReq; // ml
        this.milkReq = milkReq; // ml
        this.beansReq = beansReq; // g
    }

    public int getPrice() {
        return price;
    }

    public int getWaterReq() {
        return waterReq;
    }

    public int getMilkReq() {
        return milkReq;
    }

    public int getBeansReq() {
        return beansReq;
    }
}

public class CoffeeMachine {
    private final Map<String, Integer> inventory;

    public CoffeeMachine() {
        this.inventory = getInventory();
    }

    public static Map<String, Integer> getInventory() {
        Map<String, Integer> inventory = new LinkedHashMap<>();
        for (Resources resource : Resources.values()) {
            inventory.put(resource.getName(), resource.getAmount());
        }
        return inventory;
    }

    private void printCoffeeInventory() {
        System.out.println();
        System.out.println("The coffee machine has:");
        for (Map.Entry<String, Integer> resource : inventory.entrySet()) {
            if (resource.getKey().equals("disposable cups")) break;
            var name = resource.getKey();
            var amount = resource.getValue();
            var unit = (name.equals("coffee beans")) ? "g" : Resources.valueOf(name.toUpperCase()).getUnit();
            System.out.printf("%d %s of %s\n", amount, unit, name);
        }
        System.out.printf("%d disposable cups\n", inventory.get("disposable cups"));
        System.out.printf("$%d of money\n", inventory.get("money"));
        System.out.println();
    }

    private static String getInput() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextLine();
    }

    private void take() {
        System.out.printf("I gave you $%d\n", inventory.get("money"));
        inventory.put("money", 0);
    }

    private void fill() {
        Scanner scanner = new Scanner(System.in);
        for (Map.Entry<String, Integer> resource : inventory.entrySet()) {
            var name = resource.getKey();
            if (name.equals("money")) break;
            var unit = (name.equals("coffee beans")) ? "grams" :
                    (name.equals("disposable cups")) ? "disposable cups" :
                            Resources.valueOf(resource.getKey().toUpperCase()).getUnit();
            var resourceAmount = resource.getValue();

            System.out.printf("Write how many %s of %s you want to add:\n", unit, name);
            int amountToAdd = scanner.nextInt();
            inventory.put(name, resourceAmount + amountToAdd);
        }
    }

    public void buy() {
        System.out.print("What do you want to buy? 1 - espresso, 2 - latte, 3 - cappuccino, back - to main menu:\n");
        int waterQuantity = inventory.get("water");
        int milkQuantity = inventory.get("milk");
        int beansQuantity = inventory.get("coffee beans");
        int money = inventory.get("money");
        int price = 0;
        int disposableCups = inventory.get("disposable cups");

        boolean back = false; // Go back, don't buy anything nor update the inventory
        boolean sufficientResources = false;

        switch (getInput()) {
            case "1" -> {
                price = MenuItems.ESPRESSO.getPrice();
                waterQuantity -= MenuItems.ESPRESSO.getWaterReq();
                milkQuantity -= MenuItems.ESPRESSO.getMilkReq();
                beansQuantity -= MenuItems.ESPRESSO.getBeansReq();
            }
            case "2" -> {
                price = MenuItems.LATTE.getPrice();
                waterQuantity -= MenuItems.LATTE.getWaterReq();
                milkQuantity -= MenuItems.LATTE.getMilkReq();
                beansQuantity -= MenuItems.LATTE.getBeansReq();
            }
            case "3" -> {
                price = MenuItems.CAPPUCCINO.getPrice();
                waterQuantity -= MenuItems.CAPPUCCINO.getWaterReq();
                milkQuantity -= MenuItems.CAPPUCCINO.getMilkReq();
                beansQuantity -= MenuItems.CAPPUCCINO.getBeansReq();
            }
            case "back" -> {
                back = true;
            }
        }

        if (waterQuantity < 0) {
            System.out.print("Sorry, not enough water!\n");
        } else if (milkQuantity < 0) {
            System.out.print("Sorry, not enough milk!\n");
        } else if (beansQuantity < 0) {
            System.out.print("Sorry, not enough coffee beans!\n");
        } else if (disposableCups < 0) {
            System.out.print("Sorry, not enough disposable cups!\n");
        } else {
            System.out.print("I have enough resources, making you a coffee!\n");
            sufficientResources = true;
        }

        if (!back && sufficientResources) {
            inventory.put("water", waterQuantity);
            inventory.put("milk", milkQuantity);
            inventory.put("coffee beans", beansQuantity);
            inventory.put("money", money + price);
            inventory.put("disposable cups", disposableCups - 1);
        }
    }

    private int selectAction() {
        System.out.print("Write action (buy, fill, take, remaining, exit):\n");
        switch (getInput()) {
            case "buy" -> buy();
            case "fill" -> fill();
            case "take" -> take();
            case "remaining" -> printCoffeeInventory();
            case "exit" -> {
                return 0;
            }
            default -> throw new IllegalStateException("Unexpected value: " + getInput());
        }
        return 1;
    }

    public void run() {
        while (true) {
            int val = selectAction();
            if (val == 0) break;
        }
    }

    public static void main(String[] args) {
        CoffeeMachine coffeeMachine = new CoffeeMachine();
        coffeeMachine.run();
    }
}