package br.uniriotec.ppgi.fernandoxavier;

import java.util.ArrayList;

public class DataScienceApp
{

    public ArrayList<OperationCommand> commands;

    public DataScienceApp() {
        addCommands();
    }

    public DataScienceApp(OperationCommand command) {
        addSingleCommand(command);
    }

    private void addSingleCommand(OperationCommand command) {
        commands = new ArrayList<OperationCommand>();
        commands.add(command);
    }

    private void addCommands() {
        commands = new ArrayList<OperationCommand>();
        commands.add(new DatasetLoadCommand());
        commands.add(new PreProcessingCommand());
        commands.add(new PreAnalysisCommand());
        commands.add(new ProcessingCommand());
        commands.add(new ReportingCommand());
    }

    private void executeCommands() {
        OperationInvoker invoker = new OperationInvoker();
        for (int i=0; i<commands.size();i++) {
            OperationCommand command = commands.get(i);
            try {
                invoker.executeCommand(command);
            } catch (Exception e) {
                System.out.println("Occurred an error : " + e.getMessage());
                errorProcessing(i);
            }
        }
    }

    private void errorProcessing(int i) {
        System.out.println("The error occurred at " + commands.get(i).getClass().getSimpleName());
        System.out.println("In this moment nothing to do");
    }

    public static void main( String[] args )
    {
        System.out.println( "Client says: Hello World!" );
        DataScienceApp dataScienceApp = new DataScienceApp();
        dataScienceApp.executeCommands();

    }
}
