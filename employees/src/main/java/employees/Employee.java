package employees;

public class Employee {

    private String name;

    private int yearOfBirth;

    public Employee(String name, int yearOfBirth) {
        if (yearOfBirth < 1700) {
            throw new IllegalArgumentException("Can not be lower than 1700");
        }
        this.name = name;
        this.yearOfBirth = yearOfBirth;
    }

    public int getAge(int year) {
        return year - yearOfBirth;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public int getYearOfBirth() {
        return yearOfBirth;
    }

    public void setYearOfBirth(int yearOfBirth) {
        this.yearOfBirth = yearOfBirth;
    }
}
