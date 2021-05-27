package daisyworld;


import java.io.BufferedWriter;
import java.io.FileWriter;
import java.io.IOException;
import java.nio.Buffer;
import java.util.ArrayList;

public class ExportData {
    private World world;

    public ExportData (World world) {
        this.world = world;
    }

    public void writeTickData () {
        try {
            BufferedWriter tickDataWriter = new BufferedWriter(new FileWriter("tickData.csv"));
            tickDataWriter.write("total daisy population every ticks");
            tickDataWriter.newLine();
            for (int i = 1; i <= world.getTickTotalDaisyPopulation().size() ; i++) {
                if (i < world.getTickTotalDaisyPopulation().size()) {
                    tickDataWriter.write(world.getTickTotalDaisyPopulation().get(i - 1).toString() + ',');
                } else {
                    tickDataWriter.write(world.getTickTotalDaisyPopulation().get(i - 1).toString());
                }
            }
            tickDataWriter.newLine();
            tickDataWriter.write("black daisy population every ticks");
            tickDataWriter.newLine();
            for (int i = 1; i <= world.getTickBlackDaisyPopulation().size(); i++) {
                if (i < world.getTickBlackDaisyPopulation().size()) {
                    tickDataWriter.write(world.getTickBlackDaisyPopulation().get(i - 1).toString() + ',');
                } else {
                    tickDataWriter.write(world.getTickBlackDaisyPopulation().get(i - 1).toString());
                }
            }
            tickDataWriter.newLine();
            tickDataWriter.write("white daisy population every ticks");
            tickDataWriter.newLine();
            for (int i = 1; i <= world.getTickWhiteDaisyPopulation().size(); i++) {
                if (i < world.getTickWhiteDaisyPopulation().size()) {
                    tickDataWriter.write(world.getTickWhiteDaisyPopulation().get(i -1).toString() + ',');
                } else {
                    tickDataWriter.write(world.getTickWhiteDaisyPopulation().get(i - 1).toString());
                }
            }
            tickDataWriter.newLine();
            tickDataWriter.write("average temperature every ticks");
            tickDataWriter.newLine();
            for (int i = 1; i <= world.getTickAvgTemperature().size(); i++) {
                if (i < world.getTickAvgTemperature().size()) {
                    tickDataWriter.write(world.getTickAvgTemperature().get(i - 1).toString() + ',');
                } else {
                    tickDataWriter.write(world.getTickAvgTemperature().get(i - 1).toString());
                }
            }
            tickDataWriter.newLine();
            tickDataWriter.write("average soil quality every ticks");
            tickDataWriter.newLine();
            for (int i = 1; i <= world.getTickSoilQuality().size(); i++) {
                if (i < world.getTickSoilQuality().size()) {
                    tickDataWriter.write(world.getTickSoilQuality().get(i - 1).toString() + ',');
                } else {
                    tickDataWriter.write(world.getTickSoilQuality().get(i - 1).toString());
                }
            }
            tickDataWriter.newLine();
            tickDataWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void writeTotalData () {
        try {
            BufferedWriter totalDataWriter = new BufferedWriter(new FileWriter("totalData.csv"));
            totalDataWriter.write("total daisy population at the end of ticks");
            totalDataWriter.newLine();
            totalDataWriter.write(String.valueOf(world.getCountDaisy()));
            totalDataWriter.newLine();
            totalDataWriter.write("total black daisy population at the end of ticks");
            totalDataWriter.newLine();
            totalDataWriter.write(String.valueOf(world.getCountBlackDaisy()));
            totalDataWriter.newLine();
            totalDataWriter.write("total white daisy population at the end of ticks");
            totalDataWriter.newLine();
            totalDataWriter.write(String.valueOf(world.getCountWhiteDaisy()));
            totalDataWriter.newLine();
            totalDataWriter.write("average temperature at the end of ticks");
            totalDataWriter.newLine();
            totalDataWriter.write(String.valueOf(world.getAvgTemps()));
            totalDataWriter.newLine();
            totalDataWriter.write("average soil quality at the end of ticks");
            totalDataWriter.newLine();
            totalDataWriter.write(String.valueOf(world.getAvgSoilQuality()));
            totalDataWriter.newLine();
            totalDataWriter.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
