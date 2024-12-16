package pl.edu.pjatk.MPR_Project.exception;

public class CapybaraAlreadyExists extends RuntimeException {
    public CapybaraAlreadyExists() {
        super("Capybara already exists");
    }
}
