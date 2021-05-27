package daisyworld;

enum Types {None, White, Black}

/**
 * @author Jesse Zhao
 */
public class Daisy {
    private Types type;
    private int age;
    private double albedo;

    public Daisy(){
        this(Types.None);
    }

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

    public Daisy(Types type, int age){
        this(type);
        this.age = age;
    }

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

    public double getAlbedo(){return albedo;}

    public boolean hasDaisy(){
        return type != Types.None;
    }

    public Types getType() {return type;}

    public int getAge() {return age;}
}
