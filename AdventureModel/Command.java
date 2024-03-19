package AdventureModel;

/**
 * The Command interface represents a command action in the adventure game.
 * Implementing classes are expected to provide a specific implementation of the command.
 */
public interface Command<T>{
    /**
     * Executes the command and returns a result as a String.
     *
     * @return A string representing the outcome of the command execution.
     */
    public T execute();
}
