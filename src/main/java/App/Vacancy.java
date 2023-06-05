package App;

import java.util.List;

public class Vacancy {
    private String title;
    private String experience;
    private String author;
    private String dateOfCreated;
    private List<String> skills;
    private String salary;

    private String linkOfVacancy;

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public String getExperience() {
        return experience;
    }

    public void setExperience(String experience) {
        this.experience = experience;
    }

    public String getAuthor() {
        return author;
    }

    public void setAuthor(String author) {
        this.author = author;
    }

    public String getDateOfCreated() {
        return dateOfCreated;
    }

    public void setDateOfCreated(String dateOfCreated) {
        this.dateOfCreated = dateOfCreated;
    }


    public String getSalary() {
        return salary;
    }

    public void setSalary(String salary) {
        this.salary = salary;
    }

    public String getLinkOfVacancy() {
        return linkOfVacancy;
    }

    public void setLinkOfVacancy(String linkOfVacancy) {
        this.linkOfVacancy = linkOfVacancy;
    }

    public List<String> getSkills() {
        return skills;
    }

    public void setSkills(List<String> skills) {
        this.skills = skills;
    }

    @Override
    public String toString() {
        String skillsString = skills.isEmpty() ? "Не указаны" : String.join(", ", skills);
        return "Наименование должности: " + title + "\n" +
                experience + "\n" +
                "Компания: " + author + "\n" +
                dateOfCreated + "\n" +
                "Ключевые навыки: " + skillsString + "\n" +
                "Заработная плата: " + salary + "\n";
    }
}
