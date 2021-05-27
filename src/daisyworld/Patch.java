package daisyworld;

/**
 * @author Jesse Zhao
 */
public class Patch {
    private double temperature;
    private Daisy daisy;
    private boolean justDied = false;
    private double soilQuality; // the soil quality for the extension experiment

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

        if (!Params.QUALITY_SWITCH) {
            soilQuality = 1.0;
        } else {
            soilQuality = Params.MAX_QUALITY * Math.random();
        }
    }

    public Patch(Daisy daisy) {
        this();
        this.daisy = new Daisy(daisy.getType(), daisy.getAge());
    }

    public Patch(Daisy daisy, double temperature) {
        this();
        this.temperature = temperature;
        this.daisy = new Daisy(daisy.getType(), daisy.getAge());
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

    // function for getting and setting the parameter of extension experiment
    public double getSoilQuality () {return soilQuality;}

    public void setSoilQuality (double soilQuality) {this.soilQuality = soilQuality;}

    /**
     * In this function the soil quality would change according to the current quality and if there is
     * daisy living on this patch. The basic idea is if sth is good, then it's easier to become better.
     * Otherwise, if sth is bad, then it's easier to become worse, Which is the balance of echo system.
     */
    public void changeSoilQuality () {
        double currentQuality = this.soilQuality;
        double nonPerfectRate = 1 - this.soilQuality;
        double decreasePossibility = Params.CHANGE_BASE * nonPerfectRate;
        double increasePossibility = Params.CHANGE_BASE * currentQuality;
        double newQuality;
        if (this.hasDaisy() == false) {
            newQuality = Math.max(0, currentQuality - decreasePossibility);
        } else {
            newQuality = Math.min(1, currentQuality + increasePossibility);
        }
        this.soilQuality = newQuality;
    }

    public boolean hasDaisy() {return daisy.hasDaisy();}
    
    public Daisy getDaisy() {return daisy;}

    public void agedDaisy() {
        int res = daisy.age();
        if (res == 1) {
            // the aged daisy has died due to exceeding max age
            justDied = true;
        }
    }

    public void spawnDaisy(Daisy daisy) {
        setDaisy(daisy);
    }

    /**
     * update daisy of this patch; avoids shallow copy
     * @param daisy daisy set to this patch
     */
    public void setDaisy(Daisy daisy) {this.daisy = new Daisy(daisy.getType());}

    public boolean justDied() {
        return justDied;
    }

    public void clearJustDied() {
        justDied = false;
    }
}
