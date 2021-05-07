enum Types {None, White, Black};

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
        if (type == Types.None) albedo = Params.ALBEDO_SURFACE;
        else if (type == Types.Black) albedo = Params.ALBEDO_BLACK;
        else if (type == Types.White) albedo = Params.ALBEDO_WHITE;
    }

    public void age() {
        if (type != Types.None) age++;

        if (age > Params.MAX_AGE) {
            type = Types.None;
            age = 0;
            albedo = Params.ALBEDO_SURFACE;
        }
    }
    
    public String toString(){
        if (type == Types.None) return "None";
        else if (type == Types.Black) return "Black";
        else return "White";
    }

    public double getAlbedo(){return albedo;}

    public boolean hasDaisy(){
        return type != Types.None;
    }
}
