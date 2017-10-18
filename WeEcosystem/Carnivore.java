import greenfoot.*;  // (World, Actor, GreenfootImage, Greenfoot and MouseInfo)
import java.util.*;

/**
 * Write a description of class Carnivore here.
 * 
 * @author (your name) 
 * @version (a version number or a date)
 */
public class Carnivore extends AbstOrganism
{
    private int size = 1; // Starting size+10 of an organism

    /**
     * Act - do whatever the Algae wants to do. This method is called whenever
     * the 'Act' or 'Run' button gets pressed in the environment.
     */

    // Base Constructor
    protected int radius = 120; // radius, default using 1
    protected Herbivore target = null; // when it is null, player has no target
    Herbivore herbivore = new Herbivore();
    public Carnivore() {
        GreenfootImage image = new GreenfootImage(size+10, size+10); // Creates an empty transparent image with the given size+10
        image.setColor(Color.BLACK);        // Sets the color black
        image.drawRect(0, 0, size+10, size+10);   // Draws oval with the given size+10 on top of transparent image 
        image.setColor(Color.BLACK);        // Sets the color red
        image.fillRect(0, 0, size+10, size+10);   // Fills oval with the current color
        this.setImage(image);                  // Sets this as an actor image
        lifeforms.add(this); // Adds this algae to the list containing all objects under the class type of AbstOrganism
        prey = new ArrayList <AbstOrganism> ();//list of all that the types of organism can feed on
        predators = new ArrayList <AbstOrganism> ();//list of all the types of organsims that the organism can be eaten by   
        trophic_lvl = 1; // The trophic level of the organism, its place in the food web / chain
        age = 0; // An int that increments each time act runs to store the age
        energy = 0; //Starts with zero energy
        hasMutated = false;
        stats = new int [] {300, 1, 1, 2, 200, 2}; //traits for Herbivore 
        this.radius = radius; // Radius equals random number that is set during reproduction
    }
    
    public Carnivore (int radius) {
        GreenfootImage image = new GreenfootImage(size+10, size+10); // Creates an empty transparent image with the given size+10
        image.setColor(Color.BLACK);        // Sets the color black
        image.drawRect(0, 0, size+10, size+10);   // Draws oval with the given size+10 on top of transparent image 
        image.setColor(Color.BLACK);        // Sets the color red
        image.fillRect(0, 0, size+10, size+10);   // Fills oval with the current color
        this.setImage(image);                  // Sets this as an actor image
        lifeforms.add(this); // Adds this algae to the list containing all objects under the class type of AbstOrganism
        prey = new ArrayList <AbstOrganism> ();//list of all that the types of organism can feed on
        predators = new ArrayList <AbstOrganism> ();//list of all the types of organsims that the organism can be eaten by   
        trophic_lvl = 1; // The trophic level of the organism, its place in the food web / chain
        age = 0; // An int that increments each time act runs to store the age
        energy = 0; //Starts with zero energy
        hasMutated = false;
        stats = new int [] {300, 1, 1, 2, 200, 2}; //traits for Herbivore 
        this.radius = radius; // Radius equals random number that is set during reproduction
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
        // If the world reference is not stored:
        if (world == null) {

            world = (MyWorld) getWorld (); // Store the reference to the current world

        }   

        //preforms a mutation once in the organisms lifetime 
        if (hasMutated == false) {
            mutate ();
            hasMutated = true;
        }

        if (target == null) { // when player has no target, using keyboard controller
            AI.checkPrey(this); // after each movement, check whether the food is in sight
        } else { // else player moves automatically to the target 
            AI.hunt(this); // Attacks preys
        }

        Herbivore herbivore = (Herbivore) getOneIntersectingObject(Herbivore.class);
        if (herbivore != null) {
            ((MyWorld) getWorld()).foodEaten ++;          // Increases foodeaten, a variable in My World
            energy+= (herbivore.energy)/5; //energy gained after eating
            ((MyWorld) getWorld()).removeObject(herbivore);      // Removes Algae object
            target = null; // clear the target after removing the apple

        }

        age ();     // Increases age by 1 every time the act method executes
        eat ();     // Increases energy by 1 every time the act method executes, adds 5 each time they eat other organism
        grow ();    // Grow depending on energy they have
        shift();    // Randomly moves around
        split();    // Reproduces when reaches the certain stage
    }

    protected void checkHerbivore() { // Check if there is any algae within the given radius
        List<Herbivore> list = getObjectsInRange(radius, Herbivore.class); // getting all apples in range
        if (list.isEmpty()){     // If there is no food, it gets out of function
            return;
        }

        else {
           
            target = (Herbivore) list.get(0); // always set the first one in the list as the target
            
        }
    }

    
    public List<Herbivore> givesOffList() {
    List<Herbivore> list = getObjectsInRange(radius, Herbivore.class);
    return list;
}

    
    protected void hunt(){
        if(age < lifespan){ // If its not dying and the food it is going to eat is not going to die

            turnTowards(target.getX(), target.getY());         // Turns toward the food

      
            target = null;

            move(5);                                            // Moves in certain speed

       
        }

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
        image.setColor(Color.BLACK);        // Sets the color green
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
                Herbivore temp_Splited = new Herbivore(range+40);//this is where the mutation for new radius is 
                System.out.println(range);
                //lifeforms.add(new Algae());
                world.addObject(temp_Splited, getX(), getY());

                temp_Splited.turn ((180 - angle_split * i) + (Greenfoot.getRandomNumber(angle_split) - angle_split / 2));
                //temp_Splited.move(7);

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
        

    }

    public void mutate() {
        //say ("Mutate not implemented");
        for (int i = 0; i < stats [5]; i ++) {

            int mutating = (int) Greenfoot.getRandomNumber (stats.length - 1);

            if (mutating == 0) { // lifespan

                stats [mutating] = (int) getRandomNumber(1600, 2500);

            } else if (mutating == 1) { // speed

                stats [mutating] = (int) getRandomNumber(5, 10);

            } else if (mutating == 2) { // range

                stats [mutating] = (int) getRandomNumber(120, 300);

            } else if (mutating == 3) { // num_split

                stats [mutating] = (int) getRandomNumber(2, 2);

            } else if (mutating == 4) { // split_energy

                stats [mutating] = (int) getRandomNumber(600, 800);

            } else if (mutating == 5) { // mutation_rate

                stats [mutating] = (int) getRandomNumber(2, 6);

            }     
        }
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

    public int getRandomNumber(int start, int end)
   
    {
        System.out.println(end-start+1);
        int normal = Greenfoot.getRandomNumber(end-start+1);
        
        return normal+start;
    }
       
}