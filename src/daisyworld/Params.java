package daisyworld;

import java.security.PublicKey;

/**
 * @author Jesse Zhao
 */
public class Params {
    public static final double START_PERCENT_WHITES = 0.2;

    public static final double START_PERCENT_BLACK = 0.2;

    public static final double ALBEDO_WHITE = 0.75;

    public static final double ALBEDO_BLACK = 0.25;

    public static final double ALBEDO_SURFACE = 0.4;

    public static final double SOLAR_LUMINOSITY = 0.8;

    // Max age of Daisy

    public static final double MAX_AGE = 10;

    // World size

    public static final int WORLD_SIZE = 16;

    // Parameters for the extension experiment.
    // Diffusion Rate

    public static final double DIFFUSION_RATE = 0.5;

    // The switch of the extension of soil quality, True = switch on, False = switch off.

    public static final boolean QUALITY_SWITCH = false;

    // The best value of quality of soil, the common value should fluctuate from 0 to this value.
    // The average quality of all the patches at the beginning should be 0.5.

    public static final int MAX_QUALITY = 1;

    // The death line of the soil quality, should be a small value

    public static final double DEATH_LINE = 0.02;

    //Change base, the change will dependent on product of base and current non-perfect degree.
    public static final double CHANGE_BASE = 0.1;

}
