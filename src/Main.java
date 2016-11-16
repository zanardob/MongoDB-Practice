import java.io.*;
import java.util.ArrayList;

public class Main {
    public static void main(String[] argv){
        convertElectionDatabase("script.txt");
    }

    public static void convertElectionDatabase(String fileName){
        MongoConverter mc = new MongoConverter("db");

        try (BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(new FileOutputStream(fileName), "UTF-8"))) {
            ArrayList<ArrayList<String>> buffer = new ArrayList<>();

            System.out.println("Converting database...");

            buffer.add(mc.buildCollection("ESTADO"));
            buffer.add(mc.buildCollection("CIDADE"));
            buffer.add(mc.buildCollection("ZONA"));
            buffer.add(mc.buildCollection("BAIRRO"));
            buffer.add(mc.buildCollection("URNA"));
            buffer.add(mc.buildCollection("SESSAO"));
            buffer.add(mc.buildCollection("CANDIDATO", "PARTIDO"));
            buffer.add(mc.buildCollection("CARGO"));
            buffer.add(mc.buildCollection("CANDIDATURA"));
            buffer.add(mc.buildCollection("PESQUISA"));

            buffer.add(mc.buildManyToManyRelationships("PLEITO"));
            buffer.add(mc.buildManyToManyRelationships("INTENCAODEVOTO"));

            for (ArrayList<String> commandList : buffer) {
                for (String command : commandList) {
                    writer.write(command);
                    writer.newLine();
                }
            }

            System.out.println("Database successfully converted!");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
