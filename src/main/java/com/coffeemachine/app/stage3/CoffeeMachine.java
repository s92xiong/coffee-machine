package com.coffeemachine.app.stage3;

import java.util.*;

record Ingredient(String name, int amount, String unit) {}

public class CoffeeMachine {
    private final static int waterReqPerCup = 200;
    private final static int milkReqPerCup = 50;
    private final static int beansReqPerCup = 15;
    private final Map<String, Integer> currentQuantityInCoffeeMachine;

    private final int maxCoffeeCupOutput;

    public CoffeeMachine(Map<String, Integer> currentQuantityInCoffeeMachine) {
        this.currentQuantityInCoffeeMachine = currentQuantityInCoffeeMachine;
        this.maxCoffeeCupOutput = calcMaxOutput();
    }

    /**
     * This function calculates the maximum cups of coffee the machine can output
     * limited by the lowest amount of ingredient based on its per cup requirement
     */
    private int calcMaxOutput() {
        int lowest = 2147483647;
        for (Map.Entry<String, Integer> ingredient : currentQuantityInCoffeeMachine.entrySet()) {
            var name = ingredient.getKey();
            int requiredVal = name.equals("water") ? waterReqPerCup :
                    name.equals("milk") ? milkReqPerCup :
                            beansReqPerCup;
            var result = ingredient.getValue() / requiredVal;
            lowest = Math.min(result, lowest);
        }
        return lowest;
    }

    private int getNumberOfCoffeeCups() {
        Scanner scanner = new Scanner(System.in);
        return scanner.nextInt();
    }

    public void run() {
        System.out.print("Write how many cups of coffee you will need:\n");
        int numCups = getNumberOfCoffeeCups();

        if (numCups == maxCoffeeCupOutput) {
            System.out.print("Yes, I can make that amount of coffee\n");
        } else if (numCups > maxCoffeeCupOutput) {
            System.out.printf("No, I can make only %d cup(s) of coffee\n", maxCoffeeCupOutput);
        } else {
            System.out.printf("Yes, I can make that amount of coffee (and even %d more than that)\n", maxCoffeeCupOutput - numCups);
        }
    }

    private static CoffeeMachine setIngredientAmounts() {
        // Define the ingredients
        Ingredient waterIng = new Ingredient("water", waterReqPerCup, "mL");
        Ingredient milkIng = new Ingredient("milk", milkReqPerCup, "mL");
        Ingredient coffeeBeansIng = new Ingredient("coffee beans", beansReqPerCup, "g");
        List<Ingredient> ingredients = new ArrayList<>(Arrays.asList(waterIng, milkIng, coffeeBeansIng));

        // Define a map that contains the ingredient name and its current quantity within the coffee machine
        Map<String, Integer> currentQuantityInCoffeeMachine = new LinkedHashMap<>();

        // Define the amount for each ingredient
        Scanner scanner = new Scanner(System.in);
        for (Ingredient ingredient : ingredients) {
            var name = ingredient.name();
            var unit = (ingredient.unit().equals("g")) ? "grams" : ingredient.unit();
            System.out.printf("Write how many %s of %s the coffee machine has:\n", unit, name);
            int ingredientAmount = scanner.nextInt();
            currentQuantityInCoffeeMachine.put(name, ingredientAmount);
        }

        return new CoffeeMachine(currentQuantityInCoffeeMachine);
    }

    public static void main(String[] args) {
        CoffeeMachine coffeeMachine = setIngredientAmounts();
        coffeeMachine.run();
    }
}
