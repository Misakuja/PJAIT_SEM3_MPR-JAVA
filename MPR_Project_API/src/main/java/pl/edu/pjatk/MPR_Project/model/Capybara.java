package pl.edu.pjatk.MPR_Project.model;

import jakarta.persistence.*;

@Entity
public class Capybara {
    private String name;
    private int age;

    private long identification;

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    Long id;

    public Capybara() {}

    public Capybara(String name, int age) {
        this.name = name;
        this.age = age;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getAge() {
        return age;
    }

    public void setAge(int age) {
        this.age = age;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public long getIdentification() {
        return identification;
    }

    public void setIdentification() {
        String name = this.getName();
        String ageString = Integer.valueOf(this.getAge()).toString();

        String finalString = name + ageString;

        int identification = 0;

        for (int i = 0; i < finalString.length(); i++) {
            identification += Character.getNumericValue(finalString.charAt(i));
        }
        this.identification = identification;
    }

    @Override
    public String toString() {
        return "Capybara{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", age=" + age +
                ", identification=" + identification +
                '}';
    }
}
