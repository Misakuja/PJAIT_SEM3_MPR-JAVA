package pl.edu.pjatk.MPR_Project.exception;

public class CapybaraNotFoundException extends RuntimeException {
    public CapybaraNotFoundException() {
        super("Capybara not found");
    }
}
