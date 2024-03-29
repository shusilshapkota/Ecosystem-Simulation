import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)

import java.util.*;



/** Herbivore

 * A Herbivore tries to track down and eat any algae in range.
 * 
 * @author Hawke, Mudaser, Parmeet, Shusil, Tim  

 * @version 12/10/2017

 */

public class Herbivore extends AbstOrganism 

{



    private int size = 1; // Starting size+10 of an organism



    /**

     * Act - do whatever the Algae wants to do. This method is called whenever

     * the 'Act' or 'Run' button gets pressed in the environment.

     */



    // Base Constructor

    protected int range = 120; // range, default using 1

    protected Algae target = null; // when it is null, player has no target

    Carnivore carnivore = new Carnivore();


    

    

    public Herbivore () {

        GreenfootImage image = new GreenfootImage(size+10, size+10); // Creates an empty transparent image with the given size+10

        image.setColor(Color.BLACK);        // Sets the color black

        image.drawRect(0, 0, size+10, size+10);   // Draws oval with the given size+10 on top of transparent image 

        image.setColor(Color.GREEN);        // Sets the color red

        image.fillRect(0, 0, size+10, size+10);   // Fills oval with the current color

        this.setImage(image);                  // Sets this as an actor image

        lifeforms.add(this); // Adds this algae to the list containing all objects under the class type of AbstOrganism

        prey = new ArrayList <AbstOrganism> ();//list of all that the types of organism can feed on

        predators = new ArrayList <AbstOrganism> ();//list of all the types of organsims that the organism can be eaten by   

        age = 0; // An int that increments each time act runs to store the age

        energy = 50; //Starts with zero energy


        stats = new int [] {300, 1, 120, 2, 200, 2}; //traits for Herbivore 

        this.range = range; // Range equals random number that is set during reproduction, how far the detection radius is for the organism

    }

    

    public Herbivore (int [] newStats) {

        GreenfootImage image = new GreenfootImage(size+10, size+10); // Creates an empty transparent image with the given size+10

        image.setColor(Color.BLACK);        // Sets the color black

        image.drawRect(0, 0, size+10, size+10);   // Draws oval with the given size+10 on top of transparent image 

        image.setColor(Color.GREEN);        // Sets the color red

        image.fillRect(0, 0, size+10, size+10);   // Fills oval with the current color

        this.setImage(image);                  // Sets this as an actor image

        lifeforms.add(this); // Adds this algae to the list containing all objects under the class type of AbstOrganism

        prey = new ArrayList <AbstOrganism> ();//list of all that the types of organism can feed on

        predators = new ArrayList <AbstOrganism> ();//list of all the types of organsims that the organism can be eaten by   

        age = 0; // An int that increments each time act runs to store the age

        energy = 50; //Starts with zero energy


        stats = newStats; //traits for Herbivore 

        this.range = range; // Range equals random number that is set during reproduction, how far the detection radius is for the organism

    }



    public void act() 

    {



        lifespan = stats [0]; //max limit an organism can be in the world 

        speed = stats [1]; //rate of movement of the organism 

        range = stats [2]; // Int storing detection range

        num_split = stats [3]; //number of offspring an organism can produce

        split_energy = stats [4]; //set energy needed to perform a split

        mutation_rate = stats [5]; // An int which determines how many random gene stats can be changed



        AI.checkPrey(this);
        
        AI.checkPredatorHerb(this); // use herb's method of checkPredator

        // If the world reference is not stored:

        if (world == null) {



            world = (MyWorld) getWorld (); // Store the reference to the current world



        }   




        if (target == null) { // when player has no target, using keyboard controller

            AI.checkPrey(this); // after each movement, check whether the food is in sight

            AI.checkPredatorHerb(this);
        } else { // else player moves automatically to the target 

            AI.hunt(this); // Attacks preys

            AI.flee(this);
        }



        Algae algae = (Algae) getOneIntersectingObject(Algae.class);

        if (algae != null) {

            ((MyWorld) getWorld()).foodEaten ++;          // Increases foodeaten, a variable in My World

            energy+= (algae.energy)/2; //energy gained after eating

            ((MyWorld) getWorld()).removeObject(algae);      // Removes Algae object

            target = null; // clear the target after removing the apple



        }



        age ();     // Increases age by 1 every time the act method executes

        eat ();     // Increases energy by 1 every time the act method executes, adds 5 each time they eat other organism

        grow ();    // Grow depending on energy they have

        
        
        

        split();    // Reproduces when reaches the certain stage

        //display();

    }


   public List<Algae> givesOffList() { //List of preys around
           
    List<Algae> list = getObjectsInRange(range, Algae.class); // List of Algae around 
    
    return list;
}


  protected List givesOffListPredator(){ // List of predator around

      List<Carnivore> list = getObjectsInRange(range, Carnivore.class); // List of Carnvivores around 
      
        return list;
    }
  


    public void eat() {

        // Increases the energy amount.

        //energy += 1;



    }



    public void grow() {

        // Modify the size+10 of the image based on  the current energy

        size = (int) (0.02 * energy + 5); //Change in size+10

        GreenfootImage image = new GreenfootImage(size+10, size+10); 

        image.setColor(Color.BLACK);

        image.drawRect(0, 0, size+10, size+10);

        image.setColor(Color.GREEN);        // Sets the color green

        image.fillRect(0, 0, size+10, size+10);

        this.setImage(image);



    }



    public void split(){



        angle_split = 360 / num_split;              // In which angle would the child go 



        // Check to see if there if enough energy (size?) to split

        if (energy >= split_energy && age < lifespan) {



            // If yes, then call the constructor for two new ones and kill the parent

            // energy -= split_energy; // Subtract the used up energy needed to split.



            // A for loop running once for each num_Split (child to be made)

            for (int i = 0; i < num_split; i ++) {

                //range = getRandomNumber(500, 700); in case range does not mutate 

                Herbivore temp_Splitted = new Herbivore(stats);//this is where the mutation for new range is 

                Mutation.mutate(temp_Splitted);


                world.addObject(temp_Splitted, getX(), getY());


                temp_Splitted.turn ((180 - angle_split * i) + (Greenfoot.getRandomNumber(angle_split) - angle_split / 2));





            }



            die();

        }



    }



    public void age() {



        age += 1; //Increase age by 1

        if (age >= lifespan) {    // If it is less than or equal to its lifespan

             world.addObject(new Carcass(energy), getX(), getY());

            die();                // dies

        }



    }



    public void die() {


        // Remove this object from its lists

        lifeforms.remove(this);

        // Remove from the world

        world.removeObject(this);

        return;



    }




    public void shift(){

        move(speed); //adjust speed ranging form 2-8 

        //randomize turning left and right

        if(Greenfoot.getRandomNumber(100)<10){



            turn(Greenfoot.getRandomNumber(90)-45);



        }



        //if at the edge turn away 

        if(age<lifespan && isAtEdge()==true){



            turn(180);



        }



    }



    public void shift(int action, AbstOrganism target) {



    } //a function the allows the organism to move based on detect command 



    public int detect (int trophic_lvl) {



        return 0;



    }
    
    public void thinkNaively() {
        System.out.println("I'm " + this);
        System.out.println("Think Naively");
        List preys = givesOffList();
        List predators = givesOffListPredator();
        if (predators.isEmpty()) return; // no predator
        Carnivore predator = (Carnivore) getNearstOrganism(predators);
        if (!preys.isEmpty()) { // if there is preys
            System.out.println("Shall I eat that ?");
            Algae prey = (Algae) getNearstOrganism(preys);
            int distanceToPrey = Util.distanceBetween(this, prey);
            int distanceToPredator = Util.distanceBetween(this, predator);
            if (distanceToPrey < distanceToPredator) { // decide wether to eat or to run
                System.out.println("Yes");
                turnTowards(prey.getX(), prey.getY());
                return;
            }
            System.out.println("No");
        }
        turnTowards(predator.getX(), predator.getY()); // run
        turn(-180);
        return;
    }
    
    public void thinkElderly() {
        int degree = 0;
        
        return;
    }
    
    private AbstOrganism getNearstOrganism(List<AbstOrganism> organisms) {
        AbstOrganism nearstOrganism = null;
        int nearstDistance = 1000000;
        for (AbstOrganism o : organisms) { // iterate all the members to find the nearst one
            int distance = Util.distanceBetween(this, o);
            if (distance < nearstDistance) {
                nearstOrganism = o;
                nearstDistance = distance;
            }
        }
        return nearstOrganism;
    }
}