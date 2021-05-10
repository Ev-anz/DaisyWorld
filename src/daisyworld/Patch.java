public class Patch {
    private double temperature;
    private Daisy daisy;

    public Patch() {
        double random = Math.random();

        if (random < Params.START_PERCENT_BLACK) daisy = new Daisy(Types.Black);
        else if (random > 1 - Params.START_PERCENT_WHITES) daisy = new Daisy(Types.White);
        else daisy = new Daisy(Types.None);

        temperature = 15 + 5 * Math.random();
    }

    public Patch(Daisy daisy) {
        this();
        this.daisy = daisy;
    }

    public double calcTemp() {
        double absorbed_luminosity = calcLuminosity();
        double local_heating = absorbed_luminosity > 0 ? Math.log(absorbed_luminosity) * 72 + 80 : 80;

        return (temperature + local_heating) / 2;
    }

    private double calcLuminosity() {
        return (1 - daisy.getAlbedo()) * Params.SOLAR_LUMINOSITY;
    }
    
    public double getTemp() {return temperature;}

    public void setTemp(double temperature) {
        this.temperature = temperature;
    }

    public boolean hasDaisy() {return daisy.hasDaisy();}
    
    public Daisy getDaisy() {return daisy;}

    public void agedDaisy() {
        daisy.age();
    }

    public void spawnDaisy() {
        // TODO
    }

}
