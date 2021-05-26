package daisyworld;

import java.util.Random;

/**
 * @author Jesse Zhao
 */
public class World {
    private final int size;
    private Patch[][] patches;
    enum Directions {UP, DOWN, LEFT, RIGHT};

    /*
    TODO: find usage for population statistics, average temperature, maybe in save results
     */

    public double getAvgTemps() {
        double average = 0.0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                average += patches[i][j].getTemp();
            }
        }
        return average / (size * size);
    }

    public int getCountDaisy() {
        int count = 0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (patches[i][j].hasDaisy()) {count++;}
            }
        }
        return count;
    }

    // function for getting average soil quality of the extension experiment
    public double getAvgSoilQuality () {
        double averageSolQuality = 0.0;
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                averageSolQuality += patches[i][j].getTemp();
            }
        }
        return averageSolQuality / (size * size);
    }

    // TODO: Scenario where luminosity ramps up and down

    public World() {
        // default size of the world: 29*29
        this(Params.WORLD_SIZE);
    }

    public World(int size) {
        this.size = size;
        patches = new Patch[size][size];
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                patches[i][j] = new Patch();
            }
        }
    }

    /**
     * Simulates a single tick of the world
     */
    public void update() {
        Patch[][] newPatches = new Patch[size][size];

        /**
         * Initialize a new world patches for the next tick.
         * Note that simply using '=' will cause known bugs;
         * This is due to the shallow copy property of java.
         */
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                newPatches[i][j] = new Patch(patches[i][j].getDaisy(), patches[i][j].getTemp());
            }
        }

        // updates the new world patches
        updatePatch(newPatches);
        patches = newPatches;

        // TODO: save locally
    }

    /**
     * Update newPatches into a new state
     * @param newPatches new patches state
     */
    private void updatePatch(Patch[][] newPatches) {

        // calculate new temperature by local-heating
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                newPatches[i][j].setTemp(newPatches[i][j].calcTemp());
            }
        }

        // diffuse temperature
        diffuse(newPatches);

        // spawn/aged daisy
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                if (patches[i][j].hasDaisy()) {
                    sproutDaisy(newPatches, i, j);
                    newPatches[i][j].agedDaisy();
                }
            }
        }
        /**
         * Clear just died signal;
         * When a daisy died at one tick,
         * other daisies should not be able to spawn a new daisy at
         * the same position at the same tick;
         * Therefore we create a counter to signal the daisy just died in this tick.
         */
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                newPatches[i][j].clearJustDied();
            }
        }
    }

    /**
     * get 4 direction's coords, try to grow new daisy
     * use getCoord() to avoid corner cases
     * @param newPatches new patches state
     * @param i coordinate
     * @param j coordinate
     */
    private void sproutDaisy(Patch[][] newPatches, int i, int j) {
        int[] upCoords = getCoord(i, j - 1);
        int[] downCoords = getCoord(i, j + 1);
        int[] leftCoords = getCoord(i - 1, j);
        int[] rightCoords = getCoord(i, j + 1);
        /**
         * check if new daisy can be grown at a certain coordinate
         */
        grow(newPatches, i, j, upCoords);
        grow(newPatches, i, j, downCoords);
        grow(newPatches, i, j, leftCoords);
        grow(newPatches, i, j, rightCoords);
    }

    /**
     * if the location currently has no daisy + no daisy has died at this coordinate this tick, then =>
     * use temperature of this location to see if a new daisy can be grown.
     * The growth of new daisy is FIFO; the first success growth will block later attempts
     * @param newPatches new patches state
     * @param i source coordinate x
     * @param j source coordinate y
     * @param coords target coordinate array: [x,y]
     */
    private void grow(Patch[][] newPatches, int i, int j, int[] coords) {
        if (!newPatches[coords[0]][coords[1]].hasDaisy() && !newPatches[coords[0]][coords[1]].justDied()) {
            double temperature = newPatches[coords[0]][coords[1]].getTemp();
            double soilQuality = newPatches[coords[0]][coords[1]].getSoilQuality();
            // Probability check: only with temperature higher than 5 degree, 100% success at 22.5 degree
            // Extension possibility check: rnd * soil quality, if the extension switch is off,
            // then soil quality should be 1 and won't affect the result.
            double rnd = (0.1457 * temperature - 0.0032 * temperature * temperature - 0.6443) * soilQuality;

            if (Math.random() < rnd) {
                // grow new daisy with the same type
                newPatches[coords[0]][coords[1]].spawnDaisy(patches[i][j].getDaisy());
            }
        }
    }

    /**
     * Update newPatches's temperatures by diffusion
     * @param newPatches new patches state
     */
    private void diffuse(Patch[][] newPatches) {
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                updateByDirection(newPatches, i, j, Directions.UP);
                updateByDirection(newPatches, i, j, Directions.DOWN);
                updateByDirection(newPatches, i, j, Directions.LEFT);
                updateByDirection(newPatches, i, j, Directions.RIGHT);
            }
        }
    }

    /**
     * Update a single direction's temperature diffusion
     * @param newPatches new patches state
     * @param i coordinate
     * @param j coordinate
     * @param direction Direction: up, down, left, right
     */
    private void updateByDirection(Patch[][] newPatches, int i, int j, Directions direction) {
        Patch source = patches[i][j];
        int target = -1;
        double diff = 0;
        double soilQualityDiff = 0.0;
        switch (direction) {
            case UP:
                target = j == 0 ? size - 1 : j - 1;
                diff = (source.getTemp() - patches[i][target].getTemp()) * Params.DIFFUSION_RATE;
                // calculation of soil quality difference
                soilQualityDiff = (source.getSoilQuality() - patches[i][target].getTemp()) * Params.DIFFUSION_RATE;
                newPatches[i][target].setTemp(newPatches[i][target].getTemp() + diff);
                // set new patch soil quality
                if (newPatches[i][target].getSoilQuality() == 0.0) {
                    newPatches[i][target].setSoilQuality(0.0);
                } else {
                    newPatches[i][target].setSoilQuality(newPatches[i][target].getSoilQuality() + soilQualityDiff);
                }
                break;
            case DOWN:
                target = j == size - 1 ? 0 : j + 1;
                diff = (source.getTemp() - patches[i][target].getTemp()) * Params.DIFFUSION_RATE;
                // calculation of soil quality difference
                soilQualityDiff = (source.getSoilQuality() - patches[i][target].getTemp()) * Params.DIFFUSION_RATE;
                newPatches[i][target].setTemp(newPatches[i][target].getTemp() + diff);
                // set new patch soil quality
                if (newPatches[i][target].getSoilQuality() == 0.0) {
                    newPatches[i][target].setSoilQuality(0.0);
                } else {
                    newPatches[i][target].setSoilQuality(newPatches[i][target].getSoilQuality() + soilQualityDiff);
                }
                break;
            case LEFT:
                target = i == 0 ? size - 1 : i - 1;
                diff = (source.getTemp() - patches[target][j].getTemp()) * Params.DIFFUSION_RATE;
                // calculation of soil quality difference
                soilQualityDiff = (source.getSoilQuality() - patches[target][i].getTemp()) * Params.DIFFUSION_RATE;
                newPatches[target][j].setTemp(newPatches[target][j].getTemp() + diff);
                // set new patch soil quality
                if (newPatches[target][i].getSoilQuality() == 0.0) {
                    newPatches[target][i].setSoilQuality(0.0);
                } else {
                    newPatches[target][i].setSoilQuality(newPatches[target][i].getSoilQuality() + soilQualityDiff);
                }
                break;
            case RIGHT:
                target = i == size - 1 ? 0 : i + 1;
                diff = (source.getTemp() - patches[target][j].getTemp()) * Params.DIFFUSION_RATE;
                // calculation of soil quality difference
                soilQualityDiff = (source.getSoilQuality() - patches[target][i].getTemp()) * Params.DIFFUSION_RATE;
                newPatches[target][j].setTemp(newPatches[target][j].getTemp() + diff);
                // set new patch soil quality
                if (newPatches[target][i].getSoilQuality() <= Params.DEATH_LINE) {
                    newPatches[target][i].setSoilQuality(0.0);
                } else {
                    newPatches[target][i].setSoilQuality(newPatches[target][i].getSoilQuality() + soilQualityDiff);
                }
                break;
            default:
                break;
        }
    }

    public void bulkUpdate(int times) {
        while(times > 0) {
            update();
            // TODO: Progress Bar
            times--;
        }
        // TODO: Save locally
    }

    public void display() {
        double[][] tempMap = new double[size][size];
        String[][] daisyMap = new String[size][size];
        double[][] soilQualityMap = new double[size][size]; // the variable for the extension experiment

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Patch cur = patches[i][j];
                tempMap[i][j] = cur.getTemp();
                daisyMap[i][j] = cur.getDaisy().toString();
                soilQualityMap[i][j] = cur.getSoilQuality();
            }
        }
        System.out.println(" Temperature map : \n");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.printf("%.2f", tempMap[i][j]);
                System.out.print('\t');
            }
            System.out.println('\n');
        }

        System.out.println(" Daisy map : \n");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(daisyMap[i][j]);
                System.out.print('\t');
            }
            System.out.println('\n');
        }

        // function for displaying the soil quality map
        System.out.println(" Soil quality map : \n");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.printf("%.2f", soilQualityMap[i][j]);
                System.out.print('\t');
            }
            System.out.println('\n');
        }
    }

    /**
     * Helper function, get pass corner cases
     * Positions at the edge will come across the border as connected
     * @param x
     * @param y
     * @return
     */
    private int[] getCoord(int x, int y) {
        int[] coords = new int[2];

        if (x < 0 || x == size) {
            coords[0] = x < 0 ? size - 1 : 0;
        } else {coords[0] = x;}

        if (y < 0 || y == size) {
            coords[0] = y < 0 ? size - 1 : 0;
        } else {coords[1] = y;}

        return coords;
    }

    // The reference functions to get across the corner and calculate the temperature.
    private static int wrap (int coordinate) {
        if (coordinate < 0) {
            return  Params.WORLD_SIZE - 1;
        } else if (coordinate >= Params.WORLD_SIZE) {
            return 0;
        } else {
            return coordinate;
        }
    }

    /**
     * calculate the diffused value of patch (x, y) to its neighbours, and add it to the delta grid
     * @param patchValue    the patch value to be diffused
     * @param gridDelta     an array to record the value change for every patches after diffusion
     * @param x             x coordinate
     * @param y             y coordinate
     * @param diffusionRate diffusion rate
     */
    private static void calculateShares (double patchValue, double[][] gridDelta,
                                         int x, int y, double diffusionRate) {
        double deltaValue = diffusionRate * patchValue / 8;
        gridDelta[wrap(x - 1)][wrap(y - 1)] += deltaValue;
        gridDelta[wrap(x - 1)][wrap(y)] += deltaValue;
        gridDelta[wrap(x - 1)][wrap(y + 1)] += deltaValue;
        gridDelta[wrap(x)][wrap(y - 1)] += deltaValue;
        gridDelta[wrap(x)][wrap(y + 1)] += deltaValue;
        gridDelta[wrap(x + 1)][wrap(y - 1)] += deltaValue;
        gridDelta[wrap(x + 1)][wrap(y)] += deltaValue;
        gridDelta[wrap(x + 1)][wrap(y + 1)] += deltaValue;
    }

    /**
     * add the temperature change from the delta grid to the remaining temperature in patches
     * @param gridDelta     the temperature change of each patch
     * @param grid          the grid of the world
     * @param diffusionRate the diffusion rate of the temperature
     */
    private static void applyTemperatureShares (
            double[][] gridDelta, Patch[][] grid, double diffusionRate
    ) {
        for (int i = 0; i < Params.WORLD_SIZE; i++) {
            for (int j = 0; j < Params.WORLD_SIZE; j++) {
                double newTemperature = grid[i][j].getTemp() * (1 - diffusionRate) + gridDelta[i][j];
                grid[i][j].setTemp(newTemperature);
            }
        }
    }

    /**
     * add the soil quality change from the delta grid to the remaining soil quality in patches
     * @param gridDelta     the soil quality change of each patch
     * @param grid          the grid of the world
     * @param diffusionRate the diffusion rate of the soil quality
     */
    private static void applySoilQualityShares (
        double[][] gridDelta, Patch[][] grid, double diffusionRate
    ) {
        for (int i = 0; i < Params.WORLD_SIZE; i++) {
            for (int j = 0; j < Params.WORLD_SIZE; j++) {
                double newSoilQuality = grid[i][j].getSoilQuality() * (1 - diffusionRate) + gridDelta[i][j];
                grid[i][j].setSoilQuality(newSoilQuality);
            }
        }
    }

}
