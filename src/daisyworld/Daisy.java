package daisyworld;

/**
 * Note: we have 3 different types:
 * Black, White and NONE. NONE refers to no daisy on the patch
 * This is to simplify codes in patch.java and world.java
 */
enum Types {None, White, Black}

/**
 * @author Jesse Zhao
 */
public class Daisy {
    private Types type;
    private int age;
    private double albedo;

    /**
     * spawn a new daisy with type: none
     */
    public Daisy(){
        this(Types.None);
    }

    /**
     * spawn a new daisy with given type
     * @param type type given
     */
    public Daisy(Types type){
        age = 0;
        this.type = type;
        if (type == Types.None) {
            albedo = Params.ALBEDO_SURFACE;
        } else if (type == Types.Black) {
            albedo = Params.ALBEDO_BLACK;
        } else if (type == Types.White) {
            albedo = Params.ALBEDO_WHITE;
        }
    }

    /**
     * spawn a new daisy with given type and age
     * @param type type given
     * @param age age given
     */
    public Daisy(Types type, int age){
        this(type);
        this.age = age;
    }

    /**
     * increase current daisy age by 1, dies if exceeding max age
     * @return 0 if age successfully, 1 if the daisy died
     */
    public int age() {
        if (type != Types.None) {
            age++;
        }

        if (age > Params.MAX_AGE) {
            this.type = Types.None;
            this.age = 0;
            this.albedo = Params.ALBEDO_SURFACE;
            return 1;
        }

        return 0;
    }

    /**
     * override for display
     * @return string format of daisy
     */
    @Override
    public String toString(){
        if (type == Types.None) {
            return "None";
        } else if (type == Types.Black) {
            return "Black";
        } else {
            return "White";
        }
    }

    /**
     *
     * @return albedo of current daisy
     */
    public double getAlbedo(){return albedo;}

    /**
     *
     * @return boolean if current daisy is an actual daisy instance
     */
    public boolean hasDaisy(){
        return type != Types.None;
    }

    /**
     * getters
     * @return properties
     */
    public Types getType() {return type;}

    public int getAge() {return age;}

    public void setAge () {this.age = (int)(Math.random() * Params.MAX_AGE);}
}
