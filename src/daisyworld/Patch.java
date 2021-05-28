package daisyworld;

/**
 * @author Jesse Zhao
 */
public class Patch {
    private double temperature;
    private Daisy daisy;
    // if the daisy of this patch died this tick; block the spawning of new daisy
    private boolean justDied = false;
    // the soil quality for the extension experiment
    private double soilQuality;

    public Patch() {
        double random = Math.random();

        // initialize daisy status
        if (random < Params.START_PERCENT_BLACK) {
            daisy = new Daisy(Types.Black);
            daisy.setAge();
        } else if (random > 1 - Params.START_PERCENT_WHITES) {
            daisy = new Daisy(Types.White);
            daisy.setAge();
        } else {
            daisy = new Daisy(Types.None);
        }

        // initialize random temperature
        temperature = 15 + 5 * Math.random();

        // only change soil quality if the switch is turned on
        if (!Params.QUALITY_SWITCH) {
            soilQuality = 1.0;
        } else {
            soilQuality = Params.MAX_QUALITY * Math.random();
        }
    }

    // generate a new patch with the same daisy stats
    public Patch(Daisy daisy) {
        this();
        this.daisy = new Daisy(daisy.getType(), daisy.getAge());
    }

    // generate a new patch with the same daisy and temperature stats
    public Patch(Daisy daisy, double temperature) {
        this();
        this.temperature = temperature;
        this.daisy = new Daisy(daisy.getType(), daisy.getAge());
    }

    // generate a new patch with the same daisy and temperature stats
    public Patch(Daisy daisy, double temperature, double soilQuality) {
        this();
        this.temperature = temperature;
        this.daisy = new Daisy(daisy.getType(), daisy.getAge());
        this.soilQuality = soilQuality;
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

    /**
     * calculate luminosity according to albedo and solar luminosity
     * @return patch luminosity
     */
    private double calcLuminosity() {
        return (1 - daisy.getAlbedo()) * Params.SOLAR_LUMINOSITY;
    }

    /**
     *
     * @return current temperature
     */
    public double getTemp() {return temperature;}

    /**
     * set current temperature
     * @param temperature temperature to be set
     */
    public void setTemp(double temperature) {
        this.temperature = temperature;
    }

    /**
     *
     * @return current soil quality
     */
    public double getSoilQuality () {return soilQuality;}

    /**
     * set current soil qualitu
     * @param soilQuality soil quaity to be set
     */
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
        // if there is no daisy on the patch, decrease the soil quality
        // else increase the soil quality
        if (!this.hasDaisy()) {
            newQuality = Math.max(0, currentQuality - decreasePossibility);
        } else {
            newQuality = Math.min(1, currentQuality + increasePossibility);
        }
        this.soilQuality = newQuality;
    }

    /**
     * getter functions for daisy
     */
    public boolean hasDaisy() {return daisy.hasDaisy();}
    
    public Daisy getDaisy() {return daisy;}

    /**
     * update daisy of this patch; avoids shallow copy
     * @param daisy daisy set to this patch
     */
    public void setDaisy(Daisy daisy) {this.daisy = new Daisy(daisy.getType());}

    /**
     * increase daisy age; daisy dies when exceeding max age
     */
    public void agedDaisy() {
        int res = daisy.age();
        if (res == 1) {
            // the aged daisy has died due to exceeding max age
            justDied = true;
        }
    }

    /**
     * spawn new daisy on the patch
     * @param daisy daisy stats
     */
    public void spawnDaisy(Daisy daisy) {
        setDaisy(daisy);
    }

    /**
     *
     * @return justDied status of current patch
     */
    public boolean justDied() {
        return justDied;
    }

    /**
     * clear justDied status of current patch
     */
    public void clearJustDied() {
        justDied = false;
    }
}
