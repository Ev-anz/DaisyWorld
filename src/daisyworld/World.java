public class World {
    private int size;
    private Patch[][] patches;
    // TODO: Average temperatures, population statistics

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

    public void update() {
        Patch[][] new_patches = patches;
        for(int i = 0; i<size;i++)
        {
            for (int j = 0; j < size; j++) {
                updatePatch(new_patches, i, j);
            }
        }

        patches = new_patches;

        // TODO: save locally
    }

    public void updatePatch(Patch[][] new_patches, int i, int j) {
        Patch new_patch = new_patches[i][j];

        new_patch.setTemp(new_patch.calcTemp());

        // TODO: diffuse temp

        if (new_patch.hasDaisy()) new_patch.agedDaisy();

        // TODO: spawn new daisy
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
        double[][] temp_map = new double[size][size];
        String[][] daisy_map = new String[size][size];

        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                Patch cur = patches[i][j];
                temp_map[i][j] = cur.getTemp();
                daisy_map[i][j] = cur.getDaisy().toString();
            }
        }
        System.out.println(" Temperature map : \n");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(String.format("%.2f", temp_map[i][j]));
                System.out.print('\t');
            }
            System.out.println('\n');
        }

        System.out.println(" Daisy map : \n");
        for (int i = 0; i < size; i++) {
            for (int j = 0; j < size; j++) {
                System.out.print(daisy_map[i][j]);
                System.out.print('\t');
            }
            System.out.println('\n');
        }
    }

}
