package br.uniriotec.ppgi.fernandoxavier;


public class OperationInvoker {

    public void executeCommand(OperationCommand command) {

        System.out.println("Invoker says: I will call " + command.getClass().getSimpleName());
        command.execute();

    }

}
