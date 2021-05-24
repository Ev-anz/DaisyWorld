package daisyworld;

/**
 * @author Jesse Zhao
 */
public class Patch {
    private double temperature;
    private Daisy daisy;
    private boolean justDied = false;

    public Patch() {
        double random = Math.random();

        if (random < Params.START_PERCENT_BLACK) {
            daisy = new Daisy(Types.Black);
        } else if (random > 1 - Params.START_PERCENT_WHITES) {
            daisy = new Daisy(Types.White);
        } else {
            daisy = new Daisy(Types.None);
        }

        temperature = 15 + 5 * Math.random();
    }

    public Patch(Daisy daisy) {
        this();
        this.daisy = daisy;
    }

    public Patch(Daisy daisy, double temperature) {
        this();
        this.temperature = temperature;
        this.daisy = new Daisy(daisy.getType());
    }

    /**
     *
     * @return next tick's temperature of current patch
     */
    public double calcTemp() {
        double absorbedLuminosity = calcLuminosity();
        double localHeating = absorbedLuminosity > 0 ? Math.log(absorbedLuminosity) * 72 + 80 : 80;

        return (temperature + localHeating) / 2;
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
        int res = daisy.age();
        if (res == 1) {
            // the aged daisy has died due to exceeding max age
            justDied = true;
        };
    }

    public void spawnDaisy(Daisy daisy) {
        setDaisy(daisy);
    }

    /**
     * update daisy of this patch; avoids shallow copy
     * @param daisy
     */
    public void setDaisy(Daisy daisy) {this.daisy = new Daisy(daisy.getType());}

    public boolean justDied() {
        return justDied;
    }

    public void clearJustDied() {
        justDied = false;
    }
}
