import java.util.*;
import java.io.*;
class jurrassicWorldProgram{
    //Define all final variables that would be used in the program
    public static final Integer dinoLimit = 28;
    public static final Scanner scanner = new Scanner(System.in);
    public static final Random rand = new Random();
    public static final String spacing = "\n \n \n \n";
   
   
    public static void main (String[] args){

        List possibleDinos = new ArrayList<Dinosaur>();

        setupPossibleDinoList(possibleDinos);
        //Set up basic variables that would be used for the game
        List<Dinosaur> usersDinos = new ArrayList<Dinosaur>();
        Integer usersMoney = 0;
        printMessage(spacing);

        //load a game if there is one available
        usersMoney = loadGame(usersMoney, usersDinos, possibleDinos);

        if (usersMoney == 0){
            //ask if the user would like an intro to the game
            starterMessage();
            //set up the game difficulty which is how much money you start with
            usersMoney = setGameDifficulty(usersMoney);
            printMessage(spacing);
        }
        //get the users dame
        String userName = getName();
        printMessage(spacing);
        //keep running the program untill the user would like to quit
        Boolean gameState = true;
        while (gameState == true){

            //ask the user for an option for what they would like to do
            String option = mainMenu( userName);
            //do what the user would like to do
            usersMoney = evaluateOption(option, usersMoney, usersDinos, possibleDinos);
            checkPlayerStatus(usersMoney);

        }
    }

    //prompt the user for an input and evaluate whether their input is valid
    public static String mainMenu ( String userName){
        printMessage("Hello " + userName +"\nWhat would you like to do:");
        printMessage("Buy Dinosaurs [b] \nSell Dinosaurs [s] \nEnd Month [e] \nExit Game [q] \nSave Game [k]");
        
        Boolean answerGiven = false;
        String answer = "";
        while (answerGiven == false){

            answer = scanner.next();
            if(answer.toLowerCase().equals("b") || answer.toLowerCase().equals("s") || answer.toLowerCase().equals("e") || answer.toLowerCase().equals("b") || answer.toLowerCase().equals("k")){
                answerGiven = true;
            }else if (answer.toLowerCase().equals("q")){
                System.exit(0);
            }else{
                printMessage("Please enter a valid option:");
            }
             
        }
        return answer;
    }

    //execute what the user would like to do
    public static Integer evaluateOption(String option, Integer usersMoney, List usersDinos, List possibleDinos) {

        if (option.equalsIgnoreCase("b")){
            //allow the user to buy dinos and then sort their dinos in id order
            usersMoney = buyDinosaurs(usersMoney, usersDinos, possibleDinos);
            sortUsersDinoList(usersDinos);
            return usersMoney;
        }else if (option.equalsIgnoreCase("s")){
            usersMoney = sellDinos(usersMoney, usersDinos, possibleDinos);
            return usersMoney;
        }else if(option.equalsIgnoreCase("e")){
            usersMoney = nextMonth(usersMoney, usersDinos, possibleDinos);
            return usersMoney;
        }else{
            saveGame(usersDinos, usersMoney);
        }
        return usersMoney;
    }

    // a method that would give an explanation to the game if the user would like one
    public static void starterMessage (){
        
        printMessage("Would you like an explanation on how the game will work? [y/n]");
        String answer = scanner.next();
        if (answer.toLowerCase().equals("y")){
            printMessage("Every round, you would have the chance to buy or sell dinosaurs \n If your balance ever goes below zero, you lose"
            + "\nOnce you think your ready, end the round and the game will travel a month in the future" 
            + "\nKeep in mind, the more dangerous the dinours you own, the more likely they are to break out and cost you money");
        }
    }

    //method to set the games initial money ammount depending on what dificulty the user wants
    public static Integer setGameDifficulty (Integer usersMoney){
        printMessage("What dificulty would you like to set the game?");
        printMessage("Type 'e' for Easy, 'm' for medium and 'h' for hard:" );
        String answer = scanner.next();

        if (answer.toLowerCase().equals("e")){
            usersMoney = 1000;
            printMessage("Thank you, setting your difficulty to easy...");
        }else if (answer.toLowerCase().equals("m")){
            usersMoney = 500;  
            printMessage("Thank you, setting your difficulty to medium..."); 
        }else if (answer.toLowerCase().equals("h")){
            usersMoney = 250;
            printMessage("Thank you, setting your difficulty to hard...");
        }else {
            printMessage("I couldn't undertand what you entered, setting dificulty to medium...");
            usersMoney = 5000000;
        }
        return usersMoney;
    }

    //Skips forward a month in the game
    // the code raises the users money by randonly generating how much the dinosaurs have earnt
    // it then calculates how much you have lost due to your dinosaurs breaking out
    //it then ages all dinosaurs and informs the user if any have died and removes them from the list
    public static Integer nextMonth(Integer usersMoney, List usersDinos, List possibleDinos){
        printMessage(spacing);
        for (Integer i = 1; i <= usersDinos.size(); i ++){
            String[] incomeRange = getIncome((Dinosaur)possibleDinos.get(getId((Dinosaur)usersDinos.get(i - 1)))).split("-");
            Integer revenueMade = rand.nextInt(Integer.parseInt(incomeRange[1])-Integer.parseInt(incomeRange[0])) + Integer.parseInt(incomeRange[0]) + 1;
            usersMoney += revenueMade;
            printMessage("Your " + getName((Dinosaur)usersDinos.get(i-1)) + " made $" + revenueMade + "K");
        }

        for (Integer i = 1; i <= usersDinos.size(); i ++){
            Integer chance = getDanger((Dinosaur)possibleDinos.get(getId((Dinosaur)usersDinos.get(i - 1))));
            Integer randomNumber = rand.nextInt(100) + 1;
            if (randomNumber <= chance){
                Integer moneyLost = rand.nextInt(500) + 101;
                usersMoney -= moneyLost;
                printMessage("Ohh No! Your " + getName((Dinosaur)usersDinos.get(i-1))+ " broke out and we lost $" + moneyLost + "K in repairs and lawsuits");
            }
        }

        for (Integer i = 1; i <= usersDinos.size(); i ++) {

            Dinosaur ageDino = (Dinosaur)usersDinos.get(i - 1);
            ageDino.age = ageDino.age + 1;
        }

        for (Integer i = 1; i <= usersDinos.size(); i ++){

            if (getMaxAge((Dinosaur)usersDinos.get(i-1)) == getAge((Dinosaur)usersDinos.get(i-1))){
                printMessage("Unfortunatly, " + getName((Dinosaur)usersDinos.get(i-1)) + " passed away last month");
                usersDinos.remove(i-1);
            }
        }
        sortUsersDinoList(usersDinos);
        return usersMoney;
    }

    //get and return the users name
    public static String getName(){
        printMessage("Hello sir, welcome to youe island. \n What would you like to be called?");
        String name = scanner.next();
        return name;
    }

    //method used to allow the user to buy dinosaurs
    public static Integer buyDinosaurs(Integer usersMoney, List usersDinos, List possibleDinos){

        printDinoList(usersMoney, possibleDinos);

        Boolean continueBuying = true;
        while (continueBuying == true){
            printMessage("Please enter the number in [] for the dinosaur you would like to buy:");
            printMessage("Or enter 0 to exit buying menu");
            Integer answer = 0;
            try{
                answer = scanner.nextInt();
                if (answer > dinoLimit || answer < 0){
                    printMessage("Please enter a valid number");
                }else if (answer == 0 ){
                    continueBuying = false;
                }else if (getCost((Dinosaur)possibleDinos.get((answer - 1))) > usersMoney){
                    printMessage("Sorry, you dont have enough money for that dinosaur");
                }else if (getCost((Dinosaur)possibleDinos.get((answer - 1))) <= usersMoney){
                    printMessage("Buying you a " + getName((Dinosaur)possibleDinos.get((answer - 1))) + ", please wait...");
                    usersMoney -= getCost((Dinosaur)possibleDinos.get((answer - 1)));
                    Dinosaur newDino = new Dinosaur();
                    setDinosaur(newDino, (answer-1), possibleDinos);
                    usersDinos.add(newDino);
                    printDinoList(usersMoney, possibleDinos);
                }
            } catch (Exception e){
                printMessage("That is not a valid input... \nPutting you back to main menue...");
                continueBuying = false;
            }
            printMessage(spacing);
        }
        return usersMoney;
    }

    // print a table with all the characteristicsof the dinosaurs and all the values of the dinosaurs
    public static void printDinoList(Integer usersMoney, List possibleDinos){

        System.out.println(String.format("%55s  %25s ","", "Your money: " + usersMoney));
        System.out.println(String.format("%25s %25s %10s %25s %10s %25s %8s %25s %5s", "Dinosaur", "|", " Cost (K)", "|", "Danger", "|",
        " Age range", "|", "Income"));
        System.out.println(String.format("%25s %25s %10s %25s %10s %25s %8s %25s %5s", "        ", "|", "        ", "|", "Level", "|",
        "  (Months)", "|", "      "));
        System.out.println("-----------------------------------------------------------------------------------------" + 
        "-----------------------------------------------------------------------------------------");

        Integer counter = 0;
        for (Integer i = 1 ; i <= possibleDinos.size(); i++){

            System.out.println(String.format("%25s %25s %10s %25s %10s %25s %10s %25s %10s", getName((Dinosaur)possibleDinos.get(counter)) + " ["+(counter + 1) +"]", "|", getCost((Dinosaur)possibleDinos.get(counter)), "|", getDangerCatogary((Dinosaur)possibleDinos.get(counter)), "|", 
            getAgeRange((Dinosaur)possibleDinos.get(counter)), "|", getIncome((Dinosaur)possibleDinos.get(counter))));
            counter ++;
        }
    }

    //prints a table that contains all the dinosaurs the player owns
    public static void printUsersDinoList (Integer usersMoney, List usersDinos, List possibleDinos){

        System.out.println(String.format("%55s  %25s ","", "Your money: " + usersMoney));
        System.out.println(String.format("%25s %25s %10s %25s %10s %25s %8s", "Dinosaur", "|", " Value (K)", "|", "Danger", "|",
        " Age"));
        System.out.println("----------------------------------------------------------------------------------------"+
        "----------------------------------------------------------------------------------------");
        Integer counter = 0;
        for (Integer i = 1; i <= usersDinos.size(); i++){

            Integer id = getId((Dinosaur)usersDinos.get(counter));
            System.out.println(String.format("%25s %25s %10s %25s %10s %25s %8s", getName((Dinosaur)usersDinos.get(counter)) + " [" + (i) +"]" , 
            "|", " " + getCost((Dinosaur)possibleDinos.get(id)), "|", getDangerCatogary((Dinosaur)possibleDinos.get(id)), "|"," "+ getAge((Dinosaur)usersDinos.get(counter))));
            counter ++;
        }
    }

    // method used to make sure the player isnt bankcrupt
    public static void checkPlayerStatus (Integer usersMoney){
        if (usersMoney < 0){
            printMessage("Unfortunetly, you are bankcrupt! \nGame now over");
            System.exit(0);
        }
    }

    //method used to sell the users dinosaurs 
    public static Integer sellDinos (Integer usersMoney, List usersDinos, List possibleDinos){

        printUsersDinoList(usersMoney, usersDinos, possibleDinos);

        Boolean continueSelling = true;
        while (continueSelling == true){

            printMessage("Please enter the number in [] for the dinosaur you would like to buy:");
            printMessage("Or ender 0 to exit buying menu");
            Integer answer = 0;

            try{
                answer = scanner.nextInt();
                if (answer > usersDinos.size() || answer < 0){
                    printMessage("Please enter a valid number");
                }else if (answer == 0){
                    continueSelling = false;
                }else {
                    usersMoney += getCost((Dinosaur)possibleDinos.get(answer - 1));
                    usersDinos.remove(answer - 1);
                    printUsersDinoList(usersMoney, usersDinos, possibleDinos);
                }
            }catch(Exception e){
                printMessage("That is not a valid input... \nPutting you back to main menue...");
                continueSelling = false;
            }
            printMessage(spacing);
        }
        return usersMoney;
    }

    //Method used to save the game state, it saves the amount of money the user has
    // it also saves the id, ages and max ages of all dions
    public static void saveGame (List usersDinos, Integer usersMoney){

        try{

            FileWriter fileWriter = new FileWriter("jurasicGameSave.rtf");
            PrintWriter printWriter = new PrintWriter(fileWriter);

            printWriter.println(usersMoney);
            for (Integer i = 1; i <= usersDinos.size(); i ++){
                printWriter.println(getId((Dinosaur)usersDinos.get(i - 1)) + "-" + getAge((Dinosaur)usersDinos.get(i -1)) + "-" + getMaxAge((Dinosaur)usersDinos.get(i-1)));
            }
            printWriter.close();
            fileWriter.close();
        }catch(Exception e){
            printMessage("Sorry, something whent wrong. Game could not be saved");
        }
    }

    // method used to load a saved game, it takes the saved id, ages and max ages and fills in the rest of the information
    public static Integer loadGame (Integer usersMoney, List usersDinos, List possibleDinos){

        printMessage("would you like to load the a saved game? [y/n]");
        String answer = scanner.nextLine();
        if (answer.toLowerCase().equals("y")){
            try{

                FileReader fileReader = new FileReader("jurasicGameSave.rtf");
                BufferedReader bufferedReader = new BufferedReader(fileReader);
                String moneyStr = bufferedReader.readLine();
                usersMoney = Integer.parseInt(moneyStr);
                String nextRow;
                while ((nextRow = bufferedReader.readLine()) != null){
                    Dinosaur newDino = new Dinosaur();
                    String[] dinoInfo = nextRow.split("-");
                    setId(newDino, Integer.parseInt(dinoInfo[0]));
                    setAge(newDino, Integer.parseInt(dinoInfo[1]));
                    setMaxAge(newDino, Integer.parseInt(dinoInfo[2])); 
                    setCost(newDino, getCost((Dinosaur)possibleDinos.get(Integer.parseInt(dinoInfo[0]))));
                    setDanger(newDino, getDanger((Dinosaur)possibleDinos.get(Integer.parseInt(dinoInfo[0]))));
                    setName(newDino, getName((Dinosaur)possibleDinos.get(Integer.parseInt(dinoInfo[0]))));
                    usersDinos.add(newDino);
                }
                bufferedReader.close();
                fileReader.close();
            }catch(Exception e){
                printMessage("Sorry, something whent wrong. \nCould not load game");
            }
        }
        return usersMoney;
    }

    // one big setter method that sets all the information for a dinosaur
    public static Dinosaur setDinosaur (Dinosaur d, Integer position, List possibleDinos){
        setId(d, position);
        setName(d, getName((Dinosaur)possibleDinos.get(position)));
        setCost(d, position);
        setDanger(d, position);
        setAge(d, 1);
        String[] ageRange = getAgeRange((Dinosaur)possibleDinos.get(position)).split("-");
        Integer age = rand.nextInt(Integer.parseInt(ageRange[1])-Integer.parseInt(ageRange[0])) + Integer.parseInt(ageRange[0]) + 1;
        setMaxAge(d, age);
        return d;
    }

    // a bubble sort algorith that sorts the users dinosaurs in terms of their id number
    public static void sortUsersDinoList (List usersDinos){
        if (usersDinos.size() > 1){
            Integer passesMade = -1;
            while (passesMade != 0){
                passesMade = 0;
                for (Integer i = 1; i < usersDinos.size(); i ++){
                    if (getId((Dinosaur)usersDinos.get(i - 1)) > getId((Dinosaur)usersDinos.get(i))){

                        Dinosaur extangeVar = new Dinosaur();
                        extangeVar = (Dinosaur)usersDinos.get(i - 1);
                        usersDinos.remove(i - 1);
                        usersDinos.add(i, extangeVar);
                        passesMade ++;
                    }
                }
            }
        }
    }

    //This methods adds the object dinosaur to the list possible dinosaurs and returns it with all its corisponding atributes
    public static List setupPossibleDinoList (List possibleDinos){

        String[] dinos = {"Allosaurus", "Ankylosaurus", "Apatosaurus", "Archaeornithomimus", "Baryonyx", "Brachiosaurus",  "Carnotaurus",
        "Ceratosaurus", "Dilophosaurus", "Diplodocus", "Dracorex", "Edmontosaurus", "Gallimimus", "Giganotosaurus", "Indominus rex", "Indoraptor", "Majungasaurus",
        "Pachycephalosaurus", "Parasaurolophus", "Sinoceratops", "Spinosaurus", "Stegosaurus", "Struthiomimus", "Styracosaurus", "Suchomimus", "Triceratops"
        , "Tyrannosaurus", "Velociraptor"};
        Integer[] costs = {1873, 315, 851, 70, 784, 784, 1384, 550, 317, 625, 150, 170, 80, 1717, 2516, 2710, 1465, 195, 180, 241, 2012, 320
        , 30, 315, 1228, 230, 1964, 373};
        Integer[] danger = {66, 25, 20, 8, 69, 20, 40, 45, 30, 25, 60, 15, 10, 70, 80, 85, 50, 50, 55, 30, 55, 30, 11, 33, 59, 43, 88, 77};
        String[] dangerCatogary = {"High", "Medium", "Medium", "Low", "high", "Low", "Medium", "Medium", "Medium", "Medium", "High", "Low"
        , "Low", "High", "High", "High", "Medium", "Medium", "High", "Medium", "High", "Medium", "Low", "Medium", "High", "Medium", "High", "High"};
        String[] age = {"40-60", "40-60", "40-60", "10-30", "40-60", "40-60",  "40-60",  "40-60",  "30-40",  "40-60", "40-60", "40-60", "10-20",  "40-60",
        "50-60",  "50-60",  "40-60",  "30-50",  "40-60",  "40-60", "40-60", "40-60", "20-30", "40-60", "40-60", "40-60", "40-60", "40-60"};
        String[] income = {"200-400", "200-400", "200-400", "50-60",  "200-400", "200-400", "200-400", "200-400", "80-100", "200-400", "200-400", "200-400",
        "50-60",  "200-400", "600-900", "700-999", "200-400", "80-150", "200-400", "200-400", "200-400", "200-400", "40-90",  "200-400", "200-400", "200-400", "200-400", "200-400",};
        
        Integer counter = 0;
        for (String i : dinos){
            Dinosaur dino = new Dinosaur();
            setId(dino, counter);
            setName(dino, i);
            setAge(dino, 1);
            setCost(dino, costs[counter]);
            setDanger(dino, danger[counter]);
            setDangerCatogary(dino, dangerCatogary[counter]);
            setIncome(dino, income[counter]);
            setAgeRange(dino, age[counter]);
            String[] ageRange = age[counter].split("-");
            Integer maxAge = rand.nextInt(Integer.parseInt(ageRange[1])-Integer.parseInt(ageRange[0])) + Integer.parseInt(ageRange[0]) + 1;
            setMaxAge(dino, maxAge);
            counter ++;
            
            possibleDinos.add(dino);
        }
        return possibleDinos;
    }

    //getter methods to get details of dinosaurs for the user
    public static Integer getId (Dinosaur d){
        return d.id;
    }
    public static String getName (Dinosaur d){
        return d.name;
    }
    public static Integer getCost (Dinosaur d){
        return d.cost;
    }
   public static Integer getDanger (Dinosaur d){
       return d.danger;
   }
   public static Integer getAge (Dinosaur d){
       return d.age;
   }
   public static Integer getMaxAge (Dinosaur d){
       return d.maxAge;
   }
   public static String getDangerCatogary (Dinosaur d){
       return d.dangerCatogary;
   }
   public static String getIncome (Dinosaur d){
       return d.income;
   }
   public static String getAgeRange (Dinosaur d){
       return d.ageRange;
   }
   
   //Setter methods to set values to the record Dinosaur
   public static Dinosaur setId(Dinosaur d, Integer id){
       d.id = id;
       return d;
   }
   public static Dinosaur setName(Dinosaur d, String name){
        d.name = name;
        return d;
    }
    public static Dinosaur setCost(Dinosaur d, Integer cost){
        d.cost = cost;
        return d;
    }
    public static Dinosaur setDanger(Dinosaur d, Integer danger){
        d.danger = danger;
        return d;
    }
    public static Dinosaur setAge(Dinosaur d, Integer age){
        d.age = age;
        return d;
    }
    public static Dinosaur setMaxAge(Dinosaur d, Integer maxAge){
        d.maxAge = maxAge;
        return d;
    }
    public static Dinosaur setDangerCatogary (Dinosaur d, String level){
        d.dangerCatogary = level;
        return d;
    }
    public static Dinosaur setIncome (Dinosaur d, String money){
        d.income = money;
        return d;
    }
    public static Dinosaur setAgeRange (Dinosaur d, String range){
        d.ageRange = range;
        return d;
    }

   //method to print out a message
   public static void printMessage (String message){
    System.out.println(message);
    return;
   }
   
}

// Dinosour class
class Dinosaur {

    Integer id;
    String name;
    Integer cost;
    Integer danger;
    String dangerCatogary;
    Integer maxAge;
    Integer age;
    String ageRange;
    String income;
}